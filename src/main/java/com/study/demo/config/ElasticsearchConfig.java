package com.study.demo.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URL;

@Configuration
public class ElasticsearchConfig implements FactoryBean<RestHighLevelClient>, InitializingBean, DisposableBean {
    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;
    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchConfig.class);
    private RestHighLevelClient restHighLevelClient;
 
    @Override
    public void destroy() throws Exception {
        try {
            if (restHighLevelClient!=null){
                restHighLevelClient.close();
            }
        }catch (IOException e){
            LOG.error("Error closing ElasticSearch client: ", e);
        }finally {
 
        }
    }
 
    @Override
    public RestHighLevelClient getObject() throws Exception {
        return restHighLevelClient;
    }
 
    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }
 
    @Override
    public void afterPropertiesSet() throws Exception {
        String[] urlList = clusterNodes.split(",");
        HttpHost[] nodes = new HttpHost[urlList.length];
        for (int i = 0; i < urlList.length; i++) {
            URL url = new URL(urlList[i]);
            HttpHost node = new HttpHost(url.getHost(),url.getPort(),url.getProtocol());
            nodes[i] = node;
        }
        RestClientBuilder builder = RestClient.builder(nodes);
        restHighLevelClient = new RestHighLevelClient(builder);
    }
}
