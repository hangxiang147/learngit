package com.zhizaolian.staff.service;

import java.util.List;

/**
*@author Zhouk
*@date 2017年4月25日 上午9:01:02
*@describtion  
*字典 service 主要负责 
*1根据form的Name input的Name 记录词语出现的次数
*2根据form的Name input的Name 已经输入的部分数据 取出前10个符合条件的	
* 排序方式  先取 key% 前十个
* 		   假如不到十个 再取 like  %key% and not like key%  不到几个取几个
**/
public interface DicService {
	 int MAX_EFFECTIVE_NUMBER=10;
	 void recordRate(String formType,String inputKey,String content);
	 List<Object> getPossibleContentByKey(String formType,String inputKey,String key);
	 String getTopContentByKey(String formType,String inputKey);
}
