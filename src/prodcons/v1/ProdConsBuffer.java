package prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer{

	int bufferSz;
	Message buffer[];
	int nfull;
	int nempty;
	int in = 0;
	int out = 0;
	
	public ProdConsBuffer(int bufferSz) {
		this.bufferSz = bufferSz;
		this.buffer = new Message[bufferSz];
		this.nfull = bufferSz;
		this.nempty = 0;
	}

	
	public synchronized void put(Message msg) throws InterruptedException {
		while (nfull == 0) {
			wait();
		}
		buffer[in] = msg;
		in = (in + 1) % bufferSz;
		nempty++;
		nfull--;
		notifyAll();		
	}

	
	public synchronized Message get() throws InterruptedException {
		while (nempty == 0) {
			wait();
		}
		Message msg = buffer[out];
		out = (out + 1) % bufferSz;
		nempty--;
		nfull++;
		notifyAll();
		return msg;
	}

	
	public int nmsg() {
		return nempty;
	}

	
	public int totmsg() {
		return 0;
	}
}