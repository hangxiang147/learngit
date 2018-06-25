package com.zhizaolian.staff.service;

import java.util.List;

public interface EnTrustService {
	List<Object> findTaskIdsByUserGroupIDs(String taskName, List<String> groups, List<String> users);
	List<Object> findAssigneeTaskIdsByUserIdAndTaskName(String taskName,String assigneeId);
	Object getDetailByRightID(String rightId);
	void transferAssigneer(String receiverUserId, String resignationUserID);
	void addRight(String receiverUserId, int parseInt);
	void transferTasks(String receiverUserId, String resignationUserID);
}
