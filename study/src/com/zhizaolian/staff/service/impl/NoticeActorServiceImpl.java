package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.dao.NoticeActorDao;
import com.zhizaolian.staff.entity.DepartmentEntity;
import com.zhizaolian.staff.entity.NoticeActorEntity;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.NoticeActorService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.transformer.DepartmentVOTransformer;
import com.zhizaolian.staff.utils.JPushClientUtil;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.NoticeActorVO;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;

public class NoticeActorServiceImpl implements NoticeActorService {
	@Autowired
	private NoticeActorDao noticeActorDao;
    @Autowired
	private BaseDao baseDao;
	@Autowired
	private DepartmentDao departmentDao;
	
	@Autowired
	private StaffService staffService;
	@Autowired
	private IdentityService identityService;
	
	@Override
	public void saveNoticeActorVO(NoticeActorVO noticeActorVO) {
		Date now = new Date();
		NoticeActorEntity noticeActorEntity = NoticeActorEntity.builder()
				                                       .userID(noticeActorVO.getUserID())
				                                       .noticeID(noticeActorVO.getNoticeID())
				                                       .status(noticeActorVO.getStatus())
				                                       .isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
			                                           .addTime(now)
			                                           .updateTime(now)
			                                           .build();
		noticeActorDao.saveNoticeActor(noticeActorEntity);
		User usr = identityService.createUserQuery().userId(noticeActorVO.getUserID()).singleResult();
		JPushClientUtil.sendToRegistrationID(usr.getFirstName(), Constants.NOTIFICATION_MESSAGE);

	}

	@Override
	public void updateNoticeActor(Integer ntcID,String userID) {
		noticeActorDao.updateNoticeActor(ntcID,userID);
		
	}

	@Override
	public void saveNoticeListByIDs(Integer companyID,Integer departmentID,Integer noticeID) {			
		List<Object> list=baseDao.findBySql(getQuerySqlByNoticeVO1(companyID,departmentID));		
		for(Object obj:list){
				NoticeActorEntity noticeActorEntity=new NoticeActorEntity();
				Date date=new Date();
				
				String userID=(String) obj;										
				noticeActorEntity.setUserID(userID);
				noticeActorEntity.setNoticeID(noticeID);
				noticeActorEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
				noticeActorEntity.setAddTime(date);
				noticeActorEntity.setUpdateTime(date);
				noticeActorEntity.setStatus(0);
				noticeActorDao.saveNoticeActor(noticeActorEntity);	
				User usr = identityService.createUserQuery().userId(userID).singleResult();
				//JPushClientUtil.sendToRegistrationID(usr.getFirstName(), Constants.NOTIFICATION_MESSAGE);
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
				
		
	String getQuerySqlByNoticeVO1(Integer companyID,Integer departmentID){
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
	public Set<NoticeActorVO> findNtcActorVOByNtcID(Integer ntcID) {
		List<NoticeActorEntity> noticeActorEntities = noticeActorDao.findNoticeActorsByNtcID(ntcID);
		Set<NoticeActorVO> noticeActorVOs = new HashSet<>();
		for(NoticeActorEntity noticeActorEntity:noticeActorEntities){
			NoticeActorVO noticeActorVO = new NoticeActorVO();
			noticeActorVO.setUserName(staffService.getStaffByUserID(noticeActorEntity.getUserID()).getLastName());
			List<GroupDetailVO> groups = staffService.findGroupDetailsByUserID(noticeActorEntity.getUserID());
			noticeActorVO.setGroupList(Lists2.transform(groups, new SafeFunction<GroupDetailVO, String>() {
				@Override
				protected String safeApply(GroupDetailVO input) {
					return input.getCompanyName()+"-"+input.getDepartmentName()+"-"+input.getPositionName();
				}
			}));
			noticeActorVOs.add(noticeActorVO);
			}
		return noticeActorVOs;

	}

}
