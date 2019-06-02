package org.quickstart.jstorm.example4.demo;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Administrator on 2017/6/2.
 */
public class GalLaunch {

  public static void main(String[] args) {
    String topologyName = "topoabc";
    if (args != null && args.length > 0 && StringUtils.isNotEmpty(args[0])) {
      topologyName = args[0];
    }
    ITopology iTopology = new GalTopology();
    iTopology.start(topologyName);
  }

}
