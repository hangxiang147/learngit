<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="/assets/css/base1.css" rel="stylesheet" type="text/css" />  
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/bootstrap.min.css" rel="stylesheet" type="text/css" />

</head>
<body>
    <div class="getpassbox">
	<h4>找回密码</h4>
    <div class="getpass_flow">
        <ul>
            <li>
                <span>1</span>
                <p>填写账号</p>
            </li>
            <li class="act">
                <span>2</span>
                <p>设置密码</p>
            </li>
            <li>
                <span>3</span>
                <p>完成</p>
            </li>
        </ul>
    </div>
    <form action="/toResetStep3" method="post" id="form1">
    <div class="getpass">
    	<input style="display:none" name="code" value="${code}"/>
 	    <input style="display:none" name="userId" value="${userId}"/>    
 	    <label style="padding-left:20px;width: 300px;height: 32px;line-height: 32px;text-align: left">花名：&nbsp;&nbsp;&nbsp;${userName}</label>	
        <p class="user">
            <label>新密码：</label>
            <input id="inputPassword" name="inputPassword" type="password" placeholder="请输入您的新密码" /></p>
        <p class="user">
            <label>确认密码：</label>
            <input id="inputPasswordAgain" name="inputPasswordAgain" type="password" placeholder="请再次输入您的新密码" /></p>
        <div class="tc">
            <input  value="下一步" class="getpass_btn" onclick="toNext()" /></div>
    </div>
    </form>
 </div>
    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/bootstrap.min.js"></script>
    <script>
    var toNext=function (){
    	if(checkTotal())
    		$('#form1').submit();
    }
    function checkTotal() {

		var password = $("#inputPassword").val();
		var passwordAgain = $("#inputPasswordAgain").val();
		if (password != passwordAgain) {
			alert("密码不一致，请重新输入！");
    		return false;
		}
		
		if (password.length<6 || password.length>16) {
			alert("密码长度不合法！");
    		return false;
		}
		return true;
	}
    $(function () {
    	var _box=$(".getpassbox");
    	var boxH=_box.height();
    	var boxW=_box.width();
    	var _scrollHeight = $(document).scrollTop(),//获取当前窗口距离页面顶部高度
    		_windowHeight = $(window).height(),//获取当前窗口高度
    		_windowWidth = $(window).width(),//获取当前窗口宽度
    		_Top = (_windowHeight - boxH)/2 + _scrollHeight-100;
    		_Left = (_windowWidth - boxW)/2;
    	_box.css({"top":_Top,"left":_Left});
    });
    </script>
</body>
</html>


