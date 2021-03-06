<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<link href="assets/bootstrap-star/css/star-rating.css" rel="stylesheet">
<script src="assets/bootstrap-star/js/star-rating.js" type="text/javascript"></script>
<style>
.badge{
	padding:3px 5px
}
.icon {
   width: 1.5em; height: 1.5em;
   vertical-align: -0.15em;
   fill: currentColor;
   overflow: hidden;
}
#tab{
	border-collapse:collapse;
	width:100%;
	margin-top:5px;
}
#tab tr td{word-wrap:break-word;word-break:break-all;font-size:10px;padding:8px 7px;text-align:center;border:1px solid #ddd}
.table td{word-wrap:break-word;word-break:break-all;}
.nav-tabs>li{
	margin-bottom:-3px;
}
</style>
<script src="/assets/icon/iconfont.js"></script>
<script src="/assets/js/layer/layer.js"></script>
<script type="text/javascript">
	$(function() {
		set_href();
		$("[data-toggle='tooltip']").tooltip();
		var type = $("#type").val();
		var tabShowLink={
				1:0,
				2:1,
				3:3,
				4:4,
				5:2,
				6:5,
				8:6,
				10:7,
				11:8,
				12:13,
				13:14,
				14:17,
				15:18,
				16:19,
				17:20,
				18:11,
				19:24,
				20:12,
				21:15,
				22:16,
				23:9,
				24:21,
				25:10,
				26:22,
				27:23,
				28:25,
				29:26,
				30:27,
				33:28,
				34:29,
				35:30,
				37:31,
				38:32,
				39:33,
				40:34,
				42:35,
				43:36,
				44:37,
				45:38,
				46:39,
				47:40,
				48:41,
				49:42,
				50:43,
				51:44,
				52:45
		};
		for(key in tabShowLink){
			if(type== key){
				$("#myTab li:eq("+tabShowLink[key]+") a").tab("show");
			}
			$("#myTab li:eq("+tabShowLink[key]+") a").attr("index",key).click(function(e) {
				e.preventDefault();
				window.location.href = "personal/findTaskList?type="+$(this).attr("index");
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		} 
		if("${coursePlanId}"){
			location.href="train/generateSignInTable?coursePlanId=${coursePlanId}";
		}
	});
	
	//组合活动：添加子活动
	$(document).on("click",".resumeadd",function(){
		$(".resume").last().after($(".resume").eq(0).clone());
		$(".resume").last().find("input").val("");
		$(".resume").last().find("textarea").val("");
	});
	$(document).on("click",".resumedel",function(){
		if(confirm("确认删除吗？")){
			if($(".resume").length==1){
				alert("最后一个工作经验不可删除！");
			}else{
				$(this).parent().remove();		
			}
		}	
	});

	function checkNum(obj) {
		$(obj).val($(obj).val().replace(/[^0-9+\.]+/,'').replace(/\b(0+)/gi,""));
	}
	function signIn(coursePlanId, taskId){
		$('.all').prop("checked",false);
		$("input[name='coursePlanId']").val(coursePlanId);
		$("input[name='signIntaskId']").val(taskId);
		$.ajax({
			url:'train/getAllJoinerByCourseId',
			data:{'coursePlanId':coursePlanId},
			success:function(data){
				$("#joinerList").html("");
				var html = "";
				$.each(data.joiners, function(index, value){
					html += "<tr><td><input type='checkbox' class='check' name='joinerUserId' value='"+value.userID+"'></td><td>"+(index+1)+"</td><td>"+value.departmentName+"</td><td>"+value.lastName+"</td></tr>";
				});
				$("#joinerList").html(html);
				//checkbox单击事件
			    $(".check").on("click",function(){
			        fullCkOrNot();
			    });
			}
		});
	}
	function confirmSignIn(){
		if($("input[name='joinerUserId']:checked").length<1){
			layer.alert("请勾选参加的学员",{offset:'100px'});
			return false;
		}
		$("#signIn .close").click();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function giveScore(coursePlanId, taskId){
		//去除星级评价的清空按钮
		$(".clear-rating").remove();
		$.ajax({
			url:'train/getCoursePlanLecturer',
			data:{'coursePlanId':coursePlanId},
			success:function(data){
				$("#lecturer").text(data.lecturer.staffName);
				$("input[name='lecturerId']").val(data.lecturer.userID);
			}
		});
		$("input[name='coursePlanId']").val(coursePlanId);
		$("input[name='commentTaskId']").val(taskId);
	}
	function completeComment(flag){
		var starValue = $("#commentForLecturer").val();
		starValue = (parseFloat(starValue)/5)*100;
		location.href="train/completeComment?coursePlanId="+$("input[name='coursePlanId']").val()
				+"&taskId="+$("input[name='commentTaskId']").val()+"&starValue="+starValue+"&flag="+flag;
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function testOrNot(coursePlanId, taskId){
		$("#uploadTestsBtn").click();
		layer.confirm("是否需要对学员进行测试",{btn: ['是', '否'],offset:'100px'},function(index){
			//location.href="train/testOrNot?taskId="+taskId+"&test=1";
			$("input[name='coursePlanId']").val(coursePlanId);
			$("input[name='uploadTestsTaskId']").val(taskId);
			$("#uploadTestSubmit").removeAttr("disabled");
			layer.close(index);
		},function(index){
			layer.close(index);
			$("#uploadTests .close").click();
			location.href="train/testOrNot?taskId="+taskId+"&test=2";
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	}
	/* function uploadTests(coursePlanId, taskId){
		$("input[name='coursePlanId']").val(coursePlanId);
		$("input[name='uploadTestsTaskId']").val(taskId);
	} */
	function addLoadingForTest(){
		$("#uploadTests .close").click();
		Load.Base.LoadingPic.FullScreenShow(null);
	}
	function noMatch(processInstanceId, taskId){
		layer.alert("<form class='form-horizontal'><div class='form-group'><label class='col-sm-3 control-label'>原因"+
		    "<span style='color:red'> *<span></label><div class='col-sm-8'><textarea class='form-control' rows='4' id='noMatchComment'></textarea></div></div></form>", {area:'350px',offset:'100px'},function(index){
	    	var comment = $("#noMatchComment").val();
			if(!comment.trim()){
				layer.alert("原因不能为空",{type:1,offset:'150px',area:['200px','130px']});
				return false;
			}
		    layer.close(index);
			location.href="administration/public/noMatchPublicEvent?processInstanceId="+processInstanceId+"&taskId="+taskId+"&comment="+comment;
			Load.Base.LoadingPic.FullScreenShow(null);
		});
	}
	function noHanle(processInstanceId, taskId){
		layer.alert("<form class='form-horizontal'><div class='form-group'><label class='col-sm-3 control-label'>原因"+
			    "<span style='color:red'> *<span></label><div class='col-sm-8'><textarea class='form-control' rows='4' id='noHandleComment'></textarea></div></div></form>", {area:'350px',offset:'100px'},function(index){
		    	var comment = $("#noHandleComment").val();
				if(!comment.trim()){
					layer.alert("原因不能为空",{type:1,offset:'150px',area:['200px','130px']});
					return false;
				}
			    layer.close(index);
				location.href="administration/public/noHandlePublicEvent?processInstanceId="+processInstanceId+"&taskId="+taskId+"&comment="+comment;
				Load.Base.LoadingPic.FullScreenShow(null);
			});
	}
	function showVacationUsers(users){
		users = users.substring(1, users.length-1);
		layer.alert(users, {title:'请假人员', offset:'100px'});
	}
	function inquireInfo(obj){
		var exitReason = $(obj).parent().attr("data-exitReason");
   		layer.alert(exitReason,{offset:['100px']})
	}
	function referApprovalOpinion(obje){
		var userId = $(obje).attr("data-userId");
		var approvalOpinion = $(obje).attr("data-approvalOpinion");
		var processInstanceId = $(obje).attr("data-processInstanceId");
		var id = $(obje).attr("data-id");
		layer.open({
			title:'审批意见',
			content:'<textarea id="approvalOpinion'+processInstanceId+'" rows="5" style="width: 460px;height: 170px;">'
						+approvalOpinion+
					'</textarea>',
			btn:['确认', '取消'],
			offset:'100px',
			area:['500px','300px'],
			yes: function(index) {
     			layer.close(index);
     			var approvalOpinionComment = $("textarea").val();
     			
     			$.ajax({
     				url:"administration/partner/updateApprovalOpinion",
     				type:"post",
     				data:{'userId':userId,'approvalOpinionComment':approvalOpinionComment,'processInstanceId':processInstanceId},
     				success:function(data){
     					window.location.href='/personal/findTaskList?type=47'; 
  	    				Load.Base.LoadingPic.FullScreenShow(null);
     				}
     			});
     		}
		})
	}
	function agreeExit(objec){
		var userId = $(objec).attr("data-userId");
		var taskId = $(objec).attr("data-taskId");
		
		var processInstanceId = $(objec).attr("data-processInstanceId");
		var auditStatus = $(objec).attr("data-auditStatus");
		
		var exitPartnerCount = $(objec).attr("data-exitPartnerCount");
		
		layer.open({
			title:'审批意见',
			content:'<div><p style="color:red;">请内容在100字以内</p>'+
					'<textarea maxlength="100" id="approvalOpinion'+processInstanceId+'" rows="4" style="width: 360px;height: 100px;">'+
					'</textarea></div>',
			btn:['确认','取消'],
			offset:'100px',
			area:['400px','270px'],
			yes: function(index){
				layer.close(index);
				var approvalOpinionComment = $("textarea").val();
				$.ajax({
     				url:"administration/partner/checkApply",
     				type:"post",
     				data:{'userId':userId,'taskId':taskId,'approvalOpinionComment':approvalOpinionComment,'processInstanceId':processInstanceId,'auditStatus':auditStatus},
     				success:function(data){
     					if(parseInt(exitPartnerCount)==1){
     						window.location.href='/personal/findTaskList?type=1'; 
      	    				Load.Base.LoadingPic.FullScreenShow(null);
     					}else if(parseInt(exitPartnerCount)>1){
     						window.location.href='/personal/findTaskList?type=47'; 
      	    				Load.Base.LoadingPic.FullScreenShow(null);
     					}
     				}
     			});
			}	
			
		})
		
	}
	function disagreeExit(operate){
		agreeExit(operate);
	}
	
	
</script>
<script src="/assets/js/require/require2.js"></script>
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" /> 
</head>
  <body>

    <div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
       	  <input type="hidden" id="type" value="${type }" />
       	  <ul class="nav nav-tabs" role="tablist" id="myTab" style="font-size:10px;">
			  <li role="presentation"><a href="#vacation" role="tab" data-toggle="tab">请假申请<span class="badge" style="background-color:red;<c:if test='${vacationCount == 0 }'>display:none;</c:if>">${vacationCount }</span></a></li>
			  <li role="presentation"><a href="#assignment" role="tab" data-toggle="tab">任务分配<span class="badge" style="background-color:red;<c:if test='${assignmentCount == 0 }'>display:none;</c:if>">${assignmentCount }</span></a></li>
			  <li role="presentation"><a href="#reimbursement" role="tab" data-toggle="tab">报销申请<span class="badge" style="background-color:red;<c:if test='${reimbursementCount == 0 }'>display:none;</c:if>">${reimbursementCount }</span></a></li>
			  <li role="presentation"><a href="#resignation" role="tab" data-toggle="tab">离职申请<span class="badge" style="background-color:red;<c:if test='${resignationCount == 0 }'>display:none;</c:if>">${resignationCount }</span></a></li>
			  <li role="presentation"><a href="#formal" role="tab" data-toggle="tab">转正申请<span class="badge" style="background-color:red;<c:if test='${formalCount == 0 }'>display:none;</c:if>">${formalCount }</span></a></li>
			  <li role="presentation" <c:if test='${emailCount == 0 }'>style="display:none"</c:if> ><a href="#email" role="tab" data-toggle="tab">邮箱申请<span class="badge" style="background-color:red;<c:if test='${emailCount == 0 }'>display:none;</c:if>">${emailCount }</span></a></li>
		  	  <li role="presentation" <c:if test='${auditCount == 0 }'>style="display:none"</c:if>><a href="#audit" role="tab" data-toggle="tab">背景调查<span class="badge" style="background-color:red;<c:if test='${auditCount == 0 }'>display:none;</c:if>">${auditCount }</span></a></li>			
		  	  <li role="presentation" <c:if test='${socialSecurityCount == 0 }'>style="display:none"</c:if>><a href="#socialSecurity" role="tab" data-toggle="tab">社保审核<span class="badge" style="background-color:red;<c:if test='${socialSecurityCount == 0 }'>display:none;</c:if>">${socialSecurityCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${chopBorrowCount == 0 }'>style="display:none"</c:if>><a href="#chopBorrow" role="tab" data-toggle="tab">公章申请<span class="badge" style="background-color:red;<c:if test='${chopBorrowCount == 0 }'>display:none;</c:if>">${chopBorrowCount }</span></a></li>
	 	   	  <li role="presentation" <c:if test='${chopDestroyCount == 0 }'>style="display:none"</c:if>><a href="#chopDestroy" role="tab" data-toggle="tab">印章缴销<span class="badge" style="background-color:red;<c:if test='${chopDestroyCount == 0 }'>display:none;</c:if>">${chopDestroyCount }</span></a></li>
	 	   	  <li role="presentation" <c:if test='${carveChopCount == 0 }'>style="display:none"</c:if>><a href="#carveChop" role="tab" data-toggle="tab">印章刻制<span class="badge" style="background-color:red;<c:if test='${carveChopCount == 0 }'>display:none;</c:if>">${carveChopCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${certificateBorrowCount == 0 }'>style="display:none"</c:if>><a href="#certificateBorrow" role="tab" data-toggle="tab">证件申请<span class="badge" style="background-color:red;<c:if test='${certificateBorrowCount == 0 }'>display:none;</c:if>">${certificateBorrowCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${contractBorrowCount == 0 }'>style="display:none"</c:if>><a href="#contractBorrow" role="tab" data-toggle="tab">合同借阅<span class="badge" style="background-color:red;<c:if test='${contractBorrowCount == 0 }'>display:none;</c:if>">${contractBorrowCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${idBorrowCount == 0 }'>style="display:none"</c:if> ><a href="#idBorrow" role="tab" data-toggle="tab">身份证借用<span class="badge" style="background-color:red;<c:if test='${idBorrowCount == 0 }'>display:none;</c:if>">${idBorrowCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${contractCount == 0 }'>style="display:none"</c:if> ><a href="#contract" role="tab" data-toggle="tab">合同签署<span class="badge" style="background-color:red;<c:if test='${contractCount == 0 }'>display:none;</c:if>">${contractCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${changeContractCount == 0 }'>style="display:none"</c:if> ><a href="#changeContract" role="tab" data-toggle="tab">合同变更或解除<span class="badge" style="background-color:red;<c:if test='${changeContractCount == 0 }'>display:none;</c:if>">${changeContractCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${changeBankAccountCount == 0 }'>style="display:none"</c:if> ><a href="#changeBankAccount" role="tab" data-toggle="tab">开设、变更及撤销银行账户<span class="badge" style="background-color:red;<c:if test='${changeBankAccountCount == 0 }'>display:none;</c:if>">${changeBankAccountCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${carUseCount == 0 }'>style="display:none"</c:if>><a href="#carUse" role="tab" data-toggle="tab">车辆预约<span class="badge" style="background-color:red;<c:if test='${carUseCount == 0 }'>display:none;</c:if>">${carUseCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${advanceCount == 0 }'>style="display:none"</c:if>><a href="#advance" role="tab" data-toggle="tab">预约付款<span class="badge" style="background-color:red;<c:if test='${advanceCount == 0 }'>display:none;</c:if>">${advanceCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${vitaeCount == 0 }'>style="display:none"</c:if>><a href="#vitae" role="tab" data-toggle="tab">招聘需求审核<span class="badge" style="background-color:red;<c:if test='${vitaeCount == 0 }'>display:none;</c:if>">${vitaeCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${softPerformanceCount == 0 }'>style="display:none"</c:if>><a href="#softPerformance" role="tab" data-toggle="tab">需求单<span class="badge" style="background-color:red;<c:if test='${softPerformanceCount == 0 }'>display:none;</c:if>">${softPerformanceCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${purchasePropertyCount == 0 }'>style="display:none"</c:if>><a href="#purchaseProperty" role="tab" data-toggle="tab">财产购置<span class="badge" style="background-color:red;<c:if test='${purchasePropertyCount == 0 }'>display:none;</c:if>">${purchasePropertyCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${handlePropertyCount == 0 }'>style="display:none"</c:if>><a href="#handleProperty" role="tab" data-toggle="tab">资产处置<span class="badge" style="background-color:red;<c:if test='${handlePropertyCount == 0 }'>display:none;</c:if>">${handlePropertyCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${transferPropertyCount == 0 }'>style="display:none"</c:if>><a href="#transferProperty" role="tab" data-toggle="tab">资产调拨<span class="badge" style="background-color:red;<c:if test='${transferPropertyCount == 0 }'>display:none;</c:if>">${transferPropertyCount }</span></a></li>
		 	  <li role="presentation" <c:if test='${commonSubjectCount == 0 }'>style="display:none"</c:if>><a href="#commonSubject" role="tab" data-toggle="tab">通用流程<span class="badge" style="background-color:red;<c:if test='${commonSubjectCount == 0 }'>display:none;</c:if>">${commonSubjectCount }</span></a></li>
		  	  <li role="presentation" <c:if test='${shopApplyCount == 0 }'>style="display:none"</c:if>><a href="#shopApply" role="tab" data-toggle="tab">店铺申请<span class="badge" style="background-color:red;<c:if test='${shopApplyCount == 0 }'>display:none;</c:if>">${shopApplyCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${shopPayApplyCount == 0 }'>style="display:none"</c:if>><a href="#shopPayApply" role="tab" data-toggle="tab">店铺付费申请<span class="badge" style="background-color:red;<c:if test='${shopPayApplyCount == 0 }'>display:none;</c:if>">${shopPayApplyCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${workOvertimeCount == 0 }'>style="display:none"</c:if>><a href="#workOvertime" role="tab" data-toggle="tab">加班申请<span class="badge" style="background-color:red;<c:if test='${workOvertimeCount == 0 }'>display:none;</c:if>">${workOvertimeCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${trainCount == 0 }'>style="display:none"</c:if>><a href="#classHour" role="tab" data-toggle="tab">培训<span class="badge" style="background-color:red;<c:if test='${trainCount == 0 }'>display:none;</c:if>">${trainCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${problemOrderCount == 0 }'>style="display:none"</c:if>><a href="#problemOrder" role="tab" data-toggle="tab">问题单<span class="badge" style="background-color:red;<c:if test='${problemOrderCount == 0 }'>display:none;</c:if>">${problemOrderCount }</span></a></li>
		  	  <li role="presentation" <c:if test='${paymentCount == 0 }'>style="display:none"</c:if>><a href="#payment" role="tab" data-toggle="tab">付款申请<span class="badge" style="background-color:red;<c:if test='${paymentCount == 0}'>display:none;</c:if>">${paymentCount}</span></a></li> 
		  	  <li role="presentation" <c:if test='${morningMeetingCount == 0 }'>style="display:none"</c:if>><a href="#morningMeetingReport" role="tab" data-toggle="tab">周早会汇报<span class="badge" style="background-color:red;<c:if test='${morningMeetingCount == 0}'>display:none;</c:if>">${morningMeetingCount}</span></a></li> 
		  	  <li role="presentation" <c:if test='${projectCount == 0 }'>style="display:none"</c:if>><a href="#project" role="tab" data-toggle="tab">项目<span class="badge" style="background-color:red;<c:if test='${projectCount == 0}'>display:none;</c:if>">${projectCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${brandAuthCount == 0 }'>style="display:none"</c:if>><a href="#brandAuth" role="tab" data-toggle="tab">品牌授权<span class="badge" style="background-color:red;<c:if test='${brandAuthCount == 0}'>display:none;</c:if>">${brandAuthCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${publicEventCount == 0 }'>style="display:none"</c:if>><a href="#publicEvent" role="tab" data-toggle="tab">公关申请<span class="badge" style="background-color:red;<c:if test='${publicEventCount == 0}'>display:none;</c:if>">${publicEventCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${performanceCount == 0 }'>style="display:none"</c:if>><a href="#performance" role="tab" data-toggle="tab">岗位绩效<span class="badge" style="background-color:red;<c:if test='${performanceCount == 0}'>display:none;</c:if>">${performanceCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${performanceTargetTaskCount == 0 }'>style="display:none"</c:if>><a href="#performanceTarget" role="tab" data-toggle="tab">考核指标<span class="badge" style="background-color:red;<c:if test='${performanceTargetTaskCount == 0}'>display:none;</c:if>">${performanceTargetTaskCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${performanceActualTaskCount == null || performanceActualTaskCount == 0 }'>style="display:none"</c:if>><a href="#performanceActual" role="tab" data-toggle="tab">实际完成<span class="badge" style="background-color:red;<c:if test='${performanceActualTaskCount == 0}'>display:none;</c:if>">${performanceActualTaskCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${personalPerformanceCount == 0 }'>style="display:none"</c:if>><a href="#personalPerformance" role="tab" data-toggle="tab">个人绩效<span class="badge" style="background-color:red;<c:if test='${personalPerformanceCount == 0}'>display:none;</c:if>">${personalPerformanceCount}</span></a></li>
		      <li role="presentation" <c:if test='${credentialApplyCount == 0 }'>style="display:none"</c:if>>
		      	<a href="#credentialApply" role="tab" data-toggle="tab">
		      		证书审核
		      		<span class="badge" style="background-color:red;<c:if test='${credentialApplyCount == 0}'>display:none;</c:if>">
		      			${credentialApplyCount}
		      		</span>
		      	</a>
		      </li>
		      
		      <li role="presentation" <c:if test='${exitPartnerCount == 0 }'>style="display:none"</c:if>>
		      	<a href="#checkExitPartner" role="tab" data-toggle="tab">
		      		退出合伙人
		      		<span class="badge" style="background-color:red;<c:if test='${exitPartnerCount == 0}'>display:none;</c:if>">
		      			${exitPartnerCount}
		      		</span>
		      	</a>
		      </li>

		      

		      <li role="presentation" <c:if test='${alterSalaryCount == 0 }'>style="display:none"</c:if>><a href="#alterSalary" role="tab" data-toggle="tab">薪资调整<span class="badge" style="background-color:red;<c:if test='${alterSalaryCount == 0}'>display:none;</c:if>">${alterSalaryCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${changeSalaryDetailCount == 0 }'>style="display:none"</c:if>><a href="#changeSalaryDetail" role="tab" data-toggle="tab">更改工资单<span class="badge" style="background-color:red;<c:if test='${changeSalaryDetailCount == 0}'>display:none;</c:if>">${changeSalaryDetailCount}</span></a></li>
		  	  <li role="presentation" <c:if test='${paySalaryCount == 0 }'>style="display:none"</c:if>><a href="#paySalary" role="tab" data-toggle="tab">发放工资<span class="badge" style="background-color:red;<c:if test='${paySalaryCount == 0}'>display:none;</c:if>">${paySalaryCount}</span></a></li>
		 	  <li role="presentation" <c:if test='${rewardAndPunishmentCount == 0 }'>style="display:none"</c:if>><a href="#rewardAndPunishment" role="tab" data-toggle="tab">行政奖惩<span class="badge" style="background-color:red;<c:if test='${rewardAndPunishmentCount == 0}'>display:none;</c:if>">${rewardAndPunishmentCount}</span></a></li>

		      <li role="presentation" <c:if test='${carMaintainApplyCount == 0 }'>style="display:none"</c:if>>
		      	<a href="#carMaintainApply" role="tab" data-toggle="tab">
		      		车辆维修保养申请
		      		<span class="badge" style="background-color:red;<c:if test='${carMaintainApplyCount == 0}'>display:none;</c:if>">
		      			${carMaintainApplyCount}
		      		</span>
		      	</a>
		      </li>
		  </ul>
       	  <div class="tab-content">
			  <div role="tabpanel" class="tab-pane" id="vacation">
			  	<div class="table-responsive" style="margin-top:30px;">
            		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th style="width:10%">请假人/部门</th>
		                  <th>岗位</th>
		                  <th>开始时间</th>
		                  <th>结束时间</th>
		                  <th>休假天数</th>
		                  <th>工作代理人</th>
		                  <th>请假类型</th>
		                  <th>事由</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.vacationUserName" /></td>
		              			<td class="col-sm-2"><s:iterator id="group" value="#taskVO.groupList" status="st"><s:if test="#st.index != 0 "><br></s:if><s:property value="#group"/></s:iterator></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.beginDate" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.endDate" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.vacationTime" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.agentName" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.vacationType" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.reason" /></td>
		              			<td class="col-sm-2">
		              			<s:if test="#taskVO.type=='部门'">
			              				<a href="javascript:void(0)" onclick="showVacationUsers('${taskVO.staffNames}')">
	           		             			<svg class="icon" aria-hidden="true" title="查看请假人员" data-toggle="tooltip">
												<use xlink:href="#icon-Detailedinquiry"></use>
											</svg>
		           		             	</a>
			              			&nbsp;
		              			</s:if>
		              			<a onclick="goPath('personal/taskComplete?taskID=<s:property value='#taskVO.taskID' />&result=1&businessType=请假申请')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="同意" data-toggle="tooltip">
	             						<use xlink:href="#icon-wancheng"></use>
	             					</svg>
		              			</a>
		              			&nbsp;
		              			<a onclick="goPath('personal/taskComplete?taskID=<s:property value='#taskVO.taskID' />&result=2&businessType=请假申请')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="不同意" data-toggle="tooltip">
	             						<use xlink:href="#icon-butongyi"></use>
	             					</svg>
		              			</a>
<!-- 暂时屏蔽 该功能   原因: 领导可能有多个职务 无法判断领导department位置 -->
<%-- 		              								 <s:if test="#taskVO.defKey == 'vacation_supervisor_audit'"> --%>
<%-- 		              								 <br/><a href="personal/taskComplete?taskID=<s:property value='#taskVO.taskID' />&result=3&businessType=请假申请">同意(但仍需上级主管审批)</a> --%>
<%-- 		              								 </s:if> --%>
		              			<s:if test="#taskVO.attachmentSize>0">&nbsp;<a onclick="goPath('/personal/showVacationAttachment?taskID=<s:property value='#taskVO.taskID' />&selectedPanel=findTaskList')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="查看附件" data-toggle="tooltip">
	             						<use xlink:href="#icon-fujian"></use>
	             					</svg>
		              			</a>
		              			</s:if>
		              			&nbsp;
		              			<a onclick="goPath('personal/processHistory?processInstanceID=<s:property value='#taskVO.processInstanceID'/>&selectedPanel=findTaskList')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="查看已审批环节" data-toggle="tooltip">
	             						<use xlink:href="#icon-liucheng"></use>
	             					</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           		    </table>
          		</div> 
          		<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="assignment">
			  	<div class="table-responsive" style="margin-top:30px;">
            		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-3">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-2">
		              			<a onclick="goPath('personal/perform?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="办理" data-toggle="tooltip">
	             						<use xlink:href="#icon-banli"></use>
	             					</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           		    </table>
          		</div> 
          		<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="reimbursement">
			  
			<form  class="form-horizontal" action="/personal/findTaskList?" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
          	<input style="display:none" name="type" value="5"/>
			<div class="form-group" style="margin-top:10px">
				<label for="beginDate" class="col-sm-1 control-label">报销单号</label>
				<div class="col-sm-2">
			    	<input type="text" class="form-control" id="No_code" name="No_code" value="${No_code}"  placeholder="报销编号">
			    </div>
			    <label class="col-sm-1 control-label">发起人</label>
				<div class="col-sm-2">
			    	<input type="text" class="form-control" name="applyerName" value="${applyerName}" onkeyup="checkEmpty()" autoComplete="off" id="applyer">
			    	<input name="applyerId" type="hidden" value="${applyerId}"/>
			    </div>
			    <label for="beginDate" class="col-sm-1 control-label">发起时间</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="startTime" value="${startTime}" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endTime" value="${endTime }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			    <div class="col-sm-1">
			    <button type="submit"  class="btn btn-primary"  >查询</button>
			    </div>
			</div>
          </form>
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>报销单号</th>
                 		  <th>合计金额</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.reimbursementNo"/></td>
              					<td class="col-sm-2"><s:property value="#taskVO.totalAmount" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('personal/auditReimbursement?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="${taskDefKey == 'updateAccount'?'办理':'审批'}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="resignation">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>岗位</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-3">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-3"><s:iterator id="group" value="#taskVO.groupList" status="st"><s:if test="#st.index != 0 "><br></s:if><s:property value="#group"/></s:iterator></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('personal/perform?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="办理" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="formal">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>入职日期</th>
		                  <th>岗位</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.entryDate" /></td>
		              			<td class="col-sm-3"><s:iterator id="group" value="#taskVO.groupList" status="st"><s:if test="#st.index != 0 "><br></s:if><s:property value="#group"/></s:iterator></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-3">
		              			<s:if test="#taskVO.taskName == '员工填写申请表'">
		              			<a onclick="goPath('personal/fillFormal?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="填写申请表" data-toggle="tooltip">
	             						<use xlink:href="#icon-banli"></use>
	             					</svg>
		              			</a>
		              			</s:if>
		              			<s:else>
		              			<a onclick="goPath('personal/perform?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="办理" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
		              			</a>
		              			</s:else>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="email">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-3">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-3">
		              			<a onclick="goPath='personal/perform?taskID=<s:property value='#taskVO.taskID' />'" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="办理" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>

			   <div role="tabpanel" class="tab-pane" id="trip">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-3">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-3">
		              			<a onclick="goPath('personal/perform?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="办理" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  
			  <div role="tabpanel" class="tab-pane" id="audit">
			  	<c:if test="${empty staffAuditVO }"><div class="table-responsive" style="margin-top:30px;">暂无；</div></c:if>
			    <c:if test="${not empty staffAuditVO }">
			  	<form action="personal/fillAuditForm" method="post" onsubmit="Load.Base.LoadingPic.FullScreenShow(null)">
			  	<div class="table-responsive" style="margin-top:30px;">
			  	<input type="hidden" name="taskID" value="${taskID }" />
			  		<div class="fz20">基本资料<span style="color:red;font-size:16px;">（调查信息请尽快如实汇报，如有弄虚作假，后果自负！）</span></div>
					<div class="mt10">
					<table class="form_tabA">
					  <tbody>
					    <tr>
					      <td width="100">学历证书编号：</td>
					      <td><input id="num" name="staffAuditVO.educationID" type="text" class="input" value="${staffAuditVO.educationID }" required="required" /></td>
						  <td width="110">学位证书编号：</td>
					      <td><input id="num" name="staffAuditVO.degreeID" type="text" class="input" value="${staffAuditVO.degreeID }" /></td>
					    </tr>
						<tr>
					      <td>案底说明：</td>
					      <td colspan="3"><textarea name="staffAuditVO.criminalRecord" id="textarea" cols="45" rows="2" class="w80p textarea" >${staffAuditVO.criminalRecord }</textarea></td>
					    </tr>
					  </tbody>
					</table>
					</div>
					<div class="mt20 bdt_gray pt20 clearfix"><span class="fz20 fl">工作经验</span><a href="javascript:void(0);" class="oa_btn resumeadd fr">添加</a></div>
					<div class="blank10"></div>
					<div class="resume">
						<a href="javascript:void(0);" class="resumedel"></a>
						<table class="form_tabA">
						  <tbody>
							<tr>
							  <td width="80">企业名称：</td>
							  <td><input id="dw" name="staffAuditVO.company" type="text" class="input" required="required"/></td>
						<!-- 	  <td width="120">工作时间：</td>
							  <td><input id="num" name="staffAuditVO.years" type="text" class="input" required="required" oninput="checkNum(this);"/></td> -->
							  <td>开始时间：</td>
							  <td>
	    			 			<input autocomplete="off" type="text" class="input" name="staffAuditVO.beginDate" 
			    					onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })" placeholder="开始时间">
							  </td>
							  <td>结束时间：</td>
							  <td>
	    			 			<input autocomplete="off" type="text" class="input" name="staffAuditVO.endDate"
			    					onclick="WdatePicker({ dateFmt: 'yyyy-MM-dd' })" placeholder="结束时间">
							  </td>
							</tr><tr>
							  <td>证明人：</td>
							  <td><input id="num" name="staffAuditVO.referee" type="text" class="input" required="required"/></td>
							  <td>证明人电话：</td>
							  <td><input id="num" name="staffAuditVO.telephone" type="text" class="input" required="required"/></td>
							</tr>
							<tr>
							  <td>工作描述：</td>
							  <td colspan="5"><textarea name="staffAuditVO.description" id="textarea" cols="45" rows="4" class="w80p textarea" required="required"></textarea></td>
							</tr>
						  </tbody>
						</table>
					</div>
					<div class="mt30 tc"><input type="submit" id="tijiao" value="提交" class="oa_btn btn_s bg_blue plr20"/></div>
			  	</div>
			  	</form>
			  	</c:if>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="socialSecurity">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-3">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-3">
		              			<a onclick="goPath('personal/auditSocialSecurity?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              				<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  
			  <div role="tabpanel" class="tab-pane" id="chopBorrow">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty chopTasksVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					
		              		<c:forEach items="${chopTasksVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
								<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
			              		
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="certificateBorrow">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty certificateTasksVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					
		              		<c:forEach items="${certificateTasksVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
			              		
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="contractBorrow">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty contractTasksVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					
		              		<c:forEach items="${contractTasksVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="idBorrow">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty idBorrowTasksVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
					
		              		<c:forEach items="${idBorrowTasksVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
			              		
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  
			   <div role="tabpanel" class="tab-pane" id="contract">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty contractTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${contractTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="changeContract">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty contractChangeTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${contractChangeTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			   <div role="tabpanel" class="tab-pane" id="changeBankAccount">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty changeBankAccountTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${changeBankAccountTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			   <div role="tabpanel" class="tab-pane" id="chopDestroy">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty chopDestroyTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${chopDestroyTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="carveChop">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty carveChopTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${carveChopTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			   <div role="tabpanel" class="tab-pane" id="purchaseProperty">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty purchasePropertyTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${purchasePropertyTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="handleProperty">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty handlePropertyTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${handlePropertyTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="transferProperty">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty transferPropertyTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${transferPropertyTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="shopApply">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th class="">序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>平台</th>
		                  <th>店铺名称</th>
		                  <th>开店时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty shopApplyTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${shopApplyTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td>${item.platform }</td>
			              		<td>${item.shopName }</td>
			              		<td>${item.shopStartTime }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="shopPayApply">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th class="">序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty shopPayApplyTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${shopPayApplyTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="workOvertime">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:5%">序号</th>
		                  <th style="width:20%">加班人员</th>
		                  <th style="width:10%">所属部门</th>
		                  <th style="width:10%">开始时间</th>
		                  <th style="width:10%">结束时间</th>
		                  <th style="width:10%">预计加班工时</th>
		                  <th style="width:15%">加班原因</th>
		                  <th style="width:10%">发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty workOvertimeTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${workOvertimeTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.department}</td>
			              		<td>${item.beginDate}</td>
			              		<td>${item.endDate}</td>
			              		<td>${item.workHours}</td>
			              		<td>${item.reason}</td>
			              		<td>${item.requestDate}</td>
          				             <td>
          				               	<a onclick="goPath('personal/taskComplete?taskID=${item.taskID}&result=1&businessType=加班申请')" href="javascript:void(0)">
          				               	<svg class="icon" aria-hidden="true" title="同意" data-toggle="tooltip">
										<use xlink:href="#icon-wancheng"></use>
										</svg>
          				               	</a>
          				               	&nbsp;
         							   	<a onclick="goPath('personal/taskComplete?taskID=${item.taskID}&result=2&businessType=加班申请')" href="javascript:void(0)">
         							   	<svg class="icon" aria-hidden="true" title="不同意" data-toggle="tooltip">
										<use xlink:href="#icon-butongyi"></use>
										</svg>
         							   	</a>
         							   	&nbsp;
         							   	<a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findTaskList')" href="javascript:void(0)">
         							    <svg class="icon" aria-hidden="true" title="查看已审批环节" data-toggle="tooltip">
										<use xlink:href="#icon-liucheng"></use>
										</svg>
         							   	</a>
         							 </td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="classHour">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:10%">序号</th>
		                  <th style="width:15%">任务</th>
		                  <th style="width:15%">课程名称</th>
		                  <th style="width:10%">培训类别</th>
		                  <th style="width:15%">开始时间</th>
		                  <th style="width:10%">时长（h）</th>
		                  <th style="width:15%">地点</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty coursePlanVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${coursePlanVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>【${item.taskName}】</td>
			              		<td>${item.courseName}</td>
			              		<td>${item.trainClass}</td>
			              		<td>${item.beginTime}</td>
			              		<td>${item.trainHours}</td>
			              		<td>${item.place}</td>
          				        <td>
          				        <c:if test="${item.taskName=='点名签到'}">
          				        	<a href="javacript:void(0)" onclick="signIn(${item.coursePlanId},${item.taskID})" data-toggle="modal" data-target="#signIn">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
          				        </c:if>
<%--           				        <c:if test="${item.taskName=='评分'}">
          				        	<a href="javacript:void(0)" onclick="giveScore(${item.coursePlanId},${item.taskID})" data-toggle="modal" data-target="#courseScore">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
          				        </c:if> --%>
          				        <c:if test="${item.taskName=='评分'}">
          				        	<a href="javacript:void(0)" onclick="CommentOrNot(${item.coursePlanId},${item.taskID})">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
          				        </c:if>
          				        <c:if test="${item.taskName=='是否测试'}">
          				        	<a href="javacript:void(0)" onclick="testOrNot(${item.coursePlanId},${item.taskID})">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
	             					<div id="uploadTestsBtn"  data-toggle="modal" data-target="#uploadTests"></div>
          				        </c:if>
<%--           				        <c:if test="${item.taskName=='上传试题'}">
          				        	<a href="javacript:void(0)" onclick="uploadTests(${item.coursePlanId},${item.taskID})" data-toggle="modal" data-target="#uploadTests">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
          				        </c:if> --%>
          				        <c:if test="${item.taskName=='学员测试'}">
          				        	<a onclick="goPath('train/startNewTest?coursePlanId=${item.coursePlanId}&taskId=${item.taskID}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
          				        </c:if>
          				        <c:if test="${item.taskName=='完结课时'}">
          				        	<a onclick="goPath('train/showLecturerAndStuScore?coursePlanId=${item.coursePlanId}&taskId=${item.taskID}&courseName=${item.courseName}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
          				        </c:if>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="problemOrder">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:10%">序号</th>
		                  <th style="width:15%">任务</th>
		                  <th style="width:15%">问题名称</th>
		                  <th style="width:10%">创建人</th>
		                  <th style="width:15%">创建时间</th>
		                  <th style="width:10%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty problemOrderTasks}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${problemOrderTasks}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>【${item.taskName}】</td>
			              		<td>${item.orderName}</td>
			              		<td>${item.creatorName}</td>
			              		<td>${item.addTime}</td>
          				        <td>
          				        	<a onclick="goPath('/performance/soft/handleProblemOrder?problemOrderId=${item.id}&taskId=${item.taskId}&taskName=${item.taskName}&processInstanceId=${item.processInstanceID}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="morningMeetingReport">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th style="width:10%">序号</th>
		                  <th style="width:15%">汇报人</th>
		                  <th style="width:15%">所属部门</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:10%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty morningMeetingTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${morningMeetingTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.userName}</td>
			              		<td>${item.department}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
          				        	<c:if test="${item.taskName=='老板审批'}">
          				        	<a onclick="goPath('personal/auditWeekMeetingReport?taskID=${item.taskId}&processInstanceId=${item.processInstanceID}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
	             					</c:if>
	             					<c:if test="${item.taskName!='老板审批'}">
          				        	<a onclick="goPath('personal/toReportWeekMeeting?taskID=${item.taskId}&processInstanceId=${item.processInstanceID}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-modify"></use>
	             					</svg>
	             					</a>
	             					</c:if>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="project">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:10%">标题</th>
		                  <th style="width:15%">项目名称</th>
		                  <th style="width:10%">项目进度</th>
		                  <th style="width:10%">负责人</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:8%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty projectInfoTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${projectInfoTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.userName}发起的项目</td>
			              		<td>${item.projectName}</td>
			              		<td>${item.projectProgress}</td>
			              		<td>${item.projectLeaderName}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
          				        	<a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findTaskList')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="查看流程详情" data-toggle="tooltip">
	             						<use xlink:href="#icon-Detailedinquiry"></use>
	             					</svg>
	             					</a>
	             					&nbsp;
	             					<a onclick="goPath('administration/project/showTask?processInstanceID=${item.processInstanceID}&taskId=${item.taskId}&taskName=${item.taskName}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="${item.taskName}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="brandAuth">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:10%">标题</th>
		                  <th style="width:10%">授权公司名称</th>
		                  <th style="width:10%">品牌</th>
		                  <th style="width:15%">授权时间</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:8%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty brandAuthTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${brandAuthTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.userName}发起的申请</td>
			              		<td>${item.companyName}</td>
			              		<td>${item.brand}</td>
			              		<td>${item.authBeginDate} 至 ${item.authEndDate}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
          				        	<a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findTaskList')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="查看流程详情" data-toggle="tooltip">
	             						<use xlink:href="#icon-liucheng"></use>
	             					</svg>
	             					</a>
	             					&nbsp;
	             					<c:if test="${item.taskName!='申请盖章'}">
		             					<a onclick="goPath('administration/process/toAuditBrandAuth?processInstanceID=${item.processInstanceID}&taskId=${item.taskId}&taskName=${item.taskName}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
		             						<use xlink:href="#icon-shenpi"></use>
		             					</svg>
		             					</a>
	             					</c:if>
	             					<c:if test="${item.taskName=='申请盖章'}">
		             					<a onclick="goPath('administration/process/toApplyChop?processInstanceID=${item.processInstanceID}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="申请盖章" data-toggle="tooltip">
		             						<use xlink:href="#icon-shenpi"></use>
		             					</svg>
		             					</a>
	             					</c:if>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="publicEvent">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:3%">序号</th>
		                  <th style="width:10%">标题</th>
		                  <th style="width:20%">需公关事件说明</th>
		                  <th style="width:7%">倒计时</th>
		                  <th style="width:10%">申请人电话</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:8%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty publicEventTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${publicEventTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.userName}发起的申请</td>
			              		<td>${item.eventDescription}</td>
			              		<td>
			              			<c:if test="${item.countdown=='时间截止'}"><span style="color:red">${item.countdown}</span></c:if>
			              			<c:if test="${item.countdown!='时间截止'}"><span>${item.countdown}</span></c:if>
			              		</td>
			              		<td>${item.phone}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
          				        	<a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findTaskList')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="查看流程详情" data-toggle="tooltip">
	             						<use xlink:href="#icon-liucheng"></use>
	             					</svg>
	             					</a>
	             					&nbsp;
	             					<c:if test="${item.taskName=='匹配公关'}">
		             					<a onclick="goPath('/administration/public/toMatchPublicEvent?processInstanceID=${item.processInstanceID}&taskId=${item.taskId}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="匹配" data-toggle="tooltip">
		             						<use xlink:href="#icon-shenpi"></use>
		             					</svg>
		             					</a>
		             					&nbsp;
		             					<a onclick="noMatch(${item.processInstanceID},${item.taskId})" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="无法匹配" data-toggle="tooltip">
		             						<use xlink:href="#icon-butongyi"></use>
		             					</svg>
		             					</a>
	             					</c:if>
	             					<c:if test="${item.taskName=='事件处理'}">
		             					<a onclick="goPath('/administration/public/toHandlePublicEvent?processInstanceID=${item.processInstanceID}&taskId=${item.taskId}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="完成处理" data-toggle="tooltip">
		             						<use xlink:href="#icon-shenpi"></use>
		             					</svg>
		             					</a>
		             					&nbsp;
		             					<a onclick="noHanle(${item.processInstanceID},${item.taskId})" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="无法处理" data-toggle="tooltip">
		             						<use xlink:href="#icon-butongyi"></use>
		             					</svg>
		             					</a>
	             					</c:if>
	             					<c:if test="${item.taskName=='意见反馈'}">
		             					<a onclick="goPath('/administration/public/toAdvicePublicEvent?processInstanceID=${item.processInstanceID}&taskId=${item.taskId}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="意见反馈" data-toggle="tooltip">
		             						<use xlink:href="#icon-shenpi"></use>
		             					</svg>
		             					</a>
	             					</c:if>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="performance">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:10%">发起人</th>
		                  <th style="width:10%">绩效岗位</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty performanceTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${performanceTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.userName}</td>
			              		<td>${item.positionNames}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
	             					<c:if test="${item.taskName=='制定绩效考核方案'}">
		             					<a onclick="goPath('/administration/performance/modifyPositionPerformance?templateIds=${item.templateIds}&taskId=${item.taskId}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="修改绩效方案" data-toggle="tooltip">
		             						<use xlink:href="#icon-shenpi"></use>
		             					</svg>
		             					</a>
		             					&nbsp;
				              			<a onclick="goPath('personal/processHistory?processInstanceID=${item.processInstanceID}&selectedPanel=findTaskList')" href="javascript:void(0)">
					              			<svg class="icon" aria-hidden="true" title="查看原因" data-toggle="tooltip">
			            						<use xlink:href="#icon-liucheng"></use>
			            					</svg>
				              			</a>
	             					</c:if>
	             					<c:if test="${item.taskName=='PM评审'}">
		             					<a onclick="goPath('/administration/performance/toPMAudit?templateIds=${item.templateIds}&taskId=${item.taskId}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
		             						<use xlink:href="#icon-shenpi"></use>
		             					</svg>
		             					</a>
	             					</c:if>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="personalPerformance">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:7%">发起人</th>
		                  <th style="width:7%">考核人员</th>
		                  <th style="width:14%">职务</th>
		                  <th style="width:10%">考核月份</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty performanceTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${performanceTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.userName}</td>
			              		<td>${item.requestUserName}</td>
	           					<td>
		           					<c:forEach items="${item.groupDetailVOs}" var="groupDetailVO">
		           						<li style="list-style:none">${groupDetailVO.companyName}-${groupDetailVO.departmentName}-${groupDetailVO.positionName}</li>
		           					</c:forEach>
	           					</td>
	           					<td>${item.year}-${item.month}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
	             					<a onclick="goPath('/administration/performance/toPMAuditPersonal?staffName=${item.requestUserName}&checkItemIds=${item.checkItemIds}&taskId=${item.taskId}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="alterSalary">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:7%">调薪人员</th>
		                  <th style="width:14%">部门</th>
		                  <th style="width:7%">发起人</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty alterSalaryTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${alterSalaryTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.staffName}</td>
			              		<td>${item.department}</td>
			              		<td>${item.userName}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
	             					<a onclick="goPath('HR/staffSalary/auditSalaryAlteration?id=${item.id}&taskId=${item.taskId}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="changeSalaryDetail">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:7%">姓名</th>
		                  <th style="width:14%">部门</th>
		                  <th style="width:7%">年份</th>
		                  <th style="width:7%">月份</th>
		                  <th style="width:7%">发起人</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty changeSalaryDetailTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${changeSalaryDetailTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.department}</td>
			              		<td>${item.year}</td>
			              		<td>${item.month}</td>
			              		<td>${item.userName}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
	             					<a onclick="goPath('HR/staffSalary/auditSalaryDetailChange?processInstanceId=${item.processInstanceID}&taskId=${item.taskId}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="paySalary">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:7%">发放年份</th>
		                  <th style="width:7%">发放月份</th>
		                  <th style="width:7%">发起人</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty paySalaryTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${paySalaryTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.year}</td>
			              		<td>${item.month}</td>
			              		<td>${item.userName}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
	             					<a onclick="goPath('HR/staffSalary/findPaySalaryInfos?processInstanceId=${item.processInstanceID}&taskId=${item.taskId}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="打款确认" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="rewardAndPunishment">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:5%">序号</th>
		                  <th style="width:12%">奖惩人员</th>
		                  <th style="width:7%">类型</th>
		                  <th style="width:7%">额度(元)</th>
		                  <th style="width:7%">生效时间</th>
		                  <th style="width:7%">发起人</th>
		                  <th style="width:10%">发起时间</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty rewardAndPunishmentTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${rewardAndPunishmentTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.requestUserNames}</td>
			              		<td>${item.type==0 ? '奖励':'惩罚'}</td>
			              		<td>${item.money}</td>
			              		<td>${item.effectiveDate}</td>
			              		<td>${item.userName}</td>
			              		<td><fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
          				        <td>
	             					<a onclick="goPath('HR/staffSalary/toAuditRewardAndPunishment?processInstanceId=${item.processInstanceID}&taskId=${item.taskId}')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
				<div role="tabpanel" class="tab-pane" id="credentialApply">
			  		<div class="table-responsive" style="margin-top:30px;">
			  			<table class="table table-striped">
			  				<thead>
			  					<tr>
			  						<th style="width:12%;">序号</th>
			  						<th style="width:15%">审核人</th>
			  						<th style="width:20%">职务</th>
			  						<th style="width:25%">发起时间</th>
			  						<th style="width:15%">上传证书人</th>
			  						<th style="width:15%">操作</th>
			  					</tr>
			  				</thead>
			  				<tbody>
			  					<c:if test="${not empty credentialTaskVos }">
			  					<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
			  					<c:forEach items="${credentialTaskVos }" var="item" varStatus="number">
				  					<tr>
				  						<td>${number.index+startIndex+1}</td>
				  						<td>${item.applyUserName }</td>
				  						<td>${item.applyPositionNames }</td>
				  						<td>
				  							<fmt:formatDate value="${item.addTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
				  						</td>
				  						<td>${item.offerUserName }</td>
				  						<td>
				  						<c:choose>
				  							<c:when test="${item.status==1 }">
					  							<a onclick="goPath('HR/staff/postCredentialUpload?taskId=${item.taskId }&id=${item.id }')" href="javascript:void(0)">
													<svg class="icon" aria-hidden="true" title="填写" data-toggle="tooltip">
														<use xlink:href="#icon-modify"></use>
													</svg>
												</a>
											</c:when>
											<c:when test="${item.status==2 }">
												<a onclick="goPath('HR/staff/postCredentialCheck?taskId=${item.taskId }&id=${item.id }')" href="javascript:void(0)">
													<svg class="icon" aria-hidden="true" title="审核" data-toggle="tooltip">
														<use xlink:href="#icon-modify"></use>
													</svg>
												</a>
											</c:when>
											<c:when test="${item.status==3 }">
												<a onclick="goPath('HR/staff/postCredentialUpload?taskId=${item.taskId }&id=${item.id }')" href="javascript:void(0)">
													<svg class="icon" aria-hidden="true" title="查看" data-toggle="tooltip">
														<use xlink:href="#icon-modify"></use>
													</svg>
												</a>
											</c:when>
											<c:when test="${item.status==4 }">
												<a onclick="goPath('HR/staff/postCredentialAmend?taskId=${item.taskId }&id=${item.id }')" href="javascript:void(0)">
													<svg class="icon" aria-hidden="true" title="修改" data-toggle="tooltip">
														<use xlink:href="#icon-modify"></use>
													</svg>
												</a>
											</c:when>
										</c:choose>
				  						</td>
				  					</tr>
				  					
			  					</c:forEach>
			  					</c:if>
			  				</tbody>
			  			</table>
			  		</div>
			  		
			  		<div class="dropdown" style="margin-top:25px;">
						<label>每页显示数量：</label>
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
						${limit}
						<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
							<li><a class="dropdown-item-20" href="#">20</a></li>
							<li><a class="dropdown-item-50" href="#">50</a></li>
							<li><a class="dropdown-item-100" href="#">100</a></li>
						</ul>
						&nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
						<input type="hidden" id="page" value="${page}"/>
						<input type="hidden" id="limit" value="${limit}" />
						<input type="hidden" id="totalPage" value="${totalPage }" />
					</div>
					<%@include file="/includes/pager.jsp" %>
				</div>
				
				<div role="tabpanel" class="tab-pane" id="checkExitPartner">
			  		<div class="table-responsive" style="margin-top:30px;">
			  			<table class="table table-striped">
			  				<thead>
			  					<tr>
			  						<th style="width: 10%;">序号</th>
			  						<th style="width: 15%;">申请人</th>
			  						<th style="width: 20%;">申请时间</th>
			  						<th>申请原因</th>
			  						<th style="width: 15%;">审批</th>
			  					</tr>
			  				</thead>
			  				<tbody>
			  					<c:if test="${not empty exitPartnerTaskVOs }">
			  					<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
			  					<c:forEach items="${exitPartnerTaskVOs }" var="item" varStatus="number">
				  					<tr>
				  						<td>${number.index+startIndex+1}</td>
				  						<td id="userId${item.id }" data-userId="${item.userId }">
				  							
				  						</td>
				  						<td id="applyExitTime${item.id }">
				  							<fmt:formatDate value="${item.applyAddTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				  						</td>
				  						<td data-exitReason="${item.exitReason }">
				  							${item.exitReason }
				  						</td>
				  						<script type="text/javascript">
				  						$(function(){
				  							var userId = $("#userId${item.id }").attr("data-userId");
			  								$.ajax({
				  								url:"/administration/partner/pageLoad",
				  								type:"post",
				  								data:{"userId": userId},
				  								success:function(data){
				  									$("#userId${item.id }").html(data.staffName);
				  								}
				  							})
				  						});
				  						</script>
				  						<td>
				  							<a onclick="agreeExit(this)" data-taskId="${item.taskId }" data-userId="${item.userId }" data-processInstanceId="${item.processInstanceId }" data-auditStatus="2"
				  							 href="javascript:void(0)" data-exitParterApplyId="${item.exitParterApplyId }"
				  							 data-exitPartnerCount="${exitPartnerCount}">
					              				<svg class="icon" aria-hidden="true" title="同意" data-toggle="tooltip">
				             						<use xlink:href="#icon-wancheng"></use>
				             					</svg>
					              			</a>
					              			&nbsp;
					              			<a onclick="disagreeExit(this)" data-taskId="${item.taskId }" data-userId="${item.userId }" data-processInstanceId="${item.processInstanceId }" data-auditStatus="1" 
					              			 href="javascript:void(0)" data-exitParterApplyId="${item.exitParterApplyId }"
					              			 data-exitPartnerCount="${exitPartnerCount}">
					              				<svg class="icon" aria-hidden="true" title="不同意" data-toggle="tooltip">
				             						<use xlink:href="#icon-butongyi"></use>
				             					</svg>
					              			</a>
				  								
				  						</td>
				  					</tr>
			  					</c:forEach>
			  					</c:if>
			  				</tbody>
			  			</table>
			  		</div>
			  		
			  		<div class="dropdown" style="margin-top:25px;">
						<label>每页显示数量：</label>
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
						${limit}
						<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
							<li><a class="dropdown-item-20" href="#">20</a></li>
							<li><a class="dropdown-item-50" href="#">50</a></li>
							<li><a class="dropdown-item-100" href="#">100</a></li>
						</ul>
						&nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
						<input type="hidden" id="page" value="${page}"/>
						<input type="hidden" id="limit" value="${limit}" />
						<input type="hidden" id="totalPage" value="${totalPage }" />
					</div>
					<%@include file="/includes/pager.jsp" %>
				</div>

				
				
				
				
				<div role="tabpanel" class="tab-pane" id="carMaintainApply">
			  		<div class="table-responsive" style="margin-top:30px;">
			  			<table class="table table-striped">
			  				<thead>
			  					<tr>
			  						<th style="width: 20%;">序号</th>
			  						<th style="width: 20%;">申请人</th>
			  						<th style="width: 25%;">申请时间</th>
			  						<th style="width: 20%;">操作</th>
			  					</tr>
			  				</thead>
			  				<tbody>
			  					<c:if test="${not empty carMaintainApplys }">
			  					<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
			  					<c:forEach items="${carMaintainApplys }" var="item" varStatus="number">
				  					<tr>
				  						<td>${number.index+startIndex+1}</td>
				  						<td>
				  							${item.vehicleVo.leaderName }
				  						</td>
				  						<td>
				  							<fmt:formatDate value="${item.addTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				  						</td>
				  						<td>
				  							
											
											<c:choose>
												<c:when test="${item.managerAuditResult==1 }">
													<a onclick="goPath('/administration/process/updateVehicle?vehicleInfoId=${item.vehicleInfoId }&maintainType=${item.type }&taskId=${item.taskId }')" href="javascript:void(0)">
														<svg class="icon" aria-hidden="true" title="填写维修保养记录" data-toggle="tooltip">
															<use xlink:href="#icon-modify"></use>
														</svg>
													</a>
												</c:when>
												<c:otherwise>
													<a onclick="goPath('/administration/process/checkCarMaintainApply?taskId=${item.taskId }&processInstanceID=${item.processInstanceID }')" href="javascript:void(0)">
														<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
															<use xlink:href="#icon-modify"></use>
														</svg>
													</a>
												</c:otherwise>
											</c:choose>
				  						</td>
				  					</tr>
			  					</c:forEach>
			  					</c:if>
			  				</tbody>
			  			</table>
			  		</div>
			  		
			  		<div class="dropdown" style="margin-top:25px;">
						<label>每页显示数量：</label>
						<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
						${limit}
						<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
							<li><a class="dropdown-item-20" href="#">20</a></li>
							<li><a class="dropdown-item-50" href="#">50</a></li>
							<li><a class="dropdown-item-100" href="#">100</a></li>
						</ul>
						&nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
						<input type="hidden" id="page" value="${page}"/>
						<input type="hidden" id="limit" value="${limit}" />
						<input type="hidden" id="totalPage" value="${totalPage }" />
					</div>
					<%@include file="/includes/pager.jsp" %>
				</div>
				
							
				
			  <!--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx  -->
			  

			  <div role="tabpanel" class="tab-pane" id="performanceTarget">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:10%">序号</th>
		                  <th style="width:10%">姓名</th>
		                  <th style="width:10%">年份</th>
		                  <th style="width:10%">月份</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty performanceTargetValueTasks}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${performanceTargetValueTasks}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.staffName}</td>
			              		<td>${item.year}</td>
			              		<td>${item.month}</td>
          				        <td>
	             					<a onclick="goPath('/administration/performance/writeTargetValue?taskId=${item.taskId}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="填写目标值" data-toggle="tooltip">
		             						<use xlink:href="#icon-banli"></use>
		             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="performanceActual">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		            	  <th style="width:10%">序号</th>
		                  <th style="width:10%">姓名</th>
		                  <th style="width:10%">年份</th>
		                  <th style="width:10%">月份</th>
		                  <th style="width:5%">操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty performanceActualValueTasks}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${performanceActualValueTasks}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.staffName}</td>
			              		<td>${item.year}</td>
			              		<td>${item.month}</td>
          				        <td>
	             					<a onclick="goPath('/administration/performance/writeActualValue?taskId=${item.taskId}')" href="javascript:void(0)">
		             					<svg class="icon" aria-hidden="true" title="填写完成值" data-toggle="tooltip">
		             						<use xlink:href="#icon-banli"></use>
		             					</svg>
	             					</a>
         						</td>
         					 </tr>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  <div role="tabpanel" class="tab-pane" id="carUse">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty carUseTaskVos}">
		              	<c:set var="startIndex" value="${(page-1)*limit }"></c:set>
		              		<c:forEach items="${carUseTaskVos}" var= "item"  varStatus="index" >
		              		<tr>
			              		<td>${index.index+startIndex+1}</td>
			              		<td>${item.title}</td>
			              		<td>${item.requestUserName}</td>
			              		<td>${item.requestDate }</td>
			              		<td class="col-sm-2"><a onclick="goPath('personal/perform?taskID=${item.taskID}')" href="javascript:void(0)">
			              		<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
			              		</a></td>
		              		</c:forEach>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  
			  
		   <div role="tabpanel" class="tab-pane" id="vitae">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                   <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty vitaeTasksVos}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.vitaeTasksVos" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('/HR/vitae/toConfirmVitae?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="审批" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  
			   <div role="tabpanel" class="tab-pane" id="commonSubject">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                   <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>内容标题</th>
		                  <th>类别</th>
		                  <th>流通方式</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty commonSubjectVos}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.commonSubjectVos" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.title_"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.type"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.route"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('/personal/tablePerform?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="${taskVO.type eq '告知'?'查看告知':'审批'}" data-toggle="tooltip">
								<use xlink:href="#icon-shenpi"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			   <div role="tabpanel" class="tab-pane" id="softPerformance">
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>发起人</th>
		                  <th>所属项目</th>
		                  <th>版本</th>
		                  <th>模块</th>
		                  <th>分值</th>
		                  <th>截止时间</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty softPerformacneVos}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.softPerformacneVos" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.projectName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.versionName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.moduleName"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.score"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.limitTime"/></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('/personal/tablePerform?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="办理" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			  
			  
			   <div role="tabpanel" class="tab-pane" id="advance">
		  
		    <form  class="form-horizontal" action="/personal/findTaskList?">
          	<input style="display:none" name="type" value="15"/>
			<div class="form-group" style="margin-top:10px">
				<label for="beginDate" class="col-sm-1 control-label">预付单号</label>
				<div class="col-sm-2">
			    	<input type="text" class="form-control" id="No_code" name="No_code" value="${No_code}"  placeholder="预付编号">
			    </div>
			    <label class="col-sm-1 control-label">发起人</label>
				<div class="col-sm-2">
			    	<input type="text" class="form-control" name="applyerNameForAdvance" value="${applyerNameForAdvance}" onblur="checkEmptyForAdvance()" autoComplete="off" id="applyerForAdvance">
			    	<input name="applyerIdForAdvance" type="hidden" value="${applyerIdForAdvance}"/>
			    </div>
			    <label for="beginDate" class="col-sm-1 control-label">发起时间</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="startTime" value="${startTime}" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endTime" value="${endTime }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			    <div class="col-sm-1">
			    <button type="submit"  class="btn btn-primary"  >查询</button>
			    </div>
			</div>
          </form>
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>预付单号</th>
                 		  <th>合计金额</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.reimbursementNo"/></td>
              					<td class="col-sm-2"><s:property value="#taskVO.totalAmount" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('personal/auditAdvance?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="${taskDefKey=='updateAccount'?'办理':'审批'}" data-toggle="tooltip">
									<use xlink:href="#icon-shenpi"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
			<div role="tabpanel" class="tab-pane" id="payment">
		    <form  class="form-horizontal" action="/personal/findTaskList?">
          	<input style="display:none" name="type" value="35"/>
			<div class="form-group" style="margin-top:10px">
				<label for="beginDate" class="col-sm-1 control-label">付款单号</label>
				<div class="col-sm-2">
			    	<input type="text" class="form-control" id="No_code" name="No_code" value="${No_code}"  placeholder="付款单号">
			    </div>
			    <label class="col-sm-1 control-label">发起人</label>
				<div class="col-sm-2">
			    	<input type="text" class="form-control" name="applyerNameForPay" value="${applyerNameForPay}" onblur="checkEmptyForPay()" autoComplete="off" id="applyerForPay">
			    	<input name="applyerIdForPay" type="hidden" value="${applyerIdForPay}"/>
			    </div>
			    <label for="beginDate" class="col-sm-1 control-label">发起时间</label>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="beginDate" name="startTime" value="${startTime}" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="开始时间">
			    </div>
			    <div class="col-sm-2">
			    	<input type="text" class="form-control" id="endDate" name="endTime" value="${endTime }" onclick="WdatePicker({maxDate:'%y-%M-%d'})" placeholder="结束时间">
			    </div>
			    <div class="col-sm-1">
			    <button type="submit"  class="btn btn-primary"  >查询</button>
			    </div>
			</div>
          </form>
			  	<div class="table-responsive" style="margin-top:30px;">
			  		<table class="table table-striped">
		              <thead>
		                <tr>
		                  <th>序号</th>
		                  <th>标题</th>
		                  <th>付款单号</th>
                 		  <th>合计金额</th>
		                  <th>发起人</th>
		                  <th>发起时间</th>
		                  <th>操作</th>
		                </tr>
		              </thead>
              		  <tbody>
		              	<c:if test="${not empty taskVOs}">
		              	<s:set name="task_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="taskVO" value="#request.taskVOs" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#task_id+1"/></td>
		              			<td class="col-sm-2">【<s:property value="#taskVO.taskName"/>】<s:property value="#taskVO.title"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.reimbursementNo"/></td>
              					<td class="col-sm-2"><s:property value="#taskVO.totalAmount" /></td>
		              			<td class="col-sm-1"><s:property value="#taskVO.requestUserName"/></td>
		              			<td class="col-sm-2"><s:property value="#taskVO.requestDate"/></td>
		              			<td class="col-sm-1">
		              				<a onclick="goPath('personal/auditPayment?taskID=<s:property value='#taskVO.taskID' />')" href="javascript:void(0)">
	             					<svg class="icon" aria-hidden="true" title="${taskDefKey == 'updateAccount'?'办理':'审批'}" data-toggle="tooltip">
	             						<use xlink:href="#icon-shenpi"></use>
	             					</svg>
	             					</a>
		              			</td>
		              		</tr>
		              		<s:set name="task_id" value="#task_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
		              </tbody>
           			</table>
			  	</div>
			  	<div class="dropdown" style="margin-top:25px;">
		           	<label>每页显示数量：</label>
		           	<button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
		           		${limit}
		           		<span class="caret"></span>
		           	</button>
		           	<ul class="dropdown-menu" aria-labelledby="dropdownMenu1" style="left:104px;min-width:120px;">
					    <li><a class="dropdown-item-20" href="#">20</a></li>
					    <li><a class="dropdown-item-50" href="#">50</a></li>
					    <li><a class="dropdown-item-100" href="#">100</a></li>
				    </ul>
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${totalCount}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
			  </div>
		  </div>
          
		  </div>
        </div>
   		<div id="signIn" class="modal fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog" style="width:500px">
		<form id="signInForm" class="form-horizontal" method="post" action="train/completeSignIn" onsubmit="return confirmSignIn()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">学员列表</h4>
			</div>
			<div class="modal-body" style="min-height:200px; max-height:500px; overflow:scroll">
				<table id="tab">
					<tbody>
						<tr>
							<td><input type="checkbox" class="all"></td>
							<td>序号</td>
							<td>部门</td>
							<td>姓名</td>
						</tr>
					</tbody>
				  <tbody id="joinerList">
	              </tbody>
				</table>
			</div>
			<input type="hidden" name="coursePlanId">
			<input type="hidden" name="signIntaskId">
			<div class="modal-footer">
				<div style="float:left"><input checked type="checkbox" name="exportSignIn" value="true"><span style="font-size:10px"> 导出签到表</span></div>
				<button type="submit" class="btn btn-primary">签到</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
		</div>
		</div>
		<div id="courseScore" class="modal fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
		<form class="form-horizontal">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">评分</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="control-label col-sm-3">讲师</label>
					<div class="control-label col-sm-9" id="lecturer" style="text-align:left"></div>
					<input type="hidden" name="lecturerId">
				</div>
				<div class="form-group">
					<label class="control-label col-sm-3">评分</label>
					<div class="col-sm-9" style="margin-top:-3px">
						<input id="commentForLecturer" class="rating" data-size="xs" value="3">
					</div>
				</div>
			</div>
			<input type="hidden" name="coursePlanId">
			<input type="hidden" name="commentTaskId">
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-primary" onclick="completeComment(1)">评分</button>
				<button type="button" class="btn btn-default" onclick="completeComment(2)">不评分</button>
			</div>
		</div>
		</form>
		</div>
		</div>
		<div id="uploadTests" class="modal fade" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
		<form class="form-horizontal" enctype="multipart/form-data" action="train/uploadTests" method="post" onsubmit="addLoadingForTest()">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">上传试题</h4>
			</div>
			<div class="modal-body">
				<div class="form-group">
					<label class="control-label col-sm-3">模板下载：</label>
					<div class="control-label col-sm-9" style="text-align:left">
						<a href="/template/questionTempalte.xls">试题上传模板excel</a>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-3">试题文件：</label>
					<div class="col-sm-9">
						<input type="file" name="testFile" required accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel">
					</div>
				</div>
				<span style="color:red">注：试题文件务必按照模板格式</span>
			</div>
			<input type="hidden" name="coursePlanId">
			<input type="hidden" name="uploadTestsTaskId">
			<div class="modal-footer" style="text-align:center">
				<button type="submit" id="uploadTestSubmit" class="btn btn-primary" disabled>上传</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
		</form>
		</div>
		</div>
      </div>
      <script>
      if(localStorage.isRefresh==1){
    	  localStorage.isRefresh=null;
    	  location.reload();
      }
      require(['staffComplete'],function (staffComplete){
  		new staffComplete().render($('#applyer'),function ($item){
  			$("input[name='applyerId']").val($item.data("userId"));
  		});
  		new staffComplete().render($('#applyerForAdvance'),function ($item){
  			$("input[name='applyerIdForAdvance']").val($item.data("userId"));
  		});
  		new staffComplete().render($('#applyerForPay'),function ($item){
  			$("input[name='applyerIdForPay']").val($item.data("userId"));
  		});
  	  });
	   	function checkEmpty(){
	   		if($("#applyer").val()==''){
	   			$("input[name='applyerId']").val('');
	   		}
	   	}
	   	function checkEmptyForAdvance(){
	   		if($("#applyerForAdvance").val()==''){
	   			$("input[name='applyerIdForAdvance']").val('');
	   		}
	   	}
	   	function checkEmptyForPay(){
	   		if($("#applyerForPay").val()==''){
	   			$("input[name='applyerIdForPay']").val('');
	   		}
	   	}
		$(document).click(function (event) {
			if ($("input[name='applyerIdForAdvance']").val()=='')
			{
				$("#applyerForAdvance").val("");
			}
			if ($("input[name='applyerId']").val()=='')
			{
				$("#applyer").val("");
			}
			if ($("input[name='applyerIdForPay']").val()=='')
			{
				$("#applyerForPay").val("");
			}
		});
		//全选checkbox响应其他checkbox的选中事件
		var fullCkOrNot = function(){
		    var allCB = $(".all");
		    if($(".check:checked").length == $(".check").length){
		        allCB.prop("checked",true);
		    }else{
		   	 allCB.prop("checked",false);
			}
		}
		//全选checkbox点击事件
		$(".all").on("click",function(){
			 if($(".all").prop("checked")){
				 $(".check").each(function(){
					 $(this).prop("checked",true);
				 });
			 }else{
				 $(".check").each(function(){
					 $(this).prop("checked",false);
				 });
			 }
		});
		function CommentOrNot(coursePlanId, taskId){
			layer.confirm("是否进行评价",{btn: ['是', '否'], offset:'100px'},function(index){
				layer.close(index);
				location.href="train/CommentOrNot?coursePlanId="+coursePlanId+"&taskId="+taskId+"&result="+1;
				Load.Base.LoadingPic.FullScreenShow(null);
			},
			function(index){
				layer.close(index);
				location.href="train/CommentOrNot?coursePlanId="+coursePlanId+"&taskId="+taskId+"&result="+2;
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}
      </script>
  </body>
</html>
