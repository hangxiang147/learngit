<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
	.tab table tr .title {text-align:center;color:#000;width:15%}
	.bold{font-weight:bold}
	.underline{text-decoration:underline}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row"> 
      	<s:set name="panel" value="'chopList'"></s:set>
      	<s:set name="selectedPanel" value="'chopList'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<div id="printArea">
        	<h4 style="text-align:center" class="bold">印章使用申请单    
        					<button id="return" type="button" class="btn btn-default"
							onclick="location.href='javascript:history.go(-1)'" style="float:right;margin-top:-10px"
							>返回</button>
							<div style="float:right;height:20px;width:10px"></div>
        					<button id="print" type="button" class="btn btn-default"
							onclick="printOrder()" style="float:right;margin-top:-10px;"
							>打印</button></h4>
        	  	<div style="text-align:right;margin-right:20px">NO:年份&nbsp;<span class="underline">${year}</span>
        	  	&nbsp;编号&nbsp;<span class="underline">0000${chopBorrow_Id}</span></div>
        	  	<div class="tab">
        	  	<table>
        	  		<tr>
        	  			<td class="title">所属部门</td>
        	  			<td style="width:24%">${department}</td>
        	  			<td class="title">申请人</td>
        	  			<td>${userName}</td>
        	  			<td class="title">申请日期</td>
        	  			<td style="width:16%"><fmt:formatDate value="${chopBorrowVo.addTime}"  type="both" /></td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">印章名称</td>
        	  			<td colspan="5">${chopBorrowVo.chop_Name}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">是否外借</td>
        	  			<td><c:if test="${chopBorrowVo.isBorrow==1}">是</c:if><c:if test="${chopBorrowVo.isBorrow==0}">否</c:if></td>
        	  			<td class="title">开始时间</td>
        	  			<td><fmt:formatDate value="${chopBorrowVo.startTime}"  type="both" /></td>
        	  			<td class="title">结束时间</td>
        	  			<td><fmt:formatDate value="${chopBorrowVo.endTime}"  type="both" /></td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">文件基本信息</td></tr>
        	  		<tr>
        	  			<td class="title">文件名称</td>
        	  			<td colspan="3">${chopBorrowVo.fileName}</td>
        	  			<td class="title">文件类型</td>
        	  			<td>${chopBorrowVo.fileType}</td>
        	  		</tr>
        	  		<tr>
        	  			<td class="title">文件用途</td>
        	  			<td colspan="3">${chopBorrowVo.fileUse}</td>
        	  			<td style="text-align:left">是否涉及法律等重要事项</td>
        	  			<td>
        	  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrowVo.relateLaw=="1"}'>checked</c:if> name="chopBorrrowVo.relateLaw" value="1" style="height:15px;width:15px">&nbsp;<label>是</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrowVo.relateLaw=="0"}'>checked</c:if> name="chopBorrrowVo.relateLaw" value="0" style="height:15px;width:15px">&nbsp;<label>否</label> 
        	  			</td>
        	  		</tr>
        	  		<c:if test="${chopBorrowVo.fileType=='合同'}">
        	  		<tr>
        	  			<td class="title">甲方</td>
        	  			<td>${chopBorrowVo.partyA}</td>
        	  			<td class="title">乙方</td>
        	  			<td>${chopBorrowVo.partyB}</td>
        	  			<td class="title">合同申请时间</td>
        	  			<td>${chopBorrowVo.contractApplyDate}</td>
        	  		</tr>
        	  		</c:if>
        	  		<tr>
        	  			<td class="title">正版/复印件</td>
        	  			<td colspan="3">
        	  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrowVo.isCopy=="0"}'>checked</c:if> name="chopBorrrowVo.isCopy" value="0" style="height:15px;width:15px">&nbsp;<label>正版</label>&nbsp;&nbsp;&nbsp;
			  				<input type="checkbox" onclick="return false;" <c:if test='${chopBorrowVo.isCopy=="1"}'>checked</c:if> name="chopBorrrowVo.isCopy" value="1" style="height:15px;width:15px">&nbsp;<label>复印件</label>
        	  			</td>
        	  			<td class="title">份数</td>
        	  			<td>${chopBorrowVo.fileNum}</td>
        	  		</tr>
        	  		<tr><td colspan="6" class="bold" style="text-align:center;background:#efefef">审批情况</td></tr>
        	  		<tr>
        	  			<td rowspan="2">所属部门经理</td>
        	  			<td colspan="5">审批意见：
        	  				<c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[1].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[1].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[1].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">所属部门分管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[2].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[2].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[2].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">印章主管领导</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[3].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[3].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[3].endTime}</td>
        	  		</tr>
        	  		<tr>
        	  			<td rowspan="2">印章管理人员</td>
        	  			<td colspan="5">审批意见：
        	  			    <c:forEach items="${comments }" var="comment" varStatus="st">
	        	  				<c:if test="${comment.taskID == finishedTaskVOs[4].taskID }">
					  			${comment.content }
	        	  				</c:if>
        	  				</c:forEach>
        	  			</td>
        	  		</tr>
        	  		<tr>
        	  			<td colspan="3">签字：${finishedTaskVOs[4].assigneeName}</td>
        	  			<td>日期：</td>
        	  			<td>${finishedTaskVOs[4].endTime}</td>
        	  		</tr>
        	  	</table>
        	 </div>
        </div>
        </div>
      </div>
    </div>
    <script src="/assets/js/underscore-min.js"></script>
    <script type="text/javascript">
    	function printOrder(){
    	$("#return").remove();
    	$("#print").remove();
	  	var content=$('#printArea').html();
		$('body').empty().html(content);
		window.print();
		window.location.reload();
	   	}
		var search=location.search;
		//假如参数为空  这种情况 不可能发生 除了 从 print 页跳转回来
		if(!search){
			location.href=localStorage.lastUrl;
		}
		localStorage.lastUrl=location.href;
	</script>
  </body>
</html>
