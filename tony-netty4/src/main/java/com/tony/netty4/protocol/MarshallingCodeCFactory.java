package com.tony.netty4.protocol;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

public final class MarshallingCodeCFactory {

	public static NettyMarshallingEncoder buildNettyMarshallingEncoder() {
		MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration conf = new MarshallingConfiguration();
		conf.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, conf);
		NettyMarshallingEncoder encoder = new NettyMarshallingEncoder(provider);
		return encoder;
	}

	public static NettyMarshallingDecoder buildNettyMarshallingDecoder() {
		MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
		NettyMarshallingDecoder decoder = new NettyMarshallingDecoder(provider, 1024);
		return decoder;
	}

}
