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
        <s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h3 class="sub-header" style="margin-top:0px;">社保缴纳记录</h3>
       	    <a onclick="goPath('HR/process/showSocialSecurityDiagram')" href="javascript:void(0)" class="btn btn-primary" style="margin-top:30px;"><span class="glyphicon glyphicon-plus"></span> 缴纳社保</a>
       	    <div class="table-responsive" style="padding-top:10px;">
		  		<table class="table table-striped">
	              <thead>
	                <tr>
	                  <th>序号</th>
	                  <th>标题</th>
	                  <th>个人部分</th>
	                  <th>公司部分</th>
	                  <th>合计</th>
	                  <th>提交人</th>
	                  <th>提交时间</th>
	                  <th>当前处理人</th>
	                  <th>流程状态</th>
	                </tr>
	              </thead>
             		  <tbody>
	              	<c:if test="${not empty socialSecurityProcessVOs}">
	              	<s:set name="ssp_id" value="(#request.page-1)*(#request.limit)"></s:set>
	              	<s:iterator id="socialSecurityProcessVO" value="#request.socialSecurityProcessVOs" status="count">
	              		<tr>
	              			<td class="col-sm-1"><s:property value="#ssp_id+1"/></td>
	              			<td class="col-sm-2"><a href="HR/process/getSocialSecurityHistory?processInstanceID=<s:property value='#socialSecurityProcessVO.processInstanceID'/>">五险一金申报表审核</a></td>
	              			<td class="col-sm-1"><s:property value="#socialSecurityProcessVO.personalCount"/></td>
	              			<td class="col-sm-1"><s:property value="#socialSecurityProcessVO.companyCount"/></td>
	              			<td class="col-sm-1"><s:property value="#socialSecurityProcessVO.totalCount"/></td>
	              			<td class="col-sm-1"><s:property value="#socialSecurityProcessVO.userName"/></td>
	              			<td class="col-sm-2"><s:property value="#socialSecurityProcessVO.requestDate"/></td>
	              			<td class="col-sm-1"><s:property value="#socialSecurityProcessVO.assigneeUserName"/></td>
	              			<td class="col-sm-1"><s:property value="#socialSecurityProcessVO.status"/></td>
	              		</tr>
	              		<s:set name="ssp_id" value="#ssp_id+1"></s:set>
	              	</s:iterator>
	              	</c:if>
	              </tbody>
          		</table>
          	</div>
			    <div class="dropdown" style="margin-top:25px;">
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
