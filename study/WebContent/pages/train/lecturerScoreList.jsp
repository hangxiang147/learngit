<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function(){
	$("[data-toggle='tooltip']").tooltip();
});
/* function showCourseScoreDetail(userId){
	$.ajax({
		url:'train/showCourseScoreDetail',
		data:{'userId':userId},
		success:function(data){
			var html = "";
			$.each(data.courseScoreList, function(index, value){
				html += "<tr><td>"+(index+1)+"</td><td>"+value[1]+"</td><td>"+value[2]+"</td><td>"+value[3]+"</td><td>"+value[0]+"</td></tr>";
			});
			$("#scores").html(html);
			$("#courseScoreDetail").modal('show');
		}
	});
} */
</script>
<style type="text/css">
	.title{
		width:100%
	}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	#tab{
		border-collapse:collapse;
		width:100%;
		margin-top:5px;
	}
	#tab tr td{word-wrap:break-word;font-size:10px;padding:8px 7px;text-align:center;border:1px solid #ddd}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/train/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">讲师评分列表</h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:15%">序号</th>
                  <th style="width:20%">公司</th>
                  <th style="width:20%">部门</th>
                  <th style="width:15%">讲师名字</th>
                  <th style="width:15%">评分</th>
                  <th style="width:15%">操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${lectureScoreList}" var="lectureScore" varStatus="status">
              		<tr>
              		<td>${status.index+1}</td>
              		<td>${lectureScore.companyName}</td>
              		<td>${lectureScore.departmentName}</td>
              		<td>${lectureScore.userName}</td>
              		<td>${lectureScore.score}</td>
              		<td>
             			<a href="javascript:void(0)" onclick="goPath('train/showCourseScoreDetail?userId=${lectureScore.userId}')">
             				<svg class="icon" aria-hidden="true" title="查看课程评分详情" data-toggle="tooltip">
             					<use xlink:href="#icon-Detailedinquiry"></use>
             				</svg>
             			</a>
              		</td>
              	</c:forEach>
              </tbody>
            </table>
          </div>
        </div>
	    <div id="courseScoreDetail" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:550px">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">课程评分</h4>
				</div>
				<div class="modal-body" style="min-height:200px; max-height:500px; overflow:scroll">
					<table id="tab">
						<tbody>
							<tr>
								<td style="width:10%">序号</td>
								<td style="width:30%">课程名</td>
								<td style="width:20%">培训类别</td>
								<td style="width:30%">开始时间</td>
								<td style="width:10%">评分</td>
							</tr>
						</tbody>
						<tbody id="scores"></tbody>
					</table>
				</div>
			</div>
		  </div>
		</div>
      </div>
    </div>
  </body>
</html>
