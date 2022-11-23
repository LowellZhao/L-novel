package com.lowellzhao.lnovel.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * 过滤器配置
 *
 * @author lowellzhao
 */
@Slf4j
@Configuration
public class FilterConfiguration {

    @Bean
    public FilterRegistrationBean<Filter> lNovelFilter() {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new LNovelFilter());
        log.info("lNovelFilter bean [{}]", bean);
        return bean;
    }

}
