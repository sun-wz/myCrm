function searchCustomer() {
    $("#dg").datagrid("load",{
        khno:$("#s_khno").val(),
        name:$("#s_name").val()
    })

}
function openCustomerAddDialog() {
    $("#fm").form("clear");
    $("#dlg").dialog("open").dialog("setTitle","添加客户信息");
}

function closeCustomerDialog() {
    $("#dlg").dialog("close");
}


function saveOrUpdateCustomer() {
    var id = $("#id").val();
    var url = ctx+"/customer/update";
    if(isEmpty(id)){
        url = ctx+"/customer/insert";
    }

    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            if(data.code==200){
                closeCustomerDialog();
                searchCustomer();
                $.messager.alert("crm系统",data.msg,"info");
            }else{
                $.messager.alert("crm系统",data.msg,"error");
            }
        }
    })
}
function openCustomerModifyDialog() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows==0){
        $.messager.alert("crm系统","请选择一行记录!","info");
        return;
    }
    if(rows>1){
        $.messager.alert("crm系统","请选择一行记录!","info");
        return;
    }

    $("#fm").form("load",rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改客户信息");
}

function deleteCustomer() {
    var rows=$("#dg").datagrid("getSelections");
    if (rows.length==0){
        $.messager.alert("crm系统","请选中一条记录","error");
        return;
    }
    var params="id=";
    for (var i=0;i<rows.length;i++){
        if(i<rows.length-1){
            params=params+rows[i].id+"&id=";
        }else {
            params=params+rows[i].id;
        }
    }
    $.messager.confirm("crm系统","你确定要删除所选的记录嘛？",function (r) {
        if (r){
            $.ajax({
                url:ctx+"/customer/delete",
                data:params,
                dataType:'json',
                success:function (data) {
                    if (data.code==200){
                        $.messager.alert("crm系统",data.msg,"info");
                        searchCustomer();
                    }else {
                        $.messager.alert("crm系统",data.msg,"error");
                    }
                }
            })
        }
    })
}

function openCustomerOtherInfo(title,type) {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length==0){
        $.messager.alert("crm","请先选中一个客户","info");
        return;
    }
    if(rows.length>1){
        $.messager.alert("crm","只能选中一个客户","info");
        return;
    }
    window.parent.openTab(title,ctx+"/customer/openCustomerOtherInfo/"+type+"/"+rows[0].id);

}