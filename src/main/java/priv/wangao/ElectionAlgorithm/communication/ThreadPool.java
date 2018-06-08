package priv.wangao.ElectionAlgorithm.communication;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import priv.wangao.ElectionAlgorithm.util.XML;

public class ThreadPool {

	private int maxThreadNum;
	private ExecutorService executors;
	private static ThreadPool instance = new ThreadPool();
	
	@SuppressWarnings("unchecked")
	private ThreadPool() {
		Map<String, Object> conf = new XML().nodeConf();
		int nodeNum = ((List<String>) (conf.get("nodeList"))).size();
		this.maxThreadNum = nodeNum * 10;
		executors = Executors.newFixedThreadPool(maxThreadNum);
	}
	
	public static ThreadPool getInstance() {
		return instance;
	}

	public void add_tasks(Runnable task) {
		// TODO Auto-generated method stub
		executors.execute(task);
	}
	
}
