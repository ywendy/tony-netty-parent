package com.tony.netty4.codec.protobuf;

import org.junit.Assert;
import org.junit.Test;

import com.google.protobuf.InvalidProtocolBufferException;


public class SubscribeReqTest {

	@Test
	public void testSerilizable() throws InvalidProtocolBufferException {
		SubscribeReqProto.SubscribeReq req = createSubcribeReq();
		SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
		Assert.assertEquals(req, req2);
	}

	private byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray();
	}

	private SubscribeReqProto.SubscribeReq decode(byte[] req) throws InvalidProtocolBufferException {
		return SubscribeReqProto.SubscribeReq.parseFrom(req);
	}

	private SubscribeReqProto.SubscribeReq createSubcribeReq() {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(1);
		builder.setUsername("tony");
		builder.setProductName("rebate");
		builder.setAddress("BeiJing Tiantan");
		return builder.build();
	}

}
