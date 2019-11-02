package com.mage.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mage.crm.base.CrmConstant;
import com.mage.crm.dao.ModuleDao;
import com.mage.crm.dao.PermissionDao;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.util.AssertUtil;
import com.mage.crm.vo.Module;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ModuleService {
    @Resource
    private ModuleDao moduleDao;
    @Resource
    private PermissionDao permissionDao;

    public List<ModuleDto> queryAllsModuleDtos(Integer rid) {
        //查询所有的模块资源
        List<ModuleDto> moduleDtos = moduleDao.queryAllsModuleDtos();
        //查询指定角色拥有的模块权限的集合
        List<Integer> moduleIds = permissionDao.queryModuleIdsByRid(rid);
        //循环设置模块选中状态
        if (null != moduleDtos && moduleDtos.size() > 0) {
            for (ModuleDto moduleDto:moduleDtos) {
                if(moduleIds.contains(moduleDto.getId())){
                    moduleDto.setChecked(true);
                }
            }
        }
        return moduleDtos;
    }

    public Map<String,Object> queryModulesByParams(ModuleQuery moduleQuery) {
        PageHelper.startPage(moduleQuery.getPage(),moduleQuery.getRows());
        List<Module> moduleList = moduleDao.queryModulesByParams(moduleQuery);
        PageInfo<Module> modulePageInfo = new PageInfo<>(moduleList);
        Map<String,Object> map = new HashMap<>();
        map.put("total",modulePageInfo.getTotal());
        map.put("rows",modulePageInfo.getList());
        return map;
    }

    public List<Module> queryModulesByGrade(String grade) {
        return moduleDao.queryModulesByGrade(grade);
    }

    public void insert(Module module) {
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue());
        //校验模块名是否同名
        AssertUtil.isTrue(null != moduleDao.queryModuleByGradeAndModuleName(module), "该层级下模块名已存在!");
        AssertUtil.isTrue(null != moduleDao.queryModuleByOptValue(module.getOptValue()), "权限值不能重复!");
        if (module.getGrade() != 0) {
                AssertUtil.isTrue(null == moduleDao.queryModuleById(module.getParentId()), "父级菜单不存在!");
        }
        module.setIsValid(1);
        module.setCreateDate(new Date());
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleDao.insert(module) < 1, CrmConstant.OPS_FAILED_MSG);

    }

    private void checkModuleParams(String moduleName, Integer grade, String optValue) {
        AssertUtil.isTrue(StringUtils.isBlank(moduleName), "模块名非空!");
        AssertUtil.isTrue(null == grade, "层级值非法!");
        Boolean flag = (grade != 0 && grade != 1 && grade != 2);
        AssertUtil.isTrue(flag, "层级值非法!");
        AssertUtil.isTrue(StringUtils.isBlank(optValue), "权限值非空!");
    }

    public void update(Module module) {
        checkModuleParams(module.getModuleName(), module.getGrade(), module.getOptValue());
        //校验模块名是否同名
        Module temp = moduleDao.queryModuleByOptValue(module.getOptValue());
        AssertUtil.isTrue(null != temp && !temp.getId().equals(module.getId()), "权限值不能重复!");
        temp = moduleDao.queryModuleByGradeAndModuleName(module);
        AssertUtil.isTrue(null != temp && !temp.getId().equals(module.getId()), "该层级下模块名不能重复!");
        if (module.getGrade() != 0) {
            AssertUtil.isTrue(null == moduleDao.queryModuleById(module.getParentId()), "父级菜单不存在!");
        }
        module.setUpdateDate(new Date());
        AssertUtil.isTrue(moduleDao.update(module) < 1, CrmConstant.OPS_FAILED_MSG);
    }

    public void delete(Integer id) {
        AssertUtil.isTrue(null == id || null == moduleDao.queryModuleById(id), "待删除记录不存在!");
        //声明接收子模块id的集合
        List<Integer> mids = new ArrayList<Integer>();
        //获取子模块id
        mids = getSubModuleIds(id, mids);
        AssertUtil.isTrue(moduleDao.delete(mids) < mids.size(), CrmConstant.OPS_FAILED_MSG);
    }

    private List<Integer> getSubModuleIds(Integer id, List<Integer> mids) {
        Module module = moduleDao.queryModuleById(id);
        if(module!=null){
            //模块id有效，加入要删除的集合中
            mids.add(id);
            //查询该id下的子模块
            List<Module> moduleList = moduleDao.querySubModulesByPid(id);
            if(moduleList!=null&&moduleList.size()>0){
                //调用getSubModuleIds方法，加入集合，并查询其子模块
                for (Module temp:moduleList) {
                    getSubModuleIds(temp.getId(),mids);
                }

            }

        }
        return mids;
    }
}
