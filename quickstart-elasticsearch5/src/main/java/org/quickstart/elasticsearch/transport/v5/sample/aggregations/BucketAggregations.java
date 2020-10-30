package org.quickstart.elasticsearch.transport.v5.sample.aggregations;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filters.Filters;
import org.elasticsearch.search.aggregations.bucket.filters.FiltersAggregator;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 桶分聚合 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_bucket_aggregations.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/aggregations/bucket-aggregations.html'></a> Created by
 * http://quanke.name on 2017/11/16.
 */
public class BucketAggregations extends ElasticsearchClientBase {

  @Test
  public void testGlobalAggregation() throws Exception {

    AggregationBuilder aggregation = AggregationBuilders
        .global("agg")
        .subAggregation(
            AggregationBuilders.terms("users").field("user")
        );

    SearchResponse sr = client.prepareSearch("twitter")
        .addAggregation(aggregation)
        .get();

    // sr is here your SearchResponse object
    Global agg = sr.getAggregations().get("agg");
    long count = agg.getDocCount(); // Doc count

    System.out.println("global count:" + count);
  }

  @Test
  public void testFilterAggregation() throws Exception {

    AggregationBuilder aggregation =
        AggregationBuilders
            .filters("agg",
                new FiltersAggregator.KeyedFilter("men", QueryBuilders.termQuery("gender", "male")),
                new FiltersAggregator.KeyedFilter("women",
                    QueryBuilders.termQuery("gender", "female")));

    SearchResponse sr = client.prepareSearch("twitter")
        .addAggregation(aggregation)
        .get();

    // sr is here your SearchResponse object
    Filters agg = sr.getAggregations().get("agg");
    // For each entry

    for (Filters.Bucket entry : agg.getBuckets()) {
      String key = entry.getKeyAsString();            // bucket key
      long docCount = entry.getDocCount();            // Doc count

      System.out.println("global " + key + " count:" + docCount);
    }

  }
}
