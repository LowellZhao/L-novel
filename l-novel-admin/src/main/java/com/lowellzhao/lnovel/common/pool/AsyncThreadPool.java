package com.lowellzhao.lnovel.common.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步线程池配置
 * <p>
 * 设置proxyTargetClass=true来强制使用基于cglib的代理
 *
 * @author lowellzhao
 * @since 2022/9/7
 */
@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncThreadPool {

    /**
     * 日志的线程池
     *
     * @return 日志的线程池
     */
    @Primary
    @Bean("logExecutor")
    public Executor logExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(2);
        // 最大线程数
        executor.setMaxPoolSize(5);
        // 队列容量
        executor.setQueueCapacity(500);
        // 线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 线程名称
        executor.setThreadNamePrefix("logExecutor-");
        // 拒绝策略 ，异常不抛出异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时长（秒）
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    /**
     * 业务线程池
     *
     * @return 业务线程池
     */
    @Primary
    @Bean("taskExecutor")
    public Executor searchExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 核心线程数
        executor.setCorePoolSize(2);
        // 最大线程数
        executor.setMaxPoolSize(5);
        // 队列容量
        executor.setQueueCapacity(500);
        // 线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);
        // 线程名称
        executor.setThreadNamePrefix("taskExecutor-");
        // 拒绝策略 ，异常不抛出异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时长（秒）
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

}
