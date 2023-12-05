package prodcons.v1;

public class Consumer extends Thread{
	ProdConsBuffer pcbuffer;
	Message msg;

	public Consumer(ProdConsBuffer pcbuffer) {
		this.pcbuffer = pcbuffer;
		start();
	}
	
	public void run() {
		try {
			msg = pcbuffer.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}