package com.mage.crm.dao;

import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.vo.Module;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface ModuleDao {
    @Select("SELECT m.id,m.module_name as 'name',m.parent_id  as 'pId'from t_module m where is_valid=1")
    List<ModuleDto> queryAllsModuleDtos();

    @Select("select opt_value from t_module where id=#{moduleId}")
    String queryOptValueById(Integer moduleId);

    List<Module> queryModulesByParams(ModuleQuery moduleQuery);

    List<Module> queryModulesByGrade(String grade);
    @Select("select id,module_name as moduleName,url,opt_value as optValue"
            + " from t_module where grade=#{grade} and module_name=#{moduleName} and parent_id=#{parentId} and is_valid=1" )
    Module queryModuleByGradeAndModuleName(Module module);

    @Select("select  id,module_name as moduleName,url,opt_value as optValue"
            + "  from t_module where opt_value=#{optValue} and is_valid=1")
    Module queryModuleByOptValue(String optValue);

    @Select("select id,module_name as moduleName,url,opt_value as optValue"
            + " from t_module where id=#{id} and is_valid=1" )
    Module queryModuleById(@Param("id") Integer id);

    @Insert("insert into t_module(module_name,module_style,url,opt_value,parent_id,grade,"
            + "orders,is_valid,create_date,update_date) values("
            + "#{moduleName},#{moduleStyle},#{url},#{optValue},#{parentId},#{grade},"
            + "#{orders},#{isValid},#{createDate},#{updateDate})")
    Integer insert(Module module);


    @Update("update t_module set module_name=#{moduleName},module_style=#{moduleStyle},"
            + "url=#{url},opt_value=#{optValue},parent_id=#{parentId},"
            + "grade=#{grade},orders=#{orders},update_date=#{updateDate}"
            + "where id=#{id} and is_valid=1")
    Integer update(Module module);
    @Select("select id,module_name as moduleName "
            + " from t_module where parent_id=#{pid} and is_valid=1")
    List<Module> querySubModulesByPid(Integer pid);

    Integer delete(List<Integer> mids);
}
