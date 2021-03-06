<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
	});
</script>
<style type="text/css">
.icon {
width: 1.5em; height: 1.5em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'process'"></s:set>
			<%@include file="/pages/HR/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">申请列表</h3>
				<div class="table-responsive" style="margin-top: 30px;">
					<table class="table table-striped">
						<thead>
							<tr>
								<th style="width: 5%">序号</th>
								<th style="width: 20%">加班人员</th>
								<th style="width: 10%">所属部门</th>
								<th style="width: 10%">开始时间</th>
								<th style="width: 10%">结束时间</th>
								<th style="width: 10%">预计加班工时</th>
								<th style="width: 15%">加班原因</th>
								<th style="width: 10%">发起时间</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty taskVOs}">
								<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
								<c:forEach items="${taskVOs}" var="item"
									varStatus="index">
									<tr>
										<td>${index.index+startIndex+1}</td>
										<td>${item.requestUserName}</td>
										<td>${item.department}</td>
										<td>${item.beginDate}</td>
										<td>${item.endDate}</td>
										<td>${item.workHours}</td>
										<td>${item.reason}</td>
										<td>${item.requestDate}</td>
										<td>
										<a onclick="goPath('HR/process/taskComplete?taskID=${item.taskID}&result=1&businessType=加班申请')" href="javascript:void(0)">
										<svg class="icon" aria-hidden="true" title="同意" data-toggle="tooltip">
											<use xlink:href="#icon-wancheng"></use>
										</svg>
										</a>
										&nbsp;
										<a onclick="goPath('HR/process/taskComplete?taskID=${item.taskID}&result=2&businessType=加班申请')" href="javascript:void(0)">
										<svg class="icon" aria-hidden="true" title="不同意" data-toggle="tooltip">
											<use xlink:href="#icon-butongyi"></use>
										</svg>
										</a>
										&nbsp;
										<a onclick="goPath('HR/process/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findTaskList')" href="javascript:void(0)">
										<svg class="icon" aria-hidden="true" title="查看已审批环节" data-toggle="tooltip">
											<use xlink:href="#icon-liucheng"></use>
										</svg>
										</a>
										</td>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
				<div class="dropdown">
					<label>每页显示数量：</label>
					<button class="btn btn-default dropdown-toggle" type="button"
						id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="true">
						${limit} <span class="caret"></span>
					</button>
					<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
						style="left: 104px; min-width: 120px;">
						<li><a class="dropdown-item-20" href="#">20</a></li>
						<li><a class="dropdown-item-50" href="#">50</a></li>
						<li><a class="dropdown-item-100" href="#">100</a></li>
					</ul>
					&nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
					<input type="hidden" id="page" value="${page}" /> <input
						type="hidden" id="limit" value="${limit}" /> <input type="hidden"
						id="totalPage" value="${totalPage }" />
				</div>
				<%@include file="/includes/pager.jsp"%>

			</div>
		</div>
	</div>
</body>
</html>
