function searchRoles() {
    $("#dg").datagrid("load",{
        roleName:$("#roleName").val()
    });
}
function openAddRoleDialog() {
    $("#fm").form("clear");
    $("#dlg").dialog("open").dialog("setTitle","添加角色记录")
}
function openModifyRoleDialog() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("crm系统","请选择一条记录","info");
        return;
    }
    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","添加角色记录")
}

function saveOrUpdateRole() {
    var id = $("#id").val();
    var url = ctx+'/role/update';
    if(isEmpty(id)){
        url = ctx+'/role/insert';
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            $("#fm").form("validate");
        },
        success:function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                closeDialog();
                searchRoles();
            }else{
                $.messager.alert("来自crm",data.msg,"error");
            }
            
        }
    })


}
function closeDialog() {
    $("#dlg").dialog("close");
}
function deleteRole() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("crm系统","请选择一条记录","info");
        return;
    }
    $.messager.confirm('crm系统', '您确定要删除该记录吗？', function(r){
        if (r){
            var param = 'id='+rows[0].id;
            $.ajax({
                type:'post',
                url:ctx+'/role/delete',
                data:param,
                dataType:'json',
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        searchRoles();
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            });
        }
    });
}

function openRelatePermissionDlg() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length!=1){
        $.messager.alert("crm系统","请选择一条记录","info");
        return;
    }
    $("#rid").val(rows[0].id);
    loadModuleData();
    $("#dlg02").dialog("open");
}

function zTreeOnCheck() {
    //获取选中的节点的集合
    var znodes=ztreeObj.getCheckedNodes(true);
    var moduleIds = "moduleIds=";
    if(znodes.length>0){
        for(var i=0;i<znodes.length;i++){
            if(i<znodes.length-1){
                moduleIds=moduleIds+znodes[i].id+"&moduleIds=";
            }else{
                moduleIds=moduleIds+znodes[i].id;
            }
        }
    }
    console.log(moduleIds);
    $("#moduleIds").val(moduleIds);
}

var ztreeObj;
function loadModuleData(){
    $.ajax({
        type:"post",
        url:ctx+"/module/queryAllsModuleDtos",
        data:"rid="+$("#rid").val(),
        dataType:"json",
        success:function(data){

            // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
            var setting = {
                check: {
                    enable: true
                },
                data: {
                    simpleData: {
                        enable: true
                    }
                },
                callback: {
                    onCheck: zTreeOnCheck
                }
            };
            // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
            var zNodes =data;
            ztreeObj= $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            zTreeOnCheck();
        }
    });
}

function closeDialog02() {
    $("#dlg02").dialog("close");
}
function addPermission(){
    /**
     * 1.角色id
     * 2.资源id
     */
    $.ajax({
        type:"post",
        url:ctx+"/role/addPermission",
        data:"rid="+$("#rid").val()+"&"+$("#moduleIds").val(),
        dataType:"json",
        success:function(data){
            console.log(data);
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                $("#moduleIds").val("");
                $("#rid").val("");
                closeDialog02();
            }else{
                $.messager.alert("来自crm",data.msg,"info");
            }
        }
    });
}

