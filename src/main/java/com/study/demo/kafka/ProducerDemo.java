package com.study.demo.kafka;

import org.apache.dubbo.common.json.JSON;
import org.apache.kafka.clients.producer.*;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by heyanwei-thinkpad on 2019/6/5.
 */
public class ProducerDemo {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        //Producer将消息发布到指定的Topic中,同时Producer也能决定将此消息归属于哪个partition;比如基于"round-robin"方式或者通过其他的一些算法等.
        for(int i = 0; i < 100; i++)
            producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), UUID.randomUUID().toString()), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    try {
                        System.out.println("消息："+ JSON.json(recordMetadata));
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });

        producer.close();
    }
}
