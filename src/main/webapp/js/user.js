function searchUsers() {
    $("#dg").datagrid("load",{
        userName:$("#userName").val(),
        trueName:$("#trueName").val(),
        phone:$("#phone").val(),
        email:$("#email").val()
    });
}

function openAddUserDialog() {
    $("#dlg").dialog("open").dialog("setTitle","添加用户记录");
    $("#fm").form("clear");
}

function openModifyUserDialog() {
    var row = $("#dg").datagrid("getSelections");
    if(row.length!=1){
        $.messager.alert("crm系统","请选择一条记录","info");
        return;
    }
    $("#fm").form("load",row[0]);
    $("#dlg").dialog("open").dialog("setTitle","修改用户记录");
}

function saveOrUpdateUser() {
    var id = $("#id").val();
    var url = ctx+"/user/update";
    if(isEmpty(id)){
        url = ctx+"/user/insert";
    }
    $("#fm").form("submit",{
        url:url,
        onSubmit:function () {
            return $("#fm").form("validate");
        },
        success: function (data) {
            data = JSON.parse(data);
            if(data.code==200){
                $.messager.alert("来自crm",data.msg,"info");
                searchUsers();
                closeDialog();
            }else{
                $.messager.alert("来自crm",data.msg,"info");
            }
        }
    });
}
function closeDialog() {
    $("#dlg").dialog("close");
}

function deleteUser() {
    var row = $("#dg").datagrid("getSelections");
    if(row.length!=1){
        $.messager.alert("crm系统","请选择一条记录","info");
        return;
    }
    $.messager.confirm('crm系统', '您确定要删除该记录吗？', function(r){

        if (r){
            var param = 'id='+row[0].id;
            $.ajax({
                type:'post',
                url:ctx+'/user/delete',
                data:param,
                dataType:'json',
                success:function (data) {
                    if(data.code==200){
                        $.messager.alert("来自crm",data.msg,"info");
                        searchUsers();
                    }else{
                        $.messager.alert("来自crm",data.msg,"error");
                    }
                }
            });
        }
    });



}