package org.quickstart.elasticsearch.transport.v5.sample.document;

import org.elasticsearch.action.get.GetResponse;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchClientBase;

/**
 * Created by http://quanke.name on 2017/11/10.
 */
public class GetAPI extends ElasticsearchClientBase {


  @Test

  public void testForGetApi() throws Exception {

    /**
     * 根据id查看文档
     */
    GetResponse response = client.prepareGet("twitter", "tweet", "1").get();

//        GetResponse response = client.prepareGet("twitter", "tweet", "1")
//                .setOperationThreaded(false)  //`true` 是在不同的线程里执行此次操作
//                .get();
    if (response.isExists()) {
      System.out.println("GetApi 有此文档：" + response.toString());
    } else {
      System.out.println("GetApi 没有此文档：" + response.toString());
    }

  }

}
