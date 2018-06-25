<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

  <body>

    <div class="container-fluid">
      <div class="row">
      	<c:if test="${param.panel == 'dangan' }">
      		<s:set name="panel" value="'dangan'"></s:set>
      	</c:if>
      	<c:if test="${param.panel == 'position' }">
      		<s:set name="panel" value="'position'"></s:set>
      	</c:if>
      	<c:if test="${param.panel == 'infoAlteration' }">
      		<s:set name="panel" value="'infoAlteration'"></s:set>
      	</c:if>
      	<s:set name="panel" value="#request.panel"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<div class="alert alert-danger" role="alert">${errorMessage }</div>
        </div>
      </div>
    </div>
  </body>
</html>