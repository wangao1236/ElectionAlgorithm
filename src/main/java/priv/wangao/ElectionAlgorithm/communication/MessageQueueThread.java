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
			System.err.println("22222");
			newMsg = MessageQueue.getInstance().getMessage();
			jsonObject = JSONObject.fromObject(newMsg);
			boolean isNormal = false;
			System.err.println(newMsg);
			decideStatus((String)jsonObject.get("type"), jsonObject);
			//System.err.println("Now Node msg: " + Node.getInstance().getElectionMsg());
				
		}
	}
	
	private synchronized void decideStatus(String statusType, JSONObject jsonObject) {
//		Node.getInstance().lock.lock();
//		try {
			switch (statusType) {
				case "HELLO":	
					//Node.getInstance().helloCon.signalAll();
					Node.getInstance().setStatus(StatusType.RUNNING);
					break;
				case "ELECTION":	
					if (Node.getInstance().getStatus() == StatusType.RUNNING) {
						Node.getInstance().setElectionMsg(jsonObject.getString("msg") + "," +
		                          Node.getInstance().nodeID);
						Node.getInstance().setStatus(StatusType.WAITING);
					} else if (Node.getInstance().getStatus() == StatusType.WAITING) {
						System.out.println("Into Election");
		//				ElectionThread.setElectionMsg(jsonObject.getString("msg"));
						Node.getInstance().setElectionMsg(jsonObject.getString("msg"));
						//Node.getInstance().electCon.signalAll();
						Node.getInstance().setStatus(StatusType.Electing);
					}
					break;
				case "INFORMATION":
					int leader = Integer.parseInt(jsonObject.getString("msg"));
					Node.getInstance().setLeaderID(leader);
					ElectionThread.setElectionMsg(null);
					//Node.getInstance().helloCon.signalAll();
					Node.getInstance().setStatus(StatusType.RUNNING);
					System.out.println("New Leader: " + leader);
					break;
			}
//		} finally {
//			Node.getInstance().lock.unlock();
//		}
		
	}
	
	public void work() {
	}

}
