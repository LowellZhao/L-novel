package com.lowellzhao.lnovel.common.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 爬取测试类
 *
 * @author lowellzhao
 * @date 2022/5/27
 */
public class HttpUtilTest {

    /**
     * 测试抓取
     */
    @Test
    public void test01() {
        // 分类页
        int catId = 1;
        // 页码
        int page = 1;
        String bookListHtml = HttpUtil.get("https://wap.shuquge.com/sort/" + catId + "/0_" + page + ".html");
        System.out.println(bookListHtml);
        if (StringUtils.isBlank(bookListHtml)) {
            return;
        }
        // href="/s/(\d+)\.html"
        Pattern bookIdPatten = Pattern.compile("href=\"/s/(\\d+)\\.html\"");
        Matcher bookIdMatcher = bookIdPatten.matcher(bookListHtml);
        boolean isFindBookId = bookIdMatcher.find();
        if (isFindBookId) {
            String bookId = bookIdMatcher.group(1);
            System.out.println(bookId);
            String bookDetailUrl = "https://wap.shuquge.com/s/" + bookId + ".html";
            String bookDetailHtml = HttpUtil.get(bookDetailUrl);
            System.out.println(bookDetailHtml);
            if (StringUtils.isNotBlank(bookDetailHtml)) {
                // <a\s+href="/s/\d+\.html"><h2>([^/]+)</h2></a>
                Pattern bookNamePatten = Pattern.compile("<a\\s+href=\"/s/\\d+\\.html\"><h2>([^/]+)</h2></a>");
                Matcher bookNameMatch = bookNamePatten.matcher(bookDetailHtml);

                boolean isFindBookName = bookNameMatch.find();
                if (isFindBookName) {
                    String bookName = bookNameMatch.group(1);
                    System.out.println(bookName);

                    // <p>作者：([^/]+)</p>
                    Pattern authorNamePatten = Pattern.compile("<p>作者：([^/]+)</p>");
                    Matcher authorNameMatch = authorNamePatten.matcher(bookDetailHtml);
                    boolean isFindAuthorName = authorNameMatch.find();
                    if (isFindAuthorName) {
                        String authorName = authorNameMatch.group(1);
                        System.out.println(authorName);

                        // 封面
                        // <img\s+src="(https://www.shuquge.com/files/article/image/\d+/\d+/\d+s.jpg)"
                        String picUrlPattenStr = "<img\\s+src=\"(https://www.shuquge.com/files/article/image/\\d+/\\d+/\\d+s.jpg)\"";
                        if (StringUtils.isNotBlank(picUrlPattenStr)) {
                            Pattern picUrlPatten = Pattern.compile(picUrlPattenStr);
                            Matcher picUrlMatch = picUrlPatten.matcher(bookDetailHtml);
                            boolean isFindPicUrl = picUrlMatch.find();
                            if (isFindPicUrl) {
                                String picUrl = picUrlMatch.group(1);
                                System.err.println(picUrl);
                            }
                        }


                        int descStartIndex = bookDetailHtml.indexOf("<div class=\"intro_info\">");
                        String desc = bookDetailHtml.substring(descStartIndex + "<div class=\"intro_info\">".length());
                        desc = desc.substring(0, desc.indexOf("最新章节推荐地址"));
                        //过滤掉简介中的特殊标签
                        desc = desc.replaceAll("<a[^<]+</a>", "")
                                .replaceAll("<font[^<]+</font>", "")
                                .replaceAll("<p>\\s*</p>", "")
                                .replaceAll("<p>", "")
                                .replaceAll("</p>", "<br/>");
                        System.out.println(desc);

                        String indexListHtml = bookDetailHtml.substring(bookDetailHtml.indexOf("正文") + "正文".length());
                        // <li><a\s+href="/chapter/\d+_\d+.html">[^/]+</a></li>
                        Pattern indexIdPatten = Pattern.compile("<li><a\\s+href=\"/chapter/\\d+_(\\d+).html\">[^/]+</a></li>");
                        Matcher indexIdMatch = indexIdPatten.matcher(indexListHtml);
                        boolean indexIdFind = indexIdMatch.find();

                        Pattern indexNamePatten = Pattern.compile("<li><a\\s+href=\"/chapter/\\d+_\\d+.html\">([^/]+)</a></li>");
                        Matcher indexNameMatch = indexNamePatten.matcher(indexListHtml);
                        boolean indexNameFind = indexNameMatch.find();

                        if (indexIdFind && indexNameFind) {
                            String indexName = indexNameMatch.group(1);
                            System.out.println(indexName);

                            String indexId = indexIdMatch.group(1);
                            System.out.println(indexId);

                            String contentUrl = "https://wap.shuquge.com/chapter/" + bookId + "_" + indexId + ".html";
                            String contentHtml = HttpUtil.get(contentUrl);
                            System.out.println(contentHtml);
                            StringBuilder allContent = new StringBuilder();
                            while (StringUtils.isNotBlank(contentHtml)) {
                                String startContent = "<div id=\"nr1\">";
                                String content = contentHtml.substring(contentHtml.indexOf(startContent) + startContent.length());
                                String endContent = "</div>";
                                content = content.substring(0, content.indexOf(endContent));
                                System.out.println(content);
                                allContent.append(content).append("\n");

                                // <a\s+id="pb_next"\s+href="/chapter/\d+_(\d+_\d+).html">下一页</a>
                                Pattern nextContentPatten = Pattern.compile("<a\\s+id=\"pb_next\"\\s+href=\"/chapter/\\d+_(\\d+_\\d+).html\">下一页</a>");
                                Matcher nextContentMatch = nextContentPatten.matcher(contentHtml);
                                boolean nextContentFind = nextContentMatch.find();
                                if (!nextContentFind) {
                                    break;
                                }
                                String nextIndexId = nextContentMatch.group(1);
                                contentUrl = "https://wap.shuquge.com/chapter/" + bookId + "_" + nextIndexId + ".html";
                                contentHtml = HttpUtil.get(contentUrl);
                            }
                            System.out.println(allContent);
                            String realContent = allContent.toString().replaceAll("&nbsp;", " ")
                                    .replaceAll("<br />", "")
                                    .replaceAll("<br/>", "");
                            System.out.println(realContent);
                        }

                    }
                }

                // 下一页
                // <a\s+href="(/d/\d+_\d+.html)"\s+class="onclick">下一页</a>
                Pattern nextIndexPatten = Pattern.compile("<a\\s+href=\"(/d/\\d+_\\d+.html)\"\\s+class=\"onclick\">下一页</a>");
                Matcher nextIndexMatch = nextIndexPatten.matcher(bookDetailHtml);
                boolean nextIndexMatchFind = nextIndexMatch.find();
                if (nextIndexMatchFind) {
                    String nextIndexUrl = nextIndexMatch.group(1);
                    bookDetailUrl = "https://wap.shuquge.com" + nextIndexUrl;
                    bookDetailHtml = HttpUtil.get(bookDetailUrl);
                    System.out.println(bookDetailHtml);
                }
            }

//            isFindBookId = bookIdMatcher.find();
        }
    }

    /**
     * 分类抓取
     */
    @Test
    public void test02() {
        String categoryListHtml = HttpUtil.get("https://wap.shuquge.com/sort/");
        System.out.println(categoryListHtml);

        System.out.println(categoryListHtml);
        if (StringUtils.isBlank(categoryListHtml)) {
            return;
        }
        // <li\s+class="prev"><a[^/]+href="/sort/(\d+)/0_1.html">[^/]+</a></li>
        Pattern categoryIdPatten = Pattern.compile("<li\\s+class=\"prev\"><a[^/]+href=\"/sort/(\\d+)/0_1.html\">[^/]+</a></li>");
        Matcher categoryIdMatcher = categoryIdPatten.matcher(categoryListHtml);
        boolean isFindCategoryId = categoryIdMatcher.find();

        Pattern categoryNamePatten = Pattern.compile("<li\\s+class=\"prev\"><a[^/]+href=\"/sort/\\d+/0_1.html\">([^/]+)</a></li>");
        Matcher categoryNameMatcher = categoryNamePatten.matcher(categoryListHtml);
        boolean isFindCategoryName = categoryNameMatcher.find();

        while (isFindCategoryId && isFindCategoryName) {
            String categoryId = categoryIdMatcher.group(1);
            System.out.println(categoryId);

            String categoryName = categoryNameMatcher.group(1);
            System.out.println(categoryName);

            isFindCategoryId = categoryIdMatcher.find();
            isFindCategoryName = categoryNameMatcher.find();
        }
    }


}
