package com.lowellzhao.lnovel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.common.util.VoiceUtil;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.mapper.BookMapper;
import com.lowellzhao.lnovel.model.entity.Book;
import com.lowellzhao.lnovel.model.entity.BookContent;
import com.lowellzhao.lnovel.model.entity.BookIndex;
import com.lowellzhao.lnovel.service.BookContentService;
import com.lowellzhao.lnovel.service.BookIndexService;
import com.lowellzhao.lnovel.service.BookService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private BookIndexService bookIndexService;
    @Resource
    private BookContentService bookContentService;

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

    @Override
    public Result readBook(Long bookId, Long indexId) {
        Book book = this.getById(bookId);
        if (book == null) {
            return Result.error("书本不存在");
        }
        // 获取所有章节信息
        List<BookIndex> bookIndexList = bookIndexService.listByBookId(book.getId());
        if (CollectionUtils.isEmpty(bookIndexList)) {
            return Result.error("书本内容为空");
        }
        // 获取章节内容
        List<Long> bookIndexIdList = bookIndexList.stream().map(BookIndex::getId).collect(Collectors.toList());
        List<BookContent> bookContentList = bookContentService.listByIndexIdList(bookIndexIdList);
        if (CollectionUtils.isEmpty(bookContentList)) {
            return Result.error("书本内容为空");
        }

        // 章节内容map
        Map<Long, String> bookContentMap = bookContentList.stream()
                .collect(Collectors.toMap(BookContent::getIndexId, BookContent::getContent));

        // 排序，升序
        bookIndexList.sort(Comparator.comparing(BookIndex::getSortId));
        // 阅读开始标识
        boolean readFlag = false;
        if (indexId == null) {
            // 没有开始阅读的章节，全部开始阅读
            readFlag = true;
        }
        for (BookIndex bookIndex : bookIndexList) {
            if (indexId != null && indexId.equals(bookIndex.getId())) {
                // 找到对应的章节，开始阅读
                readFlag = true;
            }
            if (readFlag) {
                String content = bookContentMap.getOrDefault(bookIndex.getId(), "");
                VoiceUtil.speakingText(content);
            }
        }
        return Result.success();
    }
}
