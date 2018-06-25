<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="assets/js/layer/layer.js"></script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;word-break:break-all !important}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:12px;text-align:center}
	.tab table tbody tr td{height:auto;line-height:10px;padding:5px 5px;}
	.title {color:#000;background:#efefef}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
		<%@include file="/pages/attendance/salayPanel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<h3 class="sub-header" style="margin-top:0px;">申请发放工资的人员明细
        		<button type="button" style="float:right" onclick="history.go(-1)" class="btn btn-default">返回</button>
        	</h3>
        	<div class="tab">
			<table>
				<tr>
					<td class="title" style="width:5%">序号</td>
					<td class="title" style="width:6%">姓名</td>
					<td class="title" style="width:5%">工号</td>
					<td class="title" style="width:10%">公司</td>
					<td class="title" style="width:10%">部门</td>
					<td class="title" style="width:7%">税前工资</td>
					<td class="title" style="width:7%">个税</td>
					<td class="title" style="width:7%">税后工资</td>
				</tr>
				<tbody>
					<c:forEach items="${paySalaryInfos}" var="paySalaryInfo" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${paySalaryInfo.userName}</td>
						<td>${paySalaryInfo.staffNum}</td>
						<td>${paySalaryInfo.company}</td>
						<td>${paySalaryInfo.department}</td>
						<td>${paySalaryInfo.preTaxSalary}</td>
						<td>${paySalaryInfo.personalIncomeTax}</td>
						<td>${paySalaryInfo.afterTaxSalary}</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
		</div>
      </div>
    </div>
</body>
</html>