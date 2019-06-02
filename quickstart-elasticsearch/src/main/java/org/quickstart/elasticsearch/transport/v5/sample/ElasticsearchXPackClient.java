package org.quickstart.elasticsearch.transport.v5.sample;

import java.net.InetAddress;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.util.Utils;

/**
 * Elasticsearch XPack Client Created by http://quanke.name on 2017/11/10.
 */
public class ElasticsearchXPackClient {

  protected TransportClient client;

  @Before
  public void setUp() throws Exception {
    /**
     * 如果es集群安装了x-pack插件则以此种方式连接集群 1. java客户端的方式是以tcp协议在9300端口上进行通信 2. http客户端的方式是以http协议在9200端口上进行通信
     */
    // 安装x-pack后的初始化方法
    Settings settings = Settings.builder()//
        .put("xpack.security.user", "elastic:utan100")//
        .put("cluster.name", "utan-es")// 集群名
        .put("client.transport.sniff", true) // 自动嗅探集群中其他节点
        .put("xpack.security.transport.ssl.enabled", false)
        .put("xpack.security.user", "elastic:changme")// x-pack用户密码
        .build();
    client = new PreBuiltXPackTransportClient(settings)
        .addTransportAddress(
            new InetSocketTransportAddress(InetAddress.getByName("192.168.1.10"), 9300));
    // 一定要注意,9300为elasticsearch的tcp端口
//        try {
//            for (String ip : IP) {
//                client.addTransportAddresses(new InetSocketTransportAddress(InetAddress.getByName(ip), PORT));
//            }
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
    // final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    // credentialsProvider.setCredentials(AuthScope.ANY,
    // new UsernamePasswordCredentials("elastic", "utan100"));

    System.out.println("ElasticsearchXPackClient 启动成功");
  }

  @Test
  public void testClientConnection() throws Exception {

    System.out.println("--------------------------");
  }

  @After
  public void tearDown() throws Exception {
    if (client != null) {
      client.close();
    }

  }

  protected void println(SearchResponse searchResponse) {
    Utils.println(searchResponse);
  }
}
