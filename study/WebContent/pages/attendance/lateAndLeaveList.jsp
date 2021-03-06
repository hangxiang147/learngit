<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
	table{
	border-collapse:collapse;
	margin-top:10px;
	}
	table tr td{word-wrap:break-word;font-size:10px;padding:8px 7px;text-align:center}
	table tr th{font-size: 10px;text-align:center}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'process'"></s:set>
        <%@include file="/pages/attendance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<ul class="nav nav-tabs" role="tablist" id="myTab">
        		<c:if test="${not empty companyVOs }">
	        	<c:forEach items="${companyVOs }" var="companyVO" varStatus="st">
	        		<li role="presentation"><a href="#${companyVO.companyID}" role="tab" data-toggle="tab">${companyVO.companyName}</a></li>
	        	</c:forEach>
	        	</c:if>
        	</ul>
			<div class="tab-content">
				<c:forEach items="${companyVOs}" var="companyVO">
					<div role="tabpanel" class="tab-pane" id="${companyVO.companyID}">
						<div>
							<table style="width:100%;float:left">
							<tr>
								<th style="width:15%">名次</th>
								<th style="width:15%">迟到次数</th>
								<th style="width:30%">姓名</th>
								<th style="width:40%;border-right:1px solid #eee">部门</th>
							</tr>
							<c:forEach items="${lateObjs}" var="lateObj" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${lateObj[1]}</td>
									<td>${lateObj[2]}</td>
									<td style="border-right:1px solid #eee">${lateObj[3]}</td>
								</tr>
							</c:forEach>
							</table>
						<%-- 	<table style="width:50%;float:left">
							<tr>
								<th style="width:15%">名次</th>
								<th style="width:15%">早退次数</th>
								<th style="width:30%">姓名</th>
								<th style="width:40%">部门</th>
							</tr>
							<c:forEach items="${leaveObjs}" var="leaveObj">
								<tr>
									<td>${leaveObj[0]}</td>
									<td>${leaveObj[1]}</td>
									<td>${leaveObj[2]}</td>
									<td>${leaveObj[3]}</td>
								</tr>
							</c:forEach>
							</table> --%>
						</div>
					</div>
				</c:forEach>
			</div>    
        </div>
      </div>
    </div>
    <script>
    	$(function(){
    		var companyNum = '${fn:length(companyVOs)}';
    		var companyId = ${companyId};
    		for(var i=0; i<parseInt(companyNum); i++){
    			if((companyId-1) == i){
    				$("#myTab li:eq("+i+") a").tab("show");
    			}
    			$("#myTab li:eq("+i+") a").attr("index",(i+1)).click(function(e) {
       				e.preventDefault();
       				window.location.href = "attendance/lateAndLeaveList?companyId="+$(this).attr("index");
       				Load.Base.LoadingPic.FullScreenShow(null);
       			});
    		}
    	});
    </script>
  </body>
</html>
