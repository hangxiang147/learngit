package com.zhizaolian.staff.action.finance;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.BankAccountEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.service.SocialSecurityService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AdvanceTaskVO;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.BankAccountVO;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.HousingFundVO;
import com.zhizaolian.staff.vo.PaymentTaskVO;
import com.zhizaolian.staff.vo.PaymentVo;
import com.zhizaolian.staff.vo.ReimbursementTaskVO;
import com.zhizaolian.staff.vo.ReimbursementVO;
import com.zhizaolian.staff.vo.SocialSecurityProcessVO;
import com.zhizaolian.staff.vo.SocialSecurityVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;

import lombok.Getter;
import lombok.Setter;

public class ProcessAction extends BaseAction {

	@Getter
	@Setter
	private String selectedPanel;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	private String errorMessage;
	@Setter
	private Integer result;  //任务处理结果
	@Setter
	@Getter
	private ReimbursementVO reimbursementVO;
	@Setter
	@Getter
	private AdvanceVo advanceVo;
	@Setter
	@Getter
	private PaymentVo paymentVo;
	@Setter
	@Getter
	private String reimbursementNo;
	@Setter
	@Getter
	private String beginDate;
	@Setter
	@Getter
	private String endDate;
	@Setter
	@Getter
	private String demandName;

	@Autowired
	private IdentityService identityService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ReimbursementService reimbursementService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private SocialSecurityService socialSecurityService;

	private static final long serialVersionUID = 1L;

	public String showReimbursementDiagram() {
		selectedPanel = "newReimbursement";
		return "reimbursementDiagram";
	}
	public String showAdvanceDiagram() {
		selectedPanel = "newAdvance";
		return "advanceDiagram";
	}
	public String showPaymentDiagram() {
		selectedPanel = "newPayment";
		return "paymentDiagram";
	}
	public String newReimbursement() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			BankAccountVO bankAccountVO = reimbursementService.getBankAccountByUserID(user.getId());
			if (bankAccountVO != null) {
				reimbursementVO = new ReimbursementVO();
				reimbursementVO.setCardName(bankAccountVO.getCardName());
				reimbursementVO.setBank(bankAccountVO.getBank());
				reimbursementVO.setCardNumber(bankAccountVO.getCardNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取打款账号失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		selectedPanel = "newReimbursement";
		return "newReimbursement";
	}

	public String reNewReimbursement() {
		String processInstanceID=request.getParameter("processInstanceID");
		request.setAttribute("processInstanceID", processInstanceID);
		reimbursementVO = reimbursementService.getReimbursementVOByProcessInstanceID(processInstanceID);
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			BankAccountVO bankAccountVO = reimbursementService.getBankAccountByUserID(user.getId());
			if (bankAccountVO != null) {
				//				reimbursementVO = new ReimbursementVO();
				reimbursementVO.setCardName(bankAccountVO.getCardName());
				reimbursementVO.setBank(bankAccountVO.getBank());
				reimbursementVO.setCardNumber(bankAccountVO.getCardNumber());
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取打款账号失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstanceID);

		if(CollectionUtils.isNotEmpty(attas)){
			Map<String,List<Attachment>> resultMap=new LinkedHashMap<>();
			for (Attachment attachment : attas) {

				String lastPre="0";
				try{
					lastPre=attachment.getDescription().substring(0,1);
				}catch(Exception e){};
				List<Attachment> list=resultMap.get(lastPre);
				if(list==null){
					list=new ArrayList<>();
				}
				list.add(attachment);
				resultMap.put(lastPre, list);
			}
			request.setAttribute("attaMap", resultMap);
		}
		reimbursementService.updateRestartStatus(reimbursementVO.getReimbursementID());
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		selectedPanel = "newReimbursement";
		return "reNewReimbursement";
	}


	public String newAdvance() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			//此处做个修改，考虑到预付申请领款人，可能是公司外的人员（oa里面没有对应的id），这边改成姓名；考虑到领款人对应多个付款账号，获取所有账号
/*			BankAccountVO bankAccountVO = reimbursementService.getBankAccountByUserID(user.getId());
			if (bankAccountVO != null) {
				advanceVo = new AdvanceVo();
				advanceVo.setCardName(bankAccountVO.getCardName());
				advanceVo.setBank(bankAccountVO.getBank());
				advanceVo.setCardNumber(bankAccountVO.getCardNumber());
			}*/
			StaffVO staffVo = staffService.getStaffByUserID(user.getId());
			List<BankAccountEntity> bankAccountVOs = reimbursementService.getBankAccountByPayeeName(staffVo.getLastName());
			request.setAttribute("bankAccountVOs", bankAccountVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取打款账号失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		selectedPanel = "newAdvance";
		return "newAdvance";
	}
	public String newPayment() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			//此处做个修改，考虑到预付申请领款人，可能是公司外的人员（oa里面没有对应的id），这边改成姓名；考虑到领款人对应多个付款账号，获取所有账号
/*			BankAccountVO bankAccountVO = reimbursementService.getBankAccountByUserID(user.getId());
			if (bankAccountVO != null) {
				paymentVo = new PaymentVo();
				paymentVo.setCardName(bankAccountVO.getCardName());
				paymentVo.setBank(bankAccountVO.getBank());
				paymentVo.setCardNumber(bankAccountVO.getCardNumber());
			}*/
			StaffVO staffVo = staffService.getStaffByUserID(user.getId());
			List<BankAccountEntity> bankAccountVOs = reimbursementService.getBankAccountByPayeeName(staffVo.getLastName());
			request.setAttribute("bankAccountVOs", bankAccountVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取打款账号失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		selectedPanel = "newPayment";
		return "newPayment";
	}
	@Getter
	@Setter
	private File[] file;
	@Getter
	@Setter
	private String fileDetail;
	public String startReimbursement() {
		try {
			reimbursementService.startReimbursement(reimbursementVO,file,fileDetail);
		} catch (Exception e) {
			e.printStackTrace(); 
			errorMessage = "报销申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "startReimbursement";
	}

	public String reStartReimbursement() {
		try {
			reimbursementService.reStartReimbursement(reimbursementVO,file,fileDetail,request.getParameter("processInstanceID"));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "报销申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "startReimbursement";
	}
	public String startAdvance(){
		try {
			reimbursementService.startAdvance(advanceVo,file,fileDetail);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "预付申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "startAdvance";
	}
	public String startPayment(){
		try {
			reimbursementService.startPayment(paymentVo,file,fileDetail);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "付款申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "startPayment";
	}
	/**
	 * 查询我发起的报销申请列表
	 * @return
	 */
	public String myReimbursementList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
//			int count = 0;
			ListResult<ReimbursementVO> rbListResult = reimbursementService.findReimbursementListByUserID(user.getId(), page, limit);
			request.setAttribute("reimbursementVOs", rbListResult.getList());
			count = rbListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "myReimbursementList";
		return "myReimbursementList";
	}


	public String myAdvanceList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
//			int count = 0;
			ListResult<AdvanceVo> rbListResult = reimbursementService.findAdvanceListByUserID(user.getId(), page, limit);
			request.setAttribute("reimbursementVOs", rbListResult.getList());
			count = rbListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "myAdvanceList";
		return "myAdvanceList";
	}
	public String myPaymentList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		try {
//			int count = 0;
			ListResult<PaymentVo> rbListResult = reimbursementService.findPaymentListByUserID(user.getId(), page, limit);
			request.setAttribute("reimbursementVOs", rbListResult.getList());
			count = rbListResult.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "myPaymentList";
		return "myPaymentList";
	}
	public String processHistory() {
		String processInstanceID = request.getParameter("processInstanceID");
		List<FormField> formFields = processService.getFormFieldsByProcessInstanceID(processInstanceID);
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		if("1".equals(request.getParameter("type"))){
			selectedPanel = "myAdvanceList";
		}else if("2".equals(request.getParameter("type"))){			
			selectedPanel = "myPaymentList";
		}else{
			selectedPanel = "myReimbursementList";
		}
		return "processHistory";
	}

	/**
	 * 查询待审批的报销申请列表
	 * @return
	 */
	public String findReimbursementList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "reimbursementList";
			return "reimbursementList";
		}

		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		//等老数据 走完 应该删掉 第一个enum
		tasks.add(TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT);
		tasks.add(TaskDefKeyEnum.REMIT_MONEY);
		tasks.add(TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT);
		ListResult<ReimbursementTaskVO> taskListResult = reimbursementService.findReimbursementsByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), reimbursementNo, beginDate, endDate, page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;

		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "reimbursementList";
		return "reimbursementList";
	}

	
	public String findReimbursementListAll(){
		ListResult<ReimbursementTaskVO> taskListResult =reimbursementService.findReimbursementsAll(reimbursementNo, demandName, beginDate, endDate, page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		request.setAttribute("taskVOs", taskListResult.getList());
		return "findReimbursementListAll";
	}

	public String findAdvanceListAll(){
		ListResult<AdvanceTaskVO> taskListResult = reimbursementService.findAdvancessAll(reimbursementNo, beginDate, endDate, page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		request.setAttribute("taskVOs", taskListResult.getList());
		return "findAdvanceListAll";
	}
	public String findPaymentListAll(){
		ListResult<PaymentTaskVO> taskListResult = reimbursementService.findPaymentsAll(reimbursementNo, beginDate, endDate, page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;
		request.setAttribute("taskVOs", taskListResult.getList());
		return "findPaymentListAll";
	}
	public String findAdvanceList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "AdvanceList";
			return "AdvanceList";
		}

		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		//等老数据 走完 应该删掉 第一个enum
		tasks.add(TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT);
		tasks.add(TaskDefKeyEnum.REMIT_MONEY);
		tasks.add(TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT);
		tasks.add(TaskDefKeyEnum.ADVANCE_UPLOAD_INVOICE);
		ListResult<AdvanceTaskVO> taskListResult = reimbursementService.findAdvancessByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), reimbursementNo, beginDate, endDate, page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;

		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "advanceList";
		return "advanceList";
	}
	public String findPaymentList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "paymentList";
			return "paymentList";
		}

		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		//等老数据 走完 应该删掉 第一个enum
		tasks.add(TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT);
		tasks.add(TaskDefKeyEnum.REMIT_MONEY);
		tasks.add(TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT);
		ListResult<PaymentTaskVO> taskListResult = reimbursementService.findPaymentsByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), reimbursementNo, beginDate, endDate, page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;

		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "paymentList";
		return "paymentList";
	}
	/**
	 * 查询待处理的社保缴纳申请列表
	 * @return
	 */
	public String findSocialSecurityList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "socialSecurityList";
			return "socialSecurityList";
		}

		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.SS_FINANCIAL_PROCESSING);
		ListResult<TaskVO> taskListResult = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count%limit==0 ? count/limit : count/limit+1;

		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "socialSecurityList";
		return "socialSecurityList";
	}
	private void getAndSetLeaderMsg(List<TaskVO> finishedVos,final List<CommentVO> comments){
		Function<String, String> getCommentBytaskId=new Function<String, String>() {
			@Override
			public String apply(String taskId) {
				if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(comments)&&StringUtils.isNotBlank(taskId)){
					for (CommentVO commentVO : comments) {
						if(taskId.equals(commentVO.getTaskID())){
							return commentVO.getContent();
						}
					}
				}
				return "";
			}
		};
		String dept_leader_msg="";
		if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(finishedVos)){
			for (TaskVO taskVO : finishedVos) {
				String taskName=taskVO.getTaskName();
				if("主管审批".equals(taskName)||"总经理审批".equals(taskName)||"分公司负责人".equals(taskName)||"分公司总经理审批".equals(taskName)){
					String resultMsg="【"+taskName+"】	"+taskVO.getAssigneeName()+":";
					resultMsg+=(taskVO.getResult()==null)?"":taskVO.getResult()+":";
					resultMsg+=getCommentBytaskId.apply(taskVO.getTaskID());
					dept_leader_msg+=resultMsg+"</br>";
				} 
				if("公司总经理审批".equals(taskName)||"总公司总经理".equals(taskName)){
					String resultMsg="【"+taskName+"】	"+taskVO.getAssigneeName()+":";
					resultMsg+=(taskVO.getResult()==null)?"":taskVO.getResult()+":";
					resultMsg+=getCommentBytaskId.apply(taskVO.getTaskID());
					request.setAttribute("company_leader_msg", resultMsg);
				}
			}
		}
		request.setAttribute("dept_leader_msg", dept_leader_msg);
	}

	public String auditReimbursement() {
		String taskID = request.getParameter("taskID");
		//是否查看全部页面
		request.setAttribute("isFromAll", request.getParameter("isFromAll"));
		if(StringUtils.isBlank(taskID)){
			return "auditReimbursement";
		}
		ReimbursementVO reimbursementVO = reimbursementService.getReimbursementVOByTaskID(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		List<Group> groups = identityService.createGroupQuery().groupMember(reimbursementVO.getRequestUserID()).list();
		if (groups.size() > 0) {
			String[] positionIDs = groups.get(0).getType().split("_");
			String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
			DepartmentVO departmentVo = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]));
			String departmentName = "";
			if(null != departmentVo){
				departmentName = departmentVo.getDepartmentName();
			}
			request.setAttribute("companyName", companyName);
			request.setAttribute("departmentName", departmentName);
		}
		//单子上的两个流程走到才能确定的姓名 记录在实体类上
		ReimbursementVO reimbursementVO2=reimbursementService.getReimbursementVOByProcessInstanceID(processInstance.getId());
		reimbursementVO.setShowPerson1(reimbursementVO2.getShowPerson1());
		if ("财务打款".equals(request.getParameter("taskName"))) {
			User user=(User) request.getSession().getAttribute("user");
			reimbursementVO.setShowPerson3(staffService.getRealNameByUserId(user.getId()));
		}
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		return "auditReimbursement";
	}


	public String auditAdvance() {
		String taskID = request.getParameter("taskID");
		//是否查看全部页面
		request.setAttribute("isFromAll", request.getParameter("isFromAll"));
		if(StringUtils.isBlank(taskID)){
			return "auditAdvance";
		}
		AdvanceVo reimbursementVO = reimbursementService.getAdvanceVOByTaskID(taskID);
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		List<Attachment> invoiceAttas = new ArrayList<>();
		List<Group> groups = identityService.createGroupQuery().groupMember(reimbursementVO.getRequestUserID()).list();
		if (groups.size() > 0) {
			String[] positionIDs = groups.get(0).getType().split("_");
			String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
			DepartmentVO departmentVo = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]));
			String departmentName = "";
			if(null != departmentVo){
				departmentName = departmentVo.getDepartmentName();
			}
			request.setAttribute("companyName", companyName);
			request.setAttribute("departmentName", departmentName);
		}
		//单子上的两个流程走到才能确定的姓名 记录在实体类上
		AdvanceVo reimbursementVO2=reimbursementService.geAdvanceTaskVOByProcessInstanceID(processInstance.getId());
		String invoiceAttaIdStr = reimbursementVO2.getInvoiceAttaIds();
		if(StringUtils.isNotBlank(invoiceAttaIdStr)){
			List<String> invoiceAttaIds = Arrays.asList(invoiceAttaIdStr.split(","));
			//过滤出附件
			Iterator<Attachment> ite = attas.iterator();
			while(ite.hasNext()){
				Attachment atta = ite.next();
				if(invoiceAttaIds.contains(atta.getId())){
					ite.remove();
				}
			}
			for(String invoiceAttaId: invoiceAttaIds){
				Attachment attach = taskService.getAttachment(invoiceAttaId);
				invoiceAttas.add(attach);
			}
		}
		reimbursementVO.setShowPerson1(reimbursementVO2.getShowPerson1());
		reimbursementVO.setPayeeName(reimbursementVO2.getPayeeName());
		if ("财务打款".equals(request.getParameter("taskName"))) {
			User user=(User) request.getSession().getAttribute("user");
			reimbursementVO.setShowPerson3(staffService.getRealNameByUserId(user.getId()));
		}		
		request.setAttribute("attas", attas);
		request.setAttribute("invoiceAttas", invoiceAttas);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		getAndSetLeaderMsg(finishedTaskVOs,comments);
		selectedPanel = "advanceList";
		return "auditAdvance";
	}
	public String auditPayment() {
		String taskID = request.getParameter("taskID");
		//是否查看全部页面
		request.setAttribute("isFromAll", request.getParameter("isFromAll"));
		if(StringUtils.isBlank(taskID)){
			return "auditPayment";
		}
		PaymentVo reimbursementVO = reimbursementService.getPaymentVOByTaskID(taskID);
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		List<Group> groups = identityService.createGroupQuery().groupMember(reimbursementVO.getRequestUserID()).list();
		if (groups.size() > 0) {
			String[] positionIDs = groups.get(0).getType().split("_");
			String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
			DepartmentVO departmentVo = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]));
			String departmentName = "";
			if(null != departmentVo){
				departmentName = departmentVo.getDepartmentName();
			}
			request.setAttribute("companyName", companyName);
			request.setAttribute("departmentName", departmentName);
		}
		//单子上的两个流程走到才能确定的姓名 记录在实体类上
		PaymentVo reimbursementVO2=reimbursementService.gePaymentTaskVOByProcessInstanceID(processInstance.getId());
		reimbursementVO.setShowPerson1(reimbursementVO2.getShowPerson1());
		reimbursementVO.setPayeeName(reimbursementVO2.getPayeeName());
		if ("财务打款".equals(request.getParameter("taskName"))) {
			User user=(User) request.getSession().getAttribute("user");
			reimbursementVO.setShowPerson3(staffService.getRealNameByUserId(user.getId()));
		}		request.setAttribute("attas", attas);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		getAndSetLeaderMsg(finishedTaskVOs,comments);
		selectedPanel = "paymentList";
		return "auditPayment";
	}
	public String taskComplete() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String businessType = request.getParameter("businessType");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			//完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			//更新业务表的流程节点状态processStatus
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		if (BusinessTypeEnum.SOCIAL_SECURITY.getName().equals(businessType)) {
			return "socialSecurityComplete"; 
		}else if(BusinessTypeEnum.ADVANCE.getName().equals(businessType)){
			return "advanceComplete";
		}else if(BusinessTypeEnum.PAYMENT.getName().equals(businessType)){
			return "paymentComplete";
		}
		return "reimbursementComplete";
	}
	public String auditInvoice() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		try {
			//完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "advanceComplete";
	}
	public String reComplete() {

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String businessType = request.getParameter("businessType");
		String selectedPanel=request.getParameter("selectedPanel");
		String type_=request.getParameter("type_");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			//完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			//更新业务表的流程节点状态processStatus
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			//根据选中页判断 是 预约还是报销还是付款
			if("advanceList".equals(selectedPanel)){
				reimbursementService.setAdvanceFinancialFirstAuditName(pInstance.getId(),staffService.getRealNameByUserId(user.getId()),StringUtils.isNotEmpty(type_)?2:1);					
				return "advanceComplete";
			}else if("paymentList".equals(selectedPanel)){
				reimbursementService.setPaymentFinancialFirstAuditName(pInstance.getId(),staffService.getRealNameByUserId(user.getId()),StringUtils.isNotEmpty(type_)?2:1);					
				return "paymentComplete";
			}else{
				reimbursementService.setfinancialFirstAuditName(pInstance.getId(),staffService.getRealNameByUserId(user.getId()),StringUtils.isNotEmpty(type_)?2:1);					
				return "reimbursementComplete";
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	}
	public String auditSocialSecurity() {
		String taskID = request.getParameter("taskID");
		try {
			String processInstanceID = processService.getProcessInstance(taskID).getId();
			SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
			List<SocialSecurityVO> socialSecurityVOs = socialSecurityService.findSocialSecurityListByProcessID(socialSecurityProcessVO.getSspID());
			for (SocialSecurityVO socialSecurityVO : socialSecurityVOs) {
				socialSecurityVO.setUserName(staffService.getStaffByUserID(socialSecurityVO.getUserID()).getLastName());
			}
			request.setAttribute("socialSecurityVOs", socialSecurityVOs);
			List<HousingFundVO> housingFundVOs = socialSecurityService.findHousingFundListByProcessID(socialSecurityProcessVO.getSspID());
			for (HousingFundVO housingFundVO : housingFundVOs) {
				housingFundVO.setUserName(staffService.getStaffByUserID(housingFundVO.getUserID()).getLastName());
			}
			request.setAttribute("housingFundVOs", housingFundVOs);

			List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
			List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);

			request.setAttribute("socialSecurityProcessVO", socialSecurityProcessVO);
			request.setAttribute("comments", comments);
			request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取审批信息失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		request.setAttribute("taskID", taskID);
		selectedPanel = "socialSecurityList";
		return "auditSocialSecurity";
	}

}
