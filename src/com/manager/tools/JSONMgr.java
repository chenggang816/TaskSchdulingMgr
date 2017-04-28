package com.manager.tools;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.manager.main.TaskInfo;
import com.tools.FileHelper;
import com.tools.JSONHelper;

import static com.tools.JSONHelper.toJSONString;

public class JSONMgr {
	/*
	 * 获取包含所有任务信息的Json对象
	 */
	public static JSONObject getTasksJsonObj(){
		File tasksDir = FileMgr.getTaskDir();
		File[] tasks = tasksDir.listFiles();
		JSONArray tasksJsonArray = new JSONArray();
		for(File task:tasks){
			if(task.isFile()) continue;
			
			File conf = FileMgr.getTaskConfigFile(task);
			TaskInfo taskInfo = new TaskInfo();
			taskInfo.parseJSON(FileHelper.ReadAllFromFile(conf));
			
			JSONObject theTask= new JSONObject();
			theTask.put("taskname", task.getName());
			theTask.put("version", taskInfo.getVersion());
			tasksJsonArray.add(theTask);
		}
		
		JSONObject tasksInfo = new JSONObject();
		tasksInfo.put("tasks", tasksJsonArray);
		return tasksInfo;
	}

	/*
	 * 获取包含所有任务信息的Json字符串
	 */
	public static String getTasksJsonStr(){
		return toJSONString(getTasksJsonObj());
	}
}
