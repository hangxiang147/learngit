<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<!DOCTYPE html> 
<html lang="zh-CN">

  <body>

    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/PM/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<div class="alert alert-danger" role="alert">${errorMessage }</div>
        </div>
      </div>
    </div>
  </body>
</html>