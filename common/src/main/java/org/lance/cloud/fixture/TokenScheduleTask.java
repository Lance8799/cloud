package org.lance.cloud.fixture;

import org.lance.cloud.api.client.AuthorizeClient;
import org.lance.cloud.api.request.AuthRequest;
import org.lance.cloud.api.result.HttpResult;
import org.lance.cloud.api.result.HttpResultBuilder;
import org.lance.cloud.exception.enums.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 定时获取token，写到系统配置信息中。
 * 也可以使用redis等
 */
@Component
public class TokenScheduleTask {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final long DELAY = 60 * 1000;

    @Value("${spring.application.name}")
    private String authId;

    @Autowired
    private AuthorizeClient authClient;

    /**
     * 启动后1秒，获取token，失败重试，超过次数设置为空字符
     */
    @Scheduled(fixedDelay = DELAY, initialDelay = 1000)
    public void run(){
        HttpResult<String> result = getToken();

        for (int count = 0; count < 5 && !result.success(); count++){
            result = getToken();
            logger.info("获取Token任务结果[{}]，执行次数[{}]", result.getData(), count);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
        System.setProperty("auth.token", result.success() ? result.getData() : "");
    }


    private HttpResult<String> getToken(){
        try{
            return authClient.token(new AuthRequest(authId));
        } catch (Exception e){
            return HttpResultBuilder.fail(ErrorType.SYSTEM);
        }
    }
}
