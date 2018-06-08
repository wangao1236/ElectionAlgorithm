package priv.wangao.ElectionAlgorithm.communication;

import java.util.concurrent.LinkedBlockingQueue;

public class MessageQueue {
	
	private LinkedBlockingQueue<String> mq;
	private static MessageQueue instance = new MessageQueue();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private MessageQueue() {
		mq = new LinkedBlockingQueue();
	}
	
	public static MessageQueue getInstance() {
		return instance;
	}
	
	public String getMessage() {
		String message = null;
		try {
			message = mq.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	public void pushMessage(String message) {
		try {
			mq.put(message);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
