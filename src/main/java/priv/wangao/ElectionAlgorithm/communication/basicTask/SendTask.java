package priv.wangao.ElectionAlgorithm.communication.basicTask;

import java.io.IOException;

import priv.wangao.ElectionAlgorithm.communication.MySocket;

public class SendTask implements Runnable {
	
	private MySocket socket;
	private String message;

	public SendTask(MySocket socket, String message) {
		this.socket = socket;
		this.message = message;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			socket.write(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
