<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/require/require2.js"></script>
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
	<div class="container_fluid">
		<div class="row">
			<s:set name="panel" value="'dangan'"></s:set>
			<%@include file="/pages/HR/panel.jsp" %>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top:0px;">证书审核列表</h3>
				
				<a onclick="goPath('HR/staff/showPostCredentialApply')" href="javascript:void(0)" class="btn btn-primary">
					<span class="glyphicon glyphicon-plus"></span> 发起证书审核
				</a>
				
				<table class="table table-striped" style="margin-top:2%;">
					<thead>
						<tr>
							<th style="width:13%;">序号</th>
							<th style="width:15%;">审核人</th>
							<th style="width:22%;">发起时间</th>
							<th style="width:15%;">上传证书人</th>
							<th style="width:15%;">状态</th>
							<th style="width:15%;">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${not empty credentialList }">
						<c:set var="credential_id" value="${(page-1)*limit}"></c:set>
							<c:forEach items="${credentialList }" var="credList" varStatus="number">
								<tr>
									<td>${number.index+credential_id+1 }</td>
									<td id="applyUserId${credList.id }" data-applyUserId="${credList.applyUserId }">
										
									</td>
									<td>
										<fmt:formatDate value="${credList.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
									</td>
									<td id="offerUserId${credList.id }" data-offerUserId="${credList.offerUserId }">
										
									</td>
									<script type="text/javascript">
										$(function(){
											$("[data-toggle='tooltip']").tooltip();
											set_href();
											var applyUserId = $("#applyUserId${credList.id }").attr("data-applyUserId");
											var offerUserId = $("#offerUserId${credList.id }").attr("data-offerUserId");
											$.ajax({
												type:"post",
												data:{"applyUserId":applyUserId,"offerUserId":offerUserId},
												url:"HR/staff/getStaffnameByUserId",
												success: function(data){
													$("#applyUserId${credList.id }").html(data.applyUserName);
													$("#offerUserId${credList.id }").html(data.offerUserName);
												}
											})
										})
									</script>
									<c:choose>
										<c:when test="${credList.status==1 }">
											<td>证书上传</td>
										</c:when>
										<c:when test="${credList.status==2 }">
											<td>正在审核</td>
										</c:when>
										<c:when test="${credList.status==3 }">
											<td>审核完成</td>
										</c:when>
										<c:when test="${credList.status==4 }">
											<td>不通过重新上传</td>
										</c:when>
										<c:otherwise>
											<td>无状态</td>
										</c:otherwise>
									</c:choose>
									<td>
										<a onclick="goPath('HR/staff/queryLog?credentialEntityId=${credList.id }')" href="javascript:void(0)">
				              				<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
												<use xlink:href="#icon-Detailedinquiry"></use>
											</svg>
										</a>
										
									</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
				
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