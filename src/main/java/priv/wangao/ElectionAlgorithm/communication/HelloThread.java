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
//		Node.getInstance().lock.lock();
//		try {
			int ID = Node.getInstance().nodeID;
			String[] split = Node.getInstance().getAddrByID(ID).split(":");
			String IP = split[0];
			int Port = Integer.parseInt(split[1]);
			
			while (true) {
				System.err.println("22222");
				
				while (Node.getInstance().getStatus() == StatusType.Electing) {
					System.err.println("Waiting for running");
					try {
						Thread.sleep(1000 * Node.getInstance().nodeAddrListSize);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					Node.getInstance().helloCon.await();
//					System.err.println("Start running");
				}
				
				try {
					Thread.sleep(1000 * Node.getInstance().nodeAddrListSize);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				int nextID = Node.getInstance().getNextID(ID);
				split = Node.getInstance().getAddrByID(nextID).split(":");
				String nextIP = split[0];
				int nextPort = Integer.parseInt(split[1]);
				
				try {
					//System.out.println("1->Before hello socket connect: " + nextIP + ":" + nextPort + "<" + nextID + ">");
					Socket socket = new Socket(nextIP, nextPort);
					MySocket ms = new MySocket(socket);
					JSONObject jsonObject = new JSONObject();
					if (Node.getInstance().getStatus() == StatusType.RUNNING) {
						jsonObject.put("type", MessageType.HELLO);
						jsonObject.put("msg", "");
					} else if (Node.getInstance().getStatus() == StatusType.WAITING) {
						jsonObject.put("type", MessageType.ELECTION);
						jsonObject.put("msg", Node.getInstance().getElectionMsg());
					}
					jsonObject.put("addr", Node.getInstance().nodeIP+":"+Node.getInstance().nodePort);
					SendTask sendTask = new SendTask(ms, jsonObject.toString());
					ThreadPool.getInstance().add_tasks(sendTask);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					nextID = Node.getInstance().getNextID(nextID);
					split = Node.getInstance().getAddrByID(nextID).split(":");
					nextIP = split[0];
					nextPort = Integer.parseInt(split[1]);
					while (nextID != ID) {
						try {
							//System.out.println("2->Before hello socket connect: " + nextIP + ":" + nextPort + "<" + nextID + ">");
							Socket socket = new Socket(nextIP, nextPort);
							MySocket ms = new MySocket(socket);
							if (nextID != ID) {
								//System.out.println("Get NextAddr: " + nextPort);
								JSONObject jsonObject = new JSONObject();
								jsonObject.put("type", MessageType.ELECTION);
								jsonObject.put("addr", IP + ":" + Port);
								jsonObject.put("msg", Node.getInstance().nodeID);
								Node.getInstance().setStatus(StatusType.WAITING);
								//System.out.println("Send Next Msg: " + jsonObject.toString());
								SendTask sendTask = new SendTask(ms, jsonObject.toString());
								ThreadPool.getInstance().add_tasks(sendTask);
								break;
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							//e1.printStackTrace();
							nextID = Node.getInstance().getNextID(nextID);
							split = Node.getInstance().getAddrByID(nextID).split(":");
							nextIP = split[0];
							nextPort = Integer.parseInt(split[1]);
						}
					}
					if (nextID != ID) {
						Node.getInstance().setNextID(nextID);
					}
					//System.out.println("Towards addr: " + nextIP + ":" + nextPort);
					
				}
				
				
				
				//SocketList.getInstance().rebornHelloSockets();
			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			Node.getInstance().lock.unlock();
//		}
	}

}
