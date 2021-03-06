<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
	$("#submitBtn").click(function(){
		if(checkEmpty()){
			return false;
		}
		layer.confirm("确认交卷？",{offset:'100px'},function(index){
			layer.close(index);
			$("#form").submit();
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	});
});
function countdown(){
	var timer = $("#timer");
	prepareProcessTimes(timer);
	//先调用一次，避免误差
	processTimer(timer);
	setInterval(processTimer,1000,timer);
}
//预处理一下时间为倒计时秒数
function prepareProcessTimes(timer){
    var total = parseInt(timer.attr("data-timer"));
    total = total*60;
    timer.attr("data-timer",total);
}
//倒计时
function processTimer(timer){
    var total=parseInt(timer.attr("data-timer"));
    var t=total;
    //倒计时不能为负
    if(total<0) return; //TODO 后续版本加上计时完毕可以回调函数
    
    //找到显示时间的元素
    var day=timer.find(".day");
    var hour=timer.find(".hour");
    var minute=timer.find(".minute");
    var second=timer.find(".second");
    
    //刷新计时器显示的值
    if(day.length){
        var d=Math.floor(t/(60*60*24));
        day.text(d);
        t-=d*(60*60*24);
    }
    if(hour.length){
        var h=Math.floor(t/(60*60));
        hour.text((h<10?"0":"")+h);
        t-=h*(60*60);
    }
    if(minute.length){
        var m=Math.floor(t/60);
        minute.text((m<10?"0":"")+m);
        t-=m*60;
    }
    if(second.length){
        second.text((t<10?"0":"")+t);
    }
    
    //一秒过去了...
    total--;
    //时间到
    if(total==0){
    	$(".answer").each(function(){$(this).css("display","block")});
    	$("#form").submit();
    	Load.Base.LoadingPic.FullScreenShow(null);
    }
    timer.attr("data-timer",total);
}
function startTest(){
	layer.confirm("确定开始？",{offset:'100px'},function(index){
		$("#startTest").css("display","none");
		$("#courseName").css("display","none");
		$("#questionLst").css("display","block");
		countdown();
		layer.close(index);
	})
}
//检查未做的题目
function checkEmpty(){
	var index = 0;
	$("input[name='questionId']").each(function(){
		var questionId = $(this).val();
		var checkedObj = $("input[name='answer"+questionId+"']:checked");
		if(checkedObj.length<1){
			index++;
		}
	});
	if(index>0){
		layer.alert("还有"+index+"条题目未做",{offset:'100px'});
		return true;
	}
}
</script>
<style type="text/css">
	.title{
		width:100%
	}
	.questionList tr td{width:50%}
	.hour,.minute,.second{font-size:20px}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">培训测试</h3>
          <div id="courseName" style="font-size:16px;margin-bottom:10px">课程名：${courseName}</div>
          <div id="startTest" class="btn btn-primary" onclick="startTest()">
          	开始答题
          </div>
          <div id="questionLst" style="display:none">
          	<div style="font-size:16px;text-align:center;font-weight:bold">${courseName}测试题</div>
          	<div id="timer" style="float:right;margin-top:-2.5%;color:red;" data-timer="${singleAnswerQuestions[0].timer==null ? multipleAnswerQuestions[0].timer:singleAnswerQuestions[0].timer}">
          	  <span>倒计时：</span>
              <span class="hour">00</span>时
              <span class="minute">00</span>分
              <span class="second">00</span>秒
          	</div>
          	<form id="form" action="train/save_submitTests" method="post">
          	<s:token></s:token>
          	<c:if test="${!empty singleAnswerQuestions}">
          		<div style="font-weight:bold;">一、单项选择题</div>
          	</c:if>
          	<table style="width:100%;font-size:14px" class="questionList">
          		<c:forEach items="${singleAnswerQuestions}" var="question" varStatus="status">
          			<input type="hidden" name="questionId" value="${question.id}">
          			<c:if test="${question.multipleChoice==null}">
          			<tr>
          				<td colspan="2" class="title">${status.index+1}. ${question.title}（）</td>
          			</tr>
	          		<tr>
	          			<td><input type="radio" value="A" name="answer${question.id}"> A. ${question.choiceA}</td>
	          			<td><input type="radio" value="B" name="answer${question.id}"> B. ${question.choiceB}</td>
	          		</tr>
	          		<c:if test="${question.choiceC!=null && question.choiceC!=''}">
	          		<tr>
	          			<td><input type="radio" value="C" name="answer${question.id}"> C. ${question.choiceC}</td>
	          			<c:if test="${question.choiceD!=null && question.choiceD!=''}">
	          			<td><input type="radio" value="D" name="answer${question.id}"> D. ${question.choiceD}</td>
	          			</c:if>
	          			<c:if test="${question.choiceD==null || question.choiceD==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<c:if test="${question.choiceE!=null && question.choiceE!=''}">
	          		<tr>
	          			<td><input type="radio" value="E" name="answer${question.id}"> E. ${question.choiceE}</td>
	          			<c:if test="${question.choiceF!=null && question.choiceF!=''}">
	          				<td><input type="radio" value="F" name="answer${question.id}"> F. ${question.choiceF}</td>
	          			</c:if>
	          			<c:if test="${question.choiceF==null || question.choiceF==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<tr><td colspan="2" style="color:red;display:none" class="answer">正确答案：${question.answer}</td></tr>
	          		</c:if>
          		</c:forEach>
          	</table>
          	<c:if test="${!empty singleAnswerQuestions && !empty multipleAnswerQuestions}">
          		<div style="font-weight:bold;">二、多项选择题</div>
          	</c:if>
          	<c:if test="${empty singleAnswerQuestions && !empty multipleAnswerQuestions}">
          		<div style="font-weight:bold;">一、多项选择题</div>
          	</c:if>
          	<table style="width:100%;font-size:14px" class="questionList">
          		<c:forEach items="${multipleAnswerQuestions}" var="question" varStatus="status">
          			<input type="hidden" name="questionId" value="${question.id}">
          			<c:if test="${question.multipleChoice==1}">
          			<tr>
          				<td colspan="2" class="title">${status.index+1}. ${question.title}（）</td>
          			</tr>
	          		<tr>
	          			<td><input type="checkbox" value="A" name="answer${question.id}"> A. ${question.choiceA}</td>
	          			<td><input type="checkbox" value="B" name="answer${question.id}"> B. ${question.choiceB}</td>
	          		</tr>
	          		<c:if test="${question.choiceC!=null && question.choiceC!=''}">
	          		<tr>
	          			<td><input type="checkbox" value="C" name="answer${question.id}"> C. ${question.choiceC}</td>
	          			<c:if test="${question.choiceD!=null && question.choiceD!=''}">
	          			<td><input type="checkbox" value="D" name="answer${question.id}"> D. ${question.choiceD}</td>
	          			</c:if>
	          			<c:if test="${question.choiceD==null || question.choiceD==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<c:if test="${question.choiceE!=null && question.choiceE!=''}">
	          		<tr>
	          			<td><input type="checkbox" value="E" name="answer${question.id}"> E. ${question.choiceE}</td>
	          			<c:if test="${question.choiceF!=null  && question.choiceF!=''}">
	          				<td><input type="checkbox" value="F" name="answer${question.id}"> F. ${question.choiceF}</td>
	          			</c:if>
	          			<c:if test="${question.choiceF==null || question.choiceF==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<tr><td colspan="2" style="color:red;display:none" class="answer">正确答案：${question.answer}</td></tr>
	          		</c:if>
          		</c:forEach>
          	</table>
          	<br>
          	<input type="hidden" value="${coursePlanId}" name="coursePlanId">
          	<input type="hidden" value="${taskId}" name="taskId">
          	<input type="hidden" value="${courseName}" name="courseName">
          	<div style="text-align:center"><button id="submitBtn" type="button" class="btn btn-primary">交卷</button></div>
          	<button type="submit" style="display:none"></button>
          	</form>
          	<span style="color:red">注：时间到，自动交卷</span>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
