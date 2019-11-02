package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.dao.CustomerDao;
import com.mage.crm.dao.CustomerLossDao;
import com.mage.crm.dto.CustomerDto;
import com.mage.crm.query.CustomerQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Customer;
import com.mage.crm.vo.CustomerLoss;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {
    @Resource
    private CustomerDao customerDao;
    @Resource
    private CustomerLossDao customerLossDao;

    public List<Customer> queryAllCustomers(){
        return customerDao.queryAllCustomers();
    }

    public Map<String,Object> queryCustomersByParams(CustomerQuery customerQuery) {
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getRows());
        List<Customer> customerList = customerDao.queryCustomersByParams(customerQuery);
        PageInfo<Customer> customerPageInfo = new PageInfo<>(customerList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",customerPageInfo.getTotal());
        map.put("rows",customerPageInfo.getList());
        return map;
    }

    public void insert(Customer customer) {
        //非空判断
        checkParams(customer.getName(),customer.getFr(),customer.getPhone());
        customer.setIsValid(1);
        customer.setState(0);
        customer.setCreateDate(new Date());
        customer.setUpdateDate(new Date());
        customer.setKhno("KH"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        AssertUtil.isTrue(customerDao.insert(customer)<1,"客户插入失败");
    }

    private void checkParams(String name, String fr, String phone) {
        AssertUtil.isTrue(StringUtils.isBlank(name),"客户名称不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(fr),"法人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"联系电话不能为空");
    }

    public void update(Customer customer) {
        //非空判断
        checkParams(customer.getName(),customer.getFr(),customer.getPhone());
        customer.setUpdateDate(new Date());
        AssertUtil.isTrue(customerDao.update(customer)<1,"客户修改失败");
    }

    public void delete(Integer[] id) {
        AssertUtil.isTrue(customerDao.delete(id)<id.length,"客户删除失败");
    }

    public Customer queryCustomerById(Integer id) {
        return customerDao.queryCustomersById(id);

    }

    public void updateCustomerLossState() {
        List<CustomerLoss> customerLossList = customerDao.queryCustomerLoss();
        if(customerLossList!=null&&customerLossList.size()>0) {
            for (CustomerLoss customerLoss : customerLossList) {
                customerLoss.setIsValid(1);
                customerLoss.setCreateDate(new Date());
                customerLoss.setUpdateDate(new Date());
                customerLoss.setState(0);//预处理流失 暂缓流失
            }
        }
        AssertUtil.isTrue( customerLossDao.insertBatch(customerLossList)<1,"客户流失数据添加失败");
    }

    public Map<String,Object> queryCustomersContribution(CustomerQuery customerQuery) {
        PageHelper.startPage(customerQuery.getPage(),customerQuery.getRows());
        List<CustomerDto> customerList = customerDao.queryCustomersContribution(customerQuery);
        PageInfo<CustomerDto> customerPageInfo = new PageInfo<>(customerList);
        Map<String, Object> map = new HashMap<>();
        map.put("total",customerPageInfo.getTotal());
        map.put("rows",customerPageInfo.getList());
        return map;

    }

    public Map<String,Object> queryCustomerGC() {
        List<CustomerDto>  customerDtos = customerDao.queryCustomerGC();
        String[] levels= null;
        int[] counts=null;

        Map<String,Object> map  = new HashMap<>();
        map.put("code",300);

        if(customerDtos!=null&customerDtos.size()>0){
            int size = customerDtos.size();
            levels=new String[size];
            counts=new int[size];
            for (int i=0;i<size;i++){
                levels[i]=customerDtos.get(i).getLevel();
                counts[i]=customerDtos.get(i).getCount();
            }
            map.put("code",200);
        }
        map.put("levels",levels);
        map.put("counts",counts);
        return map;

    }
}
