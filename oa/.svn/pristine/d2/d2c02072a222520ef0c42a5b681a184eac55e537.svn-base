<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function(){
	$("[data-toggle='tooltip']").tooltip();
});

</script>
<style type="text/css">
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
          <h3 class="sub-header" style="margin-top:0px;">学员评分列表<button style="float:right" class="btn btn-default" onclick="history.go(-1)">返回</button></h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
          		<tr>
					<td style="width:20%">序号</td>
					<td style="width:20%">课时开始时间</td>
					<td style="width:20%">评价人</td>
					<td style="width:20%">评分</td>
					<td style="width:20%">操作</td>
				</tr>
              </thead>
              <tbody>
              	<c:forEach items="${scoreList}" var="score" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${score[2]}</td>
              			<td>${score[0]==null?'匿名':score[0]}</td>
              			<td>${score[1]}</td>
              			<td>
              				<a onclick="goPath('train/showCommentDetail?scoreId=${score[3]}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="查看学员课程调查表" data-toggle="tooltip">
	             				<use xlink:href="#icon-Detailedinquiry"></use>
	             			</svg>
	             			</a>
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
