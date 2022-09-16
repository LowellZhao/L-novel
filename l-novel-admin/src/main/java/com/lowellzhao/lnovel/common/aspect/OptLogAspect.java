package com.lowellzhao.lnovel.common.aspect;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.lowellzhao.lnovel.common.component.CurrentRequestComponent;
import com.lowellzhao.lnovel.service.OperationLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 操作日志切面处理
 *
 * @author lowellzhao
 * @since 2022/9/7
 */
@Aspect
@Component
public class OptLogAspect {

    @Resource
    private OperationLogService operationLogService;
    @Resource
    private CurrentRequestComponent currentRequestComponent;

    /**
     * 切点
     */
    @Pointcut("@annotation(com.lowellzhao.lnovel.common.annotation.OptLog)")
    public void pointCut() {
        // 定义切点
    }

    /**
     * 环绕
     *
     * @param point 切点
     * @return 结果
     * @throws Throwable Throwable
     */
    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        HttpServletRequest request = currentRequestComponent.getCurrentRequest();
        long start = System.currentTimeMillis();
        // 执行，得到返回结果
        Object proceed;
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            operationLogService.insertLogAsync(request, point, ExceptionUtil.stacktraceToString(e), -1L);
            throw e;
        }
        long end = System.currentTimeMillis();
        // 记录日志
        operationLogService.insertLogAsync(request, point, proceed, end - start);
        return proceed;
    }

}
