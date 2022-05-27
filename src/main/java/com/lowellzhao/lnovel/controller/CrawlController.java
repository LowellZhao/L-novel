package com.lowellzhao.lnovel.controller;

import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.service.CrawlService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lowellzhao
 * @date 2022/5/26
 */
@RestController
@RequestMapping("/crawl")
public class CrawlController {

    @Resource
    private CrawlService crawlService;

    /**
     * 爬取指定爬虫源站的小说
     *
     * @param sourceId 爬虫源id
     */
    @PostMapping("/")
    public Result crawl(Integer sourceId) {
        crawlService.crawl(sourceId);
        return Result.success();
    }

    /**
     * 爬取指定爬虫源站的小说分类
     *
     * @param sourceId 爬虫源id
     */
    @PostMapping("/category")
    public Result crawlCategory(Integer sourceId) {
        crawlService.crawlCategory(sourceId);
        return Result.success();
    }

}
