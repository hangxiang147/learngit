<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	$(function() {
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "assetUsageVO.departmentID");
		$('body').on('click','.input_text',function (event) { 
			if($(".text_down ul").html() != ""){
			$(".text_down").show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down ul li',function () {
			var shtml=$(this).html();
			$(".text_down").hide();
			$("#receiveOperation").val(shtml.split("（")[0]);
			$("#receiveOperationFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#receiveOperationID").val(agent.split("@")[0]);
			$("#receiveOperationName").val(agent.split("@")[1]);
			});
		}); 
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#receiveOperation").val()!=$("#receiveOperationFlag").val()) {$("#recipentOperation").val("");}});  
		$('.inputout').click(function (event) {$(".text_down").show();});
		$('body').on('click','.input_text1',function (event) { 
			if($(".text_down1 ul").html() != ""){
			$(".text_down1").show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down1 ul li',function () {
			var shtml=$(this).html();
			$(".text_down1").hide();
			$("#recipient").val(shtml.split("（")[0]);
			$("#recipientFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#recipientID").val(agent.split("@")[0]);
			$("#recipientName").val(agent.split("@")[1]);
			});
		}); 
		$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#recipient").val()!=$("#recipientFlag").val()) {$("#recipient").val("");}});  
		$('.inputout1').click(function (event) {$(".text_down1").show();});
		
		  $("#deleteAs").click(function() {
		        $("input[name='test']:checked").each(function() { // 遍历选中的checkbox
		            n = $(this).parents("tr").index();
		            n=n+1                                              // 获取checkbox所在行的顺序
		            $("table#test_table").find("tr:eq("+n+")").remove();
		        });
		    });
	});
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid1").html(html);
	}
	function findStaffByName() {
		var name = $("#receiveOperation").val();
		if (name.length == 0) {
			return;
		}
		$(".text_down ul").empty();
		$.ajax({
			url:'personal/findStaffByName',
			type:'post',
			data:{name:name},
			dataType:'json',
			success:function (data){
				$.each(data.staffVOs, function(i, staff) {
					var groupDetail = staff.groupDetailVOs[0];
					$(".text_down ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
				});
				$(".text_down").show();
			}
		});
		
	}
	function findStaffByName1() {
		var name = $("#recipient").val();
		if (name.length == 0) {
			return;
		}
		$(".text_down1 ul").empty();
		$.ajax({
			url:'personal/findStaffByName',
			type:'post',
			data:{name:name},
			dataType:'json',
			success:function (data){
				$.each(data.staffVOs, function(i, staff) {
					var groupDetail = staff.groupDetailVOs[0];
					$(".text_down1 ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
				});
				$(".text_down1").show();
			}
		});
		
	}
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "assetUsageVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "assetUsageVO.departmentID");
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
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}
	function showAssets(){
		$.ajax({
			url:"asset/findAssets",
			type:"post",
			contentType:false,
			processData:false,
		    dataType:"json",
		    success:function(data){
		    	
		    	if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "asset/error?panel=asset&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	var html="";
		    	$.each(data.assetVOs, function(i, asset) {
		    		 switch (asset.status)
					  {
						  case 0:
						    status="闲置";
						    break;
						  case 1:
							status="在用";
						    break;
						 
					  }
		    		 switch (asset.type)
					  {
						  case 1:
						    typeName="行政办公设备";
						    break;
						  case 2:
							typeName="电子产品";
						    break;
						  case 3:
						    typeName="通信设备";
						    break;
						  case 4:
							typeName="交通工具";
						    break;

					  }
		    		 var companyName;
		    		 switch (asset.companyID)
					  {
						  case 1:
						  companyName="智造链骑岸总部";
						    break;
						  case 2:
						   companyName="智造链如东迷丝莱分部";
						    break;
						  case 3:
						  companyName="智造链南通分部";
						    break;
						  case 4:
						  companyName="智造链广州亦酷亦雅分部";
						    break;
						  case 5:
						  companyName="智造链南京分部";
						    break;
						  case 6:
						  companyName="智造链佛山迷丝茉分部"
						break;
	
					  }
		    		html +="<tr><td><input type='checkbox' name='assetVO.assetIDs' value='"+asset.assetID+"' /></td><td>"+status+"</td><td>"+asset.serialNumber+"</td><td>"+asset.assetName+"</td><td>"+typeName+"</td><td>"+companyName+"</td><td>"+asset.storageLocation+"</td></tr>";
					
				});
		    	$('#assets').html(html);
		    	$('#editModal').modal('show');
		    	
		    }

		});
		
	}
	function findAssets(){
		var formData = new FormData($("#assetForm2")[0]);
		$.ajax({
			url:"asset/findAssets",
			type:"post",
			data:formData,
			contentType:false,
			processData:false,
		    dataType:"json",
		    success:function(data){
		    	
		    	if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "asset/error?panel=asset&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	var html="";
		    	$.each(data.assetVOs, function(i, asset) {
		    		 switch (asset.status)
					  {
						  case 0:
						    status="闲置";
						    break;
						  case 1:
							status="在用";
						    break;
						 
					  }
		    		 switch (asset.type)
					  {
						  case 1:
						    typeName="行政办公设备";
						    break;
						  case 2:
							typeName="电子产品";
						    break;
						  case 3:
						    typeName="通信设备";
						    break;
						  case 4:
							typeName="交通工具";
						    break;

					  }
		    		 switch (asset.companyID)
					  {
						  case 1:
						  companyName="智造链骑岸总部";
						    break;
						  case 2:
						   companyName="智造链如东迷丝莱分部";
						    break;
						  case 3:
						  companyName="智造链南通分部";
						    break;
						  case 4:
						  companyName="智造链广州亦酷亦雅分部";
						    break;
						  case 5:
						  companyName="智造链南京分部";
						    break;
						  case 6:
							  companyName="智造链佛山迷丝茉分部"
							break;

					  }
		    		html +="<tr><td><input type='checkbox' name='assetVO.assetIDs' value='"+asset.assetID+"' /></td><td>"+status+"</td><td>"+asset.serialNumber+"</td><td>"+asset.assetName+"</td><td>"+typeName+"</td><td>"+companyName+"</td><td>"+asset.storageLocation+"</td></tr>";
					
				});
		    	$('#assets').html(html);
		    }

		});
		
	}
	function addAssets(){
		var formData = new FormData($("#assetForm")[0]);
		$.ajax({
			url:"asset/addAsseets",
			type:"post",
			data:formData,
			contentType:false,
			processData:false,
		    dataType:"json",
		    success:function(data){
		    	
		    	if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "asset/error?panel=asset&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	var html="";
		    	$.each(data.assetVOs, function(i, asset) {
		    		 switch (asset.type)
					  {
						  case 1:
						    typeName="行政办公设备";
						    break;
						  case 2:
							typeName="电子产品";
						    break;
						  case 3:
						    typeName="通信设备";
						    break;
						  case 4:
							typeName="交通工具";
						    break;

					  }
		    		 switch (asset.companyID)
					  {
						  case 1:
						  companyName="智造链骑岸总部";
						    break;
						  case 2:
						   companyName="智造链如东迷丝莱分部";
						    break;
						  case 3:
						  companyName="智造链南通分部";
						    break;
						  case 4:
						  companyName="智造链广州亦酷亦雅分部";
						    break;
						  case 5:
						  companyName="智造链南京分部";
						    break;
						  case 6:
							  companyName="智造链佛山迷丝茉分部"
							break;
						    
					  }

		    		html +="<tr><td></td><td><input type='hidden' name='assetUsageVO.assetIDs' value='"+asset.assetID+"' />"+asset.serialNumber+"</td><td>"+asset.assetName+"</td><td>"+typeName+"</td><td>"+companyName+"</td><td><a href='javascript:void(0);' onclick='deleteAsset(this)'>删除</a></td></tr>";
					
				});
		    	$('#as').append(html);
		    	$('#editModal').modal('hide');
		    }

		});
		
		
	}
	function deleteAsset(obj){
		n = $(obj).parents("tr").index();
		n=n+1
	    $("table#test_table").find("tr:eq("+n+")").remove();
		
	}
	
	
	
</script>
<style type="text/css">
    #div1{
    right:200px;
    }
	 
	
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
    .inputout{position:relative;}
	.text_down{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down ul{padding:2px 10px;}
	.text_down ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down ul li span{color:#cc0000;}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
</style>
</head>
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'asset'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="asset/saveAssetUsage" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">领用</h3> 
        	   <div class="form-group">
	           <label for="serialNumber" class="col-sm-1 control-label">使用人&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2 inputout1">
			    	<span class="input_text1">
			    	<input type="text" id="recipient" class="form-control" required="required" oninput="findStaffByName1()"  />
			    	<input type="hidden" id="recipientFlag" value=""/>
			    	<input type="hidden" id="recipientID" name="assetUsageVO.recipientID" />
			    	<input type="hidden" id="recipientName" name="vacationVO.agentName" />
			    	</span>
			    	<div class="text_down1">
						<ul></ul>
					</div>
			    </div>
	           <label for="receiveTime" class="col-sm-2 control-label">领用时间&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control reportDate"  id="receiveTime" name="assetUsageVO.receiveTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			    </div>
			    <label for="reason" class="col-sm-2 control-label">领用存放地点</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="model" name="assetUsageVO.receiveLocation"/>
			    </div>
	         </div>
	          <div class="form-group">
	           <label for="reason" class="col-sm-1 control-label">使用公司&nbsp;<span style="color:red;">*</span></label>
			   
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="assetUsageVO.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.assetUsageVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			    <c:if test="${not empty departmentVOs}">
			    <c:if test="${not empty selectedDepartmentIDs}">
			    <s:set name="departmentClass" value="'col-sm-2'"></s:set>
			    <s:set name="parent" value="0"></s:set>
			    <s:iterator id="selectedDepartmentID" value="#request.selectedDepartmentIDs" status="st">
			    	<s:set name="level" value="#st.index+1"></s:set>
			    	<s:set name="selectDepartmentID" value="#selectedDepartmentID"></s:set>
			    	<s:set name="departmentClass" value="#departmentClass+' department'+#level"></s:set>
			    	<s:set name="hasNextLevel" value="'false'"></s:set>
			    	<div class="<s:property value='#departmentClass'/>" id="department<s:property value='#level'/>_div" >
			    		<select class="form-control" id="department<s:property value='#level'/>" onchange="showDepartment(this, <s:property value='#level'/>)">
			    			<option value="">--<s:property value="#level"/>级部门--</option>
			    			<s:iterator id="department" value="#request.departmentVOs" status="department_st">
			    			<s:if test="#department.parentID == #parent">
			    				<option value="<s:property value='#department.departmentID'/>" <s:if test="#department.departmentID == #selectedDepartmentID">selected="selected"</s:if>><s:property value="#department.departmentName"/></option>
			    			</s:if>
			    			<s:if test="#department.parentID == #selectedDepartmentID"><s:set name="hasNextLevel" value="'true'"></s:set></s:if>
			    			</s:iterator>
			    		</select>
			    	</div>
			    	<s:set name="parent" value="#selectedDepartmentID"></s:set>
			    </s:iterator>
			    <input type="hidden" id="departmentLevel" value="<s:property value='#level'/>"/>
			    <s:if test="#hasNextLevel == 'true'">
				    <s:set name="index" value="#level+1"></s:set>
				    <div class="<s:property value="#departmentClass+' department'+#index"/>" id="department<s:property value='#index'/>_div" >
			    		<select class="form-control" id="department<s:property value='#index'/>" onchange="showDepartment(this, <s:property value='#index'/>)">
			    			<option value="">--<s:property value="#index"/>级部门--</option>
			    			<s:iterator id="department" value="#request.departmentVOs" status="department_st">
			    			<s:if test="#department.parentID == #selectDepartmentID">
			    				<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
			    			</s:if>
			    			</s:iterator>
			    		</select>
			    	</div>
			    </s:if>
			    </c:if>
			    <c:if test="${empty selectedDepartmentIDs}">
			    	<div class="col-sm-2 department1" id="department1_div" >
			    		<select class="form-control" id="department1" onchange="showDepartment(this, 1)">
			    			<option value="">--1级部门--</option>
			    			<s:iterator id="department" value="#request.departmentVOs" status="department_st">
			    			<s:if test="#department.level == 1">
			    				<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
			    			</s:if>
			    			</s:iterator>
			    		</select>
			    	</div>
			    </c:if>
			    </c:if>
			    </div>
			    <div class="form-group">
	           <label for="reason" class="col-sm-1 control-label">领用人:</label>
			   <div class="col-sm-3 inputout">
			    	<span class="col-sm-2 control-label">${staffVO.lastName }</span>
			    	<input type="hidden" id="receiveOperationID" name="assetUsageVO.receiveOperatorID" value="${staffVO.userID}"/>
			    </div>
			    <label for="reason" class="col-sm-1 control-label">备注</label>
				    <div class="col-sm-3">
				    	<input type="text" class="form-control" id="model" name="assetUsageVO.receiveNote"/>
				    </div>
	          </div>
	         
	          <div class="form-group">
	          <div class="col-sm-5" style="text-align:right">
	          <a class="btn btn-primary" onclick="showAssets()">选择资产</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	           </div>
	           </div>
	           <div class="form-group">
	           <div class="col-sm-9" style="text-align:right">    
	           <table class="table table-striped" id="test_table">
              <thead>
                <tr>
                  <th class="col-sm-1" ></th>
                  <th class="col-sm-1" >资产条码</th>
                  <th class="col-sm-1" >资产名称</th>
                  <th class="col-sm-1" >资产类别</th>
                  <th class="col-sm-1" >所属公司</th>
                  <th class="col-sm-1" ></th>
                </tr>
              </thead>
              <tbody id="as">
              		
              </tbody>
            </table>
	          </div>
	          </div>
	        
        	  
			  <div class="form-group" >
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
      <div class="modal fade bs-example-modal-lg" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">选择资产</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	        <form id="assetForm2" class="form-horizontal">       	
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">所属公司</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="assetVO.companyID" >
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.assetVO.companyID == #company.companyID ">selected="selected"</s:if> ><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			     </div>
			      <label for="company" class="col-sm-1 control-label">资产条码</label>
			      <div class="col-sm-2">
			      	<input type="text" class="form-control" name="assetVO.SerialNumber" value="<s:property value="#request.assetVO.SerialNumber" />"/>
			      </div>
			       <label for="company" class="col-sm-1 control-label">资产名称</label>
			      <div class="col-sm-2">
			      	<input type="text" class="form-control" name="assetVO.AssetName" value="<s:property value="#request.assetVO.AssetName" />"/>
			      </div>
			      </div>
			       <div class="form-group">
			       <label for="company" class="col-sm-1 control-label">资产类别</label>
			      <div class="col-sm-2">
			      	<select class="form-control"  name="assetVO.type" >
				      <option value="">请选择</option>
				      <option value="1" <s:if test="#request.assetVO.type == 1 ">selected="selected"</s:if> >行政办公设备</option>
				      <option value="2" <s:if test="#request.assetVO.type == 2 ">selected="selected"</s:if> >电子产品</option>
				      <option value="3" <s:if test="#request.assetVO.type == 3 ">selected="selected"</s:if> >通信设备</option>
				      <option value="4" <s:if test="#request.assetVO.type == 4 ">selected="selected"</s:if> >交通工具</option>
					</select>
			      </div>
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			      &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			      <button type="button" id="submitButton" class="btn btn-primary" onclick="findAssets()">查询</button>
			    </div>
          </form>
	        <form id="assetForm" class="form-horizontal">
	           <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>复选框</th>
                  <th>状态</th>
                  <th>资产条码</th>
                  <th>资产名称</th>
                  <th>资产类别</th>
                  <th>所属公司</th>
                  <th>存放地点</th>
                </tr>
              </thead>
              <tbody id="assets">
              		
              </tbody>
            </table>
          </div>
	        </form>
	      </div>
	      <div class="modal-footer" >
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button type="button" class="btn btn-primary"  onclick="addAssets()" id="submitButton">添加</button>
	      </div>
	    </div>
	  </div>
	</div>

</body>
</html>