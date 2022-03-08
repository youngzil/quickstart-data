package org.quickstart.flink.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体类封装
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Entity {
    public String id;
    public String phoneName;
    public String os;
    public String city;
    public String loginTime;
}