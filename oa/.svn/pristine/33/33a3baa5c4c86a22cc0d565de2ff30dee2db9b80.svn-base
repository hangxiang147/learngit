<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
   
<!DOCTYPE html> 
<html lang="zh-CN">
  <body>
    <div class="container-fluid">
      <div class="row">
      	<c:if test="${selectedPanel=='findTaskList'}">
      		<%@include file="/pages/personal/panel.jsp" %>	
      	</c:if>
      	<c:if test="${selectedPanel!='findTaskList'}">
      		<%@include file="/pages/attendance/salayPanel.jsp" %>
      	</c:if>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<div class="alert alert-danger" role="alert">${errorMessage }</div>
        </div>
      </div>
    </div>
  </body>
</html>