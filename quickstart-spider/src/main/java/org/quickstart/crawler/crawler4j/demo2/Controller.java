/**
 * 项目名称：quickstart-crawler 文件名：Controller.java 版本信息： 日期：2017年7月21日 Copyright yangzl Corporation
 * 2017 版权所有 *
 */
package org.quickstart.crawler.crawler4j.demo2;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Controller
 *
 * @version 2.0
 * @author：youngzil@163.com
 * @2017年7月21日 下午10:21:20
 */
public class Controller {

  public static void main(String[] args) throws Exception {
    String crawlStorageFolder = "/Users/yangzl/crawler2";
    int numberOfCrawlers = 7;

    CrawlConfig config = new CrawlConfig();
    config.setCrawlStorageFolder(crawlStorageFolder);

    /*
     * Instantiate the controller for this crawl.
     */
    PageFetcher pageFetcher = new PageFetcher(config);
    RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
    RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
    CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

    /*
     * For each crawl, you need to add some seed urls. These are the first
     * URLs that are fetched and then the crawler starts following links
     * which are found in these pages
     */
    controller.addSeed("http://www.ics.uci.edu/~welling/");
    controller.addSeed("http://www.ics.uci.edu/~lopes/");
    controller.addSeed("http://www.ics.uci.edu/");

    /*
     * Start the crawl. This is a blocking operation, meaning that your code
     * will reach the line after this only when crawling is finished.
     */
    controller.start(MyCrawler.class, numberOfCrawlers);
  }
}
