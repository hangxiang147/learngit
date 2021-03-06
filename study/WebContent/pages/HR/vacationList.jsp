<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
	});
	function showVacationUsers(users){
		users = users.substring(1, users.length-1);
		layer.alert(users, {title:'请假人员', offset:'100px'});
	}
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
      	<s:set name="panel" value="'process'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">申请列表</h3>
       	  
       	  <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th>序号</th>
                  <th style="width:10%">请假人/部门</th>
                  <th>岗位</th>
                  <th>开始时间</th>
                  <th>结束时间</th>
                  <th>休假天数</th>
                  <th>工作代理人</th>
                  <th>请假类型</th>
                  <th>事由</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty taskVOs}">
              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
              			<td class="col-sm-1"><s:property value="#taskVO.vacationUserName" /></td>
              			<td class="col-sm-2"><s:iterator id="group" value="#taskVO.groupList" status="st"><s:if test="#st.index != 0 "><br></s:if><s:property value="#group"/></s:iterator></td>
              			<td class="col-sm-1"><s:property value="#taskVO.beginDate" /></td>
              			<td class="col-sm-1"><s:property value="#taskVO.endDate" /></td>
              			<td class="col-sm-1"><s:property value="#taskVO.vacationTime" /></td>
              			<td class="col-sm-1"><s:property value="#taskVO.agentName" /></td>
              			<td class="col-sm-1"><s:property value="#taskVO.vacationType" /></td>
              			<td class="col-sm-1"><s:property value="#taskVO.reason" /></td>
              			<td class="col-sm-2">
            			<s:if test="#taskVO.type=='部门'">
	              			<a href="javascript:void(0)" onclick="showVacationUsers('${taskVO.staffNames}')">
          		             	<svg class="icon" aria-hidden="true" title="查看请假人员" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
           		            </a>
	              			&nbsp;
              			</s:if>
              			<a onclick="goPath('HR/process/taskComplete?taskID=<s:property value='#taskVO.taskID' />&result=1&businessType=请假申请')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="同意" data-toggle="tooltip">
							<use xlink:href="#icon-wancheng"></use>
						</svg>
              			</a>
              			&nbsp;	              								 
              			<a onclick="goPath('HR/process/taskComplete?taskID=<s:property value='#taskVO.taskID' />&result=2&businessType=请假申请')" href="javascript:void(0)">
              			<svg class="icon" aria-hidden="true" title="不同意" data-toggle="tooltip">
							<use xlink:href="#icon-butongyi"></use>
						</svg>
              			</a>
		              	<s:if test="#taskVO.attachmentSize>0">
		              	&nbsp;
		              	<a onclick="goPath('/personal/showVacationAttachment?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=vacationList')" href="javascript:void(0)">
		              	<svg class="icon" aria-hidden="true" title="查看附件" data-toggle="tooltip">
							<use xlink:href="#icon-fujian"></use>
						</svg>
		              	</a>
		              	</s:if>
		              	&nbsp;
		              	<a onclick="goPath('HR/process/processHistory?processInstanceID=<s:property value='#taskVO.processInstanceID'/>&selectedPanel=vacationList')" href="javascript:void(0)">
		              	<svg class="icon" aria-hidden="true" title="查看已审批环节" data-toggle="tooltip">
							<use xlink:href="#icon-liucheng"></use>
						</svg>
		              	</a>
		              	</td>
              		</tr>
              		<s:set name="task_id" value="#task_id+1"></s:set>
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
  </body>
</html>
