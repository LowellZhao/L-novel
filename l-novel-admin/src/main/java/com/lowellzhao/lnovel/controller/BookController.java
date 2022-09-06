package com.lowellzhao.lnovel.controller;


import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.service.BookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 电子书信息 前端控制器
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    @PostMapping("/readBook")
    public Result readBook(Long bookId, Long indexId) {
        if (bookId == null) {
            return Result.error("请选择要阅读的书");
        }
        return bookService.readBook(bookId, indexId);
    }

}

