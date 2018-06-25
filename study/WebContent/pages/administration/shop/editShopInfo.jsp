<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForJS.tld" prefix="jf" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
</style>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	function checkInfo(){
		if(!checkAlipayAccount() || !checkPhone()){
			return false;
		}
	}
	//验证支付宝账号：邮箱或者手机号
	function checkAlipayAccount(){
		var accountNumber = $("input[name='shopInfo.registerAlipayAccount']").val();
		if(!accountNumber){
			return true;
		}
		var phoneReg = new RegExp(/^1\d{10}$/);
		var phoneFlag = false;
		var mailFlag = false;
		if(phoneReg.test(accountNumber)){
			phoneFlag = true;
		}
		var mailReg = new RegExp(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/);
		if(mailReg.test(accountNumber)){
			mailFlag = true;
		}
		//邮箱和手机号都不符合
		if(!phoneFlag && !mailFlag){
			layer.alert("支付宝账号格式不对，请检查",{offset:'100px'});
			return false;
		}
		return true;
	}
	function checkPhone(){
		var registerPhone = $("input[name='shopInfo.registerTelephone']").val();
		var phoneReg = new RegExp(/^1\d{10}$/);
		if(!phoneReg.test(registerPhone)){
			layer.alert("注册手机号格式不对，请检查",{offset:'100px'});
			return false;
		}
		var reservePhone = $("input[name='shopInfo.reserveTelephone']").val();
		if(!reservePhone){
			return true;
		}else if(!phoneReg.test(reservePhone)){
			layer.alert("预留手机号格式不对，请检查",{offset:'100px'});
			return false;
		}
		return true;
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
      	<s:set name="panel" value="'shopManage'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px">编辑店铺信息</h3>
				<form  id="versionInfo" action="/administration/shopManage/save_saveShopInfo"
					method="post" class="form-horizontal" onsubmit="return checkInfo()">
					<input type="hidden" name="shopInfo.id" value="${shopInfo.id}">
					<input type="hidden" name="shopInfo.addTime" value="${shopInfo.addTime}">
					<input type="hidden" name="shopInfo.shopStatus" value="${shopInfo.shopStatus}">
					<s:token></s:token>
					<div class="form-group">
					<label class="col-sm-1 control-label" style="width:12%">店铺名称<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input autoComplete="off" class="form-control" required name="shopInfo.shopName" value="${shopInfo.shopName}">
					</div>
					<label class="col-sm-1 control-label" style="width:13%">开店时间</label>
					<div class="col-sm-3">
						<input autoComplete="off" class="form-control" name="shopInfo.openDate" value="${shopInfo.openDate}"  onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })">
					</div>
					</div>
					<div class="form-group">
					<label class="col-sm-1 control-label" style="width:12%">账号<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input autoComplete="off" class="form-control" required name="shopInfo.account" value="${shopInfo.account}">
					</div>
					<label class="col-sm-1 control-label" style="width:13%">密码<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input autoComplete="off" class="form-control" required name="shopInfo.pwd" value="${shopInfo.pwd}">
					</div>	
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label" style="width:12%">注册公司</label>
						<div class="col-sm-3">
							<input autoComplete="off" class="form-control" name="shopInfo.registerCompany" value="${shopInfo.registerCompany}">
						</div>
						<label class="col-sm-1 control-label" style="width:13%">营业执照注册号</label>
						<div class="col-sm-3">
							<input autoComplete="off" class="form-control" name="shopInfo.registerNum" value="${shopInfo.registerNum}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label" style="width:12%">注册手机号<span style="color:red"> *</span></label>
						<div class="col-sm-3">
							<input autoComplete="off" class="form-control" name="shopInfo.registerTelephone" required
							 oninput="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" onblur="this.v()"
							 value="${shopInfo.registerTelephone}">
						</div>
						<label class="col-sm-1 control-label" style="width:13%">注册手机号属主<span style="color:red"> *</span></label>
						<div class="col-sm-3">
							<input class="form-control" autoComplete="off" required name="shopInfo.regPhoneOwner" value="${shopInfo.regPhoneOwner}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label" style="width:12%">预留手机号</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="shopInfo.reserveTelephone"
							 oninput="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" onblur="this.v()"
							 value="${shopInfo.reserveTelephone}">
						</div>
						<div class="col-sm-1">
							<input id="phoneStop" type="checkbox" class="checkboxClass" value="停用" ${shopInfo.reservePhoneStatus=='停用'?'checked':''} name="shopInfo.reservePhoneStatus" style="margin-bottom:-2%"><label for="phoneStop">&nbsp;停用</label>
						</div>
						<label class="col-sm-1 control-label" style="width:13%">预留手机号属主</label>
						<div class="col-sm-3">
							<input class="form-control" autoComplete="off" name="shopInfo.reservePhoneOwner" value="${shopInfo.reservePhoneOwner}">
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label" style="width:12%">注册支付宝账户</label>
						<div class="col-sm-3">
							<input autoComplete="off" class="form-control" name="shopInfo.registerAlipayAccount" value="${shopInfo.registerAlipayAccount}">
						</div>
						<label class="col-sm-1 control-label" style="width:13%">注册银行卡账号</label>
						<div class="col-sm-3">
							<input autoComplete="off" class="form-control" name="shopInfo.registerBankAccount" value="${shopInfo.registerBankAccount}">
						</div>
					</div>
				<br>
				<div class="col-sm-1" style="width:12%"></div>
				<button type="submit" class="btn btn-primary">提交</button>
				<button type="button" class="btn btn-default" onclick="history.go(-1)" style="margin-left:2%">返回</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>