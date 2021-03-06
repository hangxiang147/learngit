package com.zhizaolian.staff.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ShortMsgSender {
	private  static String basicUrl ;
	private  static String userName ;
	private  static String password ;
	static {
		Properties properties=PropertiesHelper.getInstance().getProperties("shortMsg");
		basicUrl=properties.getProperty("basicUrl");
		userName=properties.getProperty("userName");
		password=properties.getProperty("password");
	}
	private ShortMsgSender() {
	};

	private static ShortMsgSender shortMsgSender = null;
	private static Map<String, String> wrongMsgMap = null;

	public static synchronized ShortMsgSender getInstance() {
		if (shortMsgSender == null) {
			wrongMsgMap = new HashMap<String, String>();
			wrongMsgMap.put("-1", "uid或密码为空");
			wrongMsgMap.put("-2", "下发号码为空");
			wrongMsgMap.put("-3", "下发内容为空");
			wrongMsgMap.put("-4", "内容超长");
			wrongMsgMap.put("-5", "下发号码超长");
			wrongMsgMap.put("-6", "uid或密码不正确");
			wrongMsgMap.put("-7", "余额不足");
			wrongMsgMap.put("-8", "IP地址鉴权不对");
			wrongMsgMap.put("-9", "协议类型不对");
			wrongMsgMap.put("-99", "系统异常");
			shortMsgSender=new ShortMsgSender();
		}
		return shortMsgSender;
	}

	public void send(String telephone, String content) {
		List<String> sendList = new ArrayList<String>();
		sendList.add(telephone);
		send(sendList, content, null);

	}

	public void send(List<String> telephones, String content) {
		send(telephones, content, null);

	}

	public void send(List<String> telephones, String content, Date sendTime) {
		//拼接 请求信息
		ShortMsg shortMsg = ShortMsg.builder().uid(userName).pw(password).tm(new Date()).mb(telephones).content(content)
				.dm(sendTime).build();
		String result = null;
		try {
			result = sendPost(basicUrl, shortMsg.getUrl());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		//非正常返回 处理
		if (!"0".equals(result)) {
			String errorMsg = wrongMsgMap.get(result);
			if (StringUtils.isNotBlank(errorMsg)) {
				throw new RuntimeException(errorMsg);
			}
		}
	}

	public static String sendPost(String url,Set<Pair<String, String>> set)  {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			StringBuilder sb = new StringBuilder();
			if(CollectionUtils.isNotEmpty(set)){
				for (Pair<String, String> pair : set) {
					sb.append(pair.getLeft()+"="+URLEncoder.encode(pair.getRight(),"utf-8")+"&");
				}
			}
			sb.deleteCharAt(sb.length()-1);
			out = new PrintWriter(conn.getOutputStream());
			out.write(sb.toString());
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
				throw new RuntimeException(ex);
			}
		}
		return result;
	}
	public static void main(String[] args) {
	}
}
