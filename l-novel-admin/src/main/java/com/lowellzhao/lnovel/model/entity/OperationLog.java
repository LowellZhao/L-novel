package com.lowellzhao.lnovel.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作日志表
 * </p>
 *
 * @author lowellzhao
 * @since 2022-09-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_operation_log")
public class OperationLog extends Model<OperationLog> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作模块
     */
    @TableField("opt_module")
    private String optModule;

    /**
     * 操作类型
     */
    @TableField("opt_type")
    private String optType;

    /**
     * 操作url
     */
    @TableField("opt_url")
    private String optUrl;

    /**
     * 操作方法
     */
    @TableField("opt_method")
    private String optMethod;

    /**
     * 请求参数
     */
    @TableField("request_param")
    private String requestParam;

    /**
     * 请求方式
     */
    @TableField("request_method")
    private String requestMethod;

    /**
     * 返回数据
     */
    @TableField("response_data")
    private String responseData;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 用户昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 操作ip
     */
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 操作地址
     */
    @TableField("ip_source")
    private String ipSource;

    /**
     * 请求时间
     */
    @TableField("total_time")
    private Long totalTime;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
