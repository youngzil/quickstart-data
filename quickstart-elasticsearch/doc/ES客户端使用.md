https://www.elastic.co/guide/en/elasticsearch/client/index.html
一、ES Client 简介
Elasticsearch Java API 客户端连接
1、一个是TransportClient，
2、一个是NodeClient，
3、还有一个XPackTransportClient


TransportClient：
作为一个外部访问者，请求ES的集群，对于集群而言，它是一个外部因素。

NodeClient
作为ES集群的一个节点，它是ES中的一环，其他的节点对它是感知的。

XPackTransportClient：
服务安装了 x-pack 插件



二、Java REST Client介绍
ES提供了两个JAVA REST client 版本
1、Java Low Level REST Client: 低级别的REST客户端，通过http与集群交互，用户需自己编组请求JSON串，及解析响应JSON串。兼容所有ES版本。
2、Java High Level REST Client: 高级别的REST客户端，基于低级别的REST客户端，增加了编组请求JSON串、解析响应JSON串等相关api。使用的版本需要保持和ES服务端的版本一致，否则会有版本问题。



三：Jest客户端
它是Elasticsearch的Java HTTP Rest客户端。
https://github.com/searchbox-io/Jest



四：SpringBoot的elasticsearch支持：spring-boot-starter-data-elasticsearch





es启动监听两个端口，9300和9200
9300端口是使用tcp客户端连接使用的端口；
9200端口是通过http协议连接es使用的端口；



参考文章
https://blog.csdn.net/u011781521/article/category/7096008



TransportClient参考
http://quanke.name/categories/es/
http://woquanke.com/categories/es/
https://blog.csdn.net/g1969119894/article/details/80169055
https://blog.csdn.net/moxiong3212/article/category/7449595
http://soledede.iteye.com/category/292042
http://www.opscoder.info/es_javaclient.html


NodeClient参考
https://endymecy.gitbooks.io/elasticsearch-guide-chinese/content/java-api/client.html



Java REST Client参考
https://quanke.gitbooks.io/elasticsearch-java-rest/
https://meta.tn/a/5bfd35ebe73b082dba233783da1a677c9e8498bbdf39423f62f2f606f4ffb864


Jest参考




综合
https://www.ezlippi.com/blog/2017/03/elasticsearch-java-client.html
https://www.cnblogs.com/leeSmall/p/9218779.html
http://www.gaowm.com/2018/02/06/Elasticsearch-%E4%BA%94-java%E5%AE%A2%E6%88%B7%E7%AB%AF%E4%BB%8B%E7%BB%8D/
http://www.person168.com/?p=252
客户端各种语言汇总：http://www.searchtech.pro/elasticsearch-clients










