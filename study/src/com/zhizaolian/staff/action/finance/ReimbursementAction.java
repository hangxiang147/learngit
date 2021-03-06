package com.zhizaolian.staff.action.finance;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Function;
import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.BankAccountEntity;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.PaymentVo;
import com.zhizaolian.staff.vo.ReimbursementVO;
import com.zhizaolian.staff.vo.TaskVO;

import lombok.Getter;
import lombok.Setter;

public class ReimbursementAction extends BaseAction {

	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	@Setter
	private String errorMessage;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Setter
	@Getter
	private String taskID;
	@Autowired
	private ReimbursementService reimbursementService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private TaskService taskService;
	@Setter
	@Getter
	private ReimbursementVO reimbursementVO =new ReimbursementVO();	
	
	
	private static final long serialVersionUID = 1L;
	
	public String findReimbursementList() {		
		try{
			String name = reimbursementVO.getRequestUserName();
			if (name != null) {
				String trans = new String(name.getBytes("ISO-8859-1"),"UTF-8");
				name = URLDecoder.decode(trans , "UTF-8");
				reimbursementVO.setRequestUserName(name);
			}
			ListResult<ReimbursementVO> reimbursementList=reimbursementService.findReimbursementList(reimbursementVO, page, limit);
			count=reimbursementList.getTotalCount();
			totalPage=count%limit ==0 ? count/limit : count/limit + 1;
			request.setAttribute("reimbursementList", reimbursementList.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "获取报销记录失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "findReimbursementList";
		return "findReimbursementList";
	}
	
	public String findAdvanceList() {		
		try{
			String name = reimbursementVO.getRequestUserName();
			if (name != null) {
				String trans = new String(name.getBytes("ISO-8859-1"),"UTF-8");
				name = URLDecoder.decode(trans , "UTF-8");
				reimbursementVO.setRequestUserName(name);
			} 
			ListResult<AdvanceVo> reimbursementList=reimbursementService.findAdvanceList(reimbursementVO, page, limit);
			count=reimbursementList.getTotalCount();
			totalPage=count%limit ==0 ? count/limit : count/limit + 1;
			request.setAttribute("reimbursementList", reimbursementList.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "获取预付记录失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
		selectedPanel = "findAdvanceList";
		return "findAdvanceList";
	}
	public String findPaymentList() {		
		try{
			String name = reimbursementVO.getRequestUserName();
			if (name != null) {
				String trans = new String(name.getBytes("ISO-8859-1"),"UTF-8");
				name = URLDecoder.decode(trans , "UTF-8");
				reimbursementVO.setRequestUserName(name);
			} 
			ListResult<PaymentVo> reimbursementList = reimbursementService.findPaymentList(reimbursementVO, page, limit);
			count=reimbursementList.getTotalCount();
			totalPage=count%limit ==0 ? count/limit : count/limit + 1;
			request.setAttribute("reimbursementList", reimbursementList.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "获取付款记录失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
		selectedPanel = "findPaymentList";
		return "findPaymentList";
	}
	public String getReimbursementProcess() {
		String type=request.getParameter("type");
		if("1".equals(type)){
			selectedPanel = "ReimbursementList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("2".equals(type)){
			selectedPanel = "AdvanceList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("3".equals(type)){
			selectedPanel = "paymentList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("4".equals(type)){
			selectedPanel = "findAdvanceList";
			request.setAttribute("panel", "reimbursementManagement");
		}else if("5".equals(type)){
			selectedPanel = "findPaymentList";
			request.setAttribute("panel", "reimbursementManagement");
		}else{
			selectedPanel = "findReimbursementList";
			request.setAttribute("panel", "reimbursementManagement");
		}
		String processInstanceID = request.getParameter("processInstanceID");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		selectedPanel = request.getParameter("selectedPanel");
		return "processHistory";
	}
	
	public String getReimbursementDetail() {
		String processInstanceID = request.getParameter("processInstanceID");
		String type=request.getParameter("type");
		if("1".equals(type)){
			selectedPanel = "ReimbursementList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("2".equals(type)){
			selectedPanel = "AdvanceList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("3".equals(type)){
			selectedPanel = "findAdvanceList";
			request.setAttribute("panel", "reimbursementManagement");
		}else if("4".equals(type)){
			selectedPanel = "findProcessList";
			request.setAttribute("panel", "personal");
		}else if("5".equals(type)){
			selectedPanel = request.getParameter("selectedPanel");
			request.setAttribute("panel", "reimbursement");
		}
		else{
			selectedPanel = "findReimbursementList";
			request.setAttribute("panel", "reimbursementManagement");
		}
		if(StringUtils.isBlank(processInstanceID)){
			return "reimbursementDetail";
		}
		ReimbursementVO reimbursementVO = reimbursementService.getReimbursementVOByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstanceID);
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceID).singleResult();
		List<CommentVO> comments=new ArrayList<CommentVO>();
		//因为 有些环节 人一开始就确定了 但是 不应该显示出来 这里做特殊判断
		boolean isShow=false;
		if(finishedTaskVOs!=null&&finishedTaskVOs.size()>0){
			for (TaskVO taskVO : finishedTaskVOs) {
				if("财务主管审批".equals(taskVO.getTaskName())){
					isShow=true;
					break;
				}
			}
		}
		if (!isShow) {
			reimbursementVO.setShowPerson2("");
		}
		if(task!=null){
			comments= processService.getComments(task.getId());
			taskID=task.getId();
		}else{
			comments=processService.getCommentsByProcessInstanceID(processInstanceID);
		}
		request.setAttribute("processInstanceID", processInstanceID);
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
		
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("attas", attas);
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		selectedPanel = "paymentOfRefund";
		return "reimbursementDetail";
	}
	
	
	public String getAdvanceDetail() {
		String processInstanceID = request.getParameter("processInstanceID");
		String type=request.getParameter("type");
		if("1".equals(type)){
			selectedPanel = "ReimbursementList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("2".equals(type)){
			selectedPanel = "AdvanceList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("3".equals(type)){
			selectedPanel = "findAdvanceList";
			request.setAttribute("panel", "reimbursementManagement");
		}else if("4".equals(type)){
			selectedPanel = "findProcessList";
			request.setAttribute("panel", "personal");
		}else if("5".equals(type)){
			selectedPanel = request.getParameter("selectedPanel");
			request.setAttribute("panel", "reimbursement");
		}
		else{
			selectedPanel = "findReimbursementList";
			request.setAttribute("panel", "reimbursementManagement");
		}
		if(StringUtils.isBlank(processInstanceID)){
			return "advanceDetail";
		}
		AdvanceVo reimbursementVO = reimbursementService.geAdvanceTaskVOByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstanceID);
		
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceID).singleResult();
		List<CommentVO> comments=new ArrayList<CommentVO>();
		//因为 有些环节 人一开始就确定了 但是 不应该显示出来 这里做特殊判断
		boolean isShow=false;
		if(finishedTaskVOs!=null&&finishedTaskVOs.size()>0){
			for (TaskVO taskVO : finishedTaskVOs) {
				if("财务主管审批".equals(taskVO.getTaskName())){
					isShow=true;
					break;
				}
			}
		}
		if (!isShow) {
			reimbursementVO.setShowPerson2("");
		}
		if(task!=null){
			comments= processService.getComments(task.getId());
			taskID=task.getId();
		}else{
			comments=processService.getCommentsByProcessInstanceID(processInstanceID);
		}
		request.setAttribute("processInstanceID", processInstanceID);
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
		List<Attachment> invoiceAttas = new ArrayList<>();
		String invoiceAttaIdStr = reimbursementVO.getInvoiceAttaIds();
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
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("attas", attas);
		request.setAttribute("invoiceAttas", invoiceAttas);
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		selectedPanel = "prepaidManagement";
		return "advanceDetail";
	}
	public String getPaymentDetail() {
		String processInstanceID = request.getParameter("processInstanceID");
		String type=request.getParameter("type");
		if("1".equals(type)){
			selectedPanel = "paymentList";
			request.setAttribute("panel", "myHistoryProcess");
		}else if("2".equals(type)){
			selectedPanel = "findPaymentList";
			request.setAttribute("panel", "reimbursementManagement");
		}else if("5".equals(type)){
			selectedPanel = request.getParameter("selectedPanel");
			request.setAttribute("panel", "reimbursement");
		}
		else{
			selectedPanel = "findProcessList";
			request.setAttribute("panel", "personal");
		}
		if(StringUtils.isBlank(processInstanceID)){
			return "paymentDetail";
		}
		PaymentVo reimbursementVO = reimbursementService.gePaymentTaskVOByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstanceID);
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceID).singleResult();
		List<CommentVO> comments=new ArrayList<CommentVO>();
		//因为 有些环节 人一开始就确定了 但是 不应该显示出来 这里做特殊判断
		boolean isShow=false;
		if(finishedTaskVOs!=null&&finishedTaskVOs.size()>0){
			for (TaskVO taskVO : finishedTaskVOs) {
				if("财务主管审批".equals(taskVO.getTaskName())){
					isShow=true;
					break;
				}
			}
		}
		if (!isShow) {
			reimbursementVO.setShowPerson2("");
		}
		if(task!=null){
			comments= processService.getComments(task.getId());
			taskID=task.getId();
		}else{
			comments=processService.getCommentsByProcessInstanceID(processInstanceID);
		}
		request.setAttribute("processInstanceID", processInstanceID);
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
		
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("attas", attas);
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		selectedPanel ="paymentOrderManagement";
		return "paymentDetail";
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
	@Setter
	@Getter
	private BankAccountEntity bankAccountVo;
	public void addBankAccount(){
		Map<String, Object> resultMap = new HashMap<>();
		//验证是否已存在
		if(reimbursementService.checkBankAccountExist(bankAccountVo)){
			resultMap.put("exist", true);
		}
		else if(null != bankAccountVo.getAccountID()){
			BankAccountEntity bankAccount = reimbursementService.getBankAccountById(bankAccountVo.getAccountID());
			bankAccountVo.setAddTime(bankAccount.getAddTime());
			reimbursementService.updateBankAccount(bankAccountVo);
		}else{
			int bankAccountId = reimbursementService.saveBankAccount(bankAccountVo);
			bankAccountVo.setAccountID(bankAccountId);
		}
		resultMap.put("bankAccountVo", bankAccountVo);
		printByJson(resultMap);
	}
	public void getBankAccountsByPayeeName(){
		Map<String, Object> resultMap = new HashMap<>();
		String payeeName = request.getParameter("payeeName");
		List<BankAccountEntity> bankAccountVos = reimbursementService.getBankAccountByPayeeName(payeeName);
		resultMap.put("bankAccountVos", bankAccountVos);
		printByJson(resultMap);
	}
	public String updateBankAccount(){
		String bankAccountId = request.getParameter("bankAccountId");
		BankAccountEntity bankAccount = reimbursementService.getBankAccountById(Integer.parseInt(bankAccountId));
		request.setAttribute("bankAccount", bankAccount);
		request.setAttribute("processType", request.getParameter("processType"));
		request.setAttribute("taskID", request.getParameter("taskID"));
		return "updateBankAccount";
	}
	
	public String paymentOfRefund(){
		try{
			ListResult<ReimbursementVO> reimbursementVOs = reimbursementService.getReimbursementVOList(reimbursementVO, page, limit);
			count=reimbursementVOs.getTotalCount();
			totalPage=count%limit ==0 ? count/limit : count/limit + 1;
			request.setAttribute("reimbursementVOLists", reimbursementVOs.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "获取报销记录失败："+e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		
		selectedPanel = "paymentOfRefund";
		return "paymentOfRefund";
	}
	
	@Setter
	@Getter
	private AdvanceVo advanceVo = new AdvanceVo();
	public String prepaidManagement(){
		try{
			ListResult<AdvanceVo> advanceVoList =reimbursementService.getAdvanceVoList(advanceVo, page, limit);
			count=advanceVoList.getTotalCount();
			totalPage=count%limit ==0 ? count/limit : count/limit + 1;
			request.setAttribute("advanceVoList", advanceVoList.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "获取预付记录失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
			
		}
		selectedPanel = "prepaidManagement";
		return "prepaidManagement";
	}
	@Setter
	@Getter
	private PaymentVo paymentVo = new PaymentVo();
	public String paymentOrderManagement(){
		try{
			ListResult<PaymentVo> paymentVoList =reimbursementService.getPaymentVoList(paymentVo,page,limit);
			count=paymentVoList.getTotalCount();
			totalPage=count%limit ==0 ? count/limit : count/limit + 1;
			request.setAttribute("paymentVoList", paymentVoList.getList());
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = ""+e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel ="paymentOrderManagement";
		return "paymentOrderManagement";
	}
}
