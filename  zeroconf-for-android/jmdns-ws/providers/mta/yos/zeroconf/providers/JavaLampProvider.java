package mta.yos.zeroconf.providers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import mta.yos.zeroconf.devices.DeviceProvider;

public class JavaLampProvider implements DeviceProvider{

	String hostname;
	int port;
	
	public JavaLampProvider(String hostname, int port) {
		this.hostname= hostname;
		this.port= port;
	}
	
	private String sendReceive(String message) throws IOException{
		Socket socket=null;
		PrintWriter out=null;
		BufferedReader in=null;
		String response=null;
		try {
		socket = new Socket(hostname, port);
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		// send
		out.println(message);
		out.flush();
		// receive
		response = in.readLine();
		} finally {
			try {
				if (in!=null) in.close();
			} catch (IOException e) {
			}
			if (out!=null) out.close();
			try {
				if (socket!=null) socket.close();
			} catch (IOException e) {
			}
		}
		
		return response;

	}

	@Override
	public void turnOff() throws IOException{
		sendReceive("turnOff");
		
	}

	@Override
	public void turnOn() throws IOException{
		sendReceive("turnOn");
	}

	@Override
	public int status() throws IOException{
		String status=sendReceive("status");
		try {
			return Integer.parseInt(status);
		} catch (IllegalArgumentException e){
			throw new IOException("unable to receive status (status is: "+ status+")");
		}
	}

}
