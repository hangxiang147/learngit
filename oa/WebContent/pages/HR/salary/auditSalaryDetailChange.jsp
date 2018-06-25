<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="assets/js/layer/layer.js"></script>
<script type="text/javascript">
function completeTask(result){
	var comment = $("#comment").val();
	if(result==2 && !comment.trim()){
		layer.alert("审批意见不可为空",{offset:'100px'});
		return;
	}
	location.href = "HR/staffSalary/completeTaskForChangeSalary?taskId=${taskId}&result="+result+"&comment="+comment;
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;word-break:break-all !important}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;text-align:center}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
	.title {color:#000;background:#efefef}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
		<%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<h3 class="sub-header" style="margin-top:0px;">工资单更改审批<span style="color:red;font-size:16px">（仅限其它扣除项和其它补贴项）</span>
        	</h3>
        	<div class="tab">
			<table>
				<tr>
					<td style="width:15%">姓名</td>
					<td style="width:15%">${staffSalary.staffName}</td>
					<td style="width:15%">部门</td>
					<td style="width:15%">${staffSalary.depName}</td>
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
					<td colspan="3">${staffSalary.actualAttendanceDays}</td>
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
					<td>${staffSalary.otherDeduction==null ? '——':staffSalary.otherDeduction}</td>
				</tr>
				<tr>
					<td>其它补贴项</td>
					<td>${staffSalary.otherSubsidy==null ? '——':staffSalary.otherSubsidy}</td>
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
			<br>
			<form class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-1 control-label" style="width:10%">修改前：</label>
				<div class="col-sm-1 control-label" style="text-align:left;width:12%">其它扣除项>></div>
				<div class="col-sm-5 control-label" style="text-align:left">
					<c:if test="${empty staffSalary.oldDeductionItemMap}">
						无
					</c:if>
					<c:if test="${not empty staffSalary.oldDeductionItemMap}">
					<div class="tab">
					<table>
					<tr>
					<td style="width:70%">扣除原因</td>
					<td>扣除金额</td>
					</tr>
					<c:forEach items="${staffSalary.oldDeductionItemMap}" var="deductionItem">
						<tr>
						<td>${deductionItem.key}</td>
						<td>${deductionItem.value}</td>
						</tr>
					</c:forEach>
					</table>
					</div>
					</c:if>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" style="width:10%"></label>
				<div class="col-sm-2 control-label" style="text-align:left;width:12%">其它补贴项>></div>
				<div class="col-sm-5 control-label" style="text-align:left">
					<c:if test="${empty staffSalary.oldSubsidyItemMap}">
						无
					</c:if>
					<c:if test="${not empty staffSalary.oldSubsidyItemMap}">
					<div class="tab">
					<table>
					<tr>
					<td style="width:70%">补贴原因</td>
					<td>补贴金额</td>
					</tr>
					<c:forEach items="${staffSalary.oldSubsidyItemMap}" var="subsidyItem">
						<tr>
						<td>${subsidyItem.key}</td>
						<td>${subsidyItem.value}</td>
						</tr>
					</c:forEach>
					</table>
					</div>
					</c:if>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" style="width:10%">修改后：</label>
				<div class="col-sm-2 control-label" style="text-align:left;width:12%">其它扣除项>></div>
				<div class="col-sm-5 control-label" style="text-align:left">
					<c:if test="${empty staffSalary.deductionItemMap}">
						无
					</c:if>
					<c:if test="${not empty staffSalary.deductionItemMap}">
					<div class="tab">
					<table>
					<tr>
					<td style="width:70%">扣除原因</td>
					<td>扣除金额</td>
					</tr>
					<c:forEach items="${staffSalary.deductionItemMap}" var="deductionItem">
						<tr>
						<td>${deductionItem.key}</td>
						<td>${deductionItem.value}</td>
						</tr>
					</c:forEach>
					</table>
					</div>
					</c:if>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" style="width:10%"></label>
				<div class="col-sm-2 control-label" style="text-align:left;width:12%">其它补贴项>></div>
				<div class="col-sm-5 control-label" style="text-align:left">
					<c:if test="${empty staffSalary.subsidyItemMap}">
						无
					</c:if>
					<c:if test="${not empty staffSalary.subsidyItemMap}">
					<div class="tab">
					<table>
					<tr>
					<td style="width:70%">补贴原因</td>
					<td>补贴金额</td>
					</tr>
					<c:forEach items="${staffSalary.subsidyItemMap}" var="subsidyItem">
						<tr>
						<td>${subsidyItem.key}</td>
						<td>${subsidyItem.value}</td>
						</tr>
					</c:forEach>
					</table>
					</div>
					</c:if>
				</div>
			</div>
			<div class="form-group">
				<label class="col-sm-1 control-label" style="width:10%">审批意见：</label>
				<div class="col-sm-5">
					<textarea class="form-control" rows="4" id="comment"></textarea>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-1" style="width:10%"></div>
				<div class="col-sm-5">
				<button type="button" onclick="completeTask(1)" class="btn btn-primary">同意</button>
				<button type="button" style="margin-left:2%" onclick="completeTask(2)" class="btn btn-primary">不同意</button>
				<button type="button" style="margin-left:2%" onclick="history.go(-1)" class="btn btn-default">返回</button>
				</div>
			</div>
			</form>
		</div>
      </div>
    </div>
</body>
</html>