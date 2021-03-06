﻿package com.zhizaolian.staff.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.zhizaolian.staff.entity.AdvanceEntity;
import com.zhizaolian.staff.entity.AlterStaffSalaryEntity;
import com.zhizaolian.staff.entity.BankAccountEntity;
import com.zhizaolian.staff.entity.BrandAuthEntity;
import com.zhizaolian.staff.entity.CertificateEntity;
import com.zhizaolian.staff.entity.Chop;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.ContractDetailEntity;
import com.zhizaolian.staff.entity.ContractEntity;
import com.zhizaolian.staff.entity.ContractManageEntity;
import com.zhizaolian.staff.entity.CredentialEntity;
import com.zhizaolian.staff.entity.FunctionEntity;
import com.zhizaolian.staff.entity.MorningMeetingEntity;
import com.zhizaolian.staff.entity.PartnerEntity;
import com.zhizaolian.staff.entity.PerformanceEntity;
import com.zhizaolian.staff.entity.PurchasePropertyEntity;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.entity.ScoreResultEntity;
import com.zhizaolian.staff.entity.ShopOtherPayEntity;
import com.zhizaolian.staff.entity.ShopPayPluginEntity;
import com.zhizaolian.staff.entity.SoftGroupEntity;
import com.zhizaolian.staff.entity.SpreadShopApplyEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.StaffSalaryEntity;
import com.zhizaolian.staff.entity.UserMonthlyRestEntity;
import com.zhizaolian.staff.entity.VacationEntity;
import com.zhizaolian.staff.entity.VersionFuncionInfo;
import com.zhizaolian.staff.entity.WeekReportEntity;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.FormalTypeEnum;
import com.zhizaolian.staff.enums.SoftPerformanceScore;
import com.zhizaolian.staff.enums.SoftPosition;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.AssetService;
import com.zhizaolian.staff.service.AssignmentService;
import com.zhizaolian.staff.service.AttendanceService;
import com.zhizaolian.staff.service.BankAccountService;
import com.zhizaolian.staff.service.BrandAuthService;
import com.zhizaolian.staff.service.CertificateService;
import com.zhizaolian.staff.service.ChopService;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.service.EmailService;
import com.zhizaolian.staff.service.FormalService;
import com.zhizaolian.staff.service.HandlePropertyService;
import com.zhizaolian.staff.service.MorningMeetingReportService;
import com.zhizaolian.staff.service.NoticeActorService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PartnerService;
import com.zhizaolian.staff.service.PerformanceService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ProjectService;
import com.zhizaolian.staff.service.PublicService;
import com.zhizaolian.staff.service.PurchasePropertyService;
import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.service.ShopApplyService;
import com.zhizaolian.staff.service.SigninService;
import com.zhizaolian.staff.service.SocialSecurityService;
import com.zhizaolian.staff.service.SoftPerformanceService;
import com.zhizaolian.staff.service.StaffSalaryService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TransferPropertyService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.service.VersionInfoService;
import com.zhizaolian.staff.service.ViewReportService;
import com.zhizaolian.staff.service.WeekWorkReportService;
import com.zhizaolian.staff.service.WorkOvertimeService;
import com.zhizaolian.staff.service.WorkReportService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.AdvanceTaskVO;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.AssetVO;
import com.zhizaolian.staff.vo.AssignmentVO;
import com.zhizaolian.staff.vo.AttendanceVO;
import com.zhizaolian.staff.vo.BankAccountVO;
import com.zhizaolian.staff.vo.BaseVO;
import com.zhizaolian.staff.vo.BrandAuthVo;
import com.zhizaolian.staff.vo.CarveChopVo;
import com.zhizaolian.staff.vo.CertificateBorrowVo;
import com.zhizaolian.staff.vo.ChangeBankAccountVo;
import com.zhizaolian.staff.vo.ChangeContractVo;
import com.zhizaolian.staff.vo.ChangeSalaryDetailVo;
import com.zhizaolian.staff.vo.ChopBorrrowVo;
import com.zhizaolian.staff.vo.ChopDestroyVo;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.CommonSubjectTaskVo;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.ContractBorrowVo;
import com.zhizaolian.staff.vo.ContractDetailVo;
import com.zhizaolian.staff.vo.ContractSignVo;
import com.zhizaolian.staff.vo.CoursePlanTaskVo;
import com.zhizaolian.staff.vo.DepartmentFrameVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.EmailVO;
import com.zhizaolian.staff.vo.FormField;
import com.zhizaolian.staff.vo.FormalVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.HandlePropertyVo;
import com.zhizaolian.staff.vo.HousingFundVO;
import com.zhizaolian.staff.vo.IdBorrowVo;
import com.zhizaolian.staff.vo.MorningMeetingVo;
import com.zhizaolian.staff.vo.NextWeekWorkPlan;
import com.zhizaolian.staff.vo.NoticeVO;
import com.zhizaolian.staff.vo.PaySalaryTaskVo;
import com.zhizaolian.staff.vo.PaymentTaskVO;
import com.zhizaolian.staff.vo.PaymentVo;
import com.zhizaolian.staff.vo.PerformanceVo;
import com.zhizaolian.staff.vo.PositionVO;
import com.zhizaolian.staff.vo.ProblemOrderVo;
import com.zhizaolian.staff.vo.ProjectInfoVo;
import com.zhizaolian.staff.vo.PublicRelationEventVo;
import com.zhizaolian.staff.vo.PurchasePropertyVo;
import com.zhizaolian.staff.vo.ReimbursementTaskVO;
import com.zhizaolian.staff.vo.ReimbursementVO;
import com.zhizaolian.staff.vo.ResignationVO;
import com.zhizaolian.staff.vo.RewardAndPunishmentVo;
import com.zhizaolian.staff.vo.RiskVo;
import com.zhizaolian.staff.vo.ShopApplyTaskVo;
import com.zhizaolian.staff.vo.ShopApplyVo;
import com.zhizaolian.staff.vo.ShopPayApplyListVo;
import com.zhizaolian.staff.vo.ShopPayApplyVo;
import com.zhizaolian.staff.vo.SigninVO;
import com.zhizaolian.staff.vo.SocialSecurityProcessVO;
import com.zhizaolian.staff.vo.SocialSecurityVO;
import com.zhizaolian.staff.vo.SoftPerformanceTaskVO;
import com.zhizaolian.staff.vo.SoftPerformanceVo;
import com.zhizaolian.staff.vo.SpreadShopVo;
import com.zhizaolian.staff.vo.StaffAuditVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;
import com.zhizaolian.staff.vo.ThisWeekWorkVo;
import com.zhizaolian.staff.vo.TransferPropertyVo;
import com.zhizaolian.staff.vo.VacationTaskVO;
import com.zhizaolian.staff.vo.VacationVO;
import com.zhizaolian.staff.vo.WorkOvertimeTaskVo;
import com.zhizaolian.staff.vo.WorkOvertimeVo;
import com.zhizaolian.staff.vo.WorkReportDetailVO;
import com.zhizaolian.staff.vo.WorkReportVO;
import com.zhizaolian.staff.vo.ViewWorkReportVo;

import lombok.Getter;
import lombok.Setter;

public class PersonalAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String selectedPanel;
	@Getter
	private String errorMessage;
	@Getter
	private InputStream inputStream;
	@Getter
	private InputStream vacationPicture;
	@Setter
	@Getter
	private StaffVO staffVO;
	@Setter
	@Getter
	private VacationVO vacationVO;
	@Setter
	@Getter
	private WorkReportVO workReportVO;
	@Setter
	@Getter
	private AssignmentVO assignmentVO;
	@Setter
	@Getter
	private AttendanceVO attendanceVO;
	@Setter
	@Getter
	private WorkReportDetailVO workReportDetailVO;
	@Setter
	@Getter
	private ResignationVO resignationVO;
	@Setter
	@Getter
	private FormalVO formalVO;
	@Setter
	@Getter
	private ReimbursementVO reimbursementVO;
	@Getter
	private BankAccountVO bankAccountVO;
	@Setter
	@Getter
	private NoticeVO noticeVO;
	@Setter
	@Getter
	private StaffAuditVO staffAuditVO;
	@Setter
	@Getter
	private Integer type;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	private Integer totalCount = 0;
	@Setter
	private Integer result; // 任务处理结果
	@Getter
	private List<DepartmentVO> departmentVOs;
	@Getter
	private List<StaffVO> staffVOs;
	@Getter
	private List<CompanyVO> companyVOs;
	@Getter
	private List<PositionVO> positionVOs;
	@Getter
	@Setter
	private ChopBorrrowVo chopBorrrowVo;
	@Getter
	@Setter
	private CertificateBorrowVo certificateBorrrowVo;
	@Getter
	@Setter
	private ContractBorrowVo contractBorrowVo;
	@Getter
	@Setter
	private IdBorrowVo idBorrowVo;
	@Getter
	@Setter
	private ContractDetailVo contractDetailVo;
	@Getter
	@Setter
	private ContractSignVo contractSignVo;
	@Getter
	@Setter
	private ChangeContractVo changeContractVo;
	@Setter
	@Getter
	private ChangeBankAccountVo changeBankAccountVo;
	@Setter
	@Getter
	private ChopDestroyVo chopDestroyVo;
	@Setter
	@Getter
	private PurchasePropertyVo purchasePropertyVo;
	@Setter
	@Getter
	private CarveChopVo carveChopVo;
	@Setter
	@Getter
	private HandlePropertyVo handlePropertyVo;
	@Setter
	@Getter
	private TransferPropertyVo transferPropertyVo;
	@Setter
	@Getter
	private ShopApplyVo shopApplyVo;
	@Setter
	@Getter
	private ViewWorkReportVo viewWorkReport;
	@Setter
	@Getter
	private PublicRelationEventVo publicRelationEvent;
	@Getter
	private double dailyHours;
	@Getter
	private String beginTime;
	@Getter
	private String endTime;
	@Getter
	private String amEndTime;
	@Getter
	private String pmBeginTime;
	@Getter
	private String beginDay;
	@Getter
	private String workOverBeginTime;
	@Autowired
	private StaffService staffService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private AssignmentService assignmentService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private ResignationService resignationService;
	@Autowired
	private WorkReportService workReportService;
	@Autowired
	private FormalService formalService;
	@Autowired
	private ReimbursementService reimbursementService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeActorService noticeActorService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private SocialSecurityService socialSecurityService;
	@Autowired
	private ChopService chopService;
	@Setter
	private File[] attachment;
	@Setter
	private String[] attachmentFileName;
	@Setter
	private File[] attachment2;
	@Setter
	private String[] attachment2FileName;
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
	private MorningMeetingReportService morningMeetingReportService;
	@Autowired
	private PublicService publicService;
	@Autowired
	private ProjectService projectService;
	@Autowired
	private BrandAuthService brandAuthService;
	@Autowired
	private PerformanceService performanceService;
	@Setter
	@Getter
	private SpreadShopVo spreadShopVo;
	@Setter
	@Getter
	private SpreadShopApplyEntity spreadShopApply;
	@Setter
	@Getter
	private ShopPayPluginEntity shopPayPlugin;
	@Setter
	@Getter
	private ShopOtherPayEntity otherPay;
	@Setter
	@Getter
	private ShopPayApplyVo shopPayApplyVo;
	@Setter
	@Getter
	private WorkOvertimeVo workOvertimeVo;
	@Autowired
	private WorkOvertimeService workOvertimeService;
	@Autowired
	private WeekWorkReportService weekWorkReportService;
	@Setter
	@Getter
	private ThisWeekWorkVo thisWeekWorkVo;
	@Setter
	@Getter
	private RiskVo riskVo;
	@Setter
	@Getter
	private NextWeekWorkPlan nextWeekWorkPlan;
	@Setter
	private String weekWorkSummary;
	@Setter
	private WeekReportEntity weekReport;
	@Autowired
	private SoftPerformanceService softPerformanceService;
	@Setter
	@Getter
	private MorningMeetingVo morningMeetingVo;
	@Autowired
	private VersionInfoService versionInfoService;
	@Autowired
	private SigninService signinService;
	@Autowired
	private StaffSalaryService staffSalaryService;
	
	public String error() {
		try {
			errorMessage = URLDecoder.decode(errorMessage, "utf-8");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return "error";
	}

	public String updateInformation() {
		try {
			StaffVO staffVO = getPersonalInformation();
			request.setAttribute("staffVO", staffVO);
		} catch (Exception e) {
			e.printStackTrace(); 
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("更新员工信息失败："+sw.toString());
			return "error";
		}

		selectedPanel = "updateInformation";
		return "updateInformation";
	}

	public String showInformation() {
		try {
			if (type == 1 || type == 3) {
				StaffVO staffVO = getPersonalInformation();
				request.setAttribute("staffVO", staffVO);
			} else if (type == 2) {
				// 人物卡片
				StaffVO staff = getPersonalInformation();
				StaffVO staffVO1 = staffService.getStaffByStaffID(staff.getStaffID());
				request.setAttribute("staffVO1", staffVO1);
			}

		} catch (Exception e) {
			e.printStackTrace(); 
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("显示员工信息失败："+sw.toString());
			return "error";
		}

		selectedPanel = "showInformation";
		return "myInformation";
	}

	public String getUserPicture(){
		try {
			byte[] picArray = identityService.getUserPicture(request.getParameter("userId")).getBytes();
			if (picArray == null) {
				picArray = new byte[10];
			}

			inputStream = new ByteArrayInputStream(picArray);
		} catch (Exception e) {
			inputStream = new ByteArrayInputStream(new byte[10]);
		}
		return "imgStream";
	}

	public String getPicture() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		byte[] picArray = identityService.getUserPicture(user.getId()).getBytes();
		if (picArray == null) {
			picArray = new byte[10];
		}

		inputStream = new ByteArrayInputStream(picArray);
		return "imgStream";
	}

	public String saveInformation() {
		try {
			staffService.updateStaff(staffVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "修改失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("保存员工信息失败："+sw.toString());
		}
		return "saveInformation";
	}

	public String findGroupDetails() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		List<GroupDetailVO> groupDetailVOs = staffService.findGroupDetailsByUserID(user.getId());
		request.setAttribute("groupDetailVOs", groupDetailVOs);
		selectedPanel = "findGroupDetails";
		return "groupDetailList";
	}

	private StaffVO getPersonalInformation() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			throw new RuntimeException("您尚未登录，请先登录！");
		}

		StaffVO staffVO = staffService.getStaffByUserID(user.getId());
		staffVO.setUserID(user.getId());
		staffVO.setStaffNumber(user.getFirstName());
		try {
			staffVO.setAnnualVacationInfo(vacationService.getStaffAnnualVacationInfo(user.getId()));
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH)+1;
			UserMonthlyRestEntity monthlyRest = attendanceService.getMonthlyRest(user.getId(), year, month);
			if(null != monthlyRest){
					staffVO.setMonthlyRestDays(monthlyRest.getRestDays());
					staffVO.setMonthlyWorkDays(monthlyRest.getWorkDays());
			}
			//获取员工薪资明细
			StaffSalaryEntity staffSalary = staffSalaryService.getStaffSalary(user.getId());
			request.setAttribute("staffSalary", staffSalary);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staffVO;
	}

	public String startVacation() {

		try {
			vacationService.startVacation(vacationVO, attachment, attachmentFileName);
		} catch (Exception e) {
			errorMessage = "请假申请提交失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("请假申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.VACATION.getValue();
		return "startVacation";
	}

	public void getVacationHistoryDetail() {
		String processInstanceID = vacationService.getInstanceIdByVacationId(request.getParameter("vacationId"));
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		printByJson(finishedTaskVOs);
	}

	public String startAssignment() {
		try {
			assignmentService.startAssignment(assignmentVO);
		} catch (Exception e) {
			errorMessage = "任务分配提交失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("任务分配提交失败："+sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.ASSIGNMENT.getValue();
		return "startAssignment";
	}

	public String startChopBorrow() {
		try {
			String processInstanceId = request.getParameter("processInstanceId");
			chopService.startChopBorrow(chopBorrrowVo, processInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "公章申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("公章申请提交失败："+sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CHOP_BORROW.getValue();
		return "startAssignment_";
	}

	public String startCertificateBorrow() {
		try {
			certificateService.startCertificateBorrow(certificateBorrrowVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "证件申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("证件申请提交失败："+sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CERTIFICATE_BORROW.getValue();
		return "startAssignment_";
	}
	public String startContractBorrow() {
		try {
			contractService.startContractBorrow(contractBorrowVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "合同借阅申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("合同借阅申请提交失败："+sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CONTRACT_BORROW.getValue();
		return "startAssignment_";
	}
	public String startIdBorrow() {
		try {
			chopService.startIdBorrow(idBorrowVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "身份证借用提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("身份证借用提交失败："+sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CHOP_BORROW.getValue();
		return "startAssignment_";
	}

	@Setter
	@Getter
	private File file;
	@Setter
	@Getter
	private String fileName;

	public String startContract() {
		try {
			chopService.startContract(contractDetailVo, file, fileName);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "合同签署提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("合同签署提交失败："+sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CONTRACT.getValue();
		return "startAssignment_";
	}



	public String startContractSign(){
		try {
			contractService.startContractSign(contractSignVo, attachment, attachmentFileName, attachment2, attachment2FileName);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "合同签署提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("合同签署提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.CONTRACT.getValue();
		return "startAssignment_";
	}
	public String startChangeContract(){
		try {
			contractService.startChangeContract(changeContractVo, attachment, attachmentFileName);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "合同变更或解除申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("合同变更或解除申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.CONTRACT_CHANGE.getValue();
		return "startAssignment_";
	}
	public String startBankAccount(){
		try {
			bankAccountService.startBankAccount(changeBankAccountVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "银行账户申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("银行账户申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.BANK_ACCOUNT_CHANGE.getValue();
		return "startAssignment_";
	}
	public String startChopDestroy(){
		try {
			chopService.startChopDestroy(chopDestroyVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "印章缴销申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("印章缴销申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.CHOP_DESTROY.getValue();
		return "startAssignment_";
	}
	public String startPurchaseProperty(){
		try {
			purchasePropertyService.startPurchaseProperty(purchasePropertyVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "财产购置申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("财产购置申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.PURCHASE_PROPERTY.getValue();
		return "startAssignment_";
	}
	public String startCarveChop(){
		try {
			chopService.startCarveChop(carveChopVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "印章刻制申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("印章刻制申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.CARVE_CHOP.getValue();
		return "startAssignment_";
	}
	public String startHandleProperty(){
		try {
			String recipientId = request.getParameter("recipientId");
			handlePropertyService.startHandleProperty(handlePropertyVo, recipientId);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "资产处置申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("资产处置申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.HANDLE_PROPERTY.getValue();
		return "startAssignment_";
	}
	public String startTransferProperty(){
		try {
			String recipientId = request.getParameter("recipientId");
			transferPropertyService.startTransferProperty(transferPropertyVo, recipientId);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "资产调拨申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("资产调拨申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.TRANSFER_PROPERTY.getValue();
		return "startAssignment_";
	}
	public String startShopApply(){
		//申请类型
		String applyType = shopApplyVo.getApplyType();
		try {
			shopApplyService.startShopApply(shopApplyVo, attachment, attachmentFileName);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = applyType+"申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(applyType+"申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.SHOP_APPLY.getValue();
		return "startAssignment_";
	}
	/*	public String startShopPayApply(){
		//申请类型
		String applyType = shopPayApplyVo.getApplyType();
		try {
			shopApplyService.startShopPayApply(shopPayApplyVo, spreadShopVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = applyType+"申请提交失败：" + e.getMessage();
			return "error";
		}
		type = BusinessTypeEnum.SHOP_PAY_APPLY.getValue();
		return "startAssignment_";
	}*/
	public String startShopPayApply(){
		//申请类型
		String applyType = request.getParameter("applyType");
		try {
			if(Constants.PAY_SPREAD.equals(applyType)){
				spreadShopApply.setApplyType(applyType);
				//付费推广充值
				shopApplyService.startShopSpreadPayApply(spreadShopApply, spreadShopVo);
			}else if(Constants.PAY_PLUG_IN.equals(applyType)){
				shopPayPlugin.setApplyType(applyType);
				//付费服务/插件开通
				shopApplyService.startShopPayPlugInApply(shopPayPlugin);
			}else{
				otherPay.setApplyType(applyType);
				//其它费用申请
				shopApplyService.startShopOtherPayApply(otherPay);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = applyType+"申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(applyType+"申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.SHOP_PAY_APPLY.getValue();
		return "startAssignment_";
	}
	public String startWorkOvertime(){
		try {
			workOvertimeService.startWorkOvertime(workOvertimeVo);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage ="加班申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("加班申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.WORK_OVERTIME.getValue();
		return "startAssignment";
	}
	public String startViewReportApply(){
		try {
			viewReportService.startViewReportApply(viewWorkReport);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查看日报申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("查看日报申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.VIEW_REPORT.getValue();
		return "startAssignment_";
	}
	public String startApplyPublicEvent(){
		try {
			publicService.startApplyPublicEvent(publicRelationEvent);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "公关申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("公关申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.PUBLIC_EVENT.getValue();
		return "startAssignment_";
	}
	public void changeBaseAttr() throws Exception{
		String processInstacneId=request.getParameter("instanceId");
		String key=request.getParameter("key");
		String value=request.getParameter("value");
		BaseVO  object =  (BaseVO) runtimeService.getVariable(processInstacneId, "arg");
		java.lang.reflect.Field field=BaseVO.class.getDeclaredField(key);
		field.setAccessible(true);
		field.set(object, value);
		runtimeService.setVariable(processInstacneId,"arg",object);
	}
	public void changeAttr() throws Exception{
		String processInstacneId=request.getParameter("instanceId");
		String key=request.getParameter("key");
		String value=request.getParameter("value");
		Object  object =   runtimeService.getVariable(processInstacneId, "arg");
		java.lang.reflect.Field field=BaseVO.class.getDeclaredField(key);
		field.setAccessible(true);
		field.set(object, value);
		runtimeService.setVariable(processInstacneId,"arg",object);
	}

	public String startResignation() {
		try {
			resignationService.startResignation(resignationVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "离职申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("离职申请提交失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.RESIGNATION.getValue();
		return "startResignation";
	}
	public String startWeekMorningMeetingReport(){
		try {
			morningMeetingReportService.startWeekMorningMeetingReport(morningMeetingVo, attachment, attachmentFileName);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "早会汇报失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error("早会汇报失败："+sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.MORNING_MEETING.getValue();
		return "startAssignment";
	}
	public String findTaskList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		String startTime = request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		String No_code = request.getParameter("No_code");
		String applyerId = request.getParameter("applyerId");
		String applyerName = request.getParameter("applyerName");
		int count = 0;
		try {
			switch (BusinessTypeEnum.valueOf(type)) {
			case VACATION:
				List<Task> vacationTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.VACATION).listPage((page - 1) * limit, limit);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.VACATION).count();
				List<VacationTaskVO> vacationTaskVOs = vacationService.createTaskVOListByTaskList(vacationTasks);
				request.setAttribute("taskVOs", vacationTaskVOs);
				break;
			case ASSIGNMENT:
				List<Task> assignmentTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.ASSIGNMENT).listPage((page - 1) * limit, limit);
				List<TaskVO> assignmentTaskVOs = processService.createTaskVOList(assignmentTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.ASSIGNMENT).count();
				request.setAttribute("taskVOs", assignmentTaskVOs);
				break;
			case RESIGNATION:
				List<Task> resignationTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.RESIGNATION).listPage((page - 1) * limit, limit);
				List<TaskVO> resignationTaskVOs = resignationService.createTaskVOListByTaskList(resignationTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.RESIGNATION).count();
				request.setAttribute("taskVOs", resignationTaskVOs);
				break;
			case FORMAL:
				List<Task> formalTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.FORMAL).listPage((page - 1) * limit, limit);
				List<TaskVO> formalTaskVOs = formalService.createTaskVOListByTaskList(formalTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.FORMAL).count();
				request.setAttribute("taskVOs", formalTaskVOs);
				break;
			case REIMBURSEMENT:
				request.setAttribute("startTime", startTime);
				request.setAttribute("endTime", endTime);
				request.setAttribute("No_code", No_code);
				request.setAttribute("applyerId", applyerId);
				request.setAttribute("applyerName", applyerName);

				List<String> ids = chopService.getTaskIds("OA_Reimbursement", user.getId(), null, No_code, startTime,
						endTime, page, limit, applyerId);
				List<Task> resignationTask_ = new ArrayList<Task>();
				if (ids != null && ids.size() > 0) {
					for (String string : ids) {
						Task resignationTask = taskService.createTaskQuery().taskId(string).singleResult();
						resignationTask_.add(resignationTask);
					}
				}
				List<ReimbursementTaskVO> reimbursementTaskVOs = reimbursementService
						.createTaskVOListByTaskList(resignationTask_);
				count = chopService.getTaskCount("OA_Reimbursement", user.getId(), null, No_code, startTime, endTime, applyerId);
				request.setAttribute("taskVOs", reimbursementTaskVOs);
				break;
			case EMAIL:
				List<Task> emailTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.EMAIL).listPage((page - 1) * limit, limit);
				List<TaskVO> emailTaskVOs = processService.createTaskVOList(emailTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.EMAIL).count();
				request.setAttribute("taskVOs", emailTaskVOs);
				break;
			case AUDIT:
				Task auditTask = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.AUDIT).singleResult();
				if (auditTask != null) {
					ProcessInstance processInstance = processService.getProcessInstance(auditTask.getId());
					StaffAuditVO staffAuditVO = (StaffAuditVO) runtimeService.getVariable(processInstance.getId(),
							"arg");
					request.setAttribute("staffAuditVO", staffAuditVO);
					request.setAttribute("taskID", auditTask.getId());
				}
				break;
			case SOCIAL_SECURITY:
				List<Task> ssTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SOCIAL_SECURITY).listPage((page - 1) * limit, limit);
				List<TaskVO> ssTaskVOs = processService.createTaskVOList(ssTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.SOCIAL_SECURITY).count();
				request.setAttribute("taskVOs", ssTaskVOs);
				break;
			case CHOP_BORROW:
				List<Task> chopTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CHOP_BORROW).listPage((page - 1) * limit, limit);
				List<TaskVO> chopTasksVos = processService.createTaskVOList(chopTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CHOP_BORROW).count();
				request.setAttribute("chopTasksVos", chopTasksVos);
				break;
			case CERTIFICATE_BORROW:
				List<Task> certificateTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CERTIFICATE_BORROW).listPage((page - 1) * limit, limit);
				List<TaskVO> certificateTasksVos = processService.createTaskVOList(certificateTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CERTIFICATE_BORROW).count();
				request.setAttribute("certificateTasksVos", certificateTasksVos);
				break;
			case CONTRACT_BORROW:
				List<Task> contractBorrowTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CONTRACT_BORROW).listPage((page - 1) * limit, limit);
				List<TaskVO> contractTasksVos = processService.createTaskVOList(contractBorrowTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CONTRACT_BORROW).count();
				request.setAttribute("contractTasksVos", contractTasksVos);
				break;
			case ID_BORROW:
				List<Task> idBorrowTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.ID_BORROW).listPage((page - 1) * limit, limit);
				List<TaskVO> idBorrowTasksVos = processService.createTaskVOList(idBorrowTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.ID_BORROW).count();
				request.setAttribute("idBorrowTasksVos", idBorrowTasksVos);
				break;
			case CONTRACT:
				List<Task> contractTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CONTRACT_SIGN).listPage((page - 1) * limit, limit);
				List<TaskVO> contractTaskVos = processService.createTaskVOList(contractTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CONTRACT_SIGN).count();
				request.setAttribute("contractTaskVos", contractTaskVos);
				break;
			case CAR_USE:
				List<Task> carUseTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CAR_USE).listPage((page - 1) * limit, limit);
				List<TaskVO> carUseTaskVos = processService.createTaskVOList(carUseTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CAR_USE).count();
				request.setAttribute("carUseTaskVos", carUseTaskVos);
				break;
			case ADVANCE:
				request.setAttribute("startTime", startTime);
				request.setAttribute("endTime", endTime);
				request.setAttribute("No_code", No_code);
				String applyerNameForAdvance = request.getParameter("applyerNameForAdvance");
				String applyerIdForAdvance = request.getParameter("applyerIdForAdvance");
				request.setAttribute("applyerIdForAdvance", applyerIdForAdvance);
				request.setAttribute("applyerNameForAdvance", applyerNameForAdvance);

				List<String> ids_ = chopService.getTaskIds("OA_Advance", user.getId(), null, No_code, startTime,
						endTime, page, limit, applyerIdForAdvance);
				List<Task> resignationTask__ = new ArrayList<Task>();

				if (ids_ != null && ids_.size() > 0) {
					for (String string : ids_) {
						Task resignationTask = taskService.createTaskQuery().taskId(string).singleResult();
						resignationTask__.add(resignationTask);
					}
				}
				List<AdvanceTaskVO> reimbursementTaskVOs_ = reimbursementService
						.createAdvanceTaskVOListByTaskList(resignationTask__);
				count = chopService.getTaskCount("OA_Advance", user.getId(), null, No_code, startTime, endTime, applyerId);
				request.setAttribute("taskVOs", reimbursementTaskVOs_);
				break;
			case VITAE:
				List<Task> vitaeTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.VITAE).listPage((page - 1) * limit, limit);
				List<TaskVO> vitaeTasksVos = processService.createTaskVOList(vitaeTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.VITAE).count();
				request.setAttribute("vitaeTasksVos", vitaeTasksVos);
				break;
			case SOFTPERFORMANCE:
				List<Task> softPerformacne = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SOFTPERFORMANCE).listPage((page - 1) * limit, limit);
				List<SoftPerformanceTaskVO> softPerformacneVos = processService
						.createSoftPerformanceTaskVoList(softPerformacne);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.SOFTPERFORMANCE).count();
				request.setAttribute("softPerformacneVos", softPerformacneVos);
				break;
			case TRAIN:
				List<Task> classHours = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CLASS_HOUR).listPage((page - 1) * limit, limit);
				List<CoursePlanTaskVo> coursePlanVos = processService
						.createCoursePlanTaskVoList(classHours);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CLASS_HOUR).count();
				request.setAttribute("coursePlanVos", coursePlanVos);
				if(null != request.getParameter("coursePlanId")){
					request.setAttribute("coursePlanId", request.getParameter("coursePlanId"));
				}
				break;
			case COMMONSUBJECT:
				List<Task> commonSubjectList = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.COMMONSUBJECT).listPage((page - 1) * limit, limit);
				List<CommonSubjectTaskVo> commonSubjectVos = processService
						.createCommonSubjectVoList(commonSubjectList);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.COMMONSUBJECT).count();
				request.setAttribute("commonSubjectVos", commonSubjectVos);
				break;
			case CONTRACT_CHANGE:
				List<Task> contractChangeTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CONTRACT_CHANGE).listPage((page - 1) * limit, limit);
				List<TaskVO> contractChangeTaskVos = processService.createTaskVOList(contractChangeTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CONTRACT_CHANGE).count();
				request.setAttribute("contractChangeTaskVos", contractChangeTaskVos);
				break;
			case BANK_ACCOUNT_CHANGE:
				List<Task> changeBankAccountTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.BANK_ACCOUNT).list();
				List<Task> changeBankAccountTasksAssigneeCandidate = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.BANK_ACCOUNT).list();
				changeBankAccountTasks.addAll(changeBankAccountTasksAssigneeCandidate);
				List<TaskVO> changeBankAccountTaskVos = processService.createTaskVOList(ActionUtil.page(page, limit, changeBankAccountTasks));
				long bankCount = taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.BANK_ACCOUNT).count();
				long _bankCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.BANK_ACCOUNT).count();
				count = (int)(bankCount+_bankCount);
				request.setAttribute("changeBankAccountTaskVos", changeBankAccountTaskVos);
				break;
			case CHOP_DESTROY:
				List<Task> chopDestroyTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CHOP_DESTROY).listPage((page - 1) * limit, limit);
				List<TaskVO> chopDestroyTaskVos = processService.createTaskVOList(chopDestroyTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CHOP_DESTROY).count();
				request.setAttribute("chopDestroyTaskVos", chopDestroyTaskVos);
				break;
			case PURCHASE_PROPERTY:
				List<Task> purchasePropertyTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PURCHASE_PROPERTY).list();
				//任务可能不是分配给指定人，是分配给候选人
				List<Task> purchasePropertyTasksAssigneeCandidate = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.PURCHASE_PROPERTY).list();
				purchasePropertyTasks.addAll(purchasePropertyTasksAssigneeCandidate);
				List<TaskVO> purchasePropertyTaskVos = processService.createTaskVOList(ActionUtil.page(page, limit, purchasePropertyTasks));
				long purchasePropertyCount = taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.PURCHASE_PROPERTY).count();
				//任务可能不是分配给指定人，是分配给候选人
				long _purchasePropertyCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.PURCHASE_PROPERTY).count();
				count = (int)(purchasePropertyCount + _purchasePropertyCount);
				request.setAttribute("purchasePropertyTaskVos", purchasePropertyTaskVos);
				break;
			case CARVE_CHOP:
				List<Task> carveChopTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CARVE_CHOP).listPage((page - 1) * limit, limit);
				List<TaskVO> carveChopTaskVos = processService.createTaskVOList(carveChopTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.CHOP_DESTROY).count();
				request.setAttribute("carveChopTaskVos", carveChopTaskVos);
				break;
			case HANDLE_PROPERTY:
				List<Task> handlePropertyTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.HANDLE_PROPERTY).listPage((page - 1) * limit, limit);
				List<TaskVO> handlePropertyTaskVos = processService.createTaskVOList(handlePropertyTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.HANDLE_PROPERTY).count();
				request.setAttribute("handlePropertyTaskVos", handlePropertyTaskVos);
				break;
			case TRANSFER_PROPERTY:
				List<Task> transferPropertyTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.TRANSFER_PROPERTY).listPage((page - 1) * limit, limit);
				List<TaskVO> transferPropertyTaskVos = processService.createTaskVOList(transferPropertyTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.TRANSFER_PROPERTY).count();
				request.setAttribute("transferPropertyTaskVos", transferPropertyTaskVos);
				break;
			case SHOP_APPLY:
				List<Task> shopApplyTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SHOP_APPLY).list();
				//任务可能不是分配给指定人，是分配给候选人
				List<Task> shopApplyTasksAssigneeCandidate = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.SHOP_APPLY).list();
				shopApplyTasks.addAll(shopApplyTasksAssigneeCandidate);
				List<ShopApplyTaskVo> shopApplyTaskVos = shopApplyService.getShopApplyTaskVOList(ActionUtil.page(page, limit, shopApplyTasks));
				long shopApplyCount = taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.SHOP_APPLY).count();
				long _shopApplyCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.SHOP_APPLY).count();
				count = (int)(shopApplyCount+_shopApplyCount);
				request.setAttribute("shopApplyTaskVos", shopApplyTaskVos);
				break;
			case SHOP_PAY_APPLY:
				List<Task> shopPayApplyTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SHOP_PAY_APPLY).list();
				//任务可能不是分配给指定人，是分配给候选人
				List<Task> shopPayApplyTasksAssigneeCandidate = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.SHOP_PAY_APPLY).list();
				shopPayApplyTasks.addAll(shopPayApplyTasksAssigneeCandidate);
				List<ShopApplyTaskVo> shopPayApplyTaskVos = shopApplyService.getShopPayApplyTaskVOList(ActionUtil.page(page, limit, shopPayApplyTasks));
				long shopPayApplyCount = taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.SHOP_PAY_APPLY).count();
				long _shopPayApplyCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.SHOP_PAY_APPLY).count();
				count = (int)(shopPayApplyCount+_shopPayApplyCount);
				request.setAttribute("shopPayApplyTaskVos", shopPayApplyTaskVos);
				break;
			case WORK_OVERTIME:
				List<Task> workOvertimeTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.WORK_OVERTIME).listPage((page - 1) * limit, limit);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.WORK_OVERTIME).count();
				List<WorkOvertimeTaskVo> workOvertimeTaskVos = workOvertimeService.createTaskVOListByTaskList(workOvertimeTasks);
				request.setAttribute("workOvertimeTaskVos", workOvertimeTaskVos);
				break;
			case PROBLEM_ORDER:
				List<Task> problemOrderTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PROBLEM_ORDER).listPage((page - 1) * limit, limit);
				List<ProblemOrderVo> problemOrderTaskVos = softPerformanceService.getProblemOrdersByInstanceId(problemOrderTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.PROBLEM_ORDER).count();
				request.setAttribute("problemOrderTasks", problemOrderTaskVos);
				break;
			case PAYMENT:
				request.setAttribute("startTime", startTime);
				request.setAttribute("endTime", endTime);
				request.setAttribute("No_code", No_code);
				String applyerNameForPay = request.getParameter("applyerNameForPay");
				String applyerIdForPay = request.getParameter("applyerIdForPay");
				request.setAttribute("applyerIdForPay", applyerIdForPay);
				request.setAttribute("applyerNameForPay", applyerNameForPay);

				List<String> taskIds = chopService.getTaskIds("OA_Payment", user.getId(), null, No_code, startTime,
						endTime, page, limit, applyerIdForPay);
				List<Task> paymentTask = new ArrayList<Task>();

				if (taskIds != null && taskIds.size() > 0) {
					for (String string : taskIds) {
						Task payTask = taskService.createTaskQuery().taskId(string).singleResult();
						paymentTask.add(payTask);
					}
				}
				List<PaymentTaskVO> paymentTaskVOs = reimbursementService
						.createPaymentTaskVOListByTaskList(paymentTask);
				count = chopService.getTaskCount("OA_Payment", user.getId(), null, No_code, startTime, endTime, applyerId);
				request.setAttribute("taskVOs", paymentTaskVOs);
				break;
			case MORNING_MEETING:
				List<Task> morningMeetTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.MORNING_MEETING).listPage((page - 1) * limit, limit);
				List<MorningMeetingVo> morningMeetingTaskVos = morningMeetingReportService.getMorningMeetingsByInstanceId(morningMeetTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.MORNING_MEETING).count();
				request.setAttribute("morningMeetingTaskVos", morningMeetingTaskVos);
				break;
			case PROJECT:
				List<Task> projectTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PRPJECT).listPage((page - 1) * limit, limit);
				List<ProjectInfoVo> projectInfoTaskVos = projectService.getProjectInfosByInstanceId(projectTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.PRPJECT).count();
				request.setAttribute("projectInfoTaskVos", projectInfoTaskVos);
				break;
			case BRAND_AUTH:
				List<Task> brandAuthTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.BRAND_AUTH).listPage((page - 1) * limit, limit);
				List<BrandAuthVo> brandAuthTaskVos = brandAuthService.getBrandAuthTasksByInstanceId(brandAuthTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.BRAND_AUTH).count();
				request.setAttribute("brandAuthTaskVos", brandAuthTaskVos);
				break;
			case PUBLIC_EVENT:
				List<Task> publicEventTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PUBLIC_EVENT).list();
				List<PublicRelationEventVo> publicEventTaskVos = publicService.getPublicEventTasksByInstanceId(publicEventTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.PUBLIC_EVENT).count();
				request.setAttribute("publicEventTaskVos", ActionUtil.page(page, limit, publicEventTaskVos));
				break;
/*			case SHOP_DAY_SALE_REPORT:
				List<ShopRelatedPersonEntity> saleReportTasks = shopSaleService.findSaleReportTasksByUserId(user.getId());
				break;*/
			case PERFORMANCE:
				List<Task> performanceTasks = taskService.createTaskQuery().taskAssignee(user.getId())
		                .processDefinitionKey(Constants.PERFORMANCE).list();
				List<Task> performanceTasksAssigneeCandidate = taskService.createTaskQuery().taskCandidateUser(user.getId())
				        .processDefinitionKey(Constants.PERFORMANCE).list();
				performanceTasks.addAll(performanceTasksAssigneeCandidate);
				List<PerformanceEntity> performanceTaskVos = performanceService.findPerformanceTaskVos(ActionUtil.page(page, limit, performanceTasks));
				long performanceCount = taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.PERFORMANCE).count();
				long _performanceCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.PERFORMANCE).count();
				count = (int)(performanceCount+_performanceCount);
				request.setAttribute("performanceTaskVos", performanceTaskVos);
				break;
			case PERFORMANCE_TARGET:
				List<PerformanceVo> performanceTargetValueTasks = performanceService.findPerformanceTasks(
						user.getId(), BusinessTypeEnum.PERFORMANCE_TARGET);
				count = performanceTargetValueTasks.size();
				request.setAttribute("performanceTargetValueTasks", ActionUtil.page(page, limit, performanceTargetValueTasks));
				break;
			case PERFORMANCE_ACTUAL:
				if(staffService.isPM(user.getId())){
					List<PerformanceVo> performanceActualValueTasks = performanceService.findPerformanceTasks(
							null, BusinessTypeEnum.PERFORMANCE_ACTUAL);
					count = performanceActualValueTasks.size();
					request.setAttribute("performanceActualValueTasks", ActionUtil.page(page, limit, performanceActualValueTasks));
				}
				break;
			case PERSONAL_PERFORMANCE:
				List<Task> personalPerformanceTasks = taskService.createTaskQuery().taskCandidateUser(user.getId())
		                .processDefinitionKey(Constants.PERSONAL_PERFORMANCE).listPage((page - 1) * limit, limit);
				List<PerformanceEntity> personalPerformanceTaskVos = performanceService.findPersonalPerformanceTaskVos(personalPerformanceTasks);
				count = (int)taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processDefinitionKey(Constants.PERSONAL_PERFORMANCE).count();
				request.setAttribute("performanceTaskVos", personalPerformanceTaskVos);
				break;
			case ALTER_SALARY:
				List<Task> alterSalaryTasks = taskService.createTaskQuery().processVariableValueEquals("processType",
						Constants.ALTER_SALARY).taskAssignee(user.getId()).listPage((page - 1) * limit, limit);
				List<AlterStaffSalaryEntity> alterSalaryTaskVos = staffSalaryService.findAlterSalaryTaskVos(alterSalaryTasks);
				count = (int)taskService.createTaskQuery().processVariableValueEquals("processType",
						Constants.ALTER_SALARY).taskAssignee(user.getId()).count();
				request.setAttribute("alterSalaryTaskVos", alterSalaryTaskVos);
				break;
			case CHANGE_SALARY_DETAIL:
				List<Task> changeSalaryDetailTasks = taskService.createTaskQuery().processVariableValueEquals("processType",
						Constants.CHANGE_SALARY_DETAIL).taskAssignee(user.getId()).listPage((page - 1) * limit, limit);
				List<ChangeSalaryDetailVo> changeSalaryDetailTaskVos = staffSalaryService.findChangeSalaryDetailTaskVos(changeSalaryDetailTasks);
				count = (int)taskService.createTaskQuery().processVariableValueEquals("processType",
						Constants.CHANGE_SALARY_DETAIL).taskAssignee(user.getId()).count();
				request.setAttribute("changeSalaryDetailTaskVos", changeSalaryDetailTaskVos);
				break;
			case POST_CREDENTIAL:
				List<Task> credentialAuditTasks = taskService.createTaskQuery()
						.taskAssignee(user.getId())
						.processDefinitionKey(Constants.POST_CREDENTIAL)
						.listPage((page-1)*limit, limit);
				List<CredentialEntity> credentialTaskVos = processService
						.findCredentialVOTasks(credentialAuditTasks);
				count = (int) taskService.createTaskQuery().taskAssignee(user.getId())
						.processDefinitionKey(Constants.POST_CREDENTIAL).count();
				request.setAttribute("credentialTaskVos", credentialTaskVos);
				break;
			case EXIT_PARTNER:
				/*List<Task> exitPartnerTasks = taskService.createTaskQuery()
						.taskAssignee(user.getId())
						.processDefinitionKey("easyProcess")
						.listPage((page-1)*limit, limit);*/
				List<Task> exitPartnerTasks = taskService.createTaskQuery()
						.processVariableValueEquals("processType",Constants.EXIT_PARTNER)
						.taskAssignee(user.getId())
						.listPage((page-1)*limit, limit);
				List<PartnerEntity> exitPartnerTaskVOs = partnerService
						.findExitPartnerTaskVOs(exitPartnerTasks);
				/*count = (int) taskService.createTaskQuery()
						.taskAssignee(user.getId())
						.processDefinitionKey("easyProcess")
						.count();*/
				count = (int) taskService.createTaskQuery()
						.processVariableValueEquals("processType",Constants.EXIT_PARTNER)
						.taskAssignee(user.getId())
						.count();
				request.setAttribute("exitPartnerTaskVOs", exitPartnerTaskVOs);
				break;
			case PAY_SALARY:
				List<Task> paySalaryTasks = taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processVariableValueEquals("processType",Constants.PAY_SALARY).listPage((page - 1) * limit, limit);
				List<PaySalaryTaskVo> paySalaryTaskVos = staffSalaryService.findPaySalaryTaskVos(paySalaryTasks);
				count = (int)taskService.createTaskQuery().taskCandidateUser(user.getId())
						.processVariableValueEquals("processType",Constants.PAY_SALARY).count();
				request.setAttribute("paySalaryTaskVos", paySalaryTaskVos);
				break;
			case REWARD_PUNISHMENT:
				List<Task> rewardAndPunishmentTasks = taskService.createTaskQuery().taskAssignee(user.getId())
				.processVariableValueEquals("processType",Constants.REWARD_PUNISHMENT).listPage((page - 1) * limit, limit);
				List<RewardAndPunishmentVo> rewardAndPunishmentTaskVos = staffSalaryService.findRewardAndPunishmentTaskVos(rewardAndPunishmentTasks);
				count = (int)taskService.createTaskQuery().taskAssignee(user.getId())
						.processVariableValueEquals("processType",Constants.REWARD_PUNISHMENT).count();
				request.setAttribute("rewardAndPunishmentTaskVos", rewardAndPunishmentTaskVos);
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
		totalCount = count;
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		request.setAttribute("vacationCount", (int) taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.VACATION).count());
		request.setAttribute("assignmentCount", (int) taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.ASSIGNMENT).count());
		request.setAttribute("resignationCount", (int) taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.RESIGNATION).count());
		request.setAttribute("formalCount", (int) taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.FORMAL).count());
		request.setAttribute("reimbursementCount", (int) taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.REIMBURSEMENT).count());
		request.setAttribute("emailCount", (int) taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.EMAIL).count());
		request.setAttribute("auditCount", (int) taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.AUDIT).count());
		request.setAttribute("socialSecurityCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SOCIAL_SECURITY).count());
		request.setAttribute("chopBorrowCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CHOP_BORROW).count());
		request.setAttribute("certificateBorrowCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CERTIFICATE_BORROW).count());
		request.setAttribute("contractBorrowCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CONTRACT_BORROW).count());
		request.setAttribute("idBorrowCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.ID_BORROW).count());
		request.setAttribute("contractCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CONTRACT_SIGN).count());
		request.setAttribute("carUseCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CAR_USE).count());
		request.setAttribute("advanceCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.ADVANCE).count());
		request.setAttribute("paymentCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PAYMENT).count());
		request.setAttribute("vitaeCount",
				taskService.createTaskQuery().taskAssignee(user.getId()).processDefinitionKey(Constants.VITAE).count());
		request.setAttribute("softPerformanceCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SOFTPERFORMANCE).count());
		request.setAttribute("commonSubjectCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.COMMONSUBJECT).count());
		request.setAttribute("changeContractCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CONTRACT_CHANGE).count());
		long bankCount = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.BANK_ACCOUNT).count();
		long _bankCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
				.processDefinitionKey(Constants.BANK_ACCOUNT).count();
		request.setAttribute("changeBankAccountCount", bankCount+_bankCount);
		request.setAttribute("chopDestroyCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CHOP_DESTROY).count());
		long purchasePropertyCount = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PURCHASE_PROPERTY).count();
		//任务可能不是分配给指定人，是分配给候选人
		long _purchasePropertyCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
				.processDefinitionKey(Constants.PURCHASE_PROPERTY).count();
		request.setAttribute("purchasePropertyCount", (purchasePropertyCount+_purchasePropertyCount));
		request.setAttribute("carveChopCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CARVE_CHOP).count());
		request.setAttribute("handlePropertyCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.HANDLE_PROPERTY).count());
		request.setAttribute("transferPropertyCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.TRANSFER_PROPERTY).count());

		long shopApplyCount = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SHOP_APPLY).count();
		long _shopApplyCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
				.processDefinitionKey(Constants.SHOP_APPLY).count();
		request.setAttribute("shopApplyCount", (shopApplyCount+_shopApplyCount));

		long shopPayApplyCount = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.SHOP_PAY_APPLY).count();
		long _shopPayApplyCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
				.processDefinitionKey(Constants.SHOP_PAY_APPLY).count();
		request.setAttribute("shopPayApplyCount", (shopPayApplyCount+_shopPayApplyCount));
		request.setAttribute("workOvertimeCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.WORK_OVERTIME).count());
		request.setAttribute("trainCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CLASS_HOUR).count());
		request.setAttribute("problemOrderCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PROBLEM_ORDER).count());
		request.setAttribute("morningMeetingCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.MORNING_MEETING).count());
		request.setAttribute("projectCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PRPJECT).count());
		request.setAttribute("brandAuthCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.BRAND_AUTH).count());
		request.setAttribute("publicEventCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PUBLIC_EVENT).count());
		long performanceCount = taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.PERFORMANCE).count();
		long _performanceCount = taskService.createTaskQuery().taskCandidateUser(user.getId())
				.processDefinitionKey(Constants.PERFORMANCE).count();
		request.setAttribute("performanceCount", performanceCount+_performanceCount);
		int performanceTargetTaskCount = performanceService.getPerformanceTaskCount(user.getId(),
				BusinessTypeEnum.PERFORMANCE_TARGET);
		request.setAttribute("performanceTargetTaskCount", performanceTargetTaskCount);
		if(staffService.isPM(user.getId())){
			int performanceActualTaskCount = performanceService.getPerformanceTaskCount(null,
					BusinessTypeEnum.PERFORMANCE_ACTUAL);
			request.setAttribute("performanceActualTaskCount", performanceActualTaskCount);
		}
		request.setAttribute("personalPerformanceCount", taskService.createTaskQuery().taskCandidateUser(user.getId())
				.processDefinitionKey(Constants.PERSONAL_PERFORMANCE).count());
		request.setAttribute("alterSalaryCount",taskService.createTaskQuery().processVariableValueEquals("processType",
				Constants.ALTER_SALARY).taskAssignee(user.getId()).count());
		request.setAttribute("changeSalaryDetailCount",taskService.createTaskQuery().processVariableValueEquals("processType",
		Constants.CHANGE_SALARY_DETAIL).taskAssignee(user.getId()).count());
		request.setAttribute("credentialApplyCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.POST_CREDENTIAL).count());
		request.setAttribute("exitPartnerCount", taskService.createTaskQuery().processVariableValueEquals("processType",Constants.EXIT_PARTNER)
				.taskAssignee(user.getId()).count());
		request.setAttribute("carMaintainApplyCount", taskService.createTaskQuery().taskAssignee(user.getId())
				.processDefinitionKey(Constants.CAR_MAINTAIN_APPLY).count());
		request.setAttribute("paySalaryCount",taskService.createTaskQuery().processVariableValueEquals("processType",
				Constants.PAY_SALARY).taskCandidateUser(user.getId()).count());
		request.setAttribute("rewardAndPunishmentCount",taskService.createTaskQuery().processVariableValueEquals("processType",
				Constants.REWARD_PUNISHMENT).taskAssignee(user.getId()).count());
		selectedPanel = "findTaskList";
		return "taskList";
	}

	public String perform() {
		String taskID = request.getParameter("taskID");
		if(StringUtils.isBlank(taskID)){
			return "perform";
		}
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		if(BusinessTypeEnum.SHOP_APPLY.getName().equals(processDefinition.getName())){
			ShopApplyVo shopApplyVo = shopApplyService.getShopApplyVo(taskID);
			request.setAttribute("shopApplyVo", shopApplyVo);
		}else if(BusinessTypeEnum.SHOP_PAY_APPLY.getName().equals(processDefinition.getName())){
			ShopPayApplyVo shopPayApplyVo = shopApplyService.getShopPayApplyVo(taskID);
			request.setAttribute("shopPayApplyVo", shopPayApplyVo);
			Map<String, String> resultMap = new HashMap<>();
			List<ShopPayApplyListVo> shopPayApplyListVos = shopApplyService.getShopPayApplyListVos(shopPayApplyVo, resultMap);
			request.setAttribute("shopPayApplyListVos", shopPayApplyListVos);
			request.setAttribute("resultMap", resultMap);
		}else{
			List<FormField> formFields = processService.getFormFields(taskID);
			request.setAttribute("formFields", formFields);
		}
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		if (TaskDefKeyEnum.EMAIL_CONFIRM.getName().equals(task.getTaskDefinitionKey())) {
			EmailVO emailVO = emailService.getEmailVOByProcessInstanceID(processInstance.getId());
			request.setAttribute("emailVO", emailVO);
		}
		if (BusinessTypeEnum.CHOP_BORROW.getName().equals(processDefinition.getName())) {
			ChopBorrrowVo chopBorrowVo = chopService.getChopByInstanceId(processInstance.getId());
			Integer chopBorrow_Id = chopBorrowVo.getChopBorrow_Id();
			Date addTime = chopBorrowVo.getAddTime();
			String year = new SimpleDateFormat("yyyy").format(addTime);
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(chopBorrowVo.getUser_Id());
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("chopBorrow_Id", chopBorrow_Id);
			request.setAttribute("year", year);
			request.setAttribute("department", department);
		}
		else if (BusinessTypeEnum.CONTRACT.getName().equals(processDefinition.getName())) {
			String userId = contractService.getUserIdByInstanceId(processInstance.getId());
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
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
		}
		else if (BusinessTypeEnum.CONTRACT_CHANGE.getName().equals(processDefinition.getName())) {
			String userId = contractService.getChangeContractUserIdByInstanceId(processInstance.getId());
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
		}
		else if (BusinessTypeEnum.BANK_ACCOUNT_CHANGE.getName().equals(processDefinition.getName())) {
			String userId = bankAccountService.getUserIdByInstanceId(processInstance.getId());
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
		}
		else if (BusinessTypeEnum.CHOP_DESTROY.getName().equals(processDefinition.getName())) {
			String userId = chopService.getChopDestroyUserIdByInstanceId(processInstance.getId());
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
		}
		else if (BusinessTypeEnum.PURCHASE_PROPERTY.getName().equals(processDefinition.getName())) {
			PurchasePropertyEntity purchaseProperty = purchasePropertyService.getPurchasePropertyUserIdByInstanceId(processInstance.getId());
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(purchaseProperty.getUserID());
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
			request.setAttribute("id", purchaseProperty.getId());
		}
		else if (BusinessTypeEnum.CARVE_CHOP.getName().equals(processDefinition.getName())) {
			String userId = chopService.getCarveChopUserIdByInstanceId(processInstance.getId());
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
		}
		else if (BusinessTypeEnum.SHOP_APPLY.getName().equals(processDefinition.getName())) {
			String userId = shopApplyService.getShopApplyUserIdByInstanceId(processInstance.getId());
			String department = "";
			List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
			if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
				GroupDetailVO group = groupDetails.get(0);
				department += group.getCompanyName() + "-" + group.getDepartmentName();
			}
			request.setAttribute("department", department);
		}
		else if (BusinessTypeEnum.SHOP_PAY_APPLY.getName().equals(processDefinition.getName())) {
			String userIdStr = shopApplyService.getShopPayApplyUserIdByInstanceId(processInstance.getId());
			//都是一个部门的人，任选一个
			String userId = userIdStr.split(",")[0];
			if(StringUtils.isNotBlank(userId)){
				String department = "";
				List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(userId);
				if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
					GroupDetailVO group = groupDetails.get(0);
					department += group.getCompanyName() + "-" + group.getDepartmentName();
				}
				request.setAttribute("department", department);
			}
		}
		List<HistoricDetail> historicDetails = historyService.createHistoricDetailQuery().processInstanceId(processInstance.getId()).list();
		for (HistoricDetail historicDetail : historicDetails) {
			HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;

			if ("supervisor".equals(variable.getVariableName())) {

				String  supervisor = (String)variable.getValue();
				request.setAttribute("supervisor", supervisor);
			}
		}
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		selectedPanel = "findTaskList";
		return "perform";
	}

	public String tablePerform() {
		String taskID = request.getParameter("taskID");
		List<FormField> formFields = processService.getFormFields(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		if (TaskDefKeyEnum.EMAIL_CONFIRM.getName().equals(task.getTaskDefinitionKey())) {
			EmailVO emailVO = emailService.getEmailVOByProcessInstanceID(processInstance.getId());
			request.setAttribute("emailVO", emailVO);
		}
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		if (task.getTaskDefinitionKey().equals("soft_resultRecord")) {
			try {
				SoftPerformanceVo arg = (SoftPerformanceVo) runtimeService.getVariable(processInstance.getId(), "arg");
				request.setAttribute("taskPersonId", finishedTaskVOs.get(1).getAssigneeId());
				request.setAttribute("taskPersonName", finishedTaskVOs.get(1).getAssigneeName());
				request.setAttribute("confirmPersonId", finishedTaskVOs.get(0).getAssigneeId());
				request.setAttribute("confirmPersonName", finishedTaskVOs.get(0).getAssigneeName());
				// 从后往前最后一次测试
				for (int i = finishedTaskVOs.size() - 1; i > 0; i--) {
					if ("测试".equals(finishedTaskVOs.get(i).getTaskName())) {
						arg.setTesterId(finishedTaskVOs.get(i).getAssigneeId());
						arg.setTesterName(finishedTaskVOs.get(i).getAssigneeName());
						request.setAttribute("checkPersonId", finishedTaskVOs.get(i).getAssigneeId());
						request.setAttribute("checkPersonName", finishedTaskVOs.get(i).getAssigneeName());
						break;
					}
				}
				String xqId = softPerformanceService.getReuirementPersonId(arg.getRequirementId() + "");
				String xqName = staffService.getRealNameByUserId(xqId);
				arg.setXuqiuName(xqName);
				arg.setXuqiuId(xqId);
				request.setAttribute("xuqiuPersonId", xqId);
				request.setAttribute("xuqiuPersonName", xqName);
				List<SoftGroupEntity> cpPersons = softPerformanceService.getSoftPersonsAll(SoftPosition.产品经理,
						arg.getProjectName());
				if (CollectionUtils.isEmpty(cpPersons)) {
					cpPersons = softPerformanceService.getSoftPersons(SoftPosition.产品经理, true);
				}
				if (cpPersons != null && cpPersons.size() > 0) {
					arg.setChanpinManager(cpPersons.get(0).getUserName());
					arg.setChanpinManagerId(cpPersons.get(0).getUserId());
					request.setAttribute("chanPinManagerPersonId", cpPersons.get(0).getUserId());
					request.setAttribute("chanPinManagerPersonName", cpPersons.get(0).getUserName());
				}
				runtimeService.setVariable(processInstance.getId(), "arg", arg);
				;
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				logger.error(sw.toString());
			}
			;
		}
		selectedPanel = "findTaskList";
		if ("group".equals(request.getParameter("isGroup"))) {
			selectedPanel = "softPerformanceSubject";
		}
		return "tablePerform";
	}

	public String toRecoredResult() {
		String taskID = request.getParameter("taskID");
		List<FormField> formFields = processService.getFormFields(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		if (TaskDefKeyEnum.EMAIL_CONFIRM.getName().equals(task.getTaskDefinitionKey())) {
			EmailVO emailVO = emailService.getEmailVOByProcessInstanceID(processInstance.getId());
			request.setAttribute("emailVO", emailVO);
		}
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		return "toRecoredResult";
	}

	public String fillFormal() {
		String taskID = request.getParameter("taskID");
		List<CommentVO> comments = processService.getComments(taskID);
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		formalVO = (FormalVO) runtimeService.getVariable(processInstance.getId(), "arg");
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		selectedPanel = "findTaskList";
		return "fillFormal";
	}

	/**
	 * 员工填写转正申请表
	 * 
	 * @return
	 */
	public String submitFormal() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		try {
			ProcessInstance pinInstance = processService.getProcessInstance(taskID);
			FormalVO arg = (FormalVO) runtimeService.getVariable(pinInstance.getId(), "arg");
			arg.setRequestFormalDate(formalVO.getRequestFormalDate());
			arg.setSummary(formalVO.getSummary());
			runtimeService.setVariable(pinInstance.getId(), "arg", arg);
			// 完成任务
			processService.completeTask(taskID, user.getId(), null, comment);
			// 更新业务表的数据
			formalService.updateFormal(formalVO, pinInstance.getId(), null);

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "转正申请表填写失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		return "submitFormal";
	}
	@Setter
	@Getter
	private BankAccountEntity bankAccount;
	public String updateBankAccount() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		try {
			if ("advance".equals(request.getParameter("processType"))) {
				ProcessInstance pInstance = processService.getProcessInstance(taskID);
				AdvanceVo arg = (AdvanceVo) runtimeService.getVariable(pInstance.getId(), "arg");
				arg.setCardName(reimbursementVO.getCardName());
				arg.setBank(reimbursementVO.getBank());
				arg.setCardNumber(reimbursementVO.getCardNumber());
				runtimeService.setVariable(pInstance.getId(), "arg", arg);
				// 完成任务
				processService.completeTask(taskID, user.getId(), null, comment);
				// 更新打款账号
				//reimbursementService.updateAdvanceBankAccountByUserID(arg.getPayeeID(), arg);
				bankAccount.setCardName(reimbursementVO.getCardName());
				bankAccount.setBank(reimbursementVO.getBank());
				bankAccount.setCardNumber(reimbursementVO.getCardNumber());
				reimbursementService.updateBankAccount(bankAccount);
			} else if("payment".equals(request.getParameter("processType"))){
				ProcessInstance pInstance = processService.getProcessInstance(taskID);
				PaymentVo arg = (PaymentVo) runtimeService.getVariable(pInstance.getId(), "arg");
				arg.setCardName(reimbursementVO.getCardName());
				arg.setBank(reimbursementVO.getBank());
				arg.setCardNumber(reimbursementVO.getCardNumber());
				runtimeService.setVariable(pInstance.getId(), "arg", arg);
				// 完成任务
				processService.completeTask(taskID, user.getId(), null, comment);
				// 更新打款账号
				//reimbursementService.updatePaymentBankAccountByUserID(arg.getPayeeID(), arg);
				bankAccount.setCardName(reimbursementVO.getCardName());
				bankAccount.setBank(reimbursementVO.getBank());
				bankAccount.setCardNumber(reimbursementVO.getCardNumber());
				reimbursementService.updateBankAccount(bankAccount);
			} else {
				ProcessInstance pInstance = processService.getProcessInstance(taskID);
				ReimbursementVO arg = (ReimbursementVO) runtimeService.getVariable(pInstance.getId(), "arg");
				arg.setCardName(reimbursementVO.getCardName());
				arg.setBank(reimbursementVO.getBank());
				arg.setCardNumber(reimbursementVO.getCardNumber());
				runtimeService.setVariable(pInstance.getId(), "arg", arg);
				// 完成任务
				processService.completeTask(taskID, user.getId(), null, comment);
				// 更新打款账号
				reimbursementService.updateBankAccountByUserID(arg.getPayeeID(), arg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "修改打款账号失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.REIMBURSEMENT.getValue();
		return "updateBankAccount";
	}

	/**
	 * 执行人确认是否接收分配任务
	 * 
	 * @return
	 */
	public String confirmAssignment() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String beginDate = request.getParameter("beginDate");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			if (result == TaskResultEnum.RECEIVE.getValue() && !StringUtils.isBlank(beginDate)) {
				// 设置任务的开始时间
				assignmentService.updateBeginDate(pInstance.getId(), DateUtil.getFullDate(beginDate));
			}
			// 完成确认
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			// 更新OA_Assignment表的流程节点状态processStatus
			assignmentService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.ASSIGNMENT.getValue();
		return "taskComplete";
	}

	/**
	 * 员工填写背景调查表格
	 * 
	 * @return
	 */
	public String fillAuditForm() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			// 完成确认
			processService.completeTask(taskID, user.getId(), null, null);
			// 记录调查信息,并更新员工调查状态
			staffService.saveAudit(staffAuditVO, pInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.AUDIT.getValue();
		return "taskComplete";
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
			Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
			if ("vacation_supervisor_audit".equals(task.getTaskDefinitionKey())) {
				// 如果需要上级主管审批 那么 判断是否有上级主管
				Map<String, Object> keys = new HashMap<String, Object>();
				keys.put("needSuperSubject", 2);
				if (result == 3) {
					// 再上级 主管 就是 当前 人的上级主管
					String supervisor = staffService.querySupervisor(user.getId());
					if (StringUtils.isNotBlank(supervisor)) {
						keys.put("vacation_super_person", supervisor);
						keys.put("needSuperSubject", 1);
					}
				}
				// 1 代表 通过 有上级主管 3代表 通过无上级主管
				if (result == 3) {
					result = 1;
					keys.put("needSuperSubject", 2);
				}
				processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment, keys);
			}else if(BusinessTypeEnum.CHOP_BORROW.getName().equals(businessType)){
				//盖章未通过
				if(result == 2){
					//若是品牌授权的公章申请，需要完结品牌授权的任务
					BrandAuthEntity brandAuth = brandAuthService.getBrandAuthByChopInstanceId(pInstance.getId());
					if(null != brandAuth){
						Task brandAuthTask = taskService.createTaskQuery().processInstanceId(
								brandAuth.getProcessInstanceID()).singleResult();
						taskService.setAssignee(brandAuthTask.getId(), user.getId());
						taskService.complete(brandAuthTask.getId());
						brandAuthService.updateProcessStatus(brandAuth.getProcessInstanceID(), String.valueOf(TaskResultEnum.REFUSE_STAMP.getValue()));
					}
				}else if("chop_borrow".equals(task.getTaskDefinitionKey())){
					//若是品牌授权的公章申请，需要完结品牌授权的任务
					BrandAuthEntity brandAuth = brandAuthService.getBrandAuthByChopInstanceId(pInstance.getId());
					if(null != brandAuth){
						Task brandAuthTask = taskService.createTaskQuery().processInstanceId(
								brandAuth.getProcessInstanceID()).singleResult();
						taskService.setAssignee(brandAuthTask.getId(), user.getId());
						taskService.complete(brandAuthTask.getId());
						brandAuthService.updateProcessStatus(brandAuth.getProcessInstanceID(), String.valueOf(TaskResultEnum.COMPLETE_STAMP.getValue()));
					}
				}
				processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			}else {
				// 完成任务
				processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			}
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			someSpecialHandle(businessType, pInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = "处理失败：" + e.getMessage();
			return "error";
		}

		type = getBusinessTypeValue(businessType);
		return "taskComplete";
	}
	public String contractSignConfirm(){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String businessType = request.getParameter("businessType");
		String exceedGroup = request.getParameter("exceedGroup");
		String exceedSeason =request.getParameter("exceedSeason");
		String exceedSeasonRate = request.getParameter("exceedSeasonRate");
		String exceedGroupRate =request.getParameter("exceedGroupRate");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			ContractSignVo contractSignVo = (ContractSignVo)runtimeService.getVariable(pInstance.getId(), "arg");
			contractSignVo.setExceedGroup(Integer.parseInt(StringUtils.isBlank(exceedGroup) ? "0":exceedGroup));
			contractSignVo.setExceedSeason(Integer.parseInt(StringUtils.isBlank(exceedSeason) ? "0":exceedSeason));
			contractSignVo.setExceedGroupRate(exceedGroupRate);
			contractSignVo.setExceedSeasonRate(exceedSeasonRate);
			// 完成任务
			processService.contractSignFinancialConfirm(taskID, user.getId(), TaskResultEnum.valueOf(result), comment, contractSignVo);
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			contractSignVo.setProcessInstanceID(pInstance.getId());
			//更新业务表的数据
			contractService.updateContractSign(contractSignVo);

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = getBusinessTypeValue(businessType);
		return "taskComplete";
	}
	public String softTaskCompleteGroup() {
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
			//这边状态做下修改，兼容老数据
			if(TaskResultEnum.SOFT_TESTER.getValue() == result){
				processService.completeTask(taskID, user.getId(), TaskResultEnum.SOFT_CONFIRMCODE, comment);
				processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			}else{
				processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
				processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "softTaskCompleteGroup";
	}

	public String commonSubjectComplete() {
		User user = (User) request.getSession().getAttribute("user");
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String complete=request.getParameter("complete");
		try {
			if("1".equals(complete)){
				ProcessInstance pInstance = processService.getProcessInstance(taskID);
				processService.completeTask(taskID, user.getId(), TaskResultEnum.DISAGREE, comment);
				processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.DISAGREE,
						BusinessTypeEnum.COMMONSUBJECT.getName());
				type=BusinessTypeEnum.COMMONSUBJECT.getValue();
			}else{
				ProcessInstance pInstance = processService.getProcessInstance(taskID);
				processService.completeTask(taskID, user.getId(), TaskResultEnum.AGREE, comment);
				processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.COMPLETEAll,
						BusinessTypeEnum.COMMONSUBJECT.getName());
				type=BusinessTypeEnum.COMMONSUBJECT.getValue();
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "taskComplete";
	}

	public void editSoftPerformanceTask() {
		User user = (User) request.getSession().getAttribute("user");
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String businessType = request.getParameter("businessType");
		String name = request.getParameter("name");
		String content = request.getParameter("content");
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.COMPLETE, businessType);
		SoftPerformanceVo arg = (SoftPerformanceVo) runtimeService.getVariable(pInstance.getId(), "arg");
		arg.setName(name);
		arg.setTitle(name);
		arg.setDescription(content);
		runtimeService.setVariable(pInstance.getId(), "arg", arg);
		FunctionEntity functionEntity = softPerformanceService.geFunctionEntityByInstanceId(pInstance.getId());
		functionEntity.setName(name);
		functionEntity.setDescription(content);
		softPerformanceService.commonUpdate(functionEntity);
		try {
			processService.completeTask(taskID, user.getId(), TaskResultEnum.COMPLETE, comment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String confirmScore() {
		User user = (User) request.getSession().getAttribute("user");
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String businessType = request.getParameter("businessType");
		try {

			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			// 记录分值
			SoftPerformanceVo arg = (SoftPerformanceVo) runtimeService.getVariable(pInstance.getId(), "arg");
			FunctionEntity functionEntity = softPerformanceService.geFunctionEntityByInstanceId(pInstance.getId());
			functionEntity.setVoDetail(ObjectByteArrTransformer.toByteArray(arg));
			softPerformanceService.commonUpdate(functionEntity);
			Date now = new Date();
			FunctionEntity fe = softPerformanceService.geFunctionEntityByInstanceId(pInstance.getId());
			String kf = arg.getAssignerId();
			String zz = arg.getCreatorId();
			String cs = arg.getTesterId();
			String ss = arg.getSsPersonId();
			String xq = arg.getXuqiuId();
			String jl = arg.getChanpinManagerId();
			String score = arg.getScore();
			double score_ = 0;
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			score_ = Double.parseDouble(score);

			if (TaskResultEnum.SOFT_CONFIRMSCOREEFFCTIVE.getValue() == result) {
				// 根据requirementId 查询 requirementId 下所有 subrequirement的办结状态 假如
				// 都是 30 那么 完结需求
				softPerformanceService.checkIsAllComplete(arg.getRequirementId() + "");
				if ("否".equals(arg.getIsSatisfy())) {
					// 当前只会塞入
					String duty = arg.getDutyType();
					String[] duties = duty.split(",");
					if (!SoftPerformanceVo.isInStringArray(duties, SoftPerformanceScore.实施.getIndex())) {
						ScoreResultEntity ssScore = new ScoreResultEntity();
						ssScore.setDuty(SoftPerformanceScore.实施.name());
						ssScore.setItemDate(now);
						ssScore.setAddTime(now);
						ssScore.setUserId(ss);
						ssScore.setIsDeleted(0);
						ssScore.setResultScore(score_ * arg.getSSpercent() / 100);
						ssScore.setTaskId(fe.getId());
						softPerformanceService.commonSave(ssScore);
					}
					if (!SoftPerformanceVo.isInStringArray(duties, SoftPerformanceScore.开发人员.getIndex())) {
						ScoreResultEntity kfScore = new ScoreResultEntity();
						kfScore.setDuty(SoftPerformanceScore.开发人员.name());
						kfScore.setItemDate(now);
						kfScore.setAddTime(now);
						kfScore.setIsDeleted(0);
						kfScore.setUserId(kf);
						kfScore.setResultScore(score_ * arg.getKFPercent() / 100);
						kfScore.setTaskId(fe.getId());
						softPerformanceService.commonSave(kfScore);
					}
					if (!SoftPerformanceVo.isInStringArray(duties, SoftPerformanceScore.组长.getIndex())) {
						ScoreResultEntity zzScore = new ScoreResultEntity();
						zzScore.setDuty(SoftPerformanceScore.组长.name());
						zzScore.setItemDate(now);
						zzScore.setAddTime(now);
						zzScore.setUserId(zz);
						zzScore.setIsDeleted(0);
						zzScore.setResultScore(score_ * arg.getZZpercent() / 100);
						zzScore.setTaskId(fe.getId());
						softPerformanceService.commonSave(zzScore);
					}
					if (!SoftPerformanceVo.isInStringArray(duties, SoftPerformanceScore.测试.getIndex())) {
						ScoreResultEntity zzScore = new ScoreResultEntity();
						zzScore.setDuty(SoftPerformanceScore.测试.name());
						zzScore.setItemDate(now);
						zzScore.setAddTime(now);
						zzScore.setUserId(cs);
						zzScore.setIsDeleted(0);
						zzScore.setResultScore(score_ * arg.getCSpercent() / 100);
						zzScore.setTaskId(fe.getId());
						softPerformanceService.commonSave(zzScore);
					}
					if (!SoftPerformanceVo.isInStringArray(duties, SoftPerformanceScore.项目经理.getIndex())) {
						ScoreResultEntity jlScore = new ScoreResultEntity();
						jlScore.setDuty(SoftPerformanceScore.项目经理.name());
						jlScore.setItemDate(now);
						jlScore.setAddTime(now);
						jlScore.setIsDeleted(0);
						jlScore.setUserId(jl);
						jlScore.setResultScore(score_ * arg.getJLPercent() / 100);
						jlScore.setTaskId(fe.getId());
						softPerformanceService.commonSave(jlScore);
					}
					if (!SoftPerformanceVo.isInStringArray(duties, SoftPerformanceScore.需求.getIndex())) {
						ScoreResultEntity xqScore = new ScoreResultEntity();
						xqScore.setDuty(SoftPerformanceScore.需求.name());
						xqScore.setItemDate(now);
						xqScore.setAddTime(now);
						xqScore.setIsDeleted(0);
						xqScore.setUserId(xq);
						xqScore.setResultScore(score_ * arg.getXQpercent() / 100);
						xqScore.setTaskId(fe.getId());
						softPerformanceService.commonSave(xqScore);
					}
				} else {

					try {
						score_ = Double.parseDouble(score);
					} catch (Exception ignore) {
					}
					;

					ScoreResultEntity ssScore = new ScoreResultEntity();
					ssScore.setDuty(SoftPerformanceScore.实施.name());
					ssScore.setItemDate(now);
					ssScore.setAddTime(now);
					ssScore.setUserId(ss);
					ssScore.setIsDeleted(0);
					ssScore.setResultScore(score_ * arg.getSSpercent() / 100);
					ssScore.setTaskId(fe.getId());
					softPerformanceService.commonSave(ssScore);

					ScoreResultEntity kfScore = new ScoreResultEntity();
					kfScore.setDuty(SoftPerformanceScore.开发人员.name());
					kfScore.setItemDate(now);
					kfScore.setAddTime(now);
					kfScore.setIsDeleted(0);
					kfScore.setUserId(kf);
					kfScore.setResultScore(score_ * arg.getKFPercent() / 100);
					kfScore.setTaskId(fe.getId());
					softPerformanceService.commonSave(kfScore);

					ScoreResultEntity zzScore = new ScoreResultEntity();
					zzScore.setDuty(SoftPerformanceScore.组长.name());
					zzScore.setItemDate(now);
					zzScore.setAddTime(now);
					zzScore.setUserId(zz);
					zzScore.setIsDeleted(0);
					zzScore.setResultScore(score_ * arg.getZZpercent() / 100);
					zzScore.setTaskId(fe.getId());
					softPerformanceService.commonSave(zzScore);

					ScoreResultEntity csScore = new ScoreResultEntity();
					csScore.setDuty(SoftPerformanceScore.测试.name());
					csScore.setItemDate(now);
					csScore.setAddTime(now);
					csScore.setIsDeleted(0);
					csScore.setUserId(cs);
					csScore.setResultScore(score_ * arg.getCSpercent() / 100);
					csScore.setTaskId(fe.getId());
					softPerformanceService.commonSave(csScore);

					ScoreResultEntity jlScore = new ScoreResultEntity();
					jlScore.setDuty(SoftPerformanceScore.项目经理.name());
					jlScore.setItemDate(now);
					jlScore.setAddTime(now);
					jlScore.setIsDeleted(0);
					jlScore.setUserId(jl);
					jlScore.setResultScore(score_ * arg.getJLPercent() / 100);
					jlScore.setTaskId(fe.getId());
					softPerformanceService.commonSave(jlScore);

					ScoreResultEntity xqScore = new ScoreResultEntity();
					xqScore.setDuty(SoftPerformanceScore.需求.name());
					xqScore.setItemDate(now);
					xqScore.setAddTime(now);
					xqScore.setIsDeleted(0);
					xqScore.setUserId(xq);
					xqScore.setResultScore(score_ * arg.getXQpercent() / 100);
					xqScore.setTaskId(fe.getId());
					softPerformanceService.commonSave(xqScore);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = getBusinessTypeValue(businessType);
		return "taskComplete";
	}

	@Setter
	private String[] cause;

	// 软件绩效特殊情况提交
	public String softResultRecord() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String isMeet = request.getParameter("isMeet");
		String score = request.getParameter("score");
		String causeDetail = request.getParameter("causeDetail");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			SoftPerformanceVo arg = (SoftPerformanceVo) runtimeService.getVariable(pInstance.getId(), "arg");
			arg.setSsPersonId(user.getId());
			arg.setSsPersonName(staffService.getRealNameByUserId(user.getId()));
			arg.setReason(causeDetail);
			arg.setDutyType(StringUtils.join(cause, ","));
			arg.setEvaluate(StringUtils.isBlank(score) ? 0 : Integer.parseInt(score));
			arg.setIsSatisfy(isMeet);
			runtimeService.setVariable(pInstance.getId(), "arg", arg);
			processService.completeTask(taskID, user.getId(), TaskResultEnum.SOFT_SSCOMPLETE, comment);
			softPerformanceService.updateProcessStatus(TaskResultEnum.SOFT_SSCOMPLETE, pInstance.getId());
			// 同步业务表
			FunctionEntity fe = softPerformanceService.geFunctionEntityByInstanceId(pInstance.getId());
			fe.setSsId(user.getId());
			fe.setReason(causeDetail);
			fe.setTesterId(arg.getTesterId());
			fe.setDutyType(StringUtils.join(cause, ","));
			fe.setEvaluate(StringUtils.isBlank(score) ? 0 : Integer.parseInt(score));
			fe.setIsSatisfy(isMeet);
			//fe.setResult(result);
			softPerformanceService.commonUpdate(fe);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "softResultRecord";
	}

	private void someSpecialHandle(String buinessType, String intanceId) {
		String taskDefKey = request.getParameter("taskDefKey");
		if (BusinessTypeEnum.CHOP_BORROW.getName().equals(buinessType)) {
			if ("chop_borrow".equals(taskDefKey)) {
				chopService.updateRealBeginTime(intanceId);
			} else if ("chop_return".equals(taskDefKey)) {
				chopService.updateRealEnd(intanceId);
			}
		} else if (BusinessTypeEnum.ID_BORROW.getName().equals(buinessType)) {
			if ("return_id_card".equals(taskDefKey)) {
				chopService.updateIdRealBeginTime(intanceId);
			} else if ("subject_apply_Id".equals(taskDefKey)) {
				chopService.updateIdRealEndTime(intanceId);
			}
		} else if (BusinessTypeEnum.CERTIFICATE_BORROW.getName().equals(buinessType)) {
			if ("certificate_borrow".equals(taskDefKey)) {
				certificateService.updateIdRealBeginTime(intanceId);
			} else if ("certificate_return".equals(taskDefKey)) {
				certificateService.updateIdRealEndTime(intanceId);
			}
		} else if (BusinessTypeEnum.CONTRACT_BORROW.getName().equals(buinessType)) {
			if ("contract_borrow".equals(taskDefKey)) {
				contractService.updateRealBeginTime(intanceId);
			} else if ("contract_return".equals(taskDefKey)) {
				contractService.updateRealEndTime(intanceId);
			}
		}
	}

	private int getBusinessTypeValue(String name) {
		if (BusinessTypeEnum.VACATION.getName().equals(name)) {
			return BusinessTypeEnum.VACATION.getValue();
		} else if (BusinessTypeEnum.ASSIGNMENT.getName().equals(name)) {
			return BusinessTypeEnum.ASSIGNMENT.getValue();
		} else if (BusinessTypeEnum.RESIGNATION.getName().equals(name)) {
			return BusinessTypeEnum.RESIGNATION.getValue();
		} else if (BusinessTypeEnum.FORMAL.getName().equals(name)) {
			return BusinessTypeEnum.FORMAL.getValue();
		} else if (BusinessTypeEnum.REIMBURSEMENT.getName().equals(name)) {
			return BusinessTypeEnum.REIMBURSEMENT.getValue();
		} else if (BusinessTypeEnum.EMAIL.getName().equals(name)) {
			return BusinessTypeEnum.EMAIL.getValue();
		} else if (BusinessTypeEnum.SOCIAL_SECURITY.getName().equals(name)) {
			return BusinessTypeEnum.SOCIAL_SECURITY.getValue();
		} else if (BusinessTypeEnum.CHOP_BORROW.getName().equals(name)) {
			return BusinessTypeEnum.CHOP_BORROW.getValue();
		} else if (BusinessTypeEnum.ID_BORROW.getName().equals(name)) {
			return BusinessTypeEnum.ID_BORROW.getValue();
		} else if (BusinessTypeEnum.CAR_USE.getName().equals(name)) {
			return BusinessTypeEnum.CAR_USE.getValue();
		} else if (BusinessTypeEnum.ADVANCE.getName().equals(name)) {
			return BusinessTypeEnum.ADVANCE.getValue();
		} else if (BusinessTypeEnum.SOFTPERFORMANCE.getName().equals(name)) {
			return BusinessTypeEnum.SOFTPERFORMANCE.getValue();
		} else if (BusinessTypeEnum.CERTIFICATE_BORROW.getName().equals(name)) {
			return BusinessTypeEnum.CERTIFICATE_BORROW.getValue();
		} else if (BusinessTypeEnum.CONTRACT_BORROW.getName().equals(name)) {
			return BusinessTypeEnum.CONTRACT_BORROW.getValue();
		} else if (BusinessTypeEnum.CONTRACT.getName().equals(name)) {
			return BusinessTypeEnum.CONTRACT.getValue();
		} else if (BusinessTypeEnum.CONTRACT_CHANGE.getName().equals(name)) {
			return BusinessTypeEnum.CONTRACT_CHANGE.getValue();
		} else if (BusinessTypeEnum.BANK_ACCOUNT_CHANGE.getName().equals(name)) {
			return BusinessTypeEnum.BANK_ACCOUNT_CHANGE.getValue();
		} else if (BusinessTypeEnum.CHOP_DESTROY.getName().equals(name)) {
			return BusinessTypeEnum.CHOP_DESTROY.getValue();
		} else if (BusinessTypeEnum.PURCHASE_PROPERTY.getName().equals(name)) {
			return BusinessTypeEnum.PURCHASE_PROPERTY.getValue();
		} else if (BusinessTypeEnum.CARVE_CHOP.getName().equals(name)) {
			return BusinessTypeEnum.CARVE_CHOP.getValue();
		} else if (BusinessTypeEnum.HANDLE_PROPERTY.getName().equals(name)) {
			return BusinessTypeEnum.HANDLE_PROPERTY.getValue();
		} else if (BusinessTypeEnum.TRANSFER_PROPERTY.getName().equals(name)) {
			return BusinessTypeEnum.TRANSFER_PROPERTY.getValue();
		} else if (BusinessTypeEnum.SHOP_APPLY.getName().equals(name)) {
			return BusinessTypeEnum.SHOP_APPLY.getValue();
		} else if (BusinessTypeEnum.SHOP_PAY_APPLY.getName().equals(name)) {
			return BusinessTypeEnum.SHOP_PAY_APPLY.getValue();
		} else if (BusinessTypeEnum.WORK_OVERTIME.getName().equals(name)) {
			return BusinessTypeEnum.WORK_OVERTIME.getValue();
		} else if (BusinessTypeEnum.PAYMENT.getName().equals(name)) {
			return BusinessTypeEnum.PAYMENT.getValue();
		}
		return 0;
	}

	public String resignationConfirm() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			String confirmLeaveDate = request.getParameter("confirmLeaveDate");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			if (result == TaskResultEnum.AGREE.getValue() && !StringUtils.isBlank(confirmLeaveDate)) {
				// 确认员工的离职日期
				Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
				resignationService.confirmLeaveDate(pInstance.getId(), DateUtil.getSimpleDate(confirmLeaveDate),
						task.getTaskDefinitionKey());
			}
			// 完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result),
					BusinessTypeEnum.RESIGNATION.getName());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.RESIGNATION.getValue();
		return "taskComplete";
	}

	/**
	 * 审核步骤
	 * 
	 * @return
	 */
	public String contractConfirm1() {
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
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result), businessType);
			ContractDetailEntity contractDetailEntity = chopService.getContractByInstanceId(pInstance.getId());
			contractDetailEntity.setSubjectTime(new Date());
			chopService.updateContract(contractDetailEntity);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.CONTRACT.getValue();
		return "taskComplete";
	}

	/**
	 * 签署步骤
	 * 
	 * @return
	 */
	public String contractConfirm2() {
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
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.CONTRACT_SIGNED, businessType);
			String store_person_id = request.getParameter("store_person_id");
			String store_area = request.getParameter("store_area");
			ContractDetailEntity contractDetailEntity = chopService.getContractByInstanceId(pInstance.getId());
			contractDetailEntity.setSignTime(new Date());
			contractDetailEntity.setStorePersonId(store_person_id);
			contractDetailEntity.setStorePlace(store_area);
			chopService.updateContract(contractDetailEntity);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.CONTRACT.getValue();
		return "taskComplete";
	}

	public String formalConfirm() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String actualFormalDate = request.getParameter("actualFormalDate");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			if (result == TaskResultEnum.AGREE.getValue() && !StringUtils.isBlank(actualFormalDate)) {
				// 确认员工的转正日期
				formalService.confirmFormalDate(pInstance.getId(), DateUtil.getSimpleDate(actualFormalDate));
			}
			// 完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(result), comment);
			// 更新业务表中流程节点状态
			processService.updateProcessStatus(pInstance.getId(), TaskResultEnum.valueOf(result),
					BusinessTypeEnum.FORMAL.getName());
			if (runtimeService.createProcessInstanceQuery().processInstanceId(pInstance.getId()).singleResult() == null
					&& (result == TaskResultEnum.AGREE.getValue())) {
				// 转正流程结束，且审批通过时，修改staff表的status和转正日期
				FormalVO formal = formalService.getFormalByProcessInstanceID(pInstance.getId());
				staffService.updateStaffStatusFormalDate(formal.getRequestUserID(), StaffStatusEnum.FORMAL,
						DateUtil.getSimpleDate(actualFormalDate));
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.FORMAL.getValue();
		return "taskComplete";
	}

	public String updateAssignment() {
		String processInstanceID = request.getParameter("processInstanceID");
		if (StringUtils.isBlank(processInstanceID)) {
			String taskID = request.getParameter("taskID");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			processInstanceID = pInstance.getId();
			request.setAttribute("taskID", taskID);
			selectedPanel = "findTaskList";
		} else {
			request.setAttribute("processInstanceID", processInstanceID);
			selectedPanel = "findProcessList";
		}

		AssignmentVO assignment = (AssignmentVO) runtimeService.getVariable(processInstanceID, "arg");
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("assignmentVO", assignment);
		request.setAttribute("companyVOs", companyVOs);
		return "updateAssignment";
	}

	public String processDetail() {
		String processInstanceID = request.getParameter("processInstanceID");
		List<FormField> formFields = processService.getFormFieldsByProcessInstanceID(processInstanceID);
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
		request.setAttribute("formFields", formFields);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		selectedPanel = "findProcessList";
		return "processDetail";
	}

	public String getPrintDetail() {
		String instanceId = this.request.getParameter("instanceId");
		Task task1 = taskService.createTaskQuery().processInstanceId(instanceId).singleResult();
		String taskID = task1.getId();
		ReimbursementVO reimbursementVO = reimbursementService.getReimbursementVOByTaskID(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		List<Group> groups = identityService.createGroupQuery().groupMember(reimbursementVO.getRequestUserID()).list();
		if (groups.size() > 0) {
			String[] positionIDs = groups.get(0).getType().split("_");
			String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
			String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
					.getDepartmentName();
			request.setAttribute("companyName", companyName);
			request.setAttribute("departmentName", departmentName);
		}
		ReimbursementVO reimbursementVO2 = reimbursementService
				.getReimbursementVOByProcessInstanceID(processInstance.getId());
		reimbursementVO.setShowPerson1(reimbursementVO2.getShowPerson1());
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("attas", attas);
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("businessKey", processDefinition.getKey());
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		selectedPanel = "findProcessList";
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		return "getPrintDetail";
	}

	public String findProcessList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		int count = 0;
		try {
			switch (BusinessTypeEnum.valueOf(type)) {
			case VACATION:
				ListResult<VacationVO> vaListResult = vacationService.findVacationListByUserID(user.getId(), page,
						limit);
				List<VacationVO> vacationVOs = vaListResult.getList();
				for(VacationVO vacationVO: vacationVOs){
					String instanceId = vacationVO.getProcessInstanceID();
					List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(instanceId);
					if(finishedTaskVOs!=null && finishedTaskVOs.size()==1){
						vacationVO.setCanStopInstance(true);
					}else{
						vacationVO.setCanStopInstance(false);
					}
				}
				request.setAttribute("vacationVOs", vacationVOs);
				count = vaListResult.getTotalCount();
				break;
			case ASSIGNMENT:
				ListResult<AssignmentVO> asListResult = assignmentService.findAssignmentListByUserID(user.getId(), page,
						limit);
				request.setAttribute("assignmentVOs", asListResult.getList());
				count = asListResult.getTotalCount();
				break;
			case RESIGNATION:
				ListResult<ResignationVO> reListResult = resignationService.findResignationListByUserID(user.getId(),
						page, limit);
				request.setAttribute("resignationVOs", reListResult.getList());
				count = reListResult.getTotalCount();
				break;
			case FORMAL:
				ListResult<FormalVO> fmListResult = formalService.findFormalListByUserID(user.getId(), page, limit);
				request.setAttribute("formalVOs", fmListResult.getList());
				count = fmListResult.getTotalCount();
				break;
			case REIMBURSEMENT:
				ListResult<ReimbursementVO> rbListResult = reimbursementService
				.findReimbursementListByUserID(user.getId(), page, limit);
				List<ReimbursementVO> reimbursementVOs = rbListResult.getList();
				for(ReimbursementVO reimbursementVO: reimbursementVOs){
					String instanceId = reimbursementVO.getProcessInstanceID();
					List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(instanceId);
					if(finishedTaskVOs!=null && finishedTaskVOs.size()==1){
						reimbursementVO.setCanStopInstance(true);
					}else{
						reimbursementVO.setCanStopInstance(false);
					}
				}
				request.setAttribute("reimbursementVOs", reimbursementVOs);
				count = rbListResult.getTotalCount();
				break;
			case ADVANCE:
				ListResult<AdvanceVo> adListResult = reimbursementService.findAdvanceListByUserID(user.getId(), page,
						limit);
				List<AdvanceVo> advanceVos = adListResult.getList();
				for(AdvanceVo advanceVo: advanceVos){
					String instanceId = advanceVo.getProcessInstanceID();
					List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(instanceId);
					if(finishedTaskVOs!=null && finishedTaskVOs.size()==1){
						advanceVo.setCanStopInstance(true);
					}else{
						advanceVo.setCanStopInstance(false);
					}
				}
				request.setAttribute("reimbursementVOs", advanceVos);
				count = adListResult.getTotalCount();
				break;
			case PAYMENT:
				ListResult<PaymentVo> paymentListResult = reimbursementService.findPaymentListByUserID(user.getId(), page,
						limit);
				List<PaymentVo> paymentVos = paymentListResult.getList();
				for(PaymentVo paymentVo: paymentVos){
					String instanceId = paymentVo.getProcessInstanceID();
					List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(instanceId);
					if(finishedTaskVOs!=null && finishedTaskVOs.size()==1){
						paymentVo.setCanStopInstance(true);
					}else{
						paymentVo.setCanStopInstance(false);
					}
				}
				request.setAttribute("reimbursementVOs", paymentVos);
				count = paymentListResult.getTotalCount();
				break;
			case WORK_OVERTIME:
				ListResult<WorkOvertimeVo> workOvertimeListResult = workOvertimeService.findWorkOvertimeListByUserID(user.getId(), page,
						limit);
				request.setAttribute("workOvertimeVos", workOvertimeListResult.getList());
				count = workOvertimeListResult.getTotalCount();
				break;
			case MORNING_MEETING:
				ListResult<MorningMeetingVo> morningMeetingListResult = morningMeetingReportService.findMorningMeetingListByUserID(user.getId(), page,
						limit);
				request.setAttribute("morningMeetingVos", morningMeetingListResult.getList());
				count = morningMeetingListResult.getTotalCount();
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
		totalCount = count;
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		selectedPanel = "findProcessList";
		return "processList";
	}

	public void stopInstance() {
		Map<String, Boolean> resultMap = new HashMap<>();
		try {
			String instanceId = request.getParameter("instanceId");
			String bussinessKey = request.getParameter("bussinessKey");
			List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(instanceId);
			if (finishedTaskVOs != null && finishedTaskVOs.size() == 1) {
				runtimeService.deleteProcessInstance(instanceId, "complete");
				processService.updateProcessStatus(instanceId, TaskResultEnum.END, bussinessKey);
				resultMap.put("isSuccess", true);
			} else {
				resultMap.put("isSuccess", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("isSuccess", false);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}

	public String newTask() {
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "newTask";
		return "newTask";
	}

	public String showVacationDiagram() {
		selectedPanel = "newVacation";
		return "vacationDiagram";
	}

	public String showWorkOvertime() {
		selectedPanel = "newWorkOvertime";
		return "showWorkOvertime";
	}

	public String showAssignmentDiagram() {
		selectedPanel = "newTask";
		return "assignmentDiagram";
	}

	public String showResignationDiagram() {
		User user = (User) request.getSession().getAttribute("user");
		request.setAttribute("userId", user.getId());
		selectedPanel = "newResignation";
		return "resignationDiagram";
	}

	public String showChopBorrow() {
		selectedPanel = "newChopBorrow";
		return "showChopBorrow";
	}

	public String showCertificateBorrow() {
		selectedPanel = "newCertificateBorrow";
		return "showCertificateBorrow";
	}
	public String showContractBorrow() {
		selectedPanel = "newContractBorrow";
		return "showContractBorrow";
	}
	public String showIdBorrow() {
		selectedPanel = "newIdBorrow";
		return "showIdBorrow";
	}

	public String showContractSign() {
		selectedPanel = "newContractSign";
		return "showContractSign";
	}
	public String showChangeContract() {
		selectedPanel = "newChangeContract";
		return "showChangeContract";
	}
	public String showContract() {
		selectedPanel = "newContract";
		return "showContract";
	}
	public String showBankAccount(){
		selectedPanel = "newBankAccount";
		return "showBankAccount";
	}
	public String showChopDestroy() {
		selectedPanel = "newChopDestroy";
		return "showChopDestroy";
	}
	public String showCarveChop(){
		selectedPanel = "newCarveChop";
		return "showCarveChop";
	}
	public String showPurchaseProperty() {
		selectedPanel = "newPurchase";
		return "showPurchaseProperty";
	}
	public String showHandleProperty(){
		selectedPanel = "newHandleProperty";
		return "showHandleProperty";
	}
	public String showTransferProperty(){
		selectedPanel = "newTransferProperty";
		return "showTransferProperty";
	}
	public String showShopApply(){
		selectedPanel = "newShopApply";
		return "showShopApply";
	}
	public String showShopPayApply(){
		selectedPanel = "newShopPayApply";
		return "showShopPayApply";
	}
	public String showViewWorkReport(){
		selectedPanel = "newViewWorkReport";
		return "showViewWorkReport";
	}
	public String showWeekMeetingReport() {
		selectedPanel = "newWeekMeetingReport";
		request.setAttribute("weekDay", DateUtil.getWeekDay(new Date()));
		return "showWeekMeetingReport";
	}
	public String showPublic(){
		selectedPanel = "newPublic";
		return "showPublic";
	}
	public String newChopBorrow() {
		selectedPanel = "newChopBorrow";
		try {
			String name = request.getParameter("name");
			ListResult<Chop> chops = chopService.getChopByName(name, null, page, limit);
			request.setAttribute("name", name);
			int count = chops.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page - 1) * limit);
			request.setAttribute("chops", chops.getList());
			return "newChopBorrowList";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	}

	public String newCertificateBorrow() {
		selectedPanel = "newCertificateBorrow";
		try {
			String name = request.getParameter("name");
			ListResult<CertificateEntity> certificates = certificateService.getCertificateLst(name, limit, page);
			request.setAttribute("name", name);
			int count = certificates.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page - 1) * limit);
			request.setAttribute("certificates", certificates.getList());
			return "certificateLst";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	}

	public String newHandleProperty() {
		selectedPanel = "newHandleProperty";
		try {
			String assetNum = request.getParameter("assetNum");
			String assetName = request.getParameter("assetName");
			String assetStatus = request.getParameter("assetStatus");
			ListResult<AssetVO> assets = handlePropertyService.getAssetList(assetNum, assetName, assetStatus, limit, page);
			int count = assets.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page - 1) * limit);
			request.setAttribute("assetList", assets.getList());
			request.setAttribute("assetNum", assetNum);
			request.setAttribute("assetName", assetName);
			request.setAttribute("assetStatus", assetStatus);
			return "assetList";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	}
	public String newTransferProperty() {
		selectedPanel = "newTransferProperty";
		try {
			String assetNum = request.getParameter("assetNum");
			String assetName = request.getParameter("assetName");
			String assetStatus = request.getParameter("assetStatus");
			ListResult<AssetVO> assets = handlePropertyService.getAssetList(assetNum, assetName, assetStatus, limit, page);
			int count = assets.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page - 1) * limit);
			request.setAttribute("assetList", assets.getList());
			request.setAttribute("assetNum", assetNum);
			request.setAttribute("assetName", assetName);
			request.setAttribute("assetStatus", assetStatus);
			return "assetListForTransfer";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	}
	public String newContractBorrow() {
		selectedPanel = "newContractBorrow";
		try {
			String name = request.getParameter("name");
			ListResult<ContractManageEntity> contracts = contractService.getContractLst(name, limit, page);
			request.setAttribute("name", name);
			int count = contracts.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page - 1) * limit);
			request.setAttribute("contracts", contracts.getList());
			return "contractLst";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	}
	public String toBeginChopBorrowPage() {
		String chopId = request.getParameter("id");
		String processInstanceId = request.getParameter("processInstanceId");
		Chop chop = chopService.getChopById(chopId);
		request.setAttribute("chop", chop);
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		if(StringUtils.isNotBlank(processInstanceId)){
			request.setAttribute("processInstanceId", processInstanceId);
			request.setAttribute("fileUse", request.getParameter("fileUse"));
			request.setAttribute("fileName", request.getParameter("fileName"));
			request.setAttribute("fileNum", "1");
		}
		selectedPanel = "newChopBorrow";
		return "toBeginChopBorrowPage";
	}

	public String toBeginCertificateBorrowPage() {
		String certificateId = request.getParameter("id");
		CertificateEntity certificate = certificateService.getCertificate(certificateId);
		request.setAttribute("certificate", certificate);
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toBeginCertificateBorrowPage";
	}
	public String toBeginContractBorrowPage() {
		String contractId = request.getParameter("id");
		ContractManageEntity contract = contractService.getContract(contractId);
		request.setAttribute("contract", contract);
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toBeginContractBorrowPage";
	}
	public String toBeginIdBorrowPage() {
		selectedPanel = "newIdBorrow";

		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toBeginIdBorrowPage";
	}

	public String toBeginContractPage() {
		selectedPanel = "newContract";

		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toBeginContractPage";
	}
	public String toBeginContractSignPage() {
		selectedPanel = "newContractSign";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toBeginContractSignPage";
	}
	public String toBeginChangeContractPage() {
		selectedPanel = "newChangeContract";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toBeginChangeContractPage";
	}
	public String toBeginBankAccountPage() {
		selectedPanel = "newBankAccount";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toBeginBankAccountPage";
	}
	public String showFormalDiagram() {
		selectedPanel = "newFormal";
		return "formalDiagram";
	}
	public String toBeginChopDestroyPage() {
		String chopId = request.getParameter("id");
		Chop chop = chopService.getChopById(chopId);
		request.setAttribute("chop", chop);
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		selectedPanel = "newChopDestroy";
		return "toBeginChopDestroyPage";
	}
	public String toStartPurchasePropertyPage(){
		selectedPanel = "newPurchase";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toStartPurchasePropertyPage";
	}
	public String toStartCarveChopPage(){
		selectedPanel = "newCarveChop";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toStartCarveChopPage";
	}
	public String toBeginHandlePropertyPage(){
		selectedPanel = "newHandleProperty";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		request.setAttribute("assetName", request.getParameter("assetName"));
		request.setAttribute("assetNum", request.getParameter("assetNum"));
		request.setAttribute("useDepartment", request.getParameter("useDepartment"));
		request.setAttribute("model", request.getParameter("model"));
		request.setAttribute("recipientId", request.getParameter("recipientId"));
		return "toBeginHandlePropertyPage";
	}
	public String toBeginTransferPropertyPage(){
		selectedPanel = "newTransferProperty";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		request.setAttribute("assetName", request.getParameter("assetName"));
		request.setAttribute("assetNum", request.getParameter("assetNum"));
		request.setAttribute("assetType", request.getParameter("assetType"));
		request.setAttribute("model", request.getParameter("model"));
		request.setAttribute("recipientId", request.getParameter("recipientId"));
		request.setAttribute("oldCompany", request.getParameter("oldCompany"));
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		request.setAttribute("assetId", request.getParameter("assetId"));
		return "toBeginTransferPropertyPage";
	}
	public String toBeginShopApplyPage(){
		selectedPanel = "newShopApply";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		request.setAttribute("applyType", request.getParameter("applyType"));
		return "toBeginShopApplyPage";
	}
	public String toBeginShopPayApplyPage(){
		selectedPanel = "newShopPayApply";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		request.setAttribute("applyType", request.getParameter("applyType"));
		return "toBeginShopPayApplyPage";
	}
	public String toStartViewWorkReportPage(){
		selectedPanel = "newViewWorkReport";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companys", companyVOs);
		return "toStartViewWorkReportPage";
	}
	public String toStartMorningMeetingReport(){
		selectedPanel = "newWeekMeetingReport";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		request.setAttribute("weekDay", DateUtil.getWeekDay(new Date()));
		request.setAttribute("currentDate", DateUtil.formateFullDate(new Date()));
		return "toStartMorningMeetingReport";
	}
	public String toStartPublicEventPage(){
		selectedPanel = "newPublic";
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		return "toStartPublicEventPage";
	}
	@SuppressWarnings("static-access")
	public String newVacation() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			Date date = new Date();
			Calendar calendar = new GregorianCalendar(); 
			calendar.setTime(date); 
			calendar.add(calendar.DATE,1);
			date=calendar.getTime(); 
			beginDay = DateUtil.getDayStr(date);
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			String companyIDString = group.getType().split("_")[0];
			String departmentId = group.getType().split("_")[1];
			dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(date));
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(date));
			beginTime = " " + workTimeArray[0] + ":00";
			amEndTime = " " + workTimeArray[1] + ":00";
			pmBeginTime = " " + workTimeArray[2] + ":00";
			endTime = " " + workTimeArray[3] + ":00";
			request.setAttribute("companyId", companyIDString);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工所在分公司作息时间失败！";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("userName", staff.getLastName());
		request.setAttribute("objectType", request.getParameter("objectType"));
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companys", companyVOs);
		selectedPanel = "newVacation";
		return "newVacation";
	}
	@SuppressWarnings("static-access")
	public String newWorkOvertime() {
		User user = (User) request.getSession().getAttribute("user");
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staff);
		Date date = new Date();
		Calendar calendar = new GregorianCalendar(); 
		calendar.setTime(date); 
		calendar.add(calendar.DATE,1);
		date = calendar.getTime(); 
		beginDay = DateUtil.getDayStr(date);
		try {
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			String companyIDString = groups.get(0).getType().split("_")[0];
			String departmentId = groups.get(0).getType().split("_")[1];
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(date));
			beginTime = " " + workTimeArray[0] + ":00";
			workOverBeginTime = attendanceService.getWorkOverBeginTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(date));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工所在分公司作息时间失败！";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "newWorkOvertime";
		request.setAttribute("objectType", request.getParameter("objectType"));
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companys", companyVOs);
		return "newWorkOvertime";
	}
	public String getWorkTimeByUserID() {
		String userID = request.getParameter("userID");
		try {
			List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
			//存在多个职位，以总部的职位优先
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			String companyIDString = group.getType().split("_")[0];
			String departmentId = group.getType().split("_")[1];
			//String companyIDString = groups.get(0).getType().split("_")[0];
			//String departmentId = groups.get(0).getType().split("_")[1];
			dailyHours = attendanceService.getDailyHoursByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyIDString, departmentId, DateUtil.formateDate(new Date()));
			beginTime = " " + workTimeArray[0] + ":00";
			endTime = " " + workTimeArray[3] + ":00";
		} catch (Exception e) {
			errorMessage = "获取员工所在分公司作息时间失败！";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

		return "getWorkTimeByUserID";
	}

	public String getBankAccountByUserID() {
		String userID = request.getParameter("userID");
		try {
			bankAccountVO = reimbursementService.getBankAccountByUserID(userID);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工的打款账号失败！";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "getBankAccountByUserID";
	}

	public String findAllCompanys() {
		companyVOs = positionService.findAllCompanys();
		return "findAllCompanys";
	}

	public String findDepartmentsStaffList() {
		int companyID = Integer.parseInt(request.getParameter("companyID"));
		int departmentID = Integer.parseInt(request.getParameter("parentID"));
		departmentVOs = positionService.findDepartmentsByCompanyIDDepartmentID(companyID, departmentID);

		if (departmentID != 0) {
			staffVOs = staffService.findStaffsByCompanyIDDepartmentID(companyID, departmentID);
			request.setAttribute("staffVOs", staffVOs);
		}

		if (CollectionUtils.isEmpty(departmentVOs)) {
			errorMessage = "获取页面加载信息失败，请联系系统管理员！";
		}
		return "departmentStaffList";
	}

	/**
	 * 修改已分配任务
	 * 
	 * @return
	 */
	public String saveAssignment() {
		try {
			String processInstanceID = request.getParameter("processInstanceID");
			if (!StringUtils.isBlank(processInstanceID)) {
				// 执行人确认任务前，分配人修改已分配任务
				assignmentService.updateAssignment(processInstanceID, assignmentVO);
				type = BusinessTypeEnum.ASSIGNMENT.getValue();
				return "startAssignment";
			}

			String taskID = request.getParameter("taskID");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			// 修改任务参数
			assignmentService.updateAssignment(pInstance.getId(), assignmentVO);
			// 完成任务
			processService.completeTask(taskID, assignmentVO.getUserID(), TaskResultEnum.COMPLETE, "");
			// 更新任务分配业务表流程节点状态
			assignmentService.updateProcessStatus(pInstance.getId(), TaskResultEnum.COMPLETE);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "修改分配任务失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.ASSIGNMENT.getValue();
		return "taskComplete";
	}

	public String emailConfirm() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			// 完成确认
			processService.completeTask(taskID, user.getId(), TaskResultEnum.ASSIGNMENT_COMPLETED, comment);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.EMAIL.getValue();
		return "taskComplete";
	}

	/**
	 * 任务分配人验收任务完成情况
	 * 
	 * @return
	 */
	public String inspectAssignment() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		// String score = request.getParameter("score");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			/*
			 * if (!StringUtils.isBlank(score)) { //保存任务完成得分
			 * assignmentService.updateScore(pInstance.getId(),
			 * Float.parseFloat(score)); }
			 */
			// 完成确认
			processService.completeTask(taskID, user.getId(), TaskResultEnum.ASSIGNMENT_INSPECT, comment);
			// 更新OA_Assignment表的流程节点状态processStatus
			assignmentService.updateProcessStatus(pInstance.getId(), TaskResultEnum.ASSIGNMENT_INSPECT);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.ASSIGNMENT.getValue();
		return "taskComplete";
	}

	public String findStaffByName() {
		staffVOs = staffService.findStaffByName(request.getParameter("name"));
		return "findStaffByName";
	}

	public String findStaffByNameLimit20() {
		staffVOs = staffService.findStaffByName(request.getParameter("name"),20);
		return "findStaffByName";
	}

	public String findStaffByNameAndStatus() {
		staffVOs = staffService.findStaffByNameAndtStatus(request.getParameter("name"),
				Integer.parseInt(request.getParameter("positionCategory")));
		return "findStaffByNameAndStatus";
	}
	
	@Autowired
	private PartnerService partnerService;
	
	public void checkMeetCondition(){
		String userId = request.getParameter("userId");
		Map<String,String> resultMap = new HashMap<>();
		String result = null;
		PartnerEntity partnerEntity = partnerService.getPartnerEntityBy(userId);
		if(partnerEntity!=null){
			result = "0";//提示先退出合伙人
		}else{
			result = staffService.checkStaffMeetResignation(userId);
		}
		resultMap.put("result", result);
		printByJson(resultMap);
	}
	
	public String applyDimission (){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		/*PartnerEntity partnerEntity = partnerService.getPartnerEntityBy(user.getId());
		if(partnerEntity != null){
			selectedPanel = "newResignation";
			return "reminder";
		}else{*/
			StaffVO staff = staffService.getStaffByUserID(user.getId());
			request.setAttribute("staff", staff);
			
			/*ContractEntity contractEntity = contractService.queryContractEntityBy(user.getId());
			String dateDiff = staffService.getDayDiff(new Date(),contractEntity.getEndDate(),1);
			request.setAttribute("dateDiff", dateDiff);*/
			
			ContractEntity contractEntity = contractService.queryContractEntityBy(user.getId());
			StaffEntity staffEntity = staffService.getStaffByUserId(user.getId());
			String dateDiff = null;
			if(contractEntity==null){
				dateDiff = staffEntity.getStaffName()+"&nbsp;的合同不存在";
			}else{
				String dateDifferent = staffService.getDayDiff(new Date(),contractEntity.getEndDate(),1);
				dateDiff = "剩余&nbsp;"+dateDifferent;
			}
			request.setAttribute("dateDiff", dateDiff);
			selectedPanel = "newResignation";
			return "newResignation";
		/*}*/
	}

	public String gotoExitPartner(){
		request.getSession().setAttribute("isPartner", true);
		selectedPanel = "exitPartner";
		return "gotoExitPartner";
	}
	public String newFormal() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		request.setAttribute("staff", staff);
		selectedPanel = "newFormal";
		return "newFormal";
	}

	public String startFormal() {
		try {
			StaffVO staff = staffService.getStaffByUserID(formalVO.getRequestUserID());
			if (staff.getStatus() == 3 || staff.getStatus() == 4) {
				errorMessage = "该员工已转正！";
				return "error";
			}

			if (formalService.hasSubmitted(formalVO.getRequestUserID())) {
				errorMessage = "该转正申请已存在，请勿重复申请！";
				return "error";
			}

			formalService.startFormal(formalVO, FormalTypeEnum.APPLICATION.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "转正申请提交失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.FORMAL.getValue();
		return "startFormal";
	}

	public String attendanceDetail() {
		User user = (User) request.getSession().getAttribute("user");

		if (attendanceVO == null) {
			attendanceVO = new AttendanceVO();
		}
		attendanceVO.setUserID(user.getId());
		try {
			ListResult<AttendanceVO> attendanceVOListResult = attendanceService
					.findAttendancePageListByAttendanceVO(attendanceVO, page, limit);
			request.setAttribute("attendanceVOList", attendanceVOListResult.getList());
			int count = attendanceVOListResult.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "考勤明细查询失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "attendanceDetail";
		return "attendanceDetail";
	}

	public String processHistory() {
		try {
			String processInstanceID = request.getParameter("processInstanceID");
			List<CommentVO> comments = processService.getCommentsByProcessInstanceID(processInstanceID);
			List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceID);
			request.setAttribute("comments", comments);
			request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "processHistory";
	}

	public String structure() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
		List<GroupDetailVO> groupDetails = positionService.findGroupDetailsByGroups(groups);
		List<DepartmentFrameVO> departmentFrameVOs = new ArrayList<DepartmentFrameVO>();
		for (GroupDetailVO groupDetail : groupDetails) {

			DepartmentVO department = positionService.findDepartmentsByDempartmentID(groupDetail.getDepartmentID());
			DepartmentVO upDepartment = positionService.findDepartmentsByDempartmentID(department.getParentID());
			if (upDepartment == null) {
				upDepartment = department;
			}
			DepartmentFrameVO departmentFrameVO = new DepartmentFrameVO();
			int companyID = positionService.getCompanyIDByDepartmentID(upDepartment.getDepartmentID());
			if (upDepartment.getDepartmentName().equals("管理层") || upDepartment.getDepartmentName().equals("总经办")) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDDepartmentID(companyID, 0);
				departmentFrameVO.setDepartmentVOs(departmentVOs);
				CompanyVO company = positionService.getCompanyByCompanyID(companyID);
				departmentFrameVO.setCompanyVO(company);
			} else {
				CompanyVO company = positionService.getCompanyByCompanyID(companyID);
				departmentFrameVO.setCompanyVO(company);
				departmentFrameVO.setDepartmentVO(upDepartment);
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByParentID(upDepartment.getDepartmentID());
				departmentFrameVO.setDepartmentVOs(departmentVOs);
				List<PositionVO> positionVOs = positionService
						.findPositionsByDepartmentID(upDepartment.getDepartmentID());
				departmentFrameVO.setPositionVOs(positionVOs);
			}
			departmentFrameVOs.add(departmentFrameVO);

		}

		request.setAttribute("departmentFrameVOs", departmentFrameVOs);

		selectedPanel = "structure";
		return "structure";
	}

	public String showDepartmentsPositions() {
		int departmentID = Integer.valueOf(request.getParameter("departmentID"));
		departmentVOs = positionService.findDepartmentsByParentID(departmentID);
		positionVOs = positionService.findPositionsByDepartmentID(departmentID);

		return "showDepartmentsPositions";
	}
	public void addDept(){
		positionService.addDepartment(
				request.getParameter("name"),
				request.getParameter("id"),
				request.getParameter("companyId"), 
				request.getParameter("level"));
		returnSuccess();
	}

	public void addPosition(){
		positionService.addPostion(
				request.getParameter("name"),
				request.getParameter("id"),
				request.getParameter("companyId"),
				request.getParameter("positionType"));
		returnSuccess();
	}
	public void showStaff() {

		int departmentID = Integer.valueOf(request.getParameter("departmentID"));
		int positionID = Integer.valueOf(request.getParameter("positionID"));
		GroupDetailVO groupDetailVO = positionService.findGroudetailByDepartmentIDPositionID(departmentID, positionID);
		List<User> users = identityService.createUserQuery().memberOfGroup(groupDetailVO.getGroupID()).list();
		List<StaffVO> staffVOs = new ArrayList<>();
		for (User user : users) {
			if (StringUtils.isBlank(user.getLastName()) && StringUtils.isBlank(user.getPassword())) {
				continue;
			}
			StaffVO staff = staffService.getStaffByUserID(user.getId());
			staffVOs.add(staff);
		}
		printByJson(staffVOs);
	}

	public String newWorkReport() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		Calendar rightNow = Calendar.getInstance();
		String date = DateUtil.formateDate(rightNow.getTime());
		;
		List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
		Integer companyID = groups.get(0).getCompanyID();
		String rightNow1 = DateUtil.formateFullDate(rightNow.getTime());
		/*
		 * String beginTime =
		 * date+positionService.getBeginTimeByCompanyID(CompanyIDEnum.valueOf(
		 * companyID));
		 */
		//String workTimes = CompanyIDEnum.valueOf(companyID).getTimeLimitByDate(null);
		//String[] workTimeArray = workTimes.split(" ");
		String departmentId = groups.get(0).getDepartmentID()+"";
		try {
			String[] workTimeArray = attendanceService.getWorkRestTimeByCompanyIDOrDepartmentId(companyID+"", departmentId, DateUtil.formateDate(new Date()));
			beginTime = " " + workTimeArray[0] + ":00";
			endTime = " " + workTimeArray[3] + ":00";
			String beginTime = date + " " + workTimeArray[0] + ":00";
			String[] weekOfDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
			String weekDay = "";
			if (DateUtil.getFullDate(rightNow1).compareTo(DateUtil.getFullDate(beginTime)) >= 0) {
				date = DateUtil.formateDate(rightNow.getTime());
				weekDay = weekOfDays[rightNow.get(Calendar.DAY_OF_WEEK) - 1];
			} else {
				rightNow.add(Calendar.DATE, -1);
				date = DateUtil.formateDate(rightNow.getTime());
				weekDay = weekOfDays[rightNow.get(Calendar.DAY_OF_WEEK) - 1];

			}
			request.setAttribute("staff", staff);
			request.setAttribute("date", date);
			request.setAttribute("weekDay", weekDay);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取员工所在分公司作息时间失败！";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "workReportDetail";
		return "newWorkReport";
	}
	public String newWeekReport(){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		Calendar rightNow = Calendar.getInstance();
		String today = DateUtil.formateDate(rightNow.getTime());
		request.setAttribute("date", today);
		request.setAttribute("staff", staff);
		try {
			request.setAttribute("disabled", weekWorkReportService.checkCanWriteReport(user.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "weekReportDetail";
		return "newWeekReport";
	}

	public String savaWorkReport() {

		workReportDetailVO = new WorkReportDetailVO();
		workReportDetailVO.setUserID(workReportVO.getUserID());
		workReportDetailVO.setReportDate(workReportVO.getReportDate());
		ListResult<WorkReportDetailVO> workReportDetailVOListResult = workReportService
				.findWorkReportListByUserID(workReportDetailVO, page, limit);
		if (workReportDetailVOListResult.getTotalCount() != 0) {
			printByJson("error");
		} else {
			try {
				workReportService.saveWorkReport(workReportVO);
				//自动签到
				SigninVO signin = new SigninVO();
				signin.setUserID(workReportVO.getUserID());
				signin.setSigninDate(workReportVO.getReportDate());
				signinService.saveSignin(signin);
			} catch (Exception e) {
				printByJson("errorMessage");
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				logger.error(sw.toString());
			}
		}
		return "addWorkReport";
	}

	public String workReportDetail() {
		try {
			User user = (User) request.getSession().getAttribute("user");
			if (workReportDetailVO == null) {
				workReportDetailVO = new WorkReportDetailVO();
			}
			workReportDetailVO.setUserID(user.getId());
			ListResult<WorkReportDetailVO> workReportDetailVOListResult = workReportService
					.findWorkReportListByUserID(workReportDetailVO, page, limit);
			request.setAttribute("workReportDetailVOs", workReportDetailVOListResult.getList());
			int count = workReportDetailVOListResult.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询工作列表失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "workReportDetail";
		return "workReportDetail";

	}
	public String weekReportDetail() {
		try {
			User user = (User) request.getSession().getAttribute("user");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			List<WeekReportEntity> weekReportList = weekWorkReportService.getWeekReportList(user.getId(), beginDate, endDate);
			int count = weekReportList.size();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			request.setAttribute("weekReportList", ActionUtil.page(page, limit, weekReportList));
			request.setAttribute("disabled", weekWorkReportService.checkCanWriteReport(user.getId()));
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询工作周报列表失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "weekReportDetail";
		return "weekReportDetail";

	}

	public String  underlingWorkReportDetail(){
		if(workReportDetailVO==null){
			workReportDetailVO=new WorkReportDetailVO();						
		}
		try{
			User user = (User) request.getSession().getAttribute("user");
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
			Integer departmentId=-1;
			Integer companyId=-1;
			String groupIndex=request.getParameter("groupIndex");
			request.setAttribute("groupIndex", groupIndex);
			int  index=0;
			ListResult<WorkReportDetailVO> workReportDetailVOListResult=null;

			List<String[]> companyDetail=new ArrayList<String[]>();
			for (GroupDetailVO groupDetailVO : groups) {
				if("主管".equals(groupDetailVO.getPositionName())){
					companyDetail.add(new String[]{groupDetailVO.getCompanyName(),groupDetailVO.getDepartmentName(),index+""});
				}
				index++;
			}
			request.setAttribute("companyDetail", companyDetail);

			index=0;
			if(StringUtils.isBlank(groupIndex)){
				for (GroupDetailVO groupDetailVO : groups) {
					if("主管".equals(groupDetailVO.getPositionName())){
						departmentId=groupDetailVO.getDepartmentID();
						companyId=groupDetailVO.getCompanyID();
						break;
					}
					index++;
				}
				workReportDetailVO.setDepartmentID(departmentId);
				workReportDetailVO.setCompanyID(companyId);
			}else{
				for (GroupDetailVO groupDetailVO : groups) {
					if("主管".equals(groupDetailVO.getPositionName())&&groupIndex.equals(index+"")){
						departmentId=groupDetailVO.getDepartmentID();
						companyId=groupDetailVO.getCompanyID();
						break;
					}
					index++;
				}
				workReportDetailVO.setDepartmentID(departmentId);
				workReportDetailVO.setCompanyID(companyId);
			}

			if(companyId!=-1){
				workReportDetailVOListResult=workReportService.findWorkReportListByUserID(workReportDetailVO,page,limit);
			}else{
				workReportDetailVOListResult=new ListResult<WorkReportDetailVO>(new ArrayList<WorkReportDetailVO>(), 0);
			}
			request.setAttribute("workReportDetailVOs", workReportDetailVOListResult.getList());
			int count=workReportDetailVOListResult.getTotalCount();
			totalCount = count;
			totalPage = count%limit==0 ? count/limit : count/limit+1;
		}catch(Exception e){
			errorMessage = "查询工作列表失败："+e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "underlingWorkReportDetail";
		return "underlingWorkReportDetail";
	}

	public String getVacationAttachment() {
		String taskID = request.getParameter("taskID");
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		if (processInstance != null) {
			List<Attachment> attachments = taskService.getProcessInstanceAttachments(processInstance.getId());
			if (attachments.size() > 0) {
				vacationPicture = taskService.getAttachmentContent(attachments.get(0).getId());
			}
		}
		return "vacationAttachment";
	}
	public String getAttachmentByAttachmentId(){
		String attachmentId = request.getParameter("attachmentId");
		vacationPicture =  taskService.getAttachmentContent(attachmentId);
		return "vacationAttachment";
	}
	@Getter
	@Setter
	private Integer index;
	@Setter
	@Getter
	private String downloadFileFileName;

	public String getVacationAttachmentAll() {
		String taskID = request.getParameter("taskID");
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		if (processInstance != null) {
			List<Attachment> attachments = taskService.getProcessInstanceAttachments(processInstance.getId());
			if (index == null)
				index = 0;
			if (attachments.size() > 0) {
				vacationPicture = taskService.getAttachmentContent(attachments.get(index).getId());
				try {
					downloadFileFileName = new String(attachments.get(index).getName().getBytes("gbk"),"iso-8859-1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return "vacationAttachmentAll";
	}

	public String getVacationAttachmentAll_() {
		String instanceId = request.getParameter("processInstanceID");
		if (instanceId != null) {
			List<Attachment> attachments = taskService.getProcessInstanceAttachments(instanceId);
			if (index == null)
				index = 0;
			if (attachments.size() > 0) {
				vacationPicture = taskService.getAttachmentContent(attachments.get(index).getId());
				try {
					downloadFileFileName = new String(attachments.get(index).getName().getBytes("gbk"),"iso-8859-1");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return "vacationAttachmentAll";
	}

	public String workReportOver() {
		try {
			String reportDate = request.getParameter("reportDate");
			User user = (User) request.getSession().getAttribute("user");
			List<WorkReportVO> workReportVOs = workReportService.findWorkReportByDateAndUserID(reportDate,
					user.getId());
			List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
			List<String> groupList = Lists2.transform(groups, new SafeFunction<Group, String>() {
				@Override
				protected String safeApply(Group input) {
					String[] positionIDs = input.getType().split("_");
					String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
					String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
							.getDepartmentName();
					String positionName = positionService.getPositionByPositionID(Integer.parseInt(positionIDs[2]))
							.getPositionName();
					return companyName + " — " + departmentName + " — " + positionName;
				}
			});
			request.setAttribute("groupList", groupList);
			request.setAttribute("workReportVOs", workReportVOs);
			request.setAttribute("reportDate", reportDate);
			request.setAttribute("userName", staffService.getStaffByUserID(user.getId()).getLastName());
			request.setAttribute("isPartner", staffService.isPartner(user.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		selectedPanel = "workReportDetail";
		return "workReportOver";
	}

	public String auditReimbursement() {
		String taskID = request.getParameter("taskID");
		ReimbursementVO reimbursementVO = reimbursementService.getReimbursementVOByTaskID(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		List<Group> groups = identityService.createGroupQuery().groupMember(reimbursementVO.getRequestUserID()).list();
		if (groups.size() > 0) {
			String[] positionIDs = groups.get(0).getType().split("_");
			String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
			String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
					.getDepartmentName();
			request.setAttribute("companyName", companyName);
			request.setAttribute("departmentName", departmentName);
		}
		// 单子上的两个流程走到才能确定的姓名 记录在实体类上
		ReimbursementVO reimbursementVO2 = reimbursementService
				.getReimbursementVOByProcessInstanceID(processInstance.getId());
		reimbursementVO.setShowPerson1(reimbursementVO2.getShowPerson1());
		reimbursementVO.setShowPerson3(reimbursementVO2.getShowPerson3());
		request.setAttribute("attas", attas);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		selectedPanel = "findTaskList";
		return "auditReimbursement";
	}

	public String auditAdvance() {
		String taskID = request.getParameter("taskID");
		AdvanceVo reimbursementVO = reimbursementService.getAdvanceVOByTaskID(taskID);
		ProcessInstance processInstance = processService.getProcessInstance(taskID);
		List<CommentVO> comments = processService.getComments(taskID);
		Task task = taskService.createTaskQuery().taskId(taskID).singleResult();
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstance.getId());
		List<Group> groups = identityService.createGroupQuery().groupMember(reimbursementVO.getRequestUserID()).list();
		if (groups.size() > 0) {
			String[] positionIDs = groups.get(0).getType().split("_");
			String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
			String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
					.getDepartmentName();
			request.setAttribute("companyName", companyName);
			request.setAttribute("departmentName", departmentName);
		}
		List<Attachment> attas = taskService.getProcessInstanceAttachments(processInstance.getId());
		List<Attachment> invoiceAttas = new ArrayList<>();
		// 单子上的两个流程走到才能确定的姓名 记录在实体类上
		AdvanceVo reimbursementVO2 = reimbursementService.geAdvanceTaskVOByProcessInstanceID(processInstance.getId());
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
		reimbursementVO.setShowPerson3(reimbursementVO2.getShowPerson3());
		reimbursementVO.setPayeeName(reimbursementVO2.getPayeeName());
		request.setAttribute("attas", attas);
		request.setAttribute("invoiceAttas", invoiceAttas);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		selectedPanel = "findTaskList";
		return "auditAdvance";
	}
	public String auditPayment() {
		String taskID = request.getParameter("taskID");
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
			String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
					.getDepartmentName();
			request.setAttribute("companyName", companyName);
			request.setAttribute("departmentName", departmentName);
		}
		// 单子上的两个流程走到才能确定的姓名 记录在实体类上
		PaymentVo reimbursementVO2 = reimbursementService.gePaymentTaskVOByProcessInstanceID(processInstance.getId());
		reimbursementVO.setShowPerson1(reimbursementVO2.getShowPerson1());
		reimbursementVO.setShowPerson3(reimbursementVO2.getShowPerson3());
		reimbursementVO.setPayeeName(reimbursementVO2.getPayeeName());
		request.setAttribute("attas", attas);
		request.setAttribute("attachmentSize", attas.size());
		request.setAttribute("taskDefKey", task.getTaskDefinitionKey());
		request.setAttribute("reimbursementVO", reimbursementVO);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("taskID", taskID);
		getAndSetLeaderMsg(finishedTaskVOs, comments);
		selectedPanel = "findTaskList";
		return "auditPayment";
	}
	/**
	 * 将2个步骤的审批意见和结果提取出来塞入表单
	 * 
	 * @param finishedVos
	 * @param comments
	 */
	private void getAndSetLeaderMsg(List<TaskVO> finishedVos, final List<CommentVO> comments) {
		Function<String, String> getCommentBytaskId = new Function<String, String>() {
			@Override
			public String apply(String taskId) {
				if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(comments)
						&& StringUtils.isNotBlank(taskId)) {
					for (CommentVO commentVO : comments) {
						if (taskId.equals(commentVO.getTaskID())) {
							return commentVO.getContent();
						}
					}
				}
				return "";
			}
		};
		String dept_leader_msg = "";
		if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(finishedVos)) {
			for (TaskVO taskVO : finishedVos) {
				String taskName = taskVO.getTaskName();
				if ("主管审批".equals(taskName) || "总经理审批".equals(taskName) || "分公司负责人".equals(taskName)
						|| "分公司总经理审批".equals(taskName)) {
					String resultMsg = "【" + taskName + "】	" + taskVO.getAssigneeName() + ":";
					resultMsg += (taskVO.getResult() == null) ? "" : taskVO.getResult() + ":";
					resultMsg += getCommentBytaskId.apply(taskVO.getTaskID());
					dept_leader_msg += resultMsg + "</br>";
				}
				if ("公司总经理审批".equals(taskName)) {
					String resultMsg = "【" + taskName + "】	" + taskVO.getAssigneeName() + ":";
					resultMsg += (taskVO.getResult() == null) ? "" : taskVO.getResult() + ":";
					resultMsg += getCommentBytaskId.apply(taskVO.getTaskID());
					request.setAttribute("company_leader_msg", resultMsg);
				}
			}
		}
		request.setAttribute("dept_leader_msg", dept_leader_msg);
	}

	public String findNoticeList() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		try {
			ListResult<NoticeVO> noticeList = noticeService.findNoticesByUserID(user.getId(), limit, page);
			int count = noticeList.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("noticeVOs", noticeList.getList());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "消息查询失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "findNoticeList";
		return "noticeList";

	}
	@Getter
	private List<CommonAttachment> attachments;
	public String updateNoticeActor() {
		Integer ntcID = Integer.parseInt(request.getParameter("ntcID"));
		String userID = request.getParameter("userID");
		try {
			noticeActorService.updateNoticeActor(ntcID, userID);
			noticeVO = noticeService.getNoticeByNtcID(ntcID);
			attachments = noticeService.getCommonAttachmentByFID(ntcID, AttachmentType.NOTICE);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "更新失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

		return "updateNoticeActor";
	}
	public String home() {
		User user = (User) request.getSession().getAttribute("user");
		
		//获得当前时期的xx月xx日
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
		Date date = new Date();
		String whatsTheDate = dateFormat.format(date);
		request.setAttribute("whatsTheDate", whatsTheDate);
		//获得当前时期是星期几
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEEE",Locale.CHINA);
		String dayOfTheWeek = dateFormat2.format(date);
		request.setAttribute("dayOfTheWeek", dayOfTheWeek);
		//获得入职天数:
		StaffEntity staff = staffService.getStaffByUserId(user.getId());
		Date entrydate = staff.getEntryDate();
		String dayDiff = staffService.getDayDiff(entrydate, new Date(),0);
		request.setAttribute("dayDiff", dayDiff);
		
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setType(1);
		ListResult<NoticeVO> noticeList = noticeService.findNoticeList1(noticeVO, 5, 0);
		ListResult<NoticeVO> messageList = noticeService.findNoticesByUserID(user.getId(), 5, 0);
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(user.getId()).orderByTaskCreateTime().desc()
				.listPage(0, 5);
		List<TaskVO> taskVOs = processService.createTaskVOList(taskList);
		List<GroupDetailVO> groupDetailVOs = staffService.findGroupDetailsByUserID(user.getId());

		List<SalaryDetailEntity> softGroupEntities = staffService.getSalarys(user.getId());

		List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
		if (!CollectionUtils.isEmpty(groups)) {
			request.setAttribute("userDept", groups.get(0).getDepartmentName());
			//存在多个职位，以总部的职位优先
			GroupDetailVO group = null;
			for(GroupDetailVO _group: groups){
				group = _group;
				if(CompanyIDEnum.QIAN.getValue() == group.getCompanyID()){
					break;
				}
			}
			String companyId = String.valueOf(group.getCompanyID());
			//迟到次数统计信息
			List<Object> lateObjs = attendanceService.getLateOrLeaveNumInfo(companyId, Constants.LATE);
			//早退次数统计信息
			//List<Object> leaveObjs = attendanceService.getLateOrLeaveNumInfo(companyId, Constants.LEAVE);
			request.setAttribute("lateObjs", lateObjs);
			//request.setAttribute("leaveObjs", leaveObjs);
			request.setAttribute("companyId", companyId);
		}
		request.setAttribute("noticeVOs", noticeList.getList());
		request.setAttribute("userName", staffService.getRealNameByUserId(user.getId()));
		request.setAttribute("messageVOs", messageList.getList());
		request.setAttribute("taskVOs", taskVOs);
		request.setAttribute("groupDetailVOs", groupDetailVOs);
		request.setAttribute("softGroupEntities", softGroupEntities);
		if("true".equalsIgnoreCase(request.getParameter("showVersionNotice"))){
			try {
				VersionFuncionInfo versionFunctionInfo = versionInfoService.getLatestVersionFunctionInfo();
				request.setAttribute("versionFunctionInfo", versionFunctionInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("showVersionNotice", true);
		}else{
			request.setAttribute("showVersionNotice", false);
		}
/*		//判断有没有绑定微信（openId）
		StaffVO staff = staffService.getStaffByUserID(user.getId());
		if(StringUtils.isBlank(staff.getOpenId())){
			request.setAttribute("bind", false);
		}*/
		//request.setAttribute("firstLogin", request.getParameter("firstLogin"));
		return "home";
	}

	public void confirmSalary() {
		String id = request.getParameter("id");
		SalaryDetailEntity salaryDetailEntity = staffService.getSaleryById(id);
		salaryDetailEntity.setStatus(2);
		staffService.saveSalarys(salaryDetailEntity);

	}

	public void againestSalary() {
		User user = (User) request.getSession().getAttribute("user");
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		SalaryDetailEntity salaryDetailEntity = staffService.getSaleryById(id);
		Map<String, Integer> result = new HashMap<String, Integer>();
		if (salaryDetailEntity != null && (salaryDetailEntity.getUserId().equals(user.getId()))) {
			salaryDetailEntity.setStatus(1);
			salaryDetailEntity.setContent(content);
			staffService.saveSalarys(salaryDetailEntity);
			result.put("success", 1);
		} else {
			result.put("success", 0);

		}
		printByJson(result);
	}

	public String auditSocialSecurity() {
		String taskID = request.getParameter("taskID");
		try {
			String processInstanceID = processService.getProcessInstance(taskID).getId();
			SocialSecurityProcessVO socialSecurityProcessVO = socialSecurityService
					.getSocialSecurityProcessByProcessInstanceID(processInstanceID);
			List<SocialSecurityVO> socialSecurityVOs = socialSecurityService
					.findSocialSecurityListByProcessID(socialSecurityProcessVO.getSspID());
			for (SocialSecurityVO socialSecurityVO : socialSecurityVOs) {
				socialSecurityVO.setUserName(staffService.getStaffByUserID(socialSecurityVO.getUserID()).getLastName());
			}
			request.setAttribute("socialSecurityVOs", socialSecurityVOs);
			List<HousingFundVO> housingFundVOs = socialSecurityService
					.findHousingFundListByProcessID(socialSecurityProcessVO.getSspID());
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
			errorMessage = "获取审核信息失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		request.setAttribute("taskID", taskID);
		selectedPanel = "findTaskList";
		return "auditSocialSecurity";
	}

	public String showVacationAttachment() {
		String taskId = request.getParameter("taskID");
		ProcessInstance pInstance = processService.getProcessInstance(taskId);
		List<Attachment> attas = taskService.getProcessInstanceAttachments(pInstance.getId());
		request.setAttribute("attas", attas);
		request.setAttribute("taskId", taskId);
		return "showVacationAttachment";
	}
	public String underlingSalaryDetail(){
		selectedPanel = "underlingSalaryDetail";
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		Calendar calendar=Calendar.getInstance();
		if(StringUtils.isBlank(year)){
			year = (calendar.get(Calendar.YEAR))+"";
		}
		if(StringUtils.isBlank(month)){
			month = (calendar.get(Calendar.MONTH))+"";
		}else if(month.startsWith("0")){

			month = month.substring(1, month.length());
		}
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		User user = (User) request.getSession().getAttribute("user");
		List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
		//下属员工的工资集合
		List<SalaryDetailEntity> underlingSalaryList = new ArrayList<>();
		//下属员工集合
		List<String> allUnderlings = new ArrayList<>();
		for (GroupDetailVO groupDetailVO : groups) {
			if("主管".equals(groupDetailVO.getPositionName())){
				List<String> underlings = new ArrayList<>();
				//部门下面的所有人员
				List<String> userList = staffService.getDepartmentStaffs(groupDetailVO.getCompanyID(), groupDetailVO.getDepartmentID(), underlings, 1, false);
				allUnderlings.addAll(userList);
			}else if("组长".equals(groupDetailVO.getPositionName())){
				List<String> underlings = new ArrayList<>();
				//部门下面的所有人员
				List<String> userList = staffService.getDepartmentStaffs(groupDetailVO.getCompanyID(), groupDetailVO.getDepartmentID(), underlings, 1, true);
				allUnderlings.addAll(userList);
			}
		}
		for(String userId: allUnderlings){
			List<SalaryDetailEntity> salaryDetailEntities = attendanceService.getSalaryByCondition(userId, month, year);
			if(null != salaryDetailEntities){
				underlingSalaryList.addAll(salaryDetailEntities);
			}
		}

		count = underlingSalaryList.size();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page-1)*limit);
		try {
			request.setAttribute("lists", ActionUtil.page(page, limit, underlingSalaryList));
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "underlingSalaryDetail";
	}
	public String toChopListPage() {
		selectedPanel = "newChopDestroy";
		try {
			String name = request.getParameter("name");
			ListResult<Chop> chops = chopService.getChopByName(name, null, page, limit);
			request.setAttribute("name", name);
			int count = chops.getTotalCount();
			totalCount = count;
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page - 1) * limit);
			request.setAttribute("chops", chops.getList());
			return "newChopDestroyList";
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
	}
	public String handOverChop() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			processService.completeTask(taskID, user.getId(), TaskResultEnum.HAND_OVER_CHOP, comment);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CHOP_DESTROY.getValue();
		return "taskComplete";
	}
	@Autowired
	private AssetService assetService;

	public String completeTransfer() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			processService.completeTask(taskID, user.getId(), TaskResultEnum.COMPLETE_TRANSFER, comment);
			//更新资产信息
			String assetId = request.getParameter("assetId");
			String newCompanyName = request.getParameter("newCompanyName");
			assetService.updateAssetCompany(assetId, newCompanyName);
			transferPropertyService.updateTransferPropertyProcessStatus(TaskResultEnum.COMPLETE_TRANSFER, pInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.TRANSFER_PROPERTY.getValue();
		return "taskComplete";
	}
	public void purchaseConfirm() {
		Map<String, String> resultMap = new HashMap<>();
		User user = (User) request.getSession().getAttribute("user");
		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			String taskDefKey = request.getParameter("taskDefKey");
			// 完成任务
			if("budgetConfirm".equals(taskDefKey)){
				String propertyType = request.getParameter("propertyType");
				String propertyNum = request.getParameter("propertyNum");
				String useTime = request.getParameter("useTime");
				String netSalvageRate = request.getParameter("netSalvageRate");

				ProcessInstance pInstance = processService.getProcessInstance(taskID);
				PurchasePropertyVo purchasePropertyVo = (PurchasePropertyVo)runtimeService.getVariable(pInstance.getId(), "arg");
				purchasePropertyVo.setPropertyType(propertyType);
				purchasePropertyVo.setPropertyNum(propertyNum);
				purchasePropertyVo.setUseTime(useTime);
				purchasePropertyVo.setNetSalvageRate(netSalvageRate);
				purchasePropertyVo.setProcessInstanceID(pInstance.getId());
				runtimeService.setVariable(pInstance.getId(), "arg", purchasePropertyVo);
				processService.completeTask(taskID, user.getId(), TaskResultEnum.BUDGET_CONFIRM, comment);
				//更新业务表的数据
				purchasePropertyService.updatePurchasePropertyForBudget(purchasePropertyVo);
			}else{
				String productName = request.getParameter("productName");
				String _model = request.getParameter("_model");
				String _number = request.getParameter("_number");
				String unitPrice = request.getParameter("unitPrice");
				String purchaserCheckResult = request.getParameter("purchaserCheckResult");
				ProcessInstance pInstance = processService.getProcessInstance(taskID);
				PurchasePropertyVo purchasePropertyVo = (PurchasePropertyVo)runtimeService.getVariable(pInstance.getId(), "arg");
				purchasePropertyVo.setProductName(productName);
				purchasePropertyVo.set_model(_model);
				purchasePropertyVo.set_number(_number);
				purchasePropertyVo.setUnitPrice(unitPrice);
				purchasePropertyVo.setPurchaserCheckResult(purchaserCheckResult);
				purchasePropertyVo.setProcessInstanceID(pInstance.getId());
				runtimeService.setVariable(pInstance.getId(), "arg", purchasePropertyVo);
				processService.completeTask(taskID, user.getId(), TaskResultEnum.PURCHASE_CONFIRM, comment);
				purchasePropertyService.updatePurchaseProperty(purchasePropertyVo);
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			resultMap.put("error", errorMessage);
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

		type = BusinessTypeEnum.PURCHASE_PROPERTY.getValue();
		resultMap.put("type", type+"");
		printByJson(resultMap);
	}
	public String propertySign(){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			processService.completeTask(taskID, user.getId(), TaskResultEnum.PURCHASE_SIGN, comment);
			purchasePropertyService.updateProcessStatus(TaskResultEnum.PURCHASE_SIGN, pInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.PURCHASE_PROPERTY.getValue();
		return "taskComplete";
	}
	public String handleSuccess(){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			processService.completeTask(taskID, user.getId(), TaskResultEnum.HANDLE_SUCCESS, comment);
			shopApplyService.updateShopPayApplyProcessStatus(TaskResultEnum.HANDLE_SUCCESS, pInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.SHOP_PAY_APPLY.getValue();
		return "taskComplete";
	}
	public String completeHandle(){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			String businessKey = request.getParameter("businessKey");
			String expiredTime = request.getParameter("expiredTime");
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			processService.completeTask(taskID, user.getId(), TaskResultEnum.COMPLETE_HANDLE, comment);
			if(Constants.SHOP_APPLY.equals(businessKey)){
				shopApplyService.updateShopApplyProcessStatus(TaskResultEnum.COMPLETE_HANDLE, pInstance.getId());
				type = BusinessTypeEnum.SHOP_APPLY.getValue();
			}else if(Constants.SHOP_PAY_APPLY.equals(businessKey)){
				shopApplyService.updateShopPayApplyProcessStatus(TaskResultEnum.COMPLETE_HANDLE, pInstance.getId());
				if(StringUtils.isNoneBlank(expiredTime)){
					shopApplyService.updateShopPayApplyExpiredTime(expiredTime, pInstance.getId());
				}
				type = BusinessTypeEnum.SHOP_PAY_APPLY.getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "taskComplete";
	}
	public String financialOpenAccount(){
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String publicBankAccount = request.getParameter("publicBankAccount");
		String aliPayAccount = request.getParameter("aliPayAccount");
		String aliPayPhone = request.getParameter("aliPayPhone");
		String shopType = request.getParameter("shopType");
		String applyType = request.getParameter("applyType");
		ProcessInstance pInstance = processService.getProcessInstance(taskID);
		if("开店".equals(applyType)){
			ShopApplyVo shopApplyVo = (ShopApplyVo)runtimeService.getVariable(pInstance.getId(), "arg");
			shopApplyVo.setPublicBankAccount(publicBankAccount);
			if("企业开店".equals(shopType)){
				shopApplyVo.setCompanyAliPayAccount(aliPayAccount);
				shopApplyVo.setCompanyAliPayPhone(aliPayPhone);
			}else{
				shopApplyVo.setPrivateAliPayAccount(aliPayAccount);
				shopApplyVo.setPrivateAliPayPhone(aliPayPhone);
			}
			shopApplyService.updateShopApplyInfo(shopType, aliPayAccount, aliPayPhone, publicBankAccount, pInstance.getId());
			runtimeService.setVariable(pInstance.getId(), "arg", shopApplyVo);
		}
		try {
			processService.completeTask(taskID, user.getId(), TaskResultEnum.COMPLETE_HANDLE, comment);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		type = BusinessTypeEnum.SHOP_APPLY.getValue();
		return "taskComplete";
	}
	public String completeDestroy() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "error";
		}

		try {
			String taskID = request.getParameter("taskID");
			String comment = request.getParameter("comment");
			ProcessInstance processInstance = processService.getProcessInstance(taskID);
			if(attachment != null){
				int index = 0;
				for(File file: attachment){
					InputStream is = new FileInputStream(file);
					taskService.createAttachment("picture", taskID, processInstance.getId(),
							attachmentFileName[index], "", is);	
					index++;
				}
			}
			// 完成确认
			processService.completeTask(taskID, user.getId(), TaskResultEnum.COMPLETE_DESTROY, comment);
			chopService.updateChopDestroyProcessStatus(TaskResultEnum.COMPLETE_DESTROY, processInstance.getId());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "处理失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

		type = BusinessTypeEnum.CHOP_DESTROY.getValue();
		return "taskComplete";
	}
	/**
	 * 检查人员是否有未完结的离职申请
	 */
	public void checkIsApplyQuit(){
		Map<String, String> resultMap = new HashMap<>();
		String applyUserId = request.getParameter("applyUserId");
		if(staffService.checkIsApplyQuit(applyUserId)){
			resultMap.put("quit", "true");
		}else{
			resultMap.put("quit", "false");
		}
		printByJson(resultMap);
	}
	@Getter
	private int weekReportId;
	public String saveWeekReport(){
		try {
			if(!weekWorkReportService.checkRepeatedReport(weekReport.getUserId())){
				errorMessage = "本周已提交工作周报，无需重复提交";
				return "error";
			}
			weekReportId = weekWorkReportService.saveWeekReport(weekReport, thisWeekWorkVo, riskVo, nextWeekWorkPlan, weekWorkSummary);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "weekReportDetail";
		return "showWeekReport";
	}
	public String weekReportOver(){
		try {
			String weekReportId = request.getParameter("weekReportId");
			String reporter = request.getParameter("reporter");
			User user = (User) request.getSession().getAttribute("user");
			WeekReportEntity weekReport = weekWorkReportService.getWeekReportDetail(weekReportId);
			ThisWeekWorkVo thisWeekWorkVos = (ThisWeekWorkVo) ObjectByteArrTransformer.toObject(weekReport.getThisWeekWorks());
			request.setAttribute("thisWeekWorkVos", thisWeekWorkVos);
			byte[] risks = weekReport.getRisks();
			if(null != risks){
				RiskVo riskVos =  (RiskVo) ObjectByteArrTransformer.toObject(risks);
				request.setAttribute("riskVos", riskVos);
			}
			byte[] nextWeekWorks = weekReport.getNextWorkPlans();
			if(null != nextWeekWorks){
				NextWeekWorkPlan nextWeekWorkPlans = (NextWeekWorkPlan) ObjectByteArrTransformer.toObject(nextWeekWorks);
				request.setAttribute("nextWeekWorkPlans", nextWeekWorkPlans);
			}
			if(StringUtils.isBlank(reporter)){
				reporter = user.getId();
			}
			List<Group> groups = identityService.createGroupQuery().groupMember(reporter).list();
			List<String> groupList = Lists2.transform(groups, new SafeFunction<Group, String>() {
				@Override
				protected String safeApply(Group input) {
					String[] positionIDs = input.getType().split("_");
					String companyName = CompanyIDEnum.valueOf(Integer.parseInt(positionIDs[0])).getName();
					String departmentName = positionService.getDepartmentByID(Integer.parseInt(positionIDs[1]))
							.getDepartmentName();
					String positionName = positionService.getPositionByPositionID(Integer.parseInt(positionIDs[2]))
							.getPositionName();
					return companyName + " — " + departmentName + " — " + positionName;
				}
			});
			request.setAttribute("weekReport", weekReport);
			request.setAttribute("groupList", groupList);
			request.setAttribute("userName", staffService.getStaffByUserID(reporter).getLastName());
			request.setAttribute("isPartner", staffService.isPartner(user.getId()));
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "weekReportDetail";
		return "weekReportOver";
	}
	public void calVacationTime(){
		User user = (User) request.getSession().getAttribute("user");
		String beginTime = request.getParameter("beginTime");
		String endTime = request.getParameter("endTime");
		try {
			String[] vacationTextAndHours = vacationService.getVacationTextAndHours(user.getId(), beginTime, endTime);
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("vacationTextAndHours", vacationTextAndHours);
			printByJson(resultMap);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
	/**
	 * 下属请假明细
	 * @return
	 */
	public String underlingVacationDetail(){
		try{
			String vacationDate = request.getParameter("vacationDate");
			User user = (User) request.getSession().getAttribute("user");
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(user.getId());
			List<Integer> departmentIds = new ArrayList<>();
			for (GroupDetailVO groupDetailVO : groups) {
				if("主管".equals(groupDetailVO.getPositionName())){
					departmentIds.add(groupDetailVO.getDepartmentID());
				}
			}
			List<VacationEntity> underlingVacationVos = vacationService.getUnderlingVacationVos(
					vacationDate, departmentIds, user.getId());
			request.setAttribute("underlingVacationVos", underlingVacationVos);
			request.setAttribute("vacationDate", vacationDate);
		}catch(Exception e){
			errorMessage = "查询下属请假列表失败："+e.toString();
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "underlingVacationDetail";
		return "underlingVacationDetail";
	}
	
	public String uploadAdvanceInvoice(){
		User user = (User) request.getSession().getAttribute("user");
		String taskID = request.getParameter("taskID");
		String comment = request.getParameter("comment");
		String uploadMode = request.getParameter("uploadMode");
		try {
			ProcessInstance pInstance = processService.getProcessInstance(taskID);
			int index = 0;
			List<String> attaIds = new ArrayList<>();
			for(File file: attachment){
				Attachment atta = taskService.createAttachment("", taskID, pInstance.getId(), attachmentFileName[index], "", new FileInputStream(file));
				attaIds.add(atta.getId());
				index++;
			}
			if(StringUtils.isNotBlank(uploadMode)){
				AdvanceEntity advance = reimbursementService.getAdvanceEntityByPInstanceId(pInstance.getId());
				String invoiceAttaIdStr = advance.getInvoiceAttaIds();
				List<String> invoiceAttaIds  = Lists.newArrayList(Arrays.asList(invoiceAttaIdStr.split(",")));
				if(Constants.ADD.equals(uploadMode)){
					invoiceAttaIds.addAll(attaIds);
					attaIds = invoiceAttaIds;
				}else if(Constants.REPLACE.equals(uploadMode)){
					for(String invoiceAttaId: invoiceAttaIds){
						taskService.deleteAttachment(invoiceAttaId);
					}
				}
			}
			reimbursementService.updateAdvanceInvoiceIds(StringUtils.join(attaIds, ","), pInstance.getId());
			// 完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.AGREE, comment);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = "处理失败：" + e.getMessage();
			return "error";
		}
		type = BusinessTypeEnum.ADVANCE.getValue();
		return "taskComplete";
	}
	public String showImage(){
		String attaId = request.getParameter("attaId");
		inputStream = taskService.getAttachmentContent(attaId);
		return "imgStream";
	}
	@Getter
	private boolean fromPersonal;
	public String addProblemOrder(){
		fromPersonal = true;
		int projectId = softPerformanceService.getProjectIdByName(Constants.OA);
		request.setAttribute("projectId", projectId);
		User user = (User)request.getSession().getAttribute("user");
		request.setAttribute("questionerId", user.getId());
		StaffVO staffVo = staffService.getStaffByUserID(user.getId());
		request.setAttribute("questionerName", staffVo.getLastName());
		return "addProblemOrder";
	}
	public String auditWeekMeetingReport(){
		String taskID = request.getParameter("taskID");
		String processInstanceId = request.getParameter("processInstanceId");
		List<CommentVO> comments = processService.getComments(taskID);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(processInstanceId);
		MorningMeetingEntity morningMeetingEntity = morningMeetingReportService.getMorningMeetingByInstanceId(processInstanceId);
		MorningMeetingVo morningMeetingVo = (MorningMeetingVo) CopyUtil.tryToVo(morningMeetingEntity, MorningMeetingVo.class);
		morningMeetingVo.setUserName(staffService.getStaffByUserID(morningMeetingVo.getUserID()).getLastName());
		morningMeetingVo.setTaskId(taskID);
		List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(morningMeetingVo.getUserID());
		if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)) {
			GroupDetailVO group = groupDetails.get(0);
			String department = group.getCompanyName() + "-" + group.getDepartmentName();
			morningMeetingVo.setDepartment(department);
		}
		String attachmentIdStr = morningMeetingVo.getAttachmentIds();
		List<CommonAttachment> attaList = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentIdStr)){
			String[] attachmentIds = attachmentIdStr.split(",");
			for(String attachmentId: attachmentIds){
				CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
				String suffix = attachment.getSuffix();
				//图片
				if(Constants.PIC_SUFFIX.contains(suffix)){
					attachment.setSuffix("png");
				}
				attaList.add(attachment);
			}
		}
		request.setAttribute("morningMeetingVo", morningMeetingVo);
		request.setAttribute("comments", comments);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("attaList", attaList);
		return "auditWeekMeetingReport";
	}
	public String auditMeetReport(){
		User user = (User) request.getSession().getAttribute("user");
		String taskID = request.getParameter("taskId");
		String processInstanceID = request.getParameter("processInstanceId");
		String comment = request.getParameter("comment");
		String result = request.getParameter("result");
		try {
			// 完成任务
			processService.completeTask(taskID, user.getId(), TaskResultEnum.valueOf(Integer.parseInt(result)), comment);
			morningMeetingReportService.updateProcessStatus(processInstanceID, result);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			errorMessage = "处理失败：" + e.getMessage();
			return "error";
		}
		type = BusinessTypeEnum.MORNING_MEETING.getValue();
		return "taskComplete";
	}
	public String toReportWeekMeeting(){
		StaffVO staff = staffService.getStaffByUserID(((User) request.getSession().getAttribute("user")).getId());
		request.setAttribute("staff", staff);
		request.setAttribute("weekDay", DateUtil.getWeekDay(new Date()));
		request.setAttribute("currentDate", DateUtil.formateFullDate(new Date()));
		request.setAttribute("taskId", request.getParameter("taskID"));
		request.setAttribute("processInstanceId", request.getParameter("processInstanceId"));
		selectedPanel = "findTaskList";
		return "toReportWeekMeeting";
	}
	public String showMorningMeetReport(){
		String processInstanceId = request.getParameter("processInstanceId");
		MorningMeetingEntity morningMeeting = morningMeetingReportService.getMorningMeetingByInstanceId(processInstanceId);
		String attachmentIdStr = morningMeeting.getAttachmentIds();
		List<CommonAttachment> attaList = new ArrayList<>();
		if(StringUtils.isNotBlank(attachmentIdStr)){
			String[] attachmentIds = attachmentIdStr.split(",");
			for(String attachmentId: attachmentIds){
				CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(attachmentId));
				String suffix = attachment.getSuffix();
				//图片
				if(Constants.PIC_SUFFIX.contains(suffix)){
					attachment.setSuffix("png");
				}
				attaList.add(attachment);
			}
		}
		request.setAttribute("morningMeeting", morningMeeting);
		request.setAttribute("attaList", attaList);
		selectedPanel = "findTaskList";
		return "showMorningMeetReport";
	}
	
	public void getDateDiff(){
		String userId = request.getParameter("userId");
		String userName = request.getParameter("userName");
		Map<String,String> resultMap = new HashMap<>();
		ContractEntity contractEntity = contractService.queryContractEntityBy(userId);
		String dateDiff = null;
		if(contractEntity==null){
			dateDiff = userName+"&nbsp;的合同不存在";
		}else{
			String dateDifferent = staffService.getDayDiff(new Date(),contractEntity.getEndDate(),1);
			dateDiff = "剩余&nbsp;"+dateDifferent;
		}
		resultMap.put("dateDiff", dateDiff);
		printByJson(resultMap);
	}
	
}
