/**
 * 项目名称：quickstart-elasticsearch 
 * 文件名：UserTest.java
 * 版本信息：
 * 日期：2018年8月20日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.elasticsearch.jest.example;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;

/**
 * UserTest
 * 
 * @author：yangzl@asiainfo.com
 * @2018年8月20日 下午11:14:47
 * @since 1.0
 */
public class ClientMonitorTest {

    private JestService jestService;
    private JestClient jestClient;
    private String indexName = "client_monitor_index";
    private String typeName = "client_monitor";

    @Before
    public void setUp() throws Exception {

        jestService = new JestService();
        jestClient = jestService.getJestClient();
    }

    @After
    public void tearDown() throws Exception {

        jestService.closeJestClient(jestClient);
    }

    @Test
    public void createIndex() throws Exception {

        boolean result = jestService.createIndex(jestClient, indexName);
        System.out.println(result);
    }

    @Test
    public void createIndexMapping() throws Exception {

        // String source = "{\"" + typeName + "\":{\"properties\":{" + "\"id\":{\"type\":\"integer\"}" + ",\"name\":{\"type\":\"string\",\"index\":\"not_analyzed\"}"
        // + ",\"birth\":{\"type\":\"date\",\"format\":\"strict_date_optional_time||epoch_millis\"}" + "}}}";

        File File = new File("");
        String currentPath = File.getCanonicalPath();
        File file = new File(currentPath + "/target/classes/client_monitor.json");
        String source = FileUtils.readFileToString(file, "UTF-8");

        System.out.println(source);

        boolean result = jestService.createIndexMapping(jestClient, indexName, typeName, source);
        System.out.println(result);
    }

    @Test
    public void getIndexMapping() throws Exception {

        String result = jestService.getIndexMapping(jestClient, indexName, typeName);
        System.out.println(result);
    }

    @Test
    public void index() throws Exception {

        List<Object> objs = new ArrayList<Object>();
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            ClientMonitor clientMonitor = new ClientMonitor();
            clientMonitor.setDockerID("111111111");
            clientMonitor.setIp("10.21.20.183");
            clientMonitor.setSendNum(random.nextInt(100));
            clientMonitor.setFailNum(random.nextInt(100));
            clientMonitor.setAverageCost(random.nextInt(100));
            clientMonitor.setAverageTps(random.nextInt(100));
            clientMonitor.setCollectTime(new Date());

            objs.add(clientMonitor);
        }

        boolean result = jestService.index(jestClient, indexName, typeName, objs);
        System.out.println(result);
    }

    @Test
    public void termQuery() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.termQuery("name", "T:o\"m-");// 单值完全匹配查询
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(0);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        SearchResult result = jestService.search(jestClient, indexName, typeName, query);
        List<Hit<ClientMonitor, Void>> hits = result.getHits(ClientMonitor.class);
        System.out.println("Size:" + hits.size());
        for (Hit<ClientMonitor, Void> hit : hits) {
            ClientMonitor user = hit.source;
            System.out.println(user.toString());
        }
    }

    @Test
    public void termsQuery() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.termsQuery("name", new String[] {"T:o\"m-", "J,e{r}r;y:"});// 多值完全匹配查询
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(0);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        SearchResult result = jestService.search(jestClient, indexName, typeName, query);
        List<Hit<ClientMonitor, Void>> hits = result.getHits(ClientMonitor.class);
        System.out.println("Size:" + hits.size());
        for (Hit<ClientMonitor, Void> hit : hits) {
            ClientMonitor user = hit.source;
            System.out.println(user.toString());
        }
    }

    @Test
    public void wildcardQuery() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name", "*:*");// 通配符和正则表达式查询
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(0);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        SearchResult result = jestService.search(jestClient, indexName, typeName, query);
        List<Hit<ClientMonitor, Void>> hits = result.getHits(ClientMonitor.class);
        System.out.println("Size:" + hits.size());
        for (Hit<ClientMonitor, Void> hit : hits) {
            ClientMonitor user = hit.source;
            System.out.println(user.toString());
        }
    }

    @Test
    public void prefixQuery() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.prefixQuery("name", "T:o");// 前缀查询
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(0);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        SearchResult result = jestService.search(jestClient, indexName, typeName, query);
        List<Hit<ClientMonitor, Void>> hits = result.getHits(ClientMonitor.class);
        System.out.println("Size:" + hits.size());
        for (Hit<ClientMonitor, Void> hit : hits) {
            ClientMonitor user = hit.source;
            System.out.println(user.toString());
        }
    }

    @Test
    public void rangeQuery() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.rangeQuery("birth").gte("2016-09-01T00:00:00").lte("2016-10-01T00:00:00").includeLower(true).includeUpper(true);// 区间查询
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(0);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        SearchResult result = jestService.search(jestClient, indexName, typeName, query);
        List<Hit<ClientMonitor, Void>> hits = result.getHits(ClientMonitor.class);
        System.out.println("Size:" + hits.size());
        for (Hit<ClientMonitor, Void> hit : hits) {
            ClientMonitor user = hit.source;
            System.out.println(user.toString());
        }
    }

    @Test
    public void queryString() throws Exception {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(QueryParser.escape("T:o\""));// 文本检索，应该是将查询的词先分成词库中存在的词，然后分别去检索，存在任一存在的词即返回，查询词分词后是OR的关系。需要转义特殊字符
        searchSourceBuilder.query(queryBuilder);
        searchSourceBuilder.size(10);
        searchSourceBuilder.from(0);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        SearchResult result = jestService.search(jestClient, indexName, typeName, query);
        List<Hit<ClientMonitor, Void>> hits = result.getHits(ClientMonitor.class);
        System.out.println("Size:" + hits.size());
        for (Hit<ClientMonitor, Void> hit : hits) {
            ClientMonitor user = hit.source;
            System.out.println(user.toString());
        }
    }

    @Test
    public void count() throws Exception {

        String[] name = new String[] {"T:o\"m-", "Jerry"};
        String from = "2016-09-01T00:00:00";
        String to = "2016-10-01T00:00:00";
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termsQuery("name", name)).must(QueryBuilders.rangeQuery("birth").gte(from).lte(to));
        searchSourceBuilder.query(queryBuilder);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        Double count = jestService.count(jestClient, indexName, typeName, query);
        System.out.println("Count:" + count);
    }

    @Test
    public void get() throws Exception {

        String id = "2";
        JestResult result = jestService.get(jestClient, indexName, typeName, id);
        if (result.isSucceeded()) {
            ClientMonitor user = result.getSourceAsObject(ClientMonitor.class);
            System.out.println(user.toString());
        }
    }

    @Test
    public void deleteIndexDocument() throws Exception {

        String id = "2";
        boolean result = jestService.delete(jestClient, indexName, typeName, id);
        System.out.println(result);
    }

    @Test
    public void deleteIndex() throws Exception {

        boolean result = jestService.delete(jestClient, indexName);
        System.out.println(result);
    }
}
