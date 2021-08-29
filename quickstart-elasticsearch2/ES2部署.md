[下载地址](https://www.elastic.co/cn/downloads/past-releases/elasticsearch-2-4-6)



解压
tar -xzvf elasticsearch-2.4.6.tar.gz

cd elasticsearch-2.4.6


配置文件
vi config/elasticsearch.yml
```aidl
cluster.name: my-application
network.host: 127.0.0.1
http.port: 9200
```


启动
bin/elasticsearch


访问
http://localhost:9200/



新增数据
curl -XPUT localhost:9200/user_idx/type_tags/12  -d  '{"name" : "Mr.YF", "tags" : ["Go","Java","Lua","C++","Tcl","..."]}'

查询数据
http://localhost:9200/user_idx/type_tags/12







参考文章  
[ElasticSearch 2.X教程](https://blog.csdn.net/jiuqiyuliang/category_6200503.html)  
[ES-ElasticSearch 2.4.0端口和安装问题](https://blog.csdn.net/mixiu888/article/details/80698613)  
[分布式搜索elasticsearch配置文件详解](https://blog.csdn.net/laigood/article/details/7421197)  




