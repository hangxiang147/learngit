<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- <div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'process'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'resignationList' }">class="active"</c:if>><a onclick="goPath('PM/process/findResignationList')" href="javascript:void(0)">离职申请   <span class="badge" style="background-color:red;<c:if test='${resignationPMCount == 0 }'>display:none;</c:if>">${resignationPMCount }</span></a></li>
	</ul>
</div> --%> 

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'workReport'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'workReportList' }">class="active"</c:if>><a onclick="goPath('workReport/findWorkReportList')" href="javascript:void(0)">工作日报查询</a></li>	  
	  <li <c:if test="${selectedPanel == 'weekReportList' }">class="active"</c:if>><a onclick="goPath('workReport/findWeekReportList')" href="javascript:void(0)">工作周报查询</a></li>
	  <li <c:if test="${selectedPanel == 'workReportStatistics' }">class="active"</c:if>><a onclick="goPath('workReport/findWorkReportStatistics?flag=1&companyID=1')" href="javascript:void(0)">日报统计</a></li>	  
	  <li <c:if test="${selectedPanel == 'weekReporterManage' }">class="active"</c:if>><a onclick="goPath('workReport/weekReporterManage')" href="javascript:void(0)">周报人员维护</a></li>
	  <li <c:if test="${selectedPanel == 'weekReportStatistics' }">class="active"</c:if>><a onclick="goPath('workReport/findWeekReportStatistics?flag=1&companyID=1')" href="javascript:void(0)">周报统计</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" ${selectedPanel!='rewardAndPunishmentList'? "style='display:none;'":""}>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'rewardAndPunishmentList' }">class="active"</c:if>><a onclick="goPath('PM/process/findRewardAndPunishmentList')" href="javascript:void(0)">行政奖惩记录</a></li>	  
	</ul>
</div>

