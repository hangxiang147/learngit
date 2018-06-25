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
	a:hover, a:focus{text-decoration:none}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/train/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">培训课程列表</h3>
          <br>
          <form action="train/myCourseList" class="form-horizontal">
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
                  <th style="width:10%">培训类别</th>
                  <th style="width:15%">课程名称</th>
                  <th style="width:10%">课时</th>
                  <th style="width:10%">角色</th>
                  <th style="width:10%">已报名人数</th>
                  <th style="width:15%">首开培训时间</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${trainCourses}" var="trainCourse" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${trainCourse.trainClass}</td>
              			<td>${trainCourse.courseName}</td>
              			<td>${trainCourse.classHour}</td>
              			<td>${trainCourse.role}</td>
              			<td>${trainCourse.joinerNum}</td>
              			<td>${trainCourse.beginTime}</td>
              			<td>
              				<a href="train/showCourseDetail?courseId=${trainCourse.id}&myCourse=true">
              				<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
	             				<use xlink:href="#icon-Detailedinquiry"></use>
	             			</svg>
	             			</a>
	             			<c:if test="${trainCourse.role=='学员'}">
	             			<a href="javacript:void(0)" onclick="vacation(${trainCourse.id})">
	             			<svg class="icon" aria-hidden="true" title="请假" data-toggle="tooltip">
	             				<use xlink:href="#icon-qingjia"></use>
	             			</svg>
	             			</a>
	             			</c:if>
	             			<c:if test="${trainCourse.role=='讲师'}">
	             			<a href="javacript:void(0)" onclick="startClassHour(${trainCourse.id})">
	             			<svg class="icon" aria-hidden="true" title="开始上课" data-toggle="tooltip">
	             				<use xlink:href="#icon-kaishi"></use>
	             			</svg>
	             			</a>
	             			</c:if>
              			</td>
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
