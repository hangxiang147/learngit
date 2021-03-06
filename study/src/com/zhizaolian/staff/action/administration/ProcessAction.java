﻿package com.zhizaolian.staff.action.administration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.PurchasePropertyEntity;
import com.zhizaolian.staff.entity.ShopOtherPayEntity;
import com.zhizaolian.staff.entity.ShopPayPluginEntity;
import com.zhizaolian.staff.entity.SpreadShopApplyEntity;
import com.zhizaolian.staff.entity.SpreadShopEntity;
import com.zhizaolian.staff.entity.TripEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.BankAccountService;
import com.zhizaolian.staff.service.BrandAuthService;
import com.zhizaolian.staff.service.CarUseService;
import com.zhizaolian.staff.service.CardService;
import com.zhizaolian.staff.service.CertificateService;
import com.zhizaolian.staff.service.ChopService;
import com.zhizaolian.staff.service.CommonSubjectService;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.service.EmailService;
import com.zhizaolian.staff.service.HandlePropertyService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.PublicService;
import com.zhizaolian.staff.service.PurchasePropertyService;
import com.zhizaolian.staff.service.ShopApplyService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TransferPropertyService;
import com.zhizaolian.staff.service.TripService;
import com.zhizaolian.staff.service.ViewReportService;
import com.zhizaolian.staff.service.VitaeService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.BrandAuthVo;
import com.zhizaolian.staff.vo.CarUseVo;
import com.zhizaolian.staff.vo.CardVO;
import com.zhizaolian.staff.vo.CarveChopVo;
import com.zhizaolian.staff.vo.CertificateBorrowVo;
import com.zhizaolian.staff.vo.ChangeBankAccountVo;
import com.zhizaolian.staff.vo.ChangeContractVo;
import com.zhizaolian.staff.vo.ChopBorrrowVo;
import com.zhizaolian.staff.vo.ChopDestroyVo;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.CommonSubjectVo;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.ContractBorrowVo;
import com.zhizaolian.staff.vo.ContractSignVo;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.EmailVO;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.HandlePropertyVo;
import com.zhizaolian.staff.vo.IdBorrowVo;
import com.zhizaolian.staff.vo.PositionVO;
import com.zhizaolian.staff.vo.PublicRelationEventVo;
import com.zhizaolian.staff.vo.PurchasePropertyVo;
import com.zhizaolian.staff.vo.ShopApplyVo;
import com.zhizaolian.staff.vo.ShopPayApplyListVo;
import com.zhizaolian.staff.vo.ShopPayApplyVo;
import com.zhizaolian.staff.vo.ShopPayVo;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;
import com.zhizaolian.staff.vo.TransferPropertyVo;
import com.zhizaolian.staff.vo.TripVo;
import com.zhizaolian.staff.vo.ViewWorkReportVo;
import com.zhizaolian.staff.vo.VitaeVo;
import com.zhizaolian.staff.vo.WorkReportDetailVO;

import lombok.Getter;
import lombok.Setter;

public class ProcessAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	private String errorMessage;
	@Getter
	@Setter
	private EmailVO emailVO;
	@Getter
	@Setter
	private CardVO cardVO;
	@Getter
	@Setter
	private CarUseVo carUseVo;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Setter
	private Integer result; // 任务处理结果
	@Setter
	@Getter
	private Integer type;
	@Setter
	@Getter
	private TripVo tripVo;
	@Setter
	@Getter
	private BrandAuthVo brandAuthVo;
	@Autowired
	private CarUseService carUseService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private CardService cardService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private TripService tripService;
	@Autowired
	private ChopService chopService;
	@Autowired
	private VitaeService vitaeService;
	@Autowired
	private CommonSubjectService commonSubjectService;

	@Autowired
	private CertificateService certificateService;

	@Autowired
	private ContractService contractService;

	@Autowired
	private BankAccountService bankAccountService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private PurchasePropertyService purchasePropertyService;
	@Autowired
	private HandlePropertyService handlePropertyService;
	@Autowired
	private TransferPropertyService transferPropertyService;
	@Autowired
	private ShopApplyService shopApplyService;
	@Autowired
	private ViewReportService viewReportService;
	@Autowired
	private PublicService publicService;
	@Autowired
	private BrandAuthService brandAuthService;
	
	public String showEmailDiagram() {
		selectedPanel = "newEmail";
		return "emailDiagram";
	}

	public String vitaeQuery(){
		User user = (User) request.getSession().getAttribute("user");
		ListResult<Object> datas= vitaeService.getVitaeResultEntityByName(request.getParameter("name"), user.getId(), page, limit);
		request.setAttribute("name", request.getParameter("name"));
		request.setAttribute("userId", user.getId());
		count = datas.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page - 1) * limit);
		request.setAttribute("resultList", datas.getList());
		return "vitaeQuery";
	}
	public String showVitaeDiagram() {
		selectedPanel = "newVitae";
		return "vitaeDiagram";
	}
	public String newVitae() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staff);
		selectedPanel = "newVitae";
		return "newVitae";
	}
	public String showCardDiagram() {
		selectedPanel = "newCard";
		return "cardDiagram";
	}

	public String findUserReimbursementList(){
		User user = (User) request.getSession().getAttribute("user");
		String code=request.getParameter("code");
		String applyerId = request.getParameter("applyerId");
		//查询除了发起 意外的所有流程
		String filterSql=" and t.NAME_ !='报销申请' and t.Name_ !='修改打款账号' ";
		if(StringUtils.isNotBlank(code)){
			filterSql+=" and r.ReimbursementNo like '%"+code+"%' ";
		}
		if(StringUtils.isNotBlank(applyerId)){
			filterSql += " and r.RequestUserID ='"+applyerId+"' ";
		}
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		if(StringUtils.isNotBlank(startTime)){
			filterSql+=" and r.AddTime>= '"+startTime+"' ";
			request.setAttribute("startTime", startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			filterSql+=" and r.AddTime<= '"+endTime+"' ";
			request.setAttribute("endTime", endTime);
		}
		ListResult<Object> reimbursementList=processService.getAllInstanceIdByUserAndTypePrefix(user.getId(), "Reimbursement", page, limit,filterSql);
		count=reimbursementList.getTotalCount();
		totalPage=count%limit ==0 ? count/limit : count/limit + 1;
		request.setAttribute("reimbursementList", reimbursementList.getList());
		request.setAttribute("userName", staffService.getRealNameByUserId(user.getId()));
		request.setAttribute("code", code);
		request.setAttribute("applyer", request.getParameter("applyer"));
		request.setAttribute("applyerId", applyerId);
		return "findUserReimbursementList";
	}

	public String findUserAdvanceList(){
		User user = (User) request.getSession().getAttribute("user");
		//查询除了发起 意外的所有流程
		String code=request.getParameter("code");
		String applyerId = request.getParameter("applyerId");
		String filterSql=" and t.NAME_ !='报销申请' and t.NAME_ !='预付申请' and t.NAME_ !='付款申请' and t.Name_ !='修改打款账号' ";
		if(StringUtils.isNotBlank(code)){
			filterSql+=" and r.ReimbursementNo like '%"+code+"%' ";
		}
		if(StringUtils.isNotBlank(applyerId)){
			filterSql += " and r.RequestUserID ='"+applyerId+"' ";
		}
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		if(StringUtils.isNotBlank(startTime)){
			filterSql+=" and r.AddTime>= '"+startTime+"' ";
			request.setAttribute("startTime", startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			filterSql+=" and r.AddTime<= '"+endTime+"' ";
			request.setAttribute("endTime", endTime);
		}
		ListResult<Object> reimbursementList=processService.getAllInstanceIdByUserAndTypePrefix(user.getId(), "Advance", page, limit,filterSql);
		count=reimbursementList.getTotalCount();
		totalPage=count%limit ==0 ? count/limit : count/limit + 1;
		request.setAttribute("reimbursementList", reimbursementList.getList());
		request.setAttribute("userName", staffService.getRealNameByUserId(user.getId()));
		request.setAttribute("code", code);
		request.setAttribute("applyer", request.getParameter("applyer"));
		request.setAttribute("applyerId", applyerId);
		return "findUserAdvanceList";
	}
	public String findUserPaymentList(){
		User user = (User) request.getSession().getAttribute("user");
		//查询除了发起 意外的所有流程
		String code=request.getParameter("code");
		String applyerId = request.getParameter("applyerId");
		String filterSql=" and t.NAME_ !='付款申请' and t.Name_ !='修改打款账号' ";
		if(StringUtils.isNotBlank(code)){
			filterSql+=" and r.ReimbursementNo like '%"+code+"%' ";
		}
		if(StringUtils.isNotBlank(applyerId)){
			filterSql += " and r.RequestUserID ='"+applyerId+"' ";
		}
		String startTime=request.getParameter("startTime");
		String endTime=request.getParameter("endTime");
		if(StringUtils.isNotBlank(startTime)){
			filterSql+=" and r.AddTime>= '"+startTime+"' ";
			request.setAttribute("startTime", startTime);
		}
		if(StringUtils.isNotBlank(endTime)){
			filterSql+=" and r.AddTime<= '"+endTime+"' ";
			request.setAttribute("endTime", endTime);
		}
		ListResult<Object> reimbursementList=processService.getAllInstanceIdByUserAndTypePrefix(user.getId(), "Payment", page, limit,filterSql);
		count=reimbursementList.getTotalCount();
		totalPage=count%limit ==0 ? count/limit : count/limit + 1;
		request.setAttribute("reimbursementList", reimbursementList.getList());
		request.setAttribute("userName", staffService.getRealNameByUserId(user.getId()));
		request.setAttribute("code", code);
		request.setAttribute("applyer", request.getParameter("applyer"));
		request.setAttribute("applyerId", applyerId);
		return "findUserPaymentList";
	}
	public String newEmail() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staff);

		selectedPanel = "newEmail";
		return "newEmail";
	}
	public String newCarUse(){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staff);
		selectedPanel = "newCarUseTripFlow";
		return "newCarUse";
	}
	public String newCard() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		StaffVO staffVO = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staffVO);

		selectedPanel = "newCard";
		return "newCard";
	}

	public String startEmail() {
		try {
			emailService.startEmail(emailVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "公司邮箱申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.EMAIL.getValue();
		return "startEmail";
	}
	public String startCarUse() {
		try {
			carUseService.startCarUse(carUseVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "车辆预约提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.CAR_USE.getValue();
		return "startEmail";
	}
	public String startCard() {
		try {
			cardService.startCard(cardVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "工牌申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CARD.getValue();
		return "startCard";
	}

	public String findMyProcessList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

//		int count = 0;
		try {
			switch (BusinessTypeEnum.valueOf(type)) {
			case EMAIL:
				ListResult<EmailVO> emailListResult = emailService.findEmailListByUserID(user.getId(), page, limit);
				request.setAttribute("emailVOs", emailListResult.getList());
				count = emailListResult.getTotalCount();
				break;
			case CARD:
				ListResult<CardVO> cardListResult = cardService.findCardListByUserID(user.getId(), page, limit);
				request.setAttribute("cardVOs", cardListResult.getList());
				count = cardListResult.getTotalCount();
				break;
			case BUSSNIESSTRIP:
				ListResult<TripVo> tripListResult = tripService.findTripListByUserID(user.getId(), page, limit);
				request.setAttribute("tripVOs", tripListResult.getList());
				count = tripListResult.getTotalCount();
				break;
			case CHOP_BORROW:
				ListResult<ChopBorrrowVo> chopBorrorList = chopService.findChopBorrrowListByUserID(user.getId(), page, limit);
				request.setAttribute("chopBorrowVos", chopBorrorList.getList());
				count = chopBorrorList.getTotalCount();
				break;
			case CERTIFICATE_BORROW:
				ListResult<CertificateBorrowVo> certificateBorrowLst = certificateService.findCertificateBorrowLstByUserID(user.getId(), page, limit);
				request.setAttribute("certificateBorrowVos", certificateBorrowLst.getList());
				count = certificateBorrowLst.getTotalCount();
				break;
			case CONTRACT_BORROW:
				ListResult<ContractBorrowVo> contractBorrowLst = contractService.findContractBorrowLstByUserID(user.getId(), page, limit);
				request.setAttribute("contractBorrowVos", contractBorrowLst.getList());
				count = contractBorrowLst.getTotalCount();
				break;
			case ID_BORROW:
				ListResult<IdBorrowVo> idBorrowList=chopService.findIdBorrrowListByUserID(user.getId(), page, limit);
				request.setAttribute("idBorrowVos", idBorrowList.getList());
				count = idBorrowList.getTotalCount();
				break;
			case CONTRACT:
				/*ListResult<ContractDetailVo> contractList=chopService.findContractListByUserID(user.getId(), page, limit);
				request.setAttribute("constractVos", contractList.getList());
				count = contractList.getTotalCount();*/
				ListResult<ContractSignVo> contractSignLst = contractService.findContractSignLstByUserId(user.getId(), page, limit);
				request.setAttribute("constractSignVos", contractSignLst.getList());
				count = contractSignLst.getTotalCount();
				break;
			case CAR_USE:
				Map<String, String> params= new HashMap<String,String>();
				params.put("userId", user.getId());
				ListResult<CarUseVo> carUseVos=carUseService.getCarUseByKeys(params, page, limit);
				request.setAttribute("carUseVos", carUseVos.getList());
				count = carUseVos.getTotalCount();
				break;
			case VITAE:
				ListResult<VitaeVo> vitaeList=vitaeService.getVitaeByKeys(user.getId(), page, limit);
				request.setAttribute("vitaeList", vitaeList.getList());
				count = vitaeList.getTotalCount();
				break;
			case COMMONSUBJECT:
				CommonSubjectVo commonSubjectVo=new  CommonSubjectVo();
				commonSubjectVo.setUserID(user.getId());
				ListResult<CommonSubjectVo> commonSubjectList=commonSubjectService.getCommonSubjectByKey(commonSubjectVo, null, page, limit);
				request.setAttribute("commonSubjectList", commonSubjectList.getList());
				count = commonSubjectList.getTotalCount();
				break;
			case CONTRACT_CHANGE:
				ListResult<ChangeContractVo> changeContractList = contractService.findChangeContractListByUserID(user.getId(), page, limit);
				request.setAttribute("changeContractVos", changeContractList.getList());
				count = changeContractList.getTotalCount();
				break;
			case BANK_ACCOUNT_CHANGE:
				ListResult<ChangeBankAccountVo> changeBankAccountList = bankAccountService.findChangeBankAccountListByUserID(user.getId(), page, limit);
				request.setAttribute("changeBankAccountVos", changeBankAccountList.getList());
				count = changeBankAccountList.getTotalCount();
				break;
			case CHOP_DESTROY:
				ListResult<ChopDestroyVo> chopDestroyList = chopService.findChopDestroyListByUserID(user.getId(), page, limit);
				request.setAttribute("chopDestroyVos", chopDestroyList.getList());
				count = chopDestroyList.getTotalCount();
				break;
			case PURCHASE_PROPERTY:
				ListResult<PurchasePropertyVo> purchasePropertyList = purchasePropertyService.findPurchasePropertyListByUserID(user.getId(), page, limit);
				request.setAttribute("purchasePropertyVos", purchasePropertyList.getList());
				count = purchasePropertyList.getTotalCount();
				break;
			case CARVE_CHOP:
				ListResult<CarveChopVo> carveChopVoList = chopService.findCarveChopListByUserID(user.getId(), page, limit);
				request.setAttribute("carveChopVos", carveChopVoList.getList());
				count = carveChopVoList.getTotalCount();
				break;
			case HANDLE_PROPERTY:
				ListResult<HandlePropertyVo> handlePropertyList = handlePropertyService.findHandlePropertyListByUserID(user.getId(), page, limit);
				request.setAttribute("handlePropertyVos", handlePropertyList.getList());
				count = handlePropertyList.getTotalCount();
				break;
			case TRANSFER_PROPERTY:
				ListResult<TransferPropertyVo> transferPropertyList = transferPropertyService.findTransferPropertyListByUserID(user.getId(), page, limit);
				request.setAttribute("transferPropertyVos", transferPropertyList.getList());
				count = transferPropertyList.getTotalCount();
				break;
			case SHOP_APPLY:
				ListResult<ShopApplyVo> shopApplyList = shopApplyService.findShopApplyListByUserID(user.getId(), page, limit);
				request.setAttribute("shopApplyVos", shopApplyList.getList());
				count = shopApplyList.getTotalCount();
				break;
			case SHOP_PAY_APPLY:
				List<ShopPayApplyVo> shopPayApplyList = shopApplyService.findShopPayApplyListByUserID(user.getId());
				List<SpreadShopApplyEntity> spreadShopApplyList = shopApplyService.getSpreadShopApplyList(user.getId());
				if(spreadShopApplyList.size()>0){
					shopPayApplyList.addAll(shopApplyService.changeToShopPayApplyVo(spreadShopApplyList, Constants.PAY_SPREAD));
				}
				List<ShopPayPluginEntity> shopPayPluginList = shopApplyService.getShopPayPluginList(user.getId());
				if(shopPayPluginList.size()>0){
					shopPayApplyList.addAll(shopApplyService.changeToShopPayApplyVo(shopPayPluginList, Constants.PAY_PLUG_IN));
				}
				List<ShopOtherPayEntity> otherPayList = shopApplyService.getShopOtherPayList(user.getId());
				if(otherPayList.size()>0){
					shopPayApplyList.addAll(shopApplyService.changeToShopPayApplyVo(otherPayList, ""));
				}
				Collections.sort(shopPayApplyList);
				request.setAttribute("shopPayApplyVos", ActionUtil.page(page, limit, shopPayApplyList));
				count = shopPayApplyList.size();
				break;
			case VIEW_REPORT:
				ListResult<ViewWorkReportVo> viewWorkReportList = viewReportService.findViewWorkReportListByUserID(user.getId(), page, limit);
				request.setAttribute("viewWorkReportVos", viewWorkReportList.getList());
				count = viewWorkReportList.getTotalCount();
				break;
			case PUBLIC_EVENT:
				ListResult<PublicRelationEventVo> publicRelationEventList = publicService.findPublicEventListByUserID(user.getId(), page, limit);
				request.setAttribute("publicRelationEventVos", publicRelationEventList.getList());
				count = publicRelationEventList.getTotalCount();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		selectedPanel = "myProcessList";
		return "myProcessList";
	}

	public String processHistory() {
		String processInstanceID = request.getParameter("processInstanceID");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		String selectedPanel = request.getParameter("selectedPanel");
		if(StringUtils.isNotBlank(selectedPanel)){
			this.selectedPanel = selectedPanel;
		}else{
			selectedPanel = "myProcessList";
		}
		return "processHistory";
	}

	public String findEmailList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "emailList";
			return "emailList";
		}

		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.EMAIL_AUDIT);
		tasks.add(TaskDefKeyEnum.OPEN_MAILBOX);
		ListResult<TaskVO> taskListResult = processService.findTasksByUserGroupIDs(tasks, groups,
				Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;

		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "emailList";
		return "emailList";
	}
	private final static String[] CAR_USE_LIST_PARAMS={"startTime","endTime","id","userName"};
	public String findCarUseList(){
		Map<String, String> params=ActionUtil.createMapByRequest(request, true, CAR_USE_LIST_PARAMS);
		params.put("status", "1");
		params.put("userId", params.get("id"));
		params.remove("id");
		ListResult<CarUseVo> carUseVos=carUseService.getCarUseByKeys(params, page, limit);
		count = carUseVos.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("carUseVos", carUseVos.getList());
		selectedPanel = "carUseList";
		return "carUseList";

	}

	public String findCardList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			selectedPanel = "cardList";
			return "cardList";
		}

		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.CARD_AUDIT);
		tasks.add(TaskDefKeyEnum.MAKE_CARD);
		ListResult<TaskVO> taskListResult = processService.findTasksByUserGroupIDs(tasks, groups,
				Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;

		request.setAttribute("taskVOs", taskListResult.getList());
		selectedPanel = "cardList";
		return "cardList";
	}

	public String auditTask() {
		String taskID = request.getParameter("taskID");
		List<FormField> formFields = processService.getFormFields(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		if (TaskDefKeyEnum.MAKE_CARD.getName().equals(task.getTaskDefinitionKey())) {
			CardVO cardVO = cardService.getCardVOByProcessInstanceID(processInstance.getId());
			User usr = identityService.createUserQuery().userId(cardVO.getRequestUserID()).singleResult();
			cardVO.setStaffNumber(usr.getFirstName());
			cardVO.setNickName(usr.getLastName());
			List<Group> groups = identityService.createGroupQuery().groupMember(cardVO.getRequestUserID()).list();
			if (groups.size() > 0) {
				String[] positions = groups.get(0).getType().split("_");
				CompanyIDEnum company = CompanyIDEnum.valueOf(Integer.parseInt(positions[0]));
				cardVO.setCompanyName(company == null ? "" : company.getName());
				DepartmentVO department = positionService.getDepartmentByID(Integer.parseInt(positions[1]));
				cardVO.setDepartmentName(department == null ? "" : department.getDepartmentName());
				PositionVO position = positionService.getPositionByPositionID(Integer.parseInt(positions[2]));
				cardVO.setPositionName(position == null ? "" : position.getPositionName());
			}
			request.setAttribute("cardVO", cardVO);
		}

		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		return "auditTask";
	}

	public String openMailBox() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			String confirmAddress = request.getParameter("confirmAddress");
			String originalPassword = request.getParameter("originalPassword");
			String loginUrl = request.getParameter("loginUrl");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			if (result == TaskResultEnum.EMAIL_SUCCESS.getValue()) {
				// 确认邮箱开通信息
				emailService.confirmEmailAccount(pInstance.getId(), confirmAddress, originalPassword, loginUrl);
			}
			// 完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			// 更新业务表的流程节点状态processStatus
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result),
					BusinessTypeEnum.EMAIL.getName());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		return "openMailBox";
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

			// 完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);

			// 更新业务表的流程节点状态processStatus
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);


		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return getBusinessTypeValue( businessType);
	}




	private String getBusinessTypeValue(String name) {
		if (BusinessTypeEnum.EMAIL.getName().equals(name)) {
			return "emailComplete";
		} else if (BusinessTypeEnum.CARD.getName().equals(name)) {
			return "cardComplete";
		}else if(BusinessTypeEnum.BUSSNIESSTRIP.getName().equals(name)){
			return "tripComplete";
		}
		return "emailComplete";
	}

	public String showBussinessTripFlow() {
		selectedPanel = "newBussinessTripApply";
		return "showBussinessTripFlow";
	}
	public String showCommonSubject(){
		selectedPanel="newCommonSubject";
		return "showCommonSubject";
	}

	public String  startCommonSubject(){
		selectedPanel="newCommonSubject";
		return "startCommonSubject";
	}
	public String showCarUseTripFlow(){
		selectedPanel = "newCarUseTripFlow";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "showCarUseTripFlow";

	}

	public String newBussinessTripApply() {
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		selectedPanel = "newBussinessTripApply";
		return "newBussinessTripApply";
	}

	public String  startBussinessTripApply() {
		try {
			tripService.StartTrip(tripVo);
		} catch (Exception e) {
			errorMessage = "出差申请提交失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		type = BusinessTypeEnum.BUSSNIESSTRIP.getValue();
		return "startTrip";
	}

	public String findTripList(){
		selectedPanel = "tripList";
		User user=(User) request.getSession().getAttribute("user");
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			return "tripList";
		}
		List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
		tasks.add(TaskDefKeyEnum.BUSSINESSTRIP_CONFIRM);
		tasks.add(TaskDefKeyEnum.BUSSINESSTRIP_BUYTICKET);
		ListResult<TaskVO> taskListResult = processService.findTasksByUserGroupIDs(tasks, groups,
				Arrays.asList(user.getId()), page, limit);
		count = taskListResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("taskVOs", taskListResult.getList());
		return "tripList";
	}
	public String findShopPayList(){
		selectedPanel = "shopPayList";
		User user = (User) request.getSession().getAttribute("user");
		List<ShopPayVo> shopPayVos = shopApplyService.getShopPayList(user.getId());
		count = shopPayVos.size();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		try {
			request.setAttribute("shopPayVos", ActionUtil.page(page, limit, shopPayVos));
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "shopPayList";
	}
	public String bussinessTripList(){
		Map<String, String> queryMap=ActionUtil.createMapByRequest(request,true, "startTime","endTime","userId","userName");
		ListResult<TripEntity> tripList=tripService.getTripByKeys(queryMap,page,limit);
		ActionUtil.setListResult(request, tripList,page,limit);
		count = tripList.getTotalCount();
		selectedPanel="bussinessTripList";
		request.setAttribute("tripList", tripList.getList());
		return "bussinessTripList";
	}
	@Getter
	@Setter
	private InputStream excelFile;
	@Getter
	private String excelFileName;
	public String  exportBussniessTrip(){
		try{
			Map<String, String> queryMap=ActionUtil.createMapByRequest(request,true, "startTime","endTime","userId","userName");
			HSSFWorkbook workbook = tripService.exportTrips(queryMap);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("出差明细.xlsx".getBytes(), "ISO8859-1");
			output.flush();
			output.close();
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "导出excel失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "exportBussniessTrip";
	}
	public String showApproveDetail(){
		String processInstanceID = request.getParameter("processInstanceID");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstanceID);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		String department = "";
		String userId = "";
		String businessType = request.getParameter("businessType");
		switch(businessType){
		case Constants.CONTRACT_SIGN:
			ContractSignVo contractSignVo = contractService.getContractSignVoByProcessInstanceId(processInstanceID);
			request.setAttribute("contractSignVo", contractSignVo);
			userId = contractSignVo.getUserID();
			List<Attachment> lawAttas = new ArrayList<>();
			List<Attachment> contractAttas = new ArrayList<>();
			for(Attachment atta: attas){
				String description = atta.getDescription();
				//附件是关于法务审批的
				if(Constants.LAW_WORK_AUDIT.equals(description)){
					lawAttas.add(atta);
				}else{
					contractAttas.add(atta);
				}
			}
			request.setAttribute("lawAttas", lawAttas);
			request.setAttribute("contractAttas", contractAttas);
			break;
		case Constants.CONTRACT_CHANGE:
			ChangeContractVo changeContractVo = contractService.getChangeContractVoByProcessInstanceId(processInstanceID);
			request.setAttribute("changeContractVo", changeContractVo);
			userId = changeContractVo.getUserID();
			break;
		case Constants.BANK_ACCOUNT:
			ChangeBankAccountVo changeBankAccountVo = bankAccountService.getChangeBankAccountVoByProcessInstanceId(processInstanceID);
			request.setAttribute("changeBankAccountVo", changeBankAccountVo);
			userId = changeBankAccountVo.getUserID();
			break;
		case Constants.CHOP_DESTROY:
			ChopDestroyVo chopDestroyVo = chopService.getChopDestroyVoByProcessInstanceId(processInstanceID);
			request.setAttribute("chopDestroyVo", chopDestroyVo);
			userId = chopDestroyVo.getUserID();
			List<Attachment> attasForChopDestroy = taskService.getProcessInstanceAttachments(processInstanceID);
			request.setAttribute("attas", attasForChopDestroy);
			break;
		case Constants.PURCHASE_PROPERTY:
			PurchasePropertyVo purchasePropertyVo = purchasePropertyService.getPurchasePropertyVoByProcessInstanceId(processInstanceID);
			request.setAttribute("purchasePropertyVo", purchasePropertyVo);
			userId = purchasePropertyVo.getUserID();
			PurchasePropertyEntity purchaseProperty = purchasePropertyService.getPurchasePropertyUserIdByInstanceId(processInstanceID);
			request.setAttribute("id", purchaseProperty.getId());
			break;
		case Constants.CARVE_CHOP:
			CarveChopVo carveChopVo = chopService.getCarveChopVoByProcessInstanceId(processInstanceID);
			request.setAttribute("carveChopVo", carveChopVo);
			userId = carveChopVo.getUserID();
			break;
		case Constants.HANDLE_PROPERTY:
			HandlePropertyVo handlePropertyVo = handlePropertyService.getHandlePropertyVoByProcessInstanceId(processInstanceID);
			request.setAttribute("handlePropertyVo", handlePropertyVo);
			userId = handlePropertyVo.getUserID();
			break;
		case Constants.TRANSFER_PROPERTY:
			TransferPropertyVo transferPropertyVo = transferPropertyService.getTransferPropertyVoByProcessInstanceId(processInstanceID);
			request.setAttribute("transferPropertyVo", transferPropertyVo);
			userId = transferPropertyVo.getUserID();
		case Constants.SHOP_APPLY:
			ShopApplyVo shopApplyVo = shopApplyService.getShopApplyVoByProcessInstanceId(processInstanceID);
			request.setAttribute("shopApplyVo", shopApplyVo);
			List<Attachment> shopApplyAttas = taskService.getProcessInstanceAttachments(processInstanceID);
			request.setAttribute("attas", shopApplyAttas);
			userId = shopApplyVo.getUserID();
			break;
		case Constants.SHOP_PAY_APPLY:
			ShopPayApplyVo shopPayApplyVo = shopApplyService.getShopPayApplyVoByProcessInstanceId(processInstanceID);
			request.setAttribute("shopPayApplyVo", shopPayApplyVo);
			/*List<SpreadShopEntity> spreadShops = shopApplyService.getSpreadShops(shopPayApplyVo.getSpreadId());
				request.setAttribute("spreadShops", spreadShops);*/
			Map<String, String> resultMap = new HashMap<>();
			List<ShopPayApplyListVo> shopPayApplyListVos = shopApplyService.getShopPayApplyListVos(shopPayApplyVo, resultMap);
			request.setAttribute("shopPayApplyListVos", shopPayApplyListVos);
			request.setAttribute("resultMap", resultMap);
			String userIdStr = shopPayApplyVo.getUserID();
			String[] userIds = userIdStr.split(",");
			if(userIds.length>0){
				userId = userIds[0];
			}
			break;
		case Constants.CHOP_BORROW:
			ChopBorrrowVo chopBorrrowVo = chopService.getChopByInstanceId(processInstanceID);
			request.setAttribute("chopBorrrowVo", chopBorrrowVo);
			userId = chopBorrrowVo.getUser_Id();
			Date addTime = chopBorrrowVo.getAddTime();
			String year = new SimpleDateFormat("yyyy").format(addTime);
			request.setAttribute("year", year);
			String userName = staffService.getRealNameByUserId(chopBorrrowVo.getUser_Id());
			request.setAttribute("userName", userName);
			break;
		}
		List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
		if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
			GroupDetailVO group = groupDetails.get(0);
			department += group.getCompanyName() + "-" + group.getDepartmentName();
		}
		List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().processInstanceId(processInstanceID).list();
		for (HistoricDetail historicDetail : historicDetails) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;

			if ("supervisor".equals(variable.getVariableName())) {

				String  supervisor = (String)variable.getValue();
				request.setAttribute("supervisor", supervisor);
			}
		}
		request.setAttribute("attas", attas);
		request.setAttribute("department", department);
		request.setAttribute("businessType", businessType);
		request.setAttribute("processInstanceID", processInstanceID);
		request.setAttribute("from", request.getParameter("from"));
		String selectedPanel = request.getParameter("selectedPanel");
		if(StringUtils.isNotBlank(selectedPanel)){
			request.setAttribute("selectedPanel", selectedPanel);
		}
		return "showApproveDetail";
	}
	@Setter
	private Integer index;
	@Getter
	private InputStream inputStream;
	public String showImg() {
		String processInstanceID = request.getParameter("processInstanceID");
		if (processInstanceID != null) {
			List<Attachment> attachments = taskService.getProcessInstanceAttachments(processInstanceID);
			if (index == null)
				index = 0;
			if (attachments.size() > 0) {
				inputStream = taskService.getAttachmentContent(attachments.get(index).getId());
			}
		}
		return "imgStream";
	}
	public void showShopPayApplyDetail(){
		String id = request.getParameter("id");
		String applyType = request.getParameter("applyType");
		Map<String, Object> resultMap = new HashMap<>();
		if(Constants.PAY_SPREAD.equals(applyType)){
			String spreadId = shopApplyService.getSpreadId(id);
			//获取推广充值项
			List<SpreadShopEntity> spreadShops = shopApplyService.getSpreadShops(spreadId);
			resultMap.put("spreadShops", spreadShops);
		}else if(Constants.PAY_PLUG_IN.equals(applyType)){
			ShopPayPluginEntity shopPayPlugin = shopApplyService.getShopPayPlugin(id);
			resultMap.put("shopPayPlugin", shopPayPlugin);
		}else{
			ShopOtherPayEntity shopOtherPay = shopApplyService.getShopOtherPay(id);
			resultMap.put("shopOtherPay", shopOtherPay);
		}
		resultMap.put("applyType", applyType);
		printByJson(resultMap);
	}
	public String auditShopApply(){
		User user = (User) request.getSession().getAttribute("user");
		String idAndApplyTypeStr = request.getParameter("idAndApplyTypes");
		String auditResult = request.getParameter("auditResult");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String[] idAndApplyTypes = idAndApplyTypeStr.split(",");
		try {
			shopApplyService.startShopPayApply(idAndApplyTypes, auditResult, user.getId(), beginDate, endDate);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "店铺付费申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "render_shopPayList";
	}
	public String findContractSignList(){
		User user = (User) request.getSession().getAttribute("user");
		String applyerId = request.getParameter("applyerId");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		//根据审批人来获取签署的合同列表
		ListResult<Object> contractSignVos = contractService.getContractSignVoByAssigner(user.getId(), applyerId, beginDate, endDate, page, limit);
		count = contractSignVos.getTotalCount();
		totalPage = count%limit ==0 ? count/limit : count/limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("contractSignVos", contractSignVos.getList());
		request.setAttribute("applyerId", applyerId);
		request.setAttribute("applyer", request.getParameter("applyer"));
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		selectedPanel = "contractSignList";
		return "contractSignList";
	}
	public String findShopPayAuditList(){
		User user = (User) request.getSession().getAttribute("user");
		String applyerId = request.getParameter("applyerId");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		//根据审批人来获取店铺付费的申请列表
		ListResult<Object> shopPayApplyVos = shopApplyService.getShopPayApplyVosByAssigner(user.getId(), applyerId, beginDate, endDate, page, limit);
		count = shopPayApplyVos.getTotalCount();
		totalPage = count%limit ==0 ? count/limit : count/limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("shopPayApplyVos", shopPayApplyVos.getList());
		request.setAttribute("applyerId", applyerId);
		request.setAttribute("applyer", request.getParameter("applyer"));
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		selectedPanel = "shopPayList";
		return "shopPayAuditList";
	}
	public String processHistoryForApprovaRecord() {
		String processInstanceID = request.getParameter("processInstanceID");
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		selectedPanel =  request.getParameter("selectedPanel");
		return "processHistoryForApprovaRecord";
	}
	public String viewColleagueWorkReport(){
		User user = (User)request.getSession().getAttribute("user");
		boolean showDep = viewReportService.checkCanViewDepWorkReport(user.getId());
		request.setAttribute("showDep", showDep);
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		return "viewColleagueWorkReport";
	}
	@Setter
	@Getter
	private ViewWorkReportVo viewReportVo;
	@Getter
	private String alertMessage;
	public String viewColleagueWorkReportByconditions(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			viewReportVo.setRequestUserId(user.getId());
			viewReportVo.setPage(page);
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (viewReportVo != null && viewReportVo.get_companyID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(viewReportVo.get_companyID());
				if (departmentVOs != null && viewReportVo.get_departmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = viewReportVo.get_departmentID();
					while (selectedDepartmentID != 0) {
						selectedDepartmentIDs.add(0, selectedDepartmentID);
						for (DepartmentVO departmentVO : departmentVOs) {
							if (departmentVO.getDepartmentID() == selectedDepartmentID) {
								selectedDepartmentID = departmentVO.getParentID();
							}
						}
					}
					request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
				}
				request.setAttribute("departmentVOs", departmentVOs);
			}
			request.setAttribute("companyVOs", companyVOs);
			boolean showDep = viewReportService.checkCanViewDepWorkReport(user.getId());
			request.setAttribute("showDep", showDep);
			String userName = viewReportVo.getUserName();
			String userId = viewReportVo.getUserID();
			Integer companyId = viewReportVo.get_companyID();
			Integer depId = viewReportVo.get_departmentID();
			//点击查询按钮算用掉了一次机会，翻页不算
			if(page == 1){
				//检查是否申请过此公司部门
				if(StringUtils.isBlank(userName) && null!=companyId){
					//没有申请过
					if(!viewReportService.checkApplyThisDep(companyId, depId, user.getId())){
						alertMessage = "你没有权限查看，请先申请";
						return "viewColleagueWorkReport";
					}else{
						
					}
				}else if(StringUtils.isNotBlank(userName)){
					//检查是否申请查看过此人
					if(!viewReportService.checkApplyThisUser(userId, user.getId())){
						alertMessage = "你没有权限查看，请先申请";
						return "viewColleagueWorkReport";
					}else{
						//删除一次性的
						viewReportService.deleteUserIdByOneTime(userId, user.getId());
					}
				}
			}
			ListResult<WorkReportDetailVO> workReports = viewReportService.getWorkReportsByConditions(viewReportVo, page, limit);
			if(workReports.getList().size()<1){
				alertMessage = "没有日报记录，查看权限可能已用完";
				return "viewColleagueWorkReport";
			}
			//判断是否是一次性的部门，若是，删除已查看
			if(viewReportVo.isOneTime()){
				for(WorkReportDetailVO workReport: workReports.getList()){
					String reporterId = workReport.getUserID();
					//删除一次性的
					viewReportService.deleteUserIdByOneTime(reporterId, user.getId());
				}
				
			}
			request.setAttribute("workReportDetailVOs", workReports.getList());
			count = workReports.getTotalCount();
			totalPage = count%limit==0 ? count/limit : count/limit+1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查看失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "viewColleagueWorkReport";
	}
	public String findBrandAuthList(){
		String companyName = request.getParameter("companyName");
		ListResult<BrandAuthVo> brandAuthList = brandAuthService.findAllBrandAuthList(companyName, page, limit);
		request.setAttribute("brandAuthVos", brandAuthList.getList());
		count = brandAuthList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		selectedPanel = "allBrandAuthList";
		request.setAttribute("companyName", companyName);
		return "brandAuthList";
	}
	public String toBrandAuth(){
		selectedPanel = "addBrandAuth";
		return "toBrandAuth";
	}
	public String newBrandAuth(){
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		selectedPanel = "addBrandAuth";
		return "newBrandAuth";
	}
	public String startBrandAuth(){
		try {
			brandAuthService.startBrandAuth(brandAuthVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "品牌授权申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "render_myBrandAuthList";
	}
	public String findMyBrandAuthList(){
		User user = (User) request.getSession().getAttribute("user");
		ListResult<BrandAuthVo> brandAuthList = brandAuthService.findBrandAuthListByUserID(user.getId(), page, limit);
		request.setAttribute("brandAuthVos", brandAuthList.getList());
		count = brandAuthList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		selectedPanel = "myBrandAuthList";
		return "myBrandAuthList";
	}
	public String toAuditBrandAuth(){
		try {
			String processInstanceID = request.getParameter("processInstanceID");
			String taskId = request.getParameter("taskId");
			String taskName = request.getParameter("taskName");
			BrandAuthVo brandAuth = brandAuthService.getBrandAuthVoByInstanceId(processInstanceID);
			List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
			request.setAttribute("comments", comments);
			request.setAttribute("brandAuth", brandAuth);
			request.setAttribute("taskId", taskId);
			request.setAttribute("taskName", taskName);
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(brandAuth.getUserId());
			String department = "";
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
			List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
			request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "auditBrandAuth";
	}
	public String auditBrandAuth(){
		User user = (User) request.getSession().getAttribute("user");
		String taskID = request.getParameter("taskId");
		String processInstanceID = request.getParameter("processInstanceId");
		String comment = request.getParameter("comment");
		String result = request.getParameter("result");
		try {
			// 完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
			brandAuthService.updateProcessStatus(processInstanceID, result);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = "处理失败：" + e.getMessage();
			return "error";
		}
		type = BusinessTypeEnum.BRAND_AUTH.getValue();
		return "taskComplete";
	}
	public String toApplyChop(){
		String processInstanceID = request.getParameter("processInstanceID");
		BrandAuthVo brandAuth = brandAuthService.getBrandAuthVoByInstanceId(processInstanceID);
		request.setAttribute("brandAuth", brandAuth);
		return "toApplyChop";
	}
	public String showBrandAuthDetail(){
		String processInstanceID = request.getParameter("processInstanceID");
		BrandAuthVo brandAuth = brandAuthService.getBrandAuthVoByInstanceId(processInstanceID);
		request.setAttribute("brandAuth", brandAuth);
		selectedPanel = request.getParameter("selectedPanel");
		return "showBrandAuthDetail";
	}
}
