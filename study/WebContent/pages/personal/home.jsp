<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html> 
<html lang="zh-CN">
<head>
<link href="/assets/css/base.css" rel="stylesheet" type="text/css" />
<link href="/assets/css/global.css" rel="stylesheet" type="text/css" />
<link href="/assets/js/layer/skin/default/layer.css" rel='stylesheet'/>
<link rel="stylesheet" type="text/css" href="/assets/css/gdt-style.css">
<script src="/assets/js/layer/layer.js"></script>
<script src="/assets/icon/iconfont.js"></script>
<script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
<script type="text/javascript">

	var confirmRight=function(id){
		layer.confirm("是否确认有效？",{offset:'100px'},function (){
			$.ajax({
				url:'/personal/confirmSalary',
				data:{id:id},
				success:function (result){
					location.reload();
				}
			})
		},function (){
			layer.closeAll();
		})
	};

	var tcyy=function (id){
		layer.prompt({title: '请指出有问题的地方', formType: 3,offset:'100px'},function (content,index){
			layer.close(index);
			$.ajax({
				url:'/personal/againestSalary',
				data:{id:id,content:content},
				type:'post',
				dataType:'json',
				success:function (result){
					if(result.success=="1"){
						layer.alert("修改成功",{offset:'100px'},function(){
							location.reload();
						})
					}else{
						layer.alert("修改失败",{offset:'100px'})
					}
				}
			})
		});
	}
	$(function() {
		$('#cheakModal').on('hide.bs.modal', function () {
			window.location.reload();
		});
		
		if($("#hasSignin").val()==1){
			$("#signinButton").text("今日已签到");
			$("#signinButton").removeAttr("href");
			$("#signinButton").removeAttr("onclick");
			$("#signinButton").attr("class", "icon_signIn_selected");
		}
		$("#late_a").tab("show");
		$("[data-toggle='tooltip']").tooltip();
		if('${showVersionNotice}'=='true'){
			$("#versionNotice").modal("show");
		}
	});
	function readMessage(ntcID, userID) {
		$.ajax({
			url:'personal/updateNoticeActor',
	        type:'post',
	        data:{ntcID:ntcID,userID:userID},
	        dataType:'json',
	        success:function(data){
	        	if (data.errorMessage!=null && data.errorMessage.length!=0) {
					layer.alert("操作失败", {offset:'100px'});
					return;
				}
	        	
	        	$("#exampleModalLabel").html(data.noticeVO.ntcTitle);
	        	$("#ntcContent").html(data.noticeVO.ntcContent);
	        	if(data.noticeVO.ntcTitle!="快递领取通知"){
	        		$("#zhu").parent().remove();
	        	}
	        	var attachments = '';
	        	data.attachments.forEach(function(value, index){
	        		attachments += "<a href='administration/notice/download?attachmentPath="+value.softURL+"&attachmentName="+value.softName+"'>"+value.softName+"</a><br>";
	        	});
	        	$("#attachments").html(attachments);
	        	$("#cheakModal").modal('show');
	        }
			
		});
	}	
	var Add=function (value){
		if(/.*\+.*/.test(value+"")){
			try{
				value=eval(value);
			}catch(ignore){
			}
		}
		return value;
	}
	var prevArray=["部门","姓名","基础工资","绩效奖金","绩效","加班补贴","满勤","应抵扣金额","其他扣除","迟到扣除","扣养老","扣医保","扣失业","大病","扣住房公积金","扣个税","应发工资","合伙人","实发工资","主动放弃"];
	function showSalary (detail,key,year,month,deptName,userName){
		var detail=detail.match(/\[(.*)\]\s*/)[1].split(",");
		$("#exampleModalLabel").html(year+"年"+month+"月 工资详情");
		var resultContent=[];
		var flag = true;
		prevArray.forEach(function (value,index){
			if(index>1){
				if(value=='合伙人'){
					if(detail[index-2]==0){
						flag = false;
					}
				}else if(flag && detail[index-2] != undefined){
					resultContent.push(prevArray[index]+":"+detail[index-2])
				}
			}else{
				if(index==0){
					resultContent.push(prevArray[index]+":"+deptName);
				}else{
					resultContent.push(prevArray[index]+":"+userName);
				}
			}
		})
    	$("#ntcContent").html(resultContent.join("</br>"));
    	$("#zhu").parent().remove();
    	$("#cheakModal").modal('show');
	}
	function getNotice(ntcID) {
		$.ajax({
			url:'administration/notice/getNoticeByntcID',
	        type:'post',
	        data:{ntcID:ntcID},
	        dataType:'json',
	        success:function(data){
	        	if (data.errorMessage!=null && data.errorMessage.length!=0) {
					layer.alert("操作失败", {offset:'100px'});
					return;
				}
	        	
	        	$("#exampleModalLabel").html(data.noticeVO.ntcTitle);
	        	$("#ntcContent").html(data.noticeVO.ntcContent.replace(/\n/g,"<br/>").replace(/\s/g, "&nbsp;"));
	        	var attachments = '';
	        	data.attachments.forEach(function(value, index){
	        		attachments += "<a href='administration/notice/download?attachmentPath="+value.softURL+"&attachmentName="+value.softName+"'>"+value.softName+"</a><br>";
	        	});
	        	$("#attachments").html(attachments);
	        	$("#zhu").parent().remove();
	        	$("#cheakModal").modal('show');
	        }
			
		});
	}
	
	function signin() {
		$.ajax({
			url:'signin/signin',
			type:'post',
			data:{},
			dataType:'json',
			success:function (data){
				if (data.errorMessage!=null && data.errorMessage.length!=0) {
					alert(data.errorMessage);
				} else {
					layer.alert("签到成功！", {offset:'100px'});
				}
				$("#signinButton").text("今日已签到");
				$("#signinButton").removeAttr("href");
				$("#signinButton").removeAttr("onclick");
				$("#signinButton").attr("class", "icon_signIn_selected");
			}
		});
	}
</script>
<style type="text/css">
	.modal-body p {margin:0 0 10;}
	.modal-body h4 {margin-top:10px; margin-bottom:10px;}
	#lateAndLeave{margin-left:3px}
	#lateAndLeave ul li{border-bottom-color:#fff;
						padding: 0px 0px;
						margin-top:3px;
						}
	#lateAndLeave ul li a{height:33px}
	#lateRank, #leaveRank{
		border-collapse:collapse;
		width:100%;
		margin-top:5px;
	}
	#lateRank tr td, #leaveRank tr td{word-wrap:break-word;font-size:10px;padding:8px 7px;text-align:center}
	#lateRank tr th, #leaveRank tr th{font-size: 10px;text-align:center}
	.icon {
	   width: 3em; height: 3em;
	   vertical-align: -0.15em;
	   fill: currentColor;
	   overflow: hidden;
	}
	#showAgain{-webkit-appearance: none;outline: none;display:none;margin-bottom: -6%;}
	#showAgain{width:20px;height:20px;display:inline-block;background:url(assets/images/checkbox.gif)  no-repeat;background-position:0 0;}
	#showAgain:checked{background-position:0 -21px}
	#showAgain, #showAgain+label{cursor:pointer}
	
	
	#container{
	    position:relative;
	    left:150px;
	    overflow:hidden;
	    width:700px;
	    height:25px;
	    line-height:25px;
 	}
 
	#content{
	    position:absolute;
	    left:0;
	    top:0;
	    white-space:nowrap;
	}
</style>
</head>
<body>
	<!-- <div id="codeContainer" style="position:absolute;top:20%;left:38%;display:none"></div> -->
	<div class="container-fluid">
      <div class="row">
        <%@include file="/pages/personal/panel.jsp" %>	
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
        	<div style="color: #FFA500;margin: 0px 2%;font-size: 16px;height: 20px;line-height: 40px;">
        		<%-- <div style="padding: 0px 20px;">
	        		${whatsTheDate }
	        		&nbsp&nbsp&nbsp
	        		${dayOfTheWeek }
	        		&nbsp&nbsp&nbsp
	        		亲爱的，这是你在智造链的第 
	        		${dayDiff }
        		</div> --%>
        		
        		
        		<div id="container">
				     <div id="content">
				     	${whatsTheDate }
					        		&nbsp&nbsp&nbsp
					        		${dayOfTheWeek }
					        		&nbsp&nbsp&nbsp
					        		亲爱的，这是你在智造链的第 
					        		${dayDiff }
				     </div>
				 </div>
				 <script type="text/javascript">
				 if(!window.rollWord){
				      var rollWord = {
				           container:document.getElementById("container"),
				            content:document.getElementById("content"),
				            _containerWidth:1,
				            _contentWidth:1,
				            _speed:1,
				            setSpeed:function(opt){
				                  var This = this;
				                  This._speed = opt;
				            },
				            setContainerWidth:function(){
				                  var This = this;
				                  This._containerWidth = This.container.offsetWidth;
				            },
				            setContentWidth:function(){
				                  var This = this;
				                  This._contentWidth = This.content.offsetWidth;
				            },
				            roll:function(){
				                  var This = this;
				                  This.content.style.left = parseInt(This._containerWidth) + "px";
				                  var time = setInterval(function(){This.move()},20);
				                  This.container.onmouseover = function(){
				                       clearInterval(time);
				                  };
				                  This.container.onmouseout = function(){
				                       time = setInterval(function(){This.move()},20);
				                  };
				            },
				            move:function(){
				                  var This = this;
				                  if(parseInt(This.content.style.left)+This._contentWidth > 0)
				                  {
				                       This.content.style.left = parseInt(This.content.style.left)-This._speed + "px";
				                  }
				                   else
				                   {
				                      This.content.style.left = parseInt(This._containerWidth) + "px";
				                   }                 
				            },
				            init:function(opt){
				                 var This = this;
				                 var speed = opt.speed || 1;
				                 This.setSpeed(speed);
				                 This.setContainerWidth();
				                 This.setContentWidth();
				                 This.roll();
				            }
				       }
				}
				       rollWord.init({speed:2});
				 </script>
		
        	</div>
        	<div class="shortcut_link"></div>
        	<div class="index_link" style="margin-top:1%;">
        	<ul>
			<li><a onclick="goPath('personal/showVacationDiagram')" href="javascript:void(0)" class="icon_holiday">请假申请</a></li>
			<li><a onclick="goPath('personal/workReportDetail')" href="javascript:void(0)" class="icon_job">工作汇报</a></li>
			<li><a href="javascript:void(0);" class="icon_signIn_default" id="signinButton" onclick="signin()">未签到</a><input type="hidden" id="hasSignin" value="${hasSignin }" /></li>
			</ul>
			</div>
			<div class="blank20"></div>
			<div class="index_column_wrap clearfix">
			<div class="index_column">
			<h1>最新公告<a onclick="goPath('information/notice/findNoticeList1')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<!-- <div class="cont">
			<p>征集全国科普工作先进集体和先进工作者的通知征集全国科普工作先进集体和先进工作者的通知征集全国科普工作先进集体和先进工作者的通知</p>
			</div> -->
			<ul>
			<c:if test="${not empty noticeVOs }">
			<s:iterator id="noticeVO" value="#request.noticeVOs" status="count">
				<li><a href="javascript:void(0)" title="<s:property value="#noticeVO.ntcTitle"/>" onclick="getNotice(<s:property value='#noticeVO.ntcID'/>)" ><s:if test="#noticeVO.isTop==1">【置顶】</s:if><s:if test="#noticeVO.ntcTitle.length() > 11"><s:property value="#noticeVO.ntcTitle.substring(0, 11)"/>...</s:if><s:else><s:property value="#noticeVO.ntcTitle"/></s:else></a><span><s:property value="#noticeVO.noticeDate"/></span></li>
			</s:iterator>
			</c:if>
			<c:if test="${empty noticeVOs }">
				<li>暂无</li>
			</c:if>
			</ul>
			</div>
			
			<div class="index_column">
			<h1>最新消息<a onclick="goPath('personal/findNoticeList')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<ul>
			<c:if test="${not empty messageVOs }">
			<s:iterator id="messageVO" value="#request.messageVOs" status="count">
				<li><a href="javascript:void(0)" title="<s:property value="#messageVO.ntcTitle"/>" onclick="readMessage(<s:property value='#messageVO.ntcID'/>, '<s:property value="#messageVO.userID"/>')" <s:if test="#messageVO.status==0">style="color:#f24451;"</s:if>><s:if test="#messageVO.ntcTitle.length() > 16"><s:property value="#messageVO.ntcTitle.substring(0, 16)"/>...</s:if><s:else><s:property value="#messageVO.ntcTitle"/></s:else></a><span><s:property value="#messageVO.noticeDate"/></span></li>
			</s:iterator>
			</c:if>
			<c:if test="${empty messageVOs }">
				<li>暂无</li>
			</c:if>
			</ul>
			</div>
			<div class="index_column">
			<h1>待办列表<a onclick="goPath('personal/findTaskList?type=1')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<ul>
			<c:if test="${not empty taskVOs }">
			<s:iterator id="taskVO" value="#request.taskVOs" status="count">
				<c:if test="${!empty taskVO.title}">
				<li><a title="<s:property value="#taskVO.title"/>" onclick="goPath('')" href="<s:if test="#taskVO.businessKey == 'Vacation'">personal/findTaskList?type=1</s:if>
				             <s:if test="#taskVO.businessKey == 'Assignment'">personal/findTaskList?type=2</s:if>
				             <s:if test="#taskVO.businessKey == 'Resignation'">personal/findTaskList?type=3</s:if>
				             <s:if test="#taskVO.businessKey == 'Formal'">personal/findTaskList?type=4</s:if>
				             <s:if test="#taskVO.businessKey == 'Reimbursement'">personal/findTaskList?type=5</s:if>
				             <s:if test="#taskVO.businessKey == 'Email'">personal/findTaskList?type=6</s:if>
				             <s:if test="#taskVO.businessKey == 'Card'">personal/findTaskList?type=7</s:if>
				             <s:if test="#taskVO.businessKey == 'Audit'">personal/findTaskList?type=8</s:if>
				             <s:if test="#taskVO.businessKey == 'BussinessTrip'">personal/findTaskList?type=9</s:if>
				             <s:if test="#taskVO.businessKey == 'SocialSecurity'">personal/findTaskList?type=10</s:if>
				             <s:if test="#taskVO.businessKey == 'ChopBorrow'">personal/findTaskList?type=11</s:if>
				             <s:if test="#taskVO.businessKey == 'IDBorrow'">personal/findTaskList?type=12</s:if>
				             <s:if test="#taskVO.businessKey == 'ContractSign'">personal/findTaskList?type=13</s:if>
				             <s:if test="#taskVO.businessKey == 'CarUse'">personal/findTaskList?type=14</s:if>
				             <s:if test="#taskVO.businessKey == 'Advance'">personal/findTaskList?type=15</s:if>
				             <s:if test="#taskVO.businessKey == 'Vitae'">personal/findTaskList?type=16</s:if>
				             <s:if test="#taskVO.businessKey == 'SoftPerformance'">personal/findTaskList?type=17</s:if>
				             <s:if test="#taskVO.businessKey == 'CertificateBorrow'">personal/findTaskList?type=18</s:if>
				             <s:if test="#taskVO.businessKey == 'CommonSubject'">personal/findTaskList?type=19</s:if>
				             <s:if test="#taskVO.businessKey == 'ContractBorrow'">personal/findTaskList?type=20</s:if>
				             <s:if test="#taskVO.businessKey == 'ChangeContract'">personal/findTaskList?type=21</s:if>
				             <s:if test="#taskVO.businessKey == 'bankAccount'">personal/findTaskList?type=22</s:if>
				             <s:if test="#taskVO.businessKey == 'DestroyChop'">personal/findTaskList?type=23</s:if>
				             <s:if test="#taskVO.businessKey == 'purchaseProperty'">personal/findTaskList?type=24</s:if>
				             <s:if test="#taskVO.businessKey == 'CarveChop'">personal/findTaskList?type=25</s:if>
				             <s:if test="#taskVO.businessKey == 'handleProperty'">personal/findTaskList?type=26</s:if>
				             <s:if test="#taskVO.businessKey == 'transferProperty'">personal/findTaskList?type=27</s:if>
				             <s:if test="#taskVO.businessKey == 'shopApply'">personal/findTaskList?type=28</s:if>
				             <s:if test="#taskVO.businessKey == 'shopPayApply'">personal/findTaskList?type=29</s:if>
				             <s:if test="#taskVO.businessKey == 'workOvertime'">personal/findTaskList?type=30</s:if>
				             <s:if test="#taskVO.businessKey == 'classHour'">personal/findTaskList?type=33</s:if>
				             <s:if test="#taskVO.businessKey == 'problemOrder'">personal/findTaskList?type=34</s:if>
				             <s:if test="#taskVO.businessKey == 'Payment'">personal/findTaskList?type=35</s:if>
				             <s:if test="#taskVO.businessKey == 'viewWorkReport'">personal/findTaskList?type=36</s:if>
				             <s:if test="#taskVO.businessKey == 'MorningMeeting'">personal/findTaskList?type=37</s:if>
				             <s:if test="#taskVO.businessKey == 'project'">personal/findTaskList?type=38</s:if>
				             ">
				             <s:if test="#taskVO.title.length() > 10"><s:property value="#taskVO.title.substring(0, 10)"/>...</s:if><s:else><s:property value="#taskVO.title"/></s:else></a><span><s:property value="#taskVO.requestDate"/></span>
				</li>
				</c:if>
			</s:iterator>
			</c:if>
			<c:if test="${empty taskVOs }">
				<li>暂无</li>
			</c:if>
			</ul>
			</div>
			<div class="index_column">
			<h1>岗位职责<a onclick="goPath('personal/findGroupDetails')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<ul>
			<c:if test="${not empty groupDetailVOs }">
			<c:forEach items="${groupDetailVOs}" var="groupDetailVO" begin="0" end="${fn:length(groupDetailVOs)>4?4:fn:length(groupDetailVOs)}">
				<li><a href="personal/findGroupDetails">${groupDetailVO.companyName}-${groupDetailVO.departmentName}-${groupDetailVO.positionName}</a></li>
			</c:forEach>
			</c:if>
			</ul>
			</div>
			
			
			<div class="index_column">
			<h1>历史工资</h1>
			<ul>
			<c:if test="${not empty softGroupEntities }">
			<s:iterator id="softGroupEntity" value="#request.softGroupEntities" status="count">
				<li><a href="javascript:showSalary('${softGroupEntity.detailList}','${softGroupEntity.status}','${softGroupEntity.year}','${softGroupEntity.month}','${userDept}','${userName}')"><s:property value="#softGroupEntity.year"/>-<s:property value="#softGroupEntity.month"/></a> 
<%-- 					<c:if test="${softGroupEntity.status eq null or  softGroupEntity.status eq '' }"> --%>
<!-- 						(待确认)  -->
<%-- 						<a style="float:right" href="javascript:confirmRight(${softGroupEntity.id})">|确认无误</a> --%>
<%-- 						 <a style="float:right" href="javascript:tcyy(${softGroupEntity.id})">提出异议</a> --%>
<%-- 					</c:if>  --%>
<%-- 					<c:if test="${softGroupEntity.status eq 2}"> --%>
<!-- 						(已确认) -->
<%-- 					</c:if>  --%>
<%-- 					<c:if test="${softGroupEntity.status eq 1}"> --%>
<!-- 						(待人事重新审核) -->
<%-- 					</c:if>  --%>
				</li>
			</s:iterator>
			</c:if>
			</ul>
			</div>
			
 			<div class="index_column">
			<h1>考勤黑榜<a onclick="goPath('attendance/lateAndLeaveList?companyId=${companyId}')" href="javascript:void(0)" class="fr" style="color:#888;font-size:14px;">更多>></a></h1>
			<div id="lateAndLeave">
				<ul class="nav nav-tabs"  role="tablist" style="font-size:10px;">
				 <li role="presentation"><a id="late_a" href="#late" role="tab" data-toggle="tab">迟到</a></li>
				<!--  <li role="presentation"><a id="leave_a" href="#leave" role="tab" data-toggle="tab">早退</a></li> -->
				</ul>
				<div class="tab-content">
				<div role="tabpanel" class="tab-pane" id="late">
					<table id="lateRank">
							<tr>
								<th style="width:15%">名次</th>
								<th style="width:15%">次数</th>
								<th style="width:30%">姓名</th>
								<th style="width:40%">部门</th>
							</tr>
							<c:forEach items="${lateObjs}" var="lateObj" begin="0" end="2">
								<tr>
									<td>
									<c:if test="${lateObj[0]==1}"><img style="width:17px;height:17px" src="assets/images/gold.png"></c:if>
									<c:if test="${lateObj[0]==2}"><img style="width:17px;height:17px" src="assets/images/yin.png"></c:if>
									<c:if test="${lateObj[0]==3}"><img style="width:17px;height:17px" src="assets/images/tong.png"></c:if>
									</td>
									<td>${lateObj[1]}</td>
									<td>${lateObj[2]}</td>
									<td>${lateObj[3]}</td>
								</tr>
							</c:forEach>
					</table>
				</div>
				<div role="tabpanel" class="tab-pane" id="leave">
					<table id="leaveRank">
							<tr>
								<th style="width:15%">名次</th>
								<th style="width:15%">次数</th>
								<th style="width:30%">姓名</th>
								<th style="width:40%">部门</th>
							</tr>
							<c:forEach items="${leaveObjs}" var="leaveObj" begin="0" end="2">
								<tr>
									<td>
									<c:if test="${leaveObj[0]==1}"><img style="width:17px;height:17px" src="assets/images/gold.png"></c:if>
									<c:if test="${leaveObj[0]==2}"><img style="width:17px;height:17px" src="assets/images/yin.png"></c:if>
									<c:if test="${leaveObj[0]==3}"><img style="width:17px;height:17px" src="assets/images/tong.png"></c:if>
									</td>
									<td>${leaveObj[1]}</td>
									<td>${leaveObj[2]}</td>
									<td>${leaveObj[3]}</td>
								</tr>
							</c:forEach>
					</table>
				</div>
				</div>
			</div>
			<ul>
			</ul>
			</div>
			
			
			</div>
			<div class="blank20"></div>
			<div class="side-bar" title="系统问题反馈" data-toggle="tooltip" style="opacity:0.6"> 
				<a onclick="goPath('/personal/addProblemOrder')" href="javascript:void(0)" style="text-align: center">
					<svg class="icon" aria-hidden="true" style="margin-top:16%;fill:#fff">
             			<use xlink:href="#icon-bug"></use>
             		</svg>
				</a> 
			</div>
        </div>
      </div>
    </div>
    
    <div class="modal fade bs-example-modal-lg" id="cheakModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
	  <div class="modal-dialog modal-md" role="document">
	    <div class="modal-content" style="width:650px">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" style="text-align:center;font-weight:bold" id="exampleModalLabel"></h4><div class="container-fluid1"></div>
	      </div>
	      <div class="modal-body">
	      	<input type="hidden" id="groupDetailID" />
	      	<p id="ntcContent"></p>
	      	<div id="attachments"></div>
	      	<div>
	      	<hr>
	      	<h4>注：</h4>
	      	<p id="zhu">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;骑岸总部在门卫处领取；<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;南通分部在人事行政部领取；</p>
	      	</div>
	      
	      </div>
	      <div class="modal-footer">
	      <button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>
	      </div>
	    </div>
	  </div>
	</div>
	<div id="versionNotice" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" aria-hidden="true" onclick="closeNotice()">&times;</button>
				<h4 class="modal-title" id="myModalLabel" style="color:#428bca">系统更新</h4>
			</div>
			<div class="modal-body">
					<label style="font-weight:bold">最新版本：</label><span>${versionFunctionInfo.versionName}</span>
					<div>
						<label style="font-weight:bold">更新内容：</label>
						<c:forEach items="${versionFunctionInfo.function}" var="func" varStatus="status">
							<div>${status.index+1}、${func}</div>
						</c:forEach>
					</div>
					<br>
					<a href="javascript:void(0)" onclick="goPath('information/version/findVersionInfoList')" data-dismiss="modal">更多版本>></a>
			</div>
			<div class="modal-footer">
				<div style="float:left">
				<input id="showAgain" type="checkbox" name="showAgain" value="false"><label for="showAgain">不再显示</label>
				</div>
			</div>
		</div>
	</div>
	</div>
	<script>
/* 	$(function(){
		if('${bind}'=='false'){
			var appID = "wx9c2800cfc46895f0";
			var uri = "http://www.zhizaolian.com:9090";
			var obj = new WxLogin({
			  id: "codeContainer",
			  appid: appID,
			  scope: "snsapi_login", 
			  redirect_uri: encodeURI(uri),
			  state: guid()
			});
			$("#codeContainer").css("display","block");
			$(".row").css("display", "none");
			$(".navbar-fixed-top").css("display", "none");
		}
	}); */
	function closeNotice(){
		var showAgain = $("#showAgain:checked").val();
		$("#versionNotice").modal("hide");
		if(showAgain=='false'){
			location.href = "/information/version/addVersionNoticeActor";
		}
	}
/* 	function guid() {
	    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
	        return v.toString(16);
	    });
	} */
	</script>
</body>
</html>
