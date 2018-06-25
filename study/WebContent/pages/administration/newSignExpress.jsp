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
				$("#agent1").val(shtml.split("（")[0]);
				$("#agentFlag1").val(shtml.split("（")[0]);
				var agent = $(this).find("input").val();
				$("#agentID1").val(agent.split("@")[0]);
				$("#agentName1").val(agent.split("@")[1]);
				});
			}); 
			$(document).click(function (event) {$(".text_down1").hide();$(".text_down1 ul").empty();if ($("#agent1").val()!=$("#agentFlag1").val()) {$("#agent1").val("");}});  
			$('.inputout1').click(function (event) {$(".text_down1").show();});
	});

	

	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
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
		var name = $("#agent1").val();
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
       <s:set name="panel" value="'express'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="addVacationForm"  action="administration/express/saveSignExpress"  method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">新增</h3>
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-1 control-label">收件人</label>
			  	<div class="col-sm-2 inputout1">
			    	<span class="input_text1">
			    	<input type="text" id="agent1" class="form-control" required="required" oninput="findStaffByName1()"  />
			    	<input type="hidden" id="agentFlag1" value=""/>
			    	<input type="hidden" id="agentID1" name="signExpressVO.recipientID" />
			    	<input type="hidden" id="agentName1" name="vacationVO.agentName" />
			    	</span>
			    	<div class="text_down1">
						<ul></ul>
					</div>
			    </div>
			  </div>
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-1 control-label">收件日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="signExpressVO.receiptDate" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
	    	  </div> 
	    	 
			 <div class="form-group">
			 <label for="expressCompany" class="col-sm-1 control-label">物流公司</label>
			  	<div class="col-sm-2">						
			<select class="form-control"  name="signExpressVO.expressCompany" id="expressCompany" required="required">
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
					</select>
			     </div>
			  </div>
			 <div class="form-group">
			  	<label for="expressNumber" class="col-sm-1 control-label">快递单号</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="expressNumber" name="signExpressVO.expressNumber" required="required"/>
			    </div>
			  </div>
			<%--    <div class="form-group">
			 <label for="expressCompany" class="col-sm-1 control-label">状态</label>
			  	<div class="col-sm-2">						
			    <select class="form-control"  name="signExpressVO.status" id="expressCompany" required="required">
				      <option value="">请选择</option>
				      <option value="1" >未签收</option>
				      <option value="2" >已签收</option>
				</select>
			     </div>
			  </div> --%>
			  
			 
			 
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