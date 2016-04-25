package com.tony.netty4.seond;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.codec.protobuf.SubscribeReqProto;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SubReqServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(SubReqServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("wellcome to the SubReqServer....");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;

		logger.info("receive the message:{}", req);

		ctx.writeAndFlush(resp(req.getSubReqID()));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("The subServer occur error!");
		ctx.close();
	}

	private SubscribeReqProto.SubscribeReq resp(int id) {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setAddress("NanJing OK!==>" + id);
		builder.setSubReqID(id);
		builder.setProductName("product-resp==>" + id);
		builder.setUsername("tony-resp==>" + id);
		return builder.build();
	}

}
