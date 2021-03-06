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
		$("#submitButton").addClass("disabled");
		var email = $("#address").val() + "@" + $("#emailType").val();
		$("#emailAddress").val(email);
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
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="administration/process/save_startCard" method="post" class="form-horizontal" enctype="multipart/form-data" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">工牌申领</h3> 
        	  <!-- 填写工牌申请的操作人 -->
			  <input type="hidden" id="userID" name="cardVO.userID" value="${user.id}"/>
			  <input type="hidden" id="userName" name="cardVO.userName" value="${staff.lastName }" />
			  <!-- 工牌申请人 -->
			  <input type="hidden" id="requestUserID" name="cardVO.requestUserID" value="${user.id }"/>
   			  <input type="hidden" id="requestUserName" name="cardVO.requestUserName" value="${staff.lastName }"/>
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">工牌申请人</label>
			  	<div class="col-sm-11"><span class="detail-control">${staff.lastName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser()">修改</a></span></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">申请原因</label>
			  	<div class="col-sm-5"><input type="text" class="form-control" id="reason" name="cardVO.reason" required="required" /></div>
			  </div>
			  <div class="form-group">
			  	<label for="attachment" class="col-sm-1 control-label"><span class="glyphicon glyphicon-paperclip"></span>上传附件</label>
			    <div class="col-sm-5">
			    	<input type="file" id="attachment" name="cardVO.attachment" accept="jpeg/jpg/png/bmp" style="padding:6px 0px;"  multiple="multiple">
			    </div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-5" style="margin-top:180px;">注：请上传个人标准证件照，及微信二维码。二维码格式参照示例：</label>
			  	<div class="col-sm-3">
			  		<img alt="微信二维码格式示例" src="assets/images/qrCodeTemplate.jpg" style="width:200px;height:200px;"></img>  
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