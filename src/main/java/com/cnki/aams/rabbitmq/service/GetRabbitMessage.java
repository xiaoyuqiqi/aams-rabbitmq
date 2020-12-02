package com.cnki.aams.rabbitmq.service;

import com.cnki.aams.rabbitmq.vo.TestMessage;
import com.cnki.aams.rabbitmq.vo.TestMessageRabbitHandler;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = {"hello-java-queue"})
public class GetRabbitMessage {

    /**
     * queues：声明需要监听的所有队列
     *
     * 类型：class org.springframework.amqp.core.Message
     *参数可以写以下类型
     * 1.Message message：原生详细消息。头+体
     * 2.T<发送的消息类型> TestMessage testMessage   注意：实体类要加上toString()方法
     * 3.Channel channel :当前传输数据的通道
     *
     * Queue ：可以很多人监听。只要收到消息，队列删除消息，而且只有一个收到消息。
     *
     *场景：
     *  1）、订单服务启动多个：同一个消息，只能有一个客户端收到
     *  2）、只有一个一个消息处理完，方法运行结束，我们才可以接收到下一个消息
     *
     */
//    @RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void  getMessage1(Message message, TestMessage content, Channel channel){
        //消息体  json
        byte[] body = message.getBody();
        //消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();

        System.out.println("接收到消息...内容:"+message+"==>内容："+content);
//        System.out.println("接收到消息...内容:"+message+"==>类型："+message.getClass());

    }
    @RabbitHandler
    public void  getMessage2(Message message, TestMessageRabbitHandler content, Channel channel){
        //消息体  json
        byte[] body = message.getBody();
        //消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();

        System.out.println("接收到消息...内容:"+message+"==>内容："+content);
//        System.out.println("接收到消息...内容:"+message+"==>类型："+message.getClass());

        long deliveryTag = message.getMessageProperties().getDeliveryTag();//标签

        try {
            //签收货物非批量模式
            //deliveryTag 消息标签   multiple:false  是否批量签收
            channel.basicAck(deliveryTag,false);

            if(deliveryTag%2==0){
                channel.basicAck(deliveryTag,false);
                System.out.println("签收了货物。。。"+deliveryTag);

            }else {

                //退货  requeue=false 丢弃  requeue=true  发回服务器，服务器重新入队。


                //long deliveryTag, boolean multiple, boolean requeue
                channel.basicNack(deliveryTag,false,true);

                //long deliveryTag, boolean requeue
//                channel.basicReject();不能批量

            }


        } catch (IOException e) {
            //网络中断
            e.printStackTrace();
        }
    }
}
