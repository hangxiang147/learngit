<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	function taskComplete(result) {
		var businessKey = $("#businessKey").val();
		var businessType = "";
		if (businessKey == "Email") {
			businessType = "公司邮箱申请";
		} else if (businessKey == "Card") {
			businessType = "工牌申领";
		} else if(bussinessKey="BussinessTrip"){
			businessType = "出差预约申请";
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		var selectedPanel = $("#selectedPanel").val();
		window.location.href = "administration/process/taskComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&selectedPanel="+selectedPanel+"&businessType="+businessType;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function openMailBox(result) {
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		var selectedPanel = $("#selectedPanel").val();
		var confirmAddress = $("#confirmAddress").val();
		var originalPassword = $("#originalPassword").val();
		var loginUrl = $("#loginUrl").val();
		if (result == 13) {
			if (confirmAddress.replace(/^\s+|\s+$/g,"").length<=0) {
				showAlert("请填写开通的邮箱地址！");
				return;
			}
			if (originalPassword.replace(/^\s+|\s+$/g, "").length<=0) {
				showAlert("请填写初始密码！");
				return;
			}
			if (loginUrl.replace(/^\s+|\s+$/g, "").length<=0) {
				showAlert("请填写登录网址！");
				return;
			}
		}
		window.location.href = "administration/process/openMailBox?taskID="+taskID+"&result="+result+"&comment="+comment+"&selectedPanel="+selectedPanel+
								"&confirmAddress="+confirmAddress+"&originalPassword="+originalPassword+"&loginUrl="+loginUrl; 
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    height: 34px;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'audit'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;"><c:if test="${taskDefKey == 'emailAudit' or taskDefKey == 'cardAudit'}">行政审批</c:if>
        	  												 <c:if test="${taskDefKey == 'makeCard' }">工牌制作</c:if>
        	  												 <c:if test="${taskDefKey == 'openMailBox' }">邮箱开通</c:if></h3>
        	  <input type="hidden" id="taskID" value="${taskID}"/>
        	  <input type="hidden" id="selectedPanel" value="${selectedPanel }"/>  
			  <input type="hidden" id="businessKey" value="${businessKey }" />     	  
        	  <c:if test="${not empty formFields }"> 
        	  <c:forEach items="${formFields }" var="formField" varStatus="st">
        	  	<div class="form-group">
        	  		<label class="col-sm-2 control-label">${formField.fieldText}：</label>
        	  		<div class="col-sm-10"><span class="detail-control">${formField.fieldValue}</span></div>
        	  	</div>
        	  </c:forEach>
        	  </c:if>
        	  
        	  <c:if test="${not empty cardVO }">
        	  <div class="form-group">
        	    <label class="col-sm-2 control-label">工号：</label>
        	    <div class="col-sm-2"><span class="detail-control">${cardVO.staffNumber }</span></div>
        	    <label class="col-sm-2 control-label">花名：</label>
        	    <div class="col-sm-2"><span class="detail-control">${cardVO.nickName }</span></div>
       		  </div>
       		  <div class="form-group">
        	    <label class="col-sm-2 control-label">地区：</label>
        	    <div class="col-sm-2"><span class="detail-control">${cardVO.companyName }</span></div>
        	    <label class="col-sm-2 control-label">部门：</label>
        	    <div class="col-sm-2"><span class="detail-control">${cardVO.departmentName }</span></div>
        	    <label class="col-sm-2 control-label">职务：</label>
        	    <div class="col-sm-2"><span class="detail-control">${cardVO.positionName }</span></div>
       		  </div>
        	  <div class="form-group">
        	    <label class="col-sm-2 control-label">附件：</label>
        	    <div class="col-sm-5">
        		<s:iterator id="attachment" value="#request.cardVO.attachmentFileName" status="st">
      				<span><img style="height:100px;" alt="" src="http://www.zhizaolian.com:9000/administration/card/<s:property value="#attachment"/>"></span> 
       			</s:iterator>
       			</div>
       		  </div>
     		  </c:if>
        	  
        	  <c:if test="${taskDefKey == 'openMailBox'}">
        	  <div class="form-group">
			  	<label for="confirmAddress" class="col-sm-2 control-label">邮箱地址：</label>
			  	<div class="col-sm-5">
			  		<input type="text" class="form-control" id="confirmAddress" required="required" />
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label for="originalPassword" class="col-sm-2 control-label">初始密码：</label>
			  	<div class="col-sm-5">
			  		<input type="text" class="form-control" id="originalPassword" required="required" />
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label for="loginUrl" class="col-sm-2 control-label">登录网址：</label>
			  	<div class="col-sm-5">
			  		<input type="text" class="form-control" id="loginUrl" required="required" />
			  	</div>
			  </div>
			  </c:if>
        	  
        	  <div class="form-group">
			  	<label class="col-sm-2 control-label">备注：</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" class="form-control"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<c:if test="${taskDefKey == 'emailAudit' or taskDefKey == 'cardAudit'}">
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:15px;">同意</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:20px;">拒绝</button>
			  	</c:if>
			  	<c:if test="${taskDefKey == 'makeCard'}">
			  	    <button type="button" class="btn btn-primary" onclick="taskComplete(14)" style="margin-left:20px;">失败</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(13)" style="margin-left:15px;">完成</button>
			  	</c:if>
			  	<c:if test="${taskDefKey == 'openMailBox'}">
			  	    <button type="button" class="btn btn-primary" onclick="openMailBox(14)" style="margin-left:20px;">失败</button>
			  		<button type="button" class="btn btn-primary" onclick="openMailBox(13)" style="margin-left:15px;">完成</button>
			  	</c:if>
			  	<c:if test="${taskDefKey == 'trip_approval'}">
			  	    <button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:15px;">同意</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:20px;">拒绝</button>
			  	</c:if>
			  	<c:if test="${taskDefKey == 'buy_ticket'}">
			  	    <button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:15px;">购买完成</button>
			  	</c:if>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			  
			  <h3 class="sub-header" style="margin-top:0px;">流程状态  </h3>
			  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
			  	<div class="col-sm-10">
			  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
			  	</div>
			  	<c:if test="${not empty comments }">
        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
        	  	<c:if test="${comment.taskID == task.taskID }">
        	  		<div class="col-sm-2"></div>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${comment.content }</span>
				  	</div>
        	  	</c:if>
        	  	</c:forEach>
        	  	</c:if>
			  </div>
			  </c:forEach>
			  </c:if>
			</form>
        </div>
      </div>
    </div>
</body>
</html>