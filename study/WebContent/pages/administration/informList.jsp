<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
	$(function() {
		set_href();

	});
	function search() {
		var params = $("#sendExpressForm").serialize();
		window.location.href = "administration/express/findSendExpressList?" + encodeURI(encodeURI(decodeURIComponent(params,true)));
	}
	
	
</script>
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
      	<s:set name="panel" value="'express'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
          <form  id="sendExpressForm" class="form-horizontal">
          	<h3 class="sub-header" style="margin-top:0px;">公告列表</h3>
			<div class="form-group">
				<label for="beginDate" class="col-sm-1 control-label">日期</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="sendExpressVO.beginDate" value="${sendExpressVO.beginDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="sendExpressVO.endDate" value="${sendExpressVO.endDate }" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" placeholder="结束时间">
			    </div>
			</div>
			<div class="form-group">
			<label for="myType" class="col-sm-1 control-label">置顶</label>
			<div class="col-sm-2">						
			<select class="form-control" id="myType" name="sendExpressVO.type" >
		      <option value="">请选择</option>
		      <option value="1" <s:if test="#request.sendExpressVO.type == 1 ">selected="selected"</s:if> >否</option>
		      <option value="2" <s:if test="#request.sendExpressVO.type == 2 ">selected="selected"</s:if> >是</option>
			</select>
			</div>
			</div>
			<div class="col-sm-5">
			</div>
			<button type="submit" id="submitButton" class="btn btn-primary" onclick="search()">查询</button>
          </form>
           <a class="btn btn-primary"  href="administration/express/addSendExpress"><span class="glyphicon glyphicon-plus"></span> 快递记录</a>
          <h2 class="sub-header"></h2>
          <div class="table-responsive">
            <table class="table table-striped">
              <thead>
                <tr>
                  <th class="col-sm-1">序号</th>
                  <th class="col-sm-1">标题</th>
                  <th class="col-sm-2">创建人</th> 
                  <th class="col-sm-1">创建时间</th>
                  <th class="col-sm-1">操作</th>

                </tr>
              </thead>
              <tbody>
              	<c:if test="${not empty sendExpressVOs}">
              	<s:set name="attendance_id" value="(#request.page-1)*(#request.limit)"></s:set>
              	<s:iterator id="sendExpress" value="#request.sendExpressVOs" status="count">
              		<tr>
              			<td class="col-sm-1"><s:property value="#attendance_id+1"/></td>   
              			<td class="col-sm-1"><s:property value="#sendExpress.userName"/></td>              			
              			<td class="col-sm-1"><s:property value="#sendExpress.expressNumber"/></td>              			
              			<td class="col-sm-1"><s:property value="#sendExpress.reason"/></td>              			
              			<td class="col-sm-1"><a>查看</a><a>删除</a></td>              			
              		</tr>
              		<s:set name="attendance_id" value="#attendance_id+1"></s:set>
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
