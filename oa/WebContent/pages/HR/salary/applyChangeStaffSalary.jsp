<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){
		$("[data-toggle='tooltip']").tooltip();
		var deductionHtml = "";
		var html = "";
		<c:forEach items="${staffSalary.deductionItemMap}" var="item" varStatus="status">
		        deductionHtml += '<tr>'+
		        '<td><input class="form-control" name="deductionItem" value="${item.key}"></td>'+
		        '<td><input class="form-control" name="deductionMoney" value="${item.value}" oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,\'\');}).call(this)" onblur="this.v(),checkMoney(this)"></td>'+
				'<td>'+
				'<a href="javascript:void(0)" onclick="deleteOtherDeduction(this)">'+
				   '<svg class="icon" aria-hidden="true" title="删除" '+
						'data-toggle="tooltip">'+
					  '<use xlink:href="#icon-delete"></use>'+
				   '</svg>'+
				'</a>'+
				'</td>'+
				'</tr>';
				html += "<input name='deductionItem' value='${item.key}'>";
				html += "<input name='deductionMoney' value='${item.value}'>";
		</c:forEach>
		$("#deductions").append(deductionHtml);
		$("#applyInfoDeduction").html(html);
		var subsidyHtml = "";
		html = "";
		<c:forEach items="${staffSalary.subsidyItemMap}" var="item" varStatus="status">
				subsidyHtml += '<tr>'+
		        '<td><input class="form-control" name="subsidyItem" value="${item.key}"></td>'+
		        '<td><input class="form-control" name="subsidyMoney" value="${item.value}" oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,\'\');}).call(this)" onblur="this.v(),checkMoney(this)"></td>'+
				'<td>'+
				'<a href="javascript:void(0)" onclick="deleteOtherSubsidy(this)">'+
				   '<svg class="icon" aria-hidden="true" title="删除" '+
						'data-toggle="tooltip">'+
					  '<use xlink:href="#icon-delete"></use>'+
				   '</svg>'+
				'</a>'+
				'</td>'+
				'</tr>';
				html += "<input name='subsidyItem' value='${item.key}'>";	
				html += "<input name='subsidyMoney' value='${item.value}'>";
		</c:forEach>
		$("#subsidys").append(subsidyHtml);
		$("#applyInfoSubsidy").html(html);
	});
	function modifyOtherDeduction(){
		$("#otherDeduction").modal("show");
	}
	function modifyOtherSubsidy(){
		$("#otherSubsidy").modal("show");
	}
	function deleteOtherDeduction(obj){
		layer.confirm("确定删除？",{offset:'100px'},function(index){
			layer.close(index);
			$(obj).parent().parent().remove();
		});
	}
	function deleteOtherSubsidy(obj){
		layer.confirm("确定删除？",{offset:'100px'},function(index){
			layer.close(index);
			$(obj).parent().parent().remove();
		});
	}
	function checkMoney(obj){
		var num = $(obj).val();
		if(num.trim()){
			if(!/^[1-9]+[0-9]*$/.test(num.trim()) && !/^[1-9]+[0-9]*\.[0-9]{1,2}$/.test(num.trim()) && !/^0\.[0-9]{1,2}$/.test(num.trim())){
				layer.alert("金额输入不合法，小数最多两位",{offset:'100px'});
				$(obj).val("");
				return false;
			}
		}
		return true;
	}
	function addOtherDeduction(){
		$("#deductions").append($("#otherDeductionHtml").html());
		$("[data-toggle='tooltip']").tooltip();
	}
	function addOtherSubsidy(){
		$("#subsidys").append($("#otherSubsidyHtml").html());
		$("[data-toggle='tooltip']").tooltip();
	}
	function addDeductions(){
		var html = "";
		var flag = true;
		$("#deductions input[name='deductionItem']").each(function(){
			if(!$(this).val()){
				flag = false;
				return;
			}
			html += "<input name='deductionItem' value='"+$(this).val()+"'>";
		});
		if(!flag){
			layer.alert("扣除原因不可为空",{offset:'100px'});
			return;
		}
		var totalDeductionMoney = 0;
		$("#deductions input[name='deductionMoney']").each(function(){
			if(!$(this).val()){
				flag = false;
				return;
			}
			html += "<input name='deductionMoney' value='"+$(this).val()+"'>";
			totalDeductionMoney += parseFloat($(this).val().trim());
		});
		if(!flag){
			layer.alert("扣除金额不可为空",{offset:'100px'});
			return;
		}
		totalDeductionMoney = totalDeductionMoney.toFixed(2);
		if(totalDeductionMoney>0){
			$("#salaryOtherDeduction").text(totalDeductionMoney);
		}else{
			$("#salaryOtherDeduction").text("——");
		}
		$("#applyInfoDeduction").html(html);
		var otherSubsidyMoney = $("#salaryOtherSubsidy").text();
		if(otherSubsidyMoney=='——'){
			otherSubsidyMoney = 0; 
		}
		$.ajax({
			url:'HR/staffSalary/calAfterTaxSalary',
			data:{'staffSalaryId':'${staffSalaryId}','otherSubsidyMoney':otherSubsidyMoney,'totalDeductionMoney':totalDeductionMoney},
			success:function(data){
				$("#personalIncomeTax").text(data.tax);
				$("#afterTaxSalary").text(data.afterTaxSalary);
				$("#totalMoney").text(data.totalMoney);
			}
		});
		$("#otherDeduction").modal("hide");
	}
	function addSubsidys(){
		var html = "";
		var flag = true;
		$("#subsidys input[name='subsidyItem']").each(function(){
			if(!$(this).val()){
				flag = false;
				return;
			}
			html += "<input name='subsidyItem' value='"+$(this).val()+"'>";
		});
		if(!flag){
			layer.alert("补贴原因不可为空",{offset:'100px'});
			return;
		}
		var totalSubsidyMoney = 0;
		$("#subsidys input[name='subsidyMoney']").each(function(){
			if(!$(this).val()){
				flag = false;
				return;
			}
			html += "<input name='subsidyMoney' value='"+$(this).val()+"'>";
			totalSubsidyMoney += parseFloat($(this).val().trim());
		});
		if(!flag){
			layer.alert("补贴金额不可为空",{offset:'100px'});
			return;
		}
		totalSubsidyMoney = totalSubsidyMoney.toFixed(2);
		if(totalSubsidyMoney>0){
			$("#salaryOtherSubsidy").text(totalSubsidyMoney);
		}else{
			$("#salaryOtherSubsidy").text("——");
		}
		$("#applyInfoSubsidy").html(html);
		var otherDedutionMoney = $("#salaryOtherDeduction").text();
		if(otherDedutionMoney=='——'){
			otherDedutionMoney = 0; 
		}
		$.ajax({
			url:'HR/staffSalary/calAfterTaxSalary',
			data:{'staffSalaryId':'${staffSalaryId}','otherSubsidyMoney':totalSubsidyMoney,'totalDeductionMoney':otherDedutionMoney},
			success:function(data){
				$("#personalIncomeTax").text(data.tax);
				$("#afterTaxSalary").text(data.afterTaxSalary);
				$("#totalMoney").text(data.totalMoney);
			}
		});
		$("#otherSubsidy").modal("hide");
	}
	function checkHasChange(){
		var applyInfoDeduction = $("#applyInfoDeduction").html();
		var applyInfoSubsidy = $("#applyInfoSubsidy").html();
		if(!applyInfoDeduction && !applyInfoSubsidy){
			layer.alert("请先修改，再提交",{offset:'100px'});
			return false;
		}
		Load.Base.LoadingPic.FullScreenShow(null);
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
			<h3 class="sub-header" style="margin-top:0px;">修改工资单<span style="color:red;font-size:16px">（只可修改其它扣除项和其它补贴项）</span></h3> 	
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
					<td id="afterTaxSalary">${staffSalary.afterTaxSalary}</td>
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
					<td>
						<span id="salaryOtherDeduction">${staffSalary.otherDeduction==null ? '——':staffSalary.otherDeduction}</span>
						<a href="javascript:modifyOtherDeduction()">
			    		   <svg class="icon" aria-hidden="true" title="更改"
								data-toggle="tooltip" style="width: 15px">
							  <use xlink:href="#icon-modify"></use>
						   </svg>
						</a>
					</td>
				</tr>
				<tr>
					<td>其它补贴项</td>
					<td>
						<span id="salaryOtherSubsidy">${staffSalary.otherSubsidy==null ? '——':staffSalary.otherSubsidy}</span>
						<a href="javascript:modifyOtherSubsidy()">
			    		   <svg class="icon" aria-hidden="true" title="更改"
								data-toggle="tooltip" style="width: 15px">
							  <use xlink:href="#icon-modify"></use>
						   </svg>
						</a>
					</td>
					<td>合计</td>
					<td id="totalMoney" colspan="3">${staffSalary.totalMoney}</td>
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
					<td id="personalIncomeTax">${staffSalary.personalIncomeTax}</td>
				</tr>
				<tr>
					<td>合计</td>
					<td colspan="5">${staffSalary.totalDeduction}</td>
				</tr>
			</table>
			</div>
			<br>
			<form action="/HR/staffSalary/save_startApplyChangeStaffSalary" onsubmit="return checkHasChange()" method="post">
			<s:token></s:token>
			<div style="text-align:center">
			<button type="submit" class="btn btn-primary">提交</button>
			<button type="button" style="margin-left:5%" class="btn btn-default" onclick="history.go(-1)">返回</button>
			<div style="display:none" id="applyInfoDeduction"></div>	
			<div style="display:none" id="applyInfoSubsidy"></div> 
			<input type="hidden" name="staffSalaryId" value="${staffSalaryId}">
			</div> 
			</form>
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
				<div style="text-align:right"><button type="button" class="btn btn-primary" onclick="addOtherDeduction()"><span class="glyphicon glyphicon-plus"></span>扣除项</button></div>
				<table style="margin-top:5px">
					<tbody id="deductions">
					<tr>
						<td style="width:50%">扣除原因<span style="color:red"> *</span></td>
						<td style="width:30%">扣除金额<span style="color:red"> *</span></td>
						<td style="width:20%">操作</td>
					</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-primary" onclick="addDeductions()">确认</button>
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
				<div style="text-align:right"><button type="button" class="btn btn-primary" onclick="addOtherSubsidy()"><span class="glyphicon glyphicon-plus"></span>补贴项</button></div>
				<table style="margin-top:5px">
					<tbody id="subsidys">
					<tr>
						<td style="width:50%">补贴原因<span style="color:red"> *</span></td>
						<td style="width:30%">补贴金额<span style="color:red"> *</span></td>
						<td style="width:20%">操作</td>
					</tr>
					</tbody>
				</table>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-primary" onclick="addSubsidys()">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<script type="text/html" id="otherDeductionHtml">
					<tr>
						<td><input class="form-control" name="deductionItem"></td>
						<td><input class="form-control" name="deductionMoney" oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"></td>
						<td>
						<a href="javascript:void(0)" onclick="deleteOtherDeduction(this)">
			    		   <svg class="icon" aria-hidden="true" title="删除"
								data-toggle="tooltip">
							  <use xlink:href="#icon-delete"></use>
						   </svg>
						</a>
						</td>
					</tr>
	</script>
	<script type="text/html" id="otherSubsidyHtml">
					<tr>
						<td><input class="form-control" name="subsidyItem"></td>
						<td><input class="form-control" name="subsidyMoney" oninput="(this.v=function(){this.value=this.value.replace(/[^0-9\.]+/,'');}).call(this)" onblur="this.v(),checkMoney(this)"></td>
						<td>
						<a href="javascript:void(0)" onclick="deleteOtherSubsidy(this)">
			    		   <svg class="icon" aria-hidden="true" title="删除"
								data-toggle="tooltip">
							  <use xlink:href="#icon-delete"></use>
						   </svg>
						</a>
						</td>
					</tr>
	</script>
</body>
</html>