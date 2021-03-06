<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

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
        	<form class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;">流程详情</h3>
        	  <c:if test="${not empty formFields }">
        	  <c:forEach items="${formFields }" var="formField" varStatus="st">
        	  	<div class="form-group">
        	  		<label class="col-sm-2 control-label">${formField.fieldText}：</label>
        	  		<div class="col-sm-10">
        	  			<c:set var="fieldValue" value="${formField.fieldValue }"></c:set>
       	  				<% request.setAttribute("vEnter", "\n"); %> 
        	  			<span class="detail-control"><c:out value="${fn:replace(fieldValue, vEnter, '<br>')}" escapeXml="false"/></span>
        	  		</div>
        	  	</div>	
        	  </c:forEach>
        	  </c:if>
        	  
        	  <h3 class="sub-header" style="margin-top:0px;">流程状态</h3>
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
			  
			  <div class="form-group">
			    <a class="btn btn-primary" href="javascript:history.go(-1);" style="margin-left:15px;">返回</a>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>