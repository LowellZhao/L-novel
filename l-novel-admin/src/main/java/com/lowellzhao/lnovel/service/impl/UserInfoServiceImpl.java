package com.lowellzhao.lnovel.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lowellzhao.lnovel.common.constant.LNovelConstant;
import com.lowellzhao.lnovel.common.vo.Result;
import com.lowellzhao.lnovel.common.vo.TokenUserInfo;
import com.lowellzhao.lnovel.mapper.UserInfoMapper;
import com.lowellzhao.lnovel.model.entity.UserInfo;
import com.lowellzhao.lnovel.model.param.UserInfoLoginParam;
import com.lowellzhao.lnovel.model.param.UserInfoParam;
import com.lowellzhao.lnovel.service.UserInfoService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author lowellzhao
 * @since 2022-09-07
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result login(UserInfoLoginParam loginParam) {
        String username = loginParam.getUsername();
        UserInfo userInfo = this.getByUsername(username);
        if (userInfo == null) {
            return Result.error("用户不存在或者用户名错误");
        }
        String password = loginParam.getPassword();
        String md5Password = DigestUtils.md5Hex(password + LNovelConstant.USER_PWD_SALT);
        if (!md5Password.equals(userInfo.getPassword())) {
            return Result.error("用户不存在或者用户名错误");
        }
        // 生成token
        String s1 = RandomUtil.randomString(6);
        String s2 = RandomUtil.randomString(6);
        String s = s1 + userInfo.getId() + s2;
        String token = DigestUtils.md5Hex(s);
        // 存入缓存
        TokenUserInfo tokenUserInfo = TokenUserInfo.builder()
                .userId(userInfo.getId()).username(userInfo.getUsername()).nickname(userInfo.getNickname())
                .build();
        stringRedisTemplate.opsForValue().set(LNovelConstant.LOGIN_REDIS_KEY + token,
                JSON.toJSONString(tokenUserInfo), 1L, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result editUser(UserInfoParam param) {
        String username = param.getUsername();
        UserInfo userInfo = this.getByUsername(username);
        if (userInfo != null) {
            Integer userId = param.getUserId();
            if (userId == null) {
                return Result.error("用户名已存在");
            }
            if (param.getUserId() != null && !param.getUserId().equals(userInfo.getId())) {
                return Result.error("用户名已存在");
            }
            userInfo.setId(userId);
        } else {
            userInfo = new UserInfo();
        }

        String password = param.getPassword();
        userInfo.setPassword(DigestUtils.md5Hex(password + LNovelConstant.USER_PWD_SALT));
        userInfo.setNickname(param.getNickname());
        userInfo.setUsername(param.getUsername());
        boolean saveOrUpdate = this.saveOrUpdate(userInfo);
        if (saveOrUpdate) {
            return Result.success();
        }
        return Result.error();
    }

    @Override
    public UserInfo getByUsername(String username) {
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfo::getUsername, username);
        return baseMapper.selectOne(queryWrapper);
    }
}
