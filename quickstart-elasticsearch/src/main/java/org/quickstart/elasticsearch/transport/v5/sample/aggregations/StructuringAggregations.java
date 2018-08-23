package org.quickstart.elasticsearch.transport.v5.sample.aggregations;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/_structuring_aggregations.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/aggregations/structuring-aggregations.html'></a>
 * Created by http://quanke.name on 2017/11/16.
 */
public class StructuringAggregations extends ElasticsearchClientBase {

    @Test
    public void testStructuringAggregations() throws Exception {
        SearchResponse sr = client.prepareSearch()
                .addAggregation(
                        AggregationBuilders.terms("user").field("kimchy")  //
                                .subAggregation(AggregationBuilders.dateHistogram("by_year")
                                        .field("postDate")
                                        .dateHistogramInterval(DateHistogramInterval.YEAR)
//                                        .subAggregation(AggregationBuilders.avg("avg_children").field("children"))
                                )
                )
                .execute().actionGet();

        println(sr);
    }

}
