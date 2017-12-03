package fileSystem;

import java.io.*;
import client.Client;

public class FileSystem {
	private static final String PATH = "/home/lejos/programs/data/";

	private static void createDataDir() {
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}

	}

	public static void createDir(String s) {
		createDataDir();
		File dir = new File(PATH + s);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	public static void saveAsCSV(String name, String arr[][]) {
		try {
			File file = new File(PATH + name + ".csv");
			file.createNewFile();
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr[i].length; j++) {
					bw.write(arr[i][j] + ";");
				}
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			System.out.println("Fehler bei CSV Datei-Erstellung");
		}
	}
	
	public static void sendFileToServer(String filename, Client client) {
		File file = new File(PATH + filename);
		client.sendFile(file);
	}
}
