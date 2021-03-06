<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
<style type="text/css">
.col-sm-1 {
	padding-right: 6px;
	padding-left: 6px;
}

.detail-control {
	display: block;
	width: 100%;
	height: 34px;
	padding: 6px 12px;
	font-size: 14px;
	line-height: 1.42857143;
	color: #555;
}

.tableuser {
	border: 1px solid #ddd;
	border-spacing: 0;
	border-collapse: collapse;
	width: 880px;
	color: #555555;
}

.tableuser tr {
	display: table-row;
	vertical-align: inherit;
	border-color: inherit;
}

.tableuser tr td {
	border: 1px solid #ddd;
	height: 34px;
	line-height: 34px;
	padding: 10px 10px;
}

.tab table {
	border-collapse: collapse;
	width: 100%;
	margin-top: 5px;
}

.tab table tr td {
	word-wrap: break-word;
	font-size: 10px;
	padding: 8px 7px;
	text-align: center;
	border: 1px solid #ddd
}

.title {
	text-align: left !important;
	background: #efefef !important;
}

.form-control {
	display: inline-block;
}

.tab {
	color: #555555;
	box-shadow: 0px 1px 5px #dddddd;
}

.icon {
	width: 1.5em;
	height: 1.5em;
	vertical-align: -0.15em;
	fill: currentColor;
	overflow: hidden;
}
.textcomplete-dropdown{
	z-index:1050 !important;
}
.glyphicon-trash{text-decoration:none !important}
.glyphicon-trash:hover{color:red}
</style>
</head>
<body>

	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'PmManage'"></s:set>
			<%@include file="/pages/HR/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<ul class="nav nav-tabs" role="tablist" id="myTab"
					style="font-size: 15px; margin-top: 0%; margin-bottom: 2%">
					<li role="presentation"><a href="#workReportList"
						data-index="20" data-href="workReport/findWorkReportList"
						role="tab" data-toggle="tab">日报明细</a></li>
					<li role="presentation" class="dropdown"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"> 日报统计 <span
							class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<c:if test="${not empty companyVOs }">
								<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
									<li role="presentation"><a
										href="#${companyVO.companyID+100}" role="tab"
										data-companyid="${companyVO.companyID}"
										data-index="${companyVO.companyID+100}" data-toggle="tab"
										data-href="workReport/findWorkReportStatistics">
											${companyVO.companyName} </a></li>
								</c:forEach>
							</c:if>
						</ul></li>
					<li role="presentation"><a href="#weekReportList" role="tab"
						data-index="21" data-href="workReport/findWeekReportList"
						data-toggle="tab">周报明细</a></li>
					<li role="presentation" class="dropdown"><a href="#"
						class="dropdown-toggle" data-toggle="dropdown"> 周报统计 <span
							class="caret"></span>
					</a>
						<ul class="dropdown-menu">
							<c:if test="${not empty companyVOs }">
								<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
									<li role="presentation"><a href="#${companyVO.companyID}"
										role="tab" data-companyid="${companyVO.companyID}"
										data-index="${companyVO.companyID}" data-toggle="tab"
										data-href="workReport/findWeekReportStatistics">
											${companyVO.companyName} </a></li>
								</c:forEach>
							</c:if>
						</ul></li>
					<li role="presentation"><a href="#weekReporterManage"
						role="tab" data-index="22"
						data-href="workReport/weekReporterManage" data-toggle="tab">周报人员维护</a></li>
				</ul>
				<div class="tab-content">
					<div role="tabpanel" class="tab-pane" id="workReportList">
						<form id="workReportForm" class="form-horizontal">
							<div class="form-group">
								<label for="name" class="col-sm-1 control-label">姓名</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" id="name"
										name="workReportDetailVO.name"
										value="${workReportDetailVO.name}">
								</div>
							</div>
							<div class="form-group">
								<label for="company" class="col-sm-1 control-label">公司部门</label>
								<div class="col-sm-2" id="company_div">
									<select class="form-control" id="company"
										name="workReportDetailVO.companyID"
										onchange="showDepartment(this, 0)">
										<option value="">请选择</option>
										<c:if test="${not empty companyVOs }">
											<s:iterator id="company" value="#request.companyVOs"
												status="st">
												<option value="<s:property value="#company.companyID" />"
													<s:if test="#request.workReportDetailVO.companyID == #company.companyID ">selected="selected"</s:if>>
													<s:property value="#company.companyName" />
												</option>
											</s:iterator>
										</c:if>
									</select>
								</div>
								<c:if test="${not empty departmentVOs}">
									<c:if test="${not empty selectedDepartmentIDs}">
										<s:set name="departmentClass" value="'col-sm-2'"></s:set>
										<s:set name="parent" value="0"></s:set>
										<s:iterator id="selectedDepartmentID"
											value="#request.selectedDepartmentIDs" status="st">
											<s:set name="level" value="#st.index+1"></s:set>
											<s:set name="selectDepartmentID"
												value="#selectedDepartmentID"></s:set>
											<s:set name="departmentClass"
												value="#departmentClass+' department'+#level"></s:set>
											<s:set name="hasNextLevel" value="'false'"></s:set>
											<div class="<s:property value='#departmentClass'/>"
												id="department<s:property value='#level'/>_div">
												<select class="form-control"
													id="department<s:property value='#level'/>"
													onchange="showDepartment(this, <s:property value='#level'/>)">
													<option value="">--
														<s:property value="#level" />级部门--
													</option>
													<s:iterator id="department" value="#request.departmentVOs"
														status="department_st">
														<s:if test="#department.parentID == #parent">
															<option
																value="<s:property value='#department.departmentID'/>"
																<s:if test="#department.departmentID == #selectedDepartmentID">selected="selected"</s:if>><s:property
																	value="#department.departmentName" /></option>
														</s:if>
														<s:if test="#department.parentID == #selectedDepartmentID">
															<s:set name="hasNextLevel" value="'true'"></s:set>
														</s:if>
													</s:iterator>
												</select>
											</div>
											<s:set name="parent" value="#selectedDepartmentID"></s:set>
										</s:iterator>
										<input type="hidden" id="departmentLevel"
											value="<s:property value='#level'/>" />
										<s:if test="#hasNextLevel == 'true'">
											<s:set name="index" value="#level+1"></s:set>
											<div
												class="<s:property value="#departmentClass+' department'+#index"/>"
												id="department<s:property value='#index'/>_div">
												<select class="form-control"
													id="department<s:property value='#index'/>"
													onchange="showDepartment(this, <s:property value='#index'/>)">
													<option value="">--
														<s:property value="#index" />级部门--
													</option>
													<s:iterator id="department" value="#request.departmentVOs"
														status="department_st">
														<s:if test="#department.parentID == #selectDepartmentID">
															<option
																value="<s:property value='#department.departmentID'/>"><s:property
																	value="#department.departmentName" /></option>
														</s:if>
													</s:iterator>
												</select>
											</div>
										</s:if>
									</c:if>
									<c:if test="${empty selectedDepartmentIDs}">
										<div class="col-sm-2 department1" id="department1_div">
											<select class="form-control" id="department1"
												onchange="showDepartment(this, 1)">
												<option value="">--1级部门--</option>
												<s:iterator id="department" value="#request.departmentVOs"
													status="department_st">
													<s:if test="#department.level == 1">
														<option
															value="<s:property value='#department.departmentID'/>"><s:property
																value="#department.departmentName" /></option>
													</s:if>
												</s:iterator>
											</select>
										</div>
									</c:if>
								</c:if>
							</div>


							<div class="form-group">
								<label for="beginDate" class="col-sm-1 control-label">日期</label>
								<div class="col-sm-2">
									<input type="text" class="form-control" id="beginDate"
										name="workReportDetailVO.beginDate"
										value="${workReportDetailVO.beginDate }"
										onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
								</div>
								<div class="col-sm-2">
									<input type="text" class="form-control" id="endDate"
										name="workReportDetailVO.endDate"
										value="${workReportDetailVO.endDate }"
										onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
								</div>
							</div>
							<div class="col-sm-5"></div>
							<button type="submit" id="submitButton" class="btn btn-primary"
								onclick="searchForWorkReportList()">查询</button>
						</form>

						<h2 class="sub-header"></h2>
						<div class="table-responsive">
							<table class="table table-striped">
								<thead>
									<tr>
										<th width="50">序号</th>
										<th width="80">姓名</th>
										<th width="100">汇报日期</th>
										<th width="100">提交时间</th>
										<th width="80">工作时长</th>
										<th>工作内容</th>
										<th width="80">数量</th>
										<th width="90">下达任务人</th>
										<th width="80">完成情况</th>
										<th width="80">完成用时</th>
									</tr>
								</thead>
								<tbody>
									<c:if test="${not empty workReportDetailVOs}">
										<s:set name="attendance_id"
											value="(#request.page-1)*(#request.limit)"></s:set>
										<s:iterator id="workReportDetailVO"
											value="#request.workReportDetailVOs" status="count">
											<tr>
												<td
													rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property
														value="#attendance_id+1" /></td>
												<td
													rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property
														value="#workReportDetailVO.name" /></td>
												<td
													rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property
														value="#workReportDetailVO.reportDate" /><br>
												<s:property value="#workReportDetailVO.weekDay" /></td>
												<td
													rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property
														value="#workReportDetailVO.addTime" /></td>
												<td
													rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property
														value="#workReportDetailVO.totalHours" /></td>
												<s:iterator id="workContent"
													value="#workReportDetailVO.workContent" status="sta">
													<s:if test="#sta.index!=0">
														<tr>
													</s:if>
													<td><s:property value="#workContent" /></td>
													<td><s:property
															value="#workReportDetailVO.quantities[#sta.index]" /></td>
													<td><s:property
															value="#workReportDetailVO.assignTaskName[#sta.index]" /></td>
													<td><s:property
															value="#workReportDetailVO.completeState[#sta.index]" /></td>
													<td><s:property
															value="#workReportDetailVO.workHours[#sta.index]" /></td>
											</tr>
										</s:iterator>

										<s:set name="attendance_id" value="#attendance_id+1"></s:set>
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
							&nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span> <input
								type="hidden" id="page" value="${page}" /> <input type="hidden"
								id="limit" value="${limit}" /> <input type="hidden"
								id="totalPage" value="${totalPage }" />
						</div>
						<%@include file="/includes/pager.jsp"%>
					</div>
					<%@include file="/pages/PM/workReportStatistics.jsp"%>
					<div role="tabpanel" class="tab-pane" id="weekReportList">
						<%@include file="/pages/PM/weekReportList.jsp"%>
					</div>
					<%@include file="/pages/PM/weekReportStatistics.jsp"%>
					<div role="tabpanel" class="tab-pane" id="weekReporterManage">
						<%@include file="/pages/PM/weekReporterManage.jsp"%>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	$(function() {
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "workReportDetailVO.departmentID");
		
		if(!'${flag}'){
			var search = location.href;
			if(search.indexOf('findWorkReportList')!=-1){
				$("a[data-index='20']").tab("show");
			}else if(search.indexOf('findWeekReportList')!=-1){
				$("a[data-index='21']").tab("show");
			}else if(search.indexOf('weekReporterManage')!=-1){
				$("a[data-index='22']").tab("show");
			}
		}
		$("#myTab li>a[role='tab']").each(function(index, obj){
			if('${flag}'==$(obj).data("index")){
				$(obj).tab("show");
			}
			$(obj).click(function(){
				if($(obj).data("companyid")){
					location.href = $(obj).data("href")+"?flag="+$(obj).data("index")+"&companyID="+$(obj).data("companyid");
				}else{
					location.href = $(obj).data("href")+"?flag="+$(obj).data("index");
				}
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		});
	});
	function searchForWorkReportList() {
		var params = $("#workReportForm").serialize();
		window.location.href = "workReport/findWorkReportList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "workReportDetailVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "workReportDetailVO.departmentID");
		}
		$.ajax({
			url:'HR/staff/findDepartmentsByCompanyIDParentID',
			type:'post',
			data:{companyID: $("#company").val(),
				  parentID: parentID},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					if (level == 1) {
						window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					} else {
						return;
					}
				}
				
				var divObj = $("#"+$(obj).attr('id')+"_div");
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
							+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
							+"<option value=\"\">--"+level+"级部门--</option></select>"
							+"</div>");
				$.each(data.departmentVOs, function(i, department) {
					$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
				});
			}
		});
	}

	
</script>
</body>
</html>
