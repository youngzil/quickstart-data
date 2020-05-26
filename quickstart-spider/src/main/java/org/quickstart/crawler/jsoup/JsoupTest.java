/**
 * 项目名称：quickstart-crawler 文件名：JsoupTest.java 版本信息： 日期：2017年7月21日 Copyright yangzl Corporation
 * 2017 版权所有 *
 */
package org.quickstart.crawler.jsoup;

import static java.util.stream.Collectors.joining;

import cc11001100.ocr.clean.SingleColorFilterClean;
import cc11001100.ocr.split.ImageSplitImpl;
import cc11001100.ocr.util.ImageUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * JsoupTest
 *
 * @version 2.0
 * @author：youngzil@163.com
 * @2017年7月21日 下午8:56:04
 */
@Slf4j
public class JsoupTest {

  static String url2 = "http://www.cnblogs.com/zyw-205520/archive/2012/12/20/2826402.html";
  static String url = "http://www.njhouse.com.cn/2019/spf/sales_detail?PRJ_ID=1643150&prjid=1643150&buildid=566072&dm=01%E5%B9%A2";

  /**
   *
   */
  public static void main(String[] args) throws Exception {

    System.out.println(extractPrice(null));

    fang();

    // TODO Auto-generated method stub
    BolgBody();
    // test();
    // Blog();
    /*
     * Document doc = Jsoup.connect("http://www.oschina.net/")
     * .data("query", "Java") // 请求参数 .userAgent("I ’ m jsoup") // 设置
     * User-Agent .cookie("auth", "token") // 设置 cookie .timeout(3000) //
     * 设置连接超时时间 .post();
     */// 使用 POST 方法访问 URL

    /*
     * // 从文件中加载 HTML 文档 File input = new File("D:/test.html"); Document doc
     * = Jsoup.parse(input,"UTF-8","http://www.oschina.net/");
     */
  }

  /**
   * 获取指定HTML 文档指定的body
   */
  private static void BolgBody() throws IOException {
    // 直接从字符串中输入 HTML 文档
    String html = "<html><head><title> 开源中国社区 </title></head>"
        + "<body><p> 这里是 jsoup 项目的相关文章 </p></body></html>";
    Document doc = Jsoup.parse(html);
    System.out.println(doc.body());

    // 从 URL 直接加载 HTML 文档
    Document doc2 = Jsoup.connect(url).get();
    String title = doc2.body().toString();
    System.out.println(title);
  }

  /**
   * 获取博客上的文章标题和链接
   */
  public static void article() {
    Document doc;
    try {
      doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/").get();
      Elements ListDiv = doc.getElementsByAttributeValue("class", "postTitle");
      for (Element element : ListDiv) {
        Elements links = element.getElementsByTag("a");
        for (Element link : links) {
          String linkHref = link.attr("href");
          String linkText = link.text().trim();
          System.out.println(linkHref);
          System.out.println(linkText);
        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public static void fang() {



    Document doc;
    try {
      doc = Jsoup.connect("http://www.njhouse.com.cn/2019/spf/sales_detail?PRJ_ID=1643150&prjid=1643150&buildid=566072&dm=01%E5%B9%A2").get();

      Elements ListDiv = doc.getElementsByAttributeValue("class", "ck_table");
      for (Element element : ListDiv) {
        Elements links = element.getElementsByTag("tr");
        for (Element link : links) {
          Elements tds = link.getElementsByTag("td");

          Element td1 = tds.get(1);
          Elements as1 = td1.getElementsByTag("a");
          Element info1 = as1.last();
          Elements imgs1 = info1.getElementsByTag("img");
          System.out.println(imgs1.get(0));
          System.out.println(imgs1.get(1));

          for(int i=2;i<tds.size()-1;i++){
            Element td = tds.get(i);
            Elements as = td.getElementsByTag("a");
            if (as.size()==2) {
              Element info = as.last();
              System.out.println(info.text());
              // Elements imgs = info.getElementsByTag("img");
              // String linkHref = link.attr("href");
              // String linkText = link.text().trim();
              // System.out.println(linkHref);
              // System.out.println(linkText);
            }
          }

          Element td6 = tds.get(6);
          Elements as6 = td6.getElementsByTag("a");
          Element info6 = as6.last();
          Elements imgs6 = info6.getElementsByTag("img");
          System.out.println(imgs6.get(0));
          System.out.println(imgs6.get(1));

        }
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println("ssss");

  }

  /**
   * 获取指定博客文章的内容
   */
  public static void Blog() {
    Document doc;
    try {
      doc = Jsoup.connect("http://www.cnblogs.com/zyw-205520/archive/2012/12/20/2826402.html")
          .get();
      Elements ListDiv = doc.getElementsByAttributeValue("class", "postBody");
      for (Element element : ListDiv) {
        System.out.println(element.html());
      }
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  private static SingleColorFilterClean singleColorFilterClean = new SingleColorFilterClean(0XFFA000);
  private static ImageSplitImpl imageSplit = new ImageSplitImpl();
  private static Map<Integer, String> dictionaryMap = new HashMap<>();

  static {
    dictionaryMap.put(-2132100338, "0");
    dictionaryMap.put(-458583857, "1");
    dictionaryMap.put(913575273, "2");
    dictionaryMap.put(803609598, "3");
    dictionaryMap.put(-1845065635, "4");
    dictionaryMap.put(1128997321, "5");
    dictionaryMap.put(-660564186, "6");
    dictionaryMap.put(-1173287820, "7");
    dictionaryMap.put(1872761224, "8");
    dictionaryMap.put(-1739426700, "9");
  }

  private static int extractPrice(JSONObject houseInfo) throws IOException {
    /*JSONArray priceInfo = houseInfo.getJSONObject("data").getJSONArray("price");
    String priceRawImgUrl = "http:" + priceInfo.getString(0);
    System.out.println("priceRawImgUrl: " + priceRawImgUrl);
    JSONArray priceImgCharIndexArray = priceInfo.getJSONArray(2);
    System.out.println("priceImgCharIndexArray: " + priceImgCharIndexArray);*/

    String priceRawImgUrl = "http://www.njhouse.com.cn/2019/common/imgmake?num=1816601531&bg=%23FF0019";
    BufferedImage img = downloadImg(priceRawImgUrl);
    if (img == null) {
      throw new RuntimeException("img download failed, url=" + priceRawImgUrl);
    }
    List<BufferedImage> priceCharImgList = extractNeedCharImg(img, null);
    // List<BufferedImage> priceCharImgList = extractNeedCharImg(img, priceImgCharIndexArray);
    String priceStr = priceCharImgList.stream().map(charImg -> {
      int charImgHashCode = ImageUtil.imageHashCode(charImg);
      return dictionaryMap.get(charImgHashCode);
    }).collect(joining());
    return Integer.parseInt(priceStr);
  }

  // 因为价格通常是4位数，而返回的图片有10位数（0-9），所以第一步就是将价格字符抠出来
  // （或者也可以先全部识别为字符串然后从字符串中按下标选取）
  private static List<BufferedImage> extractNeedCharImg(BufferedImage img, JSONArray charImgIndexArray) {
    List<BufferedImage> allCharImgList = imageSplit.split(singleColorFilterClean.clean(img));
    List<BufferedImage> needCharImg = new ArrayList<>();
    for (int i = 0; i < charImgIndexArray.size(); i++) {
      int index = charImgIndexArray.getInteger(i);
      needCharImg.add(allCharImgList.get(index));
    }
    return needCharImg;
  }

  private static BufferedImage downloadImg(String url) throws IOException {
    byte[] imgBytes = getRequest(null,null);
    // byte[] imgBytes = downloadBytes(url);
    if (imgBytes == null) {
      return null;
    }
    return ImageIO.read(new ByteArrayInputStream(imgBytes));
  }

  private static byte[] downloadBytes(String url) {
    int i=0;
    // for (int i = 0; i < 3; i++) {
      long start = System.currentTimeMillis();
      try {
        Response response= Jsoup.connect(url)
            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36")
            .referrer("http://www.njhouse.com.cn/2019/spf/sales_detail?PRJ_ID=1643150&prjid=1643150&buildid=566072&dm=01%E5%B9%A2")
            .cookie("bdshare_firstime","1584717528264")
            .cookie("PHPSESSID","ba4e2dd709d9e55e337a914bfd08f057")
            .cookie("PHPSESSID_NS_Sig","oenCV6iby28kvVC9")
            .ignoreContentType(true)
            .execute();

        int dd = response.statusCode();
        byte[] responseBody = response.bodyAsBytes();
        long cost = System.currentTimeMillis() - start;
        log.info("request ok, tryTimes={}, url={}, cost={}", i, url, cost);
        return responseBody;
      } catch (Exception e) {
        long cost = System.currentTimeMillis() - start;
        log.info("request failed, tryTimes={}, url={}, cost={}, cause={}", i, url, cost, e.getMessage());
      }
    // }
    return null;
  }


  /**
   * get方法提交
   *
   * @param url
   *            String 访问的URL
   * @param repType
   *            返回类型
   * @return String
   * */
  public static byte[] getRequest(String url, String repType) {

    String priceRawImgUrl = "http://www.njhouse.com.cn/2019/common/imgmake?num=1816601531&bg=%23FF0019";

    url = priceRawImgUrl;
    repType = "image/png";

    String result = "";
    byte[] resByt = null;
    try {
      URL urlObj = new URL(url);
      HttpURLConnection conn = (HttpURLConnection) urlObj
          .openConnection();

      // 连接超时
      conn.setDoInput(true);
      conn.setDoOutput(true);
      conn.setConnectTimeout(25000);

      // 读取超时 --服务器响应比较慢,增大时间
      conn.setReadTimeout(25000);
      conn.setRequestMethod("GET");

      conn.addRequestProperty("Referer", "http://www.njhouse.com.cn/2019/spf/sales_detail?PRJ_ID=1643150&prjid=1643150&buildid=566072&dm=01%E5%B9%A2n");

      conn.addRequestProperty("Accept-Language", "zh-cn");
      conn.addRequestProperty("Content-type", repType);
      conn.addRequestProperty(
          "User-Agent",
          "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; .NET CLR 2.0.50727)");
      conn.connect();

      PrintWriter out = new PrintWriter(new OutputStreamWriter(
          conn.getOutputStream(), "UTF-8"), true);

      if ("image/jpeg".equals(repType)) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage bufImg = ImageIO.read(conn.getInputStream());
        ImageIO.write(bufImg, "jpg", outputStream);
        resByt = outputStream.toByteArray();
        outputStream.close();

      } else {
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream()));

        System.out.println("=============================");
        System.out.println("Contents of get request");
        System.out.println("=============================");
        String lines = null;
        while ((lines = reader.readLine()) != null) {
          System.out.println(lines);
          result += lines;
          result += "\r";
        }
        resByt = result.getBytes();
        reader.close();
      }
      out.print(resByt);
      out.flush();
      out.close();
      // 断开连接
      conn.disconnect();
      System.out.println("=============================");
      System.out.println("Contents of get request ends");
      System.out.println("=============================");
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    return resByt;
  }

}
