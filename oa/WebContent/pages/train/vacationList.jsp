<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
	$("[data-toggle='tooltip']").tooltip();
});
</script>
<style type="text/css">
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
        <%@include file="/pages/train/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">请假列表</h3>
          <br>
          <form action="train/vacationList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	    <div class="form-group">
					<label class="col-sm-1 control-label">课程名称</label>
					<div class="col-sm-2">
						<input class="form-control" autocomplete="off" name="courseName" value="${courseName}"/>
					</div>
					<div class="col-sm-1">
						<button type="submit" class="btn btn-primary" >查询</button>
					</div>
				</div>
          </form>
          <h3 class="sub-header"></h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:5%">序号</th>
                  <th style="width:15%">培训类别</th>
                  <th style="width:20%">课程名称</th>
                  <th style="width:15%">请假课时开始时间</th>
                  <th style="width:10%">已报名人数</th>
                  <th style="width:10%">请假人数</th>
                  <th style="width:15%">请假人</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${vacations}" var="vacation" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${vacation.trainClass}</td>
              			<td>${vacation.courseName}</td>
              			<td>${vacation.coursePlanBeginTimes}</td>
              			<td>${vacation.joinerNum}</td>
              			<td>${vacation.vacationPersonNum}</td>
              			<td>${vacation.vacationPersonNames}</td>
              		</tr>
              	</c:forEach>
              </tbody>
            </table>
          </div>
          		<div class="dropdown" >
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
    <div id="addClasshour" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:800px">
		<form class="form-horizontal" method="post" action="train/saveCourseClasshours">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">添加课时</h4>
			</div>
			<div class="modal-body">
				<a class="btn btn-primary" style="margin-left:90%" onclick="addClasshour()"><span class="glyphicon glyphicon-plus"></span> 课时</a>
				<table id="tab">
					<tbody>
						<tr>
							<td style="width:25%">开始日期<span style="color:red"> *</span></td>
							<td style="width:12%">时长（h）<span style="color:red"> *</span></td>
							<td style="width:38%">地点<span style="color:red"> *</span></td>
							<td style="width:15%">讲师<span style="color:red"> *</span></td>
							<td style="width:10%">操作</td>
						</tr>
					</tbody>
					<tbody id="classhour">
					</tbody>
				</table>
			</div>
			<input type="hidden" name="coursePlan.courseId">
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<div id="vacation" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:500px">
		<form class="form-horizontal" method="post" action="train/startVacation" onsubmit="return checkSelected()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">课时列表</h4>
			</div>
			<div class="modal-body">
				<table id="tab">
					<tbody>
						<tr>
							<td><input type="checkbox" class="all"></td>
							<td>开始日期</td>
							<td>时长（h）</td>
							<td>地点</td>
						</tr>
					</tbody>
				  <tbody id="content">
	              </tbody>
				</table>
				<br>
				<div class="form-group">
					<label class="col-sm-2 control-label">原因<span style="color:red"> *</span></label>
					<div class="col-sm-10">
					<textarea class="form-control" rows="2" name="courseVacation.reason" required></textarea>
					</div>
				</div>
			</div>
			<input type="hidden" name="courseVacation.courseId">
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">请假</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<script type="text/javascript">
	//全选checkbox响应其他checkbox的选中事件
	var fullCkOrNot = function(){
	    var allCB = $(".all");
	    if($(".check:checked").length == $(".check").length){
	        allCB.prop("checked",true);
	    }else{
	   	 allCB.prop("checked",false);
		}
	}
	//全选checkbox点击事件
	$(".all").on("click",function(){
		 if($(".all").prop("checked")){
			 $(".check").each(function(){
				 $(this).prop("checked",true);
			 });
		 }else{
			 $(".check").each(function(){
				 $(this).prop("checked",false);
			 });
		 }
	});
	</script>
  </body>
</html>
