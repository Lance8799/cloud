package org.lance.cloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义的异步执行线程池
 */
@Configuration
public class AsyncExecPoolConfig implements AsyncConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(AsyncExecPoolConfig.class);

    @Autowired
    private AsyncExecPoolProp config;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        AsyncExecutePoolConfig config = asyncExecutePoolConfig();
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        // 拒绝策略为调用者执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return null;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {

        return (ex, method, params) -> {
            logger.error("异步执行时异常，方法：" + method.getName());
            logger.error(ex.getMessage(), ex);
        };
    }

    @Bean
    public AsyncExecPoolProp asyncExecPoolConfig(){return new AsyncExecPoolProp();}

    @ConfigurationProperties("async.exec.pool")
    class AsyncExecPoolProp {

        // 核心线程数
        private int corePoolSize = 5;
        // 最大线程数
        private int maxPoolSize = 50;
        // 线程池维护线程所允许的 空闲时间
        private int keepAliveSeconds = 60;
        // 队列长度
        private int queueCapacity = 10000;
        // 线程名称前缀
        private String threadNamePrefix = "custom-async-";

        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(int maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public int getKeepAliveSeconds() {
            return keepAliveSeconds;
        }

        public void setKeepAliveSeconds(int keepAliveSeconds) {
            this.keepAliveSeconds = keepAliveSeconds;
        }

        public int getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(int queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public String getThreadNamePrefix() {
            return threadNamePrefix;
        }

        public void setThreadNamePrefix(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }
    }

}
