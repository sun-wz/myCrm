function formatterState(val) {
    if (val==0){
        return "未分配";
    }else if(val==1){
        return "已分配";
    }else {
        return "未定义";
    }
}

$(function () {
    searchSaleChances();
})
//查询方法
function searchSaleChances() {
    $("#dg").datagrid("load",{
        createMan:$("#createMan").val(),
        customerName:$("#customerName").val(),
        createDate:$("#createDate").datebox("getValue"),
        state:$("#state").combobox("getValue")
    })
}

function openAddAccountDialog() {
    $("#fm").form("clear");
    $("#dlg").dialog("open").dialog("setTitle","增加营销机会记录");

}
function closeDialog() {
    $("#dlg").dialog("close");
}

function saveAccount() {
    var id=$("#id").val();
    var url = ctx+"/sale_chance/update";
    if(isEmpty(id)){
        url = ctx+"/sale_chance/insert";
    }

    $("#fm").form("submit",{
        url:url,
        onSubmit:function (parms) {
            parms.createMan =$.cookie("trueName");
            return $("#fm").form("validate");
        },
        success:function (data) {
            data = JSON.parse(data);
            if (data.code==200){
                $.messager.alert("crm系统",data.msg,"info");
                closeDialog();
                searchSaleChances();
            }else {
                $.messager.alert("crm系统",data.msg,"error");
            }
        }
    });
}

function openModifyAccountDialog() {
    var rows = $("#dg").datagrid("getSelections");
    if(rows.length<1){
        $.messager.alert("crm系统","至少选择一行记录","error");
        return;
    }
    if(rows.length>1){
        $.messager.alert("crm系统","最多选择一行记录","error");
        return;
    }
    $("#fm").form('load',rows[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改营销机会记录");
}

function deleteAccount() {
    var rows = $("#dg").datagrid("getSelections");

    if(rows.length<1){
        $.messager.alert("crm系统","至少选择一行记录","error");
        return
    }
    var params="id=";
    for (var i=0;i<rows.length;i++){
        if(i<rows.length-1){
            params=params+rows[i].id+"&id=";
        }else {
            params=params+rows[i].id;
        }
    }

    $.messager.confirm("crm系统","您确定删除选中记录嘛？",function (r) {
        if(r){
            $.ajax({
                type:"post",
                url:ctx+"/sale_chance/delete",
                data:params,
                dataType:'json',
                success:function (data) {
                    console.log(data.msg);
                    if(data.code==200){
                        $.messager.alert("crm系统",data.msg,"info");
                        searchSaleChances();
                    }else{
                        $.messager.alert("crm系统",data.msg,"error");

                    }
                }
            })
        }

    })
}