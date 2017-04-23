package com.appmain;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.tools.JSONHelper;

public class TaskInfo {
	private String name;
	private String path;
	private String success;
	private String version;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public void parseJSON(String jsonString){
		JSONObject obj = JSONHelper.parse(jsonString);
		if(obj == null) return;
		
		setPath(obj.get("path").toString());
		if(obj.get("success") != null)
			setSuccess(obj.get("success").toString());
		setVersion(obj.get("version").toString());
	}
	/*
	 * �Ƚ�version1�Ƿ��version2�£�������£��򷵻�1��
	 * �����ͬ�򷵻�0
	 * ������ɣ��򷵻�-1
	 */
	public static int VersionCompare(String version1,String version2){
		boolean newer = true;
		String[] va1 = version1.split("\\.");
		String[] va2 = version2.split("\\.");
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