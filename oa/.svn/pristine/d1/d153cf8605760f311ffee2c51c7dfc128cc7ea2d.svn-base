<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	function taskComplete(result) {
		var taskID = $("#taskID").val();
		window.location.href = "attendance/vacationComplete?taskID="+taskID+"&result="+result+"&comment="+comment; 
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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/attendance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;">请假审批</h3>
        	  <input type="hidden" id="taskID" value="${taskID}"/>
        	  
        	  <c:if test="${not empty formFields }">
        	  <c:forEach items="${formFields }" var="formField" varStatus="st">
        	  	<div class="form-group">
        	  		<label class="col-sm-2 control-label">${formField.fieldText}：</label>
        	  		<div class="col-sm-10"><span class="detail-control">${formField.fieldValue}</span></div>
        	  	</div>
        	  </c:forEach>
        	  </c:if>
        	  
        	  <div class="form-group">
        	  	<label class="col-sm-2 control-label">评论：</label>
        	  	<c:if test="${not empty comments }">
        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
        	  		<c:if test="${st.index != 0 }"><div class="col-sm-2"></div></c:if>
        	  		<div class="col-sm-10">
        	  			<span class="detail-control">${comment.userName }（${comment.time }）：${comment.content }</span>
        	  		</div>
        	  	</c:forEach>
        	  	</c:if>
        	  </div>
        	  
        	  <div class="form-group">
			  	<label class="col-sm-2 control-label">备注：</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" class="form-control"></textarea>
			  	</div>
			  </div>
			  <div class="form-group">
			    <button type="button" class="btn btn-primary" onclick="taskComplete(1)" style="margin-left:20px;">同意</button>
			    <button type="button" class="btn btn-primary" onclick="taskComplete(2)" style="margin-left:15px;">不同意</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>