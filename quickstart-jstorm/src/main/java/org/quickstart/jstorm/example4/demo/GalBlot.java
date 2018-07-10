package org.quickstart.jstorm.example4.demo;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.TupleImpl;
import backtype.storm.tuple.Values;

import java.util.Map;

import org.quickstart.jstorm.example4.utils.Logging;
import org.quickstart.jstorm.example4.utils.Msg;

/**
 * Created by Administrator on 2017/6/2.
 */
public class GalBlot extends BaseRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple input) {
        Msg msg = (Msg) input.getValue(0);
        Fields fields = input.getFields();
        String field = fields.get(0);
        Logging.info("GalBlot.execute  msg: " + msg + ", field: " + field);
        this.collector.emit((String) msg.getData(), new Values(msg));
        this.collector.ack(input);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("GAL_DEMO"));
        declarer.declareStream("0", new Fields("MSG_0"));
        declarer.declareStream("1", new Fields("MSG_1"));
        declarer.declareStream("2", new Fields("MSG_2"));
    }

}
