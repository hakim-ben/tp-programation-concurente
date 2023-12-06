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
		while (free<=0) {
			wait();
		}
		buffer[in] = msg;
		in = (in + 1) % buffer.length;
		free--; 
		totalMessages++;
		notifyAll();		
	}

	
	public synchronized Message get() throws InterruptedException {
		while ( free >= buffer.length) {
			wait();
		}
		Message msg = buffer[out];
		out = (out + 1) % buffer.length;
		free++;
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