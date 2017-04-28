package com.manager.net;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.manager.main.IpPortPair;
import com.manager.msg.MsgCreator;
import com.manager.msg.MsgHandler;
import com.manager.msg.MsgHandlerFactory;
import com.manager.msg.ReplyMsgHandler;
import com.manager.msg.UnresolvedMsgHandler;

public class SocketHelper {
	
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
	 * ���ص�Map��keyΪip,value��Ϊnull��ʾ�ɹ�����
	 */
	public static Map<IpPortPair, Client> tryCommunicate(List<IpPortPair> ippList){
		final Map<IpPortPair,Client> map = new HashMap<IpPortPair,Client>();
		final Monitor monitor = new SocketHelper().new Monitor(ippList.size());
		final Set<IpPortPair> ipSet = new HashSet<IpPortPair>(ippList);
		for(final IpPortPair ipp:ippList){
			new Thread(){
				public void run(){
					Client client = null;
					try {
						client = new Client(ipp.getIp(),ipp.getPort());
						this.setName(ipp.getIp());
					} catch (IOException e) {
						e.printStackTrace();
					}
					String strReply = client.send(MsgCreator.createHelloMsg(ipp.getIp()));
					if(strReply == null){
						map.put(ipp, null);
					}else{
						MsgHandler handler = MsgHandlerFactory.getMsgHandler(strReply);
						if(handler instanceof ReplyMsgHandler){
							map.put(ipp, client);
						}else{
							map.put(ipp, null);
						}
						
						System.out.println(strReply);
					}
					monitor.reduceCount(1);
					ipSet.remove(ipp);
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
		for(IpPortPair ipp:ipSet){
			System.out.println("û���˳����߳�:" + ipp);
		}
		return map;
	}
}
