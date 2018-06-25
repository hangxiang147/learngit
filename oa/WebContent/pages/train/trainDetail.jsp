<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">

	$(function() {
		$('input,select,textarea',$('form[name="trainForm"]')).prop('readonly',true);
		
	});		
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
        <div class="detail-control">
        	<form action="train/updateTrain" method="post" class="form-horizontal" name="trainForm">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">查看培训</h3> 
        	  <input type="hidden" name="trainVO.trainID" value="${trainVO.trainID}"/>
        	  <div class="form-group">
			  	<label for="topic" class="col-sm-1 control-label">培训主题</label>
			    <div class="col-sm-3" >
			    	<input type="text" class="form-control" id="topic" name="trainVO.topic" value="${trainVO.topic}"  required="required"/>
			    </div>
			  </div>
			   <div class="form-group">
			  	<label for="lector" class="col-sm-1 control-label">培训讲师 </label>
			    <div class="col-sm-3">
			    	<input type="text" class="form-control" id="lector" name="trainVO.lector" value="${trainVO.lector}"  required="required"/>
			    </div>
			  </div>        	
			  <div class="form-group" id="beginDate_div">
	    		<label for="beginDate" class="col-sm-1 control-label">开始时间</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="trainVO.startTime" value="${trainVO.startTime}" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d${beginTime}'})" onBlur="calcTime()" />	    			
	    		</div>
	    	  </div> 
	    	  <div class="form-group" id="endDate_div">
	    		<label for="endDate" class="col-sm-1 control-label">结束时间</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="endDate" name="trainVO.endTime" value="${trainVO.endTime}"  required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d${beginTime}'})" onBlur="calcTime()"/>
	    			<!-- <input type="hidden" id="endDateVal" name="vacationVO.endDate" /> -->
	    		</div>
	    	  </div> 
			  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训地点 </label>
			    <div class="col-sm-3">
			    	<input type="text" class="form-control" id="place" name="trainVO.place" value="${trainVO.place}"  required="required"/>
			    </div>
			  </div>

			  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">培训内容</label>
			    <div class="col-sm-5">
			    	<textarea class="form-control" rows="5" id="content" name="trainVO.content"  required="required">${trainVO.content}</textarea>
			    </div>
			  </div>	
			  <c:if test="${!empty attas }">
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">附件</label>
			    <div class="col-sm-5">
			    	<c:forEach items="${attas }" var="item">
			    		<a href="/app/personal/downloadCommon?id=${item.attachment_ID}">${item.softName}</a>
			    		&nbsp;
			    	</c:forEach>
			    </div>
			  </div>	
			  </c:if>		 
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
			    	<c:forEach items="${trainVO.staffs}" var="staffVO" varStatus="count">
			    	<span id='spanId'>${staffVO.lastName}</span>
			    	</c:forEach>
			    	
			    	<div class="clear"></div>
			    	</div>
			    </div>
			    	<div class="text_down">
						<ul></ul>
					</div>
			    </div>
			  </div>
			  
			</form>
			</div>
			 <div class="form-group">
			    
			    
			    <a class="btn btn-primary" href="train/findTrainList">返回</a>
			  </div>
        </div>
      </div>
    </div>
</body>
</html>