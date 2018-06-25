package com.zhizaolian.staff.vo;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CommonSubjectVo extends BaseVO {

	private static final long serialVersionUID = 1L;

	private Integer id;
	/**
	 * 流程中的路线（并行 还是串行）
	 */
	private String route;
	/**
	 * 暂时只有 告知 和 审批 两种 方式
	 */
	private String type;
	private String title_;
	private String content;
	private String userIds;
	private String userNames;public static enum RouteType{
		并行(1),串行(2);
		private final int index;
		RouteType(int index) {
			this.index = index;
		}
		public int getIndex() {
			return index;
		}
		public static int getIndex(String name){
			RouteType[] routeTypes=RouteType.values();
			for(RouteType routeType: routeTypes){
				if(routeType.name().equals(name)){
					return routeType.getIndex();
				}
			}
			return -1;
		}
		
	}
	@Override
	public void createFormFields(List<FormField> fields) {
		fields.add(getFormField("type", "类别", type));
		fields.add(getFormField("title", "标题", title_));
		fields.add(getFormField("route", "流通方式", route));
		fields.add(getFormField("userNames","处理人员",userNames));
		fields.add(getFormField("content","内容",content));
	}
	
	
}
