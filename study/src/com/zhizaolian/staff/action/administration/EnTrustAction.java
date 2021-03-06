package com.zhizaolian.staff.action.administration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import java.util.Map.Entry;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.PermissionEntity;
import com.zhizaolian.staff.service.EnTrustService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.RightService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.DepartmentVO;

import lombok.Getter;
import lombok.Setter;

/**
 *@author Zhouk
 *@date 2017年5月8日 下午1:14:10
 *@describtion 
 * 针对于 流程 中可能出现的节点变更 我需要对
 **/ 
public class EnTrustAction extends BaseAction{


	/*
	 * 1:首先是两个列表页
	 * 1.1:所有有关组的 权限
	 * 1.2:所有有关人员的权限
	 * 注: 由于oa系统现有的功能区分以及现有list页面 查询限制
	 * 	   必须将个人受理 和候选受理 严格区分,导致这里 只能将个人权限转换给个人 组权限转换给另外一个组
	 * 2:任务的转移:
	 * 2.1:个人权限 
	 * 2.1.1:上一步未有完成的
	 * 替换流程数据
	 * 2.1.2:上步已经完成的
	 * 2.2:候选权限转移
	 * 2.2.1:上一步未有完成的
	 * 2.2.1.1:改变一组候选人中的某一个人	
	 * 2.2.1.1:改变一组候选组中的某一个组
	 * 2.2.1:上一步未有完成的
	 * 2.2.1.1:改变一组候选人中的某一个人
	 * 2.2.1.1:改变一组候选组中的某一个组			
	 * 注:
	 * 	1:一开始 我是选择一组人或者一个人 开始权限转移,
	 *  在转移时 对于未有完成的步骤  我改变时候 需要知道 系统中定义 标识名称
	 *  我初始化个map 将数据 全都存进去 key值是 流程名称+">"+"任务名称"+">"+"类型"
	 *  类型1:单人受理  类型2:候选user组受理 3:候选group组受理
	 *  得到了 标识名称 我就可以 在 [[上一步未有完成的]]的情况中找到值 替换 或者替换一部分
	 *  2.对于候选权限转移 中的上步已经完成的
	 *  	分别查询 user和group 重新添加受理组和人 删除原有 组和人
	 *  3.页面上  
	 *  	3.1我是先根据一个人查询
	 *  	我可以查询到 这个人 单人受理节点有哪些 这个人 候选组中包括他的一群人员受理的节点有哪些
	 *  	3.2我根据companyId-departmentId-positionId到groupId 根据groupId 进行相应操作
	 *  4.有一部分的 权限 是没发转移的 列如  主管 分公司负责人  每个公司的 hr  
	 *  	我不能从 权限表 明确地知道 这些人的权限
	 * */

	private static final long serialVersionUID = 1L;

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private EnTrustService enTrustService;
	@Autowired
	private RightService rightService;
	@Autowired
	private PositionService positionService;
	/*
	 * 6种情况下的不同处理方法
	 * */

	/**
	 * 单个人上步未完成
	 */
	private void changeSingleUser(String processDefKey,String permissionName,String oldPersonId,String newPersonId){
		List<Task> vacationTasks = taskService.createTaskQuery().processDefinitionKey(processDefKey).list();
		//无论走到哪步 我都重新设置值 ^.^
		if(CollectionUtils.isNotEmpty(vacationTasks)){
			for (Task task : vacationTasks) {
				String processInstanceId=task.getProcessInstanceId();
				Object object=runtimeService.getVariable(processInstanceId,permissionName);
				if(object instanceof String){
					if(oldPersonId.equals(object.toString())){
						runtimeService.setVariable(processInstanceId, permissionName, newPersonId);						
					}
				}
			}
		}
	}
	/**
	 * 单个人走到这一步
	 */
	private void changeSingleUserReceived(String taskId,String newPersonId){
		taskService.delegateTask(taskId, newPersonId);
	}
	private void changeCandidateUser(String processDefKey,String permissionName,String oldPersonId,String newPersonId){
		List<Task> vacationTasks = taskService.createTaskQuery().processDefinitionKey(processDefKey).list();
		if(CollectionUtils.isNotEmpty(vacationTasks)){
			for (Task task : vacationTasks) {
				String processInstanceId=task.getProcessInstanceId();
				Object object=runtimeService.getVariable(processInstanceId,permissionName);
				if(object instanceof List){
					@SuppressWarnings("unchecked")
					List<Object> item=(List<Object>)object;
					if(CollectionUtils.isNotEmpty(item)){
						for (Object object2 : item) {
							if(object2==null)
								continue;
							String key =object2.toString();
							if(key.equals(oldPersonId)){
								item.remove(object2);
								item.add(newPersonId);
								runtimeService.setVariable(processInstanceId, permissionName, item);
								break;
							}
						}
					}
				}
				//可能有特殊情况 直接塞了一个
				else if(object instanceof String){
					if(oldPersonId.equals(object.toString())){
						runtimeService.setVariable(processInstanceId, permissionName, newPersonId);						
					}
				}
			}
		}
	}
	private void changeCandidateUserReceived(String taskId,String oldGroupId,String newGroupId){
		taskService.addCandidateUser(taskId, newGroupId);
		taskService.deleteCandidateUser(taskId, oldGroupId);
	}
	private void changeCandidateGroup(String processDefKey,String permissionName,String oldGroupId,String newGroupId){
		changeCandidateUser(processDefKey, permissionName, oldGroupId, newGroupId);
	}
	private void changeCandidateGroupReceived(String taskId,String oldGroupId,String newGroupId){
		taskService.addCandidateGroup(taskId, newGroupId);
		taskService.deleteCandidateGroup(taskId, oldGroupId);
	}


	/**
	 * 主要调用如下 两个接口
	 */


	/**
	 * 用户权限转换
	 */
	@Getter
	@Setter
	private String oldId;
	@Getter
	@Setter
	private String newId;
	@Getter
	@Setter
	private String rightId;
	public void userPermissionChange(){
		Map<String, String> returnMap= new HashMap<String, String>();
		Object[] keys=(Object[]) enTrustService.getDetailByRightID(rightId);
		if(keys==null){
			returnMap.put("success", "false");
			returnMap.put("msg", "该节点未有在库中定义转换,请联系管理员");
			printByJson(returnMap);
			return;
		}
		String processKey=""+keys[2];
		String nodeKey=""+keys[3];
		String type=""+keys[4];
		String mapKey=""+keys[5];
		if("1".equals(type)){
			changeSingleUser(processKey,mapKey , oldId, newId);
			List<Object> taskIdList=enTrustService.findAssigneeTaskIdsByUserIdAndTaskName(nodeKey, oldId);
			for (Object object : taskIdList) {
				changeSingleUserReceived(object.toString(), newId);
			}
		}else{
			changeCandidateUser(processKey, mapKey , oldId, newId);
			List<Object> taskIdList=enTrustService.findTaskIdsByUserGroupIDs(nodeKey, Arrays.asList(""), Arrays.asList(oldId));
			for (Object object : taskIdList) {
				changeCandidateUserReceived(object.toString(),oldId, newId);
			}
		}
		returnMap.put("success", "true");
		printByJson(returnMap);
	}

	public void groupPermissionChange(){
		Map<String, String> returnMap= new HashMap<String, String>();
		Object[] keys=(Object[]) enTrustService.getDetailByRightID(rightId);
		if(keys==null){
			returnMap.put("success", "false");
			returnMap.put("msg", "该节点未有在库中定义转换,请联系管理员");
			printByJson(returnMap);
			return;
		}
		String processKey=""+keys[2];
		String nodeKey=""+keys[3];
		String mapKey=""+keys[5];
		changeCandidateUser(processKey, mapKey, oldId, newId);
		List<Object> taskIdList=enTrustService.findTaskIdsByUserGroupIDs(nodeKey,Arrays.asList(oldId),Arrays.asList(""));
		changeCandidateGroup(processKey,mapKey, oldId, newId);
		for (Object object : taskIdList) {
			changeCandidateGroupReceived(object.toString(),oldId, newId);
		}
		returnMap.put("success", "true");
		printByJson(returnMap);
	}

	public String listRightPersonal(){
		String keyId=request.getParameter("keyId");
		request.setAttribute("keyId", keyId);
		request.setAttribute("userName", request.getParameter("userName"));
		if(StringUtils.isBlank(keyId)){
			return "listRightPersonal";
		}
		ListResult<Object[]> resultList=rightService.getRightMemberShip(keyId,1,Integer.MAX_VALUE);
		request.setAttribute("list", resultList.getList());
		return "listRightPersonal";
	}
	public String listRightGroup(){
		String groupId=request.getParameter("oldGroupId");
		request.setAttribute("oldGroupId",request.getParameter("oldGroupId"));
		Integer companyID = StringUtils.isBlank(request.getParameter("companyID")) ? null : Integer.valueOf(request.getParameter("companyID"));
		Integer departmentID = StringUtils.isBlank(request.getParameter("departmentID")) ? null : Integer.valueOf(request.getParameter("departmentID"));
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		if (companyID != null) {
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(companyID);
			if (departmentVOs != null && departmentID != null) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int selectedDepartmentID = departmentID;
				while (selectedDepartmentID != 0) {
					selectedDepartmentIDs.add(0, selectedDepartmentID);
					for (DepartmentVO departmentVO : departmentVOs) {
						if (departmentVO.getDepartmentID() == selectedDepartmentID) {
							selectedDepartmentID = departmentVO.getParentID();
						}
					}
				}
				request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
			}
			request.setAttribute("departmentVOs", departmentVOs);
		}
		if(StringUtils.isBlank(groupId)){
			return "listRightGroup";
		}
		ListResult<Object[]> resultList=rightService.getGroupRightMemberShip(groupId,1,Integer.MAX_VALUE);
		request.setAttribute("list", resultList.getList());
		return "listRightGroup";
	}

	public String chooseItemGroup(){
		Integer companyID = StringUtils.isBlank(request.getParameter("companyID")) ? null : Integer.valueOf(request.getParameter("companyID"));
		Integer departmentID = StringUtils.isBlank(request.getParameter("departmentID")) ? null : Integer.valueOf(request.getParameter("departmentID"));
		List<CompanyVO> companyVOs = positionService.findAllCompanys();
		request.setAttribute("companyVOs", companyVOs);
		if (companyID != null) {
			List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyID(companyID);
			if (departmentVOs != null && departmentID != null) {
				List<Integer> selectedDepartmentIDs = new ArrayList<Integer>();
				int selectedDepartmentID = departmentID;
				while (selectedDepartmentID != 0) {
					selectedDepartmentIDs.add(0, selectedDepartmentID);
					for (DepartmentVO departmentVO : departmentVOs) {
						if (departmentVO.getDepartmentID() == selectedDepartmentID) {
							selectedDepartmentID = departmentVO.getParentID();
						}
					}
				}
				request.setAttribute("selectedDepartmentIDs", selectedDepartmentIDs);
			}
			request.setAttribute("departmentVOs", departmentVOs);
		}
		return "chooseItemGroup";
	}
	@Autowired
	private PermissionService permissionService;

	/*	public void transferRight(){
		try {
			//交接人
			String receiverUserId = request.getParameter("receiverUserId");
			//离职人
			String resignationUserID = request.getParameter("resignationUserID");

			//获取离职人员的权限列表
			Map<String, String> permissionIdAndNameMap = permissionService.findUserPermissionsByUserID(resignationUserID);
			Map<String, String> returnMap = new HashMap<String, String>();
			Iterator<Entry<String, String>> ite = permissionIdAndNameMap.entrySet().iterator();
			//未在库中配置的权限
			String rightNames = "";
			while(ite.hasNext()){
				Entry<String, String> entry = ite.next();
				String rightId = entry.getKey();
				String rightName = entry.getValue();
				Object[] keys=(Object[]) enTrustService.getDetailByRightID(rightId+"");
				if(keys==null){
					if(rightNames.isEmpty()){
						rightNames += rightName;
					}else{
						rightNames += "、"+rightName;
					}
					continue;
				}
				String processKey=""+keys[2];
				String nodeKey=""+keys[3];
				String type=""+keys[4];
				String mapKey=""+keys[5];
				if("1".equals(type)){
					changeSingleUser(processKey,mapKey , resignationUserID, receiverUserId);
					List<Object> taskIdList=enTrustService.findAssigneeTaskIdsByUserIdAndTaskName(nodeKey, resignationUserID);
					for (Object object : taskIdList) {
						changeSingleUserReceived(object.toString(), receiverUserId);
					}
				}else{
					changeCandidateUser(processKey, mapKey , resignationUserID, receiverUserId);
					List<Object> taskIdList=enTrustService.findTaskIdsByUserGroupIDs(nodeKey, Arrays.asList(""), Arrays.asList(resignationUserID));
					for (Object object : taskIdList) {
						changeCandidateUserReceived(object.toString(),resignationUserID, receiverUserId);
					}
				}
				enTrustService.addRight(receiverUserId, Integer.parseInt(rightId));
			}
			if(!rightNames.isEmpty()){
				returnMap.put("success", "false");
				returnMap.put("msg", "权限转移失败，"+rightNames+"，没有在库中定义转换,请联系管理员");
				printByJson(returnMap);
				return;
			}
			//职务上的任务转移（不需要设置权限）
			enTrustService.transferAssigneer(receiverUserId, resignationUserID);
			returnMap.put("success", "true");
			printByJson(returnMap);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}*/
	public void transferRight(){
		try {
			//交接人
			String receiverUserId = request.getParameter("receiverUserId");
			//离职人
			String resignationUserID = request.getParameter("resignationUserID");
			//获取离职人员的权限列表
			List<PermissionEntity> permissions = permissionService.findPermissionListByUserID(resignationUserID);
			for(PermissionEntity permission: permissions){
				String processKeyStr = permission.getProcessKeys();
				if(StringUtils.isBlank(processKeyStr)){
					continue;
				}
				String nodeKeyStr = permission.getNodeKey();
				int type = permission.getType();
				String mapKeyStr = permission.getMapKey();
				String[] processKeys = processKeyStr.split(",");
				for(String processKey: processKeys){
					String[] mapKeys = mapKeyStr.split(",");
					String[] nodeKeys = nodeKeyStr.split(",");
					if(type == 1){
						for(String mapKey: mapKeys){
							changeSingleUser(processKey,mapKey , resignationUserID, receiverUserId);
						}
						for(String nodeKey: nodeKeys){
							List<Object> taskIdList=enTrustService.findAssigneeTaskIdsByUserIdAndTaskName(nodeKey, resignationUserID);
							for (Object object : taskIdList) {
								changeSingleUserReceived(object.toString(), receiverUserId);
							}
						}
					}else{
						for(String mapKey: mapKeys){
							changeCandidateUser(processKey, mapKey , resignationUserID, receiverUserId);
						}
						for(String nodeKey: nodeKeys){
							List<Object> taskIdList=enTrustService.findTaskIdsByUserGroupIDs(nodeKey, Arrays.asList(""), Arrays.asList(resignationUserID));
							for (Object object : taskIdList) {
								changeCandidateUserReceived(object.toString(),resignationUserID, receiverUserId);
							}
						}
					}
				}
				enTrustService.addRight(receiverUserId, permission.getPermissionID());
			}
			Map<String, String> returnMap = new HashMap<String, String>();
			//职务上的任务转移（不需要设置权限）
			enTrustService.transferAssigneer(receiverUserId, resignationUserID);
			//转移待办任务
			enTrustService.transferTasks(receiverUserId, resignationUserID);
			returnMap.put("success", "true");
			printByJson(returnMap);
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
	}
}
