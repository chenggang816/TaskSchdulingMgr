package com.appmain;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static com.tools.JSONHelper.toJSONString;

public class JSONMgr {
	/*
	 * ��ȡ��������������Ϣ��Json����
	 */
	public static JSONObject getTasksJsonObj(){
		File tasksDir = FileMgr.GetTaskDir();
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
	 * ��ȡ��������������Ϣ��Json�ַ���
	 */
	public static String getTasksJsonStr(){
		return toJSONString(getTasksJsonObj());
	}
	
	public static String getReplyMsgJsonStr(String taskStateInfoStr){
		JSONObject obj = new JSONObject();
		obj.put("type", "TASK_STATE");
		obj.put("content", taskStateInfoStr);
		return toJSONString(obj);
	}
	
	/*
	 * ��ȡHello��Ϣ��Json�ַ���
	 */
	public static String getHelloMsgJsonStr(){
		JSONObject json = new JSONObject();
		json.put("type", "HELLO");
		json.put("content", null);
		return toJSONString(json);
	}
}
