package prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer{

	int bufferSz;
	Message buffer[]; 
	int totalMessages;
	//int nfull;
	//int nempty;
	int in = 0;
	int out = 0;
	int cfull; //nombre de case rempli

	public ProdConsBuffer(int bufferSz) {
		this.buffer = new Message[bufferSz];
		this.cfull=0;
}

	
	public synchronized void put(Message msg) throws InterruptedException {
		while (cfull >= buffer.length) {
			wait();
		}
		buffer[in] = msg;
		in = (in + 1) % buffer.length;
		cfull++; 
		totalMessages++;  // Mise à jour du nombre total de messages
		notifyAll();		
	}

	
	public synchronized Message get() throws InterruptedException {
		while ( cfull == 0) {
			wait();
		}
		Message msg = buffer[out];
		out = (out + 1) % buffer.length;
		cfull--;
		notifyAll();
		return msg;
	}


	public int nmsg() {
		return cfull;
	}

	public int totmsg() {
		return totalMessages;
	}
}