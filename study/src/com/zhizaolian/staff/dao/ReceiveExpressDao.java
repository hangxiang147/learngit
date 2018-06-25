package com.zhizaolian.staff.dao;



import com.zhizaolian.staff.entity.ReceiveExpressEntity;
import com.zhizaolian.staff.utils.ListResult;

public interface ReceiveExpressDao {
	void save(ReceiveExpressEntity signExpressEntity);
    ListResult<ReceiveExpressEntity> findSignExpressList(String hql, String hqlcount, int page, int limit);
    ReceiveExpressEntity getSignExpressEntityByID(Integer signExpressID);

}
