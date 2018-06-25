<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	function printOrder(){
	  	var content=$('#printArea').html();
		$('body').empty().html(content);
		window.print();
		window.location.reload();
   	}
	$(function(){
		var search=location.search;
		//假如参数为空  这种情况 不可能发生 除了 从 print 页跳转回来
		if(!search){
			location.href=localStorage.lastUrl;
		}
		localStorage.lastUrl=location.href;
	});
</script>
<style type="text/css">
	.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
	.tab table{width:100%;border-collapse:collapse;word-break:break-all !important}
	.tab table tr td{border:1px solid #ddd;word-wrap:break-word;font-size:14px}
	.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
	.tab table tr .title {text-align:center;color:#000;width:15%;background:#efefef}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'brandAuthManagement'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        <h3 class="sub-header" style="margin-top:0px;">品牌授权 </h3>
        	 <div id="printArea">
		   	 <h4 style="text-align:center;font-weight:bold">品牌授权申请</h4>
		   	 <div class="tab" style="width:80%;text-align:center;margin:auto">
       	  		<table style="width:100%">
       	  			<tr>
       	  				<td class="title">公司</td>
       	  				<td style="width:20%">${brandAuth.companyName}</td>
       	  				<td class="title">授权时间</td>
       	  				<td>${brandAuth.authBeginDate} 至 ${brandAuth.authEndDate}</td>
       	  			</tr>
       	  			<tr>
       	  				<td class="title">店铺名称</td>
       	  				<td>${brandAuth.shopName}</td>
       	  				<td class="title">店铺网址</td>
       	  				<td>${brandAuth.shopAddress}</td>
       	  			</tr>
       	  			<tr>
       	  				<td class="title">平台</td>
       	  				<td>${brandAuth.platform}</td>
       	  				<td class="title">品牌</td>
       	  				<td>${brandAuth.brand}</td>
       	  			</tr>
       	  			<tr>
       	  				<td class="title">联系人</td>
       	  				<td>${brandAuth.contact}</td>
       	  				<td class="title">电话</td>
       	  				<td>${brandAuth.telephone}</td>
       	  			</tr>
       	  		</table>
       	  	  </div>
       	  	  </div>
	       	  <div style="margin-left:10%;margin-top:2%">
		       	  	<button type="button" class="btn btn-primary" onclick="printOrder()">打印申请单</button>
		       	  	<button class="btn btn-default" style="margin-left:5%" onclick="history.go(-1)">返回</button>
	       	  </div>
        </div>
      </div>
    </div>
</body>
</html>