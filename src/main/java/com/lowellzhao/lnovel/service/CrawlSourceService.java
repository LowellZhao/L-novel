package com.lowellzhao.lnovel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lowellzhao.lnovel.entity.CrawlSource;
import com.lowellzhao.lnovel.entity.bo.RuleBo;

/**
 * <p>
 * 爬虫源信息 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public interface CrawlSourceService extends IService<CrawlSource> {

    /**
     * 获取爬虫源规则
     *
     * @param sourceId 爬虫源id
     * @return 爬虫源规则
     */
    RuleBo getRuleBoById(Integer sourceId);

}
