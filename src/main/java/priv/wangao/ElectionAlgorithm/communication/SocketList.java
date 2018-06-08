package priv.wangao.ElectionAlgorithm.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import priv.wangao.ElectionAlgorithm.util.JSON;

public class SocketList {
	
	private static SocketList instance = new SocketList();
	private Map<String, MySocket> helloMap = new HashMap<String, MySocket>();
	
	private SocketList() {
		
	}
	
	public static SocketList getInstance() {
		return instance;
	}

	public synchronized void addHelloSocket(MySocket socket, String ipport) {
		helloMap.put(ipport, socket);
	}
	
	public synchronized void rebornHelloSockets() {
		for (Map.Entry<String, MySocket> entry: helloMap.entrySet()) {
			if (entry.getValue() == null) {
				String addr = entry.getKey();
				String[] split = addr.split(":");
				String ip = split[0];
				int port = Integer.parseInt(split[1]);
				try {
					MySocket socket = new MySocket(new Socket(ip, port));
					this.addHelloSocket(socket, addr);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
}
