/**
 * 项目名称：quickstart-elasticsearch 文件名：ElasticsearchNodeClient.java 版本信息： 日期：2018年8月21日 Copyright
 * yangzl Corporation 2018 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.sample;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.util.Utils;

/**
 * ElasticsearchNodeClient
 *
 * @author：youngzil@163.com
 * @2018年8月21日 下午8:17:34
 * @since 1.0
 */
public class ElasticsearchNodeClient {

  // protected Client client;
  protected TransportClient client;

  @Before
  public void setUp() throws Exception {

    // on startup
    Settings esSettings = Settings.builder()//
        .put("cluster.name", "utan-es") // 设置ES实例的名称集群名称
        .put("client.transport.sniff", true) // 自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
        .build();
    // Node node = NodeBuilder.nodeBuilder().clusterName("flight_fuwu_order_index").client(true).settings(settings).node();
    // Client client = node.client();

    System.out.println("ElasticsearchNodeClient 启动成功");
  }

  @Test
  public void testClientConnection() throws Exception {

    System.out.println("--------------------------");
  }

  @After
  public void tearDown() throws Exception {
    if (client != null) {
      // client.close();
    }

  }

  protected void println(SearchResponse searchResponse) {
    Utils.println(searchResponse);
  }

}
