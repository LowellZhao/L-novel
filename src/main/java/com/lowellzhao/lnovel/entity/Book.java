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
 * 电子书信息
 * </p>
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_book")
public class Book extends Model<Book> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 来源id
     */
    @TableField("source_id")
    private Integer sourceId;

    /**
     * 分类id
     */
    @TableField("cid")
    private Integer cid;

    /**
     * 爬虫源书id
     */
    @TableField("source_book_id")
    private String sourceBookId;

    /**
     * 书名
     */
    @TableField("book_name")
    private String bookName;

    /**
     * 封面地址
     */
    @TableField("book_pic")
    private String bookPic;

    /**
     * 描述
     */
    @TableField("book_desc")
    private String bookDesc;

    /**
     * 作者
     */
    @TableField("author_name")
    private String authorName;

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
