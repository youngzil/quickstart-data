package org.quickstart.jstorm.example4.demo;

import org.quickstart.jstorm.example4.utils.Logging;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.TopologyBuilder;

/**
 * Created by Administrator on 2017/6/2.
 */
public class GalTopology implements ITopology {

    @Override
    public void start(String topologyName) {
        try {
            this.doStart(topologyName);
        } catch (Exception e) {
            Logging.error(e);
        }
    }

    private void doStart(String topologyName) throws AlreadyAliveException, InvalidTopologyException {

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("testspout", new GalSplout(), 1);
        builder.setBolt("testboltA", new GalBlot(), 1).shuffleGrouping("testspout");
        builder.setBolt("testboltA0", new GalSubBlot("GAL_DEMO_0"), 1).shuffleGrouping("testboltA", "0");
        builder.setBolt("testboltA1", new GalSubBlot("GAL_DEMO_1"), 1).shuffleGrouping("testboltA", "1");
        builder.setBolt("testboltA2", new GalSubBlot("GAL_DEMO_2"), 1).shuffleGrouping("testboltA", "2");

        Config config = new Config();
        config.setNumAckers(3);
        config.setNumWorkers(5);
        StormTopology stormTopology = builder.createTopology();
        StormSubmitter.submitTopology(topologyName, config, stormTopology);
        Logging.info("storm cluster will start");

    }

}
