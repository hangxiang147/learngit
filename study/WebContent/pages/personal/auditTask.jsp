<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style>
.float_div{
	position:fixed;
	width:380px;
	right:0px;
	top:70px;
	z-index:100; 
}
.form-group {
    margin-bottom: 30px;
    margin-top: 30px;
}
.col-sm-1 {
	padding-right: 0px;
	padding-left: 0px;
}

.detail-control {
	display: block;
    width: 100%;
    padding: 6px 12px;
    font-size: 14px;
    line-height: 1.42857143;
    color: #555; 
}
.inputout1{position:relative;}
.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
.text_down1 ul{padding:2px 10px;}
.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
.text_down1 ul li span{color:#cc0000;}
.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
.tab table{width:100%;border-collapse:collapse;}
.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
.tab table tr .title {text-align:center;color:#000;width:15%}
.bold{font-weight:bold}
.underline{text-decoration:underline}
table{word-break:break-all !important}
._title{text-align:center;background:#efefef}
.shopPayApply td{text-align:center;}
.col_blue{color:#428bca}
.col_blue span{color:#111;font-size:16px}
</style>
<script type="text/javascript">
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function confirmComplete(result) {
		var beginDate = $("#beginDate").val();
		if (result == 3 && beginDate == '') {
			//接收任务时，必须填写开始时间
			showAlert("请填写开始时间！");
			return;
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/confirmAssignment?taskID="+taskID+"&result="+result+"&comment="+comment+"&beginDate="+beginDate;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function taskComplete(result) {
		var businessKey = $("#businessKey").val();
		var businessType = "";
		if (businessKey == "Vacation") {
			businessType = "请假申请";
		} else if (businessKey == "Assignment") {
			businessType = "任务分配";
		} else if (businessKey == "Resignation") {
			businessType = "离职申请";
		} else if (businessKey == "Formal") {
			businessType = "转正申请";
		} else if (businessKey == "Reimbursement") {
			businessType = "报销申请";
		} else if (businessKey == "Email") {
			businessType = "公司邮箱申请";
		} else if(businessKey=="ChopBorrow"){
			businessType = "公章申请";
		}else if(businessKey=="IDBorrow"){
			businessType = "身份证借用";
		}else if(businessKey=="CarUse"){
			businessType = "车辆预约";
		}else if(businessKey=="CertificateBorrow"){
			businessType = "证件申请";
		}else if(businessKey=="ContractBorrow"){
			businessType = "合同借阅";
		}else if(businessKey=="ContractSign"){
			businessType = "合同签署";
		}else if(businessKey=="ChangeContract"){
			businessType = "合同变更或解除";
		}else if(businessKey=="bankAccount"){
			businessType = "银行账户申请";
		}else if(businessKey=="DestroyChop"){
			businessType = "印章缴销申请";
		}else if(businessKey=="purchaseProperty"){
			businessType = "财产购置申请";
		}else if(businessKey=="CarveChop"){
			businessType = "印章刻制申请";
		}else if(businessKey=="handleProperty"){
			businessType = "资产处置申请";
		}else if(businessKey=="transferProperty"){
			businessType = "资产调拨申请";
		}else if(businessKey=="shopApply"){
			businessType = "店铺申请";
		}else if(businessKey=="shopPayApply"){
			businessType = "店铺付费申请";
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/taskComplete?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}";
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function contractSignFinancialConfirm(result){
		businessType = "合同签署";
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		var exceedGroup = "";
		$("input[name='exceedGroup']").each(function(){
			if($(this).prop("checked")==true){
				exceedGroup = $(this).val();
			}
		});
		var exceedSeason = "";
		$("input[name='exceedSeason']").each(function(){
			if($(this).prop("checked")==true){
				exceedSeason = $(this).val();
			}
		});
		var exceedGroupRate = $("input[name='exceedGroupRate']").val();
		var exceedSeasonRate = $("input[name='exceedSeasonRate']").val();
		window.location.href = "personal/contractSignConfirm?taskID="+taskID+"&result="+result+"&comment="+
				comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}&exceedGroup="+exceedGroup+"&exceedGroupRate="+
				exceedGroupRate+"&exceedSeason="+exceedSeason+"&exceedSeasonRate="+exceedSeasonRate;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	/*
		合同审批做特殊处理 
		审批时: 记录审批时间(默认)
		签署时: 记录签署时间(默认)
				记录保存地址 
				记录保管人
	*/
	var constactType={
		contract_subject:1,
		contract_complete:2
	}
	var constractConfirm=function(type,result){
		var type_=constactType[type];
		var businessType = "合同签署";
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		if(type_==1){
			window.location.href="personal/contractConfirm1?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}";
			Load.Base.LoadingPic.FullScreenShow(null);
		}else{
			var store_area=$('#store_place').val();
			if(!store_area){
				alert("请填写合同存储地址")
				return;
			}
			var id=$('input[name="id"]','#store').val();
			if(!id){
				alert("请选择合同保管人")
				return;
			}
			window.location.href="personal/contractConfirm2?taskID="+taskID+"&result="+result+"&comment="+comment+"&businessType="+businessType+"&taskDefKey=${taskDefKey}&store_area="+store_area+"&store_person_id="+id;
			Load.Base.LoadingPic.FullScreenShow(null);
		}
	}
	function formalConfirm(result) {
		var actualFormalDate = $("#actualFormalDate").val();
		if (result==1 && actualFormalDate== '') {
			//同意转正时，必须填写转正日期
			showAlert("请确认转正日期！");
			return;
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/formalConfirm?taskID="+taskID+"&result="+result+"&comment="+comment+"&actualFormalDate="+actualFormalDate;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function resignationConfirm(result) {
		var confirmLeaveDate = $("#confirmLeaveDate").val();
		if (result == 1 && confirmLeaveDate== '') {
			//同意离职时，必须填写确认离职日期
			showAlert("请确认离职日期！");
			return;
		}
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/resignationConfirm?taskID="+taskID+"&result="+result+"&comment="+comment+"&confirmLeaveDate="+confirmLeaveDate;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function updateAssignment() {
		var taskID = $("#taskID").val();
		window.location.href = "personal/updateAssignment?taskID="+taskID;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function inspectAssignment() {
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/inspectAssignment?taskID="+taskID+"&comment="+comment;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function updateAccount() {
		var taskID = $("#taskID").val();
		window.location.href = "pages/personal/updateBankAccount.jsp?taskID="+taskID;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function requestUserConfirm() {
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/emailConfirm?taskID="+taskID+"&comment="+comment;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
    function checkBudget(id, target){
  	  var value = $(target).val();
  	  if(value=='1'){
  		  $("#"+id).css("display","block");
  	  }else{
  		  $("#"+id).css("display","none");
  	  }
    }
	function handOverChop() {
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/handOverChop?taskID="+taskID+"&comment="+comment;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function propertySign(){
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/propertySign?taskID="+taskID+"&comment="+comment;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function completeHandle(businessKey){
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		var expiredTime = $("#expiredTime").val();
		window.location.href = "personal/completeHandle?taskID="+taskID+"&comment="+comment+"&businessKey="+businessKey+"&expiredTime="+expiredTime;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function handleSuccess(){
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/handleSuccess?taskID="+taskID+"&comment="+comment;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function purchaseConfirm(taskDefKey){
		  if(taskDefKey=='purchaserConfirm'){
			  if($("input[name='productName']").val()==''){
				  layer.alert("品名不能为空",{offset : ['100px']});
				  return;
			  }
			  if($("input[name='_model']").val()==''){
				  layer.alert("规格型号不能为空",{offset : ['100px']});
				  return;
			  }
			  if($("input[name='_number']").val()==''){
				  layer.alert("数量不能为空",{offset : ['100px']});
				  return;
			  }
			  if($("input[name='unitPrice']").val()==''){
				  layer.alert("单价不能为空",{offset : ['100px']});
				  return;
			  }
		  }
		  var $form = $("form");
		  $.ajax({  
              type: "post",  
              data: $form.serialize(),
              url: "/personal/purchaseConfirm?taskDefKey="+taskDefKey,
              success: function (data) { 
            	  var error = data.error;
            	  if(error != undefined){
            		  layer.alert(error,{offset:['100px']});
            	  }else{
            		  window.location.href="personal/findTaskList?type="+data.type;
            		  Load.Base.LoadingPic.FullScreenShow(null);
            	  }
              }
          }); 
	}
	function completeTransfer(){
		var taskID = $("#taskID").val();
		var comment = $("#comment").val();
		window.location.href = "personal/completeTransfer?taskID="+taskID+"&comment="+comment+"&assetId=${formFields[14].fieldValue}"
				+"&newCompanyName=${formFields[13].fieldValue}";
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function financialOpenAccount(){
		var shopType = '${shopApplyVo.shopType}';
		if($("#publicBankAccount").val()=='' && shopType=='企业开店'){
			  layer.alert("对公银行账号不能为空",{offset : '100px'});
			  return;
		}
		if($("#aliPayAccount").val()==''){
			  layer.alert("认证支付宝不能为空",{offset : '100px'});
			  return;
		}
		if($("#aliPayPhone").val()==''){
			  layer.alert("支付宝验证手机不能为空",{offset : '100px'});
			  return;
		}
		if(!checkError()){
			return;
		}
		window.location.href="personal/financialOpenAccount?"+$("form").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
  	function checkError(){
  		var flag = '';
  		$(".error").each(function(){
  			if($(this).css("display")!="none"){
  				flag += 'false';
  			}
  		});
  		if(flag!=''){
  			layer.alert("存在不规范信息，请修改",{"offset":"100px"});
			return false;
  		}
  		return true;
  	}
</script>
<style type="text/css">
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal" enctype="multipart/form-data" 
        	action="/personal/completeDestroy" method="post" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <h3 class="sub-header" style="margin-top:0px;">任务办理</h3>
        	  <input type="hidden" id="taskID" value="${taskID}" name="taskID"/>
        	  <input type="hidden" id="businessKey" value="${businessKey}" />
         	  <c:if test="${not empty formFields && businessKey!='ChopBorrow' && businessKey!='ContractSign'
         	   && businessKey!='ChangeContract' && businessKey!='bankAccount' && businessKey!='DestroyChop'
         	   && businessKey!='purchaseProperty' && businessKey!='CarveChop' && businessKey!='handleProperty'
         	   && businessKey!='transferProperty'}">
        	  <c:forEach items="${formFields }" var="formField" varStatus="st">
        	  	<div class="form-group">
        	  		<label class="col-sm-2 control-label">${formField.fieldText}：</label>
        	  		<div class="col-sm-10">
        	  			<c:set var="fieldValue" value="${formField.fieldValue }"></c:set>
       	  				<% request.setAttribute("vEnter", "\n"); %> 
        	  			<span class="detail-control"><c:out value="${fn:replace(fieldValue, vEnter, '<br>')}" escapeXml="false"/></span>
        	  		</div>
        	  	</div>	
        	  </c:forEach>
        	  </c:if>
        	  <c:if test="${businessKey=='ChopBorrow'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">印章使用申请单</h4>
        	  	<div style="text-align:right;margin-right:20px">NO:年份&nbsp;<span class="underline">${year}</span>
        	  	&nbsp;编号&nbsp;<span class="underline">0000${chopBorrow_Id}</span></div>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">所属部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${formFields[2].fieldValue}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">印章名称</td>
        	  			<td colspan="5">${formFields[4].fieldValue}</td>
        	  		</tr>
        	  		<tr>
	        	  		<c:if test="${formFields[11].fieldValue=='是'}">
	        	  			<td class="title">是否外借</td>
	        	  			<td>${formFields[11].fieldValue}</td>
	        	  			<td class="title">开始时间</td>
	        	  			<td>${formFields[12].fieldValue}</td>
	        	  			<td class="title">结束时间</td>
	        	  			<td>${formFields[13].fieldValue}</td>
	        	  		</c:if>
	        	  		<c:if test="${formFields[11].fieldValue=='否'}">
	        	  			<td class="title">是否外借</td>
	        	  			<td colspan="5">${formFields[11].fieldValue}</td>
	        	  		</c:if>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">文件基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">文件名称</td>
        	  			<td colspan="3">${formFields[6].fieldValue}</td>
        	  			<td class="title">文件类型</td>
        	  			<td>
        	  				<c:if test="${formFields[11].fieldValue=='否'}">
        	  					${formFields[12].fieldValue}
        	  				</c:if>
        	  				<c:if test="${formFields[11].fieldValue=='是'}">
        	  					${formFields[14].fieldValue}
        	  				</c:if>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">文件用途</td>
        	  			<td colspan="3">${formFields[7].fieldValue}</td>
        	  			<td style="text-align:left">是否涉及法律等重要事项</td>
        	  			<td>
        	  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[8].fieldValue=="1"}'>checked</c:if> name="chopBorrrowVo.relateLaw" value="1" style="height:15px;width:15px">&nbsp;<label>是</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[8].fieldValue=="0"}'>checked</c:if> name="chopBorrrowVo.relateLaw" value="0" style="height:15px;width:15px">&nbsp;<label>否</label> 
        	  			</td>
        	  		</tr>
        	  		<c:if test="${formFields[12].fieldValue=='合同'}">
        	  		<tr>
        	  			<td class="title">甲方</td>
        	  			<td>${formFields[13].fieldValue}</td>
        	  			<td class="title">乙方</td>
        	  			<td>${formFields[14].fieldValue}</td>
        	  			<td class="title">合同申请时间</td>
        	  			<td>${formFields[15].fieldValue}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${formFields[14].fieldValue=='合同'}">
        	  		<tr>
        	  			<td class="title">甲方</td>
        	  			<td>${formFields[15].fieldValue}</td>
        	  			<td class="title">乙方</td>
        	  			<td>${formFields[16].fieldValue}</td>
        	  			<td class="title">合同申请时间</td>
        	  			<td>${formFields[17].fieldValue}</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr>
        	  			<td class="title">正版/复印件</td>
        	  			<td colspan="3">
        	  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[9].fieldValue=="0"}'>checked</c:if> name="chopBorrrowVo.isCopy" value="0" style="height:15px;width:15px">&nbsp;<label>正版</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[9].fieldValue=="1"}'>checked</c:if> name="chopBorrrowVo.isCopy" value="1" style="height:15px;width:15px">&nbsp;<label>复印件</label>
        	  			</td>
        	  			<td class="title">份数</td>
        	  			<td>${formFields[10].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门经理</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门分管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章主管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理人员</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门经理</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">所属部门分管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章主管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理人员</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='ContractSign'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">合同审批表</h4>
        	  	<div style="text-align:right;margin-right:20px">合同编号：<span class="underline">${formFields[4].fieldValue}</span></div>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">主办部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">拟稿人</td>
        	  			<td>${formFields[2].fieldValue}</td>
        	  			<td class="title">提交日期</td>
        	  			<td style="width:16%">${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">合同基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">合同名称</td>
        	  			<td colspan="5">${formFields[5].fieldValue}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">对方公司名称</td>
						<td colspan="3">${formFields[6].fieldValue}</td>
						<td class="title">合同金额</td>
						<td>${formFields[8].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">合同详情</td>
        	  			<td colspan="5">${formFields[7].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">主办部门经理</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="4">财务部负责人</td>
        	  			<td class="title" rowspan="2">仅限支出类合同</td>
        	  			<c:if test="${formFields[9].fieldValue=='是'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" ${formFields[10].fieldValue=='否'?'checked':''} style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" ${formFields[10].fieldValue=='是'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   ${formFields[12].fieldValue}%）</td>
        	  			</c:if>
        	  			<c:if test="${formFields[9].fieldValue=='否'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<c:if test="${formFields[9].fieldValue=='是'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" ${formFields[11].fieldValue=='否'?'checked':''} style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" ${formFields[11].fieldValue=='是'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（    ${formFields[13].fieldValue}%）</td>
        	  			</c:if>
        	  			<c:if test="${formFields[9].fieldValue=='否'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">主办部门经理</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="4">财务部负责人</td>
        	  			<td class="title" rowspan="2">仅限支出类合同</td>
        	  			<c:if test="${formFields[9].fieldValue=='是'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" ${formFields[10].fieldValue=='否'?'checked':''} style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" ${formFields[10].fieldValue=='是'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   ${formFields[12].fieldValue}%）</td>
        	  			</c:if>
        	  			<c:if test="${formFields[9].fieldValue=='否'}">
	        	  			<td colspan="2">是否超出本季度预算 </td>
	        	  			<td>
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
	        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
	        	  			</td>
	        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<c:if test="${formFields[9].fieldValue=='是'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" ${formFields[11].fieldValue=='否'?'checked':''} style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" ${formFields[11].fieldValue=='是'?'checked':''} style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（    ${formFields[13].fieldValue}%）</td>
        	  			</c:if>
        	  			<c:if test="${formFields[9].fieldValue=='否'}">
        	  			 <td colspan="2">是否超出集团审定预算 </td>
        	  			 <td>
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>否</label>&nbsp;&nbsp;&nbsp;
        	  				<input type="checkbox" onclick="return false;" style="height:15px;width:15px">&nbsp;<label>是</label>
        	  			</td>
        	  			<td>超出比例（   &nbsp;%）</td>
        	  			</c:if>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='CarveChop'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">印章刻制申请表</h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${formFields[2].fieldValue}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">印章基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">申请刻制印章名称（全称）</td>
        	  			<td colspan="3">${formFields[4].fieldValue}</td>
        	  			<td class="title">印章类型</td>
        	  			<td colspan="1">${formFields[6].fieldValue}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">刻章理由</td>
						<td colspan="5">
							<input type="checkbox" onclick="return false;" ${formFields[5].fieldValue=='新制'?'checked':''} style="height:15px;width:15px">&nbsp;<label style="margin-bottom:2px">新制</label>&nbsp;&nbsp;&nbsp;
	        	  			<input type="checkbox" onclick="return false;" ${formFields[5].fieldValue=='遗失'?'checked':''} style="height:15px;width:15px">&nbsp;<label>遗失</label>&nbsp;&nbsp;&nbsp;
							<input type="checkbox" onclick="return false;" ${formFields[5].fieldValue=='损坏'?'checked':''} style="height:15px;width:15px">&nbsp;<label>损坏</label>&nbsp;&nbsp;&nbsp;
							<input type="checkbox" onclick="return false;" ${formFields[5].fieldValue=='更名'?'checked':''} style="height:15px;width:15px">&nbsp;<label>更名</label>&nbsp;&nbsp;&nbsp;
							<input type="checkbox" onclick="return false;" ${formFields[5].fieldValue=='更换材质'?'checked':''} style="height:15px;width:15px">&nbsp;<label>更换材质</label>
						</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">备注</td>
        	  			<td colspan="5">${formFields[7].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">用章部门</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">行政人事部门</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">用章部门</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">行政人事部门</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='handleProperty'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">资产处置申请单</h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请人</td>
        	  			<td colspan="3">${formFields[2].fieldValue}</td>
        	  			<td class="title">申请日期</td>
        	  			<td colspan="2">${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">资产基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">资产名称</td>
        	  			<td colspan="3">${formFields[5].fieldValue}</td>
        	  			<td class="title">资产编号</td>
        	  			<td colspan="1">${formFields[6].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">规格型号</td>
        	  			<td colspan="3">${formFields[7].fieldValue}</td>
        	  			<td class="title">使用部门</td>
        	  			<td colspan="1">${formFields[4].fieldValue}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">处置原因</td>
						<td colspan="5">${formFields[8].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">处置方案</td>
        	  			<td colspan="5">${formFields[9].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物使用部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物管理部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='transferProperty'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">固定资产调拨申请单</h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请人</td>
        	  			<td colspan="3">${formFields[2].fieldValue}</td>
        	  			<td class="title">申请日期</td>
        	  			<td>${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">资产基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">资产名称</td>
        	  			<td colspan="3">${formFields[4].fieldValue}</td>
        	  			<td class="title">资产编号</td>
        	  			<td colspan="1">${formFields[5].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">规格型号</td>
        	  			<td colspan="3">${formFields[7].fieldValue}</td>
        	  			<td class="title">类型</td>
        	  			<td colspan="1">${formFields[6].fieldValue}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">数量</td>
						<td>${formFields[8].fieldValue}</td>
						<td class="title">单价</td>
						<td>${formFields[9].fieldValue}元</td>
						<td class="title">金额</td>
						<td>${formFields[10].fieldValue}元</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">调出单位</td>
        	  			<td colspan="3">${formFields[12].fieldValue}</td>
        	  			<td class="title">调入单位</td>
        	  			<td colspan="1">${formFields[13].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">调拨原因</td>
        	  			<td colspan="5">${formFields[11].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物使用部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">实物管理部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务部门负责人</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='shopApply'}">
        	  	<div id="printArea">
        	  	
        	  	<h4 style="text-align:center" class="bold">${shopApplyVo.applyType}申请单</h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${shopApplyVo.userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${shopApplyVo.requestDate}</td>
        	  		</tr>
        	  		<c:if test="${shopApplyVo.applyType=='开店'}">
        	  			<tr>
	        	  			<td class="title">办理人</td>
	        	  			<td colspan="5">${shopApplyVo.handlerName}</td>
	        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='店铺信息维护'}">
	        	  		<tr>
	        	  			<td class="title">操作账号</td>
	        	  			<td>${shopApplyVo.operationAccount}</td>
	        	  			<td class="title">办理人</td>
	        	  			<td colspan="3">${shopApplyVo.handlerName}</td>
	        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='关店'}">
	        	  		<tr>
	        	  			<td class="title">操作账号</td>
	        	  			<td>${shopApplyVo.operationAccount}</td>
	        	  			<td class="title">财产接收人</td>
	        	  			<td>${shopApplyVo.propertyReceiver}</td>
	        	  			<td class="title">办理人</td>
	        	  			<td>${shopApplyVo.handlerName}</td>
	        	  		</tr>
        	  		</c:if>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">店铺基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">店铺名称</td>
        	  			<td colspan="1">${shopApplyVo.shopName}</td>
        	  			<td class="title">平台</td>
        	  			<td colspan="1">${shopApplyVo.platform}</td>
        	  			<td class="title">开店时间</td>
        	  			<td colspan="1">${shopApplyVo.shopStartTime}</td>
        	  		</tr>
        	  		<c:if test="${shopApplyVo.applyType=='开店'}">
        	  		<tr>
        	  			<td class="title" rowspan="2">开店类型（个人开店/企业开店）</td>
        	  			<td rowspan="2">${shopApplyVo.shopType}</td>
        	  			<td class="title" rowspan="2">主营</td>
        	  			<td class="title">一级类目</td>
        	  			<td colspan="2">${shopApplyVo.firstCategory}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">二级类目</td>
        	  			<td colspan="2">${shopApplyVo.secondCategory}</td>
        	  		</tr>
					<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">认证信息</td></tr>
					<c:if test="${shopApplyVo.shopType=='企业开店'}">
						<tr>
							<td class="title" rowspan="3">企业</td>
							<td class="title">法定代表人</td>
							<td>${shopApplyVo.legalPerson}</td>
							<td class="title">认证企业</td>
							<td colspan="2">${shopApplyVo.certificatedCompany}</td>
						</tr>
						<tr>
							<td class="title">支付宝验证手机</td>
							<td>${shopApplyVo.companyAliPayPhone}</td>
							<td class="title">企业认证支付宝</td>
							<td colspan="2">${shopApplyVo.companyAliPayAccount}</td>
						</tr>
						<tr>
							<td class="title">对公银行账号</td>
							<td colspan="5">${shopApplyVo.publicBankAccount}</td>
						</tr>
					</c:if>
					<c:if test="${shopApplyVo.shopType=='个人开店'}">
						<tr>
							<td class="title" rowspan="4">个人</td>
							<td class="title">店铺负责人</td>
							<td>${shopApplyVo.shopOwner}</td>
							<td class="title">认证支付宝</td>
							<td colspan="2">${shopApplyVo.privateAliPayAccount}</td>
						</tr>
						<tr>
							<td class="title">支付宝验证手机</td>
							<td>${shopApplyVo.privateAliPayPhone}</td>
							<td class="title">个人与企业是否签订协议</td>
							<td colspan="2">${shopApplyVo.signIn}</td>
						</tr>
						<tr>
							<td class="title">法定代表人</td>
							<td>${shopApplyVo.privateLegalPerson}</td>
							<td class="title">认证企业</td>
							<td colspan="2">${shopApplyVo.privateCertificatedCompany}</td>
						</tr>
						<tr>
							<td class="title">对公银行账号</td>
							<td colspan="4">${shopApplyVo.publicBankAccount}</td>
						</tr>
					</c:if>
					<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">开店费用</td></tr>
					<tr>
        	  			<td class="title">保证金</td>
        	  			<td colspan="1">${shopApplyVo.bond}元</td>
        	  			<td class="title">技术年费</td>
        	  			<td colspan="1">${shopApplyVo.technologyAnnualFee}元</td>
        	  			<td class="title">平台佣金比例</td>
        	  			<td colspan="1">${shopApplyVo.commissionRate}%</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='店铺信息维护'}">
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">申请修改信息</td></tr>	
        	  		<tr>
        	  			<td class="title" style="height:80px">原信息</td>
        	  			<td colspan="5">${shopApplyVo.oldInformation}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="height:80px">变更后信息</td>
        	  			<td colspan="5">${shopApplyVo.changeInformation}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${shopApplyVo.applyType=='关店'}">
        	  		<tr>
        	  			<td class="title" style="height:80px">关店原因</td>
        	  			<td colspan="3">${shopApplyVo.closeShopReason}</td>
        	  			<td class="title" style="height:80px">关店时间</td>
        	  			<td>${shopApplyVo.closeShopTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法人代表）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法人代表）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='shopPayApply'}">
        	  	<div id="printArea">
        	  	
        	  	<h4 style="text-align:center" class="bold">运营部营销费用充值申请表</h4>
        	  	<div class="tab shopPayApply">
        	  	<table>
        	  		<tr>
        	  			<td class="_title">申请时间</td>
        	  			<td colspan="2">${fn:split(shopPayApplyVo.requestDate, " ")[0]}</td>
        	  			<td class="_title">申请人</td>
        	  			<td colspan="3">${shopPayApplyVo.userName}</td>
        	  			<td class="_title">使用部门</td>
        	  			<td colspan="2">${department}</td>
        	  			<td class="_title">充值周期</td>
        	  			<td colspan="3">${shopPayApplyVo.beginDate}至${shopPayApplyVo.endDate}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="_title" colspan="5">运营推广费用</td>
        	  			<td class="_title" colspan="5">付费插件费用</td>
        	  			<td class="_title" colspan="4">其他服务费</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="_title" style="width:8%">店铺名称</td>
        	  			<td class="_title" style="width:6%">直通车</td>
        	  			<td class="_title" style="width:6%">钻石展位</td>
        	  			<td class="_title" style="width:6%">品销宝</td>
        	  			<td class="_title" style="width:6%">小计</td>
        	  			<td class="_title" style="width:8%">店铺名称</td>
        	  			<td class="_title" style="width:8%">服务/应用名称</td>
        	  			<td class="_title" style="width:8%">插件/服务作用</td>
        	  			<td class="_title" style="width:8%">开通时长</td>
        	  			<td class="_title" style="width:8%">付费金额</td>
        	  			<td class="_title" style="width:8%">项目名称</td>
        	  			<td class="_title" style="width:7%">项目作用</td>
        	  			<td class="_title" style="width:7%">项目明细</td>
        	  			<td class="_title" style="width:6%">项目花费</td>
        	  		</tr>
        	  		<c:forEach items="${shopPayApplyListVos}" var="shopPayApplyListVo">
        	  			<tr>
        	  				<td>${shopPayApplyListVo.spreadShopName}</td>
        	  				<td>${shopPayApplyListVo.directPassMoney}</td>
        	  				<td>${shopPayApplyListVo.showMoney}</td>
        	  				<td>${shopPayApplyListVo.saleMoney}</td>
        	  				<td>${shopPayApplyListVo.total}</td>
        	  				<td>${shopPayApplyListVo.pluginShopName}</td>
        	  				<td>${shopPayApplyListVo.serviceName}</td>
        	  				<td>${shopPayApplyListVo.serviceUse}</td>
        	  				<td><c:if test="${shopPayApplyListVo.openTime!='' && shopPayApplyListVo.openTime!=null}">${shopPayApplyListVo.openTime}个月</c:if></td>
        	  				<td>${shopPayApplyListVo.payMoney}</td>
        	  				<td>${shopPayApplyListVo.projectName}</td>
        	  				<td>${shopPayApplyListVo.projectUse}</td>
        	  				<td>${shopPayApplyListVo.projectDescription}</td>
        	  				<td>${shopPayApplyListVo.projectPay}</td>
        	  			</tr>
        	  		</c:forEach>
        	  		<tr>
        	  			<td class="_title">运营推广费用合计</td>
        	  			<td colspan="2">${resultMap.运营推广费用合计==null?'/':resultMap.运营推广费用合计}</td>
        	  			<td class="_title">付费插件费用合计</td>
        	  			<td colspan="2">${resultMap.付费插件费用合计==null?'/':resultMap.付费插件费用合计}</td>
        	  			<td class="_title">其他服务费合计</td>
        	  			<td colspan="2">${resultMap.其他服务费合计==null?'/':resultMap.其他服务费合计}</td>
        	  			<td class="_title">总计（大写）</td>
        	  			<td colspan="4" id="totalShopPay" class="col_blue"></td>
        	  		</tr>
        	  		<tr>
        	  			<td class="_title">总经理：</td>
        	  			<td colspan="3">${finishedTaskVOs[2].assigneeName}</td>
        	  			<td class="_title">财务主管：</td>
        	  			<td colspan="3">${finishedTaskVOs[1].assigneeName}</td>
        	  			<td class="_title">部门主管：</td>
        	  			<td colspan="2">${finishedTaskVOs[0].assigneeName}</td>
        	  			<td class="_title">经办人：</td>
        	  			<td colspan="2">${finishedTaskVOs[4].assigneeName}</td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	</div>
        	 	</c:if>
        	   <c:if test="${businessKey=='ChangeContract'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">合同变更或解除申请表</h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">所属部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${formFields[2].fieldValue}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">合同基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">合同名称</td>
        	  			<td colspan="5">${formFields[5].fieldValue}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">合同编号</td>
						<td colspan="3">${formFields[4].fieldValue}</td>
						<td class="title">签订时间</td>
						<td>${formFields[7].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">合同内容</td>
        	  			<td colspan="5">${formFields[6].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">合同已履行情况介绍</td>
        	  			<td colspan="5">${formFields[8].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="3">变更合同</td>
        	  			<td class="title">变更合同原因</td>
        	  			<td colspan="4">${formFields[9].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更前内容</td>
        	  			<td colspan="4">${formFields[10].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更后内容</td>
        	  			<td colspan="4">${formFields[11].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">解除合同原因</td>
        	  			<td colspan="4">${formFields[12].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='bankAccount'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">开设、变更及撤销银行账户申请表</h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">经办人</td>
        	  			<td>${formFields[2].fieldValue}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">账户基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">单位名称</td>
        	  			<td colspan="5">${formFields[4].fieldValue}</td>
        	  		</tr>
        	  		<tr>
						<td class="title">开户行全称</td>
						<td colspan="5">${formFields[6].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">账户类别</td>
        	  			<td colspan="2">${formFields[5].fieldValue}</td>
        	  			<td class="title">账   号</td>
        	  			<td colspan="2">${formFields[7].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">申请信息</td></tr>
        	  		<c:if test="${formFields[15].fieldValue=='开立'}">        	  		
        	  		<tr>
        	  			<td class="title" rowspan="2">开立</td>
        	  			<td class="title">开户依据</td>
        	  			<td colspan="4">${formFields[8].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">账户用途</td>
        	  			<td colspan="4">${formFields[9].fieldValue}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${formFields[15].fieldValue=='变更'}">
        	  		<tr>
        	  			<td class="title" rowspan="3">变更</td>
        	  			<td class="title">变更事项</td>
        	  			<td colspan="4">${formFields[10].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更后信息</td>
        	  			<td colspan="4">${formFields[11].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">变更原因</td>
        	  			<td colspan="4">${formFields[12].fieldValue}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${formFields[15].fieldValue=='撤销'}">
        	  		<tr>
        	  			<td class="title" rowspan="2">撤销</td>
        	  			<td class="title">销户原因</td>
        	  			<td colspan="4">${formFields[13].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">资金去向</td>
        	  			<td colspan="4">${formFields[14].fieldValue}</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[0].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[0].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">财务主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）审批</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='DestroyChop'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">印章缴销申请表</h4>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">申请部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${formFields[2].fieldValue}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%">${formFields[0].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">印章基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">印章名称</td>
        	  			<td colspan="2">${formFields[4].fieldValue}</td>
        	  			<td class="title">印章类型</td>
        	  			<td colspan="2">
        	  			<c:if test="${formFields[7].fieldValue=='1'}">
        	  				公章
        	  			</c:if>
        	  			<c:if test="${formFields[7].fieldValue=='2'}">
        	  				合同专用章
        	  			</c:if>
        	  			<c:if test="${formFields[7].fieldValue=='3'}">
        	  				法人章
        	  			</c:if>
        	  			<c:if test="${formFields[7].fieldValue=='4'}">
        	  				财务专用章
        	  			</c:if>
        	  			<c:if test="${formFields[7].fieldValue=='5'}">
        	  				发票专用章
        	  			</c:if>
        	  			</td>
        	  		</tr>
        	  		<tr>
						<td class="title">印章描述</td>
						<td colspan="5">${formFields[6].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">缴销原因</td>
        	  			<td colspan="5">${formFields[5].fieldValue}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<c:if test="${null != supervisor}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理部门领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${null == supervisor}">
        	  		<tr>
        	  			<td class="title" rowspan="2">部门主管</td>
        	  			<td colspan="5">审批意见：
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：</td>
        	  			<td>日期：</td>
        	  			<td></td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">分管副总</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments}" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">印章管理部门领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" rowspan="2">总经理（法定代表人）</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		</c:if>
        	  	</table>
        	  	</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${businessKey=='purchaseProperty'}">
        	  	<div id="printArea">
        	  	<h4 style="text-align:center" class="bold">财产购置申请单</h4>
        	  	<div style="font-weight:bold">
        	  	<div style="float:left;margin-left:20px">申购单编号：0000${id}</div>
        	  	<div style="float:right;margin-right:20px">申购日期：${fn:split(formFields[0].fieldValue," ")[0]}</div>
        	  	</div>
        	  	<br>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">部门</td>
        	  			<td colspan="3">${department}</td>
        	  			<td class="title">申购人</td>
        	  			<td>${formFields[2].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">固定资产和低值易耗品名称</td>
        	  			<td colspan="3">${formFields[4].fieldValue}</td>
        	  			<td class="title">数量</td>
        	  			<td>${formFields[5].fieldValue}</td>
        	  		</tr>
        	  		<tr>
	        	  		<td class="title">预算总价</td>
        	  			<td>${formFields[6].fieldValue}元</td>
        	  			<td class="title">预算</td>
        	  			<td>
        	  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[7].fieldValue=="0"}'>checked</c:if>  value="0" style="height:15px;width:15px">&nbsp;<label>内</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[7].fieldValue=="1"}'>checked</c:if>  value="1" style="height:15px;width:15px">&nbsp;<label>外</label> 
        	  			</td>
        	  			<td class="title">型号规格</td>
        	  			<td colspan="5">${formFields[8].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">使用地点</td>
        	  			<td colspan="3">${formFields[9].fieldValue}</td>
        	  			<td class="title">使用(保管)人</td>
        	  			<td>${formFields[10].fieldValue}</td>
        	  		</tr>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td style="border-right-color:#fff;border-bottom-color:#fff" class="title">购置原因：</td>
        	  			<td style="border-bottom-color:#fff" colspan="5">${formFields[11].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td style="border-right-color:#fff" colspan="4"></td>
        	  			<td style="border-right-color:#fff">申购部门主管签字：</td>
        	  			<td>${finishedTaskVOs[1 ].assigneeName}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title">购置原因</td>
        	  			<td colspan="5">${formFields[11].fieldValue}</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor!=null}">
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">采购人意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='purchaserAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">办公室意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">分管领导意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[5].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">总经理意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[6].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">预算小组意见：</td>
        	  			<td colspan="5">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='budgetAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		</c:if>
        	  		<c:if test="${supervisor==null}">
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">采购人意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='purchaserAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">办公室意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">分管领导意见：</td>
        	  			<td colspan="2" style="width:40%">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  			<td class="title" style="border-right-color:#fff">总经理意见：</td>
        	  			<td colspan="2">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[5].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title" style="border-right-color:#fff">预算小组意见：</td>
        	  			<td colspan="5">
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:forEach items="${finishedTaskVOs}" var="task">
		        	  				<c:if test="${comment.taskID == task.taskID && task.taskDefKey=='budgetAudit'}">
						  				${comment.content }
		        	  				</c:if>
	        	  				</c:forEach>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr>
        	  			<td colspan="6" class="bold" style="text-align:center">以下由采购人员填写</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2">品名:${formFields[12].fieldValue}</td>
        	  			<td colspan="2">规格型号:${formFields[13].fieldValue}</td>
        	  			<td>数量:${formFields[14].fieldValue}</td>
        	  			<td>单价:<c:if test="${formFields[15].fieldValue!=''&&formFields[15].fieldValue!=null}">${formFields[15].fieldValue}元</c:if></td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2" class="title">使用或采购部门验收意见</td>
        	  			<td colspan="2">
        	  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[16].fieldValue=="0"}'>checked</c:if>  value="1" style="height:15px;width:15px">&nbsp;<label>合格</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[16].fieldValue=="1"}'>checked</c:if>  value="0" style="height:15px;width:15px">&nbsp;<label>不合格</label> 
        	  			</td>
        	  			<td class="title">使用或申购部门签收</td>
        	  			<td>
        	  				<c:forEach items="${finishedTaskVOs}" var="task">
	        	  				<c:if test="${task.taskDefKey=='propertySign'}">
					  				${task.assigneeName}
	        	  				</c:if>
	        	  			</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="6" class="bold" style="text-align:center">以下由财务部填写</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2" class="title">固定资产和低值易耗品分类</td>
        	  			<td colspan="2">
        	  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[17].fieldValue=="0"}'>checked</c:if>  value="1" style="height:15px;width:15px">&nbsp;<label>固定资产</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${formFields[17].fieldValue=="1"}'>checked</c:if>  value="0" style="height:15px;width:15px">&nbsp;<label>低值易耗品</label> 
        	  			</td>
        	  			<td colspan="2">固定资产和低值易耗品编号：${formFields[18].fieldValue}</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="2" class="title">折旧年限</td>
        	  			<td colspan="2"><c:if test="${formFields[19].fieldValue!=''&&formFields[19].fieldValue!=null}">${formFields[19].fieldValue}年</c:if></td>
        	  			<td class="title">净残值率</td>
        	  			<td><c:if test="${formFields[20].fieldValue!=''&&formFields[20].fieldValue!=null}">${formFields[20].fieldValue}%</c:if></td>
        	  		</tr>
        	  	</table>
        	  	</div>
        	  	<div style="color:red">注：本表一式二份，一份由办公室固定资产和低值易耗品管理员留存，一份由财务作为入账凭证。</div>
        	  	</div>
        	  </c:if>
        	  <c:if test="${taskDefKey=='contract_complete'}">
        	  	<div class="form-group">
        	    <label class="col-sm-2 control-label">合同保存人：</label>
        	    <div class="col-sm-2" id="store"><input class="form-control" id="store_input" autocomplete=off ></div>
       		 	 </div>
       		 	 <div class="form-group">
        	    <label class="col-sm-2 control-label">合同保管地址：</label>
        	    <div class="col-sm-5"><input class="form-control" id="store_place"></div>
       		 	 </div>
        	  </c:if>
        	  <c:if test="${taskDefKey == 'requestUserConfirm' }">
        	  <div class="form-group">
        	    <label class="col-sm-2 control-label">邮箱地址：</label>
        	    <div class="col-sm-10"><span class="detail-control">${emailVO.confirmAddress }</span></div>
       		  </div>
       		  <div class="form-group">
        	    <label class="col-sm-2 control-label">初始密码：</label>
        	    <div class="col-sm-10"><span class="detail-control">${emailVO.originalPassword }</span></div>
       		  </div>
       		  <div class="form-group">
        	    <label class="col-sm-2 control-label">登录网址：</label>
        	    <div class="col-sm-10"><span class="detail-control">${emailVO.loginUrl }</span></div>
       		  </div>
        	  </c:if>
			 
			  <c:if test="${taskDefKey == 'assignment_confirm' }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">开始时间：</label>
			  	<div class="col-sm-2">
			  		<input type="text" class="form-control" id="beginDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s'})">
			  	</div>
			  </div>
			  </c:if>
			  
			  <c:if test="${businessKey == 'Formal' and taskDefKey == 'supervisor_audit' }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">确认转正日期：</label>
			  	<div class="col-sm-2">
			  		<input type="text" class="form-control" id="actualFormalDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})" />
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey == 'Formal' and taskDefKey == 'manager_audit' }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">确认转正日期：</label>
			  	<div class="col-sm-2">
			  		<input type="text" class="form-control" id="actualFormalDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'})" />
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey == 'Resignation' and taskDefKey == 'supervisor_audit' }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">确认离职日期：</label>
			  	<div class="col-sm-2">
			  		<input type="text" class="form-control" id="confirmLeaveDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey == 'Resignation' and taskDefKey == 'manager_audit' }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">确认离职日期：</label>
			  	<div class="col-sm-2">
			  		<input type="text" class="form-control" id="confirmLeaveDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
			  	</div>
			  	<span class="col-sm-4" style="color:red;padding-top:14px;">离职日期以该时间为准，请慎重！</span>
			  </div>
			  </c:if>

			  <c:if test="${businessKey == 'Vacation' and attachmentSize > 0 }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">附件：</label>
			  	<div class="col-sm-5">
			  		<img src="personal/getVacationAttachment?taskID=${taskID }" alt="附件图片" id="photo" class="img-thumbnail" style="width:270px;height:410px;">
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey=='ContractSign' && fn:length(lawAttas)>0}">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">法务的审批或签名：</label>
			  	<div class="col-sm-5">
			  		<c:forEach items="${lawAttas}" var="lawAtta" varStatus="status">
			  		<a href="javascript:showPic('${lawAtta.name}',${lawAtta.id})" target="_self">
			  		<img src="personal/getAttachmentByAttachmentId?attachmentId=${lawAtta.id}" alt="附件图片" id="photo" class="img-thumbnail" style="width:100px;height:100px;">
			  		</a>
			  		</c:forEach>
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey=='ChangeContract' && fn:length(attas)>0}">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">法务的审批或签名：</label>
			  	<div class="col-sm-5">
			  		<c:forEach items="${attas}" var="atta" varStatus="status">
			  		<a href="javascript:showPic('${atta.name}',${atta.id})" target="_self">
			  		<img src="personal/getAttachmentByAttachmentId?attachmentId=${atta.id}" alt="附件图片" id="photo" class="img-thumbnail" style="width:100px;height:100px;">
			  		</a>
			  		</c:forEach>
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey=='ContractSign' && fn:length(contractAttas)>0}">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">合同封面：</label>
			  	
			  	<div class="col-sm-5">
			  		<c:forEach items="${contractAttas}" var="contractAtta">
			  		<a href="javascript:showPic('${contractAtta.name}', ${contractAtta.id})" target="_self">
			  		<img src="personal/getAttachmentByAttachmentId?attachmentId=${contractAtta.id}" alt="附件图片" id="photo" class="img-thumbnail" style="width:100px;height:100px;">
			  		</a>
			  		</c:forEach>
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey=='shopApply' and attachmentSize > 0}">
			  	<div class="form-group">
			  	<label class="col-sm-2 control-label">附件：</label>
			  	<div class="col-sm-5" style="margin-top:0.6%">
			  		<c:forEach items="${attas}" var="atta" varStatus="status">
			  			<a href="personal/getVacationAttachmentAll?taskID=${taskID}&index=${status.index}">${atta.name}</a>&nbsp;&nbsp;
			  		</c:forEach>
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${formFields[9].fieldValue=='是' && taskDefKey=='contractSign_financial_audit'}">
			  <div class="form-group">
			  	<label class="control-label col-sm-2">超出本季度预算：</label>
			  	<div class="col-sm-1" style="margin-top:5px">
			  		<input onclick="checkBudget('exceedSeason',this)" name="exceedSeason" checked type="radio" style="height:15px;width:15px" value="0">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
        	  		<input onclick="checkBudget('exceedSeason',this)" name="exceedSeason" type="radio" style="height:15px;width:15px" value="1">&nbsp;<label>是</label>
			  	</div>
			  	<div id="exceedSeason" class="col-sm-6" style="display:none">
			  		<label class="control-label col-sm-3">超出比例</label>
			  		<div class="col-sm-2">
			  			<input autocomplete="off" name="exceedSeasonRate" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control">
			  		</div>
			  		<div class="col-sm-1" style="margin-top: 6px">&nbsp;%</div>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="control-label col-sm-2">超出集团审定预算：</label>
			  	<div class="col-sm-1" style="margin-top:5px">
			  		<input onclick="checkBudget('exceedGroup',this)" name="exceedGroup" checked type="radio" style="height:15px;width:15px" value="0">&nbsp;<label style="margin-bottom:2px">否</label>&nbsp;&nbsp;&nbsp;
        	  		<input onclick="checkBudget('exceedGroup',this)" name="exceedGroup" type="radio" style="height:15px;width:15px" value="1">&nbsp;<label>是</label>
			  	</div>
			  	<div id="exceedGroup" class="col-sm-6" style="display:none">
			  		<label class="control-label col-sm-3">超出比例</label>
			  		<div class="col-sm-2">
			  			<input autocomplete="off" name="exceedGroupRate" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control">
			  		</div>
			  		<div class="col-sm-1" style="margin-top: 6px">&nbsp;%</div>
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${taskDefKey=='completeDestroy'}">
			  	<div class="form-group">
			  		<label class="col-sm-2 control-label">添加公安等部门出具的相关缴销文件<span style="color:red"> *</span>：</label>
			  		<div class="col-sm-5">
			  		<input type="file" required id="attachment" name="attachment"  style="padding:6px 0px;" multiple accept="image/gif,image/jpeg,image/jpg,image/png"/>
			  		</div>
			  	</div>
			  </c:if>
			  <c:if test="${taskDefKey=='budgetConfirm'}">
			  	<div class="form-group">
			  		<label class="col-sm-2 control-label">固定资产和低值易耗品分类</label>
			  		<div class="col-sm-3">
			  		<input name="propertyType" checked type="radio" style="height:15px;width:15px" value="0">&nbsp;<label>固定资产</label>&nbsp;&nbsp;&nbsp;
        	  		<input name="propertyType" type="radio" style="height:15px;width:15px" value="1">&nbsp;<label>低值易耗品</label>
			  		</div>
			  		<label class="col-sm-2 control-label">固定资产和低值易耗品编号</label>
			  		<div class="col-sm-3">
			  			<input autocomplete="off" name="propertyNum" class="form-control">
			  		</div>
			  	</div>
			  	<div class="form-group">
			  		<label class="col-sm-2 control-label">折旧年限</label>
			  		<div class="col-sm-2">
			  			<input autocomplete="off" name="useTime" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control">
			  		</div>
			  		<div class="col-sm-1" style="margin-top: 6px">&nbsp;年</div>
			  		<label class="col-sm-2 control-label">净残值率</label>
			  		<div class="col-sm-2">
			  			<input autocomplete="off" name="netSalvageRate" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control">
			  		</div>
			  		<div class="col-sm-1" style="margin-top: 6px">&nbsp;%</div>
			  	</div>
			  </c:if>
			  <c:if test="${taskDefKey=='purchaserConfirm'}">
			  	<div class="form-group">
			  		<label class="col-sm-2 control-label">品名<span style="color:red"> *</span></label>
			  		<div class="col-sm-3">
			  			<input autocomplete="off"  name="productName" class="form-control">
			  		</div>
			  		<label class="col-sm-2 control-label">规格型号<span style="color:red"> *</span></label>
			  		<div class="col-sm-2">
			  			<input autocomplete="off"  name="_model" class="form-control">
			  		</div>
			  	</div>
			  	<div class="form-group">
			  		<label class="col-sm-2 control-label">数量<span style="color:red"> *</span></label>
			  		<div class="col-sm-3">
			  			<input autocomplete="off" name="_number" class="form-control">
			  		</div>
			  		<label class="col-sm-2 control-label">单价<span style="color:red"> *</span></label>
			  		<div class="col-sm-2">
			  			<input autocomplete="off"  name="unitPrice" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control">
			  		</div>
			  		<div class="col-sm-1" style="margin-top: 6px">&nbsp;元</div>
			  	</div>
			  	<div class="form-group">
			  		<label class="col-sm-2 control-label">使用或采购部门验收意见</label>
			  		<div class="col-sm-2">
			  			<input name="purchaserCheckResult" checked type="radio" style="height:15px;width:15px" value="0">&nbsp;<label>合格</label>&nbsp;&nbsp;&nbsp;
        	  			<input name="purchaserCheckResult" type="radio" style="height:15px;width:15px" value="1">&nbsp;<label>不合格</label>
			  		</div>
			  	</div>
			  </c:if>
			  <c:if test="${taskDefKey=='financialOpenAccount' && shopApplyVo.applyType=='开店'}">
			  			<input type="hidden" name="shopType" value="${shopApplyVo.shopType}">
			  			<input type="hidden" name="applyType" value="${shopApplyVo.applyType}">
			  			<div class="form-group">
			  			<label class="col-sm-2 control-label">对公银行账号<c:if test="${shopApplyVo.shopType=='企业开店'}"><span style="color:red"> *</span></c:if></label>
				  		<div class="col-sm-3">
				  			<input autocomplete="off" id="publicBankAccount" name="publicBankAccount" class="form-control" onblur="checkBankAccount()">
				  		</div>
				  		<div class="error" id="bankErrorMsg" style="color:red;margin-top:1%;display:none">不符合规范</div>
				  		</div>
				  		<div class="form-group">
				  		<label class="col-sm-2 control-label"><c:if test="${shopApplyVo.shopType=='企业开店'}">企业</c:if>认证支付宝<span style="color:red"> *</span></label>
				  		<div class="col-sm-3">
				  			<input autocomplete="off" id="aliPayAccount" name="aliPayAccount" class="form-control" onblur="checkAlipayAccount('aliPayAccount')">
				  		</div>
				  		<div class="error" style="color:red;margin-top:1%;display:none">不符合规范</div>
				  		</div>
				  		<div class="form-group">
				  		<label class="col-sm-2 control-label">支付宝验证手机<span style="color:red"> *</span></label>
				  		<div class="col-sm-3">
				  			<input autocomplete="off"  type="number" id="aliPayPhone"  name="aliPayPhone" class="form-control" onblur="checkPhone('aliPayPhone')">
				  		</div>
				  		<div class="error" style="color:red;margin-top:1%;display:none">不符合规范</div>
				  		</div>
			  </c:if>
			  <c:if test="${businessKey=='ContractSign' || (businessKey=='bankAccount' && taskDefKey!='financialHandle') || businessKey=='ChangeContract' 
			  || (businessKey=='DestroyChop' && taskDefKey!='handOverChop' && taskDefKey!='completeDestroy') ||
			  (businessKey=='purchaseProperty' && taskDefKey!='budgetConfirm' && taskDefKey!='purchaserConfirm' && taskDefKey!='propertySign')
			  || businessKey=='CarveChop' || businessKey=='handleProperty' || (businessKey=='transferProperty' && taskDefKey!='completeTransfer')
			  || (businessKey=='shopApply' && taskDefKey!='financialOpenAccount' && taskDefKey!='completeHandle') || (businessKey=='shopPayApply' && taskDefKey!='financialHandle' && taskDefKey!='handleSuccess')}">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">审批意见：</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" name="comment" class="form-control" ></textarea>
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${businessKey!='ContractSign' && (businessKey!='bankAccount' || taskDefKey=='financialHandle') && businessKey!='ChangeContract'
			  && (businessKey!='DestroyChop' || taskDefKey=='handOverChop' || taskDefKey=='completeDestroy') &&
			  (businessKey!='purchaseProperty' || taskDefKey=='budgetConfirm' || taskDefKey=='purchaserConfirm' || taskDefKey=='propertySign')
			  && businessKey!='CarveChop' && businessKey!='handleProperty' && (businessKey!='transferProperty' || taskDefKey=='completeTransfer')
			  && (businessKey!='shopApply' || taskDefKey=='completeHandle' || taskDefKey=='financialOpenAccount') && (businessKey!='shopPayApply' || taskDefKey=='financialHandle' || taskDefKey=='handleSuccess')}">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">备注：</label>
			  	<div class="col-sm-5">
			  		<textarea id="comment" name="comment" class="form-control" ></textarea>
			  	</div>
			  </div>
			  </c:if>
			  <div class="form-group">
			  <c:if test="${taskDefKey == 'vacation_supervisor_audit' or taskDefKey == 'vacation_manager_audit' or taskDefKey == 'vacation_hr_audit'
			   or taskDefKey == 'supervisor_audit' or taskDefKey == 'manager_audit' or taskDefKey == 'financialSecondAudit' 
			   or taskDefKey == 'financialFirstAudit' or taskDefKey=='subject_apply' or taskDefKey=='chop_supervisor_audit' 
			   or taskDefKey=='chop_manager_audit' or taskDefKey=='certificate_supervisor_audit' or taskDefKey=='certificate_manager_audit'
			   or taskDefKey=='certificate_subject_apply' or taskDefKey=='contract_supervisor_audit' or taskDefKey=='contract_manager_audit'
			   or taskDefKey=='contract_subject_apply' or taskDefKey=='contractSign_supervisor_audit' or taskDefKey=='contractSign_financial_audit'
			   or taskDefKey=='contractSign_manager_audit' or taskDefKey=='contractSign_finalManager_audit' or taskDefKey=='changeContract_supervisor_audit'
			   or taskDefKey=='changeContract_manager_audit' or taskDefKey=='changeContract_finalManager_audit' or (businessKey=='bankAccount' && taskDefKey!='financialHandle') or taskDefKey=='destroyChop_supervisor_audit'
			   or taskDefKey=='destroyChop_manager_audit' or taskDefKey=='chopManagerAudit' or taskDefKey=='finalManagerAudit'
			   or taskDefKey=='purchaseProperty_supervisor_audit' or (businessKey=='purchaseProperty' && taskDefKey!='purchaserConfirm' && taskDefKey!='budgetConfirm'
			   && taskDefKey!='propertySign') or businessKey=='CarveChop' or businessKey=='handleProperty' or (businessKey=='transferProperty' && taskDefKey!='completeTransfer')
			   or (businessKey=='shopApply' && taskDefKey!='completeHandle' && taskDefKey!='financialOpenAccount') or (businessKey=='shopPayApply' && taskDefKey!='handleSuccess' && taskDefKey!='financialHandle')}">
			  	<button type="button" class="btn btn-primary" <s:if test="#request.businessKey == 'Formal' && #request.taskDefKey == 'supervisor_audit'">onclick="formalConfirm(2)"</s:if><s:if test="#request.businessKey == 'Formal' && #request.taskDefKey == 'manager_audit'">onclick="formalConfirm(2)"</s:if>
			  	<s:if test="#request.businessKey == 'Resignation' && #request.taskDefKey == 'supervisor_audit'">onclick="resignationConfirm(2)"</s:if>
			  	<s:if test="#request.businessKey == 'Resignation' && #request.taskDefKey == 'manager_audit'">onclick="resignationConfirm(2)"</s:if>
			  	<s:if test="#request.taskDefKey == 'contractSign_financial_audit'">onclick="contractSignFinancialConfirm(2)"</s:if>
			  	<s:else>onclick="taskComplete(2)"</s:else> style="margin-left:15px;">不同意</button>
			  	<button type="button" class="btn btn-primary" <s:if test="#request.businessKey == 'Formal' && #request.taskDefKey == 'supervisor_audit'">onclick="formalConfirm(1)"</s:if><s:if test="#request.businessKey == 'Formal' && #request.taskDefKey == 'manager_audit'">onclick="formalConfirm(1)"</s:if>
			  	<s:if test="#request.businessKey == 'Resignation' && #request.taskDefKey == 'supervisor_audit'">onclick="resignationConfirm(1)"</s:if>
			  	<s:if test="#request.businessKey == 'Resignation' && #request.taskDefKey == 'manager_audit'">onclick="resignationConfirm(1)"</s:if>
			  	<s:if test="#request.taskDefKey == 'contractSign_financial_audit'">onclick="contractSignFinancialConfirm(1)"</s:if>
			  	<s:else>onclick="taskComplete(1)"</s:else> style="margin-left:20px;">同意</button>
			  	<c:if test="${businessKey=='ChopBorrow' or businessKey=='ContractSign'
			  	or businessKey=='ChangeContract' or businessKey=='bankAccount' or
			  	businessKey=='DestroyChop' or businessKey=='purchaseProperty' or businessKey=='CarveChop' 
			  	or businessKey=='handleProperty' or businessKey=='transferProperty' or businessKey=='shopApply' or businessKey=='shopPayApply'}">
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			  	</c:if>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  
			 <c:if test="${taskDefKey=='contract_subject'}">
			  	<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="constractConfirm('${taskDefKey}',1)">同意</button>
			  	<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="constractConfirm('${taskDefKey}',2)">不同意</button>
			  </c:if>
			  
			  <c:if test="${taskDefKey=='car_use_superior_subject' or taskDefKey=='car_use_manage_subject' }">
			  <button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="taskComplete(1)">同意</button>
			  	<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="taskComplete(2)">不同意</button>
			  </c:if>
			  <c:if test="${taskDefKey=='contract_complete'}">
			  		<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="constractConfirm('${taskDefKey}',18)">签署完成</button>
			  </c:if>
			  <c:if test="${taskDefKey=='subject_apply_Id'}">
			  <!-- 身份证 -->
			  	<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="taskComplete(1)">同意并交付</button>
			  	<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="taskComplete(2)">不同意</button>
			  </c:if>
			  <c:if test="${taskDefKey=='chop_borrow' || taskDefKey=='certificate_borrow' || taskDefKey=='contract_borrow'}">
			  <!-- 公章 -->
			  	<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="taskComplete(1)">完成</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			  </c:if>
			   <c:if test="${taskDefKey=='chop_return' or taskDefKey=='return_id_card' or taskDefKey=='certificate_return' or taskDefKey=='contract_return'}">
			    <!-- 公章 -->
			  	<button type="button" class="btn btn-primary" style="margin-left:15px;" onclick="taskComplete(17)">已归还</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			  </c:if> 
			  <c:if test="${taskDefKey == 'assignment_confirm' }">
			  	<button type="button" class="btn btn-primary" onclick="confirmComplete(4)" style="margin-left:15px;">拒绝</button>
			  	<button type="button" class="btn btn-primary" onclick="confirmComplete(3)" style="margin-left:20px;">接收</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'assignment_modify' }">
			  	<button type="button" class="btn btn-primary" onclick="updateAssignment()" style="margin-left:20px;">修改任务</button>
			    <button type="button" class="btn btn-primary" onclick="taskComplete(6)" style="margin-left:15px;">关闭任务</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'assignment_do' }">
			  	<button type="button" class="btn btn-primary" onclick="taskComplete(7)" style="margin-left:20px;">交付</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'assignment_inspect' }">
			  	<button type="button" class="btn btn-primary" onclick="inspectAssignment()" style="margin-left:20px;">验收</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'updateAccount' }">
			  	<button type="button" class="btn btn-primary" onclick="updateAccount()" style="margin-left:20px;">修改</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'requestUserConfirm' }">
			  	<button type="button" class="btn btn-primary" onclick="requestUserConfirm()" style="margin-left:20px;">确认</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'requestUserConfirm'}">
			  	<button type="button" class="btn btn-primary" onclick="requestUserConfirm()" style="margin-left:20px;">确认</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'completeTransfer'}">
			  	<button type="button" class="btn btn-primary" onclick="completeTransfer()" style="margin-left:20px;">确认</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'handOverChop' }">
			  	<button type="button" class="btn btn-primary" onclick="handOverChop()" style="margin-left:20px;">印章交付</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'completeDestroy' }">
			  	<button type="submit" class="btn btn-primary" style="margin-left:20px;">完成缴销</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'budgetConfirm' || taskDefKey == 'purchaserConfirm'}">
			  	<button type="button" class="btn btn-primary" onclick="purchaseConfirm('${taskDefKey}')" style="margin-left:20px;">确认</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'propertySign'}">
			  	<button type="button" class="btn btn-primary" onclick="propertySign()" style="margin-left:20px;">签收</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'financialOpenAccount'}">
			  	<button type="button" class="btn btn-primary" onclick="financialOpenAccount()" style="margin-left:20px;">确认</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'completeHandle'}">
			  	<button type="button" class="btn btn-primary" onclick="completeHandle('shopApply')" style="margin-left:20px;">完成办理</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'financialHandle' && businessKey=='shopPayApply'}">
			  	<button type="button" class="btn btn-primary" onclick="completeHandle('shopPayApply')" style="margin-left:20px;">完成办理</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'financialHandle' && businessKey=='bankAccount'}">
			  	<button type="button" class="btn btn-primary" onclick="taskComplete('39')" style="margin-left:20px;">完成办理</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  <c:if test="${taskDefKey == 'handleSuccess'}">
			  	<button type="button" class="btn btn-primary" onclick="handleSuccess()" style="margin-left:20px;">确认办理成功</button>
			  	<button class="btn btn-primary" onclick="printOrder()" style="margin-left:20px;" >打印申请单</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </c:if>
			  </div>
			  
			  <h3 class="sub-header" style="margin-top:0px;">流程状态</h3>
			  <c:if test="${not empty finishedTaskVOs }">
			  <c:forEach items="${finishedTaskVOs }" var="task" varStatus="st">
			  <c:if test="${not empty task }">
			  <div class="form-group">
			  	<label class="col-sm-2 control-label">【${task.taskName }】</label>
			  	<div class="col-sm-10">
			  		<span class="detail-control">${task.assigneeName }（${task.endTime }）：${task.result}</span>
			  	</div>
			  	<c:if test="${not empty comments }">
        	  	<c:forEach items="${comments }" var="comment" varStatus="st">
        	  	<c:if test="${comment.taskID == task.taskID }">
        	  		<div class="col-sm-2"></div>
				  	<div class="col-sm-10">
				  		<span class="detail-control">${comment.content }</span>
				  	</div>
        	  	</c:if>
        	  	</c:forEach>
        	  	</c:if>
			  </div>
			  </c:if>
			  </c:forEach>
			  </c:if>
			</form>
        </div>
      </div>
    </div>
   <c:if test="${taskDefKey=='contract_complete'}">
   	 <script src="/assets/js/underscore-min.js"></script>
      <script>
      var staffInputBind=(function ($,_){
      	var staffInputBind=function(id){
      		this.id=id||_.uniqueId("staffInput");
  			this.$elem=null;
  			this.selector=null;
  			this.$wapper=null;
  			this.lastQueryName=null;
      	}
      	staffInputBind.prototype.render=function(selector,afterChoose){
      		this.$elem=$(selector);
      		this.selector=selector;
      		this.fn_afterChoose=afterChoose;
      		this.$elem.attr("data-id",this.id);
      		this.$elem.wrap("<span class='input_text1'></span>");
      		this.$elem.parent().after("<div class='text_down1  inputout1'><ul></ul></div>");
      		this.$elem.parent().after("<input name=\"name\" hidden></input><input name=\"id\" hidden></input><input name=\"detail\" hidden></input>")
      		this.$wapper=this.$elem.closest("div");
      		this.$elem.keyup(this.textChange.bind(this));
      		return  this;
      	}
      	staffInputBind.prototype.hide=function(){
      		this.$wapper.find("ul").parent().hide();
      	}
      	staffInputBind.prototype.textChange=function(){
      		var value=this.$elem.val();
      		if(!value)return;
      		if(this.lastQueryName){
      			if(this.lastQueryName===value){
      				return;
      			}else{
      				this.lastQueryName=value;
      				this.query.call(this,value);
      			}
      		}else{
      			this.lastQueryName=value;
  				this.query.call(this,value);
      		}
      	}
      	staffInputBind.prototype.queryCallback=function(data){
      		if(!_.isArray(data.staffVOs))return;
      		data.staffVOs=[].slice.call(data.staffVOs,0,100);
      		var resultHtml=_.chain(data.staffVOs).map(function(staff){
  	    		var  groupDetail = staff.groupDetailVOs[0];
      			return "<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>";
      		}).join("").value();
      		if(!resultHtml)return;
      		var $ul=this.$wapper.find("ul");
      		$ul.empty().append(resultHtml);
      		$ul.parent().show();
      		this.choose.call(this,$ul);
      	};
      	staffInputBind.prototype.choose=function($ul){
      		var this_=this;
      		$ul.find("li").bind("click",function(){
      			var value_arr=$(this).find("input").val().split("@");
      			this_.$wapper.find("input[name='id']").val(value_arr[0])
      			this_.$wapper.find("input[name='name']").val(value_arr[1]);
      			var index;
      			if(~(index=$(this).html().indexOf("<input"))){
      				var extraMsg=$(this).html().substring(0,index);
      				this_.$wapper.find("input[name='detail']").val(extraMsg)		
      			}
      		 	this_.$elem.val(value_arr[1]);
      			this_.lastQueryName=value_arr[1];
      			this_.$wapper.find("ul").parent().hide();
      			if(this_.fn_afterChoose){
      				this_.fn_afterChoose();
          		}
      		})
      		
      	};
      	staffInputBind.prototype.query=function(value){
      		var this_=this;
      		$.ajax({
      			url:'personal/findStaffByName',
      			type:'post',
      			data:{name:value},
      			dataType:'json',
      			success:function(data){
      				this_.queryCallback.call(this_,data);
      			}
      		});
      	};
      	return staffInputBind;
      })(jQuery,_)
  		var init=(function (){
  			new staffInputBind().render("#store_input");
  		})();
     </script>
    </c:if>
    <script type="text/javascript">
    	function printOrder(){
	  	var content=$('#printArea').html();
		$('body').empty().html(content);
		window.print();
		var href = location.href;
		var taskID = href.substring(href.lastIndexOf("=")+1, href.length);
		$("#taskID").val(taskID);
		window.location.reload();
	   	}
		var search=location.search;
		//假如参数为空  这种情况 不可能发生 除了 从 print 页跳转回来
		if(!search){
			location.href=localStorage.lastUrl;
		}
		localStorage.lastUrl=location.href;
		$(function(){
			var totalPay = '${resultMap.总计}';
			toUpperCase(totalPay);
		});
		//数字转大写
		function toUpperCase(number){
			var number_arr_=["零","壹","贰","叁","肆","伍","陆","柒","捌","玖"];
			var unit_arr1=["拾","佰","仟"];
			var unit_arr2=["万","亿","兆"];
			var index;
			if(~(index=number.indexOf("."))){
			    //只取到分
			    if(index+3<number.length){
			    	number=number.substring(0,index+3);
			    }else{
			        var num=2-(number.length-1-index);
			        //需要填充的 num的数量
			        for(var i=0;i<num;i++){
			        	number+="0";
			        }
			    }
			}else{
				number=number+".00";
			}
			var prev=number.substring(0,number.length-3);
			var tail=number.substring(number.length-2);
			var getTailHtml=function(){
				return "<span>"+number_arr_[tail[0]]+"</span>　角　" + "<span>"+number_arr_[tail[1]]+"</span>　分　";
			}
			var number_arr=[];
			var getPrevHtml=function(){
				for(var i=prev.length;i>=0;i-=4){
					var startIndex=(i-4>=0)?i-4:0;
					var endIndex=i;
					number_arr.push(prev.substring(startIndex,endIndex));
					if(startIndex==0)break;
				}
			}
			getPrevHtml();
			var resultHtml="";
			for(var i=0;i<number_arr.length;i++){
				if(i==0){
					var number_str=number_arr[i];
					for(var j=number_str.length-1,k=0;j>=0;j--,k++){
						var number_=number_str.charAt(j);
						if(k==0){
							resultHtml="<span>"+number_arr_[number_]+"</span>　元　"+resultHtml;				
						}else{
							resultHtml="<span>"+number_arr_[number_]+"</span>　"+unit_arr1[k-1]+"　"+resultHtml;				
						}
					}
				}else{
					var number_str=number_arr[i];
					for(var j=number_str.length-1,k=0;j>=0;j--,k++){
						var number_=number_str.charAt(j);
						if(k==0){
							resultHtml="<span>"+number_arr_[number_]+"</span>　"+unit_arr2[i-1]+"　"+resultHtml;						
						}else{
							resultHtml="<span>"+number_arr_[number_]+unit_arr1[k-1]+"</span>"+resultHtml;
							
						}
					}
				}
			}
			var upperCase = resultHtml+getTailHtml();
			$("#totalShopPay").html(upperCase);
		}
		function checkBankAccount(){
      		var accountNumber = $("#publicBankAccount").val();
      		if(accountNumber==""){
      			$("#bankErrorMsg").css("display","none");
      			return;
      		}
      		var reg = new RegExp(/^[0-9]{10,30}$/);
      		var result = reg.test(accountNumber);
      		if(!result){
      			$("#bankErrorMsg").css("display","block");
      		}else{
      			$("#bankErrorMsg").css("display","none");
      		}
      	}
      	//验证支付宝账号：邮箱或者手机号
      	function checkAlipayAccount(id){
      		var accountNumber = $("#"+id).val();
      		if(accountNumber==""){
      			$("#"+id).parent().next().css("display","none");
      			return;
      		}
      		var phoneReg = new RegExp(/^1\d{10}$/);
      		var phoneFlag = false;
      		var mailFlag = false;
      		if(phoneReg.test(accountNumber)){
      			phoneFlag = true;
      		}
      		var mailReg = new RegExp(/^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/);
      		if(mailReg.test(accountNumber)){
      			mailFlag = true;
      		}
      		//邮箱和手机号都不符合
      		if(!phoneFlag && !mailFlag){
      			$("#"+id).parent().next().css("display","block");
      		}else{
      			$("#"+id).parent().next().css("display","none");
      		}
      	}
      	function checkPhone(id){
      		var accountNumber = $("#"+id).val();
      		if(accountNumber==""){
      			$("#"+id).parent().next().css("display","none");
      			return;
      		}
      		var phoneReg = new RegExp(/^1\d{10}$/);
      		if(!phoneReg.test(accountNumber)){
      			$("#"+id).parent().next().css("display","block");
      		}else{
      			$("#"+id).parent().next().css("display","none");
      		}
      	}
      	
       	var showPic=function (name, id){
       		var picData={
   	       		start:0,
   	       		data:[]
       	    }
       		picData.data.push({alt:name, src:"personal/getAttachmentByAttachmentId?attachmentId="+id})
       		layer.photos({
       			offset: '50px',
       		    photos: picData
       		    ,anim: 5 
       		  });
       	}
	</script>
	<script src="/assets/js/layer/layer.js"></script>
</body>
</html>