package org.quickstart.jstorm.example3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.MessageId;

/**
 * @author GavinCook
 * @@since 1.0.0
 */
public class DemoSpout extends BaseRichSpout {

  private SpoutOutputCollector collector;

  public void open(Map map, TopologyContext topologyContext,
      SpoutOutputCollector spoutOutputCollector) {
    this.collector = spoutOutputCollector;
  }

  public void nextTuple() {
    List<Object> nextDouble = new ArrayList<Object>();
    nextDouble.add(Math.random());
    collector.emit(nextDouble, MessageId.generateId(new Random()));

  }

  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields("demo"));
  }

  @Override
  public void ack(Object msgId) {
    super.ack(msgId);
  }

  @Override
  public void fail(Object msgId) {
    super.fail(msgId);
  }
}
