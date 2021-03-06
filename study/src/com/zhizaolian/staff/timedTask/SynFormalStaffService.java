package com.zhizaolian.staff.timedTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.DepartmentDao;
import com.zhizaolian.staff.enums.StaticResource;
import com.zhizaolian.staff.vo.StaffVO;

/**
 * 定时同步即将转正的员工（入职满30天）
 * @author yxl
 *
 */
@Lazy(value=false)
public class SynFormalStaffService {

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private DepartmentDao departmentDao;

	public void findFormalStaffs(){
		//清空
		StaticResource.companyIdAndformalStaffVosMap.clear();
		StringBuffer sql = new StringBuffer("select s.StaffID,s.StaffName,s.EntryDate,s.DepartmentID,s.CompanyID from("
				+ "select DISTINCT * from(select staff.StaffID,staff.StaffName,staff.EntryDate,groupDetail.DepartmentID,groupDetail.CompanyID from OA_Staff staff "
				+ "LEFT JOIN ACT_ID_MEMBERSHIP membership ON staff.UserID = membership.USER_ID_  "
				+ "LEFT JOIN OA_GroupDetail groupDetail ON membership.GROUP_ID_ = groupDetail.GroupID "
				+ "where staff.IsDeleted = 0 and groupDetail.IsDeleted = 0 and staff.Status= 1 "				
				+ "and staff.`Status` = 1 and staff.UserID "
				+ "NOT IN (SELECT f.RequestUserID FROM OA_Formal f, ACT_HI_PROCINST p WHERE f.ProcessInstanceID = p.PROC_INST_ID_ AND p.END_ACT_ID_ IS NULL AND f.IsDeleted = 0) ");
		sql.append(" )a) s");
		sql.append(" GROUP BY s.StaffID order by s.EntryDate ");
		List<Object> formalStaffs = baseDao.findBySql(sql.toString());
		Date date=new Date();
		for(Object obj: formalStaffs){
			Object[] objs = (Object[]) obj;
			long days = (date.getTime()-((Date)objs[2]).getTime())/86400000;
			//入职满30天，转正提醒
			if(days<30){
				break;
			}
			StaffVO staffVO=new StaffVO();
			staffVO.setStaffID((Integer)objs[0]);
			staffVO.setLastName((String)objs[1]);
			staffVO.setEntryDate1((Date)objs[2]);
			staffVO.setDepartmentID((Integer)objs[3]);
			if(staffVO.getDepartmentID()!=null){
				staffVO.setDepartmentName(departmentDao.getDepartmentByDepartmentID(staffVO.getDepartmentID()).getDepartmentName());
			}
			//入职天数
			staffVO.setDays(days+"");
			if(StaticResource.companyIdAndformalStaffVosMap.containsKey((Integer)objs[4])){
				StaticResource.companyIdAndformalStaffVosMap.get((Integer)objs[4]).add(staffVO);
			}else{
				List<StaffVO> list = new ArrayList<>();
				list.add(staffVO);
				StaticResource.companyIdAndformalStaffVosMap.put((Integer)objs[4], list);
			}
		}				
	}
}
