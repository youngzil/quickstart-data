关系型数据库和ElasticSearch概念对应关系
Relational DB	ElasticSearch
Database	Indice
Tables	Types
Rows	Documents
Cloumns	Fields

Relational DB -> Databases -> Tables -> Rows -> Columns
Elasticsearch -> Indices   -> Types  -> Documents -> Fields

与ES服务集群交互方式
可以通过两种方式来连接到elasticsearch（简称es）集群，第一种是通过在你的程序中创建一个嵌入es节点（Node），使之成为es集群的一部分，然后通过这个节点来与es集群通信。第二种方式是用TransportClient这个接口和es集群通信。


ElasticSearch基本概念介绍（一）
http://blog.csdn.net/eff666/article/details/52443141


Elasticsearch核心概念 
（1）接近实时（NRT） 
（2）集群（cluster） 
（3）节点（node） 
（4）索引（index） 
（5）类型（type） 
（6）文档（document） 
（7）分片和复制（shards & replicas） 


mapping组成 
一个mapping由一个或多个analyzer组成， 一个analyzer又由一个或多个filter组成的。当ES索引文档的时候，它把字段中的内容传递给相应的analyzer，analyzer再传递给各自的filters。












