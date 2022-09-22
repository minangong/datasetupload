package com.bdilab.datasetupload.utils.clickhouse;

//import com.baomidou.dynamic.datasource.annotation.DS;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * ClickHouse Jdbc Utils.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/13
 */
@Repository
public class ClickHouseJdbcUtils {
  @Resource(name = "clickHouseJdbcTemplate")
  JdbcTemplate ckJdbcTemplate;

  public List<Map<String, Object>> queryForList(String sql) {
    return ckJdbcTemplate.queryForList(sql);
  }

  public List<String> queryForStrList(String sql) {
    return ckJdbcTemplate.queryForList(sql, String.class);
  }

  public List<Double> queryForDouList(String sql) {
    return ckJdbcTemplate.queryForList(sql, Double.class);
  }

  public Long queryForLong(String sql) {
    return ckJdbcTemplate.queryForObject(sql, Long.class);
  }

  public Double queryForDouble(String sql) {
    return ckJdbcTemplate.queryForObject(sql, Double.class);
  }

  public void execute(String sql) {
    ckJdbcTemplate.execute(sql);
  }

  /**
   * Query for chart.
   */
  public List<?> query(String sql) {
    String limitSql = sql + " limit 1";
    List<Map<String, Object>> mapList = this.queryForList(limitSql);
    Object o = null;
    for (String key : mapList.get(0).keySet()) {
      o = mapList.get(0).get(key);
    }
    assert o != null;
    return ckJdbcTemplate.queryForList(sql, o.getClass());
  }

  public Long getCount(String sql) {
    String countSql = "select count(*) from (" + sql + ")";
    return ckJdbcTemplate.queryForObject(countSql, Long.class);
  }

}
