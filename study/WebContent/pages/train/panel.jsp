<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>

<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <%-- <li <c:if test="${selectedPanel == 'trainList' }">class="active"</c:if>><a href="train/findTrainList">培训管理</a></li> --%>
	  <li <c:if test="${selectedPanel == 'courseList' }">class="active"</c:if>><a onclick="goPath('train/findCourseList')" href="javascript:void(0)">培训课程</a></li>
	  <li <c:if test="${selectedPanel == 'myCourseList' }">class="active"</c:if>><a onclick="goPath('train/myCourseList')" href="javascript:void(0)">我参加的课程</a></li>
	  <auth:hasPermission name="trainManagement">
	  <li <c:if test="${selectedPanel == 'vacationTaskList' }">class="active"</c:if>><a onclick="goPath('train/vacationTaskList')" href="javascript:void(0)">请假审批
	  <span class="badge" style="background-color:red;<c:if test='${vacationTaskCount == 0}'>display:none;</c:if>">${vacationTaskCount}</span></a></li>
	  <li <c:if test="${selectedPanel == 'vacationList' }">class="active"</c:if>><a onclick="goPath('train/vacationList')" href="javascript:void(0)">请假查询</a></li>
	  <li <c:if test="${selectedPanel == 'noSignInList' }">class="active"</c:if>><a onclick="goPath('train/noSignInList')" href="javascript:void(0)">未签到查询</a></li>
	  <li <c:if test="${selectedPanel == 'stuScoreList' }">class="active"</c:if>><a onclick="goPath('train/stuScoreList')" href="javascript:void(0)">学员得分查询</a></li>
	  </auth:hasPermission>
	  <li <c:if test="${selectedPanel == 'lecturerScoreList' }">class="active"</c:if>><a onclick="goPath('train/lecturerScoreList')" href="javascript:void(0)">讲师的评分</a></li>
	</ul>
</div> 