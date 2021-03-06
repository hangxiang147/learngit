<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%    
	String path = request.getContextPath();    
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";    
	%> 
	<base href="<%=basePath%>"/> 
    <link rel="icon" href="assets/images/favicon.ico">

    <title>智造链OA办公系统</title>
    <script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
    <!-- Bootstrap core CSS -->
   <link href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
   
    <style type="text/css">
    	body {
		  padding-top: 40px;
		  padding-bottom: 40px;
		  background-color: #eee;
		}
		h1 {
			text-align:center;
		}
		.tips {
			font-size:12px;
			color: grey;
		    padding-top: 10px;
		}
		.form-horizontal {
			margin-top:30px;
		}
    </style>
    
    <script type="text/javascript">
	    $(function() {
	    	$("#errorMessage").attr("style", "display:none;");
	    	var errorMessage = $("#errorMessageText").val();
	    	if (errorMessage!=null && errorMessage.length!=0) {
	    		$("#errorMessage span").text(errorMessage);
	    		$("#errorMessage").removeAttr("style");
	    	}
		});
    
    	function showName(obj) {
    		$("#errorMessage").attr("style", "display:none;");
    		$("#nickname_div").remove();
    		
    		var type = $(obj).val();
    		if (type == '') {
    			return;
    		}
    		$.ajax({
    			url:'findNicknamesByType',
    			type:'post',
    			data:{type:type},
    		    dateType:'json',
    		    success:function(data) {
    		    	if (data.errorMessage!=null && data.errorMessage.length!=0) {
    		    		$("#errorMessage span").text(data.errorMessage);
    		    		$("#errorMessage").removeAttr("style");
    		    		return;
    		    	}
    		    	
    				$("#type_div").after("<div id=\"nickname_div\" class=\"col-sm-3\">"
							+"<select id=\"nickname\" class=\"form-control\" name=\"nicknameID\" required=\"required\">"
    							+"<option value=\"\">--请选择--</option></select>"
    							+"</div>");
    				$.each(data.nicknameVOs, function(i, nickname) {
    					if (nickname.status == 0) {
    						$("#nickname").append("<option style='font-weight:bold' value='"+nickname.nicknameID+"'>"+nickname.name+"</option>");
    					} else {
    						$("#nickname").append("<option value='"+nickname.nicknameID+"' disabled='disabled'>"+nickname.name+"</option>");
    					}
    					
    				});
    		    }
    		});
    	}
    	
    	function checkPassword(obj) {
    		var password = $(obj).val();
    		if (password.length<6 || password.length>16) {
    			$("#errorMessage span").text("密码长度不合法！");
	    		$("#errorMessage").removeAttr("style");
    		} else {
    			$("#errorMessage span").text("");
	    		$("#errorMessage").attr("style", "display:none;");
    		}
    	}
    
    	function checkTotal() {
    		var password = $("#inputPassword").val();
    		var passwordAgain = $("#inputPasswordAgain").val();
    		if (password != passwordAgain) {
    			$("#errorMessage span").text("密码不一致，请重新输入！");
	    		$("#errorMessage").removeAttr("style");
	    		return false;
    		}
    		
    		if (password.length<6 || password.length>16) {
    			$("#errorMessage span").text("密码长度不合法！");
	    		$("#errorMessage").removeAttr("style");
	    		return false;
    		}
    	}
    </script>
  </head>

  <body>
	    
    <div class="container">
    	<h1>初始化员工账号</h1>
    	<div class="row">
			<div class="col-sm-8 col-sm-offset-4 col-md-9 col-md-offset-3 main">
				<form action="saveUserAccount" method="post" class="form-horizontal" onsubmit="return checkTotal()">
					<div class="form-group">
						<label for="type" class="col-sm-2 control-label">请选择花名</label>
					    <div class="col-sm-3" id="type_div">
					    	<select class="form-control" id="type" onchange="showName(this)" required>
						      <option value="">--分类--</option>
							  <option value="1">飞狐外传</option>
							  <option value="2">雪山飞狐</option>
						      <option value="3">连诚诀</option>
							  <option value="4">天龙八部</option>
							  <option value="5">射雕英雄传</option>
							  <option value="6">白马啸西风</option>
							  <option value="7">鹿鼎记</option>
							  <option value="8">笑傲江湖</option>
							  <option value="9">书剑恩仇录</option>
							  <option value="10">神雕侠侣</option>
							  <option value="11">侠客行</option>
							  <option value="12">倚天屠龙记</option>
							  <option value="13">碧血剑</option>
							  <option value="14">鸳鸯刀</option>
							</select>
				    	</div>
					</div>
					<div class="form-group">
						<label for="inputPassword" class="col-sm-2 control-label">密码</label>
					    <div class="col-sm-4">
				    		<input type="password" class="form-control" id="inputPassword" name="password" placeholder="密码" onchange="checkPassword(this)" required>
				    	</div>
				    	<label class="col-sm-4 tips">6~16位字符，区分大小写</label>
					</div>
					<div class="form-group">
						<label for="inputPasswordAgain" class="col-sm-2 control-label">确认密码</label>
					    <div class="col-sm-4">
				    		<input type="password" class="form-control" id="inputPasswordAgain" placeholder="密码" required>
				    	</div>
				    	<label class="col-sm-4 tips">请再次填写</label>
					</div>
					<div class="form-group" id="errorMessage" style="display:none;">
						<input id="errorMessageText" type="hidden" value="${errorMessage }" />
						<span class="col-sm-10 col-sm-offset-2" style="color:red; height: 30px;"></span>
					</div>
					<div class="form-group" style="padding-top:15px;">
						<button class="btn btn-primary col-sm-offset-4" type="submit" >提交</button>
					</div>
					<div class="form-group">
						<div class="col-sm-12">
							<span style="color:red;">注：请务必记住自己选择的花名，以后使用本次设置的花名和密码登录系统！</span>
						</div>
					</div>
				</form>
			</div>
		</div>
    </div> <!-- /container -->


	<!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  </body>
</html>
