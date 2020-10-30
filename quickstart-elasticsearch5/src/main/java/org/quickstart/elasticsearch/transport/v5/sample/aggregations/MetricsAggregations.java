package org.quickstart.elasticsearch.transport.v5.sample.aggregations;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.max.Max;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.min.Min;
import org.elasticsearch.search.aggregations.metrics.min.MinAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_metrics_aggregations.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/aggregations/metrics-aggregations.html'></a> Created by
 * http://quanke.name on 2017/11/16.
 */
public class MetricsAggregations extends ElasticsearchClientBase {

  @Test
  public void testMinAggregation() throws Exception {
    MinAggregationBuilder aggregation =
        AggregationBuilders
            .min("agg")
            .field("age");

    SearchResponse sr = client.prepareSearch("twitter")
        .addAggregation(aggregation)
        .get();

    // sr is here your SearchResponse object
    Min agg = sr.getAggregations().get("agg");
    String value = agg.getValueAsString();//这个统计的是日期，一般用下面方法获得最小值

//        double value = agg.getValue();

    System.out.println("min value:" + value);
  }

  @Test
  public void testMaxAggregation() throws Exception {
    MaxAggregationBuilder aggregation =
        AggregationBuilders
            .max("agg")
            .field("age");

    SearchResponse sr = client.prepareSearch("twitter")
        .addAggregation(aggregation)
        .get();

    // sr is here your SearchResponse object
    Max agg = sr.getAggregations().get("agg");
//        double value = agg.getValue();

    String value = agg.getValueAsString();

    System.out.println("max value:" + value);
  }

  @Test
  public void testSumAggregation() throws Exception {
    SumAggregationBuilder aggregation =
        AggregationBuilders
            .sum("agg")
            .field("age");

    SearchResponse sr = client.prepareSearch("twitter")
        .addAggregation(aggregation)
        .get();

    // sr is here your SearchResponse object
    Sum agg = sr.getAggregations().get("agg");
//        double value = agg.getValue();

    String value = agg.getValueAsString();

    System.out.println("sum value:" + value);
  }

  @Test
  public void testAvgAggregation() throws Exception {

  }

  @Test
  public void testStatsAggregation() throws Exception {

  }

  @Test
  public void testExtendedStatsAggregation() throws Exception {

  }

  @Test
  public void testValueCountAggregation() throws Exception {

  }

  @Test
  public void testPrepareAggregation() throws Exception {

  }

  @Test
  public void testPrepareRanksAggregation() throws Exception {

  }

  @Test
  public void testCardinalityAggregation() throws Exception {

  }

  @Test
  public void testGeoBoundsAggregation() throws Exception {

  }

  @Test
  public void testTopHitsAggregation() throws Exception {

  }

  @Test
  public void testScriptedMetricAggregation() throws Exception {

  }
}
