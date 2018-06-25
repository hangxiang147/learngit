package com.zhizaolian.staff.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SoftScoreVo {
	//用户名
	private String name;
	//某个时间段内的总得分
	private String socre;
	//每条的描述 (暂定为 :被分配到的任务的名称 )
	private String[] details;
	//每条的得分 (可能是扣分项)
	private String[] scores;
	//每条的时间
	private String[] times;
	//假如对扣除分数 存在 疑义  可能需要进行修改的审核
	private String[] ids;
}
