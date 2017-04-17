package com.appmain;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.tools.NetHelper;
import com.tools.SocketHelper;

public class FrameMain extends JFrame{
	JButton btnGainAllIp,btnGainHostNames;
	JButton btnTestService,btnTaskCheck,btnTaskUpdate;
	JPanel panelTop;
	JTable table;
	DefaultTableModel model;

	public FrameMain() {
		initialize();
		WorkFlow.updateTasksJsonFile();
	}
	
	
	
 	private void initialize() {
		final List<String> ipList = new ArrayList<String>();
		panelTop = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		btnGainAllIp = new JButton("��ȡ����������IP");
		btnGainAllIp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				ipList.clear();
				ipList.add(NetHelper.getLocalHostIp());
				ipList.addAll(NetHelper.getIPs());
				Collections.sort(ipList);
				model.setRowCount(0);
				for(int i=0;i<ipList.size();i++){
					String ip = ipList.get(i);
					model.addRow(new Object[]{i + 1,ip,"δ֪","δ֪","δ֪"});
				}
			}
		});
		panelTop.add(btnGainAllIp);
		
		btnGainHostNames = new JButton("��ȡ������");
		panelTop.add(btnGainHostNames);
		btnGainHostNames.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ipList.size() <= 0){
					return;
				}
				new Thread(){
					public void run(){
						try {
							btnGainHostNames.setText("���ڻ�ȡ������");
							btnGainHostNames.setEnabled(false);
							Map<String, String> mapIpHostNames = NetHelper.getHostnames(ipList);
							for(int i=0;i<ipList.size();i++){
								String ip = ipList.get(i);
								model.setValueAt(mapIpHostNames.get(ip), i, 2);
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}finally{
							btnGainHostNames.setText("��ȡ������");
							btnGainHostNames.setEnabled(true);
						}
						
					}
				}.start();
			}
		});
		
		btnTestService = new JButton("���Է����Ƿ���");
		panelTop.add(btnTestService);
		btnTestService.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ipList.size() <= 0){
					return;
				}
				new Thread(){
					public void run(){
						String strBtnTxt = btnTestService.getText();
						btnTestService.setText("���ڲ��Է���");
						btnTestService.setEnabled(false);
						Map<String, Boolean> map;
						map = SocketHelper.tryCommunicate(ipList);
						for(int i = 0;i < ipList.size();i++){
							String r = map.get(ipList.get(i)) ? "��":"��";
							model.setValueAt(r, i, 3);
						}
						btnTestService.setText(strBtnTxt);
						btnTestService.setEnabled(true);
						System.out.println("����������");
					}
				}.start();
			}
		});
		
		btnTaskCheck = new JButton("����������");
		panelTop.add(btnTaskCheck);
		btnTaskCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ipList.isEmpty()) return;
				boolean[] b = WorkFlow.checkUpdateState(ipList);
				for(int i = 0; i < ipList.size(); i++){
					boolean serviceOpen = model.getValueAt(i, 3).toString().equals("��");
					model.setValueAt(serviceOpen?(b[i]?"������":"��Ҫ����"):"", i, 4);
				}
			}
		});
		
		btnTaskUpdate = new JButton("�������");
		panelTop.add(btnTaskUpdate);
		btnTaskUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(ipList.isEmpty()) return;
			}
		});
		
		model = new DefaultTableModel(new String[]{"���","IP","������","�����Ƿ���","�������"},0){
			public boolean isCellEditable(int r,int c){
				return false;
			}
		};
		
		table = new JTable(model);
		table.setRowHeight(45);		
		//���䷽ʽ����  
        DefaultTableCellRenderer d = new DefaultTableCellRenderer();            
        //���ñ��Ԫ��Ķ��뷽ʽΪ���ж��뷽ʽ  
        d.setHorizontalAlignment(JLabel.CENTER);  
        for(int i = 0; i< table.getColumnCount();i++)  
        {  
            TableColumn col = table.getColumn(table.getColumnName(i));  
            col.setCellRenderer(d);  
        }  
		
		this.add(panelTop,BorderLayout.NORTH);
		this.add(new JScrollPane(table),BorderLayout.CENTER);
		
		this.setTitle("������ȹ���");
		this.setLocation(150,100);
		this.setSize(1000,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		this.setVisible(true);
	}
}
