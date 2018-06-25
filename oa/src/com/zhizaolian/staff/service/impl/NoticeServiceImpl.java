package com.zhizaolian.staff.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.zhizaolian.staff.dao.BaseDao;
import com.zhizaolian.staff.dao.NoticeActorDao;
import com.zhizaolian.staff.dao.NoticeDao;
import com.zhizaolian.staff.entity.CommonAttachment;
import com.zhizaolian.staff.entity.NoticeActorEntity;
import com.zhizaolian.staff.entity.NoticeEntity;
import com.zhizaolian.staff.enums.AttachmentType;
import com.zhizaolian.staff.enums.IsDeletedEnum;
import com.zhizaolian.staff.service.NoticeService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.DateUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.NoticeVO;

public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private BaseDao baseDao;
	@Autowired
	private StaffService staffService;
	@Autowired
	private NoticeActorDao noticeActorDao;
	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public Integer saveNoticeVO(NoticeVO noticeVO) {
		Date now = new Date();
		NoticeEntity noticeEntity = NoticeEntity.builder()
				                          .creatorID(noticeVO.getCreatorID())
				                          .ntcTitle(noticeVO.getNtcTitle())
				                          .ntcContent(noticeVO.getNtcContent())
				                          .isTop(noticeVO.getIsTop())
				                          .topStartTime(DateUtil.getSimpleDate(noticeVO.getTopStartTime()))
				                          .topEndTime(DateUtil.getSimpleDate(noticeVO.getTopEndTime()))
				                          .type(noticeVO.getType())
				                          .departmentIDs(noticeVO.getDepartments())
				                          .companyIDs(noticeVO.getCompanys())
				                          .isDeleted(IsDeletedEnum.NOT_DELETED.getValue())
                                          .addTime(now)
                                          .updateTime(now)
                                          .build();
		return noticeDao.saveNotice(noticeEntity);
		}

	@Override
	public ListResult<NoticeVO> findNoticesByUserID(String userID, int limit, int page) {
		String sql = "select distinct * from (select notice.NtcTitle,notice.NtcContent,noticeActor.UserID,notice.AddTime,noticeActor.Status,notice.ntcID"
				+ " from OA_Notice notice,OA_NoticeActor noticeActor where "
				+ "notice.NtcID = noticeActor.NoticeID and notice.IsDeleted = 0 and "
				+ "noticeActor.IsDeleted = 0  and noticeActor.UserID='"+userID+"' order by noticeActor.AddTime desc) a";
		List<Object> noticeObjs=baseDao.findPageList(sql, page, limit);
		List<NoticeVO> noticeVOs=new ArrayList<>();
		for(Object obj:noticeObjs){
			NoticeVO noticeVO = new NoticeVO();
			Object[] objs=(Object[])obj;
			noticeVO.setNtcTitle((String)objs[0]);
			noticeVO.setNtcContent((String) objs[1]);
			noticeVO.setUserID((String)objs[2]);
			noticeVO.setNoticeDate(DateUtil.formateDate((Date) objs[3]));
			noticeVO.setStatus(Integer.parseInt(objs[4].toString()));
			noticeVO.setNtcID((Integer)objs[5]);
			noticeVOs.add(noticeVO);
		}
		String countSql = "select count(*) from (select distinct * from (select notice.NtcTitle,notice.NtcContent,noticeActor.UserID,notice.AddTime,noticeActor.Status,notice.ntcID"
				+ " from OA_Notice notice,OA_NoticeActor noticeActor where "
				+ "notice.NtcID = noticeActor.NoticeID and notice.IsDeleted = 0 and "
				+ "noticeActor.IsDeleted = 0  and noticeActor.UserID='"+userID+"' order by noticeActor.AddTime desc) a) s";
		Object countObj = baseDao.getUniqueResult(countSql);
		int count = countObj==null ? 0 : ((BigInteger)countObj).intValue();
		return new ListResult<>(noticeVOs, count);
	}
	
	
	
	@Override
	public NoticeVO getNoticeByNtcActorID(int ntcActorID) {
		String sql = "select notice.NtcTitle,notice.NtcContent "
				+ "from OA_Notice notice,OA_NoticeActor noticeActor where "
				+ "notice.NtcID = noticeActor.NoticeID and notice.IsDeleted = 0 and "
				+ "noticeActor.IsDeleted = 0  and noticeActor.NtcActorID='"+ntcActorID+"'";
		Object obj = baseDao.getUniqueResult(sql);
		Object[] objs=(Object[])obj;
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setNtcTitle((String)objs[0]);
		noticeVO.setNtcContent((String) objs[1]);
		return noticeVO;
		
	}

	@Override
	public ListResult<NoticeVO> findNoticeList(NoticeVO noticeVO, int limit, int page) {
		ListResult<NoticeEntity> nListResult=noticeDao.findNoticeList(getQuerySqlByNoticeVO(noticeVO),getQueryCountSqlByNoticeVO(noticeVO), page, limit);
		List<NoticeVO> list=new ArrayList<>();
		for(NoticeEntity noticeEntity:nListResult.getList()){
			NoticeVO noticeVO2=new NoticeVO();
			noticeVO2.setNtcID(noticeEntity.getNtcID());
			noticeVO2.setCreatorID(noticeEntity.getCreatorID());
			noticeVO2.setIsTop(noticeEntity.getIsTop());
			noticeVO2.setNtcContent(noticeEntity.getNtcContent());
			noticeVO2.setNtcTitle(noticeEntity.getNtcTitle());
			noticeVO2.setType(noticeEntity.getType());
			noticeVO2.setTopEndTime(noticeEntity.getTopEndTime()==null?null:DateUtil.formateDate(noticeEntity.getTopEndTime()));
			noticeVO2.setTopStartTime(noticeEntity.getTopStartTime()==null?null:DateUtil.formateDate(noticeEntity.getTopStartTime()));
			noticeVO2.setCreatorName(staffService.getStaffByUserID(noticeEntity.getCreatorID()).getLastName());
			noticeVO2.setNoticeDate(DateUtil.formateDate(noticeEntity.getAddTime()));
			list.add(noticeVO2);
		}
		return new ListResult<NoticeVO>(list,nListResult.getTotalCount());
	}

	private String getQuerySqlByNoticeVO(NoticeVO noticeVO){
		StringBuffer hql=new StringBuffer("from NoticeEntity notice where notice.isDeleted = 0");
		hql.append(getWhereByNoticeVO(noticeVO));
		hql.append(" order by notice.addTime desc");
		return hql.toString();
	}
	
	@Override
	public int countUnReadNoticeByUserID(String userID) {
		String sql = "select count(*) from (select distinct * from(select ntcActor.UserID,ntcActor.NoticeID from OA_Notice ntc, OA_NoticeActor ntcActor where ntc.NtcID = ntcActor.NoticeID "
				+ "and ntc.IsDeleted = 0 and ntcActor.IsDeleted = 0 and ntcActor.UserID = '"+userID+"' "
				+ "and ntcActor.Status = 0) a) s";
		Object count = baseDao.getUniqueResult(sql);
		return count==null ? 0 : ((BigInteger) count).intValue();
	}
	
	
	private String getQueryCountSqlByNoticeVO(NoticeVO noticeVO){
		StringBuffer hql=new StringBuffer("SELECT COUNT(*) FROM NoticeEntity notice WHERE notice.isDeleted = 0");
		hql.append(getWhereByNoticeVO(noticeVO));
		return hql.toString();
	}
	
	private String getWhereByNoticeVO(NoticeVO noticeVO){
		StringBuffer whereSql=new StringBuffer();
		if (noticeVO.getType()!=null) {
			whereSql.append(" and notice.type = "+noticeVO.getType());
		}
		
	    if (noticeVO.getIsTop()!=null) {
		whereSql.append(" and notice.isTop = "+noticeVO.getIsTop());
	    }
	    
	    if (!StringUtils.isBlank(noticeVO.getBeginTime())) {
			whereSql.append(" and notice.addTime >= '"+noticeVO.getBeginTime()+"'");
		}
	    
	   if (!StringUtils.isBlank(noticeVO.getEndTime())) {
		whereSql.append(" and notice.addTime <= '"+noticeVO.getEndTime()+"'");
	    }
		return whereSql.toString();
	 }


	@Override	public void deleteNotice(Integer ntcID) {
		noticeDao.deleteNotice(ntcID);
		
	}

	@Override
	public NoticeVO getNoticeByNtcID(Integer ntcID) {
			NoticeEntity noticeEntity=noticeDao.getNoticeByNtcID(ntcID);
			NoticeVO noticeVO=new NoticeVO();
			noticeVO.setNtcID(noticeEntity.getNtcID());
			noticeVO.setCreatorID(noticeEntity.getCreatorID());
			noticeVO.setCreatorName(staffService.getStaffByUserID(noticeEntity.getCreatorID()).getLastName());
			noticeVO.setIsTop(noticeEntity.getIsTop());
			noticeVO.setNtcContent(noticeEntity.getNtcContent());
			noticeVO.setNtcTitle(noticeEntity.getNtcTitle());
			noticeVO.setType(noticeEntity.getType());
			noticeVO.setTopEndTime(noticeEntity.getTopEndTime()==null?null:DateUtil.formateDate(noticeEntity.getTopEndTime()));
			noticeVO.setTopStartTime(noticeEntity.getTopStartTime()==null?null:DateUtil.formateDate(noticeEntity.getTopStartTime()));
			noticeVO.setCompanys(noticeEntity.getCompanyIDs());
			noticeVO.setDepartments(noticeEntity.getDepartmentIDs());
			return noticeVO;	
	}
	
	@Override
	public NoticeVO getLatestNotice() {
		NoticeEntity noticeEntity = noticeDao.getLatestNotice();
		if (noticeEntity == null) {
			return null;
		}
		NoticeVO noticeVO = new NoticeVO();
		noticeVO.setNtcTitle(noticeEntity.getNtcTitle());
		noticeVO.setNtcContent(noticeEntity.getNtcContent());
		return noticeVO;
	}
	
	@Override
	public Integer saveAttachMent(CommonAttachment commonAttachment) {
		return noticeDao.saveAttachMent(commonAttachment);		
	}

	@Override
	public ListResult<NoticeVO> findNoticeList1(NoticeVO noticeVO, int limit, int page) {
		List<Object> nListResult=baseDao.findPageList(getQuerySqlByNoticeVO1(noticeVO), page, limit);
		List<NoticeVO> list=new ArrayList<>();
		for(Object obj:nListResult){
			NoticeVO noticeVO2=new NoticeVO();
			Object[] objs = (Object[]) obj;
			noticeVO2.setIsTop((Integer)objs[0]);
			noticeVO2.setNtcTitle((String)objs[1]);
			noticeVO2.setNtcContent((String)objs[2]);
			noticeVO2.setNoticeDate(DateUtil.formateDate((Date)objs[3]));
			noticeVO2.setNtcID((Integer)objs[4]);
			list.add(noticeVO2);
		}
		Object countObj = baseDao.getUniqueResult(getQueryCountSqlByNoticeVO1(noticeVO));
		int count = countObj==null ? 0 : ((BigInteger) countObj).intValue();
		return new ListResult<NoticeVO>(list,count);
		
	}
	private String getQuerySqlByNoticeVO1(NoticeVO noticeVO){
		StringBuffer hql=new StringBuffer("SELECT ( CASE WHEN SYSDATE() < notice.TopEndTime AND SYSDATE() > TopStartTime"
				+ " AND notice.IsTop = 1 THEN 1 ELSE 0 END ) AS isTop, notice.NtcTitle, notice.NtcContent, "
				+ "notice.AddTime,notice.NtcID  FROM OA_Notice notice where notice.IsDeleted = 0 and notice.Type = 1 "
				+ "ORDER BY isTop DESC, notice.AddTime DESC ");		
				return hql.toString();
	}
	private String getQueryCountSqlByNoticeVO1(NoticeVO noticeVO){
		StringBuffer hql=new StringBuffer("SELECT COUNT(*) FROM OA_Notice notice WHERE notice.IsDeleted = 0 and notice.Type = 1 ");		
		return hql.toString();
	}

	@Override
	public List<CommonAttachment> getCommonAttachmentByFID(Integer fId,AttachmentType attachmentType) {
		return noticeDao.getCommonAttachmentByFID(fId,attachmentType);
	}

	@Override
	public CommonAttachment getCommonAttachmentById(Integer id) {
		return noticeDao.getCommonAttachmentById(id);
	}

	@Override
	public void synNotice(Integer companyID, Integer departmentID, String userID) {
		String hql = "from NoticeEntity where LOCATE('"+companyID+",',CompanyIDs)>0 and type=2";
		@SuppressWarnings("unchecked")
		List<NoticeEntity> notices = (List<NoticeEntity>) baseDao.hqlfind(hql);
		List<NoticeEntity> needSendToNewStaffNotices = new ArrayList<>();
		for(NoticeEntity notice: notices){
			String companyIDStr = notice.getCompanyIDs();
			String departmentIDStr = notice.getDepartmentIDs();
			String[] companyIDs = companyIDStr.split(",");
			String[] departmentIDs = departmentIDStr.split(",");
			int index = 0;
			//查找员工所属部门是不是在消息的发布范围内
			for(String companyId: companyIDs){
				if(companyId.equals(companyID+"")){
					if(departmentIDs[index].equals(departmentID+"") || departmentIDs[index].equals("null")){
						needSendToNewStaffNotices.add(notice);
					}
				}
				index++;
			}
		}
		for(NoticeEntity notice: needSendToNewStaffNotices){
			NoticeActorEntity noticeActorEntity=new NoticeActorEntity();
			Date date = new Date();
			noticeActorEntity.setUserID(userID);
			noticeActorEntity.setNoticeID(notice.getNtcID());
			noticeActorEntity.setIsDeleted(IsDeletedEnum.NOT_DELETED.getValue());
			noticeActorEntity.setAddTime(date);
			noticeActorEntity.setUpdateTime(date);
			noticeActorEntity.setStatus(0);
			noticeActorDao.saveNoticeActor(noticeActorEntity);	
		}
	}

	@Override
	public void updateNoticeVO(NoticeVO noticeVO) {
		NoticeEntity noticeEntity = noticeDao.getNoticeByNtcID(noticeVO.getNtcID());
		noticeEntity.setNtcTitle(noticeVO.getNtcTitle());
		noticeEntity.setNtcContent(noticeVO.getNtcContent());
		noticeEntity.setIsTop(noticeVO.getIsTop());
		noticeEntity.setTopStartTime(DateUtil.getSimpleDate(noticeVO.getTopStartTime()));
		noticeEntity.setTopEndTime(DateUtil.getSimpleDate(noticeVO.getTopEndTime()));
		baseDao.hqlUpdate(noticeEntity);
	}

	@Override
	public void deleteCommentAttachmentByFId(Integer ntcID, AttachmentType attachmentType) {
		String hql="update CommonAttachment c set isDeleted=1 where c.foreign_ID=:foreign_ID and c.type=:type";
		this.sessionFactory.getCurrentSession().createQuery(hql).setParameter("foreign_ID", ntcID)
					.setParameter("type", attachmentType.getIndex()).executeUpdate();
	}
}
