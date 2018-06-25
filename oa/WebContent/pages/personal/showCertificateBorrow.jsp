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
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">证件申请流程</h3> 
           	<div class="formal_img">
               	<img alt="证件申请" src="assets/images/CertificateBorrow.png"></img>  
            </div>
            <div><a onclick="goPath('personal/newCertificateBorrow')" href="javascript:void(0)" class="btn btn-primary">我要申请</a></div>
      	</div>
      </div>
    </div>
</body>
</html>