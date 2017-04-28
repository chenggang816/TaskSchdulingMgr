package com.manager.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.manager.main.WorkFlowTestTask;

public class FrameTestTask extends JFrame {
	
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel jp3 = new JPanel();
	JLabel lb = new JLabel("选择主机与任务，点击执行任务按钮进行测试");
	JComboBox cb1,cb2;
	JButton btnTest = new JButton("执行任务");
	WorkFlowTestTask workFlow = new WorkFlowTestTask();
	
	public FrameTestTask() {
		jp1.setLayout(new FlowLayout());
		jp1.add(lb);
		
		cb1 = new JComboBox(workFlow.getComboboxModel("HOST"));
		cb2 = new JComboBox(workFlow.getComboboxModel("TASK"));
		jp2.setLayout(new FlowLayout());
		jp2.add(cb1);
		jp2.add(cb2);
		
		jp3.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jp3.add(btnTest);
		
		add(jp1,BorderLayout.NORTH);
		add(jp2,BorderLayout.CENTER);
		add(jp3,BorderLayout.SOUTH);
		
		setTitle("任务测试");
		setSize(600,300);
		setLocation(400,300);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
