function searchModules() {
    $("#dg").datagrid("load",{
        moduleName:$("#moduleName").val(),
        optValue:$("#optValue").val(),
        parentModuleName:$("#parentModuleName").val()
    })
}

function formatterGrade(val) {
    switch (val){
        case 0:
            return "根级";
        case 1:
            return "第一级";
        case 2:
            return "第二级";
    }
}

$(function () {
    //默认隐藏父模块
    $("#parentMenu").hide();
    //等级修改动态改变父模块显示或隐藏
    $("#grade").combobox({
        onChange:function(grade){
            if(grade==1||grade==2){
                $("#parentMenu").show();
            }
            if(grade==0){
                $("#parentMenu").hide();
            }

            loadParentModules(grade-1);
        }
    });
});

function loadParentModules(grade) {
    $("#parentId").combobox("clear");
    $("#parentId").combobox({
        url:ctx+"/module/queryModulesByGrade?grade="+grade,
        valueField:'id',
        textField:'moduleName'
    });
}

function openAddModuleDialog() {
    $("#fm").form("clear");
    $("#dlg").dialog("open").dialog("setTitle","添加模块");

}
function closeDialog() {
    $("#dlg").dialog("close");
}
function openModifyModuleDialog() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("crm系统","请选择一条记录","info");
        return;
    }

    var grade = rows[0].grade;
    if(grade==1||grade==2){
        $("#parentMenu").show();
    }
    if(grade==0){
        $("#parentMenu").hide();
    }

    loadParentModules(grade-1);
    $("#fm").form("load",rows[0]);
    $("#grade").combobox("setValue",grade);
    $("#dlg").dialog("open").dialog("setTitle","修改模块");
}
function saveOrUpdateModule() {
    var id = $("#id").val();
    var url = ctx + '/module/update';
    if(isEmpty(id)){
        url = ctx + '/module/insert';
    }

    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm系统",data.msg,"info")
                $("#fm").form("clear");
                closeDialog();
                searchModules();
            }else{
                $.messager.alert("来自crm系统",data.msg,"info")
            }
        }
    })

}

function deleteModule() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("crm系统","请选择一条记录","info");
        return;
    }

    $.messager.confirm("来自crm","确定删除选中的记录?",function(r){
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/module/delete",
                data:'id='+rows[0].id,
                dataType:"json",
                success:function(data){
                    $.messager.alert("来自crm",data.msg,"info");
                    if(data.code==200){
                        closeDialog();
                        searchModules();
                    }
                }
            });
        }
    });
}