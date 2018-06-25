<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'salaryUpload' }">class="active"</c:if>><a onclick="goPath('attendance/salaryUpload')" href="javascript:void(0)">工资单数据上传</a></li> 
	  <li <c:if test="${selectedPanel == 'salaryShow' }">class="active"</c:if>><a onclick="goPath('attendance/salaryShow')" href="javascript:void(0)">工资单查看</a></li> 
	  <li <c:if test="${selectedPanel == 'salarySend' }">class="active"</c:if>><a onclick="goPath('attendance/salarySend')" href="javascript:void(0)">工资单信息推送</a></li> 
	</ul>
</div>

