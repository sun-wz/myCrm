package com.mage.crm.controller;

import com.mage.crm.base.BaseController;
import com.mage.crm.dto.ModuleDto;
import com.mage.crm.model.MessageModel;
import com.mage.crm.query.ModuleQuery;
import com.mage.crm.service.ModuleService;
import com.mage.crm.vo.Module;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController{
    @Resource
    private ModuleService moduleService;

    @RequestMapping("queryAllsModuleDtos")
    @ResponseBody
    public List<ModuleDto> queryAllsModuleDtos(Integer rid){
        return moduleService.queryAllsModuleDtos(rid);
    }

    @RequestMapping("index")
    public String index(){
        return "module";
    }

    @RequestMapping("queryModulesByParams")
    @ResponseBody
    public Map<String,Object> queryModulesByParams(ModuleQuery moduleQuery){
        return moduleService.queryModulesByParams(moduleQuery);
    }

    @ResponseBody
    @RequestMapping("queryModulesByGrade")
    public List<Module> queryModulesByGrade(String grade){
        return moduleService.queryModulesByGrade(grade);
    }

    @RequestMapping("insert")
    @ResponseBody
    public MessageModel insert(Module module){
        moduleService.insert(module);
        return new MessageModel("模块添加成功");
    }

    @ResponseBody
    @RequestMapping("update")
    public MessageModel update(Module module){
        moduleService.update(module);
        return new MessageModel("模块修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public MessageModel delete(Integer id){
        moduleService.delete(id);
        return new MessageModel("模块删除成功");
    }


}
