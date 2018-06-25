<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	function completeTask(result){
		var comment = $("#comment").val();
		var taskId = $("#taskId").val();
		location.href = "administration/performance/pmAudit?result="+result+"&comment="+comment+"&taskId="+taskId;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	.title{background-color:#F2F2F2}
	.tab tr th, .tab tr td{
		word-wrap:break-word;
		word-break:break-all;
		font-size:10px;
		padding:8px 7px;
		text-align:center;
		border:1px solid #ddd
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">绩效方案明细</h3> 
       	   <input id="taskId" value="${taskId}" type="hidden">
       	   <c:forEach items="${postionPerformances}" var="postionPerformance">
 		   <div style="font-size:15px;margin-left:5%;margin-bottom:5px"><span>岗位：</span><span>${postionPerformance.templateName}</span></div>
		   <table class="tab" style="width:90%;margin-left:5%">
	           <thead>
				   <tr>
					   <td class="title" style="width:15%">项目</td>
					   <td class="title" style="width:15%">考核项目</td>
					   <td class="title" style="width:10%">考核系数</td>
					   <td class="title" colspan="2" style="width:20%">奖励条件</td>
					   <td class="title" colspan="2" style="width:20%">少发条件</td>
		           </tr>
	           </thead>
	           <tbody>
	           	   <c:forEach items="${postionPerformance.projects}" var="project">
		           	   <c:forEach items="${project.checkItems}" var="checkItem" varStatus="status">
		           	   <tr>
		           	   	  <c:if test="${status.index==0}">
		           	   	  	<td rowspan="${fn:length(project.checkItems)*2}">${project.project}</td>
		           	   	  </c:if>
		           	   	  	<td rowspan="2">${checkItem.checkItem}</td>
			  				<td rowspan="2">${checkItem.coefficient}</td>
			  				<td class="title">${checkItem.addMoneyType=='+'?'每多':'每少'}</td>
			  			  	<td class="title">奖励金额</td>
			  			  	<td class="title">${checkItem.reduceMoneyType=='-'?'每少':'每多'}</td>
			  			  	<td class="title">少发金额</td>
		           	   </tr>
		           	   <tr>
			           	   <td>${checkItem.perAddMoneyValue!=null?checkItem.perAddMoneyValue:'——'}</td>
			           	   <td>${checkItem.addMoney!=null?checkItem.addMoney:'——'}</td>
			           	   <td>${checkItem.perReduceMoneyValue!=null?checkItem.perReduceMoneyValue:'——'}</td>
			           	   <td>${checkItem.reduceMoney!=null?checkItem.reduceMoney:'——'}</td>
		  			   </tr>
		           	   </c:forEach>
	           	   </c:forEach>
	           </tbody>
           </table>
           </c:forEach>
           <div class="col-sm-12" style="margin-top:2%">
           <label class="control-label col-sm-1" style="width:10%;text-align:left;margin-left:2%">审批意见：</label>
           <div class="col-sm-4">
           	   <textarea rows="3" class="form-control" id="comment"></textarea>
           </div>
           </div>
           <div style="margin-left:5%;margin-top:12%">
           <button class="btn btn-primary" onclick="completeTask(1)">通过</button>
           <button class="btn btn-primary" onclick="completeTask(2)" style="margin-left:3%">打回</button>
           <button class="btn btn-default" style="margin-left:3%" onclick="history.go(-1)">返回</button>
           </div>
      	</div>
      </div>
    </div>
</body>
</html>