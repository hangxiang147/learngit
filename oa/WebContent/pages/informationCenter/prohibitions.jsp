<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
</script>
<style type="text/css">
	
</style>
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'regulation'"></s:set>
        <%@include file="/pages/informationCenter/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<h3 class="sub-header" style="margin-top:0px;text-align: center;">智造链员工管理高压线</h3>
        	<ol>
			  <li>禁止泄露公司机密</li>
			  <li>禁止以权谋私，或营私舞弊，或索贿、受贿，或欺诈，或利用职权间接获利的行为</li>
			  <li>禁止盗窃损坏公司财产</li>
			  <li>禁止办公室恋情</li>
			  <li>禁止拉帮结派</li>
			  <li>禁止公司同事之间相互借款</li>
			  <li>禁止异性同事间的骚扰</li>
			  <li>禁止在工作时间做与工作无关的事项</li>
			  <li>禁止同事之间发生打架行为</li>
			  <li>禁止工作时间饮酒闹事</li>
			  <li>禁止在工作区域大声喧哗，影响他人工作</li>
			</ol>
        </div>
      </div>
    </div>
  </body>
</html>
