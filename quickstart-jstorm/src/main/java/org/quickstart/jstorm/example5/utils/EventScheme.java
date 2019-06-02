package org.quickstart.jstorm.example5.utils;

import java.util.List;

import org.quickstart.jstorm.example5.dto.StockRealTimeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

/**
 * Created by tonye0115 on 2016/12/7.
 */
public class EventScheme implements Scheme {

  private static final Logger log = LoggerFactory.getLogger(EventScheme.class);

  @Override
  public List<Object> deserialize(byte[] bytes) {
    try {
      String msg = new String(bytes, "UTF-8");
      log.info("#### msg:" + msg);
      StockRealTimeEvent stockRealTimeEvent = JSONObject.parseObject(msg, StockRealTimeEvent.class);
      return new Values(stockRealTimeEvent);
    } catch (Exception e) {
      e.printStackTrace();
      log.error("Exception:", e);
    }
    return null;
  }

  @Override
  public Fields getOutputFields() {
    return new Fields("msg");
  }
}
