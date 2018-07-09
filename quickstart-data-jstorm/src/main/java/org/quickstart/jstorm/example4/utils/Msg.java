package org.quickstart.jstorm.example4.utils;

import java.util.Date;
import java.util.Random;

/**
 * 消息
 */
public class Msg {

    private String id;
    private Date createTime;
    private Object data;

    public Msg() {}

    public Msg(Object data) {
        this.data = data;
        this.id = Utils.UID.ID();
        this.createTime = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class Builder {

        private static Object[] dataArr = {"0", "1", "2"};
        private static Random random = new Random();

        public static Msg get() {
            Msg msg = new Msg(dataArr[random.nextInt(100) % 3]);
            return msg;
        }
    }

    @Override
    public String toString() {
        return "{ id:" + id + ", createTime:" + createTime + ", data:" + data + "}";
    }
}
