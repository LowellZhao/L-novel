package com.lowellzhao.lnovel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.entity.CategoryInfo;
import com.lowellzhao.lnovel.mapper.CategoryInfoMapper;
import com.lowellzhao.lnovel.service.CategoryInfoService;
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

    @Override
    public CategoryInfo getBySourceIdAndCid(Integer sourceId, String sourceCid) {
        LambdaQueryWrapper<CategoryInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CategoryInfo::getSourceId, sourceId);
        lambdaQueryWrapper.eq(CategoryInfo::getSourceCid, sourceCid);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public CategoryInfo getBySourceIdAndCName(Integer sourceId, String sourceCName) {
        LambdaQueryWrapper<CategoryInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CategoryInfo::getSourceId, sourceId);
        lambdaQueryWrapper.eq(CategoryInfo::getCname, sourceCName);
        return this.getOne(lambdaQueryWrapper);
    }
}
