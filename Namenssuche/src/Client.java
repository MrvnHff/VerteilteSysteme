import java.rmi.Naming;
import java.util.Scanner;

public class Client {

	public static void main(String args[]) {
		Server server;
		
		int MAX = 10;
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
			server = (Server) Naming.lookup("rmi://0.0.0.0:1234/Server");
			//TestServer server = new TestServer();
			System.out.println(server.getValue(namen));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
