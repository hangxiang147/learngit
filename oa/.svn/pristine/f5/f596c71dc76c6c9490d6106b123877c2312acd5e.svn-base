package com.zhizaolian.staff.service.impl;

import java.util.ArrayList;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.CarUseDao;
import com.zhizaolian.staff.entity.CarUseEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.CarUseService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.CopyUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CarUseVo;

public class CarUseServiceImpl implements CarUseService {
	@Autowired
	private StaffService staffService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private ProcessService processService;
	@Autowired
	private CarUseDao carUseDao;
	@Override
	public void startCarUse(CarUseVo carUseVo) {
		String enumName = BusinessTypeEnum.CAR_USE.getName();
		carUseVo.setBusinessType(enumName);
		carUseVo.setTitle(carUseVo.getRequsetUserName() + "的" + enumName);
		Map<String, Object> vars = new HashMap<>();
		String supervisor = staffService
				.querySupervisor(carUseVo.getUserID());
		String manager = staffService.queryManager(carUseVo.getUserID());
		if (StringUtils.isNotEmpty(supervisor)) {
			vars.put("car_use_contain_super", "1");
			vars.put("car_use_superior", supervisor);
		} else if (StringUtils.isNotEmpty(supervisor)) {
			vars.put("car_use_contain_super", "2");
			vars.put("car_use_manager", manager);
		} else {
			throw new RuntimeException("找不到审批人！");
		}
		vars.put("arg", carUseVo);
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(Constants.CAR_USE);
		Task task = taskService.createTaskQuery()
				.processInstanceId(processInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), carUseVo.getRequestUserID());
		taskService.complete(task.getId(), vars);
		saveCarUse(carUseVo, processInstance.getId());
	}

	private void saveCarUse(CarUseVo carUseVo, String instanceId) {
		CarUseEntity carUseEntity=(CarUseEntity)CopyUtil.toEntity(carUseVo, CarUseEntity.class);
		carUseEntity.setProcessInstanceID(instanceId);
		carUseDao.save(carUseEntity);
	}
	@Override
	public void updateCarUseStatus(String instanceId, Integer status) {
		Map<String, String> map= new HashMap<>();
		map.put("instanceId", instanceId);
		CarUseEntity carUseEntity=carUseDao.getUniqueCarUseEntity(map);
		carUseEntity.setProcessStatus(status);
		carUseDao.update(carUseEntity);
	}
	@Override
	public ListResult<CarUseVo> getCarUseByKeys(Map<String, String> params,
			int page, int limit) {
		List<CarUseEntity> carUseEntities=carUseDao.getCarUseByKeys(params, page, limit);
		if(CollectionUtils.isNotEmpty(carUseEntities)){
			List<CarUseVo> carUseVos=new ArrayList<>();
			for (CarUseEntity carUseEntity : carUseEntities) {
				CarUseVo carUseVo=(CarUseVo)CopyUtil.toVo(carUseEntity, CarUseVo.class);
				carUseVo.setRequsetUserName(staffService.getRealNameByUserId(carUseVo.getRequestUserID()));
				List<HistoricDetail> datas = historyService
						.createHistoricDetailQuery()
						.processInstanceId(carUseVo.getProcessInstanceID())
						.list();
				for (HistoricDetail historicDetail : datas) {
					HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
					if ("arg".equals(variable.getVariableName())) {
						CarUseVo arg = (CarUseVo) variable.getValue();
						carUseVo.setRequestDate(arg.getRequestDate());
						carUseVo.setTitle(arg.getTitle());
					}
				}
				ProcessInstance pInstance = runtimeService
						.createProcessInstanceQuery()
						.processInstanceId(carUseVo.getProcessInstanceID())
						.singleResult();
				if (pInstance != null) {
					carUseVo.setStatus("处理中");
					carUseVo.setAssigneeUserName(processService
							.getProcessTaskAssignee(pInstance.getId()));
				} else {
					Integer value_ = carUseVo.getProcessStatus();
					if (value_ != null) {
						TaskResultEnum t = TaskResultEnum.valueOf(value_);
						carUseVo.setStatus(t.getName());
					}
				}
				carUseVos.add(carUseVo);
			}
			return new ListResult<>(carUseVos,carUseDao.getCarUseCountByKeys(params));
		}
		return new ListResult<>(new ArrayList<CarUseVo>(), 0);
	}
}
