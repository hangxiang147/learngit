<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">

	$(function() {	
		//参加人
		$('body').on('click','.input_text',function (event) { 
			if($(".text_down ul").html() != ""){
			$(".text_down").show();
			event.stopPropagation();
			}
			
		});
		$('body').on('click','.text_down ul li',function () {
			var shtml=$(this).html();
			$(".text_down").hide();
			$("#agent").val(shtml.split("（")[0]);
			$("#agentFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#agentID").val(agent.split("@")[0]);
			$("#agentName").val(agent.split("@")[1]);
			var indexspan=agent.split("@")[0];
			if($("#namecy").html().indexOf(indexspan)!=-1){
				layer.alert("已存在", {offset:'100px'});
				return;
			}
			
			$("#namecy").append("<span>"+shtml.split("（")[0]+"<input type='hidden'  name='meetingVO.ppIDs' value="+agent.split("@")[0]+" /><a href=\"javasript:(0)\" onclick='deleteTag(this)' class=\"namecolse\">×</a></span>");
			$(".text_down ul").empty();
			$("#agent").css("text-indent",$("#namecy").width());

		});
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();$("#agent").val("");});  
		$('.inputout').click(function (event) {$(".text_down").show();});
		//抄送人
		$('body').on('click','.input_text3',function (event) { 
			if($(".text_down3 ul").html() != ""){
			$(".text_down3").show();
			event.stopPropagation();
			}
			
		});
		$('body').on('click','.text_down3 ul li',function () {
			var shtml=$(this).html();
			$(".text_down3").hide();
			$("#agent").val(shtml.split("（")[0]);
			$("#agentFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#agentID").val(agent.split("@")[0]);
			$("#agentName").val(agent.split("@")[1]);
			var indexspan=agent.split("@")[0];
			if($("#namecy3").html().indexOf(indexspan)!=-1){
				layer.alert("已存在", {offset:'100px'});
				return;
			}
			
			$("#namecy3").append("<span>"+shtml.split("（")[0]+"<input type='hidden'  name='meetingVO.ccIDs' value="+agent.split("@")[0]+" /><a href=\"javasript:(0)\" onclick='deleteTagCbc(this)' class=\"namecolse\">×</a></span>");
			$(".text_down3 ul").empty();
			$("#agentCbc").css("text-indent",$("#namecy3").width());

		});
		$(document).click(function (event) {$(".text_down3").hide();$(".text_down3 ul").empty();$("#agentCbc").val("");});  
		$('.inputout3').click(function (event) {$(".text_down3").show();});
		
		// 发起人
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
	});
		
	function deleteTag(data){
		$(data).parent().remove();
		$("#agent").css("text-indent",$("#namecy").width());
	}
	function deleteTagCbc(data){
		$(data).parent().remove();
		$("#agentCbc").css("text-indent",$("#namecy3").width());
	}
		
		function findStaffByName() {
			var name = $("#agent").val();
			if (name.length == 0) {
				return;
			}
			$(".text_down ul").empty();
			$.ajax({
				url:'personal/findStaffByName',
				type:'post',
				data:{name:name},
				dataType:'json',
				success:function (data){
					$.each(data.staffVOs, function(i, staff) {
						var groupDetail = staff.groupDetailVOs[0];
						$(".text_down ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
					});
					$(".text_down").show();
				}
			});
			
		}	
	

	function showAlert(message) {
			var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
					+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
					+"<span id=\"danger-message\">"+message+"</span>"
					+"</div>";
			$(".container-fluid").before(html);
		}
	function check() {
		if ($("#endDate").val() < $("#beginDate").val()) {
			showAlert("开始日期不得晚于结束日期！");
			return  false;
		}
		$("#submitButton").addClass("disabled");
		Load.Base.LoadingPic.FullScreenShow(null);
		return true;
	}
	window.history.forward();
	function changeUser() {
		var html = "<div class=\"col-sm-1\"></div>"
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
	//参加人
	 function showDepartment(obj, level, ii) {
			level = level+1;
			$(".flag"+ii+" .department"+level).remove();
			$(".flag"+ii+" .department1 select").removeAttr("name");
			if ($(obj).val() == '') {
				if (level > 2) {
					$(".flag"+ii+" #department"+(level-2)).attr("name", "meetingVO.ppDepartmentIDs");
				}
				if(level==2){
					$(".flag"+ii+" #department"+(level-1)).attr("name", "meetingVO.ppDepartmentIDs");
				}
				return;
			}
			
			var parentID = 0;
			if (level != 1) {
				parentID = $(obj).val();
				$(obj).attr("name", "meetingVO.ppDepartmentIDs");
			}
			var companyID = $($(obj).parent().parent().find("select")[0]).val();
			$.ajax({
				url:'HR/staff/findDepartmentsByCompanyIDParentID',
				type:'post',
				data:{companyID: companyID,
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
					
					var divObj = $(".flag"+ii+" #"+$(obj).attr('id')+"_div");
					if(level==1){
						$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
								+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+", "+ii+")\" name=\"meetingVO.ppDepartmentIDs\">"
								+"<option value=\"\">--"+level+"级部门--</option></select>"
								+"</div>");
						
					}else{
						$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
									+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+", "+ii+")\" >"
									+"<option value=\"\">--"+level+"级部门--</option></select>"
									+"</div>");
					}
					
					$.each(data.departmentVOs, function(i, department) {
						$(".flag"+ii+" #department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
					});
				}
			});
		}
	 var a=1;
	 //参加人
	 function addPtc(){
		 a++;
		 $("#participant").append(
				 '<div class="form-group flag'+a+'">'+
				 '<div class="col-sm-1 control-label"><a href="javasript:(0)" onclick="deleteAdd(this)">删除</a></div>'+
				 '<div class="col-sm-2" id="company'+a+'_div">'+
			    	'<select class="form-control" id="company'+a+'" name="meetingVO.ppCompanyIDs" onchange="showDepartment(this, 0, '+a+')" required="required">'+
				      '<option value="">请选择</option>'+
				      	'<s:iterator id="company" value="#request.companyVOs" status="st">'+
				      		'<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>'+
				      	'</s:iterator>'+
					'</select>'+
			    '</div>'+
			    '</div>');
	          }
	 //抄送人
	 function addCbc(){
		 a++;
		 $("#carbonCopy").append(
				 '<div class="form-group flag'+a+'">'+
				 '<div class="col-sm-1 control-label"><a href="javasript:(0)" onclick="deleteAdd(this)">删除</a></div>'+
				 '<div class="col-sm-2" id="company'+a+'_div">'+
			    	'<select class="form-control" id="company'+a+'" name="meetingVO.ccCompanyIDs" onchange="showDepartmentCbc(this, 0, '+a+')" required="required">'+
				      '<option value="">请选择</option>'+
				      	'<s:iterator id="company" value="#request.companyVOs" status="st">'+
				      		'<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>'+
				      	'</s:iterator>'+
					'</select>'+
			    '</div>'+
			    '</div>');
	          }
	 //抄送人
	 function showDepartmentCbc(obj, level, ii) {
			level = level+1;
			$(".flag"+ii+" .department"+level).remove();
			$(".flag"+ii+" .department1 select").removeAttr("name");
			if ($(obj).val() == '') {
				if (level > 2) {
					$(".flag"+ii+" #department"+(level-2)).attr("name", "meetingVO.ccDepartmentIDs");
				}
				if(level==2){
					$(".flag"+ii+" #department"+(level-1)).attr("name", "meetingVO.ccDepartmentIDs");
				}
				return;
			}
			
			var parentID = 0;
			if (level != 1) {
				parentID = $(obj).val();
				$(obj).attr("name", "meetingVO.ccDepartmentIDs");
			}
			var companyID = $($(obj).parent().parent().find("select")[0]).val();
			$.ajax({
				url:'HR/staff/findDepartmentsByCompanyIDParentID',
				type:'post',
				data:{companyID: companyID,
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
				
					var divObj = $(".flag"+ii+" #"+$(obj).attr('id')+"_div");
					if(level==1){
						$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
								+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartmentCbc(this, "+level+", "+ii+")\" name=\"meetingVO.ccDepartmentIDs\">"
								+"<option value=\"\">--"+level+"级部门--</option></select>"
								+"</div>");
						
					}else{
						$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
									+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartmentCbc(this, "+level+", "+ii+")\" >"
									+"<option value=\"\">--"+level+"级部门--</option></select>"
									+"</div>");
					}
					
					$.each(data.departmentVOs, function(i, department) {
						$(".flag"+ii+" #department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
					});
				}
			});
		}
	 
	 function deleteAdd(data){
		 $(data).parent().parent().remove();
	 }
	 
     function findStaffByName3() {
			var name = $("#agentCbc").val();
			if (name.length == 0) {
				return;
			}
			$(".text_down3 ul").empty();
			$.ajax({
				url:'personal/findStaffByName',
				type:'post',
				data:{name:name},
				dataType:'json',
				success:function (data){
					$.each(data.staffVOs, function(i, staff) {
						var groupDetail = staff.groupDetailVOs[0];
						$(".text_down3 ul").append("<li>"+staff.lastName+"（"+groupDetail.companyName+"—"+groupDetail.departmentName+"—"+groupDetail.positionName+"）<input type=\"hidden\" value="+staff.userID+"@"+staff.lastName+" /></li>");
					});
					$(".text_down3").show();
				}
			});
			
		}	


	

</script>
<style type="text/css">
		a:link {
		 text-decoration: none;
		}
		a:visited {
		 text-decoration: none;
		}
		a:hover {
		 text-decoration: none;
		}
		a:active {
		 text-decoration: none;
		}
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
	
.clear{clear:both;height:0;font-size:0px;_line-height:0px;}
#namecy{position:absolute;top:10px;left:10px;}
#namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;}
#namecy span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
	
.clear3{clear:both;height:0;font-size:0px;_line-height:0px;}
#namecy3{position:absolute;top:10px;left:10px;}
#namecy3 span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#00c0ff;float:left;margin-right:20px;}
#namecy3 span a{position:absolute;color:#ff0000;font-size:30px;top:-14px;right:-6px;}
	.inputout{position:relative;}
	.text_down{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down ul{padding:2px 10px;}
	.text_down ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down ul li span{color:#cc0000;}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
	.inputout3{position:relative;}
	.text_down3{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down3 ul{padding:2px 10px;}
	.text_down3 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down3 ul li span{color:#cc0000;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'meeting'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="administration/meeting/saveMeeting" method="post" enctype="multipart/form-data" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">新增会议</h3>
        	  <input type="hidden" id="userID"  value="${staff.userID }"/>
   			  <input type="hidden" id="userName"  value="${staff.lastName}"/> 
        	  
			  <!-- 发起人 -->
			  <input type="hidden" id="requestUserID" name="meetingVO.sponsorID" value="${staff.userID }"/>
   			  <input type="hidden" id="requestUserName" name="meetingVO.sponsorName" value="${staff.lastName}"/>
        	   <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">会议主题<span style="color:red"> *</span></label>
			    <div class="col-sm-4">
			    	<input type="text" class="form-control" id="topic" name="meetingVO.theme" required="required">
			    </div>
			  </div> 
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">发起人</label>
			  	<div class="col-sm-11"><span class="detail-control">${staff.lastName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser()">修改</a></span></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">会议地点<span style="color:red"> *</span> </label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="place" name="meetingVO.place" required="required">
			    </div>
			    <label for="executor" class="col-sm-1 control-label">会议类型<span style="color:red"> *</span></label>
			     <div class="col-sm-2">						
			          <select class="form-control"  name="meetingVO.meetingType" >
				      <option value="0">其他</option>
				      <option value="1">例会</option>
				      <option value="2">工作简会</option>
				      <option value="3">专题会议</option>
				      <option value="4">总结会议</option>
				      <option value="5">部门会议</option>
				     </select>
			     </div>
			  </div>       	
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-1 control-label">开始时间<span style="color:red"> *</span></label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="meetingVO.beginTime" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d${beginTime}'})" onBlur="calcTime()">	    			
	    		</div>
	    		<label for="endDate" class="col-sm-1 control-label">结束时间<span style="color:red"> *</span></label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="endDate" name="meetingVO.endTime" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d${beginTime}'})" onBlur="calcTime()">
	    			
	    		</div>
	    	  </div> 
			 <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">参与人</label>
			    <div class="col-sm-5 inputout">
			    <div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="agent" class="form-control" rows="3" oninput="findStaffByName()"></textarea>
			    	<input type="hidden" id="agentFlag" value="">
			    	<input type="hidden" id="agentID" name="vacationVO.agentID">
			    	<input type="hidden" id="agentName" name="vacationVO.agentName">
			    	</span>
			    	<div id="namecy">
			    	<div class="clear"></div>
			    	</div>
			    </div>
			    	<div class="text_down">
						<ul></ul>
					</div>
			    </div>
              <button type="button" onclick="addPtc()"  class="btn btn-primary">添加公司部门</button>
			  </div>

			  <div id="participant"></div>
			 <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">抄送人</label>
			    <div class="col-sm-5 inputout">
			    <div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="agentCbc" class="form-control" rows="3" oninput="findStaffByName3()"></textarea>
			    	<input type="hidden" id="agentFlag" value="">
			    	<input type="hidden" id="agentID" name="vacationVO.agentID">
			    	<input type="hidden" id="agentName" name="vacationVO.agentName">
			    	</span>
			    	<div id="namecy3">
			    	<div class="clear3"></div>
			    	</div>
			    </div>
			    	<div class="text_down3">
						<ul></ul>
					</div>
			    </div>
			    <button type="button" onclick="addCbc()"  class="btn btn-primary">添加公司部门</button>
			  </div>
			  
			  <div id="carbonCopy"></div>
			   
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">内容概述</label>
			    <div class="col-sm-5">
			    	<textarea class="form-control" rows="5" id="content" name="meetingVO.content" required="required"></textarea>
			    </div>
			  </div>
	
			  <div class="form-group">
			    <label for="uploadFile" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span>添加附件</label>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="upload"  style="padding:6px 0px;" multiple="multiple">
			    </div>
			  </div>
			   
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">消息提醒</label>
			    <div class="col-sm-3" style="padding-top:6px;">
			    	<input type="checkbox" name="meetingVO.isNotice"  value="1">发送消息通知给相关人员
			    </div>
			  </div> 
			  <div class="form-group">
			  <div class="col-sm-7"></div>
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">取消</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>