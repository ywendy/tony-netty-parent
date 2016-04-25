package com.tony.netty4.first;

import com.tony.netty4.common.Constants;

public class EchoServerStart {

	public static void main(String[] args) {
		EchoServer server = new EchoServer();
		server.bind(Constants.PORT);
	}

}
