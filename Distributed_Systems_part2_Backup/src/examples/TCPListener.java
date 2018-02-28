package examples;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPListener extends Thread{
	
	//
	private static final String OUTPUTTEIL1 = "<html><head><title>Example</title></head><body><p>";
    private static final String OUTPUTTEIL2 = "</p></body></html>";
    private static final String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n" +
    											 "Content-Type: text/html\r\n" + 
    											 "Content-Length: ";
    private static final String OUTPUT_END_OF_HEADERS = "\r\n\r\n";
    
    private int TCPPort;
    private WohnungsServer server;
	
    public TCPListener(int port){
    	TCPPort=port+1;
    }
    
	public void run(){
		while(true){
			try{
				
				ServerSocket listenSocket = new ServerSocket(TCPPort);
				String line;
				DataOutputStream toClient;
				BufferedReader fromClient;
											
				//TCP
		    	Socket client = listenSocket.accept();
		    	toClient = new DataOutputStream (client.getOutputStream());
		    	fromClient = new BufferedReader              // Datastream FROM Client
		                (new InputStreamReader(client.getInputStream()));
		    	line = fromClient.readLine();
		        //Baut AntwortMessage        
			    String message= WohnungsServer.BuildMessage();
			    System.out.println("Message: "+message);
			    String capitalizedSentence = 
			    		OUTPUT_HEADERS + (OUTPUTTEIL1.length()+ message.length()+OUTPUTTEIL2.length()) + 
			    		OUTPUT_END_OF_HEADERS + OUTPUTTEIL1 + message +OUTPUTTEIL2;
			    //Sendet Antwort
			    toClient.writeBytes(capitalizedSentence);
		     
			}catch(Exception e){
				System.out.println("Exception bei TCPListener: "+ e.getMessage());
			}
		}
	}
}
