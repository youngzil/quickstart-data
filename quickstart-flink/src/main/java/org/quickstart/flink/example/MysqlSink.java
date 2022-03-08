package org.quickstart.flink.example;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class MysqlSink extends RichSinkFunction<Entity> {
    private PreparedStatement ps = null;
    private Connection connection = null;
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://192.168.0.102:3306/flinktest?useUnicode=true&characterEncoding=UTF-8";
    String username = "sqoopuser";
    String password = "sqoopuser";

    /**
     * open()方法建立连接
     * 这样不用每次 invoke 的时候都要建立连接和释放连接
     *
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        //加载JDBC驱动
        Class.forName(driver);

        System.out.println("MysqlSink open");
        //创建连接
        // connection = DriverManager.getConnection(url, username, password);
        // String sql = "insert into web_access (city,loginTime,os,phoneName) values (?,?,?,?);";
        // ps = connection.prepareStatement(sql);
    }

    /**
     * 每插入一条数据的调用一次invoke
     *
     * @param value
     * @param context
     * @throws Exception
     */
    @Override
    public void invoke(Entity value, Context context) throws Exception {

        // ps.setString(1, value.city);
        // ps.setString(2, value.loginTime);
        // ps.setString(3, value.os);
        // ps.setString(4, value.phoneName);
        System.out.println("id="+value.id+",insert into web_access (city,loginTime,os,phoneName values (" + value.city + "," + value.loginTime + "," + value.os + ","
            + value.phoneName+");");
        // ps.executeUpdate();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

}
