package com.lowellzhao.lnovel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lowellzhao.lnovel.model.entity.BookContent;

import java.util.List;

/**
 * <p>
 * 章节内容信息 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public interface BookContentService extends IService<BookContent> {

    /**
     * 获取章节内容集合
     *
     * @param bookIndexIdList 章节id集合
     * @return 章节内容集合
     */
    List<BookContent> listByIndexIdList(List<Long> bookIndexIdList);
}
