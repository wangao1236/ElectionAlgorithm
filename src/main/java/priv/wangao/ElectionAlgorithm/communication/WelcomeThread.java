package priv.wangao.ElectionAlgorithm.communication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import priv.wangao.ElectionAlgorithm.communication.basicTask.RecvTask;
import priv.wangao.ElectionAlgorithm.entity.Node;

public class WelcomeThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String myAddr = Node.getInstance().getMyAddress();
		String[] ip_port = myAddr.split(":");
		String ip = ip_port[0];
		int port = Integer.parseInt(ip_port[1]);
		
		try {
			ServerSocket serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
			while (true) {
				Socket socket = serverSocket.accept();
				MySocket ms = new MySocket(socket);
				RecvTask task = new RecvTask(ms);
				ThreadPool.getInstance().add_tasks(task);
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
