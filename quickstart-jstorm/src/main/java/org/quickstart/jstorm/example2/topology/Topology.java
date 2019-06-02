package org.quickstart.jstorm.example2.topology;

import org.quickstart.jstorm.example2.bolt.Bolt;
import org.quickstart.jstorm.example2.spout.Spout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

/**
 * Created by Thanos Yu on 2017/8/16.
 */
public class Topology {

  /**
   * 每一个topology，既可以有多个spout，代表同时从多个数据源接收消息，也可以多个bolt，来执行不同的业务逻辑。
   */

  // 实例化TopologyBuilder类
  private static TopologyBuilder builder = new TopologyBuilder();

  public static void main(String[] args) {

    Config config = new Config();
    // 设置喷发节点并分配并发数，该并发数将会控制该对象在集群中的线程数。表示“spout”的线程数为2，任务数为4，即一个线程运行两个任务
    builder.setSpout("RandomSentence", new Spout(), 2).setNumTasks(4);
    // 设置数据处理节点并分配并发数。指定该节点接收喷发节点的策略为随机方式。总进程数Spout+Bolt=4
    builder.setBolt("WordNormalizer", new Bolt(), 2).shuffleGrouping("RandomSentence")
        .setNumTasks(4);

    // 这里是本地模式下运行的启动代码。
    // 该选项设置了一个组件最多能够分配的 executor 数（线程数上限），一般用于在本地模式运行拓扑时测试分配线程的数量限制
    config.setMaxTaskParallelism(2);
    // 当设置为true时，每次从Spout或者Bolt发送元组，Storm都会写进日志，这对于调试程序是非常有用的
    config.setDebug(false);
    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology("Wordcount", config, builder.createTopology());
    // cluster.shutdown();
  }
}
