<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForJS.tld" prefix="jf" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>

<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
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
			<c:choose>
			<c:when test="${isFromStatistic eq '1' }">
			
			<s:set name="selectedPanel" value="'statisticScoreList'"></s:set>
       		 <%@include file="/pages/performance/soft/score/scorePanel.jsp" %>	
			</c:when>
			<c:when test="${isFromScore eq '1'}">
			
			<s:set name="selectedPanel" value="'myScoreList'"></s:set>
       		 <%@include file="/pages/performance/soft/score/panel.jsp" %>			
			</c:when>
			<c:otherwise>
				<s:set name="selectedPanel" value="'showTask'"></s:set>
			<%@include file="/pages/performance/soft/manage/panel.jsp"%>
			</c:otherwise>
			</c:choose>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">任务详情</h3>
				<div class="tab">
				<table>
					<tr>
						<td class="black">任务名称</td>
						<td colspan="3">${functionVo.name}</td>
						<td class="black">所属项目</td>
						<td>${functionVo.project}</td>
					</tr>
					<tr>
						<td class="black">所属版本</td>
						<td>${functionVo.version}</td>
						<td class="black">所属模块</td>
						<td>${functionVo.module}</td>
						<td class="black">任务类型</td>
						<td>${functionVo.taskType}</td>
					</tr>
					<tr>
						<td class="black">责任人</td>
						<td>${functionVo.assigner}</td>
						<td class="black">关联需求</td>
						<td>${functionVo.associatedRequirement}</td>
						<td class="black">优先级</td>
						<td>${functionVo.priority}</td>
					</tr>
					<tr>
						<td class="black">工时</td>
						<td><c:if test="${functionVo.estimatedTime!='null'}">${functionVo.estimatedTime}</c:if></td>
						<td class="black">分值</td>
						<td>${functionVo.score}</td>
						<td class="black">截止时间</td>
						<td><c:if test="${functionVo.deadline!='null'}">${functionVo.deadline}</c:if></td>
					</tr>
					<tr>
						<td class="black">任务描述</td>
						<td colspan="5" id="description" style="text-align:left">${functionVo.description}</td>
					</tr>
					<tr>
						<td class="black">附件</td>
						<td colspan="5" style="text-align:left">
						<c:forEach items="${functionVo.attachmentNames}" var="attachmentName" varStatus="status">
							<a href="/performance/soft/downloadAttachment?index=${status.index}&instanceId=${functionVo.instanceId}">${attachmentName}</a><br>
						</c:forEach>
						</td>
					</tr>
				</table>
				</div>
				<br>
				  <h3 class="sub-header" style="margin-top:0px;">流程状态</h3>
				  <form class="form-horizontal">
				  <c:if test="${not empty finishedTaskVOs }">
				  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
				  <c:if test="${not empty task }">
				  <div class="form-group">
				  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
				  	</div>
				  	<c:if test="${not empty comments }">
	        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  	<c:if test="${comment.taskID == task.taskID }">
	        	  		<div class="col-sm-2"></div>
					  	<div class="col-sm-10">
					  		<span class="detail-control">${comment.content }</span>
					  	</div>
	        	  	</c:if>
	        	  	</c:forEach>
	        	  	</c:if>
				  </div>
				  </c:if>
				  </c:forEach>
				  </c:if>
				  </form>
				<br>
				<div >
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
    		 var html = marked($("#description").text());
    		 $("#description").html(html);
    	});
  	</script>
</body>
</html>