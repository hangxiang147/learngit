<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">


	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
  	function checkBankAccount(){
  		var accountNumber = $("#cardNumber").val();
  		var reg = new RegExp(/^[0-9]{10,30}$/);
  		var result = reg.test(accountNumber);
  		if(!result){
  			$("#errorMsg").css("display","block");
  		}else{
  			$("#errorMsg").css("display","none");
  		}
  	}
  	function checkError(){
  		var display = $("#errorMsg").css("display");
  		if(display=="block"){
  			layer.alert("银行卡号不符合规范",{offset:'100px'});
  			return false;
  		}
  		return true;
  	}
</script>
<style type="text/css">
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 15px;
	    margin-top: 15px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_updateBankAccount" method="post" class="form-horizontal" onsubmit="return checkError()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">修改打款账号</h3> 
        	   <input type="hidden" name="taskID" value="${taskID}" />
     	       <input type="hidden" name="processType" value="${processType}" />
     	       <input type="hidden" name="bankAccount.userID" value="${bankAccount.userID}"/>
     	       <input type="hidden" name="bankAccount.accountID" value="${bankAccount.accountID}"/>
     	       <input type="hidden" name="bankAccount.addTime" value="${bankAccount.addTime}"/>
		  	  <div class="form-group">
		  		<label for="cardName" class="col-sm-1 control-label">户主姓名</label>
		  		<div class="col-sm-2"><input type="text" class="form-control" id="cardName" name="reimbursementVO.cardName" required="required" value="${bankAccount.cardName}"/></div>
		  	  </div>
		  	  <div class="form-group">
		  		<label for="bank" class="col-sm-1 control-label">开户行</label>
		  		<div class="col-sm-5"><input type="text" class="form-control" id="bank" name="reimbursementVO.bank" required="required" value="${bankAccount.bank}"/></div>
		  	  </div>
		  	  <div class="form-group">
		  		<label for="cardNumber" class="col-sm-1 control-label">银行卡卡号</label>
		  		<div class="col-sm-5"><input type="text" class="form-control" id="cardNumber" name="reimbursementVO.cardNumber" value="${bankAccount.cardNumber}"
		  		onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" onblur="checkBankAccount(),this.v()" required="required"/></div>
		  	  	<div id="errorMsg" class="col-sm-1" style="display:none;margin-top:5px"><span style="color:red">不符合规范</span></div>
		  	  </div>
		  	  <div class="form-group">
			  	<label class="col-sm-1 control-label">备注</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" class="form-control" name="comment"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>