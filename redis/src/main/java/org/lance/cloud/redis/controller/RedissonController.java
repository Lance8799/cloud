package org.lance.cloud.redis.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/redisson")
public class RedissonController {

    @Value("${server.port}")
    private String port;

    @Autowired
    private RedissonClient redissonSingle;

    @GetMapping("/lock/{time}")
    public String lock(@PathVariable long time) throws InterruptedException {
        RLock lock = redissonSingle.getLock("redissonLock");
        lock.lock(10, TimeUnit.SECONDS); // 10秒自动释放

        TimeUnit.SECONDS.sleep(time);

        lock.unlock();

        return String.format("Redisson[%s], sleep time[%s]", port, time);
    }

}
