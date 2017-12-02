import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Server server = new Server();
		server.start();

		String eingabe = "";
		Scanner scan = new Scanner(System.in);

		while (!eingabe.equals("stop")) {
			eingabe = scan.nextLine();
		}
		System.out.println("Versuche Server zu stopen");
		server.setStop(true);
		while (server.isAlive()) {}
		
		System.out.println("Server geschlossen!");
		scan.close();
	}
}
