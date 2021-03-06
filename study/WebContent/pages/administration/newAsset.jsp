<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "assetUsageVO.departmentID");
		
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
	});
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid1").html(html);
	}
	
	function saveAsset() {
		if ($("#serialNumber").val().trim() == '') {
			showAlert("资产编号不能为空！");			
			return;
		}
		if ($("#assetName").val().trim() == '') {
			showAlert("资产名称不能为空！");			
			return;
		}
		if ($("#companyID").val() == '') {
			showAlert("所属公司不能为空！");			
			return;
		}
		if ($("#type").val().trim() == '') {
			showAlert("类型不能为空！");
			return;
		}
		if($("#status").val().trim() == ''){
			showAlert("状态不能为空！");
			return;
		}
		$('#editModal').modal('hide')
		var formData = new FormData($("#assetForm")[0]);
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'asset/saveAsset',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
		    dateType:'json',
		    success:function(data) {
		    	if (data.errorMessage!=null && data.errorMessage.length!=0) {
		    		window.location.href = "asset/error?panel=asset&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
		    	layer.alert("保存成功！", {offset:'100px'}, function(index){
		    		layer.close(index);
		    		Load.Base.LoadingPic.FullScreenHide();
		    		window.location.reload();
		    	});
		    },
		    complete:function(){
		    	Load.Base.LoadingPic.FullScreenHide();
		    }
		});
	}
	
	
	function search() {
		window.location.href = "asset/findAssetList?" + $("#assetForm2").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	
	function searchUsage(assetID){
		$.ajax({
		url:"asset/showAssetUsageList",
		type:"post",
        data:{assetID:assetID},
	    dataType:"json",
	    success:function(data){
	    	if(data.errorMessage!=null && data.errorMessage.length!=0){
	    		window.location.href = "asset/error?panel=asset&errorMessage="+encodeURI(encodeURI(data.errorMessage));
	    		return;
	    	}
	    	var html="";
	    	$.each(data.assetVO.assetUsageVOs,function(i,assetUsage){
	    		 switch (assetUsage.status)
				  {
					  case 0:
					    statusName="领用";
					    break;
					  case 1:
						statusName="已退库";
					    break;
					 
				  }
	    		html+="<tr><td>"+statusName+"</td><td>"+assetUsage.companyName+"</td><td>"+assetUsage.departmentName+"</td><td>"+assetUsage.recipientName+"</td><td>"+assetUsage.receiveLocation+"</td></tr>";
	    	});
	    	$("#assets1").html(html);
	    	 switch (data.assetVO.type)
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
	   	      $('#serialNumber1').text(data.assetVO.serialNumber);
			  $('#assetName1').text(data.assetVO.assetName);
			  $('#type1').text(typeName);
			  $('#model1').text(data.assetVO.model);
			  $('#purchaseTime1').text(data.assetVO.purchaseTime==null?'':data.assetVO.purchaseTime);
	    	$('#cheakModal').modal('show');
	    	
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
	function export_excel(){
		var params = $("#assetForm2").serialize();
		window.location.href= "asset/exportAssetVOToExcel?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		$("#exportBtn").attr("disabled","disabled");
	}
	function updateInfo(assetID){
		$.ajax({
			url:'asset/selectAssetVOByAssetID',
			dataType:'json',
			data:{assetID:assetID},
			type:'post',
			success:function(data){
				if(data.errorMessage!=null && data.errorMessage.length!=0){
		    		window.location.href = "asset/error?panel=asset&errorMessage="+encodeURI(encodeURI(data.errorMessage));
		    		return;
		    	}
				var assetID = data.assetVO.assetID;
				var companyID = data.assetVO.companyID;
				var type = data.assetVO.type;
				var assetName = data.assetVO.assetName;
				var model = data.assetVO.model;
				var serialNumber = data.assetVO.serialNumber;
				var deviceID = data.assetVO.deviceID;
				var amount = data.assetVO.amount;
				var purchaseTime = data.assetVO.purchaseTime;
				var status = data.assetVO.status;
				var storageLocation = data.assetVO.storageLocation;
				if(deviceID==null){
					deviceID='';
				};
				if(amount==null){
					amount='';
				};
				if(purchaseTime==null){
					purchaseTime='';
				};
				layer.open({
					offset:'100px',
					area:['1000px','325px'],
					title:'修改资产信息',
					content:'<form id="formId" class="form-horizontal" role="form">'+
							'<div class="form-group">'+
								'<label for="companyIDSec" class="col-sm-1 control-label">所属公司</label>'+
								'<div class="col-sm-3">'+
									'<input value="'+assetID+'" type="hidden" class="form-control" id="assetIDSec" name="assetEntity.assetID" required="required"/>'+
									'<select id="companyIDSec" class="form-control" name="assetEntity.companyID">'+
										'<option value="请选择">请选择</option>'+
										'<option value="1">智造链骑岸总部</option>'+		
										'<option value="2">智造链如东迷丝茉分部</option>'+
										'<option value="3">智造链南通分部</option>'+
										'<option value="4">智造链广州亦酷亦雅分部</option>'+
										'<option value="5">智造链南京分部</option>'+
										'<option value="6">智造链佛山迷丝茉分部</option>'+
									'</select>'+
							    '</div>'+
							    '<label for="typeSec" class="col-sm-1 control-label">资产类别</label>'+
							    '<div class="col-sm-3">'+
									'<select id="typeSec" class="form-control" name="assetEntity.type">'+
										'<option value="请选择">请选择</option>'+
										'<option value="1">行政办公设备</option>'+
										'<option value="2">电子产品</option>'+
										'<option value="3">通信设备</option>'+
										'<option value="4">交通工具</option>'+
									'</select>'+
						    	'</div>'+
						    	'<label for="assetNameSec" class="col-sm-1 control-label">资产名称&nbsp;<span style="color:red;">*</span></label>'+
							    '<div class="col-sm-3">'+
							    	'<input value="'+assetName+'" type="text" class="form-control" id="assetNameSec" name="assetEntity.assetName" required="required"/>'+
							    '</div>'+
						    '</div>'+
						    '<div class="form-group">'+
							    '<label for="modelSec" class="col-sm-1 control-label">规格型号</label>'+
						    	'<div class="col-sm-3">'+
							    	'<input value="'+model+'" type="text" class="form-control" id="modelSec" name="assetEntity.model"/>'+
							    '</div>'+
							    '<label for="serialNumberSec" class="col-sm-1 control-label">资产条码&nbsp;<span style="color:red;">*</span></label>'+
							    '<div class="col-sm-3">'+
							    	'<input value="'+serialNumber+'" type="text" class="form-control" id="serialNumberSec" name="assetEntity.serialNumber" required="required"/>'+
							    '</div>'+
							    '<label for="deviceIDSec" class="col-sm-1 control-label">设备序列号</label>'+
								'<div class="col-sm-3">'+
									'<input value="'+deviceID+'" type="text" class="form-control" id="deviceIDSec" name="assetEntity.deviceID"/>'+
								'</div>'+
						    '</div>'+
						    '<div class="form-group">'+
							    '<label for="amountSec" class="col-sm-1 control-label">金额</label>'+
							    '<div class="col-sm-3">'+
							    	'<input placeholder="'+"'元'为单位"+'" value="'+amount+'" onkeyup="num(this)" maxlength="10" type="text" class="form-control" id="amountSec" name="assetEntity.amount"/>'+
							    '</div>'+
							    '<label for="purchaseTimeSec" class="col-sm-1 control-label">购买时间</label>'+
							    '<div class="col-sm-3">'+
							   		"<input value='"+purchaseTime+"' type='text' class='form-control reportDate'  id='purchaseTimeSec'  onclick='WdatePicker({dateFmt:"+"\"yyyy-MM-dd\""+"})' name='assetEntity.purchaseTime'/>"+
							   	'</div>'+
							   	'<label for="statusSec" class="col-sm-1 control-label">状态&nbsp;<span style="color:red;">*</span></label>'+
							    '<div class="col-sm-3">'+
							    	'<select class="form-control" id="statusSec" required="required" name="assetEntity.status">'+
								      '<option value="">请选择</option>'+
									  '<option value="1">在用</option>'+
									  '<option value="0">闲置</option>'+
									'</select>'+
							    '</div>'+
							'</div>'+
						    '<div class="form-group">'+
							    '<label for="storageLocationSec" class="col-sm-1 control-label">存放地点</label>'+
							    '<div class="col-sm-3">'+
							    	'<input value="'+storageLocation+'" type="text" class="form-control" id="storageLocationSec" name="assetEntity.storageLocation"/>'+
							    '</div>'+
						    '</div>'+
						    '</form>',
					btn:['确定','取消'],
					success:function(layero, index){
						$("#companyIDSec").find("option[value='"+companyID+"']").attr("selected","selected");
						$("#typeSec").find("option[value='"+type+"']").attr("selected","selected");
						$("#statusSec").find("option[value='"+status+"']").attr("selected","selected");
					},
					yes:function(index){
						layer.close(index);
						window.location.href = "asset/updateAssetEntity?" + $("#formId").serialize();
						Load.Base.LoadingPic.FullScreenShow(null);
					}
					
				})
				
			}
		})
	}
	function num(obj){
		obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字
		obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个, 清除多余的
		obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
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
    .inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
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
      	<s:set name="panel" value="'asset'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
         <form id="assetForm2" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">资产入库</h3>
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">所属公司</label>
			    <div class="col-sm-2" >
				    <select class="form-control"  name="assetVO.companyID" >
						<option value="">请选择</option>
						<c:if test="${not empty companyVOs }">
							<s:iterator id="company" value="#request.companyVOs" status="st">
							<option value="<s:property value="#company.companyID" />" <s:if test="#request.assetVO.companyID == #company.companyID ">selected="selected"</s:if> ><s:property value="#company.companyName"/></option>
							</s:iterator>
						</c:if>
					</select>
			    </div>
				
				
			      
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
			      
			    <label for="company" class="col-sm-1 control-label">资产名称</label>
			    <div class="col-sm-2">
			      	<input type="text" class="form-control" name="assetVO.AssetName" value="<s:property value="#request.assetVO.AssetName" />"/>
				</div>
			</div>
			      
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">资产条码</label>
				<div class="col-sm-2">
		      		<input type="text" class="form-control" name="assetVO.SerialNumber" value="<s:property value="#request.assetVO.SerialNumber" />"/>
		        </div>
			       
			    <label for="deviceID" class="col-sm-1 control-label">设备序列号</label>
			    <div class="col-sm-2">
			    	<input type="text" id="deviceID" class="form-control" name="assetVO.deviceID" value="<s:property value="#request.assetVO.deviceID" />">
			    </div>
			      
			    <label for="company" class="col-sm-1 control-label">状态</label>
				<div class="col-sm-2">
			    	<select class="form-control"  name="assetVO.status" >
					    <option value="">请选择</option>
					    <option value="1" <s:if test="#request.assetVO.status == 1 ">selected="selected"</s:if> >在用</option>
					    <option value="0" <s:if test="#request.assetVO.status == 0 ">selected="selected"</s:if> >闲置</option>
					</select>
			    </div>
			      
			    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			    <button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
			</div>
          </form>
           <a href="javascript:void(0)" class="btn btn-primary" data-toggle="modal" data-target="#editModal"><span class="glyphicon glyphicon-plus"></span> 新增</a>
         <button id="exportBtn" type="button" onclick="export_excel()" class="btn btn-primary" style="margin-left:20px;"><span class="glyphicon glyphicon-export"></span> 导出</button>
         <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width: 14%">所属公司</th>
                  <th style="width: 8%">资产类别</th>
                  <th style="width: 16%">资产名称/规格型号</th>
                  <th style="width: 6%">资产条码</th>
                  <th style="width: 6%">设备序列号</th>
                  <th style="width: 4%">金额('元'为单位)</th>
                  <th style="width: 6%">购入时间</th>
                  <th style="width: 3%">状态</th>
                  <th style="width: 18%">使用公司/部门/人员</th>
                  <th style="width: 18%">存放地点</th>
                  <th style="width: 3%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty assetlist}">
              	<s:iterator id="asset" value="#request.assetlist" status="count">
              		<tr>
              			<td class="col-sm-1">
              				<s:if test="#asset.companyID==1">智造链骑岸总部</s:if>
              				<s:if test="#asset.companyID==2">智造链如东迷丝<br>茉分部</s:if>
              				<s:if test="#asset.companyID==3">智造链南通分部</s:if>
              				<s:if test="#asset.companyID==4">智造链广州亦酷<br>亦雅分部</s:if>
              				<s:if test="#asset.companyID==5">智造链南京分部</s:if>
              				<s:if test="#asset.companyID==6">智造链佛山迷丝<br>茉分部</s:if>
              			</td>
              			<td class="col-sm-1">
              				<s:if test="#asset.type==1">行政办公<br>设备</s:if>
              				<s:if test="#asset.type==2">电子产品</s:if>
              				<s:if test="#asset.type==3">通信设备</s:if>
              				<s:if test="#asset.type==4">交通工具</s:if>
              			</td>
              			<td class="col-sm-1">
              				<s:property value="#asset.assetName"/>
              				<s:if test="#asset.deviceID==null || #asset.deviceID==''"><br>――――――</s:if>
              				<s:if test="#asset.deviceID!=null && #asset.deviceID!=''">
              					<br>
              					<s:property value="#asset.model"/>
              				</s:if>
              				<%-- <s:if test="#asset.model!=null || #asset.model.length()!=0">
              					<br>
              					<s:property value="#asset.model"/>
              				</s:if> --%>
              			</td>
              			<td class="col-sm-1"><s:property value="#asset.serialNumber"/></td>
              			<td class="col-sm-1">
              				<s:if test="#asset.deviceID==null || #asset.deviceID==''">――――――</s:if>
              				<s:if test="#asset.deviceID!=null && #asset.deviceID!=''">
              					<s:property value="#asset.deviceID"/>
              				</s:if>
              			</td>
              			<td class="col-sm-1">
              				<s:if test="#asset.amount==null || #asset.amount==''">――――――</s:if>
              				<s:if test="#asset.amount!=null && #asset.amount!=''">
              					<s:property value="#asset.amount"/>
              				</s:if>
              			</td>
              			<td class="col-sm-1">
              				<s:if test="#asset.purchaseTime==null || #asset.purchaseTime==''">――――――</s:if>
              				<s:if test="#asset.purchaseTime!=null && #asset.purchaseTime!=''">
              					<s:property value="#asset.purchaseTime"/>
              				</s:if>
              			</td>
              			
              			<s:if test="#asset.status==0"><td class="col-sm-1"><font color=#428bca>闲置</font></td></s:if>
              			<s:if test="#asset.status==1"><td class="col-sm-1"><font color=#FF0000>在用</font></td></s:if>
              			
              			<td class="col-sm-1">
              				<s:if test="#asset.assetUsageVO.companyName!=null || #asset.assetUsageVO.companyName.length()!=0">
              					<s:property value="#asset.assetUsageVO.companyName"/>
              					<br>
              				</s:if>
              				<s:if test="#asset.assetUsageVO.companyName==null || #asset.assetUsageVO.companyName.length()==0">
              					――――――
              					<br>
              				</s:if>
              				<s:if test='#asset.assetUsageVO.departmentName!=null || #asset.assetUsageVO.departmentName.length()!=0 '>
	              				<s:property value="#asset.assetUsageVO.departmentName"/>
	              				&nbsp;
              				</s:if>
              				<s:property value="#asset.assetUsageVO.recipientName"/>
              			</td>
              			<td class="col-sm-1">
	              			<s:if test="#asset.storageLocation==null || #asset.storageLocation==''">――――――</s:if>
              				<s:if test="#asset.storageLocation!=null && #asset.storageLocation!=''">
              					<s:property value="#asset.storageLocation"/>
              				</s:if>
              			</td>
              			
              			<td class="col-sm-1">
              				<a href="javascript:void(0)" onclick="searchUsage(<s:property value='#asset.assetID'/>)">
	              				<svg class="icon" aria-hidden="true" title="查看使用记录" data-toggle="tooltip">
									<use xlink:href="#icon-jilu"></use>
								</svg>
              				</a>
              				&nbsp;
							<a href="javascript:void(0)" onclick="updateInfo(<s:property value='#asset.assetID'/>)">
								<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
									<use xlink:href="#icon-modify"></use>
								</svg>
							</a>
              			</td>
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>       
        </div>
      </div>
    </div>
    
    <div class="modal fade bs-example-modal-lg" id="editModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">新增资产</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	        <form id="assetForm" class="form-horizontal">
	          <input type="hidden" id="groupDetailID" />
	          
				<div class="form-group">
					<label for="companyID" class="col-sm-2 control-label">所属公司&nbsp;<span style="color:red;">*</span></label>
				    <div class="col-sm-2">
				        <select id="companyID" class="form-control"  name="assetVO.companyID" >
					      	<option value="">请选择</option>
					      	<c:if test="${not empty companyVOs }">
					      	<s:iterator id="company" value="#request.companyVOs" status="st">
					      		<option value="<s:property value="#company.companyID" />" ><s:property value="#company.companyName"/></option>
					      	</s:iterator>
					      	</c:if>
						</select>
				    </div>
				    
				    <label for="type" class="col-sm-2 control-label">资产类型&nbsp;<span style="color:red;">*</span></label>
				   	<div class="col-sm-2">
						<select class="form-control" id="type"  required="required" name="assetVO.type">
					    	<option value="">请选择</option>
						    <option value="1">行政办公设备</option>
						    <option value="2">电子产品</option>
						    <option value="3">通信设备</option>
						    <option value="4">交通工具</option>
						</select>
				    </div>
				    
		           	<label for="assetName" class="col-sm-2 control-label">资产名称&nbsp;<span style="color:red;">*</span></label>
				    <div class="col-sm-2">
				    	<input type="text" class="form-control" id="assetName" name="assetVO.assetName" required="required"/>
				    </div>
	         	
				</div>
				
				<div class="form-group">
	            	<label for="model" class="col-sm-2 control-label">规格型号</label>
			    	<div class="col-sm-2">
				    	<input type="text" class="form-control" id="model" name="assetVO.model"/>
				    </div>
	          		
	          		<label for="serialNumber" class="col-sm-2 control-label">资产条码&nbsp;<span style="color:red;">*</span></label>
				    <div class="col-sm-2">
				    	<input type="text" class="form-control" id="serialNumber" name="assetVO.serialNumber" required="required"/>
				    </div>
	          		
					<label for="deviceID" class="col-sm-2 control-label">设备序列号</label>
					<div class="col-sm-2">
						<input type="text" class="form-control" id="deviceID" name="assetVO.deviceID"/>
					</div>
					
			    </div>
			    
			    <div class="form-group">
				    <label for="amount" class="col-sm-2 control-label">金额('元'为单位)</label>
				    <div class="col-sm-2">
				    	<input type="text" onkeyup="num(this)" maxlength="10" class="form-control" id="amount" name="assetVO.amount"/>
				    </div>
			    	
			    	<label for="purchaseTime" class="col-sm-2 control-label">购买时间</label>
				    <div class="col-sm-2">
				   		<input type="text" class="form-control reportDate"  id="purchaseTime"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" name="assetVO.purchaseTime"/>
	    	        </div>
			    	
					<label for="status" class="col-sm-2 control-label">状态&nbsp;<span style="color:red;">*</span></label>
				    <div class="col-sm-2">
				    	<select class="form-control" id="status"  required="required" name="assetVO.status">
					      <option value="">请选择</option>
						  <option value="1">在用</option>
						  <option value="0">闲置</option>
						</select>
				    </div>
				</div>
	          
				<div class="form-group">
	          		<label for="storageLocation" class="col-sm-2 control-label">存放地点</label>
				    <div class="col-sm-2">
				    	<input type="text" class="form-control" id="storageLocation" name="assetVO.storageLocation"/>
				    </div>
				</div>
				
	            <HR align=center width=300 color=#987cb9 size=1>
	            
	         	
				
	          	<div class="form-group">
	          		<label for="company" class="col-sm-2 control-label">使用公司&nbsp;<span style="color:red;">*</span></label>
			   
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
		           	<label for="recipient" class="col-sm-2 control-label">使用人</label>
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
				    
		           	<label for="reportDate" class="col-sm-2 control-label">领用时间</label>
				    <div class="col-sm-2">
				    	<input type="text" class="form-control reportDate"  id="reportDate" name="assetUsageVO.receiveTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
				    </div>
				    <label for="receiveLocation" class="col-sm-2 control-label">领用存放地点</label>
				    <div class="col-sm-2">
				    	<input type="text" class="form-control" id="receiveLocation" name="assetUsageVO.receiveLocation"/>
				    </div>
				</div>
			    
	            <div class="form-group">
		            <label for="receiveNote" class="col-sm-2 control-label">备注</label>
				    <div class="col-sm-4">
				    	<input type="text" class="form-control" id="receiveNote" name="assetUsageVO.receiveNote"/>
				    </div>
			    </div>
	        </form>
	      </div>
	     
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
	        <button type="button" class="btn btn-primary"  onclick="saveAsset()" id="submitButton">提交</button>
	      </div>
	    </div>
	  </div>
	</div>
    <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">查看使用记录</h4>
	      </div>
	      <div class="modal-body">
	        <form id="assetForm1" class="form-horizontal">
	          <input type="hidden" id="groupDetailID" />
	          <div class="form-group">
	           <label for="reason" class="col-sm-2 control-label">资产条码:</label>
			    <div  >
			    	 <span  id="serialNumber1" class="col-sm-2 control-label" style="text-align:left"></span> 
			    </div>
	           <label for="reason" class="col-sm-2 control-label">资产名称:</label>
			    
			    	<span id="assetName1" class="col-sm-2 control-label" style="text-align:left"></span>
			  
	          
	           <label for="reason" class="col-sm-1 control-label">资产类型:</label>
			   
			    	<span id="type1" class="col-sm-2 control-label" style="text-align:left"></span>
			  
			    </div>
	          <div class="form-group">
	           <label for="reason" class="col-sm-2 control-label">规格型号:</label>
			    <div class="col-sm-2">
			    	<span id="model1" class="col-sm-2 control-label" style="text-align:left"></span>
			    </div>
	         
	           <label for="reason" class="col-sm-2 control-label">购买时间:</label>
			  
			   <span id="purchaseTime1" class="col-sm-2 control-label" style="text-align:left"></span>
    	       
	          </div>
	        </form>
	         <form id="assetForm2" class="form-horizontal">
	           <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>状态</th>
                  <th>使用公司</th>
                  <th>使用部门</th>
                  <th>使用人</th>
                  <th>存放地点</th>
                </tr>
              </thead>
              <tbody id="assets1">
              		
              </tbody>
            </table>
          </div>
	        </form>
	        
	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-default" data-dismiss="modal">返回</button>
	      </div>
	    </div>
	  </div>
	</div>

















</body>
</html>