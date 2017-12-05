package fileSystem;

import java.io.*;
import client.Client;

/**
 * Die FileSystem Klasse verwaltet das erstellen und schreiben von Datein und Ordnern.
 * Da der Roboter unter einem Unix Betriebssystem fährt, kann man auch theoretisch auf allen Ordern arbeiten.
 * Allerdings ist empfehlendswert nur unter dem Pfad "/home/lejos/programs/data/" zu arbeiten und dort alle Ordner anzulegen.
 * @author Lennart Monir
 * @version 0.1
 */
public class FileSystem {
	private static final String PATH = "/home/lejos/programs/data/";

	/**
	 * Die Methode legt automatisch einen data-Ordner an, falls dieser unter dem PATH nicht exisitiert.
	 */
	private static void createDataDir() {
		File dir = new File(PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	
	/**
	 * Diese Methode legt einen Ordner im data-Ordner an.
	 * @param s, der Ordnername
	 */
	public static void createDir(String s) {
		createDataDir();
		File dir = new File(PATH + s);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	
	/**
	 * Diese Methode erstellt aus einem zweidimensionalen Array eine CSV-Datei.
	 * @param name, der Dateiname. Beachten, dass der Pfad mit enthalten ist.
	 * @param arr, das zu speichernde Array
	 */
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
	
	/**
	 * Diese Methode sendet eine Datei an einen Server.
	 * @param filename, der Dateiname. Pfad beachten!
	 * @param client, ein Clientobjekt
	 */
	public static void sendFileToServer(String filename, Client client) {
		File file = new File(PATH + filename);
		client.sendFile(file);
	}
}
