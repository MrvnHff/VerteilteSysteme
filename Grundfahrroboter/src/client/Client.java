package client;

import java.net.Socket;
import java.io.*;

public class Client {
	private int port;
	private Socket socket;
	private String localhost;

	public Client(String localhost, int port) {
		this.port = port;
		this.localhost = localhost;
	}

	public Client() {
	}

	private void connect() {
		try {
			socket = new Socket(localhost, port);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String sendeAnfrage(String anfrage) {
		String antwort = "";
		try {
			connect();
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			out.writeObject(anfrage);
			antwort = in.readLine();

			socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return antwort;
	}

	public void sendeFile(File file) {
		try {
			connect();
			
			FileInputStream in = new FileInputStream(file);
			byte[] filebuffer = new byte[(int)file.length()];
			
			in.read(filebuffer);
			
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeInt((int)file.length());
			out.write(filebuffer);
			
			out.flush();
			out.close();
			in.close();
			socket.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setLocalHost(String localhost) {
		this.localhost = localhost;
	}

	public String getLocalHost() {
		return localhost;
	}
}
