package com.zhizaolian.staff.enums;
/**
 * 软件上传/下载 状态
 * @author wjp
 *
 */
public enum DownloadCenterTypeEnum {
	
		UPLOAD(1),
		
		DOWNLOAD(2),
	
		UPDATE(3),
		
		DELETE(4);
		
		private final int value;
		
		DownloadCenterTypeEnum(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static DownloadCenterTypeEnum valueOf(int value) {
			for (DownloadCenterTypeEnum val : values()) {
				if (val.getValue() == value) {
					return val;
				}
			}
			return null;
		}
	}

