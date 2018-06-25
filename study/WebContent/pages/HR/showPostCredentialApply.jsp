<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
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
			<s:set name="panel" value="'dangan'"></s:set>
			<%@include file="/pages/HR/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">证书审核流程</h3>
				<div class="formal_img">
					<img alt="岗位资格证书" src="assets/images/PostQualificationCertificate.png">
					</img>
				</div>
				<div>
					<a href="HR/staff/postCredentialApplyToPerson" class="btn btn-primary" >
						开始审核
					</a>
					
					<button type="button" onclick="history.go(-1)" class="btn btn-default" style="margin-left:2%;">返回</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>