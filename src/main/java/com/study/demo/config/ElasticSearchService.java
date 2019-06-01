package com.study.demo.config;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

@Service
public class ElasticSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public void save(String index,String type,Map<String,Object> saveMap){
        if(StringUtils.isEmpty(index)){
            Assert.hasText(index,"index 不能为空");
        }
        if(StringUtils.isEmpty(type)){
            Assert.hasText(type,"type 不能为空");
        }

        if(!saveMap.containsKey("_id") || StringUtils.isEmpty(saveMap.get("_id").toString())){
            Assert.notNull(saveMap.get("_id"),"saveMap中 _id 不能为空");
        }
        try {
            IndexRequest request = new IndexRequest(index,type,saveMap.get("_id").toString());
            saveMap.remove("_id");
            request.source(saveMap);
            IndexResponse response = restHighLevelClient.index(request);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
