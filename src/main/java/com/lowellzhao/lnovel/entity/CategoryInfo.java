package com.lowellzhao.lnovel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 分类信息
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_category_info")
public class CategoryInfo extends Model<CategoryInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 爬虫源id
     */
    @TableField("source_id")
    private Integer sourceId;

    /**
     * 爬虫源分类id
     */
    @TableField("source_cid")
    private String sourceCid;

    /**
     * 分类名称
     */
    @TableField("cname")
    private String cname;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标识
     */
    @TableLogic
    @TableField("del_flag")
    private Boolean delFlag;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
