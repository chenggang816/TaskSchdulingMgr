package com.appmain;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
			JSONObject theTask= new JSONObject();
			theTask.put("taskname", task.getName());
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
	
	public static String getTaskInfoMsgJsonStr(String taskInfoStr){
		JSONObject obj = new JSONObject();
		obj.put("type", "TASK_INFO");
		obj.put("content", taskInfoStr);
		return toJSONString(obj);
	}
	
	/*
	 * 获取Hello消息的Json字符串
	 */
	public static String getHelloMsgJsonStr(String ip){
		JSONObject json = new JSONObject();
		json.put("ip", ip);
		json.put("type", "HELLO");
		json.put("content", null);
		return toJSONString(json);
	}
}
