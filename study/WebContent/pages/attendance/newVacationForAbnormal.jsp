<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">

	$(function() {		
		
		$('body').on('click','.input_text',function (event) { 
			if($(".text_down ul").html() != ""){
			$(".text_down").show();
			event.stopPropagation();
			}
			$('body').on('click','.text_down ul li',function () {
			var shtml=$(this).html();
			$(".text_down").hide();
			$("#agent").val(shtml.split("（")[0]);
			$("#agentFlag").val(shtml.split("（")[0]);
			var agent = $(this).find("input").val();
			$("#agentID").val(agent.split("@")[0]);
			$("#agentName").val(agent.split("@")[1]);
			});
		}); 
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#agent").val()!=$("#agentFlag").val()) {$("#agent").val("");}});  
		$('.inputout').click(function (event) {$(".text_down").show();});
		
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
				checkAnnualVacation();
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
						calcTime();
					}
				});
			});
		}); 
		$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#request").val()!=$("#requestFlag").val()) {$("#request").val("");}});  
		$('.inputout1').click(function (event) {$(".text_down1").show();});
	});

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
		var agentID = $("#agentID").val();
		var requestUserID = $("#requestUserID").val();
		if(agentID==requestUserID){
			layer.alert("工作代理人不可以与请假人是同一人",{offset:'100px'})
			return false;
		}
		/* var date = $("#date").val();
		var beginDate = $("#beginDate").val();
		var endDate = $("#endDate").val();
		var beginDateEctype = beginDate.substr(0,10);
		var endDateEctype = endDate.substr(0,10);
		if($.trim(beginDateEctype) != $.trim(date) || $.trim(endDateEctype) != $.trim(date)){
			layer.alert("开始时间和结束时间必须是异常当天日期",{offset:'100px'});
			return false
		} */
		$("#submitButton").addClass("disabled");
		Load.Base.LoadingPic.FullScreenShow(null);
		return true;
	}
	function checkAnnualVacation(){
		var vacationType = $("#vacationType").find("option:selected").val();
		var requestUserID = $("#requestUserID").val();
		var begin = $("#beginDate").val();
		var end = $("#endDate").val();
		//年假
		if(vacationType=='3' && requestUserID && begin && end){
			$.ajax({
				url:'attendance/checkAnnualVacation',
				data:{'requestUserID':requestUserID,'beginDate':begin,'endDate':end},
				success:function(data){
					if(data.result!='true'){
						layer.alert(data.result,{offset:'100px'});
						$("#submitButton").addClass("disabled");
					}else{
						$("#submitButton").removeClass("disabled");
					}
				}
			});
		}else{
			$("#submitButton").removeClass("disabled");
		}
	}
	function autoEndDate() {
		//结束时间默认为开始时间所在当天的下班时间
		var begin = $("#beginDate").val();
		var end = $("#endDate").val();
		if(begin != '' && end == ''){
			end = begin.split(" ")[0] + $("#endTime").val();
			$("#endDate").val(end);
			calcTime();
		}
	}
	
	function calcTime() {
		var begin = $("#beginDate").val();
		var end = $("#endDate").val();
		if (begin == '' || end == '') {
			return;
		}
		
		/*var beginDate = new Date(begin.replace(/-/g, "/"));
		var endDate = new Date(end.replace(/-/g, "/"));
		var breakTime = new Date(("2000-01-02"+$("#beginTime").val()).replace(/-/g, "/")).getTime() - 
							new Date(("2000-01-01"+$("#endTime").val()).replace(/-/g, "/")).getTime();
		var diff = endDate.getTime() - beginDate.getTime();
		var beginMonth = beginDate.getMonth();
		var beginDay = beginDate.getDate();
		var endMonth = endDate.getMonth();
		var endDay = endDate.getDate();
		var diffDate = 0;
		while (beginMonth != endMonth) {
			diffDate += (new Date(new Date(beginDate.getYear(), beginMonth+1, 1).getTime() - 1000*3600*24).getDate() - beginDay + 1);
			beginMonth += 1;
			beginDay = 1;
		}
		diffDate += (endDay-beginDay);
		var vacaTime = diff - breakTime*diffDate;
			var diff1 = new Date((begin.split(" ")[0]+$("#beginTime").val()).replace(/-/g, "/")).getTime()-beginDate.getTime();
			var diff2 = endDate.getTime() - new Date((end.split(" ")[0]+$("#endTime").val()).replace(/-/g, "/")).getTime();
			if (diff1 > 0) {
				vacaTime = vacaTime - diff1;
			}
			if (diff2 > 0) {
				vacaTime = vacaTime - diff2;
			}
			var diff3 = new Date((end.split(" ")[0]+$("#beginTime").val()).replace(/-/g, "/")).getTime() - endDate.getTime();
			var diff4 = beginDate.getTime() - new Date((begin.split(" ")[0]+$("#endTime").val()).replace(/-/g, "/")).getTime();
			if (diff3 > 0) {
				vacaTime = vacaTime + diff3;
			}
			if (diff4 > 0) {
				vacaTime = vacaTime + diff4;
			}
		var hours = Math.ceil(vacaTime/(60*30*1000))/2;
		var dailyTime = new Date(("2000-01-01"+$("#endTime").val()).replace(/-/g, "/")).getTime() - 
							new Date(("2000-01-01"+$("#beginTime").val()).replace(/-/g, "/")).getTime();
		var dailyHours = Math.ceil(dailyTime/(60*30*1000))/2;
		var day = Math.floor(hours/dailyHours);
		var hour = hours-day*dailyHours;
		
		var text = "";
		var daily = $("#dailyHours").val();
		if (day != 0 && hour != 0) {
			text = day+"天"+hour+"小时";
		} else if (day == 0 && hour != 0) {
			text = hour+"小时";
		} else if (day != 0 && hour == 0) {
			text = day+"天";
		} */
		//原先的计算逻辑没有考虑午休，重新计算
		$.ajax({
			url:'attendance/calVacationTime',
			type:'post',
			data:{'beginTime':begin,'endTime':end,userID: $("#requestUserID").val()},
			success:function(data){
				$("#daysText").text(data.vacationTextAndHours[0]);
				$("#showHours").val(data.vacationTextAndHours[1]);
			}
		});
	}
	
	function changeUser() {
		var html = "<div class=\"col-sm-1\"></div>"
				+"<div class=\"col-sm-2 inputout1\">"
   				+"<span class=\"input_text1\">"
   				+"<input type=\"text\" id=\"request\" autoComplete=\"off\" class=\"form-control\" required=\"required\" oninput=\"findStaffByName1()\"/>"
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
	    $("#dailyHours").val($("#userDailyHours").val());
	    $("#beginTime").val($("#userBeginTime").val());
	    $("#endTime").val($("#userEndTime").val());
	    $("#requestUserID").val($("#userID").val());
	    checkAnnualVacation();
		$("#requestUserName").val($("#userName").val());
	    calcTime();
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
	function checkMarriageHoliday(){
		var vacationType = $("#vacationType").find("option:selected").val();
		var requestUserID = $("#requestUserID").val();
		var begin = $("#beginDate").val();
		var end = $("#endDate").val();
		if(vacationType=='4' && requestUserID && begin && end){
			var showHours = $("#showHours").val();
			var dailyHours = $("#userDailyHours").val();
			if(parseFloat(showHours) > 7*parseFloat(dailyHours)){
				layer.alert("婚假不能超过7天",{offset:'100px'});
				$("#vacationType").find("option:selected").removeAttr("selected");
			}
			$.ajax({
				url:'attendance/checkMarriageHoliday',
				data:{'userId':requestUserID},
				success:function(data){
					if(!data.hasMarriageHoliday){
						layer.alert("无法休婚假",{offset:'100px'});
						$("#vacationType").find("option:selected").removeAttr("selected");
					}
				}
			});
		}
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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
         <%@include file="/pages/attendance/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="attendance/saveVacation" method="post" class="form-horizontal" onsubmit="return check()" enctype="multipart/form-data">
        	  <s:token></s:token>
        	  <h2 class="sub-header" style="margin-top:0px;">新增请假</h2> 
        	  <!-- 填写请假申请的操作人 -->
        	  <%-- <input type="text" id="date" value="${date }" /> --%>
        	  <input type="hidden" id="userDailyHours" value="${dailyHours }" />
        	  <input type="hidden" id="userBeginTime" value="${beginTime }" />
        	  <input type="hidden" id="userEndTime" value="${endTime }" />
        	  <input type="hidden" id="dailyHours" value="${dailyHours }" name="vacationVO.dailyHours" />
        	  <input type="hidden" id="beginTime" value="${beginTime }" />
        	  <input type="hidden" id="endTime" value="${endTime }" />
			  <input type="hidden" id="userID" name="vacationVO.userID" value="${user.id}"/>
			  <input type="hidden" id="userName" name="vacationVO.userName" value="${userName }" />
			  <!-- 请假人 -->
			  <input type="hidden" id="requestUserID" name="vacationVO.requestUserID" value="${staff.userID}"/>
   			  <input type="hidden" id="requestUserName" name="vacationVO.requestUserName" value="${staff.lastName}"/>
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">请假人</label>
			  	<div class="col-sm-11"><span class="detail-control">${staff.lastName }</span></div>
			  </div>
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-1 control-label">开始时间<span style="color:red"> *</span></label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="vacationVO.beginDate"
	    			 required="required" onclick="WdatePicker({dateFmt:'${date } HH:mm:ss',maxDate:'#F{$dp.$D(\'endDate\')}'})" onBlur="calcTime(),autoEndDate(),checkAnnualVacation(),checkMarriageHoliday()" />
	    			<!-- <input type="hidden" id="beginDateVal" name="vacationVO.beginDate" /> -->
	    		</div>
	    	  </div> 
	    	  <div class="form-group" id="endDate_div">
	    		<label for="endDate" class="col-sm-1 control-label">结束时间<span style="color:red"> *</span></label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="endDate" name="vacationVO.endDate"
	    			 required="required" onclick="WdatePicker({dateFmt:'${date } HH:mm:ss'})" onBlur="calcTime(),checkAnnualVacation(),checkMarriageHoliday()"/>
	    			<!-- <input type="hidden" id="endDateVal" name="vacationVO.endDate" /> -->
	    		</div>
	    	  </div> 
			  <div class="form-group">
			    <label for="days" class="col-sm-1 control-label">请假天数</label>
			    <div class="col-sm-2">
			    	<span id="daysText" class="detail-control"></span>
			    	<!-- <input type="hidden" id="hours" name="vacationVO.hours" />
			    	<input type="hidden" id="days" name="vacationVO.days" /> -->
			    	<input type="hidden" id="showHours" name="vacationVO.showHours" /> 
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">工作代理人<span style="color:red"> *</span></label>
			    <div class="col-sm-2 inputout">
			    	<span class="input_text">
			    	<input type="text" id="agent" autoComplete="off" class="form-control" required="required" oninput="findStaffByName()"  />
			    	<input type="hidden" id="agentFlag" value=""/>
			    	<input type="hidden" id="agentID" name="vacationVO.agentID" />
			    	<input type="hidden" id="agentName" name="vacationVO.agentName" />
			    	</span>
			    	<div class="text_down">
						<ul></ul>
					</div>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="vacationType" class="col-sm-1 control-label">休假类型<span style="color:red"> *</span></label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="vacationType" name="vacationVO.vacationType" required="required" onchange="checkAnnualVacation(),checkMarriageHoliday()">
				      <option value="">请选择</option>
					  <option value="1">公假</option>
					  <option value="2">事假</option>
					  <option value="3">年休假</option>
					  <option value="4">婚假</option>
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">原因<span style="color:red"> *</span></label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="reason" name="vacationVO.reason" required="required" />
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
			    <div class="col-sm-5">
			    	<input type="file" id="attachment" multiple name="attachment" accept="image/gif,image/jpeg,image/jpg,image/png" style="padding:6px 0px;">
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			   	<button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>