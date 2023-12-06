package prodcons.v2;

public class Consumer extends Thread{
	ProdConsBuffer pcbuffer;
	Message msg; 
	int consTime;

	public Consumer(int consTime,ProdConsBuffer pcbuffer) {
		this.pcbuffer = pcbuffer; 
		this.consTime=consTime;
		start();
	}
	
	public void run() {
		try {
			msg = pcbuffer.get(); 
			Thread.sleep(this.consTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
} 