package org.quickstart.elasticsearch.transport.v5.sample.search;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 在同一API中执行多个搜索请求 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-scrolling.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/search-api/using-scrolls-in-java.html'></a> Created by
 * http://quanke.name on 2017/11/15.
 */
public class MultiSearchAPI extends ElasticsearchClientBase {

  @Test
  public void testMultiSearch() throws Exception {

    SearchRequestBuilder srb1 = client
        .prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(1);

    SearchRequestBuilder srb2 = client
        .prepareSearch().setQuery(QueryBuilders.matchQuery("name", "kimchy")).setSize(1);

    MultiSearchResponse sr = client.prepareMultiSearch()
        .add(srb1)
        .add(srb2)
        .get();

    // You will get all individual responses from MultiSearchResponse#getResponses()
    long nbHits = 0;
    for (MultiSearchResponse.Item item : sr.getResponses()) {
      SearchResponse response = item.getResponse();

      nbHits += response.getHits().getTotalHits();

      println(response);
    }

    System.out.println("nbHits:" + nbHits);

  }

}
