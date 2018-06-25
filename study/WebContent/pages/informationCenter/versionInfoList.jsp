<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
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
function deleteVersion(id){
	layer.confirm("确定删除？", {offset:'100px'}, function(index){
		layer.close(index);
		location.href= "/information/version/deleteVersion?id="+id; 
		Load.Base.LoadingPic.FullScreenShow(null);
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
        <s:set name="panel" value="'version'"></s:set>
        <%@include file="/pages/informationCenter/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">版本列表</h3>
       	<form action="information/version/findVersionInfoList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="control-label col-sm-1">版本时间</label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="beginDate" value="${beginDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })" placeholder="开始时间"/>
        		</div>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="endDate" value="${endDate}"
	    			 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })" placeholder="结束时间"/>
        		</div>
        		<button class="btn btn-primary" type="submit">查询</button>
        	</div>
        </form>
       	<auth:hasPermission name="versionManage">
        	<a class="btn btn-primary" onclick="goPath('information/version/addVersionInfo')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span> 版本</a>
		</auth:hasPermission>	
			<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:10%">版本名称</th>
		                  <th style="width:15%">版本时间</th>
		                  <th>功能点介绍</th>
		                  <th style="width:7%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${versionFuncionInfos}" var="item" varStatus="index">
		              		<tr>
			              		<td rowspan="${fn:length(item.function)}">${index.index+startIndex+1}</td>
			              		<td rowspan="${fn:length(item.function)}">${item.versionName}</td>
			              		<td rowspan="${fn:length(item.function)}">${item.versionDate}</td>
			              		<c:forEach begin="0" end="${fn:length(item.function)-1}" varStatus="status">
			              			<c:if test="${status.index!=0}"><tr></c:if>
			              			<td>${item.function[status.index]}</td>
			              			<c:if test="${status.index==0}">
					              		<td rowspan="${fn:length(item.function)}">
					              		<auth:hasPermission name="versionManage">
					              		<a onclick="goPath('information/version/updateVersion?id=${item.id}')" href="javascript:void(0)">
					              			<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
		             							<use xlink:href="#icon-modify"></use>
		             						</svg>
					              		</a>
					              		&nbsp;
					              		<a onclick="deleteVersion(${item.id})" href="javascript:void(0)">
					              			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
		             							<use xlink:href="#icon-delete"></use>
		             						</svg>
					              		</a>
					              		</auth:hasPermission>
					              		</td>
			              			</c:if>
			              		</c:forEach>
         					 </tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
          	</div> 
          	           		   <div class="dropdown" >
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