<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function confirmComplete(result) {
		var beginDate = $("#beginDate").val();
		if (result == 3 && beginDate == '') {
			//接收任务时，必须填写开始时间
			showAlert("请填写开始时间！");
			return;
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/confirmAssignment?taskID="+taskID+"&result="+result+"&comment="+comment+"&beginDate="+beginDate;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function taskComplete(result) {
		var businessKey = $("#businessKey").val();
		var businessType = "";
		if (businessKey == "Vacation") {
			businessType = "请假申请";
		} else if (businessKey == "Assignment") {
			businessType = "任务分配";
		} else if (businessKey == "Resignation") {
			businessType = "离职申请";
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/taskComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function updateAssignment() {
		var taskID = $("#taskID").val();
		window.location.href = "personal/updateAssignment?taskID="+taskID;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function inspectAssignment() {
		var taskID = $("#taskID").val();
		var score = $("#score").val();
		var comment = $("#comment").val();
		window.location.href = "personal/inspectAssignment?taskID="+taskID+"&score="+score+"&comment="+comment;
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
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_submitFormal" method="post" class="form-horizontal">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">转正申请表</h3>
        	  <input type="hidden" id="taskID" value="${taskID}" name="taskID"/>
        	  <div class="form-group">
        	  	<label class="col-sm-2 control-label">姓名：</label>
        	  	<div class="col-sm-10">
        	  		<span class="detail-control">${formalVO.requestUserName }</span>
        	  	</div>
        	  </div>
        	  <div class="form-group">
        	  	<label class="col-sm-2 control-label">申请时间：</label>
        	  	<div class="col-sm-10">
        	  		<span class="detail-control">${formalVO.requestDate }</span>
        	  	</div>
        	  </div>
        	  <div class="form-group">
        	  	<label class="col-sm-2 control-label">转正后职级：</label>
        	  	<div class="col-sm-10">
        	  		<span class="detail-control">${formalVO.gradeName }</span>
        	  	</div>
        	  </div>
        	  <div class="form-group">
        	  	<label class="col-sm-2 control-label">薪资标准：</label>
        	  	<div class="col-sm-10">
        	  		<span class="detail-control">${formalVO.salary }</span>
        	  	</div>
        	  </div>
        	  <div class="form-group">
        	  	<label class="col-sm-2 control-label">社保费缴纳标准：</label>
        	  	<div class="col-sm-10">
        	  		<span class="detail-control">${formalVO.socialSecurity }</span>
        	  	</div>
        	  </div>
        	  <div class="form-group">
			  	<label class="col-sm-2 control-label">申请转正日期：</label>
			  	<div class="col-sm-2">
	    			<input type="text" class="form-control" id="formalDate" name="formalVO.requestFormalDate" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
			  </div>
        	  <div class="form-group">
			  	<label class="col-sm-2 control-label">试用期工作小结：</label>
			  	<div class="col-sm-5">
			  		<textarea id="summary" class="form-control" name="formalVO.summary"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">备注：</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" class="form-control" name="comment"></textarea>
			  	</div>
			  </div>
			 
			  <div class="form-group">
			  	<button type="submit" id="submitButton" class="btn btn-primary">提交</button>	
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