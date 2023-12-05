package prodcons.v1;

public class Producter extends Thread{
	ProdConsBuffer pcbuffer;
	Message msg;

	public Producter(ProdConsBuffer pcbuffer, Message msg) {
		this.pcbuffer = pcbuffer;
		this.msg = msg;
		start();
	}
	
	public void run() {
		try {
			pcbuffer.put(msg);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}