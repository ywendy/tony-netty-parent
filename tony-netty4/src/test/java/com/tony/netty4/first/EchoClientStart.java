package com.tony.netty4.first;

import com.tony.netty4.common.Constants;

public class EchoClientStart {

	public static void main(String[] args) {
		EchoClient client = new EchoClient();
		client.connect(Constants.PORT, Constants.HOST);
	}

}
