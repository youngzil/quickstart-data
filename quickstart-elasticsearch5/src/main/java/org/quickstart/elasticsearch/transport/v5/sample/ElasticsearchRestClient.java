package org.quickstart.elasticsearch.transport.v5.sample;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.util.Utils;

/**
 * Elasticsearch Rest Client
 * <p>
 * Created by http://quanke.name on 2017/11/10.
 */
public class ElasticsearchRestClient {

  protected RestClient client;

  @Before
  public void setUp() throws Exception {

    Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};

    // 构建RestClient时配置HttpClientConfigCallback来配置基本认证
    // 可以通过HttpClientConfigCallback配置加密传输
    client = RestClient.builder(//
        new HttpHost("localhost", 9200, "http"), // 1
        new HttpHost("localhost", 9201, "http")).// 2
        setDefaultHeaders(defaultHeaders) // 3 设置默认头文件，避免每个请求都必须指定。
        .setMaxRetryTimeoutMillis(
            10000)// 4 也调整最大重试超时时间（默认为30秒）。设置在同一请求进行多次尝试时应该遵守的超时时间。默认值为30秒，与默认`socket`超时相同。 如果自定义设置了`socket`超时，则应该相应地调整最大重试超时。
        .setFailureListener(new RestClient.FailureListener() {
          @Override
          public void onFailure(HttpHost host) {
            // 设置每次节点发生故障时收到通知的侦听器。内部嗅探到故障时被启用。
          }
        })// 5
        .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
          @Override
          public RequestConfig.Builder customizeRequestConfig(
              RequestConfig.Builder requestConfigBuilder) {
            return requestConfigBuilder//
                .setConnectTimeout(5000)// 连接超时（默认为1秒）和socket超时（默认为30秒）
                .setSocketTimeout(10000); // 设置修改默认请求配置的回调（例如：请求超时，认证，或者其他
            // [org.apache.http.client.config.RequestConfig.Builder](https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/config/RequestConfig.Builder.html)
            // 设置）。
          }
        })// 6
        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
          @Override
          public HttpAsyncClientBuilder customizeHttpClient(
              HttpAsyncClientBuilder httpClientBuilder) {
            return httpClientBuilder// Apache Http Async Client 默认启动一个调度线程，连接管理器使用多个worker线程,线程的数量和CPU核数量相同（等于 Runtime.getRuntime().availableProcessors()
                // Runtime.getRuntime().availableProcessors()返回的数量）
                .setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build())//
                .setProxy(
                    new HttpHost("proxy", 9000, "http")); // 设置修改 http 客户端配置的回调（例如：ssl 加密通讯，或其他任何
            // [org.apache.http.impl.nio.client.HttpAsyncClientBuilder](http://hc.apache.org/httpcomponents-asyncclient-dev/httpasyncclient/apidocs/org/apache/http/impl/nio/client/HttpAsyncClientBuilder.html)
            // 设置）
          }
        })// 7
        .build();

    System.out.println("ElasticsearchRestClient 启动成功");
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
