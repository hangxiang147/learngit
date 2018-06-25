<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	function checkMeetCondition(obj){
		var userId = $(obj).data("userid");
		$.ajax({
			url:'personal/checkMeetCondition',
			type:'post',
			data:{'userId':userId},
			success:function(data){
				if(data.result=='0'){
					layer.open({
						content:"要离职，必须先退出合伙人",
						btn:['确认', '取消'],
						offset:'200px',
						yes:function(index){
							layer.close(index);
							window.location.href='personal/gotoExitPartner'; 
			    			Load.Base.LoadingPic.FullScreenShow(null);
						}
					})
				}else if(data.result=='1'){
					layer.alert("您未满足规定的离职条件(转正后，1-6个月内无法在OA系统中提交离职申请)，无在OA系统中提出离职申请。若您执意离职，请到人事部门进行沟通，进行当面离职申请",{offset:'100px'});
				}else if(data.result=='2'){
					layer.alert("您未满足规定的离职条件(仅可在每个月的25号这一天在OA系统中提出离职申请)，无在OA系统中提出离职申请。若您执意离职，请到人事部门进行沟通，进行当面离职申请",{offset:'100px'});
				}
				else{
					window.location.href='personal/applyDimission'; 
	    			Load.Base.LoadingPic.FullScreenShow(null);
				}
			}
		})
	}
</script>
<style type="text/css">
</style>
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/pages/personal/panel.jsp"%>
			<div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
				<h3 class="sub-header" style="margin-top: 0px;">离职审批流程</h3>
				<div class="vacation_img" style="margin-bottom: -80px;">
					<img alt="离职审批流程图" src="assets/images/resignation.png"></img>
				</div>
				<button data-userId="${userId }" class="btn btn-primary" onclick="checkMeetCondition(this)">
					我要离职
				</button>
			</div>
		</div>
	</div>
</body>
</html>