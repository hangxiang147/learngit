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
        	<form action="<c:if test="${panel == 'process' }">HR/process/save_saveSocialSecurity</c:if><c:if test="${panel != 'process' }">HR/staffInfoAlteration/save_addSocialSecurity</c:if>"
        	 method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
			    <s:token></s:token>
			    <div class="fz20"><c:if test="${not empty socialSecurityVO }">修改公积金明细<input type="hidden" name="socialSecurityVO.ssID" value="${socialSecurityVO.ssID }" /></c:if>
			    				  <c:if test="${empty socialSecurityVO }">添加公积金名单</c:if></div>
				<div class="fz18 mt10 pt20 bdt_gray">基本信息</div>
				<div class="mt10">
				<table class="form_tabA">
				  <tbody>
				    <tr>
				      <td width="140">职工姓名：</td>
				      <td>
					      <div class="inputout">
				    		<span class="input_text">
				    			<input id="staff" type="text" class="input" oninput="findStaffByName()" value="${socialSecurityVO.userName }" required="required" placeholder="请输入职工姓名"/>
				    			<input type="hidden" id="staffFlag" value="${socialSecurityVO.userName }"/>
			    				<input type="hidden" id="staffID" name="socialSecurityVO.userID" value="${socialSecurityVO.userID }" />
			    				<input type="hidden" id="staffName" name="socialSecurityVO.userName" value="${socialSecurityVO.userName }" />
			    				<input type="hidden" name="socialSecurityVO.paymentYear" value="${socialSecurityVO.paymentYear }"/>
			    				<input type="hidden" name="socialSecurityVO.paymentMonth" value="${socialSecurityVO.paymentMonth }" />
			    				<input type="hidden" name="socialSecurityVO.processID" value="${socialSecurityVO.processID }" />
			    				<input type="hidden" name="year" value="${year }" />
			    				<input type="hidden" name="month" value="${month }" />
			    				<input type="hidden" name="taskID" value="${taskID }" />
			    				<input type="hidden" name="socialSecurityVO.companyID" value="${socialSecurityVO.companyID }" />
			    				<input type="hidden" name="companyID" value="${companyID }" />
			    				<input type="hidden" name="ssYear" value="${ssYear }" />
			    				<input type="hidden" name="ssMonth" value="${ssMonth }" />
				    		</span>
				    		<div class="text_down">
								<ul></ul>
							</div>
				    	  </div>
				      </td>
					  <td width="110">证件类型：</td>
				      <td>
				      	<select id="idType" name="socialSecurityVO.idType" class="select" required="required" style="width:162px;">
					      <option value="">请选择</option>
						  <option value="1" <c:if test="${socialSecurityVO.idType == 1}">selected="selected"</c:if>>身份证</option>
						</select>
				      </td>
				    </tr>
					<tr>
				      <td>证件号码：</td>
				      <td><input id="idNumber" name="socialSecurityVO.idNumber" value="${socialSecurityVO.idNumber }" type="text" class="input" placeholder="请输入证件号码" required="required" /></td>
					  <td>缴存基数：</td>
				      <td><input id="basePay" name="socialSecurityVO.basePay" value="${socialSecurityVO.basePay }" type="text" class="input" placeholder="请输入缴存基数" required="required" /></td>
				    </tr>
					<tr>
				      <td>个人缴存比例(%)：</td>
				      <td><input id="selfPaidRatio" name="socialSecurityVO.selfPaidRatio" value="${socialSecurityVO.selfPaidRatio }" type="text" class="input" placeholder="请输入个人缴存比例" required="required" /></td>
					  <td>&nbsp;</td>
				      <td>&nbsp;</td>
				    </tr>
					<tr>
					  <td>增加原因：</td>
				      <td colspan="3"><textarea name="socialSecurityVO.reason" id="reason" cols="45" rows="2" class="w80p textarea">${socialSecurityVO.reason }</textarea></td>
				    </tr>
				  </tbody>
				</table>
				</div>
				<div class="fz18 mt10">住房公积金月缴存额（元）</div>
				<div class="mt10">
				<table class="form_tabA">
				  <tbody>
				  <tr>
				      <td width="140">个人部分：</td>
				      <td><input id="personalProvidentFund" name="socialSecurityVO.personalProvidentFund" value="${socialSecurityVO.personalProvidentFund }" type="text" class="input" placeholder="请输入个人部分金额" required="required" /></td>
					  <td width="110">单位部分：</td>
				      <td><input id="companyProvidentFund" name="socialSecurityVO.companyProvidentFund" value="${socialSecurityVO.companyProvidentFund }" type="text" class="input" placeholder="请输入单位部分金额" required="required" /></td>
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