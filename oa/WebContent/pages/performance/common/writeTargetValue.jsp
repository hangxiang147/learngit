<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	function checkValue(){
		var flag = true;
		$(".targetValue").each(function(){
			if(!checkNum($(this).val())){
				flag = false;
				return false;
			}
		});
		if(flag){
			Load.Base.LoadingPic.FullScreenShow(null);
		}
		return flag;
	}
	function checkNum(num){
		if(num.trim()){
			if(!/^[1-9]+[0-9]*$/.test(num.trim()) && !/^[1-9]+[0-9]*\.[0-9]+$/.test(num.trim()) && !/^0\.[0-9]+$/.test(num.trim())){
				layer.alert("数字输入不合法",{offset:'100px'});
				return false;
			}
		}
		return true;
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
       	  <h3 class="sub-header" style="margin-top:0px;">制定月度指标 </h3>
       	  <form class="form-horizontal" action="/administration/performance/save_saveTargetValue" onsubmit="return checkValue()">
       	  <s:token></s:token>
       	  <input type="hidden" name="taskId" value="${taskId}">
		  <table class="tab" style="width:90%;margin-left:5%">
	           <thead>
				   <tr>
					   <td class="title" style="width:15%">项目</td>
					   <td class="title" style="width:15%">考核项目</td>
					   <td class="title" style="width:10%">考核系数</td>
					   <td class="title" style="width:10%">月度指标<span style="color:red"> *</span></td>
					   <td class="title" colspan="2" style="width:20%">奖励条件</td>
					   <td class="title" colspan="2" style="width:20%">少发条件</td>
		           </tr>
	           </thead>
	           <tbody>
	           	   <c:forEach items="${projects}" var="project">
		           	   <c:forEach items="${project.staffCheckItems}" var="checkItem" varStatus="status">
		           	   <input type="hidden" name="id" value="${checkItem.id}">
		           	   <tr>
		           	   	  <c:if test="${status.index==0}">
		           	   	  	<td rowspan="${fn:length(project.staffCheckItems)*2}">${project.project}</td>
		           	   	  </c:if>
		           	   	  	<td rowspan="2">${checkItem.checkItem}</td>
			  				<td rowspan="2">${checkItem.coefficient}</td>
			  				<td rowspan="2">
			  					<input class="targetValue form-control" required autoComplete="off" name="targetValue"
			  					onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v()">
			  				</td>
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
           <div class="form-group" style="margin-top:1%;margin-left:5%">
           <button type="submit" class="btn btn-primary">提交</button>
           <button type="button" style="margin-left:3%" class="btn btn-default" onclick="history.go(-1)">返回</button>
           </div>
           </form> 
      	</div>
      </div>
    </div>
</body>
</html>