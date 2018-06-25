<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>c
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
});
//数组下标
var index_add = 0;
function addCheckProject(obj){
	index_add = 0;
	$("#addCheckProject").find("input[name='project.templateId']").val($(obj).parent().find("input[name='templateId']").val());
	$("#addCheckProject").modal("show");
}
function deleteProject(obj){
	layer.confirm("确定删除？",{offset:'100px'},function(index){
		layer.close(index);
		var projectId = $(obj).parent().data("project");
		var checkItemIds = [];
		$(".checkItemId").each(function(){
			checkItemIds.push($(this).text());
		});
		$.ajax({
			url:'/administration/performance/deleteStaffProject',
			data:{'projectId':projectId,'checkItemIds':checkItemIds.join(',')},
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
function deleteCheckContent(obj){
	layer.confirm("确定删除？",{offset:'100px'},function(index){
		layer.close(index);
		var checkItemId = $(obj).parent().find(".checkItem").next().val();
		if(checkItemId){
			$.ajax({
				url:'administration/performance/deleteStaffCheckItem',
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
	var html = $("#checkContent").html().replace(/checkItem\./g,"staffCheckItem["+index_add+"].");
	$("#modal-content").append(html);
}
function addCheckContentForUpdate(){
	index_add++;
	var html = $("#checkContent").html().replace(/checkItem\./g,"staffCheckItem["+index_add+"].");
	$("#modal-content-update").append(html);
}
function modifyProject(obj){
	$("#updateCheckProject").html($("#modalUpdateHtml").html());
	$("#updateProjectForm").submit(function(){
		updateCheckProject();
	});
	index_add = 0;
	var projectId = $(obj).parent().data("project");
	var checkItemIds = [];
	$(".checkItemId").each(function(){
		checkItemIds.push($(this).text());
	});
	$.ajax({
		url:'/administration/performance/findPersonalProjectCheckContent',
		data:{'projectId':projectId,'checkItemIds':checkItemIds.join(',')},
		success:function(data){
			var project = data.project;
			$("#modal-content-update").find("input[name='project.project']").val(project.project);
			$("input[name='project.id']").val(project.id);
			data.projectChecks.forEach(function(value, index){
				if(index==0){
					$("#modal-content-update").find("input[name='staffCheckItem[0].checkItem']").val(value.checkItem);
					$("#modal-content-update").find("input[name='staffCheckItem[0].coefficient']").val(value.coefficient);
					$("#modal-content-update").find("select[name='staffCheckItem[0].addMoneyType']").val(value.addMoneyType);
					$("#modal-content-update").find("input[name='staffCheckItem[0].perAddMoneyValue']").val(value.perAddMoneyValue);
					$("#modal-content-update").find("input[name='staffCheckItem[0].addMoney']").val(value.addMoney);
					$("#modal-content-update").find("select[name='staffCheckItem[0].reduceMoneyType']").val(value.reduceMoneyType);
					$("#modal-content-update").find("input[name='staffCheckItem[0].perReduceMoneyValue']").val(value.perReduceMoneyValue);
					$("#modal-content-update").find("input[name='staffCheckItem[0].reduceMoney']").val(value.reduceMoney);
					$("#modal-content-update").find("input[name='staffCheckItem[0].id']").val(value.id);
					$("#modal-content-update").find("input[name='staffCheckItem[0].cloneId']").val(value.cloneId);
					$("#modal-content-update").find("input[name='staffCheckItem[0].addTime']").val(value.addTime);
				}else{
					var html = $("#checkContent").html().replace(/checkItem\./g, "staffCheckItem["+index+"].");
					$("#modal-content-update").append(html);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].checkItem']").val(value.checkItem);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].coefficient']").val(value.coefficient);
					$("#modal-content-update").find("select[name='staffCheckItem["+index+"].addMoneyType']").val(value.addMoneyType);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].perAddMoneyValue']").val(value.perAddMoneyValue);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].addMoney']").val(value.addMoney);
					$("#modal-content-update").find("select[name='staffCheckItem["+index+"].reduceMoneyType']").val(value.reduceMoneyType);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].perReduceMoneyValue']").val(value.perReduceMoneyValue);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].reduceMoney']").val(value.reduceMoney);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].id']").val(value.id);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].cloneId']").val(value.cloneId);
					$("#modal-content-update").find("input[name='staffCheckItem["+index+"].addTime']").val(value.addTime);
					index_add++;
				}
			});
			$("#updateCheckProject").modal("show");
		}
	});
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
		url:'/administration/performance/saveStaffCheckProject',
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
			       		'<td rowspan="2" class="coefficient">'+value.coefficient+'</td>'+
				        '<td class="title">'+addMoneyType+'</td>'+
		  				'<td class="title">奖励金额</td>'+
		  				'<td class="title">'+reduceMoneyType+'</td>'+
		  				'<td class="title">少发金额</td>'+
		  				'<td style="display:none" class="checkItemId">'+value.id+'</td>';
		  		if(index==0){
		  			html += '<td data-project="'+project.id+'" data-number="'+rows+'" rowspan="'+2*rows+'">'+
	  						'<a onclick="modifyProject(this)" href="javascript:void(0)">'+
        					'<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
      						'<use xlink:href="#icon-modify"></use>'+
      						'</svg></a>&nbsp;&nbsp;'+
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
		url:'/administration/performance/updateStaffCheckProject',
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
			       		'<td rowspan="2" class="coefficient">'+value.coefficient+'</td>'+
				        '<td class="title">'+addMoneyType+'</td>'+
		  				'<td class="title">奖励金额</td>'+
		  				'<td class="title">'+reduceMoneyType+'</td>'+
		  				'<td class="title">少发金额</td>'+
		  				'<td style="display:none" class="checkItemId">'+value.id+'</td>';
		  		if(index==0){
		  			html += '<td data-project="'+project.id+'" data-number="'+rows+'" rowspan="'+2*rows+'">'+
	  						'<a onclick="modifyProject(this)" href="javascript:void(0)">'+
        					'<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">'+
      						'<use xlink:href="#icon-modify"></use>'+
      						'</svg></a>&nbsp;&nbsp;'+
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
function checkInfo(){
	var coefficient = 0;
	$("#project .coefficient").each(function(){
		coefficient += parseFloat($(this).text().trim());
	});
	coefficient = coefficient.toFixed(2);
	if(coefficient!=1){
		layer.alert("岗位对应的考核项目的考核系数之和必须为1",{offset:'100px'});
		return false;
	}
	var checkItemIds = [];
	$(".checkItemId").each(function(){
		checkItemIds.push($(this).text());
	});
	$("input[name='checkItemIds']").val(checkItemIds.join(","));
	Load.Base.LoadingPic.FullScreenShow(null);
}
</script>
<style type="text/css">
	.title{background-color:#F2F2F2}
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
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/performance/common/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">修改个人绩效方案</h3> 
       	   <form class="form-horizontal" action="administration/performance/save_updatePersonalPerformance" method="post" onsubmit="return checkInfo()">
	 		   <input type="hidden" name="checkItemIds">
	 		   <input type="hidden" name="userId" value="${userId}">
	 		   <input type="hidden" name="year" value="${year}">
	 		   <input type="hidden" name="month" value="${month}">
	 		   <s:token></s:token>
	 		   <div style="font-size:15px;margin-left:5%;margin-bottom:5px">
	 		   		<span>姓名：</span><span>${staff.staffName}</span>
	 		   		<button type="button" class="btn btn-primary" onclick="addCheckProject(this)"
	 		   		style="margin-right:5.3%;float:right;margin-top:-5px;margin-bottom:5px">
	 		   		<span class="glyphicon glyphicon-plus"></span> 项目</button>
	 		   </div>
			   <table class="tab" style="width:90%;margin-left:5%;margin-bottom:2%">
		           <thead>
					   <tr>
						   <td class="title" style="width:15%">项目</td>
						   <td class="title" style="width:15%">考核项目</td>
						   <td class="title" style="width:10%">考核系数</td>
						   <td class="title" colspan="2" style="width:20%">奖励条件</td>
						   <td class="title" colspan="2" style="width:20%">少发条件</td>
						   <td class="title" style="width:10%"></td>
			           </tr>
		           </thead>
		           <tbody id="project">
		           	   <c:forEach items="${projects}" var="project">
			           	   <c:forEach items="${project.staffCheckItems}" var="checkItem" varStatus="status">
			           	   <tr>
			           	   	  <c:if test="${status.index==0}">
			           	   	  	<td rowspan="${fn:length(project.staffCheckItems)*2}">${project.project}</td>
			           	   	  </c:if>
			           	   	  	<td rowspan="2">${checkItem.checkItem}</td>
				  				<td class="coefficient" rowspan="2">${checkItem.coefficient}</td>
				  				<td class="title">${checkItem.addMoneyType=='+'?'每多':'每少'}</td>
				  			  	<td class="title">奖励金额</td>
				  			  	<td class="title">${checkItem.reduceMoneyType=='-'?'每少':'每多'}</td>
				  			  	<td class="title">少发金额</td>
				  			  	<td style="display:none" class="checkItemId">${checkItem.id}</td>
				  			  <c:if test="${status.index==0}">
				  			  	<td data-project="${project.id}" data-number="${fn:length(project.staffCheckItems)}"
				  			  	 	rowspan="${fn:length(project.staffCheckItems)*2}">
			  						<a onclick="modifyProject(this)" href="javascript:void(0)">
			        					<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
			      							<use xlink:href="#icon-modify"></use>
			      						</svg>
		      						</a>
		      						&nbsp;
				  					<a onclick="deleteProject(this)" href="javascript:void(0)">
			        					<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
			      							<use xlink:href="#icon-delete"></use>
			      						</svg>
		      						</a>
	      						</td>
				  			  </c:if>
			           	   </tr>
			           	   <tr>
				           	   <td>${checkItem.perAddMoneyValue!=null?checkItem.perAddMoneyValue:'——'}</td>
				           	   <td>${checkItem.addMoney!=null?checkItem.addMoney:'——'}</td>
				           	   <td>${checkItem.perReduceMoneyValue!=null?checkItem.perReduceMoneyValue:'——'}</td>
				           	   <td>${checkItem.reduceMoney!=null?checkItem.reduceMoney:'——'}</td>
			  			   </tr>
			           	   </c:forEach>
		           	   </c:forEach>
		           </tbody>
	           </table>
	       <div style="margin-left:5%">
	           <button type="submit" class="btn btn-primary">确认</button>
	           <button type="button" class="btn btn-default" style="margin-left:3%" onclick="history.go(-1)">返回</button>
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
					<h4 class="modal-title" id="myModalLabel">新增项目</h4>
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
					<input type="hidden" name="staffCheckItem[0].userId" value="${userId}">
					<input type="hidden" name="staffCheckItem[0].year" value="${year}">
					<input type="hidden" name="staffCheckItem[0].month" value="${month}">
					<div class="form-group">
						<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
						<div class="col-sm-3">
							<input class="form-control checkItem" autocomplete="off" required name="staffCheckItem[0].checkItem"/>
						</div>
						<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
						<div class="col-sm-3">
							<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="staffCheckItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
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
											<select class="form-control type" name="staffCheckItem[0].addMoneyType">
												<option value="+">每多</option>
												<option value="-">每少</option>
											</select>
											</div>
											<div class="col-sm-7">
												<input class="form-control perValue" autocomplete="off" name="staffCheckItem[0].perAddMoneyValue"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
											</div>
										</div>
										<div class="form-group" style="margin-bottom:5px">
											<label class="control-label col-sm-5">奖励金额</label>
											<div class="col-sm-7">
												<input class="form-control money" autocomplete="off" name="staffCheckItem[0].addMoney"
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
											<select class="form-control type" name="staffCheckItem[0].reduceMoneyType">
												<option value="-">每少</option>
												<option value="+">每多</option>
											</select>
											</div>
											<div class="col-sm-7">
												<input class="form-control perValue" autocomplete="off" name="staffCheckItem[0].perReduceMoneyValue"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
											</div>
										</div>
										<div class="form-group" style="margin-bottom:5px">
											<label class="control-label col-sm-5">少发金额</label>
											<div class="col-sm-7">
												<input class="form-control money" autocomplete="off" name="staffCheckItem[0].reduceMoney"
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
					<input type="hidden" name="staffCheckItem[0].userId" value="${userId}">
					<input type="hidden" name="staffCheckItem[0].year" value="${year}">
					<input type="hidden" name="staffCheckItem[0].month" value="${month}">
					<input type="hidden" name="staffCheckItem[0].cloneId">
					<input type="hidden" name="staffCheckItem[0].addTime">
					<div class="form-group">
						<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
						<div class="col-sm-3">
							<input class="form-control checkItem" autocomplete="off" required name="staffCheckItem[0].checkItem"/>
							<input type="hidden" name="staffCheckItem[0].id">
						</div>
						<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
						<div class="col-sm-3">
							<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="staffCheckItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
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
											<select class="form-control addMoneyType" name="staffCheckItem[0].addMoneyType">
												<option value="+">每多</option>
												<option value="-">每少</option>
											</select>
											</div>
											<div class="col-sm-7">
												<input class="form-control perAddMoneyValue" autocomplete="off" name="staffCheckItem[0].perAddMoneyValue"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
											</div>
										</div>
										<div class="form-group" style="margin-bottom:5px">
											<label class="control-label col-sm-5">奖励金额</label>
											<div class="col-sm-7">
												<input class="form-control addMoney" autocomplete="off" name="staffCheckItem[0].addMoney"
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
											<select class="form-control reduceMoneyType" name="staffCheckItem[0].reduceMoneyType">
												<option value="-">每少</option>
												<option value="+">每多</option>
											</select>
											</div>
											<div class="col-sm-7">
												<input class="form-control perReduceMoneyValue" autocomplete="off" name="staffCheckItem[0].perReduceMoneyValue"
												onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
											</div>
										</div>
										<div class="form-group" style="margin-bottom:5px">
											<label class="control-label col-sm-5">少发金额</label>
											<div class="col-sm-7">
												<input class="form-control reduceMoney" autocomplete="off" name="staffCheckItem[0].reduceMoney"
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
					<input type="hidden" name="checkItem.userId" value="${userId}">
					<input type="hidden" name="checkItem.year" value="${year}">
					<input type="hidden" name="checkItem.month" value="${month}">
					<input type="hidden" name="checkItem.cloneId">
					<input type="hidden" name="checkItem.addTime">
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
				<input type="hidden" name="staffCheckItem[0].userId" value="${userId}">
				<input type="hidden" name="staffCheckItem[0].year" value="${year}">
				<input type="hidden" name="staffCheckItem[0].month" value="${month}">
				<div class="form-group">
					<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control checkItem" autocomplete="off" required name="staffCheckItem[0].checkItem"/>
					</div>
					<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="staffCheckItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
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
										<select class="form-control type" name="staffCheckItem[0].addMoneyType">
											<option value="+">每多</option>
											<option value="-">每少</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="staffCheckItem[0].perAddMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">奖励金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="staffCheckItem[0].addMoney"
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
										<select class="form-control type" name="staffCheckItem[0].reduceMoneyType">
											<option value="-">每少</option>
											<option value="+">每多</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perValue" autocomplete="off" name="staffCheckItem[0].perReduceMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">少发金额</label>
										<div class="col-sm-7">
											<input class="form-control money" autocomplete="off" name="staffCheckItem[0].reduceMoney"
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
				<input type="hidden" name="staffCheckItem[0].userId" value="${userId}">
				<input type="hidden" name="staffCheckItem[0].year" value="${year}">
				<input type="hidden" name="staffCheckItem[0].month" value="${month}">
				<input type="hidden" name="staffCheckItem[0].cloneId">
				<input type="hidden" name="staffCheckItem[0].addTime">
				<div class="form-group">
					<label class="col-sm-2 control-label">考核内容<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input class="form-control checkItem" autocomplete="off" required name="staffCheckItem[0].checkItem"/>
						<input type="hidden" name="staffCheckItem[0].id">
					</div>
					<label class="col-sm-2 control-label">考核系数<span style="color:red"> *</span></label>
					<div class="col-sm-3">
						<input maxLength="4" class="form-control coefficient" autocomplete="off" required name="staffCheckItem[0].coefficient" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(), checkCoefficient(this)"/>
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
										<select class="form-control addMoneyType" name="staffCheckItem[0].addMoneyType">
											<option value="+">每多</option>
											<option value="-">每少</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perAddMoneyValue" autocomplete="off" name="staffCheckItem[0].perAddMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">奖励金额</label>
										<div class="col-sm-7">
											<input class="form-control addMoney" autocomplete="off" name="staffCheckItem[0].addMoney"
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
										<select class="form-control reduceMoneyType" name="staffCheckItem[0].reduceMoneyType">
											<option value="-">每少</option>
											<option value="+">每多</option>
										</select>
										</div>
										<div class="col-sm-7">
											<input class="form-control perReduceMoneyValue" autocomplete="off" name="staffCheckItem[0].perReduceMoneyValue"
											onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v(),checkNum(this)">
										</div>
									</div>
									<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-5">少发金额</label>
										<div class="col-sm-7">
											<input class="form-control reduceMoney" autocomplete="off" name="staffCheckItem[0].reduceMoney"
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
</body>
</html>