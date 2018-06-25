package com.zhizaolian.staff.service;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.activiti.engine.identity.Group;
import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.VacationEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.VacationDetailVo;
import com.zhizaolian.staff.vo.VacationTaskVO;
import com.zhizaolian.staff.vo.VacationVO;

public interface VacationService {

	/**
	 * 启动一个请假流程
	 * @param vacation
	 * @param attachmentFileName 
	 * @param attachment 
	 */
	void startVacation(VacationVO vacation, File[] attachment, String[] attachmentFileName) throws Exception;
	
	/**
	 * 根据用户组列表，查找有权限的请假申请
	 * @param groupIDs
	 * @return
	 */
	ListResult<VacationTaskVO> findVacationTasksByGroups(List<Group> groups, int page, int limit);
	
	/**
	 * 分页查询指定用户的请假申请
	 * @param userId
	 * @return
	 */
	ListResult<VacationVO> findVacationListByUserID(String userID, int page, int limit);
	
	/**
	 * 更新请假流程节点审批结果
	 * @param userID
	 * @param taskResult
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
	
	/**
	 * 查询指定公司，指定日期所有已审批的请假记录
	 * @param companyID
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	List<VacationVO> findVacationsByCompanyAndDate(Integer companyID, Date date) throws Exception;
	
	/**
	 * 按月统计给定公司员工考勤信息
	 * @param companyID
	 * @param date
	 * @param page
	 * @param limit
	 * @return
	 * @throws Exception 
	 */
	ListResult<VacationVO> findStatisticsPageListByCompanyAndMonth(Integer companyID,String userName, Date date, int page, int limit) throws Exception;

	/*根据请假人的id查询其所有请假记录
	 * @param requestUserID
	 * */
	
	List<VacationEntity> findVacationsByRequestUserID(String requestUserID);
	
	/*
	 * 验证请假申请是否重复
	 * */
	
	boolean isDuplicateClaim(VacationVO vacationVO);

	/**
	 * 计算给定时间段内请假的时长
	 * @param begin
	 * @param end
	 * @param userID
	 * @return
	 */
	long calcVacationTime(Date begin, Date end, String userID);
	//更新请假申请
	void updateVacation(VacationVO vacationVO) throws Exception;
	//新增请假申请
	void saveVacation(VacationVO vacation) throws Exception;
	
	//删除vacation
	void deleteVacationByVacationID(int vacationID);
	
	VacationVO findVacationByVacationID(int vacationID);
	
	/**
	 * 根据流程ID查找请假记录
	 * @param processInstanceID
	 * @return
	 */
	VacationVO getVacationByProcessInstanceID(String processInstanceID);
	
	List<VacationTaskVO> createTaskVOListByTaskList(List<Task> tasks);
	
	VacationTaskVO getVacationTaskVOByTask(Task task);
	
	VacationVO getDaysAndHours(String beginDate,String endDate,String userID,Integer companyID, Integer departmentId) throws Exception;
	
	/**
	 * 开始时间 结束时间 包含  time  按照  开始时间 正序的 第一条记录 (用于迟到时间的判断)
	 * @param time
	 * @return 开始时间
	 */
	Date getBeginAndEndTimeFirst(String time,String userId);
	/**
	 * 开始时间 结束时间 包含  time  按照  结束时间 倒序排序的 第一条记录 (用于早退时间的判断)
	 * @param time
	 * @return 结束时间
	 */
	Date getBeginAndEndTimeLast(String time,String userId);
	
	/**
	 * 获取 从startTime 到 endTime的有效请假时间 (仅仅用于 早退和 迟到的判断)(时间区间内存在多条记录只取第一条进行计算)
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	long getEffectiveVacationTime(Date startTime,Date endTime,String userId);
	
	String getInstanceIdByVacationId(String id);
	/**
	 * 获取实际的请假结束时间  按照结束时间倒序排序的 第一条记录
	 * @param time 打卡开始时间
	 * @param userId
	 * @return 实际的请假结束时间
	 */
	Date getRealEndTimeLast(String time, String userId);
	/**
	 * 获取时间的请假开始时间 按照开始时间顺序 排序的 第一条记录
	 * @param time 打卡结束时间
	 * @param userId
	 * @return 实际的请假开始时间
	 */
	Date getRealBeginTimeFirst(String time, String userId);
	/**
	 * 获取流程实例id
	 * @param vacationId
	 * @return
	 */
	String getProcessInstanceId(Integer vacationId);
	
	VacationTaskVO getVacationTaskVOByTask(String processInstanceId);

	List<VacationDetailVo> getVacationDetailObjs(String companyId) throws Exception;

	InputStream exportVacationDetail(List<VacationDetailVo> vacationDetailVos) throws Exception;

	String[] getVacationTextAndHours(String id, String beginTime, String endTime) throws Exception;

	String[] getVacationTextAndHoursForHR(String id, String beginTime, String endTime) throws Exception;

	List<VacationEntity> getUnderlingVacationVos(String vacationDate,
			List<Integer> departmentIds, String userId) throws Exception;

	boolean checkVacation(VacationVO vacationVO) throws Exception;

	String getStaffAnnualVacationInfo(String userID) throws Exception;

	String checkMeetConditions(String requestUserID, String beginDate, String endDate) throws Exception;

	double calcWorkHours(Date beginDate, Date endDate, String[] workTimeArray);

	double calcRestHours(Date beginDate, Date endDate, String[] workTimeArray);

	List<String> findVacationUsers(String vacationId);

	boolean hasMarriageHoliday(String requestUserId);

	VacationVO getDaysAndHoursForCalSalary(String beginDate, String endDate, String userID, Integer companyID,
			Integer departmentId) throws Exception;

}
