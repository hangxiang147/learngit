<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
	.hand{
		cursor:hand;
	}
	.icon {
width: 1.5em; height: 1.5em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
.description p img{height:200px;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      		<s:set name="selectedPanel" value="'showRequirementCopy'"></s:set>
			<%@include file="/pages/performance/soft/subject/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      			<h3 class="sub-header" style="margin-top:0px;">任务细分
      			      						<button type="button" class="btn btn-default"
							onclick="location.href='/performance/soft/showRequirementCopy'"
							style="margin-left: 80%">返回</button></h3> 
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-1">需求名称</th>
	                  <th class="col-sm-3">需求描述</th>
	                  <th class="col-sm-1">是否已经分配任务</th>
	                  <th class="col-sm-2">附件</th>
	                  <th class="col-sm-1">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach items="${subRequirementLst}" var="sub" varStatus="status">
	              		<tr>
	              		<td>${status.index+1}</td>
	              		<td>${sub.subRequirementName}</td>
	              		<td class="description">${sub.description}</td>
	              		<td>${sub.count == 0 ?'否':'是'}</td>
	              		<td>
	              		<c:forEach items="${sub.attachmentNames}" var="attachmentName" varStatus="status">
							<a href="/performance/soft/downloadAttachment?attachmentPath=${sub.attachmentPaths[status.index]}&attachmentName=${attachmentName}">${attachmentName}</a><br>
						</c:forEach>
						</td>
	              		<td>
	              			<c:if test="${sub.count == 0}">
	              				              			<a onclick="goPath('/performance/soft/allocateTask?subRequireId=${sub.id}&requirementId=${sub.requirementId}')" href="javascript:void(0)">
	              				              			<svg class="icon" aria-hidden="true" title="分配任务" data-toggle="tooltip">
															<use xlink:href="#icon-xiahurenwufenpei"></use>
														</svg>
	              				              			</a>
	              			</c:if>
	              			<c:if test="${sub.count >0 }">
	              			
	              				              			<a onclick="goPath('/performance/soft/goToTaskManage?subRequirementId=${sub.id}')" href="javascript:void(0)">
	              				              			<svg class="icon" aria-hidden="true" title="查看流程状态" data-toggle="tooltip">
															<use xlink:href="#icon-liucheng"></use>
														</svg>
	              				              			 </a>
	              			
	              			</c:if>
	              		</td>
	              		</tr>
	              	</c:forEach>
	              </tbody>
	            </table>
	          </div>
			  </div>
      </div>
    </div>
  <script src="/assets/icon/iconfont.js"></script>
  <script src="/assets/js/textarea/marked.js"></script>
  <script src="/assets/js/layer/layer.js"></script>
  <script type="text/javascript">
    	function deleteSubRequire(target, requirementId){
    		layer.open({  
	            content: '确定删除吗？',  
	            btn: ['确认', '取消'],  
	            yes: function() {  
	            	var subRequireId = $(target).attr("data-preifx");
					window.location.href='/performance/soft/deleteSubRequire?subRequireId='+subRequireId+"&requirementId="+requirementId;
	            }
	        }); 
    	}
    	$(function(){
    		 $("[data-toggle='tooltip']").tooltip();
       		 $(".description").each(function(){
       			 var html = marked($(this).text());
       			 $(this).html(html);
       		 });
       		 
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
    	});
  </script>
</body>
</html>