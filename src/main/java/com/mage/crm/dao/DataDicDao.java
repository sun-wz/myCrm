package com.mage.crm.dao;

import com.mage.crm.vo.DataDic;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface DataDicDao {
    @Select("select data_dic_value 'dataDicValue' from t_datadic where is_valid = 1 and data_dic_name = #{dataDicName}")
    List<DataDic> queryDataDicValueByDataDicName(String dataDicName);
}
