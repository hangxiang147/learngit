<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
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
			$("#executor").val(shtml.split("（")[0]);
			$("#executorFlag").val(shtml.split("（")[0]);
			var executor = $(this).find("input").val();
			$("#executorID").val(executor.split("@")[0]);
			$("#executorName").val(executor.split("@")[1]);
			});
		}); 
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();if ($("#executor").val()!=$("#executorFlag").val()) {$("#executor").val("");}});  
		$('.inputout').click(function (event) {$(".text_down").show();});
		
	});

function showAlert(message) {
	var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
			+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
			+"<span id=\"danger-message\">"+message+"</span>"
			+"</div>";
	$(".container-fluid").before(html);
}

	function changeUser() {
		var html = "<div class=\"col-sm-1\"></div>"
				+"<div class=\"col-sm-2 inputout\">"
				+"<span class=\"input_text\">"
				+"<input type=\"text\" id=\"executor\" class=\"form-control\" required=\"required\" oninput=\"findStaffByName()\"  />"
				+"<input type=\"hidden\" id=\"executorFlag\" value=\"\"/>"
				+"</span>"
				+"<div class=\"text_down\">"
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
	    $("#executorID").val($("#executorID_bak").val());
		$("#executorName").val($("#executorName_bak").val());
	}
	
	function checkNum(obj) {
		$(obj).val($(obj).val().replace(/[^0-9+\.]+/,'').replace(/\b(0+)/gi,""));
	}
	
	function findStaffByName() {
		var name = $("#executor").val();
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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/saveAssignment" method="post" class="form-horizontal">
        	  <h3 class="sub-header" style="margin-top:0px;">任务修改</h3>
        	  <input type="hidden" name="taskID" value="${taskID }" />
			  <input type="hidden" name="processInstanceID" value="${processInstanceID }" />
			  <input type="hidden" id="userID" name="assignmentVO.userID" value="${user.id}"/>
			  <div class="form-group">
			    <label for="title" class="col-sm-1 control-label">标题</label>
			    <div class="col-sm-4">
			    	<input type="text" class="form-control" id="title" name="assignmentVO.title" value="${assignmentVO.title}" required="required">
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="type" class="col-sm-1 control-label">任务类型</label>
			  	<div class="col-sm-2">
			    	<select class="form-control" id="type" name="assignmentVO.type" required="required">
				      <option value="">请选择</option>
					  <option value="2" <c:if test="${assignmentVO.type == 2 }">selected="selected"</c:if>>IT需求</option>
					  <option value="1" <c:if test="${assignmentVO.type == 1 }">selected="selected"</c:if>>其他</option>
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="content" class="col-sm-1 control-label">任务内容</label>
			    <div class="col-sm-4">
			    	<textarea class="form-control" id="content" name="assignmentVO.content" required="required" style="width:418px;height:135px;">${assignmentVO.content }</textarea>
			    </div>
			  </div>
			  <div class="form-group" id="executor_div">
			    <label for="executor" class="col-sm-1 control-label">执行人</label>
			    <div class="col-sm-11"><span class="detail-control">${assignmentVO.executorName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="changeUser()">修改</a></span></div>
			  	<input type="hidden" id="executorID_bak" value="${assignmentVO.executorID }"/>
			  	<input type="hidden" id="executorName_bak" value="${assignmentVO.executorName }"/>
			  	<input type="hidden" id="executorID" name="assignmentVO.executorID" value="${assignmentVO.executorID }"/>
   			  	<input type="hidden" id="executorName" name="assignmentVO.executorName" value="${assignmentVO.executorName }"/>
			  </div>
			  <div class="form-group">
			    <label for="priority" class="col-sm-1 control-label">优先级</label>
			    <div class="col-sm-2">
			    	<select class="form-control" id="priority" name="assignmentVO.priority" required="required">
				      <option value="">请选择</option>
					  <option value="1" <c:if test="${assignmentVO.priority == 1 }">selected="selected"</c:if>>高（1-3天）</option>
					  <option value="2" <c:if test="${assignmentVO.priority == 2 }">selected="selected"</c:if>>中（3-5天）</option>
					  <option value="3" <c:if test="${assignmentVO.priority == 3 }">selected="selected"</c:if>>低（>5天）</option>
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="deadline" class="col-sm-1 control-label">截止日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="deadline" name="assignmentVO.deadline" value="${assignmentVO.deadline}" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d %H:%m:%s'})">
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="goal" class="col-sm-1 control-label">目标（选填）</label>
			    <div class="col-sm-4">
			    	<textarea class="form-control" id="goal" name="assignmentVO.goal" style="width:418px;height:80px;">${assignmentVO.goal }</textarea>
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary" style="margin-left:20px;">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>