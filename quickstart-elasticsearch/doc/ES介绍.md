关系型数据库和ElasticSearch概念对应关系
Relational DB	ElasticSearch
Database		Indice
Tables	Types
Rows		Documents
Cloumns	Fields

Relational DB -> Databases -> Tables -> Rows -> Columns
Elasticsearch -> Indices   -> Types  -> Documents -> Fields

与ES服务集群交互方式
可以通过两种方式来连接到elasticsearch（简称es）集群，第一种是通过在你的程序中创建一个嵌入es节点（Node），使之成为es集群的一部分，然后通过这个节点来与es集群通信。第二种方式是用TransportClient这个接口和es集群通信。




ES核心概念
1）Cluster：集群。
ES可以作为一个独立的单个搜索服务器。不过，为了处理大型数据集，实现容错和高可用性，ES可以运行在许多互相合作的服务器上。这些服务器的集合称为集群。

2）Node：节点。
形成集群的每个服务器称为节点。

3）Shard：分片。
当有大量的文档时，由于内存的限制、磁盘处理能力不足、无法足够快的响应客户端的请求等，一个节点可能不够。这种情况下，数据可以分为较小的分片。每个分片放到不同的服务器上。 
当你查询的索引分布在多个分片上时，ES会把查询发送给每个相关的分片，并将结果组合在一起，而应用程序并不知道分片的存在。即：这个过程对用户来说是透明的。

4）Replia：副本。



Elasticsearch核心概念 
（1）接近实时（NRT） 
（2）集群（cluster） 
（3）节点（node） 
（4）索引（index） 
（5）类型（type） 
（6）文档（document） 
（7）分片和复制（shards & replicas） 


集群cluster、节点node、分片shard、复制Replia
索引index、类型Type、文档Document、Field



（1）关系型数据库中的数据库（DataBase），等价于ES中的索引（Index） 
（2）一个数据库下面有N张表（Table），等价于1个索引Index下面有N多类型（Type）， 
（3）一个数据库表（Table）下的数据由多行（ROW）多列（column，属性）组成，等价于1个Type由多个文档（Document）和多Field组成。 
（4）在一个关系型数据库里面，schema定义了表、每个表的字段，还有表和字段之间的关系。 与之对应的，在ES中：Mapping定义索引下的Type的字段处理规则，即索引如何建立、索引类型、是否保存原始索引JSON文档、是否压缩原始JSON文档、是否需要分词处理、如何进行分词处理等。 
（5）在数据库中的增insert、删delete、改update、查search操作等价于ES中的增PUT/POST、删Delete、改_update、查GET.


mapping组成 
一个mapping由一个或多个analyzer组成， 一个analyzer又由一个或多个filter组成的。当ES索引文档的时候，它把字段中的内容传递给相应的analyzer，analyzer再传递给各自的filters。


es启动监听两个端口，9300和9200
9300端口是使用tcp客户端连接使用的端口；
9200端口是通过http协议连接es使用的端口；



节点通信有5种类型：Recovery Bulk Reg State Ping

网络层  
Elasticsearch 的网络层抽象很值得借鉴。它抽象出一个 Transport 层，同时兼有client和server功能，server端接收其他节点的连接，client维持和其他节点的连接，承担了节点之间请求转发的功能。Elasticsearch 为了避免传输流量比较大的操作堵塞连接，所以会按照优先级创建多个连接，称为channel。

recovery: 2个channel专门用做恢复数据。如果为了避免恢复数据时将带宽占满，还可以设置恢复数据时的网络传输速度。
bulk: 3个channel用来传输批量请求等基本比较低的请求。
regular: 6个channel用来传输通用正常的请求，中等级别。
state: 1个channel保留给集群状态相关的操作，比如集群状态变更的传输，高级别。
ping: 1个channel专门用来ping，进行故障检测。

每个节点默认都会创建13个到其他节点的连接，并且节点之间是互相连接的，每增加一个节点，该节点会到每个节点创建13个连接，而其他每个节点也会创建13个连回来的连接。

SniffNodesSampler：client会主动发现集群里的其他节点，会创建fully connect(每两个节点间建立5种连接)。这样即使手动添加的机器退服，transportClient还可以向其它集群节点发送请求。另外还支持负载均衡，每次请求都会随机访问一个集群节点。

SimpleNodeSampler：这里创建的都是light connect（简单的ping和查询连接） ,只能在指定的节点间负载均衡，而且如果所有手动添加的机器退服，trnasportClient将无法连接集群进行操作。

注意：在ES7中不建议使用Transport Client,在ES8中去除Transport Client用Java High Level REST Client代替

访问远端集群，尽量以轮询方式访问目标节点

 

2.如何选择JAVA客户端
如果要将应用程序和 Elasticsearch 集群进行解耦，传输客户端是一个理想的选择。例如，如果您的应用程序需要快速的创建和销毁到集群的连接，传输客户端比节点客户端”轻”，因为它不是一个集群的一部分。类似地，如果您需要创建成千上万的连接，你不想有成千上万节点加入集群。传输客户端（ TC ）将是一个更好的选择。
另一方面，如果你只需要有少数的、长期持久的对象连接到集群，客户端节点可以更高效，因为它知道集群的布局。但是它会使你的应用程序和集群耦合在一起，所以从防火墙的角度，它可能会构成问题。



ElasticSearch基本概念介绍（一）
http://blog.csdn.net/eff666/article/details/52443141
https://blog.csdn.net/laoyang360/article/details/52244917



