package org.quickstart.elasticsearch.transport.v5.sample.query;

import static org.elasticsearch.index.query.QueryBuilders.constantScoreQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-compound-queries.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/query-dsl/compound-queries.html'></a>
 * Created by http://quanke.name on 2017/11/16.
 */
public class CompoundQueries extends ElasticsearchClientBase {

    @Test
    public void testConstantScoreQuery() throws Exception {
        QueryBuilder qb = constantScoreQuery(
                termQuery(
                        "user",
                        "kimchy"
                )      //查询语句

        ).boost(2.0f);   //分数

        twitterPrepareSearch(qb);
    }

}
