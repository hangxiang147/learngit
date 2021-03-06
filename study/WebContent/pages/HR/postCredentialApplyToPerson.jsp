<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">

</script>
<style type="text/css">
	
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<s:set name="panel" value="'dangan'"></s:set>
			<%@include file="/pages/HR/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<form action="HR/staff/addCredentialEntity" method="post" class="form-horizontal">
					<s:token></s:token>
					<h3 class="sub-header" style="margin-top: 0px;">发起审核岗位资格证书</h3>
					<div class="form-group">
						<label for="askUserId" class="col-sm-1 control-label">
							被审核人
							<span style="color:red;">*</span>
						</label>
					    <div class="col-sm-4" id="content">
					    	<input type="hidden" class="form-control" id="askUserId" name="credentialEntity.offerUserId" required="required">
					    </div>
					</div>
					<div class="form-group">
						<label for="explain" class="col-sm-1 control-label">
							说明
							<span style="color:red;">*</span>
							<p style="color:red;"><span id="text_count">50</span>/50字</p>
						</label>
			        	<div class="col-sm-4">
			           		<textarea name="credentialEntity.applyExplain" rows="3" class="form-control" id="explain" required="required"></textarea>
			        	</div>
			        	<script type="text/javascript">  
						/*字数限制*/  
						$("#explain").on("input propertychange", function() {  
						    var $this = $(this),  
						        _val = $this.val(),
						        count = "";  
						    if (_val.length > 50) {  
						        $this.val(_val.substring(0, 50));  
						    }  
						    count = 50 - $this.val().length;
						    $("#text_count").text(count);
						}); 
						</script>
					</div>
					<div class="form-group">
				    	<label for="formButton" class="col-sm-1 control-label"></label>
				    	<div class="col-sm-2">
				    		<button type="submit" id="formButton" class="btn btn-primary">确定</button>
				    		<button type="button" onclick="history.go(-1)" class="btn btn-default" style="margin-left:10%;">返回</button>
				    	</div>
			        </div>
				</form>
				

            
            
<script src="/assets/js/require/require2.js"></script>
<script type="text/html" id="demoTr">
	<td width="250px">
		<input type="text" required="required" id="proposer" name="chooseInput" class="form-control assignTaskUserID" style="width:150px" onkeyup="checkEmpty(this)" onblur="myFunction()"/>			    	
		<input type="hidden" name="workReportVO.assignTaskUserID" />		    	
	</td>
</script>
<script>
	require(['staffComplete','jquery'],function (staffComplete,$){
		var demoTr=$('#demoTr').html();
		var $item=$(demoTr);
		$('#content').append($item)
		$item.find("a:last").click(function (){
			$item.remove();
		});
		new staffComplete().render($item.find("input[name=\"chooseInput\"]"),function ($input){
			$item.find("input[name='workReportVO.assignTaskUserID']").val($input.data("userId"));
			$("#askUserId").val($input.data("userId"));
		});
	})
	function checkEmpty(target){
  		if($(target).val()==''){
  			$(target).next().val('');
  			$("#askUserId").val('');
  		}
  	}
	function myFunction(){
		if($("#askUserId").val()==''){
  			$("#proposer").val('');
  		}
	}
</script>
	
	
	
	
			</div>
		</div>
	</div>
</body>
</html>