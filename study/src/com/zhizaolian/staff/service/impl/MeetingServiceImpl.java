package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.MeetingDao;
import com.zhizaolian.staff.entity.MeetingEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.MeetingService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.DateUtil;import com.zhizaolian.staff.vo.MeetingVO;


public class MeetingServiceImpl implements MeetingService {
	@Autowired
	private MeetingDao meetingDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffService staffService;
	
	@Override
	public Integer saveMeeting(MeetingVO meetingVO) {
		MeetingEntity meetingEntity=new MeetingEntity();
		Date now=new Date();
		meetingEntity.setMeetingID(meetingVO.getMeetingID());
		meetingEntity.setSponsorID(meetingVO.getSponsorID());
		meetingEntity.setBeginTime(DateUtil.getFullDate(meetingVO.getBeginTime()));
		meetingEntity.setEndTime(DateUtil.getFullDate(meetingVO.getEndTime()));
		meetingEntity.setPlace(meetingVO.getPlace());
		meetingEntity.setMeetingType(meetingVO.getMeetingType());
		meetingEntity.setTheme(meetingVO.getTheme());
		meetingEntity.setContent(meetingVO.getContent());
		meetingEntity.setPpCompanyIDs(meetingVO.getPpCompanys());
		meetingEntity.setPpDepartmentIDs(meetingVO.getPpDepartments());
		meetingEntity.setCcCompanyIDs(meetingVO.getCcCompanys());
		meetingEntity.setCcDepartmentIDs(meetingVO.getCcDepartments());
		
		meetingEntity.setUploadNames(meetingVO.getUploadNames());
		meetingEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		meetingEntity.setAddTime(now);
		meetingEntity.setUpdateTime(now);
		return meetingDao.saveMetting(meetingEntity);
	}
	
	@Override
	public ListResult<MeetingVO> findMeetingList(int page, int limit,String userID) {
		String sql="select distinct a.*,GROUP_CONCAT(minutes.Content) from( select meeting.MeetingID,meeting.BeginTime,meeting.EndTime,meeting.Place,meeting.Theme  from "
				+ "OA_Meeting meeting,OA_MeetingActor actor where meeting.MeetingID = actor.MeetingID  "
				+ "and meeting.IsDeleted = 0 and actor.IsDeleted = 0 and (actor.UserID = '"+userID+"' or meeting.SponsorID = '"+userID+"') "
						+ " )a left join  OA_MeetingMinutes minutes on a.MeetingID = minutes.MeetingID  group by a.MeetingID order by a.MeetingID desc";
		List<Object> list=baseDao.findPageList(sql, page, limit);
		List<MeetingVO> meetingVOs=new ArrayList<>();
		for(Object obj:list){
			MeetingVO meetingVO2=new MeetingVO();
			Object[] objs=(Object[])obj;
			meetingVO2.setMeetingID((Integer)objs[0]);
			meetingVO2.setBeginTime(DateUtil.formateFullDate((Date)objs[1]));
			meetingVO2.setEndTime(DateUtil.formateFullDate((Date)objs[2]));
			meetingVO2.setPlace((String)objs[3]);
			meetingVO2.setTheme((String)objs[4]);
			meetingVO2.setContentMins((String)objs[5]);
			meetingVOs.add(meetingVO2);
		}
		String sqlCount="select count(*)from( select distinct * from( select meeting.MeetingID,meeting.BeginTime,meeting.EndTime,meeting.Place,meeting.Theme from "
				+ "OA_Meeting meeting,OA_MeetingActor actor where meeting.MeetingID = actor.MeetingID "
				+ "and meeting.IsDeleted = 0 and actor.IsDeleted = 0 and (actor.UserID = '"+userID+"' or meeting.SponsorID = '"+userID+"') "
						+ "order by meeting.AddTime)a )b ";
		Object countObj = baseDao.getUniqueResult(sqlCount);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue(); 
		return new ListResult<>(meetingVOs,count);
	}
		
	
	
	public MeetingVO getMeetingByMeetingID(Integer meetingID){
		MeetingEntity meetingEntity=meetingDao.getMeetingByMeetingID(meetingID);
		MeetingVO meetingVO=new MeetingVO();
		meetingVO.setCcCompanys(meetingEntity.getCcCompanyIDs());
		meetingVO.setCcDepartments(meetingEntity.getCcDepartmentIDs());
		meetingVO.setPpCompanys(meetingEntity.getPpCompanyIDs());
		meetingVO.setPpDepartments(meetingEntity.getPpDepartmentIDs());
		meetingVO.setContent(meetingEntity.getContent());
		meetingVO.setTheme(meetingEntity.getTheme());
		meetingVO.setMeetingID(meetingEntity.getMeetingID());
		meetingVO.setBeginTime(DateUtil.formateFullDate(meetingEntity.getBeginTime()));
		meetingVO.setEndTime(DateUtil.formateFullDate(meetingEntity.getEndTime()));
		meetingVO.setSponsorName(staffService.getStaffByUserID(meetingEntity.getSponsorID()).getLastName());
		
		meetingVO.setPlace(meetingEntity.getPlace());
	
		meetingVO.setSponsorID(meetingEntity.getSponsorID());
		meetingVO.setMeetingType(meetingEntity.getMeetingType());
		meetingVO.setUploadNames(meetingEntity.getUploadNames());
		return meetingVO;
		
	}

}
