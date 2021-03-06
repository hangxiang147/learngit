<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
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
				var userId = request.split("@")[0];
				var userName = request.split("@")[1];
				$.ajax({
					url:'personal/getDateDiff',
					type:'post',
					data:{'userId':userId,'userName':userName},
					success:function(data){
						$("#dateDifferent").html(data.dateDiff);
					}
				})
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
		//检查离职申请人是否有未完结的离职申请
		var flag = false;
		var applyUserId = $("#requestUserID").val();
		$.ajax({
    			url:'personal/checkIsApplyQuit',
    			type:'post',
    			async: false,  
    			data:{'applyUserId':applyUserId},
    			success:function(data){
    				if(data.quit=='true'){
    					flag = true;
    					layer.alert("已有离职申请未完结，无法再次申请",{"offset":"100px"});
    				}
    			}
    		});
		if(flag){
			return false;
		}
		$("#submitButton").addClass("disabled");
		
		$("input[name='resignationVO.reason']").each(function(i, obj) {
			if (obj.checked) {
				flag = true;
			}
		});
		if (!flag) {
			showAlert("请选择至少一项离职原因！");
			return false;
		}
		Load.Base.LoadingPic.FullScreenShow(null);
		return true;
	}
	
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
		$("#requestUserID").val($("#userID").val());
		$("#requestUserName").val($("#userName").val());
		$("#executor_div a").text("修改");
	    $("#executor_div a").attr("onclick", "changeUser()");
	    
	    var userId = $("#userID").val();
		var userName = $("#userName").val();
		$.ajax({
			url:'personal/getDateDiff',
			type:'post',
			data:{'userId':userId,'userName':userName},
			success:function(data){
				$("#dateDifferent").html(data.dateDiff);
			}
		})
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
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startResignation" method="post" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">离职申请</h3> 
        	  <!-- 填写离职申请的操作人 -->
			  <input type="hidden" id="userID" name="resignationVO.userID" value="${user.id}"/>
			  <input type="hidden" id="userName" name="resignationVO.userName" value="${staff.lastName }" />
			  <!-- 离职人 -->
			  <input type="hidden" id="requestUserID" name="resignationVO.requestUserID" value="${user.id }"/>
   			  <input type="hidden" id="requestUserName" name="resignationVO.requestUserName" value="${staff.lastName }"/>
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">离职人</label>
			  	<div class="col-sm-11"><span class="detail-control">${staff.lastName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser()">修改</a></span></div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label">合同到期</label>
			  	<div class="col-sm-11"><span class="detail-control" id="dateDifferent">${dateDiff }</span></div>
			  </div>
			  <div class="form-group" id="beginDate_div">
	    		<label for="leaveDate" class="col-sm-1 control-label">离职日期<span style="color:red"> *</span></label>
	    		<div class="col-sm-2">
	    			<input type="text" autoComplete="off" class="form-control" id="leaveDate" name="resignationVO.leaveDate" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'%y-%M-{%d+1}'})" />
	    		</div>
	    	  </div> 
			  <div class="form-group">
			    <label for="reasons" class="col-sm-1 control-label">离职原因<span style="color:red"> *</span></label>
			    <div class="checkbox col-sm-11">
				  <label><input type="checkbox" name="resignationVO.reason" value="薪酬不满意">薪酬不满意</label>
				</div>
				<div class="col-sm-1"></div>
				<div class="checkbox col-sm-11">
				  <label><input type="checkbox" name="resignationVO.reason" value="作息时间不适应">作息时间不适应</label>
				</div>
				<div class="col-sm-1"></div>
				<div class="checkbox col-sm-11">
				  <label><input type="checkbox" name="resignationVO.reason" value="不能融入部门">不能融入部门</label>
				</div>
				<div class="col-sm-1"></div>
				<div class="checkbox col-sm-11">
				  <label><input type="checkbox" name="resignationVO.reason" value="公司文化不认同">公司文化不认同</label>
				</div>
				<div class="col-sm-1"></div>
				<div class="checkbox col-sm-11">
				  <label><input type="checkbox" name="resignationVO.reason" value="人际关系不适应">人际关系不适应</label>
				</div>
				<div class="col-sm-1"></div>
				<div class="checkbox col-sm-11">
				  <label><input type="checkbox" name="resignationVO.reason" value="家庭原因">家庭原因</label>
				</div>
				<div class="col-sm-1"></div>
				<div class="checkbox col-sm-11">
				  <label><input type="checkbox" name="resignationVO.reason" value="其他">其他</label>
				</div>
			  </div>
			  <div class="form-group">
			  	<label for="note" class="col-sm-1 control-label">备注</label>
			    <div class="col-sm-5">
			    	<input type="text" class="form-control" id="note" name="resignationVO.note" />
			    </div>
			  </div>
			  <div class="form-group" style="padding-top:20px">
			  <span style="float:left;">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			   </span>
			   <span style="color:red;padding-top:12px;float:left;margin-left:20px;">注：离职申请请保密，在流程审批通过之前，不能让任何不相关的人知道，否则扣除当月全部绩效！</span>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>