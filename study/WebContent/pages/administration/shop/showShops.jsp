<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
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
function deleteShop(id){
	layer.confirm("确定删除？", {offset:'100px'}, function(index){
		layer.close(index);
		location.href= "/administration/shopManage/deleteShop?id="+id; 
		Load.Base.LoadingPic.FullScreenShow(null);
	});
}
function closeShop(id){
	layer.confirm("确定关闭？", {offset:'100px'}, function(index){
		layer.close(index);
		location.href= "/administration/shopManage/closeShop?id="+id; 
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
        <s:set name="panel" value="'shopManage'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">店铺列表</h3>
       	<form action="administration/shopManage/showShops" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="control-label col-sm-1" style="width:10%">预留手机号</label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="reserveTelephone" 
        			oninput="(this.v=function(){this.value=this.value.replace(/[^0-9]+/,'');}).call(this)" onblur="this.v()" value="${reserveTelephone}"/>
        		</div>
        		<button class="btn btn-primary" type="submit" style="margin-left:3%">查询</button>
        	</div>
        </form>
        <a class="btn btn-primary" onclick="goPath('administration/shopManage/addShopInfo')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span> 店铺</a>
			<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th>店铺品牌</th>
		                  <th>账号</th>
		                  <th>密码</th>
		                  <th>注册公司</th>
		                  <th>预留手机号</th>
		                  <th>预留手机号属主</th>
		                  <th style="width:14%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${shopInfoList}" var="item" varStatus="index">
		  					<tr>
		  						<td>${startIndex+index.index+1}</td>
		  						<td>${item.shopName}</td>
		  						<td>${item.account}</td>
		  						<td>${item.pwd}</td>
		  						<td>${item.registerCompany}</td>
		  						<td>${item.reserveTelephone}</td>
		  						<td>${item.reservePhoneOwner}</td>
		  						<td>
		  							<a onclick="goPath('administration/shopManage/showShopInfoDetail?shopInfoId=${item.id}')" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
    									<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
									</a>
									&nbsp;
									<a onclick="goPath('administration/shopManage/updateShopInfo?shopInfoId=${item.id}')" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
    									<use xlink:href="#icon-modify"></use>
									</svg>
									</a>
									&nbsp;
									<c:if test="${item.shopStatus=='0'}">
									<a onclick="closeShop(${item.id})" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="关闭" data-toggle="tooltip">
    									<use xlink:href="#icon-delete2"></use>
									</svg>
									</a>
									&nbsp;
									</c:if>
									<a onclick="deleteShop(${item.id})" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
    									<use xlink:href="#icon-delete"></use>
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