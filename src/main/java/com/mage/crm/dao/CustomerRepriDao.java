package com.mage.crm.dao;

import com.mage.crm.query.CustomerRepriQuery;
import com.mage.crm.vo.CustomerReprieve;

import java.util.List;

public interface CustomerRepriDao {
    List<CustomerReprieve> customerReprieveByLossId(CustomerRepriQuery customerRepriQuery);

    int insert(CustomerReprieve customerReprieve);

    int update(CustomerReprieve customerReprieve);

    int delete(Integer id);
}
