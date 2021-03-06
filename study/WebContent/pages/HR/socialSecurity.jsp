<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">

	$(function() {
		var date=new Date;
		var year=date.getFullYear(); 
		var month=date.getMonth()+1;
		if ($("#ss_year").val().length <= 0) {
			//社保默认审核当月明细
			$("#ss_year").val(year);
			$("#ss_month").val(month);
		}
		if ($("#hf_year").val().length <= 0) {
			//公积金默认审核上个月的明细
			if (month == 1) {
				year = year-1;
				month = 12;
			} else {
				month = month-1;
			}
			$("#hf_year").val(year);
			$("#hf_month").val(month);
		}
		 
		findSocialSecurityByTime();
		findHousingFundByTime();
	});

	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function deleteSocialSecurity(ssID) {
		if (confirm("确认删除该员工公积金明细？")) {
			$.ajax({
				url:'HR/staffInfoAlteration/deleteSocialSecurity',
				type:'post',
				data:{ssID: ssID},
				dataType:'json',
				success:function (data){
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						window.location.href = "HR/staff/error?panel=infoAlteration&selectedPanel=newSocialSecurity&errorMessage="+encodeURI(encodeURI(data.errorMessage));
						return false;
					}
					
					if (confirm("删除成功！")) {
						var ssYear = $("#ss_year").val();
						var ssMonth = $("#ss_month").val();
						var hfYear = $("#hf_year").val();
						var hfMonth = $("#hf_month").val();
						window.location.href = "HR/process/newSocialSecurity?ssYear="+ssYear+"&ssMonth="+ssMonth+"&hfYear="+hfYear+"&hfMonth="+hfMonth;
					}
				}
			});
		}
	}
	
	function deleteHousingFund(hfID) {
		if (confirm("确认删除该员工社保明细？")) {
			$.ajax({
				url:'HR/staffInfoAlteration/deleteHousingFund',
				type:'post',
				data:{hfID: hfID},
				dataType:'json',
				success:function (data){
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						window.location.href = "HR/staff/error?panel=infoAlteration&selectedPanel=newSocialSecurity&errorMessage="+encodeURI(encodeURI(data.errorMessage));
						return false;
					}
					
					if (confirm("删除成功！")) {
						var ssYear = $("#ss_year").val();
						var ssMonth = $("#ss_month").val();
						var hfYear = $("#hf_year").val();
						var hfMonth = $("#hf_month").val();
						window.location.href = "HR/process/newSocialSecurity?ssYear="+ssYear+"&ssMonth="+ssMonth+"&hfYear="+hfYear+"&hfMonth="+hfMonth;
					}
				}
			});
		}
	}
	
	function findSocialSecurityByTime() {
		var year = $("#ss_year").val();
		var month = $("#ss_month").val();
		$.ajax({
			url:'HR/staffInfoAlteration/findSocialSecurityByTime',
			type:'post',
			data:{year: year,
				  month: month},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					window.location.href = "HR/staff/error?panel=infoAlteration&selectedPanel=socialSecurityList&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					return false;
				}
				
				$("#social_security").empty();
				var gender, idType, hasPaid;
				$.each(data.housingFundVOs, function(i, housingFundVO) {
					if (housingFundVO.gender == 1) {
						gender = "男";
					} else {
						gender = "女";
					}
					if (housingFundVO.idType == 1) {
						idType = "身份证";
					} else {
						idType = "";
					}
					if (housingFundVO.hasPaid == 1) {
						hasPaid = "是";
					} else {
						hasPaid = "否";
					}
					$("#social_security").append("<tr><td>"+(i+1)+"</td><td>"+housingFundVO.userName+"</td><td>"+housingFundVO.entryDate+"</td><td>"+housingFundVO.formalDate+"</td><td>"
							+gender+"</td><td>"+housingFundVO.idNumber+"</td><td>"+idType+"</td><td>"+hasPaid+"</td><td>"
							+housingFundVO.companyCount+"</td><td>"+housingFundVO.personalCount+"</td><td>"+housingFundVO.totalCount+"</td><td>"
							+housingFundVO.note+"</td><td>"
							+"<a href='HR/staffInfoAlteration/updateHousingFund?hfID="+housingFundVO.hfID+"' class='fcr'>修改</a>&nbsp;&nbsp;"
							+"<a href='javascript:void(0)' class='fcr' onclick='return deleteHousingFund("+housingFundVO.hfID+");'>删除</a></td></tr>");
				});
				$("#ssTotalCount").text(data.ssTotalCount);
				$("#ssTotalCount_submit").val(data.ssTotalCount);
			}
		});
	}
	
	function findHousingFundByTime() {
		var year = $("#hf_year").val();
		var month = $("#hf_month").val();
		$.ajax({
			url:'HR/staffInfoAlteration/findHousingFundByTime',
			type:'post',
			data:{year: year,
				  month: month},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					window.location.href = "HR/staff/error?panel=infoAlteration&selectedPanel=socialSecurityList&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					return false;
				}
				
				$("#housing_fund").empty();
				var idType;
				$.each(data.socialSecurityVOs, function(i, socialSecurity) {
					if (socialSecurity.idType == 1) {
						idType = "身份证";
					} else {
						idType = "";
					}
					$("#housing_fund").append("<tr><td>"+(i+1)+"</td><td>"+socialSecurity.userName+"</td><td>"+idType+"</td><td>"+socialSecurity.idNumber+"</td><td>"
							+socialSecurity.basePay+"</td><td>"+socialSecurity.selfPaidRatio+"%</td><td>"+socialSecurity.reason+"</td><td>"+socialSecurity.personalProvidentFund+"</td><td>"
							+socialSecurity.companyProvidentFund+"</td><td>"+socialSecurity.totalProvidentFund+"</td><td>"
							+"<a href='HR/staffInfoAlteration/updateSocialSecurity?ssID="+socialSecurity.ssID+"' class='fcr'>修改</a>&nbsp;&nbsp;"
							+"<a href='javascript:void(0)' class='fcr' onclick='return deleteSocialSecurity("+socialSecurity.ssID+");'>删除</a></td></tr>");
				});
				$("#hfPersonalCount").text(data.hfPersonalCount);
				$("#hfCompanyCount").text(data.hfCompanyCount);
				$("#hfTotalCount").text(data.hfTotalCount);
				$("#personalCount_submit").val(data.hfPersonalCount);
				$("#companyCount_submit").val(data.hfCompanyCount);
				$("#totalCount_submit").val(data.hfTotalCount);
			}
		});
	}

	function addSocialSecurity() {
		var companyID = $("#companyID").val();
		var year = $("#ss_year").val();
		var month = $("#ss_month").val();
		var hf_year = $("#hf_year").val();
		var hf_month = $("#hf_month").val();
		window.location.href = "HR/process/addHousingFund?year="+year+"&month="+month+"&companyID="+companyID+"&panel=infoAlteration&hfYear="+hf_year+"&hfMonth="+hf_month;
	}
	
	function addHousingFund() {
		var companyID = $("#companyID").val();
		var year = $("#hf_year").val();
		var month = $("#hf_month").val();
		var ss_year = $("#ss_year").val();
		var ss_month = $("#ss_month").val();
		window.location.href = "HR/process/addSocialSecurity?year="+year+"&month="+month+"&companyID="+companyID+"&panel=infoAlteration&ssYear="+ss_year+"&ssMonth="+ss_month;
	}
	
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="HR/process/save_startSocialSecurity" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
			  <div class="fz20 mt20">社会保险费申报表（参保）</div>
			  <input type="hidden" id="companyID" value="${companyID }" />
			  <div class="filter mt10 clearfix">
				<div class="fl"><a href="javascript:void(0);" onclick="addSocialSecurity()" class="oa_btn btn_s bg_red plr20">添加人员名单</a></div>
				<div class="fr clearfix">
					<span class="fl">缴纳时间：</span>
				  <span class="fl">
				  <select name="socialSecurityProcessVO.ssYear" id="ss_year" class="select" onchange="findSocialSecurityByTime()">
					<option value="">--请选择--</option>
					<option value="2014" <c:if test="${ssYear == 2014 }">selected="selected"</c:if>>2014</option>
					<option value="2015" <c:if test="${ssYear == 2015 }">selected="selected"</c:if>>2015</option>
					<option value="2016" <c:if test="${ssYear == 2016 }">selected="selected"</c:if>>2016</option>
					<option value="2017" <c:if test="${ssYear == 2017 }">selected="selected"</c:if>>2017</option>
					<option value="2018" <c:if test="${ssYear == 2018 }">selected="selected"</c:if>>2018</option>
					<option value="2019" <c:if test="${ssYear == 2019 }">selected="selected"</c:if>>2019</option>
					<option value="2020" <c:if test="${ssYear == 2020 }">selected="selected"</c:if>>2020</option>
				  </select>年</span>
				  <span class="fl ml10"><select name="socialSecurityProcessVO.ssMonth" id="ss_month" class="select" onchange="findSocialSecurityByTime()">
					<option value="">--请选择--</option>
					<option value="1" <c:if test="${ssMonth == 1 }">selected="selected"</c:if>>1</option>
					<option value="2" <c:if test="${ssMonth == 2 }">selected="selected"</c:if>>2</option>
					<option value="3" <c:if test="${ssMonth == 3 }">selected="selected"</c:if>>3</option>
					<option value="4" <c:if test="${ssMonth == 4 }">selected="selected"</c:if>>4</option>
					<option value="5" <c:if test="${ssMonth == 5 }">selected="selected"</c:if>>5</option>
					<option value="6" <c:if test="${ssMonth == 6 }">selected="selected"</c:if>>6</option>
					<option value="7" <c:if test="${ssMonth == 7 }">selected="selected"</c:if>>7</option>
					<option value="8" <c:if test="${ssMonth == 8 }">selected="selected"</c:if>>8</option>
					<option value="9" <c:if test="${ssMonth == 9 }">selected="selected"</c:if>>9</option>
					<option value="10" <c:if test="${ssMonth == 10 }">selected="selected"</c:if>>10</option>
					<option value="11" <c:if test="${ssMonth == 11 }">selected="selected"</c:if>>11</option>
					<option value="12" <c:if test="${ssMonth == 12 }">selected="selected"</c:if>>12</option>
				  </select>月</span>
				  <span class="fl ml20">合计：<font id="ssTotalCount" class="fcr">1191元</font></span>
				  <input type="hidden" name="socialSecurityProcessVO.ssTotalCount" id="ssTotalCount_submit" />
				</div>
			  </div>
			  
			  <div class="mt20">
				<table class="fundtab">
					<thead>
						<tr>
							<td>序号</td>
							<td>姓名</td>
							<td>入职时间</td>
							<td>转正时间</td>
							<td>性别</td>
							<td>身份证明号码</td>
							<td>证件名称</td>
							<td>是否参保过</td>
							<td>单位合计</td>
							<td>个人合计</td>
							<td>应缴金额</td>
							<td>备注</td>
							<td width="100">操作</td>
						</tr>
					</thead>
					<tbody id="social_security">
					</tbody>
				</table>
			  </div>
			  
			  <div id="title" class="fz20 mt20">住房公积金汇缴明细表</div>
			  <div class="filter mt10 clearfix">
				<div class="fl"><a href="javascript:void(0);" onclick="addHousingFund()" class="oa_btn btn_s bg_red plr20">添加人员名单</a></div>
				<div class="fr clearfix">
					<span class="fl">缴纳时间：</span>
				  <span class="fl">
				  <select name="socialSecurityProcessVO.year" id="hf_year" class="select" onchange="findHousingFundByTime()">
				    <option value="">--请选择--</option>
					<option value="2014" <c:if test="${hfYear == 2014 }">selected="selected"</c:if>>2014</option>
					<option value="2015" <c:if test="${hfYear == 2015 }">selected="selected"</c:if>>2015</option>
					<option value="2016" <c:if test="${hfYear == 2016 }">selected="selected"</c:if>>2016</option>
					<option value="2017" <c:if test="${hfYear == 2017 }">selected="selected"</c:if>>2017</option>
					<option value="2018" <c:if test="${hfYear == 2018 }">selected="selected"</c:if>>2018</option>
					<option value="2019" <c:if test="${hfYear == 2019 }">selected="selected"</c:if>>2019</option>
					<option value="2020" <c:if test="${hfYear == 2020 }">selected="selected"</c:if>>2020</option>
				  </select>年</span>
				  <span class="fl ml10"><select name="socialSecurityProcessVO.month" id="hf_month" class="select" onchange="findHousingFundByTime()">
					<option value="">--请选择--</option>
					<option value="1" <c:if test="${hfMonth == 1 }">selected="selected"</c:if>>1</option>
					<option value="2" <c:if test="${hfMonth == 2 }">selected="selected"</c:if>>2</option>
					<option value="3" <c:if test="${hfMonth == 3 }">selected="selected"</c:if>>3</option>
					<option value="4" <c:if test="${hfMonth == 4 }">selected="selected"</c:if>>4</option>
					<option value="5" <c:if test="${hfMonth == 5 }">selected="selected"</c:if>>5</option>
					<option value="6" <c:if test="${hfMonth == 6 }">selected="selected"</c:if>>6</option>
					<option value="7" <c:if test="${hfMonth == 7 }">selected="selected"</c:if>>7</option>
					<option value="8" <c:if test="${hfMonth == 8 }">selected="selected"</c:if>>8</option>
					<option value="9" <c:if test="${hfMonth == 9 }">selected="selected"</c:if>>9</option> 
					<option value="10" <c:if test="${hfMonth == 10 }">selected="selected"</c:if>>10</option> 
					<option value="11" <c:if test="${hfMonth == 11 }">selected="selected"</c:if>>11</option>
					<option value="12" <c:if test="${hfMonth == 12 }">selected="selected"</c:if>>12</option>
				  </select>月</span>
				  <span class="fl ml20">个人部分：<font id="hfPersonalCount" class="fcr">${personalCount }</font>&nbsp;&nbsp;&nbsp;单位部分：<font id="hfCompanyCount" class="fcr">${companyCount }</font>&nbsp;&nbsp;&nbsp;合计：<font id="hfTotalCount" class="fcr">${totalCount }</font></span>
				  <input type="hidden" name="socialSecurityProcessVO.personalCount" id="personalCount_submit" />
				  <input type="hidden" name="socialSecurityProcessVO.companyCount" id="companyCount_submit" />
				  <input type="hidden" name="socialSecurityProcessVO.totalCount" id="totalCount_submit" />
				  <!-- 提交审核的操作人 -->
				  <input type="hidden" id="userID" name="socialSecurityProcessVO.userID" value="${user.id}"/>
				  <input type="hidden" id="userName" name="socialSecurityProcessVO.userName" value="${userName }" />
				</div>
			  </div>
			  
			  <div class="mt20">
			  	<table class="fundtab">
				<thead> 
					<tr> 
						<td rowspan="2" width="50">序号</td>
						<td rowspan="2" width="80">职工姓名</td>
						<td rowspan="2" width="100">证件类型</td>
						<td rowspan="2" width="12%">证件号码</td>
						<td rowspan="2" width="80">缴存基数</td>
						<td rowspan="2" width="80">个人缴存比例(%)	</td>
						<td rowspan="2">增加原因</td>
						<td colspan="3">住房公积金月缴存额（元）</td>
						<td rowspan="2" width="100">操作</td>
					</tr>
			        <tr>
						<td width="100">个人部分</td>
						<td width="100">单位部分</td>
						<td width="100">合 计</td>
					</tr>
				</thead>
				<tbody id="housing_fund">
				<c:if test="${not empty socialSecurityVOs}">
				<c:set var="ss_id" value="0"></c:set>
				<c:forEach items="${socialSecurityVOs}" var="socialSecurityVO">
					<tr>
              			<td>${ss_id+1}</td>
              			<td>${socialSecurityVO.userName}</td>
						<td><c:if test="${socialSecurityVO.idType==1 }">身份证</c:if></td>
						<td>${socialSecurityVO.idNumber}</td>
						<td>${socialSecurityVO.basePay}</td>
						<td>${socialSecurityVO.selfPaidRatio}%</td>
						<td>${socialSecurityVO.reason}</td>
						<td>${socialSecurityVO.personalProvidentFund}</td>
						<td>${socialSecurityVO.companyProvidentFund}</td>
						<td>${socialSecurityVO.totalProvidentFund}</td>
						<td><a href="HR/staffInfoAlteration/updateSocialSecurity?ssID=${socialSecurityVO.ssID}" class="fcr">修改</a>&nbsp;&nbsp;<a href="javascript:void(0)" class="fcr" onclick="return deleteSocialSecurity('${socialSecurityVO.ssID}');">删除</a></td>
              		</tr>
              		<c:set var="ss_id" value="${ss_id+1}"></c:set>
				</c:forEach>
				</c:if>
				</tbody>
				</table>
			  </div>
			  <div class="mt20 tc"><button type="submit" class="oa_btn btn_s bg_blue plr20" >提交审核</button>
			  <a href="javascript:void(0);" onclick="location.href='javascript:history.go(-1);'" class="oa_btn btn_s bg_999 plr20">返回</a>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>