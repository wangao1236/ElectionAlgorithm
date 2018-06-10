package priv.wangao.ElectionAlgorithm.communication.Thread;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import priv.wangao.ElectionAlgorithm.communication.MySocket;
import priv.wangao.ElectionAlgorithm.communication.ThreadPool;
import priv.wangao.ElectionAlgorithm.communication.basicTask.SendTask;
import priv.wangao.ElectionAlgorithm.constant.MessageType;
import priv.wangao.ElectionAlgorithm.constant.StatusType;
import priv.wangao.ElectionAlgorithm.entity.Node;
import priv.wangao.ElectionAlgorithm.util.JSON;

public class HelloThread implements Runnable {

	public void run() {
		// TODO Auto-generated method stub
		int ID = Node.getInstance().nodeID;
		String IP = Node.getInstance().nodeIP;
		int Port = Node.getInstance().nodePort;
		
		while (true) {
			try {
				Thread.sleep(300 * Node.getInstance().nodeAddrListSize);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Node.getInstance().lock.lock();
			try {
				
				while (Node.getInstance().getStatus() == StatusType.ELECTING) {
					System.err.println("Waiting for RUNNING Status");
					Node.getInstance().helloCon.await();
					System.err.println("Start RUNNING Status");
				}
				
				int nextID = Node.getInstance().getNextID(ID);
				int firstNextID = nextID;
				String[] split = Node.getInstance().getAddrByID(nextID).split(":");
				String nextIP = split[0];
				int nextPort = Integer.parseInt(split[1]);
				try {
					Socket socket = new Socket(nextIP, nextPort);
					MySocket ms = new MySocket(socket);
					SendTask sendTask = null;
					if (Node.getInstance().getStatus() == StatusType.RUNNING) {
						sendTask = new SendTask(ms, JSON.getHelloMsg().toString());
					} else if (Node.getInstance().getStatus() == StatusType.WAITING) {
						sendTask = new SendTask(ms, JSON.getElectionMsg(Node.getInstance().getElectionMsg()).toString());
					}
					ThreadPool.getInstance().add_tasks(sendTask);
					Node.getInstance().helloCon.await();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					nextID = Node.getInstance().getNextID(nextID);
					split = Node.getInstance().getAddrByID(nextID).split(":");
					nextIP = split[0];
					nextPort = Integer.parseInt(split[1]);
					while (nextID != ID) {
						try {
							Socket socket = new Socket(nextIP, nextPort);
							MySocket ms = new MySocket(socket);
							if (nextID != ID) {
								SendTask sendTask = null;
								if (firstNextID == Node.getInstance().getLeaderID()) {
									Node.getInstance().setStatus(StatusType.WAITING);
									sendTask = new SendTask(ms, JSON.getElectionMsg(Integer.toString(Node.getInstance().nodeID)).toString());
								} else {
									sendTask = new SendTask(ms, JSON.getHelloMsg().toString());
								}
								ThreadPool.getInstance().add_tasks(sendTask);
								Node.getInstance().helloCon.await();
								break;
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							nextID = Node.getInstance().getNextID(nextID);
							split = Node.getInstance().getAddrByID(nextID).split(":");
							nextIP = split[0];
							nextPort = Integer.parseInt(split[1]);
						}
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				Node.getInstance().lock.unlock();
			}
		}
	}

}
