package examples;

import java.net.URL;
import java.util.ArrayList;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class HausverwaltungsServer extends Thread {
	public static ArrayList<String[]> Sensoren =new ArrayList<String[]>();
	public static ArrayList<ArrayList<String[]>> clientsSensoren = new ArrayList<ArrayList<String[]>>();
	private static Object[] params;
	private static ArrayList<XmlRpcClient> clients = new ArrayList<XmlRpcClient>();
	private static int send=0;
	private String [] args;
	
	
	public HausverwaltungsServer(String[] args){
		this.args=args;
	}
	public HausverwaltungsServer(){
	}
	
	public static void print(){
		
		String mes;
		Integer maxTemp;
		Integer minTemp;
		Integer gesStrom;
		
		for(int k =0;k < clientsSensoren.size(); k++){
		
			mes = "";
			maxTemp = Integer.MIN_VALUE;
			minTemp = Integer.MAX_VALUE;
			gesStrom = 0;
					
			System.out.println("Wohnung "+k+" :");
			for(int i=0;i< clientsSensoren.get(k).size();i++){
				if(Integer.parseInt(clientsSensoren.get(k).get(i)[1]) > maxTemp){
					maxTemp=Integer.parseInt(clientsSensoren.get(k).get(i)[1]);
				}
				if(Integer.parseInt(clientsSensoren.get(k).get(i)[1]) < minTemp){
					minTemp=Integer.parseInt(clientsSensoren.get(k).get(i)[1]);
				}
				
				gesStrom += Integer.parseInt(clientsSensoren.get(k).get(i)[2]);				
				 mes+="Zimmer: "+clientsSensoren.get(k).get(i)[0]+"	Temperatur:"+clientsSensoren.get(k).get(i)[1]+"	Stromverbrauch:"+clientsSensoren.get(k).get(i)[2]+"\n";
			}
			
			System.out.println(mes);
			System.out.println("Anzahl Zimmer: "+clientsSensoren.get(k).size());
			System.out.println("MaximalTemperatur: "+maxTemp);
			System.out.println("MinimalTemperatur: "+minTemp);
			System.out.println("Gesammtstromverbrauch: "+gesStrom +"\n\n");
		}
		
		System.out.println("******************************************\n\n");
		
	}
	
	public static void update(int indexCS,XmlRpcClient client){
		System.out.println("\n**************Hausverwaltungsserver****************");
		send++;
		System.out.println("Updates gesendet:"+send);
		 try{
		    	
		    	
		    	Object[] param = new Object[]{new Integer(0)};
		    	
		    	int size = (Integer) client.execute("ServerObj.getSize",param);
		    	for(int i = 0;i < size;i++){
		    		String [] values = new String[3];
		    		for(int k = 0;k < 3;k++){
		    			params = new Object[]{new Integer(i), new Integer(k)};
		    			values[k]= (String) client.execute("ServerObj.getValues",params);
		    			values[k]= values[k].trim();
		    		}
		    		if(((0 < clientsSensoren.size() )&& (clientsSensoren.size()) > indexCS))
		    		{
			    		if((0 < clientsSensoren.get(indexCS).size() )&& (clientsSensoren.get(indexCS).size() > i )){
			    			clientsSensoren.get(indexCS).get(i)[1]=values[1];
			    			clientsSensoren.get(indexCS).get(i)[2]=values[2];
			    		}
			    		else{
			        		clientsSensoren.get(indexCS).add(values);
			        	}
		    		}else{
		    			ArrayList<String[]> Sensoren =new ArrayList<String[]>();
		    			Sensoren.add(values);
		    			clientsSensoren.add(Sensoren);
		    		}
		    	}
		    		   	
		    }catch (Exception e){
		    	System.out.print(e);
		    }
	}
	public static void addClient(String port){
		System.out.println(port);
		try{
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
	    config.setServerURL(new URL("http://127.0.0.1:"+port+"/xmlrpc"));
	    
	    XmlRpcClient client = new XmlRpcClient();
	    client.setConfig(config);
	    clients.add(client);
	    
		}catch(Exception e){
			System.out.println(e);
		}
	    
	}
	
	  public static void main(String[] args){
		 try{
			  int faktor = Integer.parseInt(args[0]);
			  int tosend = Integer.parseInt(args[1]);
			  //int faktor = 30;
			  //int tosend = 100;
			  
			 /*
			  	String port1 ="8080";
			  	String port2 ="8081";
			     Clients registieren.
			  		addClient(port1);
			  		addClient(port2);
			  */
			  //String port1 ="8080";
			  //addClient(port1);
			  	for(int i=2;i<args.length;i++){
			  		addClient(args[i]);
			  	}
	
			    while(true){
			    	Thread t = new Thread();
			    	for(int i=0; i < clients.size();i++){
			    		update(i,clients.get(i));
			    	}			    
				    print();
				    t.sleep(60000/faktor);
				    if(send == tosend){
				    	break;
				    }
			    }
		 	}catch(Exception e){
			  
		 	}
		    
	  }
	  
	  public void run(){
		  main(args);
	  }

}
