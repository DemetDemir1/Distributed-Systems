package examples;

public class Execute{
	
	
	public static void main(String args[]){
		String portSensoren = args[0];
		String portWebRTC = args[1];
		String packToSend = args[2];
		String factor = args[3];
		
		String argsUDPC[] = new String[4];
		
		//for(int i=0; i < 4 ; i=i+2){
			String argsWS[] = new String[2];
			argsWS[0]=portSensoren;
			argsWS[1]=portWebRTC;
	
			new WohnungsServer(argsWS).start();

			for(int k=0; k<2;k++){
				
				argsUDPC[0]="Zimmer"+k+"Wohnung0";
				argsUDPC[1]=portSensoren;
				argsUDPC[2]=packToSend;
				argsUDPC[3]=factor;
				
				new UDPClient(argsUDPC).start();
				
				Thread t=new Thread();
				try {
					t.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	
		//}
		/*	
		String argsHVS[] = new String[4];
		argsHVS[0]="10";
		argsHVS[1]="100";
		argsHVS[2]="8080";
		argsHVS[3]="8081";
		
		new HausverwaltungsServer(argsHVS).start();
		*/
		
		
		
		
	}

}
