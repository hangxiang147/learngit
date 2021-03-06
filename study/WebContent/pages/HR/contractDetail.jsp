<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
</script>
<style type="text/css">
	a:link {
	 text-decoration: none;
	}
	a:visited {
	 text-decoration: none;
	}
	a:hover {
	 text-decoration: none;
	}
	a:active {
	 text-decoration: none;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	
	.form-group {
	    margin-bottom: 30px;
	    margin-top: 30px;
	}
	
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}

</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
      	<s:set name="panel" value="'contract'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>			
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form action="administration/meeting/saveMeeting" method="post" class="form-horizontal" onsubmit="return check()">
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">查看合同</h3>
        	  <div class="form-group" > 
			  	<label for="reason" class="col-sm-2 control-label">公司名称（甲方）：</label>
			    <div class="col-sm-4"><span class="detail-control"><c:if test="${contractVO.partyA==1 }">南通智造链科技有限公司</c:if>
			    													<c:if test="${contractVO.partyA==2 }">南通迷丝茉服饰有限公司</c:if>
			   														<c:if test="${contractVO.partyA==3 }">广州亦酷亦雅电子商务有限公司</c:if>
			   														<c:if test="${contractVO.partyA==4 }">南通智造链电子商务有限公司</c:if></span></div>
			 	<label for="executor" class="col-sm-2 control-label">员工（乙方）：</label>
			  	<div class="col-sm-4"><span class="detail-control">${contractVO.partyBName}</span></div>
			  </div> 
			  <div class="form-group" id="executor_div">
			  	<label for="executor" class="col-sm-2 control-label">开始日期：</label>
			  	<div class="col-sm-4"><span class="detail-control">${contractVO.beginDate}</span></div>
			  	<label for="executor" class="col-sm-2 control-label">结束日期：</label>
			  	<div class="col-sm-4"><span class="detail-control">${contractVO.endDate}</span></div>
			  </div>
			  <c:if test="${not empty contractVO.contractBackups}">
			  <div class="form-group">
			  <label for="reason" class="col-sm-2 control-label">合同备份：</label>
			  <table>
			    	<tr><td><span class="detail-control"><a href="<c:url value='/'/>HR/contract/download?dLDName=${contractVO.contractID}_${contractVO.contractBackups}" >${contractVO.contractBackups}</a></span></td></tr>
			    </table>
			    </div>
			    </c:if>
			  <c:if test="${not empty contractVO.signature}">
			  <div class="form-group">
			  <label for="reason" class="col-sm-2 control-label">员工签名：</label>
			   <span><img style="height:100px;" alt="" src="http://www.zhizaolian.com:9000/contract/${contractVO.contractID}_${contractVO.signature}"></span>
			    </div>
			    </c:if>
			  <div class="form-group">
			  <div class="col-sm-6"></div>
			    <button type="button" class="btn btn-primary" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			  
			  <h4 class="sub-header" style="margin-top:0px;">过期合同</h4>
			  <c:if test="${not empty expiredContractVOs}">
              	<s:iterator id="ctract" value="#request.expiredContractVOs" status="count">
              		<div class="form-group" > 
					  	<label for="reason" class="col-sm-2 control-label">公司名称（甲方）：</label>
					    <div class="col-sm-3"><span class="detail-control"><s:if test="#ctract.partyA==1">南通智造链科技有限公司</s:if>
					    													<s:if test="#ctract.partyA==2">南通迷丝茉服饰有限公司</s:if>
              			    												<s:if test="#ctract.partyA==3">广州亦酷亦雅电子商务有限公司</s:if>
              			    												<s:if test="#ctract.partyA==4">南通智造链电子商务有限公司</s:if></span></div>
					    <label for="executor" class="col-sm-1 control-label">开始日期：</label>
					  	<div class="col-sm-2"><span class="detail-control"><s:property value="#ctract.beginDate"/></span></div>
					  	<label for="executor" class="col-sm-1 control-label">结束日期：</label>
					  	<div class="col-sm-2"><span class="detail-control"><s:property value="#ctract.endDate"/></span></div>
					</div> 
					<s:if test="#ctract.contractBackups.length() != 0">
					  <div class="form-group">
					  <label for="reason" class="col-sm-2 control-label">合同备份：</label>
					  <table>
					    	<tr><td><span class="detail-control"><a href="<c:url value='/'/>HR/contract/download?dLDName=<s:property value="#ctract.contractID"/>_<s:property value="#ctract.contractBackups"/>" ><s:property value="#ctract.contractBackups"/></a></span></td></tr>
					  </table>
					  </div>
					</s:if>
              	</s:iterator>
              </c:if>
			</form>
        </div>
      </div>
    </div>
</body>
</html>