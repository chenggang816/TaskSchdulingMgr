package com.appmain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.manager.Client;
import com.tools.FileHelper;

public class WorkFlow {
	/*
	 * 检查每一个ip地址的任务是否是最新的，若是，对应states项为true
	 */
	public static boolean[] checkUpdateState(final List<String> ipList){
		boolean[] states = new boolean[ipList.size()];
		updateTasksJsonFile();
		String tasksStr = getTasksString();
		if(tasksStr == null) return states;
		for(String ip:ipList){
			//将taskStr发送到各个Server主机，由各个主机返回任务更新状态
			
		}
		return states;
	}
	
	private static String getTasksString(){
		File fileTasks = FileHelper.GetTasksJsonFile();
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
	
	public static void updateTasksJsonFile(){
		File fileTasks = FileHelper.GetTasksJsonFile();
		
	}
}
