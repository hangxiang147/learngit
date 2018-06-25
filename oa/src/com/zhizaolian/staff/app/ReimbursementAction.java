package com.zhizaolian.staff.app;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.service.ReimbursementService;
import com.zhizaolian.staff.vo.AdvanceVo;
import com.zhizaolian.staff.vo.BankAccountVO;
import com.zhizaolian.staff.vo.ReimbursementVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

public class ReimbursementAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	private final static int SUCCESS = 1;
	private final static int FAIL = 0;
	@Autowired
	private ReimbursementService reimbursementService;

	private Map<String, Object> simpleReturnMap(boolean isSuccess, String msg) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("result", isSuccess ? SUCCESS : FAIL);
		returnMap.put("message", msg);
		return returnMap; 
	}

	/**
	 * 手机接口：根据领款人id 获取 开户详情
	 */
	public void getPayeeDetail() {
		String userID = this.request.getParameter("userID");
		if (StringUtils.isEmpty(userID)) {
			printByJson(simpleReturnMap(false, "userId为空"));
			return;
		}
		BankAccountVO bankAccountVO = null;
		try {
			bankAccountVO = reimbursementService.getBankAccountByUserID(userID);
		} catch (Exception e) {
			printByJson(simpleReturnMap(false, e.getMessage()));
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return;
		}
		Map<String, Object> returnMap = simpleReturnMap(true, "success");
		returnMap.put("BankAccountVO", bankAccountVO);
		printByJson(returnMap);
	}
	/**
	 * 手机接口:开始费用申报工作流
	 */
	public void startReimbursement() {
		String reimbursementVOString = this.request.getParameter("reimbursementVO");
		String attachmentDetail=this.request.getParameter("attachmentDetail");
		File[] file = null;
		BASE64Decoder decoder = new BASE64Decoder();
		try{
			if(StringUtils.isNotEmpty(attachmentDetail)){
				JSONArray ja=JSONArray.fromObject(attachmentDetail);
				file=new File[ja.size()];
				for (int i = 0; i < ja.size(); i++) {
					String picStr=this.request.getParameter("attachmentContent"+i);
					byte[] attachmentBytes = decoder.decodeBuffer(picStr);
					@SuppressWarnings("unchecked")
					List<String> list =(List<String>) ja.get(i);
					String fileName=list.get(0);
					File currentFile = new File(fileName);
					FileOutputStream fstream;
					fstream = new FileOutputStream(currentFile);
					BufferedOutputStream stream = new BufferedOutputStream(fstream);
					stream.write(attachmentBytes);
					if(stream!=null)
						stream.close();
					file[i]=currentFile;
				}
			}
		}catch (IOException e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			throw new RuntimeException("附件录入错误:"+e.getMessage());
		}
		
		String extraMsg = this.request.getParameter("extraMsg");
		if (StringUtils.isEmpty(reimbursementVOString) || StringUtils.isEmpty(extraMsg)) {
			printByJson(simpleReturnMap(false, "数据缺失"));
			return;
		}
		try {
			ReimbursementVO reimbursementVO = (ReimbursementVO) JSONObject
					.toBean(JSONObject.fromObject(reimbursementVOString), ReimbursementVO.class);
			JSONArray jsonArray = JSONArray.fromObject(extraMsg);
			if (jsonArray == null || jsonArray.isEmpty()) {
				throw new RuntimeException("用途不能为空");
			}
			List<String> purpose_list = new ArrayList<String>();
			List<Double> money_list = new ArrayList<Double>();
			for (Object object : jsonArray) {
				@SuppressWarnings("unchecked")
				Map<String, Object> currentMap = (Map<String, Object>) object;
				purpose_list.add((String) currentMap.get("usage"));
				Object amout=currentMap.get("amount");
				if(amout instanceof java.lang.Integer){
					money_list.add(Double.valueOf(amout+""));
				}else if(amout instanceof java.lang.Double){
					money_list.add((Double)amout);
				}else{
					throw new IllegalArgumentException("amount 类型错误");
				}
			}
			reimbursementVO.setUsage(purpose_list.toArray(new String[] {}));
			reimbursementVO.setAmount(money_list.toArray(new Double[] {}));
			reimbursementService.startReimbursement(reimbursementVO,file,attachmentDetail);
		} catch (Exception e) {
			e.printStackTrace();
			printByJson(simpleReturnMap(false, e.getMessage()));
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return;
		}
		printByJson(simpleReturnMap(true, "success"));
	}
	
	
	public void startAdvance() {
		String reimbursementVOString = this.request.getParameter("advanceVo");
		String attachmentDetail=this.request.getParameter("attachmentDetail");
		File[] file = null;
		BASE64Decoder decoder = new BASE64Decoder();
		try{
			if(StringUtils.isNotEmpty(attachmentDetail)){
				JSONArray ja=JSONArray.fromObject(attachmentDetail);
				file=new File[ja.size()];
				for (int i = 0; i < ja.size(); i++) {
					String picStr=this.request.getParameter("attachmentContent"+i);
					byte[] attachmentBytes = decoder.decodeBuffer(picStr);
					@SuppressWarnings("unchecked")
					List<String> list =(List<String>) ja.get(i);
					String fileName=list.get(0);
					File currentFile = new File(fileName);
					FileOutputStream fstream;
					fstream = new FileOutputStream(currentFile);
					BufferedOutputStream stream = new BufferedOutputStream(fstream);
					stream.write(attachmentBytes);
					if(stream!=null)
						stream.close();
					file[i]=currentFile;
				}
			}
		}catch (IOException e) {
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			throw new RuntimeException("附件录入错误:"+e.getMessage());
		}
		
		String extraMsg = this.request.getParameter("extraMsg");
		if (StringUtils.isEmpty(reimbursementVOString) || StringUtils.isEmpty(extraMsg)) {
			printByJson(simpleReturnMap(false, "数据缺失"));
			return;
		}
		try {
			AdvanceVo reimbursementVO = (AdvanceVo) JSONObject
					.toBean(JSONObject.fromObject(reimbursementVOString), AdvanceVo.class);
			JSONArray jsonArray = JSONArray.fromObject(extraMsg);
			if (jsonArray == null || jsonArray.isEmpty()) {
				throw new RuntimeException("用途不能为空");
			}
			List<String> purpose_list = new ArrayList<String>();
			List<Double> money_list = new ArrayList<Double>();
			for (Object object : jsonArray) {
				@SuppressWarnings("unchecked")
				Map<String, Object> currentMap = (Map<String, Object>) object;
				purpose_list.add((String) currentMap.get("usage"));
				Object amout=currentMap.get("amount");
				if(amout instanceof java.lang.Integer){
					money_list.add(Double.valueOf(amout+""));
				}else if(amout instanceof java.lang.Double){
					money_list.add((Double)amout);
				}else{
					throw new IllegalArgumentException("amount 类型错误");
				}
			}
			reimbursementVO.setUsage(purpose_list.toArray(new String[] {}));
			reimbursementVO.setAmount(money_list.toArray(new Double[] {}));
			reimbursementService.startAdvance(reimbursementVO,file,attachmentDetail);
			
		} catch (Exception e) {
			e.printStackTrace();
			printByJson(simpleReturnMap(false, e.getMessage()));
			StringWriter sw = new StringWriter(); 
			e.printStackTrace(new PrintWriter(sw, true)); 
			logger.error(sw.toString());
			return;
		}
		printByJson(simpleReturnMap(true, "success"));
	}
}
