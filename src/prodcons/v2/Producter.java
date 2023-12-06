package prodcons.v2;

import java.util.Random;

public class Producter extends Thread{
	ProdConsBuffer pcbuffer;
	String msg; 

	private int nbmProd;//nombre de message que ce thread va produire
	private int ProdTime; //la durée moyenne pour produire un message
	//Chaque Producteur devra produire un nombre aléatoire de messages, compris entre minProd
	//et maxProd. 

	//minProd;nombre min de msg que ce thread va produire
    //maxProd;nombre max de msg que ce thread va produire 
	public Producter(int minProd, int maxProd,int ProdTime, ProdConsBuffer pcbuffer, String msg) {
  		this.pcbuffer = pcbuffer;
		this.msg = msg; 
		this.ProdTime=ProdTime;
		Random rand = new Random(); 
		nbmProd=rand.nextInt(maxProd-minProd)+minProd;
		this.setDaemon(true);
		start();
	}
	
	public void run() {  
		Message m; 

		for (int i = 0; i < nbmProd; i++) {

			//on envoie un nouveau message dans le buffer 
			m = new Message(this.msg + Thread.currentThread().getId());
			try {
				pcbuffer.put(m);
				Thread.sleep(ProdTime); 
			} catch (InterruptedException e) {
			e.printStackTrace();
		   } 
		}
	}
}  