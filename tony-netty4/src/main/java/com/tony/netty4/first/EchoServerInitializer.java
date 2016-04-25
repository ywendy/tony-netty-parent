package com.tony.netty4.first;

import com.tony.netty4.common.Constants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel channel) throws Exception {

		ByteBuf delimiter = Unpooled.copiedBuffer(Constants.DELIMITER.getBytes());
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
		pipeline.addLast(new StringDecoder());
		pipeline.addLast(new StringEncoder());
		pipeline.addLast(new EchoChannelInboundServerHandler());

	}

}
