package org.quickstart.elasticsearch.transport.v5.sample.document;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.elasticsearch.action.update.UpdateRequest;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 更新文档 Created by http://quanke.name on 2017/11/10.
 */
public class UpdateAPI extends ElasticsearchClientBase {

  /**
   * 使用 UpdateRequest 操作
   */
  @Test
  public void testForUpdateRequest() throws Exception {
    UpdateRequest updateRequest = new UpdateRequest();
    updateRequest.index("twitter");
    updateRequest.type("tweet");
    updateRequest.id("2");
    updateRequest.doc(jsonBuilder()
        .startObject()
        .field("user", "http://quanke.name")
        .endObject());
    client.update(updateRequest).get();
  }

  /**
   * 使用prepareUpdate
   */
  @Test
  public void testForPrepareUpdate() throws Exception {
//        client.prepareUpdate("twitter", "tweet", "2")
//                .setScript(new Script(ScriptType.INLINE, "ctx._source.user = \"quanke.name\"", null, null))
//                .get();

//        client.prepareUpdate("twitter", "tweet", "2")
//                .setDoc(jsonBuilder()
//                        .startObject()
//                        .field("user", "quanke.name")
//                        .endObject())
//                .get();

  }

}
