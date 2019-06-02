package org.quickstart.jstorm.example2.bolt;

/**
 * Created by Thanos Yu on 2017/8/16.
 */

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class Bolt extends BaseBasicBolt {

  /**
   * execute这个接口就是用户用来处理业务逻辑的地方
   */
  public void execute(Tuple tuple, BasicOutputCollector collector) {
    try {
      String title = (String) tuple.getValue(0);
      if (title != null) {
        System.out.println("bout---------------------------------------: " + title);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // declarer.declare(new Fields("Declare Word"));
  }
}
