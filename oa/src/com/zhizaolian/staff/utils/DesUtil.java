package com.zhizaolian.staff.utils;

import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesUtil {
	public static String encrypt(String plainText){
		try{
			return new DES().encrypt(plainText); 
		}catch(Exception e){
			return null;
		}
	}

	public static String encrypt(String plainText, String key) {  
		try {  
			return new DES(key).encrypt(plainText);  
		} catch (Exception e) {  
			return null;  
		}  
	}

	public static String decrypt(String plainText) {  
		try {  
			return new DES().decrypt(plainText);  
		} catch (Exception e) {  
			return null;  
		}  
	}

	public static String decrypt(String plainText, String key) {  
		try {  
			return new DES(key).decrypt(plainText);  
		} catch (Exception e) {  
			return null;  
		}  
	}
	/**
	 * 加密
	 * @param datasource byte[]
	 * @param password String
	 * @return byte[]
	 */
	public static  byte[] encrypt(byte[] data, String key) {            
		try{
			SecureRandom paramSpec = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			//key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey,paramSpec);
			return cipher.doFinal(data);
		}catch(Throwable e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 解密
	 * @param src byte[]
	 * @param password String
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		SecureRandom paramSpec = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(key.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		//key的长度不能够小于8位字节
		Key secretKey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey,paramSpec);
		return cipher.doFinal(data);
	}
	public static void main(String[] args) {
		System.out.println(DesUtil.decrypt("ef3cb6ba8de7e96f2017a788c0976def"));
	}
}
class DES {
	private static String strDefaultKey = "abcDEF123";
	private Cipher encryptCipher = null;
	private Cipher decryptCipher = null;
	public DES() throws Exception {  
		this(strDefaultKey);  
	} 
	public DES(String strKey) throws Exception {  
		Security.addProvider(new com.sun.crypto.provider.SunJCE());  
		Key key = getKey(strKey.getBytes());  
		encryptCipher = Cipher.getInstance("DES");  
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);  
		decryptCipher = Cipher.getInstance("DES");  
		decryptCipher.init(Cipher.DECRYPT_MODE, key);  
	}
	public String encrypt(String strIn) throws Exception {  
		return byteArr2HexStr(encrypt(strIn.getBytes()));  
	}
	public byte[] encrypt(byte[] arrB) throws Exception {  
		return encryptCipher.doFinal(arrB);  
	} 
	public String decrypt(String strIn) throws Exception {  
		return new String(decrypt(hexStr2ByteArr(strIn)));  
	}
	public byte[] decrypt(byte[] arrB) throws Exception {  
		return decryptCipher.doFinal(arrB);  
	}
	private Key getKey(byte[] arrBTmp) throws Exception {  
		byte[] arrB = new byte[8];  
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {  
			arrB[i] = arrBTmp[i];  
		}  
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");  
		return key;  
	}
	public static String byteArr2HexStr(byte[] arrB) throws Exception {  
		int iLen = arrB.length;  
		StringBuffer sb = new StringBuffer(iLen * 2);  
		for (int i = 0; i < iLen; i++) {  
			int intTmp = arrB[i];  
			while (intTmp < 0) {  
				intTmp = intTmp + 256;  
			}  
			if (intTmp < 16) {  
				sb.append("0");  
			}  
			sb.append(Integer.toString(intTmp, 16));  
		}  
		return sb.toString();  
	}
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {  
		byte[] arrB = strIn.getBytes();  
		int iLen = arrB.length;  
		byte[] arrOut = new byte[iLen / 2];  
		for (int i = 0; i < iLen; i = i + 2) {  
			String strTmp = new String(arrB, i, 2);  
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);  
		}  
		return arrOut;  
	}
}