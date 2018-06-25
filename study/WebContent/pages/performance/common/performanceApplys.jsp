<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	$("[data-toggle='tooltip']").tooltip();
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
        <%@include file="/pages/performance/common/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">绩效方案申请列表</h3> 
          	<div class="table-responsive">
            <table class="table table-striped">
	           <thead>
	           	   <tr>
	           	     <td style="width:5%">序号</td>
	           	     <td style="width:20%">绩效岗位</td>
	           	     <td style="width:15%">申请时间</td>
	           	     <td style="width:10%">当前处理人</td>
	           	     <td style="width:10%">状态</td>
	           	     <td style="width:6%">操作</td>
	           	   </tr>
	           </thead>
	           <tbody>
	           		<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
	           		<c:forEach items="${performances}" var="performance" varStatus="status">
	           			<tr>
	           				<td>${status.index+startIndex+1}</td>
	           				<td>${performance.positionNames}</td>
	           				<td><fmt:formatDate value="${performance.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	           				<td>${performance.assigneeUserName==null ? '——':performance.assigneeUserName}</td>
	           				<td>${performance.status}</td>
	           				<td>
	           					<a href="/administration/performance/showPerformanceDetail?templateIds=${performance.templateIds}">
			              			<svg class="icon" aria-hidden="true" title="查看方案明细" data-toggle="tooltip">
	            						<use xlink:href="#icon-Detailedinquiry"></use>
	            					</svg>
		              			</a>
		              			&nbsp;
		              			<a href="/administration/performance/showProcessHistory?selectedPanel=performanceApplys&processInstanceId=${performance.processInstanceID}">
			              			<svg class="icon" aria-hidden="true" title="查看流程" data-toggle="tooltip">
	            						<use xlink:href="#icon-liucheng"></use>
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