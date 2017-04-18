package com.appmain;

import java.io.File;
import java.io.IOException;

public class FileMgr {

	/*
	 * ��ȡdata�ļ��е�File����
	 */
	public static File GetDataDir(){
		File dirData = new File("data");
		if(!dirData.exists()) dirData.mkdir();
		if(!dirData.isDirectory()) return null;
		return dirData;
	}

	/*
	 * ��ȡtasks.json��File����
	 */
	public static File GetTaskAllJsonFile(){
		String fileName = "TaskAll.json";
		File dirData = GetDataDir();
		if(dirData == null) return null;
		for(File fileTasks:dirData.listFiles()){
			if(fileTasks.getName().equals(fileName)){
				return fileTasks;
			}
		}
		File fileNew = new File(dirData, fileName);
		try {
			fileNew.createNewFile();
			return fileNew;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * ��ȡdata/task�ļ��е�File����
	 */
	public static File GetTaskDir(){
		File dirData = GetDataDir();
		if(dirData == null) return null;
		File dirTask = new File(dirData,"task");
		if(!dirTask.exists()) dirTask.mkdir();
		return dirTask;
	}

}
