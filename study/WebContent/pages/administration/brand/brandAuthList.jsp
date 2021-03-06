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
        <s:set name="panel" value="'brandAuthManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">申请列表</h3>
        	<form action="/administration/process/findBrandAuthList" class="form-horizontal" 
        		onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        		<div class="form-group">
        			<label class="col-sm-1 control-label">授权公司</label>
        			<div class="col-sm-3">
        				<input autoComplete="off" name="companyName" value="${companyName}" class="form-control">  
        			</div>
        			<button type="submit" class="btn btn-primary" style="margin-left:3%">查询</button>
        		</div>
        	</form>
			<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th>发起时间</th>
		                  <th>公司</th>
		                  <th>品牌</th>
		                  <th>授权时间</th>
		                  <th>当前处理人</th>
		                  <th style="width:10%">状态</th>
		                  <th style="width:12%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${brandAuthVos}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
			              		<td>${item.companyName}</td>
			              		<td>${item.brand}</td>
			              		<td>${item.authBeginDate} 至 ${item.authEndDate}</td>
			              		<td>${item.assigneeUserName==null?'——':item.assigneeUserName=='完成盖章'?'盖章中':item.assigneeUserName}</td>
			              		<td>${item.status}</td>
			              		<td>
			              		<a onclick="goPath('administration/process/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=allBrandAuthList')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看流程详情" data-toggle="tooltip">
             							<use xlink:href="#icon-liucheng"></use>
             						</svg>
			              		</a>
			              		&nbsp;
			              		<a onclick="goPath('administration/process/showBrandAuthDetail?processInstanceID=${item.processInstanceID}&selectedPanel=allBrandAuthList')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看授权详情" data-toggle="tooltip">
             							<use xlink:href="#icon-Detailedinquiry"></use>
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count }条记录</span>
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