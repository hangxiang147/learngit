package com.zhizaolian.staff.service;

import java.util.List;

import org.activiti.engine.identity.Group;

import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.GroupDetailEntity;
import com.zhizaolian.staff.enums.AlterationTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GradeVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.PositionAlterationVO;
import com.zhizaolian.staff.vo.PositionVO;
import com.zhizaolian.staff.vo.StaffVO;


public interface PositionService {

	/**
	 * 查询所有公司
	 * @return
	 */
	List<CompanyVO> findAllCompanys();
	
	/**
	 * 根据公司ID和部门ID，递归查询该公司部门下所有的子部门
	 * parentID 为0时，查询指定公司的一级部门
	 * @param companyID
	 * @param parentID
	 * @return
	 */
	List<DepartmentVO> findDepartmentsByCompanyIDParentID(int companyID, int parentID);
	
	/**
	 * 根据公司ID和部门ID查询该公司部门的下一级子部门
	 * parentID 为0时，查询指定公司的一级部门
	 * @param companyID
	 * @param parentID
	 * @return
	 */
	List<DepartmentVO> findDepartmentsByCompanyIDDepartmentID(int companyID, int departmentID);
	
	/**
	 * 根据公司ID查询该公司所有部门
	 * @param companyID
	 * @return
	 */
	List<DepartmentVO> findDepartmentsByCompanyID(int companyID);
	
	/**
	 * 查询所有职务
	 * @return
	 */
	List<PositionVO> findAllPositions();
	
	/**
	 * 查询指定职务信息
	 * @param positionID
	 * @return
	 */
	PositionVO getPositionByPositionID(int positionID);
	
	/**
	 * 查询指定部门的所有职务
	 * @param departmentID
	 * @return
	 */
	List<PositionVO> findPositionsByDepartmentID(int departmentID);
	
	/**
	 * 查询所有职级
	 * @return
	 */
	List<GradeVO> findAllGrades();
	
	/**
	 * 分页查询给定查询条件的岗位信息列表
	 * @param companyID
	 * @param departmentID
	 * @param positionID
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<GroupDetailVO> findGroupDetailPageList(Integer companyID, Integer departmentID, Integer positionID, int page, int limit);
	
	/**
	 * 修改给定岗位的岗位职责
	 * @param groupDetailID
	 * @param responsibility
	 */
	void updateResponsibility(int groupDetailID, String responsibility);
	
	/**
	 * 根据职务名称查找职务ID
	 * @param name
	 * @return
	 */
	int getPositionIDByName(String name);
	
	/**
	 * 根据部门ID和职务名称查找职务信息
	 * @param departmentID
	 * @param name
	 * @return
	 */
	PositionVO getPositionByDepartmentIDName(int departmentID, String name);
	
	/**
	 * 查找指定公司部门的部门ID
	 * @param companyID
	 * @param name
	 * @return
	 */
	int getDepartmentIDByCompanyIDAndName(int companyID, String name);
	
	/**
	 * 根据ID查找部门信息
	 * @param departmentID
	 * @return
	 */
	DepartmentVO getDepartmentByID(int departmentID);
	
	/**
	 * 根据group列表，查找具体的岗位信息列表
	 * @param groups
	 * @return
	 */
	List<GroupDetailVO> findGroupDetailsByGroups(List<Group> groups);
	
	/**
	 * 添加员工岗位变动记录
	 * @param userID 员工userID
	 * @param GroupID 用户组ID
	 * @param alterationType 变动类型
	 * @param operationUserID 操作人
	 */
	void addPositionAlteration(String userID, String GroupID, AlterationTypeEnum alterationType, String operationUserID);

	/**
	 * @author  wjp
	 * @param userID 员工的userID
	 * @param page 第几页
	 * @param limit 每页显示的数量 
	 */
	ListResult<PositionAlterationVO> positionHistory(String userID, Integer page, Integer limit);

	ListResult<StaffVO> getSelectedStaff(Integer companyID, Integer departmentID, Integer positionID, String staffName,
			List<Integer> statusList, Integer page, Integer limit);//wjp

	/**
	 * 获取指定公司规定每日工作时长
	 * @param companyID
	 * @return
	 */
	double getDailyHoursByCompanyID(CompanyIDEnum companyID);
	
	/**
	 * 获取指定公司规定上班时间
	 * @param companyID
	 * @return
	 */
	String getBeginTimeByCompanyID(CompanyIDEnum companyID);
	
	/**
	 * 获取指定公司规定下班时间
	 * @param companyID
	 * @return
	 */
	String getEndTimeByCompanyID(CompanyIDEnum companyID);
	
	/*List<User> getUsersByGroupVOs(List<GroupDetailVO> groupDetailVOs);*/
	
	List<GroupDetailEntity> findGroupDetailEntitiesByGroupIDs(List<String> groupIDs);
	
	//根据groupID查询groupdetail;
	GroupDetailVO findGroupDetailByGroupID(String groupID);
	
	//根据deparmentID查询部门
	DepartmentVO findDepartmentsByDempartmentID(int departmentID);
	
	//根据companyID,DepartmentID,positionID找到groupdetail;	
	GroupDetailVO findGroudetailByDepartmentIDPositionID(Integer departmentID, Integer positionID);
	//根据部门信息找到查询子部门
	List<DepartmentVO> findDepartmentsByParentID( int parentID);
	//根据部门ID查询公司ID
	int getCompanyIDByDepartmentID(int departmentID);
	//根据公司ID,查找公司
	CompanyVO getCompanyByCompanyID(int companyID);
	
	void addDepartment(String parentName,String parentId,String companyId,String level);
	void addPostion(String parentName,String parentId,String companyId, String positionType);

	List<String> getAllParentDepIds(String depId);

	void updateName(String type, String id, String name);

	boolean checkHasPerson(String type, String id, String companyId);

	void deleteDepOrPos(String type, String id);

	DepartmentEntity findAllBigDepartmentsBycompanyId(String companyId);

	void savePositonType(String positionId, String positionType);

}
