package com.manager;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.manager.msg.MsgHandler;
import com.manager.msg.MsgHandlerFactory;
import com.manager.msg.ReplyMsgHandler;
import com.manager.msg.UnresolvedMsgHandler;

public class SocketHelper {
	public static final int port = 8899;
	
	class Monitor{
		public Monitor(int count){
			this.count = count;
		}
		private int count;
		synchronized public void reduceCount(int count){
			this.count -= count;
		}
		synchronized public int getCount(){
			System.out.println(this.count);
			return this.count;
		}
	}
	/*
	 * ����ips���ÿ��ip��Ӧport�˿ڵķ����Ƿ���
	 * ���ص�Map��keyΪip,valueΪtrue��ʾ�ɹ�����
	 */
	private static Map<String, Boolean> tryCommunicate(List<String> ips,final int port){
		final Map<String,Boolean> map = new HashMap<String,Boolean>();
		final Monitor monitor = new SocketHelper().new Monitor(ips.size());
		final Set<String> ipSet = new HashSet<String>(ips);
		for(final String ip:ips){
			new Thread(){
				public void run(){
					Client client = null;
					try {
						client = new Client(ip,port);
						this.setName(ip);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String strReply = client.sendHello();
					if(strReply == null){
						map.put(ip, false);
					}else{
						MsgHandler handler = MsgHandlerFactory.getMsgHandler(strReply);
						if(handler instanceof ReplyMsgHandler){
							map.put(ip, true);
						}else{
							map.put(ip, false);
						}
						
						System.out.println(strReply);
					}
					monitor.reduceCount(1);
					ipSet.remove(ip);
					System.out.print(this.getName() + ":");
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
		for(String ip:ipSet){
			System.out.println("û���˳����߳�:" + ip);
		}
		return map;
	}

	/*
	 * ʹ�ñ����е�port�˿�
	 */
	public static Map<String, Boolean> tryCommunicate(List<String> ips){
		return tryCommunicate(ips,port);
	}
}
