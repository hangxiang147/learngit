<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'softCategory'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'officesoft' }">class="active"</c:if>><a onclick="goPath('downloadcenter/findSoftList?softCategory=1')" href="javascript:void(0)">办公软件</a></li>
	  <li <c:if test="${selectedPanel == 'develementsoft' }">class="active"</c:if>><a onclick="goPath('downloadcenter/findSoftList?softCategory=2')" href="javascript:void(0)">开发软件</a></li>
	  <li <c:if test="${selectedPanel == 'driversoft' }">class="active"</c:if>><a onclick="goPath('downloadcenter/findSoftList?softCategory=3')" href="javascript:void(0)">驱动</a></li>
	  <li <c:if test="${selectedPanel == 'othersoft' }">class="active"</c:if>><a onclick="goPath('downloadcenter/findSoftList?softCategory=4')" href="javascript:void(0)">其它</a></li>
	</ul>
</div>

