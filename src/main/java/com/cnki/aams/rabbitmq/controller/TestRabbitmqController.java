package com.cnki.aams.rabbitmq.controller;

import com.cnki.aams.rabbitmq.vo.TestMessage;
import com.cnki.aams.rabbitmq.vo.TestMessageRabbitHandler;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestRabbitmqController {
    @Autowired
    RabbitTemplate rabbitTemplate;


    @GetMapping("/sendMessage")
    public String  sendMessage(@RequestParam(defaultValue ="10") Integer number){
        for (int i = 0; i < 10; i++) {
            if(i%2==0){
                TestMessage testMessage = new TestMessage();
                testMessage.setAge(10);
                testMessage.setName("xyqq-"+i);
                testMessage.setNumber(100);
                rabbitTemplate.convertAndSend("hello-java-exchange", "xyqqm",testMessage);
            }else {
                TestMessageRabbitHandler testMessage = new TestMessageRabbitHandler();
                testMessage.setAge(10);
                testMessage.setName("xyqq-"+i);
                testMessage.setNumber(100);
                testMessage.setError("error");
                //new CorrelationData(UUID.randomUUID().toString()) 可以指定每一条消息的id
                rabbitTemplate.convertAndSend("hello-java-exchange", "xyqqm",testMessage,new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return "ok";
    }

}
