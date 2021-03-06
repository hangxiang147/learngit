<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
});
</script>
<style type="text/css">
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'partnerCenter'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">历史认购金额缴纳及期权配比明细<button class="btn btn-default" style="float:right" onclick="history.go(-1)">返回</button></h3>
			<div class="table-responsive">
            	<table class="table table-striped">
	              <thead>
	              	<tr>
	              		<td style="width:20%">序号</td>
	              		<td style="width:20%">认购时间</td>
	              		<td style="width:20%">认购金额（元）</td>
	              		<td style="width:20%">期权配比（元）</td>
	              		<td style="width:20%">认购方式</td>
	              	</tr>
	              </thead>
	              <tbody>
	              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					<c:forEach items="${partnerOptionList}" var="partnerOption" varStatus="status">
						<tr>
						<td>${status.index+startIndex+1}</td>
						<td><fmt:formatDate value="${partnerOption.purchaseDate}" pattern="yyyy年MM月dd日" /></td>
						<td>${partnerOption.money==null?'——':partnerOption.money}</td>
						<td>${partnerOption.optionMoney==null?'待配比':partnerOption.optionMoney}</td>
						<td>${partnerOption.purchaseType}</td>
						</tr>
					</c:forEach>
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