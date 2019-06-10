package com;

import com.study.demo.DemoApplication;
import com.study.demo.config.ElasticSearchService;
import org.apache.dubbo.common.json.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringTest {

    @Autowired
    private ElasticSearchService elasticSearchService;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Test
    public void test() throws IOException {
//
        Object message = UUID.randomUUID().toString();
        ListenableFuture future = kafkaTemplate.send("test", message);
        future.addCallback(new ListenableFutureCallback() {
               public void onFailure(Throwable throwable) {
                   System.out.println("失败: "+ throwable.getMessage());
               }

               public void onSuccess(Object o) {
                   try {
                       System.out.println("发送成功： "+ JSON.json(o));
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }
        );
    }
}
