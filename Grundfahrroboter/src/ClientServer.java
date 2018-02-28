
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import client.RoboServerInterface;

public class ClientServer {
	
	private ClientServer() {}

	public static void main(String args[]) {
		RoboServerInterface robo;		
		
		try {
			Registry registry = LocateRegistry.getRegistry("192.168.178.20", 55555);
			robo = (RoboServerInterface) registry.lookup("Robert");
			
			robo.driveCm(20, 200);
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}	
}
