/**
 * 项目名称：quickstart-elasticsearch 
 * 文件名：ElasticTest.java
 * 版本信息：
 * 日期：2017年11月30日
 * Copyright asiainfo Corporation 2017
 * 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v2;

import java.io.IOException;

import org.elasticsearch.action.get.GetResponse;

/**
 * ElasticTest
 * 
 * @author：yangzl@asiainfo.com
 * @2017年11月30日 下午5:47:10
 * @since 1.0
 */
public class ElasticTest {
    public static void main(String[] args) {
        try {
            EsClient.createIndex("test"); // 创建索引 相当于关系型数据库的 数据库
            EsClient.addType(); // 创建类型，相当于关系型数据库的 表
            EsClient.createData(); // 插入数据，相当于关系数据库的 记录
            GetResponse getResponse = EsClient.get("AVUde1S5xIEMNjXAMyx4"); // 获取数据
            System.out.println(getResponse.getSourceAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
