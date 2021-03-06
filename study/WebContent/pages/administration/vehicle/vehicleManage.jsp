<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	function showVehicleDetails(id){
		location.href = "administration/vehicle/showVehicleDetails?id="+id;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function deleteVehicle(id){
		layer.confirm("确定删除吗？",{offset:'100px'},function(index){
			layer.close(index);
			location.href = "administration/vehicle/deleteVehicle?id="+id;
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	}
	function updateVehicleInfo(id){
		location.href = "administration/vehicle/updateVehicleInfo?id="+id;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	.glyphicon-remove:hover{color:red}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'vehicleManage'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="administration/vehicle/findVehicleList" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">车辆管理</h3> 
			   <div class="form-group" >
			  	<label class="col-sm-1 control-label">车牌号</label>
			  	<div class="col-sm-2" ><input type="text" autoComplete="off" class="form-control" name="licenseNumber" value="${licenseNumber}"></div>
			  	<button type="submit" id="submitButton" class="btn btn-primary" style="margin-left:2%">查询</button>
			  </div>
          </form>
          <a class="btn btn-primary"  onclick="goPath('administration/vehicle/addVehicle')" href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span> 车辆</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">车牌</th>
                  <th class="col-sm-1">品牌</th>
                  <th class="col-sm-1">车辆所有人</th>
                  <th class="col-sm-1">车辆负责人</th>
                  <th class="col-sm-1">核定载重量</th> 
                  <th class="col-sm-1">操作</th> 
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${VehicleInfoList}" var="VehicleInfo" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${VehicleInfo.licenseNumber}</td>
              			<td>${VehicleInfo.brand}</td>
              			<td>${VehicleInfo.ownerName}</td>
              			<td>${VehicleInfo.leaderName}</td>
              			<td>${VehicleInfo.deadWeight}</td>
              			<td>
              				<a href="javascript:void(0)" onclick="showVehicleDetails('${VehicleInfo.id}')">
              				<span data-toggle='tooltip' title="查看" class="glyphicon glyphicon-eye-open"></span></a>&nbsp;&nbsp;
              				<a href="javascript:void(0)" onclick="updateVehicleInfo('${VehicleInfo.id}')">
              				<span data-toggle='tooltip' title="编辑" class="glyphicon glyphicon-pencil"></span></a>&nbsp;&nbsp;
              				<a href="javascript:void(0)" onclick="deleteVehicle('${VehicleInfo.id}')">
              				<span data-toggle='tooltip' title="删除" class="glyphicon glyphicon-remove"></span></a>
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
           	<input type="hidden" id="totalPage" value="${totalPage}" />
          </div>
          <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
  </body>
</html>
