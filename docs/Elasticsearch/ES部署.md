配置说明
cluster.name表示es集群的名称，可以自定义一个自己需要的集群名称
http.port 表示对外提供http服务时的http端口。
network.host 表示本地监听绑定的ip地址，此处为测试环境，直接使用本机的ip地址 127.0.0.1.


es启动监听两个端口，9300和9200
9300端口是使用tcp客户端连接使用的端口；
9200端口是通过http协议连接es使用的端口；


1、部署elasticsearch：ES出于安全原因阻止您以root身份运行它，client版本要和server版本对应上
2、插件elasticsearch-head：直接解压放在${es.home}/pluginshead文件夹下


elasticsearch的config文件夹里面有两个配置文件：
1、elasticsearch.yml，是es的基本配置文件
2、logging.yml是日志配置文件，es也是使用log4j来记录日志的，所以logging.yml里的设置按普通log4j配置文件来设置就行了


只要集群名相同，且机器处于同一局域网同一网段，es会自动去发现其他的节点。
cluster.name ：配置es的集群名称，默认是elasticsearch，不同的集群用名字来区分，es会自动发现在同一网段下的es，配置成相同集群名字的各个节点形成一个集群。如果在同一网段下有多个集群，就可以用这个属性来区分不同的集群。
http.port ：设置对外服务的http端口，默认为9200。不能相同，否则会冲突。

jdk安装：
export JAVA_HOME=/home/msgtest/jdk1.8.0_152
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
export PATH=$JAVA_HOME/bin:$PATH

cat config/elasticsearch.yml 
  bin/elasticsearch & 
 后台运行 bin/elasticsearch -d
 jps

部署：
bin/elasticsearch
默认情况下，Elastic 只允许本机访问，如果需要远程访问，可以修改 Elastic 安装目录的config/elasticsearch.yml文件，去掉network.host的注释，将它的值改成0.0.0.0，然后重新启动 Elastic。
network.host: 0.0.0.0
上面代码中，设成0.0.0.0让任何人都可以访问。线上服务不要这样设置，要设成具体的 IP。


sudo sysctl -w vm.max_map_count=262144
sudo sysctl -w fs.file-max=65536


启动问题：

ES出于安全原因阻止您以root身份运行它：java.lang.RuntimeException: can not run elasticsearch as root

警告提示
 unable to install syscall filter: 
java.lang.UnsupportedOperationException: seccomp unavailable: requires kernel 3.5+ with CONFIG_SECCOMP and CONFIG_SECCOMP_FILTER compiled in
at org.elasticsearch.bootstrap.Seccomp.linuxImpl(Seccomp.java:349) ~[elasticsearch-5.0.0.jar:5.0.0]
at org.elasticsearch.bootstrap.Seccomp.init(Seccomp.java:630) ~[elasticsearch-5.0.0.jar:5.0.0]
报了一大串错误，其实只是一个警告。使用心得linux版本，就不会出现此类问题了。

ERROR: bootstrap checks failed
max file descriptors [4096] for elasticsearch process likely too low, 
increase to at least [65536]
max number of threads [1024] for user [lishang] likely too low,
 increase to at least [2048]
max virtual memory areas vm.max_map_count [65530] likely too low, 
increase to at least [262144]
解决：切换到root用户，编辑limits.conf 添加类似如下内容
vi /etc/security/limits.conf
添加如下内容:
* soft nofile 65536
* hard nofile 131072
* soft nproc 2048
* hard nproc 4096
对于第二条错误同意需要切换到root用户，进入limits.d目录下修改配置文件。
vi /etc/security/limits.d/90-nproc.conf
修改如下内容：
* soft nproc 1024
#修改为
* soft nproc 2048
第三条错误需要切换到root用户修改配置sysctl.conf
vi /etc/sysctl.conf
添加下面配置：
vm.max_map_count=655360
并执行命令：
sysctl -p
然后，重新启动elasticsearch，即可启动成功。


**1、Java HotSpot(TM) 64-Bit Server VM warning: INFO: os::commit_memory(0x0000000085330000, 2060255232, 0) failed; error='Cannot allocate memory' (errno=12)**
由于elasticsearch5.0默认分配jvm空间大小为2g，修改jvm空间分配
# vim config/jvm.options  
-Xms2g  
-Xmx2g  
修改为  
-Xms512m  
-Xmx512m

2、max number of threads [1024] for user [elasticsearch] is too low, increase to at least [2048]修改 /etc/security/limits.d/90-nproc.conf
*          soft    nproc     1024
*          soft    nproc     2048

3、max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
修改/etc/sysctl.conf配置文件，
cat /etc/sysctl.conf | grep vm.max_map_countvm.max_map_count=262144
如果不存在则添加
echo "vm.max_map_count=262144" >>/etc/sysctl.conf

4、max file descriptors [65535] for elasticsearch process likely too low, increase to at least [65536]
需要执行下面的命令
ulimit -n 65536




