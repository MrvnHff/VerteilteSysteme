import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
	
	private Client() {}

	public static void main(String args[]) {
		Server server;
		
		int MAX = 10000;
		int count = 0;
		String namen = "";
		String eingabe = "";
		
		System.out.println("Geben Sie bis zu zehn Nachnamen ein oder brechen Sie mit \"!q\" ab.");
		Scanner scan = new Scanner(System.in);
		
		while(count < MAX && !eingabe.equals("!q")) {
			eingabe = scan.nextLine();
			if (!eingabe.equals("!q") && !eingabe.equals("")){
				namen = namen + eingabe + ";";
				count++;
			}
		}		
		scan.close();		
		
		try {
			Registry registry = LocateRegistry.getRegistry("0.0.0.0", 42424);
			server = (Server) registry.lookup("I_bims");
			System.out.println(server.getValue(namen));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
