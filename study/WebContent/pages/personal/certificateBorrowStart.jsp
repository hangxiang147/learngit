<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
       	<s:set name="selectedPanel" value="'newCertificateBorrow'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startCertificateBorrow" method="post"  id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起证件申请</h3> 			 
				  <input style="display:none" name="certificateBorrrowVo.certificateId"  value="${certificate.id}"/>
			  	  <input style="display:none" name="certificateBorrrowVo.certificateName"  value="${certificate.name}"/>
  			  	  <input style="display:none" name="certificateBorrrowVo.userName"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="certificateBorrrowVo.userId"  value="${staff.userID}"/>  
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label" style="width:10%">证件名称</label>
			  	<div class="col-sm-2">${certificate.name}</div>
			  </div>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label" style="width:10%">申请理由<span style="color:red"> *</span></label>
			  	<div class="col-sm-5"><input type="text" autoComplete="off" class="form-control" name="certificateBorrrowVo.reason" required></div>
			  </div>
			 
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label" style="width:10%">是否外借</label>
 			  	<div class="col-sm-2">
 			  		<select class="form-control"  name="certificateBorrrowVo.isBorrow" required="required">
				      		<option value="0">否</option>
				      		<option value="1">是</option>
					</select>
 			  	</div>
			  </div>
			  <div class="form-group" id="borrowTimeDiv"  style="display:none">
			  	<label for="reason" class="col-sm-1 control-label" style="width:10%">开始时间</label>
			  	<div class="col-sm-2"><input type="text" autoComplete="off" class="form-control" id="startTime" name="certificateBorrrowVo.startTime"  onclick="var endTime=$dp.$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"></div>
			 	<label for="reason" class="col-sm-1 control-label" style="width:10%">结束时间</label>
			  	<div class="col-sm-2"><input type="text" autoComplete="off" class="form-control" id="endTime" name="certificateBorrrowVo.endTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"></div>
			  </div>
			  <div class="form-group">
			    <button type="submit" class="btn btn-primary" style="margin-left:5%">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
      </div>
      <script src="/assets/js/underscore-min.js"></script>
      <script>
      
      
      var init=+function(){
    	  $('select[name="certificateBorrrowVo.isBorrow"]').change(function (){
    		  if($(this).val()==="1"){
    			 $('#borrowTimeDiv').show();
    			 $('#startTime,#endTime').attr("required","required");
    		  }else{
     			 $('#borrowTimeDiv').hide();
    			 $('#startTime,#endTime').removeAttr("required");
    		  }
    	  })
      }();
      </script>
      </body>
</html>