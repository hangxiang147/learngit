<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'systemManagement'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'softManagement' }">class="active"</c:if>><a onclick="goPath('downloadcenter/findSoftListBySelect')" href="javascript:void(0)">软件管理</a></li>
	</ul>
</div>
