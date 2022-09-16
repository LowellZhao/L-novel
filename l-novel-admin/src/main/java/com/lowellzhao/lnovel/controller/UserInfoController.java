package com.lowellzhao.lnovel.controller;


import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.model.param.UserInfoLoginParam;
import com.lowellzhao.lnovel.model.param.UserInfoParam;
import com.lowellzhao.lnovel.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author lowellzhao
 * @since 2022-09-07
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/login")
    public Result login(@RequestBody UserInfoLoginParam loginParam) {
        return userInfoService.login(loginParam);
    }

    @PostMapping("/editUser")
    public Result editUser(@RequestBody UserInfoParam param) {
        if (StringUtils.isBlank(param.getUsername())) {
            return Result.error("用户名不能为空");
        }
        return userInfoService.editUser(param);
    }

}

