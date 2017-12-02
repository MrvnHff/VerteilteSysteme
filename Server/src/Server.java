import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private boolean stop;
	ServerSocket serverSocket;

	public Server() {
		stop = false;
	}

	public void run() {
		while (!stop) {
			System.out.println("Warte auf Nachricht");
			try {

				File file = new File("C:/Users/Lennart/Desktop/test.csv");
				if (!file.exists()) {
					file.createNewFile();
				}

				FileOutputStream fileOutput = new FileOutputStream(file);

				serverSocket = new ServerSocket(6000);
				Socket clientSocket = serverSocket.accept();

				if (!serverSocket.isClosed()) {
					DataInputStream sis = new DataInputStream(clientSocket.getInputStream());
					int length = sis.readInt(); // read length of incoming message
					byte[] message = new byte[length];
					if (length > 0) {
						sis.readFully(message, 0, message.length); // read the message
						fileOutput.write(message);
						fileOutput.flush();
					}
					sis.close();
				}
				fileOutput.close();
				if (!serverSocket.isClosed()) {
				serverSocket.close();}
				System.out.println("Nachricht erhalten!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setStop(boolean stop) {
		this.stop = stop;
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.stop = stop;
	}
}
