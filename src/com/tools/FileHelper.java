package com.tools;

import java.io.File;
import java.io.IOException;

public class FileHelper {
	public static File GetDataDir(){
		File dirData = new File("data");
		if(!dirData.exists()) dirData.mkdir();
		if(!dirData.isDirectory()) return null;
		return dirData;
	}
	public static File GetTasksJsonFile(){
		File dirData = GetDataDir();
		if(dirData == null) return null;
		for(File fileTasks:dirData.listFiles()){
			if(fileTasks.getName().equals("tasks.json")){
				return fileTasks;
			}
		}
		File fileNew = new File("data/tasks.json");
		try {
			fileNew.createNewFile();
			return fileNew;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
