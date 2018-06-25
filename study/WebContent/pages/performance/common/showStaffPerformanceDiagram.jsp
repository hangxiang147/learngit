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
        <%@include file="/pages/performance/common/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">修改绩效方案流程</h3> 
           	<div class="formal_img">
               	<img alt="修改员工绩效方案" src="assets/images/PersonalPerformance.png"></img>  
            </div>
            <div>
            <a href="administration/performance/modifyPersonalPerformance?userId=${userId}&year=${year}&month=${month}" class="btn btn-primary">我要申请</a>
            <button onclick="history.go(-1)" class="btn btn-default" style="margin-left:2%">返回</button>
            </div>
      	</div>
      </div>
    </div>
</body>
</html>