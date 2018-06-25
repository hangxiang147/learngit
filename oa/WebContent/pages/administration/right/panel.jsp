<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="col-sm-3 col-md-2 sidebar">
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'right' }">class="active"</c:if>><a onclick="goPath('/administration/right/listRightPage')" href="javascript:void(0)">权限维护</a></li>
	  <li <c:if test="${selectedPanel == 'membershippersonal' }">class="active"</c:if>><a onclick="goPath('/administration/right/listRightMemberShipPersonal')" href="javascript:void(0)">个人授予权限</a></li>
	  <li <c:if test="${selectedPanel == 'membershipgroup' }">class="active"</c:if>><a onclick="goPath('/administration/right/listRightMemberShipGroup')" href="javascript:void(0)">组授予权限</a></li>	
	  <li <c:if test="${selectedPanel == 'listRightPersonal' }">class="active"</c:if>><a onclick="goPath('/administration/enTrust/listRightPersonal')" href="javascript:void(0)">流程个人委托</a></li>	
	  <li <c:if test="${selectedPanel == 'listRightGroup' }">class="active"</c:if>><a onclick="goPath('/administration/enTrust/listRightGroup')" href="javascript:void(0)">流程群组委托</a></li>	
	</ul>
</div> 

