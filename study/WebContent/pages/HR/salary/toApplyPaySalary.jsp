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
        <%@include file="/pages/attendance/salayPanel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">申请发放工资</h3> 
           	<div class="formal_img" style="margin-bottom:-20px;">
               	<img alt="申请发放工资" src="assets/images/applyPaySalary.png"></img>  
            </div>
            <br> <a
					onclick="goPath('HR/staffSalary/applyPaySalary')"
					href="javascript:void(0)" class="btn btn-primary">我要申请</a>
			</div>
      </div>
    </div>
</body>
</html>