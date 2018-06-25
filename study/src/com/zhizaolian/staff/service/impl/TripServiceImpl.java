package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.TripDao;
import com.zhizaolian.staff.entity.TripEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.service.TripService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.TripVo;

public class TripServiceImpl implements TripService {
	@Autowired
	private RuntimeService runtimeService;
	@Autowired 
	private TaskService taskService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private TripDao tripDao;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private PermissionService permissionService;

	@Override
	public void StartTrip(TripVo tripVo) {
		String enumName = BusinessTypeEnum.BUSSNIESSTRIP.getName();
		tripVo.setBusinessType(enumName);
		tripVo.setTitle(tripVo.getRequestUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<String, Object>();
		List<String> hrGroupList = staffService.queryHRGroupList(tripVo.getRequestUserID());
		List<String> humanResources = permissionService.findUsersByPermissionCode(Constants.HUMAN_RESOURCES);
		List<String> humanResources_group = permissionService.findGroupsByPermissionCode(Constants.HUMAN_RESOURCES);
		humanResources.addAll(humanResources_group);
		vars.put("trip_group_human_resources", hrGroupList);
		vars.put("human_resources", humanResources);
		vars.put("isNeed_ticket", tripVo.getIsNeedTicket());
		vars.put("arg", tripVo);

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(Constants.BUSSNIESSTRIP);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), tripVo.getUserID());
		taskService.complete(task.getId(), vars);
		saveTrip(tripVo, processInstance.getId());

	}

	private void saveTrip(TripVo tripVo, String instanceId) {
		TripEntity tripEntity = null;
		try {
			tripEntity = TripEntity.class.cast(CopyUtil.DeclaredFieldCopy(TripEntity.class, tripVo, 2, null));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		Date now = new Date();
		tripEntity.setAddTime(now);
		tripEntity.setUpdateTime(now);
		tripEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
		tripEntity.setUserID(tripVo.getUserID());
		tripEntity.setProcessInstanceID(instanceId);
		tripDao.save(tripEntity);
	}

	@Override
	public ListResult<TripVo> findTripListByUserID(String userID, int page, int limit) {
		List<TripEntity> tripEntities = tripDao.findTripByUserID(userID, page, limit);
		List<TripVo> tripVOs = new ArrayList<TripVo>();
		for (TripEntity trip : tripEntities) {
			TripVo tripVo = null;
			try {
				tripVo = (TripVo) CopyUtil.DeclaredFieldCopy(TripVo.class, trip, 1, null);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery()
					.processInstanceId(tripVo.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if ("arg".equals(variable.getVariableName())) {
					TripVo arg = (TripVo) variable.getValue();
					tripVo.setRequestDate(arg.getRequestDate());
					tripVo.setTitle(arg.getTitle());
					tripVo.setRequestUserName(arg.getRequestUserName());
				}
			}
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(tripVo.getProcessInstanceID()).singleResult();
			if (pInstance != null) {
				tripVo.setStatus("处理中");
				tripVo.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				tripVo.setStatus(TaskResultEnum.valueOf(tripVo.getApplyResult()).getName());
			}
			tripVOs.add(tripVo);
		}

		int count = tripDao.countTripByUserID(userID);
		return new ListResult<TripVo>(tripVOs, count);
	}

	@Override
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		tripDao.updateProcessStatus(processInstanceID, taskResult);
	}

	@Override
	public ListResult<TripEntity> getTripByKeys(Map<String, String> map,int page,int limit) {
		List<TripEntity> trips = tripDao.getTripByKeys(map,page,limit);
		int count = tripDao.getCountOfTripByKeys(map);
		return new ListResult<TripEntity>(trips, count);
	}

	@Override
	public HSSFWorkbook exportTrips(Map<String, String> map) {
		List<TripEntity> trips=tripDao.getTripByKeys(map, 1, Integer.MAX_VALUE);
        HSSFWorkbook wb = new HSSFWorkbook();  
        HSSFSheet sheet = wb.createSheet("出差情况明细");  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格
        row.createCell((short) 0).setCellValue("发起人");
        row.createCell((short) 1).setCellValue("原因");  
        row.createCell((short) 2).setCellValue("开始时间");  
        row.createCell((short) 3).setCellValue("结束时间");  
        row.createCell((short) 4).setCellValue("是否需要购买车票");
        row.createCell((short) 5).setCellValue("车票详情");
        
        for (int i=0,j=sheet.getLastRowNum()+1,length=trips.size(); i < length; ++i,++j)  
        {  
            HSSFRow row_data = sheet.createRow(j);  
            TripEntity tripEntity = trips.get(i);
            // 第四步，创建单元格，并设置值  
            row_data.createCell((short) 0).setCellValue(tripEntity.getRequestUserName());  
            row_data.createCell((short) 1).setCellValue(tripEntity.getReason());  
            row_data.createCell((short) 2).setCellValue(tripEntity.getStartTime()==null?"":DateUtil.formateDate(tripEntity.getStartTime()));  
            row_data.createCell((short) 3).setCellValue(tripEntity.getEndTime()==null?"":DateUtil.formateDate(tripEntity.getEndTime()));  
            row_data.createCell((short) 4).setCellValue(1==tripEntity.getIsNeedTicket()?"是":"否");  
            row_data.createCell((short) 5).setCellValue(tripEntity.getTicketDetail());  
        }  
		return wb;
	}
	
	
}
