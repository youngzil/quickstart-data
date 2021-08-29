elasticsearch-head是一个elasticsearch的集群管理工具，它是完全由html5编写的独立网页程序，你可以通过插件把它集成到es。或直接下载源码，在本地打开index.html运行它。


[elasticsearch-head Github](https://github.com/mobz/elasticsearch-head)


插件安装方法：
1.elasticsearch/bin/plugin -install mobz/elasticsearch-head
2.运行es
3.打开http://localhost:9200/_plugin/head/

不想通过插件集成到es的话就可以直接在git上下载源码到本地运行。

在地址栏输入es服务器的ip地址和端口点connect就可以连接到集群。下面是连接后的视图。这是主界面，在这里可以看到es集群的基本信息（如：节点情况，索引情况）。

bigdesk是elasticsearch的一个集群监控工具，可以通过它来查看es集群的各种状态，如：cpu、内存使用情况，索引数据、搜索情况，http连接数等。项目git地址： https://github.com/lukas-vlcek/bigdesk。和head一样，它也是个独立的网页程序，使用方式和head一样。
插件安装运行：
1.bin/plugin -install lukas-vlcek/bigdesk
2.运行es
3.打开http://localhost:9200/_plugin/bigdesk/
当然，也可以直接下载源码运行index.html


参考
https://my.oschina.net/sunzy/blog/195332