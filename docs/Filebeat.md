

---------------------------------------------------------------------------------------------------------------------------------------------------------
## Filebeat

[Filebeat官网](https://www.elastic.co/cn/beats/filebeat)  
[Filebeat文档](https://www.elastic.co/guide/en/beats/filebeat/current/index.html)  
[]()  
[]()  
[]()  


Filebeat 是一个轻量级的传送器，用于转发和集中日志数据。Filebeat 作为代理安装在您的服务器上，监控您指定的日志文件或位置，收集日志事件，并将它们转发到Elasticsearch或 Logstash以进行索引。

Filebeat 的工作原理如下：当您启动 Filebeat 时，它会启动一个或多个在您为日志数据指定的位置查找的输入。对于 Filebeat 找到的每个日志，Filebeat 都会启动一个收割机。每个收割机读取新内容的单个日志，并将新日志数据发送到 libbeat，后者聚合事件并将聚合数据发送到您为 Filebeat 配置的输出。


Filebeat 是一个弹性节拍。它基于libbeat框架。




MacOS
curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.14.0-darwin-x86_64.tar.gz
tar xzvf filebeat-7.14.0-darwin-x86_64.tar.gz

Brew
brew tap elastic/tap
brew install elastic/tap/filebeat-full


Linux
curl -L -O https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-7.14.0-linux-x86_64.tar.gz
tar xzvf filebeat-7.14.0-linux-x86_64.tar.gz




[Filebeat 采集日志到 Kafka 配置及使用](https://lihuimintu.github.io/2019/08/05/Filebeat-Kafka/)
[Filebeat配置输出到Kafka](https://www.elastic.co/guide/en/beats/filebeat/current/kafka-output.html)


启动
./filebeat -e

---------------------------------------------------------------------------------------------------------------------------------------------------------





