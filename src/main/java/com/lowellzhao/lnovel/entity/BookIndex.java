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
 * 章节信息
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_book_index")
public class BookIndex extends Model<BookIndex> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 书id
     */
    @TableField("book_id")
    private Long bookId;

    /**
     * 数据源章节id
     */
    @TableField("source_index_id")
    private String sourceIndexId;

    /**
     * 排序值
     */
    @TableField("sort_id")
    private Integer sortId;

    /**
     * 章节标题
     */
    @TableField("title")
    private String title;

    /**
     * 是否成功
     */
    @TableField("success")
    private Boolean success;

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
