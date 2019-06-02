/**
 * 项目名称：quickstart-elasticsearch 文件名：IndexAPI.java 版本信息： 日期：2018年8月21日 Copyright asiainfo
 * Corporation 2018 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.sample.rest.high;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchRestHighClient;

/**
 * IndexAPI
 *
 * https://quanke.gitbooks.io/elasticsearch-java-rest/
 *
 * @author：yangzl@asiainfo.com
 * @2018年8月21日 下午8:06:29
 * @since 1.0
 */
public class IndexAPI extends ElasticsearchRestHighClient {

  @Test
  public void testIndex() throws Exception {
    IndexRequest request = new IndexRequest("posts", // Index
        "doc", // Type
        "1"); // Document id
    String jsonString = "{" + "\"user\":\"kimchy\"," + "\"postDate\":\"2013-01-30\","
        + "\"message\":\"trying out Elasticsearch\"" + "}";
    request.source(jsonString, XContentType.JSON); // 以字符串提供的 Document source

    Map<String, Object> jsonMap = new HashMap<>();
    jsonMap.put("user", "kimchy");
    jsonMap.put("postDate", new Date());
    jsonMap.put("message", "trying out Elasticsearch");
    IndexRequest indexRequest = new IndexRequest("posts", "doc", "1")
        .source(jsonMap); // Map 作为文档源，它可以自动转换为 JSON 格式。

    XContentBuilder builder = XContentFactory.jsonBuilder();
    builder.startObject();
    {
      builder.field("user", "kimchy");
      builder.field("postDate", new Date());
      builder.field("message", "trying out Elasticsearch");
    }
    builder.endObject();
    IndexRequest indexRequest2 = new IndexRequest("posts", "doc", "1")
        .source(builder); // XContentBuilder 对象作为文档源，由 Elasticsearch 内置的帮助器生成 JSON 内容

    IndexRequest indexRequest3 = new IndexRequest("posts", "doc", "1")
        .source("user", "kimchy", "postDate", new Date(), "message",
            "trying out Elasticsearch"); // 以键值对对象作为文档来源，它自动转换为 JSON 格式

    // 下列参数可选：
    request.routing("routing"); // Routing 值
    request.parent("parent"); // Parent 值
    request.timeout(TimeValue.timeValueSeconds(1)); // `TimeValue`类型的等待主分片可用的超时时间
    request.timeout("1s"); // `String` 类型的等待主分片可用的超时时间
    request.setRefreshPolicy(
        WriteRequest.RefreshPolicy.WAIT_UNTIL); // 以 WriteRequest.RefreshPolicy 实例的刷新策略参数
    request.setRefreshPolicy("wait_for"); // 字符串刷新策略参数
    request.version(2); // 版本
    request.versionType(VersionType.EXTERNAL); // 版本类型
    request.opType(DocWriteRequest.OpType.CREATE); // 提供一个 DocWriteRequest.OpType 值作为操作类型
    request.opType("create"); // 字符串类型的操作类型参数: 可以是 create 或 update (默认值)
    request.setPipeline("pipeline"); // 在索引文档之前要执行的摄取管道的名称

    // 同步执行
    IndexResponse indexResponse = client.index(request);

    // 异步执行
    client.indexAsync(request, new ActionListener<IndexResponse>() {
      @Override
      public void onResponse(IndexResponse indexResponse) {
        // 当操作成功完成的时候被调用。响应对象以参数的形式传入。
      }

      @Override
      public void onFailure(Exception e) {
        // 故障时被调用。异常对象以参数的形式传入
      }
    });

    // 返回的“IndexResponse”可以检索有关执行操作的信息，如下所示：
    String index = indexResponse.getIndex();
    String type = indexResponse.getType();
    String id = indexResponse.getId();
    long version = indexResponse.getVersion();
    if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
      // 处理（如果需要）首次创建文档的情况
    } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
      // 处理（如果需要）文档已经存在时被覆盖的情况
    }
    ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
    if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
      // 处理成功碎片数少于总碎片的情况
    }
    if (shardInfo.getFailed() > 0) {
      for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
        String reason = failure.reason();// 处理潜在的故障
      }
    }

    // 如果存在版本冲突，将抛出 ElasticsearchException :
    IndexRequest request5 = new IndexRequest("posts", "doc", "1").source("field", "value")
        .version(1);
    try {
      IndexResponse response = client.index(request);
    } catch (ElasticsearchException e) {
      if (e.status() == RestStatus.CONFLICT) {
        // 表示是由于返回了版本冲突错误引发的异常
      }
    }

    // 发生同样的情况发生在opType设置为create但是已经存在具有相同索引，类型和id的文档时：
    IndexRequest request6 = new IndexRequest("posts", "doc", "1").source("field", "value")
        .opType(DocWriteRequest.OpType.CREATE);
    try {
      IndexResponse response = client.index(request);
    } catch (ElasticsearchException e) {
      if (e.status() == RestStatus.CONFLICT) {
        // 表示由于返回了版本冲突错误引发的异常
      }
    }

  }

}
