<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	
</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
      <div class="row">
        <s:set name="panel" value="'application'"></s:set>
        <%@include file="./panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">前往总部车辆预约</h3> 
           	<div class="vacation_img" style="margin-bottom:0px;">
               	<img alt="前往总部车辆预约" src="assets/images/carUse.png"></img>  
            </div>
            <a href="javascript:to_applyPage()" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 申请出差</a>
      	</div>
      </div>
    </div>
    <script>
    // 当前车辆预约 仅仅限于 南通分部 前往 总部的车辆 所以需要判断 人员是否是南通分部的
    var companyId='${staff.companyID}';
   	function to_applyPage(){
   		if(companyId!='3'){
   			layer.alert("当前仅有南通分部可以预约车辆", {offset:'100px'});
   		}else{
   			location.href="administration/process/newCarUse";
   			Load.Base.LoadingPic.FullScreenShow(null);
   		}
   	}
    
    </script>
</body>
</html>