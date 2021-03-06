/**
 * 项目名称：quickstart-elasticsearch 文件名：JestService.java 版本信息： 日期：2018年8月20日 Copyright yangzl
 * Corporation 2018 版权所有 *
 */
package org.quickstart.elasticsearch.jest.example;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Count;
import io.searchbox.core.CountResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.aliases.ModifyAliases;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;

/**
 * JestService
 *
 * @author：youngzil@163.com
 * @2018年8月20日 下午11:13:35
 * @since 1.0
 */
public class JestService {

  /**
   * 获取JestClient对象
   */
  public JestClient getJestClient() {

    List<String> urlList = new ArrayList<>();
    urlList.add("http://20.26.38.165:8200");
    urlList.add("http://20.26.38.164:8201");
    urlList.add("http://20.26.38.164:8200");

    JestClientFactory factory = new JestClientFactory();
    factory.setHttpClientConfig(new HttpClientConfig.Builder(urlList)
        // .Builder("http://20.26.38.165:8200")
        .gson(new GsonBuilder().setDateFormat("yyyy-MM-dd'T'hh:mm:ss").create()).connTimeout(1500)
        .readTimeout(3000).multiThreaded(true).build());
    return factory.getObject();
  }

  /**
   * 创建索引
   */
  public boolean createIndex(JestClient jestClient, String indexName) throws Exception {
    JestResult jr = jestClient.execute(new CreateIndex.Builder(indexName).build());
    return jr.isSucceeded();
  }

  public boolean createIndexAlias(JestClient jestClient, List<String> indices, String alias)
      throws Exception {

    ModifyAliases modifyAliases = new ModifyAliases.Builder(
        new AddAliasMapping.Builder((indices), alias).build()
    ).build();
    JestResult jr = jestClient.execute(modifyAliases);
    return jr.isSucceeded();
  }

  /**
   * Put映射
   */
  public boolean createIndexMapping(JestClient jestClient, String indexName, String typeName,
      String source) throws Exception {

    PutMapping putMapping = new PutMapping.Builder(indexName, typeName, source).build();
    JestResult jr = jestClient.execute(putMapping);
    return jr.isSucceeded();
  }

  /**
   * Get映射
   */
  public String getIndexMapping(JestClient jestClient, String indexName, String typeName)
      throws Exception {

    GetMapping getMapping = new GetMapping.Builder().addIndex(indexName).addType(typeName).build();
    JestResult jr = jestClient.execute(getMapping);
    return jr.getJsonString();
  }

  /**
   * 索引文档
   */
  public boolean index(JestClient jestClient, String indexName, String typeName, List<Object> objs)
      throws Exception {

    Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(typeName);
    for (Object obj : objs) {
      Index index = new Index.Builder(obj).build();
      bulk.addAction(index);
    }
    BulkResult br = jestClient.execute(bulk.build());
    return br.isSucceeded();
  }

  /**
   * 搜索文档
   */
  public SearchResult search(JestClient jestClient, String indexName, String typeName, String query)
      throws Exception {

    Search search = new Search.Builder(query).addIndex(indexName).addType(typeName).build();
    return jestClient.execute(search);
  }

  /**
   * Count文档
   */
  public Double count(JestClient jestClient, String indexName, String typeName, String query)
      throws Exception {

    Count count = new Count.Builder().addIndex(indexName).addType(typeName).query(query).build();
    CountResult results = jestClient.execute(count);
    return results.getCount();
  }

  /**
   * Get文档
   */
  public JestResult get(JestClient jestClient, String indexName, String typeName, String id)
      throws Exception {

    Get get = new Get.Builder(indexName, id).type(typeName).build();
    return jestClient.execute(get);
  }

  /**
   * Delete索引
   */
  public boolean delete(JestClient jestClient, String indexName) throws Exception {

    JestResult jr = jestClient.execute(new DeleteIndex.Builder(indexName).build());
    return jr.isSucceeded();
  }

  /**
   * Delete文档
   */
  public boolean delete(JestClient jestClient, String indexName, String typeName, String id)
      throws Exception {

    DocumentResult dr = jestClient
        .execute(new Delete.Builder(id).index(indexName).type(typeName).build());
    return dr.isSucceeded();
  }

  /**
   * 关闭JestClient客户端
   */
  public void closeJestClient(JestClient jestClient) throws Exception {

    if (jestClient != null) {
      jestClient.shutdownClient();
    }
  }
}
