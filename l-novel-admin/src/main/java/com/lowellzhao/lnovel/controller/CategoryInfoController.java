package com.lowellzhao.lnovel.controller;


import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.convert.CategoryInfoConvert;
import com.lowellzhao.lnovel.model.entity.CategoryInfo;
import com.lowellzhao.lnovel.model.vo.CategoryInfoVo;
import com.lowellzhao.lnovel.service.CategoryInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 分类信息 前端控制器
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@RestController
@RequestMapping("/categoryInfo")
public class CategoryInfoController {

    @Resource
    private CategoryInfoService categoryInfoService;
    @Resource
    private CategoryInfoConvert categoryInfoConvert;

    @GetMapping("/listBySourceId")
    public Result<List<CategoryInfoVo>> listBySourceId(Integer sourceId) {
        List<CategoryInfo> categoryInfoList = categoryInfoService.listBySourceId(sourceId);
        List<CategoryInfoVo> voList = categoryInfoConvert.toCategoryInfoVoList(categoryInfoList);
        return Result.success(voList);
    }

}

