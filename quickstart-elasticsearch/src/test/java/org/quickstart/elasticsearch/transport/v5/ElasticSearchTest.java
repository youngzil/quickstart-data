/**
 * 项目名称：quickstart-elasticsearch 文件名：ElasticSearchTest.java 版本信息： 日期：2017年11月29日 Copyright asiainfo
 * Corporation 2017 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

/**
 * ElasticSearchTest
 *
 * @author：yangzl@asiainfo.com
 * @2017年11月29日 下午10:26:35
 * @since 1.0
 */
public class ElasticSearchTest {

  TransportClient client;

  @Before
  public void before() throws UnknownHostException, InterruptedException, ExecutionException {
    Settings esSettings = Settings.builder().put("cluster.name", "elasticsearch") // 设置ES实例的名称
        .put("client.transport.sniff", true) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
        .build();
    client = new PreBuiltTransportClient(esSettings);// 初始化client较老版本发生了变化，此方法有几个重载方法，初始化插件等。
    // client = new PreBuiltTransportClient(Settings.EMPTY);
    // 此步骤添加IP，至少一个，其实一个就够了，因为添加了自动嗅探配置
    client.addTransportAddress(
        new InetSocketTransportAddress(InetAddress.getByName("10.21.20.154"), 9300));
  }

  @Test
  public void index() throws Exception {
    Map<String, Object> infoMap = new HashMap<String, Object>();
    infoMap.put("name", "zhangsan");
    infoMap.put("age", 26);
    infoMap.put("job", "programmer");
    infoMap.put("createTime", new Date());
    IndexResponse indexResponse = client.prepareIndex("test", "info", "100").setSource(infoMap)
        .execute().actionGet();
    System.out.println("id:" + indexResponse.getId());

    Map<String, Object> infoMap2 = new HashMap<String, Object>();
    infoMap2.put("name", "lisi");
    infoMap2.put("age", 24);
    infoMap2.put("job", "super programmer");
    infoMap2.put("createTime", new Date());

    IndexResponse indexResponse2 = client.prepareIndex("test", "info", "101").setSource(infoMap2)
        .execute().actionGet();
    System.out.println("id:" + indexResponse2.getId());
  }

  @Test
  public void get() throws Exception {
    // GetResponse response = client.prepareGet("sxq", "user", "2").execute().actionGet();
    GetResponse response = client.prepareGet("test", "info", "100").execute().actionGet();
    System.out.println("response.getId():" + response.getId());
    System.out.println("response.getSourceAsString():" + response.getSourceAsString());

    GetResponse response2 = client.prepareGet("test", "info", "101").execute().actionGet();
    System.out.println("response.getId():" + response2.getId());
    System.out.println("response.getSourceAsString():" + response2.getSourceAsString());

  }

  @Test
  public void query() throws Exception {
    // term查询
    // QueryBuilder queryBuilder = QueryBuilders.termQuery("age", 50) ;
    // range查询
    QueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").gt(20);
    SearchResponse searchResponse = client.prepareSearch("test").setTypes("info")
        .setQuery(rangeQueryBuilder).addSort("age", SortOrder.ASC).setSize(20).execute()
        .actionGet();
    SearchHits hits = searchResponse.getHits();
    System.out.println("查到记录数：" + hits.getTotalHits());
    SearchHit[] searchHists = hits.getHits();
    if (searchHists.length > 0) {
      for (SearchHit hit : searchHists) {
        String name = (String) hit.getSource().get("name");
        Integer age = (Integer) hit.getSource().get("age");
        System.out.format("name:%s ,age :%d \n", name, age);
      }
    }

  }

}
