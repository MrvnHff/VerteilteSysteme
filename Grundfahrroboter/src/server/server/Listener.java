package server.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Der Listener bietet die Moeglichkeit Fahrzeugen, die sich im Netzwerk befinden, einen eigenen Worker anzufragen.
 * Ist die maximale Anzahl an Workern noch nicht erreicht, wird ein neuer Worker gestartet und dieser tritt dann 
 * mit dem Fahrzeug in Verbindung.
 * 
 */
public class Listener extends Thread implements ListenerInterface{
	private String ip;
	private int port;
	
	private Server server;
	private Registry registry;
	private Registry returnOfCreateRegistry;
	
	/**
	 * Startmethode fuer den Listener
	 * @param server Referenz auf den Server, fuer den der Listener gestartet wurde
	 * @param port Port, an dem der Listener horchen soll
	 */
	public Listener(Server server, int port) {
		this.server = server;
		InetAddress ipAddr;
		try {
			ipAddr = InetAddress.getLocalHost();
			ip = ipAddr.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.port = port;
		start();
	}
	
	
	/**
	 * Ruft die registerListener Methode auf.
	 */
	public void run(){
		registerListener();
	}

	//JavaDoc Kommentar im Interface
	// RemoteReachable
	@Override
	public synchronized void requestNewWorker(String vehicleId, String vehicleIp, int vehiclePort) throws RemoteException {
		server.addServerTextMessage("Fahrzeug " + vehicleId + " hat sich unter der IP " + vehicleIp + " gemeldet!");
		System.out.println("Listener: Fahrzeug " + vehicleId + " hat sich unter der IP " + vehicleIp + " gemeldet!");
		
		try {
			server.addWorker(vehicleId, vehicleIp, vehiclePort);
		} catch (Exception e) {
			server.addServerTextMessage("Fehler bei addWorker()" + e);
			System.err.println("Listener: Fehler bei addWorker()" + e );
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Startet die Netzwerkverbindung.
	 */
	private synchronized void registerListener() {
		try {
			//Starte lokalen Listener Server am angegebenen Port
			ListenerInterface stub = (ListenerInterface) UnicastRemoteObject.exportObject(this, 0);
			returnOfCreateRegistry = LocateRegistry.createRegistry(port);
	        registry = LocateRegistry.getRegistry(port);
	        registry.bind("Listener", stub);
			System.out.println("Listener: Listener bereit! IP: " + ip + " Port: " + port);
			server.addServerTextMessage("Listener gestartet. IP: " + ip + " Port: " + port);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	/**
	 * Beendet den Listener und gibt den Port an der Netzwerkschnittstelle wieder frei.
	 */
	public void stopListener() {
		try {					// Binding zwischen Listener und Remote-Object-Stub aufheben
			registry.unbind("Listener");
		} catch (RemoteException | NotBoundException e) {
			System.err.println("Listener: Fehler beim unbind:");
			e.printStackTrace();
		}
		
		try {					// Den Port wieder freigeben
			UnicastRemoteObject.unexportObject(returnOfCreateRegistry, true);
		} catch (NoSuchObjectException e) {
			System.err.println("Listener: Fehler beim unexport.");
			e.printStackTrace();
		}
		
		this.server = null; 	// Dereferenzieren
		
		System.out.println("Listener: Listener beendet.");
	}
	
	
	/**
	 * Gibt den Port zurueck, an dem der Listener aktiv ist.
	 * @return
	 */
	public int getPort() {
		return this.port;
	}
	
	
	/**
	 * Gibt die IP-Adresse zurueck, auf der der Listener laeuft.
	 * @return
	 */
	public String getIpAddress() {
		return this.ip;
	}
}

