
package com.zhizaolian.staff.action.HR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.SocialSecurityClassEntity;
import com.zhizaolian.staff.entity.CredentialEntity;
import com.zhizaolian.staff.entity.CredentialUploadEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.StaffSalaryEntity;
import com.zhizaolian.staff.enums.AuditStatusEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.service.AssetService;
import com.zhizaolian.staff.service.FormalService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PerformanceService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.ResignationService;
import com.zhizaolian.staff.service.SkillService;
import com.zhizaolian.staff.service.SpecialService;
import com.zhizaolian.staff.service.StaffSalaryService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.VacationService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.HttpClientUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.FormalVO;
import com.zhizaolian.staff.vo.GradeVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.PositionVO;
import com.zhizaolian.staff.vo.ResignationVO;
import com.zhizaolian.staff.vo.SkillVO;
import com.zhizaolian.staff.vo.SpecialVO;
import com.zhizaolian.staff.vo.StaffAuditVO;
import com.zhizaolian.staff.vo.StaffQueryVO;
import com.zhizaolian.staff.vo.StaffVO;

import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONObject;

public class StaffAction extends BaseAction {
	@Getter
	private String selectedPanel;
	@Getter
	@Setter
	private String panel;
	@Getter
	@Setter
	private String errorMessage;
	@Getter
	private List<DepartmentVO> departmentVOs;
	@Getter
	private List<GroupDetailVO> groups;
	@Getter
	private List<PositionVO> positionVOs;
	@Setter
	@Getter
	private StaffVO staffVO;
	@Getter
	@Setter
	private StaffSalaryEntity staffSalary;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	private InputStream inputStream;
	@Getter
	@Setter
	private StaffQueryVO staffQueryVO;
	@Getter
	@Setter
	private SkillVO skillVO;
	@Getter
	@Setter
	private Integer staffID;

	@Getter
	@Setter
	private ResignationVO resignationVO;

	@Autowired
	private StaffService staffService;
	@Autowired
	private SkillService skillService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private SpecialService specialService;
	@Setter
	@Getter
	private SpecialVO specialVO;
	@Setter
	@Getter
	private Integer status;
	@Autowired
	private FormalService formalService;
	@Setter
	@Getter
	private SpecialVO[] specialVOs;
	@Setter
	@Getter
	private File[] attachment;
	@Setter
	@Getter
	private String[] attachmentContentType;
	@Setter
	@Getter
	private String[] attachmentFileName;
	@Setter
	@Getter
	private String telephone;
	@Getter
	@Setter
	private String[] pictures;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private ResignationService resignationService;
	@Autowired
	private AssetService assetService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private PerformanceService performanceService;
	@Autowired
	private StaffSalaryService staffSalaryService;
	@Getter
	@Setter
	private CredentialEntity credentialEntity;
	@Autowired
	private ProcessService processService;
	
	private static final long serialVersionUID = 1L;

	public String error() {
		try {
			errorMessage = URLDecoder.decode(errorMessage, "utf-8");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return "error";
	}

	public String newStaff() {
		String number = staffService.getLatestStaffNumber(); // 员工工号格式：'NT00001'
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		List<GradeVO> gradeVOs = positionService.findAllGrades();
		if (CollectionUtils.isEmpty(companyVOs) || CollectionUtils.isEmpty(gradeVOs)) {
			request.setAttribute("errorMessage", "获取页面加载信息失败，请联系系统管理员！");
			panel = "dangan";
			return "error";
		}
		//获取标准社保列表
		List<SocialSecurityClassEntity> socialSecuritys = staffSalaryService.findSocialSecurityClasss();
		request.setAttribute("staffNumber", String.format("%6d", Integer.parseInt(number) + 1).replace(" ", "0"));
		request.setAttribute("companyVOs", companyVOs);
		request.setAttribute("gradeVOs", gradeVOs);
		request.setAttribute("socialSecuritys", socialSecuritys);
		selectedPanel = "staffManage";
		return "newStaff";
	}

	public String getLastName() {
		String userName = request.getParameter("userName");
		request.setAttribute("result", staffService.getStaffByName(userName));
		request.setAttribute("userName", userName);
		return "getLastName";
	}

	public String getNameByTelephone() {
		String telephone = request.getParameter("telephone");
		request.setAttribute("isSelf", request.getParameter("isSelf"));
		List<Object> userNames = staffService.getStaffNameByTelephone(telephone);
		request.setAttribute("result", userNames);
		request.setAttribute("telephone", telephone);
		return "getNameByTelephone";
	}

	public String findDepartmentsByCompanyIDParentID() {
		departmentVOs = positionService.findDepartmentsByCompanyIDDepartmentID(
				Integer.parseInt(request.getParameter("companyID")),
				Integer.parseInt(request.getParameter("parentID")));
		if (CollectionUtils.isEmpty(departmentVOs)) {
			errorMessage = "获取页面加载信息失败，请联系系统管理员！";
		}
		return "departmentsByCompanyID";
	}

	public String addStaff() {
		try {
			String number = staffService.getLatestStaffNumber();
			staffVO.setStaffNumber(String.format("%6d", Integer.parseInt(number)+1).replace(" ", "0"));
			int staffSalaryId = staffSalaryService.saveStaffSalary(staffSalary);
			staffVO.setStandardSalary(staffSalary.getStandardSalary());
			staffVO.setPerformance(staffSalary.getPerformanceSalary());
			staffVO.setStaffSalaryId(staffSalaryId);
			staffID= staffService.addStaff(staffVO);
			StaffVO staffVO1=staffService.getStaffByStaffID(staffID);
			String[] skills=staffVO.getSkills();
			String[] masters=staffVO.getMasters();
			if(skills!=null && masters!=null){
				for (int i = 0; i < skills.length; i++) {
					staffVO.setUserID(staffVO1.getUserID());
					staffVO.setSkill(skills[i]);
					staffVO.setMaster(masters[i]);

					skillService.addSkill(staffVO);
				}
			}

			// 给.com推送员工信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("key", DigestUtils.md5Hex(Constants.COM_KEY));
			map.put("type", 1);
			map.put("userID", staffVO.getUserID());
			map.put("name", staffVO.getLastName());
			map.put("gender", staffVO.getGender());
			map.put("idNumber", staffVO.getIdNumber());
			map.put("telephone", staffVO.getTelephone());
			map.put("address", staffVO.getAddress());

			JSONObject jsonObject = JSONObject.fromObject(map);
			String result = HttpClientUtil.doPost(jsonObject.toString(), Constants.COM_ACCOUNT_URL);
			if ("1".equals(result)) {
				logger.info("给.com推送员工（" + staffVO.getUserID() + "）信息成功");
			} else {
				logger.error("给.com推送员工（" + staffVO.getUserID() + "）信息失败");
			}
			// 给mes推送员工信息
			map = new HashMap<String, Object>();
			map.put("oaid", staffVO.getUserID());
			map.put("name", staffVO.getLastName());
			map.put("tel", staffVO.getTelephone());
			map.put("password", "Zzl" + staffVO.getTelephone().substring(7));
			jsonObject = JSONObject.fromObject(map);
			result = HttpClientUtil.doPost(jsonObject.toString(), Constants.MES_ADD_USER_URL);
			if (StringUtils.isNotBlank(result)) {
				JSONObject resultObj = JSONObject.fromObject(result);
				if("0".equals(resultObj.get("SUCCESS"))){
					logger.info("给mes推送员工（"+staffVO.getUserID()+"）信息成功");
				}else{
					logger.error("给mes推送员工（"+staffVO.getUserID()+"）信息失败");
				}	
			} else {
				logger.error("给mes推送员工（" + staffVO.getUserID() + "）信息失败");
			}
			// 新人入职，同步下所属部门之前收到 的消息
			noticeService.synNotice(staffVO.getCompanyID(), staffVO.getDepartmentID(), staffVO.getUserID());
			// 新人入职，同步下所属岗位的绩效方案,并生成待办
			performanceService.synStaffCheckItems(staffVO.getUserID(), staffVO.getPositionID());
		} catch (Exception e) {
			errorMessage = "录入失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "addStaff";
	}
	@Getter
	@Setter
	private Integer type;
	public String findStaffList() {
		if(type==null){
			type=0;
		}
		switch(type){
		case 0:
			List<Integer> statusList = new ArrayList<Integer>();
			statusList.add(StaffStatusEnum.PROBATION.getValue());
			statusList.add(StaffStatusEnum.PRACTICE.getValue());
			statusList.add(StaffStatusEnum.FORMAL.getValue());
			List<StaffVO> staffVOs = staffService.findStaffPageListByStatusList(statusList, page, limit);
			count = staffService.countStaffByStatusList(statusList);
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			List<CompanyVO> companyVOs = positionService.findAllCompanys();

			request.setAttribute("companyVOs", companyVOs);
			request.setAttribute("staffVOList", staffVOs);
			
			break;
		case 1:
			String year = request.getParameter("year");
			String month = request.getParameter("month");
			String status = request.getParameter("staffStatus");
			String companyId = request.getParameter("companyId");
			if (StringUtils.isBlank(year)) {
				Date now = new Date();
				year = String.valueOf(DateUtil.getYear(now));
				month = String.valueOf(DateUtil.getMonth(now));
				status = StaffStatusEnum.LEAVE.getName();
			}
			ListResult<Object> insuranceList = staffService.getInsuranceList(year, month, status, companyId, limit, page);
			count = insuranceList.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			List<CompanyVO> companyVos = positionService.findAllCompanys();
			request.setAttribute("insuranceList", insuranceList.getList());
			request.setAttribute("year", year);
			request.setAttribute("month", month);
			request.setAttribute("staffStatus", status);
			request.setAttribute("companyVOs", companyVos);
			request.setAttribute("companyId",companyId);
			break;
		}
		selectedPanel = "staffManage";
		return "staffManage";
	}

	public String updateStaff() {
		try {
			String userID = request.getParameter("userID");
			staffVO = staffService.getStaffByUserID(userID);
			staffVO.setStaffNumber(identityService.createUserQuery().userId(userID).singleResult().getFirstName());
			StaffSalaryEntity staffSalary = staffSalaryService.getStaffSalary(userID);
			if(null != staffSalary){
				request.setAttribute("standardSalary", true);
			}else{
				//获取标准社保列表
				List<SocialSecurityClassEntity> socialSecuritys = staffSalaryService.findSocialSecurityClasss();
				request.setAttribute("socialSecuritys", socialSecuritys);
			}
			/*
			 * List<CompanyVO> companyVOs = positionService.findAllCompanys();
			 * List<DepartmentVO> departmentVOs =
			 * positionService.findDepartmentsByCompanyID(staffVO.getCompanyID()
			 * ); List<PositionVO> positionVOs =
			 * positionService.findAllPositions();
			 */
			List<GradeVO> gradeVOs = positionService.findAllGrades();
			/*
			 * if (departmentVOs != null) { List<Integer> selectedDepartmentIDs
			 * = new ArrayList<Integer>(); int selectedDepartmentID =
			 * staffVO.getDepartmentID(); while (selectedDepartmentID != 0) {
			 * selectedDepartmentIDs.add(0, selectedDepartmentID); for
			 * (DepartmentVO departmentVO : departmentVOs) { if
			 * (departmentVO.getDepartmentID() == selectedDepartmentID) {
			 * selectedDepartmentID = departmentVO.getParentID(); } } }
			 * request.setAttribute("selectedDepartmentIDs",
			 * selectedDepartmentIDs); }
			 */

			/*
			 * request.setAttribute("companyVOs", companyVOs);
			 * request.setAttribute("departmentVOs", departmentVOs);
			 * request.setAttribute("positionVOs", positionVOs);
			 */
			/* if(staffVO.getAttachementNames()!=null){ */
			String[] pictureNames = StringUtils.split(staffVO.getAttachementNames(), ",");
			request.setAttribute("pictureNames", pictureNames);
			/* } */
			request.setAttribute("gradeVOs", gradeVOs);

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员：" + e.getMessage();
			panel = "dangan";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "staffManage";
		return "updateStaff";
	}

	public String getPicture() {
		String userID = request.getParameter("userID");
		if (StringUtils.isBlank(userID)) {
			inputStream = new ByteArrayInputStream(new byte[10]);
			return "imgStream";
		}
		try {
			byte[] picArray = identityService.getUserPicture(userID).getBytes();
			if (picArray == null) {
				picArray = new byte[10];
			}
			inputStream = new ByteArrayInputStream(picArray);
		} catch (Exception e) {
			inputStream = new ByteArrayInputStream(new byte[10]);
		}
		return "imgStream";
	}

	public String getPictureForStructure() {
		String userID = request.getParameter("userID");
		if (StringUtils.isBlank(userID)) {
			inputStream = new ByteArrayInputStream(new byte[10]);
			return "imgStream";
		}
		try {
			byte[] picArray = identityService.getUserPicture(userID).getBytes();
			if (picArray == null) {
				picArray = new byte[10];
			}
			inputStream = new ByteArrayInputStream(picArray);
		} catch (Exception e) {
			try {
				StaffEntity staff = staffService.getStaffByUserId(userID);
				String rootPath = request.getSession().getServletContext().getRealPath("/");
				if ("男".equals(staff.getGender())) {
					inputStream = new FileInputStream(rootPath + "/assets/images/boy.png");
				} else {
					inputStream = new FileInputStream(rootPath + "/assets/images/girl.png");
				}
			} catch (Exception e2) {
				inputStream = new ByteArrayInputStream(new byte[10]);
			}
		}
		return "imgStream";
	}

	public String showImage() {
		String imageId = request.getParameter("imageId");
		CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(imageId));
		try {
			inputStream = new FileInputStream(new File(attachment.getSoftURL()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "imgStream";
	}

	public String saveStaffInformation() {
		InputStream in = null;
		OutputStream out = null;
		try {
			StringBuffer attachmentNames = new StringBuffer();
			int j = 0;

			if (pictures != null) {
				j = Integer.parseInt(
						pictures[pictures.length - 1].substring(0, pictures[pictures.length - 1].indexOf("."))) + 1;
				attachmentNames.append(StringUtils.join(pictures, ","));
				attachmentNames.append(",");
			}
			if (attachment != null) {

				for (int i = 0; i < attachment.length; i++) {
					byte[] buffer = new byte[10 * 1024 * 1024];
					int length = 0;
					attachmentFileName[i] = (j + i)
							+ attachmentFileName[i].substring(attachmentFileName[i].lastIndexOf("."));

					in = new FileInputStream(attachment[i]);
					File f = new File(Constants.HR_FILE_DIRECTORY);
					if (!f.exists()) {
						f.mkdirs();
					}
					out = new FileOutputStream(new File(f, staffVO.getStaffID() + "_" + attachmentFileName[i]));

					while ((length = in.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, length);
					}
					attachmentNames.append(attachmentFileName[i]);
					attachmentNames.append(",");
				}
				staffVO.setAttachementNames(attachmentNames.toString());

			}
			staffVO.setAttachementNames(attachmentNames.toString());
			if (staffVO.getRegistrationForm() != null) {
				String fileName = staffVO.getRegistrationFormFileName();
				File parent = new File(Constants.HR_FILE_DIRECTORY);
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				in = new FileInputStream(staffVO.getRegistrationForm());
				out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(Constants.HR_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
				int attachmentId = noticeService.saveAttachMent(commonAttachment);
				staffVO.setRegistrationFormId(String.valueOf(attachmentId));
			}
			if(null != staffSalary){
				staffSalary.setUserId(staffVO.getUserID());
				int staffSalaryId = staffSalaryService.saveStaffSalary(staffSalary);
				staffVO.setStandardSalary(staffSalary.getStandardSalary());
				staffVO.setPerformance(staffSalary.getPerformanceSalary());
				staffVO.setStaffSalaryId(staffSalaryId);
			}
			if (staffVO.getWeixinCode() != null) {
				String fileName = staffVO.getWeixinCodeFileName();
				File parent = new File(Constants.HR_FILE_DIRECTORY);
				parent.mkdirs();
				String saveName = UUID.randomUUID().toString().replaceAll("-", "");
				in = new FileInputStream(staffVO.getWeixinCode());
				out = new FileOutputStream(new File(parent, saveName));
				byte[] buffer = new byte[10 * 1024 * 1024];
				int length = 0;
				while ((length = in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 0, length);
					out.flush();
				}
				CommonAttachment commonAttachment = new CommonAttachment();
				commonAttachment.setAddTime(new Date());
				commonAttachment.setIsDeleted(0);
				commonAttachment.setSoftURL(Constants.HR_FILE_DIRECTORY + saveName);
				commonAttachment.setSoftName(fileName);
				commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
				int attachmentId = noticeService.saveAttachMent(commonAttachment);
				staffVO.setWeixinCodeId((String.valueOf(attachmentId)));
			}
			staffService.updateStaff(staffVO);
			// 给mes推送员工修改信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("oaid", staffVO.getUserID());
			map.put("name", staffVO.getLastName());
			map.put("tel", staffVO.getTelephone());
			JSONObject jsonObject = JSONObject.fromObject(map);
			String result = HttpClientUtil.doPost(jsonObject.toString(), Constants.MES_UPDATE_USER_URL);
			if (StringUtils.isNotBlank(result)) {
				JSONObject resultObj = JSONObject.fromObject(result);
				if ("0".equals(resultObj.get("SUCCESS"))) {
					logger.info("给mes推送员工（" + staffVO.getUserID() + "）修改信息成功");
				}else{
					logger.error("给mes推送员工（" + staffVO.getUserID() + "）修改信息失败");
				}
			} else {
				logger.error("给mes推送员工（" + staffVO.getUserID() + "）修改信息失败");
			}
		} catch (Exception e) {
			errorMessage = "修改失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
		return "saveStaffInformation";
	}

	public String showStaffDetail() {
		try {
			String userID = request.getParameter("userID");
			staffVO = staffService.getStaffByUserID(userID);
			/*Integer processStatus = Integer.parseInt(request.getParameter("processStatus"));*/
			/*pages/HR/resignationManage.jsp页面需更改第270行：&processStatus=${res.processStatus }*/
			/*pages/HR/staffDetail.jsp需更改第130行*/
			// 获取今年年假信息
			staffVO.setAnnualVacationInfo(vacationService.getStaffAnnualVacationInfo(userID));
			staffVO.setStaffNumber(identityService.createUserQuery().userId(userID).singleResult().getFirstName());
			//获取员工薪资明细
			StaffSalaryEntity staffSalary = staffSalaryService.getStaffSalary(userID);
			request.setAttribute("staffSalary", staffSalary);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员：" + e.getMessage();
			panel = "dangan";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "staffManage";
		return "showStaffDetail";
	}

	public String showStaffCard() {
		try {
			String userID = request.getParameter("userID");
			StaffVO staffVO = staffService.getStaffByUserID(userID);
			request.setAttribute("staffVO", staffVO);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员：" + e.getMessage();
			panel = "dangan";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "staffManage";
		return "showStaffCard";
	}

	public String showStaffDetailonAudit() {// 这个方法是在审核时出现详情页面时请求的方法
		try {
			String userID = request.getParameter("userID");
			staffVO = staffService.getStaffByUserID(userID);
			staffVO.setStaffNumber(identityService.createUserQuery().userId(userID).singleResult().getFirstName());
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员：" + e.getMessage();
			panel = "dangan";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "staffAudit";
		return "showStaffDetailonAudit";
	}

	public String resignStaff() {
		String userID = request.getParameter("userID");
		String leaveDate = request.getParameter("leaveDate");
		try {
			//转移离职人员的权限

			staffService.deleteStaffLoginAccount(userID);
			staffService.doLeave(userID, DateUtil.getSimpleDate(leaveDate));
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				//给.com推送离职员工信息
				map.put("key", DigestUtils.md5Hex(Constants.COM_KEY));
				map.put("type", 2);
				map.put("userID", userID);
				JSONObject jsonObject = JSONObject.fromObject(map);
				String result = HttpClientUtil.doPost(jsonObject.toString(), Constants.COM_ACCOUNT_URL);
				if("1".equals(result)){
					logger.info("给.com推送离职员工（"+userID+"）信息成功");
				}else{
					logger.error("给.com推送离职员工（"+userID+"）信息失败");
				}
			} catch (Exception e) {
				e.printStackTrace();
				StringWriter sw = new StringWriter(); 
				e.printStackTrace(new PrintWriter(sw, true)); 
				logger.error(sw.toString());
			}
			//给mes推送离职员工信息
			map = new HashMap<String, Object>();
			map.put("oaid", userID);
			JSONObject jsonObject = JSONObject.fromObject(map);
			String result = HttpClientUtil.doPost(jsonObject.toString(), Constants.MES_DEL_USER_URL);
			if(StringUtils.isNotBlank(result)){
				JSONObject resultObj = JSONObject.fromObject(result);
				if("0".equals(resultObj.get("SUCCESS"))){
					logger.info("给mes推送离职员工（"+userID+"）信息成功");
				}else{
					logger.error("给mes推送离职员工（"+userID+"）信息失败");
				}
			}else{
				logger.error("给mes推送离职员工（"+userID+"）信息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "操作失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		//记录操作离职的日志信息
		User user=(User)request.getSession().getAttribute("user");
		logger.info("操作离职按钮："+user.getId()+"；离职员工："+userID);
		//员工离职，删除已有的授权
		permissionService.deleteRight(userID);
		return "resignStaff";
	}

	public String deleteStaff() {
		String userID = request.getParameter("userID");
		try {
			staffService.deleteStaff(userID);
			staffService.deleteStaffMembership(userID);
			// 给.com推送删除员工信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("key", DigestUtils.md5Hex(Constants.COM_KEY));
			map.put("type", 2);
			map.put("userID", userID);
			JSONObject jsonObject = JSONObject.fromObject(map);
			String result = HttpClientUtil.doPost(jsonObject.toString(), Constants.COM_ACCOUNT_URL);
			if ("1".equals(result)) {
				logger.info("给.com推送删除员工（" + userID + "）信息成功");
			} else {
				logger.error("给.com推送删除员工（" + userID + "）信息失败");
			}
			// 给mes推送删除员工信息
			map = new HashMap<String, Object>();
			map.put("oaid", userID);
			jsonObject = JSONObject.fromObject(map);
			result = HttpClientUtil.doPost(jsonObject.toString(), Constants.MES_DEL_USER_URL);
			if (StringUtils.isNotBlank(result)) {
				JSONObject resultObj = JSONObject.fromObject(result);
				if("0".equals(resultObj.get("SUCCESS"))){
					logger.info("给mes推送删除员工（"+userID+"）信息成功");
				}else{
					logger.error("给mes推送删除员工（"+userID+"）信息失败");
				}
			} else {
				logger.error("给mes推送删除员工（" + userID + "）信息失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "删除失败：" + e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}

		return "deleteStaff";
	}

	public String findStaffListByQueryVO() {
		try {
			String trans = new String(staffQueryVO.getName().getBytes("ISO-8859-1"), "UTF-8");
			String name = URLDecoder.decode(trans, "UTF-8");
			staffQueryVO.setName(name);
			String tran = new String(staffQueryVO.getPersonalPost().getBytes("ISO-8859-1"), "UTF-8");
			String personalPost = URLDecoder.decode(tran, "UTF-8");
			staffQueryVO.setPersonalPost(personalPost);

			ListResult<StaffVO> staffVOListResult = staffService.findStaffPageListByQueryVO(staffQueryVO, page, limit);
			request.setAttribute("staffVOList", staffVOListResult.getList());
			count = staffVOListResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;

			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (staffQueryVO != null && staffQueryVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyID(staffQueryVO.getCompanyID());
				if (departmentVOs != null && staffQueryVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = staffQueryVO.getDepartmentID();
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
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			panel = "dangan";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "staffManage";
		return "staffManage";
	}

	public String findStaffAuditList() {
		try {
			String name = request.getParameter("name");
			AuditStatusEnum auditStatus = StringUtils.isBlank(request.getParameter("auditStatus")) ? null
					: AuditStatusEnum.valueOf(Integer.valueOf(request.getParameter("auditStatus")));
			Integer companyID = StringUtils.isBlank(request.getParameter("companyID")) ? null
					: Integer.valueOf(request.getParameter("companyID"));
			Integer departmentID = StringUtils.isBlank(request.getParameter("departmentID")) ? null
					: Integer.valueOf(request.getParameter("departmentID"));
			ListResult<StaffVO> staffListResult = staffService.findStaffPageList(name, auditStatus, companyID,
					departmentID, page, limit);
			count = staffListResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;

			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (companyID != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(companyID);
				if (departmentVOs != null && departmentID != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = departmentID;
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

			request.setAttribute("name", name == null ? "" : name);
			request.setAttribute("auditStatus", auditStatus == null ? "" : auditStatus.getValue());
			request.setAttribute("companyID", companyID == null ? "" : companyID);
			request.setAttribute("staffVOList", staffListResult.getList());
			request.setAttribute("companyVOs", companyVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败：" + e.getMessage();
			panel = "dangan";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}

		selectedPanel = "staffAudit";
		return "staffAudit";
	}

	public String updateAuditResult() {
		try {
			String staffID = request.getParameter("staffID");
			AuditStatusEnum auditStatus = AuditStatusEnum
					.valueOf(Integer.parseInt(request.getParameter("auditStatusonAudit"))); // 得到auditStatus的请求参数值
			staffService.updateAuditResult(staffID, auditStatus);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "系统错误，请联系系统管理员：" + e.getMessage();
			panel = "dangan";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}

		findStaffAuditList();
		request.removeAttribute("auditStatus");
		selectedPanel = "staffAudit";
		return "staffAudit";
	}

	public String findPositionsByDepartmentID() {

		positionVOs = positionService
				.findPositionsByDepartmentID(Integer.parseInt(request.getParameter("departmentID")));

		return "positionsByDepartmentID";
	}

	public String staffWarnList() {
		try {

			User user = (User) request.getSession().getAttribute("user");
			String userID = user.getId();
			groups = staffService.findGroupDetailsByUserID(userID);
			Integer companyID = groups.get(0).getCompanyID();
			ListResult<StaffVO> staffVOs = staffService.findStaffList(staffVO, limit, page, companyID);
			count = staffVOs.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			List<FormalVO> formalVOs = formalService.findNotEndFormals();
			request.setAttribute("staffVOs", staffVOs.getList());
			request.setAttribute("formalVOs", formalVOs);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询错误：" + e.getMessage();
			selectedPanel = "staffWarn";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "staffWarn";
		return "staffWarn";
	}

	public String staffCard() {
		if (staffVO == null) {
			staffVO = new StaffVO();
		}
		try {
			staffID = Integer.parseInt(request.getParameter("staffID"));
			StaffVO staffVO1 = staffService.getStaffByStaffID(staffID);
			request.setAttribute("staffVO1", staffVO1);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询错误：" + e.getMessage();
			selectedPanel = "newStaff";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "staffManage";
		return "staffCard";

	}

	// --------------------------------
	@Getter
	@Setter
	private String requestUserID;

	public String staffCardByUserId() {
		if (staffVO == null) {
			staffVO = new StaffVO();
		}
		try {
			requestUserID = request.getParameter("requestUserID");
			StaffVO staffVO1 = staffService.getStaffByUser_ID(requestUserID);

			request.setAttribute("staffVO1", staffVO1);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询错误：" + e.getMessage();
			selectedPanel = "newStaff";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "staffManage";
		return "staffCardByUserId";

	}

	public void showPersonalInfo() {
		String userID = request.getParameter("userID");
		StaffVO staffVO1 = staffService.getStaffByUser_ID(userID);
		printByJson(staffVO1);
	}
	// --------------------------------

	public String deleteSpecial() {
		try {
			String documentStrIds = request.getParameter("documentStrIds");
			if (documentStrIds != null && !"".equals(documentStrIds.trim())) {
				String[] ids = documentStrIds.split(";");
				if (ids.length > 0) {
					for (String idStr : ids) {
						specialService.deleteSpecial(Integer.parseInt(idStr));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "删除失败：" + e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "deleteSpecial1";
	}

	public String saveSpecial() {
		try {
			for (String userID : specialVO.getUserIDs()) {
				status = specialService.getSpecialByTypeUserID(specialVO.getType(), userID);
				if (status == 0) {
					specialVO.setUserID(userID);
					specialService.saveSpecial(specialVO);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "获取失败：" + e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "saveSpecial";
	}

	public String findSpecialList() {
		try {
			List<SpecialVO> specialVOs = specialService.findBySql(1, 1);
			List<SpecialVO> specialVOs1 = specialService.findBySql(1, 2);
			List<SpecialVO> specialVOs2 = specialService.findBySql(2, 1);
			List<SpecialVO> specialVOs3 = specialService.findBySql(2, 2);
			request.setAttribute("specialVOs", specialVOs);
			request.setAttribute("specialVOs1", specialVOs1);
			request.setAttribute("specialVOs2", specialVOs2);
			request.setAttribute("specialVOs3", specialVOs3);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询错误：" + e.getMessage();
			selectedPanel = "staffSpecial";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "staffSpecial";
		return "findSpecialList";
	}

	public String findStaffRegularRecord() {
		if (staffVO == null) {
			staffVO = new StaffVO();
		}
		try {
			ListResult<StaffVO> staffVOs = staffService.findStaffRegularRecord(staffVO, limit, page);
			count = staffVOs.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("staffVOs", staffVOs.getList());
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (staffVO != null && staffVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(staffVO.getCompanyID());
				if (departmentVOs != null && staffVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = staffVO.getDepartmentID();
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
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询错误：" + e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "staffRegularRecord";
		return "staffRegularRecord";
	}

	public String getStaffByTelephone() {

		try {
			staffVO = staffService.getStaffByTelephone(telephone);
		} catch (Exception e) {
			errorMessage = "查询错误：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";

		}
		return "getStaffByTelephone";
	}

	public String updatePassWord() {
		try {
			String telephone1 = request.getParameter("telephone1");
			String password = request.getParameter("password");
			StaffVO staffVO = new StaffVO();
			staffVO.setTelephone(telephone1);
			staffVO.setPassword(password);
			String userID = staffService.updateStaffTelephone(staffVO);
			User user = identityService.createUserQuery().userId(userID).singleResult();
			ServletActionContext.getContext().getSession().put("user", user);
			List<String> permissions = permissionService.findPermissionsByUserID(user.getId());
			ServletActionContext.getContext().getSession().put("permissions", permissions);
			staffService.updateStaffTelephone(staffVO);

		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "查询错误：" + e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		return "updatePassWord";
	}

	public String findSkillList() {
		if (skillVO == null) {
			skillVO = new SkillVO();
		}
		try {
			ListResult<SkillVO> listResult = skillService.findSkillListByUserID(skillVO, page, limit);
			request.setAttribute("skillVOs", listResult.getList());
			count = listResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		} catch (Exception e) {
			errorMessage = "查询错误：" + e.getMessage();
			e.printStackTrace();
			selectedPanel = "staffSkill";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "staffSkill";
		return "findSkillList";
	}

	public String getSkillBySkillID() {
		try {
			String userID = request.getParameter("userID");
			SkillVO skillVO = skillService.getSkillBySkillID(userID);
			request.setAttribute("skillVO", skillVO);
		} catch (Exception e) {
			errorMessage = "查询错误：" + e.getMessage();
			e.printStackTrace();
			selectedPanel = "staffSkill";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		return "getSkillBySkillID";
	}

	public String updateSkill() {
		try {
			skillService.updateSkill(staffVO, staffVO.getUserID());
		} catch (Exception e) {
			errorMessage = "删除失败：" + e.getMessage();
			e.printStackTrace();
			selectedPanel = "staffSkill";
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "staffSkill";
		return "updateSkill";
	}

	public String deletePicture() {
		String path = request.getParameter("path");
		File f = new File(Constants.HR_FILE_DIRECTORY + path);
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
			dos.close();
			f.getAbsoluteFile().delete();
			Integer staffID = Integer.parseInt(path.substring(0, path.indexOf("_")));
			String pictureName = path.substring(path.indexOf("_") + 1);
			staffService.updateAttachementNames(pictureName, staffID);
		} catch (Exception e) {
			errorMessage = "删除失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "deletePicture";
	}

	public String findResignationList() {
		if (resignationVO == null) {
			resignationVO = new ResignationVO();
		}

		try {
			ListResult<ResignationVO> resignationList = resignationService.findResignationByResignationVO(resignationVO,
					page, limit);
			count = resignationList.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			request.setAttribute("resignationVOs", resignationList.getList());
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if (resignationVO != null && resignationVO.getCompanyID() != null) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyID(resignationVO.getCompanyID());
				if (departmentVOs != null && resignationVO.getDepartmentID() != null) {
					List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
					int selectedDepartmentID = resignationVO.getDepartmentID();
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
		} catch (Exception e) {
			errorMessage = "查询失败：" + e.getMessage();
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "resignationList";
		return "resignationList";
	}

	public String startAudit() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "startAudit";
		}

		try {
			String auditUserID = request.getParameter("userID");
			StaffAuditVO staffAuditVO = new StaffAuditVO();
			staffAuditVO.setUserID(user.getId());
			staffAuditVO.setUserName(staffService.getStaffByUserID(user.getId()).getLastName());
			staffAuditVO.setAuditUserID(auditUserID);
			StaffVO staff = staffService.getStaffByUserID(auditUserID);
			staffAuditVO.setAuditUserName(staff.getLastName());
			staffAuditVO.setEducationID(staff.getEducationID());
			staffAuditVO.setDegreeID(staff.getDegreeID());
			staffAuditVO.setCriminalRecord(staff.getCriminalRecord());
			staffService.startAudit(staffAuditVO);
			// 更新OA_Staff表员工背景调查状态为 待填写表格
			staffService.updateAuditResult(String.valueOf(staff.getStaffID()), AuditStatusEnum.TO_FILL_FORM);
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "发送背景调查表格失败：" + e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}

		return "startAudit";
	}

	public void getStaffAssets() {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String userId = request.getParameter("userId");
			List<Object> assetObjs = assetService.getStaffAssets(userId);
			resultMap.put("assetObjs", assetObjs);
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("error", e.toString());
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		printByJson(resultMap);
	}

	/**
	 * 手机号码做唯一性校验
	 */
	public void checkTelephone() {
		Map<String, Object> resultMap = new HashMap<>();
		String telephone = request.getParameter("telephone");
		String staffId = request.getParameter("staffId");
		// 号码已存在
		if (staffService.checkTelephone(telephone, staffId)) {
			resultMap.put("exist", "true");
		} else {
			resultMap.put("exist", "false");
		}
		printByJson(resultMap);
	}

	public String findFormalStaffApplicationList() {
		if(type==null){
			type=0;
		}
		switch(type){
		case 0:
			String staffName = request.getParameter("staffName");
			String beginDate = request.getParameter("beginDate");
			String endDate = request.getParameter("endDate");
			ListResult<FormalVO> fmListResult = formalService.findFormalStaffApplicationList(staffName, page, limit,
					beginDate, endDate);
			count = fmListResult.getTotalCount();
			request.setAttribute("formalApplys", fmListResult.getList());
			request.setAttribute("staffName", staffName);
			request.setAttribute("beginDate", beginDate);
			request.setAttribute("endDate", endDate);
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			
			break;
		case 1:
			if (staffVO == null) {
				staffVO = new StaffVO();
			}
			try {
				ListResult<StaffVO> staffVOs = staffService.findStaffRegularRecord(staffVO, limit, page);
				count = staffVOs.getTotalCount();
				totalPage = count % limit == 0 ? count / limit : count / limit + 1;
				if (totalPage == 0) {
					totalPage = 1;
				}
				request.setAttribute("staffVOs", staffVOs.getList());
				List<CompanyVO> companyVOs = positionService.findAllCompanys();
				if (staffVO != null && staffVO.getCompanyID() != null) {
					List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(staffVO.getCompanyID());
					if (departmentVOs != null && staffVO.getDepartmentID() != null) {
						List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
						int selectedDepartmentID = staffVO.getDepartmentID();
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
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = "查询错误：" + e.getMessage();
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw, true));
				logger.error(sw.toString());
				return "error";
			}
			break;
		case 2:
			try {

				User user = (User) request.getSession().getAttribute("user");
				String userID = user.getId();
				groups = staffService.findGroupDetailsByUserID(userID);
				Integer companyID = groups.get(0).getCompanyID();
				ListResult<StaffVO> staffVOs = staffService.findStaffList(staffVO, limit, page, companyID);
				count = staffVOs.getTotalCount();
				totalPage = count % limit == 0 ? count / limit : count / limit + 1;
				if (totalPage == 0) {
					totalPage = 1;
				}
				List<FormalVO> formalVOs = formalService.findNotEndFormals();
				request.setAttribute("staffVOs", staffVOs.getList());
				request.setAttribute("formalVOs", formalVOs);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessage = "查询错误：" + e.getMessage();
				selectedPanel = "staffWarn";
				StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw, true));
				logger.error(sw.toString());
				return "error";
			}
			break;
		}
		selectedPanel = "formalStaffApplicationList";
		return "formalStaffApplicationList";
	}

	public void getAllStaffsByDepId() {
		String companyId = request.getParameter("companyId");
		String depId = request.getParameter("depId");
		Set<String> staffs = new HashSet<>();
		staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), Integer.parseInt(depId), staffs);
		List<StaffVO> staffVos = new ArrayList<>();
		for (String staff : staffs) {
			StaffVO staffVo = staffService.getStaffByUserID(staff);
			staffVos.add(staffVo);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("staffVos", staffVos);
		printByJson(resultMap);
	}

	public String getInsuranceList() {
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String status = request.getParameter("staffStatus");
		String companyId = request.getParameter("companyId");
		if (StringUtils.isBlank(year)) {
			Date now = new Date();
			year = String.valueOf(DateUtil.getYear(now));
			month = String.valueOf(DateUtil.getMonth(now));
			status = StaffStatusEnum.LEAVE.getName();
		}
		ListResult<Object> insuranceList = staffService.getInsuranceList(year, month, status, companyId, limit, page);
		count = insuranceList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("insuranceList", insuranceList.getList());
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		request.setAttribute("staffStatus", status);
		request.setAttribute("companyVOs", companyVOs);
		selectedPanel = "staffManage";
		return "insuranceList";
	}

	@Getter
	private String downloadFileName;

	public String exportInsuranceList() {
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String status = request.getParameter("staffStatus");
		String companyId = request.getParameter("companyId");
		String title = "";
		if (StaffStatusEnum.LEAVE.getName().equals(status)) {
			title = year + "年" + month + "月离职员工名单";
		} else {
			title = year + "年" + month + "月在职员工名单";
		}
		List<Object> insuranceList = staffService.getAllInsuranceList(year, month, status, companyId);
		try {
			inputStream = staffService.exportInsuranceList(insuranceList, status, title);
			downloadFileName = new String((title + ".xls").getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "exportInsuranceList";
	}
	public String findAllInJobStaffs(){
		String staffName = request.getParameter("staffName");
		ListResult<StaffEntity> allStaffs = staffService.findAllInJobStaffs(staffName, limit, page);
		int totalCount = allStaffs.getTotalCount();
		totalPage = totalCount%limit==0 ? totalCount/limit : totalCount/limit+1;
		if(totalPage==0){
			totalPage = 1;
		}
		request.setAttribute("allStaffs", allStaffs.getList());
		request.setAttribute("staffName", staffName);
		selectedPanel = "salaryAlteration";
		return "allStaffs";
	}

	// 岗位资格证书
	public String showPostCredentialApply() {

		selectedPanel = "postCredentialApplyList";
		return "showPostCredentialApply";
	}

	// 岗位资格证书
	public String postCredentialApplyToPerson() {

		selectedPanel = "postCredentialApplyList";
		return "postCredentialApplyToPerson";
	}

	// 添加岗位资格证书
	public String addCredentialEntity() {
		User user = (User) request.getSession().getAttribute("user");
		String applyUserId = user.getId();
		try{
			processService.addCredentialEntity(applyUserId, credentialEntity.getApplyExplain(),
					credentialEntity.getOfferUserId());
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "addCredentialEntity";
	}

	// 获得证书审核的记录
	public String postCredentialApplyRecord() {
		try{
			ListResult<CredentialEntity> credentialListResult = processService.findCredentialBy(page, limit);
			request.setAttribute("credentialList", credentialListResult.getList());
			count = credentialListResult.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		selectedPanel = "postCredentialApplyList";
		return "postCredentialApplyRecord";
	}

	// 根据applyUserId和offerUserId查询人名
	public void getStaffnameByUserId() {
		Map<String, Object> resultMap = new HashMap<>();
		String applyUserId = request.getParameter("applyUserId");
		String offerUserId = request.getParameter("offerUserId");
		StaffEntity applyUser = null;
		StaffEntity offerUser = null;
		try{
			applyUser = staffService.getStaffByUserId(applyUserId);
			offerUser = staffService.getStaffByUserId(offerUserId);
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		
		String applyUserName = applyUser.getStaffName();
		String offerUserName = offerUser.getStaffName();
		resultMap.put("applyUserName", applyUserName);
		resultMap.put("offerUserName", offerUserName);
		printByJson(resultMap);
	}

	public String postCredentialUpload() {
		String taskId = request.getParameter("taskId");
		Integer id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("taskId", taskId);
		request.setAttribute("id", id);
		CredentialEntity credentialEntity = null;
		try{
			credentialEntity = processService.getCredentialEntityBy(id);
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		request.setAttribute("credentialEntity", credentialEntity);
		User user = (User) request.getSession().getAttribute("user");
		request.setAttribute("offerUserId", user.getId());
		selectedPanel = "findTaskList";
		return "postCredentialUpload";
	}

	@Getter
	@Setter
	private List<CredentialUploadEntity> credentialUploadEntityList;
	@Getter
	@Setter
	private CredentialUploadEntity credentialUploadEntity;
	@Autowired
	private TaskService taskService;
	
	public String credentialUpload() {
		if (credentialUploadEntityList != null && credentialUploadEntityList.size() > 0) {
			for (int i = 0; i < credentialUploadEntityList.size(); i++) {
				credentialUploadEntity = credentialUploadEntityList.get(i);
				if(credentialUploadEntity !=null){
					try {
						processService.addCredentialUploadEntity(credentialUploadEntity);
					} catch (Exception e) {
						e.printStackTrace();
						StringWriter sw = new StringWriter();
						e.printStackTrace(new PrintWriter(sw, true));
						logger.error(sw.toString());
					}
				}
			}
		}
		String taskId = request.getParameter("taskId");
		Integer id = Integer.parseInt(request.getParameter("id"));
		
		processService.updateCredentialEntity(id);
		
		taskService.complete(taskId);
		selectedPanel = "findTaskList";
		return "credentialUpload";
	}
	public String uploadCredential(){
		String taskId = request.getParameter("taskId");
		Integer id = Integer.parseInt(request.getParameter("id"));
		try {
			processService.addCredentialUploadEntity(credentialUploadEntity);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		processService.updateCredentialEntity(id);
		taskService.complete(taskId);
		return "uploadCredential";
	}
	public String postCredentialCheck(){
		String taskId = request.getParameter("taskId");
		Integer id = Integer.parseInt(request.getParameter("id"));
		List<CredentialEntity> credentialEntitys;
		try {
			credentialEntitys = processService.getPersonalCredentialById(id);
			request.setAttribute("credentialEntitys", credentialEntitys);
			request.setAttribute("taskId", taskId);
			request.setAttribute("id", id);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		selectedPanel = "findTaskList";
		return "postCredentialCheck";
	}
	public String checkPicture(){
		Integer credentialUploadId = Integer.parseInt(request.getParameter("credentialUploadId"));
		CredentialUploadEntity credentialUploadEntity = processService.getCredentialUploadEntityById(credentialUploadId);
		try {
			byte[] pictureData = credentialUploadEntity.getCredentialPictureData();
			inputStream = new ByteArrayInputStream(pictureData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "checkPicture";
	}
	public String checkCredential(){
		String taskId = request.getParameter("taskId");
		String result = request.getParameter("result");
		String applyResult = request.getParameter("applyResult");
		if(applyResult==""){
			applyResult = null;
		}
		Integer id = Integer.parseInt(request.getParameter("id"));
		try{
			processService.checkCredential(taskId, result,applyResult, id);
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		
		
		
		return "checkCredential";
	}
	
	public String postCredentialAmend(){
		String taskId = request.getParameter("taskId");
		Integer id = Integer.parseInt(request.getParameter("id"));
		List<CredentialEntity> credentialEntitys;
		CredentialEntity credential = new CredentialEntity();
		try {
			credential=processService.getCredentialEntityBy(id);
			credentialEntitys = processService.getPersonalCredentialById(id);
			request.setAttribute("applyResult", credential.getApplyResult());
			request.setAttribute("credentialEntitys", credentialEntitys);
			request.setAttribute("taskId", taskId);
			request.setAttribute("id", id);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		selectedPanel = "findTaskList";
		
		return "postCredentialAmend";
	}
	
	public String amendCredential(){
		Integer id = Integer.parseInt(request.getParameter("id"));
		if (credentialUploadEntityList != null && credentialUploadEntityList.size() > 0) {
			for (int i = 0; i < credentialUploadEntityList.size(); i++) {
				credentialUploadEntity = credentialUploadEntityList.get(i);
				if(credentialUploadEntity !=null){
					Integer credentialUploadId = credentialUploadEntityList.get(i).getId();
					CredentialUploadEntity credentialUpload = processService.getCredentialUploadEntityById(credentialUploadId);
					if(credentialUpload==null){
						try {
							processService.addCredentialUploadEntity(credentialUploadEntity);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						try {
							processService.updateCredentialUploadEntity(credentialUploadEntity,id);
						} catch (Exception e) {
							e.printStackTrace();
							StringWriter sw = new StringWriter();
							e.printStackTrace(new PrintWriter(sw, true));
							logger.error(sw.toString());
						}
					}
				}
			}
		}
		String taskId = request.getParameter("taskId");
		processService.updateCredential(id, 2);
		
		taskService.complete(taskId);
		return "amendCredential";
	}
	
	public void deleteCredentialUpload(){
		Integer id = Integer.parseInt(request.getParameter("credentialUploadId"));
		try{
			processService.deleteCredentialUpload(id);
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		printByJson(1);
	}
	
	public String queryLog(){
		String credentialEntityId  = request.getParameter("credentialEntityId");
		Integer id = Integer.parseInt(credentialEntityId);
		List<CredentialUploadEntity> list = null;
		try{
			list = processService.getCredentialUploadEntityBy(id);
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		request.setAttribute("list", list);
		selectedPanel = "postCredentialApplyList";
		return "queryLog";
	}

	@Setter
	@Getter
	private ByteArrayInputStream excelFile;
	
	@Setter
	@Getter
	private String excelFileName;
	
	public String exportStaffQueryVOToExcel(){
		try {
			if(staffQueryVO.getName()!=null){
				String trans = new String(staffQueryVO.getName().getBytes("ISO-8859-1"), "UTF-8");
				String name = URLDecoder.decode(trans, "UTF-8");
				staffQueryVO.setName(name);
			}
			XSSFWorkbook workbook = staffService.exportStaffQueryVO(staffQueryVO);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("员工档案.xlsx".getBytes(), "ISO8859-1");
			
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "导出excel失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "exportStaffQueryVOToExcel";
	}
	public String exportResignationVOToExcel(){
		try {
			if(resignationVO.getRequestUserName()!=null){
				String trans = new String(resignationVO.getRequestUserName().getBytes("ISO-8859-1"), "UTF-8");
				String requestUserName = URLDecoder.decode(trans, "UTF-8");
				resignationVO.setRequestUserName(requestUserName);
			}
			if(resignationVO.getStatus()!=null){
				String trans = new String(resignationVO.getStatus().getBytes("ISO-8859-1"), "UTF-8");
				String status = URLDecoder.decode(trans, "UTF-8");
				resignationVO.setStatus(status);
			}
			if(resignationVO.getAssigneeUserName()!=null){
				String trans = new String(resignationVO.getAssigneeUserName().getBytes("ISO-8859-1"), "UTF-8");
				String assigneeUserName = URLDecoder.decode(trans, "UTF-8");
				resignationVO.setAssigneeUserName(assigneeUserName);
			}
			XSSFWorkbook workbook = resignationService.exportResignationVO(resignationVO);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("离职人员列表.xlsx".getBytes(),"ISO8859-1");
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "导出excel失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "exportResignationVOToExcel";
	}
	public String downloadStaffCardInfos(){
		try {
			if(staffQueryVO.getName()!=null){
				String trans = new String(staffQueryVO.getName().getBytes("ISO-8859-1"), "UTF-8");
				String name = URLDecoder.decode(trans, "UTF-8");
				staffQueryVO.setName(name);
			}
			inputStream = staffService.downloadStaffCardInfos(staffQueryVO);
			// 解决中文乱码
			downloadFileName = new String(("胸卡信息.zip").getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "导出胸卡信息失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "downloadStaffCardInfos";
	}
}
