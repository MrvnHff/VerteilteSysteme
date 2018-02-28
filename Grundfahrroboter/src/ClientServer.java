
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import client.RoboServerInterface;

public class ClientServer {
	
	private ClientServer() {}

	public static void main(String args[]) {
		RoboServerInterface robo;		
		
		try {
			Registry registry = LocateRegistry.getRegistry("192.168.178.21", 55555);
			robo = (RoboServerInterface) registry.lookup("Robo4");
			robo.turnLeft();
			robo.turnLeft();
			robo.turnLeft();
			robo.turnLeft();
		} catch (Exception e) {
			System.out.println(e);
		}
	}	
}
