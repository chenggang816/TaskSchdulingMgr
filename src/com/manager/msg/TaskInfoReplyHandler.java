package com.manager.msg;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.appmain.FileMgr;
import com.tools.FileHelper;
import com.tools.JSONHelper;

public class TaskInfoReplyHandler extends MsgHandler{
	private JSONObject msg;
	public TaskInfoReplyHandler(JSONObject msg) {
		this.msg = msg;
	}
	
	@Override
	public String handle() {
		if(msg == null) return "false";
		//表示客户端任务是否是最新的，true表示是最新的
		boolean isUpdated = true;
		//将任务更新状态信息写入到文件中
		String ip = msg.get("ip").toString();
		String port = msg.get("port").toString();
		JSONObject taskNameVersion = (JSONObject)msg.get("content");
		
		System.out.println("任务更新信息：");
		System.out.println(JSONHelper.toJSONString(msg));
		
		File file = FileMgr.getClientFile(ip,port);
		JSONHelper.saveJSONFile(taskNameVersion, file);
		for(Object taskName:taskNameVersion.keySet()){
			String clientTaskVersion = (String)taskNameVersion.get(taskName);
			if(clientTaskVersion.equalsIgnoreCase("NotExist") || clientTaskVersion.equalsIgnoreCase("ConfigException")){
				isUpdated = false;
				break;
			}
			String localTaskVersion = getLocalTaskVersion(taskName.toString());
			if(VersionCompare(localTaskVersion, clientTaskVersion) > 0){
				isUpdated = false;
				break;
			}
		}
		//需要更新，返回false
		return String.valueOf(isUpdated);
	}

	private String getLocalTaskVersion(String taskName) {
		return null;
	}
	
	/*
	 * 比较version1是否比version2新，如果更新，则返回1，
	 * 如果相同则返回0
	 * 如果更旧，则返回-1
	 */
	private int VersionCompare(String version1,String version2){
		boolean newer = true;
		String[] va1 = version1.split(".");
		String[] va2 = version2.split(".");
		int len = Math.min(va1.length, va2.length);
		for(int i = 0; i < len; i++){
			try{
				int v1 = Integer.parseInt(va1[i]);
				int v2 = Integer.parseInt(va2[i]);
				if(v1 > v2) 
					return 1;
				if(v1 < v2)
					return -1;
			}catch(RuntimeException e){
				e.printStackTrace();
				return -2;
			}
			
		}
		//跳出循环，说明前面的版本号一致
		if(va1.length > va2.length)
			return 1;
		if(va1.length < va2.length)
			return -1;
		
		//运行到这里，说明两个版本号完全一致
		return 0;
	}

}
