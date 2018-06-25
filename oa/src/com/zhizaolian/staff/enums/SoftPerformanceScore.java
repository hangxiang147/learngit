package com.zhizaolian.staff.enums;

public enum SoftPerformanceScore {
	开发人员(1,0.4),组长(2,0.05), 测试(3,0.2), 需求(4,0.05), 项目经理(5,0.25),实施(6,0.05),问题单(7,100);
	private final int index;
	private final double percent;
	SoftPerformanceScore(int index,double percent) {
		this.index=index;
		this.percent = percent;
	}
	public double getPercent() {
		return percent;
	}
	public int getIndex() {
		return index;
	}
	public static  double getPercentByIndex(int index){
		for(SoftPerformanceScore softPerformanceScore:SoftPerformanceScore.values()){
			if(softPerformanceScore.index==index){
				return softPerformanceScore.percent;
			}
		}
		return 0;
	}
	public static SoftPerformanceScore getSoftPerformanceByIndex(int index){
		for(SoftPerformanceScore softPerformanceScore:SoftPerformanceScore.values()){
			if(softPerformanceScore.index==index){
				return softPerformanceScore;
			}
		}
		return null;
	}
	public static String getpercentDescribtion(){
		String strs="";
		for(SoftPerformanceScore softPerformanceScore:SoftPerformanceScore.values()){
			strs+=softPerformanceScore.name()+":"+softPerformanceScore.getPercent()+"%"+";";
		}
		return strs;
	}
}
