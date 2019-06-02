package org.quickstart.jstorm.example2.spout;

import java.util.List;
import java.util.Map;

import org.quickstart.jstorm.example2.sql.MySQL;
import org.quickstart.jstorm.example2.sql.SQLUtil;

/**
 * Created by Thanos Yu on 2017/8/16.
 */

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

// 随机发送一条内置消息，该spout继承BaseRichSpout/IRichSpout类

/**
 *
 */
@SuppressWarnings("serial")
public class Spout extends BaseRichSpout {

  private List<String> titles;
  private SpoutOutputCollector spoutOutputCollector;

  // 进行spout的一些初始化工作，包括参数传递
  public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
    spoutOutputCollector = collector;
    titles = SQLUtil.getList(MySQL.O2O_TITLES);
  }

  /**
   * 进行Tuple处理的主要方法 就是获取下一条消息。执行时，可以理解成JStorm框架会不停地调这个接口，以从数据源拉取数据并往bolt发送数据。
   */
  public void nextTuple() {
    if (titles.size() > 0) {
      for (String title : titles) {
        // 使用emit方法进行Tuple发布，参数用Values申明
        spoutOutputCollector.emit(new Values(title));
        System.out.println("spout---------------------------------------: " + title);
      }
    }
    Utils.sleep(10000);
  }

  // 消息保证机制中的ack确认方法
  public void ack(Object id) {
  }

  // 消息保证机制中的fail确认方法
  public void fail(Object id) {
  }

  // 声明字段
  public void declareOutputFields(OutputFieldsDeclarer declarer) {
    // 默认ID的信息流定义
    declarer.declare(new Fields("Declare Word"));
    // 自定义ID的消息流
    declarer.declareStream("streamId", new Fields("Declare Word"));
  }

}
