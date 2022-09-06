package com.lowellzhao.lnovel.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.model.entity.CrawlSource;
import com.lowellzhao.lnovel.model.bo.RuleBo;
import com.lowellzhao.lnovel.mapper.CrawlSourceMapper;
import com.lowellzhao.lnovel.service.CrawlSourceService;
import com.lowellzhao.lnovel.model.vo.CrawlSourceVo;
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

    @Override
    public Result edit(CrawlSourceVo sourceVo) {
        CrawlSource crawlSource = new CrawlSource();
        crawlSource.setId(sourceVo.getId());
        crawlSource.setName(sourceVo.getName());
        crawlSource.setStatus(sourceVo.getStatus());
        RuleBo rule = sourceVo.getRule();
        crawlSource.setRule(JSON.toJSONString(rule));
        boolean saveOrUpdate = this.saveOrUpdate(crawlSource);
        if (saveOrUpdate) {
            return Result.success();
        }
        return Result.error();
    }

    @Override
    public Result delete(Integer id) {
        boolean removeById = this.removeById(id);
        if (removeById) {
            return Result.success();
        }
        return Result.error();
    }
}
