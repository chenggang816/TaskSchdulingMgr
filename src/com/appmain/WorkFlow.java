package com.appmain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.manager.Client;
import com.tools.FileHelper;

public class WorkFlow {
	/*
	 * ���ÿһ��ip��ַ�������Ƿ������µģ����ǣ���Ӧstates��Ϊtrue
	 */
	public static boolean[] checkUpdateState(final List<String> ipList){
		boolean[] states = new boolean[ipList.size()];
		updateTasksJsonFile();
		String tasksStr = getTasksString();
		if(tasksStr == null) return states;
		for(String ip:ipList){
			//��taskStr���͵�����Server�������ɸ������������������״̬
			
		}
		return states;
	}
	
	private static String getTasksString(){
		File fileTasks = FileMgr.GetTaskAllJsonFile();
		if(fileTasks == null) return null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileTasks)));
			String line,tasksStr = "";
			while((line = in.readLine()) != null){
				tasksStr += line;
			}
			return tasksStr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * ����tasks.json�ļ�������һ����ʱ�ļ������ȫ��������Ϣ
	 */
	public static void updateTasksJsonFile(){
		File fileTasks = FileMgr.GetTaskAllJsonFile();
		String tasksJSONString = JSONMgr.getTasksJsonStr();
		FileHelper.WriteToFile(fileTasks, tasksJSONString);
	}
	

}
