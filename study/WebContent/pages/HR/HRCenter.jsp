<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@  taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
		$(function(){
			set_href();
			$("[data-toggle='tooltip']").tooltip();
			var type = $("#type").val();
			var tabShowLink={
					1:0,
					30:1,
					3:2,
					4:3,
					31:4,
					32:5,
					8:6,
					36:7,
					10:8
			}
			for(key in tabShowLink){
				if(type== key){
					$("#myTab li:eq("+tabShowLink[key]+") a").tab("show");
				}
				$("#myTab li:eq("+tabShowLink[key]+") a").attr("index",key).click(function(e) {
					window.location.href = "HRCenter/goHRCenter?type="+$(this).attr("index");
					Load.Base.LoadingPic.FullScreenShow(null);
				});
			} 
		});
		function showBirthStaffs(birthStaffs){
			var staffs = [];
			<c:forEach items="${birthStaffs}" var="staff">
				staffs.push('${staff.staffName}');
			</c:forEach>
			layer.alert(staffs.join(",&nbsp;"),{offset:'100px'});
		}
		function showAnniversaryStaffs(anniversaryStaffs){
			var staffs = [];
			<c:forEach items="${anniversaryStaffs}" var="staff">
				staffs.push('${staff.staffName}');
			</c:forEach>
			layer.alert(staffs.join(",&nbsp;"),{offset:'100px'});
		}
		function showContractListPartyBName(contractList){
			var staffs = [];
			<c:forEach items="${contractList }" var="staff">
				staffs.push('${staff.partyB}');
			</c:forEach>
			layer.alert(staffs.join(",&nbsp;"),{offset:'100px'});
		}
		function completeHandle(type, id){
			if(type=="年检"){
				$("#yearInspection").modal("show");
				$("input[name='vehicleId']").val(id);
			}else{
				layer.confirm("确定办理完成？", {offset:'100px'}, function(index){
					location.href = 'administration/vehicle/completeHandle?handleType='+type+'&vehicleId='+id;
					layer.close(index);
					Load.Base.LoadingPic.FullScreenShow(null);
				}); 
			}
		}
		function saveVehicleYearInspection(){
			$("#yearInspection").modal("hide");
			Load.Base.LoadingPic.FullScreenShow(null);
		}
		function showVacationUsers(users){
			users = users.substring(1, users.length-1);
			layer.alert(users, {title:'请假人员', offset:'100px'});
		}
		function auditTask(taskId){
			layer.confirm("<label class='col-sm-4'>审批意见：</label><div class='col-sm-8'><input class='form-control' id='comment'/></div>",{title:'审批',offset:'100px',btn:['同意', '不同意'],btnAlign:'c'},
			function(index){
				layer.close(index);
				location = 'HR/process/taskComplete?taskID='+taskId+'&result=1&businessType=日报查看申请&comment='+$("#comment").val();
				Load.Base.LoadingPic.FullScreenShow(null);
			}, function(index){
				layer.close(index);
				location = 'HR/process/taskComplete?taskID='+taskId+'&result=2&businessType=日报查看申请&comment='+$("#comment").val();
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}
		function showEntryingStaffNames(obj){
			var staffNames = [];
			<c:forEach items="${entryingStaffs}" var="staff">
				staffNames.push('${staff}');
			</c:forEach>
			$(obj).attr("data-original-title", staffNames.join(","));
		}
		function showQuitingStaffNames(obj){
			var staffNames = [];
			<c:forEach items="${quitingStaffs}" var="staff">
				staffNames.push('${staff.staffName}');
			</c:forEach>
			$(obj).attr("data-original-title", staffNames.join(","));
		}
	</script>
<style type="text/css">
.box {
	display: inline-block;
	height: 40px;
	line-height: 40px;
	width: 13%;
	border: 1px solid #827f7f;
	font-size: 14px;
	font-weight: bold;
	text-align: center;
	margin: 0 1%;
}

.title {
	font-size: 14px;
	font-weight: bold;
	color: #929090;
}

.chartTitle {
	font-size: 12px;
	font-weight: bold;
	color: #5f5b5b;
}

.icon {
	width: 1.5em;
	height: 1.5em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
}

.badge {
	-webkit-transform: scale(0.8);
	margin-top: -3px;
}

.table {
	font-size: 13px;
}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'HRCenter'"></s:set>
			<s:set name="selectedPanel" value="'HRCenter'"></s:set>
			<%@include file="/pages/HR/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<div class="title">员工基础数据>></div>
				<div style="margin: 1% 1%">
					<div class="box">
						员工总数<span style="color: red">${totalStaffNum}人</span>
					</div>
					<div class="box">
						试用期<span style="color: red">${probationNum}人</span>
					</div>
					<div class="box" onmouseover="showQuitingStaffNames(this)" data-toggle="tooltip" data-placement="bottom">
						待离职<span style="color: red">${fn:length(quitingStaffs)}人</span>
					</div>
					<div class="box">
						正式职工<span style="color: red">${formalStaffNum}人</span>
					</div>
					<div class="box" onmouseover="showEntryingStaffNames(this)" data-toggle="tooltip" data-placement="bottom">
						待入职<span style="color: red">${fn:length(entryingStaffs)}人</span>
					</div>
				</div>
				<div class="title">图表数据分析>></div>
				<div style="margin: 1% 2%">
					<table style="width: 45%">
						<tr>
							<td class="chartTitle hand" onclick="goPath('HRCenter/showStaffsInJobAnalysis')">在职员工分析</td>
							<td style="width:15%"></td>
							<td class="chartTitle hand" onclick="goPath('HRCenter/showLeaveStaffsAnalysis')">离职员工分析</td>
							<td style="width:15%"></td>
							<td class="chartTitle hand">销售薪资数据分析<span style="color:red">（待定）</span></td>
						</tr>
						<tr>
							<td class="hand" onclick="goPath('HRCenter/showStaffsInJobAnalysis')"><img src="assets/images/chart1.png"></td>
							<td style="width:15%"></td>
							<td class="hand" onclick="goPath('HRCenter/showLeaveStaffsAnalysis')"><img src="assets/images/chart2.png"></td>
							<td style="width:15%"></td>
							<td class="hand"><img src="assets/images/chart3.png"></td>
						</tr>
					</table>
				</div>
				<div class="title">人事提醒>></div>
				<div style="margin: 1% 2%">
					<c:if test="${not empty birthStaffs}">
						<div style="font-weight: bold; font-size: 12px">
							本月有<span style="font-size: 15px">${fn:length(birthStaffs)}</span>名同事生日&nbsp;
							<a href="javascript:showBirthStaffs('${birthStaffs}')"> <svg
									class="icon" aria-hidden="true" title="查看人员明细"
									data-toggle="tooltip" style="width: 15px">
								<use xlink:href="#icon-Detailedinquiry"></use>
							</svg>
							</a>
						</div>
					</c:if>
					<c:if test="${not empty anniversaryStaffs}">
					<div style="font-weight: bold; font-size: 12px">
						本月有<span style="font-size: 15px">${fn:length(anniversaryStaffs)}</span>名员工入职周年庆&nbsp;
						<a href="javascript:showAnniversaryStaffs('${anniversaryStaffs}')">
							<svg class="icon" aria-hidden="true" title="查看人员明细"
								data-toggle="tooltip" style="width: 15px">
								<use xlink:href="#icon-Detailedinquiry"></use>
							</svg>
						</a>
					</div>
					</c:if>
					<c:if test="${not empty contractList }">
						<div style="font-weight: bold; font-size: 12px;">
							当前有<span style="font-size:15px;">${contractListCount }</span>名员工合同即将到期，请尽快处理&nbsp;
							<a href="javascript:showContractListPartyBName('${contractList }')">
								<svg class="icon" aria-hidden="true" title="查看人员明细" data-toggle="tooltip" style="width: 15px">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
							</a>
						</div>
					</c:if>
				</div>
				<div class="title">人事待办>></div>
				<div style="margin: 0 2%">
					<input type="hidden" id="type" value="${type }" />
					<ul class="nav nav-tabs" role="tablist" id="myTab"
						style="font-size: 12px;">
						<li role="presentation"><a href="#vacation" role="tab"
							data-toggle="tab">请假申请<span class="badge"
								style="background-color:red;<c:if test='${vacationCount == 0 }'>display:none;</c:if>">${vacationCount }</span></a></li>
						<li role="presentation"><a href="#overtime" role="tab"
							data-toggle="tab">加班申请<span class="badge"
								style="background-color:red;<c:if test='${workOvertimeCount == 0 }'>display:none;</c:if>">${workOvertimeCount }</span></a></li>
						<li role="presentation"><a href="#resignation" role="tab"
							data-toggle="tab">离职申请<span class="badge"
								style="background-color:red;<c:if test='${resignationHRCount == 0 }'>display:none;</c:if>">${resignationHRCount }</span></a></li>
						<li role="presentation"><a href="#formal" role="tab"
							data-toggle="tab">转正申请<span class="badge"
								style="background-color:red;<c:if test='${formalHRCount == 0 }'>display:none;</c:if>">${formalHRCount }</span></a></li>
						<li role="presentation"
							<c:if test='${formalRemindCount == 0 }'>style="display:none"</c:if>><a
							href="#formalStaff" role="tab" data-toggle="tab">转正提醒<span
								class="badge"
								style="background-color:red;<c:if test='${formalRemindCount == 0 }'>display:none;</c:if>">${formalRemindCount}</span></a></li>
						<li role="presentation"
							<c:if test='${soonOverDueVehicleCount == 0 }'>style="display:none"</c:if>><a
							href="#soonOverDueVehicle" role="tab" data-toggle="tab">车辆过期<span
								class="badge"
								style="background-color:red;<c:if test='${soonOverDueVehicleCount == 0 }'>display:none;</c:if>">${soonOverDueVehicleCount}</span></a></li>
						<li role="presentation"
							<c:if test='${auditHRCount == 0 }'>style="display:none"</c:if>><a
							href="#audit" role="tab" data-toggle="tab">背景调查<span
								class="badge"
								style="background-color:red;<c:if test='${auditHRCount == 0 }'>display:none;</c:if>">${auditHRCount}</span></a></li>
						<li role="presentation"
							<c:if test='${viewReportCount == 0 }'>style="display:none"</c:if>><a
							href="#viewReport" role="tab" data-toggle="tab">查看日报申请<span
								class="badge"
								style="background-color:red;<c:if test='${viewReportCount == 0 }'>display:none;</c:if>">${viewReportCount}</span></a></li>
						<li role="presentation"
							<c:if test='${socialSecurityHRCount == 0 }'>style="display:none"</c:if>><a
							href="#socialSecurity" role="tab" data-toggle="tab">社保缴纳审核<span
								class="badge"
								style="background-color:red;<c:if test='${socialSecurityHRCount == 0 }'>display:none;</c:if>">${socialSecurityHRCount}</span></a></li>
					</ul>
					<div class="tab-content">
						<div role="tabpanel" class="tab-pane" id="soonOverDueVehicle">
							<div class="table-responsive" style="margin-top: 10px;">
								<table class="table table-striped">
									<thead>
										<tr>
											<th style="width: 10%">序号</th>
											<th style="width: 15%">车牌</th>
											<th style="width: 25%">车辆负责人</th>
											<th style="width: 20%">到期类型</th>
											<th style="width: 15%">到期时间</th>
											<th style="width: 15%">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty vehicleInfos}">
											<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
											<c:forEach items="${vehicleInfos}" var="item"
												varStatus="index">
												<tr>
													<td>${index.index+startIndex+1}</td>
													<td>${item.licenseNumber}</td>
													<td>${item.leaderName}</td>
													<td>${item.type}</td>
													<td><c:if test="${item.type=='保险'}">
			              			${item.insuranceRecordVo.nextInsuranceTime[fn:length(item.insuranceRecordVo.nextInsuranceTime)-1]}
			              		</c:if> <c:if test="${item.type=='年检'}">
			              			${item.yearlyInspectionVo.nextYearlyInspectionTime[fn:length(item.yearlyInspectionVo.nextYearlyInspectionTime)-1]}
			              		</c:if></td>
													<td><a
														onclick="completeHandle('${item.type}',${item.id})"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="完成办理" data-toggle="tooltip">
											<use xlink:href="#icon-wancheng"></use>
										</svg>
													</a></td>
												</tr>
											</c:forEach>
										</c:if>
									</tbody>
								</table>
							</div>
							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${soonOverDueVehicleCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="formalStaff">
							<div class="table-responsive" style="margin-top: 10px;">
								<table class="table table-striped">
									<thead>
										<tr>
											<th style="width: 10%">序号</th>
											<th style="width: 15%">姓名</th>
											<th style="width: 25%">部门</th>
											<th style="width: 20%">入职时间</th>
											<th style="width: 15%">入职天数</th>
											<th style="width: 15%">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty formalStaffs}">
											<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
											<c:forEach items="${formalStaffs}" var="item"
												varStatus="index">
												<tr>
													<td>${index.index+startIndex+1}</td>
													<td>${item.lastName}</td>
													<td>${item.departmentName}</td>
													<td>${item.entryDate1}</td>
													<td>${item.days}</td>
													<td><a
														onclick="goPath('HR/process/sendFormalInvitation?staffID=${item.staffID}')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="发送转正邀请" data-toggle="tooltip">
										<use xlink:href="#icon-banli"></use>
									</svg>
													</a></td>
												</tr>
											</c:forEach>
										</c:if>
									</tbody>
								</table>
							</div>
							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${formalRemindCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="vacation">
							<div class="table-responsive" style="margin-top: 10px;">
								<table class="table table-striped">
									<thead>
										<tr>
											<th>序号</th>
											<th style="width:10%">请假人/部门</th>
											<th>岗位</th>
											<th>开始时间</th>
											<th>结束时间</th>
											<th>休假天数</th>
											<th>工作代理人</th>
											<th>请假类型</th>
											<th>事由</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty taskVOs}">
											<s:set name="task_id"
												value="(#request.page-1)*(#request.limit)"></s:set>
											<s:iterator id="taskVO" value="#request.taskVOs"
												status="count">
												<tr>
													<td class="col-sm-1" style="width: 5%"><s:property
															value="#task_id+1" /></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.vacationUserName" /></td>
													<td class="col-sm-2"><s:iterator id="group"
															value="#taskVO.groupList" status="st">
															<s:if test="#st.index != 0 ">
																<br>
															</s:if>
															<s:property value="#group" />
														</s:iterator></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.beginDate" /></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.endDate" /></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.vacationTime" /></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.agentName" /></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.vacationType" /></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.reason" /></td>
													<td class="col-sm-2"><s:if test="#taskVO.type=='部门'">
															<a href="javascript:void(0)"
																onclick="showVacationUsers('${taskVO.staffNames}')">
																<svg class="icon" aria-hidden="true" title="查看请假人员"
																	data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
															</a>
	              			&nbsp;
              			</s:if> <a
														onclick="goPath('HR/process/taskComplete?taskID=<s:property value='#taskVO.taskID' />&result=1&businessType=请假申请')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="同意" data-toggle="tooltip">
							<use xlink:href="#icon-wancheng"></use>
						</svg>
													</a> &nbsp; <a
														onclick="goPath('HR/process/taskComplete?taskID=<s:property value='#taskVO.taskID' />&result=2&businessType=请假申请')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="不同意" data-toggle="tooltip">
							<use xlink:href="#icon-butongyi"></use>
						</svg>
													</a> <s:if test="#taskVO.attachmentSize>0">
		              	&nbsp;
		              	<a
																onclick="goPath('/personal/showVacationAttachment?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=vacationList')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="查看附件" data-toggle="tooltip">
							<use xlink:href="#icon-fujian"></use>
						</svg>
															</a>
														</s:if> &nbsp; <a
														onclick="goPath('HR/process/processHistory?processInstanceID=<s:property value='#taskVO.processInstanceID'/>&selectedPanel=vacationList')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="查看已审批环节" data-toggle="tooltip">
							<use xlink:href="#icon-liucheng"></use>
						</svg>
													</a></td>
												</tr>
												<s:set name="task_id" value="#task_id+1"></s:set>
											</s:iterator>
										</c:if>
									</tbody>
								</table>
							</div>
							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${vacationCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="resignation">
							<div class="table-responsive" style="margin-top: 10px;">
								<table class="table table-striped">
									<thead>
										<tr>
											<th>序号</th>
											<th>标题</th>
											<th>岗位</th>
											<th>发起人</th>
											<th>发起时间</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty resignationTasks}">
											<s:set name="task_id"
												value="(#request.page-1)*(#request.limit)"></s:set>
											<s:iterator id="taskVO" value="#request.resignationTasks"
												status="count">
												<tr>
													<td class="col-sm-1"><s:property value="#task_id+1" /></td>
													<td class="col-sm-3">【<s:property
															value="#taskVO.taskName" />】<s:property
															value="#taskVO.title" /></td>
													<td class="col-sm-3"><s:iterator id="group"
															value="#taskVO.groupList" status="st">
															<s:if test="#st.index != 0 ">
																<br>
															</s:if>
															<s:property value="#group" />
														</s:iterator></td>
													<td class="col-sm-2"><s:property
															value="#taskVO.requestUserName" /></td>
													<td class="col-sm-2"><s:property
															value="#taskVO.requestDate" /></td>
													<td class="col-sm-1"><s:if
															test="#taskVO.taskName!='工资清算'">
															<a
																onclick="goPath('HR/process/auditTask?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=resignationList')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="审批" data-toggle="tooltip">
															<use xlink:href="#icon-shenpi"></use>
														</svg>
															</a>
														</s:if> <s:if test="#taskVO.taskName=='工资清算'">
															<a
																onclick="goPath('PM/process/auditTask?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=resignationList')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="审批" data-toggle="tooltip">
															<use xlink:href="#icon-shenpi"></use>
														</svg>
															</a>
														</s:if></td>
												</tr>
												<s:set name="task_id" value="#task_id+1"></s:set>
											</s:iterator>
										</c:if>
									</tbody>
								</table>
							</div>

							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${resignationHRCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="formal">
							<div class="table-responsive" style="margin-top: 10px">
								<table class="table table-striped">
									<thead>
										<tr>
											<th>序号</th>
											<th>标题</th>
											<th>入职日期</th>
											<th>岗位</th>
											<th>发起人</th>
											<th>发起时间</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty formalTasks}">
											<s:set name="task_id"
												value="(#request.page-1)*(#request.limit)"></s:set>
											<s:iterator id="taskVO" value="#request.formalTasks"
												status="count">
												<tr>
													<td class="col-sm-1"><s:property value="#task_id+1" /></td>
													<td class="col-sm-2">【<s:property
															value="#taskVO.taskName" />】<s:property
															value="#taskVO.title" /></td>
													<td class="col-sm-2"><s:property
															value="#taskVO.entryDate" /></td>
													<td class="col-sm-3"><s:iterator id="group"
															value="#taskVO.groupList" status="st">
															<s:if test="#st.index != 0 ">
																<br>
															</s:if>
															<s:property value="#group" />
														</s:iterator></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.requestUserName" /></td>
													<td class="col-sm-2"><s:property
															value="#taskVO.requestDate" /></td>
													<td class="col-sm-1"><s:if
															test="#taskVO.taskDefKey == 'formalInvitation'">
															<a
																onclick="goPath('HR/process/sendInvitation?taskID=<s:property value='#taskVO.taskID'/>')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="发送邀请" data-toggle="tooltip">
						<use xlink:href="#icon-banli"></use>
						</svg>
															</a>
              			&nbsp;
              			<a
																onclick="goPath('HR/process/taskComplete?taskID=<s:property value='#taskVO.taskID'/>&result=9&businessType=转正申请')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="拒绝" data-toggle="tooltip">
						<use xlink:href="#icon-butongyi"></use>
						</svg>
															</a>
														</s:if> <s:else>
															<a
																onclick="goPath('HR/process/auditTask?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=formalList')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="审批" data-toggle="tooltip">
								<use xlink:href="#icon-shenpi"></use>
							</svg>
															</a>
														</s:else></td>
												</tr>
												<s:set name="task_id" value="#task_id+1"></s:set>
											</s:iterator>
										</c:if>
									</tbody>
								</table>
							</div>

							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${formalHRCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="overtime">
							<div class="table-responsive" style="margin-top: 10px;">
								<table class="table table-striped">
									<thead>
										<tr>
											<th style="width: 5%">序号</th>
											<th style="width: 20%">加班人员</th>
											<th style="width: 10%">所属部门</th>
											<th style="width: 10%">开始时间</th>
											<th style="width: 10%">结束时间</th>
											<th style="width: 10%">预计加班工时</th>
											<th style="width: 15%">加班原因</th>
											<th style="width: 10%">发起时间</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty overtimeTasks}">
											<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
											<c:forEach items="${overtimeTasks}" var="item"
												varStatus="index">
												<tr>
													<td>${index.index+startIndex+1}</td>
													<td>${item.requestUserName}</td>
													<td>${item.department}</td>
													<td>${item.beginDate}</td>
													<td>${item.endDate}</td>
													<td>${item.workHours}</td>
													<td>${item.reason}</td>
													<td>${item.requestDate}</td>
													<td><a
														onclick="goPath('HR/process/taskComplete?taskID=${item.taskID}&result=1&businessType=加班申请')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="同意" data-toggle="tooltip">
											<use xlink:href="#icon-wancheng"></use>
										</svg>
													</a> &nbsp; <a
														onclick="goPath('HR/process/taskComplete?taskID=${item.taskID}&result=2&businessType=加班申请')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="不同意" data-toggle="tooltip">
											<use xlink:href="#icon-butongyi"></use>
										</svg>
													</a> &nbsp; <a
														onclick="goPath('HR/process/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findTaskList')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="查看已审批环节" data-toggle="tooltip">
											<use xlink:href="#icon-liucheng"></use>
										</svg>
													</a></td>
											</c:forEach>
										</c:if>
									</tbody>
								</table>
							</div>
							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${workOvertimeCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="audit">
							<div class="table-responsive" style="margin-top: 10px">
								<table class="table table-striped">
									<thead>
										<tr>
											<th>序号</th>
											<th>标题</th>
											<th>调查人</th>
											<th>岗位</th>
											<th>发起人</th>
											<th>发起时间</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty auditTaks}">
											<s:set name="task_id"
												value="(#request.page-1)*(#request.limit)"></s:set>
											<s:iterator id="taskVO" value="#request.auditTaks"
												status="count">
												<tr>
													<td class="col-sm-1"><s:property value="#task_id+1" /></td>
													<td class="col-sm-2">【<s:property
															value="#taskVO.taskName" />】<s:property
															value="#taskVO.title" /></td>
													<td class="col-sm-2"><s:property
															value="#taskVO.auditUserName" /></td>
													<td class="col-sm-3"><s:iterator id="group"
															value="#taskVO.groupList" status="st">
															<s:if test="#st.index != 0 ">
																<br>
															</s:if>
															<s:property value="#group" />
														</s:iterator></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.requestUserName" /></td>
													<td class="col-sm-2"><s:property
															value="#taskVO.requestDate" /></td>
													<td class="col-sm-1"><a
														onclick="goPath('HR/process/auditBackground?taskID=<s:property value='#taskVO.taskID' />')"
														href="javascript:void(0)"> <svg class="icon"
																aria-hidden="true" title="审核" data-toggle="tooltip">
								<use xlink:href="#icon-shenpi"></use>
							</svg>
													</a></td>
												</tr>
												<s:set name="task_id" value="#task_id+1"></s:set>
											</s:iterator>
										</c:if>
									</tbody>
								</table>
							</div>

							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${auditHRCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="viewReport">
							<div class="table-responsive" style="margin-top: 10px;">
								<table class="table table-striped">
									<thead>
										<tr>
											<th style="width: 4%">序号</th>
											<th style="width: 6%">申请人员</th>
											<th style="width: 10%">所属部门</th>
											<th style="width: 15%">查看公司（部门）</th>
											<th style="width: 17%">查看人员</th>
											<th style="width: 7%">查看权限</th>
											<th style="width: 10%">申请时间</th>
											<th style="width: 5%">操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty viewReportTasks}">
											<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
											<c:forEach items="${viewReportTasks}" var="item"
												varStatus="index">
												<tr>
													<td>${index.index+startIndex+1}</td>
													<td>${item.requestUserName}</td>
													<td>${item.department}</td>
													<td><c:if test="${empty item.companyAndDepList}">——</c:if>
														<c:forEach items="${item.companyAndDepList}"
															var="companyAndDep">
												${companyAndDep}<br>
														</c:forEach></td>
													<td><c:if test="${empty item.userNames}">——</c:if>
														${item.userNames}</td>
													<td>${item.viewType}</td>
													<td>${item.requestDate}</td>
													<td><a href="javascipt:void(0)"
														onclick="auditTask(${item.taskID})"> <svg class="icon"
																aria-hidden="true" title="审批" data-toggle="tooltip">
	             								<use xlink:href="#icon-shenpi"></use>
	             						     </svg>
													</a></td>
											</c:forEach>
										</c:if>
									</tbody>
								</table>
							</div>
							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${viewReportCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
						<div role="tabpanel" class="tab-pane" id="socialSecurity">
							<div class="table-responsive" style="margin-top: 10px">
								<table class="table table-striped">
									<thead>
										<tr>
											<th>序号</th>
											<th>标题</th>
											<th>发起人</th>
											<th>发起时间</th>
											<th>操作</th>
										</tr>
									</thead>
									<tbody>
										<c:if test="${not empty socialSecTasks}">
											<s:set name="task_id"
												value="(#request.page-1)*(#request.limit)"></s:set>
											<s:iterator id="taskVO" value="#request.socialSecTasks"
												status="count">
												<tr>
													<td class="col-sm-1"><s:property value="#task_id+1" /></td>
													<td class="col-sm-2">【<s:property
															value="#taskVO.taskName" />】<s:property
															value="#taskVO.title" /></td>
													<td class="col-sm-1"><s:property
															value="#taskVO.requestUserName" /></td>
													<td class="col-sm-2"><s:property
															value="#taskVO.requestDate" /></td>
													<td class="col-sm-1"><s:if
															test="#taskVO.taskDefKey == 'ssHRUpdate'">
															<a
																onclick="goPath('HR/process/updateSocialSecurity?taskID=<s:property value='#taskVO.taskID' />')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="修改" data-toggle="tooltip">
							<use xlink:href="#icon-modify"></use>
						</svg>
															</a>
														</s:if> <s:if test="#taskVO.taskDefKey == 'ssFollowUp'">
															<a
																onclick="goPath('HR/process/updateSocialSecurity?taskID=<s:property value='#taskVO.taskID' />')"
																href="javascript:void(0)"> <svg class="icon"
																	aria-hidden="true" title="跟进" data-toggle="tooltip">
							<use xlink:href="#icon-shenpi"></use>
						</svg>
															</a>
														</s:if></td>
												</tr>
												<s:set name="task_id" value="#task_id+1"></s:set>
											</s:iterator>
										</c:if>
									</tbody>
								</table>
							</div>

							<div class="dropdown">
								<label>每页显示数量：</label>
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="true">
									${limit} <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"
									style="left: 104px; min-width: 120px;">
									<li><a class="dropdown-item-20" href="#">20</a></li>
									<li><a class="dropdown-item-50" href="#">50</a></li>
									<li><a class="dropdown-item-100" href="#">100</a></li>
								</ul>
								&nbsp;&nbsp;&nbsp;&nbsp;<span>共${socialSecurityHRCount}条记录</span>
								<input type="hidden" id="page" value="${page}" /> <input
									type="hidden" id="limit" value="${limit}" /> <input
									type="hidden" id="totalPage" value="${totalPage }" />
							</div>
							<%@include file="/includes/pager.jsp"%>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade bs-example-modal-lg" id="yearInspection" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document" style="width:30%">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">记录年检日期</h4>
	      </div>
	       <form class="form-horizontal" action="administration/vehicle/saveVehicleYearInspection" onsubmit="saveVehicleYearInspection()">
	      <div class="modal-body">
		  	<div class="form-group">
			     <label class="col-sm-4 control-label">本次年检日期<span style="color:red"> *</span></label>
			  	 <div class="col-sm-7">
			  	 	<input type="text" autocomplete="off" class="form-control" name="thisTime" required
						onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d'})"/>
			  	 </div>
			</div>
			<div class="form-group">
			  	 <label class="col-sm-4 control-label">下次年检日期<span style="color:red"> *</span></label>
			  	 <div class="col-sm-7">
			  	 	<input type="text" autocomplete="off" class="form-control" name="nextTime" required
						onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
			  	 </div>
			 </div>
			 <input type="hidden" name="vehicleId">
	      </div>
	      <div class="modal-footer" style="text-align:center">
	      <button type="submit" class="btn btn-primary">确定</button>
	      <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
	      </div>
	      </form>
	    </div>
	  </div>
	</div>
</body>
</html>
