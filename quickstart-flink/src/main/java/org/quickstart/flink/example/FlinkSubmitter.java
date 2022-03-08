package org.quickstart.flink.example;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;

import java.util.Properties;

/**
 * create by xiax.xpu on @Date 2019/4/13 14:50
 */
public class FlinkSubmitter {
    public static void main(String[] args) throws Exception {
        //获取运行时环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //设置并行度，为了方便测试，查看消息的顺序，这里设置为1，可以更改为多并行度
        env.setParallelism(1);
        //checkpoint设置
        //每隔10s进行启动一个检查点【设置checkpoint的周期】
        //开启checkpoint，时间间隔为毫秒
        env.enableCheckpointing(500);
        //设置模式为：exactly_one，仅一次语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //确保检查点之间有1s的时间间隔【checkpoint最小间隔】
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        //检查点必须在10s之内完成，或者被丢弃【checkpoint超时时间】
        env.getCheckpointConfig().setCheckpointTimeout(10000);
        //同一时间只允许进行一次检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //表示一旦Flink程序被cancel后，会保留checkpoint数据，以便根据实际需要恢复到指定的checkpoint
        //env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //设置statebackend,将检查点保存在hdfs上面，默认保存在内存中。这里先保存到本地
        // env.setStateBackend(new FsStateBackend("file:///Users/temp/cp/"));

        //kafka配置文件
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        // props.put("zookeeper.connect", "192.168.83.129:2181");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "lengfeng.consumer.group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");  //key 反序列化
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer"); //value 反序列化
        //这里我们使用的是011版本，011 与 09 或者10 的区别在于，011支持Exactly-once语义
        FlinkKafkaConsumer<String> flinkKafkaConsumer = new FlinkKafkaConsumer<>("kafka_flink_mysql",/*String序列*/ new SimpleStringSchema(), props);
        flinkKafkaConsumer.setCommitOffsetsOnCheckpoints(true);
        SingleOutputStreamOperator<Entity> StreamRecord = env.addSource(flinkKafkaConsumer) //
            .map(string -> JSON.parseObject(string, Entity.class)) //
            .setParallelism(1);

        // StreamRecord.addSink(new MysqlSink());
        StreamRecord.addSink(new MySqlTwoPhaseCommitSink());

        env.execute("KafkatoMysql");

    }
}
