package com.lowellzhao.lnovel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.common.annotation.OptLog;
import com.lowellzhao.lnovel.common.component.CurrentRequestComponent;
import com.lowellzhao.lnovel.common.util.IpUtils;
import com.lowellzhao.lnovel.common.vo.TokenUserInfo;
import com.lowellzhao.lnovel.mapper.OperationLogMapper;
import com.lowellzhao.lnovel.model.entity.OperationLog;
import com.lowellzhao.lnovel.model.param.OperationLogSearchParam;
import com.lowellzhao.lnovel.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-09-07
 */
@Slf4j
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Resource
    private CurrentRequestComponent currentRequestComponent;

    @Override
    @Async(value = "logExecutor")
    public void insertLogAsync(HttpServletRequest request, ProceedingJoinPoint point, Object responseData, long totalTime) {
        OperationLog operationLog = new OperationLog();
        try {
            // 获取访问的类
            Class<?> clazz = point.getTarget().getClass();
            // 获得访问的方法名
            String methodName = point.getSignature().getName();
            // 得到方法的参数的类型
            Class[] argClass = ((MethodSignature) point.getSignature()).getParameterTypes();
            // 得到访问的方法对象
            Method method = clazz.getMethod(methodName, argClass);

            OptLog annotation = method.getAnnotation(OptLog.class);
            if (annotation == null) {
                return;
            }
            // 请求URL
            operationLog.setOptUrl(request.getRequestURI());
            // 请求方式
            String requestMethod = request.getMethod();
            operationLog.setRequestMethod(requestMethod);
            // 参数
            String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
            Object[] args = point.getArgs();
            StringBuilder params = new StringBuilder();
            if (parameterNames.length != 0) {
                for (int i = 0; i < parameterNames.length; i++) {
                    if (args[i] instanceof MultipartFile) {
                        continue;
                    }
                    params.append(parameterNames[i]).append(": ").append(JSON.toJSONString(args[i])).append("; ");
                }
            }
            operationLog.setRequestParam(params.toString());
            // 返回结果
            if (annotation.needReturn()) {
                operationLog.setResponseData(JSON.toJSONString(responseData));
            }
            // 模块
            String optModule = annotation.optModule();
            if (StringUtils.isNotBlank(optModule)) {
                operationLog.setOptModule(optModule);
            }
            // 操作方法
            operationLog.setOptMethod(point.getSignature().getDeclaringTypeName() + "." + methodName);
            // 操作类型
            String optType = annotation.optType();
            if (StringUtils.isNotBlank(optType)) {
                operationLog.setOptType(optType);
            }
            // ip
            String ipAddress = IpUtils.getIpAddress(request);
            operationLog.setIpAddress(ipAddress);
            operationLog.setIpSource(IpUtils.getIpSource(ipAddress));
            // 执行时间
            operationLog.setTotalTime(totalTime);
            // 请求用户
            String headerToken = currentRequestComponent.getHeaderToken(request);
            TokenUserInfo tokenUserInfo = currentRequestComponent.getCurrentTokenUserInfo(headerToken);
            if (tokenUserInfo != null) {
                operationLog.setUserId(tokenUserInfo.getUserId());
                operationLog.setNickname(tokenUserInfo.getNickname());
            }
            baseMapper.insert(operationLog);
        } catch (Exception e) {
            log.error("save operation log error, operationLog:{}", JSON.toJSONString(operationLog), e);
        }
    }

    @Override
    public Page<OperationLog> pageSearch(OperationLogSearchParam param) {
        Page<OperationLog> page = new Page<>(param.getCurrent(), param.getSize());
        LambdaQueryWrapper<OperationLog> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(OperationLog::getCreateTime);
        baseMapper.selectPage(page, lambdaQueryWrapper);
        return page;
    }
}
