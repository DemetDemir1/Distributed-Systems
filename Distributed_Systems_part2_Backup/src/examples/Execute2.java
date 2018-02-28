package examples;

public class Execute2{
	
	
	public static void main(String args[]){
		String argsUDPC []= new String [4];
		
		String argsWS[] = new String[2];
		argsWS[0]="10001";
		argsWS[1]="8081";
	
		new WohnungsServer(argsWS).start();
	
		for(int k=0; k<2;k++){
			
			argsUDPC[0]="Zimmer"+k+"Wohnung1";
			argsUDPC[1]="10001";
			argsUDPC[2]="100";
			argsUDPC[3]="1";
			
			new UDPClient(argsUDPC).start();
			
			Thread t=new Thread();
			try {
				t.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
