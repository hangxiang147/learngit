<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
	
<title>登录</title>
<link rel="icon" href="/assets/images/favicon.ico?verson=<%=Math.random()%>>">
<link href="/assets/css/base1.css" rel="stylesheet" type="text/css" />  
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	.login_fs {
	    position: absolute;
	    right: 0;
	    top: 9.5%;
	    width: 52px;
	    height: 52px;
	    cursor: pointer;
	    z-index:100
	}
	.qrcode_btn {
   		background: #ffffff url(/assets/images/loginfs.png) no-repeat;
	}
	.qrcode_btn:hover {
   	 	background: #ffffff url(/assets/images/loginfs.png) no-repeat 0px -60px;
   	}
</style>
<script type="text/javascript" src="/assets/js/jquery.min.js"></script>
<script type="text/javascript" src="/assets/js/jquery-ui.min.js"></script>
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
</head>
<body class="login_bg">
<div class="logo"><div class="fl"><img src="/assets/images/logo.png?verson=<%=Math.random()%>>"/></div><div class="fl logolm">办公系统</div></div>
<div class="login">
<div class="login_fs qrcode_btn" title="微信扫码登录" onclick="showQrcode()"></div>
<div id="login">
<form action="/login" method="post" id="myform" class="form-horizontal" >
<div class="login_tab">
<div class="lm">员工登录</div>
<div class="login_form">
<ul>
<li class="login_user"><input style="width:95%" id="userName" name="staffVO.userName" type="text"  class="login_text" value="" placeholder="登录用户名" required autofocus /></li>
<li class="login_pass"><input style="width:95%" id="password" name="staffVO.password" type="password"  class="login_text" placeholder="登录密码" required/><span class="error">${errorMessage}</span></li>
<li class="login_btndiv" style="margin-top: 25px"><input id="Button1"  type="submit" value="登录" class="login_btn"/></li>
</ul>
</div>
<div class="mt10"><a href="/toResetStep1" style="color:#ee5252" >忘记密码/花名</a></div>

<div class="mt10">地区：通州，南通，如东，广州，南京，佛山</div>
</div>
</form>
<div class="login_banner">创新&nbsp;&nbsp;专业&nbsp;&nbsp;合作&nbsp;&nbsp;务实</div>
</div>
</div>
<div id="loginTab" style="position:absolute;top:20%;left:38%;display:none"></div>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
	if('${bind}'=='false'){
		layer.alert("请先登录OA绑定账号",{offset:'100px'});
	}
});
var appID = "wx9c2800cfc46895f0";
var uri = "http://www.zhizaolian.com:9090";
var obj = new WxLogin({
  id: "loginTab",
  appid: appID,
  scope: "snsapi_login", 
  redirect_uri: encodeURI(uri),
  state: guid()
});
function showQrcode(){
	$(".login").css("opacity", "0.1");
	$(".logo").css("opacity", "0.1");
	$("#loginTab").css("display", "block");
	$("input").css("pointer-events", "none");
	$(".qrcode_btn").css("pointer-events", "none");
}
$("#login, .logo").click(function(){
	if($("#loginTab").css("display")=='block'){
		$("#loginTab").css("display", "none");
		$(".login").css("opacity", "1");
		$(".logo").css("opacity", "1");
		$("input").css("pointer-events", "");
		$(".qrcode_btn").css("pointer-events", "");
	}
});
function guid() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}
</script>
</body>
</html>
