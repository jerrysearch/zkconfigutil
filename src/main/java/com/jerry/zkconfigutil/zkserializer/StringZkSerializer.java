package com.jerry.zkconfigutil.zkserializer;

import java.nio.charset.Charset;

import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

public final class StringZkSerializer implements ZkSerializer {

	final Charset charset = Charset.forName("utf-8");

	@Override
	public byte[] serialize(Object data) throws ZkMarshallingError {
		// TODO Auto-generated method stub
		return data.toString().getBytes(this.charset);
	}

	@Override
	public Object deserialize(byte[] bytes) throws ZkMarshallingError {
		// TODO Auto-generated method stub
		return new String(bytes, this.charset);
	}

}
