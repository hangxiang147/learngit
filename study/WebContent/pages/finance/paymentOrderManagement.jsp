<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function() {
	set_href();
	$("[data-toggle='tooltip']").tooltip();
	
	var statusEctype = $("#statusEctype").val();
	$("#status").find("option[value='"+statusEctype+"']").attr("selected","selected");
	
	var thecurrenLinkEctype = $("#thecurrenLinkEctype").val();
	$("#thecurrenLink").find("option[value='"+thecurrenLinkEctype+"']").attr("selected","selected");
	
	$(".candidateUsers").each(function(){
		if($(this).text().length>36){
			$(this).html($(this).text().replace(/\s+/g, "").substr(0, 8) + "...");
		}
	})
});
function search(){
	window.location.href = "finance/reimbursement/paymentOrderManagement?" + $("#queryReimbursementForm").serialize();
	Load.Base.LoadingPic.FullScreenShow(null);
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
			<s:set name="panel" value="'paymentOfRefund'"></s:set>
			<s:set name="selectedPanel" value="'paymentOrderManagement'"></s:set>
			<%@include file="/pages/finance/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<form action="" id="queryReimbursementForm" class="form-horizontal">
					<h3 class="sub-header" style="margin-top:0px;">付款列表</h3>
					<div class="form-group">
						<label for="demandName" class="col-sm-1 control-label">申请人</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="requestUserName" name="paymentVo.requestUserName" value="${paymentVo.requestUserName }" >
						</div>
						
						<label for="reimbursementNo" class="col-sm-1 control-label">付款单号</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="reimbursementNo" name="paymentVo.reimbursementNo" value="${paymentVo.reimbursementNo }" >
						</div>
						
						<label for="beginDate" class="col-sm-1 control-label">发起时间</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="beginDate" name="paymentVo.beginDate" value="${paymentVo.beginDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
						</div>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="endDate" name="paymentVo.endDate" value="${paymentVo.endDate }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
						</div>
					</div>

					<div class="form-group">
						<label for="status" class="col-sm-1 control-label">流程状态</label>
					    <div class="col-sm-2">
					    	<select class="form-control" name="paymentVo.status" id="status">
					    		<option value="请选择">请选择</option>
					    		<option selected value="进行中">进行中</option>
					    		<option value="打款成功">打款成功</option>
					    		<option value="未通过完结">未通过完结</option>
					    		<option value="流程作废">流程作废</option>
					    	</select>
					    	<input id="statusEctype" type="hidden" class="form-control" value="${paymentVo.status }">
					    </div>
					    
					    <label class="col-sm-1 control-label" for="">当前环节</label>
					    <div class="col-sm-2">
					    	<select class="form-control" name="paymentVo.thecurrenLink" id="thecurrenLink">
					    		<option value="">请选择</option>
					    		<option value="主管审批">主管审批</option>
					    		<option value="分公司总经理审批">分公司总经理审批</option>
					    		<option value="财务一级审批">财务一级审批</option>
					    		<option value="财务主管审批">财务主管审批</option>
					    		<option value="总公司总经理">总公司总经理</option>
								<option value="资金分配">资金分配</option>
					    		<option value="财务打款">财务打款</option>
					    		<option value="修改打款账号">修改打款账号</option>
					    	</select>
					    	<input class="form-control" type="hidden" id="thecurrenLinkEctype" value="${paymentVo.thecurrenLink }">
					    </div>
					
						<label class="col-sm-1 control-label">待处理人</label>
						<div class="col-sm-2">
							<input type="text" class="form-control" id="assigneeUserName" name="paymentVo.assigneeUserName" value="${paymentVo.assigneeUserName }">
						</div>
						
						<div class="col-sm-2" style="padding-top:2px;">
							<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
						</div>
					</div>
					
				</form>
				
				<h3 class="sub-header"></h3>
				
				<div class="table-responsive">
					<table class="table table-striped">
						<thead>
							<tr>
								<th style="width:6%;">序号</th>
								<th style="width:8%">申请人<br>/在职状态</th>
								<th style="width:12%">付款单号</th>
								<th style="width:8%">合计金额</th>
								<th style="width:8%">发起时间</th>
								<th style="width:10%">流程状态</th>
								<th style="width:10%">当前环节<br>/待处理人</th>
								<th style="width:8%">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:if test="${not empty paymentVoList }">
							<s:set name="reimbursement_id" value="(#request.page-1)*(#request.limit)"></s:set>
								<c:forEach items="${paymentVoList }" var="items" varStatus="number">
									<tr>
										<td><s:property value="#reimbursement_id+1"/></td>
										<td>
											${items.requestUserName }<br>
											<c:if test="${items.workingState=='在职' }">
												<span>${items.workingState }</span>
											</c:if>
											<c:if test="${items.workingState=='离职' }">
												<span style="color:red">${items.workingState }</span>
											</c:if>
											
										</td>
										<td>${items.reimbursementNo }</td>
										<td>${items.totalAmount }</td>
										<td>
											<fmt:formatDate value="${items.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
										</td>
										<td>${items.status }</td>
										<td>
											<c:choose>
												<c:when test="${items.thecurrenLink==null && items.assigneeUserName==null }">
													――――――
												</c:when>
												<c:when test="${items.thecurrenLink!=null && items.assigneeUserName==null }">
													${items.thecurrenLink }
													<br>
													<span class="candidateUsers" title="${items.candidateUsers }">
														${items.candidateUsers }
													</span>
												</c:when>
												<c:otherwise>
													${items.thecurrenLink }<br>${items.assigneeUserName }
												</c:otherwise>
											</c:choose>
										</td>
										<td>
											<a onclick="goPath('finance/reimbursement/getPaymentDetail?type=2&processInstanceID=${items.processInstanceID }')" href="javascript:void(0)">
						              			<svg class="icon" aria-hidden="true" title="查看付款单详情" data-toggle="tooltip">
						    						<use xlink:href="#icon-Detailedinquiry"></use>
												</svg>
					              			</a>
					              			&nbsp;
					              			<a onclick="goPath('finance/reimbursement/getReimbursementProcess?type=5&processInstanceID=${items.processInstanceID }&selectedPanel=${selectedPanel }')" href="javascript:void(0)">
						              			<svg class="icon" aria-hidden="true" title="流程审批详情" data-toggle="tooltip">
						    						<use xlink:href="#icon-liucheng"></use>
												</svg>
					              			</a>
										</td>
									</tr>
									<s:set name="reimbursement_id" value="#reimbursement_id+1"></s:set>
								</c:forEach>
							</c:if>
						</tbody>
					</table>
				</div>
				
				
				<div class="dropdown">
					<label>每页显示数量：</label>
					<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
						${limit}
						<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
						<li><a class="dropdown-item-20" href="#">20</a></li>
						<li><a class="dropdown-item-50" href="#">50</a></li>
						<li><a class="dropdown-item-100" href="#">100</a></li>
					</ul>
					&nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
					<input type="hidden" id="page" value="${page}"/>
					<input type="hidden" id="limit" value="${limit}" />
					<input type="hidden" id="totalPage" value="${totalPage }" />
				</div>
				<%@include file="/includes/pager.jsp" %>
				
			</div>
		</div>
	</div>
</body>
</html>