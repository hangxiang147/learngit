<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startTransferProperty" method="post" id="form_"class="form-horizontal">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起资产调拨申请</h3> 			 
  			  	  <input style="display:none" name="transferPropertyVo.userName"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="transferPropertyVo.userID"  value="${staff.userID}"/>  
  			  	  <input style="display:none" name="transferPropertyVo.assetId" value="${assetId}"/>
  			  	  <input style="display:none" name="recipientId" value="${recipientId}"/>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">资产名称</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="transferPropertyVo.assetName" value="${assetName}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">资产编号</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="transferPropertyVo.assetNum" value="${assetNum}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">规格型号</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="transferPropertyVo.model" value="${model}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">类型</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="transferPropertyVo.assetType" value="${fn:trim(assetType)}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">调出单位</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="transferPropertyVo.oldCompany" value="${oldCompany}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%;margin-left:-0.68%">调入单位<span style="color:red"> *</span></label>
			  	<div class="col-sm-4">
			  		<select class="form-control" name="transferPropertyVo.newCompany" required>
			  			<option value="">请选择</option>
			  			<c:forEach items="${companyVOs}" var="company">
			  			<option value="${company.companyName}">${company.companyName}</option>
			  			</c:forEach>
			  		</select>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">数量</label>
			  	<div class="col-sm-2">
			  		<input type="number" onblur="autoWriteMoney()" autoComplete="off" min="1" max="10000" class="form-control" name="transferPropertyVo.number">
			  	</div>
			  	<label class="col-sm-1 control-label">单价</label>
			  	<div class="col-sm-2">
			  		<input type="text"  onblur="autoWriteMoney()" autoComplete="off" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"
			  		class="form-control" name="transferPropertyVo.unitPrice">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;width:1%">元</div>
			  	<label class="col-sm-1 control-label">金额</label>
			  	<div class="col-sm-2">
			  		<input type="text"  autoComplete="off" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"
			  		class="form-control" name="transferPropertyVo.money">
			  	</div>
			  	<div class="col-sm-1" style="margin-top: 0.6%;width:1%">元</div>
			  </div>
			  <div class="form-group">
			  <label for="reason" class="col-sm-1 control-label" style="width:9%;margin-left:-0.68%">调拨原因<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-4">
			  		<textarea rows="3" cols="4" class="form-control" required name="transferPropertyVo.transferReason"></textarea>
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
      <script type="text/javascript">
       	$(function(){
      		$("#form_").submit(function(){
      			cancelDisabled();
      		});
      	});
      	function cancelDisabled(){
      		$("input").each(function(){
      			$(this).removeAttr("disabled");
      		});
      	}
      	function autoWriteMoney(){
      		var num = $("input[name='transferPropertyVo.number']").val();
      		var unitPrice = $("input[name='transferPropertyVo.unitPrice']").val();
      		if(num!='' && unitPrice!=''){
      			$("input[name='transferPropertyVo.money']").val(parseFloat(num)*parseFloat(unitPrice));
      		}
      	}
      </script>
      </body>
</html>