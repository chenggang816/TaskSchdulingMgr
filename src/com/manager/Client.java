package com.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;

import com.appmain.JSONMgr;
import com.manager.msg.MsgCreator;

public class Client {
	Socket socket = null;
	String host = null;
	int port;
	public Client(String host,int port) throws UnknownHostException, IOException {
		this.host = host;
		this.port = port;

	}  
	public boolean connect(){
		//与服务端建立连接  
		try {
			socket = new Socket(host, port);
			return true;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}  
	}
	public void close(){
		if(socket == null)
			return;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String send(String strMsg){
		if(!connect())
			return null;
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(socket.getOutputStream(),true);
			out.println(strMsg);
			 
			//写完以后进行读操作  
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line ;
			while((line= in.readLine()) != null){
				System.out.println("Server:" + line);
				return line;
			}	
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("异常主机ip:" + host);
		}  finally{
			try {
				out.close();
				in.close(); 
				close();
			} catch (Exception e) {
				return null;
			} 
		}
		return null;
	}
}
