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
        <s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">社保缴纳流程</h3> 
           	<div class="vacation_img" style="margin-bottom:-80px;">
               	<img alt="社保缴纳流程图" src="assets/images/socialSecurity.png"></img>  
            </div>
            <a onclick="goPath('HR/process/newSocialSecurity')" href="javascript:void(0)" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 缴纳社保</a>
            <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
      	</div>
      </div>
    </div>
</body>
</html>