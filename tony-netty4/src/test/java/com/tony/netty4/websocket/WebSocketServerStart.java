package com.tony.netty4.websocket;

import com.tony.netty4.common.Constants;

public class WebSocketServerStart {

	public static void main(String[] args) {
		new WebSocketServer().start(Constants.PORT);
	}

}
