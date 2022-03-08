package org.quickstart.flink.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 此类主要检测jdbc 连接是否成功
 */
public class JdbcTest {
    public static void main(String[] args) throws Exception {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.1.12:3306/flinktest";
        String username = "sqoopuser";
        String password = "sqoopuser";
        Connection connection = null;
        Statement statement = null;

        try {
            //加载驱动
            Class.forName(driver);
            //创建连接
            connection = DriverManager.getConnection(url, username, password);
            //获取执行语句
            statement = connection.createStatement();
            //执行查询，获得结果集
            ResultSet resultSet = statement.executeQuery("select stuid,stuname,stuaddr,stusex from student");
            //处理结果集
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt("stuid"), resultSet.getString("stuname").trim(), resultSet.getString("stuaddr").trim(),
                    resultSet.getString("stusex").trim());
                System.out.println(student);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接 释放资源
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                connection.close();
            }
        }

    }
}