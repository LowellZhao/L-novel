package com.lowellzhao.lnovel.common.filter;

import com.alibaba.fastjson.JSON;
import com.lowellzhao.lnovel.common.constant.LNovelConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * LNovelFilter
 *
 * @author lowellzhao
 */
@Slf4j
public class LNovelFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 获取请求信息
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 请求头信息
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder heads = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String tempHead = headerNames.nextElement();
            heads.append(tempHead).append(": ").append(request.getHeader(tempHead)).append("; ");
        }
        // 请求包装
        LNovelHttpServletRequestWrapper lNovelHttpServletRequestWrapper = new LNovelHttpServletRequestWrapper(request);
        // traceId
        String traceId = this.getTraceId(request);
        // 日志增加 traceId
        MDC.put(LNovelConstant.TRACE_ID, traceId);
        // 请求头增加 traceId
        lNovelHttpServletRequestWrapper.addHeader(LNovelConstant.TRACE_ID, traceId);
        // body参数
        String body = LNovelHttpServletRequestWrapper.getBodyString(lNovelHttpServletRequestWrapper, Boolean.TRUE);
        // uri
        String uri = request.getRequestURI();
        if (!uri.contains("swagger") && !uri.contains("api-docs")) {
            log.info("接口请求请求过滤: traceId: {} | url: {} | method: {} | heard: {} | paraMap：{} | body: {}",
                    traceId, uri, request.getMethod(), heads, JSON.toJSONString(request.getParameterMap()), body);
        }
        // doFilter
        filterChain.doFilter(lNovelHttpServletRequestWrapper, servletResponse);
    }

    @Override
    public void destroy() {
        MDC.remove(LNovelConstant.TRACE_ID);
    }

    /**
     * 获取 traceId
     *
     * @param request 请求
     * @return 返回
     */
    private String getTraceId(HttpServletRequest request) {
        // 先从请求头获取
        String traceId = request.getHeader(LNovelConstant.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        return traceId;
    }
}
