<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	function toModifyPersonalPerformance(userId){
		$.ajax({
			url:'/administration/performance/checkHasUpdateApply',
			data:{'userId':userId,'year':'${year}','month':'${month}'},
			success:function(data){
				if(data.hasUpdateApply){
					layer.alert("修改申请已在审批，不可重复提交", {offset:'100px'});
				}else{
					location.href = '/administration/performance/toModifyPersonalPerformance?userId='+userId+'&year=${year}&month=${month}';
					Load.Base.LoadingPic.FullScreenShow(null);
				}
			}
		});
	}
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
			<%@include file="/pages/performance/common/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<form class="form-horizontal">
					<h3 class="sub-header" style="margin-top: 0px;">个人绩效考核</h3>
					<div class="form-group">
						<label class="col-sm-1 control-label">姓名</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="userName" value="${userName}">
						</div>
						<label class="col-sm-1 control-label">年份</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="year" value="${year}">
						</div>
						<label class="col-sm-1 control-label">月份</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="month" value="${month}">
						</div>
					</div>
					<div class="col-sm-1"></div>
					<button type="submit" class="btn btn-primary">查询</button>
				</form>

				<h2 class="sub-header"></h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead style="word-break:keep-all">
							<tr>
								<th>序号</th>
								<th>公司</th>
								<th>岗位</th>
								<th>姓名</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${performances}" var="performance" varStatus="status">
								<tr>
									<td>${status.index+1+(page-1)*limit}</td>
									<td>${performance[2]}</td>
									<td>${performance[3]}</td>
									<td>${performance[1]}</td>
									<td>
										<a onclick="goPath('/administration/performance/showPersonalPerformance?selectedPanel=underlingPerformances&userId=${performance[0]}&year=${year}&month=${month}')" href="javascript:void(0)">
				        					<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
				      							<use xlink:href="#icon-Detailedinquiry"></use>
				      						</svg>
		      							</a>
		      							&nbsp;
										<a onclick="toModifyPersonalPerformance('${performance[0]}')" href="javascript:void(0)">
				        					<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
				      							<use xlink:href="#icon-modify"></use>
				      						</svg>
		      							</a>
									</td>
								</tr>
							</c:forEach>
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
