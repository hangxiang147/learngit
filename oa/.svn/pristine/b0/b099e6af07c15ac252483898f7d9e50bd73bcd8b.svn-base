package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.MeetingActorDao;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.MeetingActorEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.MeetingActorService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.DepartmentVOTransformer;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.MeetingActorVO;

public class MeetingActorServiceImpl implements MeetingActorService {
	@Autowired
	private MeetingActorDao meetingActorDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private DepartmentDao departmentDao;
	@Autowired
	private StaffService staffService;
	
	@Override
	public void saveMettingByCompany(Integer companyID, Integer departmentID, Integer meetingID, Integer type) {
		List<Object> list=baseDao.findBySql(getQuerySqlByNoticeVO1(companyID,departmentID));
		for(Object obj:list){
			MeetingActorEntity meetingActorEntity=new MeetingActorEntity();
			Date date=new Date();
			String userID=(String)obj;
			meetingActorEntity.setUserID(userID);
			meetingActorEntity.setActorType(type);
			meetingActorEntity.setAddType(1);
			meetingActorEntity.setMeetingID(meetingID);
			meetingActorEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			meetingActorEntity.setAddTime(date);
			meetingActorEntity.setUpdateTime(date);
			meetingActorDao.save(meetingActorEntity);
		}
	}
	
	private List<DepartmentVO> findDepartmentsByCompanyIDParentID(int companyID, int parentID) {
		List<DepartmentEntity> departmentEntities = departmentDao.findDepartmentsByCompanyIDParentID(companyID, parentID);
		if (departmentEntities.size()<= 0){
			return Collections.emptyList();
		}		
		List<DepartmentVO> departmentVOs = Lists2.transform(departmentEntities, DepartmentVOTransformer.INSTANCE);
		List<DepartmentVO> result = new ArrayList<DepartmentVO>();
		for (DepartmentVO department : departmentVOs){
			result.addAll(findDepartmentsByCompanyIDParentID(companyID, department.getDepartmentID()));
		}
		result.addAll(departmentVOs);
		return result;
	}	
	
	String getQuerySqlByNoticeVO1(Integer companyID, Integer departmentID){
		StringBuffer sql=new StringBuffer("select membership.USER_ID_ from ACT_ID_MEMBERSHIP membership "
				+ " left join OA_GroupDetail detail on membership.GROUP_ID_ = detail.GroupID "
				+ " left join OA_Staff staff on membership.USER_ID_ = staff.UserID "
				+ "where detail.IsDeleted = 0 and staff.IsDeleted = 0 and staff.Status != 4 ");
		if (companyID != null) {
			sql.append(" and detail.CompanyID = ").append(companyID);
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
				sql.append(" and detail.DepartmentID in ("+arrayString.substring(1, arrayString.length()-1)+")");
			}
		}
		return sql.toString();
	}

	@Override
	public void saveMettingByUserID(Integer meetingID, Integer type, String userID) {
		MeetingActorEntity meetingActorEntity=new MeetingActorEntity();
		Date date=new Date();
		meetingActorEntity.setActorType(type);
		meetingActorEntity.setAddType(2);
		meetingActorEntity.setMeetingID(meetingID);
		meetingActorEntity.setUserID(userID);
		meetingActorEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		meetingActorEntity.setAddTime(date);
		meetingActorEntity.setUpdateTime(date);
		meetingActorDao.save(meetingActorEntity);
	}
	
	@Override
	public Set<MeetingActorVO> findMeetingActorVOByMeetingID(Integer meetingID,Integer actorType) {
		List<MeetingActorEntity> meetingActorEntities = meetingActorDao.findMeetingActorByMeetingID(meetingID,actorType);
		Set<MeetingActorVO> meetingActorVOs = new HashSet<>();
		for(MeetingActorEntity meetingActorEntity:meetingActorEntities){
			MeetingActorVO meetingActorVO = new MeetingActorVO();
			meetingActorVO.setUserName(staffService.getStaffByUserID(meetingActorEntity.getUserID()).getLastName());
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(meetingActorEntity.getUserID());
			meetingActorVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
				@Override
				protected String safeApply(GroupDetailVO input) {
					return input.getCompanyName()+"-"+input.getDepartmentName()+"-"+input.getPositionName();
				}
			}));
			meetingActorVOs.add(meetingActorVO);
			}
		return meetingActorVOs;

	}
	
	

}
