package com.study.demo.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

/**
 * Created by heyanwei-thinkpad on 2019/6/5.
 */


/**
 *   如果所有的consumer都具有相同的group,这种情况和queue模式很像;消息将会在consumers之间负载均衡.
 *     如果所有的consumer都具有不同的group,那这就是"发布-订阅";消息将会广播给所有的消费者.
 *     在kafka中,一个partition中的消息只会被group中的一个consumer消费;每个group中consumer消息消费互相独立;我们可以认为一个group是一个"订阅"者,一个Topic中的每个partions,只会被一个"订阅者"中的一个consumer消费,不过一个consumer可以消费多个partitions中的消息.kafka只能保证一个partition中的消息被某个consumer消费时,消息是顺序的.事实上,从Topic角度来说,消息仍不是有序的.
 *
 *     kafka的原理决定,对于一个topic,同一个group中不能有多于partitions个数的consumer同时消费,否则将意味着某些consumer将无法得到消息.
 *
 *
 *  Guarantees
 *     1) 发送到partitions中的消息将会按照它接收的顺序追加到日志中
 *     2) 对于消费者而言,它们消费消息的顺序和日志中消息顺序一致.
 *     3) 如果Topic的"replicationfactor"为N,那么允许N-1个kafka实例失效.
 *
 *
 *
 *
 *     kafka通过partition的概念，保证了partition内消息有序性，缓解了上面的问题。partition内消息会复制分发给所有分组，每个分组只有一个consumer能消费这条消息。这个语义保证了某个分组消费某个分区的消息，是同步而非并发的。如果一个topic只有一个partition，那么这个topic并发消费有序，否则只是单个partition有序。
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers","127.0.0.1:9092");
        properties.put("group.id","hello-group");
        properties.put("enable.auto.commit","true");
        properties.put("auto.commit.interval.ms","1000");
        properties.put("session.timeout.ms","30000");
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        Consumer<String,String> consumer = new KafkaConsumer<String,String>(properties);
        // 本质上kafka只支持Topic.每个consumer属于一个consumer group;反过来说,每个group中可以有多个consumer.发送到Topic的消息,只会被订阅此Topic的每个group中的一个consumer消费.
        consumer.subscribe(Collections.singletonList("test"), new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {

            }
        });

        while (true){
            ConsumerRecords<String, String> records = consumer.poll(5);
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(String.format("offset=%d,key=%s,value=%s",record.offset(),record.key(),record.value()));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
