package com.tony.netty4.protocol;

import java.io.Serializable;

public final class NettyMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2151263767187779086L;
	private Header header;
	private Object body;
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody(Object body) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "NettyMessage [header=" + header + ", body=" + body + "]";
	}
	
	
	
	
	
	

}
