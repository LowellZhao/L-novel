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

    /**
     * 分类抓取
     */
    @Test
    public void test03() {
        String categoryListHtml = HttpUtil.get("http://www.newbiquge.com/", "gbk");
        System.out.println(categoryListHtml);
        if (StringUtils.isBlank(categoryListHtml)) {
            return;
        }
        // <li><a\s+href="(/\w+/)">[^/]+</a></li>
        Pattern categoryIdPatten = Pattern.compile("<li><a\\s+href=\"(/\\w+/)\">[^/]+</a></li>");
        Matcher categoryIdMatcher = categoryIdPatten.matcher(categoryListHtml);
        boolean isFindCategoryId = categoryIdMatcher.find();

        // <li><a\s+href="/\w+/">([^/]+)</a></li>
        Pattern categoryNamePatten = Pattern.compile("<li><a\\s+href=\"/\\w+/\">([^/]+)</a></li>");
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

    /**
     * 测试抓取
     */
    @Test
    public void test04() {
        // 分类页
        String bookListHtml = HttpUtil.get("http://www.newbiquge.com" + "/xuanhuanxiaoshuo/", "gbk");
        System.out.println(bookListHtml);
        if (StringUtils.isBlank(bookListHtml)) {
            return;
        }
        // 《<a\s+href="http://www.newbiquge.com/book/(\d+).html"\s+target="_blank">[^/]+</a>》
        Pattern bookIdPatten = Pattern.compile("《<a\\s+href=\"http://www.newbiquge.com/book/(\\d+).html\"\\s+target=\"_blank\">[^/]+</a>》");
        Matcher bookIdMatcher = bookIdPatten.matcher(bookListHtml);
        boolean isFindBookId = bookIdMatcher.find();
        if (isFindBookId) {
            String bookId = bookIdMatcher.group(1);
            System.out.println(bookId);
            String bookDetailUrl = "http://www.newbiquge.com/book/" + bookId + ".html";
            String bookDetailHtml = HttpUtil.get(bookDetailUrl, "gbk");
            System.out.println(bookDetailHtml);
//            String bookDetailHtml = HtmlUtil.getHtml(bookDetailUrl);
//            System.out.println(bookDetailHtml);
            if (StringUtils.isNotBlank(bookDetailHtml)) {
                // <h1>([^/]+)</h1>
                Pattern bookNamePatten = Pattern.compile("<h1>([^/]+)</h1>");
                Matcher bookNameMatch = bookNamePatten.matcher(bookDetailHtml);

                boolean isFindBookName = bookNameMatch.find();
                if (isFindBookName) {
                    String bookName = bookNameMatch.group(1);
                    System.out.println(bookName);

                    // <p>作者：<a.+>([^/]+)</a></p>
                    Pattern authorNamePatten = Pattern.compile("<p>作者：<a.+>([^/]+)</a></p>");
                    Matcher authorNameMatch = authorNamePatten.matcher(bookDetailHtml);
                    boolean isFindAuthorName = authorNameMatch.find();
                    if (isFindAuthorName) {
                        String authorName = authorNameMatch.group(1);
                        System.out.println(authorName);

                        // 封面
                        // <img\s+src="(https://www.shuquge.com/files/article/image/\d+/\d+/\d+s.jpg)"
                        // <img src="(http://www.newbiquge.com/files/article/image/.+jpg)"
                        String picUrlPattenStr = "<img src=\"(http://www.newbiquge.com/files/article/image/.+jpg)\"";
                        if (StringUtils.isNotBlank(picUrlPattenStr)) {
                            Pattern picUrlPatten = Pattern.compile(picUrlPattenStr);
                            Matcher picUrlMatch = picUrlPatten.matcher(bookDetailHtml);
                            boolean isFindPicUrl = picUrlMatch.find();
                            if (isFindPicUrl) {
                                String picUrl = picUrlMatch.group(1);
                                System.err.println(picUrl);
                            }
                        }

                        String startDesc = "<meta property=\"og:description\" content=\"";
                        int descStartIndex = bookDetailHtml.indexOf(startDesc);
                        String desc = bookDetailHtml.substring(descStartIndex + startDesc.length());
                        desc = desc.substring(0, desc.indexOf("\"/>"));
                        //过滤掉简介中的特殊标签
                        desc = desc.replaceAll("<a[^<]+</a>", "")
                                .replaceAll("<font[^<]+</font>", "")
                                .replaceAll("<p>\\s*</p>", "")
                                .replaceAll("<p>", "")
                                .replaceAll("</p>", "<br/>");
                        System.out.println(desc);

                        String indexListHtml = bookDetailHtml.substring(bookDetailHtml.indexOf("正文") + "正文".length());
                        // <dd><a\s+href="(/.+).html">([^/]+)</a></dd>
                        Pattern indexIdPatten = Pattern.compile("<dd><a\\s+href=\"(/.+).html\">[^/]+</a></dd>");
                        Matcher indexIdMatch = indexIdPatten.matcher(indexListHtml);
                        boolean indexIdFind = indexIdMatch.find();

                        Pattern indexNamePatten = Pattern.compile("<dd><a\\s+href=\"/.+.html\">([^/]+)</a></dd>");
                        Matcher indexNameMatch = indexNamePatten.matcher(indexListHtml);
                        boolean indexNameFind = indexNameMatch.find();

                        if (indexIdFind && indexNameFind) {
                            String indexName = indexNameMatch.group(1);
                            System.out.println(indexName);
                            Pattern indexSortPattern = Pattern.compile("第([^/]+)章");
                            Matcher indexSortMatch = indexSortPattern.matcher(indexName);
                            boolean indexSortFind = indexSortMatch.find();
                            if (indexSortFind) {
                                String indexSort = indexSortMatch.group(1);
                                if (StringUtils.isNumeric(indexSort)) {
                                    System.out.println(Integer.parseInt(indexSort));
                                } else {
                                    int indexSortNumber = StringUtil.toNumber(indexSort);
                                    System.out.println(indexSortNumber);
                                }
                            }

                            String indexId = indexIdMatch.group(1);
                            System.out.println(indexId);

                            String contentUrl = "http://www.newbiquge.com" + indexId + ".html";
                            String contentHtml = HtmlUtil.getHtml(contentUrl);
                            System.out.println(contentHtml);
                            StringBuilder allContent = new StringBuilder();
                            while (StringUtils.isNotBlank(contentHtml)) {
                                String startContent = "<div id=\"content\">";
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
//                                String nextIndexId = nextContentMatch.group(1);
//                                contentUrl = "https://wap.shuquge.com/chapter/" + bookId + "_" + nextIndexId + ".html";
//                                contentHtml = HttpUtil.get(contentUrl);
                            }
                            System.out.println(allContent);
                            String realContent = allContent.toString().replaceAll("&nbsp;", " ")
                                    .replaceAll("<br />", "")
                                    .replaceAll("<br/>", "")
                                    .replaceAll("\n\n", "\n");
                            System.out.println(realContent);
                        }
                    }
                }
//
//                // 下一页
//                // <a\s+href="(/d/\d+_\d+.html)"\s+class="onclick">下一页</a>
//                Pattern nextIndexPatten = Pattern.compile("<a\\s+href=\"(/d/\\d+_\\d+.html)\"\\s+class=\"onclick\">下一页</a>");
//                Matcher nextIndexMatch = nextIndexPatten.matcher(bookDetailHtml);
//                boolean nextIndexMatchFind = nextIndexMatch.find();
//                if (nextIndexMatchFind) {
//                    String nextIndexUrl = nextIndexMatch.group(1);
//                    bookDetailUrl = "https://wap.shuquge.com" + nextIndexUrl;
//                    bookDetailHtml = HttpUtil.get(bookDetailUrl);
//                    System.out.println(bookDetailHtml);
//                }
            }

//            isFindBookId = bookIdMatcher.find();
        }
    }

    /**
     * 分类抓取
     * www.yuanzun888.com
     */
    @Test
    public void test05() {
        String categoryListHtml = HttpUtil.get("https://www.yuanzun888.com/");
        System.out.println(categoryListHtml);
        if (StringUtils.isBlank(categoryListHtml)) {
            return;
        }
        // <li><a href="/([^/]+)/">[^/]+</a></li>
        Pattern categoryIdPatten = Pattern.compile("<li><a href=\"/([^/]+)/\">[^/]+</a></li>");
        Matcher categoryIdMatcher = categoryIdPatten.matcher(categoryListHtml);
        boolean isFindCategoryId = categoryIdMatcher.find();

        // <li><a href="/[^/]+/">([^/]+)</a></li>
        Pattern categoryNamePatten = Pattern.compile("<li><a href=\"/[^/]+/\">([^/]+)</a></li>");
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

    /**
     * 测试抓取
     * www.yuanzun888.com
     */
    @Test
    public void test06() {
        String bookListHtml = HttpUtil.get("https://www.yuanzun888.com/xuanhuan/");
        System.out.println(bookListHtml);
        if (StringUtils.isBlank(bookListHtml)) {
            return;
        }
        // <span class="s2"><a href="/book/(\d+)/">恶仙</a></span>
        Pattern bookIdPatten = Pattern.compile("<span class=\"s2\"><a href=\"/book/(\\d+)/\">[^/]+</a></span>");
        Matcher bookIdMatcher = bookIdPatten.matcher(bookListHtml);
        boolean isFindBookId = bookIdMatcher.find();
        if (isFindBookId) {
            String bookId = bookIdMatcher.group(1);
            System.out.println(bookId);
            String bookDetailUrl = "https://www.yuanzun888.com/book/" + bookId;
            String bookDetailHtml = HttpUtil.get(bookDetailUrl);
            System.out.println(bookDetailHtml);
            if (StringUtils.isNotBlank(bookDetailHtml)) {
                // <h1>([^/]+)</h1>
                Pattern bookNamePatten = Pattern.compile("<h1>([^/]+)</h1>");
                Matcher bookNameMatch = bookNamePatten.matcher(bookDetailHtml);

                boolean isFindBookName = bookNameMatch.find();
                if (isFindBookName) {
                    String bookName = bookNameMatch.group(1);
                    System.out.println(bookName);

                    // <p>作&nbsp;&nbsp;&nbsp;&nbsp;者：([^/]+)</p>
                    Pattern authorNamePatten = Pattern.compile("<p>作&nbsp;&nbsp;&nbsp;&nbsp;者：([^/]+)</p>");
                    Matcher authorNameMatch = authorNamePatten.matcher(bookDetailHtml);
                    boolean isFindAuthorName = authorNameMatch.find();
                    if (isFindAuthorName) {
                        String authorName = authorNameMatch.group(1);
                        System.out.println(authorName);

                        // 封面
                        // <div id="fmimg"><img alt="[^/]+" src="([^/]+)" width="120" height="150" onerror="this.src='/images/nocover.jpg'" /></div>
                        String picUrlPattenStr = "<div id=\"fmimg\"><img alt=\"[^/]+\" src=\"([^/]+)\"";
                        if (StringUtils.isNotBlank(picUrlPattenStr)) {
                            Pattern picUrlPatten = Pattern.compile(picUrlPattenStr);
                            Matcher picUrlMatch = picUrlPatten.matcher(bookDetailHtml);
                            boolean isFindPicUrl = picUrlMatch.find();
                            if (isFindPicUrl) {
                                String picUrl = picUrlMatch.group(1);
                                System.err.println(picUrl);
                            }
                        }

                        // 简介
                        int descStartIndex = bookDetailHtml.indexOf("<div id=\"intro\">");
                        String desc = bookDetailHtml.substring(descStartIndex + "<div id=\"intro\">".length());
                        desc = desc.substring(0, desc.indexOf("</div>"));
                        //过滤掉简介中的特殊标签
                        desc = desc.replaceAll("<a[^<]+</a>", "")
                                .replaceAll("<font[^<]+</font>", "")
                                .replaceAll("<p>\\s*</p>", "")
                                .replaceAll("<p>", "")
                                .replaceAll("</p>", "<br/>");
                        System.out.println(desc);

                        String indexListHtml = bookDetailHtml.substring(bookDetailHtml.indexOf("<div class=\"listmain\">") + "<div class=\"listmain\">".length());
                        // <dd><a href ="/book/\d+/(\d+).html">[^/]+</a></dd>
                        Pattern indexIdPatten = Pattern.compile("<dd><a href =\"/book/\\d+/(\\d+).html\">[^/]+</a></dd>");
                        Matcher indexIdMatch = indexIdPatten.matcher(indexListHtml);
                        boolean indexIdFind = indexIdMatch.find();

                        Pattern indexNamePatten = Pattern.compile("<dd><a href =\"/book/\\d+/\\d+.html\">([^/]+)</a></dd>");
                        Matcher indexNameMatch = indexNamePatten.matcher(indexListHtml);
                        boolean indexNameFind = indexNameMatch.find();

                        System.out.println("==============================================================================");

                        if (indexIdFind && indexNameFind) {
                            String indexName = indexNameMatch.group(1);
                            System.out.println(indexName);

                            String indexId = indexIdMatch.group(1);
                            System.out.println(indexId);

                            String contentUrl = "https://www.yuanzun888.com/book/" + bookId + "/" + indexId + ".html";
                            String contentHtml = HtmlUtil.getHtml(contentUrl);
                            System.out.println(contentHtml);
                            StringBuilder allContent = new StringBuilder();
                            while (StringUtils.isNotBlank(contentHtml)) {
                                String startContent = "<div id=\"content\" class=\"showtxt\">";
                                String content = contentHtml.substring(contentHtml.indexOf(startContent) + startContent.length());
                                String endContent = "</div>";
                                content = content.substring(0, content.indexOf(endContent));
                                System.out.println(content);
                                allContent.append(content).append("\n");
                                break;
                            }
                            System.out.println(allContent);
                            String realContent = allContent.toString().replaceAll("&nbsp;", " ")
                                    .replaceAll("<br />", "")
                                    .replaceAll("天才一秒记住本站地址：www.yuanzun888.com。元尊小说网手机版阅读网址：m.yuanzun888.com", "")
                                    .replaceAll("<br/>", "");
                            System.out.println(realContent);
                        }

                    }
                }
            }

            isFindBookId = bookIdMatcher.find();
        }
    }

}
