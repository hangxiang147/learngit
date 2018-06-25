<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<div class="col-sm-3 col-md-2 sidebar">
	<div id="toDoCount" style="display:none">
	<auth:hasPermission name="hrManagement">
	<span>${hrCount}</span>
	</auth:hasPermission>
	<span>${count}</span>
	</div>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'findTaskList' or param.selectedPanel == 'findTaskList' }">class="active"</c:if>><a onclick="goPath('personal/findTaskList?type=1')" href="javascript:void(0)">我的待办   <span id="toDo" class="badge" style="background-color:red;display:none"></span></a></li>
	  <li <c:if test="${selectedPanel == 'findNoticeList' }">class="active"</c:if>><a onclick="goPath('personal/findNoticeList')" href="javascript:void(0)">我的消息   <span class="badge" style="background-color:red;<c:if test='${unReadNoticeCount == 0 }'>display:none;</c:if>">${unReadNoticeCount }</span></a></li>	
	  <li <c:if test="${selectedPanel == 'findProcessList' }">class="active"</c:if>><a onclick="goPath('personal/findProcessList?type=1')" href="javascript:void(0)">我发起的流程</a></li>
	  <li <c:if test="${selectedPanel == 'workReportDetail' }">class="active"</c:if>><a onclick="goPath('personal/workReportDetail')" href="javascript:void(0)">工作日报</a></li>
	  <li <c:if test="${selectedPanel == 'underlingWorkReportDetail' }">class="active"</c:if>><a onclick="goPath('personal/underlingWorkReportDetail')" href="javascript:void(0)">下属工作汇报</a></li>
	  <%-- <li <c:if test="${selectedPanel == 'underlingVacationDetail' }">class="active"</c:if>><a onclick="goPath('personal/underlingVacationDetail')" href="javascript:void(0)">下属请假明细</a></li> --%>
	  <auth:hasPermission name="morningMeetingReport">
	  <li <c:if test="${selectedPanel == 'newWeekMeetingReport'}">class="active"</c:if>><a onclick="goPath('personal/showWeekMeetingReport')" href="javascript:void(0)">周早会汇报</a></li>
	  </auth:hasPermission>
	  <li <c:if test="${selectedPanel == 'weekReportDetail' }">class="active"</c:if>><a onclick="goPath('personal/weekReportDetail')" href="javascript:void(0)">工作周报</a></li>
	 <%--  <li <c:if test="${selectedPanel == 'underlingSalaryDetail' }">class="active"</c:if>><a href="personal/underlingSalaryDetail">下属工资查询</a></li> --%>
	  <li <c:if test="${selectedPanel == 'newVacation' }">class="active"</c:if>><a onclick="goPath('personal/showVacationDiagram')" href="javascript:void(0)">请假申请</a></li>
	  <li <c:if test="${selectedPanel == 'newWorkOvertime' }">class="active"</c:if>><a onclick="goPath('personal/showWorkOvertime')" href="javascript:void(0)">加班申请</a></li>
	  <li <c:if test="${selectedPanel == 'newTask' }">class="active"</c:if>><a onclick="goPath('personal/showAssignmentDiagram')" href="javascript:void(0)">分配任务</a></li>
	  <li <c:if test="${selectedPanel == 'newFormal' }">class="active"</c:if>><a onclick="goPath('personal/showFormalDiagram')" href="javascript:void(0)">转正申请</a></li>
	  <li <c:if test="${selectedPanel == 'newResignation' }">class="active"</c:if>><a onclick="goPath('personal/showResignationDiagram')" href="javascript:void(0)">离职申请</a></li>
	  <li <c:if test="${selectedPanel =='attendanceDetail' }">class="active"</c:if>><a onclick="goPath('personal/attendanceDetail')" href="javascript:void(0)">考勤&签到</a></li>
	  <li <c:if test="${selectedPanel == 'showInformation' }">class="active"</c:if>><a onclick="goPath('personal/showInformation?type=1')" href="javascript:void(0)">个人信息</a></li>
     <%--  <li <c:if test="${selectedPanel == 'updateInformation' }">class="active"</c:if>><a href="personal/updateInformation">基本信息修改</a></li> --%>
      <li <c:if test="${selectedPanel == 'findGroupDetails' }">class="active"</c:if>><a onclick="goPath('personal/findGroupDetails')" href="javascript:void(0)">我的岗位职责</a></li>
      <li <c:if test="${selectedPanel == 'structure' }">class="active"</c:if>><a onclick="goPath('personal/structure')" href="javascript:void(0)">组织结构</a></li>	     
	</ul>
	<script>
		$(function(){
			var total = 0;
			$("#toDoCount").find("span").each(function(){
				var value = $(this).text();
				if(value!=""){
					total += parseInt(value);
				}
			});
			if(total>0){
				$("#toDo").css("display","inline-block");
				$("#toDo").text(total);
			}
		});
	</script>
</div>