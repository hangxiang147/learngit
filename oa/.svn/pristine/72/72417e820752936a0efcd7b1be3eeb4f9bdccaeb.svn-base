<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<script type="text/javascript">
</script>
<style type="text/css">
.tab {
	color: #555555;
	box-shadow: 0px 1px 5px #dddddd;
}

.tab table {
	width: 100%;
	border-collapse: collapse;
}

.tab table tr td {
	border: 1px solid #ddd;
	word-wrap: break-word;
	font-size: 14px
}

.tab table tbody tr td {
	height: auto;
	line-height: 20px;
	padding: 10px 10px;
	text-align: center;
}
.tab table tr .black {
	background: #efefef;
	text-align: center;
	color: #000;
	width: 15%
}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'shopManage'"></s:set>
        <%@include file="/pages/administration/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        		<h3 class="sub-header" style="margin-top: 0px;">
					店铺详情
					<button style="float: right"
						onclick="location.href='javascript:history.go(-1);'"
						class="btn btn-default">返回</button>
				</h3>
				<div class="tab">
					<table>
						<tr>
							<td class="black">店铺名称</td>
							<td style="width:15%">${shopInfo.shopName}</td>
							<td class="black">店铺状态</td>
							<td>${shopInfo.shopStatus=='0'?'正常':'关闭'}</td>
							<td class="black">开店时间</td>
							<td style="width:20%">${shopInfo.openDate}</td>
						</tr>
						<tr>
							<td class="black">账号</td>
							<td>${shopInfo.account}</td>
							<td class="black">密码</td>
							<td>${shopInfo.pwd}</td>
							<td class="black">注册手机号</td>
							<td>${shopInfo.registerTelephone}</td>
						</tr>
						<tr>
							<td class="black">注册手机号的属主</td>
							<td>${shopInfo.regPhoneOwner}</td>
							<td class="black">注册支付宝账户</td>
							<td>${shopInfo.registerAlipayAccount}</td>
							<td class="black">注册银行卡账号</td>
							<td>${shopInfo.registerBankAccount}</td>
						</tr>
						<tr>
							<td class="black">预留手机号</td>
							<td>${shopInfo.reserveTelephone}</td>
							<td class="black">预留手机号的属主</td>
							<td>${shopInfo.reservePhoneOwner}</td>
							<td class="black">预留手机号的状态</td>
							<td>${shopInfo.reservePhoneStatus}</td>
						</tr>
						<tr>
							<td class="black">注册公司</td>
							<td colspan="3">${shopInfo.registerCompany}</td>
							<td class="black">营业执照注册号</td>
							<td>${shopInfo.registerNum}</td>
						</tr>
						<tr>
						</tr>
					</table>
				</div>
			
        </div>
      </div>
    </div>
  </body>
</html>