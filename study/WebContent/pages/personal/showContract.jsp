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
              <s:set name="panel" value="'application'"></s:set>
      
        <%@include file="/pages/administration/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">合同签署流程</h3> 
           	<div class="formal_img" style="margin-bottom:-20px;">
               	<img alt="合同签署" src="assets/images/Contract.png"></img>  
            </div>
            <a href="personal/toBeginContractPage" class="btn btn-primary">我要签署</a>
      	</div>
      </div>
    </div>
</body>
</html>