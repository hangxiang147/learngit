<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
</script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;width:16%}
	.tab table tr .black {background:#efefef;text-align:center;color:#000}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">课时完结</h3>
		  <div class="tab">
			<table>
				<tr>
					<td class="black">课程</td>
					<td colspan="5">${courseName}</td>
				</tr>
				<tr>
					<td class="black">地点</td>
					<td>${coursePlan.place}</td>
					<td class="black">开始时间</td>
					<td>${coursePlan.beginTime}</td>
					<td class="black">时长（h）</td>
					<td>${coursePlan.trainHours}</td>
				</tr>
				<tr>
					<td class="black">讲师得分</td>
					<td colspan="5">
						<c:if test="${lecturerScore.score==0.0}">
							没有评分
						</c:if>
						<c:if test="${lecturerScore.score!=0.0}">
							${lecturerScore.userName}：${lecturerScore.score}
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="black">学员得分</td>
					<td colspan="5">
						<c:if test="${empty stuScores}">没有测试</c:if>
						<c:if test="${!empty stuScores}">
							<c:forEach items="${stuScores}" var="stuScore">
								${stuScore.userName}：${stuScore.score};&nbsp;&nbsp;
							</c:forEach>
						</c:if>
					</td>
				</tr>
			</table>
		  </div>
		  <br>
		  <button onclick="goPath('train/completeClassHour?taskId=${taskId}&coursePlanId=${coursePlan.id}')" 
		  class="btn btn-primary" style="margin-left:45%">完结</button>
        </div>
      </div>
    </div>
  </body>
</html>
