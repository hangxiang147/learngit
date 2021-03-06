<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">
	$(function(){
		$("[data-toggle='tooltip']").tooltip();
	});
	function showMore(type){
		location.href="/administration/partner/showPartnerDetailByUserId?type="+encodeURIComponent(type);
	}
</script>
<style type="text/css">
	#partnerInfo{
		font-size:14px;
	}
	.icon {
	   width: 1.5em; height: 1.5em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	.index_column ul li{
		border-color:#fff;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'partnerCenter'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<h4 style="font-weight:bold">恭喜您成为智造链合伙人</h4>
        	<table id="partnerInfo">
        		<tbody>
        			<tr>
        				<td rowspan="4" style="width:8%"><img alt="个人照片 "src="HR/staff/getPicture?userID=${partner.userId}" class="img-thumbnail"></td>
        				<td colspan="2">&nbsp;&nbsp;工号：${staffNum}</td>
        			</tr>
        			<tr>
        				<td colspan="2">
        				&nbsp;&nbsp;职位：<c:forEach items="${depAndPoss}" var="depAndPos">${depAndPos}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</c:forEach></td>
        			</tr>
        			<tr>
        				<td colspan="2">&nbsp;&nbsp;申请加入时间：<fmt:formatDate value="${partner.addTime}" pattern="yyyy年MM月dd日" /></td>
        			</tr>
        			<tr>
        				<td style="width:30%">&nbsp;&nbsp;您已成功缴纳认购金：<fmt:formatNumber type="number" value="${totalMoney}" pattern="0.0" maxFractionDigits="1"/>元</td>
        				<td>您可享受期权金额：<fmt:formatNumber type="number" value="${optionMoney}" pattern="0.0" maxFractionDigits="1"/>元&nbsp;&nbsp;
        					<a href="javascript:void(0)" onclick="goPath('administration/partner/showPartnerOptionDetail')">
		              			<svg class="icon" aria-hidden="true" title="查看明细" data-toggle="tooltip">
            						<use xlink:href="#icon-Detailedinquiry"></use>
            					</svg>
		              		</a></td>
        			</tr>
        		</tbody>
        	</table>
        	<div class="sub-header" style="margin-bottom:1%"></div>
        	<div class="index_column">
			<h1>已享受福利&待遇<a onclick="showMore('已享受福利&待遇')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<ul>
			<c:if test="${not empty typeAndDetailListMap['已享受福利&待遇']}">
				<c:forEach items="${typeAndDetailListMap['已享受福利&待遇']}" var="detail" varStatus="status" end="4">
					<li>${status.index+1}、${detail}</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty typeAndDetailListMap['已享受福利&待遇'] }">
				<li>暂无</li>
			</c:if>
			</ul>
			</div>
			<div class="index_column">
			<h1>已进行的培训和拓展<a onclick="showMore('已进行的培训和拓展')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<ul>
			<c:if test="${not empty typeAndDetailListMap['已进行的培训和拓展']}">
				<c:forEach items="${typeAndDetailListMap['已进行的培训和拓展']}" var="detail" varStatus="status" end="4">
					<li>${status.index+1}、${detail}</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty typeAndDetailListMap['已进行的培训和拓展'] }">
				<li>暂无</li>
			</c:if>
			</ul>
			</div>
			<div class="index_column">
			<h1>合伙人章程&规定<a onclick="showMore('合伙人章程&规定')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<ul>
			<c:if test="${not empty typeAndDetailListMap['合伙人章程&规定']}">
				<c:forEach items="${typeAndDetailListMap['合伙人章程&规定']}" var="detail" varStatus="status" end="4">
					<li>${status.index+1}、${detail}</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty typeAndDetailListMap['已享受福利&待遇'] }">
				<li>暂无</li>
			</c:if>
			</ul>
			</div>
			<div class="index_column">
			<h1>奖励<a onclick="showMore('奖励')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<ul>
			<c:if test="${not empty typeAndDetailListMap['奖励']}">
				<c:forEach items="${typeAndDetailListMap['奖励']}" var="detail" varStatus="status" end="4">
					<li>${status.index+1}、${detail}</li>
				</c:forEach>
			</c:if>
			<c:if test="${empty typeAndDetailListMap['奖励'] }">
				<li>暂无</li>
			</c:if>
			</ul>
			</div>
      	</div>
      </div>
    </div>
</body>
</html>