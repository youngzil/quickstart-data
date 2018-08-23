/**
 * 项目名称：quickstart-elasticsearch 
 * 文件名：ProductPlan.java
 * 版本信息：
 * 日期：2018年8月21日
 * Copyright asiainfo Corporation 2018
 * 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.example2;

import java.util.List;

/**
 * ProductPlan 
 *  
 * @author：yangzl@asiainfo.com
 * @2018年8月21日 下午2:33:11 
 * @since 1.0
 */
public class ProductPlan {

    private String id;
    private String name;
    private String desc;
    private float price;
    private List<Category> categorys;

    private String productId;
    private String productName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public List<Category> getCategorys() {
        return categorys;
    }
    public void setCategorys(List<Category> categorys) {
        this.categorys = categorys;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }


    public ProductPlan() {
        super();
    }
    public ProductPlan(String id, String name, String desc, float price, List<Category> categorys, String productId,
            String productName) {
        super();
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.categorys = categorys;
        this.productId = productId;
        this.productName = productName;
    }
    @Override
    public String toString() {
        return "ProductPlan [id=" + id + ", name=" + name + ", desc=" + desc + ", price=" + price + ", categorys="
                + categorys + ", productId=" + productId + ", productName=" + productName + "]";
    }



}
