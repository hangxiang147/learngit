package com.zhizaolian.staff.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.zhizaolian.staff.dao.CardDao;
import com.zhizaolian.staff.entity.CardEntity;
import com.zhizaolian.staff.enums.BusinessTypeEnum;
import com.zhizaolian.staff.enums.Constants;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.service.CardService;
import com.zhizaolian.staff.service.PermissionService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CardVO;

public class CardServiceImpl implements CardService {

	@Autowired
	private PermissionService permissionService;
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
	private CardDao cardDao;
	
	@Override
	public void startCard(CardVO cardVO) {
		if (cardVO.getAttachment() == null || cardVO.getAttachment().length <= 0 || cardVO.getAttachment().length > 3) {
			throw new RuntimeException("请务必上传附件，且个数不超过3个！");
		}
		
		cardVO.setBusinessType(BusinessTypeEnum.CARD.getName());
		cardVO.setTitle(cardVO.getRequestUserName()+"的"+BusinessTypeEnum.CARD.getName());
		//初始化任务参数
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("arg", cardVO);
		
		List<String> cardAuditUsers = permissionService.findUsersByPermissionCode(Constants.CARD_AUDIT);
		List<String> cardAuditGroups = permissionService.findGroupsByPermissionCode(Constants.CARD_AUDIT);
		List<String> makeCardUsers = permissionService.findUsersByPermissionCode(Constants.MAKE_CARD);
		List<String> makeCardGroups = permissionService.findGroupsByPermissionCode(Constants.MAKE_CARD);
		if ((!staffService.hasGroupMember(cardAuditGroups) && CollectionUtils.isEmpty(cardAuditUsers)) ||
				(!staffService.hasGroupMember(makeCardGroups) && CollectionUtils.isEmpty(makeCardUsers))) {
			throw new RuntimeException("未找到该申请的审批人！");
		}

		vars.put("makeCardUsers", makeCardUsers);
		vars.put("makeCardGroups", makeCardGroups);
		vars.put("cardAuditUsers", cardAuditUsers);
		vars.put("cardAuditGroups", cardAuditGroups);
		ProcessInstance pInstance = runtimeService.startProcessInstanceByKey(Constants.CARD);
		Task task = taskService.createTaskQuery().processInstanceId(pInstance.getId()).singleResult();
		taskService.setAssignee(task.getId(), cardVO.getUserID());
		taskService.complete(task.getId(), vars);
		saveCard(cardVO, pInstance.getId());
	}
	
	@Override
	public ListResult<CardVO> findCardListByUserID(String userID, int page, int limit) {
		//查询OA_IDCard表的数据
		List<CardEntity> cardEntities = cardDao.findCardsByUserID(userID, page, limit);
		List<CardVO> cardVOs = new ArrayList<CardVO>();
		for (CardEntity card : cardEntities) {
			CardVO cardVO = new CardVO();
			cardVO.setProcessInstanceID(card.getProcessInstanceID());
			String[] attachmentNames = card.getAttachmentNames().split("\\$");
			cardVO.setAttachmentFileName(attachmentNames);
			cardVO.setReason(card.getReason());
			
			List<HistoricDetail> datas = historyService.createHistoricDetailQuery()
					.processInstanceId(card.getProcessInstanceID()).list();
			for (HistoricDetail historicDetail : datas) {
				HistoricVariableUpdate variable = (HistoricVariableUpdate) historicDetail;
				if (variable.getVariableName().equals("arg")) {
					CardVO arg = (CardVO) variable.getValue();
					cardVO.setRequestDate(arg.getRequestDate());
					cardVO.setTitle(arg.getTitle());
					cardVO.setRequestUserName(arg.getRequestUserName());
				}
			}
			
			//查询流程实例
			ProcessInstance pInstance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(card.getProcessInstanceID()).singleResult();
			if (pInstance != null) {
				cardVO.setStatus("处理中");
				cardVO.setAssigneeUserName(processService.getProcessTaskAssignee(pInstance.getId()));
			} else {
				cardVO.setStatus(TaskResultEnum.valueOf(card.getProcessStatus()).getName());
			}
			cardVOs.add(cardVO);
		}
				
		int count = cardDao.countCardsByUserID(userID);
		return new ListResult<CardVO>(cardVOs, count);
	}
	
	@Override
	public CardVO getCardVOByProcessInstanceID(String processInstanceID) {
		CardEntity cardEntity = cardDao.getCardByProcessInstanceID(processInstanceID);
		CardVO cardVO = new CardVO();
		cardVO.setUserID(cardEntity.getUserID());
		cardVO.setRequestUserID(cardEntity.getRequestUserID());
		String[] attachmentNames = cardEntity.getAttachmentNames().split("\\$");
		cardVO.setAttachmentFileName(attachmentNames);
		cardVO.setReason(cardEntity.getReason());
		return cardVO;
	}
	
	@Override
	public void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult) {
		if (taskResult == null) {
			throw new RuntimeException("处理结果不合法！");
		}
		
		cardDao.updateProcessStatusByProcessInstanceID(processInstanceID, taskResult.getValue());
	}
	
	private void saveCard(CardVO cardVO, String processInstanceID) {
		Date now = new Date();
		InputStream in = null;
		OutputStream out = null;
		StringBuffer attachmentNames = new StringBuffer();
		try {
			File[] attachments = cardVO.getAttachment();
			String[] attachmentFileNames = cardVO.getAttachmentFileName();
			int size = attachments.length;
			for (int i=0; i<size&&i<3; ++i) {
				byte[] buffer = new byte[10*1024*1024];
				int length = 0;
				String fileName = now.getTime()+"_"+attachmentFileNames[i];
				in = new FileInputStream(attachments[i]);
				File file = new File(Constants.CARD_FILE_DIRECTORY);
				if (!file.exists()) {
					file.mkdirs();
				}
				out = new FileOutputStream(new File(Constants.CARD_FILE_DIRECTORY, fileName));
				
				while ((length=in.read(buffer, 0, buffer.length)) != -1) {
					out.write(buffer, 00, length);
				}
				if (i != 0) {
					attachmentNames.append("$");
				}
				attachmentNames.append(fileName);
			} 
			
		} catch (Exception e) {
			throw new RuntimeException("保存附件至服务器失败！");
		} finally {
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
				}
			if(out!=null)
				try {
					out.close();
				} catch (IOException e) {
				}
		}
		
		CardEntity cardEntity = CardEntity.builder()
				 					.userID(cardVO.getUserID())
				 					.requestUserID(cardVO.getRequestUserID())
				 					.attachmentNames(attachmentNames.toString())
				 					.reason(cardVO.getReason())
				 					.processInstanceID(processInstanceID)
				 					.isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
									.addTime(now)
									.updateTime(now)
									.build();
		cardDao.save(cardEntity);
		
	}
}
