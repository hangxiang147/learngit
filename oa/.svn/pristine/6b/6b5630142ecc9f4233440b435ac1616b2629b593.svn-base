<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'job'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'jobList' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toJobList')" href="javascript:void(0)">应聘职位录入</a></li>
	  <li <c:if test="${selectedPanel == 'toFixVitaeList' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toFixVitaeList')" href="javascript:void(0)">确认应聘信息
	  <span class="badge" style="background-color:red;<c:if test='${confirmMsgTaskCount == 0}'>display:none;</c:if>">${confirmMsgTaskCount}</span></a></li>
	  <li <c:if test="${selectedPanel == 'toStartVitaeList' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toStartVitaeList')" href="javascript:void(0)">发起应聘邀请
	  <span class="badge" style="background-color:red;<c:if test='${startVitaeTaskCount == 0}'>display:none;</c:if>">${startVitaeTaskCount}</span></a></li>
	  <li <c:if test="${selectedPanel == 'toVitaeStep1' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toVitaeStep1')" href="javascript:void(0)">面试开始
	  <span class="badge" style="background-color:red;<c:if test='${step1TaskCount == 0}'>display:none;</c:if>">${step1TaskCount}</span></a></li>
	  <li <c:if test="${selectedPanel == 'toVitaeStep2' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toVitaeStep2')" href="javascript:void(0)">完善人员信息
	  <span class="badge" style="background-color:red;<c:if test='${step2TaskCount == 0}'>display:none;</c:if>">${step2TaskCount}</span></a></li>
	  <li <c:if test="${selectedPanel == 'toVitaeStep3' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toVitaeStep3')" href="javascript:void(0)">确认应聘结果
	  <span class="badge" style="background-color:red;<c:if test='${step3TaskCount == 0}'>display:none;</c:if>">${step3TaskCount}</span></a></li>
	  <li <c:if test="${selectedPanel == 'toVitaeStep4' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toVitaeStep4')" href="javascript:void(0)">确认入职
	  <span class="badge" style="background-color:red;<c:if test='${step4TaskCount == 0}'>display:none;</c:if>">${step4TaskCount}</span></a></li>
	  <li <c:if test="${selectedPanel == 'toSignList' }">class="active"</c:if>><a onclick="goPath('/HR/vitae/toSignList')" href="javascript:void(0)">登记信息查询</a></li>
	</ul>
</div> 