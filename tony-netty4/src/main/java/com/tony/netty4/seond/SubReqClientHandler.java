package com.tony.netty4.seond;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.codec.protobuf.SubscribeReqProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqClientHandler extends ChannelInboundHandlerAdapter {
private static final Logger logger = LoggerFactory.getLogger(SubReqClientHandler.class);

@Override
public void channelActive(ChannelHandlerContext ctx) throws Exception {
	logger.info("active the server success!");
	for (int i = 0; i < 10; i++) {
		ctx.writeAndFlush(subReq(i));
	}
	logger.info("send message finish!!!!!!!!!!!!!!!!!1");
	
}

@Override
public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
	SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq)msg;
	logger.info("Client receive the message:{}",req.toString());
	
	
}

@Override
public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	ctx.flush();
}

@Override
public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	logger.warn("client occur error!");
	ctx.close();
}


private SubscribeReqProto.SubscribeReq subReq(int i){
	SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
	builder.setAddress("address:"+i);
	builder.setProductName("productName:"+i);
	builder.setSubReqID(i);
	builder.setUsername("username"+i);
	return builder.build();
}

}
