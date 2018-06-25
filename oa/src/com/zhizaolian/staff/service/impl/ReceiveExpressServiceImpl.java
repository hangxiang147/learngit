package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.ReceiveExpressDao;
import com.zhizaolian.staff.entity.ReceiveExpressEntity;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.ReceiveExpressService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.SignExpressVO;


public class ReceiveExpressServiceImpl implements ReceiveExpressService {
	@Autowired
	private ReceiveExpressDao signExpressDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffService staffService;
	 


	@Override
	public void saveSignExpress(SignExpressVO signExpressVO) {
		Date now = new Date();
		ReceiveExpressEntity signExpressEntity = ReceiveExpressEntity.builder()
				                                  .operatorID(signExpressVO.getOperatorID())
				                                  .recipientID(signExpressVO.getRecipientID())
				                                  .receiptDate(DateUtil.getSimpleDate(signExpressVO.getReceiptDate()))
				                                  .expressCompany(signExpressVO.getExpressCompany())
				                                  .expressNumber(signExpressVO.getExpressNumber())
				                                  .status(signExpressVO.getStatus())
				                                  .claimID(signExpressVO.getClaimID())
				                                  .claimDate(signExpressVO.getClaimDate()==null?null:DateUtil.getFullDate(signExpressVO.getClaimDate()))
				                                  .isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
												  .addTime(now)
												  .updateTime(now)
												  .build();
		signExpressDao.save(signExpressEntity);

	}


	@Override
	public ListResult<SignExpressVO> findSignExpressVOsBySignExpressVO(SignExpressVO signExpressVO, int page,
			int limit) {
		List<Object> signExpressObjs = baseDao.findPageList(getQueryHqlBySignExpressVO(signExpressVO), page, limit);
		List<SignExpressVO> signExpressVOs = new ArrayList<>();
		for(Object obj:signExpressObjs){
			Object[] objs = (Object[]) obj;
			SignExpressVO signExpressVO1=new SignExpressVO();
			signExpressVO1.setRecipientName((String)objs[0]);
			signExpressVO1.setRecipientID((String)objs[1]);
			signExpressVO1.setReceiptDate(DateUtil.formateDate((Date)objs[2]));
			signExpressVO1.setExpressCompany(Integer.parseInt(objs[3].toString()));
			signExpressVO1.setExpressNumber((String)objs[4]);
			signExpressVO1.setStatus(Integer.parseInt(objs[5].toString()));
			signExpressVO1.setClaimID((String)objs[6]);
			signExpressVO1.setClaimDate((Date)objs[7]==null?null:DateUtil.formateFullDate((Date)objs[7]));
			signExpressVO1.setSignExpressID((Integer)objs[8]);
			if(!StringUtils.isBlank((String)objs[6])){
				signExpressVO1.setClaimName(staffService.getStaffByUserID((String)objs[6]).getLastName());
			}
			signExpressVOs.add(signExpressVO1);
		}
		Object countObj = baseDao.getUniqueResult(getQueryCountSqlBySignExpressVO(signExpressVO));
		int count = countObj==null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<SignExpressVO>(signExpressVOs,count);
	}


	private String getQueryHqlBySignExpressVO(SignExpressVO signExpressVO) {
		StringBuffer Sql = new StringBuffer("select staff.staffName,receiveExpress.recipientID,receiveExpress.receiptDate,"
				+ "receiveExpress.expressCompany,receiveExpress.expressNumber,receiveExpress.status,receiveExpress.claimID, "
				+ "receiveExpress.claimDate,receiveExpress.receiveExpressID From OA_ReceiveExpress receiveExpress,OA_Staff staff where receiveExpress.recipientID = staff.userID and "
				+ "receiveExpress.isDeleted = 0 and staff.isDeleted = 0 ");
		Sql.append(getWhereByExpress(signExpressVO));
		Sql.append(" order by receiveExpress.receiptDate desc");
		return Sql.toString();
	}
	private String getQueryCountSqlBySignExpressVO(SignExpressVO signExpressVO) {
		StringBuffer countHql = new StringBuffer("select count(*) "
				+ "From OA_ReceiveExpress receiveExpress,OA_Staff staff where receiveExpress.recipientID = staff.userID and "
				+ "receiveExpress.isDeleted = 0 and staff.isDeleted = 0 ");
		countHql.append(getWhereByExpress(signExpressVO));
		
		return countHql.toString();
	}
	
	private String getWhereByExpress(SignExpressVO signExpressVO) {
		StringBuffer whereSql=new StringBuffer("");
		if(!StringUtils.isBlank(signExpressVO.getBeginDate())){
			whereSql.append(" and receiveExpress.receiptDate >='"+signExpressVO.getBeginDate()+"'");
			
		}
		if(!StringUtils.isBlank(signExpressVO.getEndDate())){
			whereSql.append(" and receiveExpress.receiptDate <='"+signExpressVO.getEndDate()+"'");
			
		}
		if(!StringUtils.isBlank(signExpressVO.getRecipientName())){
			whereSql.append(" and staff.staffName like '%"+signExpressVO.getRecipientName()+"%'");
		}
		if(signExpressVO.getExpressCompany()!=null){
			whereSql.append(" and receiveExpress.expressCompany="+signExpressVO.getExpressCompany());
			
		}
		if(signExpressVO.getStatus()!=null){
			whereSql.append(" and receiveExpress.status="+signExpressVO.getStatus());
			
		}
		return whereSql.toString();
	}


	@Override
	public SignExpressVO getSignExpressByID(Integer signExpressID) {
		String sql = "select staff.staffName,receiveExpress.recipientID,receiveExpress.receiptDate,"
				+ "receiveExpress.expressCompany,receiveExpress.expressNumber "
				+ "From OA_ReceiveExpress receiveExpress,OA_Staff staff where receiveExpress.recipientID = staff.userID and "
				+ "receiveExpress.isDeleted = 0 and staff.isDeleted = 0 and receiveExpress.receiveExpressID ="+signExpressID;
		List<Object> signExpressObjs=baseDao.findBySql(sql);
		Object[] objs = (Object[]) signExpressObjs.get(0);
		SignExpressVO signExpressVO=new SignExpressVO();
		signExpressVO.setRecipientName((String)objs[0]);
		signExpressVO.setRecipientID((String)objs[1]);
		signExpressVO.setReceiptDate(DateUtil.formateDate((Date)objs[2]));
		signExpressVO.setExpressCompany(Integer.parseInt(objs[3].toString()));
		signExpressVO.setExpressNumber((String)objs[4]);
		return signExpressVO;
	}


	@Override
	public void updateSignExpress(SignExpressVO signExpressVO) {
		ReceiveExpressEntity signExpressEntity = signExpressDao.getSignExpressEntityByID(signExpressVO.getSignExpressID());
		signExpressEntity.setStatus(signExpressVO.getStatus());
		signExpressEntity.setClaimID(signExpressVO.getClaimID());
		signExpressEntity.setClaimDate(DateUtil.getFullDate(signExpressVO.getClaimDate()));
		Date now = new Date();
		signExpressEntity.setUpdateTime(now);
		signExpressDao.save(signExpressEntity);
		
	}

}
