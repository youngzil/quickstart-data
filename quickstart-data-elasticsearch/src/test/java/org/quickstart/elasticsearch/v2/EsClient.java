/**
 * 项目名称：quickstart-elasticsearch 
 * 文件名：EsClient.java
 * 版本信息：
 * 日期：2017年11月30日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.elasticsearch.v2;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * EsClient ESclient 工具类
 * 
 * @author：yangzl@asiainfo.com
 * @2017年11月30日 下午4:51:44
 * @since 1.0
 */
public class EsClient {
    static Logger logger = LoggerFactory.getLogger(EsClient.class);
    static final String host = "10.21.20.154"; // 节点IP
    public static Client client;
    static {
        // 设置集群名称和自动感知节点 集群名称为"my-application"
        Settings settings = Settings.settingsBuilder()//
                .put("cluster.name", "my-application")//
                .put("client.transport.sniff", true).build();
        try {
            // 创建节点客户端，9300位节点的端口，不是集群端口
            client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.info("unknownHost={}", host);
        }
    }

    /**
     * 通过ID查询数据， test为索引库，blog为类型，id为标识
     */

    public static GetResponse get(String id) {
        return client.prepareGet("test", "blog", id).get();
    }

    /**
     * 创建索引，indexName 索引名称
     */
    public static CreateIndexResponse createIndex(String indexName) throws IOException {
        return client.admin().indices().prepareCreate(indexName).execute().actionGet();
    }

    // 集群状态
    public static void clusterStatus() {
        // 注意集群的client获取方式略有不同，
        ClusterAdminClient clusterAdminClient = client.admin().cluster();
        ClusterHealthResponse healths = clusterAdminClient.prepareHealth().get();
        String clusterName = healths.getClusterName();
        int numberOfDataNodes = healths.getNumberOfDataNodes();
        int numberOfNodes = healths.getNumberOfNodes();
        ClusterHealthStatus status = healths.getStatus();
        System.out.println(clusterName + "###" + numberOfDataNodes + "###" + status.name());

    }

    public static void addType() throws IOException {
        // 定义索引字段属性,其实这里就是组合json,可以参考curl 方式创建索引的json格式 此处blog 和执行时blog必须一致
        XContentBuilder builder = XContentFactory.jsonBuilder().startObject().startObject("blog").startObject("properties").startObject("id").field("type", "integer").field("store", "yes").endObject()
                .startObject("title").field("type", "string").field("store", "yes").endObject().startObject("content").field("type", "string").field("store", "yes").endObject().endObject().endObject()
                .endObject();

        PutMappingRequest mappingRequest = Requests.putMappingRequest("test").type("blog").source(builder);
        client.admin().indices().putMapping(mappingRequest).actionGet();

    }

    /**
     * 创建数据
     */
    public static void createData() throws IOException {
        // 创建数据json 注意此ID是一个字段不是上面查询的id
        XContentBuilder builder = jsonBuilder().startObject().field("id", "2").field("title", "我是标题").field("content", "我是内容").endObject();
        IndexResponse indexResponse = client.prepareIndex("test", "blog").setSource(builder).execute().actionGet();
        System.out.println(indexResponse.isCreated());
    }

}
