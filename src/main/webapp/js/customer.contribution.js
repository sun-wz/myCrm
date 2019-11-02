function searchCustomerContributions() {
    $("#dg").datagrid("load",{
        name:$("#customerName").val()
    });
}
