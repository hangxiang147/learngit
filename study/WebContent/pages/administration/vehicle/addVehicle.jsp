<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.identity.User" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/formatForJS.tld" prefix="jf" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<link rel="stylesheet" href="/assets/js/textarea/bootstrap-markdown.min.css">
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style type="text/css">
	#vehicleInfo{
		border-collapse:collapse;
		width:100%;
		margin-top:5px;
	}
	#vehicleInfo tr td{word-wrap:break-word;font-size:14px;padding:8px 7px;text-align:center;border:1px solid #ddd}
	.title{
		text-align:center !important;
		background:#efefef;
	}
	.form-control{
		display:inline-block;
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
				<h3 class="sub-header" style="margin-top: 0px">编辑车辆信息</h3>
				<form id="saveProjectInfo" action="/administration/vehicle/save_saveVehicle"
					method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
					<s:token></s:token>
					<table id="vehicleInfo">
						<tbody>
							<tr>
								<td colspan="4" class="title">基本信息</td>
							</tr>
							<tr>
								<td style="width:25%">车牌<span style="color:red"> *</span></td>
								<td style="width:25%">
									<input class="form-control" autocomplete="off" required name="vehicleVo.licenseNumber" maxLength="50">
								</td>
								<td style="width:25%">品牌<span style="color:red"> *</span></td>
								<td style="width:25%">
									<input class="form-control" autocomplete="off" required name="vehicleVo.brand" maxLength="50">
								</td>
							</tr>
							<tr>
								<td>车辆所有人<span style="color:red"> *</span></td>
								<td>
									<input class="form-control" id="vehicleOwner" type="text" autocomplete="off" required name="vehicleVo.ownerName" maxLength="50">
								</td>
								<td>车架号</td>
								<td>
									<input class="form-control" autocomplete="off" name="vehicleVo.frameNumber" maxLength="50">
								</td>
							</tr>
							<tr>
								<td>使用性质</td>
								<td>
									<input class="form-control" autocomplete="off" name="vehicleVo.useProperty" maxLength="50">
								</td>
								<td>发动机号</td>
								<td>
									<input class="form-control" autocomplete="off" name="vehicleVo.engineNumber" maxLength="50">
								</td>
							</tr>
							<tr>
								<td>核定载重量</td>
								<td>
									<input class="form-control" autocomplete="off" name="vehicleVo.deadWeight" maxLength="50">
								</td>
								<td>车辆负责人<span style="color:red"> *</span></td>
								<td>
									<input class="form-control" id="vehicleLeader" type="text" autocomplete="off" onkeyup="checkEmptyForLeader()" required name="vehicleVo.leaderName" maxLength="50">
									<input type="hidden" name="vehicleVo.leaderId">
								</td>
							</tr>
						</tbody>
						<tbody id="insuranceRecord">
							<tr>
								<td colspan="4" class="title">保险记录<a onclick="addInsuranceRecord()" style="float:right;font-size:10px;margin: -5 0" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td>时间<span style="color:red"> *</span></td>
								<td>保额（元）<span style="color:red"> *</span></td>
								<td>下次保险时间<span style="color:red"> *</span></td>
								<td>操作</td>
							</tr>
						</tbody>
						<tbody id="yearlyInspection">
							<tr>
								<td colspan="4" class="title">年检记录<a onclick="addInspectionRecord()" style="float:right;font-size:10px;margin: -5 0" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td>时间<span style="color:red"> *</span></td>
								<td colspan="2">下次年检时间<span style="color:red"> *</span></td>
								<td>操作</td>
							</tr>
						</tbody>
						<tbody id="maintainRecord">
							<tr>
								<td colspan="4" class="title">保养记录<a onclick="addMaintainRecord()" style="float:right;font-size:10px;margin: -5 0" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td>时间<span style="color:red"> *</span></td>
								<td>保养里程数（公里）<span style="color:red"> *</span></td>
								<td>保养费用（元）<span style="color:red"> *</span></td>
								<td>操作</td>
							</tr>
						</tbody>
						<tbody id="repairRecord">
							<tr>
								<td colspan="4" class="title">维修记录<a onclick="addRepairRecord()" style="float:right;font-size:10px;margin: -5 0" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td>时间<span style="color:red"> *</span></td>
								<td>维修项目<span style="font-size:10px">（限100字）</span><span style="color:red"> *</span></td>
								<td>维修金额（元）<span style="color:red"> *</span></td>
								<td>操作</td>
							</tr>
						</tbody>
						<tbody id="vehicleUseRecord">
							<tr>
								<td colspan="4" class="title">车辆使用记录<a onclick="addVehicleUseRecord()" style="float:right;font-size:10px;margin: -5 0" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td>时间<span style="color:red"> *</span></td>
								<td>事由<span style="font-size:10px">（限100字）</span><span style="color:red"> *</span></td>
								<td>使用人<span style="color:red"> *</span></td>
								<td>操作</td>
							</tr>
						</tbody>
					</table>
					<br>
					<button type="submit" class="btn btn-primary" style="float:right;margin-right:5px">提交</button>
				</form>
			</div>
		</div>
	</div>
  	<script src="/assets/js/layer/layer.js"></script>
    <script src="/assets/js/require/require2.js"></script>
	<script>
  	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#vehicleOwner'),function ($item){
			$("input[name='vehicleVo.ownerId']").val($item.data("userId"));
		});
		new staffComplete().render($('#vehicleLeader'),function ($item){
			$("input[name='vehicleVo.leaderId']").val($item.data("userId"));
		});
		require(['staffComplete'],function (staffComplete){
			$(".vehicleUser").each(function(){
				var obj = $(this);
				new staffComplete().render(obj,function ($item){
					obj.next().val($item.data("userId"));
	    		});
			});
		});
	});
  	function checkEmpty(){
  		if($("#vehicleOwner").val()==''){
  			$("input[name='vehicleVo.ownerId']").val('');
  		}
  	}
  	function checkEmptyForLeader(){
  		if($("#vehicleLeader").val()==''){
  			$("input[name='vehicleVo.leaderId']").val('');
  		}
  	}
  	function checkEmptyForUse(target){
  		if($(target).val()==''){
  			$(target).next().val('');
  		}
  	}
	$(document).click(function (event) {
		if ($("input[name='vehicleVo.ownerId']").val()=='')
		{
			$("#vehicleOwner").val("");
		}
		if ($("input[name='vehicleVo.leaderId']").val()=='')
		{
			$("#vehicleLeader").val("");
		}
		$(".vehicleUser").each(function(){
			if($(this).next().val()==''){
				$(this).val('');
			}
		});
	});
	function addInsuranceRecord(){
		$("#insuranceRecord").append($("#insuranceRecordHtml").html());
	}
	function addInspectionRecord(){
		$("#yearlyInspection").append($("#yearlyInspectionHtml").html());
	}
	function addMaintainRecord(){
		$("#maintainRecord").append($("#maintainRecordHtml").html());
	}
	function addRepairRecord(){
		$("#repairRecord").append($("#repairRecordHtml").html());
	}
	function addVehicleUseRecord(){
		$("#vehicleUseRecord").append($("#vehicleUseRecordHtml").html());
	  	require(['staffComplete'],function (staffComplete){
			require(['staffComplete'],function (staffComplete){
				$(".vehicleUser").each(function(){
					var obj = $(this);
					new staffComplete().render(obj,function ($item){
						obj.next().val($item.data("userId"));
		    		});
				});
			});
		});
	}
	function deleteInsuranceRecord(target){
		$(target).parent().parent().remove();
	}
	function deleteInspectionRecord(target){
		$(target).parent().parent().remove();
	}
	function deleteMaintainRecord(target){
		$(target).parent().parent().remove();
	}
	function deleteRepairRecord(target){
		$(target).parent().parent().remove();
	}
	function deleteVehicleUseRecord(target){
		$(target).parent().parent().remove();
	}
	</script>
	<script type="text/html" id="insuranceRecordHtml">
							<tr>
								<td>
									<input type="text" autocomplete="off" class="form-control" name="insuranceRecord.time" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d'})"/>
								</td>
								<td>
									<input autoComplete="off" required onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"
			  						class="form-control" name="insuranceRecord.money">
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" name="insuranceRecord.nextInsuranceTime" required
	    			 				onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'%y-%M-%d' })"/>
								</td>
								<td>
									<a href="javascript:void(0)" onclick="deleteInsuranceRecord(this)"><span class="glyphicon glyphicon-remove"></span></a>
								</td>
							</tr>
	</script>
	<script type="text/html" id="yearlyInspectionHtml">
	<tr>
	<td>
		<input type="text" autocomplete="off" class="form-control" name="yearlyInspectionVo.time" required
		onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d'})"/>
	</td>
	<td colspan="2">
		<input type="text" autocomplete="off" class="form-control" name="yearlyInspectionVo.nextYearlyInspectionTime" required
			onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'%y-%M-%d' })"/>
	</td>
	<td>
		<a href="javascript:void(0)" onclick="deleteInspectionRecord(this)"><span class="glyphicon glyphicon-remove"></span></a>
	</td>
	</tr>
	</script>
	<script type="text/html" id="maintainRecordHtml">
							<tr>
								<td>
									<input type="text" autocomplete="off" class="form-control" name="maintainRecord.time" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d'})"/>
								</td>
								<td>
									<input autoComplete="off" required onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"
			  						class="form-control" name="maintainRecord.maintainMileages">
								</td>
								<td>
									<input autoComplete="off" required onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"
			  						class="form-control" name="maintainRecord.maintainMoney">
								</td>
								<td>
									<a href="javascript:void(0)" onclick="deleteMaintainRecord(this)"><span class="glyphicon glyphicon-remove"></span></a>
								</td>
							</tr>
	</script>
	<script type="text/html" id="repairRecordHtml">
							<tr>
								<td>
									<input type="text" autocomplete="off" class="form-control" name="repairRecord.time" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d'})"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" name="repairRecord.repairItems" required maxLength="100"/>
								</td>
								<td>
									<input autoComplete="off" required onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();"
			  						class="form-control" name="repairRecord.repairMoney">
								</td>
								<td>
									<a href="javascript:void(0)" onclick="deleteRepairRecord(this)"><span class="glyphicon glyphicon-remove"></span></a>
								</td>
							</tr>
	</script>
	<script type="text/html" id="vehicleUseRecordHtml">
							<tr>
								<td>
									<input type="text" autocomplete="off" class="form-control" name="vehicleUseRecord.time" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d'})"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" name="vehicleUseRecord.reason" required maxLength="100"/>
								</td>
								<td>
									<input class="form-control vehicleUser" type="text" autocomplete="off" onkeyup="checkEmptyForUse(this)" required name="vehicleUseRecord.userName" maxLength="50">
									<input type="hidden" name="vehicleUseRecord.userId">
								</td>
								<td>
									<a href="javascript:void(0)" onclick="deleteVehicleUseRecord(this)"><span class="glyphicon glyphicon-remove"></span></a>
								</td>
							</tr>
	</script>
</body>
</html>