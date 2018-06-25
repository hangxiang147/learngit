<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'dangan'"></s:set>
      	<s:set name="selectedPanel" value="'getLastName'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>		
         <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" action="/HR/staff/getLastName" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">花名查询</h3> 
          	<div class="form-group"  >
				<label for="name" class="col-sm-1 control-label" style="width:10%">员工姓名<span style="color:red"> *</span></label>
			    <div class="col-sm-2">
			    	<input name="userName"  class="form-control" required="required" value="${userName }" autocomplete="off"/>
			    </div>
	
			    <div class="col-sm-2">
					<button type="submit"  class="btn btn-primary">查询</button>
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
                  <th class="col-sm-1">花名</th>
                  <th class="col-sm-1">工号</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty result}">
              	<c:forEach items="${result}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
              		<td>${userName }</td>
            		<td>${item[1] }</td>
              		<td>${item[0] }</td>
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
