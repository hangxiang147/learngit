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
var optionHtml = "";
var deadLine = "";
function arrangeClasshours(courseId){
	Load.Base.LoadingPic.FullScreenShow(null);
	$.ajax({
		url:'train/arrangeClasshours',
		type:'post',
		data:{'courseId':courseId},
		success:function(data){
			deadLine = data.deadLine;
			$("input[name='coursePlan.courseId']").val(data.courseId);
			optionHtml = "<option value=''>请选择</option>";
			data.staffs.forEach(function(value, index){
				optionHtml += "<option value='"+value.userID+"'>"+value.lastName+"</option>";
			});
			if(data.coursePlans.length>0){
				$('#classhour').html("");
			}else{
				$('#classhour').html($("#addClasshourHtmlForFirst").html().replace(/替换区域/g,deadLine));
			}
			data.coursePlans.forEach(function(value, index){
				var selectedOptionHtml = "<option value=''>请选择</option>";
				data.staffs.forEach(function(_value, _index){
					if(value.lecturer == _value.userID){
						selectedOptionHtml += "<option value='"+_value.userID+"' selected>"+_value.lastName+"</option>";
					}else{
						selectedOptionHtml += "<option value='"+_value.userID+"' >"+_value.lastName+"</option>";
					}
				});
				$('#classhour').append('<tr><td>'+
						'<input type="text" autocomplete="off" class="form-control" name="coursePlan._beginTime" required '+
						'onclick="WdatePicker({ dateFmt: \'yyyy-MM-dd HH:mm:00\', minDate:\''+data.deadLine+'\'})" value="'+value.beginTime+'"/>'+
						'</td>'+
						'<td>'+
						'<input autocomplete="off" value="'+value.trainHours+'"onkeyup="(this.v=function(){this.value=this.value.replace(/[^0-9/.]+/,\'\');}).call(this)" onblur="this.v();" class="form-control"'+
						'name="coursePlan._trainHours" required/>'+
						'</td>'+
						'<td>'+
						'<input class="form-control" autocomplete="off" required name="coursePlan._place" value="'+value.place+'" maxLength="100">'+
						'</td>'+
						'<td>'+
						'<select class="form-control" name="coursePlan._lecturer" required>'+selectedOptionHtml+
						'</select>'+
						'</td>'+
						'<td></td><input type="hidden" name="coursePlan._id" value="'+value.id+'"></tr>');
			});
			$("#addClasshour").modal('show');
			$("#selectLecturer").html(optionHtml);
		},
		complete:function(){
			Load.Base.LoadingPic.FullScreenHide();
		}
	});
}
var index = 0;
function addClasshour(){
	index++;
	var classhourHtml = $('#addClasshourHtml').html().replace(/selectLecturer/g,"selectLecturer"+index).replace(/替换区域/g,deadLine);
	$('#classhour').append(classhourHtml);
	$("#selectLecturer"+index).html(optionHtml);
	$("[data-toggle='tooltip']").tooltip();
}
function join(courseId){
	layer.confirm("确认报名？",{offset:'100px'},function(index){
		layer.close(index);
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'train/joinCourse',
			data:{'courseId':courseId},
			success:function(data){
				if(data.joined=="true"){
					layer.alert("您已报名！",{offset:'100px'});
				}else if(data.success=="true"){
					layer.alert("报名成功！",{offset:'100px'},function(index){
						layer.close(index);
						location.href = "train/findCourseList";
						Load.Base.LoadingPic.FullScreenShow(null);
					});
				}
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
		});
	});
}
function deleteTrainCourse(courseId){
	layer.open({  
        content: '<div class="form-group">'+
				 '<label class="col-sm-4 control-label">取消原因<span style="color:red">*</span>：</label>'+
			     '<div class="col-sm-8">'+
				 '<textarea class="form-control" id="deleteReason"/></div></div>',
        btn: ['确认', '取消'], 
        offset:'100px',
        yes: function(index) { 
        	var cancelReason = $("#deleteReason").val();
        	if(cancelReason==''){
        		layer.alert("取消原因不能为空",{offset:'100px'});
        		return;
        	}
        	layer.close(index);
        	Load.Base.LoadingPic.FullScreenShow(null);
        	$.ajax({
        		url:'train/deleteTrainCourse',
        		data:{'courseId':courseId,'cancelReason':cancelReason},
        		success:function(data){
        			var cancel = data.cancel;
        			if(cancel=="false"){
        				layer.alert("课程已开始，无法作废",{offset:'100px'});
        			}else{
        				window.location.href='train/findCourseList';
        				Load.Base.LoadingPic.FullScreenShow(null);
        			}
        		},
        		complete:function(){
        			Load.Base.LoadingPic.FullScreenHide();
        		}
        	});
        }
    }); 
}
</script>
<style type="text/css">
	.hand{cursor:pointer}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	a:hover, a:focus{text-decoration:none}
	.delete:hover{color:red !important}
	#tab{
		border-collapse:collapse;
		width:100%;
		margin-top:5px;
	}
	#tab tr td{word-wrap:break-word;font-size:10px;padding:8px 7px;text-align:center;border:1px solid #ddd}
	.title{
		text-align:left !important;
		background:#efefef;
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/train/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">培训课程列表</h3>
          <br>
          <form action="train/findCourseList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	    <div class="form-group">
					<label class="col-sm-1 control-label">课程名称</label>
					<div class="col-sm-2">
						<input class="form-control" autocomplete="off" name="courseName" value="${courseName}"/>
					</div>
					<label class="col-sm-1 control-label">状态</label>
					<div class="col-sm-2">
						<select class="form-control" name="status">
							<option value="">请选择</option>
							<option value="报名进行中" ${status=='报名进行中'?'selected':''}>报名进行中</option>
							<option value="报名截止" ${status=='报名截止'?'selected':''}>报名截止</option>
							<option value="不可报名" ${status=='不可报名'?'selected':''}>不可报名</option>
							<option value="完结" ${status=='完结'?'selected':''}>完结</option>
							<option value="取消" ${status=='取消'?'selected':''}>取消</option>
						</select>
					</div>
					<div class="col-sm-1">
						<button type="submit" class="btn btn-primary" >查询</button>
					</div>
				</div>
          </form>
          <auth:hasPermission name="trainManagement">
          <a class="btn btn-primary" href="train/addCourse"><span class="glyphicon glyphicon-plus"></span> 课程</a>
          </auth:hasPermission>
          <h3 class="sub-header"></h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:5%">序号</th>
                  <th style="width:10%">培训类别</th>
                  <th style="width:10%">课程名称</th>
                  <th style="width:10%">课时</th>
                  <th style="width:10%">讲师</th>
                  <th style="width:10%">已报名人数</th>
                  <th style="width:10%">首开培训时间</th>
                  <th style="width:10%">状态</th>
                  <th style="width:10%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${trainCourses}" var="trainCourse" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${trainCourse.trainClass}</td>
              			<td>${trainCourse.courseName}</td>
              			<td>${trainCourse.classHour==null?'——':trainCourse.classHour}</td>
              			<td>${trainCourse.lecturerNames}</td>
              			<td>${trainCourse.joinerNum}</td>
              			<td>${trainCourse.beginTime==null?'——':trainCourse.beginTime}</td>
              			<td><span style="color:${trainCourse.status=='报名进行中'||trainCourse.status=='培训中' ? '#4caf50':trainCourse.status=='完结'?'#2196f3':'red'}">${trainCourse.status}</span></td>
              			<td>
              				<a onclick="goPath('train/showCourseDetail?courseId=${trainCourse.id}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
	             				<use xlink:href="#icon-Detailedinquiry"></use>
	             			</svg>
	             			</a>
	             			<c:if test="${trainCourse.status=='报名进行中'}">
	             			<a href="javacript:void(0)" onclick="join(${trainCourse.id})">
	             			<svg class="icon" aria-hidden="true" title="报名" data-toggle="tooltip">
	             				<use xlink:href="#icon-baoming"></use>
	             			</svg>
	             			</a>
	             			</c:if>
	             			<c:if test="${trainCourse.cancelReason==null &&  trainCourse.status!='完结'}">
	             			<auth:hasPermission name="trainManagement">
	             			<a href="javacript:void(0)" onclick="arrangeClasshours(${trainCourse.id})">
	             			<svg class="icon" aria-hidden="true" title="课时安排" data-toggle="tooltip">
	             				<use xlink:href="#icon-keshi"></use>
	             			</svg>
	             			</a>
	             			</auth:hasPermission>
	             			</c:if>
	             			<c:if test="${trainCourse.cancelReason==null && trainCourse.status!='培训中'
	             			 	  && trainCourse.status!='报名进行中' && trainCourse.status!='完结'}">
	             			<auth:hasPermission name="trainManagement">
	             			<a href="javacript:void(0)" onclick="deleteTrainCourse(${trainCourse.id})" class="delete">
	             			<svg class="icon" aria-hidden="true" title="课程取消" data-toggle="tooltip">
	             				<use xlink:href="#icon-delete"></use>
	             			</svg>
	             			</a>
	             			</auth:hasPermission>
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
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd HH:mm:00', minDate:'替换区域'})"/>
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
							<input type="hidden" name="coursePlan._id">
						</tr>
	</script>
	<script type="text/html" id="addClasshourHtmlForFirst">
						<tr>
							<td>
								<input type="text" autocomplete="off" class="form-control" name="coursePlan._beginTime" required
	    							onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd HH:mm:00', minDate:'替换区域'})"/>
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
							<input type="hidden" name="coursePlan._id">
						</tr>
	</script>
  </body>
</html>
