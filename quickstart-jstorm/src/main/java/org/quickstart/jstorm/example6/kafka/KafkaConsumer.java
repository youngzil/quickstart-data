package org.quickstart.jstorm.example6.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka消息队列 消费者
 * 
 * @author xiongyan
 * @date 2016年10月13日 下午4:38:01
 */
public class KafkaConsumer {

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    /**
     * kafka 服务器地址
     */
    private String servers;

    /**
     * 消费组
     */
    private String group;

    /**
     * 主题列表
     */
    private List<String> topics;

    /**
     * 消费者
     */
    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> consumer;

    /**
     * 初始化消费者
     */
    public void init() {
        Properties props = new Properties();
        // kafka 服务器地址
        props.put("bootstrap.servers", servers);
        // group
        props.put("group.id", group);
        // consumer向zookeeper提交offset（不建议自动提交）
        props.put("enable.auto.commit", "false");
        // consumer向zookeeper提交offset的频率（单位毫秒，默认60*1000）
        props.put("auto.commit.interval.ms", "1000");
        // 心跳检测时间（单位毫秒，默认6000）
        props.put("session.timeout.ms", "30000");
        // key序列化
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // value序列化
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 初始化kafka连接
        consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<String, String>(props);
        // 订阅topic列表
        consumer.subscribe(topics);

        logger.info("kafka servers【{}】，group【{}】，topics【{}】， kafka消费者初始化成功！", servers, group, topics);
    }

    /**
     * 关闭消费者连接
     */
    public void close() {
        if (null != consumer) {
            consumer.close();
            consumer = null;
            logger.info("kafka servers【{}】，group【{}】，topics【{}】， kafka消费者关闭成功！", servers, group, topics);
        }
    }

    /**
     * 接收消息
     * 
     * @param callback
     */
    public void receive(KafkaCallback callback) {
        // 超时时间
        ConsumerRecords<String, String> records = consumer.poll(100);
        for (ConsumerRecord<String, String> record : records) {
            logger.info("Received message: ({}, {}) at offset({})", record.topic(), record.value(), record.offset());
            callback.receive(record);
        }
        if (!records.isEmpty()) {
            // 手动提交
            consumer.commitSync();
        }
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public static void main(String[] args) {
        final KafkaConsumer consumer = new KafkaConsumer();
        consumer.setServers("192.168.11.101:9092,192.168.12.128:9092,192.168.12.154:9092");
        consumer.setGroup("group");
        consumer.setTopics(Arrays.asList("bolt"));
        consumer.init();

        while (true) {
            consumer.receive(new KafkaCallback() {
                public void receive(ConsumerRecord<String, String> record) {
                    System.out.println(record.topic() + "---" + record.value());
                }
            });
        }
    }
}
