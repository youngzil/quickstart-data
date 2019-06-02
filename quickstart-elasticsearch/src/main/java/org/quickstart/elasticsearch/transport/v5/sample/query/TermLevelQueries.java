package org.quickstart.elasticsearch.transport.v5.sample.query;

import static org.elasticsearch.index.query.QueryBuilders.existsQuery;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;
import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 术语查询 通常用于结构化数据，如数字、日期和枚举，而不是全文字段 官方文档 @see <a href='https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-term-level-queries.html'></a>
 * 中文文档 @see <a href='https://es.quanke.name/query-dsl/term-level-queries.html'></a> Created by
 * http://quanke.name on 2017/11/16.
 */
public class TermLevelQueries extends ElasticsearchClientBase {

  @Test
  public void testTermQuery() throws Exception {
    QueryBuilder qb = termQuery(
        "user",    //field
        "kimchy"   //text
    );

    twitterPrepareSearch(qb);
  }


  @Test
  public void testTermsQuery() throws Exception {
    QueryBuilder qb = termsQuery(
        "user",    //field
        "kimchy", "quanke");                    //values

    twitterPrepareSearch(qb);
  }

  @Test
  public void testRangeQuery() throws Exception {

//        gte() :范围查询将匹配字段值大于或等于此参数值的文档。
//        gt() :范围查询将匹配字段值大于此参数值的文档。
//        lte() :范围查询将匹配字段值小于或等于此参数值的文档。
//        lt() :范围查询将匹配字段值小于此参数值的文档。
//        from() 开始值 to() 结束值 这两个函数与includeLower()和includeUpper()函数配套使用。
//        includeLower(true) 表示 from() 查询将匹配字段值大于或等于此参数值的文档。
//        includeLower(false) 表示 from() 查询将匹配字段值大于此参数值的文档。
//        includeUpper(true) 表示 to() 查询将匹配字段值小于或等于此参数值的文档。
//        includeUpper(false) 表示 to() 查询将匹配字段值小于此参数值的文档。

//        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
//        rangeQueryBuilder.from(19);
//        rangeQueryBuilder.to(21);
//        rangeQueryBuilder.includeLower(true);
//        rangeQueryBuilder.includeUpper(true);

    //上面代码等价于下面代码

    QueryBuilder qb = QueryBuilders.rangeQuery("age").gte(11).lte(21);

    twitterPrepareSearch(qb);
  }

  @Test
  public void testExistsQuery() throws Exception {
    QueryBuilder qb = existsQuery("user");

    twitterPrepareSearch(qb);
  }

  /**
   * 查找指定字段包含以指定的精确前缀开头的值的文档。
   */
  @Test
  public void testPrefixQuery() throws Exception {
    QueryBuilder qb = prefixQuery(
        "user",    //field
        "q"     //prefix
    );

    twitterPrepareSearch(qb);
  }

  /**
   * 通配符查询
   */
  @Test
  public void testWildcardQuery() throws Exception {
    QueryBuilder qb = wildcardQuery(
        "user",
        "k?mc*"
    );

    twitterPrepareSearch(qb);
  }

  /**
   * 正则表达式查询
   */
  @Test
  public void testRegexpQuery() throws Exception {

    QueryBuilder qb = regexpQuery(
        "user",        //field
        "k.*");             //regexp
    twitterPrepareSearch(qb);
  }

  @Test
  public void testFuzzyQuery() throws Exception {

    QueryBuilder qb = fuzzyQuery(
        "user",     //field
        "kimzh"    //text
    ).fuzziness(Fuzziness.TWO);

    twitterPrepareSearch(qb);
  }


}
