
import java.rmi.server.*;
import java.rmi.RemoteException;

public class ServerImpl extends UnicastRemoteObject implements Server {
	private String namelist[];
	private static final int MAX = 8;

	public ServerImpl() throws RemoteException {
		namelist = new String[MAX];
		namelist[0] = "Lennart Monir";
		namelist[1] = "Marvin Hoff";
		namelist[2] = "Lauryn Monir";
		namelist[3] = "Gunda Monir";
		namelist[4] = "Helga Hoffhenke";
		namelist[5] = "Line Hoffhenke";
		namelist[6] = "Henrik Diestel";
		namelist[7] = "Jannis Groth";
	}

	public String getValue(String s) throws NoNameFoundException {
		String[] names = s.split(";");
		int l = names.length;
		String name = "";
		for (int i = 0; i < l; i++) {
			name = name + findName(names[i]);
		}
		return name;
	}

	private String findName(String s) throws NoNameFoundException {
		String name = "";

		for (int j = 0; j < MAX; j++) {			
				if (namelist[j].contains(s)) {
					name = name + namelist[j] + " " + "\n";
				}			
		}
		if (name.equals("")) {
			throw new NoNameFoundException("Der Name " + s + " ist nicht vorhanden!");
		}
		return name;
	}
}