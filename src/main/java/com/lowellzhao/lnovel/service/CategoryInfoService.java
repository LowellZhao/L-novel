package com.lowellzhao.lnovel.service;

import com.lowellzhao.lnovel.entity.CategoryInfo;
import com.baomidou.mybatisplus.extension.service.IService;

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

    List<CategoryInfo> listBySourceId(Integer sourceId);

}
