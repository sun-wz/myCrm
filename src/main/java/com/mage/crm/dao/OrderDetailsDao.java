package com.mage.crm.dao;

import com.mage.crm.vo.OrderDetails;

import java.util.List;

public interface OrderDetailsDao {
    int delete(Integer id);

    int save(OrderDetails record);

    int saveSte(OrderDetails record);

    OrderDetails get(Integer id);

    int updateSte(OrderDetails record);

    int update(OrderDetails record);

    List<OrderDetails> queryOrderDetailsByOrderId(Integer orderId);
}