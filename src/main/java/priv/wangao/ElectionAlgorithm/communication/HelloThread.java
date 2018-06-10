package priv.wangao.ElectionAlgorithm.communication;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import priv.wangao.ElectionAlgorithm.constant.MessageType;
import priv.wangao.ElectionAlgorithm.constant.StatusType;
import priv.wangao.ElectionAlgorithm.server.Node;
import priv.wangao.ElectionAlgorithm.util.JSON;

public class HelloThread implements Runnable {

	public void run() {
		// TODO Auto-generated method stub
		int ID = Node.getInstance().nodeID;
		String[] split = Node.getInstance().getAddrByID(ID).split(":");
		String IP = split[0];
		int Port = Integer.parseInt(split[1]);
		
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
				split = Node.getInstance().getAddrByID(nextID).split(":");
				String nextIP = split[0];
				int nextPort = Integer.parseInt(split[1]);
				try {
					Socket socket = new Socket(nextIP, nextPort);
					MySocket ms = new MySocket(socket);
					JSONObject jsonObject = new JSONObject();
					if (Node.getInstance().getStatus() == StatusType.RUNNING) {
						jsonObject.put("type", MessageType.HELLO);
						jsonObject.put("msg", Node.getInstance().getLeaderID());
					} else if (Node.getInstance().getStatus() == StatusType.WAITING) {
						jsonObject.put("type", MessageType.ELECTION);
						jsonObject.put("msg", Node.getInstance().getElectionMsg());
					}
					jsonObject.put("addr", Node.getInstance().nodeIP+":"+Node.getInstance().nodePort);
					SendTask sendTask = new SendTask(ms, jsonObject.toString());
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
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("addr", IP + ":" + Port);
								if (firstNextID == Node.getInstance().getLeaderID()) {
									jsonObject.put("type", MessageType.ELECTION);
									jsonObject.put("msg", Node.getInstance().nodeID);
									Node.getInstance().setStatus(StatusType.WAITING);
								} else {
									jsonObject.put("type", MessageType.HELLO);
									jsonObject.put("msg", Node.getInstance().getLeaderID());
								}
								SendTask sendTask = new SendTask(ms, jsonObject.toString());
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
