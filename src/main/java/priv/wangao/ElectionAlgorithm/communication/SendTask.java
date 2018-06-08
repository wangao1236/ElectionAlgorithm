package priv.wangao.ElectionAlgorithm.communication;

import java.io.IOException;

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
