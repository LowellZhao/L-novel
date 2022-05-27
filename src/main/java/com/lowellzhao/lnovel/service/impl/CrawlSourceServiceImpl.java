package com.lowellzhao.lnovel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.entity.CrawlSource;
import com.lowellzhao.lnovel.entity.bo.RuleBo;
import com.lowellzhao.lnovel.mapper.CrawlSourceMapper;
import com.lowellzhao.lnovel.service.CrawlSourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 爬虫源信息 服务实现类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Service
public class CrawlSourceServiceImpl extends ServiceImpl<CrawlSourceMapper, CrawlSource> implements CrawlSourceService {

    @Override
    public RuleBo getRuleBoById(Integer sourceId) {
        CrawlSource crawlSource = this.getById(sourceId);
        if (crawlSource == null) {
            return null;
        }
        String rule = crawlSource.getRule();
        if (StringUtils.isBlank(rule)) {
            return null;
        }
        return JSON.parseObject(rule, RuleBo.class);
    }
}
