<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">

*{margin:auto;}
.logolm{background:#003e87;line-height:40px;width:450px;height:40px;text-align:center;color:#fff;font-size:18px;}
.introduce{width:490px;color:#555555;box-shadow:0px 1px 5px #dddddd;}
.introduce table{width:100%;border-collapse:collapse;}
.introduce table tr td{border:1px solid #ddd;height:auto;line-height:20px;padding:15px 10px;word-wrap:break-word;}
.introduce table tr td.lm{background:#f1f1f1;color:#000;}
.introduce table tr td p{padding:4px 0px;}

</style>
</head>
  <body>
<div class="container-fluid">
<div class="row" style="padding-top:30px;">
<s:set name="panel" value="'dangan'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>
        <div class="row">
    <div class="introduce ">
    <form class="form-horizontal" enctype="multipart/form-data">
<table>
  <tbody>
	<tr style="height:50px;">
	  
	  <td colspan="4" class="logolm">智造传奇，链接你我</td>
    </tr>
    <tr style="height:50px;">
      <td width="20%" class="lm">姓名:</td>
      <td width="30%">${staffVO1.lastName}</td>
      <td colspan="2" rowspan="5" align="center" valign="middle"><img src="HR/staff/getPicture?userID=${staffVO1.userID }" alt="个人照片 " id="photo" class="img-thumbnail" style="width:190px;height:230px;"></td>
    </tr>
    <tr style="height:50px;">
      <td class="lm">性别:</td>
      <td>${staffVO1.gender}</td>
    </tr>
    <tr style="height:50px;">
      <td class="lm">年龄:</td>
      <td>${staffVO1.age}</td>
    </tr>
    <tr style="height:50px;">
      <td class="lm">部门:</td>
      <td>${staffVO1.departmentName}</td>
    </tr>
    <tr style="height:50px;">
      <td class="lm">岗位:</td>
      <td>${staffVO1.groupDetail}</td>
      
    </tr>
	<tr style="height:50px;">
	  <td class="lm" width="20%">联系电话:</td>
      <td width="30%">${staffVO1.telephone}</td>
      <td class="lm" style="width:90px;">入职时间:</td>
      <td style="width:180px;">${staffVO1.entryDate}</td>

    </tr>
    <tr style="height:50px;">
      <td class="lm">地区:</td>
      <td><c:if test="${staffVO1.companyID==6}">佛山</c:if><c:if test="${staffVO1.companyID==1}">总部</c:if><c:if test="${staffVO1.companyID==2}">如东</c:if><c:if test="${staffVO1.companyID==3}">南通</c:if><c:if test="${staffVO1.companyID==4}">广州</c:if><c:if test="${staffVO1.companyID==5}">南京</c:if></td>
      <td class="lm" style="width:20%">毕业学校:</td>
      <td>${staffVO1.school}</td>
    </tr>
    <tr style="height:50px;">
      <td class="lm">学历:</td>
      <td>${staffVO1.education}</td>
      <td class="lm">专业:</td>
      <td>${staffVO1.major}</td>
    </tr>
    <tr style="height:50px;">
      <td class="lm">婚姻状况:</td>
      <td><c:if test="${staffVO1.maritalStatus==1}">已婚</c:if><c:if test="${staffVO1.maritalStatus==0 }">未婚</c:if></td>
      <td class="lm">籍贯:</td>
      <td>${staffVO1.nativePlace}</td>
    </tr>
    <tr>
      <td colspan="4"></td>
    </tr>
  		</tbody>
	  </table>
	  </form>
	 </div>
	</div>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<div class="row col-sm-9 col-sm-offset-6 col-md-10 col-md-offset-6">
	      		<button type="button" class="btn btn-primary" onclick="goPath('HR/staff/findStaffList')" style="margin-left:15px;">返回</button>
	     	</div>
	     </div>
	   </div>
  </body>
</html>
