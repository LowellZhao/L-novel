package com.lowellzhao.lnovel.service;

import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.model.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lowellzhao.lnovel.model.param.UserInfoLoginParam;
import com.lowellzhao.lnovel.model.param.UserInfoParam;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-09-07
 */
public interface UserInfoService extends IService<UserInfo> {

    Result login(UserInfoLoginParam loginParam);

    Result editUser(UserInfoParam param);

    UserInfo getByUsername(String username);

}
