<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){
		$("[data-toggle='tooltip']").tooltip();
	});
	var showPic = function (name, path){
   		var picData={
	       		start:0,
	       		data:[]
   	    }
   		picData.data.push({alt:name, src:"train/showImage?attachmentPath="+path})
   		layer.photos({
   			offset: '50px',
   		    photos: picData
   		    ,anim: 5 
   		  });
   	}
	function showDisplay(obj){
		var classHoursObj = $(obj).next();
		if(classHoursObj.css("display")=="none"){
			classHoursObj.css("display","block");
		}else{
			classHoursObj.css("display","none");
		}
	}
	function showVacationDetail(reason, status, comment){
		var content = "请假原因："+reason+"<br>审批状态："+status;
		if(comment){
			content += "<br>审批意见："+comment;
		}
		layer.alert(content, {offset:'100px'});
	}
</script>
<style type="text/css">
	a:hover,a:focus,a:visited{text-decoration:none}
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
	.tab table tr .black {background:#efefef;text-align:center;color:#000;width:15%}
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
          <h3 class="sub-header" style="margin-top:0px;">课程详情<button style="float:right" onclick="location.href='javascript:history.go(-1);'" class="btn btn-default">返回</button></h3>
		 		<div class="tab">
				<table>
					<tr>
						<td class="black">课程名称</td>
						<td colspan="3">${trainCourse.courseName}</td>
						<td class="black">培训类别</td>
						<td>${trainCourse.trainClass}</td>
					</tr>
					<tr>
						<td class="black">课时</td>
						<td>${trainCourse.classHour}</td>
						<td class="black">讲师</td>
						<td>${trainCourse.lecturerNames}</td>
						<td class="black">已报名人数</td>
						<td>${trainCourse.joinerNum}</td>
					</tr>
					<tr>
						<td class="black">公开报名</td>
						<td>${trainCourse.publicReport}</td>
						<td class="black">报名截止时间</td>
						<td>
						<c:if test="${trainCourse.publicReport=='否'}">——</c:if>
						<c:if test="${trainCourse.publicReport=='是'}">${trainCourse.deadline}</c:if>
						</td>
						<td class="black">截止人数</td>
						<td>
						<c:if test="${trainCourse.publicReport=='否'}">——</c:if>
						<c:if test="${trainCourse.publicReport=='是'}">${trainCourse.maxPersonNum}</c:if>
						</td>
					</tr>
					<tr>	
						<td class="black">参与人员</td>
						<td>
							<c:if test="${trainCourse.joinUsers=='' || trainCourse.joinUsers==null}">——</c:if>
							<c:if test="${trainCourse.joinUsers!='' && trainCourse.joinUsers!=null}">${trainCourse.joinUsers}</c:if>
						</td>
						<td class="black">参与部门</td>
						<td colspan="3">
							<c:if test="${empty(trainCourse.companyAndDepNames)}">——</c:if>
							<c:if test="${!empty(trainCourse.companyAndDepNames)}">
								<c:forEach items="${trainCourse.companyAndDepNames}" var="companyAndDepName">
									${companyAndDepName}<br>
								</c:forEach>
							</c:if>
						</td>
					</tr>
					<tr>
						<td class="black">课程纲要</td>
						<td colspan="5" style="text-align:left">${trainCourse.description}</td>
					</tr>
					<tr>
						<td class="black">附件</td>
						<td colspan="5" style="text-align:left">
						<c:forEach items="${trainCourse.attaList}" var="atta">
							<c:if test="${atta.suffix=='png'}">
									<a href="javascript:showPic('${atta.softName}','${atta.softURL}')" style="text-decoration:none">
			  						<img src="train/showImage?attachmentPath=${atta.softURL}" style="width:100px;height:100px">&nbsp;&nbsp;
									</a>
							</c:if>
							<c:if test="${atta.suffix!='png'}">
								<a href="train/downloadAttachment?attachmentPath=${atta.softURL}&attachmentName=${atta.softName}">${atta.softName}</a>&nbsp;&nbsp;
							</c:if>
						</c:forEach>
						</td>
					</tr>
				</table>
				</div>
			<a style="display:inline-block;margin-top:10px;font-size:15px" href="javacript:void(0)" onclick="showDisplay(this)">课时详情>></a>
			<div class="tab">
			<table>
				<tbody>
					<tr>
						<td class="black">课时序号</td>
						<td class="black">开始时间</td>
						<td class="black">时长（h）</td>
						<td class="black">地点</td>
						<td class="black">讲师</td>
						<c:if test="${myCourse}">
							<td style="width:20%" class="black">请假</td>
						</c:if>
						<c:if test="${!myCourse}">
							<td style="width:20%" class="black">完结</td>
						</c:if>
					</tr>
					<c:forEach items="${coursePlans}" var="coursePlan" varStatus="status">
						<tr>
							<c:if test="${!myCourse}">
							<td>第${status.index+1}课时</td>
							<td>${coursePlan[2]}</td>
							<td>${coursePlan[3]}</td>
							<td>${coursePlan[1]}</td>
							<td>${coursePlan[0]}</td>
							<td>${coursePlan[4]=='1'?'是':'否'}</td>
							</c:if>
							<c:if test="${myCourse}">
							<td>第${status.index+1}课时</td>
							<td>${coursePlan.beginTime}</td>
							<td>${coursePlan.trainHours}</td>
							<td>${coursePlan.place}</td>
							<td>${coursePlan.lecturer}</td>
							<td>
								<c:if test="${coursePlan.reason!='' && coursePlan.reason!=null}">
								<c:if test="${coursePlan.auditStatus=='未通过'}">
									<span style="margin-left:17%;color:red">未通过</span>
								</c:if>
								<c:if test="${coursePlan.auditStatus!='未通过'}">
									<span style="margin-left:17%">是</span>
								</c:if>
								&nbsp;
								<a href="javascript:void(0)" onclick="showVacationDetail('${coursePlan.reason}','${coursePlan.auditStatus}','${coursePlan.comment.content}')">
								<svg class="icon" aria-hidden="true" title="查看请假详细" data-toggle="tooltip">
	             					<use xlink:href="#icon-Detailedinquiry"></use>
	             				</svg>
								</a>
								</c:if>
								<c:if test="${coursePlan.reason=='' || coursePlan.reason==null}">否</c:if>
							</td>
							</c:if>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</div>
        </div>
      </div>
    </div>
  </body>
</html>
