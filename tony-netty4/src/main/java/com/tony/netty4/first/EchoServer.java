package com.tony.netty4.first;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

	private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);

	public void bind(int port) {
		logger.info("begin to start netty4.........");
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100).childHandler(new EchoServerInitializer());

			ChannelFuture future = bootstrap.bind(port).sync();
			logger.info("netty4 started.");
			future.channel().closeFuture().sync();
		} catch (Throwable t) {
			logger.error("start netty server error", t);
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}
