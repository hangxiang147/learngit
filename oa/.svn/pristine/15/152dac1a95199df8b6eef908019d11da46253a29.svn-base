package com.zhizaolian.staff.service;

import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.CardVO;

public interface CardService {

	/**
	 * 启动一个工牌申请流程
	 * @param cardVO
	 */
	void startCard(CardVO cardVO);
	
	/**
	 * 分页查询指定用户的工牌申请
	 * @param userID
	 * @param page
	 * @param limit
	 * @return
	 */
	ListResult<CardVO> findCardListByUserID(String userID, int page, int limit);
	
	/**
	 * 根据流程ID查找工牌申领记录
	 * @param processInstanceID
	 * @return
	 */
	CardVO getCardVOByProcessInstanceID(String processInstanceID);
	
	/**
	 * 更新工牌申请流程节点的处理结果
	 * @param processInstanceID
	 * @param taskResult
	 */
	void updateProcessStatus(String processInstanceID, TaskResultEnum taskResult);
}
