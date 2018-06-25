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
	#vehicleInfo{
		border-collapse:collapse;
		width:100%;
		margin-top:5px;
	}
	#vehicleInfo tr td{word-wrap:break-word;font-size:14px;padding:8px 7px;text-align:center;border:1px solid #ddd}
	.title{
		text-align:center !important;
		background:#efefef;
	}
	.form-control{
		display:inline-block;
	}
	.glyphicon-remove:hover{color:red}
	._title{font-weight:bold}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
      	<s:set name="panel" value="'vehicleManage'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px">车辆信息</h3>
					<table id="vehicleInfo">
						<tbody>
							<tr>
								<td colspan="4" class="title">基本信息</td>
							</tr>
							<tr>
								<td class="_title" style="width:25%">车牌</td>
								<td style="width:25%">
									${vehicleVo.licenseNumber}
								</td>
								<td class="_title" style="width:25%">品牌</td>
								<td style="width:25%">
									${vehicleVo.brand}
								</td>
							</tr>
							<tr>
								<td class="_title">车辆所有人</td>
								<td>
									${vehicleVo.ownerName}
								</td>
								<td class="_title">车架号</td>
								<td>
									${vehicleVo.frameNumber}
								</td>
							</tr>
							<tr>
								<td class="_title">使用性质</td>
								<td>
									${vehicleVo.useProperty}
								</td>
								<td class="_title">发动机号</td>
								<td>
									${vehicleVo.engineNumber}
								</td>
							</tr>
							<tr>
								<td class="_title">核定载重量</td>
								<td>
									${vehicleVo.deadWeight}
								</td>
								<td class="_title">车辆负责人</td>
								<td>
									${vehicleVo.leaderName}
								</td>
							</tr>
						</tbody>
						<tbody>
							<c:if test="${null!=vehicleVo.insuranceRecordVo}">
								<tr>
									<td colspan="4" class="title">保险记录</td>
								</tr>
								<tr>
									<td class="_title" colspan="2">时间</td>
									<td class="_title">保额（元）</td>
									<td class="_title">下次保险时间</td>
								</tr>
							</c:if>
							<c:forEach items="${vehicleVo.insuranceRecordVo.time}" varStatus="status">
								<tr>
									<td colspan="2">${vehicleVo.insuranceRecordVo.time[status.index]}</td>
									<td>${vehicleVo.insuranceRecordVo.money[status.index]}</td>
									<td>${vehicleVo.insuranceRecordVo.nextInsuranceTime[status.index]}</td>
								</tr>
							</c:forEach>
							<c:if test="${null!=vehicleVo.yearlyInspectionVo}">
							<tr>
								<td colspan="4" class="title">年检记录</td>
							</tr>
							<tr>
								<td class="_title" colspan="2">时间</td>
								<td class="_title" colspan="2">下次年检时间</td>
							</tr>
							</c:if>
							<c:forEach items="${vehicleVo.yearlyInspectionVo.time}" varStatus="status">
								<tr>
									<td colspan="2">${vehicleVo.yearlyInspectionVo.time[status.index]}</td>
									<td colspan="2">${vehicleVo.yearlyInspectionVo.nextYearlyInspectionTime[status.index]}</td>
								</tr>
							</c:forEach>
							<c:if test="${null!=vehicleVo.maintainRecordVo}">
							<tr>
								<td colspan="4" class="title">保养记录</td>
							</tr>
							<tr>
								<td class="_title" colspan="2">时间</td>
								<td class="_title">保养里程数（公里）</td>
								<td class="_title">保养费用（元）</td>
							</tr>
							</c:if>
							<c:forEach items="${vehicleVo.maintainRecordVo.time}" varStatus="status">
								<tr>
									<td colspan="2">${vehicleVo.maintainRecordVo.time[status.index]}</td>
									<td>${vehicleVo.maintainRecordVo.maintainMileages[status.index]}</td>
									<td>${vehicleVo.maintainRecordVo.maintainMoney[status.index]}</td>
								</tr>
							</c:forEach>
							<c:if test="${null!=vehicleVo.repairRecordVo}">
							<tr>
								<td colspan="4" class="title">维修记录</td>
							</tr>
							<tr>
								<td class="_title">时间</td>
								<td class="_title" colspan="2">维修项目</td>
								<td class="_title">维修金额（元）</td>
							</tr>
							</c:if>
							<c:forEach items="${vehicleVo.repairRecordVo.time}" varStatus="status">
								<tr>
									<td>${vehicleVo.repairRecordVo.time[status.index]}</td>
									<td colspan="2">${vehicleVo.repairRecordVo.repairItems[status.index]}</td>
									<td>${vehicleVo.repairRecordVo.repairMoney[status.index]}</td>
								</tr>
							</c:forEach>
							<c:if test="${null!=vehicleVo.vehicleUseRecordVo}">
							<tr>
								<td colspan="4" class="title">车辆使用记录</td>
							</tr>
							<tr>
								<td class="_title">时间</td>
								<td class="_title" colspan="2">事由</td>
								<td class="_title">使用人</td>
							</tr>
							</c:if>
							<c:forEach items="${vehicleVo.vehicleUseRecordVo.time}" varStatus="status">
								<tr>
									<td>${vehicleVo.vehicleUseRecordVo.time[status.index]}</td>
									<td colspan="2">${vehicleVo.vehicleUseRecordVo.reason[status.index]}</td>
									<td>${vehicleVo.vehicleUseRecordVo.userName[status.index]}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<br>
					<button class="btn btn-default" style="float:right;margin-right:5px" onclick="location.href='javascript:history.go(-1);'">返回</button>
			</div>
		</div>
	</div>
</body>
</html>