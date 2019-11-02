package com.mage.crm.dao;

import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.vo.SaleChance;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SaleChanceDao {
    List<SaleChance> querySaleChancesByParams(SaleChanceQuery saleChanceQuery);

    int insert(SaleChance saleChance);

    int update(SaleChance saleChance);

    int delete(Integer[] id);

    SaleChance querySaleChancesById(Integer id);

    int updateDevResult(@Param("id") Integer id, @Param("dev") int dev);
}
