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
        	<form action="personal/save_startHandleProperty" method="post" id="form_"class="form-horizontal">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起资产处置申请</h3> 			 
  			  	  <input style="display:none" name="handlePropertyVo.userName"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="handlePropertyVo.userID"  value="${staff.userID}"/>  
  			  	  <input style="display:none" name="recipientId" value="${recipientId}"/>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">资产名称</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="handlePropertyVo.assetName" value="${assetName}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">资产编号</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="handlePropertyVo.assetNum" value="${assetNum}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">规格型号</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="handlePropertyVo.model" value="${model}">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">使用部门</label>
			  	<div class="col-sm-4">
			  		<input disabled type="text" class="form-control" name="handlePropertyVo.useDepartment" value="${useDepartment}">
			  	</div>
			  </div>
			  <div class="form-group">
			  <label class="col-sm-1 control-label" style="width:9%">处置原因<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-4">
			  		<textarea rows="3" cols="4" required class="form-control" name="handlePropertyVo.handleReason" maxLength="200"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">处置方案<span style="font-size:5px">（限200字）</span><span style="color:red"> *</span></label>
			  	<div class="col-sm-4">
			  		<textarea rows="3" cols="4" required class="form-control" name="handlePropertyVo.handleCase" maxLength="200"></textarea>
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
      			Load.Base.LoadingPic.FullScreenShow(null);
      		});
      	});
      	function cancelDisabled(){
      		$("input").each(function(){
      			$(this).removeAttr("disabled");
      		});
      	}
      </script>
      </body>
</html>