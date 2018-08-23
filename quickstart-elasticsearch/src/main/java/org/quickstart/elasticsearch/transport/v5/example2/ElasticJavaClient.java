/**
 * 项目名称：quickstart-elasticsearch 
 * 文件名：ElasticJavaClient.java
 * 版本信息：
 * 日期：2018年8月21日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.example2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ElasticJavaClient
 * 
 * @author：yangzl@asiainfo.com
 * @2018年8月21日 下午2:33:44
 * @since 1.0
 */
public class ElasticJavaClient {

    public TransportClient getClient() throws UnknownHostException {
        Settings settings = Settings.builder().put("cluster.name", "wali").build();
        TransportClient client = new PreBuiltTransportClient(settings)//
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.254.128"), 9300))//
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.254.129"), 9300));
        return client;
    }

    /**
     * 添加一个文档
     * 
     * @param client
     * @throws JsonProcessingException
     */
    @SuppressWarnings("deprecation")
    public void addIndex(TransportClient client) throws JsonProcessingException {
        List<Category> categorys = new ArrayList<>();
        Category category1 = new Category("111", "123", "意外险", null);
        Category category2 = new Category("111", "126", "财产险", "0");
        Collections.addAll(categorys, category1, category2);
        ProductPlan plan = new ProductPlan("001", "成人综合保险", "全面保障你的财产安全", 3300, categorys, "111", null);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(plan);
        IndexResponse response = client.prepareIndex("product", "product_plan").setSource(json).get();
        System.out.println(response.status().getStatus());
        // 创建成功 反会的状态码是201
        if (response.status().getStatus() == 201) {
            System.out.println(response.getId());
        }
    }

    /**
     * 查询一个文档
     * 
     * @param client
     * @throws Exception
     */
    public void getIndex(TransportClient client) throws Exception {
        GetResponse response = client.prepareGet("product", "product_plan", "AWG4BHScpkaDOYy0S6LV").get();
        System.out.println("1： \n " + response.getSource());
        // 得到的是Map类型的结果
        Map<String, Object> map = response.getSource();
        map.forEach((k, v) -> System.out.println("key:value = " + k + ":" + v));
        ObjectMapper mapper = new ObjectMapper();
        // 得到Json类型的结果 可以转成对象
        ProductPlan plan = mapper.readValue(response.getSourceAsString(), ProductPlan.class);
        System.out.println(plan.getCategorys() + "\n " + plan.getId());
    }

    /**
     * 删除一个文档
     * 
     * @param client
     */
    public void deleteIndex(TransportClient client) {
        DeleteResponse response = client.prepareDelete("product", "product_plan", "AWG4AgRIWB5UoIZiLx5h").get();
        if (response.status().getStatus() == 200) {
            System.out.println("删除成功");
        }
    }

    /**
     * 更新
     * 
     * @param client
     * @throws Exception
     */
    public void updateIndex(TransportClient client) throws Exception {

        // 方案一 使用对象进行更新的话 如果对象中值为空的属性 都会被更新为null 数值更新为0
        /* ProductPlan plan=new ProductPlan();
           plan.setName("成人");
           ObjectMapper mapper =new ObjectMapper();
           String json=mapper.writeValueAsString(plan);
         */
        // 方案二 开始 更新需要更新的属性
        XContentBuilder json = XContentFactory.jsonBuilder();
        json.startObject().field("id", "005").field("name", "成人意外保险").field("price", 6000).endObject();
        // 方案二 结束
        UpdateRequest updateRequest = new UpdateRequest("product", "product_plan", "AWG4A4gypkaDOYy0S6LU").doc(json);

        UpdateResponse response = client.update(updateRequest).get();
        if (response.status().getStatus() == 200) {
            System.out.println("更新成功");
        }
    }

    public static void main(String[] args) throws Exception {
        ElasticJavaClient elasticJavaClient = new ElasticJavaClient();
        // elasticJavaClient.addIndex(elasticJavaClient.getClient());
        // elasticJavaClient.getIndex(elasticJavaClient.getClient());
        // elasticJavaClient.deleteIndex(elasticJavaClient.getClient());
        elasticJavaClient.updateIndex(elasticJavaClient.getClient());
    }
}
