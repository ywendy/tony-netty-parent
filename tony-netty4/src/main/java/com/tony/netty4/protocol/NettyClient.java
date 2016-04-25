package com.tony.netty4.protocol;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.common.Constants;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClient {

	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	public static void main(String[] args) {
		new NettyClient().connect(Constants.PORT, Constants.HOST);
	}

	public void connect(int port, String host) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap client = new Bootstrap();
			client.group(group).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {

							ch.pipeline().addLast("readtimeoutHandler", new ReadTimeoutHandler(50));
							ch.pipeline().addLast(MarshallingCodeCFactory.buildNettyMarshallingDecoder());
							ch.pipeline().addLast(MarshallingCodeCFactory.buildNettyMarshallingEncoder());
							ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
							ch.pipeline().addLast("HeartBeathandler", new HeartBeatReqHandler());
						}
					});

			ChannelFuture f = client.connect(host,port).sync();

			logger.info("Netty Client connect successed!");
			f.channel().closeFuture().sync();

		} catch (Exception e) {
			logger.warn("服务器异常，等待重连...");

		} finally {
			executor.execute(new Runnable() {

				@Override
				public void run() {

					try {
						TimeUnit.SECONDS.sleep(5);
						connect(Constants.PORT, Constants.HOST);
					} catch (Exception e) {

					}

				}
			});
		}

	}

}
