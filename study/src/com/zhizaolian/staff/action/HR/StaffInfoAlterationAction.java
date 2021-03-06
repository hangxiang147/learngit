package com.zhizaolian.staff.action.HR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.SocialSecurityService;
import com.zhizaolian.staff.service.StaffInfoAlterationService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.ShortMsgSender;
import com.zhizaolian.staff.vo.GradeVO;
import com.zhizaolian.staff.vo.HousingFundVO;
import com.zhizaolian.staff.vo.SocialSecurityProcessVO;
import com.zhizaolian.staff.vo.SocialSecurityVO;
import com.zhizaolian.staff.vo.StaffInfoAlterationVO;
import com.zhizaolian.staff.vo.StaffVO;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;

/**
 * 员工信息变动的Action
 * @author wjp
 */
public class StaffInfoAlterationAction extends BaseAction{

	@Getter
	private String selectedPanel; 
	@Getter
	@Setter
	private String panel;
	@Getter
	@Setter
	private String errorMessage;
	@Getter
	@Setter
	private StaffVO staffVO;
	@Setter
	@Getter
	private StaffInfoAlterationVO staffInfoAlterationVO;
	@Setter
	@Getter
	private SocialSecurityVO socialSecurityVO;
	@Setter
	@Getter
	private HousingFundVO housingFundVO;
	@Setter
	@Getter
	private List<SocialSecurityVO> socialSecurityVOs;
	@Setter
	@Getter
	private double hfPersonalCount = 0;
	@Setter
	@Getter
	private double hfCompanyCount = 0;
	@Setter
	@Getter
	private double hfTotalCount = 0;
	@Setter
	@Getter
	private List<HousingFundVO> housingFundVOs;
	@Setter
	@Getter
	private double ssTotalCount = 0;
	@Setter
	@Getter
	private int ssYear;
	@Setter
	@Getter
	private int ssMonth;
	@Setter
	@Getter
	private int hfYear;
	@Setter
	@Getter
	private int hfMonth;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;

	@Autowired
	private StaffService staffService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private StaffInfoAlterationService staffInfoAlterationService;
	@Autowired
	private SocialSecurityService socialSecurityService;
	@Autowired
	private IdentityService identityService;
	@Setter
	@Getter
	private File[] attachment;
	@Setter
	@Getter
	private String[] attachmentFileName;
	@Autowired
	private NoticeService noticeService;
	@Setter
	@Getter
	private InputStream inputStream;
	@Setter
	@Getter
	private String downloadFileName;
	
	private static ShortMsgSender shortMsgSender = ShortMsgSender.getInstance();
	
	private static final long serialVersionUID = 1L;
	/*
	 * 打开职级变更页面请求该方法
	 */
	public String gradeAlteration() {

		try {
			List<GradeVO> gradeVOs = positionService.findAllGrades(); //查询出职级列表
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(StaffStatusEnum.PROBATION.getValue());
			statusList.add(StaffStatusEnum.PRACTICE.getValue());
			statusList.add(StaffStatusEnum.FORMAL.getValue());
			List<StaffVO> staffVOs = staffService.findStaffPageListByStatusList(statusList, page, limit);
			count = staffService.countStaffByStatusList(statusList);
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
			request.setAttribute("gradeVOs", gradeVOs); 
			request.setAttribute("staffVOList", staffVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "infoAlteration";
		selectedPanel = "gradeAlteration";
		return "gradeAlteration";

	}

	/*
	 * 打开薪资调整页面时请求的方法
	 */
	public String salaryAlteration() {

		try {
			List<GradeVO> gradeVOs = positionService.findAllGrades(); //查询出职级列表
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(StaffStatusEnum.PROBATION.getValue());
			statusList.add(StaffStatusEnum.PRACTICE.getValue());
			statusList.add(StaffStatusEnum.FORMAL.getValue());
			List<StaffVO> staffVOs = staffService.findStaffPageListByStatusList(statusList, page, limit);
			count = staffService.countStaffByStatusList(statusList);
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
			request.setAttribute("gradeVOs", gradeVOs); 
			request.setAttribute("staffVOList", staffVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "infoAlteration";
		selectedPanel = "salaryAlteration";
		return "salaryAlteration";

	}

	/*
	 * 利用员工的userID查询职级变动历史记录
	 */
	public String gradeHistory() {

		try{
			ListResult<StaffInfoAlterationVO> staffInfoAlterationVOList = staffInfoAlterationService.gradeHistory(staffInfoAlterationVO.getUserID(),page,limit);
//			int count = 0;
			if(staffInfoAlterationVOList!=null) {
				request.setAttribute("staffInfoAlterationVOList", staffInfoAlterationVOList.getList());
				count = staffInfoAlterationVOList.getTotalCount();
			}
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
		} catch (Exception e) {
			e.printStackTrace();
			panel = "infoAlteration";
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage();
			selectedPanel = "gradeAlteration";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "infoAlteration";
		selectedPanel = "gradeAlteration";
		return "gradeHistory";
	}

	/*
	 * 利用员工的userID查询薪资变动历史记录
	 */
	public String salaryHistory() {

		try{
			ListResult<StaffInfoAlterationVO> staffInfoAlterationVOList = staffInfoAlterationService.salaryHistory(staffInfoAlterationVO.getUserID(),page,limit);
//			int count = 0;
			if(staffInfoAlterationVOList!=null) {
				request.setAttribute("staffInfoAlterationVOList", staffInfoAlterationVOList.getList());
				count = staffInfoAlterationVOList.getTotalCount();
			}
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
		} catch (Exception e) {
			e.printStackTrace();
			panel = "infoAlteration";
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage();
			selectedPanel = "salaryAlteration";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "infoAlteration";
		selectedPanel = "salaryAlteration";
		return "salaryHistory";
	}

	public String update() {

		//得到userID gradeID 在service层记录表 员工信息表同时变动
		try {
			User user = (User)ServletActionContext.getContext().getSession().get("user");
			if(user==null) {
				return null;
			}
			String userID = StringUtils.isBlank(request.getParameter("userID")) ? null : request.getParameter("userID");
			String salaryAfter = StringUtils.isBlank(request.getParameter("salaryAfter")) ? null : request.getParameter("salaryAfter");
			Integer gradeAfter = (Integer) (StringUtils.isBlank(request.getParameter("gradeAfter")) ? null : Integer.parseInt(request.getParameter("gradeAfter")));
			Integer type = (Integer) (StringUtils.isBlank(request.getParameter("type")) ? null : Integer.parseInt(request.getParameter("type")));
			if(staffInfoAlterationVO==null)
				staffInfoAlterationVO = new StaffInfoAlterationVO();
			staffInfoAlterationVO.setOperatorID(user.getId()); //操作人的userID
			if(salaryAfter!=null)
				staffInfoAlterationVO.setSalaryAfter(salaryAfter); //修改之后的薪资
			staffInfoAlterationVO.setUserID(userID); //被操作员工的userID
			staffInfoAlterationVO.setType(type); //设置修改类型
			if(gradeAfter!=null)
				staffInfoAlterationVO.setGradeIDAfter(gradeAfter); //修改后的职级
			staffInfoAlterationService.update(staffInfoAlterationVO);

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "infoAlteration";
		selectedPanel = "gradeAlteration";
		return "update";
	}
	/*
	 * 通过姓名和职级查询出员工列表   
	 * wjp
	 */
	public String findStaffByGradeAndNameGrade() {

		try {
			List<GradeVO> gradeVOs = positionService.findAllGrades(); //查询出职级列表
			ListResult<StaffVO> listResult= staffInfoAlterationService.findStaffByGradeAndName(staffVO,page,limit);
//			int count = 0;
			if(listResult!=null) {
				count = listResult.getTotalCount();
				request.setAttribute("staffVOList", listResult.getList());
			}
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
			request.setAttribute("gradeVOs", gradeVOs); 
			request.setAttribute("questName", staffVO.getLastName()); //向request域中保存表单提交上来的数据，用于回显
			request.setAttribute("questGrade", staffVO.getGradeID()); //向request域中保存表单提交上来的数据，用于回显
		} catch (Exception e) {
			e.printStackTrace();
			panel = "infoAlteration";
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage()+e.toString();
			selectedPanel = "gradeAlteration";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "infoAlteration";
		selectedPanel = "gradeAlteration";
		return "gradeAlteration";

	}


	public String findStaffByGradeAndNameSalary() {

		try {
			List<GradeVO> gradeVOs = positionService.findAllGrades(); //查询出职级列表
			ListResult<StaffVO> listResult= staffInfoAlterationService.findStaffByGradeAndName(staffVO,page,limit);
//			int count = 0;
			if(listResult!=null) {
				count = listResult.getTotalCount();
				request.setAttribute("staffVOList", listResult.getList());
			}
			totalPage = count%limit==0 ? count/limit : count/limit+1;
			if(totalPage==0)
				totalPage = 1;
			request.setAttribute("gradeVOs", gradeVOs); 
			request.setAttribute("questName", staffVO.getLastName()); //向request域中保存表单提交上来的数据，用于回显
			request.setAttribute("questGrade", staffVO.getGradeID()); //向request域中保存表单提交上来的数据，用于回显
		} catch (Exception e) {
			e.printStackTrace();
			panel = "infoAlteration";
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage()+e.toString();
			selectedPanel = "salaryAlteration";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		panel = "infoAlteration";
		selectedPanel = "salaryAlteration";
		return "salaryAlteration";

	}

	public String addSocialSecurity() {
		if (!StringUtils.isBlank(request.getParameter("year"))) {
			socialSecurityVO.setPaymentYear(Integer.parseInt(request.getParameter("year")));
			socialSecurityVO.setPaymentMonth(Integer.parseInt(request.getParameter("month")));
		} 

		if (socialSecurityVO.getCompanyID() == null) {
			socialSecurityVO.setCompanyID(Integer.parseInt(request.getParameter("companyID")));
		}

		try {
			socialSecurityService.save(socialSecurityVO);
		} catch (Exception e) {
			e.printStackTrace();
			panel = "infoAlteration";
			errorMessage = "保存失败："+e.getMessage();
			selectedPanel = "socialSecurityList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		hfYear = socialSecurityVO.getPaymentYear();
		hfMonth = socialSecurityVO.getPaymentMonth();
		return "addSocialSecurity";
	}

	public String addHousingFund() {
		if (!StringUtils.isBlank(request.getParameter("year"))) {
			housingFundVO.setPaymentYear(Integer.parseInt(request.getParameter("year")));
			housingFundVO.setPaymentMonth(Integer.parseInt(request.getParameter("month")));
		} 

		if (housingFundVO.getCompanyID() == null) {
			housingFundVO.setCompanyID(Integer.parseInt(request.getParameter("companyID")));
		}

		try {
			socialSecurityService.save(housingFundVO);
		} catch (Exception e) {
			e.printStackTrace();
			panel = "infoAlteration";
			errorMessage = "保存失败："+e.getMessage();
			selectedPanel = "socialSecurityList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		ssYear = housingFundVO.getPaymentYear();
		ssMonth = housingFundVO.getPaymentMonth();
		return "addHousingFund";
	}

	public String updateHousingFund() {
		int hfID = Integer.parseInt(request.getParameter("hfID"));
		HousingFundVO housingFundVO = socialSecurityService.getHousingFundByID(hfID);
		StaffVO staffVO = staffService.getStaffByUserID(housingFundVO.getUserID());
		if (staffVO == null) {
			panel = "infoAlteration";
			errorMessage = "该员工在系统不存在！";
			selectedPanel = "socialSecurityList";
			return "error";
		}

		housingFundVO.setUserName(staffVO.getLastName());
		request.setAttribute("housingFundVO", housingFundVO);
		request.setAttribute("panel", request.getParameter("panel"));
		request.setAttribute("taskID", request.getParameter("taskID"));
		selectedPanel = "socialSecurityList";
		return "updateHousingFund";
	}

	public String updateSocialSecurity() {
		int ssID = Integer.parseInt(request.getParameter("ssID"));
		SocialSecurityVO socialSecurityVO = socialSecurityService.getSocialSecurityByID(ssID);
		StaffVO staffVO = staffService.getStaffByUserID(socialSecurityVO.getUserID());
		if (staffVO == null) {
			panel = "infoAlteration";
			errorMessage = "该员工在系统不存在！";
			selectedPanel = "socialSecurityList";
			return "error";
		}
		socialSecurityVO.setUserName(staffVO.getLastName());
		request.setAttribute("socialSecurityVO", socialSecurityVO);
		request.setAttribute("panel", request.getParameter("panel"));
		request.setAttribute("taskID", request.getParameter("taskID"));
		selectedPanel = "socialSecurityList";
		return "updateSocialSecurity";
	}

	public String deleteSocialSecurity() {
		int ssID = Integer.parseInt(request.getParameter("ssID"));
		try {
			socialSecurityService.deleteSocialSecurityByID(ssID);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "删除失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

		return "deleteSocialSecurity";
	}

	public String deleteHousingFund() {
		int hfID = Integer.parseInt(request.getParameter("hfID"));
		try {
			socialSecurityService.deleteHousingFundByID(hfID);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "删除失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

		return "deleteHousingFund";
	}

	public String findSocialSecurityByTime() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "findSocialSecurityByTime";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			errorMessage = "您的岗位信息为空，请联系人事";
			return "findSocialSecurityByTime";
		} 

		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		housingFundVOs = socialSecurityService.findHousingFundListByTime(year, month, companyID);
		if (CollectionUtils.isEmpty(housingFundVOs)) {
			housingFundVOs = copyLastProcessSocialSecurityListBySSTime(year, month, companyID, year, month);
			if (CollectionUtils.isEmpty(housingFundVOs)) {
				housingFundVOs = copyLastProcessSocialSecurityListBySSTime(month==1?year-1:year, month==1?12:month-1, companyID, year, month);
			}
		}

		for (HousingFundVO housingFundVO : housingFundVOs) {
			ssTotalCount += housingFundVO.getTotalCount();
		}

		return "findSocialSecurityByTime";
	}

	public String findHousingFundByTime() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) { 
			errorMessage = "您尚未登录，请先登录！";
			return "findHousingFundByTime";
		}

		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		if (groups.size() <= 0) {
			errorMessage = "您的岗位信息为空，请联系人事";
			return "findHousingFundByTime";
		} 

		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		int year = Integer.parseInt(request.getParameter("year"));
		int month = Integer.parseInt(request.getParameter("month"));
		socialSecurityVOs = socialSecurityService.findSocialSecurityListByTime(year, month, companyID);
		if (CollectionUtils.isEmpty(socialSecurityVOs)) {
			//该月未提交审核记录为空，则取该月最近一次提交申请审核的公积金明细复制
			socialSecurityVOs = copyLastProcessHousingFundListByHFTime(year, month, companyID, year, month);
			if (CollectionUtils.isEmpty(socialSecurityVOs)) {
				//当月未提交过审核申请，取上月最后一次提交申请审核的公积金明细复制
				socialSecurityVOs = copyLastProcessHousingFundListByHFTime(month==1?year-1:year, month==1?12:month-1, companyID, year, month);
			}
		}

		for (SocialSecurityVO socialSecurityVO : socialSecurityVOs) {
			hfPersonalCount += socialSecurityVO.getPersonalProvidentFund();
			hfCompanyCount += socialSecurityVO.getCompanyProvidentFund();
			hfTotalCount += socialSecurityVO.getTotalProvidentFund();
		}

		return "findHousingFundByTime";
	}

	private List<HousingFundVO> copyLastProcessSocialSecurityListBySSTime(int year, int month, int companyID, int toYear, int toMonth) {
		SocialSecurityProcessVO processVO = socialSecurityService.getLastProcessVOBySSTime(year, month, companyID);
		if (processVO == null) {
			return Collections.emptyList();
		}

		List<HousingFundVO> housingFundVOs = socialSecurityService.findHousingFundListByProcessID(processVO.getSspID());
		for (HousingFundVO housingFundVO : housingFundVOs) {
			housingFundVO.setHfID(null);
			housingFundVO.setProcessID(null);
			housingFundVO.setPaymentYear(toYear);
			housingFundVO.setPaymentMonth(toMonth);
			housingFundVO.setHfID(socialSecurityService.save(housingFundVO));
			housingFundVO.setUserName(staffService.getStaffByUserID(housingFundVO.getUserID()).getLastName());
		}
		return housingFundVOs;
	}

	private List<SocialSecurityVO> copyLastProcessHousingFundListByHFTime(int year, int month, int companyID, int toYear, int toMonth) {
		SocialSecurityProcessVO processVO = socialSecurityService.getLastProcessVOByHFTime(year, month, companyID);
		if (processVO == null) {
			return Collections.emptyList();
		}

		List<SocialSecurityVO> socialSecurityVOs = socialSecurityService.findSocialSecurityListByProcessID(processVO.getSspID());
		for (SocialSecurityVO socialSecurityVO : socialSecurityVOs) {
			socialSecurityVO.setSsID(null); 
			socialSecurityVO.setProcessID(null);
			socialSecurityVO.setPaymentYear(toYear);
			socialSecurityVO.setPaymentMonth(toMonth);
			socialSecurityVO.setSsID(socialSecurityService.save(socialSecurityVO));
			socialSecurityVO.setUserName(staffService.getStaffByUserID(socialSecurityVO.getUserID()).getLastName());
		}
		return socialSecurityVOs;
	}
	public String updateSalary(){
		try {
			User user = (User)request.getSession().getAttribute("user");
			staffInfoAlterationVO.setOperatorID(user.getId());
			if(null != attachment){
				int index = 0;
				List<Integer> attachmentIds = new ArrayList<>();
				for(File file: attachment){
					String fileName = attachmentFileName[index];
					File parent = new File(Constants.SALARY_FILE_DIRECTORY);
					parent.mkdirs();
					String saveName = UUID.randomUUID().toString().replaceAll("-", "");
					@Cleanup
					InputStream in = new FileInputStream(file);
					@Cleanup
					OutputStream out = new FileOutputStream(new File(parent, saveName));
					byte[] buffer = new byte[10 * 1024 * 1024];
					int length = 0;
					while ((length = in.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, length);
						out.flush();
					}
					CommonAttachment commonAttachment = new CommonAttachment();
					commonAttachment.setAddTime(new Date());
					commonAttachment.setIsDeleted(0);
					commonAttachment.setSoftURL(
							Constants.SALARY_FILE_DIRECTORY + saveName);
					commonAttachment.setSoftName(fileName);
					commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
					Integer attachmentId = noticeService.saveAttachMent(commonAttachment);
					attachmentIds.add(attachmentId);
					index++;
				}
				if(attachmentIds.size()>0){
					staffInfoAlterationVO.setAttachmentIds(StringUtils.join(attachmentIds, ","));
				}
			}
			staffInfoAlterationService.update(staffInfoAlterationVO);
			//短信提醒
			String staffUserId = staffInfoAlterationVO.getUserID();
			StaffVO staffVo = staffService.getStaffByUserID(staffUserId);
			if(null!=staffVo){
				String telephone = staffVo.getTelephone();
				String name = "";
				if("男".equals(staffVo.getGender())){
					name = staffVo.getLastName()+"先生";
				}else{
					name = staffVo.getLastName()+"女士";
				}
				String msgContent = "【智造链】 尊敬的"+name+"，您的薪资调整为："+staffInfoAlterationVO.getSalaryAfter()+
						"，"+staffInfoAlterationVO.getEffectDate()+"起生效，若有疑问，请及时联系公司人事";
				shortMsgSender.send(telephone, msgContent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "salaryAlteration";
		return "render_updateSalary";
	}
	public void showAttachment(){
		Map<String, Object> resultMap = new HashMap<>();
		String attachmentIdStr = request.getParameter("attachmentIds");
		if(StringUtils.isNotBlank(attachmentIdStr)){
			resultMap.put("hasAtta", true);
			String[] attachmentIds = attachmentIdStr.split(",");
			List<CommonAttachment> attaList = new ArrayList<>();
			for(String attachmentId: attachmentIds){
				CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
				String suffix = attachment.getSuffix();
				//图片
				if(Constants.PIC_SUFFIX.contains(suffix)){
					attachment.setSuffix("png");
				}
				attaList.add(attachment);
			}
			resultMap.put("attaList", attaList);
		}else{
			resultMap.put("hasAtta", false);
		}
		printByJson(resultMap);
	}
	public String showImage(){
		String attachmentPath = request.getParameter("attachmentPath");
		try {
			inputStream = new FileInputStream(new File(attachmentPath)) ;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "showImage";
	}
	public String downloadAttach() {
		try {
			String attachmentPath = request.getParameter("attachmentPath");
			String attachmentName = request.getParameter("attachmentName");
			inputStream = new FileInputStream(new File(attachmentPath));
			// 解决中文乱码
			downloadFileName = new String(attachmentName.getBytes(),
					"ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "downloadAttachment";
	}
}
