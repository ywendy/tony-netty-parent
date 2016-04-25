package com.tony.netty4.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.common.Constants;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyServer {

	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	/**
	 * @param port
	 */
	public void start(int port) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						/* (non-Javadoc)
						 * @see io.netty.channel.ChannelInitializer#initChannel(io.netty.channel.Channel)
						 */
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
							ch.pipeline().addLast(MarshallingCodeCFactory.buildNettyMarshallingDecoder());
							ch.pipeline().addLast(MarshallingCodeCFactory.buildNettyMarshallingEncoder());
							ch.pipeline().addLast(new LoginAuthRespHandler());
							ch.pipeline().addLast(new HeartBeatRespHandler());
						}
					});

			ChannelFuture f = server.bind(Constants.HOST, Constants.PORT).sync();
			logger.info("Netty server start ok:{}:{}", Constants.HOST, Constants.PORT);
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			logger.warn("服务器异常......");
		} finally {

			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

	public static void main(String[] args) {
		new NettyServer().start(Constants.PORT);
	}
}
