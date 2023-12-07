package prodcons.v1;

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
		while (true) {
			try { 
				System.out.println("exec du thread consummer de id :"+ Thread.currentThread().getId());
				msg = pcbuffer.get();  
				Thread.sleep(this.consTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		}
		
} 