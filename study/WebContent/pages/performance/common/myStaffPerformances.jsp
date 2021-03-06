<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
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
       	  <h3 class="sub-header" style="margin-top:0px;">我的月度绩效考核明细</h3>
		  <form class="form-horizontal">
		  		<div class="form-group">
					<label class="col-sm-1 control-label">年份</label>
					<div class="col-sm-2">
						<input autoComplete="off" class="form-control" name="year" value="${year}">
					</div>
					<label class="col-sm-1 control-label">月份</label>
					<div class="col-sm-2">
						<input autoComplete="off" class="form-control" name="month" value="${month}">
					</div>
					<button type="submit" class="btn btn-primary" style="margin-left:2%">查询</button>
				</div>
		  </form>
		  <table class="tab" style="width:90%;margin-left:5%">
	           <thead>
	           	   <tr>
	           	   	<td class="title">姓名</td>
	           	   	<td>${staff.staffName}</td>
	           	   	<td class="title">考核期间</td>
	           	   	<td>${month}</td>
	           	   	<td class="title">身份证号</td>
	           	   	<td colspan="3">${staff.idNumber}</td>
	           	   	<td class="title">薪资标准</td>
	           	   	<td>${staff.salary}</td>
	           	   	<td class="title">绩效工资</td>
	           	   	<td>${staff.performance}</td>
	           	   </tr>
				   <tr>
					   <td class="title" style="width:10%">项目</td>
					   <td class="title" style="width:10%">考核项目</td>
					   <td class="title" style="width:8%">考核系数</td>
					   <td class="title" style="width:8%">占比金额</td>
					   <td class="title" style="width:8%">平衡点</td>
					   <td class="title" colspan="2" style="width:16%">奖励条件</td>
					   <td class="title" colspan="2" style="width:16%">少发条件</td>
					   <td class="title" style="width:8%">完成值</td>
					   <td class="title" style="width:8%">绩效金额</td>
					   <td class="title" style="width:8%">绩效工资</td>
		           </tr>
	           </thead>
	           <tbody>
	           	   <c:forEach items="${projects}" var="project">
		           	   <c:forEach items="${project.staffCheckItems}" var="checkItem" varStatus="status">
		           	   <input type="hidden" name="staffCheckItem[${status.index}].id" value="${checkItem.id}">
		           	   <tr>
		           	   	  <c:if test="${status.index==0}">
		           	   	  	<td rowspan="${fn:length(project.staffCheckItems)*2}">${project.project}</td>
		           	   	  </c:if>
		           	   	  	<td rowspan="2">${checkItem.checkItem}</td>
			  				<td rowspan="2">${checkItem.coefficient}</td>
			  				<td rowspan="2">${checkItem.rateMoney}</td>
			  				<td rowspan="2">${checkItem.targetValue}</td>
			  				<td class="title">${checkItem.addMoneyType=='+'?'每多':'每少'}</td>
			  			  	<td class="title">奖励金额</td>
			  			  	<td class="title">${checkItem.reduceMoneyType=='-'?'每少':'每多'}</td>
			  			  	<td class="title">少发金额</td>
			  			  	<td rowspan="2">${checkItem.actualValue}</td>
			  			  	<td rowspan="2">${checkItem.performanceMoney}</td>
			  			  	<td rowspan="2">${checkItem.performanceSalary}</td>
		           	   </tr>
		           	   <tr>
			           	   <td>${checkItem.perAddMoneyValue!=null?checkItem.perAddMoneyValue:'——'}</td>
			           	   <td>${checkItem.addMoney!=null?checkItem.addMoney:'——'}</td>
			           	   <td>${checkItem.perReduceMoneyValue!=null?checkItem.perReduceMoneyValue:'——'}</td>
			           	   <td>${checkItem.reduceMoney!=null?checkItem.reduceMoney:'——'}</td>
		  			   </tr>
		           	   </c:forEach>
	           	   </c:forEach>
	           	   <tr>
	           	   	<td class="title">合计</td>
	           	   	<td></td>
	           	   	<td>100%</td>
	           	   	<td>${staff.performance}</td>
	           	   	<td></td>
	           	   	<td></td>
	           	   	<td></td>
	           	   	<td></td>
	           	   	<td></td>
	           	   	<td></td>
	           	   	<td></td>
	           	   	<td>${staff.totalPerformanceMoney}</td>
	           	   </tr>
	           	   <tr>
	           	   </tr>
	           </tbody>
           </table>
      	</div>
      </div>
    </div>
</body>
</html>