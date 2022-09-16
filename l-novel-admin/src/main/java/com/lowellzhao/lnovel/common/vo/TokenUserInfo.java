package com.lowellzhao.lnovel.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 登陆用户信息
 *
 * @author lowellzhao
 * @since 2022/9/7
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenUserInfo {

    /**
     * 用户Id
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
     * 登陆时间
     */
    private LocalDateTime loginTime;

}
