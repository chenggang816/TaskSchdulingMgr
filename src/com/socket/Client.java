package com.socket;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	Socket client = null;
	String host = null;
	int port;
	public Client(String host,int port) throws UnknownHostException, IOException {
		this.host = host;
		this.port = port;

	}  
	public boolean connect(){
		//�����˽�������  
		try {
			client = new Socket(host, port);
			return true;
		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}  
	}
	public void close(){
		if(client == null)
			return;
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String send(int flag,String msg){
		if(!connect())
			return null;
		Writer writer = null;
		Reader reader = null;
		try {
			writer = new OutputStreamWriter(client.getOutputStream());
			writer.write(flag);
			writer.write(msg);
			writer.write("eof");  
			writer.flush();//д���Ҫ�ǵ�flush 
			
			//д���Ժ���ж�����  
			reader = new InputStreamReader(client.getInputStream());  
			char chars[] = new char[64];  
			int len;  
			StringBuilder sb = new StringBuilder();  
			String temp;  
			int index;  
			while ((len=reader.read(chars)) != -1) {  
				temp = new String(chars, 0, len);  
				if ((index = temp.indexOf("eof")) != -1) {//����eofʱ�ͽ�������  
					sb.append(temp.substring(0, index));  
					break;  
				}  
				sb.append(temp);  
			}  
			return sb.toString();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}  finally{
			try {
				writer.close();
				reader.close(); 
				close();
			} catch (Exception e) {
				return null;
			} 
		}
		return null;
	}
	public String sendHello(){
		return this.send(0,"HELLO");
	}
	public String sendMsg(String msg){
		return this.send(1, msg);
	}
}
