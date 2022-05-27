package com.lowellzhao.lnovel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.entity.Book;
import com.lowellzhao.lnovel.mapper.BookMapper;
import com.lowellzhao.lnovel.service.BookService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电子书信息 服务实现类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Override
    public void queryAndSave(Book book) {
        LambdaQueryWrapper<Book> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Book::getSourceId, book.getSourceId());
        lambdaQueryWrapper.eq(Book::getSourceBookId, book.getSourceBookId());
        Book one = this.getOne(lambdaQueryWrapper);
        if (one != null) {
            book.setId(one.getId());
            return;
        }
        this.save(book);
    }
}
