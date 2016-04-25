package com.tony.netty4.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(HeartBeatRespHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		logger.info("客户端数量：{}",LoginAuthRespHandler.nodeCheck.size());
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.vlaue()) {
			logger.info("Receive client heart beat message:---->{}", message);
			NettyMessage heartBeat = buildHeatBeat();
			logger.info("Send heart beat response message to client : ---->{}", heartBeat);
			ctx.writeAndFlush(heartBeat);

		} else {
			ctx.fireChannelRead(msg);
		}
	}

	
	
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("client channel actived..................");
	}




	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("发生错误",cause);
	}




	private NettyMessage buildHeatBeat() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.HEARTBEAT_RESP.vlaue());
		message.setHeader(header);
		return message;
	}

}
