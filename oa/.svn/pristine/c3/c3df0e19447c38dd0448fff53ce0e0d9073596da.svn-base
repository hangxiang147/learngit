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
            <li class="act">
                <span>1</span>
                <p>填写账号</p>
            </li>
            <li>
                <span>2</span>
                <p>设置密码</p>
            </li>
            <li>
                <span>3</span>
                <p>完成</p>
            </li>
        </ul>
    </div>
    <div class="getpass">
    	<form action="toResetStep2"  method="post" id="form1">
        <p class="user">
            <label>账号：</label>
            <input id="num" name="num" type="text" placeholder="花名/手机号码" />
            <input name="userId" id="userId" style="display:none"/>
            <input name="telephone" id="telephone" style="display:none" />
        </p>
        <p class="yzm">
            <label>验证码：</label>
            <input name="code" id="code" type="text"  maxlength="6"/>
            <input type="button" id="btnSendCode" value="免费获取验证码" onclick="sendMessage()" /></p>
        <div class="tc">
            <input value="下一步" class="getpass_btn" onclick="toNext()" /></div>
        </form>
    </div>
 </div>
   
    <script src="/assets/js/jquery.min.js"></script>
    <script src="/assets/js/bootstrap.min.js"></script>
    <script>
    
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
    	var canSubmit=false;
    	var canGetValidateCode=true;
    	var toNext=function (){
    		var value=$('#code').val();
    		if(!canSubmit){
    			alert("请先获取验证码");
    		}else{
    			if(!value)return;
				if(value.length<6){
					alert("验证码为6位数字")
					return;
				}
				var result=getValidateResult(value);
				if(result==1){
					alert("验证码过期！");
					return;
				}
				if(result==2){
					alert("验证码错误！");
					return;
				}
    			$('#form1').submit();
    		}
    	}
				
		var getValidateResult=function (value){
   				var userId=$('#userId').val();
   				if(!userId){
   					alert("请先请求验证码");
   					return;
   				}
   				var result;
   				$.ajax({
   					url:'/checkValidateKey',
   					data:{userId:userId,validateKey:value},
   					type:'post',
   					async:false,
   					success:function (data){
   						result=data.result;
   					}
   				})
   				return result;
   			}
    	var sendMessage=function (){
    		if(!canGetValidateCode){
    			alert("请耐心等待验证码");
    			return;
    		}
			var key=$('#num').val();
			if(!key){
				alert("请输入花名或者手机号");
				$('#num').focus();
				return;
			}
			$.ajax({
				url:'/getUserIdByKey',
				data:{key:key},
				type:'post',
				success:function (data){
					if(data.userId){
						var userId=data.userId;
						var telephone=data.telephone;
						$('#telephone').val(telephone);
						$('#userId').val(userId);
						if(!telephone||"null"==telephone){
							alert("该用户手机号为空");
						}else{
							$.ajax({
								url:'getValidateKey',
								data:{userId:userId,telephone:telephone},
								type:'post',
								success:function (data){
									if(data.success){
										alert("验证码已经发出,请注意查收");
										canGetValidateCode=false;
										$('#btnSendCode').data("second",60);
										var remain=setInterval(function(){
											var remainSecond=$('#btnSendCode').data("second");
											$('#btnSendCode').val("剩余"+remainSecond+"s");
											$('#btnSendCode').data("second",Number(remainSecond)-1);
										},1000);
										setTimeout(function(){
											window.clearInterval(remain)
											$('#btnSendCode').val("免费获取验证码");;
											canGetValidateCode=true;
										},1000*61);
										canSubmit=true;
									}
								}
							})
						}
					}else{
						alert("花名或者手机号不存在")
					}
				}	
			})
    	}
    </script>
</body>
</html>


