package com.lowellzhao.lnovel.common.config.graceful;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 优雅停机配置
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
@Configuration
public class GracefulShutdownCfg {

    @Value("${graceful.shutdown.timeout.seconds:30}")
    private long shutdownTimeoutSeconds;

    @Bean
    public GracefulShutdown gracefulShutdown() {
        return new GracefulShutdown(shutdownTimeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * 只支持Embedded嵌入式 服务容器
     **/
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers(gracefulShutdown());
        return factory;
    }
}
