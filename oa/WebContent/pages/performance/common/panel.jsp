<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'createPerformanceCase' }">class="active"</c:if>><a onclick="goPath('/administration/performance/showPerformanceDiagram')" href="javascript:void(0)">制定岗位绩效方案</a></li>
	  <li <c:if test="${selectedPanel == 'performanceApplys' }">class="active"</c:if>><a onclick="goPath('/administration/performance/findPerformanceApplys')" href="javascript:void(0)">岗位绩效方案审批查询</a></li>
	  <li <c:if test="${selectedPanel == 'updatePerformanceApplys' }">class="active"</c:if>><a onclick="goPath('/administration/performance/findUpdatePerformanceApplys')" href="javascript:void(0)">个人绩效方案审批查询</a></li>
	  <auth:hasPermission name="staffPerformances">
	  	<li <c:if test="${selectedPanel == 'staffPerformances' }">class="active"</c:if>><a onclick="goPath('/administration/performance/findStaffPerformances')" href="javascript:void(0)">员工绩效考核查询</a></li>
	  </auth:hasPermission>
	  <li <c:if test="${selectedPanel == 'underlingPerformances' }">class="active"</c:if>><a onclick="goPath('/administration/performance/findUnderlingPerformances')" href="javascript:void(0)">个人绩效考核</a></li>
	  <li <c:if test="${selectedPanel == 'myStaffPerformances' }">class="active"</c:if>><a onclick="goPath('/administration/performance/findMyStaffPerformances')" href="javascript:void(0)">我的绩效考核</a></li>
	</ul>
</div>