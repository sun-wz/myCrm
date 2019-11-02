package com.mage.crm.controller;

import com.mage.crm.RequestPermission;
import com.mage.crm.base.BaseController;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.SaleChanceQuery;
import com.mage.crm.service.SaleChanceService;
import com.mage.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    @RequestMapping("index/{id}")
    public String index(@PathVariable("id") String id) {
        switch (id) {
            case "1":
                return "sale_chance";
            case "2":
                return "cus_dev_plan";
            default:
                return "error";
        }
    }

    @RequestMapping("querySaleChancesByParams")
    @ResponseBody
    public Map<String, Object> querySaleChancesByParams(SaleChanceQuery saleChanceQuery){
        return saleChanceService.querySaleChancesByParams(saleChanceQuery);
    }


    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(SaleChance saleChance){
        saleChanceService.insert(saleChance);
        return new MessageModel("营销机会插入成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public MessageModel update(SaleChance saleChance){
        saleChanceService.update(saleChance);
        return new MessageModel("营销机会更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    @RequestPermission(aclVal = "101003")
    public MessageModel delete(Integer[] id){
        saleChanceService.delete(id);
        return new MessageModel("营销机会删除成功");
    }

    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public MessageModel updateSaleChanceDevResult(Integer devResult,Integer saleChanceId){
        saleChanceService.updateSaleChanceDevResult(devResult,saleChanceId);
        return new MessageModel("营销机会状态修改成功");
    }


}
