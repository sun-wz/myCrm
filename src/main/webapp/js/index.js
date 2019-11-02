function userLogin() {
    var userName = $("#userName").val();
    var userPwd = $("#userPwd").val();
    if(isEmpty(userName)){
        alert("用户名不能为空");
        return;
    }
    if(isEmpty(userPwd)){
        alert("密码不能为空");
        return;
    }

    $.ajax({
        type:"post",
        url:ctx+"/user/userLogin",
        data:{'userName':userName,'userPwd':userPwd},
        dateType:"json",
        success:function (data) {
            if(data.code==200){
                //alert(data.msg);
                $.cookie("id",data.result.id);
                $.cookie("userName",data.result.userName);
                $.cookie("trueName",data.result.trueName);
                //页面跳转
                window.location.href="main";
            }else{
                alert(data.msg);
            }
        }
    })
}