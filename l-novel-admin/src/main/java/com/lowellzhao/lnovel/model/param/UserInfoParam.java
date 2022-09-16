package com.lowellzhao.lnovel.model.param;

import lombok.Data;

/**
 * @author lowellzhao
 * @since 2022/9/7
 */
@Data
public class UserInfoParam {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

}
