package org.quickstart.jstorm.example2.sql;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;

import java.util.List;

/**
 * Created by Thanos Yu on 2017/8/25.
 */
public class SQLUtil {

  private static QueryRunner query;

  public static <T> List<T> getList(String sql, Object... params) {
    try {
      query = new QueryRunner(ConnectionPool.getInstance().getDataSource());
      List<T> r = query.query(sql, new ColumnListHandler<T>(), params);
      return r;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static <T> List<T> getList(String sql, Class clazz, Object... params) {
    try {
      query = new QueryRunner(ConnectionPool.getInstance().getDataSource());
      return query.query(sql, new BeanListHandler<T>(clazz), params);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
