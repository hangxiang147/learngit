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
	<h4>完成</h4>
    <div class="getpass_flow">
        <ul>
            <li >
                <span>1</span>
                <p>填写账号</p>
            </li>
            <li>
                <span>2</span>
                <p>设置密码</p>
            </li>
            <li class="act">
                <span>3</span>
                <p>完成</p>
            </li>
        </ul>
    </div>
    <div class="getpass_success">
        <p>
            <img src="/assets/images/success.png"></p>
        <p class="mt10">设置成功</p>
   
    <p class="tc mt20"><span id="time" class="fcr">10S</span>&nbsp;返回首页，如不跳转点击&nbsp;<a href="/" class="fcr">这里</a></p>
</div>
</div>
    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/bootstrap.min.js"></script>
<script type="text/javascript" >
var second=10;
var time=setInterval(function showTime(){ 
	if(second>0)
	{
		$("#time").html(second+'S');
		second--;
	}else{
		window.location="/";
		clearInterval(time);
	}
},1000)

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


