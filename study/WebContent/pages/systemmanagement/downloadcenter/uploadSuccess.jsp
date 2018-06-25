<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>

<style type="text/css">
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
</style>

</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'systemManagement'"></s:set>
      	<s:set name="selectedPanel" value="'softUpload'"></s:set>
        <%@include file="/pages/systemmanagement/downloadcenter/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<div class="row">
        		<h2>上传成功！</h2>
        	</div>
	     	<div class="row col-sm-9 col-sm-offset-7 col-md-10 col-md-offset-3" >
	    		<button type="button" class="btn btn-primary" onclick="location.href='javascript:history.go(-1);'">返回</button>
	      	</div>
        </div>
      </div>
    </div>
  </body>
</html>
