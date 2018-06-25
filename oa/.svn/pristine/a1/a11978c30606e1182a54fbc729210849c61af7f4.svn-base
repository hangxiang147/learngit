<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
	<style type="text/css">
	</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <s:set name="selectedPanel" value="'findTaskList'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="/administration/public/save_handlePublicEvent" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">处理公关事件</h3>
			  <input type="hidden" name="taskId" value="${taskId}">
			  <input type="hidden" name="processInstanceId" value="${processInstanceId}">
  			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">申请人：</label>
			  	<div class="col-sm-1 control-label" style="text-align:left">
			  		${publicEvent.userName}
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:7%">电话：</label>
			  	<div class="col-sm-1 control-label" style="text-align:left">
			  		${publicEvent.phone}
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:13%">事件截止时间：</label>
			  	<div class="col-sm-2 control-label" style="text-align:left">
			  		${publicEvent.deadlineDate}
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">对方联系人：</label>
			  	<div class="col-sm-1 control-label" style="text-align:left">
			  		${publicRelation.otherName}
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:7%">电话：</label>
			  	<div class="col-sm-1 control-label" style="text-align:left">
			  		${publicRelation.otherPhone}
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:13%">职位：</label>
			  	<div class="col-sm-2 control-label" style="text-align:left">
			  		${publicRelation.otherJob}
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">需公关事件说明</label>
			  	<div class="col-sm-5 control-label" style="text-align:left">
					${publicEvent.eventDescription}
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">处理结果说明<span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
			  		<textarea rows="3" class="form-control" required name="comment"></textarea>
			  	</div>
			  </div>
			  <br>
			  <div class="form-group">
			  	<div class="col-sm-1" style="width:13%"></div>
			  	<div class="col-sm-4">
				    <button type="submit" class="btn btn-primary">提交</button>
				    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			 	</div>
			  </div>
			</form>
        </div>
      </div>
      </div>
      </body>
</html>