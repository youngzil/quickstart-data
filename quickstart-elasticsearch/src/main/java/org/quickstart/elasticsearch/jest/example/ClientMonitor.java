/**
 * 项目名称：quickstart-elasticsearch 文件名：User.java 版本信息： 日期：2018年8月20日 Copyright yangzl Corporation
 * 2018 版权所有 *
 */
package org.quickstart.elasticsearch.jest.example;

import java.util.Date;

/**
 * User
 *
 * @author：youngzil@163.com
 * @2018年8月20日 下午11:11:47
 * @since 1.0
 */
public class ClientMonitor {

  // @JestId
  // private Integer id;
  private String dockerID;
  private String ip;
  private long sendNum;
  private long failNum;
  private long averageCost;
  private long averageTps;
  private Date collectTime;

  // public Integer getId() {
  // return id;
  // }
  //
  // public void setId(Integer id) {
  // this.id = id;
  // }

  public String getDockerID() {
    return dockerID;
  }

  public void setDockerID(String dockerID) {
    this.dockerID = dockerID;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public long getSendNum() {
    return sendNum;
  }

  public void setSendNum(long sendNum) {
    this.sendNum = sendNum;
  }

  public long getFailNum() {
    return failNum;
  }

  public void setFailNum(long failNum) {
    this.failNum = failNum;
  }

  public long getAverageCost() {
    return averageCost;
  }

  public void setAverageCost(long averageCost) {
    this.averageCost = averageCost;
  }

  public long getAverageTps() {
    return averageTps;
  }

  public void setAverageTps(long averageTps) {
    this.averageTps = averageTps;
  }

  public Date getCollectTime() {
    return collectTime;
  }

  public void setCollectTime(Date collectTime) {
    this.collectTime = collectTime;
  }

  @Override
  public String toString() {
    return "ClientMonitor [dockerID=" + dockerID + ", ip=" + ip + ", sendNum=" + sendNum
        + ", failNum=" + failNum + ", averageCost=" + averageCost + ", averageTps=" + averageTps
        + ", collectTime="
        + collectTime + ", toString()=" + super.toString() + "]";
  }

}
