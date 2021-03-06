<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html> 
<html lang="zh-CN">

<head>
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
        <s:set name="panel" value="'position'"></s:set>
        <%@include file="/pages/personal/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" class="form-horizontal" action="/personal/underlingVacationDetail" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">下属请假列表</h3> 
			   <div class="form-group" >
			  	<label  class="col-sm-1 control-label">日期</label>
			  	<div class="col-sm-2">
			    	<input name="vacationDate"  class="form-control"  onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" value="${vacationDate}"/>
			    </div>
			    <div class="col-sm-2">
			    	<button type="submit" class="btn btn-primary">查看</button>
			    </div>
			  </div>
          </form>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">姓名</th>
                  <th class="col-sm-1">开始时间</th>
                  <th class="col-sm-1">结束时间</th>
                  <th class="col-sm-1">工作代理人</th>
                  <th class="col-sm-1">请假原因</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty underlingVacationVos}">
              	<c:forEach items="${underlingVacationVos}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+1}</td>
              		<td>${item.vacationUserName}</td>
            		<td>${item.beginDate}</td>
            		<td>${item.endDate}</td>
            		<td>${item.workAgentName}</td>
            		<td>${item.reason}</td>
              		</tr>
              	</c:forEach>
              	</c:if>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
