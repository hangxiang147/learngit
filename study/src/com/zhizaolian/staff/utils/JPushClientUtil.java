package com.zhizaolian.staff.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class JPushClientUtil {

	private static final String APP_KEY = "1adb6aeca02d5214dc497951";
	private static final String MASTER_SECRET = "bf042e280f2e833b48faf51e";
	private static JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
	
	/**
	 * 发送给所有用户
	 * @param notification_title 通知标题
	 * @return 0推送失败，1推送成功
	 */
	public static int sendToAll(String notification_title) {
		int result = 0;
		try {
			PushPayload pushPayload = buildPushObject_android_and_ios(notification_title);
			PushResult pushResult = jpushClient.sendPush(pushPayload);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIConnectionException e) {
			throw new RuntimeException("Connection error, should retry later:"+e.getMessage());

	    } catch (APIRequestException e) {
	    	throw new RuntimeException("Error Message: "+e.getErrorMessage());
	    }
		
		return result;
	}
	
	/**
	 * 发送通知给指定标识设备
	 * @param registrationID 设备标识
	 * @param notification_title 通知标题
	 * @return 0推送失败，1推送成功
	 */
	public static int sendToRegistrationID(String registrationID, String notification_title) {
		int result = 0;
		try { 
			PushPayload pushPayload = buildPushObject_all_alias_alert(registrationID, notification_title);
			PushResult pushResult = jpushClient.sendPush(pushPayload);
			if (pushResult.getResponseCode() == 200) {
				result = 1;
			}
		} catch (APIConnectionException e) {
			throw new RuntimeException("Connection error, should retry later:"+e.getMessage());

	    } catch (APIRequestException e) {
	    	throw new RuntimeException("Error Message: "+e.getErrorMessage());
	    }
		
		return result;
	}
	
	public static PushPayload buildPushObject_android_and_ios(String notification_title) {
		return PushPayload.newBuilder()
                .setPlatform(Platform.all())//设置接受的平台
                .setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(Notification.newBuilder()
						.addPlatformNotification(AndroidNotification.newBuilder() //安卓平台推送
								.setAlert(notification_title)                //通知的内容
								.build())
						.addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(notification_title)
                                .setBadge(+1)
                                .setSound("default")
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .build();
	}
	
	public static PushPayload buildPushObject_all_alias_alert(String registrationID, String notification_title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(registrationID))
                .setNotification(Notification.newBuilder()
                		.addPlatformNotification(AndroidNotification.newBuilder() //安卓平台推送
								.setAlert(notification_title)                //通知的内容
								.build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(notification_title)
                                .setBadge(+1)
                                .setSound("default")
                                .build())
                        .build())
                .setOptions(Options.newBuilder().setApnsProduction(true).build())
                .build();
    }
	
//	public static int sendToAll(String notification_title) {
//		int result = 0;
//		try {
//			PushPayload pushPayload = buildPushObject_android_and_ios(notification_title);
//			PushResult pushResult = jpushClient.sendPush(pushPayload);
//			if (pushResult.getResponseCode() == 200) {
//				result = 1;
//			}
//		} catch (APIConnectionException e) {
//			throw new RuntimeException("Connection error, should retry later:"+e.getMessage());
//
//	    } catch (APIRequestException e) {
//	    	throw new RuntimeException("Error Message: "+e.getErrorMessage());
//	    }
//		return result;
//	}
//	
//	/**
//	 * 发送通知给指定标识设备
//	 * @param registrationID 设备标识
//	 * @param notification_title 通知标题
//	 * @return 0推送失败，1推送成功
//	 */
//	public static int sendToRegistrationID(String registrationID, String notification_title) {
//		int result = 0;
//		try {
//			PushPayload pushPayload = buildPushObject_all_alias_alert(registrationID, notification_title);
//			PushResult pushResult = jpushClient.sendPush(pushPayload);
//			if (pushResult.getResponseCode() == 200) {
//				result = 1;
//			}
//		} catch (APIConnectionException e) {
//			throw new RuntimeException("Connection error, should retry later:"+e.getMessage());
//
//	    } catch (APIRequestException e) {
//	    	throw new RuntimeException("Error Message: "+e.getErrorMessage());
//	    }
//		return result;
//	}
//	
//	public static PushPayload buildPushObject_android_and_ios(String notification_title) {
//		return PushPayload.newBuilder()
//                .setPlatform(Platform.all())//设置接受的平台
//                .setAudience(Audience.all())//Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
//                .setNotification(Notification.newBuilder()
//						.addPlatformNotification(AndroidNotification.newBuilder() //安卓平台推送
//								.setAlert(notification_title)                //通知的内容
//								.build())
//						.addPlatformNotification(IosNotification.newBuilder()
//                                .setAlert(notification_title)
//                                .setBadge(+1)
//                                .setSound("default")
//                                .build())
//                        .build())
//                .setOptions(Options.newBuilder().setApnsProduction(true).build())
//                .build();
//		return null;
//	}
//	
//	public static PushPayload buildPushObject_all_alias_alert(String registrationID, String notification_title) {
//        return PushPayload.newBuilder()
//                .setPlatform(Platform.all())
//                .setAudience(Audience.alias(registrationID))
//                .setNotification(Notification.newBuilder()
//                		.addPlatformNotification(AndroidNotification.newBuilder() //安卓平台推送
//								.setAlert(notification_title)                //通知的内容
//								.build())
//                        .addPlatformNotification(IosNotification.newBuilder()
//                                .setAlert(notification_title)
//                                .setBadge(+1)
//                                .setSound("default")
//                                .build())
//                        .build())
//                .setOptions(Options.newBuilder().setApnsProduction(true).build())
//                .build();
//		return null;
//    }
	
}
