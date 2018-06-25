<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="assets/js/layer/layer.js"></script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;word-break:break-all !important}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:12px;text-align:center}
	.tab table tbody tr td{height:auto;line-height:10px;padding:5px 5px;}
	.title {color:#000;background:#efefef}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
		<%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<h3 class="sub-header" style="margin-top:0px;">打款确认<span style="color:red;font-size:15px">（打款成功，勾选成功，反之勾选失败）</span>
        		<button type="button" style="margin-left:73%" onclick="location.href='/HR/staffSalary/exportPaySalarys?processInstanceId=${processInstanceId}'" class="btn btn-primary"><span class="glyphicon glyphicon-download-alt"></span>工资单</button>
        		<button type="button" style="margin-left:2%" onclick="history.go(-1)" class="btn btn-default">返回</button>
        	</h3>
        	<div class="tab">
			<table>
				<tr>
					<td class="title" style="width:5%">序号</td>
					<td class="title" style="width:6%">姓名</td>
					<td class="title" style="width:5%">工号</td>
					<td class="title" style="width:10%">公司</td>
					<td class="title" style="width:10%">部门</td>
					<td class="title" style="width:7%">税前工资</td>
					<td class="title" style="width:7%">个税</td>
					<td class="title" style="width:7%">税后工资</td>
					<td class="title" style="width:5%"><div style="position:relative;right:17.8%;margin-top:-7%">
					<span style="bottom:3px;position:relative">成功</span><input type="checkbox" class="checkboxClass allSuccess" title="全选"></div></td>
					<td class="title" style="width:5%"><div style="position:relative;right:17.8%;margin-top:-7%">
					<span style="bottom:3px;position:relative">失败</span><input type="checkbox" class="checkboxClass allFailed" title="全选"></div></td>
				</tr>
				<tbody>
					<c:forEach items="${paySalaryInfos}" var="paySalaryInfo" varStatus="status">
					<tr>
						<td>${status.index+1}</td>
						<td>${paySalaryInfo.userName}</td>
						<td>${paySalaryInfo.staffNum}</td>
						<td>${paySalaryInfo.company}</td>
						<td>${paySalaryInfo.department}</td>
						<td>${paySalaryInfo.preTaxSalary}</td>
						<td>${paySalaryInfo.personalIncomeTax}</td>
						<td>${paySalaryInfo.afterTaxSalary}</td>
						<td><input data-index="${status.index+1}" type="checkbox" class="checkboxClass checkSuccess" value="${paySalaryInfo.salaryId}"></td>
						<td><input type="checkbox" class="checkboxClass checkFailed" value="${paySalaryInfo.salaryId}"></td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
			<br>
			<form class="form-horizontal">
			<div class="form-group">
				<label class="col-sm-1 control-label" style="width:10%">备注：</label>
				<div class="col-sm-5">
					<textarea class="form-control" rows="4" id="comment"></textarea>
				</div>
			</div>
			<div class="form-group">
				<div class="col-sm-1" style="width:10%"></div>
				<div class="col-sm-5">
				<button type="button" onclick="confirmSalaryPayStatus()" class="btn btn-primary">确认</button>
				<button type="button" style="margin-left:5%" type="button" onclick="history.go(-1)" class="btn btn-default">返回</button>
				</div>
			</div>
			</form>
		</div>
      </div>
    </div>
    <script type="text/javascript">
    	$(function(){
    		//checkbox单击事件
    		$(".checkSuccess").on("click",function(){
    		    fullCkOrNot("Success");
    		});
    		//checkbox单击事件
    		$(".checkFailed").on("click",function(){
    		    fullCkOrNot("Failed");
    		});
    		//全选checkbox点击事件
    		$(".allSuccess").on("click",function(){
    			 if($(".allSuccess").prop("checked")){
    				 $(".checkSuccess").each(function(){
    					 $(this).prop("checked",true);
    				 });
    			 }else{
    				 $(".checkSuccess").each(function(){
    					 $(this).prop("checked",false);
    				 });
    			 }
    		});
    		$(".allFailed").on("click",function(){
   			 if($(".allFailed").prop("checked")){
   				 $(".checkFailed").each(function(){
   					 $(this).prop("checked",true);
   				 });
   			 }else{
   				 $(".checkFailed").each(function(){
   					 $(this).prop("checked",false);
   				 });
   			 }
   		});
    	});
    	//全选checkbox响应其他checkbox的选中事件
    	var fullCkOrNot = function(check){
    	    var allCB = $(".all"+check);
    	    if($(".check"+check+":checked").length == $(".check"+check).length){
    	        allCB.prop("checked",true);
    	    }else{
    	   	    allCB.prop("checked",false);
    		}
    	}
		function confirmSalaryPayStatus(){
			var comment = $("#comment").val();
			var successSalaryIds = [];
			var failedSalaryIds = [];
			//同时勾选
			var selected = false;
			//同时不勾选
			var unSelected = false;
			var index;
			//检查勾选的情况，成功与失败不可同时勾选，也不可同时不勾选
			$(".checkSuccess").each(function(){
				var successCheck = $(this).prop("checked");
				index = $(this).data("index");
				if(successCheck){
					successSalaryIds.push($(this).val());
				}
				var failedCheck = $(this).parent().next().find(".checkFailed").prop("checked");
				if(successCheck && failedCheck){
					selected = true;
					return false;
				}
				if(!successCheck && !failedCheck){
					unSelected = true;
					return false;
				}
			});
			if(selected){
				layer.alert("第"+index+"行同时勾选了成功和失败，只能<br>勾选其中一项！",{offset:'100px'});
				return;
			}
			if(unSelected){
				layer.alert("第"+index+"行没有勾选成功或失败，请勾选！",{offset:'100px'});
				return;
			}
			$(".checkFailed:checked").each(function(){
				failedSalaryIds.push($(this).val());
			});
			layer.confirm("完成工资发放的打款确认？",{offset:'100px'},function(index){
				layer.close(index);
				location.href = "/HR/staffSalary/confirmSalaryPayStatus?successSalaryIds="+
						successSalaryIds+"&failedSalaryIds="+failedSalaryIds+"&comment="+comment+"&taskId=${taskId}";
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}
</script>
</body>
</html>