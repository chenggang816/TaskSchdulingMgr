package com.appmain;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import com.nettools.NetHelper;
import com.nettools.SocketHelper;

public class FrameMain extends JFrame{
	JButton btnGainAllIp,btnGainHostNames;
	JButton btnTestService;
	JPanel panelTop;
	JTable table;
	DefaultTableModel model;

	public FrameMain() {
		initialize();
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
				Map<String, Boolean> map;
				map = SocketHelper.tryCommunicate(ipList, 8000);
				for(int i = 0;i < ipList.size();i++){
					String r = map.get(ipList.get(i)) ? "��":"��";
					model.setValueAt(r, i, 3);
				}
			}
		});
		
		model = new DefaultTableModel(new String[]{"���","IP","������","�����Ƿ���","����ִ�����"},0){
			public boolean isCellEditable(int r,int c){
				return false;
			}
		};
		
		table = new JTable(model);
		table.setRowHeight(45);		
		//���䷽ʽ����  
        DefaultTableCellRenderer d = new DefaultTableCellRenderer();            
        //���ñ���Ԫ��Ķ��뷽ʽΪ���ж��뷽ʽ  
        d.setHorizontalAlignment(JLabel.CENTER);  
        for(int i = 0; i< table.getColumnCount();i++)  
        {  
            TableColumn col = table.getColumn(table.getColumnName(i));  
            col.setCellRenderer(d);  
        }  
		
		this.add(panelTop,BorderLayout.NORTH);
		this.add(new JScrollPane(table),BorderLayout.CENTER);
		
		this.setTitle("������ȹ���");
		this.setLocation(200,100);
		this.setSize(800,500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		this.setVisible(true);
	}
}