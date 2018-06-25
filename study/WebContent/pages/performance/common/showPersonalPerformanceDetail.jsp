<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
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
        <%@include file="/pages/performance/common/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">绩效方案明细
       	  	<button class="btn btn-default" onclick="history.go(-1)" style="float:right">返回</button>
       	  </h3> 
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
	           	   <c:forEach items="${projects}" var="project">
		           	   <c:forEach items="${project.staffCheckItems}" var="checkItem" varStatus="status">
		           	   <tr>
		           	   	  <c:if test="${status.index==0}">
		           	   	  	<td rowspan="${fn:length(project.staffCheckItems)*2}">${project.project}</td>
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
      	</div>
      </div>
    </div>
</body>
</html>