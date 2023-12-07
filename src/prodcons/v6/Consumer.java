package prodcons.v6;

public class Consumer extends Thread{
	public static int nbrTraite=0;
    ProdConsBuffer pcbuffer;
	Message msg;  
	Message [] m;
	int consTime;
	int k;
	public Consumer(int consTime,ProdConsBuffer pcbuffer,int k) {
		this.pcbuffer = pcbuffer; 
		this.consTime=consTime; 
		this.setDaemon(true);
		this.k=k;
		start();
	}
	public synchronized void incrnbrTraite(){ 
		nbrTraite++; 

	}
	public void run() { 
		while(true){
		try { 
			if(k==1){
			msg = pcbuffer.get();  
			incrnbrTraite();
			Thread.sleep(this.consTime);} 
			else if(k>1){ 
			m=pcbuffer.get(k);
			Thread.sleep(this.consTime);} 
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}}
	}
} 