package org.quickstart.flink.example;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 *
 */
public class StudentSourceFromMysql extends RichSourceFunction<Student> {

    private PreparedStatement ps = null;
    private Connection connection = null;
    String driver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://192.168.1.12:3306/flinktest";
    String username = "sqoopuser";
    String password = "sqoopuser";

    /**
     * open()方法中建立拦截，这样不用每次invoke的时候都需要建立连接和释放连接
     * org.apache.flink.api.common.functions.AbstractRichFunction#open
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // connection = getConnection();
        // String sql = "select * from student;";
        //获取执行语句
        // ps = connection.prepareStatement(sql);
    }

    /**
     * DataStream 调用一次 run()方法执行查询并处理结果集
     * org.apache.flink.streaming.api.functions.source.SourceFunction#run
     */
    @Override
    public void run(SourceContext<Student> ctx) throws Exception {
        /*ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            Student student = new Student(resultSet.getInt("stuid"), resultSet.getString("stuname").trim(), resultSet.getString("stuaddr").trim(),
                resultSet.getString("stusex").trim());
            ctx.collect(student);//发送结果
        }*/

        int i = 0;
        while (true) {
            i++;
            Student student = new Student(i, "stuname" + i, "stuaddr" + i, "stusex" + i);
            ctx.collect(student);//发送结果
        }

    }

    /**
     * 取消一个job时
     * org.apache.flink.streaming.api.functions.source.SourceFunction#cancel()
     */
    @Override
    public void cancel() {
    }

    /**
     * 关闭数据库连接
     * org.apache.flink.api.common.functions.AbstractRichFunction#close()
     */
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

    //获取mysql连接配置
    public Connection getConnection() {
        try {
            //加载驱动
            Class.forName(driver);
            //创建连接
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            System.out.println("********mysql get connection occur exception, msg = " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

}
