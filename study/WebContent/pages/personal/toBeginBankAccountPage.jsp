<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style>
.float_div{
	position:fixed;
	width:380px;
	right:0px;
	top:70px;
	z-index:100; 
}
.form-group {
    margin-bottom: 30px;
    margin-top: 30px;
}
.col-sm-1 {
	padding-right: 0px;
	padding-left: 0px;
}

.detail-control {
	display: block;
    width: 100%;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555; 
}
.inputout1{position:relative;}
.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
.text_down1 ul{padding:2px 10px;}
.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
.text_down1 ul li span{color:#cc0000;}
</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startBankAccount" method="post" onsubmit="return checkError()" id="form_"class="form-horizontal" enctype="multipart/form-data" >  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起开设、变更及撤销银行账户</h3> 			 
  			  	  <input style="display:none" name="changeBankAccountVo.userID"  value="${staff.userID}"/>
  			  	  <input style="display:none" name="changeBankAccountVo.userName"  value="${staff.lastName}"/>  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">账户所属单位名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-4" id="find_div"><input required="required" autoComplete="off" 
			  	type="text" class="form-control" name="changeBankAccountVo.accountCompanyName" maxLength="30"></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">账户类别<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
		  	 	<select class="form-control"  name="changeBankAccountVo.accountType" required="required">
			      		<option value="一般账户">一般账户</option>
			      		<option value="基本账户">基本账户</option>
			      		<option value="临时账户">临时账户</option>
			      		<option value="专用账户">专用账户</option>
				</select>
				</div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label" >开户行全称<span style="color:red"> *</span></label>
			  	<div class="col-sm-4" id="find_div"><input required="required" autoComplete="off" type="text" 
			  	class="form-control" name="changeBankAccountVo.bankName"  maxLength="30"></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">账号<span style="color:red"> *</span></label>
			  	<div class="col-sm-4" id="find_div"><input id="accountNumber" required="required" onblur="checkBankAccount(),this.v()" autoComplete="off" 
			  	class="form-control" name="changeBankAccountVo.accountNumber" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" ></div>
			  	<div id="errorMsg" class="col-sm-1" style="display:none;margin-top:5px"><span style="color:red">不符合规范</span></div>
			  </div>
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label">类型<span style="color:red"> *</span></label>
 			  	<div class="col-sm-2">
 			  		<select class="form-control"  name="changeBankAccountVo.applyType" required="required" onchange="changeType()">
				      		<option value="开立">开立</option>
				      		<option value="变更">变更</option>
				      		<option value="撤销">撤销</option>
					</select>
 			  	</div>
			  </div>
			  <div id="change" style="display:none">
			  <div class="form-group">
			   	<label for="reason" class="col-sm-1 control-label">变更事项<span style="font-size:5px">（限200字）</span></label>
			   	<div class="col-sm-4">
			  	<textarea rows="4" class="form-control" name="changeBankAccountVo.changeItem" maxLength="200"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			   	<label for="reason" class="col-sm-1 control-label">变更后信息<span style="font-size:5px">（限200字）</span></label>
			   	<div class="col-sm-4">
			  	<textarea rows="4" class="form-control" name="changeBankAccountVo.afterChangeInfo" maxLength="200"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			   	<label for="reason" class="col-sm-1 control-label">变更原因<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			   	<div class="col-sm-4">
			  	<textarea rows="4" class="form-control" name="changeBankAccountVo.changeReason" maxLength="200"></textarea>
			  	</div>
			  </div>
			  </div>
			  <div id="new">
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">开户依据<span style="font-size:5px">（限200字）</span></label>
			  	<div class="col-sm-4">
			  	<textarea rows="4" class="form-control" name="changeBankAccountVo.newAccountReason" maxLength="200"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">账户用途<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-4">
			  	<textarea rows="4" class="form-control" required name="changeBankAccountVo.accountUse" maxLength="200"></textarea>
			  	</div>
			  </div>
			  </div>
			  <div id="delete" style="display:none">
			  	<div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">销户原因<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-4">
			  	<textarea rows="4" class="form-control" name="changeBankAccountVo.deleteAccountReason" maxLength="200"></textarea>
			  	</div>
			  	</div>
			  	<div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">资金去向<span style="font-size:5px">（限200字）</span></label>
			  	<div class="col-sm-4">
			  	<textarea rows="4" class="form-control" name="changeBankAccountVo.moneyWhere" maxLength="200"></textarea>
			  	</div>
			  	</div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" style="margin-left:10px">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      <script src="/assets/js/underscore-min.js"></script>
      <script type="text/javascript">
      	function changeType(){
      		var type = $("select[name='changeBankAccountVo.applyType']").find("option:selected").val();
      		if(type=="开立"){
      			$("#new").css("display","block");
      			$("#change").css("display","none");
      			$("textarea[name='changeBankAccountVo.accountUse']").attr("required","required");
      			$("textarea[name='changeBankAccountVo.changeItem']").val("");
      			$("textarea[name='changeBankAccountVo.afterChangeInfo']").val("");
      			$("textarea[name='changeBankAccountVo.changeReason']").val("");
      			$("textarea[name='changeBankAccountVo.changeReason']").removeAttr("required");
      			$("#delete").css("display","none");
      			$("textarea[name='changeBankAccountVo.deleteAccountReason']").val("");
      			$("textarea[name='changeBankAccountVo.deleteAccountReason']").removeAttr("required");
      			$("textarea[name='changeBankAccountVo.moneyWhere']").val("");
      		}else if(type=="变更"){
      			$("#change").css("display","block");
      			$("#new").css("display","none");
      			$("textarea[name='changeBankAccountVo.changeReason']").attr("required","required");
      			$("textarea[name='changeBankAccountVo.newAccountReason']").val("");
      			$("textarea[name='changeBankAccountVo.newAccountReason']").removeAttr("required");
      			$("textarea[name='changeBankAccountVo.accountUse']").val("");
      			$("#delete").css("display","none");
      			$("textarea[name='changeBankAccountVo.deleteAccountReason']").val("");
      			$("textarea[name='changeBankAccountVo.deleteAccountReason']").removeAttr("required");
      			$("textarea[name='changeBankAccountVo.moneyWhere']").val("");
      		}else if(type=="撤销"){
      			$("#delete").css("display","block");
      			$("#new").css("display","none");
      			$("textarea[name='changeBankAccountVo.deleteAccountReason']").attr("required","required");
      			$("textarea[name='changeBankAccountVo.newAccountReason']").val("");
      			$("textarea[name='changeBankAccountVo.accountUse']").val("");
      			$("textarea[name='changeBankAccountVo.accountUse']").removeAttr("required");
      			$("#change").css("display","none");
      			$("textarea[name='changeBankAccountVo.changeItem']").val("");
      			$("textarea[name='changeBankAccountVo.afterChangeInfo']").val("");
      			$("textarea[name='changeBankAccountVo.changeReason']").val("");
      			$("textarea[name='changeBankAccountVo.changeReason']").removeAttr("required");
      		}
      	}
      	function checkBankAccount(){
      		var accountNumber = $("#accountNumber").val();
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
      			return false;
      		}
      		Load.Base.LoadingPic.FullScreenShow(null);
      	}
      </script>
      </body>
</html>