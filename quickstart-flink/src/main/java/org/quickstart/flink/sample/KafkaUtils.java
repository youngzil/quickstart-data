package org.quickstart.flink.sample;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaUtils {
       private static final String broker_list = "localhost:9092";
    // private static final String broker_list = "zzy:9092";
    //flink 读取kafka写入mysql exactly-once 的topic
    private static final String topic_ExactlyOnce = "mysql-exactly-Once-4";

    public static void writeToKafka2() throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", broker_list);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //        KafkaProducer producer = new KafkaProducer<String, String>(props);//老版本producer已废弃
        Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer<>(props);

        try {
            for (int i = 1; i <= 20; i++) {
                MysqlExactlyOncePOJO mysqlExactlyOnce = new MysqlExactlyOncePOJO(String.valueOf(i));
                ProducerRecord record = new ProducerRecord<String, String>(topic_ExactlyOnce, null, null, JSON.toJSONString(mysqlExactlyOnce));
                producer.send(record);
                System.out.println("发送数据: " + JSON.toJSONString(mysqlExactlyOnce));
                Thread.sleep(1000);
            }
        }catch (Exception e){

        }

        producer.flush();
    }

    public static void main(String[] args) throws InterruptedException {
        writeToKafka2();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MysqlExactlyOncePOJO {
        private String value;
    }
}



