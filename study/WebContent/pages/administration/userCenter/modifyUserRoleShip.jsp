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
<script type="text/javascript">
</script>
<style type="text/css">
	input[type='checkbox']:checked + label{
		background-color:#24312429;
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
        <form action="userCenter/save_saveUserRoleShips" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<s:token></s:token>
        	<c:forEach items="${userRoleShips}" var="userRoleShip">
        		<span style="display:inline-block;width:33%"><input name="roleId" style="margin-bottom:-5px;height:20px" ${userRoleShip[2]!=null?'checked':''} id="role_${userRoleShip[1]}" value="${userRoleShip[1]}" type="checkbox" class="checkboxClass">
        		<label for="role_${userRoleShip[1]}">${userRoleShip[0]}</label></span>
        	</c:forEach>
        	<br>
        	<button style="margin-top:2%" type="submit" class="btn btn-primary">提交</button>
        	<button type="button" style="margin-top:2%;margin-left:1%" class="btn btn-default" onclick="javascript:history.go(-1)">返回</button>
        	<div style="color:red;font-size:12px;margin-top:0.5%">注：勾选需要分配的角色</div>
        	<input type="hidden" name="userId" value="${userId}">
        	<input type="hidden" name="appId" value="${appId}">
        </form>
        </div>
      </div>
    </div>
  </body>
</html>