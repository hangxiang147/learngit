<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'public'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">公关事件列表<button style="float:right" class="btn btn-default" onclick="history.go(-1)">返回</button></h3>
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td style="width:3%">序号</td>
	              		<td style="width:20%">需公关事件说明</td>
	              		<td style="width:10%">申请人</td>
	              		<td style="width:10%">申请人电话</td>
	              		<td style="width:10%">申请时间</td>
	              		<td style="width:10%">处理人</td>
	              		<td style="width:5%">状态</td>
	              	</tr>
	              </thead>
	              <tbody>
					<c:forEach items="${historicalPublicEvents}" var="publicEvent" varStatus="status">
						<tr>
						<td>${status.index+1}</td>
						<td>${publicEvent.eventDescription}</td>
						<td>${publicEvent.userName}</td>
						<td>${publicEvent.phone}</td>
						<td>${publicEvent.requestDate}</td>
						<td>${publicEvent.handlerName}</td>
						<td>
							<c:if test="${publicEvent.applyResult==48}">处理失败</c:if>
							<c:if test="${publicEvent.applyResult==46}">已处理</c:if>
							<c:if test="${publicEvent.applyResult==null}">处理中</c:if>
						</td>
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