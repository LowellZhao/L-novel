package com.lowellzhao.lnovel.service.impl;

import com.alibaba.fastjson.JSON;
import com.lowellzhao.lnovel.common.util.HttpUtil;
import com.lowellzhao.lnovel.entity.Book;
import com.lowellzhao.lnovel.entity.BookContent;
import com.lowellzhao.lnovel.entity.BookIndex;
import com.lowellzhao.lnovel.entity.CategoryInfo;
import com.lowellzhao.lnovel.entity.CrawlSource;
import com.lowellzhao.lnovel.entity.bo.RuleBo;
import com.lowellzhao.lnovel.service.BookContentService;
import com.lowellzhao.lnovel.service.BookIndexService;
import com.lowellzhao.lnovel.service.BookService;
import com.lowellzhao.lnovel.service.CategoryInfoService;
import com.lowellzhao.lnovel.service.CrawlService;
import com.lowellzhao.lnovel.service.CrawlSourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lowellzhao
 * @date 2022/5/26
 */
@Slf4j
@Service
public class CrawlServiceImpl implements CrawlService {

    @Resource
    private CrawlSourceService crawlSourceService;
    @Resource
    private BookService bookService;
    @Resource
    private BookIndexService bookIndexService;
    @Resource
    private BookContentService bookContentService;
    @Resource
    private CategoryInfoService categoryInfoService;

    @Override
    public void crawl(Integer sourceId) {
        CrawlSource crawlSource = crawlSourceService.getById(sourceId);
        if (crawlSource == null) {
            return;
        }
        String rule = crawlSource.getRule();
        if (StringUtils.isBlank(rule)) {
            return;
        }
        RuleBo ruleBo = JSON.parseObject(rule, RuleBo.class);

        // 分类集合
        List<CategoryInfo> categoryInfoList = categoryInfoService.listBySourceId(sourceId);
        if (CollectionUtils.isEmpty(categoryInfoList)) {
            return;
        }
        // 遍历分类
        for (CategoryInfo categoryInfo : categoryInfoList) {
            int page = 1;
            // https://wap.shuquge.com/sort/{cid}/0_{page}.html
            String bookListUrl = ruleBo.getBookListUrl()
                    .replace("{cid}", categoryInfo.getSourceCid())
                    .replace("{page}", page + "");
            String bookListHtml = HttpUtil.get(bookListUrl);
            log.info(bookListHtml);
            if (StringUtils.isBlank(bookListHtml)) {
                continue;
            }
            // 保存小说信息

            // href="/s/(\d+)\.html"
            Pattern bookIdPatten = Pattern.compile(ruleBo.getBookIdPatten());
            Matcher bookIdMatcher = bookIdPatten.matcher(bookListHtml);
            boolean isFindBookId = bookIdMatcher.find();
            while (isFindBookId) {
                // 爬虫源bookId
                String bookId = bookIdMatcher.group(1);
                log.warn(bookId);
                this.saveBookInfo(bookId, ruleBo, categoryInfo);

                // 下一本小说
                isFindBookId = bookIdMatcher.find();
            }

            // todo 下一页小说
        }
    }

    /**
     * 保存小说信息
     *
     * @param bookId       bookId
     * @param ruleBo       规则
     * @param categoryInfo 分类信息
     */
    private void saveBookInfo(String bookId, RuleBo ruleBo, CategoryInfo categoryInfo) {
        Book nowBook = new Book();
        nowBook.setSourceId(categoryInfo.getSourceId());
        nowBook.setCid(categoryInfo.getId());
        nowBook.setSourceBookId(bookId);
        // https://wap.shuquge.com/s/{bookId}.html
        String bookDetailUrl = ruleBo.getBookDetailUrl().replace("{bookId}", bookId);
        // 小说详情页
        String bookDetailHtml = HttpUtil.get(bookDetailUrl);
        log.info(bookDetailHtml);
        if (StringUtils.isBlank(bookDetailHtml)) {
            return;
        }
        // 书名
        // <a\s+href="/s/\d+\.html"><h2>([^/]+)</h2></a>
        Pattern bookNamePatten = Pattern.compile(ruleBo.getBookNamePatten());
        Matcher bookNameMatch = bookNamePatten.matcher(bookDetailHtml);
        boolean isFindBookName = bookNameMatch.find();
        // 没有书名，直接返回
        if (!isFindBookName) {
            return;
        }
        String bookName = bookNameMatch.group(1);
        log.info(bookName);
        nowBook.setBookName(bookName);

        // 作者名称
        // <p>作者：([^/]+)</p>
        Pattern authorNamePatten = Pattern.compile(ruleBo.getAuthorNamePatten());
        Matcher authorNameMatch = authorNamePatten.matcher(bookDetailHtml);
        boolean isFindAuthorName = authorNameMatch.find();
        if (!isFindAuthorName) {
            return;
        }

        String authorName = authorNameMatch.group(1);
        log.info(authorName);
        nowBook.setAuthorName(authorName);

        // 封面
        if (StringUtils.isNotBlank(ruleBo.getPicUrlPatten())) {
            Pattern picUrlPatten = Pattern.compile(ruleBo.getPicUrlPatten());
            Matcher picUrlMatch = picUrlPatten.matcher(bookDetailHtml);
            boolean isFindPicUrl = picUrlMatch.find();
            if (isFindPicUrl) {
                String picUrl = picUrlMatch.group(1);
                //设置封面图片路径
                nowBook.setBookPic(picUrl);
            }
        }

        // 描述
        // <div class="intro_info">
        int descStartIndex = bookDetailHtml.indexOf(ruleBo.getDescStart());
        String desc = bookDetailHtml.substring(descStartIndex + ruleBo.getDescStart().length());
        // 最新章节推荐地址
        desc = desc.substring(0, desc.indexOf(ruleBo.getDescEnd()));
        //过滤掉简介中的特殊标签
        desc = desc.replaceAll("<a[^<]+</a>", "")
                .replaceAll("<font[^<]+</font>", "")
                .replaceAll("<p>\\s*</p>", "")
                .replaceAll("&nbsp;", "")
                .replaceAll("<p>", "")
                .replaceAll("</p>", "<br/>");
        log.info(desc);
        nowBook.setBookDesc(desc);

        // 查询书的信息或者保存
        bookService.queryAndSave(nowBook);

        // 正文
        String indexListHtml;
        if (StringUtils.isNotBlank(ruleBo.getBookIndexStart())) {
            indexListHtml = bookDetailHtml.substring(bookDetailHtml.indexOf(ruleBo.getBookIndexStart())
                    + ruleBo.getBookIndexStart().length());
        } else {
            indexListHtml = bookDetailHtml;
        }
        // <li><a\s+href="/chapter/\d+_\d+.html">[^/]+</a></li>
        Pattern indexIdPatten = Pattern.compile(ruleBo.getIndexIdPatten());
        Matcher indexIdMatch = indexIdPatten.matcher(indexListHtml);
        boolean indexIdFind = indexIdMatch.find();

        // 章节名称
        // <li><a\s+href="/chapter/\d+_\d+.html">([^/]+)</a></li>
        Pattern indexNamePatten = Pattern.compile(ruleBo.getIndexNamePatten());
        Matcher indexNameMatch = indexNamePatten.matcher(indexListHtml);
        boolean indexNameFind = indexNameMatch.find();
        // 遍历章节
        while (indexIdFind && indexNameFind) {
            String indexName = indexNameMatch.group(1);
            log.info(indexName);

            String indexId = indexIdMatch.group(1);
            log.info(indexId);
            // 保存章节信息
            this.saveIndexInfo(indexName, indexId, nowBook, ruleBo);

            indexIdFind = indexIdMatch.find();
            indexNameFind = indexNameMatch.find();
        }

        // todo 章节分页
    }

    /**
     * 保存章节信息
     *
     * @param indexName 章节名称
     * @param indexId   章节id
     * @param nowBook   book
     * @param ruleBo    规则
     */
    private void saveIndexInfo(String indexName, String indexId, Book nowBook, RuleBo ruleBo) {
        BookIndex indexInfo = bookIndexService.getIndexInfo(nowBook.getId(), indexId);
        // 成功的就不保存了
        if (indexInfo != null && Boolean.TRUE.equals(indexInfo.getSuccess())) {
            return;
        }
        if (indexInfo == null) {
            indexInfo = new BookIndex();
            indexInfo.setBookId(nowBook.getId());
            indexInfo.setSourceIndexId(indexId);
            if (StringUtils.isNumeric(indexId)) {
                indexInfo.setSortId(Integer.parseInt(indexId));
            } else {
                indexInfo.setSortId(0);
            }
            indexInfo.setTitle(indexName);
            bookIndexService.save(indexInfo);
        }

        // https://wap.shuquge.com/chapter/{bookId}_{indexId}.html
        String contentUrl = ruleBo.getBookContentUrl().replace("{bookId}", nowBook.getSourceBookId()).replace("{indexId}", indexId);
        String contentHtml = HttpUtil.get(contentUrl);
        log.info(contentHtml);
        StringBuilder allContent = new StringBuilder();
        while (StringUtils.isNotBlank(contentHtml)) {
            // <div id="nr1">
            String content = contentHtml.substring(contentHtml.indexOf(ruleBo.getContentStart())
                    + ruleBo.getContentStart().length());
            // </div>
            content = content.substring(0, content.indexOf(ruleBo.getContentEnd()));
            log.info(content);
            allContent.append(content).append("\n");

            // <a\s+id="pb_next"\s+href="/chapter/\d+_(\d+_\d+).html">下一页</a>
            Pattern nextContentPatten = Pattern.compile(ruleBo.getNextPagePatten());
            Matcher nextContentMatch = nextContentPatten.matcher(contentHtml);
            boolean nextContentFind = nextContentMatch.find();
            if (!nextContentFind) {
                break;
            }
            // 下一页
            String nextIndexId = nextContentMatch.group(1);
            contentUrl = ruleBo.getBookContentUrl().replace("{bookId}", nowBook.getSourceBookId()).replace("{indexId}", nextIndexId);
            contentHtml = HttpUtil.get(contentUrl);
        }
        log.info(allContent.toString());

        String realContent = allContent.toString().replaceAll("&nbsp;", " ")
                .replaceAll("<br />", "")
                .replaceAll("<br/>", "");
        log.info(realContent);

        if (StringUtils.isBlank(realContent)) {
            return;
        }
        BookContent bookContent = new BookContent();
        bookContent.setContent(realContent);
        bookContent.setIndexId(indexInfo.getId());
        bookContentService.save(bookContent);

        // 更新成功
        indexInfo.setSuccess(true);
        bookIndexService.updateById(indexInfo);
    }

    @Override
    public void crawlCategory(Integer sourceId) {
        CrawlSource crawlSource = crawlSourceService.getById(sourceId);
        if (crawlSource == null) {
            return;
        }
        String rule = crawlSource.getRule();
        if (StringUtils.isBlank(rule)) {
            return;
        }
        RuleBo ruleBo = JSON.parseObject(rule, RuleBo.class);

        String categoryUrl = ruleBo.getCategoryUrl();
        String categoryListHtml = HttpUtil.get(categoryUrl);
        log.info(categoryListHtml);

        if (StringUtils.isBlank(categoryListHtml)) {
            return;
        }

        List<CategoryInfo> categoryInfoList = categoryInfoService.listBySourceId(sourceId);
        Map<String, CategoryInfo> categoryInfoMap = categoryInfoList.stream()
                .collect(Collectors.toMap(CategoryInfo::getSourceCid, Function.identity()));

        // <li\s+class="prev"><a[^/]+href="/sort/(\d+)/0_1.html">[^/]+</a></li>
        Pattern categoryIdPatten = Pattern.compile(ruleBo.getCategoryIdPatten());
        Matcher categoryIdMatcher = categoryIdPatten.matcher(categoryListHtml);
        boolean isFindCategoryId = categoryIdMatcher.find();

        // <li\s+class="prev"><a[^/]+href="/sort/\d+/0_1.html">([^/]+)</a></li>
        Pattern categoryNamePatten = Pattern.compile(ruleBo.getCategoryNamePatten());
        Matcher categoryNameMatcher = categoryNamePatten.matcher(categoryListHtml);
        boolean isFindCategoryName = categoryNameMatcher.find();

        while (isFindCategoryId && isFindCategoryName) {
            String categoryId = categoryIdMatcher.group(1);
            String categoryName = categoryNameMatcher.group(1);
            log.info("categoryId:{}, categoryName:{}", categoryId, categoryName);
            CategoryInfo categoryInfo = categoryInfoMap.get(categoryId);
            if (categoryInfo == null) {
                categoryInfo = new CategoryInfo();
                categoryInfo.setSourceId(sourceId);
                categoryInfo.setSourceCid(categoryId);
                categoryInfo.setCname(categoryName);
                categoryInfoService.save(categoryInfo);
                categoryInfoMap.put(categoryId, categoryInfo);
            }

            isFindCategoryId = categoryIdMatcher.find();
            isFindCategoryName = categoryNameMatcher.find();
        }

    }
}
