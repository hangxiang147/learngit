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
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;width:20%}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	.tab table tr .black {background:#efefef;text-align:center;color:#000;width:15%}
	#description p img{height:200px;}
	.description p img{height:200px;}
	#checkStandard p img{height:200px;}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<c:choose>
			<c:when test="${fromPreparedRequire eq '1'}">
				<s:set name="selectedPanel" value="'showPreparedRequirement'"></s:set>
							<%@include file="/pages/performance/soft/manage/panel.jsp" %>
				
			</c:when>
			<c:when test="${fromPreparedRequire eq '2'}">
			<s:set name="selectedPanel" value="'showRequirementCopy'"></s:set>
			<%@include file="/pages/performance/soft/subject/panel.jsp" %>		
			</c:when>
			<c:otherwise>
				<s:set name="selectedPanel" value="'showRequirement'"></s:set>
							<%@include file="/pages/performance/soft/manage/panel.jsp" %>
				
			</c:otherwise>
			</c:choose>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">需求详情</h3>
				<div class="tab">
				<table>
					<tr>
						<td class="black">需求名称</td>
						<td colspan="3">${requirementVo.name}</td>
						<td class="black">所属模块</td>
						<td><c:if test="${requirementVo.module!='null'}">${requirementVo.module}</c:if></td>
					</tr>
					<tr>
						<td class="black">项目</td>
						<td>${requirementVo.projectName}</td>
						<td class="black">属主</td>
						<td>${requirementVo.owner}</td>
						<td class="black">备注</td>
						<td><c:if test="${requirementVo.remark!='null'}">${requirementVo.remark}</c:if></td>
					</tr>
					<tr>
						<td class="black">创建人</td>
						<td>${requirementVo.creator}</td>
 						<td class="black">评审人员</td>
						<td><c:if test="${requirementVo.reviewer!='null'}">${requirementVo.reviewer}</c:if></td>
						<td class="black">阶段</td>
						<td>${requirementVo.stage}</td>
					</tr>
					<tr>
						<td class="black">需求描述</td>
						<td colspan="5" id="description" style="text-align:left">${requirementVo.description=='null'?'':requirementVo.description}</td>
					</tr>
					<tr>
						<td class="black">验收标准</td>
						<td colspan="5" id="checkStandard" style="text-align:left">${requirementVo.checkStandard=='null'?'':requirementVo.checkStandard}</td>
					</tr>
					<tr>
						<td class="black">附件</td>
						<td colspan="5" id="projectDescription" style="text-align:left">
						<c:forEach items="${requirementVo.attachmentNames}" var="attachmentName" varStatus="status">
							<a href="/performance/soft/downloadAttachment?attachmentPath=${requirementVo.attachmentPaths[status.index]}&attachmentName=${attachmentName}">${attachmentName}</a><br>
						</c:forEach>
						</td>
					</tr>
				</table>
				</div>
				<c:if test="${requirementVo.deleteReason!=''}">
					<span style="color:red">作废原因：${requirementVo.deleteReason}</span>
				</c:if>
				<br>
				<c:if test="${not empty subRequirementLst}">
					<h3 class="sub-header" style="margin-top: 0px;">任务列表</h3>
					<div class="table-responsive">
		            <table class="table table-striped">
		              <thead>
		                <tr>
		                  <th class="col-sm-1">ID</th>
		                  <th class="col-sm-2">任务名称</th>
		                  <th class="col-sm-1">开发人员</th>
		                  <th class="col-sm-1">分值</th>
		                  <th class="col-sm-3">任务描述</th>
		                  <th class="col-sm-2">附件</th>
		                </tr>
		              </thead>
		              <tbody>
		              	<c:forEach items="${subRequirementLst}" var="sub" varStatus="status">
		              		<tr>
		              		<td>${status.index+1}</td>
		              		<td>${sub.subRequirementName}</td>
		              		<td>${sub.developer}</td>
		              		<td>${sub.score}</td>
		              		<td class="description">${sub.description}</td>
		              		<td>
		              		<c:forEach items="${sub.attachmentNames}" var="attachmentName" varStatus="status">
								<a href="/performance/soft/downloadAttachment?attachmentPath=${sub.attachmentPaths[status.index]}&attachmentName=${attachmentName}">${attachmentName}</a><br>
							</c:forEach>
							</td>
		              		</tr>
		              	</c:forEach>
		              </tbody>
		            </table>
		          	</div>
				</c:if>
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
    		 $(".description").each(function(){
       			 var html = marked($(this).text());
       			 $(this).html(html);
       		 });
    		 var html = marked($("#description").html());
    		 $("#description").html(html);
    		 var html = marked($("#checkStandard").html());
    		 $("#checkStandard").html(html);
   			 $("#description img").each(function(){
   				var url = $(this).attr("src");
   				$(this).attr("onclick","showPic('','"+url+"')");
   			 })
   			 
   			 $(".description p img").click(function(){
    			 var imgsrc = $(this).attr("src");
					var picData = {
						start : 0,
						data : []
					}
					picData.data.push({
						alt : name,
						src : imgsrc,
					})
					layer.photos({
						offset : '10%',
						photos : picData,
						anim : 5,
					});
    		 })
    		 $("#checkStandard p img").click(function(){
    			 var imgsrc = $(this).attr("src");
					var picData = {
						start : 0,
						data : []
					}
					picData.data.push({
						alt : name,
						src : imgsrc,
					})
					layer.photos({
						offset : '10%',
						photos : picData,
						anim : 5,
					});
    		 })
    	});
    	var showPic = function(name, path) {
    		var picData = {
    			start : 0,
    			data : []
    		}
    		picData.data.push({
    			alt : name,
    			src : "performance/soft/showImage?attachmentPath=" + path
    		})
    		layer.photos({
    			offset : '50px',
    			photos : picData,
    			anim : 5
    		});
    	}
  	</script>
</body>
</html>