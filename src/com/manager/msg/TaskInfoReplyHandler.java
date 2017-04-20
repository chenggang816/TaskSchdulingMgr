package com.manager.msg;

public class TaskInfoReplyHandler extends MsgHandler{
	private String content;
	public TaskInfoReplyHandler(String content) {
		this.content = content;
	}
	
	@Override
	public String handle() {
		return content;
	}

}
