package examples;

import java.util.ArrayList;

public class ExecuteListener {
	
	
	public static void main(String args[]){
		Listener listener = new Listener();
		listener.start();
		ArrayList<String[]> publisher = new ArrayList<String[]>();
		//Thread t= new Thread();
		while(true){
			publisher=listener.getPublisher();
			// System.out.println(listener.getSize());
			 //System.out.println(publisher.size());
			 for(int i=0;i<publisher.size();i++){
				/* try {
					//t.sleep(6000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				 System.out.println(publisher.size());

				 System.out.println("Pub-ID: "+publisher.get(i)[0]);
				 System.out.println("Message: "+publisher.get(i)[1]);
				 System.out.println("*******************************\n\n");
			 }
			 //System.out.println("++++++++++++++++++++\n\n");
		}
	}
	
}
