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
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	.tab table tr .black {background:#efefef;text-align:center;color:#000;width:15%}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="selectedPanel" value="'showProject'"></s:set>
			<%@include file="/pages/performance/soft/manage/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">项目详情</h3>
				<div class="tab">
				<table>
					<tr>
						<td class="black">项目名称</td>
						<td colspan="3">${projectVO.name}</td>
						<td class="black">项目代号</td>
						<td>${projectVO.code}</td>
					</tr>
					<tr>
						<td class="black">项目负责人</td>
						<td>${projectVO.projectHeaderName}</td>
						<td class="black">测试负责人</td>
						<td>${projectVO.testHeaderName}</td>
						<td class="black">最新版本</td>
						<td>${projectVO.updatestVersion}</td>
					</tr>
					<tr>
						<td class="black">项目描述</td>
						<td colspan="5" id="projectDescription" style="text-align:left;max-width:800px" >${projectVO.description}</td>
					</tr>
					<tr>
						<td class="black">版本</td>
						<td colspan="5" id="projectDescription" style="text-align:left;max-width:800px">${projectVO.versions}</td>
					</tr>
					<tr>
						<td class="black">模块</td>
						<td colspan="5" id="projectDescription" style="text-align:left;max-width:800px">${projectVO.modules}</td>
					</tr>
				</table>
				</div>
				<br>
				<div style="text-align:center">
				<button type="button" class="btn btn-default"
						onclick="location.href='javascript:history.go(-1);'"
						style="margin-left: 2%">返回</button>
				</div>
			</div>
		</div>
	</div>
	<script src="/assets/js/textarea/highlight.pack.js"></script>
	<script src="/assets/js/textarea/marked.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
	<script src="/assets/js/textarea/to-markdown.js"></script>
	<script src="/assets/js/textarea/bootstrap-markdown.js"></script>
	<script src="/assets/js/textarea/jquery.textcomplete.js"></script>

	<script type="text/javascript">
    	$(function(){
    		 var html = marked($("#projectDescription").text());
    		 $("#projectDescription").html(html);
    	});
  	</script>
</body>
</html>