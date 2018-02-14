
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import client.RoboServerInterface;

public class ClientServer {
	
	private ClientServer() {}

	public static void main(String args[]) {
		RoboServerInterface robo;		
		
		try {
			Registry registry = LocateRegistry.getRegistry("192.168.178.26", 55555);
			robo = (RoboServerInterface) registry.lookup("Robo");
			
			for (int i = 0; i < 2; i++) {
				robo.driveCm(5, 20);
				//robo.turnRight();
				//robo.turnLeft();
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}	
}
