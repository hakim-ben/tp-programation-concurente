package prodcons.v5;

public class ProdConsBuffer implements IProdConsBuffer{

	//liste des messages
	private Message buffer[];  
	//nombre de cases vide
	private int free; 
	//queue et tete de la  file fifo
	private int in,out; 
	//nombre totale des messages rajouté depuis la creation du buffer
	private int totalMessages; 
	//boolean pour voir si un consomateur produit plusieurs message pour gerer la synchro
	private boolean kmsgGet=false;
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
		System.out.println("thread " + Thread.currentThread().getId() + " est rentré dans get, "  +" place libre :"+free);
//on attend tant que le buffer est vide ou un consomateur a kmsg est entrains de consomer
		while ( free >= buffer.length || kmsgGet) {
			wait();
		}
		Message msg = buffer[out];
		out = (out + 1) % buffer.length;
		free++; 
		System.out.println("un seul message enlvé par le thread avec get() : " + Thread.currentThread().getId());

		notifyAll();
		return msg;
	}


	public int nmsg() {
		return buffer.length-free;
	}

	public int totmsg() {
		return totalMessages;
	}


	@Override
	public synchronized Message[] get(int k) throws InterruptedException {
		//while pour mettre en pause les consumers tant qu'un est deja en production
		while (kmsgGet) {
			wait();
		}


		kmsgGet = true; 
		Message[] M= new Message[k]; 
		for(int i=0;i<k;i++){ 
			while (buffer [out] == null) {
			wait();
			}
		M[i] = buffer[out]; 

		out = (out + 1) % buffer.length;
		buffer[out] = null;
		free++; 
		System.err.println("un message enlevé par le thread: " + Thread.currentThread().getId() + " via get " + k);
		}
		
	//on remet le bolean a false car le consomateur a terminé de produire kmsg les autres consomateur peuvent retravailler
	    kmsgGet = false;
		notifyAll();
		return M;
	} 
}