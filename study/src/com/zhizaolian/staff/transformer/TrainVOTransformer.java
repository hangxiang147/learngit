package com.zhizaolian.staff.transformer;

import com.zhizaolian.staff.entity.TrainEntity;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.vo.TrainVO;

public class TrainVOTransformer {
	/*
	 * 实体类向vo
	 */ 
	public static TrainVO entityToVO(TrainEntity trainEntity){
		TrainVO trainVO=new TrainVO();
		if(trainEntity.getTrainID()!=null){
			trainVO.setTrainID(trainEntity.getTrainID());
		}
		if(trainEntity.getStartTime()!=null){
			trainVO.setStartTime(trainEntity.getStartTime().toString());
		}
		if(trainEntity.getEndTime()!=null){
			trainVO.setEndTime(trainEntity.getEndTime().toString());
		}
		trainVO.setPlace(trainEntity.getPlace());
		trainVO.setLector(trainEntity.getLector());
		trainVO.setTopic(trainEntity.getTopic());
		trainVO.setContent(trainEntity.getContent());
		if(trainEntity.getPID()!=null){
		trainVO.setPID(trainEntity.getPID());
		}
		return trainVO;
	}
	
	/*
	 * vo向实体类
	 */
	public static TrainEntity voToEntity(TrainVO trainVO){
		TrainEntity trainEntity=new TrainEntity();
		if(trainVO.getTrainID()!=null){
			trainEntity.setTrainID(trainVO.getTrainID());
		}
		if(trainVO.getStartTime()!=null){
			trainEntity.setStartTime(DateUtil.getFullDate(trainVO.getStartTime()));
		}
		if(trainVO.getEndTime()!=null){
			trainEntity.setEndTime(DateUtil.getFullDate(trainVO.getEndTime()));
		}
		trainEntity.setPlace(trainVO.getPlace());
		trainEntity.setTopic(trainVO.getTopic());
		trainEntity.setContent(trainVO.getContent());
		trainEntity.setParticipantNum(trainVO.getParticipantIDs().size());
		return trainEntity;
	}
	
	
}
