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
	
	public int getSize() {
		return mq.size();
	}
	
	public String getMessage() {
		String message = null;
		try {
			//System.out.println("Before taking");
			message = mq.take();
			//System.out.println("After taking");
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
