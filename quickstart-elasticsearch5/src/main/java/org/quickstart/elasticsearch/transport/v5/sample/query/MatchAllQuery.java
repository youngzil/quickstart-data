package org.quickstart.elasticsearch.transport.v5.sample.query;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 最简单的查询，它匹配所有文档 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-query-dsl-match-all-query.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/query-dsl/match-all-query.html'></a> Created by
 * http://quanke.name on 2017/11/16.
 */
public class MatchAllQuery extends ElasticsearchClientBase {

  @Test
  public void testMatchAllQuery() throws Exception {
    QueryBuilder qb = matchAllQuery();

    twitterPrepareSearch(qb);

  }

}
