package com.zhizaolian.staff.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClientUtil {

	public static String doPost(String jsonInfo,String addUrl){
		String responseResult="";
		//String addUrl="http://localhost:8080/PLD/hello";//请求地址
		URL url;
		try { 
			//发送请求，输出数据	
			url = new URL(addUrl);		
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();//创建连接
	
	        connection.setDoInput(true);//设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
	        connection.setDoOutput(true);//设置是否从httpUrlConnection读入，默认情况下是true;
	        connection.setRequestMethod("POST");//设定请求的方法为"POST"，默认是GET
	        connection.setUseCaches(false);//Post 请求不能使用缓存
	        connection.setInstanceFollowRedirects(true);//设置本次连接是否自动处理重定向
	        connection.setRequestProperty("Content-Type","application/x-javascript;charset=UTF-8");//设定传送的内容类型及格式
	        connection.connect();//打开连接
	
	        OutputStream out = connection.getOutputStream();//获取输出流  
	        
	        byte[] data = jsonInfo.getBytes("UTF-8");//将字符串转成字节数组,格式为utf8     
	        out.write(data);//输出字节流
	        out.flush();//刷新输出剩余字节
	        out.close();//关闭流
	        
	        //接受响应，输入结果
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//获取输入流
	        String lines;
	        StringBuffer sbf = new StringBuffer();
	        //读取响应结果
	        while((lines = reader.readLine()) != null){
	            sbf.append(lines);
	        }        
	        responseResult=sbf.toString();
	        
	        reader.close();//关闭输入流   
	        connection.disconnect();//断开连接
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseResult;
	}
	public static String doGet(String addUrl){
		String responseResult="";
		URL url;
		try { 
			//发送请求，输出数据	
			url = new URL(addUrl);		
	        HttpURLConnection connection = (HttpURLConnection)url.openConnection();//创建连接
	        connection.setRequestMethod("GET");//设定请求的方法为"POST"，默认是GET
	        connection.setDoOutput(false);
	        connection.setInstanceFollowRedirects(true);//设置本次连接是否自动处理重定向
	        connection.setRequestProperty("Content-Type","application/x-javascript;charset=UTF-8");//设定传送的内容类型及格式
	        connection.connect();//打开连接
	        //接受响应，输入结果
	        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));//获取输入流
	        String lines;
	        StringBuffer sbf = new StringBuffer();
	        //读取响应结果
	        while((lines = reader.readLine()) != null){
	            sbf.append(lines);
	        }        
	        responseResult=sbf.toString();
	        
	        reader.close();//关闭输入流   
	        connection.disconnect();//断开连接
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responseResult;
	}
}
