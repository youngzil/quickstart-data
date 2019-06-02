package org.quickstart.elasticsearch.transport.v5.sample.search;

import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * Scroll API可以允许我们检索大量数据（甚至全部数据） 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-scrolling.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/search-api/using-scrolls-in-java.html'></a> Created by
 * http://quanke.name on 2017/11/15.
 */
public class ScrollsAPI extends ElasticsearchClientBase {

  private String scrollId;

  @Test
  public void testScrolls() throws Exception {

    SearchResponse scrollResp = client.prepareSearch("twitter")
        .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
        .setScroll(new TimeValue(
            60000)) //为了使用 scroll，初始搜索请求应该在查询中指定 scroll 参数，告诉 Elasticsearch 需要保持搜索的上下文环境多长时间（滚动时间）
        .setQuery(QueryBuilders.termQuery("user", "kimchy"))                 // Query 查询条件
        .setSize(5).get(); //max of 100 hits will be returned for each scroll
    //Scroll until no hits are returned

    scrollId = scrollResp.getScrollId();
    do {
      for (SearchHit hit : scrollResp.getHits().getHits()) {
        //Handle the hit...

        System.out.println("" + hit.getSource().toString());
      }

      scrollResp = client.prepareSearchScroll(scrollId).setScroll(new TimeValue(60000)).execute()
          .actionGet();
    }
    while (scrollResp.getHits().getHits().length
        != 0); // Zero hits mark the end of the scroll and the while loop.
  }

  @Override
  public void tearDown() throws Exception {
    ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
    clearScrollRequestBuilder.addScrollId(scrollId);
    ClearScrollResponse response = clearScrollRequestBuilder.get();

    if (response.isSucceeded()) {
      System.out.println("成功清除");
    }

    super.tearDown();
  }
}
