package com.tony.netty4.second;

import com.tony.netty4.common.Constants;
import com.tony.netty4.seond.SubReqClient;

public class SubClientStart {
	
	public static void main(String[] args) {
		SubReqClient client = new SubReqClient();
		client.connect(Constants.PORT, Constants.HOST);
	}

}
