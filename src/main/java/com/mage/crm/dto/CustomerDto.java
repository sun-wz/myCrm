package com.mage.crm.dto;

import com.mage.crm.vo.Customer;

public class CustomerDto extends Customer {
    private String total;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
