<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
function addOtherHarvest(){
	$("#otherHarvest").after($("#otherHarvestHtml").html());
}
function delOtherHarvest(obj){
	$(obj).parent().parent().prev().remove();
	$(obj).parent().parent().remove();
}
function checkInfo(){
	if($("input[name='studentCommentVo.harvest']:checked").length<1){
		layer.alert("第5题还未选择", {offset:'100px'});
		return false;
	}
}
function calculateScores(){
	var totalScore = 0;
	var answerScoreMap = {'A':10,'B':8,'C':5,'D':1};
	$("input[type='radio']:checked").each(function(){
		totalScore += answerScoreMap[$(this).val()];
	});
	$("#totalScores").text(totalScore);
	$("input[name='studentCommentVo.totalScores']").val(totalScore);
}
</script>
<style type="text/css">
	.glyphicon-trash:hover{color:red !important}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
      	<s:set name="selectedPanel" value="'findTaskList'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 style="margin-top:0px;text-align:center">培训学员课程调查表</h3>
          <form action="train/saveComment" method="post" onsubmit="return checkInfo()">
          <input type="hidden" name="taskId" value="${taskId}">
          <input type="hidden" name="coursePlanId" value="${coursePlanId}">
          <input type="hidden" name="studentCommentVo.totalScores">
          <div style="width:90%;text-align:center;margin:0 auto">
          <table style="width:100%">
          	<tr>
          		<td style="text-align:right;width:15%">培训课程名称：</td>
          		<td style="border-bottom:1px solid #00000096;text-align:center;width:20%">${trainCourse.courseName}</td>
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
          		${staffVo.lastName} <input class="checkboxClass" style="margin-bottom:-1%"name="studentCommentVo.hideName" type="checkbox" value="true"/>匿名
          		</td>
          	</tr>
          </table>
          </div>
          <div style="width:90%;text-align:right;margin:2% 0 1% 0;font-size:16px">合计总分：<span id="totalScores" style="width:10%;border-bottom:1px solid #00000096;display:inline-block;text-align:center"></span></div>
          <div style="width:90%;border:1px solid #00000047;margin:0 auto">
          	<table style="width:100%">
          		<tr>
          			<td style="width:22%">（1） 培训课件准备?</td>
          			<td style="width:20%"><input onchange="calculateScores()" type="radio" value="A" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> A. 非常认真</td>
          			<td style="width:20%"><input onchange="calculateScores()"type="radio" value="B" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> B. 比较认真</td>
          			<td style="width:20%"><input onchange="calculateScores()"type="radio" value="C" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> C. 一般</td>
          			<td><input onchange="calculateScores()" type="radio" value="D" required style="margin-bottom:-1.5%" name="studentCommentVo.trainReady" class="radioClass"> D. 差</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td>（2） 教学方式如何?</td>
          			<td><input onchange="calculateScores()" type="radio" value="A" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> A. 优</td>
          			<td><input onchange="calculateScores()" type="radio" value="B" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> B. 好</td>
          			<td><input onchange="calculateScores()" type="radio" value="C" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> C. 一般</td>
          			<td><input onchange="calculateScores()" type="radio" value="D" required style="margin-bottom:-1.5%" name="studentCommentVo.teaMode" class="radioClass"> D. 差</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td>&nbsp;&nbsp;其它您认为更好的形式：</td>
          			<td colspan="4">
          				<input autoComplete="off" class="form-control" style="width:95%" name="studentCommentVo.otherGoodMode">
          			</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td>（3）培训时间是否适当?</td>
          			<td><input onchange="calculateScores()" type="radio" value="A" required style="margin-bottom:-1.5%" name="studentCommentVo.trainTime" class="radioClass"> A. 适合</td>
          			<td><input onchange="calculateScores()" type="radio" value="B" required style="margin-bottom:-1.5%" name="studentCommentVo.trainTime" class="radioClass"> B. 能接受</td>
          			<td><input onchange="calculateScores()" type="radio" value="C" required style="margin-bottom:-1.5%" name="studentCommentVo.trainTime" class="radioClass"> C. 略长或略短</td>
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
          			<td><input onchange="calculateScores()" type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaSpeed" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;"><span style="margin-right:20%">清楚</span></td>
          			<td><input onchange="calculateScores()" type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaClear" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;"><span style="margin-right:20%">有条理</span></td>
          			<td><input onchange="calculateScores()" type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.teaOrganized" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;">态度：&nbsp;&nbsp;&nbsp;<span style="margin-right:20%">友善</span></td>
          			<td><input onchange="calculateScores()" type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.friend" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td style="text-align:right;"><span style="margin-right:20%">鼓励参与</span></td>
          			<td><input onchange="calculateScores()" type="radio" value="A"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="B"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="C"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          			<td><input onchange="calculateScores()" type="radio" value="D"  required style="margin-bottom:-1.5%" name="studentCommentVo.join" class="radioClass"></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="5">（5） 参加此次培训感到有什么收获？（可多选）</td>
          		</tr>
          		<tr>
          			<td colspan="3"><input type="checkbox"  style="margin-bottom:-0.2%;margin-left:7%" class="checkboxClass" value="A" name="studentCommentVo.harvest"> A. 获得了适用的新知识</td>
          			<td colspan="2"><input type="checkbox"  style="margin-bottom:-0.5%" class="checkboxClass" value="B" name="studentCommentVo.harvest"> B. 获得了新的管理观念</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td colspan="3"><input type="checkbox"  style="margin-bottom:-0.2%;margin-left:7%" class="checkboxClass" value="C" name="studentCommentVo.harvest"> C. 顺理了过去工作中的一些模糊概念</td>
          			<td colspan="2"><input type="checkbox"  style="margin-bottom:-0.5%" class="checkboxClass" value="D" name="studentCommentVo.harvest"> D. 可以使用在工作上的一些有效的技巧或技术</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td colspan="5"><input type="checkbox"  style="margin-bottom:-0.2%;margin-left:4.38%" class="checkboxClass" value="E" name="studentCommentVo.harvest"> E. 客观地观察我自己以及我的工作，帮助对过去自己的工作进行总结和思考</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr id="otherHarvest">
          			<td style="text-align:center">其它（请填写）：</td>
          			<td colspan="3">
          				<input autoComplete="off" name="studentCommentVo.otherHarvest" class="form-control">
          			</td>
          			<td><label class="glyphicon glyphicon-plus hand" style="margin-left:5%;color:#428BCA" onclick="addOtherHarvest()"></label></td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="2">（6） 对本人工作上的帮助程度：&nbsp;&nbsp;&nbsp;<input onchange="calculateScores()" type="radio" value="A"  required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> A. 非常有效</td>
          			<td colspan="3">
	          			<span style="margin-left:5%"><input onchange="calculateScores()" type="radio" value="B"  required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> B. 有效</span>
	          			<span style="margin-left:10%"><input onchange="calculateScores()" type="radio" value="C" required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> C. 普通</span>
	          			<span style="margin-left:10%"><input onchange="calculateScores()" type="radio" value="D" required style="margin-bottom:-0.5%" name="studentCommentVo.helpInWork" class="radioClass"> D. 较小</span>
          			</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="2">（7） 整体上，您对这次课程的满意程度是：</td>
          			<td colspan="3">
          				<span style="margin-left:-10%"><input onchange="calculateScores()" type="radio" value="A" required  style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> A. 非常满意</span>
	          			<span style="margin-left:10%"><input onchange="calculateScores()" type="radio" value="B" required style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> B. 满意</span>
	          			<span style="margin-left:10%"><input onchange="calculateScores()" type="radio" value="C" required style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> C. 一般</span>
	          			<span style="margin-left:10%"><input onchange="calculateScores()" type="radio" value="D" required style="margin-bottom:-0.5%" name="studentCommentVo.satisfaction" class="radioClass"> D. 不满</span>
          			</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="5">（8） 如对本次课程安排不满意请在此填写原因和相关建议：</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td colspan="5">
          				<input autoComplete="off" style="width:90%;margin-left:5%" class="form-control" name="studentCommentVo.advice">
          			</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:10px"></td>
          		</tr>
          		<tr>
          			<td colspan="4"></td>
          			<td style="text-align:center"><button type="submit" class="btn btn-primary">提交</button></td>
          		</tr>
          		<tr>
          			<td style="font-size:12px" colspan="5">&nbsp;&nbsp;备注：选择题<span style="color:red">必填</span>，其他选填，表中A代表10分，B代表8分，C代表5分，D代表1分，第（5）项不参与计分</td>
          		</tr>
          		<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          	</table>
          </div>
          </form>
        </div>
      </div>
    </div>
   <script type="text/html" id="otherHarvestHtml">
	          	<tr>
          			<td colspan="5" style="height:5px"></td>
          		</tr>
          		<tr>
          			<td ></td>
          			<td colspan="3">
          				<input autoComplete="off" name="studentCommentVo.otherHarvest" class="form-control">
          			</td>
          			<td><label class="glyphicon glyphicon-trash hand" style="margin-left:5%;color:#428BCA" onclick="delOtherHarvest(this)"></label></td>
          		</tr>
  </script>
  </body>
</html>