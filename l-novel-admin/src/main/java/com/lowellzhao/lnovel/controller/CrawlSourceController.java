package com.lowellzhao.lnovel.controller;


import com.alibaba.fastjson.JSON;
import com.lowellzhao.lnovel.common.annotation.OptLog;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.model.bo.RuleBo;
import com.lowellzhao.lnovel.model.entity.CrawlSource;
import com.lowellzhao.lnovel.model.vo.CrawlSourceVo;
import com.lowellzhao.lnovel.service.CrawlSourceService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 爬虫源信息 前端控制器
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@RestController
@RequestMapping("/crawlSource")
public class CrawlSourceController {

    @Resource
    private CrawlSourceService crawlSourceService;

    /**
     * 爬虫源列表查询
     *
     * @return 爬虫源列表
     */
    @GetMapping("/list")
    public Result list() {
        List<CrawlSource> list = crawlSourceService.list();
        if (CollectionUtils.isEmpty(list)) {
            return Result.success(Collections.emptyList());
        }
        List<CrawlSourceVo> voList = list.stream().map(e -> {
            CrawlSourceVo vo = new CrawlSourceVo();
            vo.setId(e.getId());
            vo.setName(e.getName());
            vo.setStatus(e.getStatus());
            String rule = e.getRule();
            RuleBo ruleBo = JSON.parseObject(rule, RuleBo.class);
            vo.setRule(ruleBo);
            return vo;
        }).collect(Collectors.toList());
        return Result.success(voList);
    }

    /**
     * 爬虫源编辑
     *
     * @param sourceVo 爬虫源信息
     * @return 编辑结果
     */
    @PostMapping("/edit")
    @OptLog(optModule = "crawlSource", optType = "edit")
    public Result edit(@RequestBody @Valid CrawlSourceVo sourceVo) {
        return crawlSourceService.edit(sourceVo);
    }

    /**
     * 根据爬虫源id，删除爬虫源
     *
     * @param sourceVo 爬虫源信息
     * @return 删除结果
     */
    @PostMapping("/delete")
    @OptLog(optModule = "crawlSource", optType = "delete")
    public Result delete(@RequestBody CrawlSourceVo sourceVo) {
        Integer id = sourceVo.getId();
        if (id == null) {
            return Result.error("请选择要删除的数据源");
        }
        return crawlSourceService.delete(id);
    }

}

