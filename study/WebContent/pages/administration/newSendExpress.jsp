<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

	$(function() {

		$('body').on('click','.input_text1',function (event) { 
			if($(".text_down1 ul").html() != ""){
				$(".text_down1").show();
				event.stopPropagation();
			}
			$('body').on('click','.text_down1 ul li',function () {
				var shtml=$(this).html();
				$(".text_down1").hide();
				$("#request").val(shtml.split("（")[0]);
				$("#requestFlag").val(shtml.split("（")[0]);
				var request = $(this).find("input").val();
				$("#requestUserID").val(request.split("@")[0]);
				$("#requestUserName").val(request.split("@")[1]);
				$.ajax({
					url:'personal/getWorkTimeByUserID',
					type:'post',
					data:{userID: $("#requestUserID").val()},
					dataType:'json',
					success:function (data){
						if (data.errorMessage!=null && data.errorMessage.length!=0) {
							showAlert(data.errorMessage);
							return;
						}
						$("#dailyHours").val(data.dailyHours);
						$("#beginTime").val(data.beginTime);
						$("#endTime").val(data.endTime);
						
					}
				});
			});
		}); 
		$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#request").val()!=$("#requestFlag").val()) {$("#request").val("");}});  
		$('.inputout1').click(function (event) {$(".text_down1").show();});
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
	
	
	
	
	
	function changeUser() {
		var html = "<div class=\"col-sm-2\"></div>"
				+"<div class=\"col-sm-2 inputout1\">"
   				+"<span class=\"input_text1\">"
   				+"<input type=\"text\" id=\"request\" class=\"form-control\" required=\"required\" oninput=\"findStaffByName1()\"  />"
   				+"<input type=\"hidden\" id=\"requestFlag\" value=\"\"/>"
   				+"</span>"
   				+"<div class=\"text_down1\">"
   				+"<ul></ul>"
   				+"</div>"
   				+"</div>";
		$("#executor_div").append(html);
		
	    $("#executor_div a").text("撤销");
	    $("#executor_div a").attr("onclick", "deleteUser()");
	}
	
	function deleteUser() {
		$("#executor_div div").each(function(i, obj) {
			if (i != 0) {
				$(obj).remove();
			}
		});
		$("#executor_div a").text("修改");
	    $("#executor_div a").attr("onclick", "changeUser()");
	  
	    $("#requestUserID").val($("#userID").val());
		$("#requestUserName").val($("#userName").val());
	  
	}
	
	
	
	function findStaffByName1() {
		var name = $("#request").val();
		if (name.length == 0) {
			return;
		}
		$(".text_down1 ul").empty();
		$.ajax({
			url:'personal/findStaffByName',
			type:'post',
			data:{name:name},
			dataType:'json',
			success:function (data){
				$.each(data.staffVOs, function(i, staff) {
					var groupDetail = staff.groupDetailVOs[0];
					$(".text_down1 ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
				});
				$(".text_down1").show();
			}
		});
		
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
	 function weekDay(obj){
		 
		 var wk="";
		 switch(new Date(obj.value).getDay()){
					 case 0:
					 wk="星期日";
					 break;
					 case 1:
					 wk="星期一";
					 break;
					 case 2:
					 wk="星期二";
					 break;
					 case 3:
					 wk="星期三";
					 break;
					 case 4:
					 wk="星期四";
					 break;
					 case 5:
					 wk="星期五";
					 break;
					 case 6:
					 wk="星期六";
					 break;
		 }
		 $("#weekDay").html(wk);
		 $("#inWeekDay").val(wk);
		 
	 }
	
	
</script>
<style type="text/css">
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
       <s:set name="panel" value="'express'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="addVacationForm"  action="administration/express/saveSendExpress"  method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">新增快递</h3>
        	  <input type="hidden" id="userID"  value="${staff.userID }"/>
   			  <input type="hidden" id="userName"  value="${staff.lastName}"/> 
        	  
			  <!-- 寄件人 -->
			  <input type="hidden" id="requestUserID" name="sendExpressVO.userID" value="${staff.userID }"/>
   			  <input type="hidden" id="requestUserName" name="sendExpressVO.userName" value="${staff.lastName}"/>
			  <div class="form-group">
			  	<label for="type" class="col-sm-2 control-label">类型</label>
			  	<div class="col-sm-2">						
					<select class="form-control"  name="sendExpressVO.type" id="type" required="required">
				      <option value="">请选择</option>
				      <option value="1" >寄付</option>
				      <option value="2" >到付</option>
					</select>
				</div>
			  </div>
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-2 control-label">寄件（收货）人</label>
			  	<div class="col-sm-10"><span class="detail-control">${staff.lastName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser()">修改</a></span></div>
			  </div>
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-2 control-label">寄件（收货）日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="sendExpressVO.postDate" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})"  onblur="weekDay(this)"/>
	    		</div>
	    		<span id="weekDay"></span>
	    		<input type="hidden" name="sendExpressVO.weekDay" id="inWeekDay">
	    	  </div> 
	    	  <div class="form-group">
				<label for="company" class="col-sm-2 control-label">公司部门</label>
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
			 <label for="expressCompany" class="col-sm-2 control-label">物流公司</label>
			  	<div class="col-sm-2">						
					<select class="form-control"  name="sendExpressVO.expressCompany" id="expressCompany" required="required">
				      <option value="">请选择</option>
				      <option value="1" >申通</option>
				      <option value="2" >EMS</option>
				      <option value="3" >顺丰</option>
				      <option value="4" >圆通</option>
				      <option value="5" >中通</option>
				      <option value="6" >韵达</option>
				      <option value="7" >汇通</option>
				      <option value="8" >全峰</option>
				      <option value="9" >德邦</option>
				      <option value="10" >邮政</option>
				      <option value="11" >跨越</option>
				      <option value="12" >百世</option>
				     					</select>				</div>
			  </div>
			 <div class="form-group">
			  	<label for="expressNumber" class="col-sm-2 control-label">快递单号</label>
			    <div class="col-sm-4">
			    	<input type="text" class="form-control" id="expressNumber" name="sendExpressVO.expressNumber" required="required"/>
			    </div><font color="red">注：如有多个请按;（分号）隔开</font>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-2 control-label">原因</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="reason" name="sendExpressVO.reason" />
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>