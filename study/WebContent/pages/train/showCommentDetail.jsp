<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function(){
		var harvest = '${courseScore.trainStudentCommentVo.harvest}';
		$("input[name='studentCommentVo.harvest']").each(function(){
			var optionVal = $(this).val();
			if(harvest.indexOf(optionVal)!=-1){
				$(this).attr("checked","checked");
			}
		});
		
	});
</script>
<style type="text/css">
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
      	<s:set name="selectedPanel" value="'lecturerScoreList'"></s:set>
        <%@include file="/pages/train/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 style="margin-top:0px;text-align:center">培训学员课程调查表<button class="btn btn-default" style="float:right" onclick="history.go(-1)">返回</button></h3>
          <div style="width:90%;text-align:center;margin:0 auto">
          <table style="width:100%">
          	<tr>
          		<td style="text-align:right;width:15%">培训课程名称：</td>
          		<td style="border-bottom:1px solid #00000096;text-align:center;width:20%">${courseName}</td>
          		<td style="width:35%"></td>
          		<td style="text-align:right;width:10%">日期：</td>
          		<td style="border-bottom:1px solid #00000096;text-align:center;width:20%">${beginTime}</td>
          	</tr>
          	<tr>
          		<td colspan="5" style="height:10px"></td>
          	</tr>
          	<tr>
          		<td style="text-align:right">所在部门：</td>
          		<td style="border-bottom:1px solid #00000096;text-align:center">${staffVo.departmentName}</td>
          		<td></td>
          		<td style="text-align:right">姓名：</td>
          		<td style="border-bottom:1px solid #00000096;text-align:center">
          		<c:if test="${courseScore.trainStudentCommentVo.hideName=='true'}">
          			匿名
          		</c:if>
          		<c:if test="${courseScore.trainStudentCommentVo.hideName!='true'}">
          			${staffVo.lastName} 
          		</c:if>
          		</td>
          	</tr>
          </table>
          </div>
          <div style="width:90%;text-align:right;margin:2% 0 1% 0;font-size:16px">合计总分：<span id="totalScores" style="width:10%;border-bottom:1px solid #00000096;display:inline-block;text-align:center">${courseScore.trainStudentCommentVo.totalScores}</span></div>
          <div style="width:90%;border:1px solid #00000047;margin:0 auto">
          	<table style="width:100%">
          		<tr>
          			<td style="width:22%">（1） 培训课件准备?</td>
          			<td style="width:20%"><input onclick="return false" type="radio" ${courseScore.trainStudentCommentVo.trainReady=='A'?'checked':''} value="A" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> A. 非常认真</td>
          			<td style="width:20%"><input onclick="return false" type="radio" ${courseScore.trainStudentCommentVo.trainReady=='B'?'checked':''} value="B" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> B. 比较认真</td>
          			<td style="width:20%"><input onclick="return false" type="radio" ${courseScore.trainStudentCommentVo.trainReady=='C'?'checked':''} value="C" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> C. 一般</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.trainReady=='D'?'checked':''} type="radio" value="D" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> D. 差</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td>（2） 教学方式如何?</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaMode=='A'?'checked':''} type="radio" value="A" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> A. 优</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaMode=='B'?'checked':''} type="radio" value="B" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> B. 好</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaMode=='C'?'checked':''} type="radio" value="C" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> C. 一般</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaMode=='D'?'checked':''} type="radio" value="D" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> D. 差</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td>&nbsp;&nbsp;其它您认为更好的形式：</td>
          			<td colspan="4">
          				<c:if test="${not empty courseScore.trainStudentCommentVo.otherGoodMode}">
          					<div style="width:80%;border-bottom:solid 1px #00000096">${courseScore.trainStudentCommentVo.otherGoodMode}</div>
          				</c:if>
          				<c:if test="${empty courseScore.trainStudentCommentVo.otherGoodMode}">
          					无
          				</c:if>
          			</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td>（3）培训时间是否适当?</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.trainTime=='A'?'checked':''} type="radio" value="A" required style="margin-bottom:-1.5%" name="studentCommentVo.trainTime" class="radioClass"> A. 适合</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.trainTime=='B'?'checked':''} type="radio" value="B" required style="margin-bottom:-1.5%" name="studentCommentVo.trainTime" class="radioClass"> B. 能接受</td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.trainTime=='C'?'checked':''} type="radio" value="C" required style="margin-bottom:-1.5%" name="studentCommentVo.trainTime" class="radioClass"> C. 略长或略短</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td>（4）培训师教授技巧如何?</td>
          			<td><span style="margin-left:13%">A. 非常满意</span></td>
          			<td><span style="margin-left:13%">B. 满意</span></td>
          			<td><span style="margin-left:13%">C. 普通</span></td>
          			<td><span style="margin-left:13%">D. 不满</span></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;">表达能力：&nbsp;&nbsp;&nbsp;<span style="margin-right:20%">速度</span></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaSpeed=='A'?'checked':''} type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaSpeed=='B'?'checked':''} type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaSpeed=='C'?'checked':''} type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaSpeed=='D'?'checked':''} type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;"><span style="margin-right:20%">清楚</span></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaClear=='A'?'checked':''} type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaClear=='B'?'checked':''} type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaClear=='C'?'checked':''} type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaClear=='D'?'checked':''} type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;"><span style="margin-right:20%">有条理</span></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaOrganized=='A'?'checked':''} type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaOrganized=='B'?'checked':''} type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaOrganized=='C'?'checked':''} type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.teaOrganized=='D'?'checked':''} type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;">态度：&nbsp;&nbsp;&nbsp;<span style="margin-right:20%">友善</span></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.friend=='A'?'checked':''} type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.friend=='B'?'checked':''} type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.friend=='C'?'checked':''} type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.friend=='D'?'checked':''} type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;"><span style="margin-right:20%">鼓励参与</span></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.join=='A'?'checked':''} type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.join=='B'?'checked':''} type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.join=='C'?'checked':''} type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          			<td><input onclick="return false" ${courseScore.trainStudentCommentVo.join=='D'?'checked':''} type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="5">（5） 参加此次培训感到有什么收获？（可多选）</td>
          		</tr>
          		<tr>
          			<td colspan="3"><input type="checkbox" onclick="return false" style="margin-bottom:-0.2%;margin-left:7%" class="checkboxClass" value="A" name="studentCommentVo.harvest"> A. 获得了适用的新知识</td>
          			<td colspan="2"><input type="checkbox" onclick="return false" style="margin-bottom:-0.5%" class="checkboxClass" value="B" name="studentCommentVo.harvest"> B. 获得了新的管理观念</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td colspan="3"><input type="checkbox" onclick="return false" style="margin-bottom:-0.2%;margin-left:7%" class="checkboxClass" value="C" name="studentCommentVo.harvest"> C. 顺理了过去工作中的一些模糊概念</td>
          			<td colspan="2"><input type="checkbox" onclick="return false" style="margin-bottom:-0.5%" class="checkboxClass" value="D" name="studentCommentVo.harvest"> D. 可以使用在工作上的一些有效的技巧或技术</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td colspan="5"><input type="checkbox" onclick="return false" style="margin-bottom:-0.2%;margin-left:4.38%" class="checkboxClass" value="E" name="studentCommentVo.harvest"> E. 客观地观察我自己以及我的工作，帮助对过去自己的工作进行总结和思考</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr id="otherHarvest">
          			<td style="text-align:center">其它（请填写）：</td>
          			<td colspan="4">
          				<c:if test="${fn:length(courseScore.trainStudentCommentVo.otherHarvest)>0}">
          					<div style="width:80%;border-bottom:solid 1px #00000096">1）${courseScore.trainStudentCommentVo.otherHarvest[0]}</div>
          				</c:if>
          				<c:if test="${fn:length(courseScore.trainStudentCommentVo.otherHarvest)<1}">
          					无
          				</c:if>
          			</td>
          		</tr>
          		<c:if test="${fn:length(courseScore.trainStudentCommentVo.otherHarvest)>1}">
          			<c:forEach items="${courseScore.trainStudentCommentVo.otherHarvest}" begin="1" var="otherHarvest" varStatus="status">
		          		<tr>
		          			<td colspan="5" style="height:5px"></td>
		          		</tr>
		          		<tr>
		          			<td ></td>
		          			<td colspan="4">
		          				<div style="width:80%;border-bottom:solid 1px #00000096">${status.index+1}）${otherHarvest}</div>
		          			</td>
		          		</tr>
          			</c:forEach>
          		</c:if>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="2">（6） 对本人工作上的帮助程度：&nbsp;&nbsp;&nbsp;<input onclick="return false" ${courseScore.trainStudentCommentVo.helpInWork=='A'?'checked':''} type="radio" value="A"  required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> A. 非常有效</td>
          			<td colspan="3">
	          			<span style="margin-left:5%"><input onclick="return false" ${courseScore.trainStudentCommentVo.helpInWork=='B'?'checked':''} type="radio" value="B"  required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> B. 有效</span>
	          			<span style="margin-left:10%"><input onclick="return false" ${courseScore.trainStudentCommentVo.helpInWork=='C'?'checked':''} type="radio" value="C" required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> C. 普通</span>
	          			<span style="margin-left:10%"><input onclick="return false" ${courseScore.trainStudentCommentVo.helpInWork=='D'?'checked':''} type="radio" value="D" required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> D. 较小</span>
          			</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="2">（7） 整体上，您对这次课程的满意程度是：</td>
          			<td colspan="3">
          				<span style="margin-left:-10%"><input onclick="return false" ${courseScore.trainStudentCommentVo.satisfaction=='A'?'checked':''} type="radio" value="A" required  style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> A. 非常满意</span>
	          			<span style="margin-left:10%"><input onclick="return false" ${courseScore.trainStudentCommentVo.satisfaction=='B'?'checked':''} type="radio" value="B" required style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> B. 满意</span>
	          			<span style="margin-left:10%"><input onclick="return false" ${courseScore.trainStudentCommentVo.satisfaction=='C'?'checked':''} type="radio" value="C" required style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> C. 一般</span>
	          			<span style="margin-left:10%"><input onclick="return false" ${courseScore.trainStudentCommentVo.satisfaction=='D'?'checked':''} type="radio" value="D" required style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> D. 不满</span>
          			</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<c:if test="${not empty courseScore.trainStudentCommentVo.advice}">
          		<tr>
          			<td colspan="5">（8） 如对本次课程安排不满意请在此填写原因和相关建议：</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td colspan="5">
          				<div style="width:95%;border-bottom:solid 1px #00000096;margin-left:2%">${courseScore.trainStudentCommentVo.advice}</div>
          			</td>
          		</tr>
          		</c:if>
          		<c:if test="${empty courseScore.trainStudentCommentVo.advice}">
          		<tr>
          			<td colspan="5">（8） 如对本次课程安排不满意请在此填写原因和相关建议：&nbsp;无</td>
          		</tr>
          		</c:if>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td style="font-size:12px" colspan="5">&nbsp;&nbsp;备注：选择题<span style="color:red">必填</span>，其他选填，表中A代表10分，B代表8分，C代表5分，D代表1分，第（5）项不参与计分</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          	</table>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>