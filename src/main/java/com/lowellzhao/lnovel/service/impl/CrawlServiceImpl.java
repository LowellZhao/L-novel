package com.lowellzhao.lnovel.service.impl;

import com.alibaba.fastjson.JSON;
import com.lowellzhao.lnovel.common.util.HttpUtil;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.entity.Book;
import com.lowellzhao.lnovel.entity.BookContent;
import com.lowellzhao.lnovel.entity.BookIndex;
import com.lowellzhao.lnovel.entity.CategoryInfo;
import com.lowellzhao.lnovel.entity.bo.RuleBo;
import com.lowellzhao.lnovel.service.BookContentService;
import com.lowellzhao.lnovel.service.BookIndexService;
import com.lowellzhao.lnovel.service.BookService;
import com.lowellzhao.lnovel.service.CategoryInfoService;
import com.lowellzhao.lnovel.service.CrawlService;
import com.lowellzhao.lnovel.service.CrawlSourceService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.net.URLEncoder;
import java.util.Comparator;
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
        RuleBo ruleBo = crawlSourceService.getRuleBoById(sourceId);
        if (ruleBo == null) {
            return;
        }

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
            // 循环分类页
            while (StringUtils.isNotBlank(bookListHtml)) {
                // 保存小说信息
                // href="/s/(\d+)\.html"
                Pattern bookIdPatten = Pattern.compile(ruleBo.getBookIdPatten());
                Matcher bookIdMatcher = bookIdPatten.matcher(bookListHtml);
                boolean isFindBookId = bookIdMatcher.find();
                // 没有找到，可能是页面错误，跳出循环
                if (!isFindBookId) {
                    break;
                }
                while (isFindBookId) {
                    // 爬虫源bookId
                    String bookId = bookIdMatcher.group(1);
                    log.warn("start bookId " + bookId);
                    this.saveBookInfo(bookId, ruleBo, sourceId, categoryInfo.getId());
                    log.warn("end bookId " + bookId);

                    // 下一本小说
                    isFindBookId = bookIdMatcher.find();
                }

                // 下一页小说
                bookListUrl = ruleBo.getBookListUrl()
                        .replace("{cid}", categoryInfo.getSourceCid())
                        .replace("{page}", page++ + "");
                bookListHtml = HttpUtil.get(bookListUrl);
            }
        }
    }

    /**
     * 保存小说信息
     *
     * @param bookId   bookId
     * @param ruleBo   规则
     * @param sourceId 爬虫源id
     * @param cid      分类id
     */
    private void saveBookInfo(String bookId, RuleBo ruleBo, Integer sourceId, Integer cid) {
        Book nowBook = new Book();
        nowBook.setSourceId(sourceId);
        nowBook.setCid(cid);
        nowBook.setSourceBookId(bookId);
        // https://wap.shuquge.com/s/{bookId}.html
        String bookDetailUrl = ruleBo.getBookDetailUrl().replace("{bookId}", bookId);
        // 小说详情页
        String bookDetailHtml = HttpUtil.get(bookDetailUrl);
        // 没有内容，直接返回
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
        nowBook.setBookName(bookName);

        // 作者名称
        // <p>作者：([^/]+)</p>
        Pattern authorNamePatten = Pattern.compile(ruleBo.getAuthorNamePatten());
        Matcher authorNameMatch = authorNamePatten.matcher(bookDetailHtml);
        boolean isFindAuthorName = authorNameMatch.find();
        // 没有作者，直接返回
        if (!isFindAuthorName) {
            return;
        }
        String authorName = authorNameMatch.group(1);
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
        nowBook.setBookDesc(desc);

        // 查询书的信息或者保存
        bookService.queryAndSave(nowBook);

        // 遍历保存
        int indexCount = 0;
        log.info("save book index start, bookName:{}, authorName:{}, book:{}", bookName, authorName, JSON.toJSONString(nowBook));
        while (StringUtils.isNotBlank(bookDetailHtml)) {
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
                // 章节名称
                String indexName = indexNameMatch.group(1);
                // 爬虫源章节id
                String indexId = indexIdMatch.group(1);
                // 保存章节信息
                this.saveIndexInfo(indexName, indexId, nowBook, ruleBo);
                log.info("save book index end, bookName:{}, indexName:{}, indexId:{}", bookName, indexName, indexId);
                // 章节加一
                indexCount++;

                indexIdFind = indexIdMatch.find();
                indexNameFind = indexNameMatch.find();
            }

            // 没有章节下一页匹配，直接返回
            if (StringUtils.isBlank(ruleBo.getNextIndexUrlPatten())) {
                break;
            }
            // 章节下一页
            // <a\s+href="(/d/\d+_\d+.html)"\s+class="onclick">下一页</a>
            Pattern nextIndexPatten = Pattern.compile(ruleBo.getNextIndexUrlPatten());
            Matcher nextIndexMatch = nextIndexPatten.matcher(bookDetailHtml);
            boolean nextIndexMatchFind = nextIndexMatch.find();
            // 没有下一页了，直接返回
            if (!nextIndexMatchFind) {
                break;
            }
            String nextIndexUrl = nextIndexMatch.group(1);
            if (StringUtils.isNotBlank(ruleBo.getWebPreUrl())) {
                bookDetailUrl = ruleBo.getWebPreUrl() + nextIndexUrl;
            } else {
                bookDetailUrl = nextIndexUrl;
            }
            bookDetailHtml = HttpUtil.get(bookDetailUrl);
        }
        log.info("save book index end, bookName:{}, authorName:{}, success:{}, indexCount:{}",
                bookName, authorName, indexCount, JSON.toJSONString(nowBook));
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
        StringBuilder allContent = new StringBuilder();
        while (StringUtils.isNotBlank(contentHtml)) {
            // <div id="nr1">
            String content = contentHtml.substring(contentHtml.indexOf(ruleBo.getContentStart())
                    + ruleBo.getContentStart().length());
            // </div>
            content = content.substring(0, content.indexOf(ruleBo.getContentEnd()));
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

        String realContent = allContent.toString()
                .replaceAll("&nbsp;", " ")
                .replaceAll("<br />", "")
                .replaceAll("<br/>", "")
                .replaceAll("\n\n", "\n")
                .replaceAll(indexName, "");
        // 内容匹配替换
        Map<String, String> contentPattenMap = ruleBo.getContentPattenMap();
        if (MapUtils.isNotEmpty(contentPattenMap)) {
            for (Map.Entry<String, String> entry : contentPattenMap.entrySet()) {
                realContent = realContent.replaceAll(entry.getKey(), entry.getValue());
            }
        }
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
        // 获取爬虫源规则
        RuleBo ruleBo = crawlSourceService.getRuleBoById(sourceId);
        if (ruleBo == null) {
            return;
        }

        // 获取分类页url
        String categoryUrl = ruleBo.getCategoryUrl();
        String categoryListHtml = HttpUtil.get(categoryUrl);
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

    @Override
    public void crawlByBookId(Integer sourceId, String bookId) {
        // 获取爬虫源规则
        RuleBo ruleBo = crawlSourceService.getRuleBoById(sourceId);
        if (ruleBo == null) {
            return;
        }
        // https://wap.shuquge.com/s/{bookId}.html
        String bookDetailUrl = ruleBo.getBookDetailUrl().replace("{bookId}", bookId);
        // 小说详情页
        String bookDetailHtml = HttpUtil.get(bookDetailUrl);
        // 没有内容，直接返回
        if (StringUtils.isBlank(bookDetailHtml)) {
            return;
        }
        // 分类
        // <p>分类：<a\s+href="/sort/(\d+)/0_1.html">[^/]+</a></p>
        Pattern detailCategoryIdPatten = Pattern.compile(ruleBo.getDetailCategoryIdPatten());
        Matcher detailCategoryIdMatcher = detailCategoryIdPatten.matcher(bookDetailHtml);
        boolean isFindDetailCategoryId = detailCategoryIdMatcher.find();
        if (!isFindDetailCategoryId) {
            return;
        }
        String detailCategoryId = detailCategoryIdMatcher.group(1);
        CategoryInfo categoryInfo = categoryInfoService.getBySourceIdAndCid(sourceId, detailCategoryId);
        if (categoryInfo == null) {
            return;
        }
        // 开始爬取
        log.warn("start bookId " + bookId);
        this.saveBookInfo(bookId, ruleBo, sourceId, categoryInfo.getId());
        log.warn("end bookId " + bookId);
    }

    @SneakyThrows
    @Override
    public Result download(Long bookId, HttpServletResponse response) {
        Book book = bookService.getById(bookId);
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
        StringBuilder txt = new StringBuilder(book.getBookName());
        txt.append("\n 作者:").append(book.getAuthorName());
        txt.append("\n\n\n\n");

        // 排序，升序
        bookIndexList.sort(Comparator.comparing(BookIndex::getSortId));
        for (BookIndex bookIndex : bookIndexList) {
            String content = bookContentMap.getOrDefault(bookIndex.getId(), "");
            txt.append(bookIndex.getTitle()).append("\n").append(content).append("\n\n");
        }

        // 导出
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode(book.getBookName(), "UTF-8") + ".txt");
        ServletOutputStream outputStream = response.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(txt.toString().getBytes("utf-8"));
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        outputStream.close();
        return null;
    }
}
