package com.zhizaolian.staff.action;


import com.zhizaolian.staff.utils.DesUtil;

import it.sauronsoftware.base64.Base64;



//import java.util.List;
//
//import org.activiti.engine.HistoryService;
//import org.activiti.engine.RepositoryService;
//import org.activiti.engine.RuntimeService;
//import org.activiti.engine.TaskService;
//import org.activiti.engine.repository.Deployment;
//import org.activiti.engine.repository.ProcessDefinition;
//import org.activiti.engine.runtime.Execution;
//import org.activiti.engine.runtime.ProcessInstance;
//import org.activiti.engine.task.Task;
//import org.springframework.beans.factory.annotation.Autowired;


public class TestAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
/*	
	
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private HistoryService historyService;
	public void startTest(){
		
		List<Deployment> list=repositoryService.createDeploymentQuery().list();
		List<ProcessDefinition> list1=repositoryService.createProcessDefinitionQuery().list();
		for (ProcessDefinition processDefinition : list1) {
			System.out.print(processDefinition.getId()+"|");
			System.out.print(processDefinition.getName()+"|");
			System.out.println(processDefinition.getKey()+"|");
		}
		ProcessInstance processInstance=runtimeService.startProcessInstanceByKey("Test1");
	//	Task task=taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
		List<Execution> ls=runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).messageEventSubscriptionName("Message").list();
//		List<ProcessDefinition> p1s = repositoryService.createProcessDefinitionQuery().messageEventSubscriptionName("testMsg").list();
//		List<ProcessDefinition> p2s = repositoryService.createProcessDefinitionQuery().messageEventSubscriptionName("testMsg_").list();
//		ProcessInstance processInstance=runtimeService.startProcessInstanceByMessage("testMsg");
//		ProcessInstance processInstance_=runtimeService.startProcessInstanceByMessage("testMsg_");
//		System.out.println(processInstance.isEnded());
		System.out.println(ls);
//		Task task=taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//		Task task_=taskService.createTaskQuery().processInstanceId(processInstance_.getId()).singleResult();
//		taskService.claim(task.getId(), "274af606-2799-473f-b979-cf309f23f2ab");
//		taskService.claim(task_.getId(), "274af606-2799-473f-b979-cf309f23f2ab");
//	
//		taskService.complete(task.getId());
//		taskService.complete(task_.getId());
//		System.out.print(processInstance.isEnded());
//		System.out.print(processInstance_.isEnded());
//		HistoricProcessInstance processInstanceHistory=historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
//		List<HistoricTaskInstance> h=historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceHistory.getId()).taskAssignee("274af606-2799-473f-b979-cf309f23f2ab").list();
//		System.out.print(h.isEmpty());
//		ProcessInstance processInstance2=runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
//		ProcessInstance processInstance3=runtimeService.createProcessInstanceQuery().processInstanceId(processInstance_.getId()).singleResult();
//		System.out.print(processInstance2.isEnded());
//		System.out.print(processInstance3.isEnded());
		//		ProcessInstance processInstance=runtimeService.startProcessInstanceByKey("Test1");
//		Map<String,Object> map= new HashMap<>();
//		map.put("testUser", "274af606-2799-473f-b979-cf309f23f2ab");
//		Task task=taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
//		runtimeService.sign
//		taskService.claim(task.getId(), "274af606-2799-473f-b979-cf309f23f2ab");
//		taskService.complete(task.getId(),map);
//		Map<String, Object> sendData=new HashMap<>();
//		sendData.put("hello","orange" );
//		sendData.put("testUser","274af606-2799-473f-b979-cf309f23f2ab" );
//		ProcessInstance processInstanceChild=runtimeService.startProcessInstanceByMessage("testMsg",sendData);
//		Task childTask1=taskService.createTaskQuery().processInstanceId(processInstanceChild.getId()).singleResult();
//		runtimeService.getVariable(processInstanceChild.getId(), "hello");
//		taskService.complete(childTask1.getId());
	}*/
	public static void main(String[] args) {
        //待加密内容
        String str = "123456";
        //密码，长度要是8的倍数
        String password = "oazhizao";

        byte[] result = DesUtil.encrypt(str.getBytes(),password);
        String p = new String(Base64.encode(result));
        //直接将如上内容解密
        try {
        		result = Base64.decode(p.getBytes());
                byte[] decryResult = DesUtil.decrypt(result, password);
                System.out.println("解密后："+new String(decryResult));
        } catch (Exception e1) {
                e1.printStackTrace();
        }
	}
}
