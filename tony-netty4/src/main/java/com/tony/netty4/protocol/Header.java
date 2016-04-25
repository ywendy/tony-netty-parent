package com.tony.netty4.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yaojian
 *
 */
public final class Header implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8475803855136351620L;
	/**
	 * Netty 消息校验码 <br>
	 * 0xabef+ 主版本号(1个字节)+ 次版本号(1个字节)
	 */
	private int crcCode = 0xabef0101;
	/* 消息长度(包含消息头+消息体). */
	private int length;
	/* 集群节点内全局唯一 */
	private long sessionID;
	/**
	 * 0:业务请求消息<br>
	 * 1:业务响应消息 <br>
	 * 2:业务ONE WAY 消息(既是请求消息又是响应消息)<br>
	 * 3:握手请求消息<br>
	 * 4:握手应答消息<br>
	 * 5:心跳请求消息<br>
	 * 6:心跳应答消息<br>
	 */
	private byte type;
	/* 消息优先级(0~255). */
	private byte priority;

	private Map<String, Object> attachment = new HashMap<>();

	public int getCrcCode() {
		return crcCode;
	}

	public void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getSessionID() {
		return sessionID;
	}

	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "Header [crcCode=" + crcCode + ", length=" + length + ", sessionID=" + sessionID + ", type=" + type
				+ ", priority=" + priority + ", attachment=" + attachment + "]";
	}

}
