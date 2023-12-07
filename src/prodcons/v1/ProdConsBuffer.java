package prodcons.v1;

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
		System.err.println("thread est rendtré dans put " + Thread.currentThread().getId() +"place libre :"+free);

		while (free<=0) {
			wait();
		} 
		System.err.println("un message va etre rajouté par le thread: " + Thread.currentThread().getId());
		buffer[in] = msg;
		in = (in + 1) % buffer.length;
		free--; 
		totalMessages++; 
		System.err.println("un message  a ete rajouté par le thread: " + Thread.currentThread().getId());

		notifyAll();		
	}

	
	public synchronized Message get() throws InterruptedException { 
		System.err.println("thread est rendtré dans get " + Thread.currentThread().getId() +"place libre :"+free);

		while ( free >= buffer.length) {
			wait();
		} 
		System.err.println("un message va etre enlvé  par le thread : " + Thread.currentThread().getId());
		Message msg = buffer[out];
		out = (out + 1) % buffer.length;
		free++; 
		System.err.println("un message enlvé par le thread: " + Thread.currentThread().getId());

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