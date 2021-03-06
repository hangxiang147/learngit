<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){
		var departmentLevel = new Number($("#departmentLevel").val());
		$("#department"+departmentLevel).attr("name", "workOvertimeVo.departmentId");
	});
	function showDepartment(obj, level) {
		level = level+1;
		$(".department"+level).remove();
		$(".department1 select").removeAttr("name");
		if ($(obj).val() == '') {
			if (level > 2) {
				$("#department"+(level-2)).attr("name", "workOvertimeVo.departmentId");
			}
			return;
		}
		
		var parentID = 0;
		if (level != 1) {
			parentID = $(obj).val();
			$(obj).attr("name", "workOvertimeVo.departmentId");
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
		//若选择的是部门，获取部门下面所有的人员
		if(parentID!=0){
			Load.Base.LoadingPic.FullScreenShow(null);
			$.ajax({
				url:'HR/staff/getAllStaffsByDepId',
				data:{'depId':parentID,'companyId':$("#company").val()},
				success:function(data){
					var html = "";
					data.staffVos.forEach(function(value, index){
						html += "<span><span data-userid='"+value.userID+"' class='overTimeUser'>"+value.lastName+"<span data-toggle='tooltip' data-placement='right' title='删除' onclick='$(this).parent().parent().remove();' class='deleteUser'>x</span></span>&nbsp</span>";
					});
					$("#overTimeUser").html(html);
					$("[data-toggle='tooltip']").tooltip();
				},
				complete:function(){
					Load.Base.LoadingPic.FullScreenHide();
				}
			});
		}else{
			$("#overTimeUser").html("");
		}
	}
	//限制先选开始时间
	function checkBegintime(){
		if($("#endDate").val()!='' && $("#beginDate").val()==''){
			layer.alert("请先选择开始时间",{"offset":"100px"});
			$("#endDate").val("");
		}
	}
	//计算加班工时
	function calcTime(){
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
		if(beginDate!='' && endDate!=''){
			beginDate = beginDate.replace(/-/g,"/");
      		endDate = endDate.replace(/-/g,"/");
      		var differTime = new Date(endDate).getTime() - new Date(beginDate).getTime();
      		//加班时间按半小时起算，向下取值
      		var hours = Math.floor(differTime/(60*30*1000))/2;
      		//加班工时不足半小时
      		if(hours<=0){
      			layer.alert("加班工时不足半小时，无法申请加班",{"offset":"100px"});
      			$("#endDate").val("");
      			$("input[name='workOvertimeVo.workHours']").val("");
      			return;
      		}
      		$("input[name='workOvertimeVo.workHours']").val(hours+"小时");
		}
	}
	function changeUser() {
		var html = '<div class="col-sm-2">'+
	    		   '<input type="text" id="user" required autoComplete="off" class="form-control" onkeyup="checkEmpty()"/>'+
		    	   '</div><a href="javascript:void(0)" style="display:inline-block;margin-top:0.5%" id="delete" onclick="deleteUser()">撤销</a>';
		$("#executor_div").append(html);
	    $("#applyer").css("display","none");
	    $("input[name='workOvertimeVo.requestUserID']").val("");
	    require(['staffComplete'],function (staffComplete){
			new staffComplete().render($('#user'),function ($item){
				$("input[name='workOvertimeVo.requestUserID']").val($item.data("userId"));
				$("input[name='workOvertimeVo.requestUserName']").val($item.data("userName"));
			});
		});
	}
	function deleteUser(){
		$("#executor_div div").each(function(i, obj) {
			if (i != 0) {
				$(obj).remove();
			}
		});
		$("#delete").remove();
		$("#applyer").css("display","block");
		$("input[name='workOvertimeVo.requestUserID']").val($("input[name='workOvertimeVo.userID']").val());
		$("input[name='workOvertimeVo.requestUserName']").val($("input[name='workOvertimeVo.userName']").val());
		
	}
	$(document).click(function (event) {
		if ($("input[name='workOvertimeVo.requestUserID']").val()=='')
		{
			$("#user").val("");
		}
	});  
	function inputUserIds(){
		if('${objectType}'=='个人'){
			return;
		}
		var userIds = "";
		$(".overTimeUser").each(function(){
			if(!userIds){
				userIds += $(this).attr("data-userid");
			}else{
				userIds += ','+$(this).attr("data-userid");
			}
		});
		if(!userIds){
			layer.alert("加班人员不可为空", {offset:'100px'});
			return false;
		}
		$("input[name='workOvertimeVo.requestUserID']").val(userIds);
		Load.Base.LoadingPic.FullScreenShow(null);
	}
</script>
<style type="text/css">
	.overTimeUser{
		color:#ffffff;
		background:#75b9f3;
		border-radius: 10px;
		padding:5 10;
		display: inline-block;
		margin-top: 5px;
		margin-bottom: 2px;
		position: relative;
	}
	.deleteUser{
		color:red;
		font-size:13px;
		font-weight:bold;
		position:absolute;
		top:-10%;
		cursor:pointer;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
         <%@include file="/pages/personal/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startWorkOvertime" method="post" class="form-horizontal" onsubmit="return inputUserIds()" enctype="multipart/form-data">
        	  <s:token></s:token>
        	  <h2 class="sub-header" style="margin-top:0px;">发起加班申请<span style="color:red;font-size:20px">（加班时间按半小时起算）</span></h2> 
        	  <!-- 发起人 -->
		 	  <input style="display:none" name="workOvertimeVo.userName"  value="${staff.lastName}"/>
 			  <input style="display:none" name="workOvertimeVo.userID"  value="${staff.userID}"/>
 			  <input type="hidden" name="workOvertimeVo.type" value="${objectType}"/>
 			  <!-- 实际加班人员 -->
 			  <input type="hidden" name="workOvertimeVo.requestUserName" value="${staff.lastName}"/>
 			  <input type="hidden" name="workOvertimeVo.requestUserID" value="${staff.userID}"/>
 			  <c:if test="${objectType=='个人'}">
			  <div class="form-group" id="executor_div">
			  	<label class="col-sm-1 control-label" style="width:11%">加班人员<span style="color:red"> *</span></label>
			  	<div class="col-sm-3" id="applyer">
			  	<span style="display:inline-block;margin-top:2.5%" class="detail-control">${staff.lastName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser()">修改</a></span>
			  	</div>
			  </div>
			  </c:if>
			  <c:if test="${objectType=='部门'}">
			  	<div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:11%">加班部门<span style="color:red"> *</span></label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="workOvertimeVo.companyId" onchange="showDepartment(this, 0, 1)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companys }">
				      	<s:iterator id="company" value="#request.companys" status="st">
				      		<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			   	</div>
			   	</div>
			   	<div class="form-group">
			   		<label class="col-sm-1 control-label" style="width:11%">加班人员<span style="color:red"> *</span></label>
			   		<div id="overTimeUser" class="col-sm-5" style="min-height:100px;border:1px solid #ccc;margin-left:1.3%">
			   		</div>
			   	</div>
			  </c:if>
			  <div class="form-group" id="beginDate_div">
	    		<label class="col-sm-1 control-label" style="width:11%">开始时间<span style="color:red"> *</span></label>
	    		<div class="col-sm-3">
	    			<input type="text" class="form-control" id="beginDate" name="workOvertimeVo.beginDate" autoComplete="off"
	    			 required onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d ${workOverBeginTime}:00', maxDate:'%y-%M-%d 23:59:59'})" onBlur="calcTime()" />
	    		</div>
	    	  </div> 
	    	  <div class="form-group" id="endDate_div">
	    		<label class="col-sm-1 control-label" style="width:11%">结束时间<span style="color:red"> *</span></label>
	    		<div class="col-sm-3">
	    			<input type="text" class="form-control" id="endDate" name="workOvertimeVo.endDate" autoComplete="off"
	    			required onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'beginDate\')}', maxDate:'${beginDay}${beginTime}'})" onBlur="checkBegintime(), calcTime()"/>
	    			
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
	    	  	<label class="col-sm-1 control-label" style="width:11%">预计加班工时</label>
	    	  	<div class="col-sm-3"><input class="form-control" readonly name="workOvertimeVo.workHours" ></div>
	    	  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:11%">原因<span style="color:red"> *</span></label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="reason" name="workOvertimeVo.reason" autoComplete="off" required/>
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary" style="margin-left:3%">提交</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
    <script src="/assets/js/require/require2.js"></script>
    <script>
  	function checkEmpty(){
  		if($("#user").val()==''){
  			$("input[name='workOvertimeVo.requestUserID']").val('');
  		}
  	}
    </script>
</body>
</html>