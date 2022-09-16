package com.lowellzhao.lnovel.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lowellzhao.lnovel.model.entity.OperationLog;
import com.lowellzhao.lnovel.model.param.OperationLogSearchParam;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-09-07
 */
public interface OperationLogService extends IService<OperationLog> {

    void insertLogAsync(HttpServletRequest request, ProceedingJoinPoint point, Object responseData, long totalTime);

    Page<OperationLog> pageSearch(OperationLogSearchParam param);
}
