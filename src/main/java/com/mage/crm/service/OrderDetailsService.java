package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.OrderDetailsDao;
import com.mage.crm.query.OrderDetailQuery;
import com.mage.crm.vo.OrderDetails;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderDetailsService {
    @Resource
    private OrderDetailsDao orderDetailsDao;

    public Map<String,Object> queryOrderDetailsByOrderId(OrderDetailQuery orderDetailQuery) {
        PageHelper.startPage(orderDetailQuery.getPage(),orderDetailQuery.getRows());
        List<OrderDetails> orderDetailsList = orderDetailsDao.queryOrderDetailsByOrderId(orderDetailQuery.getOrderId());
        PageInfo<OrderDetails> orderDetailPageInfo = new PageInfo<>(orderDetailsList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",orderDetailPageInfo.getTotal());
        map.put("rows",orderDetailPageInfo.getList());
        return map;
    }


}
