package com.fss.saber.adapter.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.util.iso8583.util.ByteHexUtil;

public class ThalesHSMConnect {

	private static final Logger logger = LoggerFactory.getLogger(ThalesHSMConnect.class);
	
	//@formatter:off
	public static final String send(final HSMConfig hsmConfig, final String command) throws UnknownHostException, IOException {
		logger.trace("hsm command", command);
		try(final Socket sc = new Socket(hsmConfig.host, hsmConfig.port);
			final DataInputStream din = new DataInputStream(sc.getInputStream());
			final DataOutputStream dos = new DataOutputStream(sc.getOutputStream())) {
			sc.setSoTimeout(5000);
			dos.writeUTF(command);
			dos.flush();
			final String response = din.readUTF();
			logger.trace("hsm response", response);
			return response;
		}
	}
	
	/**
	 * 
	 * @param hsmConfig
	 * @param command					: the input command must have two empty bytes in the beginning.
	 * @param logger
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static final byte[] send(final HSMConfig hsmConfig, final byte[] command) throws Exception {
		try(Socket sc = new Socket(hsmConfig.host, hsmConfig.port);
			InputStream in = sc.getInputStream();
			OutputStream os = sc.getOutputStream()) {
			sc.setSoTimeout(5000);
			command[0] = (byte) ((command.length-2)/256);
			command[1] = (byte) ((command.length-2)%256);
			logger.trace("hsm command", ByteHexUtil.byteToHex(command));
			os.write(command);
			os.flush();
			final byte b1 = (byte) in.read();
			final byte b2 = (byte) in.read();
			if(b1 < 0 || b2 < 0) throw new SocketTimeoutException("no response from hsm.");
			final byte[] response = new byte[b1*256+b2];
			in.read(response);
			logger.trace("hsm response", ByteHexUtil.byteToHex(response));
			return response;
		}
	}
	
	public static void main(String[] args) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		dos.writeUTF("0000CWAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBB4484070020000310;2105000");
		dos.flush();
		System.out.println(ByteHexUtil.byteToHex(baos.toByteArray()));
	}

}
