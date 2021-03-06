package com.zhizaolian.staff.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.GradeDao;
import com.zhizaolian.staff.dao.GroupDetailDao;
import com.zhizaolian.staff.dao.NicknameDao;
import com.zhizaolian.staff.dao.PermissionMembershipDao;
import com.zhizaolian.staff.dao.PositionDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.dao.WorkExperienceDao;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.ContractEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.GroupDetailEntity;
import com.zhizaolian.staff.entity.NicknameEntity;
import com.zhizaolian.staff.entity.PositionEntity;
import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.entity.StaffSalaryChangeLogEntity;
import com.zhizaolian.staff.entity.StaffSalaryEntity;
import com.zhizaolian.staff.entity.WorkExperienceEntity;
import com.zhizaolian.staff.enums.AuditStatusEnum;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.NicknameStatusEnum;
import com.zhizaolian.staff.enums.NicknameTypeEnum;
import com.zhizaolian.staff.enums.PositionEnum;
import com.zhizaolian.staff.enums.StaffBodyCheckEnum;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.service.ContractService;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffSalaryService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.NicknameVOTransformer;
import com.zhizaolian.staff.transformer.StaffVOTransformer;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.DesUtil;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.FileUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.ObjectByteArrTransformer;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.utils.ZipUtil;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.NicknameVO;
import com.zhizaolian.staff.vo.PositionVO;
import com.zhizaolian.staff.vo.StaffAuditVO;
import com.zhizaolian.staff.vo.StaffInfoAlterationVO;
import com.zhizaolian.staff.vo.StaffQueryVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;

import lombok.Cleanup;

public class StaffServiceImpl implements StaffService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffDao staffDao;
	@Autowired
	private GroupDetailDao groupDetailDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private PositionDao positionDao;
	@Autowired
	private GradeDao gradeDao;
	@Autowired
	private NicknameDao nicknameDao;
	@Autowired
	private WorkExperienceDao workExperienceDao;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private PermissionMembershipDao permissionMembershipDao;
	@Autowired
	private StaffSalaryService staffSalaryService;
	@Autowired
	private ContractService contractService;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<SalaryDetailEntity> getSalarys(String userId) {
		String hql = "from SalaryDetailEntity s where s.userId='" + userId
				+ "' and confirm=1 order by s.year desc,s.month desc  ";
		List<SalaryDetailEntity> salaryDetailEntities = (List<SalaryDetailEntity>) baseDao.hqlfind(hql);
		if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(salaryDetailEntities)) {
			salaryDetailEntities = salaryDetailEntities.subList(0,
					salaryDetailEntities.size() > 5 ? 5 : salaryDetailEntities.size());
			for (SalaryDetailEntity salaryDetailEntity : salaryDetailEntities) {
				if (salaryDetailEntity.getDetail() != null && salaryDetailEntity.getDetail().length > 0)
					try {
						salaryDetailEntity.setDetailList(
								(List<String>) ObjectByteArrTransformer.toObject(salaryDetailEntity.getDetail()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			return salaryDetailEntities;
		}
		return new ArrayList<SalaryDetailEntity>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getItemMonthSalaryResult(String userId, String year, String month) {

		String hql = "from SalaryDetailEntity s where s.userId='" + userId + "' and s.year=" + year + " and s.month="
				+ month + " order by s.year,s.month desc  ";
		List<SalaryDetailEntity> salaryDetailEntities = (List<SalaryDetailEntity>) baseDao.hqlfind(hql);
		if (CollectionUtils.isEmpty(salaryDetailEntities)) {
			return null;
		} else {
			try {
				return (List<String>) ObjectByteArrTransformer.toObject(salaryDetailEntities.get(0).getDetail());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void saveSalarys(SalaryDetailEntity salaryDetailEntity) {
		baseDao.hqlUpdate(salaryDetailEntity);
	}

	@Override
	public String[] getSalaryMobileDetail(String year, String month) {
		// 查询一共多少员工
		String sumStaffCountSql = " SELECT count(*) from ACT_ID_USER u,OA_Staff s where u.ID_=s.UserID and u.LAST_ is not null and s.Status !=4 ";
		// 查询一共有多少员工上传了工资数据
		String existsPersonSql = "SELECT\n" + "	count(*)\n" + "FROM\n" + "	ACT_ID_USER u,\n" + "	OA_Staff s\n"
				+ "WHERE\n" + "	u.ID_ = s.UserID\n" + "AND u.LAST_ IS NOT NULL\n" + "and s.`Status` !=4\n"
				+ "AND EXISTS (\n" + "	SELECT\n" + "		*\n" + "	FROM\n" + "		OA_SalaryDetail d\n"
				+ "	WHERE \n" + "		d.userId =s.UserID and  d.year=" + year + " and d.month=" + month + ") ";
		// 查询一共有多少员工已经上传了
		String remainPersonSql = "SELECT\n" + "	count(*)\n" + "FROM\n" + "	ACT_ID_USER u,\n" + "	OA_Staff s\n"
				+ "WHERE\n" + "	u.ID_ = s.UserID\n" + "AND u.LAST_ IS NOT NULL\n" + "and s.`Status` !=4\n"
				+ "AND EXISTS (\n" + "	SELECT\n" + "		*\n" + "	FROM\n" + "		OA_SalaryDetail d\n"
				+ "	WHERE \n" + "		d.userId =s.UserID and d.mobileSend =1 and d.year=" + year + " and d.month="
				+ month + " ) ";

		return new String[] { Integer.parseInt((baseDao.getUniqueResult(sumStaffCountSql) + "")) + "",
				Integer.parseInt((baseDao.getUniqueResult(existsPersonSql) + "")) + "",
				Integer.parseInt((baseDao.getUniqueResult(remainPersonSql) + "")) + "" };
	}

	@Override
	public String[] getSalaryEmailDetail(String year, String month) {
		// 查询一共多少员工
		String sumStaffCountSql = " SELECT count(*) from ACT_ID_USER u,OA_Staff s where u.ID_=s.UserID and u.LAST_ is not null and s.Status !=4 ";
		// 查询一共有多少员工上传了工资数据
		String existsPersonSql = "SELECT\n" + "	count(*)\n" + "FROM\n" + "	ACT_ID_USER u,\n" + "	OA_Staff s\n"
				+ "WHERE\n" + "	u.ID_ = s.UserID\n" + "AND u.LAST_ IS NOT NULL\n" + "and s.`Status` !=4\n"
				+ "AND EXISTS (\n" + "	SELECT\n" + "		*\n" + "	FROM\n" + "		OA_SalaryDetail d\n"
				+ "	WHERE \n" + "		d.userId =s.UserID and d.year=" + year + " and d.month=" + month + ") ";
		// 查询一共有多少员工已经上传了
		String remainPersonSql = "SELECT\n" + "	count(*)\n" + "FROM\n" + "	ACT_ID_USER u,\n" + "	OA_Staff s\n"
				+ "WHERE\n" + "	u.ID_ = s.UserID\n" + "AND u.LAST_ IS NOT NULL\n" + "and s.`Status` !=4\n"
				+ "AND EXISTS (\n" + "	SELECT\n" + "		*\n" + "	FROM\n" + "		OA_SalaryDetail d\n"
				+ "	WHERE \n" + "		d.userId =s.UserID and d.emailSend =1  and d.year=" + year + " and d.month="
				+ month + " ) ";

		return new String[] { Integer.parseInt((baseDao.getUniqueResult(sumStaffCountSql) + "")) + "",
				Integer.parseInt((baseDao.getUniqueResult(existsPersonSql) + "")) + "",
				Integer.parseInt((baseDao.getUniqueResult(remainPersonSql) + "")) + "" };
	}

	@Override
	public int addStaff(StaffVO staffVO) throws Exception {
		if (staffVO == null) {
			throw new RuntimeException("获取员工信息失败！");
		}
		User user = identityService.newUser(UUID.randomUUID().toString());
		staffVO.setUserID(user.getId());
		user.setFirstName(staffVO.getStaffNumber());
		user.setLastName(staffVO.getLastName());  //初始用户名为中文名
		user.setPassword(DesUtil.encrypt("Zzl"+staffVO.getTelephone().substring(7)));  //初始密码为Zzl+手机号后四位
		identityService.saveUser(user);
		if (staffVO.getPicture() != null) {
			identityService.setUserPicture(user.getId(), convertFileToPicture(staffVO.getPicture(), staffVO.getPictureExt(), user.getLastName()+" image"));
		}
		int attachmentId = 0;
		if(staffVO.getRegistrationForm() != null){
			String fileName = staffVO.getRegistrationFormFileName();
			File parent = new File(Constants.HR_FILE_DIRECTORY);
			parent.mkdirs();
			String saveName = UUID.randomUUID().toString().replaceAll("-", "");
			@Cleanup
			InputStream in = new FileInputStream(staffVO.getRegistrationForm());
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
					Constants.HR_FILE_DIRECTORY + saveName);
			commonAttachment.setSoftName(fileName);
			commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()));
			attachmentId = noticeService.saveAttachMent(commonAttachment);
		}
		int weixinCodeId = 0;
		if (staffVO.getWeixinCode() != null) {
			String fileName = staffVO.getWeixinCodeFileName();
			File parent = new File(Constants.HR_FILE_DIRECTORY);
			parent.mkdirs();
			String saveName = UUID.randomUUID().toString().replaceAll("-", "");
			@Cleanup
			InputStream in = new FileInputStream(staffVO.getWeixinCode());
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
			commonAttachment.setSoftURL(Constants.HR_FILE_DIRECTORY + saveName);
			commonAttachment.setSoftName(fileName);
			commonAttachment.setSuffix(fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()));
			weixinCodeId = noticeService.saveAttachMent(commonAttachment);
		}
		// 保存员工基本信息
		Date now = new Date();
		StaffEntity	staffEntity =StaffEntity.builder()
				.userID(user.getId())
				.staffName(staffVO.getLastName())
				.positionCategory(staffVO.getPositionCategory())
				.gender(staffVO.getGender())
				.birthday(DateUtil.getSimpleDate(staffVO.getBirthday()))
				.telephone(staffVO.getTelephone())
				.idNumber(staffVO.getIdNumber())
				.education(staffVO.getEducation())
				.major(staffVO.getMajor())
				.school(staffVO.getSchool())
				.graduationDate(DateUtil.getSimpleDate(staffVO.getGraduationDate()))
				.educationID(staffVO.getEducationID())
				.degreeID(staffVO.getDegreeID())
				.criminalRecord(staffVO.getCriminalRecord())
				.maritalStatus(staffVO.getMaritalStatus())
				.nativePlace(staffVO.getNativePlace())
				.address(staffVO.getAddress())
				.emergencyContract(staffVO.getEmergencyContract())
				.emergencyPhone(staffVO.getEmergencyPhone())
				.entryDate(DateUtil.getSimpleDate(staffVO.getEntryDate()))
				.gradeID(staffVO.getGradeID())
				.salary(staffVO.getSalary())
				.status(staffVO.getStatus())
				.auditStatus(AuditStatusEnum.NO_AUDIT.getValue())
				.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
				.companyPhone(staffVO.getCompanyPhone())
				.email(staffVO.getEmail())
				.starSign(staffVO.getStarSign())
				.insurance(staffVO.getInsurance())
				.standardSalary(staffVO.getStandardSalary())
				.bank(staffVO.getBank())
				.bankAccount(staffVO.getBankAccount())
				.managePersonNum(staffVO.getManagePersonNum())
				.addTime(now)
				.updateTime(now)
				.build();
		if(attachmentId != 0){
			staffEntity.setRegistrationFormId(String.valueOf(attachmentId));
		}
		if(staffVO.getCheckItem().length>0){
			staffEntity.setCheckItems(StringUtils.join(staffVO.getCheckItem(), ","));
		}
		staffSalaryService.updateStaffSalaryUserId(user.getId(), staffVO.getStaffSalaryId());
		if(weixinCodeId != 0){
			staffEntity.setWeixinCodeId(String.valueOf(weixinCodeId));
		}
		int staffID = staffDao.save(staffEntity);
		//设置与员工组的关系
		Group group = getGroup(staffVO.getCompanyID(), staffVO.getDepartmentID(), staffVO.getPositionID());
		identityService.createMembership(user.getId(), group.getId());
		return staffID;
	}


	@Override
	public User loginValidate(String userName, String password) {
		List<User> users = identityService.createUserQuery().userLastName(userName).list();
		if (CollectionUtils.isEmpty(users)) {
			throw new RuntimeException("用户不存在！");
		}

		for (User user : users) {
			if (password.equals(DesUtil.decrypt(user.getPassword()))) {
				return user;
			}

			// if (password.equals(user.getPassword())) {
			// return user;
			// }
		}

		throw new RuntimeException("密码错误！");

	}

	@Override
	public String getLatestStaffNumber() {
		StaffEntity staffEntity = staffDao.getLatestStaff();
		if (staffEntity == null) {
			return null;
		}
		User user = identityService.createUserQuery().userId(staffEntity.getUserID()).singleResult();
		if (user == null) {
			return null;
		}
		return user.getFirstName();
	}

	@Override
	public List<Group> findGroups(String userId) {
		return identityService.createGroupQuery().groupMember(userId).list();
	}

	@Override
	public List<Object> getUnPostMobiles(String year, String month) {
		String sql = "SELECT\n" + "	s.UserID,\n" + "	s.telephone\n" + "FROM\n" + "	ACT_ID_USER u,\n"
				+ "	OA_Staff s\n" + "WHERE\n" + "	u.ID_ = s.UserID\n" + "AND u.LAST_ IS NOT NULL\n"
				+ "AND s. STATUS != 4\n" + "AND  EXISTS (\n" + "	SELECT\n" + "		*\n" + "	FROM\n"
				+ "		OA_SalaryDetail d\n" + "	WHERE\n" + "		d.userId = s.userId\n" + "	AND d. YEAR = "
				+ year + "\n" + "	AND d. MONTH = " + month + "\n" + "	AND (\n" + "		d.mobileSend != 1\n"
				+ "		OR d.mobileSend IS NULL\n" + "	)\n" + ")";
		return baseDao.findBySql(sql);
	}

	@Override
	public List<Object> getUnPostEmails(String year, String month) {
		String sql = "SELECT\n" + "	s.UserID,Email\n" + "FROM\n" + "	ACT_ID_USER u,\n" + "	OA_Staff s\n"
				+ "WHERE\n" + "	u.ID_ = s.UserID\n" + "AND u.LAST_ IS NOT NULL\n" + "AND s. STATUS != 4\n"
				+ "AND  EXISTS (\n" + "	SELECT\n" + "		*\n" + "	FROM\n" + "		OA_SalaryDetail d\n"
				+ "	WHERE\n" + "		d.userId = s.userId\n" + "	AND (\n" + "		d.emailSend != 1\n"
				+ "		OR d.emailSend IS NULL and d.month=" + month + " and d.year=" + year + " \n" + "	)\n" + ")";
		return baseDao.findBySql(sql);
	}

	@Override
	public void completeEmail(String year, String month, String usrId) {
		String sql = "update SalaryDetailEntity s set s.emailSend=1 where s.month=" + month + " and s.userId='" + usrId
				+ "' and  s.year=" + year;
		baseDao.excuteHql(sql);
	}

	@Override
	public void completeMobile(String year, String month, String usrId) {
		String sql = "update SalaryDetailEntity s set s.mobileSend=1 where s.month=" + month + " and s.userId='" + usrId
				+ "' and  s.year=" + year;
		baseDao.excuteHql(sql);
	}

	@Override
	public StaffVO getStaffByUserID(String userID) {
		StaffEntity staffEntity = staffDao.getStaffByUserID(userID);
		if (staffEntity == null) {
			throw new RuntimeException("获取个人信息失败！" + userID);
		}

		StaffVO staffVO = StaffVOTransformer.INSTANCE.apply(staffEntity);

		List<GroupDetailVO> groups = findGroupDetailsByUserID(userID);
		if (groups.size() > 0) {
			// 存在多个职位，以总部的职位优先
			GroupDetailVO group = null;
			for (GroupDetailVO _group : groups) {
				group = _group;
				if (CompanyIDEnum.QIAN.getValue() == group.getCompanyID()) {
					break;
				}
			}
			Integer positionID = group.getPositionID();
			staffVO.setPositionName(positionDao.getPositionByPositionID(positionID).getPositionName());
			Integer companyID = group.getCompanyID();
			staffVO.setCompanyName(companyDao.getCompanyByCompanyID(companyID).getCompanyName());
			staffVO.setCompanyID(companyDao.getCompanyByCompanyID(companyID).getCompanyID());
			Integer departId = group.getDepartmentID();
			staffVO.setDepartmentName(departmentDao.getDepartmentByDepartmentID(departId).getDepartmentName());
		}
		staffVO.setGradeName(gradeDao.getGradeByGradeID(staffVO.getGradeID()).getGradeName());
		return staffVO;
	}

	@Override
	public void updateStaff(StaffVO staffVO) throws Exception {
		if (staffVO == null) {
			throw new RuntimeException("获取修改信息失败！");
		}

		User user = identityService.createUserQuery().userId(staffVO.getUserID()).singleResult();
		StaffEntity staffEntity = staffDao.getStaffByUserID(staffVO.getUserID());
		if (user == null || staffEntity == null) {
			throw new RuntimeException("获取个人信息失败！");
		}

		user.setFirstName(staffVO.getStaffNumber());
		identityService.saveUser(user);
		if (staffVO.getPicture() != null) {
			Picture test = convertFileToPicture(staffVO.getPicture(), staffVO.getPictureExt(),
					staffVO.getLastName() + " image");
			identityService.setUserPicture(staffVO.getUserID(), test);
		}
		staffEntity.setStaffName(staffVO.getLastName());
		staffEntity.setPositionCategory(staffVO.getPositionCategory());
		staffEntity.setGender(staffVO.getGender());
		staffEntity.setBirthday(DateUtil.getSimpleDate(staffVO.getBirthday()));
		staffEntity.setTelephone(staffVO.getTelephone());
		staffEntity.setIdNumber(staffVO.getIdNumber());
		staffEntity.setEducation(staffVO.getEducation());
		staffEntity.setMajor(staffVO.getMajor());
		staffEntity.setSchool(staffVO.getSchool());
		staffEntity.setGraduationDate(DateUtil.getSimpleDate(staffVO.getGraduationDate()));
		staffEntity.setDegreeID(staffVO.getDegreeID());// 设置学位证书编号
		staffEntity.setEducationID(staffVO.getEducationID());// 设置学历证书编号
		staffEntity.setMaritalStatus(staffVO.getMaritalStatus());
		staffEntity.setNativePlace(staffVO.getNativePlace());
		staffEntity.setCriminalRecord(staffVO.getCriminalRecord());// 设置犯罪记录
		staffEntity.setAddress(staffVO.getAddress());
		staffEntity.setEmergencyContract(staffVO.getEmergencyContract());
		staffEntity.setEmergencyPhone(staffVO.getEmergencyPhone());
		staffEntity.setEntryDate(DateUtil.getSimpleDate(staffVO.getEntryDate()));
		staffEntity.setGradeID(staffVO.getGradeID());
		staffEntity.setSalary(staffVO.getSalary());
		staffEntity.setStatus(staffVO.getStatus());
		staffEntity.setEmail(staffVO.getEmail());
		staffEntity.setStarSign(staffVO.getStarSign());
		staffEntity.setRegistrationFormId(staffVO.getRegistrationFormId());
		staffEntity.setWeixinCodeId(staffVO.getWeixinCodeId());
		staffEntity.setStandardSalary(staffVO.getStandardSalary());
		staffEntity.setPerformance(staffVO.getPerformance());
		if (null != staffVO.getFormalDate()) {
			staffEntity.setFormalDate(DateUtil.getSimpleDate(staffVO.getFormalDate()));
		}
		staffEntity.setUpdateTime(new Date());
		if (staffVO.getAttachementNames() != null) {
			staffEntity.setAttachementNames(staffVO.getAttachementNames());
		}
		staffEntity.setInsurance(staffVO.getInsurance());
		staffEntity.setPerformance(staffVO.getPerformance());
		staffEntity.setStandardSalary(staffVO.getStandardSalary());
		staffEntity.setBank(staffVO.getBank());
		staffEntity.setBankAccount(staffVO.getBankAccount());
		staffEntity.setManagePersonNum(staffVO.getManagePersonNum());
		if(staffVO.getCheckItem().length>0){
			staffEntity.setCheckItems(StringUtils.join(staffVO.getCheckItem(), ","));
		}
		staffDao.save(staffEntity);
	}

	@Override
	public List<StaffVO> findStaffPageListByStatusList(List<Integer> statusList, int page, int limit) {
		List<StaffEntity> staffEntities = staffDao.findStaffPageListByStatusList(statusList, page, limit);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>(); // 此处wjp添加
		// 添加此段代码的目的是将gradeName添加到staffVO中
		staffVOs = Lists2.transform(staffEntities, StaffVOTransformer.INSTANCE);//
		for (StaffVO staffVO : staffVOs) {
			String gradeName = gradeDao.getGradeByGradeID(staffVO.getGradeID()).getGradeName();
			staffVO.setGradeName(gradeName);
			staffVO.setPartner(isPartner(staffVO.getUserID()));
			List<GroupDetailVO> groups = this.findGroupDetailsByUserID(staffVO.getUserID());
			String personalPost = "";
			for(int i =0;i<groups.size();i++){
				if(i==(groups.size()-1)){
					personalPost+=groups.get(i).getCompanyName()+"-"
							+groups.get(i).getDepartmentName()+"-"
							+groups.get(i).getPositionName();
				}else{
					personalPost+=groups.get(i).getCompanyName()+"-"
							+groups.get(i).getDepartmentName()+"-"
							+groups.get(i).getPositionName()+";";
				}
			}
			staffVO.setPersonalPost(personalPost);
		} // 至此处是wjp添加
		return staffVOs;
	}

	@Override
	public int countStaffByStatusList(List<Integer> statusList) {
		return staffDao.countStaffByStatusList(statusList);
	}

	@Override
	public void deleteStaffLoginAccount(String userID) {
		User user = identityService.createUserQuery().userId(userID).singleResult();
		if (user == null) {
			throw new RuntimeException("获取个人信息失败！");
		}

		user.setLastName(null);
		user.setPassword(null);
		identityService.saveUser(user);
	}

	@Override
	public void updateStaffStatus(String userID, StaffStatusEnum status) {
		if (status == null) {
			throw new RuntimeException("给定员工在职状态不合法！");
		}

		StaffEntity staffEntity = staffDao.getStaffByUserID(userID);
		if (staffEntity == null) {
			throw new RuntimeException("获取个人信息失败！");
		}

		staffEntity.setStatus(status.getValue());
		staffEntity.setUpdateTime(new Date());
		staffDao.save(staffEntity);
	}

	@Override
	public void doLeave(String userID, Date leaveDate) {
		StaffEntity staffEntity = staffDao.getStaffByUserID(userID);
		if (staffEntity == null) {
			throw new RuntimeException("获取个人信息失败！");
		}

		staffEntity.setStatus(StaffStatusEnum.LEAVE.getValue());
		staffEntity.setLeaveDate(leaveDate);
		staffEntity.setUpdateTime(new Date());
		staffDao.save(staffEntity);
	}

	@Override
	public void updateStaffStatusFormalDate(String userID, StaffStatusEnum status, Date formalDate) {
		if (status == null) {
			throw new RuntimeException("给定员工在职状态不合法！");
		}

		StaffEntity staffEntity = staffDao.getStaffByUserID(userID);
		if (staffEntity == null) {
			throw new RuntimeException("获取个人信息失败！");
		}

		staffEntity.setStatus(status.getValue());
		staffEntity.setFormalDate(formalDate);
		staffEntity.setUpdateTime(new Date());
		staffDao.save(staffEntity);
	}

	@Override
	public void deleteStaff(String userID) {
		User user = identityService.createUserQuery().userId(userID).singleResult();
		if (user != null) {
			identityService.deleteUser(userID);
		}

		StaffEntity staffEntity = staffDao.getStaffByUserID(userID);
		if (staffEntity != null) {
			staffEntity.setIsDeleted(IsDeletedEnum.DELETED.getValue());
			staffEntity.setUpdateTime(new Date());
			staffDao.save(staffEntity);
		}
	}

	@Override
	public ListResult<StaffVO> findStaffPageListByQueryVO(StaffQueryVO staffQueryVO, int page, int limit) {
		if (staffQueryVO == null) {
			throw new RuntimeException("获取查询条件失败！");
		}

		List<Object> result = baseDao.findPageList(getQuerySqlByStaffQueryVO(staffQueryVO), page, limit);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID((String) objs[0]);
			staffVO.setLastName((String) objs[1]);
			staffVO.setGender((String) objs[2]);
			staffVO.setTelephone((String) objs[3]);
			staffVO.setEntryDate(DateUtil.formateDate((Date) objs[4]));
			staffVO.setPositionCategory(Integer.parseInt(objs[5].toString()));
			staffVO.setStaffID((Integer) objs[6]);
			staffVO.setPersonalPost((String) objs[7]);
			staffVO.setPartner(isPartner((String) objs[0]));
			staffVOs.add(staffVO);
		}

		Object resultObjs = baseDao.getUniqueResult(getQueryCountSqlByStaffQueryVO(staffQueryVO));
		int count = resultObjs == null ? 0 : ((BigInteger) resultObjs).intValue();

		return new ListResult<StaffVO>(staffVOs, count);
	}

	@Override
	public ListResult<StaffVO> findStaffPageList(String name, AuditStatusEnum auditStatus, Integer companyID,
			Integer departmentID, int page, int limit) {
		List<Object> result = baseDao.findPageList(getQuerySql(name, auditStatus, companyID, departmentID), page,
				limit);
		List<StaffVO> staffVOs = Lists2.transform(result, new SafeFunction<Object, StaffVO>() {
			@Override
			protected StaffVO safeApply(Object input) {
				StaffVO output = new StaffVO();
				Object[] objs = (Object[]) input;
				output.setStaffID((Integer) objs[0]);
				output.setLastName((String) objs[1]);
				output.setGender((String) objs[2]);
				output.setTelephone((String) objs[3]);
				output.setEducation(objs[4] == null ? "" : (String) objs[4]);
				output.setMajor(objs[5] == null ? "" : (String) objs[5]);
				output.setSchool(objs[6] == null ? "" : (String) objs[6]);
				output.setGraduationDate(objs[7] == null ? "" : DateUtil.formateDate((Date) objs[7]));
				output.setAuditStatus(Integer.parseInt(objs[8].toString()));
				output.setEducationID(objs[9] == null ? "" : (String) objs[9]);
				output.setDegreeID(objs[10] == null ? "" : (String) objs[10]);
				output.setCriminalRecord(objs[11] == null ? "" : (String) objs[11]);
				output.setUserID(objs[12] == null ? "" : (String) objs[12]); // 给stsffVO设置UserID
				return output;
			}
		});

		Object countObject = baseDao.getUniqueResult(getQueryCountSql(name, auditStatus, companyID, departmentID));
		int count = countObject == null ? 0 : ((BigInteger) countObject).intValue();

		return new ListResult<StaffVO>(staffVOs, count);
	}

	@Override
	public List<GroupDetailVO> findGroupDetailsByUserID(String userID) {
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		List<String> groupIDs = Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'" + input.getId() + "'";
			}
		});
		if (CollectionUtils.isEmpty(groupIDs)) {
			return Collections.emptyList();
		}

		String arrayString = Arrays.toString(groupIDs.toArray());
		String sql = "select gd.GroupDetailID, gd.GroupID, gd.CompanyID, cp.CompanyName, gd.DepartmentID, dp.DepartmentName, gd.PositionID, ps.PositionName, gd.Responsibility"
				+ " from OA_GroupDetail gd, OA_Company cp, OA_Department dp, OA_Position ps where"
				+ " gd.IsDeleted = 0 and cp.IsDeleted = 0 and dp.IsDeleted = 0 and ps.IsDeleted = 0"
				+ " and gd.CompanyID = cp.CompanyID and gd.DepartmentID = dp.DepartmentID and gd.PositionID = ps.PositionID"
				+ " and gd.GroupID in (" + arrayString.substring(1, arrayString.length() - 1) + ")";

		List<Object> resultList = baseDao.findBySql(sql);
		//resultList = resultList.subList(0, resultList.size() > 5 ? 5 : resultList.size());
		List<GroupDetailVO> groupDetailVOs = new ArrayList<GroupDetailVO>();
		for (Object object : resultList) {
			Object[] objs = (Object[]) object;
			GroupDetailVO groupDetailVO = new GroupDetailVO();
			groupDetailVO.setGroupDetailID((Integer) objs[0]);
			groupDetailVO.setGroupID((String) objs[1]);
			groupDetailVO.setCompanyID((Integer) objs[2]);
			groupDetailVO.setCompanyName((String) objs[3]);
			groupDetailVO.setDepartmentID((Integer) objs[4]);
			groupDetailVO.setDepartmentName((String) objs[5]);
			groupDetailVO.setPositionID((Integer) objs[6]);
			groupDetailVO.setPositionName((String) objs[7]);
			groupDetailVO.setResponsibility(objs[8] == null ? "" : (String) objs[8]);
			groupDetailVOs.add(groupDetailVO);
		}
		return groupDetailVOs;
	}

	@Override
	public List<NicknameVO> findNicknamesByType(NicknameTypeEnum nicknameType) {
		if (nicknameType == null) {
			return Collections.emptyList();
		}

		List<NicknameEntity> nicknameEntities = nicknameDao.findNicknamesByType(nicknameType.getValue());
		return Lists2.transform(nicknameEntities, NicknameVOTransformer.INSTANCE);
	}

	@Override
	public NicknameVO getNicknameByID(int nicknameID) {
		NicknameEntity nicknameEntity = nicknameDao.getNicknameByID(nicknameID);
		return NicknameVOTransformer.INSTANCE.apply(nicknameEntity);
	}

	@Override
	public void updateNicknameStatus(int nicknameID, NicknameStatusEnum status) {
		if (status == null) {
			throw new RuntimeException("花名使用状态不合法！");
		}

		NicknameEntity nicknameEntity = nicknameDao.getNicknameByID(nicknameID);
		if (nicknameEntity == null) {
			throw new RuntimeException("获取花名信息失败！");
		}

		nicknameEntity.setStatus(status.getValue());
		nicknameEntity.setUpdateTime(new Date());
		nicknameDao.save(nicknameEntity);
	}

	@Override
	public void updateAuditResult(String userID, String educationID, String degreeID, String criminalRecord,
			AuditStatusEnum auditStatus) {
		if (auditStatus == null) {
			throw new RuntimeException("审核状态不合法！");
		}
		StaffEntity staffEntity = staffDao.getStaffByUserID(userID);
		if (staffEntity == null) {
			throw new RuntimeException("员工不存在！");
		}

		staffEntity.setEducationID(educationID);
		staffEntity.setDegreeID(degreeID);
		staffEntity.setCriminalRecord(criminalRecord);
		staffEntity.setAuditStatus(auditStatus.getValue());
		staffEntity.setUpdateTime(new Date());
		staffDao.save(staffEntity);
	}

	/*
	 * 此方法是审核状态改变的方法
	 */
	@Override
	public void updateAuditResult(String staffID, AuditStatusEnum auditStatus) {
		if (auditStatus == null) {
			throw new RuntimeException("审核状态不合法！");
		}
		StaffEntity staffEntity = staffDao.getStaffByStaffID(Integer.parseInt(staffID));
		if (staffEntity == null) {
			throw new RuntimeException("员工不存在！");
		}
		staffEntity.setAuditStatus(auditStatus.getValue());
		staffEntity.setUpdateTime(new Date());
		staffDao.save(staffEntity);
	}

	@Override
	public List<StaffVO> findStaffsByCompanyIDDepartmentID(int companyID, int departmentID) {
		List<DepartmentVO> departments = positionService.findDepartmentsByCompanyIDParentID(companyID, departmentID);
		List<Integer> departmentIDs = Lists2.transform(departments, new SafeFunction<DepartmentVO, Integer>() {
			@Override
			protected Integer safeApply(DepartmentVO input) {
				return input.getDepartmentID();
			}
		});
		departmentIDs.add(departmentID);

		String arrayString = Arrays.toString(departmentIDs.toArray());
		String sql = "select distinct staff.UserID, staff.StaffName, staff.telephone "
				+ "from OA_Staff staff, ACT_ID_MEMBERSHIP membership, OA_GroupDetail gdetail where staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = gdetail.GroupID "
				+ "and staff.IsDeleted = 0 and staff.Status != 4 and gdetail.IsDeleted = 0 and gdetail.CompanyID = "
				+ companyID + " and gdetail.DepartmentID in (" + arrayString.substring(1, arrayString.length() - 1)
				+ ")";
		List<Object> result = baseDao.findBySql(sql);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (Object obj : result) {
			Object[] objs = (Object[]) obj;
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID((String) objs[0]);
			staffVO.setLastName((String) objs[1]);
			staffVO.setTelephone((String) objs[2]);
			staffVOs.add(staffVO);
		}
		return staffVOs;
	}

	@Override
	public List<StaffVO> findStaffsByCompanyID(int companyID) {
		String sql = "select distinct staff.UserID, staff.StaffName, staff.telephone "
				+ "from OA_Staff staff, ACT_ID_MEMBERSHIP membership, OA_GroupDetail gdetail where staff.UserID = membership.USER_ID_ and membership.GROUP_ID_ = gdetail.GroupID "
				+ "and staff.IsDeleted = 0 and staff.Status != 4 and gdetail.IsDeleted = 0 and gdetail.CompanyID = "
				+ companyID;
		List<Object> result = baseDao.findBySql(sql);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (Object obj : result) {
			Object[] objs = (Object[]) obj;
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID((String) objs[0]);
			staffVO.setLastName((String) objs[1]);
			staffVO.setTelephone((String) objs[2]);
			staffVOs.add(staffVO);
		}
		return staffVOs;
	}
	
	@Override
	public List<StaffVO> findStaffByName(String name, int limit) {
		List<StaffEntity> staffEntities = staffDao.findStaffByName(name, limit);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (StaffEntity staffEntity : staffEntities) {
			StaffStatusEnum staffStatus = StaffStatusEnum.valueOf(staffEntity.getStatus());
			if (staffStatus == null || staffStatus == StaffStatusEnum.LEAVE) {
				continue;
			}
			List<GroupDetailVO> groups = findGroupDetailsByUserID(staffEntity.getUserID());
			if (groups.size() <= 0) {
				// 无岗位，说明是已离职员工
				continue;
			}
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID(staffEntity.getUserID());
			staffVO.setLastName(staffEntity.getStaffName());
			staffVO.setGroupDetailVOs(groups);
			staffVO.setTelephone(staffEntity.getTelephone());
			staffVOs.add(staffVO);
		}
		return staffVOs;
	}

	@Override
	public List<StaffVO> findStaffByName(String name) {
		List<StaffEntity> staffEntities = staffDao.findStaffByName(name);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (StaffEntity staffEntity : staffEntities) {
			StaffStatusEnum staffStatus = StaffStatusEnum.valueOf(staffEntity.getStatus());
			if (staffStatus == null || staffStatus == StaffStatusEnum.LEAVE) {
				continue;
			}
			List<GroupDetailVO> groups = findGroupDetailsByUserID(staffEntity.getUserID());
			if (groups.size() <= 0) {
				// 无岗位，说明是已离职员工
				continue;
			}
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID(staffEntity.getUserID());
			staffVO.setLastName(staffEntity.getStaffName());
			staffVO.setGroupDetailVOs(groups);
			staffVO.setTelephone(staffEntity.getTelephone());
			staffVOs.add(staffVO);
		}
		return staffVOs;
	}

	@Override
	public List<StaffVO> findStaffByNameAndtStatus(String name, Integer positionCategory) {
		List<StaffEntity> staffEntities = staffDao.findStaffByNameAndStatus(name, positionCategory);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (StaffEntity staffEntity : staffEntities) {
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID(staffEntity.getUserID());
			staffVO.setLastName(staffEntity.getStaffName());
			List<GroupDetailVO> groups = findGroupDetailsByUserID(staffEntity.getUserID());
			staffVO.setGroupDetailVOs(groups);
			staffVOs.add(staffVO);
		}
		return staffVOs;
	}

	@Override
	public String queryHeadMan(String userID) {
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		if (groups.size() <= 0) {
			return null;
		}

		String[] posList = groups.get(0).getType().split("_");
		int departmentID = Integer.parseInt(posList[1]);
		while (departmentID != 0) {
			PositionVO supervisor = positionService.getPositionByDepartmentIDName(departmentID, "组长");
			if (supervisor != null) {
				Group group = identityService.createGroupQuery()
						.groupType(posList[0] + "_" + departmentID + "_" + supervisor.getPositionID()).singleResult();
				if (group != null) {
					List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
					for (User user : users) {
						if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getPassword())
								&& !userID.equals(user.getId())) {
							return user.getId();
						}
					}
				}
			}

			DepartmentVO department = positionService.getDepartmentByID(departmentID);
			departmentID = department == null ? 0 : department.getParentID();
		}
		return null;
	}

	@Override
	public String querySupervisorOneStep(String userID) {
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		if (groups.size() <= 0) {
			return null;
		}

		String[] posList = groups.get(0).getType().split("_");
		int departmentID = Integer.parseInt(posList[1]);
		PositionVO supervisor = positionService.getPositionByDepartmentIDName(departmentID, "主管");
		boolean own = false;
		if (supervisor != null) {
			Group group = identityService.createGroupQuery()
					.groupType(posList[0] + "_" + departmentID + "_" + supervisor.getPositionID()).singleResult();
			if (group != null) {
				List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
				for (User user : users) {
					if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getPassword())) {
						if (!userID.equals(user.getId())) {
							return user.getId();
						} else {
							own = true;
						}
					}
				}
			}
		}
		if (own) {
			return userID;
		} else {
			supervisor = positionService.getPositionByDepartmentIDName(departmentID, "总监");
			if (supervisor != null) {
				Group group = identityService.createGroupQuery()
						.groupType(posList[0] + "_" + departmentID + "_" + supervisor.getPositionID()).singleResult();
				if (group != null) {
					List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
					for (User user : users) {
						if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getPassword())) {
							if (!userID.equals(user.getId())) {
								return user.getId();
							} else {
								own = true;
							}
						}
					}
				}
			}
			if (own) {
				return userID;
			} else {
				return null;
			}
		}
	}

	@Override
	public String querySupervisor(String userID) {
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		if (groups.size() <= 0) {
			return null;
		}
		String[] posList = groups.get(0).getType().split("_");
		int departmentID = Integer.parseInt(posList[1]);
		while (departmentID != 0) {
			PositionVO supervisor = positionService.getPositionByDepartmentIDName(departmentID, "主管");
			if (supervisor != null) {
				Group group = identityService.createGroupQuery()
						.groupType(posList[0] + "_" + departmentID + "_" + supervisor.getPositionID()).singleResult();
				if (group != null) {
					List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
					for (User user : users) {
						if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getPassword())
								&& !userID.equals(user.getId())) {
							return user.getId();
						}
					}
				}
			}
			supervisor = positionService.getPositionByDepartmentIDName(departmentID, "总监");
			if (supervisor != null) {
				Group group = identityService.createGroupQuery()
						.groupType(posList[0] + "_" + departmentID + "_" + supervisor.getPositionID()).singleResult();
				if (group != null) {
					List<User> users = identityService.createUserQuery().memberOfGroup(group.getId()).list();
					for (User user : users) {
						if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getPassword())
								&& !userID.equals(user.getId())) {
							return user.getId();
						}
					}
				}
			}
			DepartmentVO department = positionService.getDepartmentByID(departmentID);
			departmentID = department == null ? 0 : department.getParentID();
		}
		return null;
	}

	@Override
	public String queryManager(String userID) {

		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		if (groups.size() <= 0) {
			return null;
		}
		Group group = null;
		// 存在多个职位，以总部的职位优先
		for (Group _group : groups) {
			group = _group;
			String[] posList = group.getType().split("_");
			if (CompanyIDEnum.QIAN.getValue() == Integer.parseInt(posList[0])) {
				break;
			}
		}
		String[] posList = group.getType().split("_");
		int departmentID = positionService.getDepartmentIDByCompanyIDAndName(Integer.parseInt(posList[0]), "管理层");
		if (departmentID == 0) {
			departmentID = positionService.getDepartmentIDByCompanyIDAndName(Integer.parseInt(posList[0]), "总经办");
		}
		Group group1 = identityService.createGroupQuery()
				.groupType(posList[0] + "_" + departmentID + "_"
						+ positionService.getPositionByDepartmentIDName(departmentID, "总经理").getPositionID())
				.singleResult();
		if (group1 == null) {
			return null;
		}

		List<User> users = identityService.createUserQuery().memberOfGroup(group1.getId()).list();
		for (User user : users) {
			if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getPassword())) {
				return user.getId();
			}
		}

		return null;
	}

	@Override
	public List<String> queryHRGroupList(String userID) {
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		if (groups.size() <= 0) {
			return Collections.emptyList();
		}

		String[] posList = groups.get(0).getType().split("_");
		List<Integer> departmentIDs = new ArrayList<Integer>();
		int departmentID = positionService.getDepartmentIDByCompanyIDAndName(Integer.parseInt(posList[0]), "人事");
		if (departmentID == 0) {
			return Collections.emptyList();
		}
		departmentIDs.add(departmentID);
		List<DepartmentVO> departmentVOs = positionService
				.findDepartmentsByCompanyIDParentID(Integer.parseInt(posList[0]), departmentID);
		List<Integer> departmentIDList = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
			@Override
			protected Integer safeApply(DepartmentVO input) {
				return input.getDepartmentID();
			}
		});
		departmentIDs.addAll(departmentIDList);

		List<Group> groupList = new ArrayList<Group>();
		for (Integer depID : departmentIDs) {
			List<Group> groupSubList = identityService.createGroupQuery()
					.groupNameLike("%" + posList[0] + "_" + depID + "_%").list();
			if (groupSubList.size() > 0) {
				groupList.addAll(groupSubList);
			}
		}

		return Lists2.transform(groupList, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return input.getId();
			}
		});
	}

	@Override
	public List<String> queryHRGroupList(Integer companyId) {
		List<Integer> departmentIDs = new ArrayList<Integer>();
		int departmentID = positionService.getDepartmentIDByCompanyIDAndName(companyId, "人事");
		if (departmentID == 0) {
			return Collections.emptyList();
		}
		departmentIDs.add(departmentID);
		List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(companyId, departmentID);
		List<Integer> departmentIDList = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
			@Override
			protected Integer safeApply(DepartmentVO input) {
				return input.getDepartmentID();
			}
		});
		departmentIDs.addAll(departmentIDList);

		List<Group> groupList = new ArrayList<Group>();
		for (Integer depID : departmentIDs) {
			List<Group> groupSubList = identityService.createGroupQuery()
					.groupNameLike("%" + companyId + "_" + depID + "_%").list();
			if (groupSubList.size() > 0) {
				groupList.addAll(groupSubList);
			}
		}

		return Lists2.transform(groupList, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return input.getId();
			}
		});
	}

	@Override
	public boolean hasGroupMember(List<String> groups) {
		for (String group : groups) {
			List<User> users = identityService.createUserQuery().memberOfGroup(group).list();
			for (User user : users) {
				if (!StringUtils.isBlank(user.getLastName()) && !StringUtils.isBlank(user.getPassword())) {
					return true;
				}
			}
		}
		return false;
	}

	private String getQueryCountSqlByStaffQueryVO(StaffQueryVO staffQueryVO) {
		StringBuffer sql = new StringBuffer(
				"select count(distinct os.StaffID) from OA_Staff os, ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail where os.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and os.IsDeleted = 0 and os.Status != 4");
		sql.append(getWhereByStaffQueryVO(staffQueryVO));
		return sql.toString();
	}

	private String getQuerySqlByStaffQueryVO(StaffQueryVO staffQueryVO) {
		StringBuffer sql = new StringBuffer(
				"select distinct os.UserID, os.StaffName, os.Gender, os.Telephone, os.EntryDate ,os.PositionCategory ,os.StaffID "
						+ ",GROUP_CONCAT(CONCAT_WS('-',oa_company.CompanyName,oa_department.DepartmentName,oa_position.PositionName) SEPARATOR ';') "
						+ "from OA_Staff os, ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail "
						+ ",oa_company,oa_department,oa_position "
						+ "where os.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and os.IsDeleted = 0 and os.Status != 4"
						+ " AND groupDetail.CompanyID = oa_company.CompanyID AND oa_company.IsDeleted = 0"
						+ " AND groupDetail.DepartmentID = oa_department.DepartmentID AND oa_department.IsDeleted = 0"
						+ " AND groupDetail.PositionID = oa_position.PositionID AND oa_position.IsDeleted = 0 ");
		sql.append(getWhereByStaffQueryVO(staffQueryVO));
		sql.append(" GROUP BY os.UserID");
		if(StringUtils.isNotBlank(staffQueryVO.getPersonalPost())){
			sql.append(" HAVING GROUP_CONCAT(CONCAT_WS('-',oa_company.CompanyName,oa_department.DepartmentName,oa_position.PositionName) SEPARATOR ';') LIKE '%"+staffQueryVO.getPersonalPost()+"%'");
		}
		sql.append(" ORDER BY os.EntryDate ASC");
		return sql.toString();
	}

	private String getWhereByStaffQueryVO(StaffQueryVO staffQueryVO) {
		StringBuffer whereSql = new StringBuffer();
		if (!StringUtils.isBlank(staffQueryVO.getName())) {
			if (staffQueryVO.isFuzzyQuery_Name()) {
				whereSql.append(" and os.StaffName like '%" + staffQueryVO.getName() + "%'");
			} else {
				whereSql.append(" and os.StaffName = '" + staffQueryVO.getName() + "'");
			}
		}

		if (staffQueryVO.getStatus() != null) {
			whereSql.append(" and os.Status = " + staffQueryVO.getStatus());
		}
		if (staffQueryVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.CompanyID = " + staffQueryVO.getCompanyID());
			if (staffQueryVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(
						staffQueryVO.getCompanyID(), staffQueryVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(staffQueryVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("
						+ arrayString.substring(1, arrayString.length() - 1) + ")");
			}
		}
		return whereSql.toString();
	}

	/*
	 * 在sql里面加上userID的参数 也就是在sql语句中加了os.UserID
	 */
	private String getQuerySql(String name, AuditStatusEnum auditStatus, Integer companyID, Integer departmentID) {
		StringBuffer sql = new StringBuffer(
				"select distinct os.staffID, os.StaffName, os.Gender, os.Telephone, os.Education, os.Major, os.School, os.GraduationDate, os.AuditStatus, os.EducationID, os.DegreeID, os.CriminalRecord,os.UserID"
						+ " from OA_Staff os, ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail"
						+ " where os.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and os.IsDeleted = 0 and os.Status != 4 ");
		if (!StringUtils.isBlank(name)) {
			sql.append(" and os.StaffName like '%").append(name).append("%' ");
		}
		if (auditStatus != null) {
			sql.append(" and os.AuditStatus = ").append(auditStatus.getValue());
		}
		if (companyID != null) {
			sql.append(" and groupDetail.CompanyID = ").append(companyID);
			if (departmentID != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(companyID,
						departmentID);
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(departmentID);
				String arrayString = Arrays.toString(departmentIDs.toArray());
				sql.append(" and groupDetail.DepartmentID in (" + arrayString.substring(1, arrayString.length() - 1)
				+ ")");
			}
		}

		return sql.toString();
	}

	private String getQueryCountSql(String name, AuditStatusEnum auditStatus, Integer companyID, Integer departmentID) {
		StringBuffer sql = new StringBuffer("select count(distinct os.StaffID)"
				+ " from OA_Staff os, ACT_ID_MEMBERSHIP membership, ACT_ID_GROUP idGroup, OA_GroupDetail groupDetail"
				+ " where os.UserID = membership.USER_ID_ and membership.GROUP_ID_ = idGroup.ID_ and idGroup.ID_ = groupDetail.GroupID and os.IsDeleted = 0 and os.Status != 4 ");
		if (!StringUtils.isBlank(name)) {
			sql.append(" and os.StaffName like '%").append(name).append("%' ");
		}
		if (auditStatus != null) {
			sql.append(" and os.AuditStatus = ").append(auditStatus.getValue());
		}
		if (companyID != null) {
			sql.append(" and groupDetail.CompanyID = ").append(companyID);
			if (departmentID != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(companyID,
						departmentID);
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(departmentID);
				String arrayString = Arrays.toString(departmentIDs.toArray());
				sql.append(" and groupDetail.DepartmentID in (" + arrayString.substring(1, arrayString.length() - 1)
				+ ")");
			}
		}

		return sql.toString();
	}

	private Picture convertFileToPicture(File file, String ext, String picName) throws IOException {
		BufferedImage img = ImageIO.read(file);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(img, ext, output);
		byte[] picArray = output.toByteArray();
		return new Picture(picArray, picName);
	}

	@Override
	public Group getGroup(int companyID, int departmentID, int positionID) {
		String groupType = companyID + "_" + departmentID + "_" + positionID;
		Group group = identityService.createGroupQuery().groupType(groupType).singleResult();
		if (group == null) {
			group = identityService.newGroup(UUID.randomUUID().toString());
			group.setName(groupType);
			group.setType(groupType);
			identityService.saveGroup(group);
			GroupDetailEntity groupDetailEntity = GroupDetailEntity.builder().groupID(group.getId())
					.companyID(companyID).departmentID(departmentID).positionID(positionID)
					.isDeleted(IsDeletedEnum.NOT_DELETED.getValue()).addTime(new Date()).updateTime(new Date()).build();
			groupDetailDao.save(groupDetailEntity);
		}
		return group;
	}

	/*
	 * 修改员工信息表里面的薪资和职级 wjp
	 */
	@Override
	public void updateStaff(StaffInfoAlterationVO staffInfoAlterationVO) {

		StaffEntity staffEntity = staffDao.getStaffByUserID(staffInfoAlterationVO.getUserID());
		if (staffEntity == null) {
			throw new RuntimeException("获取个人信息失败！");
		}
		if (staffInfoAlterationVO.getGradeIDAfter() != null)
			staffEntity.setGradeID(staffInfoAlterationVO.getGradeIDAfter()); // 设置薪资
		if (staffInfoAlterationVO.getSalaryAfter() != null)
			staffEntity.setSalary(staffInfoAlterationVO.getSalaryAfter()); // 设置职级
		staffEntity.setUpdateTime(new Date()); // 设置更新时间
		staffDao.save(staffEntity);

	}

	@Override
	public void updateStaffInformation(StaffVO staffVO) {
		StaffEntity staffEntity = staffDao.getStaffByUserID(staffVO.getUserID());
		if (staffEntity == null) {
			throw new RuntimeException("获取个人信息失败！");
		}

		if (!StringUtils.isBlank(staffVO.getTelephone())) {
			staffEntity.setTelephone(staffVO.getTelephone());
		}
		if (!StringUtils.isBlank(staffVO.getEmergencyContract())) {
			staffEntity.setEmergencyContract(staffVO.getEmergencyContract());
		}
		if (!StringUtils.isBlank(staffVO.getEmergencyPhone())) {
			staffEntity.setEmergencyPhone(staffVO.getEmergencyPhone());
		}
		if (!StringUtils.isBlank(staffVO.getAddress())) {
			staffEntity.setAddress(staffVO.getAddress());
		}
		staffEntity.setUpdateTime(new Date());
		staffDao.save(staffEntity);
	}

	@Override
	public List<StaffVO> getStaffVOsByUsers(List<User> users) {

		List<StaffVO> staffVOs = new ArrayList<>();
		for (User user : users) {
			staffVOs.add(getStaffByUserID(user.getId()));
		}
		return staffVOs;
	}

	@Override
	public void deleteStaffMembership(String userID) {
		List<Group> groups = identityService.createGroupQuery().groupMember(userID).list();
		for (Group group : groups) {
			identityService.deleteMembership(userID, group.getId());
		}

	}

	@Override
	public ListResult<StaffVO> findStaffList(StaffVO staffVO, int limit, int page, int companyID) {
		ListResult<Object> staffEntities = staffDao.findStaffList(getQuerySqlByStaffVO(staffVO, companyID),
				getQueryCountSqlByStaffVO(staffVO, companyID), limit, page);

		List<StaffVO> list = new ArrayList<StaffVO>();
		Date date = new Date();
		for (Object obj : staffEntities.getList()) {

			StaffVO staffVO2 = new StaffVO();
			Object[] objs = (Object[]) obj;

			staffVO2.setStaffID((Integer) objs[0]);
			staffVO2.setLastName((String) objs[1]);
			staffVO2.setEntryDate1((Date) objs[2]);
			staffVO2.setDepartmentID((Integer) objs[3]);
			if (staffVO2.getDepartmentID() != null) {
				staffVO2.setDepartmentName(
						departmentDao.getDepartmentByDepartmentID(staffVO2.getDepartmentID()).getDepartmentName());
			}

			staffVO2.setDays(String.valueOf((date.getTime() - staffVO2.getEntryDate1().getTime()) / 86400000));
			list.add(staffVO2);
		}
		return new ListResult<StaffVO>(list, staffEntities.getTotalCount());

	}

	private String getQuerySqlByStaffVO(StaffVO staffVO, int companyID) {
		StringBuffer hql = new StringBuffer("select s.StaffID,s.StaffName,s.EntryDate,s.DepartmentID from("
				+ "select DISTINCT * from(select staff.StaffID,staff.StaffName,staff.EntryDate,groupDetail.DepartmentID,groupDetail.CompanyID from OA_Staff staff "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_  "
				+ "LEFT JOIN OA_GroupDetail groupDetail ON membership.GROUP_ID_ = groupDetail.GroupID "
				+ "where staff.IsDeleted = 0 and groupDetail.IsDeleted = 0 and groupDetail.CompanyID='" + companyID
				+ "' and staff.Status= 1 " + "and staff.`Status` = 1 and staff.UserID "
				+ "NOT IN (SELECT f.RequestUserID FROM OA_Formal f, ACT_HI_PROCINST p WHERE f.ProcessInstanceID = p.PROC_INST_ID_ AND p.END_ACT_ID_ IS NULL AND f.IsDeleted = 0) ");
		hql.append(getWhereByAssetUsageVO(staffVO));
		hql.append(" )a) s");
		hql.append(" GROUP BY s.StaffID order by s.EntryDate ");
		return hql.toString();

	}

	private String getQueryCountSqlByStaffVO(StaffVO staffVO, int companyID) {
		StringBuffer hql = new StringBuffer("select count(*) from ("
				+ " select staff.StaffID,staff.StaffName,staff.EntryDate,groupDetail.DepartmentID ,groupDetail.CompanyID from OA_Staff staff "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_  "
				+ "LEFT JOIN OA_GroupDetail groupDetail ON membership.GROUP_ID_ = groupDetail.GroupID "
				+ "where staff.IsDeleted = 0 and groupDetail.IsDeleted = 0 and groupDetail.CompanyID='" + companyID
				+ "' and staff.Status= 1 "
				+ "and staff.`Status` = 1 and staff.UserID NOT IN (SELECT f.RequestUserID FROM OA_Formal f, ACT_HI_PROCINST p WHERE f.ProcessInstanceID = p.PROC_INST_ID_ AND p.END_ACT_ID_ IS NULL AND f.IsDeleted = 0) "
				+ "GROUP BY staff.StaffID");
		hql.append(getWhereByAssetUsageVO(staffVO));
		hql.append(" ) s");

		return hql.toString();

	}

	private String getWhereByAssetUsageVO(StaffVO staffVO) {
		StringBuffer whereHql = new StringBuffer();

		return whereHql.toString();

	}

	@SuppressWarnings("deprecation")
	@Override
	public StaffVO getStaffByStaffID(int staffID) {

		StaffEntity staffEntity = staffDao.getStaffByStaffID(staffID);
		Date date = new Date();
		StaffVO staffVO = new StaffVO();
		if (staffEntity != null) {

			staffVO.setLastName(staffEntity.getStaffName());
			staffVO.setGender(staffEntity.getGender());

			if (staffEntity.getBirthday() != null) {

				staffVO.setAge(String.valueOf(date.getYear() - staffEntity.getBirthday().getYear()));
			} else {
				staffVO.setAge(null);
			}
/*			List<GroupDetailVO> groups = findGroupDetailsByUserID(staffEntity.getUserID());
			Integer departmentID = groups.get(0).getDepartmentID();
			staffVO.setDepartmentName(departmentDao.getDepartmentByDepartmentID(departmentID).getDepartmentName());

			Integer positionID = groups.get(0).getPositionID();
			staffVO.setGroupDetail(positionDao.getPositionByPositionID(positionID).getPositionName());*/
			int positionId = positionDao.getPositionIdByUserId(staffEntity.getUserID());
			PositionEntity position = positionDao.getPositionByPositionID(positionId);
			if(null != position){
				staffVO.setGroupDetail(position.getPositionName());
			}
			staffVO.setTelephone(staffEntity.getTelephone());

			staffVO.setEntryDate(DateUtil.formateDate(staffEntity.getEntryDate()));

/*			Integer companyID = groups.get(0).getCompanyID();
			staffVO.setCompanyID(companyDao.getCompanyByCompanyID(companyID).getCompanyID());*/
			
			String departmentNames = getStaffDepartmentNames(staffEntity.getUserID());
			staffVO.setDepartmentName(departmentNames);
			int companyId = companyDao.getCompanyIdByUserId(staffEntity.getUserID());
			staffVO.setCompanyID(companyId);
			staffVO.setSchool(staffEntity.getSchool());
			staffVO.setMajor(staffEntity.getMajor());
			staffVO.setMaritalStatus(staffEntity.getMaritalStatus());
			staffVO.setNativePlace(staffEntity.getNativePlace());
			staffVO.setEducation(staffEntity.getEducation());

			staffVO.setUserID(staffEntity.getUserID());
		}
		return staffVO;
	}
	@Override
	public String getStaffDepartmentNames(String userID) {
		Integer departmentId = departmentDao.getDeparmentIdByUserId(userID);
		//默认显示最多3级部门
		int index = 3;
		List<String> deparmentNames = new ArrayList<>();
		while(index>0){
			DepartmentEntity dep = departmentDao.getDepartmentByDepartmentID(departmentId);
			if(null == dep){
				break;
			}
			deparmentNames.add(dep.getDepartmentName());
			departmentId = dep.getParentID();
			index--;
		}
		List<String> deparmentNamesOrderByDesc = new ArrayList<>(deparmentNames.size());
		//倒序
		for(int i=0; i<deparmentNames.size(); i++){
			deparmentNamesOrderByDesc.add(i, deparmentNames.get(deparmentNames.size()-i-1));
		}
		return StringUtils.join(deparmentNamesOrderByDesc, "-");
	}

	@Override
	public ListResult<StaffVO> findStaffRegularRecord(StaffVO staffVO, int limit, int page) {
		List<Object> result = baseDao.findPageList(getRegularSqlByStaffVO(staffVO), page, limit);
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			StaffVO output = new StaffVO();
			output.setUserID((String) objs[0]);
			output.setLastName((String) objs[1]);
			output.setEntryDate(DateUtil.formateDate((Date) objs[2]));
			if (null == (Date) objs[3]) {
				continue;
			}
			output.setFormalDate(DateUtil.formateDate((Date) objs[3]));
			output.setDepartmentName(
					positionService.getDepartmentByID(Integer.parseInt(StringUtils.split((String) objs[4], ",")[0]))
					.getDepartmentName());
			output.setCompanyName(
					positionService.getCompanyByCompanyID(Integer.parseInt(StringUtils.split((String) objs[5], ",")[0]))
					.getCompanyName());
			output.setExamineDate(DateUtil.formateDate((Date) objs[6]));

			staffVOs.add(output);
		}

		Object resultObjs = baseDao.getUniqueResult(getRegularCountSqlByStaffVO(staffVO));
		int count = resultObjs == null ? 0 : ((BigInteger) resultObjs).intValue();

		return new ListResult<StaffVO>(staffVOs, count);

	}

	private String getRegularSqlByStaffVO(StaffVO staffVO) {
		StringBuffer sql = new StringBuffer(
				"select staff.UserID,staff.StaffName,staff.EntryDate,staff.formalDate,GROUP_CONCAT(groupDetail.DepartmentID),GROUP_CONCAT(groupDetail.CompanyID),procinst.END_TIME_ "
						+ "from OA_Staff staff,ACT_ID_MEMBERSHIP membership,OA_GroupDetail groupDetail,OA_Formal formal,ACT_HI_PROCINST procinst "
						+ "where staff.UserID = membership.USER_ID_  and staff.UserID = formal.requestUserID and formal.ProcessInstanceID = procinst.PROC_INST_ID_ "
						+ "and membership.GROUP_ID_ = groupDetail.GroupID and formal.IsDeleted=0 "
						+ "and staff.IsDeleted = 0 and groupDetail.IsDeleted = 0 and staff.status = 3 and formal.processStatus=1 and procinst.END_TIME_ is not null ");
		sql.append(getRegularWhere(staffVO));
		sql.append(" GROUP BY  staff.UserID order by staff.formalDate");
		return sql.toString();
	}

	private String getRegularCountSqlByStaffVO(StaffVO staffVO) {
		StringBuffer countSql = new StringBuffer(
				"select count(*) from (select staff.UserID,staff.StaffName,staff.EntryDate,staff.formalDate,GROUP_CONCAT(groupDetail.DepartmentID),GROUP_CONCAT(groupDetail.CompanyID),procinst.END_TIME_ "
						+ "from OA_Staff staff,ACT_ID_MEMBERSHIP membership,OA_GroupDetail groupDetail,OA_Formal formal,ACT_HI_PROCINST procinst "
						+ "where staff.UserID = membership.USER_ID_  and staff.UserID = formal.requestUserID and formal.ProcessInstanceID = procinst.PROC_INST_ID_ "
						+ "and membership.GROUP_ID_ = groupDetail.GroupID and formal.IsDeleted=0 "
						+ "and staff.IsDeleted = 0 and groupDetail.IsDeleted = 0 and staff.status = 3 and formal.processStatus=1 and procinst.END_TIME_ is not null");
		countSql.append(getRegularWhere(staffVO));
		countSql.append(" GROUP BY  staff.UserID) s");
		return countSql.toString();

	}

	private String getRegularWhere(StaffVO staffVO) {
		StringBuffer whereSql = new StringBuffer();
		if (staffVO.getCompanyID() != null) {
			whereSql.append(" and groupDetail.CompanyID = " + staffVO.getCompanyID());
			if (staffVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService
						.findDepartmentsByCompanyIDParentID(staffVO.getCompanyID(), staffVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs,
						new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(staffVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and groupDetail.DepartmentID in ("
						+ arrayString.substring(1, arrayString.length() - 1) + ")");
			}
		}
		if (!StringUtils.isBlank(staffVO.getFormalBeginDate())) {
			whereSql.append(" and staff.formalDate >='" + staffVO.getFormalBeginDate() + "'");
		}
		if (!StringUtils.isBlank(staffVO.getFormalEndDate())) {
			whereSql.append(" and staff.formalDate <='" + staffVO.getFormalEndDate() + " 23:59:59'");
		}
		if (!StringUtils.isBlank(staffVO.getExamineBeginDate())) {
			whereSql.append(" and procinst.END_TIME_  >='" + staffVO.getExamineBeginDate() + "'");
		}
		if (!StringUtils.isBlank(staffVO.getExamineEndDate())) {
			whereSql.append(" and procinst.END_TIME_  <='" + staffVO.getExamineEndDate() + " 23:59:59'");
		}
		return whereSql.toString();

	}

	@Override
	public StaffVO getStaffByTelephone(String telephone) {

		String sql = "select staff.Telephone,ackuser.LAST_,staff.userID from OA_Staff staff "
				+ "LEFT JOIN ACT_ID_USER ackuser on staff.UserID = ackuser.ID_  "
				+ "where staff.IsDeleted = 0 and staff.Telephone =" + telephone + "";
		List<Object> result = baseDao.findBySql(sql);

		StaffVO staffVO = new StaffVO();
		if (result.size() != 0) {
			Object[] objs = (Object[]) result.get(0);
			staffVO.setTelephone((String) objs[0]);
			staffVO.setNickName((String) objs[1]);
			staffVO.setUserID((String) objs[2]);
		}
		return staffVO;
	}

	@Override
	public String updateStaffTelephone(StaffVO staffVO) {
		if (staffVO == null) {
			throw new RuntimeException("获取员工信息失败！");
		}
		StaffEntity staffEntity = staffDao.getStaffBytelephone(staffVO.getTelephone());
		String userID = staffEntity.getUserID();
		User user = identityService.createUserQuery().userId(userID).singleResult();
		user.setPassword(staffVO.getPassword());
		identityService.saveUser(user);

		return userID;
	}

	@Override
	public void updateAttachementNames(String pictureName, Integer staffID) {
		StaffEntity staffEntity = staffDao.getStaffByStaffID(staffID);
		String attachementNames = staffEntity.getAttachementNames();
		String newatmNames = attachementNames.replaceFirst(pictureName + ",", "");
		staffEntity.setAttachementNames(newatmNames);
		staffDao.save(staffEntity);

	}

	@Override
	public void startAudit(StaffAuditVO staffAuditVO) {
		staffAuditVO.setBusinessType(BusinessTypeEnum.AUDIT.getName());
		staffAuditVO.setTitle(staffAuditVO.getAuditUserName() + "的" + BusinessTypeEnum.AUDIT.getName());
		// 初始化流程参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", staffAuditVO);

		List<Group> groups = identityService.createGroupQuery().groupMember(staffAuditVO.getAuditUserID()).list();
		int companyID = Integer.parseInt(groups.get(0).getType().split("_")[0]);
		List<String> staffAuditHRAuditGroups = permissionService
				.findGroupsByPermissionCodeCompany(Constants.STAFF_AUDIT_HR_AUDIT, companyID);
		List<String> staffAuditHRAuditUsers = permissionService
				.findUsersByPermissionCodeCompany(Constants.STAFF_AUDIT_HR_AUDIT, companyID);
		if ((!hasGroupMember(staffAuditHRAuditGroups) && CollectionUtils.isEmpty(staffAuditHRAuditUsers))) {
			throw new RuntimeException("未找到该背景调查的审核人！");
		}

		vars.put("staffAuditHRAuditGroups", staffAuditHRAuditGroups);
		vars.put("staffAuditHRAuditUsers", staffAuditHRAuditUsers);

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.AUDIT);
		// 查询第一个任务
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		// 设置任务受理人
		taskService.setAssignee(task.getId(), staffAuditVO.getUserID());
		// 完成任务
		taskService.complete(task.getId(), vars);
	}

	@Override
	public void saveAudit(StaffAuditVO staffAuditVO, String processInstanceID) {
		StaffAuditVO auditVO = (StaffAuditVO) runtimeService.getVariable(processInstanceID, "arg");
		auditVO.setEducationID(staffAuditVO.getEducationID());
		auditVO.setDegreeID(staffAuditVO.getDegreeID());
		auditVO.setCriminalRecord(staffAuditVO.getCriminalRecord());
		auditVO.setCompany(staffAuditVO.getCompany());
		auditVO.setYears(staffAuditVO.getYears());
		auditVO.setDescription(staffAuditVO.getDescription());
		auditVO.setReferee(staffAuditVO.getReferee());
		auditVO.setTelephone(staffAuditVO.getTelephone());
		auditVO.setBeginDate(staffAuditVO.getBeginDate());
		auditVO.setEndDate(staffAuditVO.getEndDate());
		runtimeService.setVariable(processInstanceID, "arg", auditVO);

		updateAuditResult(auditVO.getAuditUserID(), staffAuditVO.getEducationID(), staffAuditVO.getDegreeID(),
				staffAuditVO.getCriminalRecord(), AuditStatusEnum.TO_AUDIT);

		String[] companys = staffAuditVO.getCompany();
		// Double[] years = staffAuditVO.getYears();
		String[] beginDates = staffAuditVO.getBeginDate();
		String[] endDates = staffAuditVO.getEndDate();
		String[] descriptions = staffAuditVO.getDescription();
		String[] referees = staffAuditVO.getReferee();
		String[] telephones = staffAuditVO.getTelephone();
		int size = companys.length;
		Date now = new Date();
		for (int i = 0; i < size; ++i) {
			WorkExperienceEntity workExperienceEntity = WorkExperienceEntity.builder().userID(auditVO.getAuditUserID())
					.company(companys[i])
					// .years(years[i])
					.beginDate(beginDates[i]).endDate(endDates[i]).description(descriptions[i]).referee(referees[i])
					.telephone(telephones[i]).isDeleted(IsDeletedEnum.NOT_DELETED.getValue()).addTime(now)
					.updateTime(now).build();
			workExperienceDao.save(workExperienceEntity);
		}
	}

	@Override
	public ListResult<TaskVO> findAuditTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups,
			List<String> users, int page, int limit) {
		String groupIDs = Arrays.toString(Lists2.transform(groups, new SafeFunction<Group, String>() {
			@Override
			protected String safeApply(Group input) {
				return "'" + input.getId() + "'";
			}
		}).toArray());
		String taskNames = Arrays.toString(Lists2.transform(tasks, new SafeFunction<TaskDefKeyEnum, String>() {
			@Override
			protected String safeApply(TaskDefKeyEnum input) {
				return "'" + input.getName() + "'";
			}
		}).toArray());
		String userIDs = Arrays.toString(Lists2.transform(users, new SafeFunction<String, String>() {
			@Override
			protected String safeApply(String input) {
				return "'" + input + "'";
			}
		}).toArray());
		String sql = "select DISTINCT task.ID_, task.PROC_INST_ID_, task.NAME_, task.TASK_DEF_KEY_ from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in (" + taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in (" + groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in (" + userIDs.substring(1, userIDs.length() - 1) + "))";
		List<Object> result = baseDao.findPageList(sql, page, limit);
		List<TaskVO> taskVOs = createTaskVOList(result);

		sql = "select count(DISTINCT task.ID_) from ACT_RU_TASK task, ACT_RU_IDENTITYLINK identityLink "
				+ "where task.ID_ = identityLink.TASK_ID_ and identityLink.TYPE_ = 'candidate' and "
				+ "task.TASK_DEF_KEY_ in (" + taskNames.substring(1, taskNames.length() - 1) + ") "
				+ "and (identityLink.GROUP_ID_ in (" + groupIDs.substring(1, groupIDs.length() - 1) + ") "
				+ "or identityLink.USER_ID_ in (" + userIDs.substring(1, userIDs.length() - 1) + "))";
		Object countObj = baseDao.getUniqueResult(sql);
		int count = countObj == null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<TaskVO>(taskVOs, count);
	}

	private List<TaskVO> createTaskVOList(List<Object> tasks) {
		List<TaskVO> taskVOs = new ArrayList<TaskVO>();
		for (Object task : tasks) {
			Object[] objs = (Object[]) task;
			// 查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery().processInstanceId((String) objs[1])
					.singleResult();
			// 查询流程参数
			StaffAuditVO arg = (StaffAuditVO) runtimeService.getVariable(pInstance.getId(), "arg");
			TaskVO taskVO = new TaskVO();
			taskVO.setProcessInstanceID((String) objs[1]);
			taskVO.setRequestUserName(arg.getUserName());
			taskVO.setRequestDate(arg.getRequestDate());
			taskVO.setTaskID((String) objs[0]);
			taskVO.setTaskName((String) objs[2]);
			taskVO.setTaskDefKey((String) objs[3]);
			taskVO.setTitle(arg.getTitle());
			taskVO.setAuditUserName(arg.getAuditUserName());
			StaffVO staffVO = getStaffByUserID(arg.getAuditUserID());
			if (staffVO != null) {
				List<GroupDetailVO> groups = findGroupDetailsByUserID(staffVO.getUserID());
				taskVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
					@Override
					protected String safeApply(GroupDetailVO input) {
						return input.getCompanyName() + "—" + input.getDepartmentName() + "—" + input.getPositionName();
					}
				}));
			}
			taskVOs.add(taskVO);
		}
		return taskVOs;
	}

	@Override
	public String getUsefulValidateKeyByUserId(String userId, Boolean isRecent) {
		return staffDao.getUsefulValidateKeyByUserId(userId, isRecent);
	}

	@Override
	public void insertRestValidateKey(String userId, String validateKey) {
		staffDao.insertRestValidateKey(userId, validateKey);
	}

	@Override
	public void updateUserPassword(String userId, String newPassword) {
		User user = identityService.createUserQuery().userId(userId).singleResult();
		if (user == null) {
			throw new RuntimeException("获取个人信息失败！");
		}
		user.setPassword(newPassword);
		identityService.saveUser(user);
	}

	@Override
	public String getRealNameByUserId(String userId) {
		return staffDao.getEmployeeNameByUsrId(userId);
	}

	@Override
	public List<Object> getStaffByName(String staffName) {
		String sql = "select u.FIRST_,u.LAST_ from ACT_ID_USER u,OA_Staff s where u.ID_=s.UserID and s.StaffName='"
				+ staffName + "' and s.isDeleted=0 and s.status!=4";
		return baseDao.findBySql(sql);
	}

	@Override
	public String getTelephoneByUserId(String userId) {
		String sql = "select s.Telephone from OA_Staff s  where s.UserID='" + userId + "'";
		return (String) baseDao.getUniqueResult(sql);
	}

	@Override
	public List<Object> getStaffNameByTelephone(String telephone) {
		String sql = "select distinct s.StaffName,s.Telephone from OA_Staff s where s.Telephone like '%" + telephone
				+ "'";
		return baseDao.findBySql(sql);
	}

	@Override
	public SalaryDetailEntity getSaleryById(String id) {
		String hql = " from SalaryDetailEntity s where s.id=" + id;
		return (SalaryDetailEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public List<String> getDepartmentStaffs(Integer companyID, Integer departmentID, List<String> underlings, int level,
			boolean isLeader) {

		// 获取部门下面的职位
		List<PositionVO> positions = positionService.findPositionsByDepartmentID(departmentID);
		for (PositionVO positionVO : positions) {
			// 如果是第一层级，过滤掉同一级的主管
			if (level == 1 && "主管".equals(positionVO.getPositionName())) {
				continue;
			}
			// 如果是组长，过滤掉同一级的组长
			else if (level == 1 && isLeader && "组长".equals(positionVO.getPositionName())) {
				continue;
			} else {
				List<String> userList = getUsrListByPositionId(positionVO.getPositionID());
				underlings.addAll(userList);
			}
		}
		level++;
		// 获取部门下面的部门
		List<DepartmentEntity> departments = departmentDao.findDepartmentsByCompanyIDParentID(companyID, departmentID);
		for (DepartmentEntity department : departments) {
			getDepartmentStaffs(companyID, department.getDepartmentID(), underlings, level, isLeader);
		}
		return underlings;
	}

	public List<String> getUsrListByPositionId(Integer positionID) {
		String sql = "SELECT\n" + "	staff.UserID\n" + "FROM\n" + "	OA_GroupDetail a\n"
				+ "INNER JOIN ACT_ID_MEMBERSHIP b ON a.GroupID = b.GROUP_ID_\n"
				+ "INNER JOIN OA_Staff staff ON b.USER_ID_ = staff.UserID\n" + "WHERE\n" + "	a.PositionID = "
				+ positionID + "\n" + "AND staff.IsDeleted = 0 AND staff.`Status`!=4 AND a.IsDeleted=0";
		List<Object> objLst = baseDao.findBySql(sql);
		List<String> userLst = new ArrayList<>();
		for (Object obj : objLst) {
			userLst.add(obj + "");
		}
		return userLst;
	}

	@Override
	public boolean checkStaffIsInJob(String userId) {
		String sql = "select `Status` from OA_Staff where UserID='" + userId + "'";
		String status = baseDao.getUniqueResult(sql) + "";
		// 状态4表示离职
		if ("4".equals(status)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean checkIsApplyQuit(String applyUserId) {
		String sql = "SELECT\n" + "	ProcessInstanceID\n" + "FROM\n" + "	OA_Resignation\n" + "WHERE\n"
				+ "	RequestUserID = '" + applyUserId + "'\n" + "ORDER BY\n" + "	AddTime DESC\n" + "LIMIT 0,1";
		Object obj = baseDao.getUniqueResult(sql);
		String processInstanceId = obj == null ? "" : baseDao.getUniqueResult(sql) + "";
		if (StringUtils.isNotBlank(processInstanceId)) {
			long taskCount = taskService.createTaskQuery().processInstanceId(processInstanceId).count();
			if (taskCount > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<String> getDepartmentAllStaffs(Integer companyID, Integer departmentID, Set<String> underlings) {

		// 获取部门下面的职位
		List<PositionVO> positions = positionService.findPositionsByDepartmentID(departmentID);
		for (PositionVO positionVO : positions) {
			List<String> userList = getUsrListByPositionId(positionVO.getPositionID());
			underlings.addAll(userList);
		}
		// 获取部门下面的部门
		List<DepartmentEntity> departments = departmentDao.findDepartmentsByCompanyIDParentID(companyID, departmentID);
		for (DepartmentEntity department : departments) {
			getDepartmentAllStaffs(companyID, department.getDepartmentID(), underlings);
		}
		return underlings;
	}

	@Override
	public boolean checkTelephone(String telephone, String staffId) {
		String sql = "select count(*) from OA_Staff where Telephone='" + telephone
				+ "' and IsDeleted=0 and `Status`!=4";
		if (StringUtils.isNotBlank(staffId)) {
			sql += " and StaffID!=" + staffId;
		}
		int count = Integer.parseInt(baseDao.getUniqueResult(sql) + "");
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isPartner(String userId) {
		// String sql = "select count(*) from OA_WeekReporter where
		// userId='"+userId+"' and partner=1 and isDeleted=0";
		String sql = "select count(id) from OA_Partner where userId='" + userId + "' and status=1 and isDeleted=0";
		int count = Integer.parseInt(baseDao.getUniqueResult(sql) + "");
		if (count > 0) {
			return true;
		}
		return false;
	}

	@Override
	public String getAdminId() {
		String sql = "SELECT UserID from OA_Staff where StaffName='Joe' and IsDeleted=0 ";
		return (String) baseDao.getUniqueResult(sql);
	}

	@Override
	public int getComeDays(String userId, String beginTime) {
		StaffEntity staff = staffDao.getStaffByUserID(userId);
		Date entryDate = staff.getEntryDate();
		//Date now = new Date();
		Date beginDate = DateUtil.getFullDate(beginTime);
		return DateUtil.differentDays(entryDate, beginDate)+1;
	}

	@Override
	public void saveStaffSalaryChange(String id, StaffVO staffVO, String salary) {
		StaffSalaryChangeLogEntity staffSalaryChange = new StaffSalaryChangeLogEntity();
		staffSalaryChange.setAddTime(new Date());
		staffSalaryChange.setIsDeleted(0);
		staffSalaryChange.setNewSalary(staffVO.getSalary());
		staffSalaryChange.setOldSalary(salary);
		staffSalaryChange.setOperatorId(id);
		staffSalaryChange.setStaffUserId(staffVO.getUserID());
		baseDao.hqlSave(staffSalaryChange);
	}

	@Override
	public ListResult<Object> getInsuranceList(String year, String month, String status, String companyId,
			Integer limit, Integer page) {
		String sql = "";
		String sqlCount = "";
		if (StaffStatusEnum.LEAVE.getName().equals(status)) {
			if (StringUtils.isNotBlank(companyId)) {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n" + "	LeaveDate\n"
						+ "FROM\n" + "	OA_Staff staff,\n" + "	ACT_ID_MEMBERSHIP ship,\n" + "	OA_GroupDetail detail\n"
						+ "WHERE\n" + "	YEAR (LeaveDate) = '" + year + "'\n" + "AND MONTH (LeaveDate) = '" + month
						+ "'\n" + "AND `Status` = 4\n" + "AND staff.IsDeleted = 0\n"
						+ "AND staff.UserID = ship.USER_ID_\n" + "AND detail.GroupID = ship.GROUP_ID_\n"
						+ "AND detail.CompanyID = " + companyId;
				sqlCount = "select count(distinct IDNumber)\n" + "FROM\n" + "	OA_Staff staff,\n"
						+ "	ACT_ID_MEMBERSHIP ship,\n" + "	OA_GroupDetail detail\n" + "WHERE\n"
						+ "	YEAR (LeaveDate) = '" + year + "'\n" + "AND MONTH (LeaveDate) = '" + month + "'\n"
						+ "AND `Status` = 4\n" + "AND staff.IsDeleted = 0\n" + "AND staff.UserID = ship.USER_ID_\n"
						+ "AND detail.GroupID = ship.GROUP_ID_\n" + "AND detail.CompanyID = " + companyId;
			} else {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n" + "	LeaveDate\n"
						+ "FROM\n" + "	OA_Staff staff\n" + "WHERE\n" + "	YEAR (LeaveDate) = '" + year + "'\n"
						+ "AND MONTH (LeaveDate) = '" + month + "'\n" + "AND `Status` = 4\n"
						+ "AND staff.IsDeleted = 0\n";
				sqlCount = "select count(distinct IDNumber)\n" + "FROM\n" + "	OA_Staff staff\n" + "WHERE\n"
						+ "	YEAR (LeaveDate) = '" + year + "'\n" + "AND MONTH (LeaveDate) = '" + month + "'\n"
						+ "AND `Status` = 4\n" + "AND staff.IsDeleted = 0\n";
			}
		} else {
			int monthInt = Integer.parseInt(month);
			monthInt++;
			if (monthInt < 10) {
				month += "0" + month;
			}
			if (StringUtils.isNotBlank(companyId)) {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n"
						+ "	CASE `Status`\n" + "WHEN '3' THEN\n" + "	'正式'\n" + "WHEN '1' THEN\n" + "	'试用'\n"
						+ "WHEN '2' THEN\n" + "	'实习'\n" + "WHEN '4' THEN\n" + "	'离职'\n" + "END,\n"
						+ " IFNULL(FormalDate, \"无\")\n" + "FROM\n" + "	OA_Staff staff,\n"
						+ "	ACT_ID_MEMBERSHIP ship,\n" + "	OA_GroupDetail detail\n" + "WHERE\n" + "	((\n"
						+ "		LeaveDate IS NULL\n" + "		AND `Status` != 4\n" + "	)\n" + "OR (\n"
						+ "	DATE(LeaveDate) >= '" + year + "-" + month + "'\n" + "	AND `Status` = 4\n" + "))\n"
						+ "AND staff.IsDeleted = 0\n" + "AND staff.UserID = ship.USER_ID_\n"
						+ "AND detail.GroupID = ship.GROUP_ID_\n" + "AND detail.CompanyID = " + companyId;
				sqlCount = "select count(distinct IDNumber)\n" + "FROM\n" + "	OA_Staff staff,\n"
						+ "	ACT_ID_MEMBERSHIP ship,\n" + "	OA_GroupDetail detail\n" + "WHERE\n" + "	((\n"
						+ "		LeaveDate IS NULL\n" + "		AND `Status` != 4\n" + "	)\n" + "OR (\n"
						+ "	DATE(LeaveDate) >= '" + year + "-" + month + "'\n" + "	AND `Status` = 4\n" + "))\n"
						+ "AND staff.IsDeleted = 0\n" + "AND staff.UserID = ship.USER_ID_\n"
						+ "AND detail.GroupID = ship.GROUP_ID_\n" + "AND detail.CompanyID = " + companyId;
			} else {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n"
						+ "	CASE `Status`\n" + "WHEN '3' THEN\n" + "	'正式'\n" + "WHEN '1' THEN\n" + "	'试用'\n"
						+ "WHEN '2' THEN\n" + "	'实习'\n" + "WHEN '4' THEN\n" + "	'离职'\n" + "END,\n"
						+ " IFNULL(FormalDate, \"无\")\n" + "FROM\n" + "	OA_Staff staff\n" + "WHERE\n" + "	((\n"
						+ "		LeaveDate IS NULL\n" + "		AND `Status` != 4\n" + "	)\n" + "OR (\n"
						+ "	DATE(LeaveDate) >= '" + year + "-" + month + "'\n" + "	AND `Status` = 4\n" + "))\n"
						+ "AND staff.IsDeleted = 0\n";
				sqlCount = "select count(distinct IDNumber)\n" + "FROM\n" + "	OA_Staff staff\n" + "WHERE\n"
						+ "	((\n" + "		LeaveDate IS NULL\n" + "		AND `Status` != 4\n" + "	)\n" + "OR (\n"
						+ "	DATE(LeaveDate) >= '" + year + "-" + month + "'\n" + "	AND `Status` = 4\n" + "))\n"
						+ "AND staff.IsDeleted = 0\n";
			}
		}
		List<Object> insuranceList = baseDao.findPageList(sql, page, limit);
		int count = Integer.parseInt(baseDao.getUniqueResult(sqlCount) + "");
		return new ListResult<>(insuranceList, count);
	}

	@Override
	public List<Object> getAllInsuranceList(String year, String month, String status, String companyId) {
		String sql = "";
		if (StaffStatusEnum.LEAVE.getName().equals(status)) {
			if (StringUtils.isNotBlank(companyId)) {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n" + "	LeaveDate\n"
						+ "FROM\n" + "	OA_Staff staff,\n" + "	ACT_ID_MEMBERSHIP ship,\n" + "	OA_GroupDetail detail\n"
						+ "WHERE\n" + "	YEAR (LeaveDate) = '" + year + "'\n" + "AND MONTH (LeaveDate) = '" + month
						+ "'\n" + "AND `Status` = 4\n" + "AND staff.IsDeleted = 0\n"
						+ "AND staff.UserID = ship.USER_ID_\n" + "AND detail.GroupID = ship.GROUP_ID_\n"
						+ "AND detail.CompanyID = " + companyId;
			} else {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n" + "	LeaveDate\n"
						+ "FROM\n" + "	OA_Staff staff\n" + "WHERE\n" + "	YEAR (LeaveDate) = '" + year + "'\n"
						+ "AND MONTH (LeaveDate) = '" + month + "'\n" + "AND `Status` = 4\n"
						+ "AND staff.IsDeleted = 0\n";
			}
		} else {
			int monthInt = Integer.parseInt(month);
			monthInt++;
			if (monthInt < 10) {
				month += "0" + month;
			}
			if (StringUtils.isNotBlank(companyId)) {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n"
						+ "	CASE `Status`\n" + "WHEN '3' THEN\n" + "	'正式'\n" + "WHEN '1' THEN\n" + "	'试用'\n"
						+ "WHEN '2' THEN\n" + "	'实习'\n" + "WHEN '4' THEN\n" + "	'离职'\n" + "END,\n"
						+ " IFNULL(FormalDate, \"无\")\n" + "FROM\n" + "	OA_Staff staff,\n"
						+ "	ACT_ID_MEMBERSHIP ship,\n" + "	OA_GroupDetail detail\n" + "WHERE\n" + "	((\n"
						+ "		LeaveDate IS NULL\n" + "		AND `Status` != 4\n" + "	)\n" + "OR (\n"
						+ "	DATE(LeaveDate) >= '" + year + "-" + month + "'\n" + "	AND `Status` = 4\n" + "))\n"
						+ "AND staff.IsDeleted = 0\n" + "AND staff.UserID = ship.USER_ID_\n"
						+ "AND detail.GroupID = ship.GROUP_ID_\n" + "AND detail.CompanyID = " + companyId;
			} else {
				sql = "SELECT\n" + "	distinct StaffName,\n" + "	IDNumber,\n" + "	Education,\n"
						+ "	YEAR (NOW()) - YEAR (Birthday),\n" + "	EntryDate,\n" + "	insurance,\n"
						+ "	CASE `Status`\n" + "WHEN '3' THEN\n" + "	'正式'\n" + "WHEN '1' THEN\n" + "	'试用'\n"
						+ "WHEN '2' THEN\n" + "	'实习'\n" + "WHEN '4' THEN\n" + "	'离职'\n" + "END,\n"
						+ " IFNULL(FormalDate, \"无\")\n" + "FROM\n" + "	OA_Staff staff\n" + "WHERE\n" + "	((\n"
						+ "		LeaveDate IS NULL\n" + "		AND `Status` != 4\n" + "	)\n" + "OR (\n"
						+ "	DATE(LeaveDate) >= '" + year + "-" + month + "'\n" + "	AND `Status` = 4\n" + "))\n"
						+ "AND staff.IsDeleted = 0\n";
			}
		}
		return baseDao.findBySql(sql);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InputStream exportInsuranceList(List<Object> insuranceList, String status, String title) throws Exception {
		// 创建一个空excel文件
		Workbook workbook = new HSSFWorkbook();
		Sheet worksheet = workbook.createSheet("员工名单");
		// 设置列宽
		worksheet.setColumnWidth(0, 2000);
		worksheet.setColumnWidth(1, 3500);
		worksheet.setColumnWidth(2, 5000);
		worksheet.setColumnWidth(3, 2000);
		worksheet.setColumnWidth(4, 2000);
		worksheet.setColumnWidth(5, 3500);
		worksheet.setColumnWidth(6, 3500);
		worksheet.setColumnWidth(7, 3500);
		worksheet.setColumnWidth(8, 3500);

		Font headfont = workbook.createFont();
		headfont.setFontName("宋体");
		headfont.setFontHeightInPoints((short) 11);// 字体大小
		headfont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		// 头部样式
		CellStyle headstyle = workbook.createCellStyle();
		headstyle.setFont(headfont);
		headstyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 左右居中
		headstyle.setLocked(true);

		// 创建标题行
		Row rowTitle = worksheet.createRow(0);
		Cell cellTitle = rowTitle.createCell(0);
		cellTitle.setCellStyle(headstyle);
		cellTitle.setCellValue(title);
		// 创建列名行
		Row rowCol = worksheet.createRow(1);
		if (StaffStatusEnum.LEAVE.getName().equals(status)) {
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));// 设置单元格合并
			String[] colNames = { "序号", "姓名", "身份证号", "学历", "年龄", "入职时间", "险种分类", "离职时间" };
			int colIndex = 0;
			for (String colName : colNames) {
				rowCol.createCell(colIndex).setCellValue(colName);
				colIndex++;
			}
		} else {
			worksheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 8));// 设置单元格合并
			String[] colNames = { "序号", "姓名", "身份证号", "学历", "年龄", "入职时间", "险种分类", "当前状态", "转正时间" };
			int colIndex = 0;
			for (String colName : colNames) {
				rowCol.createCell(colIndex).setCellValue(colName);
				colIndex++;
			}
		}
		// 数据从第三行开始填入
		int index = 2;
		// 员工序号
		int num = 1;
		for (Object obj : insuranceList) {
			Row row = worksheet.createRow(index);
			Object[] insurance = (Object[]) obj;
			row.createCell(0).setCellValue(String.valueOf(num));
			int cellIndex = 1;
			for (Object val : insurance) {
				if (null == val) {
					val = "";
				}
				row.createCell(cellIndex).setCellValue(String.valueOf(val));
				cellIndex++;
			}
			index++;
			num++;
		}
		// 将生成的excel写入到输出流里面,然后再通过这个输出流来得到我们所需要的输入流.
		try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
			workbook.write(os);
			ByteArrayInputStream in = new ByteArrayInputStream(os.toByteArray());
			return in;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			workbook.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public StaffVO getStaffByOpenId(String openid) {
		String hql = "from StaffEntity where openId='" + openid + "' and isDeleted=0";
		List<StaffEntity> staff = (List<StaffEntity>) baseDao.hqlfind(hql);
		if (null == staff || staff.size() < 1) {
			return null;
		} else {
			User user = identityService.createUserQuery().userId(staff.get(0).getUserID()).singleResult();
			StaffVO staffVo = (StaffVO) CopyUtil.tryToVo(staff.get(0), StaffVO.class);
			staffVo.setNickName(user.getLastName());
			staffVo.setPassword(DesUtil.decrypt(user.getPassword()));
			return staffVo;
		}
	}

	@Override
	public void updateStaffOpenId(String id, String openid) {
		String sql = "update OA_Staff set openId='" + openid + "' where userID='" + id + "'";
		baseDao.excuteSql(sql);
	}

	@Override
	public String getStaffHeadImg(String id) {
		String hql = "from StaffEntity where userId='" + id + "' and headImgId is not null and isDeleted=0";
		StaffEntity staff = (StaffEntity) baseDao.hqlfindUniqueResult(hql);
		if (null == staff) {
			return null;
		} else {
			return staff.getHeadImgId();
		}
	}

	@Override
	public void updateStaffHeadImgId(String userId, int headImgId) {
		String sql = "update OA_Staff set headImgId=" + headImgId + " where userId='" + userId + "'";
		baseDao.excuteSql(sql);
	}

	@Override
	public StaffEntity getStaffByUserId(String userId) {
		return staffDao.getStaffByUserID(userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StaffEntity getInJobStaffByUserId(String userId) {
		Session session = sessionFactory.getCurrentSession();
		String hql = "from StaffEntity staff where staff.userID = :userID and staff.isDeleted = 0 and staff.status!=4";
		Query query = session.createQuery(hql);
		query.setParameter("userID", userId);
		List<StaffEntity> result = query.list();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public int getTotalStaffNum() {
		String sql = "select count(staffID) from OA_Staff where isDeleted=0 and status!=4";
		return Integer.parseInt(baseDao.getUniqueResult(sql) + "");
	}

	@Override
	public int getProbationNum() {
		String sql = "select count(staffID) from OA_Staff where isDeleted=0 and (status=1 or status=2)";
		return Integer.parseInt(baseDao.getUniqueResult(sql) + "");
	}

	@Override
	public int getFormalStaffNum() {
		String sql = "select count(staffID) from OA_Staff where isDeleted=0 and status=3";
		return Integer.parseInt(baseDao.getUniqueResult(sql) + "");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StaffEntity> getQuitingStaffs() {
		String hql = "SELECT\n" +
				"	distinct staff\n" +
				"FROM\n" +
				"	StaffEntity staff,\n" +
				"	ResignationEntity res\n" +
				"WHERE\n" +
				"	staff.userID = res.requestUserID\n" +
				"AND staff.isDeleted = 0\n" +
				"AND staff.status != 4\n" +
				"AND IFNULL(res.processStatus, 0) != 2";
		return (List<StaffEntity>) baseDao.hqlfind(hql);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StaffEntity> findAllBirthStaffsByCurrentMonth() {
		// TODO Auto-generated method stub
		String hql = "from StaffEntity staff where isDeleted=0 and status!=4 and MONTH(staff.birthday)= MONTH(CURRENT_DATE)";
		return (List<StaffEntity>) baseDao.hqlfind(hql);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StaffEntity> findAllAnniversaryStaffsByCurrentMonth() {
		String hql = "from StaffEntity staff where isDeleted=0 and status!=4 and MONTH(staff.entryDate)= MONTH(CURRENT_DATE)"
				+ " AND YEAR(staff.entryDate)!=YEAR(CURRENT_DATE)";
		return (List<StaffEntity>) baseDao.hqlfind(hql);
	}

	@Override
	public StaffEntity getStaffByUnionid(String unionid) {
		String hql = "from StaffEntity where isDeleted=0 and status!=4 and unionid='" + unionid + "'";
		return (StaffEntity) baseDao.hqlfindUniqueResult(hql);
	}

	@Override
	public void updateStaffUnionId(String id, String unionid) {
		String sql = "update OA_Staff set unionid='" + unionid + "' where userID='" + id + "'";
		baseDao.excuteSql(sql);
	}

	@Override
	public List<Object> findUserList() {
		String sql = "select ACT_ID_USER.ID_,ACT_ID_USER.PWD_ from ACT_ID_USER";
		List<Object> objects = baseDao.findBySql(sql);
		return objects;
	}

	// ------------------------------------------------
	@Override
	public void updatePwdById(String userPWD,String userID) {
		String sql = "UPDATE ACT_ID_USER SET ACT_ID_USER.PWD_ = '"+userPWD+"' where ACT_ID_USER.ID_ = '"+userID+"'";
		baseDao.excuteSql(sql);
	}

	// ------------------------------------------------
	@SuppressWarnings("deprecation")
	@Override
	public StaffVO getStaffByUser_ID(String userID) {
		StaffEntity staffEntity = staffDao.getStaffByUserID(userID);
		Date date = new Date();
		StaffVO staffVO = new StaffVO();
		if (staffEntity != null) {

			staffVO.setLastName(staffEntity.getStaffName());
			staffVO.setGender(staffEntity.getGender());

			if (staffEntity.getBirthday() != null) {

				staffVO.setAge(String.valueOf(date.getYear() - staffEntity.getBirthday().getYear()));
			} else {
				staffVO.setAge(null);
			}
			List<GroupDetailVO> groups = findGroupDetailsByUserID(staffEntity.getUserID());
			Integer departmentID = groups.get(0).getDepartmentID();
			staffVO.setDepartmentName(departmentDao.getDepartmentByDepartmentID(departmentID).getDepartmentName());

			Integer positionID = groups.get(0).getPositionID();
			staffVO.setGroupDetail(positionDao.getPositionByPositionID(positionID).getPositionName());

			staffVO.setTelephone(staffEntity.getTelephone());

			staffVO.setEntryDate(DateUtil.formateDate(staffEntity.getEntryDate()));

			Integer companyID = groups.get(0).getCompanyID();
			staffVO.setCompanyID(companyDao.getCompanyByCompanyID(companyID).getCompanyID());

			staffVO.setSchool(staffEntity.getSchool());
			staffVO.setMajor(staffEntity.getMajor());
			staffVO.setMaritalStatus(staffEntity.getMaritalStatus());
			staffVO.setNativePlace(staffEntity.getNativePlace());
			staffVO.setEducation(staffEntity.getEducation());

			staffVO.setUserID(staffEntity.getUserID());
		}
		return staffVO;
	}

	@Override
	public List<Object> getStaffsByPositionId(String positionId) {
		String sql = "SELECT\n" +
				"	DISTINCT UserID\n" +
				"FROM\n" +
				"	oa_staff staff,\n" +
				"	act_id_membership ship,\n" +
				"	oa_groupdetail detail\n" +
				"WHERE\n" +
				"	staff.UserID = ship.USER_ID_\n" +
				"AND ship.GROUP_ID_ = detail.GroupID\n" +
				"AND detail.PositionID = "+positionId+"\n" +
				"AND staff.IsDeleted = 0\n" +
				"AND `Status` != 4";
		return baseDao.findBySql(sql);
	}

	@Override
	public boolean isPM(String userId) {
		return permissionMembershipDao.checkHasPermissionByUserId(Constants.PM_AUDITOR, userId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ListResult<StaffEntity> findAllInJobStaffs(String staffName, int limit, int page) {
		String hql = "from StaffEntity where isDeleted=0 and status!=4";
		if(StringUtils.isNotBlank(staffName)){
			hql += " and staffName like '%"+EscapeUtil.decodeSpecialChars(staffName)+"%'";
		}
		hql += "  order by entryDate";
		List<StaffEntity> staffs = (List<StaffEntity>) baseDao.hqlPagedFind(hql, page, limit);
		for(StaffEntity staff: staffs){
			List<GroupDetailVO> groupDetails = findGroupDetailsByUserID(staff.getUserID());
			if(!CollectionUtils.isEmpty(groupDetails)){
				GroupDetailVO g0 = groupDetails.get(0);
				staff.setDepartment(g0.getCompanyName()+"-"+g0.getDepartmentName());
			}
		}
		String hqlCount = "select count(staffID) from StaffEntity where isDeleted=0 and status!=4";
		if(StringUtils.isNotBlank(staffName)){
			hqlCount += " and staffName like '%"+EscapeUtil.decodeSpecialChars(staffName)+"%'";
		}
		int count = Integer.parseInt(String.valueOf(baseDao.hqlfindUniqueResult(hqlCount)));
		return new ListResult<>(staffs, count);
	}

	@Override
	public boolean isWhiteJob(String userId) {
		String sql = "select positionCategory from OA_Staff where userId='"+userId+"'";
		byte positionCategory = (byte)baseDao.getUniqueResult(sql);
		if(positionCategory == PositionEnum.WHITE.getValue()){
			return true;
		}
		return false;
	}
	@Override
	public String getDayDiff(Date beginDate,Date finishDate,Integer type) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 开始的时间
		String startString = format.format(beginDate);
		Date startDate = null;
		try {
			startDate = format.parse(startString);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		// 开始时间处理
		Calendar start = Calendar.getInstance();
		start.setTime(startDate);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		// 结束的时间
		String endString = format.format(finishDate);
		Date endDate = null;
		try {
			endDate = format.parse(endString);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 结束时间处理
		Calendar end = Calendar.getInstance();
		end.setTime(endDate);
		end.set(Calendar.HOUR_OF_DAY, 0);
		end.set(Calendar.MINUTE, 0);
		end.set(Calendar.SECOND, 0);

		int dayDiff = (int) ((end.getTimeInMillis() - start.getTimeInMillis()) / (1000 * 60 * 60 * 24))+1;
		String diff = null;
		//0.登录首页进智造链多少天；1.离职申请里面合同到期还有多少时间		
		if(type==0){
			/*int yearDiff = dayDiff/365+1;
			int dayDif = dayDiff%365+1;

			if(dayDif==0){
				diff=yearDiff+"&nbsp;年整";
			}else{
				diff=yearDiff+"&nbsp;年的第&nbsp;"+dayDif+"&nbsp;天";
			}*/
			diff = dayDiff+"&nbsp;天";

		}else{
			int yearDiff = dayDiff/365;
			int dayDif = dayDiff%365;
			if(yearDiff==0){
				diff=String.valueOf(dayDiff)+"&nbsp;天";
			}else{
				if(dayDif==0){
					diff=yearDiff+"&nbsp;年整";
				}else{
					diff=yearDiff+"&nbsp;年&nbsp;"+dayDif+"&nbsp;天";
				}
			}
		}

		return diff;
	}
	@Override
	public XSSFWorkbook exportStaffQueryVO(StaffQueryVO staffQueryVO) {
		List<Object> result = baseDao.findBySql(getQuerySqlByStaffQueryVO(staffQueryVO));
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			StaffVO staffVO = new StaffVO();
			staffVO.setUserID((String) objs[0]);
			StaffVO staff = this.getStaffByUserID(staffVO.getUserID());
			staffVO.setStaffNumber(staffDao.getStaffNum(staffVO.getUserID()));//工号
			staffVO.setLastName((String) objs[1]);//姓名
			staffVO.setGender((String) objs[2]);//性别
			List<String> staffDepartmentNames = getStaffDepartmentNames(staffVO.getUserID(), 3);
			staffVO.setDepartmentNames(staffDepartmentNames);//部门
			//staffVO.setDepartmentName(getStaffDepartmentNames(staffVO.getUserID()));
			int positionId = positionDao.getPositionIdByUserId(staffVO.getUserID());
			PositionEntity position = positionDao.getPositionByPositionID(positionId);
			if(null != position){
				staffVO.setPositionName(position.getPositionName());//岗位
			}
			//合同到期时间
			ContractEntity contract = contractService.getStaffLatestContractByUserId(staffVO.getUserID());
			if(null != contract){
				staffVO.setContractEndDate(DateUtil.formateDate(contract.getEndDate()));
			}
			//开户行
			staffVO.setBank(staff.getBank());
			//银行卡号
			staffVO.setBankAccount(staff.getBankAccount());
			//是否交公积金
			StaffSalaryEntity staffSalary = staffSalaryService.getStaffSalary(staffVO.getUserID());
			if(null != staffSalary && null != staffSalary.getCompanyPayFund()){
				staffVO.setPayFund("是");
			}else{
				staffVO.setPayFund("否");
			}
			//体检项目
			String checkItemStr = staff.getCheckItems();
			if(StringUtils.isNotBlank(checkItemStr)){
				String[] checkItems = checkItemStr.split(",");
				List<String> checkItemNames = new ArrayList<>();
				for(String checkItem: checkItems){
					checkItemNames.add(StaffBodyCheckEnum.valueOf(Integer.parseInt(checkItem)).name());
				}
				staffVO.setCheckItems(StringUtils.join(checkItemNames, "/"));
			}
			//岗位性质
			staffVO.setPositionType(PositionEnum.valueOf(staff.getPositionCategory()).getName());
			staffVO.setIdNumber(staff.getIdNumber());//身份证号码
			staffVO.setStarSign(staff.getStarSign());//星座
			staffVO.setTelephone((String) objs[3]);//联系电话
			staffVO.setBirthday(staff.getBirthday());//出生日期
			staffVO.setEmergencyContract(staff.getEmergencyContract());//紧急联系人
			staffVO.setEmergencyPhone(staff.getEmergencyPhone());//联系人电话
			staffVO.setEmail(staff.getEmail());//邮箱
			staffVO.setInsurance(staff.getInsurance());//保险
			staffVO.setEducationID(staff.getEducationID());//学历证书编号
			staffVO.setDegreeID(staff.getDegreeID());//学位证书编号
			staffVO.setAddress(staff.getAddress());//现住地址
			staffVO.setSchool(staff.getSchool());//毕业学校
			staffVO.setGraduationDate(staff.getGraduationDate());//毕业日期
			staffVO.setEducation(staff.getEducation());//第一学历
			staffVO.setMajor(staff.getMajor());//专业
			staffVO.setNativePlace(staff.getNativePlace());//籍贯
			staffVO.setEntryDate(DateUtil.formateDate((Date) objs[4]));//入职日期
			staffVO.setFormalDate(staff.getFormalDate());//转正日期
			staffVO.setMaritalStatus(staff.getMaritalStatus());//婚姻状况，0：未婚；1：已婚
			staffVO.setGradeName(staff.getGradeName());//职级
			Double standardSalary = staff.getStandardSalary();
			//兼容老数据
			if(null == standardSalary){
				staffVO.setSalary(staff.getSalary());
			}else{
				staffVO.setSalary(String.valueOf(standardSalary));//薪资待遇
			}
			staffVO.setCriminalRecord(staff.getCriminalRecord());//案底说明
			staffVO.setSkill(staff.getSkill());//技能
			staffVO.setStaffID((Integer) objs[6]);
			staffVOs.add(staffVO);
		}
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("员工档案");
		XSSFRow row = sheet.createRow((int)0);
		//创建单元格
		row.createCell((short) 0).setCellValue("工号");
		row.createCell((short) 1).setCellValue("姓名");
		row.createCell((short) 2).setCellValue("性别");
		row.createCell((short) 3).setCellValue("部门级别一");
		row.createCell((short) 4).setCellValue("部门级别二");
		row.createCell((short) 5).setCellValue("部门级别三");
		row.createCell((short) 6).setCellValue("岗位");
		row.createCell((short) 7).setCellValue("岗位性质");
		row.createCell((short) 8).setCellValue("合同到期时间");
		row.createCell((short) 9).setCellValue("开户行");
		row.createCell((short) 10).setCellValue("银行卡号");
		row.createCell((short) 11).setCellValue("身份证号码");
		row.createCell((short) 12).setCellValue("星座");
		row.createCell((short) 13).setCellValue("联系电话");
		row.createCell((short) 14).setCellValue("出生日期");
		row.createCell((short) 15).setCellValue("紧急联系人");
		row.createCell((short) 16).setCellValue("联系人电话");
		row.createCell((short) 17).setCellValue("邮箱");
		row.createCell((short) 18).setCellValue("保险");
		row.createCell((short) 19).setCellValue("学历证书编号");
		row.createCell((short) 20).setCellValue("学位证书编号");
		row.createCell((short) 21).setCellValue("现住地址");
		row.createCell((short) 22).setCellValue("毕业学校");
		row.createCell((short) 23).setCellValue("毕业日期");
		row.createCell((short) 24).setCellValue("第一学历");
		row.createCell((short) 25).setCellValue("专业");
		row.createCell((short) 26).setCellValue("籍贯");
		row.createCell((short) 27).setCellValue("入职日期");
		row.createCell((short) 28).setCellValue("转正日期");
		row.createCell((short) 29).setCellValue("婚姻状况");
		row.createCell((short) 30).setCellValue("职级");
		row.createCell((short) 31).setCellValue("体检项目");
		row.createCell((short) 32).setCellValue("薪资待遇");
		row.createCell((short) 33).setCellValue("是否交公积金");
		row.createCell((short) 34).setCellValue("案底说明");
		row.createCell((short) 35).setCellValue("技能");

		for(int i=0,j=sheet.getLastRowNum()+1,length=staffVOs.size();i<length;++i,++j){
			XSSFRow row_date = sheet.createRow(j);
			StaffVO staffVO = staffVOs.get(i);
			row_date.createCell((short) 0).setCellValue(staffVO.getStaffNumber());
			row_date.createCell((short) 1).setCellValue(staffVO.getLastName());
			row_date.createCell((short) 2).setCellValue(staffVO.getGender());
			List<String> staffDepartmentNames = staffVO.getDepartmentNames();
			if(staffDepartmentNames.size()==1){
				row_date.createCell((short) 3).setCellValue(staffDepartmentNames.get(0));
			}else if(staffDepartmentNames.size()==2){
				row_date.createCell((short) 3).setCellValue(staffDepartmentNames.get(0));
				row_date.createCell((short) 4).setCellValue(staffDepartmentNames.get(1));
			}else if(staffDepartmentNames.size()==3){
				row_date.createCell((short) 3).setCellValue(staffDepartmentNames.get(0));
				row_date.createCell((short) 4).setCellValue(staffDepartmentNames.get(1));
				row_date.createCell((short) 5).setCellValue(staffDepartmentNames.get(2));
			}
			row_date.createCell((short) 6).setCellValue(staffVO.getPositionName());
			row_date.createCell((short) 7).setCellValue(staffVO.getPositionType());
			row_date.createCell((short) 8).setCellValue(staffVO.getContractEndDate());
			row_date.createCell((short) 9).setCellValue(staffVO.getBank()==null ? "":staffVO.getBank());
			row_date.createCell((short) 10).setCellValue(staffVO.getBankAccount()==null ? "":staffVO.getBankAccount());
			XSSFCell cell = row_date.createCell((short) 11);
			cell.setCellValue(staffVO.getIdNumber());
			XSSFCellStyle cellStyle = wb.createCellStyle();
			XSSFDataFormat format = wb.createDataFormat();
            cellStyle.setDataFormat(format.getFormat("0"));
            cell.setCellStyle(cellStyle);
			row_date.createCell((short) 12).setCellValue(staffVO.getStarSign());
			row_date.createCell((short) 13).setCellValue(staffVO.getTelephone());
			row_date.createCell((short) 14).setCellValue(staffVO.getBirthday());
			row_date.createCell((short) 15).setCellValue(staffVO.getEmergencyContract());
			row_date.createCell((short) 16).setCellValue(staffVO.getEmergencyPhone());
			row_date.createCell((short) 17).setCellValue(staffVO.getEmail());
			row_date.createCell((short) 18).setCellValue(staffVO.getInsurance());
			row_date.createCell((short) 19).setCellValue(staffVO.getEducationID());
			row_date.createCell((short) 20).setCellValue(staffVO.getDegreeID());
			row_date.createCell((short) 21).setCellValue(staffVO.getAddress());
			row_date.createCell((short) 22).setCellValue(staffVO.getSchool());
			row_date.createCell((short) 23).setCellValue(staffVO.getGraduationDate());
			row_date.createCell((short) 24).setCellValue(staffVO.getEducation());
			row_date.createCell((short) 25).setCellValue(staffVO.getMajor());
			row_date.createCell((short) 26).setCellValue(staffVO.getNativePlace());
			row_date.createCell((short) 27).setCellValue(staffVO.getEntryDate());
			row_date.createCell((short) 28).setCellValue(staffVO.getFormalDate());
			if(staffVO.getMaritalStatus()!=null && staffVO.getMaritalStatus()==0){
				row_date.createCell((short) 29).setCellValue("未婚");
			}else if(staffVO.getMaritalStatus()!=null && staffVO.getMaritalStatus()==1){
				row_date.createCell((short) 29).setCellValue("已婚");
			}

			row_date.createCell((short) 30).setCellValue(staffVO.getGradeName());
			row_date.createCell((short) 31).setCellValue(staffVO.getCheckItems()==null ? "":staffVO.getCheckItems());
			row_date.createCell((short) 32).setCellValue(staffVO.getSalary());
			row_date.createCell((short) 33).setCellValue(staffVO.getPayFund());
			row_date.createCell((short) 34).setCellValue(staffVO.getCriminalRecord());
			row_date.createCell((short) 35).setCellValue(staffVO.getSkill());
		}
		return wb;
	}

	@Override
	public InputStream downloadStaffCardInfos(StaffQueryVO staffQueryVO) throws Exception{
		String uuid = UUID.randomUUID().toString();
		List<Object> result = baseDao.findBySql(getQuerySqlByStaffQueryVO(staffQueryVO));
		for (Object object : result) {
			Object[] objs = (Object[]) object;
			String userId  = (String) objs[0];
			StaffEntity staff = staffDao.getStaffByUserID(userId);
			User user = identityService.createUserQuery().userId(userId).singleResult();
			List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
			Group group = null;
			for(Group _group: groups){
				group = _group;
				String[] posList = group.getType().split("_");
				//总部优先
				if(CompanyIDEnum.QIAN.getValue()== Integer.parseInt(posList[0])){
					break;
				}
			}
			String departmentId = group.getType().split("_")[1];
			String positionId = group.getType().split("_")[2];
			DepartmentEntity department = departmentDao.getDepartmentByDepartmentID(Integer.parseInt(departmentId));
			PositionEntity position = positionDao.getPositionByPositionID(Integer.parseInt(positionId));
			if(null==department || null==position || null==user){
				continue;
			}
			//个人信息、照片、二维码放在一个文件夹里，文件夹命名方式：姓名-工号
			File directory = new File(Constants.HR_FILE_DIRECTORY+"cardInfo"+uuid+"/"+staff.getStaffName()+"-"+user.getFirstName()+"/");
			directory.mkdirs();
			//个人信息txt文件
			File personalInfoFile = new File(directory+"/个人信息.txt");
			personalInfoFile.createNewFile();
			@Cleanup
			PrintWriter pw = new PrintWriter(personalInfoFile);
			pw.println("姓名："+staff.getStaffName());
			pw.println("花名："+user.getLastName());
			pw.println("工号："+user.getFirstName());
			pw.println("职位："+department.getDepartmentName()+" "+position.getPositionName());
			Picture picture = null;
			try {
				//照片
				picture = identityService.getUserPicture(userId);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(staff.getStaffName()+"没有照片");
			}
			if(null == picture){
				continue;
			}
			File pictureFile = new File(directory+"/照片.png");
			FileUtil.inputstreamtofile(picture.getInputStream(), pictureFile);
			//二维码
			String weixinCodeId = staff.getWeixinCodeId();
			if(StringUtils.isBlank(weixinCodeId)){
				continue;
			}
			CommonAttachment attachment = noticeService.getCommonAttachmentById(Integer.parseInt(weixinCodeId));
			File weixinCodeFileSrc = new File(attachment.getSoftURL());
			File weixinCodeFileDes = new File(directory+"/二维码.png");
			FileUtils.copyFile(weixinCodeFileSrc, weixinCodeFileDes);
		}
		//胸卡信息的压缩包
		FileOutputStream cardZip = new FileOutputStream(new File(Constants.HR_FILE_DIRECTORY+"/胸卡信息"+uuid+".zip"));
		ZipUtil.toZip(Constants.HR_FILE_DIRECTORY+"cardInfo"+uuid, cardZip, true);
		FileUtils.deleteDirectory(new File(Constants.HR_FILE_DIRECTORY+"cardInfo"+uuid));
		return new FileInputStream(new File(Constants.HR_FILE_DIRECTORY+"/胸卡信息"+uuid+".zip"));
	}

	@Override
	public String checkStaffMeetResignation(String userId) {
		StaffEntity staff = staffDao.getStaffByUserID(userId);
		//检查是否是正式工，试用期员工不做任何限制
		if(staff.getStatus() == StaffStatusEnum.FORMAL.getValue()){
			Calendar cal = Calendar.getInstance();
			Date formalDate = staff.getFormalDate();
			if(null == formalDate){
				Date entryDate = staff.getEntryDate();
				cal.setTime(entryDate);
				//若转正日期为空，即默认转正日期为入职后3个月
				cal.add(Calendar.MONTH, 3);
				formalDate = cal.getTime();
			}
			cal.setTime(formalDate);
			cal.add(Calendar.MONTH, 6);
			Date limitDate = cal.getTime();
			Date now = new Date();
			//转正后，1-6个月内无法在OA系统中提交离职申请
			if(!DateUtil.after(DateUtil.getSimpleDate(DateUtil.formateDate(now)), limitDate)){
				return "1";
			}
			cal.setTime(now);
			int day = cal.get(Calendar.DATE);
			//仅可在每个月的25号这一天在OA系统中提出离职申请
			if(day != 25){
				return "2";
			}
		}
		return "3";
	}

	@Override
	public List<String> getStaffDepartmentNames(String userId, int index) {
		Integer departmentId = departmentDao.getDeparmentIdByUserId(userId);
		List<String> deparmentNames = new ArrayList<>();
		while(index>0){
			DepartmentEntity dep = departmentDao.getDepartmentByDepartmentID(departmentId);
			if(null == dep){
				break;
			}
			deparmentNames.add(dep.getDepartmentName());
			departmentId = dep.getParentID();
			index--;
		}
		List<String> deparmentNamesOrderByDesc = new ArrayList<>(deparmentNames.size());
		//倒序
		for(int i=0; i<deparmentNames.size(); i++){
			deparmentNamesOrderByDesc.add(i, deparmentNames.get(deparmentNames.size()-i-1));
		}
		return deparmentNamesOrderByDesc;
	}
}
