/**
 * 项目名称：quickstart-elasticsearch 文件名：CreateMapping.java 版本信息： 日期：2018年8月21日 Copyright asiainfo
 * Corporation 2018 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.sample.mapping;

import java.io.IOException;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * CreateMapping
 *
 * @author：yangzl@asiainfo.com
 * @2018年8月21日 下午2:09:59
 * @since 1.0
 */
public class CreateMapping extends ElasticsearchClientBase {

  @Test
  public void testCreateMapping() throws IOException {
    String index = "test_index";
    String type = "test_type";
    CreateIndexRequestBuilder createIndex = client.admin().indices().prepareCreate(index);
    XContentBuilder mapping = XContentFactory.jsonBuilder()
        .startObject()
        .startObject("properties") //设置之定义字段
        .startObject("name").field("type", "text").field("analyzed", "standard").endObject() //设置分析器
        .startObject("age").field("type", "long").endObject()
        .startObject("class_name").field("type", "keyword").endObject()
        .startObject("birth").field("type", "date").field("format", "yyyy-MM-dd")
        .endObject()//设置Date的格式
        .endObject()
        .endObject();
    createIndex.addMapping(type, mapping);
    CreateIndexResponse res = createIndex.execute().actionGet();
  }


}
