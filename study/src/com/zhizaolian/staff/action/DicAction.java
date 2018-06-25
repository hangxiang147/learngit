package com.zhizaolian.staff.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zhizaolian.staff.service.DicService;

import lombok.Setter;

/**
*@author Zhouk
*@date 2017年4月25日 下午2:07:30
*@describtion  字典类
**/
public class DicAction  extends BaseAction {

	private static final long serialVersionUID = 1L;
	@Setter
	private String formKey;
	@Setter
	private String inputKey;
	@Setter
	private String key;
	@Autowired
	private DicService dicService;
	
	public void getContentByKey(){
		List<Object> list= new ArrayList<>();
		if(StringUtils.isNotBlank(key)){
			if(StringUtils.isBlank(inputKey)||StringUtils.isBlank(formKey)){
				throw new IllegalArgumentException();
			}else{
				list=dicService.getPossibleContentByKey(formKey, inputKey, key);
			}
		}
		printByJson(list);
	}
	
	public void recordContent(){
		if(StringUtils.isNotBlank(formKey)&&StringUtils.isNotBlank(inputKey)&&StringUtils.isNotBlank(key)){
			dicService.recordRate(formKey, inputKey, key);
		}
	}
	 
}
