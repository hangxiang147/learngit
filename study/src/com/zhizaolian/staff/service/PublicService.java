package com.zhizaolian.staff.service;


import java.util.List;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.PublicRelationEntity;
import com.zhizaolian.staff.entity.PublicRelationEventEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.PublicRelationEventVo;

public interface PublicService {

	ListResult<PublicRelationEntity> findPublicRelations(Integer page, Integer limit, String category, String person);

	void savePublicRelation(PublicRelationEntity publicRelation);

	PublicRelationEntity getPublicRelation(String publicRelationId);

	void startApplyPublicEvent(PublicRelationEventVo publicRelationEvent);

	ListResult<PublicRelationEventVo> findPublicEventListByUserID(String id, Integer page, Integer limit);

	List<PublicRelationEventVo> getPublicEventTasksByInstanceId(List<Task> publicEventTasks) throws Exception;

	void updatePublicEventStatus(TaskResultEnum taskResult, String processInstanceId);

	PublicRelationEventEntity getPublicEventTaskByInstanceId(String processInstanceId);

	List<PublicRelationEntity> findPublicRelations();

	void updatePublicRelationId(String publicRelationId, String processInstanceId);

	void closePublicRelation(String publicRelationId);

	List<PublicRelationEventVo> showHistoricalPublicEventsByRelationId(String publicRelationId);

}
