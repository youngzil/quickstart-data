package org.quickstart.jstorm.example;

import org.quickstart.jstorm.example.bolt.SplitSentenceBolt;
import org.quickstart.jstorm.example.bolt.WordCountBolt;
import org.quickstart.jstorm.example.spout.GenRandomSentenceSpout;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Created by hzcortex on 16-7-29.
 */
public class TopologyMain {

    public static void main(String[] args) throws Exception {
        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("send", new GenRandomSentenceSpout());

        builder.setBolt("split", new SplitSentenceBolt()).shuffleGrouping("send");
        builder.setBolt("count", new WordCountBolt()).fieldsGrouping("split", new Fields("word"));

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(5);
        conf.setNumAckers(3);
        // conf.setMaxTaskParallelism(3);
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("word_count", conf, builder.createTopology());
        System.out.println("done--------------------------------------");
        // cluster.shutdown();
    }

}
