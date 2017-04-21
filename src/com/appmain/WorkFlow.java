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
	final DefaultTableModel model = new DefaultTableModel(new String[]{"序号","IP","端口号","主机名","服务是否开启","任务更新"},0){
		public boolean isCellEditable(int r,int c){
			return true;
		}
	};
	/*
	 * 初始化块，初始化一些公共操作
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
			model.addRow(new Object[]{i + 1,ipp.getIp(),ipp.getPort(),"未知","未知","未知"});
		}
	}
	
	/*
	 * 更新tasks.json文件，这是一个临时文件，存放全部任务信息
	 */
	public void updateTasksJsonFile(){
		File fileTasks = FileMgr.getTaskAllJsonFile();
		String tasksJSONString = JSONMgr.getTasksJsonStr();
		FileHelper.WriteToFile(fileTasks, tasksJSONString);
	}
	
	/*
	 * 获取所有ip
	 */
	public void doGainAllIp(){
		ipPortPairList.clear();
		ipPortPairList.add(new IpPortPair(NetHelper.getLocalHostIp()));
		ipPortPairList.addAll(IpPortPair.toIpPortList(NetHelper.getIPs()));
		Collections.sort(ipPortPairList);
		updateModel();
	}
	
	
	/*
	 * 获取主机名
	 */
	public void doGainHostNames() throws InterruptedException{
		Map<String, String> mapIpHostNames = NetHelper.getHostnames(IpPortPair.toIpList(ipPortPairList));
		for(int i=0;i<ipPortPairList.size();i++){
			String ip = ipPortPairList.get(i).getIp();
			model.setValueAt(mapIpHostNames.get(ip), i, 3);
		}
	}
	
	/*
	 * 测试服务是否开启
	 */
	public void doTestService(){
		serviceStateMap = SocketHelper.tryCommunicate(ipPortPairList);
		for(int i = 0;i < ipPortPairList.size();i++){
			String r = serviceStateMap.get(ipPortPairList.get(i)) == null ? "否" : "是";
			model.setValueAt(r, i, 4);
		}
	}
	
	/*
	 * 检查任务更新
	 */
	public void doCheckTaskUpdate(){
		if(ipPortPairList.isEmpty()) return;
		boolean[] b = checkUpdateState(ipPortPairList);
		for(int i = 0; i < ipPortPairList.size(); i++){
			boolean serviceOpen = model.getValueAt(i, 4).toString().equals("是");
			model.setValueAt(serviceOpen?(b[i]?"已最新":"需要更新"):"无服务", i, 5);
		}
	}
	
	/*
	 * 检查每一个主机上的任务是否是最新的，若是，对应states项为true
	 */
	private boolean[] checkUpdateState(final List<IpPortPair> ippList){
		boolean[] states = new boolean[ippList.size()];
		//更新文件
		updateTasksJsonFile();
		String tasksStr = FileHelper.ReadAllFromFile(FileMgr.getTaskAllJsonFile());
		if(tasksStr == null) throw new RuntimeException("无法获取本地任务信息");
		for(int i = 0; i < ippList.size(); i++){
			IpPortPair ipp = ippList.get(i);
			//将taskStr发送到各个Server主机，由各个主机返回任务更新状态
			if(serviceStateMap == null){
				System.out.println("服务开启状态未知，请先获取服务开启状态");
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
	 * 任务更新
	 */
	public void doTaskUpdate(){
		
	}
	
	/*
	 * 从配置文件中加载主机
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
	 * 编辑配置文件
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
	 * 查看配置文件
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
