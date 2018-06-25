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
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startApplyPublicEvent" method="post" id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起公关申请</h3>
  			  <input style="display:none" name="publicRelationEvent.userName"  value="${staff.lastName}"/>
  			  <input style="display:none" name="publicRelationEvent.userID"  value="${staff.userID}"/>
  			  <input style="display:none" name="publicRelationEvent.userId"  value="${staff.userID}"/>
  			  <input style="display:none" name="publicRelationEvent.phone"  value="${staff.telephone}"/>
  			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">申请人</label>
			  	<div class="col-sm-1 control-label" style="text-align:left">
			  		${staff.lastName}
			  	</div>
			  	<label class="col-sm-1 control-label" style="width:6%">电话</label>
			  	<div class="col-sm-1 control-label" style="text-align:left">
			  		${staff.telephone}
			  	</div>
			  </div>
  			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">事件截止时间<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input autoComplete="off" class="form-control" required name="publicRelationEvent.deadlineDate"
			  		 onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d %H:%m:%s'})">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">需公关事件说明<span style="color:red"> *</span></label>
			  	<div class="col-sm-5">
					<textarea required name="publicRelationEvent.eventDescription" rows="3" class="form-control"></textarea>
			  	</div>
			  </div>
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