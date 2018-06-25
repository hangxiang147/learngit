package com.zhizaolian.staff.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectByteArrTransformer {

	public static byte[] toByteArray(Object obj) throws IOException {
		byte[] bytes = null;
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos);) {
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
		}
		return bytes; 
	}

	public static Object toObject(byte[] arr)
			throws IOException, ClassNotFoundException {
		Object obj = null;
		try (ByteArrayInputStream bis = new ByteArrayInputStream(arr);
				ObjectInputStream ois = new ObjectInputStream(bis);) {
			obj = ois.readObject();
		}
		return obj;
	}
	public static byte[] inputStreamToByte(InputStream is) throws IOException { 
		ByteArrayOutputStream bAOutputStream = new ByteArrayOutputStream(); 
		int ch; 
		while((ch = is.read() ) != -1){ 
			bAOutputStream.write(ch); 
		} 
		byte data [] =bAOutputStream.toByteArray(); 
		bAOutputStream.close(); 
		return data; 
	} 
}
