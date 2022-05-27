package com.lowellzhao.lnovel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lowellzhao.lnovel.entity.CategoryInfo;
import com.lowellzhao.lnovel.mapper.CategoryInfoMapper;
import com.lowellzhao.lnovel.service.CategoryInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 分类信息 服务实现类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Service
public class CategoryInfoServiceImpl extends ServiceImpl<CategoryInfoMapper, CategoryInfo> implements CategoryInfoService {

    @Override
    public List<CategoryInfo> listBySourceId(Integer sourceId) {
        LambdaQueryWrapper<CategoryInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CategoryInfo::getSourceId, sourceId);
        return this.list(lambdaQueryWrapper);
    }
}
