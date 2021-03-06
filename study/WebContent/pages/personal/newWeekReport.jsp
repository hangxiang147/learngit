<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="zh-CN">

<head>
<script type="text/javascript">

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
		background:#efefef;
	}
	.form-control{
		display:inline-block;
	}
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/pages/personal/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">
					工作周报<span style="color: red; font-size: 20px;">（工作任务请如实汇报，杜绝弄虚作假，违者重罚）</span>
				</h3>
				<form method="post" action="personal/save_saveWeekReport">
					<s:token></s:token>
					<input type="hidden" name="weekReport.userId"
						value="${staff.userID}" />
					<div class="form-group">
						<label for="executor" class="col-sm-1 control-label">汇报人</label>
						<div class="col-sm-1">
							<span class="detail-control">${staff.lastName }</span>
						</div>
					</div>
					<br><br>
					<div class="form-group" >
						<label for="beginDate" class="col-sm-1 control-label">汇报时间</label>

						<div class="col-sm-2" style="padding-right: 0px;">
							<span class="detail-control">${date}</span>
						</div>
					</div>
					<br><br>
					<span style="color:red">（本页为提交页面，汇报工作无效，<span style="color:red; font-size: 24px">截屏无效</span>）
					</span>
					<table id="weekReport">
						<tbody id="thisWeekWork">
							<tr>
								<td colspan="8" class="title">本周工作<span style="color:red"> *</span><a onclick="addThisWeekWork()" style="float:right;font-size:10px" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td style="width:30%">工作内容<span style="font-weight:bold">（不超过50字）</span><span style="color:red"> *</span></td>
								<td style="width:9%">任务下达人<span style="color:red"> *</span></td>
								<td style="width:12%">计划开始日期<span style="color:red"> *</span></td>
								<td style="width:12%">计划结束日期<span style="color:red"> *</span></td>
								<td style="width:12%">实际开始日期<span style="color:red"> *</span></td>
								<td style="width:12%">实际结束日期<span style="color:red"> *</span></td>
								<td>完成情况<span style="color:red"> *</span></td>
								<td style="width:4%">操作</td>
							</tr>
							<tr>
								<td><input class="form-control" autocomplete="off" required name="thisWeekWorkVo.content" maxLength="50"></td>
								<td><input class="assigner form-control" name="thisWeekWorkVo.assigner" autocomplete="off" required type="text"></td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="planBeginDate" name="thisWeekWorkVo.planBeginDate" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'planEndDate\')}'})"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="planEndDate" name="thisWeekWorkVo.planEndDate" required
	    			 				onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'planBeginDate\')}' })"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="actualBeginDate" name="thisWeekWorkVo.actualBeginDate" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'actualEndDate\')}'})"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="actualEndDate" name="thisWeekWorkVo.actualEndDate" required
	    			 				onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'actualBeginDate\')}' })"/>
								</td>
								<td>
								<input style="width:81%" type="number" min="1" max="100" class="form-control" autocomplete="off" required name="thisWeekWorkVo.completeRate">%
								</td>
								<td></td>
							</tr>
						</tbody>
						<tbody id="addRisk">
							<tr>
							<td colspan="8" class="title">风险点或问题<a onclick="addRisk()" style="float:right;font-size:10px" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td colspan="2">问题描述<span style="font-weight:bold">（不超过100字）</span><span style="color:red"> *</span></td>
								<td colspan="3">解决方案<span style="font-weight:bold">（不超过100字）</span><span style="color:red"> *</span></td>
								<td>计划解决日期<span style="color:red"> *</span></td>
								<td>责任人<span style="color:red"> *</span></td>
								<td>操作</td>
							</tr>
						</tbody>
						<tbody id="nextWeekWork">
							<tr>
							<td colspan="8" class="title">下周工作计划<a onclick="addNextWeekWork()" style="float:right;font-size:10px" class="btn btn-primary">增加</a></td>
							</tr>
							<tr>
								<td colspan="3">工作内容<span style="font-weight:bold">（不超过50字）</span><span style="color:red"> *</span></td>
								<td colspan="2">计划开始日期<span style="color:red"> *</span></td>
								<td colspan="2">计划结束日期<span style="color:red"> *</span></td>
								<td>操作</td>
							</tr>
						</tbody>
							<tr>
							<td colspan="8" class="title">本周工作总结<span style="font-weight:bold">（不超过1000字）</span><span style="color:red"> *</span></td>
							</tr>
							<tr>
								<td colspan="8">
									<textarea rows="4" style="width:99.9%" class="form-control" name="weekWorkSummary" maxLength="1000" id="weekWorkSummary"></textarea>
								</td>
							</tr>
					</table>
					<br>
					<input style="display: none;" type="submit" id="sub" value="submit" />
					<button id="submitBtn" type="button" onclick="confirmOK()" class="btn btn-primary" style="float:right">提交</button>
				</form>
			</div>
		</div>
	</div>
	<script type="text/html" id="addThisWeekWorkHtml">
								<tr>
								<td><input class="form-control" autocomplete="off" required name="thisWeekWorkVo.content" maxLength="50"></td>
								<td><input class="assigner form-control" name="thisWeekWorkVo.assigner" autocomplete="off" required type="text"></td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="planBeginDate" name="thisWeekWorkVo.planBeginDate" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'planEndDate\')}'})"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="planEndDate" name="thisWeekWorkVo.planEndDate" required
	    			 				onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'planBeginDate\')}' })"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="actualBeginDate" name="thisWeekWorkVo.actualBeginDate" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'actualEndDate\')}'})"/>
								</td>
								<td>
									<input type="text" autocomplete="off" class="form-control" id="actualEndDate" name="thisWeekWorkVo.actualEndDate" required
	    			 				onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'actualBeginDate\')}' })"/>
								</td>
								<td>
								<input style="width:81%" type="number" min="1" max="100" class="form-control" autocomplete="off" required name="thisWeekWorkVo.completeRate">%
								</td>
								<td>
									<a href="javascript:void(0)" onclick="deleteThisWeekWork(this)"><span class="glyphicon glyphicon-remove"></span></a>
								</td>
	</script>
	<script type="text/html" id="addRiskHtml">
	<tr>
	<td colspan="2"><input class="form-control" autocomplete="off" required name="riskVo.riskDescription" maxLength="100"></td>
	<td colspan="3"><input class="form-control" autocomplete="off" required name="riskVo.solution" maxLength="100"></td>
	<td>
		<input type="text" autocomplete="off" class="form-control" name="riskVo.planSolveDate" required
		onclick="WdatePicker({ minDate:'%y-%M-%d'})"/>
	</td>
	<td>
		<input class="assigner form-control" name="riskVo.responsiblePerson" autocomplete="off" required type="text">
	</td>
	<td>
		<a href="javascript:void(0)" onclick="deleteRisk(this)"><span class="glyphicon glyphicon-remove"></span></a>
	</td>
	</tr>
	</script>
	<script type="text/html" id="addNextWeekWorkHtml">
							<tr>
								<td colspan="3">
									<input class="form-control" autocomplete="off" required name="nextWeekWorkPlan.content" maxLength="50">	
								</td>
								<td colspan="2">
									<input type="text" id="nextWorkPlanBeginDate" autocomplete="off" class="form-control" name="nextWeekWorkPlan.planBeginDate" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'nextWorkPlanEndDate\')}', minDate:'%y-%M-%d'})"/>
								</td>
								<td colspan="2">
									<input type="text" id="nextWorkPlanEndDate" autocomplete="off" class="form-control" name="nextWeekWorkPlan.planEndDate" required
	    			 				onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd', minDate:'#F{$dp.$D(\'nextWorkPlanBeginDate\')}' })"/>
								</td>
								<td>
									<a href="javascript:void(0)" onclick="deleteNextWeekWork(this)"><span class="glyphicon glyphicon-remove"></span></a>
								</td>
							</tr>
	</script>
    <script src="/assets/js/layer/layer.js"></script>
    <script src="/assets/js/require/require2.js"></script>
    <script src="/assets/editor/kindeditor-min.js"></script>
	<script src="/assets/editor/lang/zh_CN.js"></script>
	<script>
	$(function(){
		//富文本
    	kedit('#weekWorkSummary');
		layer.alert("汇报时间不要超过半个小时，否则会跳回登录界面",{offset:'100px'});
		if('${disabled}'=='true'){
			//$("#submitBtn").attr("disabled","disabled");
		}
	});
	require(['staffComplete'],function (staffComplete){
		$(".assigner").each(function(){
			new staffComplete().render($(this),function ($item){
    		});
		});
	});
	var index = 0;
	function addThisWeekWork(){
		index++;
		$("#thisWeekWork").append($("#addThisWeekWorkHtml").html().replace(/\"planBeginDate\"/g,"\"planBeginDate"+index+"\"")
				.replace(/\\'planBeginDate\\'/g,"\\'planBeginDate"+index+"\\'").replace(/\"planEndDate\"/g,"\"planEndDate"+index+"\"")
				.replace(/\\'planEndDate\\'/g,"\\'planEndDate"+index+"\\'").replace(/\"actualBeginDate\"/g,"\"actualBeginDate"+index+"\"")
				.replace(/\\'actualBeginDate\\'/g,"\\'actualBeginDate"+index+"\\'").replace(/\"actualEndDate\"/g,"\"actualEndDate"+index+"\"")
				.replace(/\\'actualEndDate\\'/g,"\\'actualEndDate"+index+"\\'"));
		require(['staffComplete'],function (staffComplete){
			$(".assigner").each(function(){
				new staffComplete().render($(this),function ($item){
	    		});
			});
		});
	}
	function deleteThisWeekWork(target){
		$(target).parent().parent().remove();
	}
	function deleteRisk(target){
		$(target).parent().parent().remove();
	}
	function deleteNextWeekWork(target){
		$(target).parent().parent().remove();
	}
	function addRisk(){
		$("#addRisk").append($("#addRiskHtml").html());
		require(['staffComplete'],function (staffComplete){
			$(".assigner").each(function(){
				new staffComplete().render($(this),function ($item){
	    		});
			});
		});
	}
	function addNextWeekWork(){
		index++;
		$("#nextWeekWork").append($("#addNextWeekWorkHtml").html().replace(/\"nextWorkPlanBeginDate\"/g,"\"nextWorkPlanBeginDate"+index+"\"")
				.replace(/\\'nextWorkPlanBeginDate\\'/g,"\\'nextWorkPlanBeginDate"+index+"\\'").replace(/\"nextWorkPlanEndDate\"/g,"\"nextWorkPlanEndDate"+index+"\"")
				.replace(/\\'nextWorkPlanEndDate\\'/g,"\\'nextWorkPlanEndDate"+index+"\\'"));
		require(['staffComplete'],function (staffComplete){
			$(".assigner").each(function(){
				new staffComplete().render($(this),function ($item){
	    		});
			});
		});
	}
	function confirmOK(){
		if(editor.count('text') == 0){
			layer.alert("工作总结不能为空",{offset:'100px'});
			return false;
		}
 		layer.confirm("提交后无法修改，确定提交？",{offset:'100px'},function (index){
 			$("#sub").click();
 			layer.close(index);
			Load.Base.LoadingPic.FullScreenShow(null);
	    });
	}
	var	editor;
	function kedit(kedit){
		editor =KindEditor.create(kedit, {
				resizeType : 1,
				allowPreviewEmoticons : false,
				items : [
					'fontname', 'fontsize', '|', 'bold', 'italic', 'underline',
					'|', 'justifyleft', 'justifycenter', 'justifyright', '|', 'preview']
			});
	}
	</script>
</body>
</html>
