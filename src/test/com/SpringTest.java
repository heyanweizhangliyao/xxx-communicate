package com;

import com.study.demo.DemoApplication;
import com.study.demo.config.ElasticSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringTest {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Test
    public void test() throws IOException {
        Map<String,Object> order =new HashMap<>();
        order.put("orderNo","201907240301");
        order.put("created",new Date());
        order.put("amount", BigDecimal.ONE);
        order.put("_id",order.get("orderNo"));
        elasticSearchService.save("buydeem","order",order);
    }
}
