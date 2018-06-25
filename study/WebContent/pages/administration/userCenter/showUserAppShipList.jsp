<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	$("[data-toggle='tooltip']").tooltip();
	if('${result}'=='-1'){
		layer.alert("操作失败",{offset:'100px'});
	}
});
function showDepartment(obj, level) {
	level = level+1;
	$(".department"+level).remove();
	$(".department1 select").removeAttr("name");
	if ($(obj).val() == '') {
		if (level > 2) {
			$("#department"+(level-2)).attr("name", "departmentID");
		}
		return;
	}
	
	var parentID = 0;
	if (level != 1) {
		parentID = $(obj).val();
		$(obj).attr("name", "departmentID");
	}
	$.ajax({
		url:'HR/staff/findDepartmentsByCompanyIDParentID',
		type:'post',
		data:{companyID: $("#company").val(),
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
			
			var divObj = $("#"+$(obj).attr('id')+"_div");
			$(divObj).after("<div class='col-sm-3'></div><div style='margin-top: 2px' id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
						+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+")\" >"
						+"<option value=\"\">--"+level+"级部门--</option></select>"
						+"</div>");
			$.each(data.departmentVOs, function(i, department) {
				$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
			});
		}
	});
	//若选择的是部门，获取部门下面所有的人员
	if(parentID!=0){
		$.ajax({
			url:'HR/staff/getAllStaffsByDepId',
			data:{'depId':parentID,'companyId':$("#company").val()},
			success:function(data){
				var html = "";
				data.staffVos.forEach(function(value, index){
					html += "<span><span data-userid='"+value.userID+"' class='authUser'>"+value.lastName+"<span data-toggle='tooltip' data-placement='right' title='删除' onclick='$(this).parent().parent().remove();' class='deleteUser'>x</span></span>&nbsp</span>";
				});
				$("#authUsers").html(html);
				$("[data-toggle='tooltip']").tooltip();
			}
		});
	}else{
		$("#authUsers").html("");
	}
}
function changeType(){
	var type = $("select[name='type'] option:selected").val();
	if(type=='单个'){
		$("#single").css("display","block");
		$("#batch").css("display","none");
		$("#staffName").attr("required", "required");
		
	}else if(type=='批量'){
		$("#batch").css("display","block");
		$("#single").css("display","none");
		$("#staffName").removeAttr("required");
	}
}
function checkEmpty(){
 	if($("#staffName").val()==''){
 			$("input[name='authUserIds']").val('');
 	}
}
require(['staffComplete'],function (staffComplete){
	new staffComplete().render($('#staffName'),function ($item){
		$("input[name='authUserIds']").val($item.data("userId"));
	});
});
$(document).click(function (event) {
	if ($("input[name='authUserIds']").val()=='')
	{
		$("#staffName").val("");
	}
}); 
function inputUserIds(){
	var type = $("select[name='type'] option:selected").val();
	if(type=='批量'){
		var userIds='';
		$(".authUser").each(function(){
			if(!userIds){
				userIds += $(this).attr("data-userid");
			}else{
				userIds += ','+$(this).attr("data-userid");
			}
		});
		if(!userIds){
			layer.alert("授权人员不可为空", {offset:'100px'});
			return false;
		}
		$("input[name='authUserIds']").val(userIds);
	}
	$("#addUserAppShip .close").click();
	Load.Base.LoadingPic.FullScreenShow(null);
}
function deleteShip(userAppId){
	layer.confirm("确定删除？",{offset:'100px'},function(index){
		layer.close(index);
		location.href = "userCenter/deleteUserAppShip?userAppId="+userAppId;
		Load.Base.LoadingPic.FullScreenShow(null);
	});
}
</script>
<style type="text/css">
.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
}
.authUser {
	color:#ffffff;
	background:#75b9f3;
	border-radius: 10px;
	padding:5 10;
	display: inline-block;
	margin-top: 5px;
	margin-bottom: 2px;
	position: relative;
}
.deleteUser {
	color:red;
	font-size:13px;
	font-weight:bold;
	position:absolute;
	top:-10%;
	cursor:pointer;
}
.textcomplete-dropdown{
	z-index:1050 !important;
}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'userCenter'"></s:set>
        <s:set name="selectedPanel" value="'showUserAppShipList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">应用授权列表</h3>
       	<form action="userCenter/showUserAppShipList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
        	<div class="form-group">
        		<label class="control-label col-sm-1">姓名</label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="staffName" value="${staffName}"/>
        		</div>
        		<label class="control-label col-sm-1">应用</label>
        		<div class="col-sm-2">
        			<select class="form-control" name="appId">
        				<c:forEach items="${appInfos}" var="appInfo">
        					<option ${appId==appInfo.id?'selected':''} value="${appInfo.id}">${appInfo.appName}</option>
        				</c:forEach>
        			</select>
        		</div>
        		<button class="btn btn-primary" type="submit" style="margin-left:3%">查询</button>
        	</div>
        </form>
        <a class="btn btn-primary" href="javascript:void(0)" data-toggle="modal" data-target="#addUserAppShip"><span class="glyphicon glyphicon-plus"></span> 授权</a>
			<div class="table-responsive" style="margin-top:30px;">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>姓名</th>
		                  <th>所属部门</th>
		                  <th>应用名称</th>
		                  <th style="width:14%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${userAppShipList}" var="userAppShip" varStatus="index">
		  					<tr>
		  						<td>${startIndex+index.index+1}</td>
		  						<td>${userAppShip[0]}</td>
		  						<td>${userAppShip[1]}</td>
		  						<td>${userAppShip[2]}</td>
		  						<td>
		  							<a href="javascript:deleteShip('${userAppShip[3]}')" href="javascript:void(0)">
		  							<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
    									<use xlink:href="#icon-delete"></use>
									</svg>
									</a>
		  						</td>
		  					</tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
          		</div> 
          	        <div class="dropdown">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        	</div>
		        <%@include file="/includes/pager.jsp" %>
        </div>
      </div>
    </div>
    <div id="addUserAppShip" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form method="post" action="userCenter/saveUserAppShip" class="form-horizontal" onsubmit="return inputUserIds()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">授权应用</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
	    	  		<label class="col-sm-3 control-label">应用<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
		    		<select class="form-control" name="appId" required>
        				<option value="">请选择</option>
        				<c:forEach items="${appInfos}" var="appInfo">
        					<option value="${appInfo.id}">${appInfo.appName}</option>
        				</c:forEach>
        			</select>
		    		</div>
	    	  	</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">授权类型<span style="color:red"> *</span></label>
					<div class="col-sm-7">
						<select class="form-control" name="type" onchange="changeType()" required>
						 	<option value="单个">单个</option>
						 	<option value="批量">批量</option>
						</select>
					</div>
				</div>
				<div id="single" class="form-group">
		    		<label class="col-sm-3 control-label">授权人员<span style="color:red"> *</span></label>
		    		<div class="col-sm-7">
		    			<input id="staffName" autoComplete="off" type="text" class="form-control" required onclick="checkEmpty()">
		    		</div>
	    	  	</div> 
	    	  	<div id="batch" style="display:none">
	    	  	<div class="form-group">
			  	<label class="col-sm-3 control-label">授权部门<span style="color:red"> *</span></label>
			    <div class="col-sm-7" id="company_div">
			    	<select class="form-control" id="company" name="vacationVO.companyID" onchange="showDepartment(this, 0, 1)">
				      <option value="">请选择</option>
				      <c:if test="${not empty companys }">
				      	<s:iterator id="company" value="#request.companys" status="st">
				      		<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			   	</div>
			   	</div>
			   	<div class="form-group">
			   		<label class="col-sm-3 control-label">授权人员<span style="color:red"> *</span></label>
			   		<div id="authUsers" class="col-sm-8" style="min-height:100px;border:1px solid #ccc;margin-left:3.4%">
			   		</div>
			   	</div>
			   	</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
			<input name="authUserIds" type="hidden">
		</div>
		</form>
	</div>
	</div>
  </body>
</html>