package com.appmain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.manager.Client;
import com.manager.SocketHelper;
import com.manager.msg.MsgCreator;
import com.manager.msg.MsgHandler;
import com.manager.msg.MsgHandlerFactory;
import com.tools.FileHelper;
import com.tools.NetHelper;

public class WorkFlow {
	List<IpPortPair> ipPortPairList = new ArrayList<IpPortPair>();
	Map<IpPortPair, Client> serviceStateMap;
	final DefaultTableModel model = new DefaultTableModel(new String[]{"���","IP","�˿ں�","������","�����Ƿ���","�������"},0){
		public boolean isCellEditable(int r,int c){
			return true;
		}
	};
	/*
	 * ��ʼ���飬��ʼ��һЩ��������
	 */
	{
		updateTasksJsonFile();
	}
	
	public boolean canWork(){
		return ipPortPairList != null && ipPortPairList.size() > 0;
	}
	
	public TableModel getTabelModel(){
		return model;
	}

	
	private void updateModel(){
		model.setRowCount(0);
		for(int i=0;i<ipPortPairList.size();i++){
			IpPortPair ipp = ipPortPairList.get(i);
			model.addRow(new Object[]{i + 1,ipp.getIp(),ipp.getPort(),"δ֪","δ֪","δ֪"});
		}
	}
	
	/*
	 * ����tasks.json�ļ�������һ����ʱ�ļ������ȫ��������Ϣ
	 */
	public void updateTasksJsonFile(){
		File fileTasks = FileMgr.getTaskAllJsonFile();
		String tasksJSONString = JSONMgr.getTasksJsonStr();
		FileHelper.WriteToFile(fileTasks, tasksJSONString);
	}
	
	/*
	 * ��ȡ����ip
	 */
	public void doGainAllIp(){
		ipPortPairList.clear();
		ipPortPairList.add(new IpPortPair(NetHelper.getLocalHostIp()));
		ipPortPairList.addAll(IpPortPair.toIpPortList(NetHelper.getIPs()));
		Collections.sort(ipPortPairList);
		updateModel();
	}
	
	
	/*
	 * ��ȡ������
	 */
	public void doGainHostNames() throws InterruptedException{
		Map<String, String> mapIpHostNames = NetHelper.getHostnames(IpPortPair.toIpList(ipPortPairList));
		for(int i=0;i<ipPortPairList.size();i++){
			String ip = ipPortPairList.get(i).getIp();
			model.setValueAt(mapIpHostNames.get(ip), i, 3);
		}
	}
	
	/*
	 * ���Է����Ƿ���
	 */
	public void doTestService(){
		serviceStateMap = SocketHelper.tryCommunicate(ipPortPairList);
		for(int i = 0;i < ipPortPairList.size();i++){
			String r = serviceStateMap.get(ipPortPairList.get(i)) == null ? "��" : "��";
			model.setValueAt(r, i, 4);
		}
	}
	
	/*
	 * ����������
	 */
	public void doCheckTaskUpdate(){
		if(ipPortPairList.isEmpty()) return;
		boolean[] b = checkUpdateState(ipPortPairList);
		for(int i = 0; i < ipPortPairList.size(); i++){
			boolean serviceOpen = model.getValueAt(i, 4).toString().equals("��");
			model.setValueAt(serviceOpen?(b[i]?"������":"��Ҫ����"):"�޷���", i, 5);
		}
	}
	
	/*
	 * ���ÿһ�������ϵ������Ƿ������µģ����ǣ���Ӧstates��Ϊtrue
	 */
	private boolean[] checkUpdateState(final List<IpPortPair> ippList){
		boolean[] states = new boolean[ippList.size()];
		//�����ļ�
		updateTasksJsonFile();
		String tasksStr = FileHelper.ReadAllFromFile(FileMgr.getTaskAllJsonFile());
		if(tasksStr == null) throw new RuntimeException("�޷���ȡ����������Ϣ");
		for(int i = 0; i < ippList.size(); i++){
			IpPortPair ipp = ippList.get(i);
			//��taskStr���͵�����Server�������ɸ������������������״̬
			if(serviceStateMap == null){
				System.out.println("������״̬δ֪�����Ȼ�ȡ������״̬");
				return states;
			}
			Client client = serviceStateMap.get(ipp);
			if(client != null){
				String taskInfoReply = client.send(MsgCreator.createTaskInfoMsg(tasksStr));
				MsgHandler handler = MsgHandlerFactory.getMsgHandler(taskInfoReply);
				states[i] = Boolean.parseBoolean(handler.handle());
			}
		}
		return states;
	}
	
	/*
	 * �������
	 */
	public void doTaskUpdate(){
		
	}
	
	/*
	 * �������ļ��м�������
	 */
	public void doLoadHostsFromConfig(){
		String jsonStr = FileHelper.ReadAllFromFile(FileMgr.getConfigFile());
		if(jsonStr == null) return;
		JSONParser parser = new JSONParser();
		try {
			JSONObject json = (JSONObject)parser.parse(jsonStr);
			JSONObject configs = (JSONObject) json.get("configs");
			JSONArray hosts = (JSONArray)configs.get("hosts");
			ipPortPairList.clear();
			for(Object o:hosts){
				JSONObject host = (JSONObject)o;
				ipPortPairList.add(new IpPortPair(host.get("ip").toString(),((Long)host.get("port")).intValue()));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Collections.sort(ipPortPairList);
		updateModel();
	}
	
	/*
	 * �༭�����ļ�
	 */
	public void doEditConfigFile(){
		try {
			Runtime runtime=Runtime.getRuntime();  
			String[] commandArgs={"explorer.exe",FileMgr.getConfigFile().getAbsolutePath()};  
			Process process=runtime.exec(commandArgs);  
			int exitcode=process.waitFor();  
			System.out.println("finish:"+exitcode);  
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
	/*
	 * �鿴�����ļ�
	 */
	public void doViewConfigFile(){
		try {
			Runtime runtime=Runtime.getRuntime();  
			String[] commandArgs={"explorer.exe",FileMgr.getConfigDir().getAbsolutePath()};  
			Process process=runtime.exec(commandArgs);  
			int exitcode=process.waitFor();  
			System.out.println("finish:"+exitcode);  
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}
}
