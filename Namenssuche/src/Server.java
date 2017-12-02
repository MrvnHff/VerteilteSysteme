
import java.rmi.*;

public interface Server extends Remote {
	String getValue(String s) throws RemoteException, NoNameFoundException;
}
