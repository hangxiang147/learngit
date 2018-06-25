<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'showPass' }">class="active"</c:if>><a onclick="goPath('/chart/interview/showPass')" href="javascript:void(0)">通过率分析</a></li>
	  <li <c:if test="${selectedPanel == 'showOffer' }">class="active"</c:if>><a onclick="goPath('/chart/interview/showOffer')" href="javascript:void(0)">入职率分析</a></li>
	</ul>
</div> 