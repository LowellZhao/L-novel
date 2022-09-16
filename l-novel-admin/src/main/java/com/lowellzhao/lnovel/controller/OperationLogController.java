package com.lowellzhao.lnovel.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.model.entity.OperationLog;
import com.lowellzhao.lnovel.model.param.OperationLogSearchParam;
import com.lowellzhao.lnovel.service.OperationLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author lowellzhao
 * @since 2022-09-07
 */
@RestController
@RequestMapping("/operationLog")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @GetMapping("/pageSearch")
    public Result pageSearch(OperationLogSearchParam param) {
        Page<OperationLog> page = operationLogService.pageSearch(param);
        return Result.success(page);
    }

}

