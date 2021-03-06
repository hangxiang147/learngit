<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	function changeAuditState(auditValue) {
		var auditStatus = "${staffVO.auditStatus}";
		var auditStatesNode = document.getElementById("auditStatusForm");
		 auditStatesNode.value= ""+auditValue;
		var auditFormNode = document.getElementById("auditForm");
		auditFormNode.submit();
		return;
	}
</script>
<style type="text/css">

	.col-sm-1 {
		padding-right: 6px;
		padding-left: 6px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    height: 34px;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555;
	}
	
	.float_div{
		position:absolute;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	
	.tableuser{border:1px solid #ddd;border-spacing:0;border-collapse:collapse;width:780px;color:#555555;}
	.tableuser tr{display:table-row;vertical-align:inherit;border-color:inherit;}
	.tableuser tr td{border:1px solid #ddd;height:34px;line-height:34px;padding:10px 10px;}
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
       <s:set name="panel" value="'dangan'"></s:set>
  		<%@include file="/pages/HR/panel.jsp" %>
  		<div class="row">
         <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form class="form-horizontal" enctype="multipart/form-data">
        	 	<h3 class="sub-header" style="margin-top:0px;">信息审核<span style="font-size:20px;color:#ccc;float:right;">工号：<span id="staffNumberText">&nbsp;&nbsp;&nbsp;${staffVO.staffNumber}</span></span></h3>
			  
				<table class="tableuser">
				 	<tbody><tr>
				   	<td width="18%" align="center">姓名：</td>
				    <td width="20%">${staffVO.lastName}</td>
				    <td width="18%" align="center">性别：</td>
				    <td width="20%">${staffVO.gender}</td>
				    <td colspan="2" rowspan="4" align="center"><img src="HR/staff/getPicture?userID=${staffVO.userID }" alt="个人照片 " id="photo" class="img-thumbnail" style="width:180px;height:210px;"></td>
				  </tr>
				  <tr>
				    <td align="center">身份证号码：</td>
				    <td colspan="3">${staffVO.idNumber }</td>
				  </tr>
				  <tr>
				    <td align="center">联系电话：</td>
				    <td>${staffVO.telephone}</td>
				    <td align="center">出生日期：</td>
				    <td>${staffVO.birthday}</td>
				  </tr>
				  <tr>
				  <td align="center">紧急联系人：</td>
				    <td>${staffVO.emergencyContract}</td>
				    <td align="center">联系人电话：</td>
				    <td>${staffVO.emergencyPhone}</td>
				  </tr>
				  <tr>
				  	<td align="center">学历证书编号</td>
				  	<td colspan="5">${staffVO.educationID}</td>
				  </tr>
				  <tr>
				  	<td align="center">学位证书编号</td>
				  	<td colspan="5">${staffVO.degreeID}</td>
				  </tr>
				  <tr>
				    <td align="center">现住地址：</td>
				    <td colspan="5">${staffVO.address}</td>
				  </tr>
				  <tr>
				    <td align="center">毕业学校：</td>
				    <td colspan="5">${staffVO.school}</td>
				  </tr>
				  <tr>
				    <td align="center">毕业日期：</td>
				    <td colspan="2">${staffVO.graduationDate}</td>
				    <td align="center">第一学历：</td>
				    <td colspan="2">${staffVO.education}</td>
				  </tr>
				  <tr>
				    <td align="center">专业：</td>
				    <td colspan="2">${staffVO.major}</td>
				    <td align="center">籍贯：</td>
				    <td colspan="2">${staffVO.nativePlace}</td>
				  </tr>
				  <tr>
				    <td align="center">婚姻状况：</td>
				    <td colspan="2"><c:if test="${staffVO.maritalStatus == 0}">未婚</c:if><c:if test="${staffVO.maritalStatus == 1}">已婚</c:if></td>
				    <td align="center">职级：</td>
				    <td colspan="2">${staffVO.gradeName}</td>
				  </tr>
				  <tr>
				    <td align="center">薪资待遇：</td>
				    <td colspan="2">${staffVO.salary}</td>
				    <td align="center">案底说明</td>
				    <td colspan="2">${staffVO.criminalRecord}</td>
				  </tr>
				  </tbody>
				</table>
			</form>
        </div>
        </div>
	     	 <div class="row col-sm-9 col-sm-offset-7 col-md-10 col-md-offset-4">
	     	 	<button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'">返回</button>
	       		<button type="button" class="btn btn-primary"  onclick="changeAuditState(2)">审核不通过</button> 
	        	<button type="button" class="btn btn-primary"  onclick="changeAuditState(1)">审核通过</button>
	      	</div>
	      	<div class="row">
	      		<form id="auditForm" action="<c:url value='/HR/staff/updateAuditResult'/>" method="post">
	      			<input type="hidden" name="staffID" value="${staffVO.staffID}"/>
	      			<input type="hidden" name="auditStatusonAudit" id="auditStatusForm"/>
	      		</form>
	      	</div>
   	 </div>
    </div>
  </body>
</html>
