package priv.wangao.ElectionAlgorithm.communication.Thread;

import java.io.IOException;
import java.net.Socket;

import net.sf.json.JSONObject;
import priv.wangao.ElectionAlgorithm.communication.MySocket;
import priv.wangao.ElectionAlgorithm.communication.ThreadPool;
import priv.wangao.ElectionAlgorithm.communication.basicTask.SendTask;
import priv.wangao.ElectionAlgorithm.constant.MessageType;
import priv.wangao.ElectionAlgorithm.constant.StatusType;
import priv.wangao.ElectionAlgorithm.entity.Node;
import priv.wangao.ElectionAlgorithm.util.JSON;

public class ElectionThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			
			try {
				Thread.sleep(300 * Node.getInstance().nodeAddrListSize);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Node.getInstance().lock.lock();
			try {
				System.err.println("Waiting for electing");
				Node.getInstance().electCon.await();
				System.err.println("Start electing");
			
					
				System.out.println("Start Electing : " + Node.getInstance().getElectionMsg());
				String[] split = Node.getInstance().getElectionMsg().split(",");
				int leader = Node.getInstance().nodeID;
				for (String str: split) {
					int ID = Integer.parseInt(str);
					leader = Math.max(leader, ID);
				}
				for (String str: split) {
					int ID = Integer.parseInt(str);
					String addr = Node.getInstance().getAddrByID(ID);
					String IP = addr.split(":")[0];
					int Port = Integer.parseInt(addr.split(":")[1]);
					try {
						System.out.println("Toward " + IP + ":" + Port);
						Socket socket = new Socket(IP, Port);
						MySocket ms = new MySocket(socket);
						SendTask sendTask = new SendTask(ms, JSON.getInformationMsg(Integer.toString(leader)).toString());
						ThreadPool.getInstance().add_tasks(sendTask);
						Node.getInstance().electCon.await();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
