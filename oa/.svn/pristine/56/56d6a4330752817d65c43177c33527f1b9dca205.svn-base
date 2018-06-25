<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForJS.tld" prefix="jf" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
	#vehicleInfo{
		border-collapse:collapse;
		width:100%;
		margin-top:5px;
	}
	#vehicleInfo tr td{word-wrap:break-word;font-size:14px;padding:8px 7px;text-align:center;border:1px solid #ddd}
	.title{
		text-align:center !important;
		background:#efefef;
	}
	.form-control{
		display:inline-block;
	}
	.glyphicon-remove:hover{color:red}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
      	<s:set name="panel" value="'version'"></s:set>
        <%@include file="/pages/informationCenter/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px">编辑版本信息</h3>
				<form  id="versionInfo" action="/information/version/save_saveVersionInfo"
					method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
					<s:token></s:token>
					<input name="versionFuncionInfo.versionDate" type="hidden">
					<div class="form-group">
					<label class="col-sm-1 control-label" style="width:12%">版本名称<span style="color:red"> *</span></label>
					<div class="col-sm-2">
					<select id="versionName" class="form-control" required name="versionFuncionInfo.versionName" onchange="changeVersion()">
						<option value="">请选择</option>
						<c:forEach items="${versions}" var="version">
							<option data-versionDate="${version.endDate}" value="${version.version}">${version.version}</option>
						</c:forEach>
					</select>
					</div>
					</div>
					<div class="form-group">
						<label class="col-sm-1 control-label" style="width:12%">功能点介绍<span style="color:red"> *</span></label>
						<div class="col-sm-5">
						<textarea rows="3" class="form-control" name="versionFuncionInfo.function" required></textarea>
						</div>
						<button type="button" class="btn btn-primary" onclick="addFunction()">新增</button>
					</div>
					<button id="submitBtn" style="display:none" type="submit"></button>
				</form>
				<div class="col-sm-1" style="width:12%"></div>
				<button type="button" class="btn btn-primary" onclick="$('#submitBtn').click()">提交</button>
				<button type="button" class="btn btn-default" onclick="history.go(-1)" style="margin-left:2%">返回</button>
			</div>
		</div>
	</div>
  	<script src="/assets/js/layer/layer.js"></script>
	<script>
	function addFunction(){
		$("#versionInfo").append($("#functionHtml").html());
	}
	function deleteFunction(target){
		$(target).parent().remove();
	}
	function changeVersion(){
		var selectedOption = $("#versionName option:selected");
		$("input[name='versionFuncionInfo.versionDate']").val(selectedOption.attr("data-versionDate"));
	}
	</script>
	<script type="text/html" id="functionHtml">
					<div class="form-group">
						<div class="col-sm-1" style="width:12%"></div>
						<div class="col-sm-5">
						<textarea rows="3" class="form-control" name="versionFuncionInfo.function" required></textarea>
						</div>
						<a style="margin-top:2%"class="col-sm-1" href="javascript:void(0)" onclick="deleteFunction(this)"><span class="glyphicon glyphicon-remove"></span></a>
					</div>
	</script>
</body>
</html>