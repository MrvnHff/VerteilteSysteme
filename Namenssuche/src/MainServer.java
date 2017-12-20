import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class MainServer implements Server {

	public MainServer() {}
	
	@Override
	public String getValue(String s) throws RemoteException, NoNameFoundException {
		Namelist list = new Namelist();
		return list.getName(s);		
	}
	
	public static void main(String args[]) {
        
        try {
            Server obj = new MainServer();
            Server stub = (Server) UnicastRemoteObject.exportObject(obj, 0);
            //System.out.println(obj.toString());
            LocateRegistry.createRegistry(54321);
            Registry registry = LocateRegistry.getRegistry(54321);
            registry.bind("I_bims", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
