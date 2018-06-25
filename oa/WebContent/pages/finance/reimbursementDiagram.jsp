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
        <s:set name="panel" value="'reimbursement'"></s:set>
        <%@include file="/pages/finance/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <h3 class="sub-header" style="margin-top:0px;">报销审批流程</h3> 
           	<div class="reimbursement_img" style="margin-bottom:0px;">
               	<img alt="报销流程图" src="assets/images/Reimbursement.png"></img>  
            </div>
           	<a onclick="newReimbursement()" class="btn btn-primary"><span class="glyphicon glyphicon-plus"></span> 申请报销</a>
      	</div>
      </div>
    </div>
    <script src="/assets/js/layer/layer.js"></script>
    <script type="text/javascript">
    	function newReimbursement(){
    		var content = '报销需具备以下条件'+
    					  '<br>1）走OA，领导已审批'+
    					  '<br>2）写纸档的付款申请单or报销单，账单明细，若是聚餐的报销单，请参与人签字，至少3人及以上'+
    					  '<br>3）附上便利贴，上面写好需付款时间'+
    					  '<br>注：1）出纳不在位置，以上条件+附上便利贴，From  XX ，并放在相应的纸盒里面'+
    					  		'<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2）报销从7.17日起，产生的费用必须在1周内申请完交于财务室';
    		  layer.alert(content, {
   			  btn: ['明白了'],
   			  offset : ['100px'],
   			  area : ['500px' , 'auto'],
   			  title:'提示',
   		      yes: function(index){
   		      window.location.href = "finance/process/newReimbursement";
   		      layer.close(index);
    		  Load.Base.LoadingPic.FullScreenShow(null);
   		   	  }
    		});
    	}
    </script>
</body>
</html>