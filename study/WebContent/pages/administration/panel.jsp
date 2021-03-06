<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/authentication.tld" prefix="auth" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'asset'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'assetList' }">class="active"</c:if>><a onclick="goPath('asset/findAssetList')" href="javascript:void(0)">资产入库</a></li>
	  <li <c:if test="${selectedPanel == 'assetUsageList' }">class="active"</c:if>><a onclick="goPath('asset/findAssetUsageList')" href="javascript:void(0)">领用&amp;退库</a></li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'myHistoryProcess'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'ReimbursementList' }">class="active"</c:if>><a onclick="goPath('administration/process/findUserReimbursementList')" href="javascript:void(0)">我的报销审批历史</a></li>
	  <li <c:if test="${selectedPanel == 'AdvanceList' }">class="active"</c:if>><a onclick="goPath('administration/process/findUserAdvanceList')" href="javascript:void(0)">我的预付审批历史</a></li>
	  <li <c:if test="${selectedPanel == 'paymentList' }">class="active"</c:if>><a onclick="goPath('administration/process/findUserPaymentList')" href="javascript:void(0)">我的付款审批历史</a></li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'application'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'newEmail' }">class="active"</c:if>><a onclick="goPath('administration/process/showEmailDiagram')" href="javascript:void(0)">公司邮箱申请</a></li>
	  <li <c:if test="${selectedPanel == 'newCard' }">class="active"</c:if>><a onclick="goPath('administration/process/showCardDiagram')" href="javascript:void(0)">工牌申领</a></li>
	  
	 <li <c:if test="${selectedPanel == 'newBussinessTripApply' }">class="active"</c:if>><a onclick="goPath('administration/process/showBussinessTripFlow')" href="javascript:void(0)">出差预约申请</a></li>
    <li <c:if test="${selectedPanel == 'newChopBorrow' }">class="active"</c:if>><a onclick="goPath('personal/showChopBorrow')" href="javascript:void(0)">公章申请</a></li>
   	<li <c:if test="${selectedPanel == 'newChopDestroy' }">class="active"</c:if>><a onclick="goPath('personal/showChopDestroy')" href="javascript:void(0)">印章缴销申请</a></li>
   	<li <c:if test="${selectedPanel == 'newCarveChop' }">class="active"</c:if>><a onclick="goPath('personal/showCarveChop')" href="javascript:void(0)">印章刻制申请</a></li>
    <li <c:if test="${selectedPanel == 'newCertificateBorrow' }">class="active"</c:if>><a onclick="goPath('personal/showCertificateBorrow')" href="javascript:void(0)">证件申请</a></li>
    <li <c:if test="${selectedPanel == 'newContractBorrow' }">class="active"</c:if>><a onclick="goPath('personal/showContractBorrow')" href="javascript:void(0)">合同借阅申请</a></li>
	  <li <c:if test="${selectedPanel == 'newIdBorrow' }">class="active"</c:if>><a onclick="goPath('personal/showIdBorrow')" href="javascript:void(0)">身份证借用</a></li>	 
	 <%-- <li <c:if test="${selectedPanel == 'newContract' }">class="active"</c:if>><a href="personal/showContract">合同签署</a></li> --%>	
	 <li <c:if test="${selectedPanel == 'newContractSign' }">class="active"</c:if>><a onclick="goPath('personal/showContractSign')" href="javascript:void(0)">合同签署</a></li>
	 <li <c:if test="${selectedPanel == 'newChangeContract' }">class="active"</c:if>><a onclick="goPath('personal/showChangeContract')" href="javascript:void(0)">合同变更或解除</a></li>
	 <li <c:if test="${selectedPanel == 'newBankAccount' }">class="active"</c:if>><a onclick="goPath('personal/showBankAccount')" href="javascript:void(0)">开设、变更及撤销银行账户</a></li>
	 <li <c:if test="${selectedPanel == 'newCarUseTripFlow' }">class="active"</c:if>><a onclick="goPath('administration/process/showCarUseTripFlow')" href="javascript:void(0)">车辆预约申请</a></li>	
	  <li <c:if test="${selectedPanel == 'newVitae' }">class="active"</c:if>><a onclick="goPath('administration/process/showVitaeDiagram')" href="javascript:void(0)">招聘人员申请</a></li>
	  <li <c:if test="${selectedPanel == 'newPurchase' }">class="active"</c:if>><a onclick="goPath('personal/showPurchaseProperty')" href="javascript:void(0)">财产购置申请</a></li>
	  <li <c:if test="${selectedPanel == 'newHandleProperty' }">class="active"</c:if>><a onclick="goPath('personal/showHandleProperty')" href="javascript:void(0)">资产处置申请</a></li>
	  <li <c:if test="${selectedPanel == 'newTransferProperty' }">class="active"</c:if>><a onclick="goPath('personal/showTransferProperty')" href="javascript:void(0)">资产调拨申请</a></li>
	  <li <c:if test="${selectedPanel == 'newCommonSubject' }">class="active"</c:if>><a onclick="goPath('administration/process/showCommonSubject')" href="javascript:void(0)">通用流程</a></li>
	  <li <c:if test="${selectedPanel == 'newShopApply' }">class="active"</c:if>><a onclick="goPath('personal/showShopApply')" href="javascript:void(0)">店铺申请</a></li>
	  <li <c:if test="${selectedPanel == 'newShopPayApply' }">class="active"</c:if>><a onclick="goPath('personal/showShopPayApply')" href="javascript:void(0)">店铺付费申请</a></li>
	  <li <c:if test="${selectedPanel == 'newViewWorkReport'}">class="active"</c:if>><a onclick="goPath('personal/showViewWorkReport')" href="javascript:void(0)">查看工作日报</a></li>
	  <li <c:if test="${selectedPanel == 'newPublic'}">class="active"</c:if>><a onclick="goPath('personal/showPublic')" href="javascript:void(0)">公关申请</a></li>
	  <li <c:if test="${selectedPanel == 'myProcessList' }">class="active"</c:if>><a onclick="goPath('administration/process/findMyProcessList?type=6')" href="javascript:void(0)">我的申请</a></li>
	</ul>
</div> 

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'express'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'sendExpressList' }">class="active"</c:if>><a onclick="goPath('administration/express/findSendExpressList')" href="javascript:void(0)">快递账单</a></li>
	  <auth:hasPermission name="signExpress">
	  <li <c:if test="${selectedPanel == 'signExpressList' }">class="active"</c:if>><a onclick="goPath('administration/express/findSignExpressList')" href="javascript:void(0)">快递签收</a></li>
	  </auth:hasPermission>
	</ul>
</div> 

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'notice'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'messageList' }">class="active"</c:if>><a onclick="goPath('administration/notice/findMessageList')" href="javascript:void(0)">通知</a></li>
	  <li <c:if test="${selectedPanel == 'noticeList' or param.selectedPanel == 'noticeList' }">class="active"</c:if>><a onclick="goPath('administration/notice/findNoticeList')" href="javascript:void(0)">公告</a></li>
	  <li <c:if test="${selectedPanel == 'newsList' or param.selectedPanel == 'newsList' }">class="active"</c:if>><a onclick="goPath('administration/notice/findNewsList')" href="javascript:void(0)">大事记</a></li>
	</ul>
</div>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'meeting'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'launchMeeting' }">class="active"</c:if>><a onclick="goPath('administration/meeting/launchMeeting')" href="javascript:void(0)">发起会议</a></li>
	  <li <c:if test="${selectedPanel == 'meetingList' }">class="active"</c:if>><a onclick="goPath('administration/meeting/findMeetingList')" href="javascript:void(0)">我的会议</a></li>
	</ul>
</div>

<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'audit'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'emailList' }">class="active"</c:if>><a onclick="goPath('administration/process/findEmailList')" href="javascript:void(0)">公司邮箱申请   <span class="badge" style="background-color:red;<c:if test='${emailCount == 0 }'>display:none;</c:if>">${emailCount }</span></a></li>
	  <li <c:if test="${selectedPanel == 'cardList' }">class="active"</c:if>><a onclick="goPath('administration/process/findCardList')" href="javascript:void(0)">工牌申领   <span class="badge" style="background-color:red;<c:if test='${cardCount == 0 }'>display:none;</c:if>">${cardCount }</span></a></li>
	 <li <c:if test="${selectedPanel == 'tripList' }">class="active"</c:if>><a onclick="goPath('administration/process/findTripList')" href="javascript:void(0)">出差预约申请   <span class="badge" style="background-color:red;<c:if test='${bussinessTripCount == 0 }'>display:none;</c:if>">${bussinessTripCount }</span></a></li>
	 <li <c:if test="${selectedPanel == 'shopPayList' }">class="active"</c:if>><a onclick="goPath('administration/process/findShopPayList')" href="javascript:void(0)">店铺付费申请   <span class="badge" style="background-color:red;<c:if test='${shopPayCount == 0 }'>display:none;</c:if>">${shopPayCount}</span></a></li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'bussinessTripList'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'bussinessTripList' }">class="active"</c:if>><a onclick="goPath('administration/process/bussinessTripList')" href="javascript:void(0)">出差查询 </a></li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'carpoolList'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'carpoolList' }">class="active"</c:if>><a onclick="goPath('administration/carpool/carpoolList')" href="javascript:void(0)">拼车查询 </a></li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'chopList' && #panel!='chopUseLog'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'chopList' }">class="active"</c:if>><a onclick="goPath('administration/chop/toChopListPage')" href="javascript:void(0)">公章管理</a></li>
	  <li <c:if test="${selectedPanel == 'chopUseLog' }">class="active"</c:if>><a onclick="goPath('administration/chop/toChopUseLogLst')" href="javascript:void(0)">使用登记</a></li>
	  <li <c:if test="${selectedPanel == 'contractChopList' }">class="active"</c:if>><a onclick="goPath('administration/chop/findContractChopList')" href="javascript:void(0)">合同类盖章申请列表</a></li>
	</ul>
</div> 
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'certificateList' && #panel!='certificateBorrowLst'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'certificateList' }">class="active"</c:if>><a onclick="goPath('administration/certificate/showCertificates')" href="javascript:void(0)">证件管理</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel!='contractManageLst'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'contractManageLst' }">class="active"</c:if>><a onclick="goPath('administration/contractManage/showContracts')" href="javascript:void(0)">合同管理</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel!='shopManage'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'shopInfoList' }">class="active"</c:if>><a onclick="goPath('administration/shopManage/showShops')" href="javascript:void(0)">信息维护</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'carUseList'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'carUseList' }">class="active"</c:if>><a onclick="goPath('administration/process/findCarUseList')" href="javascript:void(0)">车辆预约查询</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'approvaRecord'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'contractSignList' }">class="active"</c:if>><a onclick="goPath('/administration/process/findContractSignList')" href="javascript:void(0)">合同签署审批记录</a></li>
	  <li <c:if test="${selectedPanel == 'shopPayList' }">class="active"</c:if>><a onclick="goPath('/administration/process/findShopPayAuditList')" href="javascript:void(0)">店铺付费审批记录</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'vehicleManage'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'vehicleManage' }">class="active"</c:if>><a onclick="goPath('/administration/vehicle/findVehicleList')" href="javascript:void(0)">车辆管理</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'personalQuery'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
		<li <c:if test="${selectedPanel == 'vitaeQuery' }">class="active"</c:if>> <a onclick="goPath('/administration/process/vitaeQuery')" href="javascript:void(0)">面试结果查询</a></li>
		<auth:hasPermission name="viewWorkReport">
        <li <c:if test="${selectedPanel == 'viewColleagueWorkReport' }">class="active"</c:if>><a onclick="goPath('/administration/process/viewColleagueWorkReport')" href="javascript:void(0)" data-prefix="administration/process/">日报查询</a></li>
        </auth:hasPermission>
    </ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'projectManagement'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <auth:hasPermission name="viewAllProject">
	  <li <c:if test="${selectedPanel == 'allProjectList' }">class="active"</c:if>><a onclick="goPath('/administration/project/findAllProjectList')" href="javascript:void(0)">项目列表</a></li>
	  </auth:hasPermission>
	  <auth:hasPermission name="createProject">
	  <li <c:if test="${selectedPanel == 'addProject' }">class="active"</c:if>><a onclick="goPath('/administration/project/toAddProject')" href="javascript:void(0)">发起项目</a></li>
	  <li <c:if test="${selectedPanel == 'myAddProject' }">class="active"</c:if>><a onclick="goPath('/administration/project/findMyaddProjectList')" href="javascript:void(0)">我发起的项目</a></li>
	  </auth:hasPermission>
	  <li <c:if test="${selectedPanel == 'myProjectList' }">class="active"</c:if>><a onclick="goPath('/administration/project/findMyProjectList')" href="javascript:void(0)">我参与的项目</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'brandAuthManagement'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'allBrandAuthList' }">class="active"</c:if>><a onclick="goPath('/administration/process/findBrandAuthList')" href="javascript:void(0)">品牌授权记录</a></li>
	  <li <c:if test="${selectedPanel == 'addBrandAuth' }">class="active"</c:if>><a onclick="goPath('/administration/process/toBrandAuth')" href="javascript:void(0)">品牌授权申请</a></li>
	  <li <c:if test="${selectedPanel == 'myBrandAuthList' }">class="active"</c:if>><a onclick="goPath('/administration/process/findMyBrandAuthList')" href="javascript:void(0)">我发起的授权</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'partnerCenter'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <c:if test="${isPartner=='true'}">
	  	<li <c:if test="${selectedPanel == 'partnerCenter' }">class="active"</c:if>><a onclick="goPath('/administration/partner/partnerCenter')" href="javascript:void(0)">合伙人中心</a></li>
	  	<li <c:if test="${selectedPanel == 'quitSalaryDetails' }">class="active"</c:if>><a onclick="goPath('/administration/partner/findQuitSalaryDetails')" href="javascript:void(0)">合伙人放弃工资明细</a></li>
	  	<li <c:if test="${selectedPanel == 'exitPartner' }">class="active"</c:if>><a onclick="goPath('/administration/partner/exitPartner')" href="javascript:void(0)">申请退出合伙人</a></li>
	  </c:if>
	  <c:if test="${isPartner=='false'}">
	  	<li <c:if test="${selectedPanel == 'applyPartner' }">class="active"</c:if>><a onclick="goPath('/administration/partner/partnerCenter')" href="javascript:void(0)">申请加入合伙人</a></li>
	  </c:if>
	  <li <c:if test="${selectedPanel == 'myApplyPartner' }">class="active"</c:if>><a onclick="goPath('/administration/partner/myApplyPartner')" href="javascript:void(0)">我的申请</a></li>
	  <auth:hasPermission name="partnerAuditor">
	  	<li <c:if test="${selectedPanel == 'allApplyPartners' }">class="active"</c:if>><a onclick="goPath('/administration/partner/findAllApplyPartners')" href="javascript:void(0)">申请书审批列表
	  	<span class="badge" style="background-color:red;<c:if test='${applyCount == 0 }'>display:none;</c:if>">${applyCount}</span></a></li>
	  </auth:hasPermission>
	  <auth:hasPermission name="partnerManage">
	  	<li <c:if test="${selectedPanel == 'partnerDetailList' }">class="active"</c:if>><a onclick="goPath('/administration/partner/partnerDetailList')" href="javascript:void(0)">合伙人内容明细列表</a></li>
	  	<li <c:if test="${selectedPanel == 'partnerOptionList' }">class="active"</c:if>><a onclick="goPath('/administration/partner/partnerOptionList')" href="javascript:void(0)">待配比期权列表
	  	<span class="badge" style="background-color:red;<c:if test='${matchCount == 0 }'>display:none;</c:if>">${matchCount}</span></a></li>
	  </auth:hasPermission>
	  <!-- <li ><a href="/administration/partner/synData">刷数据</a></li> -->
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'public'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'publicRelations' }">class="active"</c:if>><a onclick="goPath('/administration/public/findPublicRelations')" href="javascript:void(0)">社会公共关系列表</a></li>
	</ul>
</div>
<div class="col-sm-3 col-md-2 sidebar" <s:if test="#panel != 'userCenter'">style="display:none;"</s:if>>
	<ul class="nav nav-sidebar">
	  <li <c:if test="${selectedPanel == 'appManagement' }">class="active"</c:if>><a onclick="goPath('/userCenter/showApps')" href="javascript:void(0)">应用管理</a></li>
	  <li <c:if test="${selectedPanel == 'roleManagement' }">class="active"</c:if>><a onclick="goPath('/userCenter/showRoles')" href="javascript:void(0)">角色管理</a></li>
	  <li <c:if test="${selectedPanel == 'rightManagement' }">class="active"</c:if>><a onclick="goPath('/userCenter/showRights')" href="javascript:void(0)">权限管理</a></li>
	  <li <c:if test="${selectedPanel == 'showUserAppShipList' }">class="active"</c:if>><a onclick="goPath('/userCenter/showUserAppShipList')" href="javascript:void(0)">应用授权</a></li>
	  <li <c:if test="${selectedPanel == 'userRoleShipManagement' }">class="active"</c:if>><a onclick="goPath('/userCenter/showUserRoleShipList')" href="javascript:void(0)">角色分配</a></li>
	  <li <c:if test="${selectedPanel == 'roleRightShipManagement' }">class="active"</c:if>><a onclick="goPath('/userCenter/showRoleRightShipList')" href="javascript:void(0)">权限设置</a></li>
	</ul>
</div>