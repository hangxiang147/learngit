<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	$(function(){
		//只有周一才可汇报
		if("${weekDay}"!="一"){
			$("#applyBtn").attr("disabled", "disabled");
		}
	});
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
              <s:set name="panel" value="'application'"></s:set>
      
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">周早会汇报流程</h3> 
           	<div class="formal_img">
               	<img src="assets/images/MorningMeeting.png"></img>  
            </div>
            <br>
            <button id="applyBtn" onclick="goPath('personal/toStartMorningMeetingReport')" class="btn btn-primary">我要汇报</button> <span style="color:red;font-size:14px">（汇报时间为周一，若早会延期召开，需提前说明原因，审批通过后，方可汇报）</span>
      	</div>
      </div>
    </div>
</body>
</html>