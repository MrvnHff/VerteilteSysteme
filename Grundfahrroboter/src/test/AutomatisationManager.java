package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** <b> Klasse zum Testen des Programms </b>
 * Der AutomatisationManager kann beliebig viele Virtuelle Roboter verwalten.
 * Jeder Virtuelle Roboter wird in einem eigenen Thread gestartet.
 * Auf die Roboter kann mit den in dieser Klasse zur Verfügung stehenden Methoden 
 * Einfluss genommen werden. Des Weiteren ist es möglich, sich den Thread zu einem 
 * Roboter geben zu lassen um so direkt Einfluss auf den Roboter zu nehmen.
 * Der Manager weiß nichts über den aktuellen Zustand eines Virtuellen Roboters oder 
 * des jeweiligen Threads, er speichert nur die RoboterID und den dazugehörigen Thread.
 * @author Janek Dahl
 *
 */
public class AutomatisationManager {

	private Map<String, VirtualRobot> management = new HashMap<String, VirtualRobot>();
	
	/**
	 * Konstruktor um einen Manager anzulegen.
	 * 
	 */
	public AutomatisationManager() {
		
	}

	
	/**
	 * Legt mehrere Virtuelle Roboter auf einmal an
	 * @param serverIp IP des Servers, bei dem der Listener läuft
	 * @param serverPort Port des Servers, bei dem der Listener läuft 
	 * @param roboterPortStart Ports, ab denen die Roboter laufen sollen (für jeden Roboter um 1 inkrementiert ab Startwert)
	 * @param waitTime Wartezeit der Roboter, bis Fahrbefehle fertig sind
	 * @param robotId Das Prefix der Roboter IDs
	 * @param count Anzahl der Roboter, die hinzugefügt werden sollen
	 */
	public void addMultitpleRobots(String serverIp, int serverPort, int roboterPortStart, int waitTime, String robotId, int count) {

		for(int i = 0; i < count; i++) {
			addRobot(serverIp, serverPort, roboterPortStart - i, waitTime, (robotId + (i + 1)));
		}
	}
	
	
	/**
	 * Legt einen Virtuellen Roboter an
	 * @param serverIp IP des Servers, bei dem der Listener läuft
	 * @param serverPort Port des Servers, bei dem der Listener läuft 
	 * @param robotPort Port des Roboters, an dem der Roboter-Server gestartet werden soll
	 * @param waitTime Wartezeit der Roboter, bis Fahrbefehle fertig sind
	 * @param robotId Die ID des Roboters
	 */
	public void addRobot(String serverIp, int serverPort, int robotPort, int waitTime, String robotId) {
		//Prüfen, ob der Port frei ist.
		//System.out.println("new VirtualRobot(" + serverIp +", " + serverPort +", " + robotPortStart +", " + waitTime +", " + robotId + ")" );
		//String serverIp, int serverPort, int robotPort, int waitTime, String robotId
		management.put(robotId, new VirtualRobot(serverIp, serverPort, robotPort, waitTime, robotId));
		management.get(robotId).start();
	}
	
	/**
	 * Schließt den Roboter, beendet den Thread und löscht ihn aus dem Manager.
	 * @param robotId Die ID des Roboters
	 */
	public void removeRobot(String robotId) {
		if(management.containsKey(robotId)) {
			try {
				management.get(robotId).quitWorker();
				management.remove(robotId);
				System.out.println("AutomatisationManager: Roboter entfernt. ID: " + robotId);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("AutomatisationManager: fehler beim Entfernen des Roboters aufgetreten.");
			}
		} else {
			System.out.println("AutomatisationManager: Roboter war nicht aktiv. ID: " + robotId);
		}
	}
	
	
	/**
	 * Beendet alle vorhandenen Roboter
	 */
	public void removeAllRobots() {
		Set<String> temp = new HashSet<String>(management.keySet());
		
		for(String key : temp) {
			removeRobot(key);
		} 
	}
	
	/**
	 * Gibt den Thread zum angegebenen Roboter zurück.
	 * <b>!!!Thread nicht manuell anhalten oder connection Beenden!!!</b>
	 * @param robotId Die ID des Roboters
	 * @return Den Thread zu dem Roboter
	 */
	public VirtualRobot getThreadToRobot(String robotId) {
		return management.get(robotId);
	}

	
	/**
	 * Gibt die Anzahl der im Manager hinterlegten Roboter zurück.
	 * @return Die Anzahl.
	 */
	public int getElementCount() {
		return management.size();
	}
}
