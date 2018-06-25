<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>

<style type="text/css">
	img {
		max-width:400px;
	}
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'process'"></s:set>
        <%@include file="/pages/HR/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
           	<div style="margin-bottom:-80px;" style="width:400px;">
               	<img src="personal/getVacationAttachment?taskID=${param.taskID }" alt="附件图片" id="photo" class= >
            </div>
      	</div>
      </div>
    </div>
    <script type="text/javascript">
    if($("#photo").attr("src")!=""){
    	//alert(1);
	var imgwidth=$("#photo").width()*0.8;
	if (imgwidth>500) {
       $("#photo").css("width", imgwidth);
    }
    }
</script>
</body>
</html>