package com.zhizaolian.staff.action.administration;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.Chop;
import com.zhizaolian.staff.service.ChopService;
import com.zhizaolian.staff.service.ProcessService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ActionUtil;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.ChopBorrrowVo;
import com.zhizaolian.staff.vo.ChopUseLogVo;
import com.zhizaolian.staff.vo.CommentVO;
import com.zhizaolian.staff.vo.GroupDetailVO;
import com.zhizaolian.staff.vo.TaskVO;

import lombok.Getter;
import lombok.Setter;

/**
 *@author Zhouk
 *@date 2017年3月22日 下午1:11:33
 *@describtion  公章维护
 **/
public class ChopAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	@Getter
	@Setter
	private String selectedPanel="chopList";
	@Getter
	private String errorMessage;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Setter
	@Getter
	private Chop chop;
	@Autowired
	private ChopService chopService;
	public String toChopListPage(){
		try{
			String name=request.getParameter("name");
			ListResult<Chop> chops=chopService.getChopByName(name, null, page, limit);
			request.setAttribute("name", name);
			count = chops.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			} 
			request.setAttribute("startIndex", (page-1)*limit);
			request.setAttribute("chops", chops.getList());
			return "chopListPage";
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

	}
	public String toChopListPage_(){
		try{
			String name=request.getParameter("name");
			ListResult<Chop> chops=chopService.getChopByName(name, null, page, limit);
			request.setAttribute("name", name);
			count = chops.getTotalCount();
			totalPage = count % limit == 0 ? count / limit : count / limit + 1;
			if (totalPage == 0) {
				totalPage = 1;
			}
			request.setAttribute("startIndex", (page-1)*limit);
			request.setAttribute("chops", chops.getList());
			return "chopListPage";
		}catch(Exception e){
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}

	}

	public String  toChopAddPage (){
		String id=request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			ListResult<Chop> chops=chopService.getChopByName(null, id, page, limit);
			if(CollectionUtils.isNotEmpty(chops.getList()))
				chop=chops.getList().get(0);
		}
		return "chopAddPage";
	}
	private final static String[] TOCHOPUSELOG_PARAMS={"chopId","startTime","endTime"};
	public String toChopUseLog(){
		Map<String, String> queryMap=ActionUtil.createMapByRequest(request, true, TOCHOPUSELOG_PARAMS);
		ListResult<ChopBorrrowVo> chops=chopService.findChopLogListByKeys(queryMap, page, limit);
		count = chops.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page-1)*limit);
		request.setAttribute("chops", chops.getList());
		return "toChopUseLog";
	}
	public String  saveChop(){
		chopService.save(chop);
		return "chopListPage";
	}
	public String updateChop(){
		chopService.update(chop);;
		return "chopListPage";
	}
	public String chopDelete(){
		String id=request.getParameter("id");
		try{
			chopService.delete(id);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "chopListPage1";
	} 
	public String toChopBorrowLstPage(){
		String name=request.getParameter("name");
		ListResult<ChopBorrrowVo> chopBorrowLst = chopService.getChopBorrowVoLst(name, page, limit);
		count = chopBorrowLst.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page - 1) * limit + 1);
		request.setAttribute("chopBorrowLst", chopBorrowLst.getList());
		request.setAttribute("name", name);
		return "chopBorrowLstPage";
	}
	@Autowired
	private StaffService staffService;
	@Autowired
	private ProcessService processService;
	public String showChopBorrowDetail(){
		String chopBorrowId = request.getParameter("chopBorrowId");
		String pInstanceId = chopService.getPInstanceId(chopBorrowId);
		ChopBorrrowVo chopBorrowVo = chopService.getChopByInstanceId(pInstanceId);
		Integer chopBorrow_Id = chopBorrowVo.getChopBorrow_Id();
		String userName = staffService.getRealNameByUserId(chopBorrowVo.getUser_Id());
		Date addTime = chopBorrowVo.getAddTime();
		String year = new SimpleDateFormat("yyyy").format(addTime);
		String department = "";
		List<GroupDetailVO> groupDetails = staffService.findGroupDetailsByUserID(chopBorrowVo.getUser_Id());
		if(org.apache.commons.collections4.CollectionUtils.isNotEmpty(groupDetails)){
			GroupDetailVO group = groupDetails.get(0);
			department += group.getCompanyName()+"-"+group.getDepartmentName();
		}
		request.setAttribute("chopBorrow_Id", chopBorrow_Id);
		request.setAttribute("year", year);
		request.setAttribute("department", department);
		List<CommentVO> comments = processService.getCommentsByProcessInstanceID(pInstanceId);
		request.setAttribute("comments", comments);
		List<TaskVO> finishedTaskVOs = processService.findFinishedTasksByProcessInstanceID(pInstanceId);
		request.setAttribute("finishedTaskVOs", finishedTaskVOs);
		request.setAttribute("chopBorrowVo", chopBorrowVo);
		request.setAttribute("userName", userName);
		return "showChopBorrowDetail";
	}
	@Setter
	@Getter
	private String startTime;
	@Setter
	@Getter
	private String endTime;
	@Setter
	@Getter
	private String userName;
	@Setter
	@Getter
	private String userId;
	public String toChopUseLogLst(){
		String[] query = {startTime, endTime, userId};
		ListResult<ChopUseLogVo> lstResult = chopService.getChopUseLog(query, limit, page);
		count = lstResult.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("startIndex", (page - 1) * limit + 1);
		request.setAttribute("chopUstLogLst", lstResult.getList());
		return "toChopUseLogLst";
	}
	public String findContractChopList(){
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		ListResult<ChopBorrrowVo> contractChopList = chopService.findContractChopList(beginDate, endDate, limit, page);
		count = contractChopList.getTotalCount();
		totalPage = count % limit == 0 ? count / limit : count / limit + 1;
		if (totalPage == 0) {
			totalPage = 1;
		}
		request.setAttribute("contractChopList", contractChopList.getList());
		request.setAttribute("beginDate", beginDate);
		request.setAttribute("endDate", endDate);
		return "contractChopList";
	}
	@Getter
	private InputStream inputStream;
	@Getter
	private String downloadFileName;
	public String exportContractChopDatas(){
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		try {
			inputStream = chopService.exportContractChopDatas(beginDate, endDate);
			downloadFileName = new String(("合同类盖章.xls").getBytes("gbk"), "iso-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "download";
	}
}
