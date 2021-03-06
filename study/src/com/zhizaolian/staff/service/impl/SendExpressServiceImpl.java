package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.SendExpressDao;
import com.zhizaolian.staff.entity.SendExpressEntity;
import com.zhizaolian.staff.enums.CompanyIDEnum;
import com.zhizaolian.staff.enums.ExpressCompanyEnum;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.SendExpressService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.utils.Lists2;
import com.zhizaolian.staff.utils.SafeFunction;
import com.zhizaolian.staff.vo.DepartmentVO;
import com.zhizaolian.staff.vo.SendExpressVO;

public class SendExpressServiceImpl implements SendExpressService {
	@Autowired
	private SendExpressDao sendExpressDao;

	@Autowired
	private PositionService positionService;
	@Autowired
	private BaseDao baseDao; 
	
	
	@Override
	public void saveSendExpress(SendExpressVO sendExpressVO) {
		String expressNumer=sendExpressVO.getExpressNumber();
		String[] expressNumbers=expressNumer.split("；",-1);
		if(expressNumbers.length==1){
			expressNumbers=expressNumer.split(";", -1);
		}
		for (String keyNumber : expressNumbers) {
			Date now = new Date();
			SendExpressEntity sendExpressEntity = SendExpressEntity.builder()
					                                         .userID(sendExpressVO.getUserID())
					                                         .postDate(DateUtil.getSimpleDate(sendExpressVO.getPostDate()))
					                                         .weekDay(sendExpressVO.getWeekDay())
					                                         .companyID(sendExpressVO.getCompanyID())
					                                         .departmentID(sendExpressVO.getDepartmentID())
					                                         .expressCompany(sendExpressVO.getExpressCompany())
					                                         .expressNumber(keyNumber)
					                                         .type(sendExpressVO.getType())
					                                         .reason(sendExpressVO.getReason())
					                                         .isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
														     .addTime(now)
														     .updateTime(now)
														     .build();
			
			sendExpressDao.save(sendExpressEntity);
		}
		
	}

	@Override
	public ListResult<SendExpressVO> findSendExpressVOsBySendExpressVO(SendExpressVO sendExpressVO, int page,
			int limit) {
		List<Object> list=baseDao.findPageList(getQueryHqlBySendExpressVO(sendExpressVO), page, limit);
		List<SendExpressVO> sendExpressVOs=new ArrayList<>();
		for(Object obj:list){
			SendExpressVO sendExpressVO2=new SendExpressVO();
			Object[] objs=(Object[])obj;
			sendExpressVO2.setType(Integer.parseInt(objs[0].toString()));
			sendExpressVO2.setUserName((String)objs[1]);
			sendExpressVO2.setPostDate(DateUtil.formateDate((Date)objs[2]));
			sendExpressVO2.setCompanyID((Integer)objs[3]);
			if(objs[4]==null){
				sendExpressVO2.setDepartmentName("");
			}else{
				DepartmentVO departmentVo = positionService.getDepartmentByID((Integer)objs[4]);
				if(null != departmentVo){
					sendExpressVO2.setDepartmentName(departmentVo.getDepartmentName());
				}
			}
			sendExpressVO2.setExpressCompany(Integer.parseInt(objs[5].toString()));
			sendExpressVO2.setExpressNumber((String)objs[6]);
			sendExpressVO2.setReason((String)objs[7]);
			sendExpressVO2.setWeekDay((String)objs[8]);
			sendExpressVOs.add(sendExpressVO2);
		}
		Object countObj=baseDao.getUniqueResult(getQueryCountSqlBySendExpressVO(sendExpressVO));
		int count=countObj == null? 0:((BigInteger)countObj).intValue();
		return new ListResult<SendExpressVO>(sendExpressVOs,count);
	}
	
	@Override
	public HSSFWorkbook exportSendExpressList(SendExpressVO sendExpressVO) {
		 // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("快递账单明细");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格
        row.createCell((short) 0).setCellValue("类型");
        row.createCell((short) 1).setCellValue("寄件（收货）人");  
        row.createCell((short) 2).setCellValue("寄件（收货）日期");  
        row.createCell((short) 3).setCellValue("地区");  
        row.createCell((short) 4).setCellValue("部门");
        row.createCell((short) 5).setCellValue("物流公司");
        row.createCell((short) 6).setCellValue("物流单号");
        row.createCell((short) 7).setCellValue("寄件原因");
        
        Object countObj = baseDao.getUniqueResult(getQueryCountSqlBySendExpressVO(sendExpressVO));
		int count = countObj==null?0:((BigInteger)countObj).intValue();
		int limit=100, page=1;
		int totalPage = count%limit==0 ? count/limit : count/limit+1;
		while (page <= totalPage) {
			ListResult<SendExpressVO> sendExpressList = findSendExpressVOsBySendExpressVO(sendExpressVO, page, limit);
			addLines(sendExpressList.getList(), sheet);
			page += 1;
		}
		
		return wb;
	}
	
	private void addLines(List<SendExpressVO> sendExpressList, HSSFSheet sheet) {
		int size = sendExpressList.size();
		for (int i=0,j=sheet.getLastRowNum()+1; i < size; ++i,++j)  
        {  
            HSSFRow row = sheet.createRow(j);  
            SendExpressVO sendExpressVO = sendExpressList.get(i);
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(sendExpressVO.getType()==1?"寄付":"到付");  
            row.createCell((short) 1).setCellValue(sendExpressVO.getUserName());  
            row.createCell((short) 2).setCellValue(sendExpressVO.getPostDate());  
            CompanyIDEnum company = sendExpressVO.getCompanyID()==null?null:CompanyIDEnum.valueOf(sendExpressVO.getCompanyID());
            row.createCell((short) 3).setCellValue(company==null?"":company.getName());
            row.createCell((short) 4).setCellValue(sendExpressVO.getDepartmentName());
            ExpressCompanyEnum expressCompany = ExpressCompanyEnum.valueOf(sendExpressVO.getExpressCompany());
            row.createCell((short) 5).setCellValue(expressCompany==null?"":expressCompany.getName());
            row.createCell((short) 6).setCellValue(sendExpressVO.getExpressNumber());
            row.createCell((short) 7).setCellValue(sendExpressVO.getReason());
        }  
	}
	
	private String getQueryHqlBySendExpressVO(SendExpressVO sendExpressVO){
		StringBuffer hql = new StringBuffer("select sendExpress.Type,staff.StaffName,sendExpress.PostDate, "
				+ "sendExpress.CompanyID,sendExpress.DepartmentID,sendExpress.ExpressCompany,sendExpress.ExpressNumber, "
				+ "sendExpress.Reason,sendExpress.weekDay from OA_SendExpress sendExpress, OA_Staff staff where "
				+ "sendExpress.IsDeleted = 0 and staff.IsDeleted = 0 and staff.UserID = sendExpress.UserID ");
		hql.append(getWhereByExpress(sendExpressVO));
		hql.append(" order by sendExpress.addTime desc");
		return hql.toString();
	}
	
	private String getQueryCountSqlBySendExpressVO(SendExpressVO sendExpressVO){
		StringBuffer countHql = new StringBuffer("select count(*) from ( select sendExpress.Type,staff.StaffName,sendExpress.PostDate, "
				+ "sendExpress.CompanyID,sendExpress.DepartmentID,sendExpress.ExpressCompany,sendExpress.ExpressNumber, "
				+ "sendExpress.Reason from OA_SendExpress sendExpress, OA_Staff staff where "
				+ "sendExpress.IsDeleted = 0 and staff.IsDeleted = 0 and staff.UserID = sendExpress.UserID ");
		countHql.append(getWhereByExpress(sendExpressVO));
		countHql.append(" ) a");
		return countHql.toString();
	}
	
	private String getWhereByExpress(SendExpressVO sendExpressVO){
		StringBuffer whereSql=new StringBuffer("");
		
		if (sendExpressVO.getCompanyID() != null) {
			whereSql.append(" and sendExpress.companyID = "+sendExpressVO.getCompanyID());
			if (sendExpressVO.getDepartmentID() != null) {
				List<DepartmentVO> departmentVOs = positionService.findDepartmentsByCompanyIDParentID(sendExpressVO.getCompanyID(), sendExpressVO.getDepartmentID());
				List<Integer> departmentIDs = Lists2.transform(departmentVOs, new SafeFunction<DepartmentVO, Integer>() {
					@Override
					protected Integer safeApply(DepartmentVO input) {
						return input.getDepartmentID();
					}
				});
				departmentIDs.add(sendExpressVO.getDepartmentID());
				String arrayString = Arrays.toString(departmentIDs.toArray());
				whereSql.append(" and sendExpress.departmentID in ("+arrayString.substring(1, arrayString.length()-1)+")");
			}
		}
		if(!StringUtils.isBlank(sendExpressVO.getBeginDate())){
			whereSql.append(" and sendExpress.postDate >='"+sendExpressVO.getBeginDate()+"'");
			
		}
		if(!StringUtils.isBlank(sendExpressVO.getEndDate())){
			whereSql.append(" and sendExpress.postDate <='"+sendExpressVO.getEndDate()+"'");
			
		}
		if(sendExpressVO.getExpressCompany()!=null){
			whereSql.append(" and sendExpress.expressCompany="+sendExpressVO.getExpressCompany());
			
		}
		if(sendExpressVO.getType()!=null) {
			whereSql.append(" and sendExpress.type="+sendExpressVO.getType());
		}
		if(!StringUtils.isBlank(sendExpressVO.getUserName())){
			whereSql.append(" and staff.StaffName like '%"+sendExpressVO.getUserName()+"%' ");
		}		return whereSql.toString();
	} 
	
	
	
	

}
