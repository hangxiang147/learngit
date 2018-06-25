package com.zhizaolian.staff.enums;
/**
 * 软件上传/下载 状态
 * @author wjp
 *
 */
public enum SoftLoadTypeEnum {
	
		UPLOAD(1),
		
		DOWNLOAD(2);
		
		private final int value;
		
		SoftLoadTypeEnum(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static SoftLoadTypeEnum valueOf(int value) {
			for (SoftLoadTypeEnum val : values()) {
				if (val.getValue() == value) {
					return val;
				}
			}
			return null;
		}
	}

