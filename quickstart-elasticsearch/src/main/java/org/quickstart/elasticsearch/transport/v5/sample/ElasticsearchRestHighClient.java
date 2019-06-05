/**
 * 项目名称：quickstart-elasticsearch 文件名：ElasticsearchRestHighClient.java 版本信息： 日期：2018年8月21日 Copyright
 * yangzl Corporation 2018 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.sample;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.util.Utils;

/**
 * ElasticsearchRestHighClient
 *
 * https://quanke.gitbooks.io/elasticsearch-java-rest/
 *
 * @author：youngzil@163.com
 * @2018年8月21日 下午7:26:14
 * @since 1.0
 */
public class ElasticsearchRestHighClient {

  protected RestHighLevelClient client;

  @Before
  public void setUp() throws Exception {
    client = new RestHighLevelClient(RestClient.builder(
        new HttpHost("localhost", 9200, "http"),
        new HttpHost("localhost", 9201, "http"))
        .build()
    );

    System.out.println("ElasticsearchRestHighClient 启动成功");
  }

  @Test
  public void testClientConnection() throws Exception {

    System.out.println("--------------------------");
  }

  @After
  public void tearDown() throws Exception {
    if (client != null) {
//            client.close();
    }

  }


  protected void println(SearchResponse searchResponse) {
    Utils.println(searchResponse);
  }
}
