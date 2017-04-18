package com.appmain;

import java.io.File;
import java.io.IOException;

public class FileMgr {

	/*
	 * 获取data文件夹的File对象
	 */
	public static File GetDataDir(){
		File dirData = new File("data");
		if(!dirData.exists()) dirData.mkdir();
		if(!dirData.isDirectory()) return null;
		return dirData;
	}

	/*
	 * 获取tasks.json的File对象
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
	 * 获取data/task文件夹的File对象
	 */
	public static File GetTaskDir(){
		File dirData = GetDataDir();
		if(dirData == null) return null;
		File dirTask = new File(dirData,"task");
		if(!dirTask.exists()) dirTask.mkdir();
		return dirTask;
	}

}
