<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script type="text/javascript">
	function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function check() {
		if ($("#endDate").val() <= $("#beginDate").val()) {
			showAlert("结束日期必须大于开始日期！");
			return  false;
		}
		$("#submitButton").addClass("disabled");
		return true;
	}
	

</script>
<style type="text/css">
	.col-sm-1 {
		padding-right:0px;
		padding-left:0px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
	
	.detail-control {
		display: block;
	    width: 100%;
	    padding: 6px 12px;
	    font-size: 14px;
	    line-height: 1.42857143;
	    color: #555; 
	}
	
	.inputout{position:relative;}
	.text_down{position:absolute;top:34px;left:18px;border:1px solid #dddddd;width:305px;display:none;z-index:9999;background:#ffffff;}
	.text_down ul{padding:2px 10px;}
	.text_down ul li{line-height:24px;cursor:pointer;list-style-type:none;vertical-align:bottom}
	.text_down ul li span{color:#cc0000;}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
       	<s:set name="panel" value="'contract'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form  action="HR/contract/saveContract"  method="post" class="form-horizontal" enctype="multipart/form-data" onsubmit="$('#submitButton').attr('disabled','disabled')">
        	
        	  <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">修改合同</h3>
        	  <input type="hidden" name="contractVO.contractID" value="${contractVO.contractID }" />
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-2 control-label">公司名称（甲方）</label>
			    <div class="col-sm-3">						
			    <select class="form-control"  name="contractVO.partyA" required="required" >
				      <option value="">请选择</option>
				      <option value="1" <c:if test="${contractVO.partyA == 1 }">selected="selected"</c:if>>南通智造链科技有限公司</option>
				      <option value="2" <c:if test="${contractVO.partyA == 2 }">selected="selected"</c:if>>南通迷丝茉服饰有限公司</option>
				      <option value="3" <c:if test="${contractVO.partyA == 3 }">selected="selected"</c:if>>广州亦酷亦雅电子商务有限公司</option>
				      <option value="4" <c:if test="${contractVO.partyA == 4 }">selected="selected"</c:if>>南通智造链电子商务有限公司</option>
				     </select>
			     </div>
			  </div>
			  <div class="form-group" >
			  	<label for="executor" class="col-sm-2 control-label">员工（乙方）</label>
			  	<div class="col-sm-6"><span class="detail-control">${contractVO.partyBName}</span></div>
			  	<input type="hidden" id="agentID" name="contractVO.partyB" value="${contractVO.partyB }"/>
			  </div>
			  <div class="form-group" >
	    		<label for="beginDate" class="col-sm-2 control-label">开始日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="beginDate" name="contractVO.beginDate" value="${contractVO.beginDate }" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
	    	  </div> 
			  <div class="form-group" >
	    		<label for="endDate" class="col-sm-2 control-label">结束日期</label>
	    		<div class="col-sm-2">
	    			<input type="text" class="form-control" id="endDate" name="contractVO.endDate" value="${contractVO.endDate }" required="required" onclick="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
	    		</div>
	    	  </div> 
	    	  <div class="form-group">
			    <label for="uploadFile" class="col-sm-2 control-label"><span class="glyphicon glyphicon-paperclip"></span>合同备份</label>
			    <c:if test="${not empty contractVO.contractBackups}">
				  	<div class="col-sm-10"><span class="detail-control"><a href="<c:url value='/'/>HR/contract/download?dLDName=${contractVO.contractID}_${contractVO.contractBackups}" >${contractVO.contractBackups}</a></span></div>
					<div class="col-sm-2"></div>
					<input type="hidden" name="contractVO.contractBackups" value="${contractVO.contractBackups }" />
				</c:if>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="contract"  style="padding:6px 0px;" >
			    	<span>（合同文件请打包上传！）</span>
			    </div>
			  </div>
	    	  <div class="form-group">
			    <label for="uploadFile" class="col-sm-2 control-label"><span class="glyphicon glyphicon-paperclip"></span>员工签名</label>
			    <c:if test="${not empty contractVO.signature}">
			   		<div class="col-sm-10"><span><img style="height:100px;" alt="" src="http://www.zhizaolian.com:9000/contract/${contractVO.contractID}_${contractVO.signature}"></span></div>
			    	<div class="col-sm-2"></div>
			    	<input type="hidden" name="contractVO.signature" value="${contractVO.signature }" />
			    </c:if>
			    <div class="col-sm-3">
			    	<input type="file" id="uploadFile" name="signat"  style="padding:6px 0px;" >
			    </div>
			  </div>
			  <div class="form-group" style="margin-left:6px;">
			    <button type="submit" id="submitButton" class="btn btn-primary" >保存</button>
			    <button type="button" class="btn btn-default" onclick="location.href='javascript:history.go(-1);'" style="margin-left:15px;">返回</button>
			  </div>
			</form>
        </div>
      </div>
    </div>
</body>
</html>