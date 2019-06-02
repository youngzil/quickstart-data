/**
 * 项目名称：quickstart-elasticsearch 文件名：Category.java 版本信息： 日期：2018年8月21日 Copyright asiainfo
 * Corporation 2018 版权所有 *
 */
package org.quickstart.elasticsearch.transport.v5.example2;

/**
 * Category
 *
 * @author：yangzl@asiainfo.com
 * @2018年8月21日 下午2:32:49
 * @since 1.0
 */
public class Category {

  private String productPlanId;
  private String id;
  private String name;
  private String parentId;

  public String getProductPlanId() {
    return productPlanId;
  }

  public void setProductPlanId(String productPlanId) {
    this.productPlanId = productPlanId;
  }

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

  public String getParentId() {
    return parentId;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public Category() {
    super();
  }

  public Category(String productPlanId, String id, String name, String parentId) {
    super();
    this.productPlanId = productPlanId;
    this.id = id;
    this.name = name;
    this.parentId = parentId;
  }

  @Override
  public String toString() {
    return "Category [productPlanId=" + productPlanId + ", id=" + id + ", name=" + name
        + ", parentId=" + parentId + "]";
  }

}
