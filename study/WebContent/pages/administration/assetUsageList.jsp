<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
	});
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid1").html(html);
	}
	function search() {
		window.location.href = "asset/findAssetUsageList?" + $("#assetUsageForm").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function check(){
		if($("input[name='assetUsageVO.usageIDs']:checked").length>0){
			$("#submitButton").addClass("disabled");
			Load.Base.LoadingPic.FullScreenShow(null);
			return true;
			
		}else{
			layer.alert("您还没有选择需要退库的资产",{offset:'100px'});
			return  false;
		}
		
	}
	function transferAsset(assetUseId, assetName, assetId){
		$("input[name='usageID']").val(assetUseId);
		$("#assetName").text(assetName);
		$("input[name='assetUsage.assetID']").val(assetId);
	}
	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#recipient'),function ($item){
			$("input[name='assetUsage.recipientID']").val($item.data("userId"));
		});
	});
	function checkEmpty(){
		if($("#recipient").val()==''){
			$("input[name='assetUsage.recipientID']").val('');
		}
	}
	$(document).click(function (event) {
		if ($("input[name='assetUsage.recipientID']").val()=='')
		{
			$("#recipient").val("");
		}
	}); 
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "assetUsage.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "assetUsage.departmentID");
		}
		$.ajax({
			url:'HR/staff/findDepartmentsByCompanyIDParentID',
			type:'post',
			data:{companyID: $("#company").val(),
				  parentID: parentID},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					if (level == 1) {
						window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} else {
						return;
					}
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div class=\"col-sm-4\"></div><div style='margin-top:5px' id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
</script>
<style type="text/css">
	 
	
    .col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	#tableTr td,#tableHeadLine th {
		text-align:center;
	}
	table{width:100%;border:0px solid #999;}
    table td{word-break: keep-all;white-space:nowrap;}
    table th{word-break: keep-all;white-space:nowrap;}
    .hand{cursor:pointer}
   	.textcomplete-dropdown{
		z-index:1050 !important;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'asset'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
         <form id="assetUsageForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">领用&退库</h3>
			<div class="form-group">
			     
			      <label for="company" class="col-sm-1 control-label">使用人</label>
			      <div class="col-sm-2">
			      	<input type="text" class="form-control" name="assetUsageVO.recipientName" value="<s:property value="#request.assetUsageVO.recipientName" />"/>
			      </div>
			      <label for="company" class="col-sm-1 control-label">资产条码</label>
			      <div class="col-sm-2">
			      	<input type="text" class="form-control" name="assetUsageVO.assetVO.SerialNumber" value="<s:property value="#request.assetUsageVO.assetVO.SerialNumber" />"/>
			      </div>
			       <label for="company" class="col-sm-1 control-label">资产名称</label>
			      <div class="col-sm-2">
			      	<input type="text" class="form-control" name="assetUsageVO.assetVO.AssetName" value="<s:property value="#request.assetUsageVO.assetVO.AssetName" />"/>
			      </div>
			      </div>
			       <div class="form-group">
			       <label for="company" class="col-sm-1 control-label">资产类别</label>
			      <div class="col-sm-2">
			      	<select class="form-control"  name="assetUsageVO.assetVO.type" >
				      <option value="">请选择</option>
				      <option value="1" <s:if test="#request.assetUsageVO.assetVO.type == 1 ">selected="selected"</s:if> >行政办公设备</option>
				      <option value="2" <s:if test="#request.assetUsageVO.assetVO.type == 2 ">selected="selected"</s:if> >电子产品</option>
				      <option value="3" <s:if test="#request.assetUsageVO.assetVO.type == 3 ">selected="selected"</s:if> >通信设备</option>
				      <option value="4" <s:if test="#request.assetUsageVO.assetVO.type == 4 ">selected="selected"</s:if> >交通工具</option>
					</select>
			      </div>
			       <label for="company" class="col-sm-1 control-label">状态</label>
			      <div class="col-sm-2">
			      	<select class="form-control"  name="assetUsageVO.status" >
				      <option value="">请选择</option>
				      <option value="0" <s:if test="#request.assetUsageVO.status == 0 ">selected="selected"</s:if> >领用</option>
				      <option value="1" <s:if test="#request.assetUsageVO.status == 1 ">selected="selected"</s:if> >已退库</option>
					</select>
			      </div>
			      
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			      <button type="button" id="submitButton1" class="btn btn-primary" onclick="search()">查询</button>
			   
			    </div>
          </form>
          <form action="asset/returnAsset" method="post" class="form-horizontal" onsubmit="return check()">
           <a onclick="goPath('asset/newAssetUsage')" href="javascript:void(0)" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 领用</a>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
           <button type="submit" id="submitButton" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 退库</button>         
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th></th>
                  <th>操作</th>
                  <th>状态</th>
                  <th>资产名称</th>
                  <th>资产条码</th>
                  <th>使用公司/部门/人员</th>
                  <th>领用人</th>
                  <th>领用地址</th>
                  <th>领用时间</th>
                  <th>备注</th>
                  <th>退库地址</th>
                  <th>退库时间</th>
                  <th>退库人</th>
                  <th>备注</th>
                  
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty assetUsagelist}">
              	<s:set name="groupDetail_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="assetUsage" value="#request.assetUsagelist" status="count">
              		<tr>
              			
              			<td class="col-sm-1"><s:if test="#assetUsage.status==0"><input type='checkbox' name="assetUsageVO.usageIDs" value="<s:property value='#assetUsage.usageID'/>" /></s:if></td>
              			<td><span data-toggle="modal" data-target="#transferAsset" onclick="transferAsset('<s:property value='#assetUsage.usageID'/>','<s:property value='#assetUsage.assetName'/>','<s:property value='#assetUsage.assetID'/>')" title="调拨" data-toggle='tooltip' class=" hand glyphicon glyphicon-transfer"></span></td>
              			<s:if test="#assetUsage.status==0"><td class="col-sm-1" ><font color=#FF0000>领用</font></td></s:if><s:if test="#assetUsage.status==1"><td class="col-sm-1" ><font >已退库</font></td></s:if>
              			<td class="col-sm-1"><s:property value="#assetUsage.assetName"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.assetVO.serialNumber"/></td>
              			<td class="col-sm-1">
              				<s:if test="#assetUsage.companyName !=null || #assetUsage.companyName.length()!=0">
              					<s:property value="#assetUsage.companyName"/><br>
              				</s:if>
              				<s:if test="#assetUsage.departmentName !=null || #assetUsage.departmentName.length()!=0">
              					<s:property value="#assetUsage.departmentName"/>&nbsp;
              				</s:if>
	              			<s:property value="#assetUsage.recipientName"/>
              			</td>
              			<td class="col-sm-1"><s:property value="#assetUsage.receiveOperatorName"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.receiveLocation"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.receiveTime"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.receiveNote"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.returnLocation"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.returnTime"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.returnOperatorName"/></td>
              			<td class="col-sm-1"><s:property value="#assetUsage.returnNote"/></td>              		</tr>
              		<s:set name="groupDetail_id" value="#groupDetail_id+1"></s:set>
              	</s:iterator>
              	</c:if>
              </tbody>
            </table>
          </div>
          </form>
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
    <div id="transferAsset" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form class="form-horizontal" action="asset/updateAssetUsage">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">物品调拨</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<input type="hidden" name="usageID"/>
					<input type="hidden" name="assetUsage.assetID">
					<label class="col-sm-4 control-label">资产名称</label>
					<div class="col-sm-7 control-label" id="assetName" style="text-align:left"></div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">使用人<span style="color:red"> *</span></label>
					<div class="col-sm-7">
						<input id="recipient" type="text" class="form-control" autocomplete="off" required onkeyup="checkEmpty()"/>
						<input type="hidden" name="assetUsage.recipientID">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">领用时间<span style="color:red"> *</span></label>
					<div class="col-sm-7">
						<input type="text" autocomplete="off" required class="form-control" name="assetUsage.receiveTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">使用公司<span style="color:red"> *</span></label>
					<div class="col-sm-7" id="company_div">
			    	<select class="form-control" required id="company" name="assetUsage.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.assetUsageVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    	</div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">领用存放地点</label>
					<div class="col-sm-7">
						<input class="form-control" autocomplete="off" name="assetUsage.receiveLocation" maxLength="50"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-4 control-label">备注<span style="font-size:12px">（限50字）</span></label>
					<div class="col-sm-7">
						<input class="form-control" autocomplete="off" name="assetUsage.receiveNote" maxLength="50"/>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
</body>
</html>