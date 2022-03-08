package org.quickstart.flink.example;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 */
public class FlinkSubmitter2 {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<Student> record = env.addSource(new StudentSourceFromMysql());
        //对于record可以添加一些处理逻辑
        record.print().setParallelism(2);
        env.execute("Flink Mysql Source");
    }
}
