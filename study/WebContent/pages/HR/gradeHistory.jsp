<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		set_href();
	});
	
</script>
<style type="text/css">

	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	.myTr td ,.myTr th {
		text-align:center;
	}
	
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'infoAlteration'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

          <h3 class="sub-header">职级变动历史记录</h3>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr class="myTr">
                  <th>序号</th>
                  <th>变动前的职级</th>
                  <th>变动后的职级</th>
                  <th>时间</th>
                  <th>操作人</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffInfoAlterationVOList}">
              	<s:set name="staff_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="staffInfoAlterationVO" value="#request.staffInfoAlterationVOList" status="count">
              		<tr class="myTr">
              			<td class="col-sm-1"><s:property value="#staff_id+1"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.gradeNameBefore"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.gradeNameAfter"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.operateTime"/></td>
              			<td class="col-sm-2"><s:property value="#staffInfoAlterationVO.operatorName"/></td>
              		</tr>
              		<s:set name="staff_id" value="#staff_id+1"></s:set>
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
		    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
           	<input type="hidden" id="page" value="${page}"/>
           	<input type="hidden" id="limit" value="${limit}" />
           	<input type="hidden" id="totalPage" value="${totalPage }" />
          </div>
          <%@include file="/includes/pager.jsp" %>
          <div class="row col-sm-9 col-sm-offset-7 col-md-10 col-md-offset-4">
	     	 	<button type="button" class="btn btn-primary" onclick="location.href='<c:url value='/' />HR/staffInfoAlteration/gradeAlteration'">返回</button>
	      </div>
        </div>
      </div>
    </div>
  </body>
</html>
