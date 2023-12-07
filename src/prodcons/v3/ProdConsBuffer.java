package prodcons.v3;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer{

	//liste des messages
	private Message buffer[];  
	//quoue et tete de la fifo
	private int in,out; 
	//nombre totale des messages rajouté depuis la creation du buffer
	private int totalMessages;  
	//nombre de message dans le buffer  
	private int nbMessageBuf;

	//pour v3 on rajoute les semaphore de gestion de recup et et d'ajout et sc mutex 
	private Semaphore notFull;
	private Semaphore notEmpty;	
	private Semaphore mutex = new Semaphore(1);

  
    /*
  * Opération | Pre-action  | Garde                 | Post-action  
  * 
  * put(m) 	  |				| free>0		        | free-- et add message
  * get 	  |		        |bufferSz -free >0		| free++ et retire return msg
  *
 */
	

	public ProdConsBuffer(int bufferSz) { 
		this.buffer = new Message[bufferSz];
		this.in=0; 
		this.out=0; 
		totalMessages=0;  
		nbMessageBuf=0;
		this.notFull = new Semaphore(bufferSz);
		this.notEmpty = new Semaphore(0);

}

	
	public void put(Message msg) throws InterruptedException {
 
		this.notFull.acquire();		
		this.mutex.acquire();

		buffer[in] = msg;
		in = (in + 1) % buffer.length; 
		nbMessageBuf++;
		totalMessages++; 
 
	    this.mutex.release();		
	    this.notEmpty.release();
	
	}

	
	public Message get() throws InterruptedException {
 
		this.notEmpty.acquire();		
		this.mutex.acquire();

		Message msg = buffer[out];
		out = (out + 1) % buffer.length;
	 	nbMessageBuf--; 

		this.mutex.release();
		this.notFull.release();

		return msg;
	}


	public int nmsg() { 
	return nbMessageBuf;
	}

	public int totmsg() {
		return totalMessages;
	} 
}