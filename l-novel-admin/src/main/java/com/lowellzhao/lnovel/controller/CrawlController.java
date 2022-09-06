package com.lowellzhao.lnovel.controller;

import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.service.CrawlService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lowellzhao
 * @since 2022/5/26
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
    @PostMapping("")
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

    /**
     * 获取爬虫源，对应bookId的书信息
     *
     * @param sourceId 爬虫源
     * @param bookId   bookId
     */
    @PostMapping("/byBookId")
    public Result crawlByBookId(Integer sourceId, String bookId) {
        crawlService.crawlByBookId(sourceId, bookId);
        return Result.success();
    }

    /**
     * 小说下载
     *
     * @param bookId   bookId
     * @param response response
     * @return 小说文件流
     */
    @GetMapping("/download")
    public Result download(Long bookId, HttpServletResponse response) {
        return crawlService.download(bookId, response);
    }

}
