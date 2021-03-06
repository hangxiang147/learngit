<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<style type="text/css">
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover, a:focus{text-decoration:none}
	.textcomplete-dropdown{
		z-index:1050 !important;
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/performance/soft/manage/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">待分配问题单</h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:5%">序号</th>
                  <th style="width:20%">问题名称</th>
                  <th style="width:10%">创建人</th>
                  <th style="width:15%">创建时间</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${problemOrderTaskVos}" var="problemOrderTaskVo" varStatus="status">
              		<tr>
              		<td>${status.index+1}</td>
              		<td>${problemOrderTaskVo.orderName}</td>
              		<td>${problemOrderTaskVo.creatorName}</td>
              		<td>${problemOrderTaskVo.addTime}</td>
              		<td>
              			<a onclick="goPath('/performance/soft/showProblemOrderDetail?problemOrderId=${problemOrderTaskVo.id}')" href="javascript:void(0)">
	              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
	              		</a>
	              		&nbsp;
	              		<a href="javascript:void(0)" onclick="allocateOrder(${problemOrderTaskVo.taskId},${problemOrderTaskVo.processInstanceID})" data-toggle="modal" data-target="#allocateOrder">
	              			<svg class="icon" aria-hidden="true" title="分配问题单" data-toggle="tooltip">
             					<use xlink:href="#icon-shenpi"></use>
             				</svg>
	              		</a>
	              		&nbsp;
	              		<a href="javascript:void(0)" onclick="changeToRequire(${problemOrderTaskVo.taskId},${problemOrderTaskVo.processInstanceID})">
	              			<svg class="icon" aria-hidden="true" title="转为需求" data-toggle="tooltip">
             					<use xlink:href="#icon-zhuanhuan"></use>
             				</svg>
	              		</a>
              		</td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
    <div id="allocateOrder" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:350px">
		<form id="allocateOrderForm" class="form-horizontal" action="performance/soft/startAllocateProblemOrder" onsubmit="return addLoading()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">分配任务</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="col-sm-3 control-label">解决人<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<input id="developer" type="text" class="form-control" autocomplete="off" required onkeyup="checkEmpty()">
						<input type="hidden" name="developerId">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">责任人<span style="color:red"> *</span></label>
					<div class="col-sm-8">
						<input id="dutyPerson" type="text" class="form-control" autocomplete="off" required onkeyup="checkEmptyForDuty()">
						<input type="hidden" name="dutyPersonId">
					</div>
				</div>
				<div class="form-group">
					<label class="col-sm-3 control-label">级别<span style="color:red"> *</span></label>
					<div class="col-sm-8">
							<select class="form-control" required name="score">
								<option value="">请选择</option>
								<option value="1">轻微</option>
								<option value="2">一般</option>
								<option value="5">严重</option>
							</select>
					</div>
				</div>
				<input type="hidden" name="taskId">
				<input type="hidden" name="processInstanceId">
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<script src="/assets/icon/iconfont.js"></script>
	<script src="/assets/js/layer/layer.js"></script>
	<script src="/assets/js/require/require2.js"></script>
	<script type="text/javascript">
	$(function(){
		$("[data-toggle='tooltip']").tooltip();
	});
	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('#developer'),function ($item){
			$("input[name='developerId']").val($item.data("userId"));
		});
		new staffComplete().render($('#dutyPerson'),function ($item){
			$("input[name='dutyPersonId']").val($item.data("userId"));
		});
	});
	function checkEmpty(){
		if($("#developer").val()==''){
			$("input[name='developerId']").val('');
		}
	}
	function checkEmptyForDuty(){
		if($("#dutyPerson").val()==''){
			$("input[name='dutyPersonId']").val('');
		}
	}
	$(document).click(function (event) {
		if ($("input[name='developerId']").val()=='')
		{
			$("#developer").val("");
		}
		if ($("input[name='dutyPersonId']").val()=='')
		{
			$("#dutyPerson").val("");
		}
	});
	function allocateOrder(taskId, processInstanceID) {
		$('input[name="taskId"]').val(taskId);
		$('input[name="processInstanceId"]').val(processInstanceID);
	}
	function addLoading(){
		$("#allocateOrder .close").click();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function changeToRequire(taskId, processInstanceId){
		layer.confirm("确认转换为需求？",{offset:'100px',title:'转为需求'},function(index){
			layer.close(index);
			location.href="performance/soft/changeToRequire?taskId="+taskId+"&processInstanceId="+processInstanceId;
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	}
	</script>
  </body>
</html>
