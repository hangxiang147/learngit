<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'systemManagement'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'softUpload' }">class="active"</c:if>><a href="<c:url value='/' />pages/systemmanagement/softupload/upload.jsp">软件上传</a></li>
	</ul>
</div>
