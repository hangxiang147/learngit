package com.zhizaolian.staff.action.administration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.identity.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.action.BaseAction;
import com.zhizaolian.staff.entity.AssetEntity;
import com.zhizaolian.staff.entity.AssetUsageEntity;
import com.zhizaolian.staff.service.AssetService;
import com.zhizaolian.staff.service.AssetUsageService;
import com.zhizaolian.staff.service.PositionService;
import com.zhizaolian.staff.service.StaffService;
import com.zhizaolian.staff.utils.ListResult;
import com.zhizaolian.staff.vo.AssetUsageVO;
import com.zhizaolian.staff.vo.AssetVO;
import com.zhizaolian.staff.vo.CompanyVO;
import com.zhizaolian.staff.vo.StaffVO;

import lombok.Getter;
import lombok.Setter;

public class AssetAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Setter
	@Getter
	private Integer page = 1;
	@Setter
	@Getter
	private Integer limit = 20;
	@Getter
	private Integer totalPage;
	@Getter
	@Setter
	private String panel;
	@Getter
	@Setter
	private AssetVO assetVO;
	@Getter
	@Setter
	private List<AssetVO> assetVOs;
	@Getter
	@Setter
	private List<AssetUsageVO> assetUsageVOs;
	@Getter
	@Setter
	private AssetUsageVO assetUsageVO;
	@Getter
	private String selectedPanel;
	@Setter
	@Getter
	private String errorMessage;
	@Autowired
	private AssetService assetService;
	@Autowired
	private AssetUsageService assetUsageService;
	@Autowired
	private PositionService positionService;
	@Autowired
	private StaffService staffService;
	@Getter
	@Setter
	private AssetUsageEntity assetUsage;

	public String error() {
		try {
			errorMessage = URLDecoder.decode(errorMessage, "utf-8");
		} catch (Exception e) {
			errorMessage = e.getMessage();
		}
		return "error";
	}

	public String findAssetList() {
		if(assetVO==null){
			assetVO=new AssetVO();
		}
		try{
			ListResult<AssetVO> assetlist=assetService.findAssetList(assetVO, limit, page);
			count=assetlist.getTotalCount();
			totalPage=count%limit==0 ? count/limit : count/limit+1;
			List<CompanyVO> companyVOs = positionService.findAllCompanys();

			if(totalPage==0){
				totalPage=1;
			}
			request.setAttribute("assetlist", assetlist.getList());
			request.setAttribute("companyVOs", companyVOs);

		}catch(Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			selectedPanel = "assetList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";

		}
		selectedPanel = "assetList";
		return "assetList";
	}

	public String saveAsset(){
		try{
			Integer assetID=assetService.addAsset(assetVO);
			if(!StringUtils.isBlank(assetUsageVO.getRecipientID())){
				Integer[] assetIDs={assetID};
				assetUsageVO.setAssetIDs(assetIDs);
				User user = (User) request.getSession().getAttribute("user");
				assetUsageVO.setReceiveOperatorID(user.getId());
				assetUsageService.SaveAssetUsage(assetUsageVO);

			}
		}catch(Exception e) {
			e.printStackTrace();
			errorMessage = "保存失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

		return "saveAsset";
	}
	@Setter
	@Getter
	private ByteArrayInputStream excelFile;
	@Setter
	@Getter
	private String excelFileName;
	public String exportAssetVOToExcel(){
		try {
			if(assetVO.getAssetName()!=null){
				String trans = new String(assetVO.getAssetName().getBytes("ISO-8859-1"), "UTF-8");
				String name = URLDecoder.decode(trans, "UTF-8");
				assetVO.setAssetName(name);
			}
			XSSFWorkbook workbook = assetService.exportAssetVO(assetVO);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			workbook.write(output);
			byte[] ba = output.toByteArray();
			excelFile = new ByteArrayInputStream(ba);
			excelFileName = new String("资产信息列表.xlsx".getBytes(), "ISO8859-1");
			output.flush();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage = "导出excel失败：" + e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		return "exportAssetVOToExcel";
	}
	
	public String newAssetUsage(){
		List<CompanyVO> companyVOs = positionService.findAllCompanys();

		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			errorMessage = "您尚未登录，请先登录！";
			return "initUserAccountFailed";
		}
		StaffVO staffVO=staffService.getStaffByUserID(user.getId());
		request.setAttribute("companyVOs", companyVOs);
		request.setAttribute("staffVO", staffVO);

		selectedPanel = "assetUsageList";
		return "newAssetUsage";
	}
	public String findAssets(){
		if(assetVO==null){
			assetVO=new AssetVO();
		}
		assetVO.setStatus(0);
		try{
			ListResult<AssetVO> assetlist=assetService.findAssetList(assetVO, limit, page);
			assetVOs = assetlist.getList();
		}catch(Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}

		return "assets";
	}
	public String addAsseets(){
		if(assetVO==null){
			assetVO=new AssetVO();
		}
		try{
			assetVOs = new ArrayList<AssetVO>();
			if(assetVO.getAssetIDs()!=null){
				for(int i=0;i<assetVO.getAssetIDs().length;i++){
					AssetVO asset = assetService.getAssetByID(assetVO.getAssetIDs()[i]);
					assetVOs.add(asset);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "assets";
	}

	public String getAssetByID(){
		try{
			int assetID=Integer.parseInt(request.getParameter("assetID"));
			AssetVO assetVO=assetService.getAssetByID(assetID);
			request.setAttribute("assetVO", assetVO);
		}catch(Exception e) {
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			selectedPanel = "assetList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";			
		}
		selectedPanel = "assetList";
		return "getAssetByID";
	}

	public String findAssetUsageList(){
		try{
			if(assetUsageVO==null){
				assetUsageVO=new AssetUsageVO();
			}
			ListResult<AssetUsageVO> assetUsagelist=assetUsageService.findAssetUsageList(assetUsageVO, limit, page);
			count=assetUsagelist.getTotalCount();
			totalPage=count%limit==0? count/limit : count/limit+1;
			List<CompanyVO> companyVOs = positionService.findAllCompanys();
			if(totalPage==0){
				totalPage=1;
			}
			request.setAttribute("assetUsagelist", assetUsagelist.getList());
			request.setAttribute("companyVOs", companyVOs);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();
			selectedPanel = "assetUsageList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "assetUsageList";
		return "assetUsageList";
	}

	public String saveAssetUsage(){
		try{
			assetUsageService.SaveAssetUsage(assetUsageVO);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "保存失败："+e.getMessage();
			selectedPanel = "assetUsageList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "assetUsageList";
		return "saveAssetUsage";
	}

	public String returnAsset(){
		List<AssetUsageVO> assetUsageVOs=new ArrayList<>();
		for(int i=0;i<assetUsageVO.getUsageIDs().length;i++){
			AssetUsageVO assetUsage = assetUsageService.getAssetUsageByUsageID(assetUsageVO.getUsageIDs()[i]);
			AssetVO asset = assetService.getAssetByID(assetUsage.getAssetID());
			assetUsage.setAssetVO(asset);
			assetUsageVOs.add(assetUsage);
		}
		request.setAttribute("assetUsageVOs", assetUsageVOs);
		selectedPanel = "assetUsageList";
		return "returnAsset";
	}
	public String updateUsage(){
		try{
			assetUsageService.updateAssetUsage(assetUsageVO);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "更新失败："+e.getMessage();
			selectedPanel = "assetUsageList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "assetUsageList";
		return "updateUsage";
	}


	public String showAssetUsageList(){
		try{
			int assetID=Integer.parseInt(request.getParameter("assetID"));
			List<AssetUsageVO> assetUsagelist=assetUsageService.getAssstUsageByAssetID(assetID);
			assetVO = assetService.getAssetByID(assetID);
			assetVO.setAssetUsageVOs(assetUsagelist);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();		
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "showAssetUsageList";
	}

	public String updateAssetUsage(){
		try{
			User user = (User) request.getSession().getAttribute("user");
			if (user == null) {
				errorMessage = "您尚未登录，请先登录！";
				return "initUserAccountFailed";
			}
			String usageID = request.getParameter("usageID");
			assetUsage.setReceiveOperatorID(user.getId());
			assetUsageService.saveAssetUsage(assetUsage);
			assetService.updateAssetUsageStatus(usageID);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "保存失败："+e.toString();
			selectedPanel = "assetUsageList";
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return "error";
		}
		selectedPanel = "assetUsageList";
		return "saveAssetUsage";
	}
	
	public String selectAssetVOByAssetID(){
		try{
			int assetID=Integer.parseInt(request.getParameter("assetID"));
			List<AssetUsageVO> assetUsagelist=assetUsageService.getAssstUsageByAssetID(assetID);
			assetVO = assetService.getAssetByID(assetID);
			for(AssetUsageVO assetUsageVO:assetUsagelist){
				if(assetUsageVO.getStatus()==0){
					assetVO.setAssetUsageVO(assetUsageVO);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "查询失败："+e.getMessage();		
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
		}
		return "selectAssetVOByAssetID";
	}
	
	@Getter
	@Setter
	private AssetEntity assetEntity;
	public String updateAssetEntity(){
		try{
			assetService.updateAssetEntity(assetEntity);
		}catch(Exception e){
			e.printStackTrace();
			errorMessage = "更新失败："+e.getMessage();
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw, true));
			logger.error(sw.toString());
		}
		return "updateAssetEntity";
	}
}
