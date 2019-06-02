/**
 * 项目名称：quickstart-elasticsearch 文件名：CsdnBlog.java 版本信息： 日期：2018年8月20日 Copyright asiainfo
 * Corporation 2018 版权所有 *
 */
package org.quickstart.elasticsearch.jest.example2;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * CsdnBlog
 *
 * @author：yangzl@asiainfo.com
 * @2018年8月20日 下午11:27:58
 * @since 1.0
 */
public class CsdnBlog {

  private String author;
  private String titile;
  private String content;
  private String date;
  private String view;
  private String tag;

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getTitile() {
    return titile;
  }

  public void setTitile(String titile) {
    this.titile = titile;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getView() {
    return view;
  }

  public void setView(String view) {
    this.view = view;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

}
