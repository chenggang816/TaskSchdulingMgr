package com.manager.msg;

import java.io.IOException;
import java.io.StringWriter;

import org.json.simple.JSONObject;

import com.appmain.JSONMgr;

public class MsgCreator {
	public static String createTaskStateMsg(String taskStateInfoStr){
		return JSONMgr.getReplyMsgJsonStr(taskStateInfoStr);
	}

	public static String createHelloMsg(){
		return JSONMgr.getHelloMsgJsonStr();
	}
}
