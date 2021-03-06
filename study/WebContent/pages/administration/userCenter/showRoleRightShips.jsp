<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/formatForHtml.tld" prefix="hf"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js?version=<%=Math.random()%>"></script>
<script src="/assets/js/layer/layer.js"></script>
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
        <s:set name="panel" value="'userCenter'"></s:set>
        <s:set name="selectedPanel" value="'roleRightShipManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">权限设置</h3>
        <form action="userCenter/showRoleRightShipList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="col-sm-1 control-label">应用</label>
        		<div class="col-sm-2">
        			<select name="appId" class="form-control">
        				<c:forEach items="${appInfos}" var="appInfo">
        					<option ${appId==appInfo.appId?'selected':''} value="${appInfo.appId}">${appInfo.appName}</option>
        				</c:forEach>
        			</select>
        		</div>
        		<label class="col-sm-1 control-label">角色名称</label>
        		<div class="col-sm-2">
        			<input autoComplete="off" name="roleName" class="form-control" value="${roleName}">
        		</div>
        		<label class="col-sm-1 control-label">权限名称</label>
        		<div class="col-sm-2">
        			<input autoComplete="off" name="rightName" class="form-control" value="${rightName}">
        		</div>
        		<button type="submit" class="btn btn-primary" style="margin-left:2%">查询</button>
        	</div>
        </form>
		<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:8%">序号</th>
		                  <th style="width:20%">角色名称</th>
		                  <th>权限</th>
		                  <th style="width:8%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:forEach items="${roleRightShips}" var="roleRightShip" varStatus="index">
		  					<tr>
		  						<td>${index.index+1+(page-1)*limit}</td>
		  						<td>${roleRightShip[0]}</td>
		  						<td>${roleRightShip[1]}</td>
		  						<td>
		  							<a onclick="goPath('userCenter/modifyRoleRightShip?roleId=${roleRightShip[2]}&appId=${appId}')" href="javascript:void(0)">
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