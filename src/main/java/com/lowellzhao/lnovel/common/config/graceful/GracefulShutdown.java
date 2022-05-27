package com.lowellzhao.lnovel.common.config.graceful;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * GracefulShutdown
 *
 * @author lowellzhao
 * @since 2022-05-26
 */
public class GracefulShutdown implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {

    private final long shutdownTimeout;

    private final TimeUnit unit;

    private volatile Connector connector;

    public GracefulShutdown(long shutdownTimeout, TimeUnit unit) {
        this.shutdownTimeout = shutdownTimeout;
        this.unit = unit;
    }

    @Override
    public void customize(Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (this.connector == null) {
            System.err.println("connector 为null, 这种通常是配置错误");
            return;
        }
        awaitTermination();
    }

    /**
     * 这个函数不要用LOG打印日志，因为在执行的时候，context可能已经关闭了导致日志输出不了，而是采用标准错误流进行记录； <br/>
     * 同时，这个函数也不会进入debug模式，因为debug端口在这个时候已经被关闭了；
     */
    private void awaitTermination() {
        this.connector.pause();

        Executor executor = this.connector.getProtocolHandler().getExecutor();
        if (executor instanceof ThreadPoolExecutor) {
            System.err.println("开始对tomcat进行优雅停机");
            try {
                ((ThreadPoolExecutor) executor).shutdown();

                if (!((ThreadPoolExecutor) executor).awaitTermination(this.shutdownTimeout, this.unit)) {
                    System.err.println("Tomcat没有在指定的时间里优雅停机, 可能有用户的业务受影响；超时时间(秒)：" + this.shutdownTimeout);
                }
                System.err.println("优雅停机完成");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
