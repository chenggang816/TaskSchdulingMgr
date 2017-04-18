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
	 * ��textд���ļ���
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
	 * ���ļ��ж�ȡȫ������
	 */
	public static String ReadAllFromFile(File file){
		throw new UnsupportedOperationException();
	}
}
