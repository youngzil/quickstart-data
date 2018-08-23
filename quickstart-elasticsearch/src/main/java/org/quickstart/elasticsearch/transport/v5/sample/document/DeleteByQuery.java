package org.quickstart.elasticsearch.transport.v5.sample.document;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * Created by http://quanke.name on 2017/11/10.
 */
public class DeleteByQuery extends ElasticsearchClientBase {

    @Test
    public void testForDeleteByQuery() throws Exception {
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(QueryBuilders.termQuery("status", 1)).execute().actionGet();

        System.out.println("删除成功！");
    }


    @Test
    public void testForAsyncDeleteByQuery() throws Exception {

        /**
         * 如果删除的比较多，需要执行的时间比较长，建议使用异步的删除操作
         */

        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("gender", "male"))
                .source("persons")
                .execute(new ActionListener<BulkByScrollResponse>() {
                    @Override
                    public void onResponse(BulkByScrollResponse response) {
                        long deleted = response.getDeleted();
                        System.out.println("异步删除成功！" + deleted);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle the exception
                        System.out.println("异步删除失败:" + e.getMessage());
                    }
                });
    }

}
