package org.quickstart.elasticsearch.transport.v5.sample.document;

import org.elasticsearch.action.delete.DeleteResponse;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 删除文档
 * Created by http://quanke.name on 2017/11/10.
 */
public class DeleteAPI extends ElasticsearchClientBase {

    @Test
    public void testForDeleteAPI() throws Exception {
        DeleteResponse response = client.prepareDelete("twitter", "tweet", "1").get();

        System.out.println("删除成功！");
    }
}
