<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function () {
		set_href();
	});
</script>
<style type="text/css">
	
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h3 class="sub-header" style="margin-top:0px;">邮箱申请列表</h3>
       	  
       	    <div class="table-responsive">
		  		<table class="table table-striped">
	              <thead>
	                <tr>
	                  <th>序号</th>
	                  <th>标题</th>
	                  <th>申请时间</th>
	                  <th>邮箱申请人</th>
	                  <th>邮箱地址</th>
	                  <th>当前处理人</th>
	                  <th>流程状态</th>
	                </tr>
	              </thead>
             		  <tbody>
	              	<c:if test="${not empty emailVOs}">
	              	<s:set name="email_id" value="(#request.page-1)*(#request.limit)"></s:set>
	              	<s:iterator id="emailVO" value="#request.emailVOs" status="count">
	              		<tr>
	              			<td class="col-sm-1"><s:property value="#email_id+1"/></td>
	              			<td class="col-sm-1"><a href="administration/process/processHistory?processInstanceID=<s:property value='#emailVO.processInstanceID'/>"><s:property value="#emailVO.title"/></a></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.requestDate"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.requestUserName"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.address"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.assigneeUserName"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.status"/></td>
	              		</tr>
	              		<s:set name="email_id" value="#email_id+1"></s:set>
	              	</s:iterator>
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
