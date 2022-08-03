package com.lowellzhao.lnovel.model.vo;

import lombok.Data;

/**
 * @author lowellzhao
 * @since 2022/8/3
 */
@Data
public class CategoryInfoVo {

    private Integer id;

    /**
     * 爬虫源id
     */
    private Integer sourceId;

    /**
     * 爬虫源分类id
     */
    private String sourceCid;

    /**
     * 分类名称
     */
    private String cname;

}
