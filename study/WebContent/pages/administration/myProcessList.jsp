<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@  taglib  uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html> 
<html lang="zh-CN">

<head>
<link href="/assets/css/dark.css" rel='stylesheet'/>
<style>
.table td{
	word-wrap:break-word;word-break:break-all;
}
._title{text-align:center;background:#efefef}
.tab{color:#555555;box-shadow:0px 1px 5px #dddddd;}
.tab table{width:100%;border-collapse:collapse;}
.tab table tr td{border:1px solid #ddd;word-wrap:break-word;word-break:break-all;font-size:14px;}
.tab table tbody tr td{height:auto;line-height:20px;padding:10px 10px;}
.tab table tr .title {text-align:center;color:#000;width:15%}
.icon {
width: 1.5em; height: 1.5em;
vertical-align: -0.15em;
fill: currentColor;
overflow: hidden;
}
</style>
<script src="/assets/icon/iconfont.js"></script>
<script type="text/javascript">

	$(function () {
		$("[data-toggle='tooltip']").tooltip();
		set_href();
		var type = $("#type").val();
		//key:enum中的index,value页面tab 页的index\
		var tabShowLink={
				6:0,
				7:1,
				9:2,
				11:3,
				18:6,
				12:8,
				13:9,
				14:11,
				16:13,
				19:17,
				20:7,
				21:10,
				22:11,
				23:4,
				24:14,
				25:5,
				26:15,
				27:16,
				28:18,
				29:19,
				36:20,
				40:21
		};
		for(key in tabShowLink){
			if(type== key){
				$("#myTab li:eq("+tabShowLink[key]+") a").tab("show");
			}
			$("#myTab li:eq("+tabShowLink[key]+") a").data("index",key).click(function(e) {
				e.preventDefault();
				window.location.href = "administration/process/findMyProcessList?type="+$(this).data("index");
				Load.Base.LoadingPic.FullScreenShow(null);
			});
		}
	});
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
            <input type="hidden" id="type" value="${type }" />
        	<ul class="nav nav-tabs" role="tablist" id="myTab">
			  <li role="presentation"><a href="#email" role="tab" data-toggle="tab">公司邮箱申请</a></li>
			  <li role="presentation"><a href="#card" role="tab" data-toggle="tab">工牌申领</a></li>
			  <li role="presentation"><a href="#trip" role="tab" data-toggle="tab">出差预约申请</a></li>
			  <li role="presentation"><a href="#chopBorrow" role="tab" data-toggle="tab">公章申请</a></li>
			  <li role="presentation"><a href="#chopDestroy" role="tab" data-toggle="tab">印章缴销</a></li>
			  <li role="presentation"><a href="#carveChop" role="tab" data-toggle="tab">印章刻制</a></li>
			  <li role="presentation"><a href="#certificateBorrow" role="tab" data-toggle="tab">证件申请</a></li>
			  <li role="presentation"><a href="#contractBorrow" role="tab" data-toggle="tab">合同审阅</a></li>
			  <li role="presentation"><a href="#idBorrow" role="tab" data-toggle="tab">身份证借用</a></li>
			  <li role="presentation"><a href="#contract" role="tab" data-toggle="tab">合同签署</a></li>
			  <li role="presentation"><a href="#changeContract" role="tab" data-toggle="tab">合同变更或解除</a></li>
			  <li role="presentation"><a href="#bankAccount" role="tab" data-toggle="tab">开设、变更及撤销银行账户</a></li>
			  <li role="presentation"><a href="#carUse" role="tab" data-toggle="tab">车辆预约</a></li>
			  <li role="presentation"><a href="#vitae" role="tab" data-toggle="tab">招聘申请</a></li>
			  <li role="presentation"><a href="#purchaseProperty" role="tab" data-toggle="tab">财产购置</a></li>
			  <li role="presentation"><a href="#handleProperty" role="tab" data-toggle="tab">资产处置</a></li>
			  <li role="presentation"><a href="#transferProperty" role="tab" data-toggle="tab">资产调拨</a></li>
			  <li role="presentation"><a href="#commonSubject" role="tab" data-toggle="tab">通用流程</a></li>
			  <li role="presentation"><a href="#shopApply" role="tab" data-toggle="tab">店铺申请</a></li>
			  <li role="presentation"><a href="#shopPayApply" role="tab" data-toggle="tab">店铺付费申请</a></li>
			  <li role="presentation"><a href="#viewReport" role="tab" data-toggle="tab">日报查看申请</a></li>
			  <li role="presentation"><a href="#publicEvent" role="tab" data-toggle="tab">公关申请</a></li>
		    </ul>
		    
		    <div class="tab-content">
		    	<div role="tabpanel" class="tab-pane" id="email">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>邮箱申请人</th>
			                  <th>邮箱地址</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty emailVOs}">
			              	<s:set name="email_id" value="(#request.page-1)*(#request.limit)"></s:set>
			              	<s:iterator id="emailVO" value="#request.emailVOs" status="count">
			              		<tr>
			              			<td class="col-sm-1"><s:property value="#email_id+1"/></td>
			              			<td class="col-sm-1"><a href="administration/process/processHistory?processInstanceID=<s:property value='#emailVO.processInstanceID'/>"><s:property value="#emailVO.title"/></a></td>
			              			<td class="col-sm-1"><s:property value="#emailVO.requestDate"/></td>
			              			<td class="col-sm-1"><s:property value="#emailVO.requestUserName"/></td>
			              			<td class="col-sm-1"><s:property value="#emailVO.address"/></td>
			              			<td class="col-sm-1"><s:property value="#emailVO.assigneeUserName"/></td>
			              			<td class="col-sm-1"><s:property value="#emailVO.status"/></td>
			              		</tr>
			              		<s:set name="email_id" value="#email_id+1"></s:set>
			              	</s:iterator>
			              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="card">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>工牌申领人</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>附件</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty cardVOs}">
			              	<s:set name="card_id" value="(#request.page-1)*(#request.limit)"></s:set>
			              	<s:iterator id="cardVO" value="#request.cardVOs" status="count">
			              		<tr>
			              			<td class="col-sm-1"><s:property value="#card_id+1"/></td>
			              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#cardVO.processInstanceID'/>')" href="javascript:void(0)"><s:property value="#cardVO.title"/></a></td>
			              			<td class="col-sm-1"><s:property value="#cardVO.requestDate"/></td>
			              			<td class="col-sm-1"><s:property value="#cardVO.requestUserName"/></td>
			              			<td class="col-sm-2"><s:property value="#cardVO.assigneeUserName"/></td>
			              			<td class="col-sm-1"><s:property value="#cardVO.status"/></td>
			              			<td class="col-sm-3">
			              			<s:if test="#cardVO.attachmentFileName.length > 0">
			              			<s:iterator id="attachment" value="#cardVO.attachmentFileName" status="st">
			              				<span><img style="height:100px;" alt="" src="http://www.zhizaolian.com:9000/administration/card/<s:property value="#attachment"/>"></span> 
			              			</s:iterator>
			              			</s:if>
			              			</td>
			              		</tr>
			              		<s:set name="card_id" value="#card_id+1"></s:set>
			              	</s:iterator>
			              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	
		    	<div role="tabpanel" class="tab-pane" id="trip">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>出差申领人</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty tripVOs}">
			              	<s:set name="tripIndex" value="(#request.page-1)*(#request.limit)"></s:set>
			              	<s:iterator id="tripVO" value="#request.tripVOs" status="count">
			              		<tr>
			              			<td class="col-sm-1"><s:property value="#tripIndex+1"/></td>
			              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#tripVO.processInstanceID'/>')" href="javascript:void(0)"><s:property value="#tripVO.title"/></a></td>
			              			<td class="col-sm-1"><s:property value="#tripVO.requestDate"/></td>
			              			<td class="col-sm-1"><s:property value="#tripVO.requestUserName"/></td>
			              			<td class="col-sm-2"><s:property value="#tripVO.assigneeUserName"/></td>
			              			<td class="col-sm-1"><s:property value="#tripVO.status"/></td>
			              		</tr>
			              		<s:set name="tripIndex" value="#tripIndex+1"></s:set>
			              	</s:iterator>
			              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="chopBorrow">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th style="width:5%">序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>印章名称</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty chopBorrowVos}">
		              	<s:set name="chopBorrowVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="chopBorrowVo" value="#request.chopBorrowVos" status="count">
		              		<tr>
		              			<td><s:property value="#chopBorrowVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#chopBorrowVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#chopBorrowVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#chopBorrowVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#chopBorrowVo.chop_Name"/></td>
		              			<td class="col-sm-1"><s:property value="#chopBorrowVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#chopBorrowVo.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#chopBorrowVo.processInstanceID'/>&businessType=ChopBorrow')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="chopBorrowVos_id" value="#chopBorrowVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="chopDestroy">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>印章名称</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty chopDestroyVos}">
		              	<s:set name="chopDestroyVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="chopDestroyVo" value="#request.chopDestroyVos" status="count">
		              		<tr>
		              			<td style="width:4%" class="col-sm-1"><s:property value="#chopDestroyVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#chopDestroyVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#chopDestroyVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#chopDestroyVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#chopDestroyVo.chopName"/></td>
		              			<td class="col-sm-1"><s:property value="#chopDestroyVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#chopDestroyVo.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#chopDestroyVo.processInstanceID'/>&businessType=DestroyChop')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="chopDestroyVos_id" value="#chopDestroyVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="carveChop">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>印章名称</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty carveChopVos}">
		              	<s:set name="carveChopVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="carveChopVo" value="#request.carveChopVos" status="count">
		              		<tr>
		              			<td style="width:4%" class="col-sm-1"><s:property value="#carveChopVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#carveChopVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#carveChopVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#carveChopVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#carveChopVo.chopName"/></td>
		              			<td class="col-sm-1"><s:property value="#carveChopVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#carveChopVo.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#carveChopVo.processInstanceID'/>&businessType=CarveChop')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="carveChopVos_id" value="#carveChopVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="purchaseProperty">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty purchasePropertyVos}">
		              	<s:set name="purchasePropertyVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="purchasePropertyVo" value="#request.purchasePropertyVos" status="count">
		              		<tr>
		              			<td style="width:4%" class="col-sm-1"><s:property value="#purchasePropertyVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#purchasePropertyVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#purchasePropertyVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#purchasePropertyVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#purchasePropertyVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#purchasePropertyVo.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#purchasePropertyVo.processInstanceID'/>&businessType=purchaseProperty')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="purchasePropertyVos_id" value="#purchasePropertyVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="handleProperty">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty handlePropertyVos}">
		              	<s:set name="handlePropertyVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="handlePropertyVo" value="#request.handlePropertyVos" status="count">
		              		<tr>
		              			<td style="width:4%" class="col-sm-1"><s:property value="#handlePropertyVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#handlePropertyVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#handlePropertyVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#handlePropertyVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#handlePropertyVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#handlePropertyVo.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#handlePropertyVo.processInstanceID'/>&businessType=handleProperty')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="handlePropertyVos_id" value="#handlePropertyVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="transferProperty">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty transferPropertyVos}">
		              	<s:set name="transferPropertyVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="transferPropertyVo" value="#request.transferPropertyVos" status="count">
		              		<tr>
		              			<td style="width:4%" class="col-sm-1"><s:property value="#transferPropertyVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#transferPropertyVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#transferPropertyVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#transferPropertyVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#transferPropertyVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#transferPropertyVo.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#transferPropertyVo.processInstanceID'/>&businessType=transferProperty')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="transferPropertyVos_id" value="#transferPropertyVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="shopApply">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>店铺名称</th>
			                  <th>平台</th>
			                  <th>申请时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty shopApplyVos}">
		              	<s:set name="shopApplyVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="shopApplyVo" value="#request.shopApplyVos" status="count">
		              		<tr>
		              			<td style="width:4%" class="col-sm-1"><s:property value="#shopApplyVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#shopApplyVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#shopApplyVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#shopApplyVo.shopName"/></td>
		              			<td class="col-sm-1"><s:property value="#shopApplyVo.platform"/></td>
		              			<td class="col-sm-1"><s:property value="#shopApplyVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#shopApplyVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#shopApplyVo.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#shopApplyVo.processInstanceID'/>&businessType=shopApply')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a>
		              			</td>
		              		</tr>
		              		<s:set name="shopApplyVos_id" value="#shopApplyVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="shopPayApply">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间/主管审批时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty shopPayApplyVos}">
		              	<s:set name="shopPayApplyVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="shopPayApplyVo" value="#request.shopPayApplyVos" status="count">
		              		<tr>
		              			<td style="width:4%" class="col-sm-1"><s:property value="#shopPayApplyVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#shopPayApplyVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#shopPayApplyVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#shopPayApplyVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#shopPayApplyVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#shopPayApplyVo.status"/></td>
		              			<td class="col-sm-1"><a onclick="showDetail('${shopPayApplyVo.spreadPayApplyIds}',
		              			'${shopPayApplyVo.pluginPayApplyIds}','${shopPayApplyVo.otherPayApplyIds}','${shopPayApplyVo.processInstanceID}',
		              			'${shopPayApplyVo.isProcess}')" href="javascript:void(0)">
		              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
									<use xlink:href="#icon-Detailedinquiry"></use>
								</svg>
		              			</a></td>
		              		</tr>
		              		<s:set name="shopPayApplyVos_id" value="#shopPayApplyVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="viewReport">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th style="width:4%">序号</th>
			                  <th style="width:8%">标题</th>
			                  <th style="width:10%">申请时间</th>
			                  <th style="width:15%">查看人员</th>
			                  <th style="width:15%">查看公司（部门）</th>
			                  <th style="width:7%">查看权限</th>
			                  <th style="width:7%">当前处理人</th>
			                  <th style="width:7%">流程状态</th>
			                </tr>
			              </thead>
		             	  <tbody>
							<c:forEach items="${viewWorkReportVos}" var="viewWorkReportVo" varStatus="status">
								<tr>
									<td>${limit*(page-1)+status.index+1}</td>
									<td><a onclick="goPath('administration/process/processHistory?processInstanceID=${viewWorkReportVo.processInstanceID}&selectedPanel=myProcessList')" href="javascript:void(0)">${viewWorkReportVo.title}</a></td>
									<td>${viewWorkReportVo.requestDate}</td>
									<td>${viewWorkReportVo.userNames==''||viewWorkReportVo.userNames==null ? '——':viewWorkReportVo.userNames}</td>
									<td>
										<c:if test="${empty viewWorkReportVo.companyAndDepList}">——</c:if>
											<c:forEach items="${viewWorkReportVo.companyAndDepList}" var="companyAndDep">
												${companyAndDep}<br>
											</c:forEach>
									</td>
									<td>${viewWorkReportVo.viewType}</td>
									<td>${viewWorkReportVo.assigneeUserName==null?'——':viewWorkReportVo.assigneeUserName}</td>
									<td>${viewWorkReportVo.status}</td>
								</tr>
							</c:forEach>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="publicEvent">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th style="width:3%">序号</th>
			                  <th style="width:10%">标题</th>
			                  <th style="width:10%">申请时间</th>
			                  <th style="width:20%">需公关事件说明</th>
			                  <th style="width:10%">截止时间</th>
			                  <th style="width:7%">当前处理人</th>
			                  <th style="width:5%">流程状态</th>
			                </tr>
			              </thead>
		             	  <tbody>
							<c:forEach items="${publicRelationEventVos}" var="publicRelationEventVo" varStatus="status">
								<tr>
									<td>${limit*(page-1)+status.index+1}</td>
									<td><a onclick="goPath('administration/process/processHistory?processInstanceID=${publicRelationEventVo.processInstanceID}&selectedPanel=myProcessList')" href="javascript:void(0)">${publicRelationEventVo.title}</a></td>
									<td>${publicRelationEventVo.requestDate}</td>
									<td>${publicRelationEventVo.eventDescription}</td>
									<td>${publicRelationEventVo.deadlineDate}</td>
									<td>${publicRelationEventVo.assigneeUserName==null?'——':publicRelationEventVo.assigneeUserName}</td>
									<td>${publicRelationEventVo.status}</td>
								</tr>
							</c:forEach>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="certificateBorrow">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>证件名称</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty certificateBorrowVos}">
		              	<s:set name="certificateBorrowVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="certificateBorrowVo" value="#request.certificateBorrowVos" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#certificateBorrowVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#certificateBorrowVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)">
		              			<s:property value="#certificateBorrowVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#certificateBorrowVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#certificateBorrowVo.certificateName"/></td>
		              			<td class="col-sm-1"><s:property value="#certificateBorrowVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#certificateBorrowVo.status"/></td>
		              		</tr>
		              		<s:set name="certificateBorrowVos_id" value="#certificateBorrowVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="contractBorrow">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>合同名称</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                </tr>
			              </thead>
		             		  <tbody>
			              	<c:if test="${not empty contractBorrowVos}">
		              	<s:set name="contractBorrowVos_id" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="contractBorrowVo" value="#request.contractBorrowVos" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#contractBorrowVos_id+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#contractBorrowVo.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)">
		              			<s:property value="#contractBorrowVo.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#contractBorrowVo.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#contractBorrowVo.contractName"/></td>
		              			<td class="col-sm-1"><s:property value="#contractBorrowVo.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#contractBorrowVo.status"/></td>
		              		</tr>
		              		<s:set name="contractBorrowVos_id" value="#contractBorrowVos_id+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="idBorrow">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                   <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>被借用者姓名</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty idBorrowVos}">
					              	<s:set name="idBorrowIndex" value="(#request.page-1)*(#request.limit)"></s:set>
					              	<s:iterator id="item" value="#request.idBorrowVos" status="count">
					              		<tr>
					              			<td class="col-sm-1"><s:property value="#idBorrowIndex+1"/></td>
					              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#item.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#item.title"/></a></td>
					              			<td class="col-sm-1"><s:property value="#item.requestDate"/></td>
					              			<td class="col-sm-1"><s:property value="#item.item_User_Name"/></td>
					              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
					              			<td class="col-sm-1"><s:property value="#item.status"/></td>
					              		</tr>
					              		<s:set name="idBorrowIndex" value="#idBorrowIndex+1"></s:set>
					              	</s:iterator>
				              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	
		    		<div role="tabpanel" class="tab-pane" id="contract">
<%-- 		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                   <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>负责人</th>
			                  <th>审核人</th>
			                  <th>签署人</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty constractVos}">
		              	<s:set name="constractIndex" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="item" value="#request.constractVos" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#constractIndex+1"/></td>
		              			<td class="col-sm-1"><a href="administration/process/processHistory?processInstanceID=<s:property value='#item.processInstanceID'/>&selectedPanel=findProcessList"><s:property value="#item.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#item.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#item.responsiblePersonName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.subjectPersonName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.signPersonName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.status"/></td>
		              		</tr>
		              		<s:set name="constractIndex" value="#constractIndex+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div> --%>
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>合同名称</th>
			                  <th>乙方</th>
			                  <th>申请时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty constractSignVos}">
				              	<s:set name="constractIndex" value="(#request.page-1)*(#request.limit)"></s:set>
				              	<s:iterator id="item" value="#request.constractSignVos" status="count">
		              			<tr>
			              			<td style="width:4%"><s:property value="#constractIndex+1"/></td>
			              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#item.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#item.title"/></a></td>
			              			<td class="col-sm-1"><s:property value="#item.contractName"/></td>
			              			<td class="col-sm-1"><s:property value="#item.otherCompanyName"/></td>
			              			<td class="col-sm-1"><s:property value="#item.requestDate"/></td>
			              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
			              			<td class="col-sm-1"><s:property value="#item.status"/></td>
			              			<td class="col-sm-1">
			              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#item.processInstanceID'/>&businessType=ContractSign')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
										<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
			              			</a>
			              			</td>
		              			</tr>
		              			<s:set name="constractIndex" value="#constractIndex+1"></s:set>
			              		</s:iterator>
			              		</c:if>
				              	</tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="changeContract">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>合同名称</th>
			                  <th>申请时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty changeContractVos}">
				              	<s:set name="constractIndex" value="(#request.page-1)*(#request.limit)"></s:set>
				              	<s:iterator id="item" value="#request.changeContractVos" status="count">
		              			<tr>
			              			<td style="width:4%"><s:property value="#constractIndex+1"/></td>
			              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#item.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#item.title"/></a></td>
			              			<td class="col-sm-1"><s:property value="#item.contractName"/></td>
			              			<td class="col-sm-1"><s:property value="#item.requestDate"/></td>
			              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
			              			<td class="col-sm-1"><s:property value="#item.status"/></td>
			              			<td class="col-sm-1">
			              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#item.processInstanceID'/>&businessType=ChangeContract')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
										<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
			              			</a>
			              			</td>
		              			</tr>
		              			<s:set name="constractIndex" value="#constractIndex+1"></s:set>
			              		</s:iterator>
			              		</c:if>
				              	</tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="bankAccount">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                  <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>操作</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty changeBankAccountVos}">
				              	<s:set name="index" value="(#request.page-1)*(#request.limit)"></s:set>
				              	<s:iterator id="item" value="#request.changeBankAccountVos" status="count">
		              			<tr>
			              			<td style="width:4%"><s:property value="#index+1"/></td>
			              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#item.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#item.title"/></a></td>
			              			<td class="col-sm-1"><s:property value="#item.requestDate"/></td>
			              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
			              			<td class="col-sm-1"><s:property value="#item.status"/></td>
			              			<td class="col-sm-1">
			              			<a onclick="goPath('administration/process/showApproveDetail?processInstanceID=<s:property value='#item.processInstanceID'/>&businessType=bankAccount')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
										<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
			              			</a></td>
		              			</tr>
		              			<s:set name="index" value="#index+1"></s:set>
			              		</s:iterator>
			              		</c:if>
				              	</tbody>
		          		</table>
		    		</div>
		    	</div>
		    	<div role="tabpanel" class="tab-pane" id="carUse">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                   <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>预约时间</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty carUseVos}">
		              	<s:set name="carUseIndex" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="item" value="#request.carUseVos" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#carUseIndex+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#item.processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#item.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#item.requestDate"/></td>
		              			<td class="col-sm-1"><s:property value="#item.useTime"/></td>
		              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.status"/></td>
		              		</tr>
		              		<s:set name="carUseIndex" value="#carUseIndex+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	
		    	
		    	<div role="tabpanel" class="tab-pane" id="vitae">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                   <th>序号</th>
			                  <th>标题</th>
			                  <th>申请时间</th>
			                  <th>申请岗位</th>
			                  <th>申请数量</th>
   			                  <th>已经录用的数量</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>子流程详情</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty vitaeList}">
		              	<s:set name="vitaeIndex" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="item" value="#request.vitaeList" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#vitaeIndex+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='#item.instanceId'/>&selectedPanel=findProcessList')" href="javascript:void(0)"><s:property value="#item.title"/></a></td>
		              			<td class="col-sm-1"><s:date format="yyyy-MM-dd hh:mm" name="#item.addTime"/></td>
		              			<td class="col-sm-1"><s:property value="#item.postName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.neddPersonNumber"/></td>
		              			<td class="col-sm-1"><s:property value="#item.effectivePersonNumber"/></td>
		              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.status"/></td>
		              			<td class="col-sm-1">
		              			<a onclick="goPath('/HR/vitae/getVitaeDetailByInstanceId?instanceId=<s:property value='#item.instanceId'/>&selectedPanel=myProcessList')" href="javascript:void(0)">
			              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
										<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
		              			</a>
		              			</td>
		              			
		              		</tr>
		              		<s:set name="vitaeIndex" value="#vitaeIndex+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	
		    	<div role="tabpanel" class="tab-pane" id="commonSubject">
		    		<div class="table-responsive" style="margin-top:30px;">
		    			<table class="table table-striped">
			              <thead>
			                <tr>
			                   <th>序号</th>
			                  <th>标题</th>
			                  <th>类别</th>
			                  <th>流通方式</th>
			                  <th>经办人</th>
			                  <th>当前处理人</th>
			                  <th>流程状态</th>
			                  <th>查看详情</th>
			                </tr>
			              </thead>
		             		  <tbody>
		              			<c:if test="${not empty commonSubjectList}">
		              	<s:set name="commonSubjectIndex" value="(#request.page-1)*(#request.limit)"></s:set>
		              	<s:iterator id="item" value="#request.commonSubjectList" status="count">
		              		<tr>
		              			<td class="col-sm-1"><s:property value="#commonSubjectIndex+1"/></td>
		              			<td class="col-sm-1"><a onclick="goPath('administration/process/processHistory?processInstanceID=<s:property value='processInstanceID'/>&selectedPanel=myProcessList')" href="javascript:void(0)"><s:property value="#item.title"/></a></td>
		              			<td class="col-sm-1"><s:property value="#item.type"/></td>
		              			<td class="col-sm-1"><s:property value="#item.route"/></td>
		              			<td class="col-sm-1"><s:property value="#item.userNames"/></td>
		              			<td class="col-sm-1"><s:property value="#item.assigneeUserName"/></td>
		              			<td class="col-sm-1"><s:property value="#item.status"/></td>
		              			<td class="col-sm-1">
		              				<a href="javascript:showContent(<s:property value="#item.id"/>)">
			              			<svg class="icon" aria-hidden="true" title="查看详情" data-toggle="tooltip">
										<use xlink:href="#icon-Detailedinquiry"></use>
									</svg>
		              				</a>
		              			</td>
		              		</tr>
		              		<s:set name="commonSubjectIndex" value="#commonSubjectIndex+1"></s:set>
		              	</s:iterator>
		              	</c:if>
			              </tbody>
		          		</table>
		    		</div>
		    	</div>
		    	
		    </div>
         
        
        
			<%-- <h3 class="sub-header" style="margin-top:0px;">邮箱申请列表</h3>
       	  
       	    <div class="table-responsive">
		  		<table class="table table-striped">
	              <thead>
	                <tr>
	                  <th>序号</th>
	                  <th>标题</th>
	                  <th>申请时间</th>
	                  <th>邮箱申请人</th>
	                  <th>邮箱地址</th>
	                  <th>当前处理人</th>
	                  <th>流程状态</th>
	                </tr>
	              </thead>
             		  <tbody>
	              	<c:if test="${not empty emailVOs}">
	              	<s:set name="email_id" value="(#request.page-1)*(#request.limit)"></s:set>
	              	<s:iterator id="emailVO" value="#request.emailVOs" status="count">
	              		<tr>
	              			<td class="col-sm-1"><s:property value="#email_id+1"/></td>
	              			<td class="col-sm-1"><a href="administration/process/processHistory?processInstanceID=<s:property value='#emailVO.processInstanceID'/>"><s:property value="#emailVO.title"/></a></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.requestDate"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.requestUserName"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.address"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.assigneeUserName"/></td>
	              			<td class="col-sm-1"><s:property value="#emailVO.status"/></td>
	              		</tr>
	              		<s:set name="email_id" value="#email_id+1"></s:set>
	              	</s:iterator>
	              	</c:if>
	              </tbody>
          		</table>
          	</div> --%>
          	
          	
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
				    &nbsp;&nbsp;&nbsp;&nbsp;<span>共${count}条记录</span>
		           	<input type="hidden" id="page" value="${page}"/>
		           	<input type="hidden" id="limit" value="${limit}" />
		           	<input type="hidden" id="totalPage" value="${totalPage }" />
		        </div>
		        <%@include file="/includes/pager.jsp" %>
		  </div>
          
        </div>
      </div>
      
      <div   class="modal fade bs-example-modal-lg" id="attachmentDiv" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
		  <div class="modal-dialog modal-md" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
		        <h4 class="modal-title" id="exampleModalLabel"></h4><div class="container-fluid1"></div>
		      </div>
		      <div class="modal-body" id="exampleModalContent">
		      	
		      </div>
		      <div class="modal-footer">
		      <button type="button" class="btn btn-primary" data-dismiss="modal" id="confirm">确定</button>
		      </div>
		    </div>
		  </div>
		</div>
	<div id="applyDetail" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog" style="width:600px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">申请详情</h4>
			</div>
			<div class="modal-body">
				<div id="content"></div>
			</div>
			<div class="modal-footer" style="text-align:center">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
	</div>
	<script src="/assets/js/textarea/highlight.pack.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <script src="/assets/js/textarea/marked.js"></script>
    <script>
     marked.setOptions({
         highlight: function (code) {
             return hljs.highlightAuto(code).value;
         }
     });
    </script>
    <script src="/assets/js/textarea/to-markdown.js"></script>
    <script src="/assets/js/textarea/bootstrap-markdown.js"></script>
		
		<script>
			var showContent=function (id){
				Load.Base.LoadingPic.FullScreenShow(null);
				$.ajax({
					url:'/administration/commonSubject/getContent',
					data:{id:id},
					type:'post',
					dataType:'json',
					success:function (result){
						var content=marked(result.content);
						$('#exampleModalLabel').html(result.title_);
						$('#exampleModalContent').html(content);
						var title=result.title;
						 $('#attachmentDiv').modal('show');

					},
					complete:function(){
						Load.Base.LoadingPic.FullScreenHide();
					}
				})
			}
			function showDetail(spreadPayApplyIds, pluginPayApplyIds, otherPayApplyIds, processInstanceID, isProcess){
				//不是流程
				if(isProcess=='0'){
					var applyType = "";
					var id = "";
					if(spreadPayApplyIds!=''){
						applyType = "付费推广充值";
						id = spreadPayApplyIds;
					}else if(pluginPayApplyIds!=''){
						applyType = "付费服务/插件开通";
						id = pluginPayApplyIds;
					}else if(otherPayApplyIds!=''){
						applyType = "其他";
						id = otherPayApplyIds;
					}
					Load.Base.LoadingPic.FullScreenShow(null);
					$.ajax({
		    			url:'/administration/process/showShopPayApplyDetail',
		    			type:'post',
		    			data:{'id':id,'applyType':applyType},
		    			dataType:'json',
		    			success:function (data){
		    				$("#content").html("");
		    				var content = "";
		    				if('付费推广充值'==applyType){
		    					content = '<table id="spread" class="table table-striped"><tr><td class="_title">店铺</td>'+
		        	  						  '<td class="_title">负责人</td>'+
		        	  						  '<td class="_title">登陆账号</td>'+
		        	  			              '<td class="_title">推广类型</td>'+
		        	  			              '<td class="_title">平均每日花费</td>'+
		        	  			              '<td class="_title">当前余额</td>'+
		        	  			              '<td class="_title">预算充值金额</td>'+
		        	  			              '<td class="_title">店铺每日总花费</td>'+
		        	  		                  '</tr>';
		    					data.spreadShops.forEach(function(value, index){
		    						content +=  '<tr>'+
		            	  			'<td>'+value.shopName+'</td>'+
		            	  			'<td>'+(value.leader=="null"?"":value.leader)+'</td>'+
		            	  			'<td>'+(value.loginAccount=="null"?"":value.loginAccount)+'</td>'+
		            	  			'<td>'+value.spreadType+'</td>'+
		            	  			'<td>'+value.costPerDay+'元'+'</td>'+
		            	  			'<td>'+(value.currentBalance=="null"?"":value.currentBalance+'元')+'</td>'+
		            	  			'<td>'+value.rechargeAmount+'元'+'</td>'+
		            	  			'<td>'+value.totalCost+'元'+'</td></tr>';
		    					});
		    					content += "</table>";
		    				}else if('付费服务/插件开通'==applyType){
		    					
		    					var shopPayPlugin = data.shopPayPlugin;
		    					content = '<div class="tab"><table><tr>'+
		    	  				'<td class="title">店铺名称</td>'+
		    	  				'<td>'+shopPayPlugin.shopName+'</td>'+
		    	  				'<td class="title">服务/应用名称</td>'+
		    	  				'<td>'+shopPayPlugin.serviceName+'</td>'+
		    	  				'<td class="title">插件/服务作用</td>'+
		    	  				'<td>'+shopPayPlugin.serviceUse+'</td>'+
		    	  			'</tr>'+
		    	  			'<tr>'+
		    	  				'<td class="title">开通时长</td>'+
		    	  				'<td>'+shopPayPlugin.openTime+'个月'+'</td>'+
		    	  				'<td class="title">付费金额</td>'+
		    	  				'<td>'+shopPayPlugin.payMoney+'元'+'</td>'+
		    	  				'<td class="title">付款账号</td>'+
		    	  				'<td>'+shopPayPlugin.payAccount+'</td></tr></table></div>';
		    				}else{
		    					var shopOtherPay = data.shopOtherPay;
		    					content = '<div class="tab"><table><tr>'+
		    	  				'<td class="title">项目名称</td>'+
		    	  				'<td>'+shopOtherPay.projectName+'</td>'+
		    	  				'<td class="title">项目作用</td>'+
		    	  				'<td>'+shopOtherPay.projectUse+'</td>'+
		    	  			'</tr>'+
			    	  			'<tr>'+
				  				'<td class="title">项目明细</td>'+
				  				'<td>'+shopOtherPay.description+'</td>'+
				  				'<td class="title">项目花费</td>'+
				  				'<td>'+shopOtherPay.projectPay+'元'+'</td></tr></table></div>';
		    				}
		    				$("#content").append(content);
		    				$("#applyDetail").modal('show');
		    			},
		    			complete:function(){
		    				Load.Base.LoadingPic.FullScreenHide();
		    			}
		    		});
				}else{
					location.href = 'administration/process/showApproveDetail?processInstanceID='+processInstanceID+'&businessType=shopPayApply';
					Load.Base.LoadingPic.FullScreenShow(null);
				}
			}
		</script>
  </body>
</html>
