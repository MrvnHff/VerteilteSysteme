import java.io.*; 
import java.net.*;

import lejos.utility.Delay; 
public class Main3 {

	public static void main(String[] args) {
		for (int i = 0; i<10000; i++) {
		try { 
		      String host = "192.168.178.24"; 
		      Socket meinEchoSocket = new Socket(host,6000); 

		      OutputStream socketoutstr = meinEchoSocket.getOutputStream(); 
		      OutputStreamWriter osr = new OutputStreamWriter( socketoutstr ); 
		      BufferedWriter bw = new BufferedWriter( osr ); 

		      InputStream socketinstr = meinEchoSocket.getInputStream(); 
		      InputStreamReader isr = new InputStreamReader( socketinstr ); 
		      BufferedReader br = new BufferedReader( isr ); 

		      String anfrage = "Hallo"+i; 
		      String antwort; 

		      bw.write(anfrage); 
		      bw.newLine(); 
		      bw.flush(); 
		      antwort = br.readLine(); 

		      //System.out.println("Host = "+host); 
		      System.out.println("Echo = "+antwort); 
		      
		    //Delay.msDelay(2000);

		      bw.close(); 
		      br.close(); 
		      meinEchoSocket.close(); 
		    } 
		    catch (UnknownHostException uhe) { 
		      System.out.println(uhe); 
		    } 
		    catch (IOException ioe) { 
		      System.out.println(ioe); 
		    } 
		}

	}

}
