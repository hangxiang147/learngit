<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%> 
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<meta http-equiv="expires" content="0">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "resignationVO.departmentID");
		$("[data-toggle='tooltip']").tooltip();
		
		var staffEntityStatus = new Number($("#inputStaffEntityStatus").val());
		if(staffEntityStatus==4){
			$("#staffEntityStatus").find("option[value='4']").attr("selected","selected");
		}else if(staffEntityStatus ==1 || staffEntityStatus==0){
			$("#staffEntityStatus").find("option[value='1']").attr("selected","selected");
		}else if(staffEntityStatus ==2){
			$("#staffEntityStatus").find("option[value='2']").attr("selected","selected");
		}
		
		var statusEctype = $("#statusEctype").val();
		$("#status").find("option[value='"+statusEctype+"']").attr("selected","selected");
	});
	
	function search() {
		var params = $("#queryResignationForm").serialize();
		window.location.href = "HR/staff/findResignationList?" + decodeURIComponent(params,true);
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "resignationVO.departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "resignationVO.departmentID");
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
	
	function export_excel(){
		var params = $("#queryResignationForm").serialize();
		window.location.href="/HR/staff/exportResignationVOToExcel?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
		$("#exportBtn").attr("disabled","disabled");
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
      	<s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       
        <form id="queryResignationForm" class="form-horizontal">
       	  <h3 class="sub-header" style="margin-top:0px;">离职人员列表</h3>
       	  
		  <div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="resignationVO.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.resignationVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
			    			<s:if test="#department.parentID == #selectedDepartmentID"><s:set name="hasNextLevel" value="'true'"></s:set></s:if>
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
		  		<label for="request_userName" class="col-sm-1 control-label">离职人</label>
			    <div class="col-sm-2">
			    	<input id="request_userName" type="text" class="form-control"  name="resignationVO.requestUserName" value="${resignationVO.requestUserName }" >
			    </div>
			    
			    <label for="staffEntityStatus" class="col-sm-1 control-label">状态</label>
			    <div class="col-sm-2" id="staffEntity_status">
			    	<select class="form-control" id="staffEntityStatus" name="resignationVO.staffEntityStatus">
				      <option value="2">请选择</option>
				      <option selected="selected" value="1">待离职</option>
				      <option value="4">已离职</option>
					</select>
					<input type="hidden" class="form-control" id="inputStaffEntityStatus" value="${resignationVO.staffEntityStatus }" >
			    </div>
			    
			    <label for="status" class="col-sm-1 control-label">当前环节</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="status" name="resignationVO.status" >
				      <option value="">请选择</option>
				      <!-- <option value="员工填写离职申请">员工填写离职申请</option> -->
				      <option value="主管审批">主管审批</option>
				      <option value="人力资源审批">人力资源审批</option>
				      <option value="总经理审批">总经理审批</option>
				      <option value="离职交接">离职交接</option>
				      <option value="工资清算">工资清算</option>
					</select>
			    	<input id="statusEctype" type="hidden" class="form-control" value="${resignationVO.status }">
			    </div>
		  		
		  		<label for="assigneeUserName" class="col-sm-1 control-label">待处理人</label>
			    <div class="col-sm-2">
			    	<input id="assigneeUserName" type="text" class="form-control" name="resignationVO.assigneeUserName" value="${resignationVO.assigneeUserName }">
			    </div>
		  </div>
		  
		  <div class="form-group">
		  		
			    	    
			    <label for="beginDate" class="col-sm-1 control-label">离职日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="resignationVO.beginDate" value="${resignationVO.beginDate}" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="resignationVO.endDate" value="${resignationVO.endDate }" onclick="WdatePicker({maxDate:'%y-%M-%d', minDate:'#F{$dp.$D(\'beginDate\')}'})" placeholder="结束时间">
			    </div>
		    	
			    <div class="col-sm-1" style="padding-top:2px;">
			    	<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
			    </div>
		  </div>
		  <button id="exportBtn" type="button" onclick="export_excel()" class="btn btn-primary" title="导出">
				<span class="glyphicon glyphicon-export"></span> 离职列表	
		  </button>
		  
		 </form>
			
		  <h3 class="sub-header"></h3>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr class="title">
                  <th style="vertical-align: middle;width:5%;">序号</th>  
                  <th style="vertical-align: middle;width:10%;">部门</th>               
                  <th style="vertical-align: middle;width:12%;">离职人<br>(含手机号)</th>
				  <th style="vertical-align: middle;width:8%;">状态</th>
                  <th style="vertical-align: middle;width:10%;">入职日期</th>
                  <th style="vertical-align: middle;width:10%;">申请时间</th>
                  <th style="vertical-align: middle;width:10%;">申请离职日期</th>
                  <th style="vertical-align: middle;width:8%;">当前环节<br>/待处理人</th>
                  <th style="vertical-align: middle;width:10%;">总经理确认<br>离职日期</th>
                  <th style="vertical-align: middle;width:10%;">离职日期</th>
                  <th style="vertical-align: middle;width:8%;">操作</th>
                  <th style="vertical-align: middle;display:none;">隐藏备注</th>                
                  <th style="vertical-align: middle;display:none;">离职原因</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty resignationVOs}">
              	<s:set name="resignation_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<c:forEach items="${resignationVOs }" var="res" varStatus="count">
              		<tr>
              			<td><s:property value="#resignation_id+1"/></td>
              			<td><c:out value="${res.departmentName==''?'――――――':res.departmentName }"></c:out></td>
              			<td>${res.requestUserName }<br>${res.telephone }</td>
              			
              			<c:choose>
              			<c:when test="${res.staffEntityStatus==4 }">
              				<td>已离职</td>
              			</c:when>
              			<c:when test="${res.staffEntityStatus!=4 && res.processStatus==1 }">
              				<td>待离职<br>(已批准)</td>
              			</c:when>
              			<c:otherwise>
              				<td>待离职</td>
              			</c:otherwise>
              			</c:choose>
              			
              			<td>${res.entryDate }</td>
              			<td><c:out value="${res.requestDate==''?'――――――':res.requestDate }"></c:out></td>
              			<td><c:out value="${res.requestLeaveDate==''?'――――――':res.requestLeaveDate }"></c:out></td>
              			<td>
              				<c:choose>
              					<c:when test="${res.status==null && (empty res.status) }">
              						<span style="color:#337ab7;">完结</span>
              					</c:when>
              					<c:otherwise>
              						${res.status }
              					</c:otherwise>
              				</c:choose>
              				<br>
		              		${res.assigneeUserName }
              			</td>
              			<td><c:out value="${res.managerConfirmDate==''?'――――――':res.managerConfirmDate }"></c:out></td>
              			<td><c:out value="${res.leaveDate==''?'――――――':res.leaveDate }"></c:out></td>
              			
              			<td>
              				<a onclick="showPersonalData(this)" href="javascript:void(0)">
	              				<svg class="icon" aria-hidden="true" title="查看离职原因" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
							</a>
							
							<%-- &nbsp;
							<a onclick="goPath('HR/staff/staffCardByUserId?requestUserID=${res.requestUserID }')" href="javascript:void(0)">
								<svg class="icon" aria-hidden="true" title="人物卡片" data-toggle="tooltip">
									<use xlink:href="#icon-gerenxinxi"></use>
								</svg>
							</a> --%>
							
							&nbsp;
							<a onclick="goPath('HR/staff/showStaffDetail?userID=${res.requestUserID }')" href="javascript:void(0)">
	              				<svg class="icon" aria-hidden="true" title="个人档案详情" data-toggle="tooltip">
									<use xlink:href="#icon-gerenxinxi"></use>
								</svg>
							</a>
              			</td>
              			<td style="display:none;">${res.note }</td>
              			<td style="display:none;">${res.reasons }</td>     			
              		</tr>
              		<s:set name="resignation_id" value="#resignation_id+1"></s:set>
              	</c:forEach>
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
    <script type="text/javascript">
       function showPersonalData(obj){
       		var check = $(obj).parent().next().text();
       		var reasons = $(obj).parent().next().next().text();
       		if(check!=""){
       			layer.alert(reasons+"；"+check,{offset:'100px',title:'离职原因'});
       		}else{
       			layer.alert(reasons,{offset:'100px',title:'离职原因'});
       		}
		}
     </script>
  </body>
</html>
