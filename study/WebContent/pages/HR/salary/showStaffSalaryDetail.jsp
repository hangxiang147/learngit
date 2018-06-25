<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){
		$("[data-toggle='tooltip']").tooltip();
	});
	function showDedutionDetail(){
		$("#otherDeduction").modal("show");
	}
	function showSubsidyDetail(){
		$("#otherSubsidy").modal("show");
	}
	function showRemark(){
		var remarks = [];
		<c:forEach items="${staffSalary.remarkList}" var="remark">
			remarks.push('${remark}');
		</c:forEach>
		layer.alert(remarks.join(","),{offset:'100px'});
	}
</script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;word-break:break-all !important}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;text-align:center}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
	.title {color:#000;background:#efefef}
	.icon {
	width: 1.5em;
	height: 1.5em;
	vertical-align: -0.35em;
	fill: currentColor;
	overflow: hidden;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<%@include file="/pages/attendance/salayPanel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h3 class="sub-header" style="margin-top:0px;">工资单详情<button style="float:right" class="btn btn-default" onclick="history.go(-1)">返回</button></h3> 	
			<div class="tab">
			<table>
				<tr>
					<td style="width:15%">姓名</td>
					<td style="width:15%">${staffName}</td>
					<td style="width:15%">部门</td>
					<td style="width:15%">${depName}</td>
					<td style="width:15%">工号</td>
					<td style="width:15%">${staffSalary.staffNum}</td>
				</tr>
				<tr>
					<td>员工状态</td>
					<td>${staffSalary.staffStatus}</td>
					<td>薪资标准</td>
					<td>${staffSalary.standardSalary}</td>
					<td>税后工资</td>
					<td>${staffSalary.afterTaxSalary}</td>
				</tr>
				<tr><td class="title" colspan="6">出勤</td></tr>
				<tr>
					<td>应出勤天数</td>
					<td>${staffSalary.attendanceDays}</td>
					<td>实际出勤天数</td>
					<td colspan="3">
						${staffSalary.actualAttendanceDays}
						<c:if test="${!empty staffSalary.remarkList}">
						    <a href="javascript:showRemark()">
				    		   <svg class="icon" aria-hidden="true" title="查看明细"
									data-toggle="tooltip" style="width: 15px">
								  <use xlink:href="#icon-Detailedinquiry"></use>
							   </svg>
							</a>
						</c:if>
					</td>
				</tr>
				<tr><td class="title" colspan="6">工资明细</td></tr>
				<tr>
					<td>基础工资</td>
					<td>${staffSalary.basicSalary}</td>
					<td>绩效奖金</td>
					<td>${staffSalary.performanceSalary}</td>
					<td>晚上加班小时</td>
					<td>${staffSalary.nightOvertimeHours==null ? '——':staffSalary.nightOvertimeHours}</td>
				</tr>
				<tr>
					<td>白天加班时长</td>
					<td>${staffSalary.dayOvertimeHours==null ? '——':staffSalary.dayOvertimeHours}</td>
					<td>加班工资</td>
					<td>${staffSalary.overtimeSubsidy==null ? '——':staffSalary.overtimeSubsidy}</td>
					<td>满勤</td>
					<td>${staffSalary.fullAttendance}</td>
				</tr>
				<tr>
					<td>抵减小时</td>
					<td>${staffSalary.deductibleHours==null ? '——':staffSalary.deductibleHours}</td>
					<td>应抵减金额</td>
					<td>${staffSalary.deductibleMoney==null ? '——':staffSalary.deductibleMoney}</td>
					<td>未刷卡次数</td>
					<td>${staffSalary.noPunchTimes}</td>
				</tr>
				<tr>
					<td>未刷卡扣除</td>
					<td>${staffSalary.noPunchMoney}</td>
					<td>迟到次数</td>
					<td>${staffSalary.lateTimes}</td>
					<td>迟到分钟</td>
					<td>${staffSalary.lateMinutes}</td>
				</tr>
				<tr>
					<td>迟到扣款</td>
					<td>${staffSalary.lateMoney==null ? '——':staffSalary.lateMoney}</td>
					<td>未发工作日报次数</td>
					<td>${staffSalary.noSendReportTimes==null ? '——':staffSalary.noSendReportTimes}</td>
					<td>未发日报罚款</td>
					<td>${staffSalary.noSendReportMoney==null ? '——':staffSalary.noSendReportMoney}</td>
				</tr>
				<tr>
					<td>奖励</td>
					<td>${staffSalary.reward==null ? '——':staffSalary.reward}</td>
					<td>行政处罚</td>
					<td>${staffSalary.penalty==null ? '——':staffSalary.penalty}</td>
					<td>其它扣除项</td>
					<td>
						${staffSalary.otherDeduction==null ? '——':staffSalary.otherDeduction}
						<c:if test="${staffSalary.otherDeduction!=null}">
						<a href="javascript:showDedutionDetail()">
			    		   <svg class="icon" aria-hidden="true" title="查看明细"
								data-toggle="tooltip" style="width: 15px">
							  <use xlink:href="#icon-Detailedinquiry"></use>
						   </svg>
						</a>
						</c:if>
					</td>
				</tr>
				<tr>
					<td>其它补贴项</td>
					<td>
						${staffSalary.otherSubsidy==null ? '——':staffSalary.otherSubsidy}
						<c:if test="${staffSalary.otherSubsidy!=null}">
						<a href="javascript:showSubsidyDetail()">
			    		   <svg class="icon" aria-hidden="true" title="查看明细"
								data-toggle="tooltip" style="width: 15px">
							  <use xlink:href="#icon-Detailedinquiry"></use>
						   </svg>
						</a>
						</c:if>
					</td>
					<td>合计</td>
					<td colspan="3">${staffSalary.totalMoney}</td>
				</tr>
				<tr><td class="title" colspan="6">应扣项目</td></tr>
				<tr>
					<td>养老保险</td>
					<td>${staffSalary.pension==null ? '——':staffSalary.pension}</td>
					<td>医保</td>
					<td>${staffSalary.medicalInsurance==null ? '——':staffSalary.medicalInsurance}</td>
					<td>失业</td>
					<td>${staffSalary.unemployment==null ? '——':staffSalary.unemployment}</td>
				</tr>
				<tr>
					<td>大病</td>
					<td>${staffSalary.seriousIllness==null ? '——':staffSalary.seriousIllness}</td>
					<td>住房公积金</td>
					<td>${staffSalary.publicFund==null ? '——':staffSalary.publicFund}</td>
					<td>个税</td>
					<td>${staffSalary.personalIncomeTax}</td>
				</tr>
				<tr>
					<td>合计</td>
					<td colspan="5">${staffSalary.totalDeduction}</td>
				</tr>
			</table>
			</div>	  
        </div>
      </div>
    </div>
    <div id="otherDeduction" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:40%">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">扣除项</h4>
			</div>
			<div class="modal-body tab">
				<table>
					<tbody id="deductions">
					<tr>
						<td style="width:50%">扣除原因</td>
						<td style="width:30%">扣除金额</td>
					</tr>
				<c:forEach items="${staffSalary.deductionItemMap}" var="item" varStatus="status">
		        <tr>
		        <td>${item.key}</td>
		        <td>${item.value}</td>
				</tr>
		</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<div id="otherSubsidy" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:40%">
		<form  class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">补贴项</h4>
			</div>
			<div class="modal-body tab">
				<table>
				<tbody id="subsidys">
				<tr>
					<td style="width:50%">补贴原因</td>
					<td style="width:30%">补贴金额</td>
				</tr>
				<c:forEach items="${staffSalary.subsidyItemMap}" var="item" varStatus="status">
				<tr>
		        <td>${item.key}</td>
		        <td>${item.value}</td>
				</tr>
				</c:forEach>
				</tbody>
				</table>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
</body>
</html>