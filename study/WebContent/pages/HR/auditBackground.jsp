<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function saveAuditResult(result) {
		var taskID = $("#taskID").val();
		window.location.href = "HR/process/saveAuditResult?taskID="+taskID+"&result="+result;
		Load.Base.LoadingPic.FullScreenShow(null);
	}

	function changeAuditState(auditValue) {
		var auditStatus = "${staffVO.auditStatus}";
		var auditStatesNode = document.getElementById("auditStatusForm");
		 auditStatesNode.value= ""+auditValue;
		var auditFormNode = document.getElementById("auditForm");
		auditFormNode.submit();
		return;
	}
</script>
<style type="text/css">
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
		<s:set name="panel" value="'HRCenter'"></s:set>
		<s:set name="selectedPanel" value="'HRCenter'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<div class="resumebody">
        	<input type="hidden" id="taskID" value="${taskID }" />
				<div class="summary">
				<h1>${staffAuditVO.auditUserName }</h1>
				<div class="blank10"></div>
				<dl><dt>学历证书编号：</dt><dd>${staffAuditVO.educationID }</dd></dl>
				<dl><dt>学位证书编号：</dt><dd>${staffAuditVO.degreeID }</dd></dl>
				<div class="clear"></div>
				<p>案底说明：${staffAuditVO.criminalRecord }</p>
				</div>
				<c:if test="${not empty staffAuditVO.company }">
				<div class="work-experience-lm">工作经验</div>
				<s:iterator id="company" value="#request.staffAuditVO.company" status="st">
					<div class="work-experience">
						<%-- <h6><s:property value="#company" />&nbsp;&nbsp;|&nbsp;&nbsp;<s:property value="#request.staffAuditVO.years[#st.index]" />年</h6> --%>
						<h6><s:property value="#company" />&nbsp;&nbsp;|&nbsp;&nbsp;<s:property value="#request.staffAuditVO.beginDate[#st.index]" />&nbsp;至&nbsp;<s:property value="#request.staffAuditVO.endDate[#st.index]" /></h6>
						<p>证明人：<s:property value="#request.staffAuditVO.referee[#st.index]"/>&nbsp;&nbsp;|&nbsp;&nbsp;证明人电话：<s:property value="#request.staffAuditVO.telephone[#st.index]" /></p>
						<h6>工作描述</h6>
						<p><s:property value="#request.staffAuditVO.description[#st.index]"/></p>
					</div>
	        	</s:iterator>
				</c:if>
				<div class="row col-sm-9 col-sm-offset-7 col-md-10 col-md-offset-4 mt20" >
		     	 	<button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'">返回</button>
		       		<button type="button" class="btn btn-primary"  onclick="saveAuditResult(2)">审核不通过</button> 
		        	<button type="button" class="btn btn-primary"  onclick="saveAuditResult(1)">审核通过</button>
		      	</div>
			</div>
        </div>
      </div>
    </div>
</body>
</html>