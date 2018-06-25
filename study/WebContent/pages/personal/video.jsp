<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
<script type="text/javascript">

</script>
<style type="text/css">
	#weekReport{
		border-collapse:collapse;
		width:100%;
		margin-top:5px;
	}
	#weekReport tr td{word-wrap:break-word;font-size:10px;padding:8px 7px;text-align:center;border:1px solid #ddd}
	.title{
		text-align:left !important;
		background:#efefef;
	}
	.form-control{
		display:inline-block;
	}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/pages/personal/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<video width="960" height="520" controls>
    				<source src="assets/video/weekReport.webm" type="video/mp4">
  				</video>
  				<br>
  			<button type="button" style="margin-left:45%" class="btn btn-primary"
			onclick="location.href='javascript:history.go(-1);'">返回</button>
			</div>
		</div>
	</div>
</body>
</html>
