<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>

<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">

	 function showAlert(message) {
		var html = "<div id=\"danger-alert\" class=\"alert alert-danger alert-dismissible fade in float_div\" role=\"alert\">"
				+"<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>"
				+"<span id=\"danger-message\">"+message+"</span>"
				+"</div>";
		$(".container-fluid").before(html);
	}
	
	function upload() {
// 		if ($("#company").val().trim()  == '') {
// 			showAlert("请选择所在分部！");
// 			return;
// 		}
		if ($("#uploadFile").val().trim()  == '') {
			showAlert("请选择上传文件！");
			return;
		}
		if(!$('input[name="year"]').val()){
			showAlert("请选择对应年份！");
			return;
		}
		if(!$('input[name="month"]').val()){
			showAlert("请选择对应年份！");
			return;
		}
		$("#formButton").addClass("disabled");
		var formData = new FormData($("#uploadForm")[0]);
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
			url:'attendance/save_salaryContentUpload',
			type:'post',
			data:formData,
			contentType:false,
			processData:false,
			success:function(data) {
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					showAlert(data.errorMessage);
					return;
				}
				layer.alert("上传成功！",{offset:'100px'},function (){
					location.reload();
				});
			},
			complete:function(){
				Load.Base.LoadingPic.FullScreenHide();
			}
		});
	}
	
	function enableSubmitButton() {
		$("#formButton").removeClass("disabled");
	}
</script>
 <script>
 	$(function (){
 		 $('#confirm').click(function (){
 			 Load.Base.LoadingPic.FullScreenShow(null);
 			 $.ajax({
 				 url:'/attendance/toSendSalary',
 				 data:$('#uploadForm').serialize(),
 				 type:'post',
 				 success:function(){
 					 layer.alert("后台正在推送数据,请耐心等待,可以在工资单查看页面查看推送情况。",{offset:'100px'},function (index){
 		 				 layer.close(index);
 		 			 })
 				 },
 				 complete:function(){
 					Load.Base.LoadingPic.FullScreenHide();
 				 }
 			 })
 		 });
 	})
	
	function send_(){
		var title=$('select[name="sendType"]').find("option:selected").html().trim();
		Load.Base.LoadingPic.FullScreenShow(null);
		$.ajax({
  			url:'/attendance/salaryDetail',
  			data:$('#uploadForm').serialize(),
  			type:'post',
  			dataType:'json',
  			success:function(data){
  				$("#exampleModalLabel").html(title+"推送详情");
  		  		var content=["oa人员总数:"+data[0],"该月已经录入工资条:"+data[1],"该月"+title+"已经推送条数:"+data[2]];	  
  		  		$("#ntcContent").empty().html(content.join("</br>"));
  		    	$("#cheakModal").modal('show');
  			},
  			complete:function(){
  				Load.Base.LoadingPic.FullScreenHide();
  			}
  		})
  		
  	}
  
  </script>
<style type="text/css">
	.col-sm-1 {
		padding-right: 0px;
		padding-left: 0px;
	}
	
	.float_div{
		position:fixed;
		width:380px;
		right:0px;
		top:70px;
		z-index:100; 
	}
</style>
</head>
  <body>
    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/attendance/salayPanel.jsp" %>		
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<form id="uploadForm" class="form-horizontal"  >
              <s:token></s:token>
        	  <h3 class="sub-header" style="margin-top:0px;">工资单数据推送</h3>
			  
			  <div class="form-group">
				<label for="category" class="col-sm-1 control-label">年份&nbsp;</label>
				    
				     <div class="col-sm-2">
				    	<input name="year"  class="form-control"  onclick="WdatePicker({dateFmt:'yyyy'})" value="${year}"/>
				    </div>
				    			<label for="category" class="col-sm-1 control-label">月份&nbsp;</label>
				    
				     <div class="col-sm-2">
				    	<input name="month"  class="form-control"  onclick="WdatePicker({dateFmt:'MM'})" value="${month}"/>
				    </div>
			  </div>
			  <div class="form-group">
			  <label for="category" class="col-sm-1 control-label">推送方式&nbsp;</label>
			  	 <div class="col-sm-2">
			  	 	<select class="form-control" name="sendType">
			  	 		<option value="1">
			  	 			短信
			  	 		</option>
			  	 		<option value="2">
			  	 			邮箱
			  	 		</option>
			  	 	</select>
			  	 </div>
			  <label for="formButton" class="col-sm-1 control-label"></label>
		    	<div class="col-sm-3 col-md-2 col-sm-offset-1  col-md-offset-1">
		    		<input type="button" value="推送" class="btn btn-primary" onclick="send_();"/>
		    	</div>
			  
			  </div>
			</form>
        </div>
      </div>
    </div>
     <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="exampleModalLabel"></h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="hidden" id="groupDetailID" />
	      	<p id="ntcContent"></p>
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary"  data-dismiss="modal" id="confirm" >确定</button>
	      <button type="button" class="btn btn-primary" data-dismiss="modal">取消</button>
	      </div>
	    </div>
	  </div>
  </div>
  </body>
 
 
</html>
