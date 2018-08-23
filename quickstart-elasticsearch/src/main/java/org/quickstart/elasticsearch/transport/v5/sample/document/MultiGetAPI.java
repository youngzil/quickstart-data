package org.quickstart.elasticsearch.transport.v5.sample.document;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * 一次获取多个文档
 * Created by http://quanke.name on 2017/11/15.
 */
public class MultiGetAPI extends ElasticsearchClientBase {


    @Test
    public void testForPrepareMultiGet()  throws Exception{
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "tweet", "1") //一个id的方式
                .add("twitter", "tweet", "2", "3", "4") //多个id的方式
                .add("another", "type", "foo")  //可以从另外一个索引获取
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) { //迭代返回值
            GetResponse response = itemResponse.getResponse();
            if (response != null && response.isExists()) {      //判断是否存在
                String json = response.getSourceAsString(); //_source 字段
                System.out.println("_source 字段:" + json);
            }
        }
    }

}
