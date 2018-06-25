<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	 $(function() {
		set_href();
	}); 
	function search() {
		window.location.href = "train/findTrainList?" + $("#trainForm").serialize();
	}

</script>
<style type="text/css">
	
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'process'"></s:set>
        <%@include file="/pages/train/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <form id="trainForm" class="form-horizontal">
       	  <h3 class="sub-header" style="margin-top:0px;">培训列表</h3>
       	  <div class="form-group">
				<label for="name" class="col-sm-1 control-label">讲师</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="name" name="trainVO.lector" value="${trainVO.lector}">
			    </div>
				<label for="name" class="col-sm-1 control-label">主题</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="name" name="trainVO.topic" value="${trainVO.topic}">
			    </div>

			</div>
       	  <div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="trainVO.startTime" value="${trainVO.startTime }" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onBlur="calcTime()" placeholder="开始时间" />
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="trainVO.endTime" value="${trainVO.endTime }"required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" onBlur="calcTime()" placeholder="结束时间"/>
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="button" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>
       	  	<div class="form-group">
       	  		<a class="btn btn-primary" href="pages/train/addtrain.jsp">新增</a>
       	  	</div>
       	  	<div class="table-responsive">
       	  		<table class="table table-striped">
       	  			<thead>
       	  				<tr>
       	  					<th>序号</th>
       	  					<th>开始时间</th>
       	  					<th>结束时间</th>
       	  					<th>地点</th>
       	  					<th>讲师</th>
       	  					<th>主题</th>
       	  					<th>操作</th>       	  					
       	  				</tr>
       	  				
       	  			</thead>
       	  			<tbody>
       	  				<c:if test="${not empty trainList}">
              			<c:set var="train_id" value="${(page-1)*limit }"/>
              			<c:forEach items="${trainList}" var="trainVO" varStatus="count">
              				<tr>
              					<td class="col-sm-1">${train_id+1 }</td>
              					<td class="col-sm-1">${trainVO.startTime }</td>
              					<td class="col-sm-1">${trainVO.endTime }</td>
              					<td class="col-sm-1">${trainVO.place}</td>
              					<td class="col-sm-1">${trainVO.lector}</td>
              					<td class="col-sm-2">${trainVO.topic}</td>
              					<td class="col-sm-2"><a href="train/findTrainDetail?trainID=${trainVO.trainID }">查看</a>|<a href="train/findTrain?trainID=${trainVO.trainID }">修改</a>|<a href="train/followTrain?trainID=${trainVO.trainID }">跟进</a>|<a href="train/deleteTrain?trainID=${trainVO.trainID }">删除</a></td>
              				</tr>
              				
              			<c:set var="train_id" value="${train_id+1}"/>
              			</c:forEach>
              			</c:if>
              			
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
