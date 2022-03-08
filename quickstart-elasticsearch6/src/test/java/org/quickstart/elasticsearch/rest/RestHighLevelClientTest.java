package org.quickstart.elasticsearch.rest;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class RestHighLevelClientTest {

    String addresses = "172.16.48.198:9201,172.16.48.199:9201";
    RestHighLevelClient restHighLevelClient;

    @BeforeEach
    public void init() {

        // RestClient restClient = initRestClient(addresses);

        List<HttpHost> httpHostList = Arrays.stream(addresses.split(",")).map(address -> {
            String[] ipAndPort = address.split(":");
            return new HttpHost(ipAndPort[0], Integer.valueOf(ipAndPort[1]), "http");
        }).collect(Collectors.toList());

        HttpHost[] HttpHosts = httpHostList.toArray(new HttpHost[httpHostList.size()]);

        restHighLevelClient = new RestHighLevelClient(RestClient.builder(HttpHosts));
    }

    @Test
    public void createIndex() throws IOException {

        CreateIndexRequest request = new CreateIndexRequest("pybbs");
        request.settings(Settings.builder()//
            .put("index.number_of_shards", 1)//
            .put("index.number_of_shards", 5));

        CreateIndexResponse response = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(response.toString());
        System.out.println(response.isAcknowledged()); // 索引已经存在，则报错
    }

    @Test
    public void createDocument() throws IOException {

        Map<String, Object> map = new HashMap<>();
        map.put("title", "上海自来水来自海上");

        // 这里最后一个参数是es里储存的id，如果不填，es会自动生成一个，个人建议跟自己的数据库表里id保持一致，后面更新删除都会很方便
        IndexRequest request = new IndexRequest("pybbs", "topic", "1");
        request.source(map);
        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        // not exist: result: code: 201, status: CREATED
        // exist: result: code: 200, status: OK
        log.info("result: code: {}, status: {}", response.status().getStatus(), response.status().name());
    }

    @Test
    public void bulkDocument() throws IOException {

        Long offset = 3746448444L;
        BulkRequest requests = new BulkRequest();

        Map<String, Object> map1 = new HashMap<>();
        map1.put("topic", "topic2");
        map1.put("partition", 0);
        map1.put("offset", offset++);
        map1.put("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())+" +08:00");
        map1.put("key", "topic2 key");
        map1.put("message", "topic2 message");

        Map header = new HashMap();
        header.put("test","header");
        map1.put("header", header);
        IndexRequest request1 = new IndexRequest("middleware_hermes-msg-search_2022-01-18","topivc");
        request1.source(map1);
        requests.add(request1);

        /*Map<String, Object> map2 = new HashMap<>();
        map2.put("title", "武汉市长江大桥欢迎您");
        map2.put("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())+" +08:00");
        IndexRequest request2 = new IndexRequest("middleware_hermes-msg-search_2022-01-18", "topic", "2");
        request2.source(map2);
        requests.add(request2);

        Map<String, Object> map3 = new HashMap<>();
        map3.put("title", "上海自来水来自海上");
        map3.put("timestamp", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())+" +08:00");
        IndexRequest request3 = new IndexRequest("middleware_hermes-msg-search_2022-01-18", "topic", "3");
        request3.source(map3);
        requests.add(request3);*/

        BulkResponse responses = restHighLevelClient.bulk(requests, RequestOptions.DEFAULT);
        // not exist: result: code: 200, status: OK
        // exist: result: code: 200, status: OK
        log.info("result: code: {}, status: {}", responses.status().getStatus(), responses.status().name());
    }

    @Test
    public void deleteDocument() throws IOException {
        for (int i = 1; i <= 10; i++) {
            DeleteRequest request = new DeleteRequest("pybbs", "topic", String.valueOf(i));

            DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);
            // exist: result: code: 200, status: OK
            // not exist: result: code: 404, status: NOT_FOUND
            log.info("result: code: {}, status: {}", response.status().getStatus(), response.status().name());
        }
    }

    @Test
    public void searchDocument() throws IOException {
        SearchRequest request = new SearchRequest("pybbs");
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.matchQuery("title", "江大桥来自中国从武汉到上海喝中国人的自来水"));
        // builder.from(0).size(2); // 分页
        request.source(builder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        log.info(response.toString(), response.getTotalShards());
        for (SearchHit documentFields : response.getHits()) {
            log.info("result: {}, code: {}, status: {}", documentFields.toString(), response.status().getStatus(), response.status().name());
        }
    }

    private static RestClient initRestClient(String addresses) {
        List<HttpHost> httpHostList = Arrays.stream(addresses.split(",")).map(address -> {
            String[] ipAndPort = address.split(":");
            return new HttpHost(ipAndPort[0], Integer.valueOf(ipAndPort[1]), "http");
        }).collect(Collectors.toList());

        return RestClient.builder(httpHostList.toArray(new HttpHost[httpHostList.size()]))//
            .setRequestConfigCallback(requestConfigBuilder -> {
                requestConfigBuilder.setConnectionRequestTimeout(60000);
                requestConfigBuilder.setConnectTimeout(5000);
                requestConfigBuilder.setSocketTimeout(60000);
                return requestConfigBuilder;
            }).setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.setMaxConnTotal(500);
                httpClientBuilder.setMaxConnPerRoute(100);
                return httpClientBuilder;
            }).setMaxRetryTimeoutMillis(5 * 60 * 1000).build();
    }

}
