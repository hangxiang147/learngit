<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'statisticScoreList' }">class="active"</c:if>><a onclick="goPath('/performance/soft/statisticScoreList')" href="javascript:void(0)">得分汇总</a></li>
	</ul>
</div> 

