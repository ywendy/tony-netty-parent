package com.tony.netty4.first;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.common.Constants;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoClient {
	private static final Logger logger = LoggerFactory.getLogger(EchoClient.class);

	public void connect(int port, String host) {
		logger.info("echoclient begin to start......");
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap bootstrap = new Bootstrap();

			bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel channel) throws Exception {
							ByteBuf delimiter = Unpooled.copiedBuffer(Constants.DELIMITER.getBytes());
							ChannelPipeline pipeline = channel.pipeline();
							pipeline.addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
							pipeline.addLast(new StringDecoder());
							pipeline.addLast(new StringEncoder());
							pipeline.addLast(new EchoChannelInboundClientHandler());
						}
					});

			ChannelFuture f = bootstrap.connect(host, port).sync();
			f.channel().closeFuture().sync();

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}

}
