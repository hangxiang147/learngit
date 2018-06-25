<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <auth:hasPermission name="hrManagement">
	  <li <c:if test="${selectedPanel == 'vacationManagement' }">class="active"</c:if>><a onclick="goPath('attendance/vacationManagement')" href="javascript:void(0)">请假管理</a></li> 
	  <li <c:if test="${selectedPanel == 'attendanceDetail' }">class="active"</c:if>><a onclick="goPath('attendance/attendanceDetail')" href="javascript:void(0)">考勤明细</a></li>
	  <li <c:if test="${selectedPanel == 'workTimeArrange' }">class="active"</c:if>><a onclick="goPath('attendance/workTimeArrange')" href="javascript:void(0)">作息时间安排</a></li>
	  <li <c:if test="${selectedPanel == 'signinStatistics' }">class="active"</c:if>><a onclick="goPath('attendance/signinStatistics?flag=1&companyID=1')" href="javascript:void(0)">签到统计</a></li> 
	  <li <c:if test="${selectedPanel == 'bussinessTripList' }">class="active"</c:if>><a onclick="goPath('administration/process/bussinessTripList')" href="javascript:void(0)">出差查询</a></li> 
	  <li <c:if test="${selectedPanel == 'workOvertimeList' }">class="active"</c:if>><a onclick="goPath('attendance/workOvertimeList')" href="javascript:void(0)">加班管理</a></li>
	 </auth:hasPermission>
	  <li <c:if test="${selectedPanel == 'lateAndLeaveList' }">class="active"</c:if>><a onclick="goPath('attendance/lateAndLeaveList')" href="javascript:void(0)">考勤黑榜</a></li>
	</ul>
</div>