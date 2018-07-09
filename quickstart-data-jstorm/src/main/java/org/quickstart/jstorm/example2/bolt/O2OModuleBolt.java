package org.quickstart.jstorm.example2.bolt;

import org.quickstart.jstorm.example2.model.O2OModule;

/**
 * Created by Thanos Yu on 2017/8/16.
 */

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class O2OModuleBolt extends BaseBasicBolt {

    // execute这个接口就是用户用来处理业务逻辑的地方
    public void execute(Tuple tuple, BasicOutputCollector collector) {
        try {
            O2OModule module = (O2OModule) tuple.getValue(0);
            if (module != null) {
                System.out.println("bout---------------------------------------module title: " + module.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // declarer.declare(new Fields("Declare Word"));
    }
}
