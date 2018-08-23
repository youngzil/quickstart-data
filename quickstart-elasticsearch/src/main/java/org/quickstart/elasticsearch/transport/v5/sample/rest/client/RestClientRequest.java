/**
 * 项目名称：quickstart-elasticsearch 
 * 文件名：RestClientRequest.java
 * 版本信息：
 * 日期：2018年8月21日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.sample.rest.client;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.junit.Test;
import org.quickstart.elasticsearch.transport.v5.sample.ElasticsearchRestClient;

/**
 * RestClientRequest 
 *  
 * @author：yangzl@asiainfo.com
 * @2018年8月21日 下午7:35:30 
 * @since 1.0
 */
public class RestClientRequest extends ElasticsearchRestClient {
    
    @Test
    public void testSimpleRequest() throws Exception {
        
        //1
        Response response = client.performRequest("GET", "/"); //最简单的发送一个请求
        
        
        //2
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        Response response2 = client.performRequest("GET", "/", params);  //发送一个带参数的请求
        
        
        //3
        Map<String, String> params3 = Collections.emptyMap();
        String jsonString = "{" +
                    "\"user\":\"kimchy\"," +
                    "\"postDate\":\"2013-01-30\"," +
                    "\"message\":\"trying out Elasticsearch\"" +
                "}";
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);// org.apache.http.HttpEntity  为了让Elasticsearch 能够解析,需要设置ContentType。
        Response response3 = client.performRequest("PUT", "/posts/doc/1", params3, entity);
        
        //4
        Map<String, String> params4 = Collections.emptyMap();
        HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory consumerFactory =
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024); //[ org.apache.http.nio.protocol.HttpAsyncResponseConsumer](http://hc.apache.org/httpcomponents-core-ga/httpcore-nio/apidocs/org/apache/http/nio/protocol/HttpAsyncResponseConsumer.html) ,
        Response response4 = client.performRequest("GET", "/posts/_search", params4, null, consumerFactory);
        
        
    }
    
    @Test
    public void testAsyncRequest() throws Exception {
        
        ResponseListener responseListener = new ResponseListener() {
            @Override
            public void onSuccess(Response response) {
                // 请求成功回调
            }

            @Override
            public void onFailure(Exception exception) {
                //请求失败时回调
            }
        };
        
        //1
        client.performRequestAsync("GET", "/", responseListener); //发送异步请求
        
        //2
        Map<String, String> params = Collections.singletonMap("pretty", "true");
        client.performRequestAsync("GET", "/", params, responseListener); // 发送带参数的异步请求
        
        //3
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        HttpEntity entity = new NStringEntity(jsonString, ContentType.APPLICATION_JSON);
        client.performRequestAsync("PUT", "/posts/doc/1", params, entity, responseListener);
        
        
        //4
        HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory consumerFactory =
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024);
        client.performRequestAsync("GET", "/posts/_search", params, null, consumerFactory, responseListener);
        
        
        //5
        final CountDownLatch latch = new CountDownLatch(10);
//        final CountDownLatch latch = new CountDownLatch(documents.length);
        for (int i = 0; i < 10; i++) {
            client.performRequestAsync(
                    "PUT",
                    "/posts/doc/" + i,
                    Collections.<String, String>emptyMap(),
                    //let's assume that the documents are stored in an HttpEntity array
//                    documents[i],
                    new StringEntity(""),
                    new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {

                            latch.countDown();//处理返回响应
                        }

                        @Override
                        public void onFailure(Exception exception) {

                            latch.countDown();//处理失败响应，exception 里带错误码
                        }
                    }
            );
        }
        latch.await();
        
        
        //6
        Response response = client.performRequest("GET", "/", new BasicHeader("header", "value"));

        
        //7
        Header[] headers = {
                new BasicHeader("header1", "value1"),
                new BasicHeader("header2", "value2")
        };
        client.performRequestAsync("GET", "/", responseListener, headers);

        
    }
    
    @Test
    public void testResponse() throws Exception {
        Response response = client.performRequest("GET", "/");
        RequestLine requestLine = response.getRequestLine(); //请求信息
        HttpHost host = response.getHost(); //返回response host信息
        int statusCode = response.getStatusLine().getStatusCode(); //返回状态行，获取状态码
        Header[] headers = response.getHeaders(); //response headers ，也可以通过名字获取 `getHeader(String)`
        String responseBody = EntityUtils.toString(response.getEntity()); //response  org.apache.http.HttpEntity 对象
    }

}
