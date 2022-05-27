package com.lowellzhao.lnovel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.entity.BookContent;
import com.lowellzhao.lnovel.mapper.BookContentMapper;
import com.lowellzhao.lnovel.service.BookContentService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 章节内容信息 服务实现类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Service
public class BookContentServiceImpl extends ServiceImpl<BookContentMapper, BookContent> implements BookContentService {

    @Override
    public List<BookContent> listByIndexIdList(List<Long> bookIndexIdList) {
        if (CollectionUtils.isEmpty(bookIndexIdList)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<BookContent> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(BookContent::getIndexId, bookIndexIdList);
        return this.list(lambdaQueryWrapper);
    }
}
