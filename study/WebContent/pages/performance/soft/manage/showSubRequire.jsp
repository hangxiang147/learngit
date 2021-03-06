<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForHtml.tld" prefix="hf"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
	.hand{
		cursor:hand;
	}
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;width:20%}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	.tab table tr .black {background:#efefef;text-align:center;color:#000;width:15%}
	#description p img{height:200px;}
	#checkStandard p img{height:200px;}
	.description p img{height:200px;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      		<s:set name="selectedPanel" value="'divideRequire'"></s:set>
      		<%@include file="/pages/performance/soft/manage/panel.jsp" %>
      		<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
      			<h3 class="sub-header" style="margin-top:0px;">任务分解
      			      						<button type="button" class="btn btn-default"
							onclick="location.href='/performance/soft/divideRequireManage'"
							style="margin-left: 80%">返回</button></h3> 
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
				<br>
      			<a class="btn btn-primary"  onclick="goPath('/performance/soft/toEditSubRequire?requirementId=${requirementId}')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span>细分</a>&nbsp;&nbsp;
				<a class="btn btn-primary hand" onclick="completeDivide(${requirementId})">完成分解</a>
				<div class="table-responsive">
	            <table class="table table-striped">
	              <thead>
	                <tr>
	                  <th class="col-sm-1">ID</th>
	                  <th class="col-sm-1">任务名称</th>
	                  <th class="col-sm-1">分值</th>
	                  <th class="col-sm-3">任务描述</th>
	                  <th class="col-sm-2">附件</th>
	                  <th class="col-sm-1">操作</th>
	                </tr>
	              </thead>
	              <tbody>
	              	<c:forEach items="${subRequirementLst}" var="sub" varStatus="status">
	              		<tr>
	              		<td>${status.index+1}</td>
	              		<td>${hf:format(sub.subRequirementName)}</td>
	              		<td>${sub.score}</td>
	              		<td class="description">${sub.description}</td>
	              		<td>
	              		<c:forEach items="${sub.attachmentNames}" var="attachmentName" varStatus="status">
							<a href="/performance/soft/downloadAttachment?attachmentPath=${sub.attachmentPaths[status.index]}&attachmentName=${attachmentName}">${attachmentName}</a><br>
						</c:forEach>
						</td>
	              		<td>
	              			<c:if test="${sub.canEdit}">
		              			<a href="javascript:location.href='/performance/soft/toEditSubRequire?subRequireId=${sub.id}&requirementId=${sub.requirementId}'">编辑 </a>
		              			<a style="cursor:hand" onclick="deleteSubRequire(this,'${sub.requirementId}')" data-preifx="${sub.id}"> | 删除</a>	
	              			</c:if>
	              			<c:if test="${!sub.canEdit}">
		              			——	
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
       		 $(".description").each(function(){
       			 var html = marked($(this).text());
       			 $(this).html(html);
       		 });
       		 var html = marked($("#description").text());
    		 $("#description").html(html);
    		 var html = marked($("#checkStandard").text());
    		 $("#checkStandard").html(html);
    		 
    		 $("#description p img").click(function(){
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
						offset : '1%',
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
						offset : '1%',
						photos : picData,
						anim : 5,
					});
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
						offset : '1%',
						photos : picData,
						anim : 5,
					});
    		 })
    	});
    	 function completeDivide(requireId){
 			layer.open({  
 	            content: '确定完成分解吗？',  
 	            btn: ['确认', '取消'], 
 	            offset:'100px',
 	            yes: function() {  
 	            	$.ajax({
	  	    			url:'/performance/soft/checkIsDivide',
	  	    			type:'post',
	  	    			data:{'requireId':requireId},
	  	    			dataType:'json',
	  	    			success:function (data){
	  	    				if(data.isDivide=="false"){
	  	    					layer.alert("该需求还未分解，无法完成分解！",{offset:'100px'});
	  	    				}else{
	  	    					window.location.href='/performance/soft/completeDivide?requireId='+requireId; 
	  	    				}
	  	    			}
	  	    		});
 	            }
 	        }); 
 	  }
  </script>
</body>
</html>