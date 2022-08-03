package com.lowellzhao.lnovel.model.vo;

import com.lowellzhao.lnovel.model.bo.RuleBo;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 爬虫数据源vo
 *
 * @author lowellzhao
 * @date 2022/6/1
 */
@Data
public class CrawlSourceVo {

    private Integer id;

    /**
     * 爬虫源名称
     */
    @NotBlank(message = "爬虫源名称不能为空")
    private String name;

    /**
     * 爬虫规则
     */
    @Valid
    @NotNull(message = "爬虫规则不能为空")
    private RuleBo rule;

    /**
     * 爬虫源状态
     */
    private Integer status;

}
