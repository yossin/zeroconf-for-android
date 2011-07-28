package mta.yos.zeroconf.lamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Listener extends Thread{
	static final Logger logger = Logger.getLogger(Listener.class.getName());
	int startPort;
	ServerSocket socket;
	LampHandler handler;
	
	public Listener(int startPort, LampHandler handler) {
		this.handler = handler;
		this.startPort=startPort;
	}

	public int getListenPort(){
		int i=0;
		while (socket ==null && i++<10){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		
		if (socket ==null) return startPort;
		return socket.getLocalPort();
	}


	private ServerSocket bind() throws IOException{
		ServerSocket socket;
		int maxBindDelta = 10;
		for (int i=0;i<maxBindDelta;i++){
			int port = startPort +i;
			try {
				socket = new ServerSocket(port);
				return socket;
			} catch (IOException e) {
				logger.fine("unable to bind port "+port);
			}
		}
		throw new IOException("unable to bind port range: "+startPort+"-"+(startPort+maxBindDelta));
	}
	
	
	
	private void clientConversation(Socket clientSocket) throws IOException{
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			String request = in.readLine();
			String response = handler.handle(request);

			out.println(response);
		} finally {
			if (out!=null) out.close();
			try {
				if (in!=null) in.close();
			} catch (IOException e) {
			}
			try {
				if (clientSocket!=null) clientSocket.close();
			} catch (IOException e) {
			}
		}
	}
	
	@Override
	public void run() {
		try {
			socket = bind();
			try {
				while (isInterrupted() == false){
					clientConversation(socket.accept());
				}
			} catch (IOException ee){
			}
		} catch (IOException e) {
			logger.severe("unable to bind socket " + e.getMessage());
		}
	} 
	@Override
	public void interrupt() {
		super.interrupt();
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
}