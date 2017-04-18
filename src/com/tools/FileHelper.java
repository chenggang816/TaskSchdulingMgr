package com.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.naming.ldap.UnsolicitedNotificationEvent;

public class FileHelper {
	/*
	 * 将text写入文件中
	 */
	public static void WriteToFile(File file,String text){
		WriteToFile(file, text,"utf-8");
	}
	public static void WriteToFile(File file,String text,String encode){
		if(file == null || text == null) return;
		try(BufferedWriter out = 
				new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),encode))) {
			out.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void WriteToFile(String path,String text){
		if(path == null || text == null) return;
		WriteToFile(new File(path), text);
	}
	/*
	 * 从文件中读取全部内容
	 */
	public static String ReadAllFromFile(File file){
		throw new UnsupportedOperationException();
	}
}
