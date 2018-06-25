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
       	  <h3 class="sub-header" style="margin-top:0px;">调整个人薪资标准</h3> 
           	<div class="formal_img" style="margin-bottom:-20px;">
               	<img alt="调整个人薪资标准" src="assets/images/alterSalary.png"></img>  
            </div>
            <br> <a
					onclick="goPath('HR/staffSalary/alterStaffSalary?userId=${userId}')"
					href="javascript:void(0)" class="btn btn-primary">我要申请</a>
				<button type="button" onclick="history.go(-1)" class="btn btn-default" style="margin-left:2%">返回</button>
			</div>
      </div>
    </div>
</body>
</html>