package com.nettools;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.socket.Client;



public class SocketHelper {
	class Monitor{
		public Monitor(int count){
			this.count = count;
		}
		private int count;
		synchronized public void reduceCount(int count){
			this.count -= count;
		}
		public int getCount(){
			System.out.println(this.count);
			return this.count;
		}
	}
	/*
	 * 测试ips里的每个ip对应port端口的服务是否开启
	 * 返回的Map的key为ip,value为true表示成功开启
	 */
	public static Map<String, Boolean> tryCommunicate(List<String> ips,final int port){
		final Map<String,Boolean> map = new HashMap<String,Boolean>();
		final Monitor monitor = new SocketHelper().new Monitor(ips.size());
		for(final String ip:ips){
			new Thread(){
				public void run(){
					Client client = null;
					try {
						client = new Client(ip,port);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String strReply = client.sendHello();
					if(strReply == null){
						map.put(ip, false);
					}else{
						map.put(ip, true);
						System.out.println(strReply);
					}
					monitor.reduceCount(1);
					if(monitor.getCount() <= 0){
						synchronized(monitor){
							monitor.notify();
						}
					}
				}
			}.start();
		}
		try {
			synchronized(monitor){
				monitor.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return map;
	}
}
