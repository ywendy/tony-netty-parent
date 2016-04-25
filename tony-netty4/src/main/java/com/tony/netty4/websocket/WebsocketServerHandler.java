package com.tony.netty4.websocket;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.common.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class WebsocketServerHandler extends ChannelInboundHandlerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(WebsocketServerHandler.class);

	private WebSocketServerHandshaker handshaker;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("客户端连接了哦.");
		ctx.writeAndFlush("欢迎使用websocket 服务.");

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, (FullHttpRequest) msg);

		} else if (msg instanceof WebSocketFrame) {
			handleWebsocketFrame(ctx, (WebSocketFrame) msg);
		}
	}

	private void handleWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

		// 如果是关闭链路的命令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}

		// 如果是ping 消息，返回Pong消息回应.
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 不是文本消息，直接抛出异常哦.
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame types not suported", frame.getClass().getName()));
		}

		String requestMsg = ((TextWebSocketFrame) frame).text();

		logger.info("{} received {}", ctx.channel(), requestMsg);

		ctx.channel().write(new TextWebSocketFrame(
				String.format("%s , 欢迎使用Netty Websocket 服务，现在时刻是：%s", requestMsg, new Date().toString())));

	}

	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
		// http 解析失败或者是websocket请求，不处理
		if (request.getDecoderResult().isFailure() || !"websocket".equals(request.headers().get("Upgrade"))) {
			sendHttpResponse(ctx, request,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		// websocket 协议
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(Constants.WS_URL, null,
				false);
		handshaker = wsFactory.newHandshaker(request);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), request);
		}

	}

	private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response) {

		logger.info("http headers:==>" + request.headers().toString());
		// 返回以应答
		if (response.getStatus().code() != HttpResponseStatus.OK.code()) {
			ByteBuf buf = Unpooled.copiedBuffer(response.getStatus().toString(), CharsetUtil.UTF_8);
			response.content().writeBytes(buf);
			buf.release();
			response.headers().set("content-length", response.content().readableBytes());
		}

		ChannelFuture f = ctx.channel().writeAndFlush(response);
		if (!isKeepAlive(request) || response.getStatus().code() != HttpResponseStatus.OK.code()) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private boolean isKeepAlive(FullHttpRequest request) {
		// TODO 是否 keep-alive
		return false;
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.warn("Websocket occur error!");
	}

}
