<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		set_href();
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "sendExpressVO.departmentID");
	});
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function search() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "administration/express/findSendExpressList?" + decodeURIComponent(params,true);
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	
	function exportExcel() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "administration/express/exportSendExpressList?" + decodeURIComponent(params,true);
	}
	
	 function showDepartment(obj, level) {
			level = level+1;
			$(".department"+level).remove();
			$(".department1 select").removeAttr("name");
			if ($(obj).val() == '') {
				if (level > 2) {
					$("#department"+(level-2)).attr("name", "sendExpressVO.departmentID");
				}
				return;
			}
			
			var parentID = 0;
			if (level != 1) {
				parentID = $(obj).val();
				$(obj).attr("name", "sendExpressVO.departmentID");
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
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'express'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="sendExpressForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">快递账单明细</h3>
         		<div class="form-group">
          			<label for="beginDate" class="col-sm-1 control-label">寄件（收货）人</label>
			    <div class="col-sm-2">
	           		<input type="text" class="form-control"  name="sendExpressVO.userName" value="${sendExpressVO.userName}" >
			    </div>
          		</div>   			    
          		<div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="sendExpressVO.companyID" onchange="showDepartment(this, 0)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" <s:if test="#request.sendExpressVO.companyID == #company.companyID ">selected="selected"</s:if>><s:property value="#company.companyName"/></option>
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
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="sendExpressVO.beginDate" value="${sendExpressVO.beginDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-1" style="width:4%"></div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="sendExpressVO.endDate" value="${sendExpressVO.endDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}'})" placeholder="结束时间">
			    </div>
			</div>
			<div class="form-group">
			<label for="beginDate" class="col-sm-1 control-label">物流公司</label>
			<div class="col-sm-2">						
			<select class="form-control"  name="sendExpressVO.expressCompany" >
		      <option value="">请选择</option>
		      <option value="1" <s:if test="#request.sendExpressVO.expressCompany == 1 ">selected="selected"</s:if> >申通</option>
		      <option value="2" <s:if test="#request.sendExpressVO.expressCompany == 2 ">selected="selected"</s:if> >EMS</option>
		      <option value="3" <s:if test="#request.sendExpressVO.expressCompany == 3 ">selected="selected"</s:if> >顺丰</option>
		      <option value="4" <s:if test="#request.sendExpressVO.expressCompany == 4 ">selected="selected"</s:if> >圆通</option>
		      <option value="5" <s:if test="#request.sendExpressVO.expressCompany == 5 ">selected="selected"</s:if> >中通</option>
		      <option value="6" <s:if test="#request.sendExpressVO.expressCompany == 6 ">selected="selected"</s:if> >韵达</option>
		      <option value="7" <s:if test="#request.sendExpressVO.expressCompany == 7 ">selected="selected"</s:if> >汇通</option>
		      <option value="8" <s:if test="#request.sendExpressVO.expressCompany == 8 ">selected="selected"</s:if> >全峰</option>
		      <option value="9" <s:if test="#request.sendExpressVO.expressCompany == 9 ">selected="selected"</s:if> >德邦</option>
		      <option value="10" <s:if test="#request.sendExpressVO.expressCompany == 10 ">selected="selected"</s:if> >邮政</option>
		      <option value="11" <s:if test="#request.sendExpressVO.expressCompany == 11 ">selected="selected"</s:if> >跨越</option>
		      <option value="12" <s:if test="#request.sendExpressVO.expressCompany == 12 ">selected="selected"</s:if> >百世</option>
		      
			</select>
			</div>
			<label for="myType" class="col-sm-1 control-label" style="width:4%">类型</label>
			<div class="col-sm-2">						
			<select class="form-control" id="myType" name="sendExpressVO.type" >
		      <option value="">请选择</option>
		      <option value="1" <s:if test="#request.sendExpressVO.type == 1 ">selected="selected"</s:if> >寄付</option>
		      <option value="2" <s:if test="#request.sendExpressVO.type == 2 ">selected="selected"</s:if> >到付</option>
			</select>
			</div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
			<button type="button" id="exportButton" class="btn btn-primary" onclick="exportExcel()" style="margin-left:20px;"><span class="glyphicon glyphicon-export"></span> 导出</button>
          </form>
           <a class="btn btn-primary" onclick="goPath('administration/express/addSendExpress')"  href="javascript:void(0)"><span class="glyphicon glyphicon-plus"></span> 快递记录</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">类型</th>
                  <th class="col-sm-1" style="padding-left:0px;padding-right:0px;">寄件（收货）人</th>
                  <th class="col-sm-2">寄件（收货）日期</th> 
                  <th class="col-sm-1">地区</th>
                  <th class="col-sm-1">部门</th>
                  <th class="col-sm-1">物流公司</th>
                  <th class="col-sm-1">物流单号</th>
                  <th class="col-sm-1">寄件原因</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty sendExpressVOs}">
              	<s:set name="attendance_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="sendExpress" value="#request.sendExpressVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#attendance_id+1"/></td>   
              			<td class="col-sm-1"><s:if test="#sendExpress.type == 1">寄付</s:if><s:else>到付</s:else></td>           			
              			<td class="col-sm-1"><s:property value="#sendExpress.userName"/></td>              			
              			<td class="col-sm-2"><s:property value="#sendExpress.postDate"/>（<s:property value="#sendExpress.weekDay"/>）</td>              			
              			           			
              			<td class="col-sm-1"><s:if test="#sendExpress.companyID==1">骑岸</s:if><s:if test="#sendExpress.companyID==2">如东</s:if><s:if test="#sendExpress.companyID==3">南通</s:if><s:if test="#sendExpress.companyID==4">广州</s:if><s:if test="#sendExpress.companyID==5">南京</s:if><s:if test="#sendExpress.companyID==6">佛山</s:if></td>              			
              			<td class="col-sm-1"><s:property value="#sendExpress.departmentName"/></td>              			
              			<td class="col-sm-1"><s:if test="#sendExpress.expressCompany==1">申通</s:if><s:if test="#sendExpress.expressCompany==2">EMS</s:if><s:if test="#sendExpress.expressCompany==3">顺丰</s:if><s:if test="#sendExpress.expressCompany==4">圆通</s:if><s:if test="#sendExpress.expressCompany==5">中通</s:if><s:if test="#sendExpress.expressCompany==6">韵达</s:if><s:if test="#sendExpress.expressCompany==7">汇通</s:if><s:if test="#sendExpress.expressCompany==8">全峰</s:if><s:if test="#sendExpress.expressCompany==9">德邦</s:if><s:if test="#sendExpress.expressCompany==10">邮政</s:if><s:if test="#sendExpress.expressCompany==11">跨越</s:if>
              			<s:if test="#sendExpress.expressCompany==12">百世</s:if>
              			
              			
              			</td>              			
              			<td class="col-sm-1"><s:property value="#sendExpress.expressNumber"/></td>              			
              			<td class="col-sm-1"><s:property value="#sendExpress.reason"/></td>              			
              		</tr>
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
