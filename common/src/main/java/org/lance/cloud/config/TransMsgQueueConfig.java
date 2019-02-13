package org.lance.cloud.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransMsgQueueConfig {

    public static final String CONFIRM_QUEUE = "confirm.queue";
    public static final String CONFIRM_EXCHANGE = "confirm.exchange";

    public static final String CHECK_QUEUE = "check.queue";
    public static final String CHECK_EXCHANGE = "check.exchange";

    public static final String MANAGER_QUEUE = "manager.queue";
    public static final String MANAGER_EXCHANGE = "manager.exchange";

    public static final String SELLER_QUEUE = "seller.queue";
    public static final String SELLER_EXCHANGE = "seller.exchange";

    public static final String DXL_QUEUE = "dxl.queue";
    private static final String DXL_EXCHANGE = "dxl.exchange";

    // 确认队列定义绑定（可每个应用设置一个）
    @Bean
    public Queue confirmQueue(){
        return new Queue(CONFIRM_QUEUE, false, false, false);
    }

    @Bean
    public TopicExchange confirmExchange(){
        return new TopicExchange(CONFIRM_EXCHANGE, false, false);
    }

    @Bean
    public Binding confirmBinging(){
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with("confirm.#");
    }

    // 检查队列定义绑定（可每个应用设置一个）
    @Bean
    public Queue checkQueue(){
        return new Queue(CHECK_QUEUE, false, false, false);
    }

    @Bean
    public TopicExchange checkExchange(){
        return new TopicExchange(CHECK_EXCHANGE, false, false);
    }

    @Bean
    public Binding checkBinging(){
        return BindingBuilder.bind(checkQueue()).to(checkExchange()).with("check.#");
    }

    // 管理应用队列定义绑定
    @Bean
    public Queue managerQueue(){
        return new Queue(MANAGER_QUEUE, false, false, false);
    }

    @Bean
    public TopicExchange managerExchange(){
        return new TopicExchange(MANAGER_EXCHANGE, false, false);
    }

    @Bean
    public Binding managerBinging(){
        return BindingBuilder.bind(managerQueue()).to(managerExchange()).with("manager.#");
    }

    // 销售应用队列定义绑定
    @Bean
    public Queue sellerQueue(){
        return new Queue(SELLER_QUEUE, false, false, false);
    }

    @Bean
    public TopicExchange sellerExchange(){
        return new TopicExchange(SELLER_EXCHANGE, false, false);
    }

    @Bean
    public Binding sellerBinging(){
        return BindingBuilder.bind(sellerQueue()).to(sellerExchange()).with("seller.#");
    }

}
