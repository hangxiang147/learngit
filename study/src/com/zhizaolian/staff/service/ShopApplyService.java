package com.zhizaolian.staff.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.zhizaolian.staff.entity.ShopOtherPayEntity;
import com.zhizaolian.staff.entity.ShopPayPluginEntity;
import com.zhizaolian.staff.entity.SpreadShopApplyEntity;
import com.zhizaolian.staff.entity.SpreadShopEntity;
import com.zhizaolian.staff.enums.TaskResultEnum;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ShopApplyTaskVo;
import com.zhizaolian.staff.vo.ShopApplyVo;
import com.zhizaolian.staff.vo.ShopPayApplyListVo;
import com.zhizaolian.staff.vo.ShopPayApplyVo;
import com.zhizaolian.staff.vo.ShopPayVo;
import com.zhizaolian.staff.vo.SpreadShopVo;

public interface ShopApplyService {

	void startShopApply(ShopApplyVo shopApplyVo, File[] attachment, String[] attachmentFileName) throws Exception;

	List<ShopApplyTaskVo> getShopApplyTaskVOList(List<Task> shopApplyTasks);

	ShopApplyVo getShopApplyVo(String taskID);

	String getShopApplyUserIdByInstanceId(String id);

	void updateShopApplyProcessStatus(TaskResultEnum result, String processInstanceID);

	ListResult<ShopApplyVo> findShopApplyListByUserID(String id, Integer page, Integer limit);

	ShopApplyVo getShopApplyVoByProcessInstanceId(String processInstanceID);

	List<ShopApplyTaskVo> getShopPayApplyTaskVOList(List<Task> shopPayApplyTasks);

	String getShopPayApplyUserIdByInstanceId(String id);

	ShopPayApplyVo getShopPayApplyVo(String taskID);

	SpreadShopEntity getSpreadShop(String spreadId);

	void updateShopPayApplyProcessStatus(TaskResultEnum result, String processInstanceID);

	List<ShopPayApplyVo> findShopPayApplyListByUserID(String id);

	void updateShopPayApplyExpiredTime(String expiredTime, String processInstanceID);

	ShopPayApplyVo getShopPayApplyVoByProcessInstanceId(String processInstanceID);

	List<SpreadShopEntity> getSpreadShops(String id);

	void startShopSpreadPayApply(SpreadShopApplyEntity spreadShopApply, SpreadShopVo spreadShopVo);

	void startShopPayPlugInApply(ShopPayPluginEntity shopPayPlugin);

	void startShopOtherPayApply(ShopOtherPayEntity otherPay);

	List<ShopPayVo> getShopPayList(String id);

	String getSpreadId(String id);

	ShopPayPluginEntity getShopPayPlugin(String id);

	ShopOtherPayEntity getShopOtherPay(String id);

	void startShopPayApply(String[] idAndApplyTypes, String auditResult, String userId, String beginDate, String endDate) throws Exception;

	List<ShopPayApplyListVo> getShopPayApplyListVos(ShopPayApplyVo shopPayApplyVo, Map<String, String> resultMap);

	List<SpreadShopApplyEntity> getSpreadShopApplyList(String id);

	List<ShopPayPluginEntity> getShopPayPluginList(String id);

	List<ShopOtherPayEntity> getShopOtherPayList(String id);

	<T> List<ShopPayApplyVo> changeToShopPayApplyVo(List<T> objList, String paySpread);

	void updateShopApplyInfo(String shopType, String aliPayAccount, String aliPayPhone, String publicBankAccount, String pInstanceId);

	ListResult<Object> getShopPayApplyVosByAssigner(String id, String applyerId, String beginDate, String endDate,
			Integer page, Integer limit);
}
