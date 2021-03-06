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
       	  <h3 class="sub-header" style="margin-top:0px;">公章申请流程</h3> 
           	<div class="formal_img">
               	<img alt="公章申请" src="assets/images/ChopBorrow.png"></img>  
            </div>
            <div><a onclick="newChopBorrow()" class="btn btn-primary">我要申请</a></div>
      	</div>
      </div>
    </div>
    <script src="/assets/js/layer/layer.js"></script>
    <script type="text/javascript">
    	function newChopBorrow(){
    		var content = '印章申请需具备以下条件'+
    					  '<br>1.待盖章 '+
    					  '<br>1）走OA，领导已审批'+
    					  '<br>2）附上便利贴，盖什么章'+
    					  '<br>2.带章走'+
    					  '<br>1）走OA，领导已审批'+
    					  '<br>2）附上便利贴，需要带走哪些'+
    					  '<br>3）在纸档申请本上签字，归还也签字（注明归还时间）'+
    					  '<br>注：印章管理人不在位置，以上条件+附上便利贴，From  XX ，并放在相应的纸盒里面';
    		  layer.alert(content, {
   			  btn: ['明白了'],
   			  offset : ['100px'],
   			  area : ['500px' , 'auto'],
   			  title:'提示',
   		      yes: function(index){
   		      layer.close(index);
   		      window.location.href = "personal/newChopBorrow";
   		   	  Load.Base.LoadingPic.FullScreenShow(null);
   		   	  }
    		});
    	}
    </script>
</body>
</html>