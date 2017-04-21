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
		//��ʾ�ͻ��������Ƿ������µģ�true��ʾ�����µ�
		boolean isUpdated = true;
		//���������״̬��Ϣд�뵽�ļ���
		String ip = msg.get("ip").toString();
		String port = msg.get("port").toString();
		JSONObject taskNameVersion = (JSONObject)msg.get("content");
		
		System.out.println("���������Ϣ��");
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
		//��Ҫ���£�����false
		return String.valueOf(isUpdated);
	}

	private String getLocalTaskVersion(String taskName) {
		return null;
	}
	
	/*
	 * �Ƚ�version1�Ƿ��version2�£�������£��򷵻�1��
	 * �����ͬ�򷵻�0
	 * ������ɣ��򷵻�-1
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
		//����ѭ����˵��ǰ��İ汾��һ��
		if(va1.length > va2.length)
			return 1;
		if(va1.length < va2.length)
			return -1;
		
		//���е����˵�������汾����ȫһ��
		return 0;
	}

}
