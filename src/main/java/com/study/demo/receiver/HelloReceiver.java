package com.study.demo.receiver;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;

//@Component
public class HelloReceiver {

    int count = 1;

    @RabbitListener(queues = "hello")
    public void process(Message message, Channel channel) throws IOException {
        System.out.println("helloReceiver : " + new String(message.getBody(),"utf-8"));
        if(count%2 == 0){
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        }else {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);
        }
        count++;

    }

}
