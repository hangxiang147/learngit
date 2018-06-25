<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">任务分配流程</h3> 
           	<div class="vacation_img" style="margin-bottom:-80px;">
               	<img alt="任务分配流程图" src="assets/images/assignment.png"></img>  
            </div>
            <a onclick="goPath('personal/newTask')" href="javascript:void(0)" class="btn btn-primary">下达任务</a>
      	</div>
      </div>
    </div>
</body>
</html>