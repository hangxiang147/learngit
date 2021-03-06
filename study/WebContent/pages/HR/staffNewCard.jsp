<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<style type="text/css">
ul,form,p,h1,i,em{margin:0px;padding:0px;}
i{list-style:none;font-style:normal;font-weight:normal;}
li{list-style-type:none;vertical-align:bottom}
*{margin:auto;}
.Work-report{max-width:400px;color:#555555;box-shadow:0px 1px 5px #dddddd;position:relative;padding:20px 25px;margin-top:20px;}
.Work-report h1{font-size:20px;font-weight:normal;}
.Work-report h1 span{font-size:14px;color:#1f5ea6;}
.Work-report .comy{color:#1f5ea6;font-size:16px;margin-top:10px;}
.Work-report .information{margin-top:20px;}
.Work-report .information p{height:30px;line-height:30px;}
.Work-report .information p span{float:left;padding-left:5px;}
.Work-report .those{margin-top:10px;*zoom:1;}
.Work-report .those:after{display:block;visibility:hidden;clear:both;overflow:hidden;height:0;content:"";}
.Work-report .those ul li{float:left;height:12px;margin:5px 0px;width:25%;text-align:center;}
.Work-report .those ul li img{height:12px;}
.ico{background:url(assets/images/ico.png) no-repeat;display:inline-block;width:13px;height:30px;float:left;}
.ico_tell{background-position:-14px 7px;}
.ico_mail{background-position:-56px 12px;}
.ico_address{background-position:0px 6px;}

</style>
</head>
  <body>
	<div class="container-fluid">
      <div class="row">
       <s:set name="panel" value="'dangan'"></s:set>
  		<%@include file="/pages/HR/panel.jsp" %>
    <div class="Work-report">
<img src="assets/images/logo2.png" width="100" style="position:absolute;right:20px;top:20px;"/>
<h1>${staffVO.lastName} <span>${staffVO.positionName}</span></h1>
<div class="comy">南通智造链科技有限公司</div>
<div class="information">
<p><i class="ico ico_tell"></i><span>${staffVO.telephone}</span></p>
<p><i class="ico ico_address"></i><span><c:if test="${staffVO.companyID == 1}">南通市通州区骑岸工业区智造链工业园<总部></c:if><c:if test="${staffVO.companyID == 2}">江苏省如东县掘港镇工业园区振兴三路北侧</c:if><c:if test="${staffVO.companyID == 3}">南通市崇川区通甲路6号中江国际广场3幢19楼</c:if><c:if test="${staffVO.companyID == 4}">南通市崇川区通甲路6号中江国际广场3幢19楼</c:if><c:if test="${staffVO.companyID == 5}">南京市玄武大道699号江苏软件园29幢1F</c:if><c:if test="${staffVO.companyID == 6}">广东省佛山市南海区盐步联安隔海路23号5楼</c:if></span></p>
</div>
<div style="margin-top:20px;text-align:center;"><img src="assets/images/logo1.png" width="120"/></div>
<div style="text-align:center;color:#1f5ea6;margin-top:15px;">专&nbsp;注&nbsp;于&nbsp;服&nbsp;装&nbsp;电&nbsp;商&nbsp;的&nbsp;小&nbsp;多&nbsp;快&nbsp;柔&nbsp;性&nbsp;供&nbsp;应&nbsp;链</div>
<div style="text-align:center;color:#1f5ea6;font-size:12px;margin-top:5px;">www.zhizaolian.com/www.haoduoyi.com</div>
<div style="color:#1f5ea6;text-align:center;margin-top:20px;border-bottom:1px dashed #1f5ea6;height:30px;">智造链旗下品牌</div>
<div class="those">
<ul>
<li><img src="assets/images/haoduoyi.png"/></li>
<li><img src="assets/images/haoyihui.png"/></li>
<li><img src="assets/images/hodoyi.png"/></li>
<li><img src="assets/images/kissmilk.png"/></li>
<li><img src="assets/images/missomo.png"/></li>
<li><img src="assets/images/yakuyiyi.png"/></li>
<li><img src="assets/images/yikuyiya.png"/></li>
</ul>
</div>
</div>
&nbsp;
&nbsp;
&nbsp;
&nbsp;

&nbsp;
&nbsp;
&nbsp;
&nbsp;
	<div class="row col-sm-9 col-sm-offset-6 col-md-10 col-md-offset-6">
   		<button type="button" class="btn btn-primary" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
  	</div>
	</div>
	</div>
  </body>
</html>
