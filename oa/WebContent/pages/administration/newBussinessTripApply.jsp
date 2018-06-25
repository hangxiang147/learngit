<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
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
        	<form action="administration/process/save_startBussinessTripApply" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">出差预约申请</h3> 
			  <input type="hidden"  name="tripVo.userID" value="${user.id}"/>
			  <input type="hidden"  name="tripVo.requestUserID" value="${user.id }"/>
			  <input type="hidden" name="tripVo.userName" value="${staff.lastName }"/> 
			  <input type="hidden" name="tripVo.requestUserName" value="${staff.lastName }"/>
			  
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">出差申请人</label>
			  	<div class="col-sm-11"><span class="detail-control">${staff.lastName }&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" id="editALink">修改</a></span></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">出差事由</label>
			  	<div class="col-sm-5"><input type="text" class="form-control"  name="tripVo.reason" required="required" maxlength="500" /></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">开始时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control"  id="startTime" name="tripVo.startTime" required="required"  onclick="var endTime=$dp.$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" /></div>
			 	<label for="reason" class="col-sm-1 control-label">结束时间</label>
			  	<div class="col-sm-2"><input type="text" class="form-control"  id="endTime" name="tripVo.endTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})" /></div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">出差地点</label>
			  	<div class="col-sm-2"><input type="text" class="form-control"  name="tripVo.itemPlace" required="required" maxlength="200" /></div>
			  	<label for="reason" class="col-sm-1 control-label">交通工具</label>
			  	<div class="col-sm-2">
			  		<select class="form-control" name="tripVo.vehicle" required="required">
				      		<option value="汽车">汽车</option>
				      		<option value="火车">火车</option>
				      		<option value="客船">客船</option>
				      		<option value="飞机">飞机</option>
				      		<option value="其他">其他</option>
					</select>
			  	</div>
			  </div>
			   <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">是否需要协助购买车票</label>
			  	<div class="col-sm-2">
			  		<select class="form-control" name="tripVo.isNeedTicket" required="required">
				      		<option value="0">否</option>
				      		<option value="1">是</option>
					</select>
			  	</div>
			  	</div>
			  	 <div id="ticketDetailDiv" class="form-group" style="display:none">
			  	<label for="reason" class="col-sm-1 control-label">车票详情</label>
			  	<div class="col-sm-5"><input type="text" class="form-control"  name="tripVo.ticketDetail" maxlength="500" /></div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
    <script>
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
	};
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

    var init=~function (){
    	
    	var events={
    		isNeedTicketChoose:function (){
    			$('select[name="tripVo.isNeedTicket"]').bind("change",function (){
    				$('#ticketDetailDiv').css("display",($(this).val()==="0")?"none":"block");
    			})
    		},
    		applyPersonEdit:function (){
    			$('#editALink').bind("click",function (){
    				if($(this).html()==="修改"){
    					changeUser();
    				}else{
    					deleteUser();
    				}
    			});
    		},
    		defaultEvent:function (){
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
    					var arr;
    					$("input[name=\"tripVo.requestUserID\"]").val((arr=request.split("@"))[0]);
    					$('input[name="tripVo.requestUserName"]').val($("#request").val());
    					$('input[name="tripVo.userName"]').val($("#request").val());

    				});
    			}); 
    			$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#request").val()!=$("#requestFlag").val()) {$("#request").val("");}});  
    			$('.inputout1').click(function (event) {$(".text_down1").show();});
    		}
    	}
    	for(key in events){
    		events[key]();
    	}
    }();
    </script>
</body>
</html>