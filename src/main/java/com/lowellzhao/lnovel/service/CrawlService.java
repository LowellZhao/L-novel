package com.lowellzhao.lnovel.service;

import com.lowellzhao.lnovel.common.vo.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * 爬虫服务
 *
 * @author lowellzhao
 * @date 2022/5/26
 */
public interface CrawlService {

    /**
     * 爬取指定爬虫源小说
     *
     * @param sourceId 爬虫源id
     */
    void crawl(Integer sourceId);

    /**
     * 爬取分类
     *
     * @param sourceId 爬虫源id
     */
    void crawlCategory(Integer sourceId);

    /**
     * 获取爬虫源，对应bookId的书信息
     *
     * @param sourceId 爬虫源
     * @param bookId   bookId
     */
    void crawlByBookId(Integer sourceId, String bookId);

    /**
     * 小说下载
     *
     * @param bookId   bookId
     * @param response response
     * @return 小说文件流
     */
    Result download(Long bookId, HttpServletResponse response);
}
