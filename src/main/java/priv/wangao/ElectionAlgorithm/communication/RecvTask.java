package priv.wangao.ElectionAlgorithm.communication;

import java.io.IOException;
import java.util.List;

public class RecvTask implements Runnable {
	
	private MySocket socket;
	
	public RecvTask(MySocket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<String> recv;
		while (true) {
			try {
				recv = socket.read();
				if (recv.isEmpty()) {
					//System.out.println("client connection has been closed.");
					socket.close();
					break;
				} else {
					//System.out.println("Get New Message!");
					for (String msg: recv) {
						MessageQueue.getInstance().pushMessage(msg);
						//System.err.println(msg);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//socket.close();
				break;
			}
		}
	}

}
