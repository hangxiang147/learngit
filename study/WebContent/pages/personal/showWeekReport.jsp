<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工作汇报截图页面</title>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript" src="assets/js/html2canvas.min.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){  
    $("#jietu").click(function(e){  
    	var targetDom = $("#screen");  
    	var copyDom = targetDom.clone();
    	copyDom.width(targetDom.width() + "px");    
        copyDom.height(targetDom.height() + "px"); 
        $("body").append(copyDom);
    	html2canvas(copyDom, { 
    		allowTaint: true,    
            taintTest: false, 
    		onrendered: function(canvas){
    			 var imgUri = canvas.toDataURL();
    			 copyDom.remove();
    			 $("#picture").attr("src",imgUri);
    			 	var photos = {"start": 0, "data":[]};
    				photos.data.push({"alt":name, "src":imgUri});
    				layer.photos({
    					offset:'100px',
    				    photos: photos,
    				    anim: 5
    			 });
    		}
        });  
     });  
});
	function printOrder(){
		var headstr = "<html><head><title></title></head><body>";  
		var footstr = "</body>";  
		var printData = document.getElementById("screen").innerHTML;
		var oldstr = document.body.innerHTML;  
		document.body.innerHTML = headstr+printData+footstr; 
		window.print();
		document.body.innerHTML = oldstr;  
		var argSearch = location.search;
		argSearch = argSearch.substring(1, argSearch.length);
		$("input[name='weekReportId']").val(argSearch.split("=")[1]);
		window.location.reload();
	   	}
</script>
<style type="text/css">
#weekReport{
	border-collapse:collapse;
	width:100%;
	margin-top:5px;
}
#weekReport tr td{word-wrap:break-word;font-size:10px;padding:8px 7px;text-align:center;border:1px solid #ddd}
.title{
	text-align:left !important;
	background:#efefef !important;
}
.form-control{
	display:inline-block;
}
.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
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
			<%@include file="/pages/personal/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<button id="print" type="button" class="btn btn-default"
						onclick="printOrder()" style="float:right;margin-top:-10px;"
						>打印</button>
				<br>
				<div style="float:right"><button id="jietu" class="btn btn-primary">生成图片</button><span style="color:red;">（注：若无法完整截屏，可生成图片，再右击【复制图片】）</span></div>
				<br><br>
				<div id="screen" style="background:#fff">
				<div style="font-weight:bold">
					<span style="float: left">姓名：<c:if test="${isPartner}"><svg class="icon" style="margin-bottom:-0.6%;color:#ec4c4c" aria-hidden="true"><use xlink:href="#icon-hezhu"></use></svg></c:if>${userName}（<c:forEach
							var="group" items="${groupList }" varStatus="st">
							<c:if test="${st.index != 0 }"> | </c:if>${group }</c:forEach>）
					</span>
				</div>
				<div style="line-height: 20px; font-size: 14px; color: #888888; height: 30px;">
					<span style="float: left">提交时间：${weekReport.addTime}</span>
				</div>
				<img id="picture"  style="display:none" alt="" src="">
				<div class="tab">
					<table id="weekReport">
						<tbody id="thisWeekWork">
							<tr style="font-weight:bold">
								<td colspan="7" class="title">本周工作</td>
							</tr>
							<tr style="font-weight:bold">
								<td style="width:30%">工作内容</td>
								<td style="width:9%">任务下达人</td>
								<td style="width:12%">计划开始日期</td>
								<td style="width:12%">计划结束日期</td>
								<td style="width:12%">实际开始日期</td>
								<td style="width:12%">实际结束日期</td>
								<td>完成情况</td>
							</tr>
							<c:forEach items="${thisWeekWorkVos.content}" varStatus="status">
								<tr>
									<td>${thisWeekWorkVos.content[status.index]}</td>
									<td>${thisWeekWorkVos.assigner[status.index]}</td>
									<td>${thisWeekWorkVos.planBeginDate[status.index]}</td>
									<td>${thisWeekWorkVos.planEndDate[status.index]}</td>
									<td>${thisWeekWorkVos.actualBeginDate[status.index]}</td>
									<td>${thisWeekWorkVos.actualEndDate[status.index]}</td>
									<td>${thisWeekWorkVos.completeRate[status.index]}%</td>
								</tr>
							</c:forEach>
						</tbody>
						<tbody id="addRisk">
							<tr style="font-weight:bold">
							<td colspan="7" class="title">风险点或问题</td>
							</tr>
							<tr style="font-weight:bold">
								<td colspan="2">问题描述</td>
								<td colspan="3">解决方案</td>
								<td>计划解决日期</td>
								<td>责任人</td>
							</tr>
							<c:forEach items="${riskVos.planSolveDate}" varStatus="status">
								<tr>
									<td colspan="2">${riskVos.riskDescription[status.index]}</td>
									<td colspan="3">${riskVos.solution[status.index]}</td>
									<td>${riskVos.planSolveDate[status.index]}</td>
									<td>${riskVos.responsiblePerson[status.index]}</td>
								</tr>
							</c:forEach>
						</tbody>
						<tbody id="nextWeekWork">
							<tr style="font-weight:bold">
							<td colspan="7" class="title">下周工作计划</td>
							</tr>
							<tr style="font-weight:bold">
								<td colspan="3">工作内容</td>
								<td colspan="2">计划开始日期</td>
								<td colspan="2">计划结束日期</td>
							</tr>
							<c:forEach items="${nextWeekWorkPlans.content}" varStatus="status">
								<tr>
									<td colspan="3">${nextWeekWorkPlans.content[status.index]}</td>
									<td colspan="2">${nextWeekWorkPlans.planBeginDate[status.index]}</td>
									<td colspan="2">${nextWeekWorkPlans.planEndDate[status.index]}</td>
								</tr>
							</c:forEach>
						</tbody>
							<tr style="font-weight:bold">
							<td colspan="7" class="title">本周工作总结</td>
							</tr>
							<tr>
								<td id="weekWorkSummary" colspan="7" style="text-align:left">
									${weekReport.weekWorkSummary}
								</td>
							</tr>
					</table>
				</div>
				</div>
				<div style="padding-top: 20px;float: left">
					<span><button type="button"
							class="btn btn-primary"
							onclick="location.href='javascript:history.go(-1);'">返回</button></span>
					<span style="color: red; margin-left: 20px;">注：此页面为工作周报截屏页！</span>
				</div>
			</div>
		</div>
	</div>
	<input name="weekReportId" type="hidden">
</body>
</html>