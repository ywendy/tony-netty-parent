package com.tony.netty4.seond;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tony.netty4.codec.protobuf.SubscribeReqProto;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class SubReqClient {
	private static final Logger logger = LoggerFactory.getLogger(SubReqClient.class);

	public void connect(int port, String host) {

		logger.info("client prepare to connect the server!");
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			Bootstrap client = new Bootstrap();

			client.group(group).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class)
					.handler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
							ch.pipeline()
									.addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
							ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
							ch.pipeline().addLast(new ProtobufEncoder());
							ch.pipeline().addLast(new SubReqClientHandler());
						}

					});

			ChannelFuture f = client.connect(host, port).sync();
			logger.info("connected the server {}:{}", host, port);
			f.channel().closeFuture().sync();
		} catch (Throwable t) {
			logger.error("connect {}:{} error,{}", host, port, t.getMessage());
		} finally {
			group.shutdownGracefully();
		}
	}

}
