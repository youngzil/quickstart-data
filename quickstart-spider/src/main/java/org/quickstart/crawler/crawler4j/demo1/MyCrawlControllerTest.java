/**
 * 项目名称：quickstart-crawler 文件名：Controller.java 版本信息： 日期：2017年7月21日 Copyright yangzl Corporation
 * 2017 版权所有 *
 */
package org.quickstart.crawler.crawler4j.demo1;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Controller 爬虫控制器
 *
 * @version 2.0
 * @author：youngzil@163.com
 * @2017年7月21日 下午9:23:47
 */
public class MyCrawlControllerTest {

  public static void main(String[] args) throws Exception {
    String crawlStorageFolder = "/Users/yangzl/crawler"; // 定义爬虫数据存储位置
    int numberOfCrawlers = 7; // 定义7个爬虫，也就是7个线程

    CrawlConfig config = new CrawlConfig(); // 定义爬虫配置
    config.setCrawlStorageFolder(crawlStorageFolder); // 设置爬虫文件存储位置

    /*
     * 实例化爬虫控制器
     */
    PageFetcher pageFetcher = new PageFetcher(config); // 实例化页面获取器
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig(); // 实例化爬虫机器人配置 比如可以设置 user-agent

    // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    // 实例化爬虫控制器
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    /**
     * 配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
     */
    controller.addSeed("http://www.java1234.com/");
    controller.addSeed("http://www.java1234.com/a/kaiyuan/");
    controller.addSeed("http://www.java1234.com/a/bysj/");

    /**
     * 启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置
     */
    controller.start(MyWebCrawler.class, numberOfCrawlers);
  }
}
