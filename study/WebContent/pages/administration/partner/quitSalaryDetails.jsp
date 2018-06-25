<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
$(function(){
	
});
</script>
<style type="text/css">
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'partnerCenter'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">合伙人放弃工资明细</h3> 
<%--          	<form action="administration/partner/uploadQuitSalaryDetail" class="form-horizontal" enctype="multipart/form-data"
        	 onsubmit="Load.Base.LoadingPic.FullScreenShow(null)" method="post">
        	<div class="form-group">
        		<label class="control-label col-sm-1" style="width:10%">选择文件<span style="color:red"> *</span></label>
        		<div class="col-sm-3" style="width:20%">
        			<input type="file" name="file" required>
        		</div>
        	</div>
        	<button class="btn btn-primary" type="submit">上传</button>
        	</form>
        	<div class="sub-header"></div> --%>
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td>6月放弃工资</td>
	              		<td>7月放弃工资</td>
	              		<td>8月放弃工资</td>
	              		<td>9月放弃工资</td>
	              		<td>10月放弃工资</td>
	              		<td>11月放弃工资</td>
	              		<td>12月放弃工资</td>
	              		<td>差额</td>
	              		<td>合计</td>
	              	</tr>
	              </thead>
	              <tbody>
	              		<c:forEach items="${quitSalaryDetails}" var="detail">
	              			<td>${detail==''?'——':detail}</td>
	              		</c:forEach>
	              </tbody>
		        </table>
		     </div>
      	</div>
      </div>
    </div>
</body>
</html>