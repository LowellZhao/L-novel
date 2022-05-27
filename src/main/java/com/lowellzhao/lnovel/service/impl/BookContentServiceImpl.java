package com.lowellzhao.lnovel.service.impl;

import com.lowellzhao.lnovel.entity.BookContent;
import com.lowellzhao.lnovel.mapper.BookContentMapper;
import com.lowellzhao.lnovel.service.BookContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
