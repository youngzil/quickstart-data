package org.quickstart.elasticsearch.transport.v5.sample.document;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 批量插入 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-bulk.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/document-apis/bulk-api.html'></a> Created by
 * http://quanke.name on 2017/11/15.
 */
public class BulkAPI extends ElasticsearchClientBase {

  @Test
  public void testPrepareBulk() throws IOException {

    BulkRequestBuilder bulkRequest = client.prepareBulk();

// either use client#prepare, or use Requests# to directly build index/delete requests
    bulkRequest.add(client.prepareIndex("twitter", "tweet", "10")
        .setSource(jsonBuilder()
            .startObject()
            .field("user", "kimchy")
            .field("postDate", new Date())
            .field("message", "trying out Elasticsearch")
            .endObject()
        )
    );

    bulkRequest.add(client.prepareIndex("twitter", "tweet", "11")
        .setSource(jsonBuilder()
            .startObject()
            .field("user", "kimchy")
            .field("postDate", new Date())
            .field("message", "another post")
            .endObject()
        )
    );

    BulkResponse bulkResponse = bulkRequest.get();
    if (bulkResponse.hasFailures()) {
      // process failures by iterating through each bulk response item
      //处理失败

      System.out.println("失败：" + bulkResponse.toString());
    }
  }
}
