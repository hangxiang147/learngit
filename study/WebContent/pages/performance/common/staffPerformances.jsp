<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "departmentId");
	});
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "departmentId");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "departmentId");
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
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/pages/performance/common/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<form class="form-horizontal">
					<h3 class="sub-header" style="margin-top: 0px;">个人绩效考核</h3>
					<div class="form-group">
						<label class="col-sm-1 control-label">姓名</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="userName" value="${userName}">
						</div>
						<label class="col-sm-1 control-label">年份</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="year" value="${year}">
						</div>
						<label class="col-sm-1 control-label">月份</label>
						<div class="col-sm-2">
							<input autoComplete="off" class="form-control" name="month" value="${month}">
						</div>
					</div>
					<div class="form-group">
						<label for="company" class="col-sm-1 control-label">公司部门</label>
						<div class="col-sm-2" id="company_div">
							<select class="form-control" id="company"
								name="companyId" onchange="showDepartment(this, 0)">
								<option value="">全部</option>
								<c:if test="${not empty companyVOs }">
									<s:iterator id="company" value="#request.companyVOs"
										status="st">
										<option value="<s:property value="#company.companyID" />"
											<s:if test="#request.companyId == #company.companyID ">selected="selected"</s:if>><s:property
												value="#company.companyName" /></option>
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
									<s:set name="selectDepartmentID" value="#selectedDepartmentID"></s:set>
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
					<div class="col-sm-1"></div>
					<button type="submit" class="btn btn-primary">查询</button>
				</form>

				<h2 class="sub-header"></h2>
				<div class="table-responsive">
					<table class="table table-striped">
						<thead style="word-break:keep-all">
							<tr>
								<th>序号</th>
								<th>公司</th>
								<th>岗位</th>
								<th>姓名</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${performances}" var="performance" varStatus="status">
								<tr>
									<td>${status.index+1+(page-1)*limit}</td>
									<td>${performance[2]}</td>
									<td>${performance[3]}</td>
									<td>${performance[1]}</td>
									<td>
										<a onclick="goPath('/administration/performance/showPersonalPerformance?selectedPanel=staffPerformances&userId=${performance[0]}&year=${year}&month=${month}')" href="javascript:void(0)">
				        					<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
				      							<use xlink:href="#icon-Detailedinquiry"></use>
				      						</svg>
		      							</a>
<%-- 		      							&nbsp;
										<a onclick="goPath('/administration/performance/toModifyPersonalPerformance?userId=${performance[0]}&year=${year}&month=${month}')" href="javascript:void(0)">
				        					<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
				      							<use xlink:href="#icon-modify"></use>
				      						</svg>
		      							</a> --%>
									</td>
								</tr>
							</c:forEach>
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
					&nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
					<input type="hidden" id="page" value="${page}" /> <input
						type="hidden" id="limit" value="${limit}" /> <input type="hidden"
						id="totalPage" value="${totalPage }" />
				</div>
				<%@include file="/includes/pager.jsp"%>
			</div>
		</div>
	</div>
</body>
</html>
