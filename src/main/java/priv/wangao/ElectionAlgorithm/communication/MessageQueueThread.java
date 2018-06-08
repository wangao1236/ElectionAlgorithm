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
			System.err.println(newMsg);
			switch ((String)jsonObject.get("type")) {
				case "HELLO":	
					Node.getInstance().setStatus(StatusType.RUNNING);
					break;
				case "ELECTION":	
					if (Node.getInstance().getStatus() == StatusType.RUNNING) {
						Node.getInstance().setElectionMsg(jsonObject.getString("msg") + "," +
		                          Node.getInstance().nodeID);
						Node.getInstance().setStatus(StatusType.WAITING);
					} else if (Node.getInstance().getStatus() == StatusType.WAITING) {
						System.out.println("Into Election");
						Node.getInstance().setElectionMsg(jsonObject.getString("msg"));
						Node.getInstance().setStatus(StatusType.Electing);
					}
					break;
				case "INFORMATION":
					int leader = Integer.parseInt(jsonObject.getString("msg"));
					Node.getInstance().setLeaderID(leader);
					System.out.println("New Leader: " + leader);
					break;
			}
			System.err.println("Now Node msg: " + Node.getInstance().getElectionMsg());
				
		}
	}
	
	public void work() {
	}

}
