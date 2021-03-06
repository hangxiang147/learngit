<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startPurchaseProperty" method="post"  onsubmit="return checkStorageUser()" id="form_"class="form-horizontal">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起财产购置申请</h3> 	
        	  <br>		 
  			  	  <input style="display:none" name="purchasePropertyVo.userName"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="purchasePropertyVo.userID"  value="${staff.userID}"/>  
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="margin-top:-2%;width:9%">固定资产和低值易耗品名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input autoComplete="off" required type="text" maxLength="30" class="form-control" name="purchasePropertyVo.propertyName" >
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">数量<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  	<input autoComplete="off" type="number" min="1" max="10000" required class="form-control" name="purchasePropertyVo.number" >
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">预算总价<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  	<input autoComplete="off" required onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"
			  	class="form-control" name="purchasePropertyVo.budgetPrice">
			  	</div>
			  	<div style="margin-top: 6px">&nbsp;元</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="margin-top:-0.5%;width:9%">预算</label>
			  	<div class="col-sm-2">
			  	<input type="radio" checked name="purchasePropertyVo.exceedBudget" value="0" style="height:15px;width:15px">&nbsp;<label>内</label>
			  	<input type="radio" name="purchasePropertyVo.exceedBudget" value="1" style="height:15px;width:15px">&nbsp;<label>外</label>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">型号规格</label>
			  	<div class="col-sm-3">
			  		<input id="fileNum" autoComplete="off" class="form-control" name="purchasePropertyVo.model" >
			  	</div>
			  </div>
			  <div class="form-group">
 			  	<label class="col-sm-1 control-label" style="width:9%">使用地点<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input id="fileNum" required autoComplete="off" maxLength="30" class="form-control" name="purchasePropertyVo.place" >
			  	</div>
			  </div>
			  <div class="form-group">
 			  	<label class="col-sm-1 control-label" style="width:9%">使用(保管)人<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input id="storage" required autoComplete="off" type="text" onkeyup="checkEmpty()" class="form-control" 
			  		name="purchasePropertyVo.storageUserName" >
			  		<input type="hidden" name="purchasePropertyVo.storageUserId">
			  	</div>
			  </div>
			  <div class="form-group">
 			  	<label class="col-sm-1 control-label" style="width:9%">购置原因<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
			  		<textarea required maxLength="200" rows="3" class="form-control" name="purchasePropertyVo.reason"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" style="margin-left:5%">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      <script src="/assets/js/layer/layer.js"></script>
      <script src="/assets/js/underscore-min.js"></script>
      <script src="/assets/js/require/require2.js"></script>
      <script>
  	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#storage'),function ($item){
			$("input[name='purchasePropertyVo.storageUserId']").val($item.data("userId"));
		});
	});
  	function checkEmpty(){
  		if($("#storage").val()==''){
  			$("input[name='purchasePropertyVo.storageUserId']").val('');
  		}
  	}
	$(document).click(function (event) {
		if ($("input[name='purchasePropertyVo.storageUserId']").val()=='')
		{
			$("#storage").val("");
		}
	}); 
  	function checkStorageUser(){
  		if($("input[name='purchasePropertyVo.storageUserId']").val()==''){
  			layer.alert("保管人不存在",{"offset":"100px"});
  			return false;
  		}
  		Load.Base.LoadingPic.FullScreenShow(null);
  	}
      </script>
      </body>
</html>