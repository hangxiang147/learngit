<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
	<style type="text/css">
	</style>
	<script type="text/javascript">
		function changePublicRelation(obj){
			var selectedObj = $(obj).find("option:selected");
			var phone = selectedObj.attr("data-phone");
			$("#otherPhone").val(phone);
			$.ajax({
				url:'/administration/public/getOurPersonsByOtherPerson',
				data:{'publicRelationId':selectedObj.val()},
				success:function(data){
					var html = '<option value="">请选择</option>';
					data.ourPersons.forEach(function(value, index){
						html += '<option data-phone="'+value.telephone+'" value="'+value.userID+'">'+value.staffName+'</option>';
					});
					$("select[name='handlerId']").html(html);
				}
			});
		}
		function changeHandler(obj){
			$("#ourPhone").val($(obj).find("option:selected").attr("data-phone"));
		}
	</script>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <s:set name="selectedPanel" value="'findTaskList'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="/administration/public/save_matchPublicEvent" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">匹配公关事件</h3>
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
			  	<label class="col-sm-1 control-label" style="width:13%">需公关事件说明</label>
			  	<div class="col-sm-5 control-label" style="text-align:left">
					${publicEvent.eventDescription}
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">匹配关系人<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
					<select name="publicRelationId" class="form-control" required onchange="changePublicRelation(this)">
						<option value="">请选择</option>
						<c:forEach items="${publicRelations}" var="publicRelation">
							<option data-phone="${publicRelation.otherPhone}" value="${publicRelation.id}">${publicRelation.otherName}-${publicRelation.otherJob}</option>
						</c:forEach>
					</select>
			  	</div>
			  	<label class="col-sm-1 control-label">联系电话</label>
			  	<div class="col-sm-2">
			  		<input disabled class="form-control" id="otherPhone">
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:13%">我方联系人<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
					<select name="handlerId" class="form-control" required onchange="changeHandler(this)">
						
					</select>
			  	</div>
			  	<label class="col-sm-1 control-label">联系电话</label>
			  	<div class="col-sm-2">
			  		<input disabled class="form-control" id="ourPhone">
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