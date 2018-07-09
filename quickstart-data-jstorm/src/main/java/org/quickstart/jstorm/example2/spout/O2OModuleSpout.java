package org.quickstart.jstorm.example2.spout;

import java.util.List;
import java.util.Map;

import org.quickstart.jstorm.example2.model.O2OModule;
import org.quickstart.jstorm.example2.sql.MySQL;
import org.quickstart.jstorm.example2.sql.SQLUtil;

/**
 * Created by Thanos Yu on 2017/8/26.
 */

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

/**
 *
 */
public class O2OModuleSpout extends BaseRichSpout {

    private List<O2OModule> modules;
    private SpoutOutputCollector spoutOutputCollector;

    // 进行spout的一些初始化工作，包括参数传递
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        spoutOutputCollector = collector;
    }

    public void nextTuple() {
        modules = SQLUtil.getList(MySQL.O2O_MODULES, O2OModule.class);
        System.out.println();
        if (modules.size() > 0) {
            for (O2OModule module : modules) {
                // 使用emit方法进行Tuple发布，参数用Values申明
                spoutOutputCollector.emit(new Values(module));
                System.out.println("spout---------------------------------------module title: " + module.getTitle());
            }
        }
        System.out.println("--------------------------------spout end: ");
        System.out.println();
        Utils.sleep(2000);
    }

    // 消息保证机制中的ack确认方法
    public void ack(Object id) {}

    // 消息保证机制中的fail确认方法
    public void fail(Object id) {}

    // 声明字段
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 默认ID的信息流定义
        declarer.declare(new Fields("Declare Word"));
        // 自定义ID的消息流
        declarer.declareStream("streamId", new Fields("Declare Word"));
    }

}
