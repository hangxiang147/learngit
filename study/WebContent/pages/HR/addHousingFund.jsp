<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">

	$(function() {		
		
		$('body').on('click','.input_text',function (event) { 
			if($(".text_down ul").html() != ""){
			$(".text_down").show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down ul li',function () {
			var shtml=$(this).html();
			$(".text_down").hide();
			$("#staff").val(shtml.split("（")[0]);
			$("#staffFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#staffID").val(agent.split("@")[0]);
			$("#staffName").val(agent.split("@")[1]);
			});
		}); 
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#staff").val()!=$("#staffFlag").val()) {$("#staff").val("");}});  
		$('.inputout').click(function (event) {$(".text_down").show();});
		
	});

	function findStaffByName() {
		var name = $("#staff").val();
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
</script>
<style type="text/css">
	.inputout{position:relative;}
	.text_down{position:absolute;top:30px;left:0px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down ul{padding:2px 10px;}
	.text_down ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down ul li span{color:#cc0000;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <c:if test="${panel != 'process' }"><s:set name="panel" value="'infoAlteration'"></s:set></c:if>
      <c:if test="${panel == 'process' }"><s:set name="panel" value="'process'"></s:set></c:if>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="<c:if test="${panel == 'process' }">HR/process/save_saveHousingFund</c:if><c:if test="${panel != 'process' }">HR/staffInfoAlteration/save_addHousingFund</c:if>"
        	      method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
			    <s:token></s:token>
			    <div class="fz20"><c:if test="${not empty housingFundVO }">修改社保明细<input type="hidden" name="housingFundVO.hfID" value="${housingFundVO.hfID }" /></c:if>
			    				  <c:if test="${empty housingFundVO }">添加社保名单</c:if></div>
				<div class="mt10 bdt_gray pt20">
				<table class="form_tabA">
				  <tbody>
				    <tr>
				      <td width="140">职工姓名：</td>
				      <td>
				      	<div class="inputout">
				    		<span class="input_text">
				    			<input id="staff" type="text" class="input" oninput="findStaffByName()" value="${housingFundVO.userName }" required="required" placeholder="请输入职工姓名"/>
				    			<input type="hidden" id="staffFlag" value="${housingFundVO.userName }"/>
			    				<input type="hidden" id="staffID" name="housingFundVO.userID" value="${housingFundVO.userID }" />
			    				<input type="hidden" id="staffName" name="housingFundVO.userName" value="${housingFundVO.userName }" />
			    				<input type="hidden" name="housingFundVO.paymentYear" value="${housingFundVO.paymentYear }"/>
			    				<input type="hidden" name="housingFundVO.paymentMonth" value="${housingFundVO.paymentMonth }" />
			    				<input type="hidden" name="housingFundVO.processID" value="${housingFundVO.processID }" />
			    				<input type="hidden" name="year" value="${year }" />
			    				<input type="hidden" name="month" value="${month }" />
			    				<input type="hidden" name="taskID" value="${taskID }" />
			    				<input type="hidden" name="housingFundVO.companyID" value="${housingFundVO.companyID }" />
			    				<input type="hidden" name="companyID" value="${companyID }" />
			    				<input type="hidden" name="hfYear" value="${hfYear }" />
			    				<input type="hidden" name="hfMonth" value="${hfMonth }" />
				    		</span>
				    		<div class="text_down">
								<ul></ul>
							</div>
				    	</div>
				      </td>
					  <td width="110">性别：</td>
				      <td><input type="radio" name="housingFundVO.gender" id="gender" value="1" required="required" <c:if test="${housingFundVO.gender == 1 }">checked="checked"</c:if>/>男
					  <input type="radio" name="housingFundVO.gender" id="gender" value="2" required="required" <c:if test="${housingFundVO.gender == 2 }">checked="checked"</c:if>/>女</td>
				    </tr>
					<tr>
				      <td>入职时间：</td>
				      <td><input id="entryDate" name="housingFundVO.entryDate" value="${housingFundVO.entryDate }" type="text" class="input" onclick="WdatePicker()" placeholder="请输入入职时间"/></td>
					  <td>转正时间：</td>
				      <td><input id="formalDate" name="housingFundVO.formalDate" value="${housingFundVO.formalDate }" type="text" class="input" onclick="WdatePicker()" placeholder="请输入转正时间"/></td>
				    </tr>
					<tr>
				      <td>证件类型：</td>
				      <td>
				      	<select id="idType" name="housingFundVO.idType" class="select" required="required" style="width:162px;">
					      <option value="">请选择</option>
						  <option value="1" <c:if test="${housingFundVO.idType == 1}">selected="selected"</c:if>>身份证</option>
						</select>
				      </td>
					  <td>身份证明号码：</td>
				      <td><input id="idNumber" name="housingFundVO.idNumber" value="${housingFundVO.idNumber }" type="text" class="input" placeholder="请输入身份证明号码" required="required"/></td>
				    </tr>
					<tr>
				      <td>是否参保过：</td>
				      <td><input type="radio" name="housingFundVO.hasPaid" id="hasPaid" value="1" required="required" <c:if test="${housingFundVO.hasPaid == 1 }">checked="checked"</c:if>/>是
				      <input type="radio" name="housingFundVO.hasPaid" id="hasPaid" value="0" required="required" <c:if test="${housingFundVO.hasPaid == 0 }">checked="checked"</c:if>/>否</td>
					  <td>单位合计：</td>
				      <td><input id="companyCount" name="housingFundVO.companyCount" value="${housingFundVO.companyCount }" type="text" class="input" placeholder="请输入单位缴纳金额" required="required"/></td>
				    </tr>
					<tr>
					  <td>个人合计：</td>
				      <td><input id="personalCount" name="housingFundVO.personalCount" value="${housingFundVO.personalCount }" type="text" class="input" placeholder="请输入个人缴纳金额" required="required"/></td>
					  <td>应缴金额：</td>
				      <td><input id="totalCount" name="housingFundVO.totalCount" value="${housingFundVO.totalCount }" type="text" class="input" placeholder="请输入应缴金额" required="required"/></td>
				    </tr>
					<tr>
					  <td>备注：</td>
				      <td colspan="3"><textarea name="housingFundVO.note" id="note" cols="45" rows="2" class="w80p textarea">${housingFundVO.note }</textarea></td>
				    </tr>
				  </tbody>
				</table>
				</div>
			    
				<div class="mt30 tc"><input type="submit" id="tijiao" value="保存" class="oa_btn btn_s bg_blue plr20"/>
				<a href="javascript:void(0);" onclick="location.href='javascript:history.go(-1);'" class="oa_btn btn_s bg_999 plr20">返回</a></div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>