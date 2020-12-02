package com.cnki.aams.rabbitmq.config;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class RabbitConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 使用JSON序列化机制，进行消息转换
     */
    @Bean
    public MessageConverter messageConverter(){

        return new Jackson2JsonMessageConverter();
    }

    /**
     *定制RabbitTemplate
     *1.服务器收到消息就回调
     *      在springboot2.2.0.RELEASE版本之前是amqp正式支持的属性，用来配置消息发送到交换器之后是否触发回调方法，
     *      在2.2.0及之后该属性过期使用spring.rabbitmq.publisher-confirm-type属性配置代替，用来配置更多的确认类型
     *      1.spring.rabbitmq.publisher-confirms=true
     *      2.设置确认回调
     *
     * 2. 消息正确抵达队列进行回调
     *      1.  #开启发送端消息抵达队列的确认
     *          spring.rabbitmq.publisher-returns=true
     *          #只要抵达队列，以异步方式优先回调我们这个returnconfirm
     *          spring.rabbitmq.template.mandatory=true
     *      2.设置确认回调ReturnsCallback
     *
     *  3.消费端确认（保证消息被正确消费，此时才可以删除broker中的消息）。
     *
     *  #手动ack消息  配置文件一定要加上  手动签收
     * spring.rabbitmq.listener.simple.acknowledge-mode=manual
     *
     *      1.默认是自动确认的，只要消息接收到，客户端会自动确认，服务端就会移除这个消息
     *          问题
     *              我们收到很多消息，自动回复给服务器ack,只有一个消息处理成功，宕机了。发生消息丢失；
     *              消费者手动确认模式。只要我们没有明确告诉MQ,货物被签收。没有ack,消息就一直是unacked状态。
     *              即使Consumer宕机。消息不会丢失，会重新变为Ready,下一次有新的Consumer链接进来就发给他。
     *      2.如何签收
     *          channel.basicAck(deliveryTag,false); 签收，业务成功执行就应该签收
     *          channel.basicNack(deliveryTag,false,true);拒签，业务失败就应该拒签
     *
     *
     */
    @PostConstruct//RabbitConfig 对象创建完成以后，执行这个方法
    public void  initRabbitTemplate(){

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback(){

            /**
             *只要消息抵达broker就ack=true
             * @param correlationData  当前消息的唯一关联数据（这个消息的唯一id）
             * @param ack  消息是否成功收到
             * @param cause   失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("confirm...correlationData["+correlationData+"]==>ack["+ack+"]==>cause["+cause+"] ");
            }
        });

        //设置消息抵达队列的确认回调

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            /**
             * 正确抵达队列不会回调，消息没送达指定队列才会回调
             * @param returned 如果出错返回详细信息
             */
            @Override
            public void returnedMessage(ReturnedMessage returned) {

                System.out.println("fail Message returned======================["+returned+"]");

            }
        });
    }


}
