package com.lowellzhao.lnovel.common.component;

import com.alibaba.fastjson.JSON;
import com.lowellzhao.lnovel.common.constant.LNovelConstant;
import com.lowellzhao.lnovel.common.vo.TokenUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 通用拦截获取相关属性
 *
 * @author lowellzhao
 */
@Slf4j
@Component
public class CurrentRequestComponent {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取当前请求 Request
     *
     * @return Request
     */
    public HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (servletRequestAttributes == null) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }

    /**
     * 获取请求头token
     *
     * @return
     */
    public String getHeaderToken() {
        HttpServletRequest request = getCurrentRequest();
        return getHeaderToken(request);
    }

    public String getHeaderToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            return request.getHeader("token");
        }
        return token;
    }

    /**
     * 获取当前登陆用户信息
     *
     * @return
     */
    public TokenUserInfo getCurrentTokenUserInfo() {
        String token = getHeaderToken();
        return getCurrentTokenUserInfo(token);
    }

    public TokenUserInfo getCurrentTokenUserInfo(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String s = stringRedisTemplate.opsForValue().get(LNovelConstant.LOGIN_REDIS_KEY + token);
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return JSON.parseObject(s, TokenUserInfo.class);
    }


}