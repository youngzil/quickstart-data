package org.quickstart.jstorm.example3;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

/**
 * @author GavinCook
 * @date 2017-08-20
 * @since 1.0.0
 */
public class DemoTopology {

    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        topologyBuilder.setSpout("demoSpout", new DemoSpout()).setNumTasks(1).setMaxTaskParallelism(1);
        topologyBuilder.setBolt("firstBolt", new FirstBolt()).setNumTasks(1).setMaxTaskParallelism(1).localOrShuffleGrouping("demoSpout");

        StormTopology topology = topologyBuilder.createTopology();
        LocalCluster localCluster = new LocalCluster();
        Config conf = new Config();
        conf.setMaxSpoutPending(10);
        conf.setNumAckers(1);
        conf.setNumWorkers(1);
        conf.setDebug(true);
        conf.setMessageTimeoutSecs(5);
        localCluster.submitTopology("demo", conf, topology);
        Utils.sleep(10000000);
    }

}
