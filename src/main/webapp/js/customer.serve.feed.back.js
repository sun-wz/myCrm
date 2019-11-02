function openFeedBackDlg() {
    var row = $("#dg").datagrid("getSelections");
    if(row.length!=1){
        $.messager.alert("crm系统","请选则一条数据","info");
        return;
    }
    $("#fm").form("load",row[0]);
    $("#dlg").dialog("open");
}
function closeCustomerServeDialog() {
    $("#dlg").dialog("close");
}
function addCustomerServeServiceFeedBack() {
    $("#fm").form("submit",{
        url:ctx + "/customer_serve/update",
        onSubmit:function (params) {
            params.state = 4;
            return $("#fm").form("validate");
        },
        success:function (data) {
            data=JSON.parse(data);
            $.messager.alert("crm",data.msg,"info");
            if(data.code==200){
                closeCustomerServeDialog();
                $("#dg").datagrid("load");
            }
        }

    })

}