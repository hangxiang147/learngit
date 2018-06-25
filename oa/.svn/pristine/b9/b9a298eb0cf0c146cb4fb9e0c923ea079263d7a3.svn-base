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
function checkName(){
	var roleName = $("input[name='roleInfo.roleName']").val();
	var appId = $("select[name='roleInfo.appId'] option:selected").val();
	var id = $("input[name='roleInfo.id']").val();
	var flag = true;
	$.ajax({
		url:'userCenter/checkRoleName',
		data:{'roleName':roleName,'appId':appId,'id':id},
		async:false,
		success:function(data){
			if(data.exist){
				layer.alert("角色名称已存在",{offset:'100px'});
				flag = false;
			}
		}
	});
	return flag;
}
function addLoding(){
	if(!checkName()){
		return false;
	}
	$("#addRole").modal('hide');
	Load.Base.LoadingPic.FullScreenShow(null);
}
function addRole(){
	$("input[name='roleInfo.roleName']").val('');
	$("input[name='roleInfo.roleDescription']").val('');
	$("input[name='roleInfo.id']").val('');
	$("select[name='roleInfo.appId'] option").each(function(value, index){
		if($(this).attr("selected")){
			$(this).removeAttr("selected");
		}
	});
	$("#addRole").modal('show');
}
function modifyRole(id, roleName, roleDescription){
	var appId = $("select[name='appId'] option:selected").val();
	$("select[name='roleInfo.appId'] option").each(function(value, index){
		if(appId==$(this).val()){
			$(this).attr("selected","selected");
		}
	});
	$("input[name='roleInfo.roleName']").val(roleName);
	$("input[name='roleInfo.roleDescription']").val(roleDescription);
	$("input[name='roleInfo.id']").val(id);
	$("#addRole").modal('show');
}
function deleteRole(roleId){
	layer.confirm("确定删除？",{offset:'100px'},function(index){
		layer.close(index);
		location.href = "userCenter/deleteRole?roleId="+roleId;
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
        <s:set name="panel" value="'userCenter'"></s:set>
        <s:set name="selectedPanel" value="'userRoleShipManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">角色分配</h3>
        <form action="userCenter/showUserRoleShipList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="col-sm-1 control-label">应用</label>
        		<div class="col-sm-2">
        			<select name="appId" class="form-control">
        				<c:forEach items="${appInfos}" var="appInfo">
        					<option ${appId==appInfo.appId?'selected':''} value="${appInfo.appId}">${appInfo.appName}</option>
        				</c:forEach>
        			</select>
        		</div>
        		<label class="col-sm-1 control-label">姓名</label>
        		<div class="col-sm-2">
        			<input autoComplete="off" name="staffName" class="form-control" value="${staffName}">
        		</div>
        		<label class="col-sm-1 control-label">角色名称</label>
        		<div class="col-sm-2">
        			<input autoComplete="off" name="roleName" class="form-control" value="${roleName}">
        		</div>
        		<button type="submit" class="btn btn-primary" style="margin-left:2%">查询</button>
        	</div>
        </form>
		<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:10%">姓名</th>
		                  <th style="width:25%">所属部门</th>
		                  <th>角色</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:forEach items="${userRoleShips}" var="userRoleShip" varStatus="index">
		  					<tr>
		  						<td>${index.index+1+(page-1)*limit}</td>
		  						<td>${userRoleShip[0]}</td>
		  						<td>${userRoleShip[1]}</td>
		  						<td>${userRoleShip[2]}</td>
		  						<td>
		  							<a onclick="goPath('userCenter/modifyUserRoleShip?userId=${userRoleShip[3]}&appId=${appId}')" href="javascript:void(0)">
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
   	<div id="addRole" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form method="post" action="userCenter/saveRole"
		class="form-horizontal" onsubmit="return addLoding()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">新增角色</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
	    	  		<label class="col-sm-3 control-label">应用<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
			    		<select name="roleInfo.appId" class="form-control" required>
			    			<option value="">请选择</option>
	        				<c:forEach items="${appInfos}" var="appInfo">
	        					<option value="${appInfo.appId}">${appInfo.appName}</option>
	        				</c:forEach>
	        			</select>
		    		</div>
	    	  	</div>
				<div class="form-group">
	    	  		<label class="col-sm-3 control-label">角色名称<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
		    		<input autoComplete="off" class="form-control" required name="roleInfo.roleName">
		    		</div>
	    	  	</div>
	    	  	<div class="form-group">
	    	  		<label class="col-sm-3 control-label">角色描述</label>
		    		<div class="col-sm-7">
		    		<input autoComplete="off" class="form-control" name="roleInfo.roleDescription">
		    		</div>
	    	  	</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
			<input name="roleInfo.id" type="hidden">
		</div>
		</form>
	</div>
	</div>
  </body>
</html>