package examples;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class WohnungsServer extends Thread{
	  
    //
    static ArrayList<String[]> Sensoren = new ArrayList<String[]>();
	static int recieved=0;
	static int recievedHV=0;
	String args[];
	
	public WohnungsServer(){
		
	}
	
	public WohnungsServer(String args[]){
		this.args=args;
	}
	
	public void run(){
		try {
			main(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static void main(String args[]) throws Exception
    {		
		int port = Integer.parseInt(args[0]);
					
		DatagramSocket serverSocket = new DatagramSocket(port);
		byte[] receiveData = new byte[1024];
                
        //-------------------TCPListener wartet auf Browseranfrage------------------------------
		
        new TCPListener(port).start();
        
        //--------------------------------------------------------------------------------------
        
        
        
        
        //--------------------------------WebRTCServer------------------------------------------
        try {
        	int portWebRTC = Integer.parseInt(args[1]);
            WebServer webServer = new WebServer(portWebRTC);

            XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
            PropertyHandlerMapping phm = new PropertyHandlerMapping();

            phm.addHandler( "ServerObj", WohnungsServer.class);
            xmlRpcServer.setHandlerMapping(phm);

           XmlRpcServerConfigImpl serverConfig =
                    (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();

            webServer.start();

            System.out.println("The WohnungsServer has been started..." );

          } catch (Exception exception) {
             System.err.println("JavaServer: " + exception);
          }
        
        //---------------------------------------------------------------------------------------------
        
        
        
        //-------------------------------UDP-Sockets-Listening-----------------------------------------
        
        while(true)
        {
        	      	
        	//UDP  Daten von den Sockets werden angenommen
        	DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	        serverSocket.receive(receivePacket);
	        String sentence = new String( receivePacket.getData());
	        
	        setSensoren(sentence.split(" "));       	
	        recieved++; 
	        System.out.println("\n***************WohnungsServer*************************");
	        System.out.println("RECEIVED: "+ recieved + " Values: " + sentence);
	        System.out.println("Anzahl Sensoren: "+Sensoren.size());
	        System.out.println("Anzahl Updateanforderungen: "+recievedHV);
	        System.out.println("********************************************************\n");
	        
	     }
        //----------------------------------------------------------------------------------------------             
    }
	
	public static String BuildMessage(){
		  String mes="";
		  ArrayList<String[]> allSensors = getSensoren();
		  for(int i=0; i < allSensors.size();i++){
			  mes+="Zimmer:"+allSensors.get(i)[0]+"	Temperatur:"+allSensors.get(i)[1]+"	Stromverbrauch:"+allSensors.get(i)[2]+"<br />";
		  }
		  return mes;
	  }
	public static void setSensoren(String[] values){
    	boolean vorhanden=false;
    	for(int i = 0;i<Sensoren.size();i++){
    		if(Sensoren.get(i)[0].equals(values[0])){
    			Sensoren.get(i)[1]=values[1];
    			Sensoren.get(i)[2]=values[2];
    			vorhanden=true;
    		}
    	}
    	if(!vorhanden){
    		Sensoren.add(values);
    	}
    }
    public static ArrayList<String[]> getSensoren(){
    	return Sensoren;
    }
    public  String getValues(int indexAL, int indexSA){
    	return Sensoren.get(indexAL)[indexSA].trim();
    }   
    public Integer getSize(int x){
    	recievedHV++;
    	System.out.println("Updateanforderung empfangen: " + recievedHV);
    	System.out.println("Anzahl Sensoren "+Sensoren.size());
    	return Sensoren.size();
    }
	
}
