function openTab(text, url, iconCls){
    if($("#tabs").tabs("exists",text)){
        $("#tabs").tabs("select",text);
    }else{
        var content="<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='" + url + "'></iframe>";
        $("#tabs").tabs("add",{
            title:text,
            iconCls:iconCls,
            closable:true,
            content:content
        });
    }
}

function logout() {
    $.messager.confirm("crm系统","您确定退出当前系统嘛？",function (r) {
        if (r){
            setTimeout(function(){
                //退出删除cookie
                $.removeCookie("id");
                $.removeCookie("userName");
                $.removeCookie("trueName");
                window.location.href = "index";
            },1000);
        }
    });

}
function openPasswordModifyDialog() {
    $("#dlg").dialog("open");

}
function closePasswordModifyDialog() {
    $("#dlg").dialog("close");
}
function modifyPassword() {

    $("#fm").form("submit",{
        url:ctx+"/user/updatePwd",
        onSubmit: function(){
            //返回false 会终止提交
            return $("#fm").form("validate");
        },
        success:function(data){
            data=JSON.parse(data);//将返回值转换成为json串

            if(200 == data.code){
                //成功
                $.messager.alert('crm系统',"修改密码成功，需重新登录",'info');

                setTimeout(function(){
                        //退出删除cookie
                        $.removeCookie("id");
                        $.removeCookie("userName");
                        $.removeCookie("trueName");
                        window.location.href = "index";
                    },1000);
            }else{
                $.messager.alert('crm系统',data.msg,'error');
            }
        }
    });

}