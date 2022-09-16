package com.lowellzhao.lnovel.model.param;

import lombok.Data;

/**
 * @author lowellzhao
 * @since 2022/9/7
 */
@Data
public class UserInfoLoginParam {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
