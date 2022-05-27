package com.lowellzhao.lnovel.service;

import com.lowellzhao.lnovel.entity.BookIndex;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 章节信息 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public interface BookIndexService extends IService<BookIndex> {

    BookIndex getIndexInfo(Long bookId, String sourceIndexId);

}
