package com.tony.netty4.common;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;

public class SessionCache extends ConcurrentHashMap<ChannelHandlerContext, ChannelHandlerContext>{

	private static final long serialVersionUID = -7758684655820274034L;

}
