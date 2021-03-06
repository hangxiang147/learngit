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
function auditVacation(taskId, processInstanceID){
		layer.open({
            	content: '<div class="form-group">'+
			 	'<label class="col-sm-4 control-label">备注：</label>'+
		     	'<div class="col-sm-8">'+
			 	'<textarea class="form-control" id="reason"/></div></div>',
				offset: '100px',
				title: '审批',
				id: 'auditVacation',
	            btn: ['同意', '不同意'],  
	            yes: function() {  
	            	location.href = "train/auditVacation?auditResult=1&processInstanceID="+processInstanceID
	            			+"&taskId="+taskId+"&comment="+$("#reason").val();
	            	Load.Base.LoadingPic.FullScreenShow(null);
	            },
	            btn2: function(){
	            	location.href = "train/auditVacation?auditResult=2&processInstanceID="+processInstanceID
	            			+"&taskId="+taskId+"&comment="+$("#reason").val();
	            	Load.Base.LoadingPic.FullScreenShow(null);
	            }
	    });
		$("#auditVacation").next().next().css("text-align","center");
}
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
          <h3 class="sub-header" style="margin-top:0px;">请假列表</h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:5%">序号</th>
                  <th style="width:15%">标题</th>
                  <th style="width:15%">申请时间</th>
                  <th style="width:15%">课程名称</th>
                  <th style="width:15%">课时开始时间</th>
                  <th style="width:15%">请假原因</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${courseVacations}" var="courseVacation" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${courseVacation.title}</td>
              			<td>${courseVacation.requestDate}</td>
              			<td>${courseVacation.courseName}</td>
              			<td>${courseVacation.coursePlanBeginTimes}</td>
              			<td>${courseVacation.reason}</td>
              			<td>
              				<a href="javascript:void(0)" onclick="auditVacation('${courseVacation.taskId}','${courseVacation.processInstanceID}')">
              				<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
	             				<use xlink:href="#icon-shenpi"></use>
	             			</svg>
	             			</a>
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
	<script type="text/html" id="addClasshourHtml">
						<tr>
							<td>
								<input type="text" autocomplete="off" class="form-control" name="coursePlan._beginTime" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd HH:mm:00'})"/>
							</td>
							<td>
								<input autocomplete="off" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control" 
	    						name="coursePlan._trainHours" required/>
							</td>
							<td>
								<input class="form-control" autocomplete="off" required name="coursePlan._place" maxLength="100">
							</td>
							<td>
								<select class="form-control" id="selectLecturer" name="coursePlan._lecturer" required>
								</select>
							</td>
							<td>
								<a class="delete" href="javascript:void(0)" onclick="$(this).parent().parent().remove()">
								<svg class="icon" aria-hidden="true" title="删除" data-toggle="tooltip">
	             					<use xlink:href="#icon-delete"></use>
	             				</svg>
	             				</a>
							</td>
						</tr>
	</script>
	<script type="text/html" id="addClasshourHtmlForFirst">
						<tr>
							<td>
								<input type="text" autocomplete="off" class="form-control" name="coursePlan._beginTime" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd HH:mm:00'})"/>
							</td>
							<td>
								<input autocomplete="off" onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,'');}).call(this)" onblur="this.v();" class="form-control" 
	    						name="coursePlan._trainHours" required/>
							</td>
							<td>
								<input class="form-control" autocomplete="off" required name="coursePlan._place" maxLength="100">
							</td>
							<td>
								<select class="form-control" id="selectLecturer" name="coursePlan._lecturer" required>
								</select>
							</td>
							<td>
								——
							</td>
						</tr>
	</script>
  </body>
</html>
