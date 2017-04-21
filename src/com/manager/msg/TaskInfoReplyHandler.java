package com.manager.msg;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.tools.JSONHelper;

public class TaskInfoReplyHandler extends MsgHandler{
	private JSONObject msg;
	public TaskInfoReplyHandler(JSONObject msg) {
		this.msg = msg;
	}
	
	@Override
	public String handle() {
		if(msg == null) return "false";
		boolean isUpdated = true;
		//将任务更新状态信息写入到文件中
		String ip = msg.get("ip").toString();
		String port = msg.get("port").toString();
		JSONObject taskNameVersion = (JSONObject)msg.get("content");
		for(Object taskName:taskNameVersion.keySet()){
			String taskVersion = (String)taskNameVersion.get(taskName);
			String localTaskVersion = getLocalTaskVersion();
//			if(taskVersion)
		}
		//需要更新，返回false
		return String.valueOf(isUpdated);
	}

	private String getLocalTaskVersion() {
		return null;
	}

}
