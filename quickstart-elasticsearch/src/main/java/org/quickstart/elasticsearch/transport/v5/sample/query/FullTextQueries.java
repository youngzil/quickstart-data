package org.quickstart.elasticsearch.transport.v5.sample.query;

import static org.elasticsearch.index.query.QueryBuilders.commonTermsQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 全文搜索
 * 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-full-text-queries.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/query-dsl/full-text-queries.html'></a>
 * Created by http://quanke.name on 2017/11/16.
 */
public class FullTextQueries extends ElasticsearchClientBase {

    /**
     * 用于执行全文查询的标准查询，包括模糊匹配和词组或邻近程度的查询
     *
     * @throws Exception
     */
    @Test
    public void testMatchQuery() throws Exception {
        QueryBuilder qb = matchQuery(
                "message",                  //field 字段
                "Elasticsearch"   // text
        );

        twitterPrepareSearch(qb);

    }

    @Test
    public void testMultiMatchQuery() throws Exception {
        QueryBuilder qb = multiMatchQuery(
                "kimchy elasticsearch", // text
                "user", "message"       //fields 多个字段
        );

        twitterPrepareSearch(qb);
    }
    @Test
    public void testCommonTermsQuery() throws Exception {
        QueryBuilder qb = commonTermsQuery(
                "name",    //field 字段
                "kimchy");  // value

        twitterPrepareSearch(qb);
    }
}
