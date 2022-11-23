package com.lowellzhao.lnovel.controller;

import com.lowellzhao.lnovel.common.annotation.OptLog;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.service.CrawlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "爬虫管理")
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
    @ApiOperation(value = "爬取指定数据源的小说")
    @PostMapping("")
    @OptLog(optModule = "crawl", optType = "crawl")
    public Result crawl(@ApiParam(value = "数据源id", required = true) Integer sourceId) {
        crawlService.crawl(sourceId);
        return Result.success();
    }

    /**
     * 爬取指定爬虫源站的小说分类
     *
     * @param sourceId 爬虫源id
     */
    @ApiOperation(value = "爬取指定爬虫源站的小说分类")
    @PostMapping("/category")
    @OptLog(optModule = "crawl", optType = "category")
    public Result crawlCategory(@ApiParam(value = "数据源id", required = true) Integer sourceId) {
        crawlService.crawlCategory(sourceId);
        return Result.success();
    }

    /**
     * 获取爬虫源，对应bookId的书信息
     *
     * @param sourceId 爬虫源
     * @param bookId   bookId
     */
    @ApiOperation(value = "获取爬虫源，对应bookId的书信息")
    @PostMapping("/byBookId")
    @OptLog(optModule = "crawl", optType = "byBookId")
    public Result crawlByBookId(@ApiParam(value = "数据源id", required = true) Integer sourceId,
                                @ApiParam(value = "数据源的bookId", required = true) String bookId) {
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
    @ApiOperation(value = "小说下载")
    @GetMapping("/download")
    @OptLog(optModule = "crawl", optType = "download")
    public Result download(@ApiParam(value = "书本id", required = true) Long bookId, HttpServletResponse response) {
        return crawlService.download(bookId, response);
    }

}
