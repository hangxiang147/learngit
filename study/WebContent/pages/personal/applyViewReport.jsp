<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<link rel="stylesheet" href="/assets/editor/themes/default/default.css" />
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/underscore-min.js"></script>
<script src="/assets/js/myjs/staffInput.js"></script>
<script src="/assets/js/myjs/textarea.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	new staffInputBind().render('#staffs',textAfterChoose,{textarea:$('#staffs'),namecy:$('#_namecy')});		
	//#namecy div 覆盖了 textarea 导致点击后不会得到焦点
	$('#_namecy').click(function (){
		$('#staffs').focus();
	});	
});
function showDepartment(obj, level, ii) {
	level = level+1;
	$(".flag"+ii+" .department"+level).remove();
	$(".flag"+ii+" .department1 select").removeAttr("name");
	if ($(obj).val() == '') {
		if (level > 2) {
			$(".flag"+ii+" #department"+(level-2)).attr("name", "viewWorkReport.departmentId");
		}
		if(level==2){
			$(".flag"+ii+" #department"+(level-1)).attr("name", "viewWorkReport.departmentId");
		}
		return;
	}
	var parentID = 0;
	if (level != 1) {
		parentID = $(obj).val();
		$(obj).attr("name", "viewWorkReport.departmentId");
	}
	$.ajax({
		url:'HR/staff/findDepartmentsByCompanyIDParentID',
		type:'post',
		data:{companyID: $($(obj).parent().parent().find("select")[0]).val(),
			  parentID: parentID},
		dataType:'json',
		success:function (data){
			if (data.errorMessage!=null && data.errorMessage.length!=0) {
				if (level == 1) {
					window.location.href = "HR/staff/error?panel=dangan&errorMessage="+encodeURI(encodeURI(data.errorMessage));
				} else {
					return;
				}
			}
			
			var divObj = $(".flag"+ii+" #"+$(obj).attr('id')+"_div");
			if(level==1){
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
						+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+", "+ii+")\" name=\"viewWorkReport.departmentId\">"
						+"<option value=\"\">--"+level+"级部门--</option></select>"
						+"</div>");
			}else{
				$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
						+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+", "+ii+")\" >"
						+"<option value=\"\">--"+level+"级部门--</option></select>"
						+"</div>");
			}
			$.each(data.departmentVOs, function(i, department) {
				$(".flag"+ii+" #department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
			});
		}
	});
}
var index = 1;
function addDep(){
	index++;
	 $("#dep").append(
			 '<div class="form-group flag'+index+'">'+
			 '<div class="col-sm-1" style="width:12%"></div>'+
			 '<div class="col-sm-2" id="company'+index+'_div">'+
		    	'<select class="form-control" id="company'+index+'" name="viewWorkReport.companyId" onchange="showDepartment(this, 0, '+index+')" required="required">'+
			      '<option value="">请选择</option>'+
			      	'<s:iterator id="company" value="#request.companys" status="st">'+
			      		'<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>'+
			      	'</s:iterator>'+
				'</select>'+
		    '</div>'+
		    '<div class="col-sm-1 control-label" style="text-align:left"><a class="delete" href="javacript:void(0)" onclick="$(this).parent().parent().remove()"><svg class="icon" aria-hidden="true">'+
				'<use xlink:href="#icon-delete"></use></svg></a>'+
 				'</div>'+
		    '</div>');
}
function checkInfo(){
	//检查公司（部门）与人员，两者需填一个
	data = $('#staffs').data("resultData");
	var userFlag = false;
	var depFlag = false;
	if(data && data.length>0){
		userFlag = true;
		var result=data.reduce(function (result,value){
			result.push(value[0]);
			return result;
		},[]);
		$('input[name="viewWorkReport.userIds"]').val(result.join(","));
	}
	$("select[name='viewWorkReport.companyId']").find("option:selected").each(function(){
		if($(this).val()){
			depFlag = true;
		}
	});;
	if(!depFlag && !userFlag){
		layer.alert("部门和人员不能同时为空",{offset:'100px'});
		return false;
	}
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
	.hand{cursor:pointer}
	.inputout1{position:relative;}
	.text_down1{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down1 ul{padding:2px 10px;}
	.text_down1 ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down1 ul li span{color:#cc0000;}
	.namecy{position:absolute;top:10px;left:10px;}
	.namecy span{display:inline-block;position:relative;padding:6px 18px;color:#ffffff;background:#75b9f3;float:left;margin-right:20px;margin-top:4px;border-radius: 10px}
	.namecy span a{position:absolute;color:#ff0000;font-size:25px;top:-12px;right:0px;cursor:pointer;text-decoration:none}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover{text-decoration:none}
	.delete:hover{color:red !important}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">发起查看日报申请</h3>
          <form class="form-horizontal" action="personal/save_startViewReportApply"
          method="post" onsubmit="return checkInfo()" >
          		<s:token></s:token>
          		<input style="display:none" name="viewWorkReport.userName"  value="${staff.lastName}"/>
  			  	<input style="display:none" name="viewWorkReport.requestUserId"  value="${staff.userID}"/>
  			  	<input style="display:none" name="viewWorkReport.userID"  value="${staff.userID}"/>  
  			  	<div class="form-group">
  			  		<label class="col-sm-1 control-label" style="width:12%">查看权限<span style="color:red"> *</span></label>
  			  		<div class="col-sm-2">
  			  			<select class="form-control" required name="viewWorkReport.viewType">
  			  				<option value="">请选择</option>
  			  				<option value="一次">一次</option>
  			  				<option value="永久">永久</option>
  			  			</select>
  			  		</div>
  			  	</div>
			  	<div class="form-group flag1">
				<label class="col-sm-1 control-label" style="width:12%">查看公司部门<span style="color:red"></span></label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="viewWorkReport.companyId" onchange="showDepartment(this, 0, 1)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companys }">
				      	<s:iterator id="company" value="#request.companys" status="st">
				      		<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			    <div class="col-sm-1"><div class="btn btn-primary" onclick="addDep()"><span class="glyphicon glyphicon-plus"></span> 部门</div></div>
				</div>
				<div id="dep"></div>
			  	<div class="form-group">
				    <label class="col-sm-1 control-label" style="width:12%">查看人员</label>
			    	<div class="col-sm-6">
			    	<div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="staffs" class="form-control" rows="3" onblur="$(this).val('')" onfocus="resetMouse(this)"></textarea>
			    	</span>
			    	<div id="_namecy" class="namecy" style="width:500px"></div>
			    	</div>
			    	</div>
			  	</div>
	    	  	<div class="form-group">
	    	  	 	<label class="col-sm-1" style="width:12%"></label>
	    	  	 	<div class="col-sm-2">
				    <button id="submitBtn" type="submit" class="btn btn-primary" >提交</button>
				    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
			  		</div>
			  	</div>
			  	<!--人员 -->
			  	<input  type="hidden" name="viewWorkReport.userIds">
          </form>
        </div>
      </div>
    </div>
  </body>
</html>