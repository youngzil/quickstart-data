package org.quickstart.jstorm.example2.model;

/**
 * Created by Thanos Yu on 2017/08/26.
 */
public class O2OModule {

    /**
     * 地址，唯一的
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 标题
     */
    private String title;

    /**
     * 排序
     */
    private Integer orderNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
