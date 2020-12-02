package com.cnki.aams.rabbitmq;

import com.cnki.aams.rabbitmq.vo.TestMessage;
import com.cnki.aams.rabbitmq.vo.TestMessageRabbitHandler;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RabbitmqApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;


    @Autowired
    RabbitTemplate rabbitTemplate;


    /**
     * 测试接受消息
     */
    @Test
    public void getMessage(){

    }

    /**
     * 测试发送消息
     */
    @Test
    public  void  sendMessage(){


        //1、发送消息，如果发送的这个消息是对象，我们会使用序列号机制，将对象写出去。对象必须实现序列化Serializable
        String message="helloWorld";

        //2、发送的消息，可以是一个json
//        rabbitTemplate.convertAndSend("hello-java-exchange", "xyqq","helloWorld");
        for (int i = 0; i < 10; i++) {
            if(i%2==0){
                TestMessage testMessage = new TestMessage();
                testMessage.setAge(10);
                testMessage.setName("xyqq-"+i);
                testMessage.setNumber(100);
                rabbitTemplate.convertAndSend("hello-java-exchange", "xyqq",testMessage);
            }else {
                TestMessageRabbitHandler testMessage = new TestMessageRabbitHandler();
                testMessage.setAge(10);
                testMessage.setName("xyqq-"+i);
                testMessage.setNumber(100);
                testMessage.setError("error");
                rabbitTemplate.convertAndSend("hello-java-exchange", "xyqq",testMessage);
            }
        }
        System.out.println("消息发送成功！");
    }

    /**
     * 1、如何创建Exchange[hello-java-exchange]、Queue、Binding
     *     1）、使用AmqpAdmin进行创建
     * 2、如何收发消息
     */
    @Test
    void creatExchange() {
        //amqpAdmin
        //DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        DirectExchange directExchange = new DirectExchange("hello-java-exchange",true,false,null);
        amqpAdmin.declareExchange(directExchange);
        System.out.println("exchange创建成功，hello-java-exchange");


    }

    /**
     * 创建队列
     */
    @Test
    public  void  creatQueue(){

        //Queue(String name, boolean durable, boolean exclusive(排他性，实际生产中所有的队列都不应该是排他的), boolean autoDelete,@Nullable Map<String, Object> arguments)
        //exclusive(排他性，实际生产中所有的队列都不应该是排他的),
        Queue queue = new Queue("hello-java-queue",true,false,false,null);
        amqpAdmin.declareQueue(queue);
        System.out.println("queue创建成功，hello-java-queue");
    }

    /**
     * 创建绑定
     */
    @Test
    public void creatBinding(){

        //Binding(String destination, DestinationType destinationType, String exchange, String routingKey,
        //			@Nullable Map<String, Object> arguments)
        //String destination【目的地】 DestinationType destinationType【目的地类型】
        //String exchange【交换机】  String routingKey【路由键】
        //Map<String, Object> arguments 【自定义参数】
        //将exchange指定的交换机和destination目的地进行绑定，使用routingKey作为指定的路由键
        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE,"hello-java-exchange","xyqq",null
        );
        amqpAdmin.declareBinding(binding);
        System.out.println("Binding创建成功！");

    }

}
