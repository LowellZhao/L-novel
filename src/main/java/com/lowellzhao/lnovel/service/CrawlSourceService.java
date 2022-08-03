package com.lowellzhao.lnovel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.model.entity.CrawlSource;
import com.lowellzhao.lnovel.model.bo.RuleBo;
import com.lowellzhao.lnovel.model.vo.CrawlSourceVo;

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

    Result edit(CrawlSourceVo sourceVo);

    Result delete(Integer id);
}
