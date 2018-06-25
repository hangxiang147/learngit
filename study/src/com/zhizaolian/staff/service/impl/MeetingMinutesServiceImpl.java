package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.MeetingMinutesDao;
import com.zhizaolian.staff.entity.MeetingMinutesEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.MeetingMinutesService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.vo.MeetingMinutesVO;


public class MeetingMinutesServiceImpl implements MeetingMinutesService {
	@Autowired
	private MeetingMinutesDao meetingMinutesDao;
	@Autowired
	private StaffService staffService;
	
	@Override
	public void saveMMVO(MeetingMinutesVO meetingMinutesVO) {
		for(int i=0;i<meetingMinutesVO.getContents().length;i++){
		Date now = new Date();
		MeetingMinutesEntity meetingMinutesEntity = MeetingMinutesEntity.builder()
																.mMID(meetingMinutesVO.getMMID())
				                                                .meetingID(meetingMinutesVO.getMeetingID())
				                                                .content(meetingMinutesVO.getContents()[i])
				                                                .ownerID(meetingMinutesVO.getOwnerIDs()[i])
				                                                .attachementNames(meetingMinutesVO.getAttachementNames())
				                                                .isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
															    .addTime(now)
															    .updateTime(now)
															    .build();
		 meetingMinutesDao.save(meetingMinutesEntity);
		}

	}

	@Override
	public List<MeetingMinutesVO> getMeetingMinutesByMeetingID(Integer meetingID){
		List<MeetingMinutesEntity> meetingMinutesEntities=meetingMinutesDao.getMeetingMinutesByMeetingID(meetingID);
		List<MeetingMinutesVO> meetingMinutesVOs = new ArrayList<>();
        for(MeetingMinutesEntity meetingMinutesEntity:meetingMinutesEntities){
        	MeetingMinutesVO meetingMinutesVO = new MeetingMinutesVO();
        	meetingMinutesVO.setContent(meetingMinutesEntity.getContent());
        	meetingMinutesVO.setOwnerName(staffService.getStaffByUserID(meetingMinutesEntity.getOwnerID()).getLastName());
        	meetingMinutesVOs.add(meetingMinutesVO);
        }
        return meetingMinutesVOs;
		
	}
	

}
