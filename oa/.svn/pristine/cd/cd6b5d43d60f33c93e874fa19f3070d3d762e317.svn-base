package com.zhizaolian.staff.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {
	
	public static void inputstreamtofile(InputStream ins,File file) throws Exception{
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[1024*8];
		while ((bytesRead = ins.read(buffer, 0, 1024*8)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}
}
