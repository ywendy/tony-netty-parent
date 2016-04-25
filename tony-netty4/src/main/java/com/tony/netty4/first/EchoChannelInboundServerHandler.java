package com.tony.netty4.first;

import java.net.InetAddress;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.common.Constants;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EchoChannelInboundServerHandler extends SimpleChannelInboundHandler<String> {

	private static final Logger logger = LoggerFactory.getLogger(EchoChannelInboundServerHandler.class);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		logger.info("the server receive message :{}", msg);
		// 接收消息，处理完成写消息给客户端.
		ctx.writeAndFlush("receive your message :" + msg + "  and processed ,response you:" + new Date().toString()
				+ Constants.DELIMITER);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("remote address [{}] actived !", ctx.channel().remoteAddress());
		logger.info("wellcome to the echo server");
		ctx.writeAndFlush(
				"wellcome to the echo server [" + InetAddress.getLocalHost().getHostName() + "]" + Constants.DELIMITER);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("the server occur error", cause.getMessage());
	}

}
