package prodcons.v2;

public class ProdConsBuffer implements IProdConsBuffer{

	//liste des messages
	private Message buffer[];  
	//nombre de cases vide
	private int free; 
	//quoue et tete de la fifo
	private int in,out; 
	//nombre totale des messages rajouté depuis la creation du buffer
	private int totalMessages;
  
    /*
  * Opération | Pre-action  | Garde                 | Post-action  
  * 
  * put(m) 	  |				| free>0		        | free-- et add message
  * get 	  |		        |bufferSz -free >0		| free++ et retire return msg
  *
 */
	

	public ProdConsBuffer(int bufferSz) { 
		this.buffer = new Message[bufferSz];
		this.free=bufferSz; 
		this.in=0; 
		this.out=0; 
		totalMessages=0;
}

	
	public synchronized void put(Message msg) throws InterruptedException {
		System.out.println("thread " + Thread.currentThread().getId() + " est rentré dans put, "  +" place libre :"+free);
		
		while (free<=0) {
			wait();
		} 
		System.out.println("un message va etre rajouté par le thread: " + Thread.currentThread().getId());

		buffer[in] = msg;
		in = (in + 1) % buffer.length;
		free--; 
		totalMessages++; 
		System.out.println("un message  a ete rajouté par le thread: " + Thread.currentThread().getId());
 

		notifyAll();		
	}

	
	public synchronized Message get() throws InterruptedException {
		System.out.println("thread " + Thread.currentThread().getId() + " est rentré dans get,"+" place libre :"+free);
 
		
		while ( free >= buffer.length) {
			wait();
		} 
		System.out.println("un message va etre enlevé  par le thread : " + Thread.currentThread().getId());

		Message msg = buffer[out];
		out = (out + 1) % buffer.length;
		free++; 
		System.out.println("un message a été enlevé par le thread: " + Thread.currentThread().getId());

		notifyAll();
		return msg;
	}


	public int nmsg() {
		return buffer.length-free;
	}

	public int totmsg() {
		return totalMessages;
	} 
}