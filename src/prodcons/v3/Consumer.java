package prodcons.v3;

public class Consumer extends Thread{
	public static int nbrTraite=0;
    ProdConsBuffer pcbuffer;
	Message msg; 
	int consTime;

	public Consumer(int consTime,ProdConsBuffer pcbuffer) {
		this.pcbuffer = pcbuffer; 
		this.consTime=consTime; 
		this.setDaemon(true);

		start();
	}
	public synchronized void incrnbrTraite(){ 
		nbrTraite++; 

	}
	public void run() {
		try {
			msg = pcbuffer.get();  
			incrnbrTraite();
			Thread.sleep(this.consTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
} 