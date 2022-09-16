package com.lowellzhao.lnovel.model.param;

import lombok.Data;

/**
 * @author lowellzhao
 * @since 2022/9/16
 */
@Data
public class BasePageParam {

    private static final Integer DEFAULT_CURRENT = 1;
    private static final Integer DEFAULT_SIZE = 20;

    /**
     * 当前页
     */
    private Integer current;
    /**
     * 每页数量
     */
    private Integer size;

    public Integer getCurrent() {
        if (this.current == null) {
            return DEFAULT_CURRENT;
        }
        return this.current;
    }

    public Integer getSize() {
        if (this.size == null) {
            return DEFAULT_SIZE;
        }
        return this.size;
    }

    /**
     * 开始查询记录
     *
     * @return 查询起始记录数
     */
    public Integer getStartRow() {
        return (getCurrent() - 1) * getSize();
    }
}
