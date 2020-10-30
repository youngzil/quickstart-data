package org.quickstart.elasticsearch.transport.v5.sample;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * Created by http://quanke.name on 2017/11/10.
 */
public abstract class ElasticsearchClientBase extends ElasticsearchClient {

  protected SearchResponse twitterPrepareSearch(QueryBuilder qb) {
    SearchResponse response = client.prepareSearch("twitter")//可以是多个index
        .setTypes("tweet")//可以是多个类型
        .setQuery(qb)    // Query 查询条件
        .get();

    println(response);
    return response;
  }

}
