package com.manager.main;

import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.manager.net.Client;

public class WorkFlowTestTask {
	WorkFlowMain main = WorkFlowMain.getWorkFlow();
	Map<IpPortPair, Client> mapIppClient;
	private DefaultComboBoxModel<String> hostModel = new DefaultComboBoxModel<String>();
	private DefaultComboBoxModel<String> taskModel = new DefaultComboBoxModel<String>();
	public WorkFlowTestTask() {
		mapIppClient = main.getMapIppClient();
	}
	public ComboBoxModel<String> getComboboxModel(String category){
		switch(category){
		case "HOST":
			if(mapIppClient != null){
				hostModel.addListDataListener(new ListDataListener() {
					
					@Override
					public void intervalRemoved(ListDataEvent e) {
						// TODO Auto-generated method stub
						System.out.println("intervalRemoved");
					}
					
					@Override
					public void intervalAdded(ListDataEvent e) {
						// TODO Auto-generated method stub
						System.out.println("intervalAdded");
					}
					
					@Override
					public void contentsChanged(ListDataEvent e) {
						// TODO Auto-generated method stub
						System.out.println("contentsChanged");
					}
				});
				for(IpPortPair ipp:mapIppClient.keySet()){
					if(mapIppClient.get(ipp) != null){
						hostModel.addElement(ipp.toString());
					}
				}
			}
			return hostModel;
		case "TASK":
			return taskModel;
		default:
			return null;
		}
	}
}
