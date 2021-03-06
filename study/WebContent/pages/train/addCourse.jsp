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
<script src="/assets/editor/kindeditor-min.js"></script>
<script src="/assets/editor/lang/zh_CN.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	kedit('textarea[name="trainCourse.description"]');
	new staffInputBind().render('#lecturer',textAfterChoose,{textarea:$('#lecturer'),namecy:$('#namecy')});		
	new staffInputBind().render('#joinUser',textAfterChoose,{textarea:$('#joinUser'),namecy:$('#_namecy')});
	//#namecy div 覆盖了 textarea 导致点击后不会得到焦点
	$('#namecy').click(function (){
		$('#lecturer').focus();
	});	
	$('#_namecy').click(function (){
		$('#joinUser').focus();
	})
});
function showDepartment(obj, level, ii) {
	level = level+1;
	$(".flag"+ii+" .department"+level).remove();
	$(".flag"+ii+" .department1 select").removeAttr("name");
	if ($(obj).val() == '') {
		if (level > 2) {
			$(".flag"+ii+" #department"+(level-2)).attr("name", "trainCourse.departmentId");
		}
		if(level==2){
			$(".flag"+ii+" #department"+(level-1)).attr("name", "trainCourse.departmentId");
		}
		return;
	}
	var parentID = 0;
	if (level != 1) {
		parentID = $(obj).val();
		$(obj).attr("name", "trainCourse.departmentId");
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
						+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showDepartment(this, "+level+", "+ii+")\" name=\"trainCourse.departmentId\">"
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
function addTrainClass(){
	layer.open({  
		id:'addTrainClass',
		title:'新增类别',
        content: '<div class="form-group">'+
				 '<label class="col-sm-3 control-label">类别<span style="color:red"> *</span></label>'+
			     '<div class="col-sm-9">'+
				 '<input class="form-control" id="trainClass"/></div></div>',
        btn: ['确认', '取消'], 
        offset:'100px',
        yes: function() { 
        	var trainClass = $("#trainClass").val();
        	if(trainClass==''){
        		layer.alert("类别不能为空",{offset:'100px'});
        		return;
        	}
        	$.ajax({
        		type:'get',
        		data:{'trainClass':trainClass},
        		url:'train/addTrainClass',
        		success:function(data){
        			$("select[name='trainCourse.trainClass']").append('<option data-id="'+data.trainClassId+'" value="'+trainClass+'">'+trainClass+'</option>');
        			$("#addTrainClass").next().next().children('a').eq(1).click();
        		}
        	});
        }
    }); 
}
var index = 1;
function addDep(){
	index++;
	 $("#dep").append(
			 '<div class="form-group flag'+index+'">'+
			 '<div class="col-sm-2"></div>'+
			 '<div class="col-sm-2" id="company'+index+'_div">'+
		    	'<select class="form-control" id="company'+index+'" name="trainCourse.companyId" onchange="showDepartment(this, 0, '+index+')" required="required">'+
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
function kedit(kedit){
	var	editor =KindEditor.create(kedit, {
			resizeType : 1,
			allowPreviewEmoticons : false,
			items : [
				'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|','preview']
		});
}
function checkName(){
	var courseName = $("input[name='trainCourse.courseName']").val();
	if(courseName){
		$.ajax({
			url:'train/checkCourseName',
			type:'post',
			data:{'courseName':courseName},
			success:function(data){
				var exist = data.exist;
				if(exist){
					layer.alert("名字已存在，请重新输入",{offset:'100px'});
					$("input[name='trainCourse.courseName']").val("");
				}
			}
		});
	}
}
function checkInfo(){
	//检查讲师是否填写
	var data = $('#lecturer').data("resultData");
	if(data && data.length>0){
		var result=data.reduce(function (result,value){
			result[0].push(value[0]);
			result[1].push(value[1]);
			return result;
		},[[],[]]);
		$('input[name="trainCourse.lecturerIds"]').val(result[0].join(","));
		$('input[name="trainCourse.lecturerNames"]').val(result[1].join(","));
	}else{
		layer.alert("讲师必填，不能为空",{offset:'100px'});
		return false;
	}
	//检查参与部门和参与人员，两者需填一个
	data = $('#joinUser').data("resultData");
	var joinUserFlag = false;
	var joinDepFlag = false;
	if(data && data.length>0){
		//检查参与人员和截止人数（截止人数不能小于参与人员的数量）
		if(data.length > parseInt($("input[name='trainCourse.maxPersonNum']").val())){
			layer.alert("截止人数不能小于参与人员的数量",{offset:'100px'});
			return false;
		}
		joinUserFlag = true;
		var result=data.reduce(function (result,value){
			result.push(value[0]);
			return result;
		},[]);
		$('input[name="trainCourse.joinUsers"]').val(result.join(","));
	}
	$("select[name='trainCourse.companyId']").find("option:selected").each(function(){
		if($(this).val()){
			joinDepFlag = true;
		}
	});;
	if(!joinDepFlag && !joinUserFlag){
		layer.alert("参与部门和参与人员不能同时为空",{offset:'100px'});
		return false;
	}
	//验证文件大小，不能超过2M(2*1024*1024)
	var maxSize = 2*1024*1024;
    var files = $("#files")[0].files;
    for(var i=0; i<files.length; i++){
 	   var file = files[i];
 	   if(file.size>maxSize){
 		   layer.alert("文件"+file.name+"超过2M，限制上传",{offset:'100px'});
 		   return false;
 	   }
    }
    $("#submitBtn").attr("disabled","disabled");
    Load.Base.LoadingPic.FullScreenShow(null);
}
function showDeadline(obj){
	if($(obj).val()=='是'){
		$("#deadline").css("display","block");
		$("input[name='trainCourse.deadline']").attr("required","required");
		$("input[name='trainCourse.maxPersonNum']").attr("required","required");
	}else{
		$("#deadline").css("display","none");
		$("input[name='trainCourse.deadline']").removeAttr("required");
		$("input[name='trainCourse.maxPersonNum']").removeAttr("required");
	}
}
var allTrainClassIds = [];
function removeTrainClass(){
	$.ajax({
		url:'train/getAllTrainClass',
		success:function(data){
			var html = "";
			$.each(data.trainClasss, function(index, value){
				allTrainClassIds.push(value.id);
				html += '<div class="trainClass" style="margin-left:5px;margin-bottom:5px;display:inline-block;height:30px;line-height:30px;border-radius:10px;background-color:#75b9f3;color:#fff" data-id="'+value.id+'">'+value.trainClass+'<span class="glyphicon glyphicon-remove red hand" onclick="$(this).parent().remove()"></span></div>';
			});
			$("#trainClassList").html(html);
			$("#removeTrainClass").modal("show");
		}
	});
}

function deleteTrainClass(){
	var remainClassIds = [];
	$("div.trainClass").each(function(){
		var trainClassId = $(this).attr("data-id");
		remainClassIds.push(trainClassId);
	});
	var deleteClassIds = [];
	allTrainClassIds.forEach(function(value, index){
		if(!remainClassIds.contains(value)){
			deleteClassIds.push(value);
		}
	});
	$.ajax({
		url:'train/deleteTrainClass',
		data:{'trainClassIds':deleteClassIds.join(",")},
		success:function(data){
			if(data.success=='true'){
				//去除下拉框中的删除类别
				for(var i=0; i<deleteClassIds.length; i++){
					$("option[data-id='"+deleteClassIds[i]+"']").remove();
				}
			}
		}
	});
}
Array.prototype.contains = function ( needle ) {
	  for (i in this) {
	    if (this[i] == needle) return true;
	  }
	  return false;
}
</script>
<style type="text/css">
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
	.ke-container{
		width:100% !important;
	}
	.radioCss{height:15px; width:15px}
	.red{
		color:red
	}
	.hand{cursor:pointer}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/train/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">新开培训课程</h3>
          <form class="form-horizontal" action="train/save_saveCourse" enctype="multipart/form-data"
          method="post" onsubmit="return checkInfo()" >
          		<s:token></s:token>
          		<div class="form-group">
					<label class="col-sm-2 control-label">课程名称<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control" autocomplete="off" required name="trainCourse.courseName" maxLength="50"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">培训类别<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<select class="form-control" id="_trainClass" name="trainCourse.trainClass" required>
							<option value="">请选择</option>
							<c:forEach items="${trainClasss}" var="trainClass">
								<option data-id="${trainClass.id}" value="${trainClass.trainClass}">${trainClass.trainClass}</option>
							</c:forEach>
						</select>
					</div>
					<div class="col-sm-1"><div class="btn btn-primary" onclick="addTrainClass()"><span class="glyphicon glyphicon-plus"></span> 类别</div></div>
					<div class="col-sm-1"><div class="btn btn-default" style="color:red" onclick="removeTrainClass()"><span class="glyphicon glyphicon-minus"></span> 类别</div></div>
				</div>
				<div class="form-group">
			  		<label class="col-sm-2 control-label">公开报名<span style="color:red"> *</span></label>
			  		<div class="col-sm-2" style="margin-top:0.5%">
			  			<input class="radioCss" checked name="trainCourse.publicReport" required type="radio" value="是" onclick="showDeadline(this)">&nbsp;<label>是</label>
			  			&nbsp;&nbsp;&nbsp;&nbsp;
			  			<input class="radioCss" name="trainCourse.publicReport" required type="radio" value="否" onclick="showDeadline(this)">&nbsp;<label>否</label>
			  		</div>
			  	</div>
				<div class="form-group" id="deadline">
					<label class="col-sm-2 control-label">报名截止时间<span style="color:red"> *</span></label>
					<div class="col-sm-2"  style="width:18%">
						<input type="text" autocomplete="off" class="form-control" name="trainCourse.deadline" required 
	    			 	onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd HH:mm:00',minDate:'%y-%M-%d %H:%m:%s'})" />
					</div>
					<label class="col-sm-1 control-label" style="width:20%">截止人数（包含指定人员）<span style="color:red"> *</span></label>
					<div class="col-sm-2" style="width:12%">
						<input type="number" min="1" autocomplete="off" class="form-control" name="trainCourse.maxPersonNum" required />
					</div>
				</div>
				<div class="form-group">
				    <label class="col-sm-2 control-label">讲师<span style="color:red"> *</span></label>
			    	<div class="col-sm-6">
			    	<div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="lecturer" class="form-control" rows="3" onblur="$(this).val('')" onfocus="resetMouse(this)"></textarea>
			    	</span>
			    	<div id="namecy" class="namecy" style="width:500px"></div>
			    	</div>
			    	</div>
			  	</div>
			  	<div class="form-group flag1">
				<label class="col-sm-2 control-label">参与部门<span style="color:red"></span></label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="trainCourse.companyId" onchange="showDepartment(this, 0, 1)">
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
				    <label class="col-sm-2 control-label">参与人员</label>
			    	<div class="col-sm-6">
			    	<div style="position:relative;">
			    	<span class="input_text">
			    	<textarea id="joinUser" class="form-control" rows="3" onblur="$(this).val('')" onfocus="resetMouse(this)"></textarea>
			    	</span>
			    	<div id="_namecy" class="namecy" style="width:500px"></div>
			    	</div>
			    	</div>
			  	</div>
			  	<div class="form-group">
			  		<label class="col-sm-2 control-label">课程纲要<span style="font-size:10px;font-weight:bold">（不超过1000字）</span></label>
			  		<div class="col-sm-6">
			  		<textarea class="form-control" rows="5" id="content" name="trainCourse.description" maxLength="1000"></textarea>
			  		</div>
			  	</div>
			  	<div class="form-group" >
				<label class="col-sm-2 control-label"><span class="glyphicon glyphicon-paperclip"></span> 添加附件</label>
				<div class="col-sm-2">
	    			<input multiple name="attachment" id="files" type="file" />
	    		</div>
	    	  	</div>
	    	  	 <div class="form-group">
	    	  	 	<label class="col-sm-2"></label>
	    	  	 	<div class="col-sm-2">
				    <button id="submitBtn" type="submit" class="btn btn-primary" >提交</button>
				    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:20px;">返回</button>
			  		</div>
			  	</div>
			  	<!-- 讲师 -->
			  	<input  type="hidden" name="trainCourse.lecturerIds">
			  	<input  type="hidden" name="trainCourse.lecturerNames">
			  	<!-- 参会人员 -->
			  	<input  type="hidden" name="trainCourse.joinUsers">
          </form>
        </div>
      </div>
    </div>
    <div id="removeTrainClass" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">删除类别</h4>
			</div>
			<div class="modal-body">
				<div id="trainClassList" style="height:150px">
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="deleteTrainClass()">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
	</div>
  </body>
</html>