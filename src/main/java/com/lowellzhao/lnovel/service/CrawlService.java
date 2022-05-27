package com.lowellzhao.lnovel.service;

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
}
