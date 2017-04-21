package com.manager.msg;

import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;

import com.appmain.JSONMgr;

public class MsgCreator {
	public static String createTaskInfoMsg(String taskStateInfoStr){
		return JSONMgr.getTaskInfoMsgJsonStr(taskStateInfoStr);
	}

	public static String createHelloMsg(String ip){
		return JSONMgr.getHelloMsgJsonStr(ip);
	}
}
