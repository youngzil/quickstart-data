package org.quickstart.jstorm.example5.bolt;

import java.util.Map;

import org.quickstart.jstorm.example5.dto.ResultStock;
import org.quickstart.jstorm.example5.dto.StockRealTimeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

/**
 * Created by tonye0115 on 2016/12/8.
 */
public class StockStrategyBolt2 extends BaseBasicBolt {

  private BasicOutputCollector basicOutputCollector;
  private EPServiceProvider epService;

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public void prepare(Map stormConf, TopologyContext context) {
    log.info("--------------------股票策略2(大买盘)初始化....");
    Configuration configuration = new Configuration();
    configuration.addEventType("StockRealTimeEvent", StockRealTimeEvent.class.getName());
    epService = EPServiceProviderManager.getProvider("strategy2", configuration);
    EPStatement stmt = epService.getEPAdministrator()
        .createEPL("select * from StockRealTimeEvent where "
            + "(buyCount5+buyCount4+buyCount3+buyCount2+buyCount1)*100"
            + ">=(sellCount5+sellCount4+sellCount3+sellCount2+sellCount1)");
    stmt.addListener(new UpdateListener() {
      @Override
      public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        if (newEvents != null) {
          EventBean theEvent = newEvents[0];
          StockRealTimeEvent stockRTEvent = (StockRealTimeEvent) theEvent.getUnderlying();
          log.info("--------股票策略2(大买盘)选出股票：" + stockRTEvent.getStockCode() + " 最新价格：" + stockRTEvent
              .getNewPrice());
          basicOutputCollector.emit(new Values(
              new ResultStock(stockRTEvent.getStockCode(), stockRTEvent.getNewPrice(), 2)));
        }
      }
    });

  }

  @Override
  public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
    this.basicOutputCollector = basicOutputCollector;
    StockRealTimeEvent stockRealTimeEvent = (StockRealTimeEvent) tuple.getValue(0);
    log.info("策略2(大买盘) ===> Esper:" + stockRealTimeEvent);
    epService.getEPRuntime().sendEvent(stockRealTimeEvent);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    outputFieldsDeclarer.declare(new Fields("StockStrageBolt2"));
  }

  @Override
  public void cleanup() {
    if (epService.isDestroyed()) {
      epService.destroy();
    }
  }
}
