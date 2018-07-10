package org.quickstart.jstorm.example2.topology;

import org.quickstart.jstorm.example2.bolt.O2OModuleBolt;
import org.quickstart.jstorm.example2.spout.O2OModuleSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;

/**
 * Created by Thanos Yu on 2017/8/26.
 */
public class O2OModuleTopology {

    private static TopologyBuilder builder = new TopologyBuilder();

    public static void main(String[] args) throws Exception {

        Config config = new Config();

        // 设置喷发节点并分配并发数，该并发数将会控制该对象在集群中的线程数。表示“spout”的线程数为2，任务数为4，即一个线程运行两个任务
        builder.setSpout("O2OModuleSpout", new O2OModuleSpout(), 2).setNumTasks(4);

        // 设置数据处理节点并分配并发数。指定该节点接收喷发节点的策略为随机方式。总线程数Spout+Bolt=4
        builder.setBolt("O2OModuleBolt", new O2OModuleBolt(), 2).shuffleGrouping("O2OModuleSpout").setNumTasks(4);

        // 这里是本地模式下运行的启动代码。
        // 该选项设置了一个组件最多能够分配的 executor 数（线程数上限），一般用于在本地模式运行拓扑时测试分配线程的数量限制
        config.setMaxTaskParallelism(2);

        // Worker的数量在Config中设置，下图代码中的部分表示Worker数量。本地模式中，Worker数不生效，只会启动一个JVM进行来执行作业。只有在集群模式设置Worker才有效。而且集群模式的时候一定要设置才能体现集群的价值。
        config.setNumWorkers(3);

        // 当设置为true时，每次从Spout或者Bolt发送元组，Storm都会写进日志，这对于调试程序是非常有用的
        config.setDebug(false);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("O2OModule", config, builder.createTopology());

        // StormSubmitter.submitTopology("O2OModuleTopology",config,builder.createTopology());
    }
}
