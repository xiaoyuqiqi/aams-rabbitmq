package com.cnki.aams.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**使用RabbitMQ
 * 快捷键 ctrl+shift+r 查找源码配置类
 * 1.引入amqp场景;RabbitAutoConfiguration就会自动生效
 * 2.给容器中自动配置了
 *      RabbitTemplate、AmqpAdmin、CachingConnectionFactory、RabbitMessagingTemplate;
 *      所有的属性都是spring.rabbitmq
 *      @ConfigurationProperties(prefix = "spring.rabbitmq")
 * public class RabbitProperties {
 * 3.给配置文件中配置spring.rabbitmq信息
 * 4.@EnableRabbit: @EnableXxxx 开启功能
 * 5.监听消息:使用@RabbitListener；必须有@EnableRabbit
 *      @RabbitListener:类+方法上   （监听哪些队列即可）
 *      @RabbitHandler：标在方法上 (重载区分不同的消息) 可以根据消息类型标注不同的方法
 *
 */

@EnableRabbit
@SpringBootApplication
public class RabbitmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqApplication.class, args);
    }

}
