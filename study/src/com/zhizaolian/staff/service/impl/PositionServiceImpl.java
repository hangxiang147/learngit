package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.CompanyDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.GradeDao;
import com.zhizaolian.staff.dao.GroupDetailDao;
import com.zhizaolian.staff.dao.PositionAlterationDao;
import com.zhizaolian.staff.dao.PositionDao;
import com.zhizaolian.staff.dao.StaffDao;
import com.zhizaolian.staff.entity.CompanyEntity;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.GradeEntity;
import com.zhizaolian.staff.entity.GroupDetailEntity;
import com.zhizaolian.staff.entity.PositionAlterationEntity;
import com.zhizaolian.staff.entity.PositionEntity;
import com.zhizaolian.staff.entity.StaffEntity;
import com.zhizaolian.staff.enums.AlterationTypeEnum;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.PositionEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.CompanyVOTransformer;
import com.zhizaolian.staff.transformer.DepartmentVOTransformer;
import com.zhizaolian.staff.transformer.GradeVOTransformer;
import com.zhizaolian.staff.transformer.GroupDetailVOTransformer;
import com.zhizaolian.staff.transformer.PositionAlterationVOTransformer;
import com.zhizaolian.staff.transformer.PositionVOTransformer;
import com.zhizaolian.staff.transformer.StaffVOTransformer;
import com.zhizaolian.staff.utils.EscapeUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GradeVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.PositionAlterationVO;
import com.zhizaolian.staff.vo.PositionVO;
import com.zhizaolian.staff.vo.StaffVO;

public class PositionServiceImpl implements PositionService{

	@Autowired
	private BaseDao baseDao;
	
	@Autowired
	private CompanyDao companyDao;
	
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private PositionDao positionDao;
	
	@Autowired
	private GradeDao gradeDao;
	
	@Autowired
	private GroupDetailDao groupDetailDao;
	
	@Autowired
	private PositionAlterationDao positionAlterationDao;
	
	@Autowired
	private StaffDao staffDao;
	
	@Autowired
	private StaffService staffService;
	@Override
	public List<CompanyVO> findAllCompanys() {
		List<CompanyEntity> companyEntities = companyDao.findAllCompanys();
		return Lists2.transform(companyEntities, CompanyVOTransformer.INSTANCE);
	}
	
	@Override
	public List<DepartmentVO> findDepartmentsByCompanyIDParentID(int companyID, int parentID) {
		List<DepartmentEntity> departmentEntities = departmentDao.findDepartmentsByCompanyIDParentID(companyID, parentID);
		if (departmentEntities.size() <= 0) {
			return Collections.emptyList();
		}
		
		List<DepartmentVO> departmentVOs = Lists2.transform(departmentEntities, DepartmentVOTransformer.INSTANCE);
		List<DepartmentVO> result = new ArrayList<DepartmentVO>();
		for (DepartmentVO department : departmentVOs) {
			result.addAll(findDepartmentsByCompanyIDParentID(companyID, department.getDepartmentID()));
		}
		result.addAll(departmentVOs);
		return result;
	}
	
	@Override
	public List<DepartmentVO> findDepartmentsByCompanyIDDepartmentID(int companyID, int departmentID) {
		List<DepartmentEntity> departmentEntities = departmentDao.findDepartmentsByCompanyIDParentID(companyID, departmentID);
		return Lists2.transform(departmentEntities, DepartmentVOTransformer.INSTANCE);
	}
	
	@Override
	public List<DepartmentVO> findDepartmentsByCompanyID(int companyID) {
		List<DepartmentEntity> departmentEntities = departmentDao.findDepartmentsByCompanyID(companyID);
		return Lists2.transform(departmentEntities, DepartmentVOTransformer.INSTANCE);
	}
	
	@Override
	public List<PositionVO> findAllPositions() {
		List<PositionEntity> positionEntities = positionDao.findAllPositions();
		return Lists2.transform(positionEntities, PositionVOTransformer.INSTANCE);
	}
	
	@Override
	public PositionVO getPositionByPositionID(int positionID) {
		PositionEntity positionEntity = positionDao.getPositionByPositionID(positionID);
		return PositionVOTransformer.INSTANCE.apply(positionEntity);
	}
	
	@Override
	public List<PositionVO> findPositionsByDepartmentID(int departmentID) {
		List<PositionEntity> positionEntities = positionDao.findPositionsByDepartmentID(departmentID);
		return Lists2.transform(positionEntities, PositionVOTransformer.INSTANCE);
	}
	
	@Override
	public List<GradeVO> findAllGrades() {
		List<GradeEntity> gradeEntities = gradeDao.findAllGrades();
		return Lists2.transform(gradeEntities, GradeVOTransformer.INSTANCE);
	}
	
	@Override
	public ListResult<GroupDetailVO> findGroupDetailPageList(Integer companyID, Integer departmentID, Integer positionID, int page, int limit) {
		List<Object> result = baseDao.findPageList(getQuerySql(companyID, departmentID, positionID), page, limit);
		List<GroupDetailVO> groupDetailVOs = Lists2.transform(result, new SafeFunction<Object, GroupDetailVO>() {
			@Override
			protected GroupDetailVO safeApply(Object input) {
				GroupDetailVO output = new GroupDetailVO();
				Object[] objs = (Object[]) input;
				output.setGroupDetailID((Integer) objs[0]);
				output.setGroupID((String) objs[1]);
				output.setCompanyID((Integer) objs[2]);
				output.setCompanyName((String) objs[3]);
				output.setDepartmentID((Integer) objs[4]);
				output.setDepartmentName((String) objs[5]);
				output.setPositionID((Integer) objs[6]);
				output.setPositionName((String) objs[7]);
				output.setResponsibility(objs[8]==null ? "" : (String) objs[8]);
				PositionEnum position = PositionEnum.valueOf((Integer)objs[9]==null ? 0:(Integer)objs[9]);
				if(null != position){
					output.setPositionType(position.getName());
				}
				return output;
			}
		});
		
		List<Integer> departmentIDs = new ArrayList<Integer>();
		if (companyID != null && departmentID != null) {
			List<DepartmentVO> departmentVOs = findDepartmentsByCompanyIDParentID(companyID, departmentID);
			departmentIDs = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
				@Override
				protected Integer safeApply(DepartmentVO input) {
					return input.getDepartmentID();
				}
			});
			departmentIDs.add(departmentID);
		}
		int count = groupDetailDao.countGroupDetails(companyID, departmentIDs, positionID);
		
		return new ListResult<GroupDetailVO>(groupDetailVOs, count);
	}
	
	@Override
	public void updateResponsibility(int groupDetailID, String responsibility) {
		GroupDetailEntity groupDetailEntity = groupDetailDao.getGroupDetailByID(groupDetailID);
		if (groupDetailEntity == null) {
			throw new RuntimeException("岗位不存在！");
		}
		
		groupDetailEntity.setResponsibility(responsibility);
		groupDetailEntity.setUpdateTime(new Date());
		groupDetailDao.save(groupDetailEntity);
	}
	
	@Override
	public int getPositionIDByName(String name) {
		PositionEntity positionEntity = positionDao.getPositionByPositionName(name);
		return positionEntity==null ? 0 : positionEntity.getPositionID();
	}
	
	@Override
	public PositionVO getPositionByDepartmentIDName(int departmentID, String name) {
		PositionEntity positionEntity = positionDao.getPositionByDepartmentIDName(departmentID, name);
		return PositionVOTransformer.INSTANCE.apply(positionEntity);
	}
	
	@Override
	public int getDepartmentIDByCompanyIDAndName(int companyID, String name) {
		DepartmentEntity departmentEntity = departmentDao.getDepartmentByCompanyIDAndName(companyID, name);
		return departmentEntity==null ? 0 : departmentEntity.getDepartmentID();
	}
	
	@Override
	public DepartmentVO getDepartmentByID(int departmentID) {
		DepartmentEntity departmentEntity = departmentDao.getDepartmentByDepartmentID(departmentID);
		return DepartmentVOTransformer.INSTANCE.apply(departmentEntity);
	}
	
	private String getQuerySql(Integer companyID, Integer departmentID, Integer positionID) {
		StringBuffer sql = new StringBuffer("select gd.GroupDetailID, gd.GroupID, gd.CompanyID, cp.CompanyName, gd.DepartmentID,"
				+ " dp.DepartmentName, gd.PositionID, ps.PositionName, gd.Responsibility, ps.positionType"
				+ " from OA_GroupDetail gd, OA_Company cp, OA_Department dp, OA_Position ps where"
				+ " gd.IsDeleted = 0 and cp.IsDeleted = 0 and dp.IsDeleted = 0 and ps.IsDeleted = 0"
				+ " and gd.CompanyID = cp.CompanyID and gd.DepartmentID = dp.DepartmentID and gd.PositionID = ps.PositionID");
		if (companyID != null) {
			sql.append(" and gd.CompanyID = ").append(companyID);
			if (departmentID != null) {
				List<DepartmentVO> departmentVOs = findDepartmentsByCompanyIDParentID(companyID, departmentID);
				List<Integer> departmentIDs = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(departmentID);
				String arrayString = Arrays.toString(departmentIDs.toArray());
				sql.append(" and gd.DepartmentID in ("+arrayString.substring(1, arrayString.length()-1)+")");
			}
		}
		if (positionID != null) {
			sql.append(" and gd.PositionID = ").append(positionID);
		}
		return sql.toString();
	}
	
	@Override
	public List<GroupDetailVO> findGroupDetailsByGroups(List<Group> groups) {
		return Lists2.transform(groups, new SafeFunction<Group, GroupDetailVO>() {
			@Override
			protected GroupDetailVO safeApply(Group input) {
				String sql = "select gd.GroupDetailID, gd.GroupID, gd.CompanyID, cp.CompanyName, gd.DepartmentID, dp.DepartmentName, gd.PositionID, ps.PositionName"
				+ " from OA_GroupDetail gd, OA_Company cp, OA_Department dp, OA_Position ps where"
				+ " gd.IsDeleted = 0 and cp.IsDeleted = 0 and dp.IsDeleted = 0 and ps.IsDeleted = 0"
				+ " and gd.CompanyID = cp.CompanyID and gd.DepartmentID = dp.DepartmentID and gd.PositionID = ps.PositionID and gd.groupID = '"+input.getId()+"'";
				Object result = baseDao.getUniqueResult(sql);
				if(null == result){
					return null;
				}
				Object[] objs = (Object[]) result;
				GroupDetailVO output = new GroupDetailVO();
				output.setGroupDetailID((Integer) objs[0]);
				output.setGroupID((String) objs[1]);
				output.setCompanyID((Integer) objs[2]);
				output.setCompanyName((String) objs[3]);
				output.setDepartmentID((Integer) objs[4]);
				output.setDepartmentName((String) objs[5]);
				output.setPositionID((Integer) objs[6]);
				output.setPositionName((String) objs[7]);
				return output;
			}
		});
	}
	
	@Override
	public void addPositionAlteration(String userID, String groupID, AlterationTypeEnum alterationType, String operationUserID) {
		if (alterationType == null) {
			throw new RuntimeException("变动类型不合法！");
		}
		
		Date now = new Date();
		PositionAlterationEntity positionAlterationEntity = PositionAlterationEntity.builder()
																	.userID(userID)
																	.groupID(groupID)
																	.alterationType(alterationType.getValue())
																	.operationUserID(operationUserID)
																	.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
																	.addTime(now)
																	.updateTime(now)
																	.build();
		positionAlterationDao.save(positionAlterationEntity);
	}
	

	@Override
	public ListResult<PositionAlterationVO> positionHistory(String userID, Integer page, Integer limit) {
		//得到实体列表
		ListResult<PositionAlterationEntity> positionAlterationEntityList = positionAlterationDao.positionHistory(userID,page,limit);
		if(positionAlterationEntityList==null)
			return null;
		//实体类集合向VO类集合的转换
		List<PositionAlterationVO> positionAlterationVOList = new ArrayList<PositionAlterationVO>();
		for(PositionAlterationEntity positionAlterationEntity :positionAlterationEntityList.getList()) {
			
			PositionAlterationVO positionAlterationVO = new PositionAlterationVO();
			positionAlterationVO = PositionAlterationVOTransformer.entityToVO(positionAlterationEntity);
			//通过得到的数据查询其它数据表，补全VO类
			List<String> groupIDList = new ArrayList<String>();
			groupIDList.add(positionAlterationVO.getGroupID());
			List<GroupDetailEntity> groupDetailEntityList = groupDetailDao.findGroupDetailsByGroupIDs(groupIDList);
			if(org.apache.commons.collections4.CollectionUtils.isEmpty(groupDetailEntityList)){
				continue;
			}
			GroupDetailEntity groupDetailEntity = groupDetailEntityList.get(0);
			Integer companyID = groupDetailEntity.getCompanyID();
			Integer departmentID = groupDetailEntity.getDepartmentID();
			Integer positionID = groupDetailEntity.getPositionID();
			//拿到上面的三个id,查出对应的实体，进而得到对应的名字
			CompanyEntity companyEntity = companyDao.getCompanyByCompanyID(companyID);
			if(null == companyEntity){
				continue;
			}
			DepartmentEntity departmentEntity = departmentDao.getDepartmentByDepartmentID(departmentID);
			if(null == departmentEntity){
				continue;
			}
			PositionEntity positionEntity = positionDao.getPositionByPositionID(positionID);
			if(null == positionEntity){
				continue;
			}
			StaffEntity staffEntityUser = staffDao.getStaffByUserID(positionAlterationVO.getUserID());
			StaffEntity staffEntityOperator = staffDao.getStaffByUserID(positionAlterationVO.getOperationUserID());
			positionAlterationVO.setCompanyID(companyID.toString());
			positionAlterationVO.setDepartmentID(departmentID.toString());
			positionAlterationVO.setPositionID(positionID.toString());
			positionAlterationVO.setCompanyName(companyEntity.getCompanyName());
			positionAlterationVO.setDepartmentName(departmentEntity.getDepartmentName());
			positionAlterationVO.setPositionName(positionEntity.getPositionName());
			positionAlterationVO.setUserName(staffEntityUser.getStaffName());
			positionAlterationVO.setOperationUserName(staffEntityOperator.getStaffName());
			
			positionAlterationVOList.add(positionAlterationVO);
		}
		if(positionAlterationVOList.size()==0){
			return null;
		}
		return new ListResult<PositionAlterationVO>(positionAlterationVOList,positionAlterationVOList.size());
	}
	
	/*
	 * wjp  根据公司名 部门 岗位  姓名模糊查询staffVO集合
	 */
	@Override
	public ListResult<StaffVO> getSelectedStaff(Integer companyID, Integer departmentID, Integer positionID, String staffName,List<Integer> statusList, Integer page,Integer limit) {
		
		//先利用姓名模糊查询出结果集合
		List<StaffEntity> list = staffDao.findStaffByName(staffName);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		//在查询出userid 使用userid查询出groupid
		for(int i = 0; i< list.size(); i++) {
			String userID = list.get(i).getUserID();
			List<GroupDetailVO> groupDetailVOList = staffService.findGroupDetailsByUserID(userID);
			//剔除数据 在公司id 部门id 岗位id中只要不是空值的，要拿来和groupdetail集合进行比较，如果不相符合，就剔除数据，否则保留数据
			for(int j = 0; j<groupDetailVOList.size(); j++) {
				if(companyID!=null) {
					if(groupDetailVOList.get(j).getCompanyID()!=companyID) {
						list.remove(i);
						i--;
						break;
					}
				}
				if(departmentID!=null) {
					if(groupDetailVOList.get(j).getDepartmentID()!=departmentID) {
						list.remove(i);
						i--;
						break;
					}
				}
				if(positionID!=null) {
					if(groupDetailVOList.get(j).getPositionID()!=positionID) {
						list.remove(i);
						i--;
						break;
					}
				}
			}
			
		}
		
		//剔除完不符合的数据之后，进行类型转换
		List<StaffVO> staffVOList = new ArrayList<StaffVO>();
		for(int k = 0; k<list.size(); k++) {
			staffVOList.add(StaffVOTransformer.INSTANCE.apply(list.get(k)));
		}
		//按照page和limit返回所需要的结果
		int beginIndex = (page.intValue()-1)*(limit.intValue());
		int endIndex = (beginIndex + limit.intValue())>=staffVOList.size()?staffVOList.size()-1:beginIndex + limit.intValue();
		List<StaffVO> staffVOs = new ArrayList<StaffVO>();
		for(int i = beginIndex; i<=endIndex; i++) {
			staffVOs.add(staffVOList.get(i));
		}
		return (new ListResult<StaffVO>(staffVOs,staffVOList.size()));
	
	}


	@Override
	public double getDailyHoursByCompanyID(CompanyIDEnum companyID) {
		if (companyID == null) {
			throw new RuntimeException("公司属性不合法！");
		}
		
		switch (companyID) {
		case QIAN:
			return Constants.QA_DAY_HOURS;
		case RUDONG:
			return Constants.RD_DAY_HOURS;
		case NANTONG:
			return Constants.NT_DAY_HOURS;
		case GUANGZHOU:
			return Constants.GZ_DAY_HOURS;
		case FUOSHAN:
			return Constants.FS_DAY_HOURS;
		default:
			return 0;
		}
	}
	
	@Override
	public String getBeginTimeByCompanyID(CompanyIDEnum companyID) {
		if (companyID == null) {
			throw new RuntimeException("公司属性不合法！");
		}
		
		switch (companyID) {
		case QIAN:
			return Constants.QA_TIME_BEGIN;
		case RUDONG:
			return Constants.RD_TIME_BEGIN;
		case NANTONG:
			return Constants.NT_TIME_BEGIN;
		case GUANGZHOU:
			return Constants.GZ_TIME_BEGIN;
		case NANJING:
			return Constants.NJ_TIME_BEGIN;
		default:
			return null;
		}
	}
	
	@Override
	public String getEndTimeByCompanyID(CompanyIDEnum companyID) {
		if (companyID == null) {
			throw new RuntimeException("公司属性不合法！");
		}
		
		switch (companyID) {
		case QIAN:
			return Constants.QA_TIME_END;
		case RUDONG:
			return Constants.RD_TIME_END;
		case NANTONG:
			return Constants.NT_TIME_END;
		case GUANGZHOU:
			return Constants.GZ_TIME_END;
		case NANJING:
			return Constants.NJ_TIME_END;
		default:
			return null;
		}
	}

	/*@Override
	public List<User> getUsersByGroupVOs(List<GroupDetailVO> groupDetailVOs) {
		List<User> users=new ArrayList<>();
		for (GroupDetailVO groupDetailVO : groupDetailVOs) {
			User user=identityService.createUserQuery().memberOfGroup(groupDetailVO.getGroupID()).singleResult();
			users.add(user);
		}		
		
		return users;
	}*/

	@Override
	public List<GroupDetailEntity> findGroupDetailEntitiesByGroupIDs(List<String> groupIDs) {
		// TODO Auto-generated method stub	
		
		return groupDetailDao.findGroupDetailsByGroupIDs(groupIDs);
	}

	
	@Override
	public GroupDetailVO findGroupDetailByGroupID(String groupID){
		GroupDetailEntity groupDetailEntity=groupDetailDao.getGroupDetailByGroupID(groupID);
		return GroupDetailVOTransformer.INSTANCE.apply(groupDetailEntity);
	}

	@Override
	public DepartmentVO findDepartmentsByDempartmentID(int departmentID) {
		DepartmentEntity departmentEntity=departmentDao.getDepartmentByDepartmentID(departmentID);
		return DepartmentVOTransformer.INSTANCE.apply(departmentEntity);
	}

	@Override
	public GroupDetailVO findGroudetailByDepartmentIDPositionID(Integer departmentID,
			Integer positionID) {
		GroupDetailEntity groupDetailEntity=groupDetailDao.geDetailEntityByDepartmentIDPositionID( departmentID, positionID);
		return GroupDetailVOTransformer.INSTANCE.apply(groupDetailEntity);
	}

	@Override
	public List<DepartmentVO> findDepartmentsByParentID(int parentID) {
		List<DepartmentEntity> departmentEntities=departmentDao.findDepartmentByParentID(parentID);
		return  Lists2.transform(departmentEntities, DepartmentVOTransformer.INSTANCE);
		
		
		
	}

	@Override
	public int getCompanyIDByDepartmentID(int departmentID) {
		DepartmentEntity departmentEntity = departmentDao.getDepartmentByDepartmentID(departmentID);		
		return departmentEntity.getCompanyID();
	}

	@Override
	public CompanyVO getCompanyByCompanyID(int companyID) {
		CompanyEntity companyEntity=companyDao.getCompanyByCompanyID(companyID);
		return CompanyVOTransformer.INSTANCE.apply(companyEntity);
	}

	@Override
	public void addDepartment(String parentName, String parentId, String companyId,String level) {
		DepartmentEntity departmentEntity=new DepartmentEntity();
		departmentEntity.setAddTime(new Date());
		departmentEntity.setCompanyID(Integer.parseInt(companyId));
		departmentEntity.setIsDeleted(0);
		departmentEntity.setSort(1F);
		departmentEntity.setParentID(Integer.parseInt(parentId));
		departmentEntity.setDepartmentName(parentName);
		departmentEntity.setLevel(Integer.parseInt(level));
		baseDao.hqlSave(departmentEntity);

	}

	@Override
	public void addPostion(String parentName, String parentId, String companyId, String positionType) {
		PositionEntity positionEntity=new PositionEntity();
		positionEntity.setIsDeleted(0);
		positionEntity.setAddTime(new Date());
		positionEntity.setLevel(1);
		positionEntity.setSort(1F);
		positionEntity.setDepartmentID(Integer.parseInt(parentId));
		positionEntity.setParentID(0);
		positionEntity.setPositionName(parentName);
		positionEntity.setPositionType(Integer.parseInt(positionType));
		baseDao.hqlSave(positionEntity);
	}

	@Override
	public List<String> getAllParentDepIds(String depId) {
		List<String> allParentDepIds = new ArrayList<>();
		int index = 0;
		while(true){
			if(StringUtils.isBlank(depId)){
				break;
			}
			DepartmentEntity dep = departmentDao.getDepartmentByDepartmentID(Integer.parseInt(depId));
			if(null == dep){
				break;
			}
			depId = dep.getParentID()+"";
			//有上级部门
			if(!"0".equals(depId)){
				allParentDepIds.add(depId);
			}
			index++;
			//防止死循环
			if(index>10){
				break;
			}
		}
		return allParentDepIds;
	}

	@Override
	public void updateName(String type, String id, String name) {
		String sql = "update OA_"+type+" set "+type+"Name='"+EscapeUtil.decodeSpecialChars(name)+"' where "+type+"ID="+id;
		baseDao.excuteSql(sql);
	}

	@Override
	public boolean checkHasPerson(String type, String id, String companyId) {
		if("Department".equalsIgnoreCase(type)){
			Set<String> underlings = new HashSet<>();
			staffService.getDepartmentAllStaffs(Integer.parseInt(companyId), Integer.parseInt(id), underlings);
			if(underlings.size()>0){
				return true;
			}
		}else{
			List<String> underlings = staffService.getUsrListByPositionId(Integer.parseInt(id));
			if(underlings.size()>0){
				return true;
			}
		}
		return false;
	}

	@Override
	public void deleteDepOrPos(String type, String id) {
		String sql = "update OA_"+type+" set IsDeleted=1 where "+type+"ID="+id;
		baseDao.excuteSql(sql);
	}

	@Override
	public DepartmentEntity findAllBigDepartmentsBycompanyId(String companyId) {
		String sql = "";
		if(StringUtils.isNotBlank(companyId)){
			sql = "SELECT\n" +
					"	GROUP_CONCAT(DepartmentID),\n" +
					"	DepartmentName\n" +
					"FROM\n" +
					"	OA_Department\n" +
					"WHERE\n" +
					"	ParentID = 0\n" +
					"AND IsDeleted = 0\n" +
					"AND CompanyID = "+companyId+"\n" +
					"AND DepartmentName != '管理层'\n" +
					"AND DepartmentName != '总经办'\n" +
					"GROUP BY\n" +
					"	DepartmentName";
		}else{
			sql = "SELECT\n" +
					"	GROUP_CONCAT(DepartmentID),\n" +
					"	DepartmentName\n" +
					"FROM\n" +
					"	OA_Department\n" +
					"WHERE\n" +
					"	ParentID = 0\n" +
					"AND IsDeleted = 0\n" +
					"AND DepartmentName != '管理层'\n" +
					"AND DepartmentName != '总经办'\n" +
					"GROUP BY\n" +
					"	DepartmentName";
		}
		List<Object> objList = baseDao.findBySql(sql);
		DepartmentEntity department = new DepartmentEntity();
		List<String> departmentIds = new ArrayList<>();
		List<String> departmentNames = new ArrayList<>();
		for(Object obj: objList){
			Object[] objs = (Object[])obj;
			departmentIds.add(String.valueOf(objs[0]));
			departmentNames.add(String.valueOf(objs[1]));
		}
		department.setDepartmentIds(departmentIds);
		department.setDepartmentNames(departmentNames);
		return department;
	}

	@Override
	public void savePositonType(String positionId, String positionType) {
		String sql = "update OA_Position set positionType="+positionType+" where positionID="+positionId;
		baseDao.excuteSql(sql);
	}
}
