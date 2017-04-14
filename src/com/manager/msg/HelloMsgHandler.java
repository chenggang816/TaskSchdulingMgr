package com.manager.msg;

public class HelloMsgHandler extends MsgHandler{

	@Override
	public String handle() {
		return null;//收到客户端发过来的HELLO消息之后，返回null表示不再发回消息
	}

}
