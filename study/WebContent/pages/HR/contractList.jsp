<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "contractVO.departmentID");
		
		
		var tt = $("#type").val();
		$("#myTab li:eq("+tt+") a").tab("show");
		$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
			var type=$(e.target).attr("data-index");
			window.location.href = "/HR/contract/findContractList?type="+type;
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	});
	
	function search() {
		var params = $("#queryResignationForm").serialize();
		window.location.href = "HR/contract/findContractList?" + decodeURIComponent(params,true);
		Load.Base.LoadingPic.FullScreenShow(null);
	}  	
	
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "contractVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "contractVO.departmentID");
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
	
	function deleteContract(contractID) {
		layer.confirm("确认删除该员工合同？", {offset:'100px'}, function(index){
			layer.close(index);
			Load.Base.LoadingPic.FullScreenShow(null);
			$.ajax({
				url:'HR/contract/deleteContractByContractID',
				type:'post',
				data:{contractID: contractID},
				dataType:'json',
				success:function (data){
					if (data.errorMessage!=null && data.errorMessage.length!=0) {
						window.location.href = "HR/staff/error?panel=contract&errorMessage="+encodeURI(encodeURI(data.errorMessage));
					}
					layer.alert("删除成功！", {offset:'100px'}, function(index){
						layer.close(index);
						window.location.href = "HR/contract/findContractList";
						Load.Base.LoadingPic.FullScreenShow(null);
					});
				},
				complete:function(){
					Load.Base.LoadingPic.FullScreenHide();
				}
			});
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
      	<s:set name="panel" value="'contract'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	 <input type="hidden" id="type" value="${type }" />
       	 <ul id="myTab" class="nav nav-tabs" role="tablist" >
       	 	<li role="presentation">
       	 		<a href="#contractList" data-toggle="tab" role="tab" data-index="0">合同列表</a>
       	 	</li>
       	 	<li role="presentation">
       	 		<a href="#expirationNotice" data-toggle="tab" role="tab" data-index="1">合同到期提醒</a>
       	 	</li>
       	 </ul>
       	 
       	 <div class="tab-content">
       	 
       	 	<div role="tabpanel" class="tab-pane" id="contractList">
       	 	
				<form id="queryResignationForm" class="form-horizontal">
				
					<div class="form-group" style="margin-top:2%;">
						<label for="company" class="col-sm-1 control-label">公司部门</label>
						<div class="col-sm-2" id="company_div">
							<select class="form-control" id="company" name="contractVO.companyID" onchange="showDepartment(this, 0)">
								<option value="">请选择</option>
								<c:if test="${not empty companyVOs }">
									<s:iterator id="company" value="#request.companyVOs" status="st">
										<option value="<s:property value="#company.companyID" />" <s:if test="#request.contractVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
									</s:iterator>
								</c:if>
							</select>
						</div>
						<c:if test="${not empty departmentVOs}">
							<c:if test="${not empty selectedDepartmentIDs}">
								<s:set name="departmentClass" value="'col-sm-2'"></s:set>
								<s:set name="parent" value="0"></s:set>
								<s:iterator id="selectedDepartmentID" value="#request.selectedDepartmentIDs" status="st">
									<s:set name="level" value="#st.index+1"></s:set>
									<s:set name="selectDepartmentID" value="#selectedDepartmentID"></s:set>
									<s:set name="departmentClass" value="#departmentClass+' department'+#level"></s:set>
									<s:set name="hasNextLevel" value="'false'"></s:set>
									<div class="<s:property value='#departmentClass'/>" id="department<s:property value='#level'/>_div" >
										<select class="form-control" id="department<s:property value='#level'/>" onchange="showDepartment(this, <s:property value='#level'/>)">
											<option value="">--<s:property value="#level"/>级部门--</option>
											<s:iterator id="department" value="#request.departmentVOs" status="department_st">
												<s:if test="#department.parentID == #parent">
													<option value="<s:property value='#department.departmentID'/>" <s:if test="#department.departmentID == #selectedDepartmentID">selected="selected"</s:if>><s:property value="#department.departmentName"/></option>
												</s:if>
												<s:if test="#department.parentID == #selectedDepartmentID">
													<s:set name="hasNextLevel" value="'true'"></s:set>
												</s:if>
											</s:iterator>
										</select>
									</div>
									<s:set name="parent" value="#selectedDepartmentID"></s:set>
								</s:iterator>
								
								<input type="hidden" id="departmentLevel" value="<s:property value='#level'/>"/>
								
								<s:if test="#hasNextLevel == 'true'">
									<s:set name="index" value="#level+1"></s:set>
									<div class="<s:property value="#departmentClass+' department'+#index"/>" id="department<s:property value='#index'/>_div" >
										<select class="form-control" id="department<s:property value='#index'/>" onchange="showDepartment(this, <s:property value='#index'/>)">
											<option value="">--<s:property value="#index"/>级部门--</option>
											<s:iterator id="department" value="#request.departmentVOs" status="department_st">
												<s:if test="#department.parentID == #selectDepartmentID">
													<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
												</s:if>
											</s:iterator>
										</select>
									</div>
								</s:if>
							</c:if>
							
							<c:if test="${empty selectedDepartmentIDs}">
								<div class="col-sm-2 department1" id="department1_div" >
									<select class="form-control" id="department1" onchange="showDepartment(this, 1)">
										<option value="">--1级部门--</option>
										<s:iterator id="department" value="#request.departmentVOs" status="department_st">
											<s:if test="#department.level == 1">
												<option value="<s:property value='#department.departmentID'/>"><s:property value="#department.departmentName"/></option>
											</s:if>
										</s:iterator>
									</select>
								</div>
							</c:if>
							
						</c:if>
					</div>
							
					<div class="form-group">
						<label for="reimbursementName" class="col-sm-1 control-label">员工姓名</label>
						<div class="col-sm-2">
							<input type="text" class="form-control"  name="contractVO.partyBName" value="${contractVO.partyBName}" >
						</div>
						
						<div class="col-sm-2" style="padding-top:2px;">
							<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
						</div>
					</div>
				
					<a class="btn btn-primary"  onclick="goPath('pages/HR/newContract.jsp?selectedPanel=contractList')" href="javascript:void(0)">
						<span class="glyphicon glyphicon-plus"></span> 合同
					</a>
								
				</form>
		 
         		<h2 class="sub-header"></h2>
         		
         <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width: 6%;">序号</th>
                  <th style="width: 10%;">员工（乙方）</th>
                  <th style="width: 20%;">公司名称（甲方）</th>
                  <th style="width: 10%;">开始时间</th>
                  <th style="width: 10%;">结束时间</th>
                  <th style="width: 10%;">上传时间</th>
                  <th style="width: 10%;">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty contracts}">
              	<s:set name="ctract_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="ctract" value="#request.contracts" status="count">
              		<tr>
              			<td><s:property value="#ctract_id+1"/></td>   
              			<td><s:property value="#ctract.partyB"/></td>    
              			<td><s:if test="#ctract.partyA==1">南通智造链科技有限公司</s:if>
              			    <s:if test="#ctract.partyA==2">南通迷丝茉服饰有限公司</s:if>
              			    <s:if test="#ctract.partyA==3">广州亦酷亦雅电子商务有限公司</s:if>
              			    <s:if test="#ctract.partyA==4">南通智造链电子商务有限公司</s:if>
              			</td>          			              			
              			<td><s:property value="#ctract.beginDate"/></td>              			
              			<td><s:property value="#ctract.endDate"/></td> 
              			<td><s:property value="#ctract.addTime"/></td>             			
              			<td>
              			<a onclick="goPath('HR/contract/findContractsByPartyB?contractID=<s:property value="#ctract.contractID"/>&selectedPanel=contractList')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
							<use xlink:href="#icon-Detailedinquiry"></use>
						</svg>
              			</a>
              			&nbsp;
              			<a onclick="goPath('HR/contract/updateContractByContractID?contractID=<s:property value="#ctract.contractID" />')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
							<use xlink:href="#icon-modify"></use>
						</svg>
              			</a>
              			&nbsp;
              			<a href="javascript:void(0);" onclick="deleteContract('<s:property value="#ctract.contractID" />')">
              			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
							<use xlink:href="#icon-delete"></use>
						</svg>
              			</a>
              			</td>              			
              		</tr>
              		<s:set name="ctract_id" value="#ctract_id+1"></s:set>
              	</s:iterator>
              	</c:if>
              </tbody>
            </table>
          </div>
       	  
       	  
          <div class="dropdown">
           	<label>每页显示数量：</label>
           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
           		${limit}
           		<span class="caret"></span>
           	</button>
           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
			    <li><a class="dropdown-item-20" href="#">20</a></li>
			    <li><a class="dropdown-item-50" href="#">50</a></li>
			    <li><a class="dropdown-item-100" href="#">100</a></li>
		    </ul>
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
       	 	</div>
       	 	
       	 	
       	 	
       	 	<div role="tabpanel" class="tab-pane" id="expirationNotice">
       	 	
       	 		<div class="table-responsive" style="margin-top:2%;">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">员工（乙方）</th> 
                  <th class="col-sm-2">公司名称（甲方）</th>
                  <th class="col-sm-1">开始时间</th>
                  <th class="col-sm-1">结束时间</th>
                  <th class="col-sm-1">操作</th>
                </tr>
              </thead> 
              <tbody>
              	<c:if test="${not empty contracts}">
              	<s:set name="ctract_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="ctract" value="#request.contracts" status="count">
              		<tr>
              			<td><s:property value="#ctract_id+1"/></td>   
              			<td><s:property value="#ctract.partyB"/></td>
              			<td><s:if test="#ctract.partyA==1">南通智造链科技有限公司</s:if>
              			    <s:if test="#ctract.partyA==2">南通迷丝茉服饰有限公司</s:if>
              			    <s:if test="#ctract.partyA==3">广州亦酷亦雅电子商务有限公司</s:if>
              			    <s:if test="#ctract.partyA==4">南通智造链电子商务有限公司</s:if>
              			</td>              			
              			<td><s:property value="#ctract.beginDate"/></td>              			
              			<td><s:property value="#ctract.endDate"/></td>              			
              			<td>
              			<a onclick="goPath('HR/contract/getContractByContractID?contractID=<s:property value="#ctract.contractID"/>&selectedPanel=remindCtrList')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
								<use xlink:href="#icon-Detailedinquiry"></use>
							</svg>
              			</a>
              			&nbsp;
              			<a onclick="goPath('HR/contract/renewContract?contractID=<s:property value="#ctract.contractID"/>')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="续签" data-toggle="tooltip">
								<use xlink:href="#icon-shenpi"></use>
							</svg>
              			</a>
              			</td>              			
              		</tr>
              		<s:set name="ctract_id" value="#ctract_id+1"></s:set>
              	</s:iterator>
              	</c:if>
              </tbody>
            </table>
          </div>
       	  
       	  
          <div class="dropdown">
           	<label>每页显示数量：</label>
           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
           		${limit}
           		<span class="caret"></span>
           	</button>
           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
			    <li><a class="dropdown-item-20" href="#">20</a></li>
			    <li><a class="dropdown-item-50" href="#">50</a></li>
			    <li><a class="dropdown-item-100" href="#">100</a></li>
		    </ul>
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
       	 	</div>
       	 	
       	 	
       	 </div>
       	 
       	 
          
        </div>
      </div>
    </div>
  </body>
</html>
