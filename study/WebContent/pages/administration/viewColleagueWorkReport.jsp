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
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "viewReportVo._departmentID");
		if('${alertMessage}'){
			layer.alert('${alertMessage}',{offset:'100px'});
		}
	});
	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('input[name="viewReportVo.userName"]'),function ($item){
			$("input[name='viewReportVo.userID']").val($item.data("userId"));
		});
	});
	$(document).click(function (event) {
		if ($("input[name='viewReportVo.userID']").val()=='')
		{
			$('input[name="viewReportVo.userName"]').val("");
		}
	}); 
  	function checkEmpty(){
  		if($('input[name="viewReportVo.userName"]').val()==''){
  			$("input[name='viewReportVo.userID']").val('');
  		}
  	}
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "viewReportVo._departmentID");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "viewReportVo._departmentID");
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
	function checkInfo(){
		var userName = $("input[name='viewReportVo.userName']").val();
		var company = $("#company").val();
		if(!userName && !company){
			layer.alert("姓名和部门不能同时为空",{offset:'100px'});
			return false;
		}
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'personalQuery'"></s:set>
        <s:set name="selectedPanel" value="'viewColleagueWorkReport'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form class="form-horizontal" action="administration/process/viewColleagueWorkReportByconditions" onsubmit="return checkInfo()">
       	  <h3 class="sub-header" style="margin-top:0px;">日报查询</h3>
       	  	<div class="form-group">
				<label for="reimbursementNo" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" name="viewReportVo.userName" onkeyup="checkEmpty()" value="${viewReportVo.userName}">
			   		<input type="hidden" name="viewReportVo.userID" value="${viewReportVo.userID}">
			    </div>
		  	</div>
		  	<c:if test="${showDep}">
			<div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="viewReportVo._companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.viewReportVo._companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
			</c:if>
		  	<div class="form-group">
			    <label for="beginDate" class="col-sm-1 control-label">日期<span style="color:red"> *</span></label>
<%-- 			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="beginDate" name="viewReportVo.beginDate" value="${viewReportVo.beginDate }" 
			    	onclick="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" placeholder="开始时间" required>
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="endDate" name="viewReportVo.endDate" value="${viewReportVo.endDate }"
			    	 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d', minDate:'#F{$dp.$D(\'beginDate\')}' })" placeholder="结束时间" required>
			    </div> --%>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" id="endDate" name="viewReportVo.reportDate" value="${viewReportVo.reportDate }"
			    	 onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'%y-%M-%d'})" placeholder="汇报时间" required>
			    </div>
			    <div class="col-sm-2" style="padding-top:2px;">
			    	<button type="submit" class="btn btn-primary">查询</button>
			    </div>
		  	</div>
		 </form>
		  <h3 class="sub-header"></h3>
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:5%">序号</th>
                  <th style="width:8%">姓名</th>
                  <th style="width:10%">汇报日期</th>
                  <th style="width:10%">提交时间</th> 
                  <th style="width:8%">工作时长</th>                
                  <th>工作内容</th>
                  <th style="width:8%">数量</th>
                  <th style="width:9%">下达任务人</th>
                  <th style="width:8%">完成情况</th>
                  <th style="width:8%">完成用时</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty workReportDetailVOs}">
              	<s:set name="attendance_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="workReportDetailVO" value="#request.workReportDetailVOs" status="count">
              		<tr>
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#attendance_id+1"/></td>              			
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.name"/></td>              			
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.reportDate"/><br><s:property value="#workReportDetailVO.weekDay"/></td>
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.addTime"/></td>
              			<td rowspan="<s:property value='#workReportDetailVO.workContent.size()'/>"><s:property value="#workReportDetailVO.totalHours"/></td>
              			  <s:iterator id="workContent" value="#workReportDetailVO.workContent" status="sta">
              				<s:if test="#sta.index!=0"><tr></s:if>
				             <td><s:property value="#workContent" /></td> 
				             <td><s:property value="#workReportDetailVO.quantities[#sta.index]" /></td> 
				             <td><s:property value="#workReportDetailVO.assignTaskName[#sta.index]" /></td>
				             <td><s:property value="#workReportDetailVO.completeState[#sta.index]" /></td>
				             <td><s:property value="#workReportDetailVO.workHours[#sta.index]" /></td></tr>
				            </s:iterator>              			

              		<s:set name="attendance_id" value="#attendance_id+1"></s:set>
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
  </body>
</html>
