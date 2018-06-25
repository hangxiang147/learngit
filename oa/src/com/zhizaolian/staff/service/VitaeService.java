package com.zhizaolian.staff.service;

import java.io.File;
import java.util.List;

import org.activiti.engine.identity.Group;

import com.zhizaolian.staff.entity.JobEntity;
import com.zhizaolian.staff.entity.VitaeEntity;
import com.zhizaolian.staff.entity.VitaeResultEntity;
import com.zhizaolian.staff.entity.VitaeSignEduEntity;
import com.zhizaolian.staff.entity.VitaeSignEntity;
import com.zhizaolian.staff.entity.VitaeSignFamilyEntity;
import com.zhizaolian.staff.entity.VitaeSignJobHistoryEntity;
import com.zhizaolian.staff.enums.TaskDefKeyEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.VitaeTaskVo;
import com.zhizaolian.staff.vo.VitaeVo;

public interface VitaeService {
	
	ListResult<JobEntity> getJobEntityList(String name,int page,int limit);
	ListResult<JobEntity> getJobEntityListMore(String name,int page,int limit);
	JobEntity getJobEntityById(String id);
	void addJob(JobEntity jobEntity);
	void updateJob(JobEntity jobEntity);
	void deleteJob(String id);
	
	VitaeEntity getVitaeEntityById(String id);
	VitaeEntity getVitaeEntityByIntstanceId(String id);
	void startVitae(VitaeVo vitaeVo,File[] files,String fileDetail);
	ListResult<VitaeVo> getVitaeByKeys(String requestUserId,int page,int limit);
	void setVitaeResultByInstanceId(String instanceId,Integer result);
    ListResult<VitaeTaskVo> findVitaeVoByUserGroupIDs(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page,int limit);
    ListResult<VitaeTaskVo> findVitaeVoByUserGroupIDsByStep(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page,int limit,int step);
    ListResult<VitaeTaskVo> findVitaeVoByUserGroupIDsByStepForNew(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users, int page,int limit,int step);
    int  findVitaeVoByUserGroupIDsCount(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,String intsanceId);
    void completeVitae(String instanceId,String name,String postKey,String ids,String names,String tids,String tnames);
    void updateVitaeStatusByInstanceId(String instanceId,Integer result);
    
    VitaeSignEntity VitaeSignEntity(String  id);
    List<VitaeSignEntity> VitaeSignEntityByVitaeId(String  id);
    List<VitaeResultEntity> getVitaeResultListBySignId(String signId);
    List<VitaeSignEduEntity> getVitaeSignEduEntityByVitaeId(String id);
    List<VitaeSignFamilyEntity> getVitaeSignFamilyEntityByVitaeId(String id);
    List<VitaeSignJobHistoryEntity> getVitaeSignJobHistoryEntityByVitaeId(String id);
    ListResult<VitaeSignEntity> getPagedVitaeByName(String name,int page,int limit);
    
    
    int commonSave(Object obj);
    void commonUpdate(Object obj);
    void clearSignLinkTable(String id);
    int  getLastBh();
    
    int getEffectiveTaskId(List<TaskDefKeyEnum> tasks, List<Group> groups, List<String> users,int stepIndex);
    void outOfTime(String resultId,String userId);
    VitaeResultEntity VitaeResultEntityByTaskId(String taskId);
    ListResult<Object> getVitaeResultEntityByName(String name,String userId,int page,int limit);
	List<String> findEntryingStaffs();
    
}
