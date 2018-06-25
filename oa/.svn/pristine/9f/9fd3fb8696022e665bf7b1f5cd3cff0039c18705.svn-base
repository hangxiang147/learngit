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
      <c:if test="${isSelf  ne '1'}">
      	<s:set name="panel" value="'dangan'"></s:set>
      	<s:set name="selectedPanel" value="'getNameByTelephone'"></s:set>
        </c:if>
        <c:if test="${isSelf eq '1' }">
        <s:set name="panel" value="'querys'"></s:set>
      	<s:set name="selectedPanel" value="'telephoneQuery'"></s:set>
        </c:if>
        <%@include file="/pages/HR/panel.jsp" %>		
         <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="form_" action="/HR/staff/getNameByTelephone" method="post" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">     	  
          	<h3 class="sub-header" style="margin-top:0px;">手机尾号查询</h3> 
          	<input style="display:none" name="isSelf" value="${isSelf}"/>
          	<div class="form-group"  >
				<label for="name" class="col-sm-1 control-label" style="width:10%">手机尾号<span style="color:red"> *</span></label>
			    <div class="col-sm-2">
			    	<input name=telephone  type="number" min=0 max=9999 class="form-control" required="required" value="${telephone }" placeholder="输入4位手机尾号" autocomplete="off" />
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
                  <th class="col-sm-1">完整号码</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty result}">
              	<c:forEach items="${result}" var="item" varStatus="index">
              		<tr>
              		<td>${index.index+startIndex+1}</td>
            		<td>${item[0]}</td>
            		<td>${item[1]}</td>
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
