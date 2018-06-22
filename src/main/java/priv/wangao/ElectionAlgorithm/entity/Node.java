package priv.wangao.ElectionAlgorithm.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import priv.wangao.ElectionAlgorithm.communication.WelcomeThread;
import priv.wangao.ElectionAlgorithm.communication.Thread.ElectionThread;
import priv.wangao.ElectionAlgorithm.communication.Thread.HelloThread;
import priv.wangao.ElectionAlgorithm.communication.Thread.MessageQueueThread;
import priv.wangao.ElectionAlgorithm.constant.StatusType;
import priv.wangao.ElectionAlgorithm.util.XML;

public class Node {
	
	private static final Node instance = new Node();
	private volatile String electionMsg = "";
	private int leaderID;
	
	public Lock lock = new ReentrantLock(true);
	public Condition helloCon = lock.newCondition();
	public Condition electCon = lock.newCondition();
	public Condition mqCon = lock.newCondition();
	public Boolean firstStart = true;
	
	public final int nodeAddrListSize;
	public final List<String> nodeAddrList;
	public final int nodeID;
	public final String nodeIP;
	public final int nodePort;
	private volatile StatusType status;
	private int lastNextID;
	
	@SuppressWarnings("unchecked")
	private Node() {
		Map<String, Object> conf = new XML().nodeConf();
		this.nodeAddrList = (List<String>) conf.get("nodeList");
		System.err.println(conf);
		this.nodeAddrListSize = this.nodeAddrList.size();
		this.nodeID = Integer.parseInt((String) conf.get("nodeID"));
		String[] split = this.nodeAddrList.get(nodeID).split(":");
		this.nodeIP = split[0];
		this.nodePort = Integer.parseInt(split[1]);
		this.leaderID = this.nodeID;
		this.status = StatusType.RUNNING;
		this.lastNextID = (this.nodeID + 1) % this.nodeAddrListSize;
	}	
	
	public void start() {
		new Thread(new WelcomeThread()).start();
		new Thread(new HelloThread()).start();
		new Thread(new ElectionThread()).start();
		new Thread(new MessageQueueThread()).start();
	}
	
	public static Node getInstance() {
		return instance;
	}
	
	public String getMyAddress() {
		return nodeAddrList.get(nodeID);
	}
	
	public synchronized void setLastNextID(int ID) {
		this.lastNextID = ID;
	}
	public int getLastNextID() {
		return this.lastNextID;
	}
	public int getNextID(int curID) {
		return (curID + 1) % this.nodeAddrListSize;
	}
	
	public String getAddrByID(int ID) {
		return this.nodeAddrList.get(ID);
	}
	
	public List<String> getInitConnectAddrList() {
		List<String> result = new ArrayList<String>();
		for (int i = 0 ; i < nodeID ; i++) {
			result.add(nodeAddrList.get(i));
		}
		return result;
	}
	
	public synchronized StatusType getStatus() {
		return this.status;
	}
	public synchronized void setStatus(StatusType status) {
		this.status = status;
	}
	
	public synchronized void setElectionMsg(String msg) {
		this.electionMsg = msg;
	}
	public String getElectionMsg() {
		return this.electionMsg;
	}
	
	public int getLeaderID() {
		return this.leaderID;
	}
	public synchronized void setLeaderID(int ID) {
		this.leaderID = ID;
	}
}
