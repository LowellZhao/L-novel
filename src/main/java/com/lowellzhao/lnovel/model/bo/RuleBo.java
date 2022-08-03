package com.lowellzhao.lnovel.model.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author lowellzhao
 * @since 2022/5/26
 */
@Data
public class RuleBo {

    // ----------------------------------------------------必须要的字段 开始
    /**
     * 必须
     * 分类列表页 url
     */
    @NotBlank(message = "bookListUrl 不能为空")
    private String bookListUrl;
    /**
     * 必须
     * 爬虫源小说id 正则
     */
    @NotBlank(message = "bookIdPatten 不能为空")
    private String bookIdPatten;
    /**
     * 必须
     * 小说详情 url
     */
    @NotBlank(message = "bookDetailUrl 不能为空")
    private String bookDetailUrl;
    /**
     * 必须
     * 小说名称 正则
     */
    @NotBlank(message = "bookNamePatten 不能为空")
    private String bookNamePatten;
    /**
     * 必须
     * 作者名称 正则
     */
    @NotBlank(message = "authorNamePatten 不能为空")
    private String authorNamePatten;
    /**
     * 必须
     * 小说简介开始
     */
    @NotBlank(message = "descStart 不能为空")
    private String descStart;
    /**
     * 必须
     * 小说简介结束
     */
    @NotBlank(message = "descEnd 不能为空")
    private String descEnd;
    /**
     * 必须
     * 章节id 正则
     */
    @NotBlank(message = "indexIdPatten 不能为空")
    private String indexIdPatten;
    /**
     * 必须
     * 章节名称 正则
     */
    @NotBlank(message = "indexNamePatten 不能为空")
    private String indexNamePatten;
    /**
     * 必须
     * 小说内容详情 url
     */
    @NotBlank(message = "bookContentUrl 不能为空")
    private String bookContentUrl;
    /**
     * 必须
     * 小说内容开始
     */
    @NotBlank(message = "contentStart 不能为空")
    private String contentStart;
    /**
     * 必须
     * 小说内容结束
     */
    @NotBlank(message = "contentEnd 不能为空")
    private String contentEnd;
    /**
     * 必须
     * 分类页 url
     */
    @NotBlank(message = "categoryUrl 不能为空")
    private String categoryUrl;
    /**
     * 必须
     * 分类页分类id 正则
     */
    @NotBlank(message = "categoryIdPatten 不能为空")
    private String categoryIdPatten;
    /**
     * 必须
     * 分类页分类名称 正则
     */
    @NotBlank(message = "categoryNamePatten 不能为空")
    private String categoryNamePatten;
    /**
     * 小说下一页 正则
     */
    @NotBlank(message = "nextPagePatten 不能为空")
    private String nextPagePatten;
    /**
     * 小说封面 正则
     */
    @NotBlank(message = "picUrlPatten 不能为空")
    private String picUrlPatten;
    /**
     * 目录开始
     */
    @NotBlank(message = "bookIndexStart 不能为空")
    private String bookIndexStart;
    /**
     * 必须
     * 详情页分类名称 正则
     */
    @NotBlank(message = "detailCategoryNamePatten 不能为空")
    private String detailCategoryNamePatten;
    // ----------------------------------------------------必须要的字段 结束

    /**
     * http请求字符类型（默认utf-8）
     */
    private String charset;
    /**
     * 下一页目录地址 正则
     */
    private String nextIndexUrlPatten;
    /**
     * 网站前缀
     */
    private String webPreUrl;
    /**
     * 内容正则替换
     */
    private Map<String, String> contentPattenMap;
    /**
     * 内容页是否需要js解析
     */
    public Boolean contentNeedJs;
    /**
     * 章节排序值 正则
     */
    private String indexIdSortPattern;

}
