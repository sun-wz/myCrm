package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.service.CustomerRepriService;
import com.mage.crm.vo.CustomerLoss;
import com.mage.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_repri")
public class CustomerRepriController extends BaseController{

    @Resource
    private CustomerRepriService customerRepriService;

    @RequestMapping("customerReprieveByLossId")
    @ResponseBody
    public Map<String,Object> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery){
        return customerRepriService.customerReprieveByLossId(customerRepriQuery);
    }

    @ResponseBody
    @RequestMapping("insert")
    public MessageModel insertReprive(CustomerReprieve customerReprieve){
        customerRepriService.insert(customerReprieve);
        return new MessageModel("增加暂缓措施成功");
    }
    @ResponseBody
    @RequestMapping("update")
    public MessageModel update(CustomerReprieve customerReprieve){
        customerRepriService.update(customerReprieve);
        return new MessageModel("修改暂缓措施成功");
    }

    @ResponseBody
    @RequestMapping("delete")
    public MessageModel delete(Integer id){
        customerRepriService.delete(id);
        return new MessageModel("删除暂缓措施成功");
    }


}
