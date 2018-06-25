<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<style type="text/css">
	
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">人员列表</h3>
       	  
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th>姓名</th>
                  <th>部门</th>
                  <th>入职时间</th>
                  <th>入职天数</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty staffVOs}">
              	<s:set name="staff_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="staffVO" value="#request.staffVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#staff_id+1"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.lastName"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.departmentName"/></td>
              			<td class="col-sm-1"><s:property value="#staffVO.entryDate1"/></td>
              			<td class="col-sm-1">
              			<s:property value="#staffVO.days"/>
              			<s:if test="#staffVO.days>=30">
              				【满足转正天数】
						</s:if>
              			</td>
						<td class="col-sm-1">
						<a href="javascript:void(0);"
              				onclick="confirmSend(<s:property value="#staffVO.days"/>, <s:property value='#staffVO.staffID'/>)"
						>发送转正邀请</a></td>
						
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
          
        </div>
      </div>
    </div>
    <script type="text/javascript">
	$(function() {
		set_href();
	});
	function confirmSend(days, staffID){
		if(days<80){
			layer.confirm("人事标准转正时间为3个月，确定需要提前转正？",{offset:'100px'},function(index){
				layer.close(index);
				location.href="HR/process/sendFormalInvitation?staffID="+staffID;
			});
		}else{
			location.href="HR/process/sendFormalInvitation?staffID="+staffID;
		}
	}
</script>
  </body>
</html>
