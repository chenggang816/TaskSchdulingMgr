package com.nettools;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.socket.Client;

public class SocketHelper {
	/*
	 * ����ips���ÿ��ip��Ӧport�˿ڵķ����Ƿ���
	 * ���ص�Map��keyΪip,valueΪtrue��ʾ�ɹ�����
	 */
	public static Map<String, Boolean> tryCommunicate(List<String> ips,int port){
		Map<String,Boolean> map = new HashMap<String,Boolean>();
		for(String ip:ips){
			Client client = null;
			try {
				client = new Client(ip,port);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(client.sendHello() == null){
				map.put(ip, false);
			}else{
				map.put(ip, true);
			}
		}
		return map;
	}
}
