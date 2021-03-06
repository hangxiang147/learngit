<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
$(function(){
	set_href();
});
function exportInsuranceList(){
	var companyId = $("select[name='companyId'] option:selected").val();
	var staffStatus = $("select[name='staffStatus'] option:selected").val();
	var year = $("input[name='year']").val();
	var month = $("input[name='month']").val();
	location.href = "HR/staff/exportInsuranceList?companyId="+companyId+"&staffStatus="+staffStatus+"&year="+year+"&month="+month;
}
</script>
<style type="text/css">
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">保险名单列表</h3>
       	<form action="HR/staff/getInsuranceList" class="form-horizontal" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
       		<div class="form-group">
       			<label class="control-label col-sm-1">公司</label>
       			<div class="col-sm-2">
       			<select class="form-control" name="companyId">
		  			<option value="">全部</option>
		  			<c:forEach items="${companyVOs}" var="company">
		  			<option value="${company.companyID}">${company.companyName}</option>
		  			</c:forEach>
			  	</select>
			  	</div>
			  	<label class="control-label col-sm-1">状态</label>
        		<div class="col-sm-2">
        			<select name="staffStatus" class="form-control">
        				<option ${staffStatus=='离职'?'selected':''} value="离职">离职</option>
        				<option ${staffStatus=='在职'?'selected':''} value="在职">在职</option>
        			</select>
        		</div>
       		</div>
        	<div class="form-group">
        		<label class="control-label col-sm-1">年份<span style="color:red"> *</span></label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="year" value="${year}" required
	    			 onclick="WdatePicker({ dateFmt: 'yyyy' })"/>
        		</div>
        		<label class="control-label col-sm-1">月份<span style="color:red"> *</span></label>
        		<div class="col-sm-2">
        			<input type="text" autocomplete="off" class="form-control" name="month" value="${month}" required
	    			 onclick="WdatePicker({ dateFmt: 'M' })"/>
        		</div>
        		<button class="btn btn-primary" type="submit" style="margin-left:5%">查询</button>
        		<button class="btn btn-primary" type="button" style="margin-left:5%" onclick="exportInsuranceList()"><span class="glyphicon glyphicon-download-alt"></span> 名单</button>
        	</div>
        </form>
			<div class="table-responsive" style="margin-top:30px;">
				<c:if test="${staffStatus=='离职'}">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:10%">姓名</th>
		                  <th style="width:15%">身份证号</th>
		                  <th style="width:10%">学历</th>
		                  <th style="width:10%">年龄</th>
		                  <th style="width:10%">入职时间</th>
		                  <th style="width:10%">险种分类</th>
		                  <th style="width:10%">离职时间</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${insuranceList}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item[0]}</td>
			              		<td>${item[1]}</td>
			              		<td>${item[2]}</td>
			              		<td>${item[3]}</td>
			              		<td>${item[4]}</td>
			              		<td>${item[5]}</td>
			              		<td>${item[6]}</td>
         					 </tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
           		    </c:if>
           		<c:if test="${staffStatus=='在职'}">
            	<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:10%">姓名</th>
		                  <th style="width:15%">身份证号</th>
		                  <th style="width:10%">学历</th>
		                  <th style="width:10%">年龄</th>
		                  <th style="width:10%">入职时间</th>
		                  <th style="width:10%">险种分类</th>
		                  <th style="width:10%">当前状态</th>
		                  <th style="width:10%">转正时间</th>
		                </tr>
		              </thead>
              		  <tbody>
              		  	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              	<c:forEach items="${insuranceList}" var="item" varStatus="index">
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item[0]}</td>
			              		<td>${item[1]}</td>
			              		<td>${item[2]}</td>
			              		<td>${item[3]}</td>
			              		<td>${item[4]}</td>
			              		<td>${item[5]}</td>
			              		<td>${item[6]}</td>
			              		<td>${item[7]}</td>
         					 </tr>
		              	</c:forEach>
		              </tbody>
           		    </table>
           		    </c:if>
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