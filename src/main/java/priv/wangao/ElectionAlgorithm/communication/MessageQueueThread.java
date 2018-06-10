package priv.wangao.ElectionAlgorithm.communication;

import net.sf.json.JSONObject;
import priv.wangao.ElectionAlgorithm.constant.MessageType;
import priv.wangao.ElectionAlgorithm.constant.StatusType;
import priv.wangao.ElectionAlgorithm.server.Node;

public class MessageQueueThread implements Runnable {

	@Override
	public void run() {
		String newMsg = null;
		JSONObject jsonObject = null;
		while (true) {
			newMsg = MessageQueue.getInstance().getMessage();
			jsonObject = JSONObject.fromObject(newMsg);
			boolean isNormal = false;
			System.err.println("Current Message" + newMsg);
			Node.getInstance().lock.lock();
			try {
				decideStatus((String)jsonObject.get("type"), jsonObject);	
			} finally {
				Node.getInstance().lock.unlock();
			}
		}
	}
	
	private synchronized void decideStatus(String statusType, JSONObject jsonObject) {
		switch (statusType) {
			case "HELLO":	
				int myLeaderID = Node.getInstance().getLeaderID();
				int lastLeaderID = jsonObject.getInt("msg");
				if (lastLeaderID > myLeaderID) {
					System.out.println("New Leader: " + lastLeaderID);
					Node.getInstance().setLeaderID(lastLeaderID);
				}

				Node.getInstance().setStatus(StatusType.RUNNING);
				Node.getInstance().helloCon.signalAll();
				break;
			case "ELECTION":	
				if (Node.getInstance().getStatus() == StatusType.RUNNING) {
					Node.getInstance().setElectionMsg(jsonObject.getString("msg") + "," +
	                          Node.getInstance().nodeID);
					Node.getInstance().helloCon.signalAll();
					Node.getInstance().setStatus(StatusType.WAITING);
				} else if (Node.getInstance().getStatus() == StatusType.WAITING) {
					String[] split = jsonObject.getString("msg").split(",");
					boolean has = false;
					for (String str: split) {
						if (Integer.parseInt(str) == Node.getInstance().nodeID) {
							has = true;
							break;
						}
					}
					if (has == true) {
						System.out.println("Into Election");
						Node.getInstance().setElectionMsg(jsonObject.getString("msg"));
						Node.getInstance().setStatus(StatusType.ELECTING);
						Node.getInstance().electCon.signalAll();
					} else {
						Node.getInstance().setElectionMsg(jsonObject.getString("msg") + "," +
		                          Node.getInstance().nodeID);
						Node.getInstance().helloCon.signalAll();
						Node.getInstance().setStatus(StatusType.WAITING);
					}
					
				}
				break;
			case "INFORMATION":
				int leader = Integer.parseInt(jsonObject.getString("msg"));
				if (leader != Node.getInstance().getLeaderID()) {
					Node.getInstance().setLeaderID(leader);
					System.out.println("New Leader: " + leader);
				}
				Node.getInstance().setStatus(StatusType.RUNNING);
				Node.getInstance().helloCon.signalAll();
				break;
		}
		
	}
	
	public void work() {
	}

}
