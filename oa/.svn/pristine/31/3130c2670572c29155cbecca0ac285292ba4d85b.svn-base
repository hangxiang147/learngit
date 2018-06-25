<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	 $(function() {
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "departmentId");
		$("[data-toggle='tooltip']").tooltip();
		
		var statusEctype = $("#statusEctype").val();
		var name_Ectype = $("#name_Ectype").val();
		
		
		if(statusEctype!=''){
			$("#status").find("option[value='"+statusEctype+"']").attr("selected","selected");
		}else{
			$("#status").find("option[value='请选择']").attr("selected","selected");
		}
		if(name_Ectype!=''){
			$("#name_").find("option[value='"+name_Ectype+"']").attr("selected","selected");
		}else{
			$("#name_").find("option[value='请选择']").attr("selected","selected");
		}
		
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
	 
	 
	function search() {
		window.location.href = "attendance/workOvertimeList?" + $("#form").serialize();
		Load.Base.LoadingPic.FullScreenShow(null);
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
      	<s:set name="panel" value="'process'"></s:set>
        <%@include file="/pages/attendance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form id="form" class="form-horizontal">
       	  <h3 class="sub-header" style="margin-top:0px;">加班列表</h3>
       	  <div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="companyId" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="companyId == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
				<label for="name" class="col-sm-1 control-label">加班人员</label>
			    <div class="col-sm-2">
			    	<input type="text" id="user" class="form-control"   autoComplete="off" name="userName" value="${userName}" onkeyup="checkEmpty()"/>
			    	<input type="hidden" name="userId" value="${userId}"/>
			    </div>
			    
			    <label for="beginDate" class="col-sm-1 control-label">开始时间</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="beginDate" value="${beginDate}"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onBlur="calcTime()" placeholder="起点时间" />
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endDate" value="${endDate}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onBlur="calcTime()" placeholder="终点时间"/>
			    </div>
			</div>
				
       	  <div class="form-group">
				<label for="status" class="col-sm-1 control-label">流程状态</label>
				<div class="col-sm-2">
					<select id="status" name="status" class="form-control">
						<option value="">请选择</option>
						<option value="进行中">进行中</option>
						<option value="已完结">已完结</option>
					</select>
					<input type="hidden" class="form-control" id="statusEctype" value="${status }">
				</div>
				
				<label for="name_" class="col-sm-1 control-label">当前环节</label>
				<div class="col-sm-2">
					<select id="name_" name="name_" class="form-control">
						<option value="">请选择</option>
						<option value="主管审批">主管审批</option>
						<option value="人力资源审批">人力资源审批</option>
						<option value="总经理审批">总经理审批</option>
					</select>
					<input type="hidden" class="form-control"  id="name_Ectype" value="${name_ }">
				</div>
				
				<label for="assignee" class="col-sm-1 control-label">待审批人</label>
				<div class="col-sm-2">
					<input type="text" id="assignee" class="form-control" name="assignee" value="${assignee }">
				</div>
				
				<button type="button" id="submitButton" class="btn btn-primary" onclick="search()" style="margin-left:20px;">查询</button>
			</div>
			
		  <h3 class="sub-header"></h3>
		  
          </form>
       	  	<div class="table-responsive">
       	  		<table class="table table-striped">
       	  			<thead>
       	  				<tr>
       	  					<th style="width: 5%">序号</th>
       	  					<th style="width: 15%">部门</th>
       	  					<th style="width: 10%">加班人员</th>
       	  					<th style="width: 15%">加班原因</th>
							<th style="width: 10%">开始时间</th>
							<th style="width: 10%">结束时间</th>
							<th style="width: 10%">预计加班工时</th>
							<th style="width: 10%">流程状态</th>
							<th style="width: 10%">当前环节<br>/待审批人</th>
							<th>操作</th>
       	  				</tr>
       	  				
       	  			</thead>
       	  			<tbody>
       	  				<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${workOvertimeVos}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.department}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.reason}</td>
			              		<td>${item.beginDate}</td>
			              		<td>${item.endDate}</td>
			              		<td>${item.workHours}</td>
			              		<td>${item.status}</td>
			              		<td>
				              		<c:choose>
					              		<c:when test="${item.thecurrenLink==null || item.thecurrenLink=='' }">
					              			――――――
					              		</c:when>
					              		<c:when test="${item.thecurrenLink!=null && item.thecurrenLink!='' && item.candidateUsers==null || item.candidateUsers=='' }">
					              			${item.thecurrenLink}<br>――――――
					              		</c:when>
					              		<c:otherwise>
					              			${item.thecurrenLink}<br>${item.candidateUsers}
					              		</c:otherwise>
			              			</c:choose>
			              		</td>
			              		<td>
			              		<a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findProcessList')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批详情" data-toggle="tooltip">
    								<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
			              		</a></td>
         					 </tr>
		              	</c:forEach>
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
        <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel">审批详情</h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="hidden" id="groupDetailID" />
	      	<p id="ntcContent"></p>
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
	<script src="/assets/js/require/require2.js"></script>
	<script src="/assets/js/underscore-min.js"></script>
    <script>
	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#user'),function ($item){
			$("input[name='userId']").val($item.data("userId"));
		});
	});
  	function checkEmpty(){
  		if($("#user").val()==''){
  			$("input[name='userId']").val('');
  		}
  	}
	$(document).click(function (event) {
		if ($("input[name='userId']").val()=='')
		{
			$("#user").val("");
		}
	}); 
    </script>
  </body>
</html>
