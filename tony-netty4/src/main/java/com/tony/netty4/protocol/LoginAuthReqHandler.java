package com.tony.netty4.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(LoginAuthReqHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("the clicent actived!!!!!!!!!!!!!!!!!!");
		ctx.writeAndFlush(buildLoginReq());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		logger.info("recevied the message:{}",message);
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.vlaue()) {
			byte loginResult = (byte) message.getBody();
			logger.info("Received from server response.");
			if (loginResult != (byte) 0) {
				ctx.close();
			} else {
				logger.info("Login is ok:{}", message);
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("login occur error!", cause);
		ctx.fireExceptionCaught(cause);
	}

	private NettyMessage buildLoginReq() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.vlaue());
		message.setHeader(header);
		return message;
	}

}
