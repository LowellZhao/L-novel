package com.lowellzhao.lnovel.service;

import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.model.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 电子书信息 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public interface BookService extends IService<Book> {

    void queryAndSave(Book book);

    Result readBook(Long bookId, Long indexId);

}
