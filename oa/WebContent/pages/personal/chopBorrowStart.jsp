<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<body>
<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="personal/save_startChopBorrow" method="post"  id="form_"class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">  
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">发起公章申请</h3> 			 
				  <input style="display:none" name="chopBorrrowVo.Chop_Id"  value="${chop.chop_Id}"/>
			  	  <input style="display:none" name="chopBorrrowVo.Chop_Name"  value="${chop.name}"/>
  			  	  <input style="display:none" name="chopBorrrowVo.User_Name"  value="${staff.lastName}"/>
  			  	  <input style="display:none" name="chopBorrrowVo.User_Id"  value="${staff.userID}"/>  
  			  	  <input type="hidden" name="processInstanceId" value="${processInstanceId}"/>
			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label" style="width:12%">公章名称</label>
			  	<div class="col-sm-2">${chop.name }</div>
			  </div>
<!-- 			  <div class="form-group">
			  	<label for="reason" class="col-sm-1 control-label">申请理由</label>
			  	<div class="col-sm-5"><input type="text" class="form-control" name="chopBorrrowVo.Reason" ></div>
			  </div> -->
			  <div class="form-group">
			  	<label for="fileName" class="col-sm-1 control-label" style="width:12%">文件名称<span style="color:red"> *</span></label>
			  	<div class="col-sm-3"><input id="fileName" autoComplete="off" type="text" class="form-control" name="chopBorrrowVo.fileName" value="${fileName}" required></div>
			  </div>
			  <div class="form-group">
			  	<label for="fileUse" class="col-sm-1 control-label" style="width:12%">文件用途<span style="color:red"> *</span></label>
			  	<div class="col-sm-3"><input id="fileUse" autoComplete="off" type="text" class="form-control" name="chopBorrrowVo.fileUse" value="${fileUse}" required></div>
			  </div>
			  <div class="form-group">
			  	<label for="fileName" class="col-sm-1 control-label" style="width:12%">文件类型<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  	<select class="form-control" name="chopBorrrowVo.fileType" required onchange="changeFileType()">
			  		<option value="">请选择</option>
			  		<option value="合同">合同</option>
			  		<option value="开店">开店</option>
			  		<option value="其他">其他</option>
			  	</select>
			  	</div>
			  </div>
			  <div id="contractInfo" style="display:none">
			  	<div class="form-group">
			  		<label class="col-sm-1 control-label" style="width:12%">甲方<span style="color:red"> *</span></label>
			  		<div class="col-sm-3">
			  		<input autoComplete="off" class="form-control" name="chopBorrrowVo.partyA">
			  		</div>
			  	</div>
			  	<div class="form-group">
			  		<label class="col-sm-1 control-label" style="width:12%">乙方<span style="color:red"> *</span></label>
			  		<div class="col-sm-3">
			  		<input autoComplete="off" class="form-control" name="chopBorrrowVo.partyB">
			  		</div>
			  	</div>
			  	<div class="form-group">
			  		<label class="col-sm-1 control-label" style="width:12%">合同申请时间<span style="color:red"> *</span></label>
			  		<div class="col-sm-3">
			  		<input autoComplete="off" class="form-control" name="chopBorrrowVo.contractApplyDate" onclick="WdatePicker()">
			  		</div>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:12%">是否涉及法律等重要事项</label>
			  	<div class="col-sm-2" style="margin-top:2%">
			  	<input type="radio" name="chopBorrrowVo.relateLaw" value="1" style="height:15px;width:15px">&nbsp;<label>是</label>
			  	<input type="radio" checked name="chopBorrrowVo.relateLaw" value="0" style="height:15px;width:15px">&nbsp;<label>否</label>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:12%">正版/复印件</label>
			  	<div class="col-sm-2" style="margin-top:1%">
			  	<input type="radio" checked name="chopBorrrowVo.isCopy" value="0" style="height:15px;width:15px">&nbsp;<label>正版</label>
			  	<input type="radio" name="chopBorrrowVo.isCopy" value="1" style="height:15px;width:15px">&nbsp;<label>复印件</label>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:12%">文件份数<span style="color:red"> *</span></label>
			  	<div class="col-sm-3">
			  		<input id="fileNum" type="number" autoComplete="off" class="form-control" name="chopBorrrowVo.fileNum" value="${fileNum}" min="1" required>
			  	</div>
			  </div>
			  <div class="form-group">
 			  	<label for="reason" class="col-sm-1 control-label" style="width:12%">是否外借</label>
 			  	<div class="col-sm-3">
 			  		<select class="form-control"  name="chopBorrrowVo.IsBorrow" required="required">
				      		<option value="0">否</option>
				      		<option value="1">是</option>
					</select>
 			  	</div>
			  </div>
			  <div class="form-group" id="borrowTimeDiv"  style="display:none">
			  	<label for="reason" class="col-sm-1 control-label" style="width:12%">开始时间</label>
			  	<div class="col-sm-3"><input type="text" class="form-control" id="startTime" name="chopBorrrowVo.StartTime"  onclick="var endTime=$dp.$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"></div>
			 	<label for="reason" class="col-sm-1 control-label" style="width:12%">结束时间</label>
			  	<div class="col-sm-3"><input type="text" class="form-control" id="endTime" name="chopBorrrowVo.EndTime" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'startTime\')}'})"></div>
			  </div>
			  <div class="form-group">
			  	<div class="col-sm-1" style="width:12%"></div>
			  	<div class="col-sm-4">
			    <button type="submit" class="btn btn-primary">提交</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  	</div>
			  </div>
			</form>
        </div>
      </div>
      </div>
      <script src="/assets/js/underscore-min.js"></script>
      <script>
      var init=+function(){
    	  $('select[name="chopBorrrowVo.IsBorrow"]').change(function (){
    		  if($(this).val()==="1"){
    			 $('#borrowTimeDiv').show();
    			 $('#startTime,#endTime').attr("required","required");
    		  }else{
     			 $('#borrowTimeDiv').hide();
    			 $('#startTime,#endTime').removeAttr("required");
    		  }
    	  })
      }();
      function changeFileType(){
    	  var fileType = $("select[name='chopBorrrowVo.fileType'] option:selected").val();
    	  if("合同"==fileType){
    		  $("#contractInfo").css("display","block");
    		  $("input[name='chopBorrrowVo.partyA']").attr("required","required");
    		  $("input[name='chopBorrrowVo.partyB']").attr("required","required");
    		  $("input[name='chopBorrrowVo.contractApplyDate']").attr("required","required");
    	  }else{
    		  $("#contractInfo").css("display","none");
    		  $("input[name='chopBorrrowVo.partyA']").removeAttr("required");
    		  $("input[name='chopBorrrowVo.partyB']").removeAttr("required");
    		  $("input[name='chopBorrrowVo.contractApplyDate']").removeAttr("required");
    	  }
      }
      </script>
      </body>
</html>