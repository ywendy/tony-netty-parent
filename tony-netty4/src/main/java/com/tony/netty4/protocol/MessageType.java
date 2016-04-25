package com.tony.netty4.protocol;

public enum MessageType {

	LOGIN_REQ((byte) 3, "握手请求消息"), LOGIN_RESP((byte) 4, "握手响应消息"),HEARTBEAT_RESP((byte)6,"心跳响应消息"),HEARTBEAT_REQ((byte)5,"心跳请求消息");
	private byte value;
	private String desc;

	private MessageType(byte value, String desc) {
		this.value = value;
		this.desc = desc;
	}

	public byte vlaue() {
		return this.value;
	}

	public String desc() {
		return this.desc;
	}

}
