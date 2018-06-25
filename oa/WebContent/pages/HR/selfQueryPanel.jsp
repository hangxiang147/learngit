<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'job'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'vitaeQuery' }">class="active"</c:if>><a href="/HR/vitae/toJobList">应聘结果查询</a></li>
	</ul>
</div> 