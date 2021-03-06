<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
    table td{word-break: keep-all;white-space:nowrap;}
    table th{word-break: keep-all;white-space:nowrap;}
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
              <s:set name="panel" value="'application'"></s:set>
      
        <%@include file="/pages/administration/panel.jsp" %>	
       <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="/personal/newHandleProperty" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">资产信息管理</h3> 
			   <div class="form-group" >
			   	<label class="col-sm-1 control-label">资产名称</label>
			  	<div class="col-sm-2" ><input type="text" autoComplete="off" class="form-control" name="assetName" value="${assetName}"></div>
			  	<label class="col-sm-1 control-label">资产编号</label>
			  	<div class="col-sm-2" ><input type="text" autoComplete="off" class="form-control" name="assetNum" value="${assetNum}"></div>
				<label class="col-sm-1 control-label">状态</label>
			  	<div class="col-sm-2" >
			  		<select class="form-control" name="assetStatus">
			  			<option value="">请选择</option>
			  			<option value="0" ${assetStatus=='0'? "selected":""}>闲置</option>
			  			<option value="1" ${assetStatus=='1'? "selected":""}>在用</option>
			  		</select>
			  	</div>
				<button type="submit" id="submitButton" class="btn btn-primary">查询</button>
			  </div>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>操作</th>
                  <th>状态</th>
                  <th>资产条码</th>
                  <th>资产名称</th>
                  <th>资产类别</th>
                  <th>规格型号</th>
                  <th>金额</th>
                  <th>使用公司</th>
                  <th>使用部门</th>
                  <th>使用人</th>
                  <th>所属公司</th>
                  <th>购入时间</th>
                  <th>存放地点</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty assetList}">
              	<s:iterator id="asset" value="#request.assetList" status="count">
              		<tr>
              			<td class="col-sm-1">
              			<a onclick="goPath('personal/toBeginHandlePropertyPage?assetName=${asset.assetName}&assetNum=${asset.serialNumber}&useDepartment=${asset.assetUsageVO.departmentName}&model=${asset.model}&recipientId=${asset.assetUsageVO.recipientID}')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="资产处置" data-toggle="tooltip">
							<use xlink:href="#icon-banli"></use>
						</svg>
              			</a>
              			</td>
              			<s:if test="#asset.status==0"><td class="col-sm-1"><font color=#00FF00>闲置</font></td></s:if><s:if test="#asset.status==1"><td class="col-sm-1"><font color=#FF0000>在用</font></td></s:if>
              			<td class="col-sm-1"><s:property value="#asset.serialNumber"/></td>
              			<td class="col-sm-1"><s:property value="#asset.assetName"/></td>
              			<td class="col-sm-1"><s:if test="#asset.type==1">行政办公设备</s:if><s:if test="#asset.type==2">电子产品</s:if><s:if test="#asset.type==3">通信设备</s:if><s:if test="#asset.type==4">交通工具</s:if></td>
              			<td class="col-sm-1"><s:property value="#asset.model"/></td>
              			<td class="col-sm-1"><s:property value="#asset.amount"/></td>
              			<td class="col-sm-1"><s:property value="#asset.assetUsageVO.companyName"/></td>
              			<td class="col-sm-1"><s:property value="#asset.assetUsageVO.departmentName"/></td>
              			<td class="col-sm-1"><s:property value="#asset.assetUsageVO.recipientName"/></td>
              			<td class="col-sm-1">
	             			<s:if test="#asset.companyID==1">智造链骑岸总部</s:if><s:if test="#asset.companyID==2">智造链如东迷丝茉分部</s:if>
	             			<s:if test="#asset.companyID==3">智造链南通分部</s:if><s:if test="#asset.companyID==4">智造链广州亦酷亦雅分部</s:if>
	             			<s:if test="#asset.companyID==5">智造链南京分部</s:if><s:if test="#asset.companyID==6">智造链佛山迷丝茉分部</s:if>
              			</td>
              			<td class="col-sm-1"><s:property value="#asset.purchaseTime"/></td>
              			<td class="col-sm-1"><s:property value="#asset.storageLocation"/></td>
              		</tr>
              	</s:iterator>
              	</c:if>
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
    <script src="/assets/icon/iconfont.js"></script>
    <script type="text/javascript">
    	$(function(){
    		$("[data-toggle='tooltip']").tooltip();
    		set_href();
    	});
	</script>
</body>
</html>