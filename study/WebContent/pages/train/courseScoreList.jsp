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
	set_href();
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
          <h3 class="sub-header" style="margin-top:0px;">讲师课程评分列表<button style="float:right" class="btn btn-default" onclick="history.go(-1)">返回</button></h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
          		<tr>
					<td style="width:10%">序号</td>
					<td style="width:30%">课程名</td>
					<td style="width:20%">培训类别</td>
					<td style="width:20%">开始时间</td>
					<td style="width:10%">评分</td>
					<td style="width:10%">操作</td>
				</tr>
              </thead>
              <tbody>
              	<c:forEach items="${courseScoreList}" var="courseScore" varStatus="status">
              		<tr>
              			<td>${limit*(page-1)+status.index+1}</td>
              			<td>${courseScore[1]}</td>
              			<td>${courseScore[2]}</td>
              			<td>${courseScore[3]}</td>
              			<td>${courseScore[0]}</td>
              			<td>
              				<a onclick="goPath('train/showCouseScoreList?courseId=${courseScore[4]}&lecturer=${courseScore[5]}')" href="javascript:void(0)">
              				<svg class="icon" aria-hidden="true" title="查看学员评分详情" data-toggle="tooltip">
	             				<use xlink:href="#icon-Detailedinquiry"></use>
	             			</svg>
	             			</a>
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
  </body>
</html>
