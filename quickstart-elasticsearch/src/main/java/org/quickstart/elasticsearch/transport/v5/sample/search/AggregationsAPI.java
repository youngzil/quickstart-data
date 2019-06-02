package org.quickstart.elasticsearch.transport.v5.sample.search;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 在搜索中添加聚合 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-aggs.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/search-api/using-aggregations.html'></a> Created by
 * http://quanke.name on 2017/11/15.
 */
public class AggregationsAPI extends ElasticsearchClientBase {


  @Test
  public void testSearchAggregations() throws Exception {
    SearchResponse sr = client.prepareSearch("twitter")
//                .setQuery(QueryBuilders.matchAllQuery())
        .setQuery(QueryBuilders.matchAllQuery())
        .addAggregation(
            AggregationBuilders.terms("agg1").field("user")  //
        )
//                .addAggregation(
//                        AggregationBuilders.dateHistogram("agg2")
//                                .field("birth")
//                                .dateHistogramInterval(DateHistogramInterval.YEAR)
//                )
        .get();

    // Get your facet results
    Terms agg1 = sr.getAggregations().get("agg1");
//        Histogram agg2 = sr.getAggregations().get("agg2");

    println(sr);
    System.out.println("" + agg1.getName());
    System.out.println("agg1:" + agg1.getBuckets().toString());

    for (Terms.Bucket bucket :
        agg1.getBuckets()) {
      System.out.println("" + bucket.getDocCount());
    }

    //注意： 可能会报Fielddata is disabled on text fields by default
    //聚合这些操作用单独的数据结构(fielddata)缓存到内存里了，需要单独开启，官方解释在此fielddata
    //https://www.elastic.co/guide/en/elasticsearch/reference/current/fielddata.html
  }

}
