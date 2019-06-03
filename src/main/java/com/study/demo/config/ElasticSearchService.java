package com.study.demo.config;

import org.apache.dubbo.common.json.JSON;
import org.apache.dubbo.common.json.ParseException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ElasticSearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    public void save(String index, String type, Map<String, Object> saveMap) {

        Assert.hasText(index, "index 不能为空");
        Assert.hasText(type, "type 不能为空");

        if (!saveMap.containsKey("_id") || StringUtils.isEmpty(saveMap.get("_id").toString())) {
            Assert.notNull(saveMap.get("_id"), "saveMap中 _id 不能为空");
        }
        try {
            IndexRequest request = new IndexRequest(index, type, saveMap.get("_id").toString());
            saveMap.remove("_id");
            request.source(saveMap);
            IndexResponse response = restHighLevelClient.index(request);
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void save(String index,String type,String json){
        try {
            Map parse = JSON.parse(json, Map.class);
            save(index,type,parse);
        } catch (ParseException e) {
            throw new IllegalArgumentException("json 格式错误");
        }
    }

   /* public List<Map<String,Object>> queryList(){

        restHighLevelClient.search();
    }*/

    /**
     * 根据id获取文档
     * @param index
     * @param type
     * @param id
     * @param excludeFields
     * @param includeFields
     * @return
     * @throws Exception
     */
    public Map<String,Object> getById(String index,String type,String id,String excludeFields,String includeFields) throws Exception{
        GetRequest getRequest = new GetRequest(index,type,id);//文档ID
        //===============================可选参数start====================================
        //禁用_source检索，默认为启用
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        //为特定字段配置_source_include
        String[] includes = Strings.EMPTY_ARRAY;
        String[] excludes = Strings.EMPTY_ARRAY;
        if(!StringUtils.isEmpty(includeFields)){
            includes = includeFields.split(",");
        }
        if(!StringUtils.isEmpty(excludeFields)){
            excludes = excludeFields.split(",");
        }
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);


        //配置指定stored_fields的检索（要求字段在映射中单独存储）

        GetResponse getResponse = restHighLevelClient.get(getRequest);
        return getResponse.getSourceAsMap();

       /* getRequest.routing("routing");//设置routing值
        getRequest.parent("parent");//设置parent值
        getRequest.preference("preference");//设置preference值
        getRequest.realtime(false);//设置realtime为false，默认是true
        getRequest.refresh(true);//在检索文档之前执行刷新（默认为false）
        getRequest.version(2);//设置版本
        getRequest.versionType(VersionType.EXTERNAL);//设置版本类型
        //===============================可选参数end====================================

        //同步执行
        GetResponse getResponse1 = restHighLevelClient.get(getRequest);


        //异步执行
        //GetResponse 的典型监听器如下所示：
        //异步方法不会阻塞并立即返回。
        ActionListener<GetResponse> listener = new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse getResponse) {
                //执行成功时调用。 Response以参数方式提供
            }

            @Override
            public void onFailure(Exception e) {
                //在失败的情况下调用。 引发的异常以参数方式提供
            }
        };
        //异步执行获取索引请求需要将GetRequest 实例和ActionListener实例传递给异步方法：
        restHighLevelClient.getAsync(getRequest, listener);*/

    }

    /**
     * 创建索引
     * @param index
     */
    public void createIndex(String index){
        CreateIndexRequest request = new CreateIndexRequest(index);
        try {
            CreateIndexResponse indexResponse = restHighLevelClient.indices().create(request);
            if(indexResponse.isAcknowledged()){
                //新增成功
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     * @param index
     */
    public void deleteIndex(String index){
        if(exist(index)){
            DeleteIndexRequest deleteRequest = new DeleteIndexRequest(index);
            try {
                AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteRequest);
                if(acknowledgedResponse.isAcknowledged()){
                    //删除成功
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 查看索引是否存在
     */
    public boolean exist(String index){
        GetIndexRequest indexRequest = new GetIndexRequest(index);
        try {
            return restHighLevelClient.indices().exists(indexRequest, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException();
    }

    /**
     * 更新数据
     */
    public void update(String index,String type,String id,Map<String,Object> map,boolean refresh){
        Assert.hasText(index, "index 不能为空");
        Assert.hasText(type, "type 不能为空");
        Assert.hasText(id, "id 不能为空");
        UpdateRequest updateRequest = new UpdateRequest(index,type,id).doc(map);
        try {
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            if(refresh){
                updateResponse.forcedRefresh();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据
     * @param index
     * @param type
     * @param id
     */
    public void delete(String index,String type,String id){
        DeleteRequest deleteRequest = new DeleteRequest(index,type,id);
        try {
            restHighLevelClient.delete(deleteRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 批量插入
     * @param index
     * @param type
     * @param mapList
     */
    public void batchSave(String index,String type,List<Map<String,Object>> mapList){
        if(CollectionUtils.isEmpty(mapList)){
            return ;
        }
        BulkRequest bulkRequest = new BulkRequest(index,type);
        List<DocWriteRequest> docWriteRequests = new ArrayList<>();
        mapList.forEach(map -> {
            if (map.get("_id") == null || StringUtils.isEmpty(map.get("_id").toString())) {
                return;
            }
            docWriteRequests.add(new IndexRequest().opType(DocWriteRequest.OpType.CREATE).id(map.get("_id").toString()).opType(DocWriteRequest.OpType.CREATE).source(map));
        });
        bulkRequest.add(docWriteRequests.toArray(new DocWriteRequest[0]));
        try {
            restHighLevelClient.bulk(bulkRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
