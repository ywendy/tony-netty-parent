package com.tony.netty4.protocol;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.common.Constants;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(LoginAuthRespHandler.class);

	public static Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();
	private String[] whiteList = { Constants.HOST };

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		nodeCheck.remove(ctx.channel().remoteAddress().toString());// 删除
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.vlaue()) {

			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage loginResp = null;
			if (nodeCheck.containsKey(nodeIndex)) {
				loginResp = buildResponse((byte) -1);
			} else {
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOK = false;
				for (String WIP : whiteList) {
					if (WIP.equals(ip)) {
						isOK = true;
						break;
					}
				}
				loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);

				if (isOK) {
					nodeCheck.put(nodeIndex, true);
				}

				logger.info("The login response is : {}  ,body[{}]", loginResp, loginResp.getBody());
				ctx.writeAndFlush(loginResp);

			}

		} else {
			ctx.fireChannelRead(msg);
		}

	}

	private NettyMessage buildResponse(byte result) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.vlaue());
		message.setHeader(header);
		message.setBody(result);
		return message;
	}

}
