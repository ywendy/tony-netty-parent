package com.tony.netty4.second;

import com.tony.netty4.common.Constants;
import com.tony.netty4.seond.SubReqServer;

public class SubServerStart {
	
	public static void main(String[] args) {
		SubReqServer server = new SubReqServer();
		server.start(Constants.PORT);
	}

}
