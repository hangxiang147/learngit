<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>工作汇报截图页面</title>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
$(function() {
	 var sum   = 0;
	 var table = document.getElementById("workTab");
     for(var i=1;i<table.rows.length-1;i++){
       sum += parseFloat(table.rows[i].cells[4].innerText);
     }
     sum=sum.toFixed(1);
     $("#workHours").text(sum);
});

</script>

<style type="text/css">

body{font:inherit;vertical-align:baseline;font-family: "Century Gothic","MicroSoft YaHei","hiragino sans GB","Helvetica Neue",Helvetica,Arial,sans-serif;font-size:14px;color:#444444;-webkit-font-smoothing:antialiased;min-width:1200px;}
*{margin:auto;}
.Work-report{max-width:700px;}
.Work-report .lm{line-height:40px;height:46px;font-size:16px;*zoom:1;}
.Work-report .lm:after{display:block;visibility:hidden;clear:both;overflow:hidden;height:0;content:"\0020";}
.Work-report .tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
.Work-report .tab table{width:100%;border-collapse:collapse;}
.Work-report .tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px;}
.Work-report .tab table thead tr td{background:#efefef;text-align:center;height:30px;line-height:30px;color:#000;}
.Work-report .tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;text-align:center;}
.Work-report .tab table tbody tr td:first-child{text-align:left;}
.Work-report .tab table tbody tr td.lm{background:#f1f1f1;color:#000;}
.Work-report .tab table tbody tr td p{padding:4px 0px;}
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
        <%@include file="/pages/personal/panel.jsp" %>
<div class="Work-report" style="margin-top:40px;">
<div class="lm"><span style="float:left">姓名：<c:if test="${isPartner}"><svg class="icon" style="margin-bottom:-0.6%;color:#ec4c4c" aria-hidden="true"><use xlink:href="#icon-hezhu"></use></svg></c:if>${userName }（<c:forEach var="group" items="${groupList }" varStatus="st"><c:if test="${st.index != 0 }"> | </c:if>${group }</c:forEach>）</span></div>
<div style="line-height:20px;font-size:14px;color:#888888;height:30px;"><span style="float:left">汇报日期：${reportDate}&nbsp;&nbsp;${workReportVOs[0].weekDay}</span><span style="float:right;">提交时间：${workReportVOs[0].addTime}</span></div>
<div class="tab">
<table id="workTab">
<thead>
<tr>
<td>工作内容</td>
<td width="80">数量</td>
<td width="100">下达任务人</td>
<td width="90">完成情况</td>
<td width="90">完成用时</td>
</tr>
</thead>
<tbody>
<c:forEach var="workReport" items="${workReportVOs }">
<tr>
<td>${workReport.workContents }</td>
<td>${workReport.quantity==null?1:workReport.quantity}</td>
<td>${workReport.assignTaskUserNames }</td>
<td>${workReport.completeStates }</td>
<td>${workReport.workHour}</td>
</tr>

</c:forEach>
<tr><td colspan="5" style="text-align:right;">合计用时：<span id="workHours"></span> 小时</td></tr></tbody>
</table>
</div>
<div style="padding-top:50px">
<span style="float:left;"><button type="button" class="btn btn-primary" onclick="location.href='javascript:history.go(-1);'">返回</button></span>
<span style="color:red;padding-top:12px;float:left;margin-left:20px;">注：此页面为工作汇报截屏页！</span></div>
</div>
</div>
</div>
</body>
</html>