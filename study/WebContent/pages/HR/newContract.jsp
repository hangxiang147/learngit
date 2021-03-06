<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
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
			
	});
	
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function check() {
		if ($("#endDate").val() <= $("#beginDate").val()) {
			showAlert("结束日期必须大于开始日期！");
			return  false;
		}
		$("#submitButton").addClass("disabled");
		Load.Base.LoadingPic.FullScreenShow(null);
		return true;
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

</script>
<style type="text/css">
	.col-sm-1 {
		padding-right:0px;
		padding-left:0px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	
	.inputout{position:relative;}
	.text_down{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down ul{padding:2px 10px;}
	.text_down ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down ul li span{color:#cc0000;}
	.icon {
		width: 1.5em; height: 1.5em;
		vertical-align: -0.15em;
		fill: currentColor;
		overflow: hidden;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
       	<s:set name="panel" value="'contract'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form  action="HR/contract/saveContract"  method="post" class="form-horizontal" enctype="multipart/form-data" onsubmit="return check();">
        	
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">新增合同</h3>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-2 control-label">公司名称（甲方）</label>
			    <div class="col-sm-3">						
			    <select class="form-control"  name="contractVO.partyA" required="required">
				      <option value="">请选择</option>
				      <option value="1">南通智造链科技有限公司</option>
				      <option value="2">南通迷丝茉服饰有限公司</option>
				      <option value="3">广州亦酷亦雅电子商务有限公司</option>
				      <option value="4">南通智造链电子商务有限公司</option>
				</select>
			    </div>
			  </div>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-2 control-label">员工（乙方）</label>
			     <div class="col-sm-2 inputout">
			    	<span class="input_text">
			    	<input type="text" id="agent" class="form-control" required="required" oninput="findStaffByName()"  />
			    	<input type="hidden" id="agentFlag" value=""/>
			    	<input type="hidden" id="agentID" name="contractVO.partyB" />
			    	<input type="hidden" id="agentName" name="vacationVO.agentName" />
			    	</span>
			    	<div class="text_down">
						<ul></ul>
					</div>
			    </div>
			  </div>
			  <div class="form-group" >
	    		<label for="beginDate" class="col-sm-2 control-label">开始日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="contractVO.beginDate" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
	    	  </div> 
			  <div class="form-group" >
	    		<label for="endDate" class="col-sm-2 control-label">结束日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="endDate" name="contractVO.endDate" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
			    <label for="uploadFile" class="col-sm-2 control-label"><span class="glyphicon glyphicon-paperclip"></span>合同备份</label>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="contract"  style="padding:6px 0px;" >
			    	<span>（合同文件请打包上传！）</span>
			    </div>
			  </div>
	    	  <div class="form-group">
			    <label for="uploadFile" class="col-sm-2 control-label"><span class="glyphicon glyphicon-paperclip"></span>员工签名</label>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="signat"  style="padding:6px 0px;" >
			    </div>
			  </div>
			  <div class="form-group" style="margin-left:6px;">
			    <button type="submit" id="submitButton" class="btn btn-primary" >提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>