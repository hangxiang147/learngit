<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
          <form  id="workReportForm" class="form-horizontal" action="/workReport/weekReporterManage" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<div class="form-group">
				<label for="name" class="col-sm-1 control-label">姓名</label>
			    <div class="col-sm-2">
			    	<input type="text" autoComplete="off" class="form-control" name="userName" value="${userName}">
			    </div>
			   </div>
			    <div class="form-group">
				<label for="company" class="col-sm-1 control-label">公司部门</label>
			    <div class="col-sm-2" id="company_div">
			    	<select class="form-control" id="company" name="companyId">
				      <option value="">请选择</option>
				      <c:if test="${not empty companyVOs }">
				      	<s:iterator id="company" value="#request.companyVOs" status="st">
				      		<option value="<s:property value="#company.companyID" />" 
				      		<s:if test="#request.companyId == #company.companyID ">selected="selected"</s:if>>
				      		<s:property value="#company.companyName"/>
				      		</option>
				      	</s:iterator>
				      </c:if>
					</select>
			    </div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary">查询</button>&nbsp;
          </form>
          <button class="btn btn-primary" data-toggle="modal" data-target="#addWeekReporter">新增</button>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped" style="table-layout: fixed">
              <thead>
                <tr>
                  <th style="width:10%">序号</th>
                  <th style="width:35%">公司</th>
                  <th style="width:25%">部门</th>
                  <th style="width:20%">姓名</th>
                  <th>操作</th>
                </tr>
              </thead>
              <tbody>
              	<c:forEach items="${needWeekReportPersons}" var="item" varStatus="status">
              	<tr>
              		<td>${status.index+1+(page-1)*limit}</td>
              		<td>${item[1]}</td>
              		<td>${item[2]}</td>
              		<td>${item[0]}</td>
              		<td>
              			<a href="javacript:void(0)" class="glyphicon glyphicon-trash" onclick="deleteWeekReporter(${item[3]})"></a>
              		</td>
              	</tr>
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
    <div id="addWeekReporter" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<form class="form-horizontal" action="workReport/addWeekReporter">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">添加人员</h4>
			</div>
			<div class="modal-body">

				<div class="form-group">
					<label for="name" class="col-sm-3 control-label">姓名<span style="color:red"> *</span></label>
					<div id="name" class="col-sm-6">
						<input type="text" class="form-control" required autocomplete="off" name="weekReporterName"/>
						<input type="hidden" name="weekReporterUserId">
					</div>
				</div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="submit" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
	</div>
	</div>
	<script src="/assets/js/require/require2.js"></script>
<script type="text/javascript">
  	require(['staffComplete'],function (staffComplete){
		new staffComplete().render($('input[name="weekReporterName"]'),function ($item){
			$("input[name='weekReporterUserId']").val($item.data("userId"));
		});
	});
  	function checkEmpty(){
  		if($("input[name='weekReporterName']").val()==''){
  			$("input[name='weekReporterUserId']").val('');
  		}
  	}
	$(document).click(function (event) {
		if ($("input[name='weekReporterUserId']").val()=='')
		{
			$("input[name='weekReporterName']").val("");
		}
	}); 
	function deleteWeekReporter(reporterId){
		layer.confirm("确定删除？",{offset:'100px'},function(){
			location.href="workReport/deleteWeekReporter?reporterId="+reporterId;
		});
	}
</script>
