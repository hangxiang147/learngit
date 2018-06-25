<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function(){
		layer.alert("<div style='text-align:center;color:red;font-size:40px'>${scores}分</div>",{title:'得分',offset:'100px',});
	});
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
          <div>
          	<div style="font-size:16px;text-align:center;font-weight:bold">${courseName}测试题</div>
          	<div style="float:right;margin-top:-2%;margin-right:2%;font-size:15px;">满分：<span style="text-decoration:underline">&nbsp;100&nbsp;</span> 得分：<span style="text-decoration:underline;color:red">&nbsp;${scores}&nbsp;</span></div>
          	<c:if test="${!empty singleAnswerQuestions}">
          		<div style="font-weight:bold;">一、单项选择题</div>
          	</c:if>
          	<table style="width:100%;font-size:14px" class="questionList">
          		<c:forEach items="${singleAnswerQuestions}" var="question" varStatus="status">
          			<input type="hidden" name="questionId" value="${question.id}">
          			<c:if test="${question.multipleChoice==null}">
          			<tr>
          				<td colspan="2" class="title">${status.index+1}. ${question.title}（${question.choice}）</td>
          			</tr>
	          		<tr>
	          			<td> A. ${question.choiceA}</td>
	          			<td> B. ${question.choiceB}</td>
	          		</tr>
	          		<c:if test="${question.choiceC!=null && question.choiceC!=''}">
	          		<tr>
	          			<td> C. ${question.choiceC}</td>
	          			<c:if test="${question.choiceD!=null && question.choiceD!=''}">
	          			<td> D. ${question.choiceD}</td>
	          			</c:if>
	          			<c:if test="${question.choiceD==null || question.choiceD==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<c:if test="${question.choiceE!=null && question.choiceE!=''}">
	          		<tr>
	          			<td> E. ${question.choiceE}</td>
	          			<c:if test="${question.choiceF!=null && question.choiceF!=''}">
	          				<td> F. ${question.choiceF}</td>
	          			</c:if>
	          			<c:if test="${question.choiceF==null || question.choiceF==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<tr><td colspan="2" style="color:red;${question.correct?'display:none':''}" class="answer">正确答案：${question.answer}</td></tr>
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
          				<td colspan="2" class="title">${status.index+1}. ${question.title}（${question.choice}）</td>
          			</tr>
	          		<tr>
	          			<td> A. ${question.choiceA}</td>
	          			<td> B. ${question.choiceB}</td>
	          		</tr>
	          		<c:if test="${question.choiceC!=null && question.choiceC!=''}">
	          		<tr>
	          			<td> C. ${question.choiceC}</td>
	          			<c:if test="${question.choiceD!=null && question.choiceD!=''}">
	          			<td> D. ${question.choiceD}</td>
	          			</c:if>
	          			<c:if test="${question.choiceD==null || question.choiceD==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<c:if test="${question.choiceE!=null && question.choiceE!=''}">
	          		<tr>
	          			<td> E. ${question.choiceE}</td>
	          			<c:if test="${question.choiceF!=null && question.choiceF!=''}">
	          				<td> F. ${question.choiceF}</td>
	          			</c:if>
	          			<c:if test="${question.choiceF==null || question.choiceF==''}">
	          				<td></td>
	          			</c:if>
	          		</tr>
	          		</c:if>
	          		<tr><td colspan="2" style="color:red;${question.correct?'display:none':''}" class="answer">正确答案：${question.answer}</td></tr>
	          		</c:if>
          		</c:forEach>
          	</table>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
