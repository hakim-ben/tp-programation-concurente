package prodcons.v6;

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
	private boolean NMsgPut=false;
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

	
	public synchronized void put(Message msg ,int n) throws InterruptedException { 
		System.out.println("thread " + Thread.currentThread().getId() + " est rentré dans put, "  +" place libre :"+free);
//si un autre producteur n'a pas terminé de produire ses n msg
		while (free<=0 || NMsgPut) { 
			
			wait();
		}  
		NMsgPut=true; 
for(int i=0;i<n;i++){  
	//on attend jusqu'a une place se libere avant chaque ajout
	while (free <= 0) {
		wait();
	}
		buffer[in] = msg;
		msg.addcopy();
		in = (in + 1) % buffer.length; 
		free--;
		totalMessages++; 
		System.err.println("le msg d'id"+ msg.hashCode() +"a été rajouté par le thread: " + Thread.currentThread().getId() );

		notifyAll();}	 
		// on redonne la permission au autre put de marcher
			NMsgPut = false;
			notifyAll();
			
		//on arrete le producteur tant que pas tout les msg qu'il a produit n'ont pas etait consomé
			while (msg.getOccurInBuffer() > 0) { 
			System.err.println("le thread d'id: " + Thread.currentThread().getId() +"est bloqué car il reste" + msg.getOccurInBuffer()+ " examplaire du msg qu'il a crée"  );
				wait(); 
			}
			notifyAll();
	
	}

	
	public synchronized Message get() throws InterruptedException { 
		System.out.println("thread " + Thread.currentThread().getId() + " est rentré dans get, "  +" place libre :"+free);
//on attend tant que le buffer est vide ou un consomateur a kmsg est entrains de consomer
		while ( free >= buffer.length || kmsgGet) {
			wait();
		}
		Message msg = buffer[out]; 
		msg.removecopy();
		out = (out + 1) % buffer.length;
		free++; 
		System.err.println("un seul message a été enlevé par le thread avec get() : " + Thread.currentThread().getId());
	    //on attend que le msg soit completement enlvé pour poursuivre 
		while (msg.getOccurInBuffer() > 0) {
				wait();
			}

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
 //il dois attendre quand soit y'a pas de msg ou bien il a consomé un nmsg  ou bien un 
 //le premier while je le met juste pour tester qu'il y'a pas un autre kget 
		while (kmsgGet) {
			wait();
		}

		kmsgGet = true;
		Message[] M= new Message[k];
		for(int i=0;i<k;i++){  
			while(buffer[out]==null){ 
				wait();
			}
		M[i] = buffer[out]; 
		out = (out + 1) % buffer.length; 
		buffer[out]=null; 
		M[i].removecopy();
		free++;  
		while(M[i].getOccurInBuffer()>0){  
			kmsgGet = false;
				wait(); 
			kmsgGet = true;
			}
		System.out.println("un message a été enlevé par le thread a get("+ k + ") : " + Thread.currentThread().getId());
		notifyAll();
	}

	//on remet le bolean a false car le consomateur a terminé de produire kmsg les autres consomateur peuvent retravailler
	    kmsgGet = false;
		notifyAll();
		return M;
	} 
}