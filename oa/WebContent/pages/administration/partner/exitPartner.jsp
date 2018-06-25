<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$("#affirm").click(function(){
		var exitReason = $("#exitReason").val();
		var userId = $("#affirm").attr("data-userId");
		if(exitReason==''){
			layer.open({
				content:'请填写退出原因',
				btn:['确认', '取消'],
				offset:'100px',
				yes: function(index) {
					layer.close(index);
				}
			});
		}else{
			$.ajax({
				url:'/administration/partner/submitExitReason',
				data:{'exitReason':exitReason,'userId':userId},
				type:'post',
				success:function(data){
					if(data.result=='0'){
						layer.open({
							content:'提交成功',
							btn:['确认', '取消'],
							offset:'100px',
							yes: function(index) {
								layer.close(index);
								window.location.href='/administration/partner/myApplyPartner'; 
		  	    				Load.Base.LoadingPic.FullScreenShow(null);
							}
						});
					}else if(data.result=='1'){
						layer.open({
							content:'已提交过申请，请等待审批结果',
							btn:['确认', '取消'],
							offset:'100px',
							yes: function(index) {
								layer.close(index);
								window.location.href='/administration/partner/myApplyPartner'; 
		  	    				Load.Base.LoadingPic.FullScreenShow(null);
							}
						});
					}
				}
			})
		}
		
	})
})
	

</script>
<style type="text/css">

</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'partnerCenter'"></s:set>
			<%@include file="/pages/administration/panel.jsp" %>	
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top:0px;">申请退出合伙人</h3>
				<form action="" method="post" class="form-horizontal">
					<div class="form-group">
						<label for="exitReason" class="col-sm-2 control-label">
							退出原因
							<span style="color:red;">*</span>
							<p style="color:red;"><span id="text_count">100</span>/100字以内</p>
						</label>
						<div class="col-sm-4">
							<textarea id="exitReason" name="exitParterApplyEntity.exitReason" rows="5" class="form-control"></textarea>
						</div>
						<script type="text/javascript">  
						/*字数限制*/  
						$("#exitReason").on("input propertychange", function() {  
						    var $this = $(this),  
						        _val = $this.val(),  
						        count = "";  
						    if (_val.length > 100) {  
						        $this.val(_val.substring(0, 100));  
						    }  
						    count = 100 - $this.val().length;
						    $("#text_count").text(count);  
						}); 
						</script>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button id="affirm" type="button" class="btn btn-primary" data-userId="${userid }">确认</button>
							<button type="button" class="btn btn-default" style="margin-left:3%" onclick="history.go(-1)">返回</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>