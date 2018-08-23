/**
 * 项目名称：quickstart-elasticsearch 
 * 文件名：FieldInfo.java
 * 版本信息：
 * 日期：2018年8月21日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.example;

/**
 * FieldInfo
 * 
 * @author：yangzl@asiainfo.com
 * @2018年8月21日 下午1:15:16
 * @since 1.0
 */
public class FieldInfo {

    private String field;
    private String type;

    private Integer participle;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getParticiple() {
        return participle;
    }

    public void setParticiple(Integer participle) {
        this.participle = participle;
    }
    
    

}
