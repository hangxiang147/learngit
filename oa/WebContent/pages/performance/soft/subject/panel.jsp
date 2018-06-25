<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'showRequirementCopy' }">class="active"</c:if>><a onclick="goPath('/performance/soft/showRequirementCopy')" href="javascript:void(0)">分配任务</a></li>
	  <li <c:if test="${selectedPanel == 'showTask' }">class="active"</c:if>><a onclick="goPath('/performance/soft/showTask')" href="javascript:void(0)">已分配任务</a></li>
	  <li <c:if test="${selectedPanel == 'softPerformanceSubject' }">class="active"</c:if>><a onclick="goPath('/performance/soft/softPerformanceSubject')" href="javascript:void(0)">测试人员审核</a></li>
	  <li <c:if test="${selectedPanel == 'softPerformanceSubjectSS' }">class="active"</c:if>><a onclick="goPath('/performance/soft/softPerformanceSubjectSS')" href="javascript:void(0)">实施人员结果填写</a></li>
	</ul>
</div> 

