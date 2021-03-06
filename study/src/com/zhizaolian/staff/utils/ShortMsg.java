package com.zhizaolian.staff.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShortMsg {
	private final static SimpleDateFormat SHORT_MSG_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	//private final static String CONTENT_CHARSET = "utf-8";
	private String uid;
	/**
	 * pw 为 md5(pw+tm)
	 */
	private String pw;
	/**
	 * 当前时间
	 */
	private Date tm;
	/**
	 * 手机号 最大 10000 个号码
	 */
	private List<String> mb;
	/**
	 * 短信内容
	 */
	private String content;
	/**
	 * 定时时间
	 */
	private Date dm;

	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Set<Pair<String,String>> getUrl() throws UnsupportedEncodingException {
		Set<Pair<String, String>> querySet=new HashSet<>();
		querySet.add(new ImmutablePair<String, String>("ua", uid));
		querySet.add(new ImmutablePair<String, String>("pw", MD5(pw + SHORT_MSG_FORMAT.format(tm))));
		StringBuffer sb = new StringBuffer();
		for (int i = 0, length = mb.size(); i < length; i++) {
			sb.append((mb.get(i)+"").trim()).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		querySet.add(new ImmutablePair<String, String>("mb", sb.toString()));
		//querySet.add(new ImmutablePair<String, String>("ms", new String(content.getBytes(), CONTENT_CHARSET)));
		querySet.add(new ImmutablePair<String, String>("ms", content));
		querySet.add(new ImmutablePair<String, String>("tm",SHORT_MSG_FORMAT.format(tm)));
		if(dm!=null)
			querySet.add(new ImmutablePair<String, String>("dm", SHORT_MSG_FORMAT.format(dm)));
		return querySet;
	}

}
