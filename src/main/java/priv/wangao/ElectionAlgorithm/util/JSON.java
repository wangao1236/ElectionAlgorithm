package priv.wangao.ElectionAlgorithm.util;

import net.sf.json.JSONObject;
import priv.wangao.ElectionAlgorithm.constant.MessageType;
import priv.wangao.ElectionAlgorithm.entity.Node;

public class JSON {

	public static JSONObject getHelloMsg() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", MessageType.HELLO);
		jsonObject.put("msg", Node.getInstance().getLeaderID());
		jsonObject.put("addr", Node.getInstance().nodeIP+":"+Node.getInstance().nodePort);
		jsonObject.put("time", System.currentTimeMillis());
		return jsonObject;
	}
	
	public static JSONObject getElectionMsg(String waitingIDs) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", MessageType.ELECTION);
		jsonObject.put("msg", waitingIDs);
		jsonObject.put("addr", Node.getInstance().nodeIP+":"+Node.getInstance().nodePort);
		jsonObject.put("time", System.currentTimeMillis());
		return jsonObject;
	}
	
	public static JSONObject getInformationMsg(String leaderID) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", MessageType.INFORMATION);
		jsonObject.put("msg", leaderID);
		jsonObject.put("addr", Node.getInstance().nodeIP+":"+Node.getInstance().nodePort);
		jsonObject.put("time", System.currentTimeMillis());
		return jsonObject;
	}
}
