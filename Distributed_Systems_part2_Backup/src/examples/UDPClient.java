package examples;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class UDPClient extends Thread {
	/*
	static String bezeichung ="Zimmer2";
	static int toSend=1000;
	static int factor=10;
	
	static int sended;//=0;
	static String args[];
	static int port=9999;
	*/
	String args[];
	
	public UDPClient(String args[]) {
		this.args = args;
	}
	public UDPClient() {
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
			String bezeichnung;
			int toSend;
			int factor;
			
			int sended=0;
			int port;
		 
		 if(args.length == 4){
		 bezeichnung = args[0];
		 port = Integer.parseInt(args[1]);
		 toSend = Integer.parseInt(args[2]);
		 factor = Integer.parseInt(args[3]);
		 }
		 else{
			 bezeichnung="Default";
			 port=9999;
			 toSend=1000;
			 factor=10;
			 
		 }
		 
	      DatagramSocket clientSocket = new DatagramSocket();
	      InetAddress IPAddress = InetAddress.getByName("localhost");
	      
	      byte[] sendData = new byte[1024];
	      
	      for(int i=0 ; i < toSend ; i++){
	    	  Random rand= new Random();
		      String sentence =bezeichnung+" "+rand.nextInt(100)+" "+rand.nextInt(100);
		      sendData = sentence.getBytes();
		      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		      clientSocket.send(sendPacket);
		      sended++;
		      System.out.println("Pakete gesendet: "+sended);
		      Thread t= new Thread();
		      t.sleep(1000/factor);
	      }

	      clientSocket.close();
	   } 
	/* 
	 public static String generateValues(){
		  Random rand= new Random();
		  String val=bezeichung+" "+rand.nextInt(100)+" "+rand.nextInt(100); 
		  return val;
	  }
	  */

}
