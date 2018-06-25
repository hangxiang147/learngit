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
		if (businessKey == "Vacation") {
			businessType = "请假申请";
		} else if (businessKey == "Assignment") {
			businessType = "任务分配";
		} else if (businessKey == "Resignation") {
			businessType = "离职申请";
		} else if (businessKey == "Formal") {
			businessType = "转正申请";
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		var selectedPanel = $("#selectedPanel").val();
		window.location.href = "HR/process/taskComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&selectedPanel="+selectedPanel+"&businessType="+businessType; 
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
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'HRCenter'"></s:set>
		<s:set name="selectedPanel" value="'HRCenter'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;"><c:if test="${businessKey == 'Vacation' }">请假审批</c:if>
        	  												 <c:if test="${taskDefKey == 'hr_audit' }">离职审批</c:if>
        	  												 <c:if test="${taskDefKey == 'resignationTransfer' }">离职交接确认</c:if>
        	  												 <c:if test="${businessKey == 'Formal' }">转正审批</c:if>
        	  </h3>
        	  <input type="hidden" id="taskID" value="${taskID}"/>
			  <input type="hidden" id="selectedPanel" value="${selectedPanel }"/>  
			  <input type="hidden" id="businessKey" value="${businessKey}" />      	  
        	  <c:if test="${not empty formFields }">
        	  <c:forEach items="${formFields }" var="formField" varStatus="st">
        	  	<div class="form-group">
        	  		<label class="col-sm-2 control-label">${formField.fieldText}：</label>
        	  		<div class="col-sm-10"><span class="detail-control">${formField.fieldValue}</span></div>
        	  	</div>
        	  </c:forEach>
        	  </c:if>
        	  
        	  <c:if test="${businessKey == 'Vacation' and attachmentSize > 0}">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">附件：</label>
			  	<div class="col-sm-5">
			  		<img src="personal/getVacationAttachment?taskID=${taskID }" alt="附件图片" id="photo" class="img-thumbnail" style="width:270px;height:410px;">
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
			  	<c:if test="${taskDefKey == 'resignationTransfer' }">
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:20px;">同意</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:15px;">不同意</button>
			  		
			  	</c:if>
			  	<c:if test="${businessKey == 'Vacation' or taskDefKey == 'hr_audit' or taskDefKey == 'formalHRAudit'}">
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:20px;">同意</button>
			  		<button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:15px;">不同意</button>
			  	</c:if>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			  
			  <h3 class="sub-header" style="margin-top:0px;">流程状态</h3>
			  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  <c:if test="${not empty task }">
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
			  </c:if>
			  </c:forEach>
			  </c:if>
			</form>
        </div>
      </div>
    </div>
</body>
</html>