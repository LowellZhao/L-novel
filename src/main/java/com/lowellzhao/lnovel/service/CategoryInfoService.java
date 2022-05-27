package com.lowellzhao.lnovel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lowellzhao.lnovel.entity.CategoryInfo;

import java.util.List;

/**
 * <p>
 * 分类信息 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public interface CategoryInfoService extends IService<CategoryInfo> {

    /**
     * 获取爬虫源分类集合
     *
     * @param sourceId 爬虫源id
     * @return 爬虫源分类集合
     */
    List<CategoryInfo> listBySourceId(Integer sourceId);

    /**
     * 获取爬虫源分类，根据爬虫源的分类id
     *
     * @param sourceId  爬虫源id
     * @param sourceCid 爬虫源分类id
     * @return 爬虫源分类
     */
    CategoryInfo getBySourceIdAndCid(Integer sourceId, String sourceCid);

}
