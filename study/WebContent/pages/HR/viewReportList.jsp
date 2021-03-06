<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
	});
	function auditTask(taskId){
		layer.confirm("<label class='col-sm-4'>审批意见：</label><div class='col-sm-8'><input class='form-control' id='comment'/></div>",{title:'审批',id:'auditBtn',offset:'100px',btn:['同意', '不同意']},
		function(index){
			layer.close(index);
			location = 'HR/process/taskComplete?taskID='+taskId+'&result=1&businessType=日报查看申请&comment='+$("#comment").val();
			Load.Base.LoadingPic.FullScreenShow(null);
		}, function(index){
			layer.close(index);
			location = 'HR/process/taskComplete?taskID='+taskId+'&result=2&businessType=日报查看申请&comment='+$("#comment").val();
			Load.Base.LoadingPic.FullScreenShow(null);
		});
		$("#auditBtn").next().next().css("text-align","center");
	}
</script>
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover,a:focus,a:visited{text-decoration:none}
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
								<th style="width: 4%">序号</th>
								<th style="width: 6%">申请人员</th>
								<th style="width: 10%">所属部门</th>
								<th style="width: 15%">查看公司（部门）</th>
								<th style="width: 17%">查看人员</th>
								<th style="width: 7%">查看权限</th>
								<th style="width: 10%">申请时间</th>
								<th style="width: 5%">操作</th>
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
										<td>
											<c:if test="${empty item.companyAndDepList}">——</c:if>
											<c:forEach items="${item.companyAndDepList}" var="companyAndDep">
												${companyAndDep}<br>
											</c:forEach>
										</td>
										<td>
										<c:if test="${empty item.userNames}">——</c:if>
										${item.userNames}
										</td>
										<td>${item.viewType}</td>
										<td>${item.requestDate}</td>
										<td>
										<a href="javascipt:void(0)" onclick="auditTask(${item.taskID})">
											<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
	             								<use xlink:href="#icon-shenpi"></use>
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
