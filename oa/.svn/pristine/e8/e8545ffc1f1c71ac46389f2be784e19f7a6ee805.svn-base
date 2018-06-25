<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		set_href();
	});

	function search() {
		window.location.href = "personal/attendanceDetail?" + $("#attendanceStatisticsForm").serialize();
	}
	
	
</script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	
        <s:set name="panel" value="'application'"></s:set>
        <s:set name="selectedPanel" value="'myProcessList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form id="attendanceStatisticsForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">招聘明细</h3>
          </form>

          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>开始应聘</th>
                  <th>填写表格</th>
                  <th>填写结果</th>
                  <th>确认入职</th>
                  <th>结果</th>
                </tr>
              </thead>
              <tbody>
             	<c:forEach items="${result2}" var="item" varStatus="index">
             		<tr>
             		<td>${index.index }</td>
             		<td>${item.xm }</td>
             		<td>${item.step1Name }&nbsp;<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${item.step1Time}" /></td>
             		<td>${item.xm }&nbsp;<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${item.step2Time}" /></td>
             		<td>${item.step3Name }&nbsp;<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${item.step3Time}" /></td>
             		<td>${item.step4Name }&nbsp;<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${item.step4Time}" /></td>
             		<td>
             			<c:if test="${item.type eq 0}">没有来应聘</c:if>
             			<c:if test="${item.type eq 1}">前来应聘 </c:if>
             			<c:if test="${item.type eq 2}">应聘通过 </c:if>
             			<c:if test="${item.type eq 3}">应聘不通过</c:if>
             			<c:if test="${item.type eq 4}">未入职</c:if>
             			<c:if test="${item.type eq 5}">已入职</c:if>
             			<c:if test="${item.type eq 6}">进入下轮复试</c:if>
             		</td>
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
