package test;

import java.rmi.RemoteException;

/**
 * Startet einen Manager, der Virtuelle Roboter verwalten kann. Dieser Prozess bleibt bestehen,
 * damit die Kindprozesse weiterlaufen können.
 * @author Janek Dahl
 *
 */
public class StartAutomatisation {

	/**
	 * Main Methode, die als Einstiegspunkt dient. Beim Starten können Parameter mit übergeben werden,
	 * um das parametrierte Anlegen von Robotern zu ermöglichen
	 * @param args Startparameter: 
	 * ServerIP ServerPort RoboterPortStart WaitTime RobotIdPrefix Anzahl
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws RemoteException, InterruptedException {
		AutomatisationManager am = new AutomatisationManager();
		
		if(args.length == 6) {	//Parametrierter Start
			try{
				String serverIp = args[0];
				int serverPort = Integer.parseInt(args[1]);
				int robotPortStart = Integer.parseInt(args[2]);
				int waitTime = Integer.parseInt(args[3]);
				String robotId = args[4];
				int count = Integer.parseInt(args[5]);
				am.addMultitpleRobots(serverIp, serverPort, robotPortStart, waitTime, robotId, count);
			} catch (Exception e){
				System.out.println("Fehlerhafte Eingabe. Keinen Roboter gestartet.");
				System.out.println("Eingabeparameter: ServerIp Serverport Roboterport WaitTime RobotID Count");
				System.out.println("0 <= WaitTime <= 10 (Ausführungsdauer einer Fahroperation in Sekunden. Zufallsdauer falls ungültig.");
			}
		} else { // Unparametrierter Start
			
			// Hier Methoden für Klassenstart ohne Parameter einfügen.
			// Methoden und Parameterbeschreibungen sind in der Klasse AutomatisationManager() zu finden.
			am.addMultitpleRobots("127.0.0.1", 55555, 55554, 1, "TestBot", 2);
			

		}
		
		// Roboter entfernen, während der Prozess aktiv ist
		/*am.addRobot("127.0.0.1", 55555, 55554, 1, "TestBot_1");
		System.out.println(am.getThreadToRobot("TestBot_1").toString());
		Thread.sleep(1000);
		am.removeRobot("TestBot_1");
		*/
		
		/* Versetztes startet und beenden (Beendet Roboter nur den eigenen Prozess)
		am.addRobot("127.0.0.1", 55555, 55554, 1, "TestBot_1");
		System.out.println(am.getThreadToRobot("TestBot_1").toString());
		Thread.sleep(8000);		
		am.addRobot("127.0.0.1", 55555, 55553, 1, "TestBot_2");
		System.out.println(am.getThreadToRobot("TestBot_2").toString());
		Thread.sleep(8000);
		am.addRobot("127.0.0.1", 55555, 55552, 1, "TestBot_3");
		System.out.println(am.getThreadToRobot("TestBot_3").toString());
		*/
		
		
		//Objekt am Leben halten
		Thread.sleep(Integer.MAX_VALUE);

		
		//Cleanup der Roboter vor Ende
		am.removeAllRobots();
		
		Thread.sleep(3 * 1000);

		System.out.println("StartAutomatisation: Ende des Objekts.");
	}

}
