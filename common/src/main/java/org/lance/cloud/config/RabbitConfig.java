package org.lance.cloud.config;

import org.lance.cloud.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
@EnableRabbit //To enable support for @RabbitListener annotations add @EnableRabbit
public class RabbitConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ThreadLocal<Boolean> rcb = ThreadLocal.withInitial(() -> false);

    /**
     * 配置消息确认和失败处理的rabbitTemplate
     * @param connectionFactory
     * @return
     */
    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @ConditionalOnProperty(value = "spring.rabbitmq.template.mandatory", havingValue = "true")
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);

        // 如果发送到交换器成功，但是没有匹配的队列（比如说取消了绑定），ack 返回值为还是 true
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack || rcb.get()){
                logger.warn("消息未能到达rabbitmq服务器, ack[{}], 关联数据{}", ack, correlationData);
                rcb.remove();
                throw new ApplicationException("消息未能到达rabbitmq服务器");
            }
            rcb.remove();
        });

        // 如果发送到交换器成功，但是没有匹配的队列，就会触发这个回调
        // 在vhost中若找不到Exchange时，confirmCallBack会被触发，而returnCallBack不会被触发
        template.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                rcb.set(true);
                logger.warn("消息发送失败，exchange[{}]，routingKey[{}]，replyText[{}]", exchange, routingKey, replyText);
                throw new ApplicationException("消息发送失败");
            }
        });

        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    /**
     * 设置@RabbitListener默认配置
     * @param connectionFactory
     * @return
     */
    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @ConditionalOnProperty("spring.rabbitmq.listener.simple.acknowledge-mode")
    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(CachingConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        factory.setErrorHandler(customErrorHandler());
        return factory;
    }

    /**
     * 注册消息转换器
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 注册异常处理
     * @return
     */
    @Bean
    public ErrorHandler customErrorHandler(){
        return new ConditionalRejectingErrorHandler(new CustomErrorHandler());
    }

    /**
     * 自定义消息异常处理
     */
    private class CustomErrorHandler extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

        private final Logger logger = LoggerFactory.getLogger(getClass());

        @Override
        public boolean isFatal(Throwable t) {
            if (t instanceof ListenerExecutionFailedException){
                ListenerExecutionFailedException ex = (ListenerExecutionFailedException) t;
                logger.error("异常消息来源队列[{}]，异常信息[{}]", ex.getFailedMessage().getMessageProperties().getConsumerQueue(), ex.getMessage(), ex);
            }
            return super.isFatal(t);
        }
    }

}
