package com.lowellzhao.lnovel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lowellzhao.lnovel.entity.BookIndex;

import java.util.List;

/**
 * <p>
 * 章节信息 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public interface BookIndexService extends IService<BookIndex> {

    /**
     * 获取书具体章节信息
     *
     * @param bookId        bookId
     * @param sourceIndexId 爬虫源章节id
     * @return 书具体章节信息
     */
    BookIndex getIndexInfo(Long bookId, String sourceIndexId);

    /**
     * 小说章节集合
     *
     * @param bookId 小说id
     * @return 小说章节集合
     */
    List<BookIndex> listByBookId(Long bookId);
}
