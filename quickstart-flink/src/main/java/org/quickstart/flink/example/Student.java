package org.quickstart.flink.example;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 实体类 用于存储数据库中的数据 javabean
 */
@AllArgsConstructor
@Data
public class Student {
    private int stuid;
    private String stuname;
    private String stuaddr;
    private String stusex;
}
