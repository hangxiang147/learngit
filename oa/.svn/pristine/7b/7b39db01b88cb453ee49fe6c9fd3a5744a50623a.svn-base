<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function(){
	$("[data-toggle='tooltip']").tooltip();
	$("#checkProjectForm").submit(function(){
		saveCheckProject();
	});
	$("#updateProjectForm").submit(function(){
		updateCheckProject();
	});
	$("#addPositionPerformance").submit(function(){
		var positionId = $("#position option:selected").val();
		var coefficient = 0;
		$("._coefficient").each(function(){
			coefficient += parseFloat($(this).text().trim());
		});
		coefficient = coefficient.toFixed(2);
		if(coefficient!=1){
			layer.alert("所有考核项目的考核系数之和必须为1",{offset:'100px'});
			return;
		}
		var projectIds = [];
		$("td[data-project]").each(function(){
			projectIds.push($(this).data("project"));
		});
		var templateName = $("select[name='departmentId'] option:selected").text()+
						   "-"+$("#position option:selected").text();
		$.ajax({
			url:'/administration/performance/savePositionTemplate',
			data:{'positionId':positionId,'projectIds':projectIds.join(","),"templateName":templateName},
			success:function(data){
				var projects = data.projects;
				var position= data.position;
				var templateId = data.templateId;
				var html = '<div style="border:1px solid #3333333b;margin-top:2%;width:90%;margin-left:5%;padding-bottom:2%">'+
		  				   '<a onclick="deletePositionCheck(this)" href="javascript:void(0)" style="position:relative;left:97.8%;color:#f16e6e">'+
    					   '<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">'+
  						   '<use xlink:href="#icon-delete2"></use></svg></a>'+
						   '<div data-position="'+positionId+'" style="font-size:15px;margin-left:5%"><span>岗位：</span><span>'+position+'</span></div>'+
	  					   '<table class="tab" style="width:90%;margin-left:5%">'+
	  			           '<thead>'+
	  					   '<tr>'+
	  					   '<td class="title" style="width:15%">项目</td>'+
	  					   '<td class="title" style="width:15%">考核项目</td>'+
	  					   '<td class="title" style="width:10%">考核系数</td>'+
	  					   '<td class="title" colspan="2" style="width:20%">奖励条件</td>'+
	  					   '<td class="title" colspan="2" style="width:20%">少发条件</td>'+
	  			           '</tr></thead><tbody>';
	  			    projects.forEach(function(data, index){
		  			    var checkItems = data.checkItems;
		  			  	var project = data.project;
		  				var rows = checkItems.length;
		  				checkItems.forEach(function(value, i){
		  	  				if(i==0){
		  	  					html += '<tr>'+
		  						   '<td rowspan="'+2*rows+'">'+project+'</td>';
		  	  				}else{
		  	  					html += '<tr>';
		  	  				}
		  	  				var addMoneyType = value.addMoneyType;
		  	  				if(addMoneyType=='+'){
		  	  					addMoneyType = "每多";
		  	  				}else{
		  	  					addMoneyType = "每少";
		  	  				}
		  	  				var reduceMoneyType = value.reduceMoneyType;
		  	  				if(reduceMoneyType=='+'){
		  	  					reduceMoneyType = "每多";
		  	  				}else{
		  	  					reduceMoneyType = "每少";
		  	  				}
		  	  				html += '<td rowspan="2">'+value.checkItem+'</td>'+
		  				       		'<td rowspan="2">'+value.coefficient+'</td>'+
		  					        '<td class="title">'+addMoneyType+'</td>'+
		  			  				'<td class="title">奖励金额</td>'+
		  			  				'<td class="title">'+reduceMoneyType+'</td>'+
		  			  				'<td class="title">少发金额</td></tr>';
		  			  				
		  			  		html += '<tr><td>'+(value.perAddMoneyValue?value.perAddMoneyValue:'——')+
		  			  				'</td><td>'+(value.addMoney?value.addMoney:'——')+
		  			  				'</td><td>'+(value.perReduceMoneyValue?value.perReduceMoneyValue:'——')+
		  			  				'</td><td>'+(value.reduceMoney?value.reduceMoney:'——')+'</td></tr>';
		  	  			});
	  			    });        
	  				html += '</tbody></table>';
	  				html += "<input type='hidden' name='templateId' value='"+templateId+"'></div>";
	  				$("#projects").append(html);
	  				//form表单回到初始状态
	  				$("#addPositionPerformance").html($("#initPageHtml").html());
			}
		});
	});
});
function deletePositionCheck(obj){
	layer.confirm("确定删除？",{offset:'100px'}, function(index){
		$(obj).parent().remove();
		layer.close(index);
	});
}
function showPosition(departmentID) {
	$.ajax({
		url: 'HR/staff/findPositionsByDepartmentID',
		type: 'post',
		data: {departmentID: departmentID},
		dataType: 'json',
		success: function(data) {
			$.each(data.positionVOs, function(i, position) {
				$("#position").append("<option value='"+position.positionID+"'>"+position.positionName+"</option>");
			});
		}
	});
}
function showChild(obj, level) {
	$("#position option").each(function(i, optionObj) {
		if (i != 0) {
			$(optionObj).remove();
		}
	});
	
	level = level+1;
	$(".department"+level).remove();
	$(".department1 select").removeAttr("name");
	if ($(obj).val() == '') {
		if (level > 2) {
			$("#department"+(level-2)).attr("name", "departmentId");
			showPosition($("#department"+(level-2)).val());
		}
		return;
	}
	var parentID = 0;
	if (level != 1) {
		parentID = $(obj).val();
		$(obj).attr("name", "departmentId");
		showPosition(parentID);
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
				} 
				return;
			}
			
			var divObj = $("#"+$(obj).attr('id')+"_div");
			$(divObj).after("<div id=\"department"+level+"_div\" class=\""+$(divObj).attr('class')+" department"+level+"\">"
						+"<select class=\"form-control\" id=\"department"+level+"\" onchange=\"showChild(this, "+level+")\">"
						+"<option value=\"\">--"+level+"级部门--</option></select>"
						+"</div>");
			$.each(data.departmentVOs, function(i, department) {
				$("#department"+level).append("<option value='"+department.departmentID+"'>"+department.departmentName+"</option>");
			});
		}
	});
}
function findTemplates(){
	var positionId = $("#position option:selected").val();
	if(positionId){
		var positionIds = [];
		$("div[data-position]").each(function(){
			positionIds.push($(this).data('position'));
		});
		//判断是否已添加该岗位绩效方案
		if(positionIds.contains(positionId)){
			layer.alert("该岗位的绩效方案已添加",{offset:'100px'});
			$("#position option").removeAttr("selected");
			return;
		}
		//检查岗位绩效模板是否已存在
		$.ajax({
			url:'/administration/performance/checkPositionTemplateExist',
			data:{'positionId':positionId},
			success:function(data){
				if(data.exist){
					layer.alert("该岗位的绩效方案已提交",{offset:'100px'});
					$("#position option").removeAttr("selected");
				}else{
					var departmentId = $("select[name='departmentId'] option:selected").val();
					$.ajax({
						url:'administration/performance/findTemplatesByDepId',
						data:{'departmentId':departmentId},
						success:function(data){
							var optionHtml = "<option value=''>请选择</option>";
							data.templates.forEach(function(value, index){
								optionHtml += "<option value='"+value.id+"'>"+value.templateName+"</option>";
							});
							$("#template").html(optionHtml);
						}
					});
				}
			}
		});
	}
}
function checkNum(obj){
	var num = $(obj).val();
	if(num.trim()){
		if(!/^[1-9]+[0-9]*$/.test(num.trim()) && !/^[1-9]+[0-9]*\.[0-9]{1,2}$/.test(num.trim()) && !/^0\.[0-9]{1,2}$/.test(num.trim())){
			layer.alert("数字输入不合法，小数最多两位",{offset:'100px'});
			return false;
		}
	}
	return true;
}
function checkCoefficient(obj){
	var num = $(obj).val();
	if(num.trim()){
		if(!/^[1-9]+[0-9]*$/.test(num.trim()) && !/^[1-9]+[0-9]*\.[0-9]+$/.test(num.trim()) && !/^0\.[0-9]+$/.test(num.trim())){
			layer.alert("考核系数输入不合法",{offset:'100px'});
			return false;
		}
		if(parseFloat(num.trim())>1){
			layer.alert("考核系数不能大于1",{offset:'100px'});
			return false;
		}
	}
	return true;
}
//数组下标
var index_add = 0;
function deleteCheckContent(obj){
	layer.confirm("确定删除？",{offset:'100px'},function(index){
		layer.close(index);
		var checkItemId = $(obj).parent().find(".checkItem").next().val();
		if(checkItemId){
			$.ajax({
				url:'administration/performance/deleteCheckItem',
				data:{'checkItemId':checkItemId},
				success:function(){
					$(obj).parent().remove();
					index_add--;
				}
			});
		}else{
			$(obj).parent().remove();
			index_add--;
		}
	});
}
function addCheckContent(){
	index_add++;
	var html = $("#checkContent").html().replace(/checkItem\./g,"checkItem["+index_add+"].");
	$("#modal-content").append(html);
}
function addCheckContentForUpdate(){
	index_add++;
	var html = $("#checkContent").html().replace(/checkItem\./g,"checkItem["+index_add+"].");
	$("#modal-content-update").append(html);
}
function saveCheckProject(){
	var flag = true;
	$("input.coefficient").each(function(){
		if(!checkCoefficient(this)){
			flag = false;
			return;
		}
	});
	if(!flag){
		return;
	}
	$("input.perValue").each(function(){
		if(!checkNum(this)){
			flag = false;
			return;
		}
	});
	if(!flag){
		return;
	}
	$("input.money").each(function(){
		if(!checkNum(this)){
			flag = false;
			return;
		}
	});
	if(!flag){
		return;
	}
	Load.Base.LoadingPic.FullScreenShow(null);
	$("#addCheckProject").modal("hide");
	$.ajax({
		url:'/administration/performance/saveCheckProject',
		data:$("#checkProjectForm").serialize(),
		type:'post',
		success:function(data){
			var project = data.project;
			var checkItem =  data.checkItem;
			var rows = checkItem.length;
			var html = '';
  			checkItem.forEach(function(value, index){
  				if(index==0){
  					html += '<tr>'+
					   '<td rowspan="'+2*rows+'">'+project.project+'</td>';
  				}else{
  					html += '<tr>';
  				}
  				var addMoneyType = value.addMoneyType;
  				if(addMoneyType=='+'){
  					addMoneyType = "每多";
  				}else{
  					addMoneyType = "每少";
  				}
  				var reduceMoneyType = value.reduceMoneyType;
  				if(reduceMoneyType=='+'){
  					reduceMoneyType = "每多";
  				}else{
  					reduceMoneyType = "每少";
  				}
  				html += '<td rowspan="2">'+value.checkItem+'</td>'+
			       		'<td rowspan="2" class="_coefficient">'+value.coefficient+'</td>'+
				        '<td class="title">'+addMoneyType+'</td>'+
		  				'<td class="title">奖励金额</td>'+
		  				'<td class="title">'+reduceMoneyType+'</td>'+
		  				'<td class="title">少发金额</td>';
		  		if(index==0){
		  			html += '<td data-project="'+project.id+'" data-number="'+rows+'" rowspan="'+2*rows+'">'+
	  						'<a onclick="modifyProject(this)" href="javascript:void(0)">'+
        					'<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
      						'<use xlink:href="#icon-modify"></use>'+
      						'</svg></a>&nbsp;'+
		  					'<a onclick="deleteProject(this)" href="javascript:void(0)">'+
        					'<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">'+
      						'<use xlink:href="#icon-delete"></use>'+
      						'</svg></a></td></tr>';
		  		}else{
		  			html += '</tr>';
		  		}
		  		html += '<tr><td>'+(value.perAddMoneyValue?value.perAddMoneyValue:'——')+
		  				'</td><td>'+(value.addMoney?value.addMoney:'——')+
		  				'</td><td>'+(value.perReduceMoneyValue?value.perReduceMoneyValue:'——')+
		  				'</td><td>'+(value.reduceMoney?value.reduceMoney:'——')+'</td></tr>';
  			});
			$("#project").append(html);
			$("[data-toggle='tooltip']").tooltip();
		},
	    complete:function(){
	    	Load.Base.LoadingPic.FullScreenHide();
	    }
	});
	index_add = 0; 
	$("#addCheckProject").html($("#modalHtml").html());
	$("#checkProjectForm").submit(function(){
		saveCheckProject();
	});
}
function updateCheckProject(){
	var flag = true;
	$("input.coefficient").each(function(){
		if(!checkCoefficient(this)){
			flag = false;
			return;
		}
	});
	if(!flag){
		return;
	}
	$("input.perValue").each(function(){
		if(!checkNum(this)){
			flag = false;
			return;
		}
	});
	if(!flag){
		return;
	}
	$("input.money").each(function(){
		if(!checkNum(this)){
			flag = false;
			return;
		}
	});
	if(!flag){
		return;
	}
	Load.Base.LoadingPic.FullScreenShow(null);
	$("#updateCheckProject").modal("hide");
	var projectId = $("#updateProjectForm").find("input[name='project.id']").val();
	$.ajax({
		url:'/administration/performance/updateCheckProject',
		data:$("#updateProjectForm").serialize(),
		type:'post',
		success:function(data){
			//先删除旧数据
			var $obj = $("[data-project='"+projectId+"']");
			var num = $obj.data("number");
			var deleteRow = $obj.parent();
			var deleteRows = [];
			deleteRows.push(deleteRow);
			for(var i=0; i<2*num-1; i++){
				deleteRow = deleteRow.next();
				deleteRows.push(deleteRow);
			}
			deleteRows.forEach(function(value, index){
				value.remove();
			});
			//再补新数据
			var project = data.project;
			var checkItem =  data.checkItem;
			var rows = checkItem.length;
			var html = '';
  			checkItem.forEach(function(value, index){
  				if(index==0){
  					html += '<tr>'+
					   '<td rowspan="'+2*rows+'">'+project.project+'</td>';
  				}else{
  					html += '<tr>';
  				}
  				var addMoneyType = value.addMoneyType;
  				if(addMoneyType=='+'){
  					addMoneyType = "每多";
  				}else{
  					addMoneyType = "每少";
  				}
  				var reduceMoneyType = value.reduceMoneyType;
  				if(reduceMoneyType=='+'){
  					reduceMoneyType = "每多";
  				}else{
  					reduceMoneyType = "每少";
  				}
  				html += '<td rowspan="2">'+value.checkItem+'</td>'+
			       		'<td rowspan="2" class="_coefficient">'+value.coefficient+'</td>'+
				        '<td class="title">'+addMoneyType+'</td>'+
		  				'<td class="title">奖励金额</td>'+
		  				'<td class="title">'+reduceMoneyType+'</td>'+
		  				'<td class="title">少发金额</td>';
		  		if(index==0){
		  			html += '<td data-project="'+project.id+'" data-number="'+rows+'" rowspan="'+2*rows+'">'+
	  						'<a onclick="modifyProject(this)" href="javascript:void(0)">'+
        					'<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
      						'<use xlink:href="#icon-modify"></use>'+
      						'</svg></a>&nbsp;'+
		  					'<a onclick="deleteProject(this)" href="javascript:void(0)">'+
        					'<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">'+
      						'<use xlink:href="#icon-delete"></use>'+
      						'</svg></a></td></tr>';
		  		}
		  		html += '</tr>';
		  		html += '<tr><td>'+(value.perAddMoneyValue?value.perAddMoneyValue:'——')+
		  				'</td><td>'+(value.addMoney?value.addMoney:'——')+
		  				'</td><td>'+(value.perReduceMoneyValue?value.perReduceMoneyValue:'——')+
		  				'</td><td>'+(value.reduceMoney?value.reduceMoney:'——')+'</td></tr>';
  			});
			$("#project").append(html);
			$("[data-toggle='tooltip']").tooltip();
		},
	    complete:function(){
	    	Load.Base.LoadingPic.FullScreenHide();
	    }
	});
	index_add = 0; 
	$("#updateCheckProject").html($("#modalUpdateHtml").html());
	$("#updateProjectForm").submit(function(){
		updateCheckProject();
	});
}
function checkProject(){
	var projects = $("#projects").text();
	if(!projects.trim()){
		layer.alert("请先添加绩效方案",{offset:'100px'});
		return false;
	}
	Load.Base.LoadingPic.FullScreenShow(null);
}
function modifyProject(obj){
	$("#updateCheckProject").html($("#modalUpdateHtml").html());
	$("#updateProjectForm").submit(function(){
		updateCheckProject();
	});
	index_add = 0;
	var projectId = $(obj).parent().data("project");
	$.ajax({
		url:'/administration/performance/findProjectCheckContent',
		data:{'projectId':projectId},
		success:function(data){
			var project = data.project;
			$("#modal-content-update").find("input[name='project.project']").val(project.project);
			$("input[name='project.id']").val(project.id);
			data.projectChecks.forEach(function(value, index){
				if(index==0){
					$("#modal-content-update").find("input[name='checkItem[0].checkItem']").val(value.checkItem);
					$("#modal-content-update").find("input[name='checkItem[0].coefficient']").val(value.coefficient);
					$("#modal-content-update").find("select[name='checkItem[0].addMoneyType']").val(value.addMoneyType);
					$("#modal-content-update").find("input[name='checkItem[0].perAddMoneyValue']").val(value.perAddMoneyValue);
					$("#modal-content-update").find("input[name='checkItem[0].addMoney']").val(value.addMoney);
					$("#modal-content-update").find("select[name='checkItem[0].reduceMoneyType']").val(value.reduceMoneyType);
					$("#modal-content-update").find("input[name='checkItem[0].perReduceMoneyValue']").val(value.perReduceMoneyValue);
					$("#modal-content-update").find("input[name='checkItem[0].reduceMoney']").val(value.reduceMoney);
					$("#modal-content-update").find("input[name='checkItem[0].id']").val(value.id);
				}else{
					var html = $("#checkContent").html().replace(/checkItem\./g, "checkItem["+index+"].");
					$("#modal-content-update").append(html);
					$("#modal-content-update").find("input[name='checkItem["+index+"].checkItem']").val(value.checkItem);
					$("#modal-content-update").find("input[name='checkItem["+index+"].coefficient']").val(value.coefficient);
					$("#modal-content-update").find("select[name='checkItem["+index+"].addMoneyType']").val(value.addMoneyType);
					$("#modal-content-update").find("input[name='checkItem["+index+"].perAddMoneyValue']").val(value.perAddMoneyValue);
					$("#modal-content-update").find("input[name='checkItem["+index+"].addMoney']").val(value.addMoney);
					$("#modal-content-update").find("select[name='checkItem["+index+"].reduceMoneyType']").val(value.reduceMoneyType);
					$("#modal-content-update").find("input[name='checkItem["+index+"].perReduceMoneyValue']").val(value.perReduceMoneyValue);
					$("#modal-content-update").find("input[name='checkItem["+index+"].reduceMoney']").val(value.reduceMoney);
					$("#modal-content-update").find("input[name='checkItem["+index+"].id']").val(value.id);
					index_add++;
				}
			});
			$("#updateCheckProject").modal("show");
		}
	});
}
function deleteProject(obj){
	layer.confirm("确定删除？",{offset:'100px'},function(index){
		layer.close(index);
		var projectId = $(obj).parent().data("project");
		$.ajax({
			url:'/administration/performance/deleteProject',
			data:{'projectId':projectId},
			success:function(){
				var num = $(obj).parent().data("number");
				var deleteRow = $(obj).parent().parent();
				var deleteRows = [];
				deleteRows.push(deleteRow);
				for(var i=0; i<2*num-1; i++){
					deleteRow = deleteRow.next();
					deleteRows.push(deleteRow);
				}
				deleteRows.forEach(function(value, index){
					value.remove();
				});
			}
		});
	});
}
function addCheckProject(){
	index_add = 0;
	$("#addCheckProject").modal("show");
}
function selectTemplate(){
	var templateId = $("#template option:selected").val();
	Load.Base.LoadingPic.FullScreenShow(null);
	$.ajax({
		url:'/administration/performance/findPositionTemplateDetail',
		data:{'templateId':templateId},
		success:function(data){
			var projects = data.projects;
			var html = '';
			projects.forEach(function(data, i){
				var project = data.project;
				var checkItems = data.checkItems;
				var rows = checkItems.length;
				checkItems.forEach(function(value, index){
	  				if(index==0){
	  					html += '<tr>'+
						   '<td rowspan="'+2*rows+'">'+project+'</td>';
	  				}else{
	  					html += '<tr>';
	  				}
	  				var addMoneyType = value.addMoneyType;
	  				if(addMoneyType=='+'){
	  					addMoneyType = "每多";
	  				}else{
	  					addMoneyType = "每少";
	  				}
	  				var reduceMoneyType = value.reduceMoneyType;
	  				if(reduceMoneyType=='+'){
	  					reduceMoneyType = "每多";
	  				}else{
	  					reduceMoneyType = "每少";
	  				}
	  				html += '<td rowspan="2">'+value.checkItem+'</td>'+
				       		'<td rowspan="2" class="_coefficient">'+value.coefficient+'</td>'+
					        '<td class="title">'+addMoneyType+'</td>'+
			  				'<td class="title">奖励金额</td>'+
			  				'<td class="title">'+reduceMoneyType+'</td>'+
			  				'<td class="title">少发金额</td>';
			  		if(index==0){
			  			html += '<td data-project="'+data.id+'" data-number="'+rows+'" rowspan="'+2*rows+'">'+
		  						'<a onclick="modifyProject(this)" href="javascript:void(0)">'+
	        					'<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
	      						'<use xlink:href="#icon-modify"></use>'+
	      						'</svg></a>&nbsp;'+
			  					'<a onclick="deleteProject(this)" href="javascript:void(0)">'+
	        					'<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">'+
	      						'<use xlink:href="#icon-delete"></use>'+
	      						'</svg></a></td></tr>';
			  		}else{
			  			html += '</tr>';
			  		}
			  		html += '<tr><td>'+(value.perAddMoneyValue?value.perAddMoneyValue:'——')+
			  				'</td><td>'+(value.addMoney?value.addMoney:'——')+
			  				'</td><td>'+(value.perReduceMoneyValue?value.perReduceMoneyValue:'——')+
			  				'</td><td>'+(value.reduceMoney?value.reduceMoney:'——')+'</td></tr>';
	  			});
			});
			$("#project").html(html);
			$("[data-toggle='tooltip']").tooltip();
		},
	    complete:function(){
	    	Load.Base.LoadingPic.FullScreenHide();
	    }
	});
}
Array.prototype.contains = function ( a ) {
	  for (i in this) {
	    if (this[i] == a) return true;
	  }
	  return false;
}
</script>
<style type="text/css">
	.tab{
	border-collapse:collapse;
	width:100%;
	margin-top:5px;
   }
	.tab tr th, .tab tr td{
	word-wrap:break-word;
	word-break:break-all;
	font-size:10px;
	padding:8px 7px;
	text-align:center;
	border:1px solid #ddd
	}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	.title{background-color:#F2F2F2}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/performance/common/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">制定岗位绩效方案</h3> 
       	  	  <form id="addPositionPerformance" class="form-horizontal" onsubmit="return false">
		  	  <div class="form-group">
			    <label for="company" class="col-sm-1 control-label" style="width:10%">部门&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="staffVO.companyID" onchange="showChild(this, 0)" required="required">
				      <option value="">-- 公司 --</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="position" class="col-sm-1 control-label" style="width:10%">岗位&nbsp;<span style="color:red;">*</span></label>
			  	<div class="col-sm-2">
			    	<select class="form-control" id="position" required="required" onchange="findTemplates()">
				      <option value="">-- 职位 --</option>
					</select>
			    </div>
			  </div>
			  <div class="form-group" style="margin-bottom:2px">
			  	<label class="col-sm-1 control-label" style="width:10%">绩效模板</label>
			  	<div class="col-sm-2">
			  		<select class="form-control" id="template" onchange="selectTemplate()">
			  			<option value=''>请选择</option>
					</select>
			  	</div>
			  </div>
			  <div class="form-group" style="margin-bottom:2px">
			  	<div class="col-sm-1" style="width:10%"></div>
			  	<div class="col-sm-10" style="text-align:right">
			  		 <button type="button" class="btn btn-primary" onclick="addCheckProject()"><span class="glyphicon glyphicon-plus"></span> 考核项目</button>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:10%">考核项目&nbsp;<span style="color:red;">*</span></label>
			  	<div class="col-sm-10">
			  		<table class="tab">
			  			<thead>
			  			<tr>
			  				<td class="title" style="width:15%">项目</td>
			  				<td class="title" style="width:15%">考核项目</td>
			  				<td class="title" style="width:10%">考核系数</td>
			  				<td class="title" colspan="2" style="width:20%">奖励条件</td>
			  				<td class="title" colspan="2" style="width:20%">少发条件</td>
			  				<td class="title" style="width:10%">操作</td>
			  			</tr>
			  			</thead>
			  			<tbody id="project">
			  			</tbody>
			  		</table>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<div class="control-label col-sm-1" style="width:10%">
			  		<button type="submit" class="btn btn-primary">添加</button>
			  	</div>
			  </div>
			  </form>
			  <h3 class="sub-header">岗位绩效方案</h3>
			  <form class="form-horizontal" action="/administration/performance/save_startPerformanceApply"
			   			method="post" onsubmit="return checkProject()">
			  	<s:token></s:token>
			  	<div id="projects">
					
			  	</div>
			  	<div class="form-group" style="margin-top:5%;text-align:center">
				  	<button type="submit" class="btn btn-primary">提交</button>
					<button type="button" class="btn btn-default" style="margin-left:5%" onclick="history.go(-1)">返回</button>
			  </div>
			  </form>
      	</div>
      </div>
    </div>
    <div id="addCheckProject" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:60%">
		<form id="checkProjectForm" class="form-horizontal" onsubmit="return false">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">新增考核项目</h4>
			</div>
			<div class="modal-body">
				<div id="modal-content">
				<div class="form-group">
					<label class="col-sm-2 control-label">项目<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control" autocomplete="off" required name="project.project"/>
					</div>
				</div>
				<div style="text-align:right"><button style="margin-top:-5%" type="button" class="btn btn-primary" onclick="addCheckContent()">添加</button></div>
				<div style="border-bottom:1px solid #3333333b;margin-bottom:10px"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control checkItem" autocomplete="off" required name="checkItem[0].checkItem"/>
					</div>
					<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="checkItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">奖励/少发<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<table class="tab calculations" data-index="0">
							<tr>
								<td style="width:35%">条件</td>
								<td style="width:50%">奖励/少发</td>
							</tr>
							<tr>
								<td>奖励条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control type" name="checkItem[0].addMoneyType">
											<option value="+">每多</option>
											<option value="-">每少</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="checkItem[0].perAddMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">奖励金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="checkItem[0].addMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>少发条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control type" name="checkItem[0].reduceMoneyType">
											<option value="-">每少</option>
											<option value="+">每多</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="checkItem[0].perReduceMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">少发金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="checkItem[0].reduceMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</div>
		</form>
	</div>
</div>
	<script type="text/html" id="checkContent">
				<div style="border:1px solid #3333333b;margin-top:2%">
					<a onclick="deleteCheckContent(this)" href="javascript:void(0)" style="position:relative;left:97%;color:#f16e6e">
            			<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
          					<use xlink:href="#icon-delete2"></use>
          				</svg>
           			</a>
					<div class="form-group">
					<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control checkItem" autocomplete="off" required name="checkItem.checkItem"/>
						<input type="hidden" name="checkItem.id">
					</div>
					<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="checkItem.coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">奖励/少发<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<table class="tab calculations" data-index="0">
							<tr>
								<td style="width:35%">条件</td>
								<td style="width:50%">奖励/少发</td>
							</tr>
							<tr>
								<td>奖励条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control type" name="checkItem.addMoneyType">
											<option value="+">每多</option>
											<option value="-">每少</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="checkItem.perAddMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">奖励金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="checkItem.addMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>少发条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control type" name="checkItem.reduceMoneyType">
											<option value="-">每少</option>
											<option value="+">每多</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="checkItem.perReduceMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">少发金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="checkItem.reduceMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
	</script>
	<script type="text/html" id="modalHtml">
	<div class="modal-dialog" style="width:60%">
		<form id="checkProjectForm" class="form-horizontal" onsubmit="return false">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">新增项目</h4>
			</div>
			<div class="modal-body">
				<div id="modal-content">
				<div class="form-group">
					<label class="col-sm-2 control-label">项目<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control" autocomplete="off" required name="project.project" value="${project.project}"/>
					</div>
				</div>
				<div style="text-align:right"><button style="margin-top:-5%" type="button" class="btn btn-primary" onclick="addCheckContent()">添加</button></div>
				<div style="border-bottom:1px solid #3333333b;margin-bottom:10px"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control checkItem" autocomplete="off" required name="checkItem[0].checkItem"/>
					</div>
					<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="checkItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">奖励/少发<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<table class="tab calculations" data-index="0">
							<tr>
								<td style="width:35%">条件</td>
								<td style="width:50%">奖励/少发</td>
							</tr>
							<tr>
								<td>奖励条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control type" name="checkItem[0].addMoneyType">
											<option value="+">每多</option>
											<option value="-">每少</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="checkItem[0].perAddMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">奖励金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="checkItem[0].addMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>少发条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control type" name="checkItem[0].reduceMoneyType">
											<option value="-">每少</option>
											<option value="+">每多</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="checkItem[0].perReduceMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">少发金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="checkItem[0].reduceMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</div>
		</form>
	</div>
	</script>
	
	<div id="updateCheckProject" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:60%">
		<form id="updateProjectForm" class="form-horizontal" onsubmit="return false">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">修改项目</h4>
			</div>
			<div class="modal-body">
				<div id="modal-content-update">
				<div class="form-group">
					<label class="col-sm-2 control-label">项目<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control" autocomplete="off" required name="project.project" value="${project.project}"/>
						<input type="hidden" name="project.id">
					</div>
				</div>
				<div style="text-align:right"><button style="margin-top:-5%" type="button" class="btn btn-primary" onclick="addCheckContentForUpdate()">添加</button></div>
				<div style="border-bottom:1px solid #3333333b;margin-bottom:10px"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control checkItem" autocomplete="off" required name="checkItem[0].checkItem"/>
						<input type="hidden" name="checkItem[0].id">
					</div>
					<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="checkItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">奖励/少发<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<table class="tab calculations" data-index="0">
							<tr>
								<td style="width:35%">条件</td>
								<td style="width:50%">奖励/少发</td>
							</tr>
							<tr>
								<td>奖励条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control addMoneyType" name="checkItem[0].addMoneyType">
											<option value="+">每多</option>
											<option value="-">每少</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perAddMoneyValue" autocomplete="off" name="checkItem[0].perAddMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">奖励金额</label>
										<div class="col-sm-7">
											<input class="form-control addMoney" autocomplete="off" name="checkItem[0].addMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>少发条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control reduceMoneyType" name="checkItem[0].reduceMoneyType">
											<option value="-">每少</option>
											<option value="+">每多</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perReduceMoneyValue" autocomplete="off" name="checkItem[0].perReduceMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">少发金额</label>
										<div class="col-sm-7">
											<input class="form-control reduceMoney" autocomplete="off" name="checkItem[0].reduceMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</div>
		</form>
	</div>
</div>
<script type="text/html" id="modalUpdateHtml">
		<div class="modal-dialog" style="width:60%">
		<form id="updateProjectForm" class="form-horizontal" onsubmit="return false">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">修改项目</h4>
			</div>
			<div class="modal-body">
				<div id="modal-content-update">
				<div class="form-group">
					<label class="col-sm-2 control-label">项目<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control" autocomplete="off" required name="project.project" value="${project.project}"/>
						<input type="hidden" name="project.id">
					</div>
				</div>
				<div style="text-align:right"><button style="margin-top:-5%" type="button" class="btn btn-primary" onclick="addCheckContentForUpdate()">添加</button></div>
				<div style="border-bottom:1px solid #3333333b;margin-bottom:10px"></div>
				<div class="form-group">
					<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control checkItem" autocomplete="off" required name="checkItem[0].checkItem"/>
						<input type="hidden" name="checkItem[0].id">
					</div>
					<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="checkItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-2 control-label">奖励/少发<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<table class="tab calculations" data-index="0">
							<tr>
								<td style="width:35%">条件</td>
								<td style="width:50%">奖励/少发</td>
							</tr>
							<tr>
								<td>奖励条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control addMoneyType" name="checkItem[0].addMoneyType">
											<option value="+">每多</option>
											<option value="-">每少</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perAddMoneyValue" autocomplete="off" name="checkItem[0].perAddMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">奖励金额</label>
										<div class="col-sm-7">
											<input class="form-control addMoney" autocomplete="off" name="checkItem[0].addMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
							<tr>
								<td>少发条件</td>
								<td>
									<div class="form-group" style="margin-bottom:5px">
										<div class="col-sm-5">
										<select class="form-control reduceMoneyType" name="checkItem[0].reduceMoneyType">
											<option value="-">每少</option>
											<option value="+">每多</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perReduceMoneyValue" autocomplete="off" name="checkItem[0].perReduceMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">少发金额</label>
										<div class="col-sm-7">
											<input class="form-control reduceMoney" autocomplete="off" name="checkItem[0].reduceMoney"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</div>
		</form>
	</div>
</script>
<script type="text/html" id="initPageHtml">
			  	  <div class="form-group">
			    <label for="company" class="col-sm-1 control-label" style="width:10%">部门&nbsp;<span style="color:red;">*</span></label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="staffVO.companyID" onchange="showChild(this, 0)" required="required">
				      <option value="">-- 公司 --</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />"><s:property value="#company.companyName"/></option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			  </div>
			  <div class="form-group">
			  	<label for="position" class="col-sm-1 control-label" style="width:10%">岗位&nbsp;<span style="color:red;">*</span></label>
			  	<div class="col-sm-2">
			    	<select class="form-control" id="position" required="required" onchange="findTemplates()">
				      <option value="">-- 职位 --</option>
					</select>
			    </div>
			  </div>
			  <div class="form-group" style="margin-bottom:2px">
			  	<label class="col-sm-1 control-label" style="width:10%">绩效模板</label>
			  	<div class="col-sm-2">
			  		<select class="form-control" id="template" onchange="selectTemplate()">
			  			<option value=''>请选择</option>
					</select>
			  	</div>
			  </div>
			  <div class="form-group" style="margin-bottom:2px">
			  	<div class="col-sm-1" style="width:10%"></div>
			  	<div class="col-sm-10" style="text-align:right">
			  		 <button type="button" class="btn btn-primary" onclick="addCheckProject()"><span class="glyphicon glyphicon-plus"></span> 项目</button>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<label class="col-sm-1 control-label" style="width:10%">考核项目&nbsp;<span style="color:red;">*</span></label>
			  	<div class="col-sm-10">
			  		<table class="tab">
			  			<thead>
			  			<tr>
			  				<td class="title" style="width:15%">项目</td>
			  				<td class="title" style="width:15%">考核项目</td>
			  				<td class="title" style="width:10%">考核系数</td>
			  				<td class="title" colspan="2" style="width:20%">奖励条件</td>
			  				<td class="title" colspan="2" style="width:20%">少发条件</td>
			  				<td class="title" style="width:10%">操作</td>
			  			</tr>
			  			</thead>
			  			<tbody id="project">
			  			</tbody>
			  		</table>
			  	</div>
			  </div>
			  <div class="form-group">
			  	<div class="control-label col-sm-1" style="width:10%">
			  		<button type="submit" class="btn btn-primary">添加</button>
			  	</div>
			  </div>
</script>
</body>
</html>