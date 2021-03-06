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
        	<form action="personal/save_startCarveChop" method="post"  id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起印章刻制申请</h3> 			 
  			  	  <input style="display:none" name="carveChopVo.userName"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="carveChopVo.userID"  value="${staff.userID}"/>  
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="margin-top:-1%;width:9%">申请刻制印章名称（全称）<span style="color:red"> *</span></label>
			  	<div class="col-sm-4">
			  		<input autoComplete="off" required type="text" class="form-control" name="carveChopVo.chopName" maxLength="50">
			  	</div>
			  </div>
			  <div class="form-group">
			  <label for="reason" class="col-sm-1 control-label" style="width:9%">印章类型<span style="color:red"> *</span></label>
			  	<div class="col-sm-2">
			  		<select class="form-control" name="carveChopVo.chopType" required>
			  			<option value="">请选择</option>
			  			<option value="公章">公章</option>
			  			<option value="合同专用章">合同专用章</option>
			  			<option value="法人章">法人章</option>
			  			<option value="财务专用章">财务专用章</option>
			  			<option value="发票专用章">发票专用章</option>
			  		</select>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">刻章理由<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  	<select class="form-control" name="carveChopVo.carveReason" required>
			  		<option value="">请选择</option>
			  		<option value="新制">新制</option>
			  		<option value="遗失">遗失</option>
			  		<option value="遗失">损坏</option>
			  		<option value="更名">更名</option>
			  		<option value="更换材质">更换材质</option>
			  	</select>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:9%">备注<span style="font-size:5px">（限200字）</span></label>
			  	<div class="col-sm-4">
			  		<textarea rows="3" cols="4" class="form-control" name="carveChopVo.remark" maxLength="200"></textarea>
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
      </body>
</html>