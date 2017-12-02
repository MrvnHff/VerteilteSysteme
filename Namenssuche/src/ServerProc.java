
import java.rmi.*;

class ServerProc
{
    public static void main (String args [])
    {
        if(System.getSecurityManager() == null)
        {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            ServerImpl obj = new ServerImpl();
            Naming.rebind("rmi://localhost:1234/Server",obj);
        }catch (Exception e){
            System.out.println(e);
        }
    
    }
    
}
