package priv.wangao.ElectionAlgorithm.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MySocket {
	private Socket socket;
	
	public MySocket(Socket socket) {
		this.socket = socket;
	}
	
	public synchronized void write(String message) throws IOException {
		byte[] msg = message.getBytes();
		try(
			OutputStream os = socket.getOutputStream();
		) {
			os.write(msg);
			os.flush();
		}
	}
	
	public List<String> read() throws IOException {
		List<String> res = new ArrayList<String>();
		try (
			InputStream is = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf8");
			BufferedReader br = new BufferedReader(isr);
		) {
			String curLine = null;
			while ((curLine = br.readLine()) != null) {
				res.add(curLine);
			}
		}
		return res;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
