package com.study.demo.receiver;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {

  private final static String QUEUE_NAME = "user";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    factory.setPort(5672);
    factory.setUsername("guest");
    factory.setPassword("guest");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
          throws IOException {
        try {
          String message = new String(body, "UTF-8");
          System.out.println(" [x] Received '" + message + "'");
//          int i = 1/0;
          channel.basicAck(envelope.getDeliveryTag(),false);
        } catch (Exception e) {
          channel.basicNack(envelope.getDeliveryTag(),false,true);
        }
      }
    };
    channel.basicConsume(QUEUE_NAME, false, consumer);

  }
}