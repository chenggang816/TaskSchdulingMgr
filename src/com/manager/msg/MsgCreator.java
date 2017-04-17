package com.manager.msg;

import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;

public class MsgCreator {
	public static String createTaskStateMsg(String tasksStr){
		JSONObject obj = new JSONObject();
		obj.put("type", "TASK_STATE");
		obj.put("content", tasksStr);
		StringWriter out = new StringWriter();
		try {
			obj.writeJSONString(out);
			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
