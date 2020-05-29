package org.lance.cloud.redis.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 监听过期 redis 过期的 key，作下一步处理
 *
 * @author Lance
 */
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    /**
     * Creates new MessageListener for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        System.out.println("过期的Key: " + message.toString());

        // Do something...
    }
}
