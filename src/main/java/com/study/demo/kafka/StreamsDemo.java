package com.study.demo.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by heyanwei-thinkpad on 2019/6/6.
 */
public class StreamsDemo {
    public static void main(String[] args) {

        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "my-stream-processing-application");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        StreamsConfig config = new StreamsConfig(props);

        StreamsBuilder builder = new StreamsBuilder();
        final Topology topology = builder.build();

        final KafkaStreams streams = new KafkaStreams(topology, config);
        final CountDownLatch latch = new CountDownLatch(1);
        Produced<String, Integer> produced = Produced.with(Serdes.String(), Serdes.Integer());
        KStream<String, String> kStream = builder.stream("test");
        kStream.mapValues(value -> value.length()).to("stream-out",produced);
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });
        try {
           streams.start();
            latch.await();
        } catch (InterruptedException e) {
           System.exit(1);
        }
        System.exit(0);
    }
}
