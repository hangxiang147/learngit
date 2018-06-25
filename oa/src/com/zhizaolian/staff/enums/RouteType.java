package com.zhizaolian.staff.enums;
public  enum RouteType{
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