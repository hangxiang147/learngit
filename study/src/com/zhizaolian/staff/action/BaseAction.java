package com.zhizaolian.staff.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PerformanceService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.service.ShopApplyService;
import com.zhizaolian.staff.service.ShopSaleService;
import com.zhizaolian.staff.service.SigninService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.ShopPayVo;

import lombok.Getter;

public class BaseAction extends ActionSupport implements Action, ServletRequestAware {

	public static final Logger logger = Logger.getLogger(BaseAction.class);

	private static final long serialVersionUID = 1L;

	public HttpServletRequest request;
	@Getter
	public int count = 0;
	@Getter
	public int hrCount = 0;
	@Getter
	public int formalHRCount = 0;
	@Getter
	public int reimbursementCount = 0;
	@Getter
	public int advanceCount=0;
	@Getter
	public int paymentCount=0;
	@Getter
	public int emailCount = 0;
	@Getter
	public int cardCount = 0;
	@Getter
	public int hasSignin;
	@Getter
	public int unReadNoticeCount = 0;
	@Getter
	public int  bussinessTripCount=0;
	@Getter
	public int socialSecurityCount = 0;	
	@Getter
	private int shopPayCount = 0;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private  ReimbursementService reimbursementService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private SigninService signinService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private ShopApplyService shopApplyService;
	@Autowired
	private ShopSaleService shopSaleService;
	@Autowired
	private PerformanceService performanceService;
	@Autowired
	private StaffService staffService;
	
	public String execute() throws Exception {
		return "";
	}

	public void setServletRequest(HttpServletRequest request) {  
		this.request = request;  
		init();
	}
	public void init(){
		User user = (User) request.getSession().getAttribute("user");
		if (user != null) {
			count = (int) taskService.createTaskQuery().taskAssignee(user.getId()).count();
			count += taskService.createTaskQuery().processDefinitionKey(Constants.PURCHASE_PROPERTY).taskCandidateUser(user.getId()).count();
			count += taskService.createTaskQuery().processDefinitionKey(Constants.SHOP_APPLY).taskCandidateUser(user.getId()).count();
			count += taskService.createTaskQuery().processDefinitionKey(Constants.SHOP_PAY_APPLY).taskCandidateUser(user.getId()).count();
			count += taskService.createTaskQuery().processDefinitionKey(Constants.BANK_ACCOUNT).taskCandidateUser(user.getId()).count();
			count += taskService.createTaskQuery().processDefinitionKey(Constants.PERFORMANCE).taskCandidateUser(user.getId()).count();
			count += taskService.createTaskQuery().processDefinitionKey(Constants.PERSONAL_PERFORMANCE).taskCandidateUser(user.getId()).count();
			//count += shopSaleService.getUnCompletedTaskByUserId(user.getId());
			count += performanceService.getPerformanceTaskCount(user.getId(), BusinessTypeEnum.PERFORMANCE_TARGET);
			if(staffService.isPM(user.getId())){
				count += performanceService.getPerformanceTaskCount(null, BusinessTypeEnum.PERFORMANCE_ACTUAL);
			}
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			if (groups.size() > 0) {
				List<TaskDefKeyEnum> tasks = new ArrayList<TaskDefKeyEnum>();
				tasks.clear();
				tasks.add(TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT);
				tasks.add(TaskDefKeyEnum.REMIT_MONEY);
				tasks.add(TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT);
				reimbursementCount = reimbursementService.findReimbursementsByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), null, null, null, 0, 1).getTotalCount();
				tasks.clear();
				tasks.add(TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT);
				tasks.add(TaskDefKeyEnum.REMIT_MONEY);
				tasks.add(TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT);
				tasks.add(TaskDefKeyEnum.ADVANCE_UPLOAD_INVOICE);
				advanceCount = reimbursementService.findAdvancessByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), null, null, null, 0, 1).getTotalCount();
				tasks.clear();
				tasks.add(TaskDefKeyEnum.FINANCIAL_SECOND_AUDIT);
				tasks.add(TaskDefKeyEnum.REMIT_MONEY);
				tasks.add(TaskDefKeyEnum.FINANCIAL_FIRST_AUDIT);
				paymentCount = reimbursementService.findPaymentsByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), null, null, null, 0, 1).getTotalCount();
				tasks.clear();
				tasks.add(TaskDefKeyEnum.EMAIL_AUDIT);
				tasks.add(TaskDefKeyEnum.OPEN_MAILBOX);
				emailCount = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();
				tasks.clear();
				tasks.add(TaskDefKeyEnum.CARD_AUDIT);
				tasks.add(TaskDefKeyEnum.MAKE_CARD);
				cardCount = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();
				tasks.clear();
				tasks.add(TaskDefKeyEnum.BUSSINESSTRIP_BUYTICKET);
				tasks.add(TaskDefKeyEnum.BUSSINESSTRIP_CONFIRM);
				bussinessTripCount=processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();
				tasks.clear();
				tasks.add(TaskDefKeyEnum.SS_FINANCIAL_PROCESSING);
				socialSecurityCount = processService.findTasksByUserGroupIDs(tasks, groups, Arrays.asList(user.getId()), 0, 1).getTotalCount();	
				tasks.clear();
				List<ShopPayVo> shopPayVos = shopApplyService.getShopPayList(user.getId());
				if(null != shopPayVos){
					shopPayCount = shopPayVos.size();
				}
			}
			Date date=new Date();
			hasSignin = signinService.findSingleByDateUserID(DateUtil.getSimpleDate(DateUtil.formateDate(date)), user.getId());
			unReadNoticeCount = noticeService.countUnReadNoticeByUserID(user.getId());
		}
	}
	protected void printByJson(Object object)
	{
		printByJson(object, "application/json");
	}

	protected void printByJson(Object obj, String type) {
		PrintWriter out = null;
		HttpServletResponse httpServletResponse = ServletActionContext.getResponse();
		httpServletResponse.setContentType(type);
		httpServletResponse.setCharacterEncoding("utf-8");
		String json = null;
		try {
			out = httpServletResponse.getWriter();
			json = JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(json);
		out.close();
	}

	protected void printByJson(String str, String type) {
		PrintWriter out = null;
		HttpServletResponse httpServletResponse = ServletActionContext.getResponse();
		httpServletResponse.setContentType(type);
		httpServletResponse.setCharacterEncoding("utf-8");
		try {
			out = httpServletResponse.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(str);
		out.close();
	}

	private final static String SUCCESS="{\"success\":true}";
	private final static String FAIL="{\"success\":false,\"extraMsg\":\"@R\"}";
	protected void returnSuccess(){
		printByJson(SUCCESS);
	}
	protected void returnFail(String extraMsg){
		printByJson(FAIL.replace("@R", extraMsg));
	}
}
