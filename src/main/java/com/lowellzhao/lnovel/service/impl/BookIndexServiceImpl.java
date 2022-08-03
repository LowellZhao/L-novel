package com.lowellzhao.lnovel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.model.entity.BookIndex;
import com.lowellzhao.lnovel.mapper.BookIndexMapper;
import com.lowellzhao.lnovel.service.BookIndexService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 章节信息 服务实现类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Service
public class BookIndexServiceImpl extends ServiceImpl<BookIndexMapper, BookIndex> implements BookIndexService {

    @Override
    public BookIndex getIndexInfo(Long bookId, String sourceIndexId) {
        LambdaQueryWrapper<BookIndex> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BookIndex::getBookId, bookId);
        lambdaQueryWrapper.eq(BookIndex::getSourceIndexId, sourceIndexId);
        return this.getOne(lambdaQueryWrapper);
    }

    @Override
    public List<BookIndex> listByBookId(Long bookId) {
        LambdaQueryWrapper<BookIndex> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BookIndex::getBookId, bookId);
        return this.list(lambdaQueryWrapper);
    }
}
