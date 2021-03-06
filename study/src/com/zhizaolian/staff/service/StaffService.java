package com.zhizaolian.staff.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zhizaolian.staff.entity.SalaryDetailEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.AuditStatusEnum;
import com.zhizaolian.staff.enums.NicknameStatusEnum;
import com.zhizaolian.staff.enums.NicknameTypeEnum;
import com.zhizaolian.staff.enums.StaffStatusEnum;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.NicknameVO;
import com.zhizaolian.staff.vo.StaffAuditVO;
import com.zhizaolian.staff.vo.StaffInfoAlterationVO;
import com.zhizaolian.staff.vo.StaffQueryVO;
import com.zhizaolian.staff.vo.StaffVO;
import com.zhizaolian.staff.vo.TaskVO;

public interface StaffService {


	List<SalaryDetailEntity> getSalarys(String userId);
	
	List<String> getItemMonthSalaryResult(String userId,String year,String month);
	void  saveSalarys(SalaryDetailEntity salaryDetailEntity);
	
	String[] getSalaryMobileDetail(String year,String month);
	String[] getSalaryEmailDetail(String year,String month);
	
	List<Object> getUnPostMobiles(String year,String month);
	void completeEmail(String year,String month,String usrId);
	List<Object> getUnPostEmails(String year,String month);
	void completeMobile(String year,String month,String usrId);
	int addStaff(StaffVO staffVO) throws Exception;
	
	/**
	 * 验证用户身份（根据用户名称和密码）
	 * @param userName
	 * @param password
	 * @return
	 */
	User loginValidate(String userName, String password);
	
	/**
	 * 查询系统当前最大的员工工号
	 * @return
	 */
	String getLatestStaffNumber();
	
	/**
	 * 根据用户查询他所在的用户组
	 * @param userId
	 * @return
	 */
	List<Group> findGroups(String userId);
	
	/**
	 * 根据岗位信息查找用户组
	 * @param companyID
	 * @param departmentID
	 * @param positionID
	 * @return
	 */
	Group getGroup(int companyID, int departmentID, int positionID);
	
	/**
	 * 根据UserID查询员工基本信息
	 * @param userID
	 * @return
	 */
	StaffVO getStaffByUserID(String userID);
	
	/**
	 * 修改员工基本信息
	 * @param staffVO
	 */
	void updateStaff(StaffVO staffVO) throws Exception;
	
	/**
	 * 修改员工职位信息
	 * @param staffVO
	 */
	/*void updateStaffPosition(StaffVO staffVO);*/
	
	/**
	 * 分页查询给定在职状态的员工列表
	 * @param page
	 * @param limit
	 * @return
	 */
	List<StaffVO> findStaffPageListByStatusList(List<Integer> statusList, int page, int limit);
	
	/**
	 * 统计给定在职状态的员工总数
	 * @param statusList
	 * @return
	 */
	int countStaffByStatusList(List<Integer> statusList);
	
	/**
	 * 删除员工登录账号
	 * @param userID
	 */
	void deleteStaffLoginAccount(String userID);

	/**
	 * 修改给定员工的在职状态
	 * @param userID
	 * @param status
	 */
	void updateStaffStatus(String userID, StaffStatusEnum status);
	
	/**
	 * 对指定员工执行离职操作
	 * @param userID  员工ID
	 * @param leaveDate  离职日期
	 */
	void doLeave(String userID, Date leaveDate);
	
	/**
	 * 修改给定员工的在职状态，转正日期
	 * @param userID
	 * @param status
	 * @param formalDate
	 */
	void updateStaffStatusFormalDate(String userID, StaffStatusEnum status, Date formalDate);
	
	/**
	 * 删除员工信息，软删除
	 * @param userID
	 */
	void deleteStaff(String userID);
	
	/**
	 * 分页查询给定查询条件的员工列表
	 * @param staffQueryVO
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<StaffVO> findStaffPageListByQueryVO(StaffQueryVO staffQueryVO, int page, int limit);
	
	/**
	 * 分页查询给定查询条件的员工信息列表
	 * @param name
	 * @param auditStatus
	 * @param companyID
	 * @param departmentID
	 * @param page
	 * @param limit  
	 * @return
	 */
	ListResult<StaffVO> findStaffPageList(String name, AuditStatusEnum auditStatus, Integer companyID, Integer departmentID,int page, int limit);
	
	/**
	 * 查询给定用户的岗位信息列表
	 * @param userID
	 * @return
	 */
	List<GroupDetailVO> findGroupDetailsByUserID(String userID);
	
	/**
	 * 查找给定花名类型下的所有花名信息
	 * @param nicknameType
	 * @return
	 */
	List<NicknameVO> findNicknamesByType(NicknameTypeEnum nicknameType);
	
	/**
	 * 根据ID查找花名信息
	 * @param nicknameID
	 * @return
	 */
	NicknameVO getNicknameByID(int nicknameID);
	
	/**
	 * 修改给定花名使用状态
	 * @param nicknameID
	 * @param status
	 */
	void updateNicknameStatus(int nicknameID, NicknameStatusEnum status);
	
	/**
	 * 更新给定员工信息审核结果
	 * @param userID
	 * @param educationID
	 * @param degreeID
	 * @param criminalRecord
	 * @param auditStatus
	 */
	void updateAuditResult(String userID, String educationID, String degreeID, String criminalRecord, AuditStatusEnum auditStatus);
	
	/**
	 * 更新给定员工信息审核结果  wjp
	 * @param staffID
	 * @param auditStatus
	 */
	void updateAuditResult(String staffID, AuditStatusEnum auditStatus);  //这个方法是执行审核状态改变时候的方法

	void updateStaff(StaffInfoAlterationVO staffInfoAlteration); //修改员工信息表的的薪资和职级  wjp
	
	void updateStaffInformation(StaffVO staffVO);
	
	/**
	 * 查找给定公司部门及其子部门的所有员工
	 * @param companyID
	 * @param departmentID
	 * @return
	 */
	List<StaffVO> findStaffsByCompanyIDDepartmentID(int companyID, int departmentID);
	
	/**
	 * 查找给定公司的所有员工
	 * @param companyID
	 * @param departmentID
	 * @return
	 */
	List<StaffVO> findStaffsByCompanyID(int companyID);

	/**
	 * 根据姓名查找员工信息
	 * @param name
	 * @return
	 */
	List<StaffVO> findStaffByName(String name);
	List<StaffVO> findStaffByName(String name,int limit);

	List<StaffVO> getStaffVOsByUsers(List<User> users);
	
	List<StaffVO> findStaffByNameAndtStatus(String name,Integer positionCategory);
    
    //删除员工关系
    void deleteStaffMembership(String userID);

	/**
	 * 根据用户ID查找上级主管ID
	 * @param userID
	 * @return
	 */
	String querySupervisor(String userID);
	String querySupervisorOneStep(String userID);
	/**
	 * 根据用户ID查找所在分部的总经理ID
	 * @param userID
	 * @return
	 */
	String queryManager(String userID);
	
	/**
	 * 根据用户ID查找所在分部的人事岗位ID
	 * @param userID
	 * @return
	 */
	List<String> queryHRGroupList(String userID);
	List<String> queryHRGroupList(Integer companyId);

	/**
	 * 检查指定岗位列表下是否有对应员工
	 * 如所有岗位下均没有员工，返回false，否则返回true
	 * @param groups
	 * @return
	 */
	boolean hasGroupMember(List<String> groups);
	
	
	public ListResult<StaffVO> findStaffList(StaffVO staffVO,int limit,int page,int companyID);
	
	public StaffVO getStaffByStaffID(int staffID);
	
	//-------------------------------
	public StaffVO getStaffByUser_ID(String userID);
	//-------------------------------
	
	
	//转正记录查询
	public ListResult<StaffVO> findStaffRegularRecord(StaffVO staffVO,int limit,int page);
	
	public StaffVO getStaffByTelephone(String telephone);
	
	String updateStaffTelephone(StaffVO staffVO);
	
	void updateAttachementNames(String pictureName,Integer staffID);
	
	/**
	 * 启动一个背景调查流程
	 * @param userID
	 */
	void startAudit(StaffAuditVO staffAuditVO);
	
	/**
	 * 保存员工背景信息 ,更新员工背景调查状态
	 * @param staffAuditVO
	 */
	void saveAudit(StaffAuditVO staffAuditVO, String processInstanceID);
	
	/**
	 * 根据用户组列表，查找有权限审核的背景调查
	 * @param groups
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<TaskVO> findAuditTasksByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page, int limit);
	

	/**
	 * 查询 一定时间内生成的 验证 
	 * @param userId
	 * @param isRecent  是否是最近 一小时 / 因为一直是 用验证码 检验跳转的有效性  
	 * 在跳转到第二个页面和最后的提交 仍然需要 验证 但是 这时候 只需要取最近的一个验证码 而不是 指定时间内的
	 * @return
	 */
	String getUsefulValidateKeyByUserId(String userId,Boolean isRecent);
	
	/**
	 * 新增一个验证码
	 * @param userId
	 * @param validateKey
	 */
	void  insertRestValidateKey(String userId,String validateKey);
	
	/**
	 * 修改 user的password
	 * @param userId
	 * @param newPassword
	 */
	void updateUserPassword(String userId,String newPassword);
	
	String getRealNameByUserId(String userId);

	String queryHeadMan(String userID);
	
	List<Object> getStaffByName(String staffName);
	
	String getTelephoneByUserId(String userId);
	
	List<Object> getStaffNameByTelephone(String name);
	
	SalaryDetailEntity getSaleryById(String id);
	/**
	 * 获取部门下面所有的人员
	 * @param departmentID
	 * @param underlings
	 * @return
	 */
	List<String> getDepartmentStaffs(Integer companyID, Integer departmentID, List<String> underlings, int level, boolean isLeader);
	List<String> getUsrListByPositionId(Integer positionID);
	/**
	 * 检查员工是否在职
	 * @param userId
	 * @return
	 */
	boolean checkStaffIsInJob(String userId);
	/**
	 * 检查人员是否有未完结的离职申请
	 * @param applyUserId
	 * @return
	 */
	boolean checkIsApplyQuit(String applyUserId);

	Set<String> getDepartmentAllStaffs(Integer companyID, Integer departmentID,Set<String> underlings);

	boolean checkTelephone(String telephone, String staffId);
	/**
	 * 是否是合伙人
	 * @param userId
	 * @return
	 */
	boolean isPartner(String userId);
	String getAdminId();
	/**
	 * 获取员工的入职天数
	 * @param requestUserID 
	 * @return
	 */
	int getComeDays(String requestUserID, String beginDate);

	void saveStaffSalaryChange(String id, StaffVO staffVO, String salary);

	ListResult<Object> getInsuranceList(String year, String month, String status, String companyId, Integer limit, Integer page);

	List<Object> getAllInsuranceList(String year, String month, String status, String companyId);

	InputStream exportInsuranceList(List<Object> insuranceList, String status, String title) throws Exception;

	StaffVO getStaffByOpenId(String openid);

	void updateStaffOpenId(String id, String openid);

	String getStaffHeadImg(String id);

	void updateStaffHeadImgId(String userId, int headImgId);
	
	StaffEntity getStaffByUserId(String userId);
	
	StaffEntity getInJobStaffByUserId(String userId);

	int getTotalStaffNum();

	int getProbationNum();

	int getFormalStaffNum();

	List<StaffEntity> getQuitingStaffs();

	List<StaffEntity> findAllBirthStaffsByCurrentMonth();

	List<StaffEntity> findAllAnniversaryStaffsByCurrentMonth();

	StaffEntity getStaffByUnionid(String unionid);
	
	List<Object> findUserList();
	
	void updatePwdById(String userPWD,String userID);
	
	void updateStaffUnionId(String id, String unionid);

	List<Object> getStaffsByPositionId(String positionId);

	boolean isPM(String userId);
	//获得入职天数
	String getDayDiff(Date beginDate,Date endDate,Integer type);
	ListResult<StaffEntity> findAllInJobStaffs(String staffName, int limit, int page);
	/**
	 * 是否是白领
	 * @param userId
	 * @return
	 */
	boolean isWhiteJob(String userId);
	XSSFWorkbook exportStaffQueryVO(StaffQueryVO staffQueryVO);

	InputStream downloadStaffCardInfos(StaffQueryVO staffQueryVO) throws Exception;

	String checkStaffMeetResignation(String userId);

	String getStaffDepartmentNames(String userID);
	/**
	 * 查询index级部门
	 * @param userId
	 * @param index
	 * @return
	 */
	List<String> getStaffDepartmentNames(String userId, int index);
}