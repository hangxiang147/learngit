<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

	$(function() {
		$("#agent").css("text-indent",$("#namecy").width());
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
				alert("已存在");
				return;
			}
			
			$("#namecy").append("<span id='spanId'>"+shtml.split("（")[0]+"<input type='hidden'  name='trainVO.participantIDs' value="+agent.split("@")[0]+" /><a href=\"javasript:(0)\" onclick='deleteTag(this)' class=\"namecolse\">×</a></span>");
			$(".text_down ul").empty();
			$("#agent").css("text-indent",$("#namecy").width());
		});
		$(document).click(function (event) {$(".text_down").hide();$(".text_down ul").empty();$("#agent").val("");});  
		$('.inputout').click(function (event) {$(".text_down").show();});
	});
		
	
	function deleteTag(obj){
		$(obj).parent().remove();
		$("#agent").css("text-indent",$("#namecy").width());
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

		var checkSubmitFlg = false;
		function check() {
			if (!checkSubmitFlg) {
				//第一次提交
			 	checkSubmitFlg = true;
			 	 return true;
			 	} else {
				//重复提交
			 	alert("Submit again!");
			 	 return false;
			 	}
			if ($("#endDate").val() < $("#beginDate").val()) {
				showAlert("开始日期不得晚于结束日期！");
				return  false;
			}
			$("#submitButton").addClass("disabled");
			return true;
		}
		window.history.forward();


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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      <s:set name="panel" value="'process'"></s:set>
       <%@include file="/pages/train/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="train/saveTrain" method="post" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">跟进培训</h3> 
        	  <input type="hidden" name="trainVO.PID" value="${trainVO.trainID}"/>
        	  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训主题:</label>
			    <div class="col-sm-3" >
			         <b><font size="4px">${trainVO.topic}</font></b>
			    	<input type="hidden" class="form-control" id="topic" name="trainVO.topic" value="${trainVO.topic}"  required="required"/>
			    </div>
			  </div> 
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训讲师 </label>
			    <div class="col-sm-3">
			    	<input type="text" class="form-control" id="lector" name="trainVO.lector" value="${trainVO.lector}" required="required"/>
			    </div>
			  </div>       	
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-1 control-label">开始时间</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="trainVO.startTime" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d${beginTime}'})" onBlur="calcTime()" />	    			
	    		</div>
	    	  </div> 
	    	  <div class="form-group" id="endDate_div">
	    		<label for="endDate" class="col-sm-1 control-label">结束时间</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="endDate" name="trainVO.endTime"  required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d${beginTime}'})" onBlur="calcTime()"/>
	    			<!-- <input type="hidden" id="endDateVal" name="vacationVO.endDate" /> -->
	    		</div>
	    	  </div> 
			  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训地点 </label>
			    <div class="col-sm-3">
			    	<input type="text" class="form-control" id="place" name="trainVO.place" required="required"/>
			    </div>
			  </div>
			  
			 
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训内容</label>
			    <div class="col-sm-5">
			    	<textarea class="form-control" rows="5" id="content" name="trainVO.content"  required="required"></textarea>
			    </div>
			  </div>			 
			 <div class="form-group">
			    <label for="agent" class="col-sm-1 control-label">参与人</label>
			    <div class="col-sm-5 inputout">
			    <div style="position:relative;">
			    	<span class="input_text">
			    	<textarea  id="agent" class="form-control" rows="2"  oninput="findStaffByName()"></textarea>
			    	<input type="hidden" id="agentFlag" value=""/>
			    	<input type="hidden" id="agentID" name="vacationVO.agentID" />
			    	<input type="hidden" id="agentName" name="vacationVO.agentName" />
			    	</span>
			    	<div id="namecy">
			    	<div class="clear"></div>
			    	<c:forEach items="${trainVO.staffs}" var="staffVO" varStatus="count">
			    	<span id='spanId'>${staffVO.lastName}<input type="hidden" name="trainVO.participantIDs" value="${staffVO.userID}"><a href="javasript:(0)" onclick="deleteTag(this)" data-userID="${staffVO.userID }" data-trainID="${trainVO.trainID}" class="namecolse">×</a></span>
			    	</c:forEach>
			    	</div>
			    </div>
			    	<div class="text_down">
						<ul></ul>
					</div>
			    </div>
			  </div>
			  <div class="form-group">
			    <button type="submit" id="submitButton" class="btn btn-primary">提交</button>
			    
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>