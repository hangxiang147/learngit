<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
function showPic(id) {
	var picData = {
		start : 0,
		data : []
	}
	picData.data.push({
		alt : name,
		src : "/administration/notice/downloadPic?id="+id,
	})
	layer.photos({
		offset : '10%',
		photos : picData,
		anim : 5,
	});
}
</script>
<style type="text/css">
	.tab tr th, .tab tr td{
		word-wrap:break-word;
		word-break:break-all;
		font-size:10px;
		padding:8px 7px;
		text-align:center;
		border:1px solid #ddd
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<h3 class="sub-header" style="margin-top:0px;">薪资调整明细
        		<button type="button" style="float:right" onclick="history.go(-1)" class="btn btn-default">返回</button>
        	</h3>
        	<h5 style="font-weight:bold">调薪前>></h5>
        		<table  class="tab" style="width:95%">
					<tr>
						<td>标准工资</td>
						<td style="width:15%">${staffSalaryALteration.beforeSalary.standardSalary}</td>
						<td>基本工资</td>
						<td style="width:15%">${staffSalaryALteration.beforeSalary.basicSalary}</td>
						<td>绩效工资</td>
						<td style="width:15%">${staffSalaryALteration.beforeSalary.performanceSalary}</td>
					</tr>
					<tr>
						<td>满勤</td>
						<td>${staffSalaryALteration.beforeSalary.fullAttendance}</td>
						<td>保险缴纳基数</td>
						<td>${staffSalaryALteration.beforeSalary.socialSecurityBasic==null ? '——':staffSalaryALteration.beforeSalary.socialSecurityBasic}</td>
						<td>养老</td>
						<td>${staffSalaryALteration.beforeSalary.pension==null ? '——':staffSalaryALteration.beforeSalary.pension}</td>
					</tr>
					<tr>
						<td>医保</td>
						<td>${staffSalaryALteration.beforeSalary.medicalInsurance==null ? '——':staffSalaryALteration.beforeSalary.medicalInsurance}</td>
						<td>失业</td>
						<td>${staffSalaryALteration.beforeSalary.unemployment==null ? '——':staffSalaryALteration.beforeSalary.unemployment}</td>
						<td>大病</td>
						<td>${staffSalaryALteration.beforeSalary.seriousIllness==null ? '——':staffSalaryALteration.beforeSalary.seriousIllness}</td>
					</tr>
					<tr>
						<td>个人缴纳保险</td>
						<td>${staffSalaryALteration.beforeSalary.personalPay==null ? '——':staffSalaryALteration.beforeSalary.personalPay}</td>
						<td>公司缴纳保险</td>
						<td>${staffSalaryALteration.beforeSalary.companyPay==null ? '——':staffSalaryALteration.beforeSalary.companyPay}</td>
						<td>公积金缴纳基数</td>
						<td>${staffSalaryALteration.beforeSalary.publicfundBasic==null ? '——':staffSalaryALteration.beforeSalary.publicfundBasic}</td>
					</tr>
					<tr>
						<td colspan="2">个人缴纳公积金</td>
						<td>${staffSalaryALteration.beforeSalary.personalPayFund==null ? '——':staffSalaryALteration.beforeSalary.personalPayFund}</td>
						<td colspan="2">公司缴纳公积金</td>
						<td>${staffSalaryALteration.beforeSalary.companyPayFund==null ? '——':staffSalaryALteration.beforeSalary.companyPayFund}</td>
					</tr>
					<c:forEach items="${staffSalaryALteration.beforeSalary.itemAndValMap}" var="item" varStatus="status">
						<c:if test="${status.index%3==0}"><tr></c:if>
						<td>${item.key}</td>
						<td>${item.value==null ? '——':item.value}</td>
					</c:forEach>
				</table>	
        	<h5 style="font-weight:bold">调薪后>></h5>
        		 <table  class="tab" style="width:95%">
					<tr>
						<td>标准工资</td>
						<td style="width:15%" ${staffSalaryALteration.afterSalary.standardSalary!=staffSalaryALteration.beforeSalary.standardSalary ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.standardSalary}</td>
						<td>基本工资</td>
						<td style="width:15%" ${staffSalaryALteration.afterSalary.basicSalary!=staffSalaryALteration.beforeSalary.basicSalary ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.basicSalary}</td>
						<td>绩效工资</td>
						<td style="width:15%" ${staffSalaryALteration.afterSalary.performanceSalary!=staffSalaryALteration.beforeSalary.performanceSalary ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.performanceSalary}</td>
					</tr>
					<tr>
						<td>满勤</td>
						<td ${staffSalaryALteration.afterSalary.fullAttendance!=staffSalaryALteration.beforeSalary.fullAttendance ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.fullAttendance}</td>
						<td>保险缴纳基数</td>
						<td ${staffSalaryALteration.afterSalary.socialSecurityBasic!=staffSalaryALteration.beforeSalary.socialSecurityBasic ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.socialSecurityBasic==null ? '——':staffSalaryALteration.afterSalary.socialSecurityBasic}</td>
						<td>养老</td>
						<td ${staffSalaryALteration.afterSalary.pension!=staffSalaryALteration.beforeSalary.pension ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.pension==null ? '——':staffSalaryALteration.afterSalary.pension}</td>
					</tr>
					<tr>
						<td>医保</td>
						<td ${staffSalaryALteration.afterSalary.medicalInsurance!=staffSalaryALteration.beforeSalary.medicalInsurance ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.medicalInsurance==null ? '——':staffSalaryALteration.afterSalary.medicalInsurance}</td>
						<td>失业</td>
						<td ${staffSalaryALteration.afterSalary.unemployment!=staffSalaryALteration.beforeSalary.unemployment ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.unemployment==null ? '——':staffSalaryALteration.afterSalary.unemployment}</td>
						<td>大病</td>
						<td ${staffSalaryALteration.afterSalary.seriousIllness!=staffSalaryALteration.beforeSalary.seriousIllness ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.seriousIllness==null ? '——':staffSalaryALteration.afterSalary.seriousIllness}</td>
					</tr>
					<tr>
						<td>个人缴纳保险</td>
						<td ${staffSalaryALteration.afterSalary.personalPay!=staffSalaryALteration.beforeSalary.personalPay ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.personalPay==null ? '——':staffSalaryALteration.afterSalary.personalPay}</td>
						<td>公司缴纳保险</td>
						<td ${staffSalaryALteration.afterSalary.companyPay!=staffSalaryALteration.beforeSalary.companyPay ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.companyPay==null ? '——':staffSalaryALteration.afterSalary.companyPay}</td>
						<td>公积金缴纳基数</td>
						<td ${staffSalaryALteration.afterSalary.publicfundBasic!=staffSalaryALteration.beforeSalary.publicfundBasic ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.publicfundBasic==null ? '——':staffSalaryALteration.afterSalary.publicfundBasic}</td>
					</tr>
					<tr>
						<td colspan="2">个人缴纳公积金</td>
						<td ${staffSalaryALteration.afterSalary.personalPayFund!=staffSalaryALteration.beforeSalary.personalPayFund ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.personalPayFund==null ? '——':staffSalaryALteration.afterSalary.personalPayFund}</td>
						<td colspan="2">公司缴纳公积金</td>
						<td ${staffSalaryALteration.afterSalary.companyPayFund!=staffSalaryALteration.beforeSalary.companyPayFund ? "style='color:red'":""}>${staffSalaryALteration.afterSalary.companyPayFund==null ? '——':staffSalaryALteration.afterSalary.companyPayFund}</td>
					</tr>
					<c:forEach items="${staffSalaryALteration.afterSalary.itemAndValMap}" var="item" varStatus="status">
						<c:if test="${status.index%3==0}"><tr></c:if>
						<td>${item.key}</td>
						<td>${item.value==null ? '——':item.value}</td>
					</c:forEach>
				</table>
				<label style="font-weight:bold;margin-top:5px">生效时间：</label><span>${staffSalaryALteration.effectDate}</span>
				<c:if test="${not empty staffSalaryALteration.attaIds}">
					<div><span style="font-weight:bold">附件：</span>
						<c:forEach items="${staffSalaryALteration.attaIds}" var="attaId">
						<img onclick="showPic(${attaId})"
						style="cursor: pointer; width:100px;"
						src="/administration/notice/downloadPic?id=${attaId}">
						</c:forEach>
					</div>
				</c:if>
		</div>
      </div>
    </div>
</body>
</html>