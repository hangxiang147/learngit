<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <auth:hasPermission name="softProject_admin">
	  <li <c:if test="${selectedPanel == 'softPersonTypeList' }">class="active"</c:if>><a onclick="goPath('/performance/soft/softPersonTypeList')" href="javascript:void(0)">人员类型维护</a></li>
	  <li <c:if test="${selectedPanel == 'showProject' }">class="active"</c:if>><a onclick="goPath('/performance/soft/showProject')" href="javascript:void(0)">项目管理</a></li>
	  </auth:hasPermission>
	  <auth:hasPermission name="softProject_require">
	  <li <c:if test="${selectedPanel == 'showPreparedRequirement' }">class="active"</c:if>><a onclick="goPath('/performance/soft/showPreparedRequirement')" href="javascript:void(0)">录入需求</a></li>
	  <li <c:if test="${selectedPanel == 'problemOrderList' }">class="active"</c:if>><a onclick="goPath('/performance/soft/problemOrderList')" href="javascript:void(0)">提问题单</a></li>
	  </auth:hasPermission>
	  <auth:hasPermission name="softProject_admin">
	  <li <c:if test="${selectedPanel == 'allotProblemOrder' }">class="active"</c:if>><a onclick="goPath('/performance/soft/allotProblemOrder')" href="javascript:void(0)">分配问题单<span class="badge" style="background-color:red;<c:if test='${problemOrderTaskCount == 0 }'>display:none;</c:if>">${problemOrderTaskCount}</span></a></li>
	  </auth:hasPermission>
	  <li <c:if test="${selectedPanel == 'divideRequire' }">class="active"</c:if>><a onclick="goPath('/performance/soft/divideRequireManage')" href="javascript:void(0)">任务分解</a></li>
	  <li <c:if test="${selectedPanel == 'showRequirement' }">class="active"</c:if>><a onclick="goPath('/performance/soft/showRequirement')" href="javascript:void(0)">版本管理</a></li>
	  <auth:hasPermission name="softProject_require">
	  <li <c:if test="${selectedPanel == 'showTask' }">class="active"</c:if>><a onclick="goPath('/performance/soft/showAllTask')" href="javascript:void(0)">查看所有任务</a></li>
	  </auth:hasPermission>
	</ul>
</div> 

