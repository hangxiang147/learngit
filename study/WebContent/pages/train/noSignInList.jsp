<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function(){
		set_href();
	});
</script>
<style type="text/css">
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/train/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <h3 class="sub-header" style="margin-top:0px;">学员得分</h3>
          <form action="train/noSignInList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<div class="form-group">
          		<label class="control-label col-sm-1">课程名称</label>
          		<div class="col-sm-2">
          			<input class="form-control" name="courseName" value="${courseName}">
          		</div>
          		<label class="control-label col-sm-1">学员姓名</label>
          		<div class="col-sm-2">
          			<input class="form-control" name="userName" value="${userName}">
          		</div>
          		<button type="submit" class="btn btn-primary" style="margin-left:5%">查询</button>
          	</div>
          </form>
          <h3 class="sub-header" style="margin-top:0px;"></h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th style="width:15%">序号</th>
                  <th style="width:25%">课程</th>
                  <th style="width:20%">培训类别</th>
                  <th style="width:20%">课时开始时间</th>
                  <th style="width:15%">未签到人员</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${noSignInPersons}" var="noSignInPerson" varStatus="status">
              		<tr>
              		<td>${(page-1)*limit+(status.index+1)}</td>
              		<td>${noSignInPerson[0]}</td>
              		<td>${noSignInPerson[1]}</td>
              		<td>${noSignInPerson[2]}</td>
              		<td>${noSignInPerson[3]}</td>
              	</c:forEach>
              </tbody>
            </table>
          </div>
          <div class="dropdown">
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
