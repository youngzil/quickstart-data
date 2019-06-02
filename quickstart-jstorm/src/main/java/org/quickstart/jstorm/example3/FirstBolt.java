package org.quickstart.jstorm.example3;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

/**
 * @author GavinCook
 * @date 2017-08-20
 * @since 1.0.0
 */
public class FirstBolt extends BaseRichBolt {

  private OutputCollector collector;

  public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
    this.collector = outputCollector;
  }

  public void execute(Tuple tuple) {
    System.out.println("值==" + tuple.getValue(0));
    collector.ack(tuple);
  }

  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

  }
}
