package org.quickstart.jstorm.example6.jstorm.topology;

import java.util.Properties;

import org.quickstart.jstorm.example6.jstorm.bolt.DoubleBolt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;
import storm.kafka.bolt.KafkaBolt;

public class LocalTopology {

  private static final Logger logger = LoggerFactory.getLogger(LocalTopology.class);

  public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
    BrokerHosts brokerHosts = new ZkHosts(
        "192.168.11.101:2181,192.168.12.128:2181,192.168.12.154:2181/kafka");
    SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, "spout", "/kafka", "spout");
    spoutConfig.scheme = new SchemeAsMultiScheme(new MessageScheme());

    TopologyBuilder builder = new TopologyBuilder();
    builder.setSpout("kafka-spout", new KafkaSpout(spoutConfig));
    builder.setBolt("collect-bolt", new DoubleBolt()).shuffleGrouping("kafka-spout");
    builder.setBolt("print-bolt", new KafkaBolt<String, String>()).shuffleGrouping("collect-bolt");

    Config config = new Config();
    Properties props = new Properties();
    props
        .put("metadata.broker.list", "192.168.11.101:9092,192.168.12.128:9092,192.168.12.154:9092");
    props.put("serializer.class", "kafka.serializer.StringEncoder");
    props.put("producer.type", "async");
    props.put("request.required.acks", "1");
    config.put("kafka.broker.properties", props);
    config.put("topic", "bolt");

    // 本地模式
    config.setDebug(true);
    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology("Toplogy-Start", config, builder.createTopology());
    logger.info("Jstorm Toplogy Start");
  }
}
