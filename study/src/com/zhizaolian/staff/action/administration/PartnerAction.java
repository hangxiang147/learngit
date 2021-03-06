package com.zhizaolian.staff.action.administration;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.PartnerDetailEntity;
import com.zhizaolian.staff.entity.PartnerEntity;
import com.zhizaolian.staff.entity.PartnerOptionEntity;
import com.zhizaolian.staff.entity.PartnerQuitSalaryDetailsEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PartnerService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.vo.GroupDetailVO;

import lombok.Getter;
import lombok.Setter;

public class PartnerAction extends BaseAction{
	
	private static final long serialVersionUID = 8202109327112518714L;
	
	@Autowired
	private PartnerService partnerService;
	@Getter
	private String selectedPanel;
	@Setter
	@Getter
	private PartnerEntity partner;
	@Autowired
	private StaffService staffService;
	@Getter
	@Setter
	private Integer limit = 20;
	@Getter
	@Setter
	private Integer page = 1;
	@Getter
	private Integer totalPage = 1;
	@Setter
	@Getter
	private PartnerDetailEntity partnerDetail;
	@Setter
	private String errorMessage;
	
	public String partnerCenter(){
		User user = (User)request.getSession().getAttribute("user");
		//检查是否有审批加入合伙人的权限
		if(partnerService.checkHasAuditPartner(user.getId())){
			//获取待审批的申请书的数量
			int count = partnerService.getToAuditApplyNum();
			request.getSession().setAttribute("applyCount", count);
		}
		//检查是否有合伙人管理的权限
		if(partnerService.checkHasPartnerManage(user.getId())){
			//获取待审批的申请书的数量
			int count = partnerService.getToMacthOptionNum();
			request.getSession().setAttribute("matchCount", count);
		}
		PartnerEntity partner = partnerService.getPartnerApplyByUserId(user.getId());
		if(null!=partner && "1".equals(partner.getStatus())){
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(user.getId());
			List<String> depAndPoss = new ArrayList<>();
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				for(GroupDetailVO group: groupDetails){
					depAndPoss.add(group.getDepartmentName()+"-"+group.getPositionName());
				}
			}
			request.setAttribute("partner", partner);
			//认购金
			double totalMoney = partnerService.getTotalMoney(user.getId());
			request.setAttribute("totalMoney", totalMoney);
			//期权金额
			double optionMoney = partnerService.getOptionMonty(user.getId());
			request.setAttribute("optionMoney", optionMoney);
			Map<String, List<String>> typeAndDetailListMap = partnerService.getDetailListGroupByType(user.getId());
			request.setAttribute("typeAndDetailListMap", typeAndDetailListMap);
			request.setAttribute("depAndPoss", depAndPoss);
			request.setAttribute("staffNum", user.getFirstName());
			request.getSession().setAttribute("isPartner", true);
			selectedPanel = "partnerCenter";
			return "partnerCenter";
		}else if(null!=partner){
			List<PartnerEntity> partners = partnerService.getPartnerApplysByUserId(user.getId());
			request.setAttribute("partners", partners);
			selectedPanel = "myApplyPartner";
			return "myApplyPartner";
		}else{
			StaffEntity staff = staffService.getStaffByUserId(user.getId());
			request.setAttribute("staff", staff);
			request.setAttribute("applyerDate", new Date());
			request.getSession().setAttribute("isPartner", false);
			selectedPanel = "applyPartner";
			return "applyPartner";
		}
	}
	public String startPartner(){
		partnerService.startPartner(partner);
		request.getSession().setAttribute("isPartner", "");
		User user = (User)request.getSession().getAttribute("user");
		//检查是否有审批加入合伙人的权限
		if(partnerService.checkHasAuditPartner(user.getId())){
			//获取待审批的申请书的数量
			int count = partnerService.getToAuditApplyNum();
			request.getSession().setAttribute("applyCount", count);
		}
		return "render_partnerCenter";
	}
	public String myApplyPartner(){
		User user = (User)request.getSession().getAttribute("user");
		List<PartnerEntity> partners = partnerService.getPartnerApplysBy(user.getId());
		request.setAttribute("partners", partners);
		
		List<PartnerEntity> list = partnerService.findTheListBy(user.getId());
		request.setAttribute("exitParterApplyList", list);
		
		PartnerEntity partnerEntity = partnerService.getPartnerEntityBy(user.getId());
		if(partnerEntity != null){
			request.getSession().setAttribute("isPartner", true);
		}else{
			request.getSession().setAttribute("isPartner", false);
		}
		
		selectedPanel = "myApplyPartner";
		return "myApplyPartner";
	}
	public void showApplyContent(){
		String id = request.getParameter("id");
		PartnerEntity partner = partnerService.getPartnerApplyById(id);
		Map<String, String> resultMap = new HashMap<>();
		resultMap.put("applyContent", partner.getApplyContent());
		printByJson(resultMap);
	}
	public String findAllApplyPartners(){
		String applyer = request.getParameter("applyer");
		ListResult<Object> objectList = partnerService.findAllApplyPartners(limit, page, applyer);
		count = objectList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("partners", objectList.getList());
		request.setAttribute("applyer", applyer);
		selectedPanel = "allApplyPartners";
		return "allApplyPartners";
	}
	public String auditApply(){
		String applyId = request.getParameter("applyId");
		String result = request.getParameter("result");
		String comment = request.getParameter("comment");
		if(TaskResultEnum.AGREE.getValue() == Integer.parseInt(result)){
			request.getSession().setAttribute("isPartner", true);
		}else{
			request.getSession().setAttribute("isPartner", false);
		}
		partnerService.auditApply(applyId, result, comment);
		//获取待审批的申请书的数量
		int count = partnerService.getToAuditApplyNum();
		request.getSession().setAttribute("applyCount", count);
		return "render_findAllApplyPartners";
	}
	public String partnerDetailList(){
		String type = request.getParameter("type");
		String staffName = request.getParameter("staffName");
		ListResult<Object> partnerDetailList = partnerService.findPartnerDetailList(type, staffName, limit, page);
		count = partnerDetailList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("partnerDetailList", partnerDetailList.getList());
		request.setAttribute("type", type);
		request.setAttribute("staffName", staffName);
		selectedPanel = "partnerDetailList";
		return "partnerDetailList";
	}
	public String addPartnerDetail(){
		selectedPanel = "partnerDetailList";
		return "addPartnerDetail";
	}
	@Setter
	@Getter
	private String success;
	public String savePartnerDetail(){
		try {
			partnerService.savePartnerDetail(partnerDetail);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "render_partnerDetailList";
	}
	public void checkIsPartner(){
		String[] userIds = request.getParameter("userIds").split(",");
		Map<String, Object> resultMap = new HashMap<>();
		//非合伙人
		List<String> noPartnerUsers = new ArrayList<>();
		for(String userId: userIds){
			if(!partnerService.checkIsPartner(userId)){
				StaffEntity staff = staffService.getStaffByUserId(userId);
				noPartnerUsers.add(staff.getStaffName());
			}
		}
		if(noPartnerUsers.size()==0){
			resultMap.put("staffNames", "");
		}else{
			resultMap.put("staffNames", noPartnerUsers.toArray(new String[noPartnerUsers.size()]));
		}
		printByJson(resultMap);
	}
	public String showPartnerOptionDetail(){
		User user = (User)request.getSession().getAttribute("user");
		ListResult<PartnerOptionEntity> partnerOptionList = partnerService.findPartnerOptionsByUserId(user.getId(), limit, page);
		count = partnerOptionList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("partnerOptionList", partnerOptionList.getList());
		selectedPanel = "partnerCenter";
		return "showPartnerOptionDetail";
	}
	public String showPartnerDetailByUserId(){
		String type = request.getParameter("type");
		User user = (User)request.getSession().getAttribute("user");
		ListResult<Object> partnerDetailList = partnerService.findPartnerDetailListByUserId(type, user.getId(), limit, page);
		count = partnerDetailList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("partnerDetailList", partnerDetailList.getList());
		selectedPanel = "partnerCenter";
		return "myPartnerDetailList";
	}
	public String deletePartnerDetail(){
		String id = request.getParameter("id");
		String rewardType = request.getParameter("rewardType");
		partnerService.deletePartnerDetail(id, rewardType);
		return "render_partnerDetailList";
	}
	public String partnerOptionList(){
		String purchaseBeginDate = request.getParameter("purchaseBeginDate");
		String purchaseEndDate = request.getParameter("purchaseEndDate");
		String purchaserId = request.getParameter("purchaserId");
		String purchaseType = request.getParameter("purchaseType");
		String status = request.getParameter("status");
		String[] conditons = {purchaseBeginDate, purchaseEndDate, purchaserId, purchaseType, status};
		ListResult<Object> partnerOptionList = partnerService.findPartnerOptions(conditons, limit, page);
		count = partnerOptionList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if(totalPage==0) totalPage = 1;
		request.setAttribute("partnerOptionList", partnerOptionList.getList());
		request.setAttribute("purchaseBeginDate", purchaseBeginDate);
		request.setAttribute("purchaseEndDate", purchaseEndDate);
		request.setAttribute("purchaserId", purchaserId);
		request.setAttribute("purchaserName", request.getParameter("purchaserName"));
		request.setAttribute("purchaseType", purchaseType);
		request.setAttribute("status", status);
		User user = (User)request.getSession().getAttribute("user");
		if(partnerService.checkHasPartnerManage(user.getId())){
			//获取待审批的申请书的数量
			int count = partnerService.getToMacthOptionNum();
			request.getSession().setAttribute("matchCount", count);
		}
		selectedPanel = "partnerOptionList"; 
		return "partnerOptionList";
	}
	@Getter
	private String purchaseBeginDate;
	@Getter
	private String purchaseEndDate;
	@Getter
	private String purchaserId;
	@Getter
	private String purchaseType;
	@Getter
	private String status;
	@Getter
	private String purchaserName;
	
	public String matchPartnerOption(){
		String matchPartnerIds = request.getParameter("matchPartnerIds");
		String ratio = request.getParameter("ratio");
		partnerService.matchPartnerOption(matchPartnerIds, ratio);
		purchaseBeginDate = request.getParameter("purchaseBeginDate");
		purchaseEndDate = request.getParameter("purchaseEndDate");
		purchaserId = request.getParameter("purchaserId");
		purchaseType = request.getParameter("purchaseType");
		status = request.getParameter("status");
		purchaserName = request.getParameter("purchaserName");
		return "render_partnerOptionList";
	}
	//临时刷数据，后面需要删除
	public String synData(){
		partnerService.synData();
		return "render_partnerOptionList";
	}
	@Setter
	@Getter
	private File file;
	public String uploadQuitSalaryDetail(){
		try {
			partnerService.uploadQuitSalaryDetail(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "render_quitSalaryDetails";
	}
	public String findQuitSalaryDetails(){
		User user = (User)request.getSession().getAttribute("user");
		PartnerQuitSalaryDetailsEntity quitSalaryDetail = partnerService.getPartnerQuitSalaryDetail(user.getId());
		try {
			@SuppressWarnings("unchecked")
			List<String> quitSalaryDetails = (List<String>) ObjectByteArrTransformer.toObject(quitSalaryDetail.getQuitSalaryDetails());
			request.setAttribute("quitSalaryDetails", quitSalaryDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PartnerEntity partnerEntity = partnerService.getPartnerEntityBy(user.getId());
		if(partnerEntity != null){
			request.getSession().setAttribute("isPartner", true);
		}else{
			request.getSession().setAttribute("isPartner", false);
			return "alreadyExit";
		}
		
		selectedPanel = "quitSalaryDetails";
		return "quitSalaryDetails";
	}
	
	public String exitPartner(){
		User user = (User) request.getSession().getAttribute("user");
		request.setAttribute("userid", user.getId());
		
		PartnerEntity partnerEntity = partnerService.getPartnerEntityBy(user.getId());
		if(partnerEntity != null){
			request.getSession().setAttribute("isPartner", true);
		}else{
			request.getSession().setAttribute("isPartner", false);
			return "alreadyExit";
		}
		
		selectedPanel = "exitPartner";
		return "exitPartner";
	}
	
	public void submitExitReason(){
		String exitReason = request.getParameter("exitReason");
		String userId = request.getParameter("userId");
		List<PartnerEntity> list = partnerService.findTheListBy(userId, 0);
		
		Map<String,String> resultMap = new HashMap<>();
		String result = null;
		if(list.size()==0){
			try{
				partnerService.addExitParterApply(exitReason,userId);
			}catch(Exception e){
				e.printStackTrace();
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw, true));
				logger.error(sw.toString());
			}
			result = "0";//提交成功
			resultMap.put("result", result);
		}else{
			result = "1";//重复提交
			resultMap.put("result", result);
		}
		printByJson(resultMap);
	}
	
	public void pageLoad(){
		String userId = request.getParameter("userId");
		StaffEntity staffEntity = staffService.getStaffByUserId(userId);
		String staffName = staffEntity.getStaffName();
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("staffName", staffName);
		printByJson(resultMap);
	}
	
	@Autowired
	private TaskService taskService;
	
	public void checkApply(){
		String userId = request.getParameter("userId");
		String taskId = request.getParameter("taskId");
		String approvalOpinion = request.getParameter("approvalOpinionComment");
		String processInstanceId = request.getParameter("processInstanceId");
		String status = request.getParameter("auditStatus");
		Integer auditStatus = Integer.parseInt(status);
		if(auditStatus==1){
			if(approvalOpinion==""){
				partnerService.updateApprovalOpinionAndOthers(null,auditStatus,processInstanceId,1);
			}else{
				partnerService.updateApprovalOpinionAndOthers(approvalOpinion,auditStatus,processInstanceId,1);
			}
			
		}else if(auditStatus==2){
			if(approvalOpinion==""){
				partnerService.updateApprovalOpinionAndOthers(null,auditStatus,processInstanceId,1);
			}else{
				partnerService.updateApprovalOpinionAndOthers(approvalOpinion,auditStatus,processInstanceId,1);
			}
			partnerService.updatePartner(userId, 1);
		}
		taskService.complete(taskId);
		Map<String,String> resultMap = new HashMap<>();
		String result = null;
		resultMap.put("result", result);
		printByJson(resultMap);
	}
}
